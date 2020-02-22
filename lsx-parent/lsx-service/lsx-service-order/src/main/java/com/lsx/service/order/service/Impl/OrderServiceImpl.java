package com.lsx.service.order.service.Impl;

import com.lsx.service.order.service.OrderService;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;


@Service
public class OrderServiceImpl implements OrderService {



    @Autowired
    private RabbitTemplate rabbitTemplate;


    public void add(String message){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("创建订单时间 ： "+simpleDateFormat.format(new Date()));


        //添加订单
        rabbitTemplate.convertAndSend("orderDelayQueue", (Object) "test", new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                //设置延时读取
                message.getMessageProperties().setExpiration("10000");//超时时间  单位：毫秒

                return message;
            }
        });

    }





}
