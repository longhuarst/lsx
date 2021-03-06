package com.lsx.service.device.controller;


import com.lsx.service.device.bean.Device;
import com.lsx.service.device.bean.DeviceBinding;
import com.lsx.service.device.bean.DeviceMessage;
import com.lsx.service.device.netty.NettyServerHandler;
import com.lsx.service.device.netty.utils.IpUtil;
import com.lsx.service.device.respository.DeviceBindingRepository;
import com.lsx.service.device.respository.DeviceMessageRespository;
import com.lsx.service.device.respository.DeviceRespository;
import com.lsx.service.device.service.DeviceService;
import com.lsx.service.device.service.TokenService;
import com.lsx.service.device.util.AuthToken;
import com.sun.org.apache.xalan.internal.xsltc.runtime.ErrorMessages_zh_CN;
import entity.Result;
import entity.StatusCode;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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

    @Autowired
    private DeviceMessageRespository deviceMessageRespository;


    @Autowired
    private DeviceBindingRepository deviceBindingRepository;


    //创建设备
    // 创建设备后，应该给设备赋予一个token device权限的token ，用于设备鉴权
    @RequestMapping("/createDevice")
    @PreAuthorize("hasAnyAuthority('DeviceManager')")   //拥有设备管理管理权限的人才可以创建设备
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






//        AuthToken authToken = new AuthToken();
//
//        authToken.setAccessToken(token);
//        authToken.setRefreshToken(null); //无法刷新令牌
//        authToken.setJti(null); //无jti

        //授权 发布token
