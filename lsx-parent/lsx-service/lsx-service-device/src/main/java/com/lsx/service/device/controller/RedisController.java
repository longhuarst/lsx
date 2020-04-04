package com.lsx.service.device.controller;


import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController {


    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @RequestMapping("/redis/test")
    public Result test(){
        redisTemplate.opsForList().leftPush("123","123");

        return new Result(false, StatusCode.OK,"ok");
    }


}
