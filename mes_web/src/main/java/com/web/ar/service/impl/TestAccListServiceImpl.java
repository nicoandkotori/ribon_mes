package com.web.ar.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.util.ResponseResult;
import com.web.ar.entity.TestAcc;
import com.web.ar.entity.TestAccList;
import com.web.ar.mapper.TestAccListMapper;
import com.web.ar.mapper.TestAccMapper;
import com.web.ar.service.ITestAccListService;
import com.web.ar.service.ITestAccService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author caihuan
 * @since 2022-06-28
 */
@Service
public class TestAccListServiceImpl extends ServiceImpl<TestAccListMapper, TestAccList> implements ITestAccListService {




    @Autowired
    TestAccListMapper testAccListMapper;


    /**
     * 删除全部数据
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult delete() throws Exception{
        ResponseResult result = new ResponseResult();
        try {

            testAccListMapper.deleteall();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }
}
