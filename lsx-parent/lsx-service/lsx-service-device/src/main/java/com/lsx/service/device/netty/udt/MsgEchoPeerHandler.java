package com.lsx.service.device.netty.udt;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.udt.UdtMessage;
import io.netty.channel.udt.nio.NioUdtProvider;

public class MsgEchoPeerHandler extends SimpleChannelInboundHandler<UdtMessage> {

    private final UdtMessage message;

    public MsgEchoPeerHandler(final int messageSize){
        super(false);
        final ByteBuf byteBuf = Unpooled.buffer(messageSize);
        for (int i=0; i<byteBuf.capacity(); ++i){
            byteBuf.writeByte((byte) i);
        }
        message = new UdtMessage(byteBuf);
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
//        System.err.println("ECHO active " + NioUdtProvider.socketUDT(ctx.channel()));
        ctx.writeAndFlush(message);
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, UdtMessage msg) throws Exception {
        ctx.write(message);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
        //super.channelReadComplete(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
        //super.exceptionCaught(ctx, cause);
    }
}
