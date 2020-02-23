package com.lsx.component.mqttBroker.mqtt.common.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;

/**
 * zookeeper 监听
 **/
public interface ZkStateListener {

    default void connectedEvent(CuratorFramework curator, ConnectionState state) {
    }

    default void ReconnectedEvent(CuratorFramework curator, ConnectionState state) {
    }

    default void lostEvent(CuratorFramework curator, ConnectionState state) {
    }

}
