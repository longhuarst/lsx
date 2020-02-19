package com.lsx.file.controller;



import com.lsx.file.file.FastDFSFile;
import com.lsx.file.util.FastDFSUtil;
import entity.Result;
import entity.StatusCode;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/file")
//@CrossOrigin        //跨域
public class FileUploadContller {


    @RequestMapping("/test")
    public String test(){
        return "test";
    }

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
                StringUtils.getFilename(file.getOriginalFilename()));//文件拓展名

        //调用fastdfs 的将工具类文件上传到fastdfs中
        FastDFSUtil.upload(fastDFSFile);

        return new Result(true, StatusCode.OK, "上传成功");

    }







}
