package com.web.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.web.demo.entity.OrderWorkType;
import com.web.demo.mapper.OrderWorkTypeMapper;
import com.web.demo.service.IOrderWorkTypeService;
import org.springframework.stereotype.Service;

@Service
public class IOrderWorkTypeServiceImpl extends ServiceImpl<OrderWorkTypeMapper, OrderWorkType> implements IOrderWorkTypeService {

}
