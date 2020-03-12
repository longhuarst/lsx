package com.lsx.service.chat.controller;


import com.lsx.service.chat.entity.FriendAdd;
import com.lsx.service.chat.entity.Notify;
import com.lsx.service.chat.respository.FriendAddRepository;
import com.lsx.service.chat.respository.NotifyRepository;
import com.lsx.service.chat.service.FriendAddService;
import entity.Result;
import entity.StatusCode;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("friendAdd")
public class FriendAddController {

    //好友添加
    @Autowired
    private FriendAddRepository friendAddRepository;


    @Autowired
    private NotifyRepository notifyRepository;

    @Autowired
    private FriendAddService friendAddService;


    //发送添加
    @RequestMapping("/sendFriendAdd")
    Result sendFriendAdd(String to,String message){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String from = authentication.getPrincipal().toString();

        FriendAdd friendAdd = new FriendAdd();
        friendAdd.setChecked(false);
        friendAdd.setFrom(from);
        friendAdd.setMessage(message);
        friendAdd.setTo(to);

        try {
            friendAddRepository.save(friendAdd);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(true, StatusCode.ERROR, "失败", e.getMessage());
        }

        return new Result(false, StatusCode.OK, "成功");

    }


    //读取添加
    @RequestMapping("/readFriendAdd")
    Result readFriendAdd(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getPrincipal().toString();

        FriendAdd example = new FriendAdd();
        example.setTo(username);
        example.setChecked(false);

        List<FriendAdd> addList = null;

        try{
            addList = friendAddRepository.findAll(Example.of(example));
        }catch (Exception e){
            e.printStackTrace();
            return new Result(true, StatusCode.ERROR, "失败", e.getMessage());
        }

        return new Result(true, StatusCode.OK, "成功", addList);
    }


    //确认
    @RequestMapping("/confirm")
    @Transactional
    Result confirm(Integer id, Boolean accept, String message){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String from = authentication.getPrincipal().toString();

        FriendAdd example = new FriendAdd();
        FriendAdd this_ = null;
        //取出相关的信息  根据id
        try{
            example.setTo(from);
            example.setId(id);
            this_ = friendAddRepository.findAll(Example.of(example)).get(0);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(true, StatusCode.ERROR, "失败", e.getMessage());
        }

        //确认已读
        try{
            FriendAdd friendAdd = new FriendAdd();
            friendAdd.setChecked(true);
            friendAdd.setTo(from);
            friendAddRepository.save(friendAdd);//确认信息
        }catch (Exception e){
            e.printStackTrace();
            return new Result(true, StatusCode.ERROR, "失败", e.getMessage());
        }



        if (accept){
            //接受
            //建立关系

            try {
                friendAddService.setRelationship(from, this_.getFrom());
            }catch (Exception e){
                e.printStackTrace();

                return new Result(true, StatusCode.ERROR, "失败", e.getMessage());
            }



        }else{
            //拒绝
            //发送通知， 携带消息
            try{
                //发送通知
                Notify notify = new Notify();
                notify.setChecked(false);
                notify.setFrom(from);
                notify.setTo(this_.getFrom());
                notify.setMessage(message);

                notifyRepository.save(notify);

            }catch (Exception e){
                e.printStackTrace();
                return new Result(true, StatusCode.ERROR, "失败", e.getMessage());
            }
        }

        return new Result(false, StatusCode.OK, "成功");
    }






}

