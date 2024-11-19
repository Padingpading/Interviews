package com.padingpading.spring.cacheable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * cacheAble
 */
@RestController
@RequestMapping("/cacheAble")
@EnableCaching
public class CacheAbleController {

    @Autowired
    private CacheAbleService cacheAbleService;
    
    @PostMapping("/add")
    @ResponseBody
    public Boolean add(CacheAbleDo req){
        return cacheAbleService.add(req);
    }
    
    
    @PostMapping("/update")
    @ResponseBody
    public CacheAbleDo update(@RequestBody CacheAbleDo req){
        return cacheAbleService.update(req);
    }
    
    @PostMapping("/select")
    @ResponseBody
    public CacheAbleDo select(@RequestParam("id")Integer id){
        return cacheAbleService.select(id);
    }
    
    @PostMapping("/selectByParam")
    @ResponseBody
    public List<CacheAbleDo> selectByParam(@RequestBody CacheAbleDo req){
        return cacheAbleService.selectByParam(req);
    }
    
    @PostMapping("/delete")
    @ResponseBody
    public Boolean delete(@RequestBody CacheAbleDo req){
        return cacheAbleService.delete(req);
    }
}
