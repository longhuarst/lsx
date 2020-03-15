package com.lsx.service.rabbitmq;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "validEmailRequestQueue")
public class EmailValidMqListener {

    Logger logger = LoggerFactory.getLogger(EmailValidMqListener.class);

    @RabbitListener
    public void getValidEmailMessage(String message){

        logger.info("收到消息：" + message);


        //发送邮件


    }
}
