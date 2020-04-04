package com.lsx.service.device.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitmqConfig {


    //创建一个email 请求的 队列
    @Bean
    public Queue deviceMessageStoreQueue(){
        System.out.println("DeviceMessageStoreQueue-------------------------");
        return new Queue("DeviceMessageStoreQueue", true);
    }

    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange("DeviceMessageStoreExchange");
    }


    @Bean
    Binding bindingEmail(){
        return BindingBuilder.bind(deviceMessageStoreQueue()).to(topicExchange()).with("device.message");

    }


}
