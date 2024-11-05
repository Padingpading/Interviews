package com.padingpading.spring.hystirx.p_1;


import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

/**
 * 初次使用。
 */
public class CommandProductWorld extends HystrixCommand<ProductInfo> {
    
    /**
     * 商品id
     */
    private Long productId;
    
    public CommandProductWorld(Long productId) {
        super(HystrixCommandGroupKey.Factory.asKey("CommandProductGroup"));
        this.productId = productId;
    }
    
    @Override
    protected ProductInfo run() {
        String url = "http://127.0.0.1:8082/getProdcutInfo?prodcutId=" + productId;
        String response = "";
        //调用第三方。
        return JSONObject.parseObject(response,ProductInfo.class);
    }
    
}
