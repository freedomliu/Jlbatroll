package com.freedom.jlbatross.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.Vector;

import com.freedom.jlbatross.config.netty.ChannelSupervise;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import org.springframework.stereotype.Component;

/**
 * @Decription: https://cloud.tencent.com/developer/article/1354612
 * @Author: liuxiangtao90
 * @CreateDate: Created in 2020/1/5 21:06
 * @Return:
 */
@Component
public class SSHRemoteCall {
    // 私有的对象
    private static SSHRemoteCall sshRemoteCall;


    /**
     * 远程登陆
     *
     * @throws Exception
     */
    public Session login(String ipAddress, int port, String userName, String password) {
        Session session = null;
        // 创建jSch对象
        JSch jSch = new JSch();
        try {
            // 获取到jSch的session, 根据用户名、主机ip、端口号获取一个Session对象
            session = jSch.getSession(userName, ipAddress, port);
            // 设置密码
            session.setPassword(password);
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);// 为Session对象设置properties
            session.connect();//// 通过Session建立连接
        } catch (JSchException e) {
            e.printStackTrace();
        }
        return session;
    }

    /**
     * 关闭连接
     */
    public void close(Session session) {
        // 调用session的关闭连接的方法
        if (session != null) {
            // 如果session不为空,调用session的关闭连接的方法
            session.disconnect();
        }

    }

    /**
     * 执行相关的命令
     *
     * @param command
     * @throws IOException
     */
    public void execCommand(Session session,String command) throws IOException {
        InputStream in = null;// 输入流(读)
        Channel channel = null;// 定义channel变量
        try {
            // 如果命令command不等于null
            if (command != null) {
                // 打开channel
                //说明：exec用于执行命令;sftp用于文件处理
                channel = session.openChannel("exec");
                // 设置command
                ((ChannelExec) channel).setCommand(command);
                // channel进行连接
                channel.connect();
                // 获取到输入流
                in = channel.getInputStream();
                // 执行相关的命令
                String processDataStream = processDataStream(in);
                // 打印相关的命令
                System.out.println("、打印相关返回的命令: " + processDataStream);
            }
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                in.close();
            }
            if (channel != null) {
                channel.disconnect();
            }
        }

    }

    /**
     * 对将要执行的linux的命令进行遍历
     *
     * @param in
     * @return
     * @throws Exception
     */
    public String processDataStream(InputStream in) throws Exception {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String result = "";
        try {
            while ((result = br.readLine()) != null) {
                sb.append(result);
                // System.out.println(sb.toString());
            }
        } catch (Exception e) {
            throw new Exception("获取数据流失败: " + e);
        } finally {
            br.close();
        }
        return sb.toString();
    }

    /**
     * 上传文件
     *
     * @param directory  上传文件的目录
     * @param localFile 将要上传的文件
     */
    public boolean uploadFile(Session session,String directory, String localFile) {
        try {
            // 打开channelSftp
            ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
            // 远程连接
            channelSftp.connect();
            // 创建一个文件名称为uploadFile的文件
            File file = new File(localFile);
            // 将文件进行上传(sftp协议)
            // 将本地文件名为src的文件上传到目标服务器,目标文件名为dst,若dst为目录,则目标文件名将与src文件名相同.
            // 采用默认的传输模式:OVERWRITE
            channelSftp.put(new FileInputStream(file), directory, ChannelSftp.OVERWRITE);
            // 切断远程连接
            channelSftp.exit();
        } catch (Exception e) {
            e.printStackTrace();
            ChannelSupervise.send2All(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 下载文件 采用默认的传输模式：OVERWRITE
     *
     * @param src linux服务器文件地址
     * @param dst 本地存放地址
     * @throws JSchException
     * @throws SftpException
     */
    public void fileDownload(Session session, String src, String dst) throws JSchException, SftpException {
        // src 是linux服务器文件地址,dst 本地存放地址
        ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
        // 远程连接
        channelSftp.connect();
        // 下载文件,多个重载方法
        channelSftp.get(src, dst);
        // 切断远程连接,quit()等同于exit(),都是调用disconnect()
        channelSftp.quit();
        // channelSftp.disconnect();
    }

    /**
     * 删除文件
     *
     * @param session
     * @param directoryFile
     * @throws SftpException
     * @throws JSchException
     */
    public void deleteFile(Session session, String directoryFile) throws SftpException, JSchException {
        // 打开openChannel的sftp
        ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
        // 远程连接
        channelSftp.connect();
        // 删除文件
        channelSftp.rm(directoryFile);
        // 切断远程连接
        channelSftp.exit();
    }

    /**
     * 列出目录下的文件
     *
     * @param directory 要列出的目录
     * @param session
     * @param directory
     * @return
     * @throws SftpException
     * @throws JSchException
     */
    public Vector listFiles(Session session, String directory) throws JSchException, SftpException {
        ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
        // 远程连接
        channelSftp.connect();
        // 显示目录信息
        Vector ls = channelSftp.ls(directory);
        // 切断连接
        channelSftp.exit();
        return ls;
    }
}