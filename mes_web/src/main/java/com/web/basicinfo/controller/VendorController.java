package com.web.basicinfo.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.util.ResponseResult;
import com.modules.data.mybatis.DBTypeEnum;
import com.modules.data.mybatis.DbContextHolder;
import com.web.basicinfo.entity.Department;
import com.web.basicinfo.entity.Vendor;
import com.web.basicinfo.service.IDepartmentService;
import com.web.basicinfo.service.IVendorService;
import com.web.common.controller.BasicController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

    /**
     * 分页等于查询
     *
     * @param page  页号
     * @param limit 页面大小
     * @param query 查询条件
     * @return {@link ResponseResult}
     */
    @GetMapping("/equal_find")
    public ResponseResult equalFind(Integer page ,Integer limit, String query){
        try {
            if (page == null){
                page = 1;
            }
            if (limit == null){
                limit = 10;
            }
            DbContextHolder.setDbType(DBTypeEnum.db2);
            Vendor vendor = JSON.parseObject(query,Vendor.class);
            LambdaQueryWrapper<Vendor> wrapper = new LambdaQueryWrapper<>();
            IPage<Vendor> iPage = new Page<>(page,limit);
            wrapper.setEntity(vendor);
            IPage<Vendor> resultPage = vendorService.page(iPage,wrapper);
            List<Vendor> resultList = resultPage.getRecords();
            if (resultList.size()<1){
                return ResponseResult.error("查询不到数据");
            }
            return ResponseResult.success(resultList);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseResult.error(e.getMessage());
        }

    }


}
