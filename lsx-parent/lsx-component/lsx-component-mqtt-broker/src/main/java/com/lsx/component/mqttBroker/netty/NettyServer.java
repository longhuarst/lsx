//package com.lsx.component.mqttBroker.netty;
//
//import io.netty.bootstrap.ServerBootstrap;
//import io.netty.buffer.PooledByteBufAllocator;
//import io.netty.channel.ChannelFuture;
//import io.netty.channel.ChannelInitializer;
//import io.netty.channel.ChannelOption;
//import io.netty.channel.EventLoopGroup;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.channel.socket.nio.NioServerSocketChannel;
//import io.netty.handler.codec.mqtt.MqttDecoder;
//import io.netty.handler.codec.mqtt.MqttEncoder;
//import io.netty.handler.timeout.IdleStateHandler;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import java.net.InetSocketAddress;
//import java.util.concurrent.ThreadFactory;
//import java.util.concurrent.atomic.AtomicInteger;
//
////@Component
//public class NettyServer {
//
////    @Value("")
////    private boolean sslFlag;
//
//    Logger logger = LoggerFactory.getLogger(NettyServer.class);
//
//    public void start(InetSocketAddress socketAddress){
//
//
//        boolean debug = logger.isDebugEnabled();
//
//        //new 一个主线程组
//        EventLoopGroup bossGroup = new NioEventLoopGroup(1, new ThreadFactory() {
//            private AtomicInteger index = new AtomicInteger(0);
//            @Override
//            public Thread newThread(Runnable r) {
//                return new Thread(r, "BOSS_" + index.incrementAndGet());
//            }
//        });
//
//        //new 一个工作线程组
//        EventLoopGroup workGroup = new NioEventLoopGroup(200, new ThreadFactory() {
//            private AtomicInteger index = new AtomicInteger(0);
//
//            @Override
//            public Thread newThread(Runnable r) {
//                return new Thread(r, "WORK_"+index.incrementAndGet());
//            }
//        });
//
//        ServerBootstrap bootstrap = new ServerBootstrap()
//                .group(bossGroup, workGroup)
//                .channel(NioServerSocketChannel.class)
//                .childHandler(new ChannelInitializer<SocketChannel>() {
//                    @Override
//                    protected void initChannel(SocketChannel ch) throws Exception {
//                        //判断是不是ssl
//                        //Todo：ssl 功能
////                        if (sslFlag){
////                            initSsl();
////                        }
//                        ch.pipeline().addLast("decoder", new MqttDecoder());
//                        ch.pipeline().addLast("encoder", MqttEncoder.INSTANCE);
//                        ch.pipeline().addLast(new IdleStateHandler(180, 0,0));//超时时间
////                        ch.pipeline().addLast(new MqttHandler());
//                        //initHandler(ch.pipeline(),)
//                    }
//                })//new ServerChannelInitializer())
//                .localAddress(socketAddress)
//                .option(ChannelOption.SO_REUSEADDR, true)
//                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
//                .option(ChannelOption.SO_RCVBUF, 10485760)//设置tcp 接受缓冲区大小
//                .option(ChannelOption.TCP_NODELAY, true) //TCP参数，立即发送数据，默认值为Ture（Netty默认为True而操作系统默认为False）。该值设置Nagle算法的启用，改算法将小的碎片数据连接成更大的报文来最小化所发送的报文的数量，如果需要发送一些较小的报文，则需要禁用该算法。Netty默认禁用该算法，从而最小化报文传输延时。
//                //设置队列大小
//                .option(ChannelOption.SO_BACKLOG, 1024)
//                //两小时内没有数据的通信时，TCP会自动发送一个活动探测报文
//                .childOption(ChannelOption.SO_KEEPALIVE, true);
//
//        //绑定端口，开始接受连接
//        try{
//            ChannelFuture future = bootstrap.bind(socketAddress).sync();
//            logger.info("服务器启动开始监听端口："+ socketAddress.getPort());
//            future.channel().closeFuture().sync();
//        }catch (InterruptedException e){
//            e.printStackTrace();
//        } finally {
//            //优雅关闭
//            bossGroup.shutdownGracefully();
//            workGroup.shutdownGracefully();
//        }
//
//    }
//}
