//package com.padingpading.transaction.akc.task;
//
//import com.padingpading.transaction.akc.enums.TaskRetryStrategyEnum;
//import com.padingpading.transaction.akc.enums.TransactionStatusEnum;
//import com.padingpading.transaction.akc.enums.TransactionTypeEnum;
//
///**
// * 异常冲正型任务
// *
// * @author liuhaoyong
// * @Date 2020年11月9日
// *
// */
//public abstract class ReversibleTask<T extends TaskExecuteResult> implements TransactionTask<T> {
//
//    @Override
//    public String getTaskType() {
//        return this.getClass().getSimpleName();
//    }
//
//    @Override
//    public final TransactionTypeEnum getTransactionType() {
//        return TransactionTypeEnum.REVERSAL;
//    }
//
//
//    @Override
//    public TaskRetryStrategyEnum getRetryStrategy() {
//        return TaskRetryStrategyEnum.INCREASING_INTERVAL;
//    }
//
//    @Override
//    public String serializeAdditionalInfo() {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//
//    /**
//     * 执行异常冲正
//     *
//     * @return
//     */
//    public abstract T doReversal();
//
//
//
//    /**
//     * 查询业务执行状态
//     * 当远程任务执行成功，但长期处于未提交状态，框架会尝试自动恢复，
//     * 此时会回调该方法，查询当前业务是要提交还是回滚
//     * @return
//     */
//    public abstract TransactionStatusEnum queryBizStatus();
//}
