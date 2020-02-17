package com.lsx.oauth.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
public class TokenConfig {


    //令牌的存储策略
    @Bean
    public TokenStore tokenStore(){
        //内存方式，生成普通令牌
        return new InMemoryTokenStore();
    }



    //使用 jwt 令牌
//    @Bean
//    @Autowired
//    public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter){
//        return new JwtTokenStore(jwtAccessTokenConverter);
//    }




}
