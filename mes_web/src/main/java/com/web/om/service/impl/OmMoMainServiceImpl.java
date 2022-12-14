package com.web.om.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.util.*;
import com.modules.security.util.SecurityUtil;
import com.web.basicinfo.entity.ComputationUnit;
import com.web.basicinfo.entity.Inventory;
import com.web.basicinfo.entity.Vendor;
import com.web.basicinfo.mapper.BasPartMapper;
import com.web.basicinfo.mapper.ComputationUnitMapper;
import com.web.basicinfo.mapper.InventoryMapper;
import com.web.basicinfo.mapper.VendorMapper;
import com.web.basicinfo.service.IInventoryService;
import com.web.om.dto.OmProductVM;
import com.web.om.entity.*;
import com.web.om.mapper.OmMoDetailsMapper;
import com.web.om.mapper.OmMoMainMapper;
import com.web.om.mapper.OmMoMaterialsMapper;
import com.web.om.mapper.OmOrderMainMapper;
import com.web.om.service.IOmMoMainService;
import com.web.u8system.util.U8SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author caihuan
 * @since 2022-06-28
 */
@Service
public class OmMoMainServiceImpl extends ServiceImpl<OmMoMainMapper, OmMoMain> implements IOmMoMainService {
    @Autowired
    private InventoryMapper inventoryMapper;
    @Autowired
    private BasPartMapper basPartMapper;
    @Autowired
    private IInventoryService inventoryService;
    @Autowired
    private OmMoMainMapper omMoMainMapper;
    @Autowired
    private OmMoDetailsMapper omMoDetailsMapper;
    @Autowired
    private OmMoMaterialsMapper omMoMaterialsMapper;
    @Autowired
    private ComputationUnitMapper computationUnitMapper;
    @Autowired
    private VendorMapper vendorMapper;
    @Value("${account.acountId}")
    private String accId;

    @Autowired
    private U8SystemUtils u8SystemUtils;

    @Override
    public IPage<OmProductVM> getMainList(OmProductVM mainDTO, IPage<OmProductVM> page) throws Exception {
        page.setRecords(omMoMainMapper.getMainList(page,mainDTO));
        return page;
    }

    @Override
    public List<OmProductVM> getDetailList(OmProductVM mainDTO) throws Exception {
        return omMoMainMapper.getDetailList(mainDTO);
    }

    @Override
    public Map<String, Object> getDetailList1(OmProductVM query)throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        List<OmProductVM> list= omMoMainMapper.getDetailList1(query);
        List<OmProductVM> list1= omMoMainMapper.getDetailList2(query);
        for(OmProductVM m:list1)
        {
            for(OmProductVM q:list)
            {
                if(m.getModetailsid().toString().equals(q.getModetailsid().toString()))
                {
                    m.setRecordId(q.getModetailsid().toString());
                    break;
                }
            }
        }
        for(OmProductVM q:list)
        {
            if(q.getRecordId()==null)
            {
                q.setRecordId(q.getModetailsid().toString());
            }
        }

