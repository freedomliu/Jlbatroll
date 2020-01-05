package com.freedom.jlbatross;

import com.freedom.jlbatross.config.netty.WebSocketServer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JlbatrossApplication implements CommandLineRunner {

    @Autowired
    WebSocketServer webSocketServer;

    public static void main(String[] args) {
        SpringApplication.run(JlbatrossApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Channel channel = webSocketServer.run(8081);
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                //这里添加一个websocket的shutdown
                webSocketServer.destroy();
            }
        });
    }
}
