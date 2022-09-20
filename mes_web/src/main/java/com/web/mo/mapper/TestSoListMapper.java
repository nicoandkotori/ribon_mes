package com.web.mo.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.web.mo.entity.TestSoList;
import org.apache.ibatis.annotations.Param;


import java.util.List;

public interface TestSoListMapper extends BaseMapper<TestSoList> {

    int deleteByCode(TestSoList record);
    int updateByCode(TestSoList record);

    int updateByCheck(TestSoList record);


    List<TestSoList> getDetailList(@Param("main")TestSoList record);


    List<TestSoList> getDetailListByWhCode(TestSoList record);

    List<TestSoList> getPrintListDevice(TestSoList record);

    TestSoList getSumQtyByInvCode(@Param("invCode") String invCode);
    TestSoList getInfoById(@Param("id") String id,@Param("database") String database);


    List<TestSoList> getListByCode(TestSoList record);

    TestSoList getInfoByCode(TestSoList record);


    int updateyQty1(@Param("main")TestSoList record,@Param("database") String database);


    int updateSufaceWay(@Param("main")TestSoList record);

}