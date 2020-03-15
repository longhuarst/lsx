package com.lsx.web.controller;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {


    @CrossOrigin()
    @RequestMapping("/test")
    String test(){
        return "test";
    }
}
