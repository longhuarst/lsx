package com.lsx.service.controller;

import com.alibaba.druid.support.spring.stat.annotation.Stat;
import com.alibaba.fastjson.JSON;
import com.lsx.service.dao.UserRespository;
import com.lsx.service.entity.User;
import com.lsx.service.service.UserService;
import entity.BCrypt;
import entity.JwtUtil;
import entity.Result;
import entity.StatusCode;
import org.omg.PortableInterceptor.USER_EXCEPTION;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
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


    static Logger logger = LoggerFactory.getLogger(UserController.class);


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


    @RequestMapping("/register")
    Result register(String username, String password, String email, String validCode){
        User user = new User();



        //验证email




        user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.setRole("user");
        user.setUsername(username);
        user.setUuid(UUID.randomUUID().toString());
        user.setMail(email);

        User resUser = null;
        try{
            resUser = userRespository.save(user);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "注册失败");
        }

        return new Result(true, StatusCode.OK, "注册成功", resUser);

    }

    @RequestMapping(value = "/addUser")
    public Result addUser(String username, String password){
        User user = new User();

        //暂时不使用加密
        //使用加密
//        System.out.println();
        user.setPassword(new BCryptPasswordEncoder().encode(password).toString());
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



    //创建管理员账户
    @RequestMapping(value = "/createAdmin")
//    @RolesAllowed(value = "admin")
//    @PreAuthorize("ROLE_USER")
    @PreAuthorize("hasAuthority('admin')")
    public Result createAdmin(String username, String password){
        User user = new User();

        //暂时不使用加密
        //使用加密
//        System.out.println();
        user.setPassword(new BCryptPasswordEncoder().encode(password).toString());
        user.setUsername(username);

        String uuid = UUID.randomUUID().toString();

        user.setUuid(uuid);

        user.setRole("admin");


        User resUser = null;
        try{
            resUser = userRespository.save(user);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "注册失败");
        }

        return new Result(true, StatusCode.OK, "注册成功", resUser);

    }





    //获取头像连接
    @RequestMapping("/getProfilePicUrlByUsername")
    @PreAuthorize("hasAnyAuthority('user', 'admin')")
    public Result getProfilePicUrlByUsername(String username){
        User user = userRespository.findByUsername(username);

        if (user != null){
            return new Result(true, StatusCode.OK, "成功", user.getProfile_pic_url());
        }

        return new Result(false, StatusCode.ERROR, "失败");

    }


    //更新头像
    @RequestMapping("/updateProfilePicUrl")
    @PreAuthorize("hasAnyAuthority('user', 'admin')")
    public Result updateProfilePicUrl(String username, String url){


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String principal = authentication.getPrincipal().toString();
        logger.info("principal = "+principal);


        logger.info("username = "+username + " 更新头像");

        //检查用户名和 token中的用户名是否一致
        if (!username.equals(principal)){
            return new Result(false, StatusCode.ERROR, "token dismatched!");
        }

        User user = userRespository.findByUsername(username);
        String oldUrl = user.getProfile_pic_url();
        //删除旧头像
        //FixMe  暂时不删除
        user.setProfile_pic_url(url);

        User newUser = userRespository.save(user);

        return new Result(true, StatusCode.OK, "更新成功", newUser);
    }



    @PostMapping("/findByUsername/{username}")
    public Result<User> findByUsername(@PathVariable("username") String username){
        //调用UserService实现根据主键查询User
        System.out.println("feign ----> start ");
        System.out.println("username = "+ username);
        User user = userRespository.findByUsername(username);
        if (user == null){
            System.out.println("result user == null");
        }
        System.out.println("feign ----> end ");
        return new Result<>(true, StatusCode.OK, "查询成功", user);
    }




    @Autowired
    UserService userService;



    //mybatis
    @RequestMapping("findByUsernameMapper")
    public Result<User> findByUsernameMapper(String username){
        User user = userService.findByUsername(username);
        if (user == null){
            return new Result<>(false, StatusCode.ERROR, "无");

        }

        return new Result<>(true, StatusCode.OK, "成功", user);
    }
}
