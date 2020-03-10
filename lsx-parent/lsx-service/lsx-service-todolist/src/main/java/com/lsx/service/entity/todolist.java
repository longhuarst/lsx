package com.lsx.service.entity;


import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "todolist")
public class todolist implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "username", nullable = false, unique = true)
    String username;

    @Column(name = "todolist")
    String todolist;

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

    public String getTodolist() {
        return todolist;
    }

    public void setTodolist(String todolist) {
        this.todolist = todolist;
    }

    @Override
    public String toString() {
        return "todolist{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", todolist='" + todolist + '\'' +
                '}';
    }
}
