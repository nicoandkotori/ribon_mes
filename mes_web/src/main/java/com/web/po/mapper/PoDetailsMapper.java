package com.web.po.mapper;

import com.web.po.entity.PoDetails;
import org.apache.ibatis.annotations.Param;

public interface PoDetailsMapper {
    PoDetails getSumQtyByInvCode(@Param("invCode") String invCode,@Param("database") String database);
}