package com.web.ar.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.common.util.ResponseResult;
import com.web.ar.entity.CostSimReport;
import com.web.ar.entity.TestAcc;
import com.web.mo.entity.U8MpsNetdemand;
import com.web.sa.entity.OaSaleContractDetail;

import java.util.List;

/**
 *
 * @author caihuan
 * @since 2022-06-28
 */
public interface ITestAccService extends IService<TestAcc> {

    ResponseResult save(List<CostSimReport> list,String istate) throws Exception;
    ResponseResult save1(List<CostSimReport> list,String istate) throws Exception;
    ResponseResult saveByInvCode(String invCode) throws Exception;

}
