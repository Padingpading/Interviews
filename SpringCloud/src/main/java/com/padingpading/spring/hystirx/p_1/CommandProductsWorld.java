package com.padingpading.spring.hystirx.p_1;


import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import java.util.List;

/**
 * 初次使用。
 */
public class CommandProductsWorld extends HystrixObservableCommand<ProductInfo> {
    
    /**
     * 商品id
     */
    private List<Long> productIds;
    
    public CommandProductsWorld(List<Long> productIds) {
        super(HystrixCommandGroupKey.Factory.asKey("CommandProductGroup"));
        this.productIds = productIds;
    }
    
    @Override
    protected Observable<ProductInfo> construct() {
        return Observable.create(new Observable.OnSubscribe<ProductInfo>() {
            @Override
            public void call(Subscriber<? super ProductInfo> observer) {
                try {
                    if (!observer.isUnsubscribed()) {
                        for (Long productId : productIds) {
                            String url = "http://127.0.0.1:8082/getProdcutInfo?prodcutId=" + productId;
                            String response = "";
                            //调用第三方。
                            ProductInfo productInfo = JSONObject.parseObject(response, ProductInfo.class);
                            observer.onNext(productInfo);
                        }
                        observer.onCompleted();
                    }
                } catch (Exception e) {
                    observer.onError(e);
                }
            }
        } ).subscribeOn(Schedulers.io());
    }
}
