package com.padingpading.transaction.akc.executor;

import com.padingpading.transaction.akc.enums.TransactionStatusEnum;
import com.padingpading.transaction.akc.task.ReversibleTask;
import com.padingpading.transaction.akc.task.TaskExecuteResult;

/**
 * 异常冲正型任务执行器
 * @author liuhaoyong
 * @Date 2020年12月25日
 *
 */
public interface ReversibleTaskExecutor {
    
    /**
     * 执行异常冲正型任务
     *
     * @param <T>
     * @param task
     * @return
     */
    public <T extends TaskExecuteResult> T execute(ReversibleTask<T> task) ;
    
    
    /**
     * 设置事务状态
     * @param status
     */
    public void setTransactionStatus(TransactionStatusEnum status);
    
    
    
    /**
     * 事务终结
     * 执行该方法后，整个线程上下文中的一冲冲正型事务将自动提交或者回滚，根据当前事务状态
     */
    public void doFinally() ;
    
    
}
