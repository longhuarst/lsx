package com.lsx.service.device.netty.chat;


import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.net.InetAddress;
import java.util.Map;

public class SecureChatServerHandler extends SimpleChannelInboundHandler<String> {

    static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);




    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //super.channelActive(ctx);
        ctx.pipeline().get(SslHandler.class).handshakeFuture().addListener(new GenericFutureListener<Future<? super Channel>>() {
            @Override
            public void operationComplete(Future<? super Channel> future) throws Exception {
                ctx.writeAndFlush("Welcome to "+ InetAddress.getLocalHost().getHostName()+ " secure chat service!\n" );
                ctx.writeAndFlush("Your session is protected by "+ ctx.pipeline().get(SslHandler.class).engine().getSession().getCipherSuite() + " cipher suite. \n");

                channels.add(ctx.channel());
            }
        });
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

        //发送收到的消息给所有channel 除了当前这个
        for (Channel c : channels){
            if (c != ctx.channel()){
                c.writeAndFlush("["+ctx.channel().remoteAddress()+"] "+msg+"\n");
            }else{
                c.writeAndFlush("[you] "+msg+"\n");
            }
        }

        //关闭连接 如果是bye
        if ("bye".equals(msg.toLowerCase())){
            ctx.close();
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
        //super.exceptionCaught(ctx, cause);
    }
}
