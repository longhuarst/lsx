package com.lsx.file.controller;



import com.lsx.file.file.FastDFSFile;
import com.lsx.file.util.FastDFSUtil;
import entity.Result;
import entity.StatusCode;
import org.csource.fastdfs.FileInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(value = "/file")
//@CrossOrigin        //跨域
public class FileUploadContller {


    @RequestMapping("/test")
    public String test(){
        return "test";
    }


    static Logger logger = LoggerFactory.getLogger(FileUploadContller.class);




    /*
    *
    * 文件上传
    *
    * */
    @PostMapping("/upload")
    public Result upload(@RequestParam(value = "file")MultipartFile file) throws Exception{
        //封装文件信息

        FastDFSFile fastDFSFile = new FastDFSFile(file.getOriginalFilename(),//文件名字
                file.getBytes(),//文件字节数组
                StringUtils.getFilenameExtension(file.getOriginalFilename()));//文件拓展名

        //调用fastdfs 的将工具类文件上传到fastdfs中
        String [] storage = FastDFSUtil.upload(fastDFSFile);


        //拼接 url 地址
        String url[] = new String[2];
        url[0] = FastDFSUtil.getTrackerInfo() + "/" + storage[0] + "/" + storage[1];
        url[1] = "/" + storage[0] + "/" + storage[1];


        return new Result(true, StatusCode.OK, "上传成功",url);

    }

    /*
    * 文件下载
    * */
    @RequestMapping("download")
    public byte[] download(String group, String name) throws Exception {
        logger.info("downlload");
        return FastDFSUtil.download(group,name);
    }


    /*
    *
    * 获取文件信息
    *
    * */
    @PostMapping("/info")
    public Result info(@RequestParam("group") String group, @RequestParam("name") String name) throws Exception{

        FileInfo fileInfo = FastDFSUtil.info(group,name);

        return new Result(true, StatusCode.OK, "获取成功", fileInfo);
    }



    /*
    * 下载文件
    */
//    @RequestMapping("download")
//    public Result download(@RequestParam("group") String group, @RequestParam("name") String name) throws Exception{
//
//    }


    /*
    * 删除文件
    * */
    @RequestMapping("delete")
    public Result delete(@RequestParam("group") String group, @RequestParam("name") String name) throws Exception{

        if (FastDFSUtil.delete(group, name) == 0){
            return new Result(true, StatusCode.OK, "删除成功");
        }else{
            return new Result(false, StatusCode.ERROR, "删除失败");
        }
    }



    /*
    * 获取Storeage信息
    * */
    @RequestMapping("storage")
    public Result storage(@RequestParam("group") String group, @RequestParam("name") String name) throws Exception{
        return new Result(true, StatusCode.OK, "获取成功", FastDFSUtil.getStorageInfo(group, name));
    }


    /*
    * 获取Tracker信息
    * */
    @RequestMapping("tracker")
    public Result tracker() throws Exception{
        return new Result(true, StatusCode.OK, "获取成功", FastDFSUtil.getTrackerInfo());
    }





}
