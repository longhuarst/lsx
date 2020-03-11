package com.lsx.service.device.controller;


import com.lsx.service.device.bean.Device;
import com.lsx.service.device.respository.DeviceRespository;
import com.lsx.service.device.service.DeviceService;
import com.lsx.service.device.service.TokenService;
import com.lsx.service.device.util.AuthToken;
import entity.Result;
import entity.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/device")
public class DeviceController {


    Logger logger = LoggerFactory.getLogger(DeviceController.class);


    @Resource
    DeviceRespository deviceRespository;


    @Resource
    private DeviceService deviceService;


    @Autowired
    private TokenService tokenService;//用于获取授权服务


    //创建设备
    // 创建设备后，应该给设备赋予一个token device权限的token ，用于设备鉴权
    @RequestMapping("/createDevice")
    Result createDevice(String username, String type, String name, String info){
        if (username == null)
            return new Result(false, StatusCode.ERROR,"空的用户名");

        if (type == null)
            return new Result(false, StatusCode.ERROR, "空的类型");

        if (name == null)
            return new Result(false, StatusCode.ERROR, "空的名字");

        if (info == null)
            return new Result(false, StatusCode.ERROR, "空的信息");

        String uuid = UUID.randomUUID().toString();
        Device device = new Device();
        device.setUsername(username);
        device.setUuid(uuid);
        device.setName(name);

        if (info != null)
            device.setInfo(info);

        device.setType(type);


        Device retDevice;


        //生成令牌
        //令牌有效期 1 年
        long time = new Date().getTime() / 1000 + 365*24*3600;
        String token = tokenService.getToken(username, uuid, time);






        AuthToken authToken = new AuthToken();

        authToken.setAccessToken(token);
        authToken.setRefreshToken(null); //无法刷新令牌
        authToken.setJti(null); //无jti

        //授权 发布token
//        try {
//            authToken = deviceService.getToken(uuid);
//        }catch (Exception e){
//            e.printStackTrace();
//            return new Result(false, StatusCode.ERROR, "创建失败，需要联系管理员");
//        }

        device.setToken(authToken);



        try {
            retDevice = deviceRespository.save(device);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,StatusCode.ERROR, e.getMessage());
        }





        return new Result(true, StatusCode.OK, "创建成功", retDevice);

    }




    @RequestMapping("/refreshDeviceToken")
    Result refreshDeviceToken(String uuid){

        //重新获取令牌

        return new Result(false, StatusCode.OK, "成功");
    }


    @RequestMapping("/findDeviceByUsername")
    Result findDeviceByUsername(String username){
        List<Device> deviceList = null;

        Device device = new Device();
        device.setUsername(username);

        try{
            deviceList = deviceRespository.findAll(Example.of(device));
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, e.getMessage());
        }


        return new Result(true, StatusCode.OK, "成功", deviceList);
    }


    @RequestMapping("deleteDeviceByUuid")
    Result deleteDeviceByUuid(String uuid){





        logger.info("正在删除 uuid = "+uuid+" 的设备");

        try{
            deviceRespository.deleteByUuid(uuid);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, e.getMessage());
        }

        return new Result(false, StatusCode.OK, "成功");
    }




    //只有持有device 权限的消息才能 上传
    @RequestMapping("upload")
    @PreAuthorize("hasAuthority('device')")
    Result upload(String uuid, String topic, String msg){


        //获取设备身份信息
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        authentication.getPrincipal();


        //从redis

        logger.info("uuid = "+uuid + ";topic = "+topic + ";msg = "+msg);


        return new Result(false, StatusCode.OK, "success");
    }



}
