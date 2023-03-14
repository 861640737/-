package cn.hjc.reggie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class Test {

    @Autowired
    RedisTemplate redisTemplate;

    @GetMapping("/aa")
    public void save() {
        redisTemplate.opsForValue().set("aa", 99);
    }


}
