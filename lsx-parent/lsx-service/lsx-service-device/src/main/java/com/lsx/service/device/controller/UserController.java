package com.lsx.service.device.controller;


import com.lsx.service.device.bean.DeviceBinding;
import com.lsx.service.device.bean.DeviceMessage;
import com.lsx.service.device.respository.DeviceBindingRepository;
import com.lsx.service.device.respository.DeviceMessageRespository;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

//用户控制
@RequestMapping("/user")
@RestController
public class UserController {


    @Autowired
    private DeviceMessageRespository deviceMessageRespository;

    @Autowired
    private DeviceBindingRepository deviceBindingRepository;



    @RequestMapping("/loadDataByUuId")
    @PreAuthorize("hasAnyAuthority('user')") //普通用户权限
    public Result loadDataByUuId(String uuid){


        //先验证 用户是否绑定是这个 uuid
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getPrincipal().toString();

        try{
            DeviceBinding example = new DeviceBinding();
            example.setUsername(username);
            example.setUuid(uuid);

            Optional<DeviceBinding> res = deviceBindingRepository.findOne(Example.of(example));

            if (!res.isPresent()){
                //未绑定
                return new Result(true , StatusCode.ERROR, "失败", "权限不足");
            }

        }catch (Exception e){
            e.printStackTrace();
            return new Result(true , StatusCode.ERROR, "失败", e.getMessage());
        }


        //绑定的设备


        DeviceMessage example = new DeviceMessage();
        example.setUuid(uuid);

        List<DeviceMessage> deviceMessageList = deviceMessageRespository.findAll(Example.of(example));


        return new Result(true, StatusCode.OK , "成功", deviceMessageList);
    }






    //加载所有绑定的设备
    @RequestMapping("/loadAllBindingDevice")
    @PreAuthorize("hasAnyAuthority('user')")
    public Result loadAllBindingDevice(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getPrincipal().toString();

        DeviceBinding example = new DeviceBinding();
        example.setUsername(username);

        List<DeviceBinding> deviceBindingList = deviceBindingRepository.findAll(Example.of(example));

        return new Result(true, StatusCode.OK, "成功", deviceBindingList);
    }


    //加载所有绑定的用户 【设备管理员才可以】
    @RequestMapping("/loadAllUsersWhoBindingByUuid")
    @PreAuthorize("hasAnyAuthority('DeviceManager')")
    public Result loadAllUsersWhoBindingByUuid(String uuid){

        DeviceBinding example = new DeviceBinding();
        example.setUsername(uuid);

        List<DeviceBinding> deviceBindingList = deviceBindingRepository.findAll(Example.of(example));


        return new Result(true, StatusCode.OK, "成功", deviceBindingList);
    }






}
