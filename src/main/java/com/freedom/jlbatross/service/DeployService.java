package com.freedom.jlbatross.service;

import com.freedom.jlbatross.config.netty.ChannelSupervise;
import com.freedom.jlbatross.utils.SSHRemoteCall;
import com.jcraft.jsch.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

/**
 * @Auther: Administrator
 * @Date: 2020/1/5 19:08
 * @Description:
 */
@Service
public class DeployService {

    private static final Logger log = Logger.getLogger(DeployService.class.getName());

    @Autowired
    SSHRemoteCall sshRemoteCall;

    public void deploy() {
        String ip = "192.168.29.129";
        ChannelSupervise.send2All(ip + "连接中...");
        Session session = sshRemoteCall.login(ip, 22, "root", "liuxiangtao");
        if(session == null) {
            ChannelSupervise.send2All("登录" + ip + "失败");
        }
        ChannelSupervise.send2All("登录" + ip + "成功");
        ChannelSupervise.send2All("开始上传");
        boolean result = sshRemoteCall.uploadFile(session,"/usr/11.txt", "D://11.txt");
        if(result){
            ChannelSupervise.send2All("上传成功");
        } else {
            ChannelSupervise.send2All("上传失败");
        }
        sshRemoteCall.close(session);
    }
}
