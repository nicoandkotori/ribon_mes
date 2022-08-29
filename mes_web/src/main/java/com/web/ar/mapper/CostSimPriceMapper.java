package com.web.ar.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.web.ar.entity.CostSimPrice;
import com.web.ar.entity.CostSimReport;
import com.web.om.entity.OmMoDetails;

public interface CostSimPriceMapper  extends BaseMapper<CostSimPrice> {
    int deleteByPrimaryKey(String id);

    int insert(CostSimPrice record);

    int insertSelective(CostSimPrice record);

    CostSimPrice selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(CostSimPrice record);

    int updateByPrimaryKey(CostSimPrice record);

    CostSimPrice getInfo(CostSimReport record);
}