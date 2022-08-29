package com.web.mo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.web.mo.entity.TestSo;
import com.web.mo.entity.U8MpsNetdemand;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface U8MpsNetdemandMapper  extends BaseMapper<U8MpsNetdemand> {

    List<U8MpsNetdemand> getList(IPage<U8MpsNetdemand> page, @Param("main") U8MpsNetdemand mainDTO);
    List<U8MpsNetdemand> getListForPrint(IPage<U8MpsNetdemand> page, @Param("main") U8MpsNetdemand mainDTO, @Param("database") String database);

    List<U8MpsNetdemand> getPrintProduct(@Param("main") U8MpsNetdemand record);


}