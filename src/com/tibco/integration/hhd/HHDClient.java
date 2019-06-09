package com.tibco.integration.hhd;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.Charset;

/**
 * Truscreen 手持手柄连接操作
 */
public class HHDClient {
    private final static String host = "127.0.0.1";
    private final static int port = 8888;

    private SocketChannel socketChannel;

    public void connect() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true).handler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new HHDClientHandler());
                }
            });
            ChannelFuture future = b.connect(host, port).sync();



            if (future.isSuccess()) {
                socketChannel = (SocketChannel) future.channel();
                System.out.println("connect server  成功---------");
            }

//            future.channel().closeFuture().sync();

        } finally {
//            group.shutdownGracefully();
        }
    }

    public void sendMsg(String request) {
        socketChannel.writeAndFlush(Unpooled.copiedBuffer(request, Charset.defaultCharset()));
    }

    public static void main(String[] args) throws Exception {
        HHDClient client = new HHDClient();
        client.connect();

        client.sendMsg("helloooo1");
        client.sendMsg("helloooo2");
        client.sendMsg("helloooo3");

    }
}
