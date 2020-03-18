package com.lsx.service.controller;

import com.alibaba.druid.support.spring.stat.annotation.Stat;
import com.alibaba.fastjson.JSON;
import com.lsx.service.dao.EmailValidRepository;
import com.lsx.service.dao.UserRespository;
import com.lsx.service.entity.EmailValid;
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
import org.springframework.data.domain.Example;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.websocket.server.PathParam;
import java.util.*;


//利用/openapi/** 暴露
@RestController
public class UserController {

    @Autowired
    UserRespository userRespository;


    @Autowired
    private EmailValidRepository emailValidRepository;

    static Logger logger = LoggerFactory.getLogger(UserController.class);


    // FIXME: 2020/3/16 这个方法已经不适用了， 通过oauth 微服务进行登陆 ！！！
    /*
     * 用户登录
     * */
    @GetMapping(value = "/openapi/login")
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



    //这个方法要暴露
    //查询用户是否可注册
    @RequestMapping("/openapi/registerValidUser")
    Result registerValidUser(String username){
        //先校验用户是否存在
        User exampleUser = new User();
        exampleUser.setUsername(username);
        Optional<User> res= userRespository.findOne(Example.of(exampleUser));
        if (res.isPresent()){
            return new Result(true, StatusCode.ERROR, "失败","用户已存在");
        }

        return new Result(true, StatusCode.OK, "成功","用户不存在");
    }




    //注册普通账户
    @RequestMapping("/openapi/register")
    Result register(String username, String password, String email, String validCode,String address, String nickName, String phone, String qq, String wx ){
        User user = new User();




        //先校验用户是否存在
        User exampleUser = new User();
        exampleUser.setUsername(username);
        Optional<User> res= userRespository.findOne(Example.of(exampleUser));
        if (res.isPresent()){
            return new Result(true, StatusCode.ERROR, "注册失败","用户已存在");
        }


        //验证email
        EmailValid example = new EmailValid();
        example.setEmail(email);
        Optional<EmailValid> emailValid = emailValidRepository.findOne(Example.of(example));

        if (emailValid.isPresent()){
            Date cur = new Date();
            if (emailValid.get().getDate().getTime() - cur.getTime() > 1000*3600*24*2){
                return new Result(true, StatusCode.ERROR, "注册失败","验证码过期");
            }

            if (!emailValid.get().getCode().equals(validCode)){
                return new Result(true, StatusCode.ERROR, "注册失败","验证码错误");
            }
        }else{
            return new Result(true, StatusCode.ERROR, "注册失败","请先发送验证邮件");
        }


        logger.info("用户" + username+"邮箱"+email+"验证通过");


        user.setPassword(new BCryptPasswordEncoder().encode(password));
        user.setRole("user");
        user.setUsername(username);
        user.setUuid(UUID.randomUUID().toString());
        user.setMail(email);
        user.setAddress(address);
        user.setName(nickName);
        user.setPhone(phone);
        user.setQq(qq);
        user.setWechat(wx);

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







    //忘记密码  用户名
    @RequestMapping("openapi/forgetPasswordByUsername")
    @Transactional //事务
    public Result forgetPassword(String username, String code, String newPassword){

        User example = new User();
        example.setUsername(username);

        Optional<User> res = userRespository.findOne(Example.of(example));
        if (res.isPresent()){

            String email = res.get().getMail();

            EmailValid emailValid = new EmailValid();
            emailValid.setEmail(email);

            Optional<EmailValid> resEmail = emailValidRepository.findOne(Example.of(emailValid));
            if (resEmail.isPresent()){
                Date now = new Date();
                if (resEmail.get().getDate().getTime() - now.getTime() > 1000*3600*24*2 ){
                    return new Result(true, StatusCode.ERROR, "失败", "验证码过期了");
                }

                if (resEmail.get().getCode().equals(code)){

                    //
                    User newUser = res.get();
                    newUser.setPassword(new BCryptPasswordEncoder().encode(newPassword));

                    userRespository.save(newUser);

                    return new Result(false, StatusCode.OK, "成功");

                }else{
                    return new Result(true, StatusCode.ERROR, "失败", "验证码错误");
                }

            }else{
                return new Result(true, StatusCode.ERROR, "失败", "请先获取验证码");
            }


        }else{
            return new Result(true, StatusCode.ERROR, "失败", "用户不存在");
        }

    }


    //忘记密码  邮箱
    @RequestMapping("openapi/forgetPasswordByEmail")
    @Transactional //事务
    public Result forgetPasswordByEmail(String email, String code, String newPassword) {
        EmailValid emailValid = new EmailValid();
        emailValid.setEmail(email);

        Optional<EmailValid> resEmail = emailValidRepository.findOne(Example.of(emailValid));
        if (resEmail.isPresent()){
            Date now = new Date();
            if (resEmail.get().getDate().getTime() - now.getTime() > 1000*3600*24*2 ){
                return new Result(true, StatusCode.ERROR, "失败", "验证码过期了");
            }

            if (resEmail.get().getCode().equals(code)){

                User exampleUser = new User();
                exampleUser.setMail(email);
                //
                Optional<User> newUser = userRespository.findOne(Example.of(exampleUser));

                if (newUser.isPresent()){

                    User saveUser = newUser.get();
                    saveUser.setPassword(new BCryptPasswordEncoder().encode(newPassword));

                    userRespository.save(saveUser);

                    return new Result(false, StatusCode.OK, "成功");
                }else{
                    return new Result(true, StatusCode.ERROR, "失败", "用户不存在");
                }


            }else{
                return new Result(true, StatusCode.ERROR, "失败", "验证码错误");
            }

        }else{
            return new Result(true, StatusCode.ERROR, "失败", "请先获取验证码");
        }

    }






    //修改密码
    @RequestMapping("/changePassword")
    @Transactional //事务
    public Result changePassword(String newPassword){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getPrincipal().toString();

        User example = new User();
        example.setUsername(username);

        Optional<User> res = userRespository.findOne(Example.of(example));

        if (res.isPresent()){

            User newUser = res.get();
            newUser.setPassword(new BCryptPasswordEncoder().encode(newPassword));

            userRespository.save(newUser);

            return new Result(false, StatusCode.OK, "成功");


        }else{
            return new Result(true, StatusCode.ERROR, "失败", "用户不存在");
        }

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





    //获取用户的权限
    @RequestMapping("/getRole")
//    @PreAuthorize("hasAnyAuthority('user')")
    Result getRole(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getPrincipal().toString();

        User user = userRespository.findByUsername(username);

        if (user == null)
            return new Result(true, StatusCode.ERROR, "失败", "用户不存在");



        String res[] =  user.getRole().split(",");



        return new Result(true, StatusCode.OK, "成功", res);


    }
}
