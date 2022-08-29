package com.web.basicinfo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.common.util.ResponseResult;
import com.modules.data.mybatis.DBTypeEnum;
import com.modules.data.mybatis.DbContextHolder;
import com.web.basicinfo.entity.Warehouse;
import com.web.basicinfo.service.IWarehouseService;
import com.web.common.controller.BasicController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by caihuan on 2021-11-13.
 */
@RestController
@RequestMapping(value = "/api/basicinfo/warehouse")
public class ApiWarehouseController extends BasicController {
    @Autowired
    private IWarehouseService warehouseService;


    /**
     * 获取仓库列表
     * @param
     * @return
     */
    @RequestMapping(value = "/getlistbypda")
    public ResponseResult getListByPda() {
        ResponseResult result = new ResponseResult();
        try{
            DbContextHolder.setDbType(DBTypeEnum.db2);
            LambdaQueryWrapper<Warehouse> select=new LambdaQueryWrapper<>();
            select.orderByAsc(Warehouse::getCwhcode);
            List<Warehouse> list = warehouseService.list(select);
            result.setResult(list);
        }catch(Exception e){
            e.printStackTrace();

        }
        return result;
    }

}
