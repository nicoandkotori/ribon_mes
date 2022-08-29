package com.web.basicinfo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.web.basicinfo.entity.Department;
import com.web.basicinfo.entity.Person;

public interface DepartmentMapper extends BaseMapper<Department> {
    int updateByPrimaryKeySelective(Department record);

    int updateByPrimaryKey(Department record);
}