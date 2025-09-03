package com.padingpading.spring.hystrix.fallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping("/hystrix/fallback")
public class FallbackController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private RestTemplate restTemplate;
    
    @GetMapping("/open")
    public ResponseEntity<String> cache() {
        //相同的id从缓存中获取。
        CommandFallbackFailure commandFallbackFailure =new CommandFallbackFailure("苹果");
        String execute = commandFallbackFailure.execute();
        return ResponseEntity.ok(execute);
    }
}
