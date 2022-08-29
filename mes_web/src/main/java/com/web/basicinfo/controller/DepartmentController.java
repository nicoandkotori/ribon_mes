package com.web.basicinfo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.common.util.ResponseResult;
import com.modules.data.mybatis.DBTypeEnum;
import com.modules.data.mybatis.DbContextHolder;
import com.web.basicinfo.entity.Department;
import com.web.basicinfo.entity.Person;
import com.web.basicinfo.entity.Warehouse;
import com.web.basicinfo.service.IDepartmentService;
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
@RequestMapping(value = "/basicinfo/department")
public class DepartmentController extends BasicController {
    @Autowired
    private IDepartmentService departmentService;



    @RequestMapping(value = "/getlist")
    public ResponseResult getListByWarehouse() {
        ResponseResult reslut = new ResponseResult();
        DbContextHolder.setDbType(DBTypeEnum.db2);
        LambdaQueryWrapper<Department> select=new LambdaQueryWrapper<>();
        select.eq(Department::getBdepend,1);
        select.orderByDesc(Department::getCdepcode);
        List<Department> list = departmentService.list(select);
        reslut.setResult(list);
        return reslut;
    }

}
