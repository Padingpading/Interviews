package com.padingpading.spring.hystrix.cache;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

public class CacheClearCommand extends HystrixCommand<Integer> {
    private final int productId;
    
    public CacheClearCommand(int productId) {
        super(HystrixCommandGroupKey.Factory.asKey("GetSetGet"));
        this.productId = productId;
    }
    @Override
    protected Integer run() {
        //更新商品信息
        //.....
        //清除缓存
        CommandUsingRequestCache.flushCache(productId);
        // no return value
        return productId;
    }
}
