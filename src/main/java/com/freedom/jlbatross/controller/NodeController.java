package com.freedom.jlbatross.controller;

import com.freedom.jlbatross.entity.Node;
import com.freedom.jlbatross.mapper.NodeMapper;
import com.freedom.jlbatross.utils.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Auther: liuxiangtao90
 * @Date: 2020/1/16 14:15
 * @Description:
 */
@Controller
@RequestMapping
public class NodeController {

    @Autowired
    NodeMapper nodeMapper;

    @GetMapping("node")
    public ModelAndView node(ModelAndView modelAndView) {
        modelAndView.setViewName("node");
        return modelAndView;
    }

    @PostMapping("node")
    @ResponseBody
    public JSONResult insertNode(Node node) {
        int i = nodeMapper.insert(node);
        return JSONResult.updateResult(1, "操作成功");
    }

    @DeleteMapping("node/{id}")
    @ResponseBody
    public JSONResult deleteNode(@PathVariable Integer id) {
        int i = nodeMapper.deleteById(id);
        return JSONResult.updateResult(i, "操作成功");
    }

    @GetMapping("node/{id}")
    @ResponseBody
    public Node getNode(@PathVariable Integer id) {
        Node node = nodeMapper.selectById(id);
        return node;
    }
}
