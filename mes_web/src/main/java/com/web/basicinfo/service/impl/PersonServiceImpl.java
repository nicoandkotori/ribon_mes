package com.web.basicinfo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.web.basicinfo.entity.Person;
import com.web.basicinfo.entity.Vendor;
import com.web.basicinfo.mapper.PersonMapper;
import com.web.basicinfo.mapper.VendorMapper;
import com.web.basicinfo.service.IPersonService;
import com.web.basicinfo.service.IVendorService;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

/**
 * Created by cai on 2021-3-12.
 */
@Service
public class PersonServiceImpl extends ServiceImpl<PersonMapper, Person> implements IPersonService{

}
