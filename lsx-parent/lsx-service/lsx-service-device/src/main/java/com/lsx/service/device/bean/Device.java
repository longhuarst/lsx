package com.lsx.service.device.bean;


import com.lsx.service.device.util.AuthToken;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "device")
@EntityListeners(AuditingEntityListener.class)
public class Device implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;


    @Column(name = "username", unique = false, nullable = false)
    String username;

    @Column(name = "uuid", unique = true, nullable = false)
    String uuid;

    //注册时间
    @CreatedDate
    @Column(name = "register_time")
    Date register_time;

    //设备别名
    @Column(name = "name")
    String name;

    //设备备注
    @Column(name = "info")
    String info;



    //设备类型
    @Column(name = "type", nullable = false)
    String type;

    @Column(name = "token", nullable = false, columnDefinition = "blob")
    AuthToken token;


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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Date getRegister_time() {
        return register_time;
    }

    public void setRegister_time(Date register_time) {
        this.register_time = register_time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public AuthToken getToken() {
        return token;
    }

    public void setToken(AuthToken token) {
        this.token = token;
    }
}
