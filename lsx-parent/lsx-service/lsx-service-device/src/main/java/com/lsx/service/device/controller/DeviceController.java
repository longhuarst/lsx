package com.lsx.service.device.controller;


import com.lsx.service.device.bean.Device;
import com.lsx.service.device.respository.DeviceRespository;
import com.lsx.service.device.service.DeviceService;
import com.lsx.service.device.util.AuthToken;
import entity.Result;
import entity.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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


        AuthToken authToken = null;

        //授权 发布token
        try {
            authToken = deviceService.getToken(uuid);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "创建失败，需要联系管理员");
        }

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



    @RequestMapping("upload")
    Result upload(String uuid, String topic, String msg){



        //从redis

        logger.info("uuid = "+uuid + ";topic = "+topic + ";msg = "+msg);


        return new Result(false, StatusCode.OK, "success");
    }



}
