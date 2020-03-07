package com.lsx.service.device.service;

import com.lsx.service.device.util.AuthToken;

public interface DeviceService {


    AuthToken getToken(String uuid) throws Exception;

}
