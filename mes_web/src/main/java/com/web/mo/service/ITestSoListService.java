package com.web.mo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.common.util.ResponseResult;
import com.web.basicinfo.entity.CustomerExtradefine;
import com.web.mo.entity.TestSo;
import com.web.mo.entity.TestSoList;

import java.util.List;

/**
 *
 * @author caihuan
 * @since 2022-06-28
 */
public interface ITestSoListService extends IService<TestSoList> {
    List<TestSoList> getDetailList(TestSoList query) throws Exception;
    List<TestSoList> getPrintListDevice(TestSoList query) throws Exception;
    List<TestSoList> getDetailListByWhCode(TestSoList query) throws Exception;

    ResponseResult delete(TestSoList m) throws Exception;

    ResponseResult saveU8(List<TestSoList> list) throws Exception;
    ResponseResult updateSufaceWay(List<TestSoList> list) throws Exception;

}
