//package com.lsx.web.configure;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.web.cors.CorsUtils;
//import org.springframework.web.server.ServerWebExchange;
//import org.springframework.web.server.WebFilter;
//import org.springframework.web.server.WebFilterChain;
//import reactor.core.publisher.Mono;
//
//
//@Configuration
//public class CorsWebFilter implements WebFilter {
//
//    private static final String MAX_AGE = "18000L";
//
//    private static final String ALL = "*";
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
//        //return null;
//        ServerHttpRequest request = exchange.getRequest();
//        String path = request.getPath().value();
//        ServerHttpResponse response = exchange.getResponse();
//
////        if ("/favicon,ico".equals(path)){
////            response.setStatusCode(HttpStatus.OK);
////
////            return
////        }
//
//        if (!CorsUtils.isCorsRequest(request)) {
//
//            return chain.filter(ctx);
//
//        }
//
//
//
//
//    }
//}
