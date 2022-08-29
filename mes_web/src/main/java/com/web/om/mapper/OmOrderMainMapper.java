package com.web.om.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.web.om.dto.OmOrderMainDTO;
import com.web.om.entity.OmOrderMain;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OmOrderMainMapper  extends BaseMapper<OmOrderMain> {

    List<OmOrderMainDTO> getMainList(IPage<OmOrderMainDTO> page, @Param("main") OmOrderMainDTO mainDTO);

    List<OmOrderMainDTO> getDetailList(@Param("main")OmOrderMainDTO record);

}