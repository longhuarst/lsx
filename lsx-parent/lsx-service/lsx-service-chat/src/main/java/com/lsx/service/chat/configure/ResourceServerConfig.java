package com.lsx.service.chat.configure;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Configuration
@EnableResourceServer   //开启资源校验服务，-》令牌校验
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true) //激活方法上的PreAuthorize注解
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {


    //公钥
    private static final String PUBLIC_KEY = "public.key";


    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    /*
    * 定义jwtTokenStore
    * */
    @Bean
    public TokenStore tokenStore(){

        return new JwtTokenStore(jwtAccessTokenConverter);
    }


    /*
    * 令牌转换器
    * */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        String publicKey = getPublicKey();
        jwtAccessTokenConverter.setVerifierKey(publicKey);
//        jwtAccessTokenConverter.setVerifierKey(getPublicKey());
//        jwtAccessTokenConverter.setVerifier();
//        jwtAccessTokenConverter.setAccessTokenConverter(setAccessTokenConverter);
        return jwtAccessTokenConverter;
    }


    /*
    * 获取非对称加密公钥 Key
    * */
    private String getPublicKey(){
        ClassPathResource classPathResource = new ClassPathResource(PUBLIC_KEY);

        try {
            System.out.println(classPathResource.contentLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(classPathResource.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            return bufferedReader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }


    /*
    *
    * 资源服务器 安全配置
    *
    * */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        //super.configure(resources);

        //设置令牌的存储方式
        resources.tokenStore(tokenStore());
    }


    /*
    * Http安全配置，对每个到达系统的http请求连接都进行校验
    * */

    @Override
    public void configure(HttpSecurity http) throws Exception {
        //super.configure(http);

        //所有请求都必须通过认证
        http.authorizeRequests()
                //这些放行
                .antMatchers("/test").permitAll()
                .antMatchers("/chat/**").permitAll()
//                .antMatchers("/findByUsername/{username}")
//                .anyRequest().permitAll();//其他都要认证才能访问
                .anyRequest().authenticated();
    }
}
