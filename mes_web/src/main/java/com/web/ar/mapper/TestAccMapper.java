package com.web.ar.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.web.ar.entity.TestAcc;
import com.web.ar.entity.TestAccList;

public interface TestAccMapper extends BaseMapper<TestAcc> {
    int deleteByPrimaryKey(String id);
    int deleteall();
    int insert(TestAcc record);

    int insertSelective(TestAcc record);

    TestAcc selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TestAcc record);

    int updateByPrimaryKey(TestAcc record);
}