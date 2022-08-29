package com.web.st.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.web.st.dto.DeviceListDTO;
import com.web.st.mapper.DeviceListMapper;
import com.web.st.service.IDeviceListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class DeviceListServiceImpl implements IDeviceListService {

    @Autowired
    private DeviceListMapper deviceListMapper;

    @Override
    public IPage<DeviceListDTO> getList(DeviceListDTO query, IPage<DeviceListDTO> page1) {
        page1.setRecords(deviceListMapper.getList(page1, query));
        return page1;
    }

    @Override
    public List<DeviceListDTO> getListByOrderNo(String orderNo, String proType) {
        List<DeviceListDTO> list = deviceListMapper.getListByOrderNo(orderNo, proType);
        for (DeviceListDTO d : list) {
            BigDecimal unOutQty = d.getOrderQty();
            if (d.getOutQty() != null) {
                unOutQty = unOutQty.subtract(d.getOutQty()).setScale(2, RoundingMode.HALF_UP);
            } else {
                d.setOutQty(BigDecimal.ZERO);
            }
            d.setUnOutQty(unOutQty);

            String barcodeId = "-" + d.getIid();
            int len = barcodeId.length();
            for (int i=0; i<12-len; i++) {
                barcodeId = "0" + barcodeId;
            }
            d.setBarcodeId(barcodeId);
        }
        return list;
    }

    @Override
    public DeviceListDTO getByCode(String barcode, String proType) {
        DeviceListDTO d = deviceListMapper.getById(barcode);
        if (d != null) {
            return deviceListMapper.getByCode(d.getOrderNo(), proType);
        }
        return new DeviceListDTO();
    }

    @Override
    public DeviceListDTO getDetail(String orderNo, String barcode) {
        DeviceListDTO m = deviceListMapper.getDetail(orderNo, barcode);
        if (m != null) {
            BigDecimal curQty = m.getCurQty() == null ? BigDecimal.ZERO : m.getCurQty();
            if (m.getUnOutQty().compareTo(curQty) > 0) {
                m.setUnOutQty(curQty);
            }
        }
        return m;
    }
}
