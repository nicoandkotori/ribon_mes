package com.web.st.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.web.st.dto.DeviceListDTO;

import java.util.List;

public interface IDeviceListService {
    IPage<DeviceListDTO> getList(DeviceListDTO query, IPage<DeviceListDTO> page1);

    List<DeviceListDTO> getListByOrderNo(String orderNo, String proType);

    DeviceListDTO getByCode(String barcode, String proType);

    DeviceListDTO getDetail(String orderNo, String barcode);
}
