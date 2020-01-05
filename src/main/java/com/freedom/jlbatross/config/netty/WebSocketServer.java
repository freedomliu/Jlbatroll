package com.freedom.jlbatross.config.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

/**
 * @Auther: liuxiangtao90
 * @Date: 2020/1/4 15:47
 * @Description:
 */
@Component
public class WebSocketServer {
    private static final Logger log = Logger.getLogger(WebSocketServer.class.getName());

    //其中 boosGroup 用于 Accetpt 连接建立事件并分发请求，workerGroup 用于处理 I/O 读写事件和业务逻辑
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    // 创建工作线程组
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    private Channel channel;

    public Channel run(int port){
        try {
            //ServerBootstrap负责初始化netty服务器，并且开始监听端口的socket请求\
            // 服务端启动引导类
            ServerBootstrap b = new ServerBootstrap();
            // NioServerSocketChannel，异步的服务器端 TCP Socket 连接。
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    // 配置入站、出站事件handler
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception{
                        // 配置入站、出站事件channel
                        ch.pipeline().addLast("http-codec", new HttpServerCodec());//设置解码器
                        ch.pipeline().addLast("aggregator", new HttpObjectAggregator(65536));//聚合器，使用websocket会用到
                        ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());//用于大数据的分区传输
                        ch.pipeline().addLast("handler", new WebSocketServerHandler());//自定义的业务handler
                    }
                    });
            channel = b.bind(port).sync().channel();
            System.out.println("web socket server started at port:" + port);
            channel.closeFuture().sync();
            //服务端管道关闭的监听器并同步阻塞,直到channel关闭,线程才会往下执行,结束进程
            //channel.closeFuture().syncUninterruptibly();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return channel;
    }

    /**
     * 停止服务
     */
    public void destroy() {
        log.info("Shutdown Netty Server...");
        if(channel != null) { channel.close();}
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
        log.info("Shutdown Netty Server Success!");
    }
}
