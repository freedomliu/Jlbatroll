package com.freedom.jlbatross.controller;

import com.freedom.jlbatross.entity.SystemConfig;
import com.freedom.jlbatross.mapper.SystemConfigMapper;
import com.freedom.jlbatross.utils.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Auther: liuxiangtao90
 * @Date: 2020/1/16 17:41
 * @Description:
 */
@Controller
@RequestMapping
public class SystemConfigController {

    @Autowired
    SystemConfigMapper systemConfigMapper;

    @GetMapping("/systemConfig")
    public ModelAndView systemConfig(ModelAndView modelAndView) {
        SystemConfig systemConfig = systemConfigMapper.selectById(1);
        modelAndView.addObject("config", systemConfig);
        modelAndView.setViewName("system_config");
        return modelAndView;
    }

    @PostMapping("sysConfig")
    @ResponseBody
    public JSONResult insertConfig(SystemConfig config) {
        config.setId(1);
        int insert = systemConfigMapper.updateById(config);
        return JSONResult.updateResult(insert);
    }

    @PutMapping("sysConfig")
    @ResponseBody
    public JSONResult updateConfig(SystemConfig config) {
        int updateCount = systemConfigMapper.updateById(config);
        return JSONResult.updateResult(updateCount);
    }
}
