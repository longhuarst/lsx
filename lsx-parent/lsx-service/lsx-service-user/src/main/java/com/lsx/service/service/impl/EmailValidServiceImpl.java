package com.lsx.service.service.impl;

import com.lsx.service.email.EmailBean;
import com.lsx.service.service.EmailValidService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class EmailValidServiceImpl implements EmailValidService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void add(String email, String code){

        EmailBean emailBean = new EmailBean();
        emailBean.setSubject("lsxweb-邮箱验证码");
        emailBean.setRecipient(email);
        emailBean.setContent("感谢使用我们的注册服务， 本次验证码为： "+code+" , 2小时后失效");


        rabbitTemplate.convertAndSend("validEmailRequestQueue",  emailBean);

    }
}
