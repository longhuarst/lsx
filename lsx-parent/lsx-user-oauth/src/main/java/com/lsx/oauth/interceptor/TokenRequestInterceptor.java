package com.lsx.oauth.interceptor;

import com.lsx.oauth.util.AdminToken;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;


@Configuration
public class TokenRequestInterceptor implements RequestInterceptor {

    @Autowired
    private AdminToken adminToken;

    /*
    * feign 调用之前执行
    * */
    @Override
    public void apply(RequestTemplate template) {
        /*
         *   从数据库中查询用户信息
         *   1。 没有令牌， Feign 调用之前 生成令牌
         *   2。 Feign 调用之前， 令牌需要携带过去
         *   3。 Feign 调用之前， 令牌需要存放到Header文件中
         *   4。 请求 -> Feign 调用 -> 拦截器 RequestInterceptor  -> Feign 调用之前拦截
         **/
//        AdminToken adminToken = new AdminToken();

        String token = adminToken.adminToken();
        template.header("Authorization", "bearer "+token);

        System.out.println("-------------> apply interceptor token");

    }
}
