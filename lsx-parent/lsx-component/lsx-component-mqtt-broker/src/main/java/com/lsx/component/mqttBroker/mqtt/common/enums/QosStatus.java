package com.lsx.component.mqttBroker.mqtt.common.enums;/**
 * Created by wangcy on 2017/12/14.
 */

/**
 * Qos确认状态
 **/
public enum QosStatus {

    PUBD, // 已发送 没收到RECD （发送）

    RECD, //publish 推送回复过（发送）


}
