package com.lsx.file.util;


/*
*   实现fastDFS文件管理
*       文件上传
*       文件删除
*       文件下载
*       文件信息获取
*       Storage信息获取
*       Tracker信息获取
*
* */


import com.lsx.file.file.FastDFSFile;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

public class FastDFSUtil {




    /*
    * 加载Tracker连接信息
    * */
    static {


        try {
            ClassPathResource classPathResource = new ClassPathResource("fdfs_client.conf");

            String filename = classPathResource.getFilename();
            ClientGlobal.init(filename);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
    }


    public static StorageClient getStorageClient() throws Exception{
        //创建一个 访问 tracker 的客户端对象 TrackerClient
        TrackerClient trackerClient = new TrackerClient();

        //通过TrackerClient 访问 TrackerServer 服务 获取连接信息
        TrackerServer trackerServer = trackerClient.getConnection();

        //通过TrackerServer 访问Storage 的连接信息， 创建 StorageClient 对象存储Storage 的连接信息
        StorageClient storageClient = new StorageClient(trackerServer, null);

        return storageClient;
    }

    /*
    * 文件上传
    * @param fastDFSFile
    * */
    public static String[] upload(FastDFSFile fastDFSFile) throws Exception {
        //附加参数
        NameValuePair [] metaList = new NameValuePair[1];
        metaList[0] = new NameValuePair("author",fastDFSFile.getAuthor());//添加作者


        //获取存储服务器客户端
        StorageClient storageClient = getStorageClient();


        //通过StorageClient 访问 Storage 实现文件的 上传， 并获取上传后的存储信息
        return storageClient.upload_file(fastDFSFile.getContent(), fastDFSFile.getExt(), null);

    }


    /*
    * 文件信息获取
    * */
    public static FileInfo info(String group, String name) throws Exception{

        StorageClient storageClient = getStorageClient();

        return storageClient.get_file_info(group, name);

    }


    /*
    * 文件下载
    * */
    public static byte[] download(String group, String name) throws Exception {
        StorageClient storageClient = getStorageClient();
        byte[] buffer = storageClient.download_file(group, name);
//        return new ByteArrayInputStream(buffer);
        return buffer;
    }


    /*
    * 文件删除
    * */
    public static int delete(String group, String name) throws Exception{
        StorageClient storageClient = getStorageClient();
        return storageClient.delete_file(group, name);
    }

    /*
     * 获取Storage信息
     * */
    public static ServerInfo[] getStorageInfo(String group, String name) throws Exception{
        //创建一个 访问 tracker 的客户端对象 TrackerClient
        TrackerClient trackerClient = new TrackerClient();

        //通过TrackerClient 访问 TrackerServer 服务 获取连接信息
        TrackerServer trackerServer = trackerClient.getConnection();

        return trackerClient.getFetchStorages(trackerServer, group, name);
    }

    /*
    * 获取tracker的信息
    * */
    public static String getTrackerInfo() throws Exception{
        //创建一个 访问 tracker 的客户端对象 TrackerClient
        TrackerClient trackerClient = new TrackerClient();


        //通过TrackerClient 访问 TrackerServer 服务 获取连接信息
        TrackerServer trackerServer = trackerClient.getConnection();

        String ip = trackerServer.getInetSocketAddress().getHostString();
        int port = ClientGlobal.getG_tracker_http_port();

        String url = "http://" + ip + ":" + port;

        return url;
    }

}
