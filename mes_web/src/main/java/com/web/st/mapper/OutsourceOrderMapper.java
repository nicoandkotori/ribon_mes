package com.web.st.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.web.st.dto.OutsourceOrderDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OutsourceOrderMapper {
    List<OutsourceOrderDTO> getList(IPage<OutsourceOrderDTO> page1, @Param("query") OutsourceOrderDTO query);

    OutsourceOrderDTO getByCode(String ccode);

    List<OutsourceOrderDTO> getListByCode(String ccode);

    OutsourceOrderDTO getDetail(@Param("ccode") String ccode, @Param("barcode") String barcode);
}
