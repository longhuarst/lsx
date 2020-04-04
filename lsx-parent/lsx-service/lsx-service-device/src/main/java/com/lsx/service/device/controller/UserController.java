package com.lsx.service.device.controller;


import com.alibaba.fastjson.JSONObject;
import com.lsx.service.device.bean.Device;
import com.lsx.service.device.bean.DeviceBinding;
import com.lsx.service.device.bean.DeviceMessage;
import com.lsx.service.device.respository.DeviceBindingRepository;
import com.lsx.service.device.respository.DeviceMessageRespository;
import com.lsx.service.device.respository.DeviceRespository;
import entity.Result;
import entity.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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

    @Autowired
    DeviceRespository deviceRespository;


    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    static Logger logger = LoggerFactory.getLogger(UserController.class);



    @RequestMapping("loadRealTimeDateByUuid")
    @PreAuthorize("hasAnyAuthority('user')")
    public Result loadRealTimeDateByUuid(String uuid){

        //先验证 用户是否绑定是这个 uuid
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getPrincipal().toString();


        // FIXME: 2020/4/4 这里最好 放入redis 缓存 减少数据库查询时间  因为这个操作是要经常操作的
        try{
            DeviceBinding example = new DeviceBinding();
            example.setUsername(username);
            example.setUuid(uuid);

            Optional<DeviceBinding> res = deviceBindingRepository.findOne(Example.of(example));

            if (!res.isPresent()){
                //未绑定
                //是否是创建者
                Device deviceExample = new Device();
                deviceExample.setUuid(uuid);
                deviceExample.setUsername(username);

                Optional<Device> rsDevice = deviceRespository.findOne(Example.of(deviceExample));

                if (!rsDevice.isPresent()) {
                    return new Result(true, StatusCode.ERROR, "失败", "无法获取未绑定的设备");
                }
//                return new Result(true , StatusCode.ERROR, "失败", "权限不足");
            }

        }catch (Exception e){
            e.printStackTrace();
            return new Result(true , StatusCode.ERROR, "失败", e.getMessage());
        }


        String redisKey = "dm_"+uuid;


        logger.info("redis size  = "+redisTemplate.opsForList().size(redisKey));


        List<String> realTimeData = new ArrayList<>();
        String col = redisTemplate.opsForList().rightPop(redisKey);
        while (col != null){
            realTimeData.add(col);
            col = redisTemplate.opsForList().rightPop(redisKey);
        }


        return new Result(true, StatusCode.OK , "成功", realTimeData);
    }



    @RequestMapping("/loadDataByUuId")
    @CrossOrigin(origins = "*")
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


                //是否是创建者
                Device deviceExample = new Device();
                deviceExample.setUuid(uuid);
                deviceExample.setUsername(username);

                Optional<Device> rsDevice = deviceRespository.findOne(Example.of(deviceExample));

                if (!rsDevice.isPresent()) {
                    return new Result(true, StatusCode.ERROR, "失败", "无法获取未绑定的设备");
                }


//                return new Result(true , StatusCode.ERROR, "失败", "权限不足");
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
    @CrossOrigin(origins = "*")
    public Result loadAllBindingDevice(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getPrincipal().toString();

        DeviceBinding example = new DeviceBinding();
        example.setUsername(username);

        List<DeviceBinding> deviceBindingList = deviceBindingRepository.findAll(Example.of(example));

//        JSONObject resObject = new JSONObject();

        List<JSONObject> rsList = new ArrayList<>();

        for (int i=0;i<deviceBindingList.size(); ++i){
            JSONObject object = new JSONObject();
            Device exampleDevice = new Device();
            exampleDevice.setUuid(deviceBindingList.get(i).getUuid());
            Optional<Device> rs = deviceRespository.findOne(Example.of(exampleDevice));
            if (rs.isPresent()){
                object.put("key",String.valueOf(i));
                object.put("name", rs.get().getName());
                object.put("type", rs.get().getType());
                object.put("uuid", rs.get().getUuid());

                rsList.add(object);
            }
        }



        return new Result(true, StatusCode.OK, "成功", rsList);
    }



    //加载所有绑定的设备 --- 某个管理员创建的
    @RequestMapping("/loadAllAdminDevice")
    @PreAuthorize("hasAnyAuthority('admin')")
    @CrossOrigin(origins = "*")
    public Result loadAllAdminDevice(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getPrincipal().toString();

        Device example = new Device();
        example.setUsername(username);

        List<Device> deviceList = deviceRespository.findAll(Example.of(example));


        List<JSONObject> rsList = new ArrayList<>();

        for (int i=0;i<deviceList.size(); ++i){
            JSONObject object = new JSONObject();
                object.put("key",String.valueOf(i));
                object.put("name", deviceList.get(i).getName());
                object.put("type", deviceList.get(i).getType());
                object.put("uuid", deviceList.get(i).getUuid());

                rsList.add(object);
        }



        return new Result(true, StatusCode.OK, "成功", rsList);
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
