package com.lsx.component.mqttBroker.mqtt.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 链接参数配置
 **/
@ConfigurationProperties(prefix = "com.lsx.component.mqttBroker")
@Data
public class ConnectOptions {

    private long connectTime;

    private String serverIp;

    private int port ;

    private boolean keepalive ;

    private boolean reuseaddr ;

    private boolean tcpNodelay ;

    private int backlog ;

    private  int  sndbuf ;

    private int revbuf ;

    private int heart;

    private boolean ssl ;

    private String jksFile;

    private String jksStorePassword;

    private String jksCertificatePassword;

    private  int minPeriod ;

    private int bossThread;

    private int workThread;

    private MqttOpntions mqtt;

    @Data
    public static class MqttOpntions{

        private  String clientIdentifier;

        private  String willTopic;

        private  String willMessage;

        private  String userName;

        private  String password;

        private  boolean hasUserName;

        private  boolean hasPassword;

        private  boolean isWillRetain;

        private  int willQos;

        private  boolean isWillFlag;

        private  boolean isCleanSession;

        private int KeepAliveTime;


    }
}
