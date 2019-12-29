package com.tibco.integration.hhd;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HHDClientHandler extends SimpleChannelInboundHandler {

    private Logger logger = Logger.getLogger(this.getClass());


    private HHDResponseHandler hhdResponseHandler = new HHDResponseHandler();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        ctx.writeAndFlush(Unpooled.copiedBuffer("this is from client : active", Charset.forName("UTF-8")));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        HHDClient.lastCommandResponseDone = true;
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "UTF-8");
        logger.info("HHD server response is : " + body);

        String[] response = body.split("\r\n");
        Map<String, String> responseMap = new HashMap<>();
        for (String rep : response) {
            String row[] = rep.split("=");
            if (row.length != 2) {
                continue;
            }
            String key = row[0];
            String value = row[1];
            responseMap.put(key, value.replace("'", ""));
        }

        hhdResponseHandler.handler(responseMap);

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        HHDClient.lastCommandResponseDone = true;
        logger.error("HHD server exception is : ", cause);
        //远程主机强迫关闭了一个现有的连接
        if (cause instanceof IOException && (cause.getMessage().contains("Connection reset by peer") || cause.getMessage().contains("远程主机"))) {
            //WIFI 重启了
            HHDClient.getInstance().setSocketChannel(null);
            HHDClient.getInstance().setCurrecntStatus(null);
        }
        super.exceptionCaught(ctx, cause);
    }
}
