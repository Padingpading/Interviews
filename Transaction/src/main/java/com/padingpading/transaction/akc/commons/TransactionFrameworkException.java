package com.padingpading.transaction.akc.commons;

import com.padingpading.transaction.akc.enums.TaskExecuteErrorCodeEnum;

/**
 * 事务框架执行过程中的异常定义
 * @author liuhaoyong
 * @Date 2020年11月4日
 *
 */
public class TransactionFrameworkException extends RuntimeException {
    
    /**
     *
     */
    private static final long serialVersionUID = -6378775606942426548L;
    
    // 错误码
    private TaskExecuteErrorCodeEnum errorCode;
    
    // 错误描述
    private String errorDesc;
    
    public TransactionFrameworkException(TaskExecuteErrorCodeEnum errorCode, String errorDesc) {
        super();
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
    }
    
    public TaskExecuteErrorCodeEnum getErrorCode() {
        return errorCode;
    }
    
    public String getErrorDesc() {
        return errorDesc;
    }
    
    
}