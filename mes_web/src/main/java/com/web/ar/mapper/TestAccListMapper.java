package com.web.ar.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.web.ar.entity.CostSimReport;
import com.web.ar.entity.TestAccList;

public interface TestAccListMapper extends BaseMapper<TestAccList> {
    int deleteByPrimaryKey(String id);
    int deleteall();
    int insert(TestAccList record);

    int insertSelective(TestAccList record);

    TestAccList selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TestAccList record);

    int updateByPrimaryKey(TestAccList record);

    int insertTestAccList(CostSimReport costSimReport);
}