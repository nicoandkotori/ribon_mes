package com.web.ar.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.common.util.ResponseResult;
import com.web.ar.entity.CostSimReport;
import com.web.ar.entity.TestAcc;
import com.web.mo.entity.TestSoList;

import java.util.List;

/**
 *
 * @author caihuan
 * @since 2022-06-28
 */
public interface ICostSimReportService extends IService<CostSimReport> {

    List<CostSimReport> getList(CostSimReport query) throws Exception;
    List<CostSimReport> getMetalList(CostSimReport query) throws Exception;
    List<CostSimReport> getMetalListForSave(CostSimReport query) throws Exception;
    List<CostSimReport> getDetailList(CostSimReport query) throws Exception;
    List<CostSimReport> getMetalDetailList(CostSimReport query) throws Exception;

    ResponseResult delete() throws Exception;

    ResponseResult insertRd(CostSimReport query) throws Exception;
    ResponseResult insertRd1(CostSimReport query) throws Exception;

    ResponseResult insertSo(CostSimReport query) throws Exception;

    ResponseResult saveprice(List<CostSimReport> list) throws Exception;
}
