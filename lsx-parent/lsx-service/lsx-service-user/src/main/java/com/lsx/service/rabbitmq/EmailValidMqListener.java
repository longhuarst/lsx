package com.lsx.service.rabbitmq;


import com.lsx.service.email.EmailBean;
import com.lsx.service.email.MailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "validEmailRequestQueue")
public class EmailValidMqListener {

    Logger logger = LoggerFactory.getLogger(EmailValidMqListener.class);




    @Autowired
    private MailUtil mailUtil;



    // FIXME: 2020/3/16 这里 如何发送了不一样的数据类型， 可能会造成死循环   需要解决一下

    @RabbitHandler
    public void getValidEmailMessage(EmailBean message){

        logger.info("收到消息：" + message);

        logger.info(message.toString());



        //发送邮件
        mailUtil.sendSimpleMail(message);




    }
}
