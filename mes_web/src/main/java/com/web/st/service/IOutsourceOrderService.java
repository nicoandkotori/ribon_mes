package com.web.st.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.web.st.dto.OutsourceOrderDTO;

import java.util.List;

public interface IOutsourceOrderService {
    IPage<OutsourceOrderDTO> getList(OutsourceOrderDTO query, IPage<OutsourceOrderDTO> page1);

    OutsourceOrderDTO getByCode(String ccode);

    List<OutsourceOrderDTO> getListByCode(String ccode);

    OutsourceOrderDTO getDetail(String ccode, String barcode);
}
