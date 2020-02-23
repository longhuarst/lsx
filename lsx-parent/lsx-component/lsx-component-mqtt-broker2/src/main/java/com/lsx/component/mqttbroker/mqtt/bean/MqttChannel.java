package com.lsx.component.mqttbroker.mqtt.bean;

import com.lsx.component.mqttbroker.mqtt.enums.SessionStatus;
import com.lsx.component.mqttbroker.mqtt.enums.SubStatus;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;


import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MqttChannel {

    private transient volatile Channel channel;

    private String deviceId; // 设备id

    private boolean isWill; //遗嘱

    private volatile SubStatus subStatus; //是否订阅过主题

    private Set<String> topic; //话题列表

    private volatile SessionStatus sessionStatus; //在线 - 离线

    private ConcurrentHashMap<Integer, SendMqttMessage> message; // messageId - message(qos1) //待确认消息

    private Set<Integer> receive;

    public void  addRecevice(int messageId){
        receive.add(messageId);
    }

    public boolean  checkRecevice(int messageId){
        return  receive.contains(messageId);
    }

    public boolean  removeRecevice(int messageId){
        return receive.remove(messageId);
    }


    public void addSendMqttMessage(int messageId,SendMqttMessage msg){
        message.put(messageId,msg);
    }


    public SendMqttMessage getSendMqttMessage(int messageId){
        return  message.get(messageId);
    }


    public  void removeSendMqttMessage(int messageId){
        message.remove(messageId);
    }




    /**
     * 判断当前channel 是否登录过
     * @return
     */
    public boolean isLogin(){
        return Optional.ofNullable(this.channel).map(channel1 -> {
            AttributeKey<Boolean> _login = AttributeKey.valueOf("login");
            return channel1.isActive() && channel1.hasAttr(_login);
        }).orElse(false);
    }

    /**
     * 非正常关闭
     */
    public void close(){
        Optional.ofNullable(this.channel).ifPresent(channel1 -> channel1.close());
    }

    /**
     *  通道是否活跃
     * @return
     */
    public  boolean isActive(){
        return  channel!=null&&this.channel.isActive();
    }



    public boolean addTopic(Set<String> topics){
        return topic.addAll(topics);
    }


    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public boolean isWill() {
        return isWill;
    }

    public void setWill(boolean will) {
        isWill = will;
    }

    public SubStatus getSubStatus() {
        return subStatus;
    }

    public void setSubStatus(SubStatus subStatus) {
        this.subStatus = subStatus;
    }

    public Set<String> getTopic() {
        return topic;
    }

    public void setTopic(Set<String> topic) {
        this.topic = topic;
    }

    public SessionStatus getSessionStatus() {
        return sessionStatus;
    }

    public void setSessionStatus(SessionStatus sessionStatus) {
        this.sessionStatus = sessionStatus;
    }

    public ConcurrentHashMap<Integer, SendMqttMessage> getMessage() {
        return message;
    }

    public void setMessage(ConcurrentHashMap<Integer, SendMqttMessage> message) {
        this.message = message;
    }

    public Set<Integer> getReceive() {
        return receive;
    }

    public void setReceive(Set<Integer> receive) {
        this.receive = receive;
    }

    public static class WillMessage {


        private String willTopic;

        private String willMessage;


        private  boolean isRetain;

        private int qos;


        public String getWillTopic() {
            return willTopic;
        }

        public void setWillTopic(String willTopic) {
            this.willTopic = willTopic;
        }

        public String getWillMessage() {
            return willMessage;
        }

        public void setWillMessage(String willMessage) {
            this.willMessage = willMessage;
        }

        public boolean isRetain() {
            return isRetain;
        }

        public void setRetain(boolean retain) {
            isRetain = retain;
        }

        public int getQos() {
            return qos;
        }

        public void setQos(int qos) {
            this.qos = qos;
        }
    }
}
