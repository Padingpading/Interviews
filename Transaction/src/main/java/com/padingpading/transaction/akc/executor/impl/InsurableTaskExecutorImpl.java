package com.padingpading.transaction.akc.executor.impl;

import com.padingpading.transaction.akc.autoconfig.TransactionTaskProperties;
import com.padingpading.transaction.akc.dao.TransactionTaskLogDO;
import com.padingpading.transaction.akc.enums.TaskExecuteErrorCodeEnum;
import com.padingpading.transaction.akc.enums.TaskExecuteStatusEnum;
import com.padingpading.transaction.akc.enums.TaskRetryStatusEnum;
import com.padingpading.transaction.akc.executor.InsurableTaskExecutor;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.sql.Timestamp;


/**
 * 努力确保型任务执行器实现
 *
 * @author liuhaoyong
 * @Date 2020年11月4日
 *
 */
public class InsurableTaskExecutorImpl implements InsurableTaskExecutor {
    
    private TransactionTaskManager transactionManager;
    private ThreadPoolTaskExecutor acceptExecutor;
    private ThreadPoolTaskExecutor workExecutor;
    private TransactionTaskLogMapper taskMapper;
    private ApplicationEventPublisher eventPublisher;
    private TransactionTaskProperties transactionTaskProperties;
    
    public InsurableTaskExecutorImpl(TransactionTaskManager transactionManager, TransactionTaskLogMapper taskMapper,
            ThreadPoolTaskExecutor workExecutor, ThreadPoolTaskExecutor acceptExecutor,
            ApplicationEventPublisher eventPublisher, TransactionTaskProperties transactionTaskProperties) {
        this.transactionManager = transactionManager;
        this.acceptExecutor = acceptExecutor;
        this.workExecutor = workExecutor;
        this.taskMapper = taskMapper;
        this.eventPublisher = eventPublisher;
        this.transactionTaskProperties = transactionTaskProperties;
    }
    
    @Override
    public <T extends TaskExecuteResult> void execute(InsurableTask<T> task) {
        // 1. 持久化任务
        TransactionTaskLogDO taskDo = transactionManager.persistTask(task);
        
        try {
            //如果任务被包装到本地事务中，则在事务提交后再执行远程任务，否则立即执行
            if (TransactionSynchronizationManager.isActualTransactionActive()) {
                eventPublisher.publishEvent(new TaskPersistEvent(new TaskHolder(taskDo, task)));
            } else {
                accept(task, taskDo);
            }
        } catch (Exception e) {
            Logger.warn("任务执行异常，忽略，系统后面将自动发起重试: taskId={}, taskType={}", task.getTaskId(), task.getTaskType(), e);
        }
    }
    
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    private void listenTransactionCommited(TaskPersistEvent taskPersistEvent) {
        TaskHolder source = (TaskHolder) taskPersistEvent.getSource();
        InsurableTask task = source.getTask();
        TransactionTaskLogDO taskDo = source.getTaskDo();
        
        accept(task, taskDo);
        
    }
    
    private <T extends TaskExecuteResult> void accept(InsurableTask<T> task, TransactionTaskLogDO taskDo) {
        acceptExecutor.execute(RunnableWrapper.of(new Runnable() {
            @Override
            public void run() {
                // 2. 执行任务
                try {
                    doExecute(task, taskDo);
                } catch (Throwable e) {
                    Logger.warn("任务执行异常，忽略，系统后面将自动发起重试: taskId={}, taskType={}", task.getTaskId(), task.getTaskType(),
                            e);
                }
            }
        }));
    }
    
    private <T extends TaskExecuteResult> void doExecute(InsurableTask<T> task, TransactionTaskLogDO taskDo) {
        workExecutor.execute(RunnableWrapper.of(new Runnable() {
            @SuppressWarnings("unchecked")
            @Override
            public void run() {
                T result = null;
                try {
                    // 2.1 调用task执行任务
                    result = task.doExecute();
                } catch (Throwable e) {
                    Logger.error("任务执行异常{}", task, e);
                    result = (T) new TaskExecuteResult();
                    result.setExecuteStatus(TaskExecuteStatusEnum.EXCEPTION);
                    result.setErrorCode(TaskExecuteErrorCodeEnum.SYSTEM_ERROR.name());
                    result.setErrorMessage(StringUtils.left(e.getMessage(), 200));
                }
                
                // 2.2 持久化执行结果
                taskDo.setStatus(result.getExecuteStatus().name());
                taskDo.setResultAdditionalInfo(result.serialize());
                if (!result.getExecuteStatus().isEndForInsurableTask()) {
                    Logger.error("任务执行异常{}", result);
                    taskDo.setErrorCode(result.getErrorCode());
                    taskDo.setErrorMessage(StringUtils.left(result.getErrorMessage(), 200));
                    taskDo.setRetryStatus(TaskRetryStatusEnum.WAIT_RETRY.name());
                    taskDo.setNextExecuteTime(
                            new Timestamp(task.getRetryStrategy().calNextExecuteTime(taskDo.getTimes()).getTime()));
                }
                taskMapper.update(taskDo);
                
                // 2.3 如果任务执行成功执行回调并删除任务
                if (result.getExecuteStatus() == TaskExecuteStatusEnum.SUCCESS) {
                    try {
                        task.callback(result);
                    } catch (Throwable e) {
                        Logger.error("重试成功后回调业务执行异常， result={}, task={}", result, task, e);
                    }
                    if (transactionTaskProperties.isEnableImmediatelyDelete()) {
                        taskMapper.deleteById(taskDo.getId());
                    }
                }
            }
        }));
        
    }
    
    private static final class TaskPersistEvent extends ApplicationEvent {
        private static final long serialVersionUID = 4636423716577502766L;
        
        private TaskPersistEvent(TaskHolder holder) {
            super(holder);
        }
    }
    
    private static final class TaskHolder {
        private TransactionTaskLogDO taskDo;
        private InsurableTask task;
        
        private TaskHolder(TransactionTaskLogDO taskDo, InsurableTask task) {
            super();
            this.taskDo = taskDo;
            this.task = task;
        }
        
        private TransactionTaskLogDO getTaskDo() {
            return taskDo;
        }
        
        private InsurableTask getTask() {
            return task;
        }
    }
    
}
