package com.lsx.service.entity;


import javax.persistence.*;

///照片墙
@Entity(name = "photo_wall")
public class PhotoWall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;


    @Column(name = "username", nullable = false, unique = true)
    String username;

    @Column(name = "photo_list", nullable = true)
    String photo_list;


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

    public String getPhoto_list() {
        return photo_list;
    }

    public void setPhoto_list(String photo_list) {
        this.photo_list = photo_list;
    }


    @Override
    public String toString() {
        return "PhotoWall{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", photo_list='" + photo_list + '\'' +
                '}';
    }
}
