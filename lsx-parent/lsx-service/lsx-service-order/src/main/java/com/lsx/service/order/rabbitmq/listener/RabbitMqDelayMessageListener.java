package com.lsx.service.order.rabbitmq.listener;


import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@RabbitListener(queues = "orderListenerQueue")
public class RabbitMqDelayMessageListener {

    //延时队列监听
    @RabbitHandler
    public void getDelayMessage(String message){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("监听消息时间 ： "+simpleDateFormat.format(new Date()));
        System.out.println("监听到的消息 ： "+message);
    }
}
