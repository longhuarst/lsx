package com.lsx.service.device.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyServer  {



    static Logger logger = LoggerFactory.getLogger(NettyServer.class);




    //创建一个线程数为100 的EventExecutorGroup
    static final EventExecutorGroup executor = new DefaultEventExecutorGroup(100);



    public void start(){

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);

        EventLoopGroup workGroup = new NioEventLoopGroup(200);

        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
                        ch.pipeline().addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
                        ch.pipeline().addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
                        ch.pipeline().addLast(executor, new NettyServerHandler());  //业务处理单独的 100个线程处理  和 IO线程分离
//                        ch.pipeline().addLast(executor, new )

                    }
                })
                .localAddress(20000)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.SO_RCVBUF, 8*1024)//设置接受缓存为8kb 物联网应用
                .childOption(ChannelOption.SO_SNDBUF, 8*1024)//设置发送缓存为8kb， 物联网应用
                ;


        try {
            ChannelFuture future = bootstrap.bind().sync();
            logger.info("服务器启动开始监听 20000");
            future.channel().closeFuture().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    //业务逻辑处理代码，此处省略
                    bossGroup.shutdownGracefully();
                    workGroup.shutdownGracefully();
                    logger.info(future.channel().toString() + "链路关闭");
                }
            });
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
//            bossGroup.shutdownGracefully();
//            workGroup.shutdownGracefully();
        }
    }


}
