package com.lsx.demo.zookeeper;


import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

public class ZookeeperTest {


    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
//    private staticatic ZooKeeper zk = null;


    @Test
    public void test(){


        //zookeeper 配置数据存放路径
        String path = "username";

        //连接zookeeper 并且注册一个默认的监听器
//        zk = new



    }
}
