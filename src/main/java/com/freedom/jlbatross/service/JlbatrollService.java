package com.freedom.jlbatross.service;

import com.freedom.jlbatross.config.netty.ChannelSupervise;
import com.freedom.jlbatross.config.netty.WebSocketServer;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collections;

/**
 * @Auther: Administrator
 * @Date: 2020/1/5 10:56
 * @Description:
 */
@Service
public class JlbatrollService {

    @Autowired
    WebSocketServer webSocketServer;

    public void install(){
        PrintStream oldPrintStream = System.out; //将原来的System.out交给printStream 对象保存
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bos)); //设置新的out

        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile( new java.io.File( "D:/IdeaProjects/jlbatross/pom.xml" ) );
        request.setGoals( Collections.singletonList( "install" ) );
        DefaultInvoker invoker = new DefaultInvoker();
        invoker.setMavenHome(new java.io.File("D:/workspace/apache-maven-3.3.3"));
        try {
            ChannelSupervise.send2All(new TextWebSocketFrame("----开始打包----"));
            invoker.execute( request );
        }
        catch (MavenInvocationException e) {
            ChannelSupervise.send2All(new TextWebSocketFrame("----系统异常----"));
            e.printStackTrace();
            ChannelSupervise.send2All(new TextWebSocketFrame(e.getMessage()));
        }

        System.setOut(oldPrintStream); //恢复原来的System.out
        ChannelSupervise.send2All(new TextWebSocketFrame(bos.toString()));
        ChannelSupervise.send2All(new TextWebSocketFrame("----打包完成----"));
    }
}
