package com.padingpading.transaction.akc.executor.impl;

import com.padingpading.transaction.akc.commons.TransactionFrameworkException;
import com.padingpading.transaction.akc.dao.TransactionTaskLogDO;
import com.padingpading.transaction.akc.enums.TaskExecuteErrorCodeEnum;
import com.padingpading.transaction.akc.enums.TaskExecuteStatusEnum;
import com.padingpading.transaction.akc.mapper.TransactionTaskLogMapper;
import com.padingpading.transaction.akc.task.TaskExecuteResult;
import com.padingpading.transaction.akc.task.TransactionTask;
import groovy.util.logging.Slf4j;

@Slf4j
/**
 *
 * @author liuhaoyong
 * @Date 2020年11月10日
 *
 */
public class TransactionTaskManager {
    
    
    public TransactionTaskLogMapper enterpriseTaskMapper;
    
    public TransactionTaskManager(TransactionTaskLogMapper enterpriseTaskMapper) {
        this.enterpriseTaskMapper = enterpriseTaskMapper;
    }
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateTransactionTaskLog(TransactionTaskLogDO taskDo)
    {
        // 3 持久化执行结果
        enterpriseTaskMapper.update(taskDo);
    }
    
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public <T extends TaskExecuteResult> TransactionTaskLogDO persistTaskForNewTransaction(TransactionTask<T> task) {
        return this.persistTask(task);
    }
    
    
    /**
     * @param <T>
     * @param task
     * @return
     */
    public <T extends TaskExecuteResult> TransactionTaskLogDO persistTask(TransactionTask<T> task) {
        TransactionTaskLogDO logDo = new TransactionTaskLogDO();
        logDo.setTaskId(task.getTaskId());
        logDo.setTaskType(task.getTaskType());
        logDo.setTaskClassName(task.getClass().getTypeName());
        logDo.setStatus(TaskExecuteStatusEnum.PROCESSING.name());
        logDo.setTransactionType(task.getTransactionType().name());
        logDo.setTimes(1);
        logDo.setRequestAdditionalInfo(task.serializeAdditionalInfo());
        
        try {
            enterpriseTaskMapper.save(logDo);
        } catch (DuplicateKeyException e) {
            // log.error("任务已存在，请不要重复提交={}", logDo);
            throw new TransactionFrameworkException(TaskExecuteErrorCodeEnum.REPEATED_REQUEST, "任务重复，请勿重复提交");
        } catch (Exception e) {
            //Logger.error("任务持久化失败={}", logDo, e);
            throw new TransactionFrameworkException(TaskExecuteErrorCodeEnum.PERSIST_FAILED,
                    TaskExecuteErrorCodeEnum.PERSIST_FAILED.getDesc());
        }
        return logDo;
    }
    
}
