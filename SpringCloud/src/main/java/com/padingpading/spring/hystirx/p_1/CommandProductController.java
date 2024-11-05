package com.padingpading.spring.hystirx.p_1;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import rx.Observable;
import rx.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 初次使用。
 */
@Controller
@RequestMapping("/hystrix/test/p1")
public class CommandProductController {
    
    @RequestMapping("testCommand")
    @ResponseBody
    public ProductInfo testCommand(@RequestParam(name = "productId") Long productId){
        CommandProductWorld commandProductWorld  = new CommandProductWorld(productId);
        ProductInfo run = commandProductWorld.run();
        return run;
    }
    
    @RequestMapping("testCommands")
    public List<ProductInfo> testCommand(@RequestParam List<Long> productIds){
        List<ProductInfo> productInfos = new ArrayList<>();
        CommandProductsWorld commandProductsWorld  = new CommandProductsWorld(productIds);
        Observable<ProductInfo> toObservable = commandProductsWorld.toObservable();
        toObservable.subscribe(new Observer<ProductInfo>() {
            @Override
            public void onCompleted() {
                System.out.println("获取完了所有的商品信息。");
            }
    
            @Override
            public void onError(Throwable throwable) {
        
            }
    
            @Override
            public void onNext(ProductInfo productInfo) {
                if(Objects.nonNull(productInfo)){
                    productInfos.add(productInfo);
                }
            }
        });
        return productInfos;
    }
    
}
