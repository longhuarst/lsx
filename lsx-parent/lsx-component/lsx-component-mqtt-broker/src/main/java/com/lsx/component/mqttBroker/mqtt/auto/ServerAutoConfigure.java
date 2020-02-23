package com.lsx.component.mqttBroker.mqtt.auto;


import com.lsx.component.mqttBroker.mqtt.bootstrap.scan.SacnScheduled;
import com.lsx.component.mqttBroker.mqtt.bootstrap.scan.ScanRunnable;
import com.lsx.component.mqttBroker.mqtt.common.enums.ProtocolEnum;
import com.lsx.component.mqttBroker.mqtt.common.properties.InitBean;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//自动化配置初始化服务
@Configuration
@ConditionalOnClass
@EnableConfigurationProperties({InitBean.class})
public class ServerAutoConfigure {


    private static final int _BLACKLOG = 1024;

    private static final int CPU = Runtime.getRuntime().availableProcessors();

    private static final int SEDU_DAY = 10;

    private static final int TIMEOUT = 120;

    private static final int BUF_SIZE = 10 * 1024 * 1024;


    public ServerAutoConfigure() {

    }

    @Bean
    @ConditionalOnMissingBean(name = "sacnScheduled")
    public ScanRunnable initRunable(@Autowired InitBean serverBean) {
        long time = (serverBean == null || serverBean.getPeriod() < 5) ? 10 : serverBean.getPeriod();
        ScanRunnable sacnScheduled = new SacnScheduled(time);
        Thread scanRunnable = new Thread(sacnScheduled);
        scanRunnable.setDaemon(true);
        scanRunnable.start();
        return sacnScheduled;
    }


    @Bean(initMethod = "open", destroyMethod = "close")
    @ConditionalOnMissingBean
    public InitServer initServer(InitBean serverBean) {
        if (!ObjectUtils.allNotNull(serverBean.getPort(), serverBean.getServerName())) {
            throw new NullPointerException("not set port");
        }
        if (serverBean.getBacklog() < 1) {
            serverBean.setBacklog(_BLACKLOG);
        }
        if (serverBean.getBossThread() < 1) {
            serverBean.setBossThread(CPU);
        }
        if (serverBean.getInitalDelay() < 0) {
            serverBean.setInitalDelay(SEDU_DAY);
        }
        if (serverBean.getPeriod() < 1) {
            serverBean.setPeriod(SEDU_DAY);
        }
        if (serverBean.getHeart() < 1) {
            serverBean.setHeart(TIMEOUT);
        }
        if (serverBean.getRevbuf() < 1) {
            serverBean.setRevbuf(BUF_SIZE);
        }
        if (serverBean.getWorkThread() < 1) {
            serverBean.setWorkThread(CPU * 2);
        }
        if (serverBean.getProtocolEnum() == null) {
            serverBean.setProtocolEnum(ProtocolEnum.MQTT);
        }
        return new InitServer(serverBean);
    }

}
