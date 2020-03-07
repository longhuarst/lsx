package com.lsx.service.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Test {


    @RequestMapping("/test")
    @ResponseBody
    public String Test(){
        return "test";
    }


    @RequestMapping("/test/role/role_user")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String TestRoleRoleUser(){
        return "你拥有 USER 的权限";
    }


    @RequestMapping("/test/role/user")
    @PreAuthorize("hasAuthority('user')")
    public String TestRoleUser(){
        return "你拥有 USER 的权限";
    }


    @RequestMapping("/test/role/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String TestRoleAdmin(){
        return "你拥有 ADMIN 的权限";
    }



    //请求的时候  token 一定要放在 header 里 否则  无法成功

    @RequestMapping("/test/role/role_admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String TestRoleRoleAdmin(){
        return "你拥有 ADMIN 的权限";
    }


}
