package com.freedom.jlbatross.config.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;

import java.util.logging.Level;
import java.util.logging.Logger;

import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;

/**
 * @Auther: Administrator
 * @Date: 2020/1/4 16:12
 * @Description:
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {
    private static final Logger log = Logger.getLogger(WebSocketServerHandler.class.getName());

    private WebSocketServerHandshaker handshaker;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //添加连接
        log.info("客户端加入连接：" + ctx.channel());
        ChannelSupervise.addChannel(ctx.channel());
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
        //  传统的HTTP接入
        if(msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        }
        // Websocket接入
        else if (msg instanceof WebSocketFrame) {
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    @Override
    public void  channelReadComplete(ChannelHandlerContext ctx){
        ctx.flush();
    }

    private void  handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req)  throws Exception{
        // 如果HTTP解码失败，返回异常信息
        if(!req.getDecoderResult().isSuccess() || (!"websocket".equals(req.headers().get("Upgrade")))) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }
        // 构造握手响应返回
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory("ws://localhost:8081", null, false);
        handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
        }
    }

    private  void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame){
        // 判断是否关闭链路的指令
        if(frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }
        // 判断是否是pnig的指令
        if(frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        //暂时只支持文本消息 文本消息
        if(!(frame instanceof TextWebSocketFrame)) {
            System.out.println("消息类型暂不支持");
            return;
        }
        // 返回应答消息
        String request =((TextWebSocketFrame) frame).text();
        if(log.isLoggable(Level.FINE)){
            log.fine(String.format("%s received  %s", ctx.channel(), request));
        }
        ctx.channel().write(new TextWebSocketFrame(request + ",这是netty返回消息"));
    }

    private static void sendHttpResponse (ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res){
        // 返回给客户端
        if(res.getStatus().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.getStatus().toString(), CharsetUtil.UTF_8);
            System.out.println("看这里");
            // sentContentLength(res, res.content().readableBytes());
        }

        // 如果是非keep-alive 关闭连接
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!isKeepAlive(req) || res.getStatus().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public  void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception{
        cause.printStackTrace();
        ctx.close();
    }
}
