package com.lsx.service.chat.controller;


import com.lsx.service.chat.entity.FriendList;
import com.lsx.service.chat.respository.FriendListRespository;
import entity.Result;

import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/chat")
public class ChatController {

    //
    @Autowired
    FriendListRespository friendListRespository;



    @RequestMapping("/getFriendList")
    public Result getFriendList(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getPrincipal().toString();

        FriendList friendList = null;

        //查询数据库
        try {
            friendList = friendListRespository.findByUsername(username);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(true, StatusCode.ERROR, "失败", e.getMessage());
        }

        return new Result(true, StatusCode.OK, "成功", friendList.getFrinendList());
    }



    @RequestMapping("/addFriend")
    Result addFriend(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getPrincipal().toString();

        FriendList friendList = null;

        //获取好友名单
        try {
            friendList = friendListRespository.findByUsername(username);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(true, StatusCode.ERROR, "失败", e.getMessage());
        }


//        friendList.getFrinendList().






    }


}
