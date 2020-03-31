package com.lsx.service.device.netty;

import com.lsx.service.device.netty.bean.Client;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class NettyServerHandler extends ChannelInboundHandlerAdapter{




    Set<Client> clientList = new ConcurrentSkipListSet<Client>();
    Map<ChannelHandlerContext, Client> clientMap = new ConcurrentHashMap<>();


    static Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);







    public NettyServerHandler() {
        super();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        logger.info("channelRegistered");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        logger.info("channelUnregistered");
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("channelActive");


        logger.info("size = "+clientMap.size());
        //增加一个新的
        clientMap.put(ctx,new Client());

        logger.info("date = "+clientMap.get(ctx).getConnectDate());

        clientMap.get(ctx).setConnectDate(new Date());

        logger.info("date = "+clientMap.get(ctx).getConnectDate());


        logger.info("size = "+clientMap.size());


        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("channelInactive");

        logger.info("size = "+clientMap.size());
        clientMap.remove(ctx); //删除一个
        logger.info("size = "+clientMap.size());

        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("channelRead");
        logger.info("data = "+msg.toString());

        Client client = clientMap.get(ctx);

        String message = (String) msg;
        if (client.isValidToken()){
            //已经验证过token 则 可以进行通信


            logger.info("token valied !");


            if (message.startsWith("publish"))









        }else{

            if (message.startsWith("bearer ")){

                //暂时不检测
                client.setValidToken(true);



            }else{
                //关闭
                ctx.close();
            }
        }
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        logger.info("channelReadComplete");

        super.channelReadComplete(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        logger.info("userEventTriggered");
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        logger.info("channelWritabilityChanged");
        super.channelWritabilityChanged(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.info("exceptionCaught");
        super.exceptionCaught(ctx, cause);
    }
}
