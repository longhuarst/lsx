package com.lsx.service.mapper;

public interface TodolistMapper {


    //更新todolist
    void updateTodolist(String uername, String todolist);

    //获取todolist
    String findTodolist(String username);
}
