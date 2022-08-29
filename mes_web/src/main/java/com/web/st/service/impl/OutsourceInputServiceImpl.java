package com.web.st.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.util.CustomStringUtils;
import com.common.util.DateUtil;
import com.web.basicinfo.entity.Inventory;
import com.web.basicinfo.mapper.InventoryMapper;
import com.web.om.entity.OmMoDetails;
import com.web.om.entity.OmMoMain;
import com.web.om.mapper.OmMoDetailsMapper;
import com.web.om.mapper.OmMoMainMapper;
import com.web.st.dto.OutsourceOrderDTO;
import com.web.st.entity.IaStUnAccountVouch01;
import com.web.st.entity.RdRecord01;
import com.web.st.entity.RdRecords01;
import com.web.st.mapper.IaStUnAccountVouch01Mapper;
import com.web.st.mapper.RdRecord01Mapper;
import com.web.st.mapper.RdRecords01Mapper;
import com.web.st.service.IOutsourceInputService;
import com.web.u8system.util.U8CurrentStockUtils;
import com.web.u8system.util.U8SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OutsourceInputServiceImpl extends ServiceImpl<RdRecord01Mapper, RdRecord01> implements IOutsourceInputService {

    @Autowired
    private RdRecord01Mapper rdRecord01Mapper;

    @Autowired
    private RdRecords01Mapper rdRecords01Mapper;

    @Autowired
    private InventoryMapper inventoryMapper;

    @Autowired
    private OmMoMainMapper omMoMainMapper;

    @Autowired
    private OmMoDetailsMapper omMoDetailsMapper;

    @Autowired
    private U8SystemUtils u8SystemUtils;

    @Autowired
    private U8CurrentStockUtils u8CurrentStockUtils;

    @Autowired
    private IaStUnAccountVouch01Mapper iaStUnAccountVouch01Mapper;

    @Value("${account.acountId}")
    private String cAcc_Id;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(OutsourceOrderDTO m, List<OutsourceOrderDTO> list, String userName) throws Exception {
        try {
            RdRecord01 main = new RdRecord01();
            main.setDefaultValueForWw();
            main.setCwhcode(m.getWhCode());
            main.setCmaker(userName);
            main.setChandler(userName);
            main.setDdate(m.getDdate());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            main.setDveridate(sdf.parse(sdf.format(new Date())));
            main.setDnverifytime(new Date());

            main.setCcode(getMaxCode("OS"));
            String sysbarcode = "||" + "st" + main.getCvouchtype() + "|" + main.getCcode();
            main.setCsysbarcode(sysbarcode);
            main.setCchecksignflag(UUID.randomUUID().toString().replaceAll("-", ""));

            Integer u8fid = u8SystemUtils.getFatherId(cAcc_Id,"rd", 10);
            main.setId(u8fid.longValue());
            if(m.getMoid()!=null)
            {
                OmMoMain omMoMain= omMoMainMapper.selectById(m.getMoid());
                main.setCptcode(omMoMain.getCptcode());
                main.setCvencode(omMoMain.getCvencode());
                main.setCordercode(omMoMain.getCcode());
                main.setCmemo(omMoMain.getCmemo());
                main.setCdefine2(omMoMain.getCdefine2());
                main.setCdefine7(omMoMain.getCdefine7());
                main.setItaxrate(omMoMain.getItaxrate());
                main.setIexchrate(omMoMain.getNflat().doubleValue());
                main.setCexchName(omMoMain.getCexchName());
                main.setCdepcode(omMoMain.getCdepcode());
                main.setIpurorderid(m.getMoid().longValue());
                main.setCpersoncode(omMoMain.getCpersoncode());
                main.setCdepcode(omMoMain.getCdepcode());
            }

            int n = rdRecord01Mapper.insert(main);
            if(n<=0) {
                throw new Exception("插入主表数据出错！");
            }

            BigDecimal taxRate = main.getItaxrate() == null ? BigDecimal.ZERO: main.getItaxrate();
            taxRate = taxRate.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
            taxRate = taxRate.add(BigDecimal.ONE);

            Integer u8cid = u8SystemUtils.getChildId(cAcc_Id, "rd", 10, list.size());
            int rowSort = 1;
            for (OutsourceOrderDTO d : list) {
                Inventory inventory=inventoryMapper.selectById(d.getCinvcode());
                if(inventory==null) {
                    throw new Exception("U8存货信息不存在，请确认数据！");
                }

                if (d.getIquantity().compareTo(BigDecimal.ZERO) == 0) {
                    throw new Exception("数量不能为0，请确认数据！");
                }

                RdRecords01 detail = new RdRecords01();
                detail.setDefaultValue();
                detail.setId(u8fid.longValue());
                detail.setAutoid(u8cid.longValue());
                detail.setIquantity(d.getIquantity());
                detail.setInquantity(d.getIquantity());
                detail.setChvencode(main.getCvencode());
                detail.setCpoid(main.getCordercode());
                detail.setIomodid(d.getModetailsid());
                sysbarcode = "||" + "st" + main.getCvouchtype() + "|" + main.getCcode();
                detail.setCbsysbarcode(sysbarcode);
                detail.setIrowno(rowSort++);

                //查询委外订单字表
                OmMoDetails omMoDetails=omMoDetailsMapper.selectById(d.getModetailsid());
                detail.setCinvcode(omMoDetails.getCinvcode());
                detail.setIprocesscost(omMoDetails.getIunitprice());

                BigDecimal processFee = detail.getIquantity().multiply(omMoDetails.getIunitprice()).multiply(taxRate).setScale(2, RoundingMode.HALF_UP);
                detail.setIprocessfee(processFee);
                detail.setCdefine26(processFee.doubleValue());

                n = rdRecords01Mapper.insert(detail);
                if (n <= 0) {
                    throw new Exception("插入子表数据出错！");
                }

                //更新委外订单字表数据
                if(CustomStringUtils.isNotBlank(detail.getIomodid()))
                {
                    OmMoDetails updateOm=new OmMoDetails();
                    updateOm.setModetailsid(omMoDetails.getModetailsid());
                    updateOm.setIreceivedqty((omMoDetails.getIreceivedqty()==null?BigDecimal.ZERO:omMoDetails.getIreceivedqty()).add(detail.getIquantity()));
                    omMoDetailsMapper.updateById(updateOm);
                }

                IaStUnAccountVouch01 iaStUnAccountVouch01=new IaStUnAccountVouch01();
                iaStUnAccountVouch01.setIdun(detail.getId().intValue());
                iaStUnAccountVouch01.setIdsun(detail.getAutoid().intValue());
                iaStUnAccountVouch01.setCvoutypeun(main.getCvouchtype());
                iaStUnAccountVouch01.setCbustypeun(main.getCbustype());
                iaStUnAccountVouch01Mapper.insert(iaStUnAccountVouch01);

                //现存量表  加上结存数量和可用数量
                u8CurrentStockUtils.updateQuanity(main.getCwhcode(),detail.getCinvcode(),"","","","", detail.getIquantity(),null, "add");

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

        LambdaQueryWrapper<RdRecord01> select=new LambdaQueryWrapper<>();
        select.select(RdRecord01::getCcode)
                .like(RdRecord01::getCcode, head);
        select.orderByDesc(RdRecord01::getCcode);
        List<RdRecord01> mainList = rdRecord01Mapper.selectList(select);
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
