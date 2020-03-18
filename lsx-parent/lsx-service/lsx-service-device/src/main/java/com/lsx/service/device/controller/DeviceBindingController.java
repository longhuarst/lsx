package com.lsx.service.device.controller;


import com.lsx.service.device.bean.Device;
import com.lsx.service.device.bean.DeviceBinding;
import com.lsx.service.device.respository.DeviceBindingRepository;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/deviceBinding")
public class DeviceBindingController {



    @Autowired
    private DeviceBindingRepository deviceBindingRepository;



//    @RequestMapping("/binding")

    //绑定设备
    @RequestMapping("/binding")
    @PreAuthorize("hasAnyAuthority('user')")
    @CrossOrigin
    public Result binding(String uuid){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getPrincipal().toString();

        //先查询是否存在
        DeviceBinding deviceBinding = new DeviceBinding();
        deviceBinding.setUsername(username);
        deviceBinding.setUuid(uuid);

        Optional<DeviceBinding> res = deviceBindingRepository.findOne(Example.of(deviceBinding));

        if (res.isPresent()){
            return new Result(true, StatusCode.ERROR, "失败", "不能重复绑定");
        }

        try {
            deviceBindingRepository.save(deviceBinding);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(true, StatusCode.ERROR, "失败", e.getMessage());
        }

        return new Result(true, StatusCode.OK, "成功", "绑定成功");

    }



    //解除绑定设备
    @RequestMapping("/unbinding")
    @PreAuthorize("hasAnyAuthority('user')")
    public Result unbinding(String uuid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getPrincipal().toString();

        DeviceBinding deviceBinding = new DeviceBinding();
        deviceBinding.setUsername(username);
        deviceBinding.setUuid(uuid);

        Optional<DeviceBinding> res = deviceBindingRepository.findOne(Example.of(deviceBinding));

        if (res.isPresent()){
            deviceBindingRepository.delete(res.get());
            return new Result(true, StatusCode.OK, "成功", "解除绑定成功");
        }else{
            return new Result(true, StatusCode.ERROR, "失败", "解除绑定失败");

        }




    }




}