//        try {
//            authToken = deviceService.getToken(uuid);
//        }catch (Exception e){
//            e.printStackTrace();
//            return new Result(false, StatusCode.ERROR, "创建失败，需要联系管理员");
//        }

        device.setToken(token);



        try {
            retDevice = deviceRespository.save(device);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,StatusCode.ERROR, e.getMessage());
        }





        return new Result(true, StatusCode.OK, "创建成功", retDevice);

    }


    @RequestMapping("createDeviceWithToken")
    @PreAuthorize("hasAnyAuthority('admin')")
    Result createDeviceWithToken(String type, String name, String info){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getPrincipal().toString();

        if (type == null)
            return new Result(false, StatusCode.ERROR, "空的类型");

        if (name == null)
            return new Result(false, StatusCode.ERROR, "空的名字");

//        if (info == null)
//            return new Result(false, StatusCode.ERROR, "空的信息");

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

        device.setToken(token);



        try {
            retDevice = deviceRespository.save(device);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,StatusCode.ERROR, e.getMessage());
        }





        return new Result(true, StatusCode.OK, "创建成功", retDevice);

    }



    // FIXME: 2020/3/16 注意  这个版本 删除删除 后 设备 的历史数据还会被保存
    //删除设备
    @RequestMapping("deleteDeviceByUuid")
    @PreAuthorize("hasAnyAuthority('admin')")
    Result deleteDeviceByUuid(String uuid){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getPrincipal().toString();

        Device example = new Device();

        example.setUsername(username);
        example.setUuid(uuid);

        Device rs = null;
        try {
            Optional<Device> rso = deviceRespository.findOne(Example.of(example));
            if (rso.isPresent()){
                rs = rso.get();
            }else{
                return new Result(true, StatusCode.ERROR, "失败", "设备不存在");
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Result(true, StatusCode.ERROR, "失败", "数据库查询出错");
        }



        logger.info("正在删除 uuid = "+uuid+" 的设备");

        try{
            deviceRespository.delete(rs);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, e.getMessage());
        }

        return new Result(false, StatusCode.OK, "成功");
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





    //获取设备信息
    @RequestMapping("/getDeviceInfoByUuid")
    @PreAuthorize("hasAnyAuthority('user')")
    Result getDeviceInfoByUuid(String uuid){


        logger.info("getDeviceInfoByUuid, uuid= "+uuid);


        logger.info("----getDeviceInfoByUuid----");
        Device example = new Device();
        example.setUuid(uuid);


        try {
            Optional<Device> resDevice = deviceRespository.findOne(Example.of(example));

            if (resDevice.isPresent()){

                return new Result(true, StatusCode.OK, "成功", resDevice.get());
            }else{
                return new Result(true, StatusCode.ERROR, "失败", "查无此设备");
            }
        }catch (Exception e){
            e.printStackTrace();

            return new Result(true, StatusCode.ERROR, "失败", e.getMessage());
        }






    }



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

        Optional<DeviceBinding> res = null;

        try {
            res = deviceBindingRepository.findOne(Example.of(deviceBinding));
        }catch (Exception e){
            e.printStackTrace();;
        }

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




    //只有持有device 权限的消息才能 上传
    @RequestMapping("upload")
    @PreAuthorize("hasAuthority('device')")
    Result upload(String topic, String msg){

        //uuid 在 authorization 中

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String principal = authentication.getPrincipal().toString();
        logger.info("principal = "+principal);


        String uuid = principal;


        System.out.println(authentication.getDetails().toString());


        //获取设备身份信息
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        authentication.getPrincipal();


        //从redis

        logger.info("uuid = "+uuid + ";topic = "+topic + ";msg = "+msg);

        //入数据库

        DeviceMessage deviceMessage = new DeviceMessage();
        deviceMessage.setTopic(topic);
        deviceMessage.setMsg(msg);
        deviceMessage.setUuid(uuid);

        try {
            deviceMessageRespository.save(deviceMessage);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(true, StatusCode.ERROR, "failed", e.getMessage());
        }



        return new Result(false, StatusCode.OK, "success");
    }




    //这个 接口提供用户测试上传
    //只有持有device 权限的消息才能 上传
    @RequestMapping("userTestUpload")
    @PreAuthorize("hasAuthority('user')")
    Result upload(String uuid, String topic, String msg){

        //uuid 在 authorization 中

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String principal = authentication.getPrincipal().toString();
        logger.info("principal = " + principal);


        String username = principal;


        //先鉴权
        DeviceBinding deviceBindingExample = new DeviceBinding();
        deviceBindingExample.setUsername(username);
        deviceBindingExample.setUuid(uuid);

        try{

            Optional<DeviceBinding> rs = deviceBindingRepository.findOne(Example.of(deviceBindingExample));
            if (rs.isPresent()){

            }else{

                //是否是创建者
                Device deviceExample = new Device();
                deviceExample.setUuid(uuid);
                deviceExample.setUsername(username);

                Optional<Device> rsDevice = deviceRespository.findOne(Example.of(deviceExample));

                if (!rsDevice.isPresent()){
                    return new Result(true, StatusCode.ERROR, "失败", "无法测试未绑定的设备");
                }


            }

        }catch (Exception e){
            e.printStackTrace();
            return new Result(true, StatusCode.ERROR, "失败", e.getMessage());
        }


        DeviceMessage deviceMessage = new DeviceMessage();
        deviceMessage.setUuid(uuid);
        deviceMessage.setMsg(msg);
        deviceMessage.setTopic(topic);

        try {
            deviceMessageRespository.save(deviceMessage);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(true, StatusCode.ERROR, "失败", e.getMessage());
        }

        return new Result(false, StatusCode.OK, "成功");

    }


    //只有持有device 权限的消息才能 读取
    @RequestMapping("getDeviceMessage")
    @PreAuthorize("hasAuthority('user')")
    Result getDeviceMessage(String uuid) {

        //uuid 在 authorization 中

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String principal = authentication.getPrincipal().toString();
        logger.info("principal = " + principal);


        String username = principal;


        //先鉴权
        DeviceBinding deviceBindingExample = new DeviceBinding();
        deviceBindingExample.setUsername(username);
        deviceBindingExample.setUuid(uuid);

        try{

            Optional<DeviceBinding> rs = deviceBindingRepository.findOne(Example.of(deviceBindingExample));
            if (rs.isPresent()){

            }else{

                //是否是创建者
                Device deviceExample = new Device();
                deviceExample.setUuid(uuid);
                deviceExample.setUsername(username);

                Optional<Device> rsDevice = deviceRespository.findOne(Example.of(deviceExample));

                if (!rsDevice.isPresent()) {
                    return new Result(true, StatusCode.ERROR, "失败", "无法获取未绑定的设备");
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            return new Result(true, StatusCode.ERROR, "失败", e.getMessage());
        }

        DeviceMessage example = new DeviceMessage();
        example.setUuid(uuid);


        try {
            List<DeviceMessage> deviceMessageList = deviceMessageRespository.findAll(Example.of(example));

//            logger.info(deviceMessageList.get(0).toString());

            return new Result(true, StatusCode.OK, "成功", deviceMessageList);


        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, StatusCode.ERROR, "失败", e.getMessage());
        }

    }


    //发布消息给设备
    @RequestMapping("publishToDevice")
    @PreAuthorize("hasAnyAuthority('user')")
    public Result publishToDeviceByUuid(HttpServletRequest request, String topic, String uuid, String msg){

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
                    return new Result(true, StatusCode.ERROR, "失败", "权限不足");
                }
//                return new Result(true , StatusCode.ERROR, "失败", "权限不足");
            }

        }catch (Exception e){
            e.printStackTrace();
            return new Result(true , StatusCode.ERROR, "失败", e.getMessage());
        }



        //验证完毕
        ChannelGroup channels = NettyServerHandler.topicMap.get(topic);
        if (channels != null){
            for (Channel channel : channels){
                String sendMsg = "pub_"+uuid+"_"+topic+"_http_"+ request.getRemoteHost().toString() +":"+msg+"\r\n";
                channel.writeAndFlush(sendMsg);
            }
        }


        return new Result(true,StatusCode.OK, "成功", channels.size());

    }






}
