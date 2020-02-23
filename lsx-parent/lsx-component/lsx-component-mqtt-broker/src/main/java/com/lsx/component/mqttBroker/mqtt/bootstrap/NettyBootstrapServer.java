package com.lsx.component.mqttBroker.mqtt.bootstrap;

import com.lsx.component.mqttBroker.mqtt.common.ip.IpUtils;
import com.lsx.component.mqttBroker.mqtt.common.properties.InitBean;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * netty 服务启动类
 **/
@Setter
@Getter
@lombok.extern.slf4j.Slf4j
public class NettyBootstrapServer extends AbstractBootstrapServer {

    Logger logger = LoggerFactory.getLogger(NettyBootstrapServer.class);

    private InitBean serverBean;

    public InitBean getServerBean() {
        return serverBean;
    }

    public void setServerBean(InitBean serverBean) {
        this.serverBean = serverBean;
    }

    private NioEventLoopGroup bossGroup;

    private NioEventLoopGroup workGroup;

    ServerBootstrap bootstrap=null ;// 启动辅助类

    /**
     * 服务开启
     */
    public void start() {
        logger.info("NettyBootstrapServer start()");
        initEventPool();
        bootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_REUSEADDR, true)//serverBean.isReuseaddr())
                .option(ChannelOption.SO_BACKLOG, 1024)//serverBean.getBacklog())
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .option(ChannelOption.SO_RCVBUF, 10485760)//serverBean.getRevbuf())
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel ch) throws Exception {
                        initHandler(ch.pipeline(),serverBean);
                    }
                })
                .childOption(ChannelOption.TCP_NODELAY, true)//serverBean.isTcpNodelay())
                .childOption(ChannelOption.SO_KEEPALIVE, true)//120)//serverBean.isKeepalive())
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        bootstrap.bind(19000/*serverBean.getPort()*/).addListener((ChannelFutureListener) channelFuture -> {
            if (channelFuture.isSuccess())
                logger.info("服务端启动成功【" + IpUtils.getHost() + ":" + /*serverBean.getPort()*/ 19000 + "】");
            else
                logger.info("服务端启动失败【" + IpUtils.getHost() + ":" + /*serverBean.getPort()*/19000 + "】");
        });
    }
    /**
     * 初始化EnentPool 参数
     */
    private void  initEventPool(){
        bootstrap= new ServerBootstrap();
        bossGroup = new NioEventLoopGroup(/*serverBean.getBossThread()*/ 1, new ThreadFactory() {
            private AtomicInteger index = new AtomicInteger(0);

            public Thread newThread(Runnable r) {
                return new Thread(r, "BOSS_" + index.incrementAndGet());
            }
        });
        workGroup = new NioEventLoopGroup(200/*serverBean.getWorkThread()*/, new ThreadFactory() {
            private AtomicInteger index = new AtomicInteger(0);

            public Thread newThread(Runnable r) {
                return new Thread(r, "WORK_" + index.incrementAndGet());
            }
        });

    }

    /**
     * 关闭资源
     */
    public void shutdown() {
        if(workGroup!=null && bossGroup!=null ){
            try {
                bossGroup.shutdownGracefully().sync();// 优雅关闭
                workGroup.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                logger.info("服务端关闭资源失败【" + IpUtils.getHost() + ":" + serverBean.getPort() + "】");
            }
        }
    }


}

