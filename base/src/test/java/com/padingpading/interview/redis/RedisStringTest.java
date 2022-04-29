package com.padingpading.interview.redis;

import com.padingpading.interview.redis.redisbase.basetypes.RedisString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


/**
 * redis
 */
@SpringBootTest
public class RedisStringTest {
    
    @Autowired
    private RedisString redisString;
    
    @Test
    void testSet() {
        System.out.println(redisString.set("test", "Hello Java"));
    }
    
    @Test
    void testGet() {
        System.out.println(redisString.get("test"));
    }
    
    @Test
    void testMset() {
        System.out.println(redisString
                .msetRaw(RedisString.RS_STR_NS + "test1", "Hello a", RedisString.RS_STR_NS + "test2", "Hello b",
                        RedisString.RS_STR_NS + "test3", "Hello c", RedisString.RS_STR_NS + "test4", "Hello d"));
    }
}
