package com.web.st.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.util.CustomStringUtils;
import com.common.util.DateUtil;
import com.web.basicinfo.entity.Inventory;
import com.web.basicinfo.mapper.InventoryMapper;
import com.web.st.dto.DeviceListDTO;
import com.web.st.entity.IaStUnAccountVouch11;
import com.web.st.entity.RdRecord11;
import com.web.st.entity.RdRecords11;
import com.web.st.mapper.IaStUnAccountVouch11Mapper;
import com.web.st.mapper.RdRecord11Mapper;
import com.web.st.mapper.RdRecords11Mapper;
import com.web.st.service.IMaterialOutputService;
import com.web.u8system.util.U8CurrentStockUtils;
import com.web.u8system.util.U8SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class MaterialOutputService extends ServiceImpl<RdRecord11Mapper, RdRecord11> implements IMaterialOutputService {

    @Autowired
    private RdRecord11Mapper rdRecord11Mapper;

    @Autowired
    private RdRecords11Mapper rdRecords11Mapper;

    @Autowired
    private InventoryMapper inventoryMapper;

    @Autowired
    private U8SystemUtils u8SystemUtils;

    @Autowired
    private U8CurrentStockUtils u8CurrentStockUtils;

    @Autowired
    private IaStUnAccountVouch11Mapper iaStUnAccountVouch11Mapper;

    @Value("${account.acountId}")
    private String cAcc_Id;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(DeviceListDTO m, List<DeviceListDTO> list, String userName) throws Exception {
        try {
            RdRecord11 main = new RdRecord11();
            main.setDefault();
            main.setCwhcode(m.getWhCode());
            main.setCdepcode(m.getDepCode());
            main.setCmaker(userName);
            main.setChandler(userName);
            main.setDdate(m.getCreatedate());
            main.setCmpocode(m.getOrderNo());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            main.setDveridate(sdf.parse(sdf.format(new Date())));
            main.setDnverifytime(new Date());

            main.setCcode(getMaxCode("OT"));
            String sysbarcode = "||" + "st" + main.getCvouchtype() + "|" + main.getCcode();
            main.setCsysbarcode(sysbarcode);

            main.setCchecksignflag(UUID.randomUUID().toString().replaceAll("-", ""));

            Integer u8fid = u8SystemUtils.getFatherId(cAcc_Id,"rd", 10);
            main.setId(u8fid.longValue());

            int n = rdRecord11Mapper.insert(main);
            if(n<=0) {
                throw new Exception("插入主表数据出错！");
            }

            Integer u8cid = u8SystemUtils.getChildId(cAcc_Id, "rd", 10, list.size());
            int rowSort = 1;
            for (DeviceListDTO d : list) {
                Inventory inventory=inventoryMapper.selectById(d.getInvCode());
                if(inventory==null) {
                    throw new Exception("U8存货信息不存在，请确认数据！");
                }

                if (d.getOutQty().compareTo(BigDecimal.ZERO) == 0) {
                    throw new Exception("数量不能为0，请确认数据！");
                }

                RdRecords11 detail = new RdRecords11();
                detail.setDefault();
                detail.setId(u8fid.longValue());
                detail.setAutoid(u8cid.longValue());
                detail.setIquantity(d.getOutQty());
                sysbarcode = "||" + "st" + main.getCvouchtype() + "|" + main.getCcode();
                detail.setCbsysbarcode(sysbarcode);
                detail.setIrowno(rowSort++);
                detail.setCinvcode(d.getInvCode());
                detail.setCmocode(m.getOrderNo());

                n = rdRecords11Mapper.insert(detail);
                if (n <= 0) {
                    throw new Exception("插入子表数据出错！");
                }

                IaStUnAccountVouch11 iaStUnAccountVouch11=new IaStUnAccountVouch11();
                iaStUnAccountVouch11.setIdun(detail.getId().intValue());
                iaStUnAccountVouch11.setIdsun(detail.getAutoid().intValue());
                iaStUnAccountVouch11.setCvoutypeun(main.getCvouchtype());
                iaStUnAccountVouch11.setCbustypeun(main.getCbustype());
                iaStUnAccountVouch11Mapper.insert(iaStUnAccountVouch11);

                //现存量表  加上结存数量和可用数量
                u8CurrentStockUtils.updateQuanity(main.getCwhcode(),detail.getCinvcode(),"","","","", detail.getIquantity(),null, "del");

                u8cid++;
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public String getMaxCode(String headCode) {
        String head =  headCode + DateUtil.getDateStr(new Date(), "yyyyMMdd");
        String sortNum = "001";
        int sortNumLength = 3;

        LambdaQueryWrapper<RdRecord11> select=new LambdaQueryWrapper<>();
        select.select(RdRecord11::getCcode)
                .like(RdRecord11::getCcode, head);
        select.orderByDesc(RdRecord11::getCcode);
        List<RdRecord11> mainList = rdRecord11Mapper.selectList(select);
        if(mainList.size() > 0){
            String maxCode = mainList.get(0).getCcode();
            if (CustomStringUtils.isNotBlank(maxCode)) {
                sortNum = Integer.parseInt(maxCode.substring(maxCode.length() - sortNumLength)) + 1 + "";
                while (sortNum.length() < sortNumLength) {
                    sortNum = "0" + sortNum;
                }
            }
        }
        return head + sortNum;
    }
}
