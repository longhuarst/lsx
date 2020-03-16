package com.lsx.service.controller;


import com.lsx.service.dao.EmailValidRepository;
import com.lsx.service.dao.UserRespository;
import com.lsx.service.entity.EmailValid;
import com.lsx.service.entity.User;
import com.lsx.service.service.EmailValidService;
import com.lsx.service.tools.ValidCodeGenerator;
import entity.Result;
import entity.StatusCode;
import org.apache.tomcat.util.http.parser.Authorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.plugin.liveconnect.SecurityContextHelper;

import java.util.Date;
import java.util.Optional;

//验证码
@RestController
@RequestMapping("/openapi/validCode")
public class ValidCodeController {


    Logger logger = LoggerFactory.getLogger(ValidCodeController.class);

    @Autowired
    EmailValidRepository emailValidRepository;

    @Autowired
    UserRespository userRespository;

    @Autowired
    private EmailValidService emailValidService;



    //这个方法需要暴露出去  不需要鉴权
    @RequestMapping("/getByEmail")
    public Result getByEmail(String email){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getPrincipal().toString();

        logger.info("email = " +email );

        if(!email.matches("^\\w+@(\\w+\\.)+\\w+$")) {
            return new Result(true, StatusCode.ERROR, "失败", "邮箱地址不正确");
        }

        User user = null;

        try {
            user = userRespository.findByMail(email);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(true, StatusCode.ERROR, "失败", e.getMessage());
        }


        String code = ValidCodeGenerator.generate();

        EmailValid emailValid = new EmailValid();
        emailValid.setEmail(email);
        emailValid.setCode(code);


        //将邮件发送 加入 消息队列  异步发送

        emailValidService.add(email, code);




        try {

            EmailValid example = new EmailValid();
            example.setEmail(email);


            Optional<EmailValid> res = emailValidRepository.findOne(Example.of(example));


            if (res.isPresent()){
                logger.info("找到目标");
                emailValid.setId(res.get().getId());
                emailValid.setDate(new Date());
                logger.info("更新");

            }else{
                logger.info("未找到目标");
            }





            emailValidRepository.save(emailValid);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(true, StatusCode.ERROR, "失败", e.getMessage());
        }

        return new Result(true, StatusCode.OK, "成功", code);

    }




    //这个方法需要暴露出去  不需要鉴权
    @RequestMapping("/getByUsername")
    public Result getByUsername(String username) {
        User exampleUser = new User();

        Optional<User> res = userRespository.findOne(Example.of(exampleUser));

        if (res.isPresent()){
            String email = res.get().getMail();
            return getByEmail(email);
        }

        return new Result(true, StatusCode.ERROR, "失败", "用户不存在");
    }


}
