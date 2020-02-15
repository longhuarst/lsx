package com.lsx.service.controller;

import com.alibaba.druid.support.spring.stat.annotation.Stat;
import com.alibaba.fastjson.JSON;
import com.lsx.service.dao.UserRespository;
import com.lsx.service.entity.User;
import entity.BCrypt;
import entity.JwtUtil;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    UserRespository userRespository;

    /*
     * 用户登录
     * */
    @GetMapping(value = "/login")
    public Result login(@PathParam("username") String username, @PathParam("password") String password, HttpServletResponse response){

        //查询用户信息
        User user = userRespository.findByUsername(username);

        //对比密码  【加密对比】  暂时不使用加密
//        if (user != null && BCrypt.checkpw(password,user.getPassword())){
        if (user != null && user.getPassword().equals(password)) {

            //创建用户令牌信息
            Map<String,Object> tokenMap = new HashMap<>();
            tokenMap.put("role", user.getRole().toString());
            tokenMap.put("success","SUCCESS");
            tokenMap.put("username",username);
            //密码不能放入
            String token = JwtUtil.createJWT(UUID.randomUUID().toString(),
                    JSON.toJSONString(tokenMap),
                    null);//默认一小时

            //把令牌存入到cookie
            Cookie cookie = new Cookie("Authorization",token);
            cookie.setDomain("localhost");
            cookie.setPath("/");
            response.addCookie(cookie);//添加cookie



            //把令牌作为参数该给用户
            //[未实现]


            //密码匹配，登陆成功
            return new Result(true, StatusCode.OK, "登陆成功了", token);

        }

        //密码匹配失败，登陆失败，账号或者密码错误
        return new Result(false, StatusCode.LOGINERROR,"账号或者密码错误");
    }


    @RequestMapping(value = "/addUser")
    public Result addUser(String username, String password){
        User user = new User();

        //暂时不使用加密
        user.setPassword(password);
        user.setUsername(username);

        String uuid = UUID.randomUUID().toString();

        user.setUuid(uuid);

        user.setRole("user");


        User resUser = null;
        try{
            resUser = userRespository.save(user);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "注册失败");
        }

        return new Result(true, StatusCode.OK, "注册成功", resUser);

    }



}
