package com.web.st.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.web.st.dto.OutsourceOrderDTO;
import com.web.st.mapper.OutsourceOrderMapper;
import com.web.st.service.IOutsourceOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class OutsourceOrderServiceImpl implements IOutsourceOrderService {

    @Autowired
    private OutsourceOrderMapper outsourceOrderMapper;

    @Override
    public IPage<OutsourceOrderDTO> getList(OutsourceOrderDTO query, IPage<OutsourceOrderDTO> page1) {
        List<OutsourceOrderDTO> list = outsourceOrderMapper.getList(page1, query);
        for (OutsourceOrderDTO d : list) {
            BigDecimal unInQty = d.getIquantity();
            if (d.getInqty() != null) {
                unInQty = unInQty.subtract(d.getInqty()).setScale(2, RoundingMode.HALF_UP);
            } else {
                d.setInqty(BigDecimal.ZERO);
            }
            d.setUninqty(unInQty);
        }
        page1.setRecords(list);
        return page1;
    }

    @Override
    public OutsourceOrderDTO getByCode(String ccode) {
        return outsourceOrderMapper.getByCode(ccode);
    }

    @Override
    public List<OutsourceOrderDTO> getListByCode(String ccode) {
        List<OutsourceOrderDTO> list = outsourceOrderMapper.getListByCode(ccode);
        for (OutsourceOrderDTO d : list) {
            BigDecimal unInQty = d.getIquantity();
            if (d.getInqty() != null) {
                unInQty = unInQty.subtract(d.getInqty()).setScale(2, RoundingMode.HALF_UP);
            } else {
                d.setInqty(BigDecimal.ZERO);
            }
            d.setUninqty(unInQty);

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
    public OutsourceOrderDTO getDetail(String ccode, String barcode) {
        return outsourceOrderMapper.getDetail(ccode, barcode);
    }
}
