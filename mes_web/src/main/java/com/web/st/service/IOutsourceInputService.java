package com.web.st.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.web.st.dto.OutsourceOrderDTO;
import com.web.st.entity.RdRecord01;

import java.util.List;

public interface IOutsourceInputService extends IService<RdRecord01> {
    void save(OutsourceOrderDTO m, List<OutsourceOrderDTO> list, String userName) throws Exception;
}
