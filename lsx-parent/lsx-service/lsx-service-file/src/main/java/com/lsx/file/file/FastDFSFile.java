package com.lsx.file.file;



/*
* 封装文件上传信息
*
*       时间：
*       上传人：
*       类型：
*       大小：
*       附加信息：
*       文件内容：
*
* */


import java.io.Serializable;

public class FastDFSFile implements Serializable {


    //文件名字
    private String name;

    //文件内容
    private byte[] content;

    //文件拓展名
    private String ext;

    //文件md5摘要
    private String md5;

    //文件创建者
    private String author;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
