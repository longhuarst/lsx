package com.lsx.service.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lsx.service.entity.PhotoWall;
import com.lsx.service.mapper.PhotoWallMapper;
import entity.Result;
import entity.StatusCode;
import org.junit.platform.commons.function.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//照片墙
@RestController
@RequestMapping("/photoWall")
public class PhotoWallController {


    Logger logger = LoggerFactory.getLogger(PhotoWallController.class);


    @Autowired
    private PhotoWallMapper photoWallMapper;




    //更新照片墙 按序号更新  0-16
    @RequestMapping("/changePhotoWallWithId")
    public Result changePhotoWallWithId(String username, Integer id, String url){
        if (id <0 || id > 15){
            return new Result(false, StatusCode.ERROR, "参数错误，id 不处于 0-15");
        }

        //从数据库中抽取数据

        String list = photoWallMapper.findByUsername(username);
        JSONObject json = null;

        if (list == null){
            json = new JSONObject();
            for (int i=0;i<16;++i){
                json.put(String.valueOf(i), "");
            }
        }

        json.remove(String.valueOf(id));
        json.put(String.valueOf(id), url);


        //更新数据库
        logger.info(json.toJSONString());

        try {
            photoWallMapper.updatePhotoWall(username, json.toJSONString());
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "失败："+e.getMessage());
        }


        return new Result(false, StatusCode.OK, "成功");


    }


    //更新照片墙
    @RequestMapping("/changePhotoWall")
    public Result changePhotoWall(String username, String [] list){
        if (list.length != 16){
            return new Result(false, StatusCode.ERROR, "参数错误，不足16");
        }

        JSONObject json = new JSONObject();





        for(int i=0; i<list.length; ++i){
            json.put(String.valueOf(i), list[i]);
        }


        logger.info(json.toJSONString());

        try {
            photoWallMapper.updatePhotoWall(username, json.toJSONString());
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "失败："+e.getMessage());
        }


        return new Result(false, StatusCode.OK, "成功");


    }



    //获取照片墙
    @RequestMapping("getPhotoWall")
    public Result getPhotoWall(String username){

        String list = photoWallMapper.findByUsername(username);

        if (list == null) {
            return new Result(false, StatusCode.ERROR, "未找到");
        }

        JSONObject jsonObject = null;

        logger.info(list);

        try {
            jsonObject = JSONObject.parseObject(list);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(true, StatusCode.ERROR, "失败", e.getMessage());
        }

        return new Result(true, StatusCode.OK, "成功", jsonObject);

    }



}
