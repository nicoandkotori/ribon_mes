package com.web.om.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.web.basicinfo.mapper.InventoryMapper;
import com.web.om.dto.OmOrderMainDTO;
import com.web.om.dto.OmProductVM;
import com.web.om.entity.OmMoDetails;
import com.web.om.entity.OmOrderMain;
import com.web.om.mapper.OmMoDetailsMapper;
import com.web.om.mapper.OmMoMainMapper;
import com.web.om.mapper.OmOrderMainMapper;
import com.web.om.service.IOmMoDetailsService;
import com.web.om.service.IOmOrderMainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author caihuan
 * @since 2022-06-28
 */
@Service
public class OmOrderMainServiceImpl extends ServiceImpl<OmOrderMainMapper, OmOrderMain> implements IOmOrderMainService {

    @Autowired
    private OmOrderMainMapper omOrderMainMapper;

    @Value("${account.acountId}")
    private String accId;

    @Override
    public IPage<OmOrderMainDTO> getMainList(OmOrderMainDTO mainDTO, IPage<OmOrderMainDTO> page) throws Exception {
        page.setRecords(omOrderMainMapper.getMainList(page,mainDTO));
        return page;
    }

    @Override
    public List<OmOrderMainDTO> getDetailList(OmOrderMainDTO mainDTO) throws Exception {
        return omOrderMainMapper.getDetailList(mainDTO);
    }

}
