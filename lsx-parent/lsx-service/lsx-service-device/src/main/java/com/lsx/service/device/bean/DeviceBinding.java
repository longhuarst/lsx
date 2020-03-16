package com.lsx.service.device.bean;


import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "device_binding")
@EntityListeners(AuditingEntityListener.class)
public class DeviceBinding {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;


    @Column(name = "username")
    String username; //用户名


    @Column(name = "uuid")
    String uuid;//绑定的uuid


    @Column(name = "date")
    @CreatedDate
    Date date; //绑定日期


    @Override
    public String toString() {
        return "DeviceBinding{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", uuid='" + uuid + '\'' +
                ", date=" + date +
                '}';
    }

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
