package com.web.st.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.web.st.dto.DeviceListDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeviceListMapper {
    List<DeviceListDTO> getList(IPage<DeviceListDTO> page1, @Param("query") DeviceListDTO query);

    List<DeviceListDTO> getListByOrderNo(@Param("orderNo") String orderNo, @Param("proType") String invType);

    DeviceListDTO getById(String id);

    DeviceListDTO getByCode(@Param("orderNo") String orderNo, @Param("proType") String proType);

    DeviceListDTO getDetail(@Param("orderNo") String orderNo, @Param("barcode") String barcode);
}
