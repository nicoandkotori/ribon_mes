package com.web.sa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.web.sa.entity.OaSaleContract;
import com.web.sa.entity.OaSaleContractContrast;
import com.web.sa.entity.OaSaleContractContrastDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OaSaleContractContrastDetailMapper  extends BaseMapper<OaSaleContractContrastDetail> {

    List<OaSaleContractContrastDetail> getList(@Param("main") OaSaleContractContrastDetail mainDTO, @Param("database") String database);

    int updateSoCode(@Param("main") OaSaleContractContrastDetail record, @Param("database") String database);
}