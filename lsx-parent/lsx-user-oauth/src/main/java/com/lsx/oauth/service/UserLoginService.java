package com.lsx.oauth.service;

import com.lsx.oauth.util.AuthToken;

public interface UserLoginService {


    /*
    * 登陆操作
    * */
    AuthToken login(String username, String password, String clientId, String clientSecret, String grantType) throws Exception;
}
