package com.lsx.service.device.service.impl;


import com.lsx.service.device.service.DeviceService;
import com.lsx.service.device.util.AuthToken;
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

import javax.xml.ws.Response;
import java.util.Base64;
import java.util.Map;

@Service
public class DeviceServiceImpl implements DeviceService {


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LoadBalancerClient loadBalancerClient;




    @Override
    public AuthToken getToken(String uuid) throws Exception {


        //从负载均衡获取
        ServiceInstance serviceInstance = loadBalancerClient.choose("lsx-user-oauth");

        if (serviceInstance == null){
            throw new Exception("无法连接到负载均衡服务器");
        }

        String url = serviceInstance.getUri() + "/oauth/token";

        MultiValueMap<String, String> parameterMap = new LinkedMultiValueMap<>();

        parameterMap.add("username", "DeviceManager");
        parameterMap.add("password", "testpasswordx");
        parameterMap.add("grant_type", "password");

        String Authorization = "Basic "+ new String(Base64.getEncoder().encode(("lsx"+":"+"lsxlsx").getBytes()), "UTF-8");

        MultiValueMap<String, String> headerMap = new LinkedMultiValueMap<>();
        headerMap.add("Authorization", Authorization);

        HttpEntity httpEntity = new HttpEntity(parameterMap, headerMap);

        ResponseEntity<Map> response = null;

        try {
            response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Map.class);
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }

        Map<String, String> map = response.getBody();


        //将令牌信息转换为AuthToken对象
        AuthToken authToken = new AuthToken();
        authToken.setAccessToken(map.get("access_token"));
        authToken.setRefreshToken(map.get("refresh_token"));
        authToken.setJti(map.get("jti"));

        return authToken;
    }
}
