package com.web.sa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.web.sa.entity.OaSaleContract;
import com.web.sa.entity.OaSaleContractContrast;
import com.web.sa.entity.SoDetails;
import org.apache.ibatis.annotations.Param;

public interface SoDetailsMapper extends BaseMapper<SoDetails> {

    int updateRate(@Param("main") SoDetails record);
}
