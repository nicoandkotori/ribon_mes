package com.web.basicinfo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.common.util.ResponseResult;
import com.modules.data.mybatis.DBTypeEnum;
import com.modules.data.mybatis.DbContextHolder;
import com.modules.security.util.SecurityUtil;
import com.web.basicinfo.entity.Person;
import com.web.basicinfo.entity.Vendor;
import com.web.basicinfo.service.IPersonService;
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
@RequestMapping(value = "/basicinfo/person")
public class PersonController extends BasicController {
    @Autowired
    private IPersonService personService;


    @RequestMapping(value = "/getlist")
    public ResponseResult getListByWarehouse() {
        ResponseResult reslut = new ResponseResult();
        DbContextHolder.setDbType(DBTypeEnum.db2);
        LambdaQueryWrapper<Person> select=new LambdaQueryWrapper<>();
        select.apply("( cpersoncode='200402009' or  cpersoncode='201609001' or  cpersoncode='200803002' )");
        select.orderByDesc(Person::getCpersoncode);
        List<Person> list = personService.list(select);
        reslut.setResult(list);
        return reslut;
    }




    @RequestMapping(value = "/getdefuser")
    public ResponseResult getDefUser() {
        ResponseResult reslut = new ResponseResult();
        DbContextHolder.setDbType(DBTypeEnum.db2);
        LambdaQueryWrapper<Person> select=new LambdaQueryWrapper<>();
        select.eq(Person::getCpersonname, SecurityUtil.getUser().getMyusername());
        select.orderByDesc(Person::getCpersoncode);
        List<Person> list = personService.list(select);
        if(list!=null&&list.size()>0)
        {
            reslut.setResult(list.get(0));
        }
        return reslut;
    }
}
