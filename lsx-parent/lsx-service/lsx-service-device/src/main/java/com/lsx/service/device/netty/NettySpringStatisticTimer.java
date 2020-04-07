package com.lsx.service.device.netty;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class NettySpringStatisticTimer {

    private static Logger logger = LoggerFactory.getLogger(NettySpringStatisticTimer.class);

    private static  final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");


    public static NettyStatistic statistic = new NettyStatistic();




    private int count = 0;

    @Scheduled(cron = "*/6 * * * * ?")
    private void process(){
        logger.info("netty 统计："+count);
    }


    @Scheduled(fixedRate = 6000)
    public void reportCurrentTime(){
        logger.info("现在时间："+dateFormat.format(new Date()));
    }




    @Scheduled(fixedRate = 1000)
    public void calc1sec(){
        //计算一秒的数据
        statistic.commitPps();

        statistic.commitCps();

        statistic.commitDps();

//        logger.info("pps = "+statistic.getPps());
//        logger.info("cps = "+statistic.getCps());
//        logger.info("dps = "+statistic.getDps());

    }

}
