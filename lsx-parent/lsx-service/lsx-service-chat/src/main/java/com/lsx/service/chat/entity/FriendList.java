package com.lsx.service.chat.entity;


import com.alibaba.fastjson.JSONObject;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity(name = "friend_list")
@EntityListeners(AuditingEntityListener.class)
@TypeDef(name = "json", typeClass = JSONObject.class)
public class FriendList {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "username", nullable = false, unique = true)
    String username;

    @Type(type = "json")
    @Column(name = "friend_list", columnDefinition = "json")
    JSONObject frinendList;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public JSONObject getFrinendList() {
        return frinendList;
    }

    public void setFrinendList(JSONObject frinendList) {
        this.frinendList = frinendList;
    }

    @Override
    public String toString() {
        return "FriendList{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", frinendList=" + frinendList +
                '}';
    }
}
