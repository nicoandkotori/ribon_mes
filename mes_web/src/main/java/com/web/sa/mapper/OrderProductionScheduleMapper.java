package com.web.sa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.web.sa.entity.OrderProductionSchedule;
import org.apache.ibatis.annotations.Param;

public interface OrderProductionScheduleMapper extends BaseMapper<OrderProductionSchedule> {

    OrderProductionSchedule selectByPrimaryKey(@Param("id")String id, @Param("database") String database);
    int insertSelective( @Param("main") OrderProductionSchedule orderProductionSchedule, @Param("database") String database);
    int updateByPrimaryKeySelective( @Param("main") OrderProductionSchedule orderProductionSchedule, @Param("database") String database);

    int udateBySodId(@Param("sodId") Integer id, @Param("database") String database);
}