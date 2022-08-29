package com.web.om.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.web.mo.entity.U8MpsNetdemand;
import com.web.om.dto.OmProductVM;
import com.web.om.entity.OmMoDetails;
import com.web.om.entity.OmMoMain;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OmMoMainMapper   extends BaseMapper<OmMoMain> {

    List<OmProductVM> getMainList(IPage<OmProductVM> page, @Param("main") OmProductVM mainDTO);

    List<OmProductVM> getDetailList(OmProductVM record);

    List<OmProductVM> getDetailList1(OmProductVM record);
    List<OmProductVM> getDetailList2(OmProductVM record);

    OmProductVM getHistoryByCinvcode(OmProductVM record);

    List<OmProductVM> getHistoryListByCinvcode(OmProductVM record);

    int updateUnCheck(OmMoMain record);
//    Integer  getMaxId();
}