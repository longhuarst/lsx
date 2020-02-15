package com.lsx.service.entity;


import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;



@Entity
@Table(name = "user")
@EntityListeners(AuditingEntityListener.class)
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    //用户名
    @Column(name = "username", unique = true, nullable = false)
    String username;

    //密码
    @Column(name = "password", nullable = false)
    String password;

    //姓名
    String name;

    //微信
    String wechat;

    //QQ
    String qq;

    //邮箱
    String mail;

    //年龄
    String age;

    //手机
    String phone;

    //地址
    String address;

    //唯一识别吗
    @Column(name = "uuid", unique = true, nullable = false)
    String uuid;

    //角色
    @Column(name = "role", nullable = false)
    String role;

    //账户状态
    String status;

    //注册时间
    @CreatedDate
    Date register_time;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getRegister_time() {
        return register_time;
    }

    public void setRegister_time(Date register_time) {
        this.register_time = register_time;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", wechat='" + wechat + '\'' +
                ", qq='" + qq + '\'' +
                ", mail='" + mail + '\'' +
                ", age='" + age + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", uuid='" + uuid + '\'' +
                ", role='" + role + '\'' +
                ", status='" + status + '\'' +
                ", register_time=" + register_time +
                '}';
    }
}
