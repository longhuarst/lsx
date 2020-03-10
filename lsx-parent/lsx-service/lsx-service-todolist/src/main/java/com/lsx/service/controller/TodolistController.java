package com.lsx.service.controller;


import com.alibaba.fastjson.JSONObject;
import com.lsx.service.mapper.TodolistMapper;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;




//用于持久化用户的 todolist

@RestController
@RequestMapping("/todolist")
public class TodolistController {

    @Autowired
    private TodolistMapper todolistMapper;



    @RequestMapping("/getList")
    public Result getList(String username){

        JSONObject jsonObject = null;

        String todolist = todolistMapper.findTodolist(username);

        if (todolist == null){
            return new Result(false, StatusCode.ERROR, "未找到");
        }

        try {
            jsonObject = JSONObject.parseObject(todolist);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(true, StatusCode.ERROR, "出错了", e.getMessage());
        }

        return new Result(true, StatusCode.OK, "成功", jsonObject);

    }


    @RequestMapping("/updateList")
    public Result updateList(String username, String todolist){
//        JSONObject jsonObject = null;
//        try {
//            jsonObject = JSONObject.parseObject(todolist);
//        }catch (Exception e){
//            e.printStackTrace();
//            return new Result(true, StatusCode.ERROR, "出错了", e.getMessage());
//        }

        //更新数据库
        try {
            todolistMapper.updateTodolist(username, todolist);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(true, StatusCode.ERROR, "出错了", e.getMessage());
        }
        return new Result(false, StatusCode.OK, "成功");

    }

}
