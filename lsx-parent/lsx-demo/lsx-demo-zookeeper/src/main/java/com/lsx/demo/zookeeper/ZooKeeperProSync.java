package com.lsx.demo.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

public class ZooKeeperProSync implements Watcher {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private static ZooKeeper zk = null;
    private static Stat stat = new Stat();



    @Override
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getState() == Event.KeeperState.SyncConnected){
            //zookeeper 连接成功通知事件

            if (Event.EventType.None == watchedEvent.getType() && null == watchedEvent.getPath()){
                connectedSemaphore.countDown();
            }else if (watchedEvent.getType() == Event.EventType.NodeDataChanged){
                //zk 目录节点数据变化通知事件
                try {
                    System.out.println("配置已修改， 新值为：" + new String(zk.getData(watchedEvent.getPath(), true, stat)));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }




    public static void main(String[] args) throws Exception{
        //zookeeper 配置存放路径
        String path = "/username";
        //连接zookeeper并且注册一个默认监听器
        zk = new ZooKeeper("127.0.0.1:2181", 5000, new ZooKeeperProSync());

        connectedSemaphore.await();

        System.out.println(new String(zk.getData(path, true, stat)));

        Thread.sleep(Integer.MAX_VALUE);
    }
}
