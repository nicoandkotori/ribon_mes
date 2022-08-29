package com.web.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.web.demo.entity.TreeTableList;
import com.web.demo.mapper.TreeTableMapper;
import com.web.demo.service.ITreeTableService;
import org.springframework.stereotype.Service;

/**
 * Created by Li on 2019/11/14.
 */
@Service
public class TreeTableServiceImpl extends ServiceImpl<TreeTableMapper, TreeTableList> implements ITreeTableService {
}
