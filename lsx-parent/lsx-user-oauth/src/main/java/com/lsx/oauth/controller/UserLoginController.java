package com.lsx.oauth.controller;


import com.lsx.oauth.service.UserLoginService;
import com.lsx.oauth.util.AuthToken;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class UserLoginController {



    //客户端id
    @Value("${auth.clientId}")
    private String clientId;

    //客户端密钥
    @Value("${auth.clientSecret}")
    private String clientSecret;


    @Autowired
    private UserLoginService userLoginService;



    /*
    * 登陆方法
    *
    * */
    @RequestMapping(value = "/login")
    public Result login(String username, String password) {


        //授权模式
        String grantType = "password";
        AuthToken authToken = null;

        try {
            authToken = userLoginService.login(username, password, clientId, clientSecret, grantType);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            return new Result(false, StatusCode.LOGINERROR, "登陆失败 : "+e.getMessage());
        }

        if (authToken != null){
            return new Result(true, StatusCode.OK, "登陆成功", authToken);
        }


        return new Result(false, StatusCode.LOGINERROR, "登陆失败");
    }



}
