package com.lsx.service.service;


import com.lsx.service.entity.User;
import com.lsx.service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    @Autowired
    UserMapper userMapper;

    public User findByUsername(String username){
        return userMapper.findByUsername(username);
    }


}
