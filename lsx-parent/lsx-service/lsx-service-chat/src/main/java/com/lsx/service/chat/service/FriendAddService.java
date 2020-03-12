package com.lsx.service.chat.service;


import com.alibaba.fastjson.JSONObject;
import com.lsx.service.chat.entity.FriendList;
import com.lsx.service.chat.respository.FriendListRepository;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendAddService {


    @Autowired
    private FriendListRepository friendListRepository;


    public void setRelationship(String username, String friendName) throws Exception {
        FriendList friendList = null;

        //获取好友名单
        friendList = friendListRepository.findByUsername(username);


        List<String> list = (List<String>) friendList.getFrinendList().get("list");
        list.add(friendName);
        JSONObject json = new JSONObject();

        json.put("list", list);
        friendList.setFrinendList(json);


        friendListRepository.save(friendList);


        friendList = friendListRepository.findByUsername(friendName);


        list = (List<String>) friendList.getFrinendList().get("list");
        list.add(username);

        json = new JSONObject();

        json.put("list", list);
        friendList.setFrinendList(json);

        friendListRepository.save(friendList);
    }


    public void removeRelationship(String username, String friendName) throws Exception {
        FriendList friendList = null;

        //获取好友名单

        friendList = friendListRepository.findByUsername(username);


        List<String> list = (List<String>) friendList.getFrinendList().get("list");
        list.remove(friendName);
        JSONObject json = new JSONObject();

        json.put("list", list);
        friendList.setFrinendList(json);


        friendListRepository.save(friendList);


        friendList = friendListRepository.findByUsername(friendName);


        list = (List<String>) friendList.getFrinendList().get("list");
        list.remove(username);

        json = new JSONObject();

        json.put("list", list);
        friendList.setFrinendList(json);

        friendListRepository.save(friendList);


    }


}
