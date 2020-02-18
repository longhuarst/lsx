package com.lsx.oauth.service.Impl;

import com.lsx.oauth.service.UserLoginService;
import com.lsx.oauth.util.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Map;


@Service
public class UserLoginServiceImpl implements UserLoginService {


    //实现请求登陆
    @Autowired
    private RestTemplate restTemplate;


    @Autowired
    private LoadBalancerClient loadBalancerClient;




    //   用户登陆  -->   用用户名和 密码 获取令牌  若成功则返回令牌  失败则 提示失败，
    //  之后用户 携带token 进行服务访问。
    @Override
    public AuthToken login(String username, String password, String clientId, String clientSecret, String grantType) throws Exception {


        System.out.println("AuthToken login(String username, String password, String clientId, String clientSecret, String grantType) throws Exception");

        //获取指定服务的微服务地址
        ServiceInstance serviceInstance = loadBalancerClient.choose("lsx-user-oauth");


        //请求调用的地址
        String url = serviceInstance.getUri()+"/oauth/token";

        //请求的提交数据封装
        MultiValueMap<String, String> parameterMap = new LinkedMultiValueMap();
        parameterMap.add("username",username);
        parameterMap.add("password", password);
        parameterMap.add("grant_type", grantType);

        //请求头封装
        String Authorization = "Basic " + new String(Base64.getEncoder().encode((clientId + ":" + clientSecret).getBytes()), "UTF-8");

        MultiValueMap<String, String> headerMap = new LinkedMultiValueMap();
        headerMap.add("Authorization", Authorization);

        //HttpEntity -> 创建该对象
        HttpEntity httpEntity = new HttpEntity(parameterMap, headerMap);


        //1.请求的地址
        //2.提交方式
        //3.requestEntity 请求提交的数据信息封装 请求体｜请求头
        //4.responseType 返回数据需要转换的类型
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Map.class);

        System.out.println(response.getBody());

        //用户登录后的令牌信息
        Map<String, String> map = response.getBody();

        //将令牌信息转换为AuthToken对象
        AuthToken authToken = new AuthToken();
        authToken.setAccessToken(map.get("access_token"));
        authToken.setRefreshToken(map.get("refresh_token"));
        authToken.setJti(map.get("jti"));

        return authToken;

    }
}