        map.put("mData", list);
        map.put("mDatas", list1);
        return map;

    }
    @Override
    public Map<String, Object> getHistoryByCinvcode1(OmProductVM query)throws Exception{
        Map<String, Object> map = new HashMap<String, Object>();
        OmProductVM m= omMoMainMapper.getHistoryByCinvcode(query);
        if(m!=null) {
            query.setModetailsid(m.getModetailsid());
            m.setRecordId(UUID.randomUUID().toString());
            m.setMomaterialsid(null);
            m.setMoid(null);
            m.setModetailsid(null);


            List<OmProductVM> list= omMoMainMapper.getHistoryListByCinvcode(query);
            if(list!=null)
            {
                BigDecimal totalPrice26=BigDecimal.ZERO;
                BigDecimal totalPrice27=BigDecimal.ZERO;
                for(OmProductVM item:list)
                {
                    item.setMomaterialsid(null);
                    item.setMoid(null);
                    item.setModetailsid(null);
                    item.setRecordId(m.getRecordId());
                    BigDecimal price=inventoryService.getPrice(item.getCinvcodes()).setScale(2,BigDecimal.ROUND_HALF_UP);
                    item.setCdefine26(price);
                    item.setCdefine27(price.multiply(item.getFbaseqtyn()==null?BigDecimal.ZERO:item.getFbaseqtyn()).setScale(2,BigDecimal.ROUND_HALF_UP));
                    totalPrice26=totalPrice26.add(price);
                    totalPrice27=totalPrice27.add(price.multiply(item.getFbaseqtyn()==null?BigDecimal.ZERO:item.getFbaseqtyn()).setScale(2,BigDecimal.ROUND_HALF_UP));
                }
                m.setCdefine26(totalPrice26);
                m.setCdefine27(totalPrice27);
                m.setTolpic((m.getCdefine26()==null?BigDecimal.ONE:m.getCdefine26()).add(m.getItaxprice()==null?BigDecimal.ZERO:m.getItaxprice()));
                m.setTolnum(m.getTolpic().multiply(m.getIquantity()).setScale(2, BigDecimal.ROUND_HALF_UP));

            }

            map.put("mData", m);
            map.put("mDatas", list);
            return map;
        }
        else
        {
            m=new OmProductVM();
            Inventory inventory=inventoryMapper.getInfoById(query.getCinvcode());
            if(CustomStringUtils.isNotBlank(inventory))
            {
                m.setCinvcode(inventory.getCinvcode());
                m.setCinvname(inventory.getCinvname());
                m.setCinvstd(inventory.getCinvstd());
                m.setCcomunitname(inventory.getCcomunitname());
            }
            map.put("mData", m);
            map.put("mDatas", null);
            return map;
        }
    }





    /**
     * ??????????????????
     * @param
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult save1(OmMoMain omProductPo, List<OmProductVM>  list,  List<OmProductVM>  listDetail) throws Exception{
        ResponseResult result = new ResponseResult();
        try{

            if(CustomStringUtils.isBlank(omProductPo.getDdate()))
            {
                throw new Exception("???????????????????????????");
            }

            if(CustomStringUtils.isBlank(omProductPo.getCdepcode()))
            {
                throw new Exception("?????????????????????");
            }
            if(CustomStringUtils.isBlank(omProductPo.getCpersoncode()))
            {
                throw new Exception("????????????????????????");
            }
            if(CustomStringUtils.isBlank(omProductPo.getCvencode()))
            {
                throw new Exception("????????????????????????");
            }

            if(list==null||list.size()==0)
            {
                throw new Exception("???????????????????????????");
            }


            if(CustomStringUtils.isNotBlank(omProductPo.getMoid())) {
                OmMoMain omMoMain=omMoMainMapper.selectById(omProductPo.getMoid());
                if(!omMoMain.getCstate().toString().equals("0"))
                {
                    throw new Exception("?????????????????????????????????????????????");
                }


                LambdaQueryWrapper<OmMoMaterials> selectM= new LambdaQueryWrapper<>();
                selectM.apply("  modetailsid in (select modetailsid from OM_MODetails where moid='"+omProductPo.getMoid()+"' )" );
                List<OmMoMaterials> listM = omMoMaterialsMapper.selectList(selectM);
                if(listM!=null)
                {
                    for(OmMoMaterials omMoMaterials:listM)
                    {
                        omMoMaterialsMapper.deleteById(omMoMaterials.getMomaterialsid());
                    }
                }

                LambdaQueryWrapper<OmMoDetails> selectD = new LambdaQueryWrapper<>();
                selectD.eq(OmMoDetails::getMoid,omProductPo.getMoid());
                List<OmMoDetails> listD = omMoDetailsMapper.selectList(selectD);
                if(listD!=null)
                {
                    for(OmMoDetails omMoDetails:listD)
                    {
                        omMoDetailsMapper.deleteById(omMoDetails.getModetailsid());
                    }
                }


                omMoMainMapper.deleteById(omProductPo.getMoid());
            }


            omProductPo.setMoid(null);

            //??????   ????????????????????????????????????
            if(CustomStringUtils.isNotBlank(omProductPo.getMoid())) {
                OmMoMain omProductPo1=omMoMainMapper.selectById(omProductPo.getMoid());
                if(omProductPo1!=null&&omProductPo1.getCstate().compareTo(Byte.valueOf("1"))!=0)
                {
                    throw new Exception("???????????????????????????");
                }
                //?????????????????????
                Vendor vendor=vendorMapper.selectById(omProductPo.getCvencode());
                omProductPo.setCvenaccount(vendor.getCvenaccount());
                omProductPo.setCvenbank(vendor.getCvenbank());
                omProductPo.setCvenperson(vendor.getCvenperson());
                int rate=13;
                if(!CustomStringUtils.isBlank(vendor.getCvendefine11()))
                {
                    rate=vendor.getCvendefine11();
                }
                omProductPo.setItaxrate(BigDecimal.valueOf(rate));
                omProductPo.setCexchName(vendor.getCvenexchName());
                int n = omMoMainMapper.updateById(omProductPo);
                if (n<0){
                    throw new Exception("?????????????????????");
                }
                //????????????????????????
                for(OmProductVM t:list)
                {
                    if(CustomStringUtils.isBlank(t.getCinvcode()))
                    {
                        throw new Exception("???????????????????????????");
                    }
                    if(CustomStringUtils.isBlank(t.getDstartdate()))
                    {
                        throw new Exception("?????????????????????????????????");
                    }
                    if(CustomStringUtils.isBlank(t.getDarrivedate()))
                    {
                        throw new Exception("?????????????????????????????????");
                    }
                    if(CustomStringUtils.isBlank(t.getIquantity()))
                    {
                        throw new Exception("?????????????????????");
                    }
//                    if(CustomStringUtils.isBlank(t.getItaxprice()))
//                    {
//                        throw new Exception("??????????????????????????????");
//                    }
//                    if(t.getItaxprice().compareTo(BigDecimal.ZERO)<0)
//                    {
//                        throw new Exception("??????????????????????????????0???");
//                    }
                    OmMoDetails omPoMain=new OmMoDetails();

                    omPoMain.setCinvcode(t.getCinvcode());
                    omPoMain.setDstartdate(t.getDstartdate());
                    omPoMain.setDarrivedate(t.getDarrivedate());
                    omPoMain.setIquantity(t.getIquantity());
                    omPoMain.setCdefine26(t.getCdefine26());
                    omPoMain.setCdefine27(t.getCdefine27());
                    omPoMain.setItaxprice(t.getItaxprice());
                    omPoMain.setIpertaxrate(BigDecimal.valueOf(rate));
                    omPoMain.setInatsum(t.getInatsum());  //??????????????????
                    omPoMain.setIsum(t.getInatsum());  //??????????????????

                    BigDecimal itaxrate=BigDecimal.ONE.add(( omPoMain.getIpertaxrate()).multiply(BigDecimal.valueOf(0.01)));

                    omPoMain.setIunitprice(omPoMain.getItaxprice().divide(itaxrate,8,BigDecimal.ROUND_HALF_UP));  //??????????????????
                    omPoMain.setImoney(omPoMain.getIsum().divide(itaxrate,8,BigDecimal.ROUND_HALF_UP));  //??????????????????

                    omPoMain.setInatunitprice(omPoMain.getItaxprice().divide(itaxrate,8,BigDecimal.ROUND_HALF_UP) );  //??????????????????
                    omPoMain.setInatmoney(omPoMain.getIsum().divide(itaxrate,8,BigDecimal.ROUND_HALF_UP) );  //??????????????????


                    omPoMain.setItax(omPoMain.getIsum().subtract(omPoMain.getImoney()));  //????????????
                    omPoMain.setInattax(omPoMain.getIsum().subtract(omPoMain.getImoney()));  //????????????



//                    omPoMain.setInatsum(t.getInatsum());

                    if(CustomStringUtils.isNotBlank(t.getModetailsid()))
                    {

//                        List<Rdrecords> listRd=rdrecordsMapper.getListByOm(t.getMainid());
//                        if(listRd!=null)
//                        {
//                            for(Rdrecords rdrecords:listRd)
//                            {
//                                BigDecimal price=omPoMain.getTaxmakeprice()==null?BigDecimal.ZERO:omPoMain.getTaxmakeprice();
//                                BigDecimal price1=rdrecords.getMakeprice()==null?BigDecimal.ZERO:rdrecords.getMakeprice();
//                                if(price.compareTo(price1)!=0)
//                                {
//                                    rdrecords.setMakeprice(omPoMain.getTaxmakeprice()==null?BigDecimal.ZERO:omPoMain.getTaxmakeprice());
//                                    rdrecords.setMakemny(rdrecords.getIquantity().multiply(rdrecords.getMakeprice()));
//                                    rdrecords.setIunitcost(rdrecords.getMakeprice());
//                                    rdrecords.setIprice(rdrecords.getMakemny());
//                                    rdrecords.setOrgmakeprice(rdrecords.getMakeprice());
//                                    rdrecords.setCdefine22("11");
//                                    rdrecordsMapper.updateByPrimaryKeySelective(rdrecords);
//                                }
//
//                            }
//                        }


                        omPoMain.setModetailsid(t.getModetailsid());
                        omPoMain.setMoid(t.getMoid());
                        n = omMoDetailsMapper.updateById(omPoMain);
                        if (n<0){
                            throw new Exception("?????????????????????");
                        }
                    }
                    else
                    {
                        omPoMain.setDefaultValueForJinGong();
                        omPoMain.setMoid(omProductPo.getMoid());



                        Integer u8fid = null;
                        u8fid=u8SystemUtils.getChildId(accId,"OM_MO",10,1);
                        u8SystemUtils.getFatherId(accId,"OM_Materials",10);
                        omPoMain.setModetailsid(u8fid);

                        t.setModetailsid(omPoMain.getModetailsid());
                        n = omMoDetailsMapper.insert(omPoMain);
                        if (n<0){
                            throw new Exception("?????????????????????");
                        }

                    }







                }
                //????????????????????????
                for(OmProductVM t:listDetail)
                {

                    if(CustomStringUtils.isNotBlank(t.getCinvcodes()))
                    {
                        if(CustomStringUtils.isBlank(t.getFbaseqtyn()))
                        {
                            throw new Exception("?????????????????????");
                        }





                        OmMoMaterials omPoDetails=new OmMoMaterials();
                        omPoDetails.setCinvcode(t.getCinvcodes());
                        omPoDetails.setIquantity(t.getFqtys());
                        omPoDetails.setIunitquantity(t.getFqtys());
                        omPoDetails.setFbaseqtyd(BigDecimal.ONE);
                        omPoDetails.setFbaseqtyn(t.getFbaseqtyn());
                        omPoDetails.setCdefine22(t.getCdefine22());
                        omPoDetails.setCdefine28(t.getCdefine28());
                        omPoDetails.setCdefine29(t.getCdefine29());
                        omPoDetails.setCdefine30(t.getCdefine30());
                        omPoDetails.setCdefine31(t.getCdefine31());
                        omPoDetails.setCdefine32(t.getCdefine32());
//                        omPoDetails.setCdefine27(t.getCdefine27());
//                        omPoDetails.setCdefine26(t.getCdefine26());
                        omPoDetails.setCdefine27(t.getCdefine26());


                        Inventory inventory = inventoryMapper.selectById(t.getCinvcodes());
                        if (inventory == null) {
                            throw new Exception("U8??????????????????????????????????????????");
                        }
                        //?????????
                        if(CustomStringUtils.isNotBlank(inventory.getCstcomunitcode()))
                        {
                            ComputationUnit unit = computationUnitMapper.selectById(inventory.getCstcomunitcode());
                            if (unit == null) {
                                throw new Exception("U8??????????????????????????????????????????");
                            }
                            omPoDetails.setCunitid(unit.getCcomunitcode());
                            omPoDetails.setFbasenumn(unit.getIchangrate());
                            omPoDetails.setIunitnum(unit.getIchangrate().multiply(t.getIquantity()));
                        }

                        for(OmProductVM q:list)
                        {
                            if(q.getRecordId().equals(t.getRecordId()))
                            {
                                omPoDetails.setModetailsid(q.getModetailsid());
                                omPoDetails.setDrequireddate(q.getDarrivedate());
                                break;
                            }
                        }
                        if(omPoDetails.getModetailsid()==null)
                        {
                            throw new Exception("?????????????????????????????????");
                        }
                        if(CustomStringUtils.isNotBlank(t.getMomaterialsid()))
                        {

                            omPoDetails.setMomaterialsid(t.getMomaterialsid());
                            n = omMoMaterialsMapper.updateById(omPoDetails);
                            if (n<0){
                                throw new Exception("????????????????????????");
                            }
                        }
                        else
                        {

                            omPoDetails.setDefaultValueForJinGong();

                            Integer u8fid = null;
                            u8fid=u8SystemUtils.getChildId(accId,"OM_Materials",10,1);

                            //??????id

                            omPoDetails.setMomaterialsid(u8fid);


                            n = omMoMaterialsMapper.insert(omPoDetails);
                            if (n<0){
                                throw new Exception("????????????????????????");
                            }
                        }
                        //???????????????????????????
                        Inventory inventoryUpdate=new Inventory();
                        inventoryUpdate.setCinvcode(t.getCinvcodes());
                        if(CustomStringUtils.isNotBlank(t.getCinvdefine2()))
                        {
                            inventoryUpdate.setCinvdefine2(t.getCinvdefine2());
                            inventoryMapper.updateById(inventoryUpdate);
                        }
//                        if(t.getCdefine26()!=null)
//                        {
//                            inventory.setIinvncost(Double.valueOf(t.getCdefine26().toString()));
//                        }

                    }




                }


            }
            //??????
            else
            {
                omProductPo.setDefaultValueForJinGong();
                Integer u8fid = null;
                u8fid= u8SystemUtils.getFatherId(accId,"OM_MO",10);
                omProductPo.setMoid(u8fid);

                //????????????

                omProductPo.setCmaker(SecurityUtil.getUser().getMyusername());
                //?????????????????????
                Vendor vendor=vendorMapper.selectById(omProductPo.getCvencode());
                omProductPo.setCvenaccount(vendor.getCvenaccount());
                omProductPo.setCvenbank(vendor.getCvenbank());
                omProductPo.setCvenperson(vendor.getCvenperson());
                int rate=13;
                if(!CustomStringUtils.isBlank(vendor.getCvendefine11()))
                {
                    rate=vendor.getCvendefine11();
                }
                omProductPo.setItaxrate(BigDecimal.valueOf(rate));
                omProductPo.setCexchName(vendor.getCvenexchName());
                String  sysbarcode = "||" + "ommo"+ "|" + omProductPo.getCcode();
                omProductPo.setCsysbarcode(sysbarcode);
                omProductPo.setCmemo(omProductPo.getCmemo());
                int n = omMoMainMapper.insert(omProductPo);
                if (n<0){
                    throw new Exception("?????????????????????");
                }
                //????????????????????????
                int row=1;
                for(OmProductVM t:list) {

                    if (CustomStringUtils.isBlank(t.getCinvcode())) {
                        throw new Exception("???????????????????????????");
                    }
                    if (CustomStringUtils.isBlank(t.getDstartdate())) {
                        throw new Exception("?????????????????????????????????");
                    }
                    if (CustomStringUtils.isBlank(t.getDarrivedate())) {
                        throw new Exception("?????????????????????????????????");
                    }
                    if (CustomStringUtils.isBlank(t.getIquantity())) {
                        throw new Exception("?????????????????????");
                    }
                    OmMoDetails omPoMain = new OmMoDetails();
                    omPoMain.setDefaultValueForJinGong();
                    omPoMain.setCinvcode(t.getCinvcode());
                    omPoMain.setMoid(omProductPo.getMoid());
                    omPoMain.setItaxprice(t.getItaxprice()==null?BigDecimal.ZERO:t.getItaxprice());
                    omPoMain.setIpertaxrate(BigDecimal.valueOf(rate));
                    omPoMain.setInatsum(t.getInatsum()==null?BigDecimal.ZERO:t.getInatsum());  //??????????????????
                    omPoMain.setIsum(omPoMain.getInatsum());  //??????????????????
                    omPoMain.setDstartdate(t.getDstartdate());
                    omPoMain.setDarrivedate(t.getDarrivedate());
                    omPoMain.setIquantity(t.getIquantity()==null?BigDecimal.ZERO:t.getIquantity());
                    omPoMain.setImrpqty(t.getIquantity()==null?BigDecimal.ZERO:t.getIquantity());
                    omPoMain.setCdefine26(t.getCdefine26()==null?BigDecimal.ZERO:t.getCdefine26());
                    omPoMain.setCdefine27(t.getCdefine27()==null?BigDecimal.ZERO:t.getCdefine27());
                    omPoMain.setIvouchrowno(row);
                    sysbarcode = "||" + "ommo"+ "|" + omProductPo.getCcode()+"|"+row;
                    omPoMain.setCbsysbarcode(sysbarcode);
                    BigDecimal itaxrate = BigDecimal.ONE.add((omPoMain.getIpertaxrate()).multiply(BigDecimal.valueOf(0.01)));

                    omPoMain.setIunitprice(omPoMain.getItaxprice().divide(itaxrate, 8, BigDecimal.ROUND_HALF_UP));  //??????????????????
                    omPoMain.setImoney(omPoMain.getIsum().divide(itaxrate, 8, BigDecimal.ROUND_HALF_UP));  //??????????????????

                    omPoMain.setInatunitprice(omPoMain.getItaxprice().divide(itaxrate, 8, BigDecimal.ROUND_HALF_UP));  //??????????????????
                    omPoMain.setInatmoney(omPoMain.getIsum().divide(itaxrate,8,BigDecimal.ROUND_HALF_UP) );  //??????????????????


                    omPoMain.setItax(omPoMain.getIsum().subtract(omPoMain.getImoney()));  //????????????
                    omPoMain.setInattax(omPoMain.getIsum().subtract(omPoMain.getImoney()));  //????????????


                    //??????id

                    u8fid = null;
                    u8fid = u8SystemUtils.getChildId(accId, "OM_MO", 10, 1);
                    u8SystemUtils.getFatherId(accId, "OM_Materials", 10);
                    omPoMain.setModetailsid(u8fid);
                    t.setRowNo(row);
                    t.setMoid(omPoMain.getMoid());
                    t.setModetailsid(omPoMain.getModetailsid());
                    n = omMoDetailsMapper.insert(omPoMain);
                    if (n < 0) {
                        throw new Exception("?????????????????????");
                    }


                    int izExist = 0;
                    for (OmProductVM material : listDetail)
                    {
                        if(material.getRecordId().equals(t.getRecordId()))
                        {
                            izExist=1;
                            break;
                        }
                    }
                    if(izExist==0)
                    {
                        OmMoMaterials omPoDetails = new OmMoMaterials();
                        omPoDetails.setDefaultValueForJinGong();
                        omPoDetails.setMoid(t.getMoid());
                        omPoDetails.setModetailsid(t.getModetailsid());
                        omPoDetails.setDrequireddate(t.getDarrivedate());
                        sysbarcode = "||" + "ommo"+ "|" + omProductPo.getCcode()+"|"+row+"|1";
                        omPoDetails.setCsubsysbarcode(sysbarcode);

                        u8fid = null;
                        u8fid=u8SystemUtils.getChildId(accId,"OM_Materials",10,1);
                        //??????id
                        omPoDetails.setMomaterialsid(u8fid);
                        omPoDetails.setCinvcode(t.getCinvcode());
                        omPoDetails.setIquantity(t.getIquantity());
                        omPoDetails.setIunitquantity(t.getIquantity());
                        omPoDetails.setFbaseqtyd(BigDecimal.ONE);
                        omPoDetails.setFbaseqtyn(t.getIquantity());
                        n = omMoMaterialsMapper.insert(omPoDetails);
                        if (n < 0) {
                            throw new Exception("????????????????????????");
                        }
                    }
                    row++;
                }
                //????????????????????????
                row=1;
                for(OmProductVM t:listDetail) {

                    if (CustomStringUtils.isNotBlank(t.getCinvcodes())) {
                        if (CustomStringUtils.isBlank(t.getFbaseqtyn()) && t.getFbaseqtyn().compareTo(BigDecimal.ZERO) == 0) {
                            throw new Exception("?????????????????????0???");
                        }

                        OmMoMaterials omPoDetails = new OmMoMaterials();
                        omPoDetails.setDefaultValueForJinGong();
                        for(OmProductVM q:list)
                        {
                            if(q.getRecordId().equals(t.getRecordId()))
                            {
                                omPoDetails.setMoid(q.getMoid());
                                omPoDetails.setModetailsid(q.getModetailsid());
                                omPoDetails.setDrequireddate(q.getDarrivedate());
                                sysbarcode = "||" + "ommo"+ "|" + omProductPo.getCcode()+"|"+q.getRowNo()+"|"+row;

                                omPoDetails.setCsubsysbarcode(sysbarcode);

                                break;
                            }

                        }
                        if(omPoDetails.getMoid()==null)
                        {
                            throw new Exception("?????????????????????????????????");
                        }
                        u8fid = null;
                        u8fid=u8SystemUtils.getChildId(accId,"OM_Materials",10,1);
                        //??????id
                        omPoDetails.setMomaterialsid(u8fid);
                        omPoDetails.setCinvcode(t.getCinvcodes());
                        omPoDetails.setIquantity(t.getFqtys());
                        omPoDetails.setIunitquantity(t.getFqtys());
                        omPoDetails.setFbaseqtyd(BigDecimal.ONE);
                        omPoDetails.setFbaseqtyn(t.getFbaseqtyn());
                        omPoDetails.setCdefine22(t.getCdefine22());
                        omPoDetails.setCdefine28(t.getCdefine28());
                        omPoDetails.setCdefine29(t.getCdefine29());
                        omPoDetails.setCdefine30(t.getCdefine30());
                        omPoDetails.setCdefine31(t.getCdefine31());
                        omPoDetails.setCdefine32(t.getCdefine32());
//                        omPoDetails.setCdefine27(t.getCdefine27());
//                        omPoDetails.setCdefine26(t.getCdefine26());
                        omPoDetails.setCdefine27(t.getCdefine26());
                        omPoDetails.setIrowno(row);

                        Inventory inventory = inventoryMapper.selectById(t.getCinvcodes());
                        if (inventory == null) {
                            throw new Exception("U8??????????????????????????????????????????");
                        }
                        //?????????
                        if(CustomStringUtils.isNotBlank(inventory.getCstcomunitcode()))
                        {
                            ComputationUnit unit = computationUnitMapper.selectById(inventory.getCstcomunitcode());
                            if (unit == null) {
                                throw new Exception("U8??????????????????????????????????????????");
                            }
                            omPoDetails.setCunitid(unit.getCcomunitcode());
                            omPoDetails.setFbasenumn(unit.getIchangrate());
                            omPoDetails.setIunitnum(unit.getIchangrate().multiply(t.getIquantity()));
                        }

                        n = omMoMaterialsMapper.insert(omPoDetails);
                        if (n < 0) {
                            throw new Exception("????????????????????????");
                        }
                        //???????????????????????????
                        Inventory inventoryUpdate = new Inventory();
                        inventoryUpdate.setCinvcode(t.getCinvcodes());
                        if (CustomStringUtils.isNotBlank(t.getCinvdefine2())) {
                            inventoryUpdate.setCinvdefine2(t.getCinvdefine2());
                            inventoryMapper.updateById(inventoryUpdate);
                        }

                        row++;
                    }

                }

            }

            result.setResult(omProductPo.getMoid());
            result.setResult1(omProductPo.getCcode());
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }





    /**
     * ??????????????????
     * @param mainid

     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteByDetailMainId(Integer id,Integer mainid,Integer subid) throws Exception{
        ResponseResult result = new ResponseResult();
        try{
            OmMoMain omProductPo= omMoMainMapper.selectById(id);
            if(omProductPo!=null)
            {
                if(omProductPo.getCstate().compareTo(Byte.valueOf("1"))!=0)
                {
                    result.setSuccess(false);
                    result.setMsg("??????????????????????????????");
                    return result;
                }

                if(CustomStringUtils.isNotBlank(subid))
                {
                    int n=omMoDetailsMapper.deleteById(subid);
                    if (n<=0){
                        throw new Exception("???????????????????????????");
                    }
                }

                int n=omMoMainMapper.deleteById(mainid);
                if (n<=0){
                    throw new Exception("???????????????????????????");
                }
            }
            else
            {
                result.setSuccess(false);
                result.setMsg("??????????????????");
                return result;
            }


        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }
    /**
     * ??????????????????
     * @param mainid

     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteByDetailSubId(Integer id,Integer mainid,Integer subid) throws Exception{
        ResponseResult result = new ResponseResult();
        try{
            OmMoMain omProductPo= omMoMainMapper.selectById(id);
            if(omProductPo!=null)
            {
                if(omProductPo.getCstate().compareTo(Byte.valueOf("1"))!=0)
                {
                    result.setSuccess(false);
                    result.setMsg("??????????????????????????????");
                    return result;
                }
                if(CustomStringUtils.isNotBlank(subid))
                {
                    omMoMaterialsMapper.deleteById(subid);

                }
            }
            else
            {
                result.setSuccess(false);
                result.setMsg("??????????????????");
                return result;
            }


        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }

    /**
     * ??????
     * @param id

     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult delete(Integer id) throws Exception{
        ResponseResult result = new ResponseResult();
        try{
            OmMoMain omMoMain= omMoMainMapper.selectById(id);
            if(omMoMain!=null)
            {
                if(!omMoMain.getCstate().toString().equals("0"))
                {
                    result.setSuccess(false);
                    result.setMsg("??????????????????????????????");
                    return result;
                }
                LambdaQueryWrapper<OmMoMaterials> selectM= new LambdaQueryWrapper<>();
                selectM.apply("  modetailsid in (select modetailsid from OM_MODetails where moid='"+id+"' )" );
                List<OmMoMaterials> listM = omMoMaterialsMapper.selectList(selectM);
                if(listM!=null)
                {
                    for(OmMoMaterials omMoMaterials:listM)
                    {
                        omMoMaterialsMapper.deleteById(omMoMaterials.getMomaterialsid());
                    }
                }

                LambdaQueryWrapper<OmMoDetails> selectD = new LambdaQueryWrapper<>();
                selectD.eq(OmMoDetails::getMoid,id);
                List<OmMoDetails> listD = omMoDetailsMapper.selectList(selectD);
                if(listD!=null)
                {
                    for(OmMoDetails omMoDetails:listD)
                    {
                        omMoDetailsMapper.deleteById(omMoDetails.getModetailsid());
                    }
                }


                omMoMainMapper.deleteById(id);


            }
            else
            {
                result.setSuccess(false);
                result.setMsg("??????????????????");
                return result;
            }


        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }



    /**
     * ??????
     * @param id

     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult check(Integer id) throws Exception{
        ResponseResult result = new ResponseResult();
        try{
            OmMoMain omProductPo= omMoMainMapper.selectById(id);
            if(omProductPo!=null)
            {
                if(!omProductPo.getCstate().toString().equals("0"))
                {
                    result.setSuccess(false);
                    result.setMsg("?????????????????????????????????????????????");
                    return result;
                }
                OmMoMain m= new OmMoMain();
                m.setMoid(id);
                m.setCstate(Byte.valueOf("1"));
                m.setIverifystatenew(2);

                m.setCverifier(SecurityUtil.getUser().getMyusername());

                m.setDverifydate(DateUtil.parseStrToDate(DateUtil.getDateStr(new Date(),"yyyy-MM-dd"),"yyyy-MM-dd") );
                m.setDverifytime(new Date());
                int n=omMoMainMapper.updateById(m);
                if(n<=0)
                {
                    result.setSuccess(false);
                    result.setMsg("???????????????");
                    return result;
                }
            }
            else
            {
                result.setSuccess(false);
                result.setMsg("??????????????????");
                return result;
            }


        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }
    /**
     * ??????
     * @param id

     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult unCheck(Integer id) throws Exception{
        ResponseResult result = new ResponseResult();
        try{
            OmMoMain omProductPo= omMoMainMapper.selectById(id);
            if(omProductPo!=null)
            {
                if(!omProductPo.getCstate().toString().equals("1"))
                {
                    result.setSuccess(false);
                    result.setMsg("??????????????????????????????????????????");
                    return result;
                }


                LambdaQueryWrapper<OmMoMaterials> selectM= new LambdaQueryWrapper<>();
                selectM.apply("  modetailsid in (select modetailsid from OM_MODetails where moid='"+id+"' )" );
                List<OmMoMaterials> listM = omMoMaterialsMapper.selectList(selectM);
                if(listM!=null)
                {
                    for(OmMoMaterials omMoMaterials:listM)
                    {
                        if(omMoMaterials.getIsendqty()!=null&&omMoMaterials.getIsendqty().compareTo(BigDecimal.ZERO)>0)
                        {
                            result.setSuccess(false);
                            result.setMsg("???????????????????????????????????????????????????");
                            return result;
                        }
                    }
                }

                LambdaQueryWrapper<OmMoDetails> selectD = new LambdaQueryWrapper<>();
                selectD.eq(OmMoDetails::getMoid,id);
                List<OmMoDetails> listD = omMoDetailsMapper.selectList(selectD);
                if(listD!=null)
                {
                    for(OmMoDetails omMoDetails:listD)
                    {
                        if(omMoDetails.getIreceivedqty()!=null&&omMoDetails.getIreceivedqty().compareTo(BigDecimal.ZERO)>0)
                        {
                            result.setSuccess(false);
                            result.setMsg("???????????????????????????????????????????????????");
                            return result;
                        }
                    }
                }



                OmMoMain m= new OmMoMain();
                m.setMoid(id);
                m.setCstate(Byte.valueOf("0"));
                m.setCverifier(null);
                m.setDverifytime(null);
                m.setDverifydate(null);
                int n=omMoMainMapper.updateUnCheck(m);
                if(n<=0)
                {
                    result.setSuccess(false);
                    result.setMsg("???????????????");
                    return result;
                }
            }
            else
            {
                result.setSuccess(false);
                result.setMsg("??????????????????");
                return result;
            }


        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }


}
