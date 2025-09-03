package com.padingpading.spring.hystrix.collapser;

/**
 * @author libin
 * @description
 * @date 2022/9/24
 */
public class ProductInfo {
    
    private  String id;
    
    public ProductInfo(String id) {
        this.id = id;
    }
    
    public String getId() {
        return id;
    }
}
