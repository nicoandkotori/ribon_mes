package com.web.ar.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.web.ar.entity.CostSimPrice;
import com.web.ar.entity.CostSimReport;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CostSimReportMapper  extends BaseMapper<CostSimReport> {
    int deleteByPrimaryKey(String id);
    int deleteall();
    int insert(CostSimReport record);

    int insertSelective(CostSimReport record);

    CostSimReport selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(CostSimReport record);

    int updateByPrimaryKey(CostSimReport record);

    List<CostSimReport> getList(@Param("main")CostSimReport record);

    int updateMQty(CostSimReport record);
    int updateKQty(CostSimReport record);
    int updateONum(CostSimReport record);
    int updateWNum(CostSimReport record);
    int updateTotalNum(CostSimReport record);



    List<CostSimReport> getMetalList(@Param("main")CostSimReport record, @Param("database") String database);

    List<CostSimReport> getMetalListForSave(@Param("main")CostSimReport record, @Param("database") String database);

    List<CostSimReport> getDetailList(@Param("main")CostSimReport record, @Param("database") String database);
    List<CostSimReport> getMetalDetailList(@Param("main")CostSimReport record, @Param("database") String database);

    int insertSo(@Param("main") CostSimReport record, @Param("database") String database);
    int insertRd(@Param("main") CostSimReport record,@Param("database") String database);
    //成本核算报表插入
    int insertRd1(@Param("main") CostSimReport record,@Param("database") String database);

}