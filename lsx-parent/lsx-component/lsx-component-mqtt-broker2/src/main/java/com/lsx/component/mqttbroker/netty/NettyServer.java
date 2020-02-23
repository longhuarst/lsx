package com.lsx.component.mqttbroker.netty;


import com.lsx.component.mqttbroker.netty.utils.IpUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class NettyServer {

    @Autowired
    private ServerConfigure configure;

    private Logger logger = LoggerFactory.getLogger(NettyServer.class);
    private boolean debug = logger.isDebugEnabled();



    public void start(){

        EventLoopGroup bossGroup = new NioEventLoopGroup(configure.getBossGroupThread(), new ThreadFactory() {
            private AtomicInteger index = new AtomicInteger(0);
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "BOSS_" + index.incrementAndGet());
            }
        });

        EventLoopGroup workerGroup = new NioEventLoopGroup(configure.getWorkerGroupThread(), new ThreadFactory() {
            private AtomicInteger index = new AtomicInteger(0);
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "WORKER_" + index.incrementAndGet());
            }
        });

        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_REUSEADDR, configure.isResueAddr())
                .option(ChannelOption.SO_BACKLOG, configure.getBackLog())
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .option(ChannelOption.SO_RCVBUF, configure.getRcvBuf())
//                .option(ChannelOption.SO_SNDBUF, configure.getSndBuf())    //这边有警告    Unknown channel option 'SO_SNDBUF' for channel '[id: 0xaf8e3b83]'
                .childOption(ChannelOption.SO_SNDBUF, configure.getSndBuf()) //sndbuf 在 childOption中设置 消除上面的警告
                .childOption(ChannelOption.TCP_NODELAY, configure.isNoDelay())
                .childOption(ChannelOption.SO_KEEPALIVE, configure.isKeepAlive())
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childHandler(new NettyChannelInitializer());

        bootstrap.bind(configure.getPort()).addListener((ChannelFutureListener) channelFuture -> {
            if (channelFuture.isSuccess()){
                logger.info("服务端 启动成功 【" + IpUtils.getHost()+ ":"+configure.getPort()+"】");
            }else{
                logger.info("服务端 启动失败 【" + IpUtils.getHost()+ ":"+configure.getPort()+"】");
            }
        });

    }



}
