package com.web.mo.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.web.mo.entity.TestSo;
import com.web.mo.entity.TestSoList;
import org.apache.ibatis.annotations.Param;

public interface TestSoMapper  extends BaseMapper<TestSo> {

    int insertTestSoList(@Param("main") TestSo record);

    int deleteByUser(@Param("createUser") String createUser);

    int updatePartName1(TestSo record);
    int updatePartName2(TestSo record);
    int updatePartName3(TestSo record);
    int updateMatSize1(TestSo record);
    int updateMatSize2(TestSo record);
    int updateMatSize3(TestSo record);

}