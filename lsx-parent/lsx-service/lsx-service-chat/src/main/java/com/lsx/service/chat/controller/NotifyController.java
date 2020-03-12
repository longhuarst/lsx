package com.lsx.service.chat.controller;


import com.lsx.service.chat.respository.NotifyRepository;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notify")
public class NotifyController {


    //通知

    @Autowired
    NotifyRepository notifyRepository;



//    @RequestMapping("/sendNotify")
//    Result sendNotify(){
//
//    }



}
