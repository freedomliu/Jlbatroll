package com.freedom.jlbatross.controller;

import com.freedom.jlbatross.config.netty.WebSocketServer;
import com.freedom.jlbatross.service.DeployService;
import com.freedom.jlbatross.service.JlbatrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Auther: Administrator
 * @Date: 2020/1/4 08:56
 * @Description:
 */
@Controller
public class JlbatrollController {

    @Autowired
    WebSocketServer webSocketServer;

    @Autowired
    JlbatrollService jlbatrollService;

    @Autowired
    DeployService ganymedService;

    @GetMapping("/")
    public ModelAndView console(ModelAndView modelAndView) {
        modelAndView.setViewName("console");
        return modelAndView;
    }

    @GetMapping("/automation")
    public void automation(){
        jlbatrollService.install();
    }

    @GetMapping("deploy")
    public void sendMst(ModelAndView modelAndView) {
        ganymedService.deploy();
    }
}
