package com.lsx.component.mqttBroker.mqtt.common.pool;

import java.util.concurrent.ScheduledFuture;

/**
 * 接口
 **/
@FunctionalInterface
public interface Scheduled {

    ScheduledFuture<?> submit(Runnable runnable);
}
