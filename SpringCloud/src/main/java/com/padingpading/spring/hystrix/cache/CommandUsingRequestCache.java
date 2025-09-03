package com.padingpading.spring.hystrix.cache;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixRequestCache;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategyDefault;

/**
 * 结合咱们的业务背景，我们做了一个批量查询商品数据的接口，在这个里面，我们其实通过HystrixObservableCommand一次性批量查询多个商品id的数据
 * 但是这里有个问题，如果说nginx在本地缓存失效了，重新获取一批缓存，传递过来的productId都没有进行去重，1,1,2,2,5,6,7
 * 那么可能说，商品id出现了重复，如果按照我们之前的业务逻辑，可能就会重复对productId=1的商品查询两次，productId=2的商品查询两次
 * 我们对批量查询商品数据的接口，可以用request cache做一个优化，就是说一次请求，就是一次request context，对相同的商品查询只能执行一次，其余的都走request cache
 */
public class CommandUsingRequestCache extends HystrixCommand<Integer> {
    
    private static final HystrixCommandKey GETTER_KEY = HystrixCommandKey.Factory.asKey("commandKey");
    
    private final int value;
    
    protected CommandUsingRequestCache(int value) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("group"))
                .andCommandKey(GETTER_KEY));
        this.value = value;
    }
    
    @Override
    protected Integer run() {
        return value;
    }
    
    /**
     * 定义缓存key
     */
    @Override
    protected String getCacheKey() {
        return String.valueOf(value);
    }
    
    /**
     * 缓存的清理
     */
    public static void flushCache(int id) {
        HystrixRequestCache.getInstance(GETTER_KEY,
                HystrixConcurrencyStrategyDefault.getInstance()).clear(String.valueOf(id));
    }
}
