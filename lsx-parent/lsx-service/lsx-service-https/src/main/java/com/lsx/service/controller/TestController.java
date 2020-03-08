package com.lsx.service.controller;


import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("test")
public class TestController {


    @RequestMapping("test")
    public String test(){
        return "test";
    }
}
