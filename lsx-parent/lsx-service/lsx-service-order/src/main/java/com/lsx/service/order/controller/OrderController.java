package com.lsx.service.order.controller;


import com.lsx.service.order.service.OrderService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableEurekaClient
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping("add")
    public Result add(@RequestParam("message") String message){
        orderService.add(message);
        return new Result(false, StatusCode.OK, "add 成功");
    }


}
