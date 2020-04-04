package com.lsx.service.device.rabbitmq;


import com.lsx.service.device.bean.DeviceMessage;
import com.lsx.service.device.netty.bean.DeviceMessageStore;
//import com.lsx.service.email.EmailBean;
//import com.lsx.service.email.MailUtil;
import com.lsx.service.device.respository.DeviceMessageRespository;
import entity.Result;
import entity.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "DeviceMessageStoreQueue")
public class DeviceMessageStoreMqListener {

    Logger logger = LoggerFactory.getLogger(DeviceMessageStoreMqListener.class);




//    @Autowired
//    private MailUtil mailUtil;
    @Autowired
    private DeviceMessageRespository deviceMessageRespository;



    // FIXME: 2020/3/16 这里 如何发送了不一样的数据类型， 可能会造成死循环   需要解决一下

    @RabbitHandler
    public void getValidEmailMessage(DeviceMessageStore message){

        logger.info("收到消息：" + message);


        //从redis

        logger.info("uuid = "+message.getUuid() + ";topic = "+message.getTopic() + ";msg = "+message.getMsg());

        //入数据库

        DeviceMessage deviceMessage = new DeviceMessage();
        deviceMessage.setTopic(message.getTopic());
        deviceMessage.setMsg(message.getMsg());
        deviceMessage.setUuid(message.getUuid());

        try {
            deviceMessageRespository.save(deviceMessage);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("来自netty的device消息数据，存入数据库失败！消息内容：【" +message.toString()+"】");
        }


//        logger.info(message.toString());
//
//
//
//        //发送邮件
//        mailUtil.sendSimpleMail(message);

    }
}
