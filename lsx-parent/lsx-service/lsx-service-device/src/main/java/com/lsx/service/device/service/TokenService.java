package com.lsx.service.device.service;

public interface TokenService {


    public String getToken(String username, String uuid, long exp);

}
