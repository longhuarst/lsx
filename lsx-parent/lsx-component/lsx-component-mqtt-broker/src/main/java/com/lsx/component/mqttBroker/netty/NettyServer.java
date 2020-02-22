package com.lsx.component.mqttBroker.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class NettyServer {

    Logger logger = LoggerFactory.getLogger(NettyServer.class);

    public void start(InetSocketAddress socketAddress){
        boolean debug = logger.isDebugEnabled();

        //new 一个主线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);

        //new 一个工作线程组
        EventLoopGroup workGroup = new NioEventLoopGroup(200);

        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ServerChannelInitializer())
                .localAddress(socketAddress)
                //设置队列大小
                .option(ChannelOption.SO_BACKLOG, 1024)
                //两小时内没有数据的通信时，TCP会自动发送一个活动探测报文
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        //绑定端口，开始接受连接
        try{
            ChannelFuture future = bootstrap.bind(socketAddress).sync();
            logger.info("服务器启动开始监听端口："+ socketAddress.getPort());
            future.channel().closeFuture().sync();
        }catch (InterruptedException e){
            e.printStackTrace();
        } finally {
            //优雅关闭
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

    }
}
