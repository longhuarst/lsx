package com.lsx.service.device.netty.bean;

import java.util.Date;

public class Client {


    Date connectDate;//连接时间


    boolean isValidToken;//是否验证过token


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
