package com.lsx.service.device.controller;


import com.lsx.service.device.bean.Device;
import com.lsx.service.device.respository.DeviceRespository;
import entity.Result;
import entity.StatusCode;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/device")
public class DeviceController {


    @Resource
    DeviceRespository deviceRespository;


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

        Device device = new Device();
        device.setUsername(username);
        device.setUuid(UUID.randomUUID().toString());
        device.setName(name);

        if (info != null)
            device.setInfo(info);

        device.setType(type);


        Device retDevice;

        try {
            retDevice = deviceRespository.save(device);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,StatusCode.ERROR, e.getMessage());
        }


        return new Result(true, StatusCode.OK, "创建成功", retDevice);

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
        try{
            deviceRespository.deleteByUuid(uuid);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, e.getMessage());
        }

        return new Result(false, StatusCode.OK, "成功");
    }




}
