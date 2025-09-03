package com.padingpading.spring.cacheable;

import java.util.List;


public interface CacheAbleService {
    
    Boolean add(CacheAbleDo t);
    
    CacheAbleDo update(CacheAbleDo t);
    
    Boolean delete(CacheAbleDo t);
    
    CacheAbleDo select(Integer id);
    
    List<CacheAbleDo> selectByParam(CacheAbleDo t);
    
}
