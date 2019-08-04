package com.tibco.integration.hhd;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.log4j.Logger;

import java.nio.charset.Charset;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Truscreen 手持手柄连接操作
 */
public class HHDClient {
    private final static String host = "192.168.2.1";
    private final static int port = 8483;

    private static HHDClient hhdClient = new HHDClient();

    private final static Logger logger = Logger.getLogger(HHDClient.class);

    public volatile boolean status = true;
    /**
     * 当前状态
     */
    public volatile String currecntStatus = null;

    /**
     * 是第一次连接WI-FI成功 如果是 则不能发起start命令
     */
    public volatile boolean isConnectedFisrt = true;

    public boolean isConnectedFisrt() {
        return isConnectedFisrt;
    }

    public void setConnectedFisrt(boolean connectedFisrt) {
        isConnectedFisrt = connectedFisrt;
    }

    public String getCurrecntStatus() {
        return currecntStatus;
    }

    public void setCurrecntStatus(String currecntStatus) {
        this.currecntStatus = currecntStatus;
    }

    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(5));

    private HHDClient() {

    }

    public static HHDClient getInstance() {
        return hhdClient;

    }

    public void setSocketChannel(SocketChannel socketChannel) {
        logger.info("HDD 重启 重置连接");
        this.socketChannel = socketChannel;
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

    public void sendMsg(final String request) {
        logger.info(request);
        if (socketChannel == null) {
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        connect();
                    } catch (Exception e) {
                        logger.error("", e);
                        throw new RuntimeException("连接手持设备出错：" + e.getMessage());
                    }
                    if (socketChannel == null) {
                        throw new RuntimeException("连接手持设备失败,请求被忽略:" + request);
                    } else {
                        writeMsg(request);
                    }
                }
            });
        } else {
            writeMsg(request);
        }

    }

    private void writeMsg(String request) {
        try {
            socketChannel.writeAndFlush(Unpooled.copiedBuffer(request, Charset.defaultCharset()));
        } catch (Throwable e) {
            socketChannel = null;
            logger.error("HDD设备重启了 需要重新建立连接", e);
        }
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
