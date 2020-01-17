package com.freedom.jlbatross.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.freedom.jlbatross.entity.Node;
import com.freedom.jlbatross.entity.NodeItem;
import com.freedom.jlbatross.mapper.NodeItemMapper;
import com.freedom.jlbatross.mapper.NodeMapper;
import com.freedom.jlbatross.utils.JSONResult;
import com.freedom.jlbatross.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * @Auther: liuxiangtao90
 * @Date: 2020/1/16 14:15
 * @Description:
 */
@Controller
@RequestMapping
public class NodeController {

    @Autowired
    NodeItemMapper nodeItemMapper;

    @Autowired
    NodeMapper nodeMapper;

    @GetMapping("node")
    public ModelAndView node(ModelAndView modelAndView) {
        modelAndView.setViewName("node");
        return modelAndView;
    }

    @GetMapping("node/list")
    @ResponseBody
    public Map<String, Object> list(@RequestParam Integer page, @RequestParam Integer limit) {
        IPage<Node> pageNpde = new Page<>(page, limit);
        IPage<Node> nodeIPage = nodeMapper.selectPage(pageNpde, new QueryWrapper<>());
        return PageResult.getPageMap(nodeIPage);
    }

    @PostMapping("node")
    @ResponseBody
    public JSONResult insertNode(NodeItem node) {
        int i = nodeItemMapper.insert(node);
        return JSONResult.updateResult(1);
    }

    @DeleteMapping("node/{id}")
    @ResponseBody
    public JSONResult deleteNode(@PathVariable Integer id) {
        int i = nodeMapper.deleteById(id);
        return JSONResult.updateResult(i);
    }

    @GetMapping("node/{id}")
    @ResponseBody
    public NodeItem getNode(@PathVariable Integer id) {
        NodeItem node = nodeItemMapper.selectById(id);
        return node;
    }

    @GetMapping("node/item")
    public ModelAndView nodeItem(ModelAndView modelAndView, @PathVariable Integer id) {
        Node node = nodeMapper.selectById(id);
        modelAndView.addObject("nodeName", node.getName());

        nodeItemMapper.selectList();

        modelAndView.setViewName("node_item");
        return modelAndView;
    }

    @GetMapping("node/item/add")
    public ModelAndView insertNode(ModelAndView modelAndView) {
        modelAndView.setViewName("node_item_add");
        return modelAndView;
    }
}
