package com.web.basicinfo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.web.basicinfo.entity.Person;
import com.web.basicinfo.entity.Vendor;

public interface PersonMapper extends BaseMapper<Person> {
    int updateByPrimaryKeySelective(Person record);

    int updateByPrimaryKey(Person record);
}