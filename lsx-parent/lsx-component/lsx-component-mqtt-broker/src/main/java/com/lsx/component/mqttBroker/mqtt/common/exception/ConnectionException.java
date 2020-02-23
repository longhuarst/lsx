package com.lsx.component.mqttBroker.mqtt.common.exception;

/**
 * 连接异常
 **/
public class ConnectionException extends  RuntimeException {

    public ConnectionException(String message) {
        super(message);
    }
}
