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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import sun.plugin.liveconnect.SecurityContextHelper;

//验证码
@RequestMapping("/valid")
public class ValidCodeController {

    @Autowired
    EmailValidRepository emailValidRepository;

    @Autowired
    UserRespository userRespository;

    @Autowired
    private EmailValidService emailValidService;

    //这个方法需要暴露出去  不需要鉴权
    @RequestMapping("/get")
    public Result get(String email){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getPrincipal().toString();

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

            emailValidRepository.save(emailValid);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(true, StatusCode.ERROR, "失败", e.getMessage());
        }

        return new Result(true, StatusCode.OK, "成功", code);

    }


}
