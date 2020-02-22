package com.lsx.component.mqtt;


import com.lsx.component.mqtt.mqtt.MqttDeserializer;
import com.lsx.component.mqtt.mqtt.MqttProtocolResolver;
import com.lsx.component.mqtt.mqtt.MqttSerializer;
import com.lsx.component.mqtt.mqtt.MqttSuperSerializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.serializer.DefaultDeserializer;
import org.springframework.core.serializer.DefaultSerializer;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.ip.tcp.TcpReceivingChannelAdapter;
import org.springframework.integration.ip.tcp.connection.TcpNetServerConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpNioServerConnectionFactory;
import org.springframework.integration.ip.tcp.serializer.ByteArrayElasticRawDeserializer;
import org.springframework.integration.ip.tcp.serializer.ByteArrayRawSerializer;
import org.springframework.integration.ip.tcp.serializer.ByteArrayStxEtxSerializer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;


@SpringBootApplication
public class MqttApplication {

    public static void main(String[] args) {

//        SpringApplication.run(MqttApplication.class, args);
        new SpringApplicationBuilder(MqttApplication.class)
                .web(WebApplicationType.NONE)   //官方指定的是  false 但是存在问题
                .run(args);
    }


    @Bean
    public MessageChannel mqttInputChannel(){
        return new DirectChannel();
    }


    @Bean
    public MessageProducer inbound(){





        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                "tcp://localhost:1883",
                "testClient",
                "topic1",
                "topic2");

        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler(){
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                System.out.println(message.getPayload());
            }
        };
    }









    //tcp 相关
    @Bean
    public TcpNioServerConnectionFactory tcpNioServerConnectionFactory(){
        TcpNioServerConnectionFactory tcpNioServerConnectionFactory =  new TcpNioServerConnectionFactory(19000);

        tcpNioServerConnectionFactory.setSerializer(new MqttSuperSerializer());
        tcpNioServerConnectionFactory.setDeserializer(new MqttSuperSerializer());
        tcpNioServerConnectionFactory.setSingleUse(true);


//        ByteArrayStxEtxSerializer



        return tcpNioServerConnectionFactory;
    }




    @Bean
    public TcpReceivingChannelAdapter tcpReceivingChannelAdapter(TcpNioServerConnectionFactory tcpNioServerConnectionFactory){
        TcpReceivingChannelAdapter receivingChannelAdapter = new TcpReceivingChannelAdapter();
        receivingChannelAdapter.setConnectionFactory(tcpNioServerConnectionFactory);
        receivingChannelAdapter.setOutputChannelName("tcp");
        return receivingChannelAdapter;
    }

    @ServiceActivator(inputChannel = "tcp")
    public void messageHandler(Message<?> message){
        byte[] bytes = (byte[]) message.getPayload();
        System.out.println(new String(bytes));
        //这里处理 mqtt 协议
//        MqttProtocolResolver.resovler();
    }











}
