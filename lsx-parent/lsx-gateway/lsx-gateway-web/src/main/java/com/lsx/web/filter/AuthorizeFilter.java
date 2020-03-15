package com.lsx.web.filter;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;

import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/*
* 全局过滤器
* 实现用户权限鉴别
* */


@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {

    //令牌的名字
    private static final String AUTHORIZE_TOKEN = "Authorization";


    static Logger logger = LoggerFactory.getLogger(AuthorizeFilter.class);
    static boolean debug = logger.isDebugEnabled();

    /*
    * 全局拦截
    * */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();


        System.out.println("path: = " + request.getPath());

        String path = request.getPath().toString();

        if (path.equals("/oauth/user/login") || path.equals("/device/upload") || path.startsWith("/fastdfs") || path.equals("/test")){
            //登陆放行

            logger.info("登陆 放行");
            return chain.filter(exchange);
        }



        //获取用户令牌信息，
        // 1. 头文件中
        String token = request.getHeaders().getFirst(AUTHORIZE_TOKEN);
        //boolean true: 令牌在头文件中   false: 令牌不在头文件中  -> 将令牌封装到头文件中，再传递给其他微服务
        boolean hasToken = true;


        // 2。参数获取令牌
        if (StringUtils.isEmpty(token)){
            token = request.getQueryParams().getFirst(AUTHORIZE_TOKEN);
            hasToken = false;
        }

        // 3。Cookie中
        if (StringUtils.isEmpty(token)){
            HttpCookie httpCookie = request.getCookies().getFirst(AUTHORIZE_TOKEN);
            if (httpCookie != null){
                token = httpCookie.getValue();
            }
        }


        // 如果没有令牌，则拦截
        if (StringUtils.isEmpty(token)){
            //设置没有权限的状态吗 401
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            //响应空数据
            return response.setComplete();
        }

//        //如果有令牌，则校验令牌是否有效
//        try {
//            JwtUtil.parseJWT(token);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            //无效拦截
//            //设置没有权限的状态吗 401
//            response.setStatusCode(HttpStatus.UNAUTHORIZED);
//            //响应空数据
//            return response.setComplete();
//        }

        //如果令牌为空 则 不允许访问 直接拦截
        if (StringUtils.isEmpty(token)){
            //设置没有权限的状态吗 401
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            //响应空数据
            return response.setComplete();
        }else{
            //如果当前令牌没有 bearer前缀，则添加
            if (!token.startsWith("bearer") && !token.startsWith("BEARER")){
                token = "bearer "+ token;
            }
        }

        if (!hasToken){
            //如果请求头没有  则加入
            //将令牌封装到头文件中
            request.mutate().header(AUTHORIZE_TOKEN,token);
        }





        logger.info("放行");


        //放行

        return chain.filter(exchange);

    }

    @Override
    public int getOrder() {
        return 0;
    }
}
