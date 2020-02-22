package com.lsx.service.order.rabbitmq.configure;



import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



//延时队列配置

@Configuration
public class RabbitMqConfig {

    //创建队列 queue1 延时队列， 会过期， 过期后将数据转发给 queue2
    @Bean
    public Queue orderDelayQueue(){

        return QueueBuilder
                .durable("orderDelayQueue")
                .withArgument("x-dead-letter-exchange", "orderListenerExchange")    //消息超时进入死信队列，绑定死信队列交换机   参数2  被绑定的交换机
                .withArgument("x-dead-letter-routing-key","orderListenerQueue")             //绑定指定的routing-key -- 参数2 路由给自己
                .build();
    }


    //创建queue2
    @Bean
    public Queue orderListenerQueue(){
        return new Queue("orderListenerQueue", true);
    }


    //创建交换机
    @Bean
    public Exchange orderListenerExchange(){
        return new DirectExchange("orderListenerExchange");
    }


    //队列queue2绑定exchange
    @Bean
    public Binding orderListenerBinding(Queue orderListenerQueue, Exchange orderListenerExchange){
        return BindingBuilder.bind(orderListenerQueue).to(orderListenerExchange).with("orderListenerQueue").noargs();
    }


}
