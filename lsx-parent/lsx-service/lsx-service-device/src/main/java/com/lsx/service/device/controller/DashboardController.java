package com.lsx.service.device.controller;

import com.lsx.service.device.netty.NettyServerHandler;
import com.lsx.service.device.netty.NettySpringStatisticTimer;
import entity.Result;
import entity.StatusCode;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/dashboard")
public class DashboardController {



    //获取在线设备【tcp连接】
    @PreAuthorize("hasAnyAuthority('user')")
    public Result getOnlineDeviceNumber(){

        int num = NettyServerHandler.clientList.size();


        return new Result(true, StatusCode.OK, "成功", num);
    }


    //统计数据 pps






    //获取所有数据
    @PreAuthorize("hasAnyAuthority('admin')")
    public Result getAllStatistic(){

        Map<String, String> resMap = new HashMap<>();


        resMap.put("online", String.valueOf(NettyServerHandler.clientList.size()));
        resMap.put("pps", String.valueOf(NettySpringStatisticTimer.statistic.getPps()));
        resMap.put("cps", String.valueOf(NettySpringStatisticTimer.statistic.getCps()));
        resMap.put("dps", String.valueOf(NettySpringStatisticTimer.statistic.getDps()));




        return new Result(true, StatusCode.OK, "成功", resMap);
    }






}
