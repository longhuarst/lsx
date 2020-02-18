package com.lsx.service.configure;




import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Configuration
@EnableResourceServer   //开启资源校验服务，-》令牌校验
@EnableGlobalMethodSecurity(prePostEnabled = true)//, securedEnabled = true) //激活方法上的PreAuthorize注解
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {


    //公钥
    private static final String PUBLIC_KEY = "public.key";


//    @Autowired
//    private JwtAccessTokenConverter jwtAccessTokenConverter;

    /*
    * 定义jwtTokenStore
    * */
    @Bean
    public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter){
        return new JwtTokenStore(jwtAccessTokenConverter);
    }


    /*
    * 令牌转换器
    * */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setVerifierKey(getPublicKey());
//        jwtAccessTokenConverter.setVerifier();
        return jwtAccessTokenConverter;
    }


    /*
    * 获取非对称加密公钥 Key
    * */
    private String getPublicKey(){
        Resource resource = new ClassPathResource(PUBLIC_KEY);


        try {
            InputStreamReader inputStreamReader = new InputStreamReader(resource.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String res =  bufferedReader.lines().collect(Collectors.joining("\n"));
            System.out.println(res);
            return res;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

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
                .anyRequest().authenticated();//其他都要认证才能访问

    }
}
