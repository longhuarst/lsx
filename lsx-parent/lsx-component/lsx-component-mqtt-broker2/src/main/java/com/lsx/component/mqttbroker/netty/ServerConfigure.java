package com.lsx.component.mqttbroker.netty;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "netty.server.configure")
public class ServerConfigure {
    private int port;
    private int bossGroupThread;
    private int workerGroupThread;
    private boolean resueAddr;
    private int backLog;
    private int rcvBuf;
    private int sndBuf;
    private boolean noDelay;
    private boolean keepAlive;

    private int heart;//心跳






    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getBossGroupThread() {
        return bossGroupThread;
    }

    public void setBossGroupThread(int bossGroupThread) {
        this.bossGroupThread = bossGroupThread;
    }

    public int getWorkerGroupThread() {
        return workerGroupThread;
    }

    public void setWorkerGroupThread(int workerGroupThread) {
        this.workerGroupThread = workerGroupThread;
    }

    public boolean isResueAddr() {
        return resueAddr;
    }

    public void setResueAddr(boolean resueAddr) {
        this.resueAddr = resueAddr;
    }

    public int getBackLog() {
        return backLog;
    }

    public void setBackLog(int backLog) {
        this.backLog = backLog;
    }

    public int getRcvBuf() {
        return rcvBuf;
    }

    public void setRcvBuf(int rcvBuf) {
        this.rcvBuf = rcvBuf;
    }

    public int getSndBuf() {
        return sndBuf;
    }

    public void setSndBuf(int sndBuf) {
        this.sndBuf = sndBuf;
    }

    public boolean isNoDelay() {
        return noDelay;
    }

    public void setNoDelay(boolean noDelay) {
        this.noDelay = noDelay;
    }

    public boolean isKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }


    public int getHeart() {
        return heart;
    }

    public void setHeart(int heart) {
        this.heart = heart;
    }




}
