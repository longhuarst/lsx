package com.lsx.service.device.bean;


import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "device_message")
@EntityListeners(AuditingEntityListener.class)
public class DeviceMessage implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "uuid", nullable = false)
    String uuid;

    @Column(name = "topic", nullable = false)
    String topic;

    @Column(name = "msg")
    String msg;

    @Column(name = "date")
    @CreatedDate
    Date date;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    @Override
    public String toString() {
        return "DeviceMessage{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", topic='" + topic + '\'' +
                ", msg='" + msg + '\'' +
                ", date=" + date +
                '}';
    }
}
