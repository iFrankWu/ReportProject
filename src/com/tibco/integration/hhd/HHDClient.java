package com.tibco.integration.hhd;

import com.tibco.util.Const;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.concurrent.*;

/**
 * Truscreen 手持手柄连接操作
 */
public class HHDClient {
    private final static String host = Const.HHD_IP_ADDRESS;
    private final static int port = Const.HHD_PORT;

    private static int requestTimes = 0;

    /**
     * 当检查结束后 不再发送 system report了
     */
    public static volatile boolean IS_CHECK_FINISH = false;

    private static HHDClient hhdClient = new HHDClient();
    /**
     * 跟HDD设备交互 是否已经接收到上次请求命令的回复信息 true 代表是
     * 系统初始化时 该值默认为ture
     * 命令发送后 更新为false
     * 收到命令回信后 更新为true
     */
    public volatile Context lastCommandContext = new Context(true, new Date(), null,false);

    class Context {
        boolean lastCommandResponseDone;
        Date lastCommandCommitTime;
        String command;
        //定时任务发起的命令
        boolean isFromTimer;

        Context(boolean lastCommandResponseDone, Date lastCommandCommitTime, String command,boolean isFromTimer) {
            this.lastCommandCommitTime = lastCommandCommitTime;
            this.lastCommandResponseDone = lastCommandResponseDone;
            this.command = command;
            this.isFromTimer = isFromTimer;
        }

        @Override
        public String toString() {
            return "Context{" +
                    "lastCommandResponseDone=" + lastCommandResponseDone +
                    ", lastCommandCommitTime=" + lastCommandCommitTime +
                    ", command='" + command + '\'' +
                    ", isFromTimer=" + isFromTimer +
                    '}';
        }
    }


    /**
     * 定时器是否已经开启
     */
    private volatile boolean TIMER_START = false;

    private final static Logger logger = Logger.getLogger(HHDClient.class);

    /**
     * 这个值有什么用？
     */
    public volatile boolean status = true;

    /**
     * 当前状态
     */
    public volatile String currecntStatus = null;


    public String getCurrecntStatus() {
        return currecntStatus;
    }

    public void setCurrecntStatus(String currecntStatus) {
        this.currecntStatus = currecntStatus;
    }

    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(5));

    // ScheduledExecutorService:是从Java SE5的java.util.concurrent里，
    private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    private HHDClient() {

    }

    public static HHDClient getInstance() {
        return hhdClient;

    }

    public void setSocketChannel(SocketChannel socketChannel) {
        logger.info(new Date() + "\tHDD连接被重置为null？" + socketChannel == null);
        this.socketChannel = socketChannel;
    }

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
                logger.info(new Date() + "\tconnect server successful");
                HHDClient.getInstance().setCurrecntStatus("Connected");
                startPingTimer();

            }

            future.channel().closeFuture().sync();

        } finally {
            group.shutdownGracefully();
        }

    }

    public void startPingTimer() {
        try {
            synchronized (this) {
                if (!TIMER_START) {
                    sendSocketStatus();
                    scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
                        @Override
                        public void run() {
                            boolean lastCmdIsTenMintueBefore = lastCommandContext == null || lastCommandContext.command == null ||  System.currentTimeMillis() - lastCommandContext.lastCommandCommitTime.getTime() > 10 * 60 * 1000;
                            if (lastCmdIsTenMintueBefore) {
                                logger.info(new Date()+"\t定时任务发送命令 上次命令执行在十分钟之前 或者命令为空 防止设备进入充电状态");
                                sendSocketStatus();
                            } else {
                                logger.info(new Date()+"\t定时任务不发送命令 上次命令执行在十分钟之内 " + lastCommandContext);
                            }


                        }
                    }, 0, 5, TimeUnit.MINUTES);
                    TIMER_START = true;
                    logger.info(new Date()+"\t定时任务 启动成功" );
                }else{
                    logger.info(new Date()+"\t定时任务 已启动，不重复启动" );
                }
            }
        } catch (Exception e) {
            logger.error(new Date() + "\t定时任务启动失败", e);
        }
    }

    private void sendSocketStatus(){
        StringBuffer request = new StringBuffer();
        request.append("socket_status?");
        request.append("\r\n");
        HHDClient.getInstance().sendMsg(request.toString(),true);
    }


    private boolean isNotAvaiable() {
        boolean notAvaiable = socketChannel == null || socketChannel.isShutdown() || socketChannel.isOutputShutdown() || socketChannel.isInputShutdown();
        logger.info(new Date() + "\t通道是否可用 avaiable:" + !notAvaiable);
        return notAvaiable;
    }

    public void sendMsg(final String request) {
        this.sendMsg(request,false);
    }

    public void sendMsg(final String request, final boolean isFromTimer) {
        Date now = new Date();

        String currentStatus = HHDClient.getInstance().getCurrecntStatus();
        if(StringUtils.isNotBlank(currentStatus) && currentStatus.contains("设备就绪")){
           if(request.contains("socket_status?")){
               logger.info(requestTimes++ + " " + now + "\t状态为：" + HHDClient.getInstance().getCurrecntStatus() + " 状态请求被忽略 :\t" + request);
               return;
           }
        }

        logger.info(requestTimes++ + " " + now + "\tSend to HHD current status:" + HHDClient.getInstance().getCurrecntStatus() + " request :\t" + request);

        while (isNotAvaiable()) {
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        connect();
                    } catch (Exception e) {
                        logger.error("连接手持设备出错", e);
                        throw new RuntimeException("连接手持设备出错：" + e.getMessage());
                    }
                    if (isNotAvaiable()) {
                        HHDClient.getInstance().setCurrecntStatus("SocketChannel-Null");
                        throw new RuntimeException("连接手持设备失败,请求被忽略:" + request);
                    } else {
                        writeMsg(request,isFromTimer);
                    }
                }
            });
        } else {
            writeMsg(request,isFromTimer);
        }

    }

    private synchronized void writeMsg(String request,boolean isFromTimer) {
        Date now = new Date();

        try {
            if (lastCommandContext.lastCommandResponseDone) {
                socketChannel.writeAndFlush(Unpooled.copiedBuffer(request, Charset.forName("UTF-8")));
                logger.info(new Date() + "\t设备连接成功，发送请求\t" + request);
                lastCommandContext.lastCommandResponseDone = false;
                lastCommandContext.command = request;
                lastCommandContext.lastCommandCommitTime = now;
                lastCommandContext.isFromTimer = isFromTimer;
            } else {

                if (now.getTime() - lastCommandContext.lastCommandCommitTime.getTime() > 30 * 1000) {
                    logger.info(new Date() + "\t上次命令发送没回到回复，但是已经等待30s未返回结果，再次发送请求\t" + request + "\tlastCommandContext:" + lastCommandContext);
                    socketChannel.writeAndFlush(Unpooled.copiedBuffer(request, Charset.forName("UTF-8")));
                    lastCommandContext.command = request;
                    lastCommandContext.lastCommandCommitTime = now;
                    lastCommandContext.isFromTimer = isFromTimer;
                } else {
                    logger.info(new Date() + "\t上次命令发送没回到回复，此命令丢弃：" + request + "\tlastCommandContext:" + lastCommandContext);
                }

            }
        } catch (Throwable e) {
            socketChannel = null;
            logger.error(new Date() + "\tHDD设备重启了 需要重新建立连接", e);
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
