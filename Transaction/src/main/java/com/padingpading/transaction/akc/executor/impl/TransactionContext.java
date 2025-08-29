//package com.padingpading.transaction.akc.executor.impl;
//
//import com.padingpading.transaction.akc.commons.Printable;
//import com.padingpading.transaction.akc.dao.TransactionTaskLogDO;
//import com.padingpading.transaction.akc.enums.TransactionStatusEnum;
//import com.padingpading.transaction.akc.task.ReversibleTask;
//import org.apache.commons.lang3.builder.ToStringBuilder;
//import org.apache.commons.lang3.builder.ToStringStyle;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
///**
// * 事务上下文
// *
// * @author liuhaoyong
// * @Date 2020年11月9日
// *
// */
//public class TransactionContext extends Printable {
//
//    private static final long serialVersionUID = 1L;
//
//    private TransactionStatusEnum transactionStatus = TransactionStatusEnum.COMMIT;
//
//    private List<ReversibleTask> transactionTaskList = new ArrayList<ReversibleTask>();
//
//    private List<TransactionTaskLogDO> transactionLogDOList = new ArrayList<TransactionTaskLogDO>();
//
//    public TransactionStatusEnum getTransactionStatus() {
//        return transactionStatus;
//    }
//
//    public void setTransactionStatus(TransactionStatusEnum transactionStatus) {
//        this.transactionStatus = transactionStatus;
//    }
//
//    public List<ReversibleTask> getTransactionTaskList() {
//        return transactionTaskList;
//    }
//
//    public List<TransactionTaskLogDO> getTransactionLogDOList() {
//        return transactionLogDOList;
//    }
//
//    public void addTransactionTask(ReversibleTask task, TransactionTaskLogDO transactionLog) {
//        transactionTaskList.add(task);
//        transactionLogDOList.add(transactionLog);
//    }
//
//    @Override
//    public String toString() {
//        return ToStringBuilder.reflectionToString(
//                transactionLogDOList.stream().map(TransactionTaskLogDO::getId).collect(Collectors.toList()),
//                ToStringStyle.JSON_STYLE);
//    }
//
//}