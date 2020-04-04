package com.lsx.service.device.netty;

import com.lsx.service.device.bean.TokenValid;
import com.lsx.service.device.netty.bean.Client;
import com.lsx.service.device.netty.bean.DeviceMessageStore;
import com.lsx.service.device.netty.utils.SpringUtil;
import com.mysql.cj.util.TimeUtil;
import entity.UuidValid;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import sun.tools.jstat.Token;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;

public class NettyServerHandler extends ChannelInboundHandlerAdapter{






    static Set<Client> clientList = new ConcurrentSkipListSet<Client>();
    static Map<ChannelHandlerContext, Client> clientMap = new ConcurrentHashMap<>();


    static Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);






    //话题表
    public static Map<String, ChannelGroup> topicMap = new ConcurrentHashMap<>();



    private static RabbitTemplate rabbitTemplate;

    static{
        rabbitTemplate = SpringUtil.getBean(RabbitTemplate.class);
    }


    private static TokenValid tokenValid;

    static {
        tokenValid = SpringUtil.getBean(TokenValid.class);
    }


    private static RedisTemplate<String, String> redisTemplate;
    static{
        redisTemplate = SpringUtil.getBean("redisTemplate",RedisTemplate.class);
    }

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


            //

            String start = ((String) msg).substring(0,3);


            int flag = ((String) msg).indexOf(":");

            if (flag == -1){
                ctx.close();//关闭
                clientList.remove(client);//删除
                logger.info("报文格式不正确，主动关闭：");
                return;
            }



            String body = ((String) msg).substring(flag+1);
            String head = ((String) msg).substring(0,flag);

            String headArray[] =  head.split("_");

            // sub_uuid_topic_type:body
            if (headArray.length != 4){
                ctx.close();//关闭
                clientList.remove(client);//删除
                logger.info("报文格式不正确，主动关闭：");
                return;
            }

            String uuid = headArray[1];
            //因为token 自带uuid 所以这个字段已经不需要，可以随意填写建议填充1个字符
            uuid = client.getUuid(); //从列表中获取uuid
            String topic = headArray[2];
            String type = headArray[3];

            logger.info("uuid="+uuid+"topic="+topic+"type="+type);


            UuidValid valid = new UuidValid();
            if (!valid.isValidUUID(uuid)){
                ctx.writeAndFlush("[error uuid]\r\n");
                ctx.close();//关闭
                clientList.remove(client);//删除
                logger.info("UUID不正确，主动关闭：");
                return;
            }



            switch (start){
                case "pub":
                    logger.info("[publish]");

                    String sendMsg = head + "_" + ctx.channel().remoteAddress().toString() + ":" + body + "\r\n";
                    //转发
                    //tcp数据
                    if (type.equals("tcp")){
                        ChannelGroup channels = topicMap.get(topic);
                        if (channels != null) {
                            for (Channel channel : channels) {
                                channel.writeAndFlush(sendMsg);
                            }
                        }
                    }else if (type.equals("http")){
                        //进行http转发，将数据填充到zookeeper节点 或者  写入redis
                        //暂时先操作redis
                        String redisKey = "dm_"+uuid;
                        if (redisTemplate.opsForList().size(redisKey) > 100){
                            redisTemplate.opsForList().rightPop(redisKey);
                        }
                        redisTemplate.opsForList().leftPush(redisKey, sendMsg);// , 6000, TimeUnit.SECONDS);
                    }
                    //持久化
                    DeviceMessageStore deviceMessageStore = new DeviceMessageStore();
                    deviceMessageStore.setUuid(uuid);
                    deviceMessageStore.setTopic(topic);
                    deviceMessageStore.setMsg(body);
                    try {
                        rabbitTemplate.convertAndSend("DeviceMessageStoreQueue", deviceMessageStore);
                    }catch (Exception e){
                        e.printStackTrace();
                        logger.info("rabbitmq write error");
                    }

                    break;
                case "sub":
                    logger.info("[subscribe]");
                    //解析
                    ChannelGroup newChannels = topicMap.get(topic);
                    if (newChannels == null){
                        newChannels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
                        newChannels.add(ctx.channel());
                        topicMap.put(topic,newChannels);
                    }else {
                        try {
                            newChannels.remove(ctx.channel());
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        newChannels.add(ctx.channel());//订阅
                        logger.info("channelList size = "+newChannels.size());
                    }


//                    topicMap.put(topic, client);//订阅
                    break;
                default:
                    break;
            }


            if (message.startsWith("publish")){
                logger.info("[publish]");
            }









        }else{

            if (message.startsWith("bearer ")){


                //检测token
                String token = message.substring("bearer ".length());
                logger.info("token = "+token);
                String tokenUuid = tokenValid.validDeviceToken(token);
                if (tokenUuid != null){
                    logger.info("token uuid = "+tokenUuid);
                    if (client == null){
                        client = new Client();
                        logger.info("client is null");
                    }
                    client.setUuid(tokenUuid);
                    client.setValidToken(true);
                    clientList.add(client);
                }else{
                    //关闭
                    ctx.writeAndFlush("[error token]\r\n");
                    ctx.close();
                }




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
