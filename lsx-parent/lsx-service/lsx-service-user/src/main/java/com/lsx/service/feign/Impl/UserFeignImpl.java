//package com.lsx.service.feign.Impl;
//
//import com.lsx.service.dao.UserRespository;
//import com.lsx.service.entity.User;
//import com.lsx.service.feign.UserFeign;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//
//@Component
//public class UserFeignImpl implements UserFeign {
//
//    @Autowired
//    private UserRespository userRespository;
//
//
//
//    @Override
//    public User findUserInfo(String username) {
//        return userRespository.findByUsername(username);
//    }
//}
