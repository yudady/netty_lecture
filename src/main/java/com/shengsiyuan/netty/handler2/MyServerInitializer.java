package com.shengsiyuan.netty.handler2;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;


public class MyServerInitializer extends ChannelInitializer<SocketChannel> {

    private int count;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        System.out.println(this + ", count =" + (++this.count));

        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new MyServerHandler());
    }
}
