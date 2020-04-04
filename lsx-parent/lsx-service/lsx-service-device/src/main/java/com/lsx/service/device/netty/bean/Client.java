package com.lsx.service.device.netty.bean;

import java.io.Serializable;
import java.util.Date;

public class Client implements Serializable {


    Date connectDate;//连接时间


    boolean isValidToken;//是否验证过token

    String uuid;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Date getConnectDate() {
        return connectDate;
    }

    public void setConnectDate(Date connectDate) {
        this.connectDate = connectDate;
    }

    public boolean isValidToken() {
        return isValidToken;
    }

    public void setValidToken(boolean validToken) {
        isValidToken = validToken;
    }
}
