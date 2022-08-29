package com.web.mo.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.util.CustomStringUtils;
import com.common.util.ResponseResult;
import com.common.util.TableResult;
import com.modules.data.mybatis.DBTypeEnum;
import com.modules.data.mybatis.DbContextHolder;
import com.web.mo.entity.TestSoList;
import com.web.mo.entity.U8MpsNetdemand;
import com.web.mo.service.IMpsNetdemandService;
import com.web.mo.service.ITestSoService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * @author caihuan
 * @create 2019-07-02 15:07
 */
@Controller
@RequestMapping("/mo/srpmainproduct")
public class SrpMainProductController {
    @Autowired
    private IMpsNetdemandService mpsNetdemandService;
    @Autowired
    private ITestSoService testSoService;

    //查询mrp运算单列表
    @RequestMapping(value = "/getmrplistbypage")
    @ResponseBody
    public TableResult<U8MpsNetdemand> getChoiceListByPage(int page, int rows, String querystr){
        TableResult<U8MpsNetdemand> result = new TableResult<>();
        IPage<U8MpsNetdemand> page1 = new Page<>(page, rows);
        IPage<U8MpsNetdemand> resultPage = new Page<>(page, rows);
        try {
            U8MpsNetdemand data = JSON.parseObject(querystr, U8MpsNetdemand.class);

            if(data == null){
                data = new U8MpsNetdemand();
            }
            if (data.getDateEnd() != null) {
                Calendar c = Calendar.getInstance();
                c.setTime(data.getDateEnd());
                c.add(Calendar.DAY_OF_MONTH, 1);
                data.setDateEnd(c.getTime());
            }

            DbContextHolder.setDbType(DBTypeEnum.db2);
            resultPage = mpsNetdemandService.getList(data,page1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        result.setRecords(resultPage.getTotal());
        result.setTotal((int)Math.ceil(resultPage.getTotal()/(double)resultPage.getSize()));
        result.setRows(resultPage.getRecords());
        return result;
    }




    //查询mrp运算单列表
    @RequestMapping(value = "/getmrpprintlistbypage")
    @ResponseBody
    public TableResult<U8MpsNetdemand> getChoicePrintListByPage(int page, int rows, String querystr){
        TableResult<U8MpsNetdemand> result = new TableResult<>();
        IPage<U8MpsNetdemand> page1 = new Page<>(page, rows);
        IPage<U8MpsNetdemand> resultPage = new Page<>(page, rows);
        try {
            U8MpsNetdemand data = JSON.parseObject(querystr, U8MpsNetdemand.class);

            if(data == null){
                data = new U8MpsNetdemand();
            }
            if (data.getDateEnd() != null) {
                Calendar c = Calendar.getInstance();
                c.setTime(data.getDateEnd());
                c.add(Calendar.DAY_OF_MONTH, 1);
                data.setDateEnd(c.getTime());
            }
            DbContextHolder.setDbType(DBTypeEnum.db2);
            resultPage = mpsNetdemandService.getListForPrint(data,page1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        result.setRecords(resultPage.getTotal());
        result.setTotal((int)Math.ceil(resultPage.getTotal()/(double)resultPage.getSize()));
        result.setRows(resultPage.getRecords());
        return result;

    }


    @RequestMapping(value = "/calculate")
    @ResponseBody
    public ResponseResult calculate(String mData) {
        ResponseResult result = new ResponseResult();
        try{
            if (CustomStringUtils.isBlank(mData)) {
                throw new Exception("参数异常！");
            }
            List<U8MpsNetdemand> srpMainProducts = JSON.parseArray(mData, U8MpsNetdemand.class);

            result=testSoService.calculate(srpMainProducts);
        }catch (Exception e){
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        }
        return result;
    }






}