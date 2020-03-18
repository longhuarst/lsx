package com.lsx.service.device.bean;


import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

//动态数据类型

@Entity
@EntityListeners(AuditingEntityListener.class)
public class DeviceType implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "type", unique = true, nullable = false)
    String type;    //设备类型

    @Column(name = "data_type")
    String dataType;//数据类型


    @Override
    public String toString() {
        return "DeviceType{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", dataType='" + dataType + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}
