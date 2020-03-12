package com.lsx.service.chat.controller;


import com.alibaba.fastjson.JSONObject;
import com.lsx.service.chat.entity.FriendList;
import com.lsx.service.chat.entity.Message;
import com.lsx.service.chat.respository.FriendListRepository;
import com.lsx.service.chat.respository.MessageRepository;
import entity.Result;

import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;


@RestController
@RequestMapping("/chat")
public class ChatController {

    //
    @Autowired
    FriendListRepository friendListRepository;


    @Autowired
    MessageRepository messageRepository;



    @RequestMapping("/getFriendList")
    public Result getFriendList(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getPrincipal().toString();

        FriendList friendList = null;

        //查询数据库
        try {
            friendList = friendListRepository.findByUsername(username);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(true, StatusCode.ERROR, "失败", e.getMessage());
        }

        return new Result(true, StatusCode.OK, "成功", friendList.getFrinendList());
    }



    @RequestMapping("/addFriend")
    @Transactional //事务控制
    Result addFriend(String friendName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getPrincipal().toString();

        FriendList friendList = null;

        //获取好友名单
        try {
            friendList = friendListRepository.findByUsername(username);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, StatusCode.ERROR, "失败", e.getMessage());
        }


        List<String> list = (List<String>) friendList.getFrinendList().get("list");
        list.add(friendName);
        JSONObject json = new JSONObject();

        json.put("list", list);
        friendList.setFrinendList(json);


        try {
            friendListRepository.save(friendList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, StatusCode.ERROR, "失败", e.getMessage());
        }


        try {
            friendList = friendListRepository.findByUsername(friendName);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, StatusCode.ERROR, "失败", e.getMessage());
        }

        list = (List<String>) friendList.getFrinendList().get("list");
        list.add(username);

        json = new JSONObject();

        json.put("list", list);
        friendList.setFrinendList(json);

        try {
            friendListRepository.save(friendList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, StatusCode.ERROR, "失败", e.getMessage());
        }



        return new Result(false, StatusCode.OK, "成功");
    }


        @RequestMapping("/removeFriend")
        @Transactional //事务控制
        Result removeFriend(String friendName){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getPrincipal().toString();

            FriendList friendList = null;

            //获取好友名单
            try {
                friendList = friendListRepository.findByUsername(username);
            }catch (Exception e){
                e.printStackTrace();
                return new Result(true, StatusCode.ERROR, "失败", e.getMessage());
            }



            List<String> list = (List<String>) friendList.getFrinendList().get("list");
            list.remove(friendName);
            JSONObject json = new JSONObject();

            json.put("list", list);
            friendList.setFrinendList(json);


            try {
                friendListRepository.save(friendList);
            }catch (Exception e){
                e.printStackTrace();
                return new Result(true, StatusCode.ERROR, "失败", e.getMessage());
            }


            try {
                friendList = friendListRepository.findByUsername(friendName);
            }catch (Exception e){
                e.printStackTrace();
                return new Result(true, StatusCode.ERROR, "失败", e.getMessage());
            }

            list = (List<String>) friendList.getFrinendList().get("list");
            list.remove(username);

            json = new JSONObject();

            json.put("list", list);
            friendList.setFrinendList(json);

            try {
                friendListRepository.save(friendList);
            }catch (Exception e){
                e.printStackTrace();
                return new Result(true, StatusCode.ERROR, "失败", e.getMessage());
            }


            return new Result(false, StatusCode.OK, "成功");


//        friendList.getFrinendList().


    }





    //发送消息
    @RequestMapping("/sendMessage")
    Result sendMessage(String to, String message){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String from = authentication.getPrincipal().toString();

        Message message1 = new Message();
        message1.setFrom(from);
        message1.setTo(to);
        message1.setChecked(false);
        message1.setMessage(message);

        try {
            messageRepository.save(message1);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(true, StatusCode.ERROR, "失败", e.getMessage());
        }

        return new Result(false, StatusCode.OK, "成功");
    }


    //接收未读消息
    @RequestMapping("/readMessage")
    Result readMessage(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String to = authentication.getPrincipal().toString();

        List<Message> messageList;

        Message example = new Message();
        example.setTo(to);
        example.setChecked(false);//未读

        try{
            messageList = messageRepository.findAll(Example.of(example));
        }catch (Exception e){
            e.printStackTrace();
            return new Result(true, StatusCode.ERROR, "失败", e.getMessage());
        }

        return new Result(true, StatusCode.OK, "成功", messageList);

    }



    //确认已读消息
    @RequestMapping("setCheckedMessage")
    Result setCheckedMessage(List<Integer> idList){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getPrincipal().toString();

        for (int i=0; i<idList.size(); ++i){
            Message message = new Message();
            message.setId(idList.get(i));
            message.setChecked(true);
            messageRepository.save(message);
        }

        return new Result(false, StatusCode.OK, "成功");
    }





















}
