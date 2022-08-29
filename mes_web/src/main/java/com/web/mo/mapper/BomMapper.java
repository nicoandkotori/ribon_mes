package com.web.mo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.web.mo.dto.BomDTO;
import com.web.mo.entity.Bom;
import com.web.mo.entity.TestSo;
import com.web.mo.entity.U8MpsNetdemand;
import org.apache.ibatis.annotations.Param;import java.util.List;

public interface BomMapper extends BaseMapper<Bom> {

    List<BomDTO> getList(IPage<BomDTO> page, @Param("main") BomDTO mainDTO);

    List<BomDTO> selectParentMenuListByInv(@Param("invCode") String invCode, @Param("database") String database);

    List<BomDTO> selectByMenu1(@Param("invCode") String invCode, @Param("database") String database);

    Integer getBomCountByParentInvCode(@Param("invCode") String invCode);

    Integer getBomChildCountByInvCode(@Param("parentInvCode") String parentInvCode, @Param("invCode") String invCode);
}