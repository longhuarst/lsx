package com.lsx.component.mqttbroker.mqtt.service;

import com.lsx.component.mqttbroker.mqtt.bean.MqttChannel;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class AbstractChannelService extends PublishApiSevice implements ChannelService ,BaseApi{

    Logger logger = LoggerFactory.getLogger(MqttHandlerService.class);
    boolean debug = logger.isDebugEnabled();


    protected AttributeKey<Boolean> _login = AttributeKey.valueOf("login");

    protected   AttributeKey<String> _deviceId = AttributeKey.valueOf("deviceId");

    protected  static char SPLITOR = '/';

    protected ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*2);


    protected static CacheMap<String,MqttChannel> cacheMap= new CacheMap<>();


    protected static ConcurrentHashMap<String ,MqttChannel> mqttChannels = new ConcurrentHashMap<>(); // deviceId - mqChannel 登录


    protected  static  ConcurrentHashMap<String,ConcurrentLinkedQueue<RetainMessage>> retain = new ConcurrentHashMap<>(); // topic - 保留消息



    protected  static  Cache<String, Collection<MqttChannel>> mqttChannelCache = CacheBuilder.newBuilder().maximumSize(100).build();

    public AbstractChannelService(ScanRunnable scanRunnable) {
        super(scanRunnable);
    }


    protected  Collection<MqttChannel> getChannels(String topic,TopicFilter topicFilter){
        try {
            return  mqttChannelCache.get(topic, () -> topicFilter.filter(topic));
        } catch (Exception e) {
            logger.info(String.format("guava cache key topic【%s】 channel   value== null ",topic));
        }
        return null;
    }


    @FunctionalInterface
    interface TopicFilter{
        Collection<MqttChannel> filter(String topic);
    }

    protected boolean deleteChannel(String topic,MqttChannel mqttChannel){
        return  Optional.ofNullable(topic).map(s -> {
            mqttChannelCache.invalidate(s);
            return  cacheMap.delete(getTopic(s),mqttChannel);
        }).orElse(false);
    }

    protected boolean addChannel(String topic,MqttChannel mqttChannel)
    {
        return  Optional.ofNullable(topic).map(s -> {
            mqttChannelCache.invalidate(s);
            return cacheMap.putData(getTopic(s),mqttChannel);
        }).orElse(false);
    }

    /**
     * 获取channel
     */
    public MqttChannel getMqttChannel(String deviceId){
        return Optional.ofNullable(deviceId).map(s -> mqttChannels.get(s))
                .orElse(null);

    }

    /**
     * 获取channelId
     */
    public String  getDeviceId(Channel channel){
        return  Optional.ofNullable(channel).map( channel1->channel1.attr(_deviceId).get())
                .orElse(null);
    }



    protected String[] getTopic(String topic)  {
        return Optional.ofNullable(topic).map(s ->
                StringUtils.split(topic,SPLITOR)
        ).orElse(null);
    }



}
