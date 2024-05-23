package com.dinghu.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 1.0
 * @author huding
 * @Date: 2024/05/23 14:41
 * @Description:
 */

@RestController
@Slf4j
@RequestMapping("/redis")
public class TestRedis {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    @GetMapping("/testRedis")
    public String testRedis () {
        redisTemplate.opsForValue().set("test", "1111");
        String value = (String) redisTemplate.opsForValue().get("test");
        return value;
    }

}
