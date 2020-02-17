package com.lsx.oauth.controller;

import com.lsx.oauth.service.AuthService;
import com.lsx.oauth.util.AuthToken;
import com.lsx.oauth.util.CookieUtil;
import entity.Result;
import entity.StatusCode;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.management.RuntimeErrorException;
import javax.servlet.http.HttpServletResponse;


@Controller
@RequestMapping("/oauth")
public class AuthController {


    Logger logger = LoggerFactory.getLogger(AuthController.class);


    @Autowired
    private AuthService authService;

    @Value("${auth.clientId}")
    private String clientId;

    @Value("${auth.clientSecret}")
    private String clientSecret;

    @Value("${auth.cookieDomain}")
    private String cookieDomain;

    @Value("${auth.cookieMaxAge}")
    private int cookieMaxAge;

    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public Result login(String username, String password, HttpServletResponse response) throws RuntimeErrorException {
        System.out.println("Result login(String username, String password, HttpServletResponse response) throws RuntimeErrorException");
        //校验参数
        if (StringUtils.isEmpty(username)){
            throw new RuntimeException("请输入用户名");
        }
        if(StringUtils.isEmpty(password)){
            throw new RuntimeException("请输入密码");
        }


        System.out.println("开始申请令牌");
        //申请令牌
        AuthToken authToken = authService.login(username, password, clientId, clientSecret);

        System.out.println("申请令牌完毕");

        //将jti的值存入cookie中
        this.saveJtiToCookie(authToken.getJti(), response);

        //返回结果
        return new Result(true, StatusCode.OK, "登陆成功", authToken.getJti());
    }

    //将令牌的断标志jti存入到cookie中
    private void saveJtiToCookie(String jti, HttpServletResponse response){
        CookieUtil.addCookie(response, cookieDomain, "/", "uid", jti, cookieMaxAge, false);
    }

}
