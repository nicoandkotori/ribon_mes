package com.web.om.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.web.ar.entity.CostSimReport;
import com.web.mo.entity.Bom;
import com.web.om.entity.OmMoDetails;
import org.apache.ibatis.annotations.Param;

public interface OmMoDetailsMapper  extends BaseMapper<OmMoDetails> {


    OmMoDetails getPrice(@Param("cinvcode") String cinvcode,@Param("database") String database);

    OmMoDetails getPriceOld1(@Param("cinvcode") String cinvcode);
    OmMoDetails getPriceOld2(@Param("cinvcode") String cinvcode);
}