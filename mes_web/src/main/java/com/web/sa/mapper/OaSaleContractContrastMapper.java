package com.web.sa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.web.basicinfo.entity.OaInventoryApplyDetail;
import com.web.sa.entity.OaSaleContractContrast;
import com.web.sa.entity.OaSaleContractDetail;
import org.apache.ibatis.annotations.Param;

public interface OaSaleContractContrastMapper extends BaseMapper<OaSaleContractContrast> {


    int updateSoCode(@Param("main") OaSaleContractContrast record, @Param("database") String database);
}