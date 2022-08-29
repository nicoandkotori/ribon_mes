package com.web.st.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.web.mo.entity.TestSoList;
import com.web.st.entity.CurrentStock;
import org.apache.ibatis.annotations.Param;

public interface CurrentStockMapper  extends BaseMapper<CurrentStock> {


    CurrentStock  getSumQtyByInvCode(@Param("invCode") String invCode,@Param("database") String database);
}