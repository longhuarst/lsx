package com.lsx.demo.zookeeper.configure;


import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CountDownLatch;

@Configuration
public class ZookeeperConfig {
//
//    private static final Logger logger = LoggerFactory.getLogger(ZookeeperConfig.class);
//
//    private static boolean debug = logger.isDebugEnabled();
//
//    @Value("${zookeeper.address}")
//    private String connectString;
//
//    @Value("${zookeeper.timeout}")
//    private int timeout;
//
//    private static Stat stat = new Stat();
//
//    @Bean
//    public ZooKeeper zkClient(){
//        ZooKeeper zooKeeper = null;
//
//        final CountDownLatch countDownLatch = new CountDownLatch(1);
//
//        //连接成功后，会回调watcher监听，此连接操作是异步的，执行完new语句后，直接调用后续代码
//        //  可指定多台服务地址 127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183
//
//        zooKeeper = new ZooKeeper(connectString, timeout, new Watcher() {
//
//            @Override
//            public void process(WatchedEvent watchedEvent) {
//
//                if (watchedEvent.getState() == Event.KeeperState.SyncConnected){
//                    //zookeeper 连接成功通知事件
//                    countDownLatch.countDown();
//                }else if (watchedEvent.getType() == Event.EventType.NodeDataChanged){
//                    //zookeeper 节点数据变化通知事件
//                    try{
//                        logger.info("配置已修改， 新值为："+new String(zooKeeper.getData(watchedEvent.getPath(), true, stat)));
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//
//
//                }
//            }
//        });
//
//        countDownLatch.await();
//
//        logger.info("【初始化ZooKeeper连接状态....】={}", zooKeeper.getState());
//    }




}
