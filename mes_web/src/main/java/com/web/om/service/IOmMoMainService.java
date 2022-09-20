package com.web.om.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.common.util.ResponseResult;
import com.web.mo.dto.BomDTO;
import com.web.mo.entity.TestSo;
import com.web.mo.entity.U8MpsNetdemand;
import com.web.om.dto.OmOrderPartDTO;
import com.web.om.dto.OmProductVM;
import com.web.om.entity.OmMoMain;
import com.web.om.entity.OmOrderMain;
import com.web.om.entity.OmOrderPart;

import java.util.List;
import java.util.Map;

/**
 *
 * @author caihuan
 * @since 2022-06-28
 */
public interface IOmMoMainService extends IService<OmMoMain> {
    IPage<OmProductVM> getMainList(OmProductVM query, IPage<OmProductVM> page) throws Exception;

    List<OmProductVM> getDetailList(OmProductVM query) throws Exception;
    Map<String, Object>  getDetailList1(OmProductVM query) throws Exception;
    Map<String, Object> getHistoryByCinvcode1(OmProductVM query) throws Exception;

    ResponseResult save1(OmMoMain main, List<OmProductVM> list, List<OmProductVM>  listDetail) throws Exception;

    ResponseResult deleteByDetailMainId(Integer id,Integer mainid,Integer subid) throws Exception;
    ResponseResult deleteByDetailSubId(Integer id,Integer mainid,Integer subid) throws Exception;
    ResponseResult delete(Integer id) throws Exception;
    ResponseResult check(Integer id) throws Exception;
    ResponseResult unCheck(Integer u8Id) throws Exception;
}
