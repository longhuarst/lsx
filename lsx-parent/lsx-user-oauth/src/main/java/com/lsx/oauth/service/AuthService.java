package com.lsx.oauth.service;

import com.lsx.oauth.util.AuthToken;

public interface AuthService {
    AuthToken login(String username, String password, String clientId, String clientSecret);
}
