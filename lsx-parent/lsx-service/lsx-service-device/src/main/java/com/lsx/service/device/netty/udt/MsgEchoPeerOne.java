package com.lsx.service.device.netty.udt;

import io.netty.util.internal.SocketUtils;

import java.net.InetSocketAddress;

public class MsgEchoPeerOne extends MsgEchoPeerBase {

    public MsgEchoPeerOne(final InetSocketAddress self, final InetSocketAddress peer, final int messageSize){
        super(self, peer, messageSize);
    }


    public static void main(String[] args) throws Exception{
        final int messageSize = 64*1024;
        final InetSocketAddress self = SocketUtils.socketAddress(Config.hostOne, Config.portOne);
        final InetSocketAddress peer = SocketUtils.socketAddress(Config.hostTwo, Config.portTwo);

        new MsgEchoPeerOne(self, peer, messageSize).run();
    }
}
