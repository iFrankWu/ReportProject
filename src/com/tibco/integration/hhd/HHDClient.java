package com.tibco.integration.hhd;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.log4j.Logger;

import java.nio.charset.Charset;

/**
 * Truscreen 手持手柄连接操作
 */
public class HHDClient {
    private final static String host = "192.168.2.1";
    private final static int port = 8483;

    private static HHDClient hhdClient = new HHDClient();

    private final static Logger logger = Logger.getLogger(HHDClient.class);

    public volatile boolean status = true;
    private HHDClient() {

    }

    public static HHDClient getInstance() {
        return hhdClient;

    }

    private SocketChannel socketChannel;

    private SingleThreadEventLoop singleThreadEventLoop = new DefaultEventLoop();

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
                logger.info("connect server  成功---------");

            }

            future.channel().closeFuture().sync();

        } finally {
            group.shutdownGracefully();
        }

    }

    public void sendMsg(String request) {
        logger.info(request);
        if (socketChannel == null) {
            try {
                connect();
            } catch (Exception e) {
                logger.error("", e);
                throw new RuntimeException("连接手持设备出错：" + e.getMessage());
            }
            if (socketChannel == null) {
                throw new RuntimeException("连接手持设备失败");
            }
        }
        socketChannel.writeAndFlush(Unpooled.copiedBuffer(request, Charset.defaultCharset()));
    }

    public static void main(String[] args) throws Exception {
        HHDClient client = new HHDClient();
        client.connect();


        StringBuffer request = new StringBuffer();

//        request.append("socket_request=terminate");
//        request.append("\\r\\n");
//        client.sendMsg(request.toString());

//        Thread.sleep(10000);


        request.append("login_1='TechTruScreen'");
        request.append("\\r\\n");
        request.append("login_2='TechULTRA'");
        request.append("\\r\\n");
        request.append("socket_request='login'");
        request.append("\\r\\n");

        client.sendMsg(request.toString());

//
//        request.append("login_1='TechTruScreen',");
//        request.append("login_2='TechULTRA',");
//        request.append("socket_request='login'");
//        request.append("\\r\\n");
//        client.sendMsg(request.toString());
    }
}
