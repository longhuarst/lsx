package com.lsx.oauth.controller;

import com.lsx.oauth.service.AuthService;
import com.lsx.oauth.util.AuthToken;
import com.lsx.oauth.util.CookieUtil;
import entity.Result;
import entity.StatusCode;
import org.apache.commons.lang.StringUtils;
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
        //校验参数
        if (StringUtils.isEmpty(username)){
            throw new RuntimeException("请输入用户名");
        }
        if(StringUtils.isEmpty(password)){
            throw new RuntimeException("请输入密码");
        }

        //申请令牌
        AuthToken authToken = authService.login(username, password, clientId, clientSecret);

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