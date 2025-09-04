package com.padingpading.spring.hystrix.collapser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


@RestController
@RequestMapping("/hystrix/collaper")
public class CollaperController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private RestTemplate restTemplate;
    
    @GetMapping("/open")
    public List<ProductInfo> cache() throws ExecutionException, InterruptedException {
        List<Long> productIds = new ArrayList<>();
        productIds.add(1L);
        productIds.add(2L);
        List<Future<ProductInfo>> execute = new ArrayList<>();
        for (Long productId : productIds) {
            CommandCollapser commandCollapser  = new CommandCollapser(productId);
            Future<ProductInfo> queue = commandCollapser.queue();
            execute.add(queue);
        }
        //批量调用
        for (Future<ProductInfo> productInfoFuture : execute) {
            ProductInfo productInfo = productInfoFuture.get();
            System.out.println(productInfo);
        }
        return null;
    }
}
