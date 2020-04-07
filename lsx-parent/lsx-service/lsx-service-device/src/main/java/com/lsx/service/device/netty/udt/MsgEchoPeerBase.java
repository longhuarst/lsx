package com.lsx.service.device.netty.udt;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.udt.UdtChannel;
import io.netty.channel.udt.nio.NioUdtProvider;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadFactory;

//udt 消息流 peer
//当一个连接打开时，发送消息，并echo任何收到的消息数据给另一个连接端
public abstract class MsgEchoPeerBase {


    protected final int messageSize;
    protected final InetSocketAddress self;
    protected final InetSocketAddress peer;

    protected MsgEchoPeerBase(final InetSocketAddress self, final InetSocketAddress peer, final int messageSize){
        this.messageSize = messageSize;
        this.self = self;
        this.peer = peer;
    }

    public void run() throws Exception{
        //配置peer
        final ThreadFactory connectFactory = new DefaultThreadFactory("rendezvous");
        final NioEventLoopGroup connectGroup =  new NioEventLoopGroup(1, connectFactory, NioUdtProvider.MESSAGE_PROVIDER);

        try {
            final Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(connectGroup)
                    .channelFactory(NioUdtProvider.MESSAGE_RENDEZVOUS)
                    .handler(new ChannelInitializer<UdtChannel>() {
                        @Override
                        protected void initChannel(UdtChannel ch) throws Exception {
                            ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO), new MsgEchoPeerHandler(messageSize));
                        }
                    });

            //开启peer
            final ChannelFuture future  = bootstrap.connect(peer, self).sync();

            //等待连接关闭
            future.channel().closeFuture().sync();

        } finally {
            //优雅的关闭 event loop 并 结束所有线程
            connectGroup.shutdownGracefully();
        }
    }
}
