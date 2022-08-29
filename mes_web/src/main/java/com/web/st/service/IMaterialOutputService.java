package com.web.st.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.web.st.dto.DeviceListDTO;
import com.web.st.entity.RdRecord11;

import java.util.List;

public interface IMaterialOutputService extends IService<RdRecord11> {

    void save(DeviceListDTO m, List<DeviceListDTO> list, String userName) throws Exception;
}
