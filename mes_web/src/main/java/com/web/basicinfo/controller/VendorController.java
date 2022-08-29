package com.web.basicinfo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.common.util.ResponseResult;
import com.modules.data.mybatis.DBTypeEnum;
import com.modules.data.mybatis.DbContextHolder;
import com.web.basicinfo.entity.Department;
import com.web.basicinfo.entity.Vendor;
import com.web.basicinfo.service.IDepartmentService;
import com.web.basicinfo.service.IVendorService;
import com.web.common.controller.BasicController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by caihuan on 2021-11-13.
 */
@RestController
@RequestMapping(value = "/basicinfo/vendor")
public class VendorController extends BasicController {
    @Autowired
    private IVendorService vendorService;

    @RequestMapping(value = "/getlist")
    public ResponseResult getListByWarehouse() {
        ResponseResult reslut = new ResponseResult();
        DbContextHolder.setDbType(DBTypeEnum.db2);
        LambdaQueryWrapper<Vendor> select=new LambdaQueryWrapper<>();
        select.eq(Vendor::getBproxyforeign,true);
        select.orderByAsc(Vendor::getCvencode);
        List<Vendor> list = vendorService.list(select);
        reslut.setResult(list);
        return reslut;
    }


}
