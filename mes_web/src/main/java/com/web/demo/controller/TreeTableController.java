package com.web.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.web.demo.entity.TreeTableList;
import com.web.demo.entity.TreeTableResult;
import com.web.demo.service.ITreeTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Li on 2019/11/14.
 */
@RestController
@RequestMapping(value = "/demo/treetable")
public class TreeTableController {

    @Autowired
    private ITreeTableService treeTableService;

    @RequestMapping(value = "/findlist")
    public TreeTableResult findList() {
        TreeTableResult tree = new TreeTableResult();

        LambdaQueryWrapper<TreeTableList> select=new LambdaQueryWrapper<>();
        select.orderByAsc(TreeTableList::getId);
        List<TreeTableList> treeList = treeTableService.list(select);

        tree.setData(treeList);
        return tree;
    }

}
