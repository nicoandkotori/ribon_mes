package com.web.sa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.web.basicinfo.entity.OaInventoryApplyDetail;
import com.web.sa.entity.OaSaleContract;
import com.web.sa.entity.OaSaleContractDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OaSaleContractMapper  extends BaseMapper<OaSaleContract> {
    List<OaSaleContract> getListByMainSync(@Param("main") OaSaleContract mainDTO, @Param("database") String database);


    List<OaSaleContract> getListBySync(@Param("main") OaSaleContract mainDTO, @Param("database") String database);
    List<OaSaleContract> getListBySyncEdit(@Param("main") OaSaleContract mainDTO, @Param("database") String database);

}