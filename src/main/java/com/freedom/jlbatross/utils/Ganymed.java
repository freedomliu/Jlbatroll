package com.freedom.jlbatross.utils;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import com.freedom.jlbatross.config.netty.ChannelSupervise;
import org.apache.maven.shared.utils.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

/**
 * @Decription:
 * @Author: liuxiangtao90
 * @CreateDate: Created in 2020/1/5 21:18
 * @Return:
 */
@Component
public class Ganymed {

    private static final Logger log = Logger.getLogger(Ganymed.class.getName());

    private Connection login(String ip, int port, String username, String password) {
        Connection connection = null;
        try {
            connection = new Connection(ip, port);
            connection.connect();// 连接
            boolean flag = connection.authenticateWithPassword(username, password);// 认证
            if (flag) {
                ChannelSupervise.send2All("----" + ip + "登录成功----");
                return connection;
            }
        } catch (IOException e) {
            ChannelSupervise.send2All("----" + ip + "登录失败----");
            ChannelSupervise.send2All(e.getMessage());
            e.printStackTrace();
            connection.close();
        }
        return connection;
    }

    /**
     *   * 远程执行shell脚本或者命令
     *   * 
     *   * @param cmd
     *   *   即将执行的命令
     *   * @return 命令执行完后返回的结果值
     *  
     */
    private static String execmd(Connection connection, String cmd) {
        String result = "";
        try {
            if (connection != null) {
                Session session = connection.openSession();// 打开一个会话
                session.execCommand(cmd);// 执行命令
                result = processStdout(session.getStdout(), "UTF-8");
                // 如果为得到标准输出为空，说明脚本执行出错了
                if(StringUtils.isBlank(result)) {
                    ChannelSupervise.send2All("执行:" + cmd + ";" + result);
                    result = processStdout(session.getStderr(), "UTF-8");
                } else {
                    ChannelSupervise.send2All("执行成功：" + cmd + "--" + connection);
                }
                connection.close();
                session.close();
            }
        } catch (IOException e) {
            ChannelSupervise.send2All("执行失败：" + cmd + ";" + e);
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 解析脚本执行返回的结果集
     *  
     *
     * @param in                 输入流对象
     * @param charset            编码
     * @return 以纯文本的格式返回
     */
    private static String processStdout(InputStream in, String charset) {
        InputStream stdout = new StreamGobbler(in);
        StringBuffer buffer = new StringBuffer();
        ;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout, charset));
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line + "\n");
                System.out.println(line);
                ChannelSupervise.send2All(line);
            }
            br.close();
        } catch (Exception e) {
            ChannelSupervise.send2All("解析脚本出错：" + e.getMessage());
            e.printStackTrace();
        }
        return buffer.toString();
    }

    public void putFile(Connection conn, String localFile, String remoteTargetDirectory) {
        try {
            SCPClient client = conn.createSCPClient();//new SCPClient(conn);
            client.put(localFile, 512, remoteTargetDirectory, null);
            ChannelSupervise.send2All("上传成功：" + localFile + "---->" + remoteTargetDirectory);
        } catch (Exception e) {
            ChannelSupervise.send2All("上传异常：" + e.getMessage());
            e.printStackTrace();
        } finally {
            conn.close();
        }
    }

    private void closeConn(Connection conn){
        if(conn != null) {
            conn.close();
        }
    }
}
