package com.lsx.service.device.netty.chat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class SecureChatClient {



    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8992"));


    public static void main(String[] args) throws Exception{

        //配置SSL
        final SslContext sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();

        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new SecureChatClientInitializer(sslCtx));

            //尝试连接
            Channel ch = b.connect(HOST, PORT).sync().channel();

            //从 stdin 读取指令
            ChannelFuture lastWriteFuture = null;
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            for(;;){
                String line = in.readLine();
                if (line == null){
                    break;
                }

                //发送给服务器
                lastWriteFuture = ch.writeAndFlush(line+"\r\n");

                //如果用户输入 bye 指令， 等待服务器关闭连接

                if ("byte".equals(line.toLowerCase())){
                    ch.closeFuture().sync();
                    break;
                }
            }

            //等待所有消息都flushed
            if (lastWriteFuture != null){
                lastWriteFuture.sync();
            }

            //
        }finally {
            //优雅的关闭
            group.shutdownGracefully();
        }
    }
}
