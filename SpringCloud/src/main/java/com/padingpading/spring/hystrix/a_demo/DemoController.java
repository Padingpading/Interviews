//package com.padingpading.spring.hystrix.a_demo;
//
//import com.padingpading.spring.hystrix.cache.CommandUsingRequestCache;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@RestController
//@RequestMapping("/demo/hystrix")
//public class DemoController {
//    private final Logger logger = LoggerFactory.getLogger(getClass());
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//    @GetMapping("/open")
//    public ResponseEntity<String> cache() {
//        //相同的id从缓存中获取。
//        List<Integer> ids  = new ArrayList<>();
//        ids.add(1);
//        ids.add(2);
//        ids.add(1);
//        for (Integer id : ids) {
//            CommandUsingRequestCache commandUsingRequestCache = new CommandUsingRequestCache(id);
//            Integer execute = commandUsingRequestCache.execute();
//            System.out.println(execute);
//            //是否从缓存中获取到的结果
//            System.out.println(commandUsingRequestCache.isResponseFromCache());
//        }
////        1
////        false
////        2
////        false
////        1
////        true
//        return ResponseEntity.ok("ok");
//    }
//}
