package com.lsx.service.device.controller;


import com.lsx.service.device.respository.DeviceBindingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/deviceBinding")
@RestController
public class DeviceBindingController {



    @Autowired
    private DeviceBindingRepository deviceBindingRepository;



//    @RequestMapping("/binding")






}


