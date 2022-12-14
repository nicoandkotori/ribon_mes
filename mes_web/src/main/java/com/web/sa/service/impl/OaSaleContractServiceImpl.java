package com.web.sa.service.impl;

import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.util.*;
import com.modules.data.mybatis.DBTypeEnum;
import com.modules.data.mybatis.DbContextHolder;
import com.task.Scheduler;
import com.util.ConnectString;
import com.web.basicinfo.entity.*;
import com.web.basicinfo.mapper.*;
import com.web.basicinfo.service.IInventoryService;
import com.web.sa.entity.*;
import com.web.sa.mapper.*;
import com.web.sa.service.IOaSaleContractService;
import com.web.system.entity.MenuClient;
import com.web.u8system.entity.U8Common;
import com.web.u8system.mapper.U8CommonMapper;
import com.web.u8system.util.U8SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author caihuan
 * @since 2022-06-28
 */
@Service
public class OaSaleContractServiceImpl extends ServiceImpl<OaSaleContractMapper, OaSaleContract> implements IOaSaleContractService {

    @Autowired
    private OaSaleContractContrastMapper oaSaleContractContrastMapper;

    @Autowired
    private OaSaleContractContrastDetailMapper oaSaleContractContrastDetailMapper;
    @Autowired
    private OaSaleContractMapper oaSaleContractMapper;

    @Autowired
    private InventoryMapper inventoryMapper;
    @Autowired
    private InventorySubMapper inventorySubMapper;
    @Autowired
    private InventoryExtradefineMapper inventoryExtradefineMapper;
    @Autowired
    private ComputationUnitMapper unitMapper;
    @Autowired
    private ComputationGroupMapper unitGroupMapper;
    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private PersonMapper personMapper;
    @Autowired
    private BasPartMapper basPartMapper;
    @Autowired
    private U8SystemUtils u8SystemUtils;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private CustomerExtradefineMapper customerExtradefineMapper;
    @Autowired
    private SoDetailsMapper soDetailsMapper;
    @Autowired
    private SoMainMapper soMainMapper;
    @Autowired
    private U8CommonMapper u8commonMapper;

    @Autowired
    private OrderProductionScheduleMapper orderProductionScheduleMapper;
    @Value("${account.acountId}")
    private String accId;


    private static final Logger log = LoggerFactory.getLogger(Scheduler.class);


    /**
     * ??????????????????
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncSaleContractData()
    {
        syncSaleContract();
    }
    /**
     * ?????????????????????????????????
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncSaleContractDataEdit()
    {
        syncSaleContractEdit();
    }

    /**
     * ??????????????????  ???????????????
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncSaleContractDataDelete()
    {
        syncSaleContractDelete();
    }

    /**
     * ????????????????????????????????????
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncSaleTrialContractData()
    {
        syncSaleTrialContract();
    }
    /**
     * ????????????????????????????????????
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncSaleTrialContractDataEdit()
    {
        syncSaleTrialContractEdit();
    }

    /**
     * ??????????????????  ???????????????
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncSaleTrialContractDataDelete()
    {
        syncSaleTrialContractDelete();
    }
    /**
     * ?????????????????????????????????
     * @return
     */
//    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult syncSaleContract() {
        ResponseResult result = new ResponseResult();
        int row=1;
        try{


            //????????????????????????
            List<OaSaleContract> listSyncMain=oaSaleContractMapper.getListByMainSync(new OaSaleContract(),ParamUtil.getParam("oaDatabase").toString());
            if(listSyncMain!=null)
            {

                //??????id??????
                List<OaSaleContract> newList= listSyncMain.stream()
                        .collect(
                                Collectors.collectingAndThen(
                                        Collectors.toCollection(() -> new TreeSet<>(
                                                Comparator.comparing(o -> o.getId())
                                        )), ArrayList::new
                                )
                        );

                //???????????????????????????
                for(OaSaleContract q:newList)
                {

                    if(CustomStringUtils.isBlank(q.getField0023()))
                    {
                        throw new Exception("??????????????????");
                    }

                    Customer customer=customerMapper.selectById(q.getField0023());
                    //???????????????????????????U8
                    if(customer==null)
                    {
                        //region ????????????
                        customer=new Customer();
                        customer.setDefaultValue();
                        customer.setCcuscode(q.getField0023());
                        customer.setCcusname(q.getField0018());
                        customer.setCcusabbname(q.getField0018());
                        customer.setCcccode(q.getField0023().substring(0,3));
                        customer.setCcusaddress(q.getField0022());
                        customer.setDcusdevdate(DateUtil.parseStrToDate(DateUtil.getDateStr(new Date(),"yyyy-MM-dd"),"yyyy-MM-dd"));
                        customer.setCcusperson(q.getField0020());
                        customer.setCcusphone(q.getField0019());
                        customer.setCcusfax(q.getField0021());
                        customer.setCcusoaddress(q.getField0022());
                        customer.setCcusheadcode(q.getField0023());
                        customer.setIid(q.getField0023());
                        customer.setCinvoicecompany(q.getField0023());
                        customer.setCcuscreditcompany(q.getField0023());
                        customerMapper.insert(customer);
                        CustomerExtradefine customerExtradefine=new CustomerExtradefine();
                        customerExtradefine.setCcuscode(customer.getCcuscode());
                        customerExtradefineMapper.insert(customerExtradefine);

                        //endregion
                    }


                    //?????????????????????????????????????????????????????????
                    LambdaQueryWrapper<SoMain> selectSo=new LambdaQueryWrapper<>();
                    selectSo.eq(SoMain::getCdefine1, q.getField0006());
                    List<SoMain> listSo=soMainMapper.selectList(selectSo);
                    SoMain soMain=new SoMain();
                    if(listSo==null||listSo.size()==0)
                    {
                        //????????????????????????
                        Integer soId=u8SystemUtils.getFatherId(accId,"Somain",10);

                        soMain.setDefaultValue();
                        soMain.setId(soId);
                        soMain.setCstcode(q.getStCode());

                        LambdaQueryWrapper<Person> selectPerson=new LambdaQueryWrapper<>();
                        selectPerson.eq(Person::getCpersonname, q.getPersonName());
                        List<Person> listPerson=personMapper.selectList(selectPerson);
                        if(listPerson!=null&&listPerson.size()>0)
                        {
                            soMain.setCpersoncode(listPerson.get(0).getCpersoncode());
                        }

                        soMain.setDdate(DateUtil.parseStrToDate(DateUtil.getDateStr(q.getStartDate(),"yyyy-MM-dd"),"yyyy-MM-dd"));
//                        soMain.setDdate(DateUtil.parseStrToDate(DateUtil.getDateStr(new Date(),"yyyy-MM-dd"),"yyyy-MM-dd"));

                        //region ???????????????
                        U8Common ucom=new U8Common();
                        ucom.setCardNumber("17");
                        ucom.setcWhCode("");
                        ucom.setVendor("");
                        U8Common rum= u8commonMapper.GetVoucherNumber(ucom);
                        String str = "", str1 = "", strRule = "";  //str1:??????
                        int len = 0;

                        //??????1----------------------------------------------------------
                        str = rum.getPrefix1();
                        len = rum.getPrefix1Len();
                        strRule = rum.getPrefix1Rule();
                        if (str.equals("?????????"))
                        {

                            U8Common rum1= u8commonMapper.GetVouchercontrapose(ucom);
                            str1=rum1.getcCode();
                        }
                        else
                        {
                            str1= str1+ ConnectString.getPrefixByType(str,len,strRule,"");
                        }



                        //-------------------------------------------------------------

                        //??????1----------------------------------------------------------
                        str = rum.getPrefix2();
                        len = rum.getPrefix2Len();
                        strRule = rum.getPrefix2Rule();

                        if (str.equals("?????????"))
                        {

                            U8Common rum1= u8commonMapper.GetVouchercontrapose(ucom);
                            str1=rum1.getcCode();
                        }
                        else
                        {
                            str1= str1+ ConnectString.getPrefixByType(str,len,strRule,"");
                        }
                        //-------------------------------------------------------------

                        //??????1----------------------------------------------------------
                        str = rum.getPrefix3();
                        len = rum.getPrefix3Len();
                        strRule = rum.getPrefix3Rule();

                        if (str.equals("?????????"))
                        {

                            U8Common rum1= u8commonMapper.GetVouchercontrapose(ucom);
                            str1=rum1.getcCode();
                        }
                        else
                        {
                            str1= str1+ ConnectString.getPrefixByType(str,len,strRule,"");
                        }
                        //-------------------------------------------------------------

                        str = rum.getiStartNumber().toString();  //????????????

                        U8Common rg=ConnectString.GetGlide(ucom.getCardNumber(),rum.getGlide(),rum.getGlideRule(),"");
                        U8Common rg1= u8commonMapper.GetGlide(rg);
                        int cNumber=rum.getiStartNumber();
                        if(rg1!=null)
                        {
                            cNumber = rg1.getcNumber()+1;
                            rg.setcNumber(cNumber);
                            rg.setCardNumber(ucom.getCardNumber());
                            u8commonMapper.UpdateGlide(rg);
                        }
                        else
                        {
                            if(rg.getGlide().equals("??????")||rg.getGlide().equals("????????????"))
                            {
                                rg.setcNumber(cNumber);
                                rg.setCardNumber(ucom.getCardNumber());
                            }
                            else if(rg.getGlide().equals("?????????"))
                            {
                                rg.setcCode("");
                                rg.setcNumber(cNumber);
                                rg.setGlideRule(null);
                                rg.setCardNumber(ucom.getCardNumber());
                            }
                            else
                            {
                                rg.setcCode(null);
                            }
                            u8commonMapper.insertGlide(rg);

                        }
                        String cnum =String.valueOf(cNumber) ;
                        while (cnum.length() < rum.getGlideLen())
                        {
                            cnum = "0" + cnum;
                        }
                        str1 = str1 + cnum ;
                        String RdCode= str1;
//            String RdCode=  clib.GetU8NewVoucherIDByCardNumber(connectionString,"0411","","",true);
                        if (RdCode != "")
                            soMain.setCsocode(RdCode);
                        else
                            throw new Exception("??????cCode?????????????????????");
                        //endregion
//                        soMain.setCsocode(q.getField0006());
                        soMain.setCcuscode(customer.getCcuscode());

                        String depCode="112";
                        String depName="?????????";

                        if(listPerson!=null&&listPerson.size()>0) {
                            Department department = departmentMapper.selectById(listPerson.get(0).getCdepcode());
                            if (department != null) {
                                depCode = department.getCdepcode();
                                depName = department.getCdepname();
                            }
                        }
                        soMain.setCdepcode(depCode);
                        soMain.setCdefine3(depName);
                        soMain.setCcusoaddress(customer.getCcusoaddress());
                        soMain.setCcusname(customer.getCcusname());

                        soMain.setDpremodatebt(q.getField0056());
                        soMain.setDpredatebt(q.getField0057());
                        soMain.setCinvoicecompany(customer.getCinvoicecompany());
                        String  sysbarcode = "||" + "SA17" + soMain.getCsocode();
                        soMain.setCmaker(q.getCreateUser()==null?"demo":q.getCreateUser());
                        soMain.setCsysbarcode(sysbarcode);
                        soMain.setDverifydate(DateUtil.parseStrToDate(DateUtil.getDateStr(new Date(),"yyyy-MM-dd"),"yyyy-MM-dd"));
                        soMain.setDverifysystime(new Date());
                        soMain.setCverifier(q.getCreateUser()==null?"demo":q.getCreateUser());
                        soMain.setCdefine1(q.getField0006());

                        soMainMapper.insert(soMain);
                    }
                    else
                    {
                        soMain=listSo.get(0);
                    }


                    //??????id??????
                    List<OaSaleContract> list= listSyncMain.stream()
                            .filter(p ->p.getId().toString().equals(q.getId().toString()))
                            .collect(
                                    Collectors.collectingAndThen(
                                            Collectors.toCollection(() -> new TreeSet<>(
                                                    Comparator.comparing(o -> o.getField0103())
                                            )), ArrayList::new
                                    )
                            );


                    for(OaSaleContract detail:list) {
                        log.info("(????????????)???????????????"+ JSON.toJSONString(detail) + DateUtil.getDateStr(new Date(), "yyyy-MM-dd hh:mm:ss"));
                        //region ??????????????????  ??????????????????
                        //?????????+??????  =???????????????????????????
                        Inventory inventory = inventoryMapper.selectById(q.getField0006() + "-" + detail.getField0103());
                        BasPart basPart = new BasPart();
                        if (inventory == null) {
                            inventory = new Inventory();
                            inventory.setDefaultValue();
                            inventory.setCinvcode(q.getField0006() + "-" + detail.getField0103());
                            inventory.setCinvname(detail.getField0008());
                            inventory.setCinvstd(detail.getField0009());
                            inventory.setCinvccode("50101");


                            inventory.setCinvdefine6(detail.getDeviceClass());
                            inventory.setCinvdefine5(detail.getDeviceType());

                            LambdaQueryWrapper<ComputationUnit> select = new LambdaQueryWrapper<>();
                            select.eq(ComputationUnit::getCcomunitname, detail.getField0123());
                            select.apply(" cGroupCode  in (select cGroupCode from  ComputationGroup where iGroupType=0) ");
                            List<ComputationUnit> listUnit = unitMapper.selectList(select);
                            //??????????????????????????????????????????
                            if (listUnit != null && listUnit.size() > 0) {
                                inventory.setCcomunitcode(listUnit.get(0).getCcomunitcode());
                                inventory.setCgroupcode(listUnit.get(0).getCgroupcode());
                                inventory.setCshopunit(inventory.getCcomunitcode());
                                inventory.setIgrouptype(Byte.valueOf("0"));
                            } else {

                                LambdaQueryWrapper<ComputationUnit> selectUnit = new LambdaQueryWrapper<>();
                                selectUnit.eq(ComputationUnit::getCcomunitname, "???");
                                selectUnit.apply(" cGroupCode  in (select cGroupCode from  ComputationGroup where iGroupType=0) ");
                                List<ComputationUnit> listUnit1 = unitMapper.selectList(selectUnit);
                                if (listUnit1 != null && listUnit1.size() > 0) {
                                    inventory.setCcomunitcode(listUnit1.get(0).getCcomunitcode());
                                    inventory.setCgroupcode(listUnit1.get(0).getCgroupcode());
                                    inventory.setCshopunit(inventory.getCcomunitcode());
                                    inventory.setIgrouptype(Byte.valueOf("0"));
                                } else {
                                    throw new Exception("????????????????????????");
                                }

                            }


                            inventory.setBsale(true);
                            inventory.setBptomodel(true);
                            inventory.setBforeexpland(true);
                            inventory.setBinvmodel(true);
                            inventory.setBexpsale(false);
                            inventory.setBbommain(true);
                            inventory.setBbomsub(true);

                            //???????????????????????????
                            inventory.setBptomodel(false);
                            inventory.setBinvmodel(false);
                            inventory.setBself(true);
                            inventory.setBproducing(true);
                            inventory.setCdefwarehouse("03");
                            inventory.setCplanmethod("N");

                            //??????????????????
                            inventoryMapper.insert(inventory);

                            //??????????????????????????????
                            InventoryExtradefine inventoryExtradefine = new InventoryExtradefine();
                            inventoryExtradefine.setCinvcode(inventory.getCinvcode());
                            inventoryExtradefineMapper.insert(inventoryExtradefine);

                            //?????????????????????
                            InventorySub inventorySub = new InventorySub();
                            inventorySub.setDefaultValue();
                            inventorySub.setCinvsubcode(inventory.getCinvcode());
                            inventorySubMapper.insert(inventorySub);


                            basPart.setDefaultValue();
                            Integer u8fid = null;
                            u8fid = u8SystemUtils.getId(accId, "bas_part", 10);
                            basPart.setPartid(u8fid);
                            basPart.setInvcode(inventory.getCinvcode());
                            basPart.setLlc(0);
                            basPart.setIsurenesstype(Short.valueOf("1"));
                            basPartMapper.insert(basPart);
                        } else {
                            //??????part
                            LambdaQueryWrapper<BasPart> select = new LambdaQueryWrapper<>();
                            select.eq(BasPart::getInvcode, inventory.getCinvcode());
                            List<BasPart> listPart = basPartMapper.selectList(select);
                            if (listPart != null && listPart.size() > 0) {
                                basPart = listPart.get(0);
                            }
                        }

                        //endregion


                        SoDetails soContractInv = new SoDetails();
                        LambdaQueryWrapper<SoDetails> selectSoDetail = new LambdaQueryWrapper<>();
                        selectSoDetail.eq(SoDetails::getId, soMain.getId());
                        selectSoDetail.eq(SoDetails::getCinvcode, q.getField0006() + "-" + detail.getField0103());
                        List<SoDetails> listSoDetail = soDetailsMapper.selectList(selectSoDetail);
                        BigDecimal itaxrate = BigDecimal.ZERO;
                        if (listSoDetail == null || listSoDetail.size() == 0) {
                            Integer soContracyId = u8SystemUtils.getChildId(accId, "Somain", 10, 1);
                            soContractInv.setDefaultValue();
                            soContractInv.setId(soMain.getId());
                            soContractInv.setIsosid(soContracyId);
                            soContractInv.setCsocode(soMain.getCsocode());
                            soContractInv.setCinvcode(inventory.getCinvcode());
                            soContractInv.setCinvname(inventory.getCinvname());
//                            soContractInv.setDpredate(soMain.getDdate());
//                            soContractInv.setDpremodate(soMain.getDdate());
                            soContractInv.setIquantity(detail.getField0010());
                            soContractInv.setItaxunitprice(detail.getField0011() == null ? BigDecimal.ZERO : detail.getField0011());
                            soContractInv.setIsum(detail.getField0012() == null ? BigDecimal.ZERO : detail.getField0012());
                            itaxrate = BigDecimal.ONE.add((soContractInv.getItaxrate()).multiply(BigDecimal.valueOf(0.01)));
                            soContractInv.setIunitprice(soContractInv.getItaxunitprice().divide(itaxrate, 2, BigDecimal.ROUND_HALF_UP));  //??????????????????
                            soContractInv.setImoney(soContractInv.getIsum().divide(itaxrate, 2, BigDecimal.ROUND_HALF_UP));  //??????????????????
                            soContractInv.setItax(soContractInv.getIsum().subtract(soContractInv.getImoney()));

                            soContractInv.setInatunitprice(soContractInv.getIunitprice());
                            soContractInv.setInatmoney(soContractInv.getImoney());
                            soContractInv.setInattax(soContractInv.getItax());
                            soContractInv.setInatsum(soContractInv.getIsum());
                            soContractInv.setIrowno(Integer.valueOf(detail.getField0103()));
                            soContractInv.setBsaleprice(true);
                            String csysbarcode = "||" + "SA17|" + soMain.getCsocode() + "|" + String.valueOf(soContractInv.getIrowno());
                            soContractInv.setCbsysbarcode(csysbarcode);
                            soContractInv.setCparentcode("{" + UUID.randomUUID().toString() + "}");
                            soContractInv.setDpremodate(detail.getField0056());
                            soContractInv.setDpredate(detail.getField0057());
                            soContractInv.setCdefine22(soMain.getCdefine1());

                            soContractInv.setCdefine32(detail.getDeviceName());
                            soContractInv.setCdefine33(detail.getDeviceStd());
                            soContractInv.setCmemo(detail.getRemark());
                            //??????OA??????id?????????????????????parent-
                            soContractInv.setCdefine30("parent-"+detail.getContrastId());
                            soDetailsMapper.insert(soContractInv);
//                            row++;


                            //?????????????????????oa
                            //??????U8?????????
                            OaSaleContractContrast oaSaleContractContrast=new OaSaleContractContrast();
                            oaSaleContractContrast.setId(detail.getContrastId());
                            oaSaleContractContrast.setField0033(soContractInv.getIsosid().toString());
                            oaSaleContractContrastMapper.updateSoCode(oaSaleContractContrast,ParamUtil.getParam("oaDatabase").toString());
                        }
                    }


                }
            }



            //????????????????????????
            List<OaSaleContract> listSync=oaSaleContractMapper.getListBySync(new OaSaleContract(),ParamUtil.getParam("oaDatabase").toString());
            if(listSync!=null)
            {
                //??????id??????
                List<OaSaleContract> newList= listSync.stream()
                        .collect(
                                Collectors.collectingAndThen(
                                        Collectors.toCollection(() -> new TreeSet<>(
                                                Comparator.comparing(o -> o.getId())
                                        )), ArrayList::new
                                )
                        );

                //???????????????????????????
                for(OaSaleContract q:newList)
                {

                    if(CustomStringUtils.isBlank(q.getField0023()))
                    {
                        throw new Exception("??????????????????");
                    }

                    Customer customer=customerMapper.selectById(q.getField0023());
                    //???????????????????????????U8
                    if(customer==null)
                    {
                        //region ????????????
                        customer=new Customer();
                        customer.setDefaultValue();
                        customer.setCcuscode(q.getField0023());
                        customer.setCcusname(q.getField0018());
                        customer.setCcusabbname(q.getField0018());
                        customer.setCcccode(q.getField0023().substring(0,3));
                        customer.setCcusaddress(q.getField0022());
                        customer.setDcusdevdate(DateUtil.parseStrToDate(DateUtil.getDateStr(new Date(),"yyyy-MM-dd"),"yyyy-MM-dd"));
                        customer.setCcusperson(q.getField0020());
                        customer.setCcusphone(q.getField0019());
                        customer.setCcusfax(q.getField0021());
                        customer.setCcusoaddress(q.getField0022());
                        customer.setCcusheadcode(q.getField0023());
                        customer.setIid(q.getField0023());
                        customer.setCinvoicecompany(q.getField0023());
                        customer.setCcuscreditcompany(q.getField0023());
                        customerMapper.insert(customer);
                        CustomerExtradefine customerExtradefine=new CustomerExtradefine();
                        customerExtradefine.setCcuscode(customer.getCcuscode());
                        customerExtradefineMapper.insert(customerExtradefine);

                        //endregion
                    }


                    //?????????????????????????????????????????????????????????
                    LambdaQueryWrapper<SoMain> selectSo=new LambdaQueryWrapper<>();
                    selectSo.eq(SoMain::getCdefine1, q.getField0006());
                    List<SoMain> listSo=soMainMapper.selectList(selectSo);
                    SoMain soMain=new SoMain();
                    if(listSo==null||listSo.size()==0)
                    {
                        //????????????????????????
                        Integer soId=u8SystemUtils.getFatherId(accId,"Somain",10);

                        soMain.setDefaultValue();
                        soMain.setId(soId);
                        soMain.setCstcode(q.getStCode());
                        LambdaQueryWrapper<Person> selectPerson=new LambdaQueryWrapper<>();
                        selectPerson.eq(Person::getCpersonname, q.getPersonName());
                        List<Person> listPerson=personMapper.selectList(selectPerson);
                        if(listPerson!=null&&listPerson.size()>0)
                        {
                            soMain.setCpersoncode(listPerson.get(0).getCpersoncode());
                        }


                        soMain.setDdate(DateUtil.parseStrToDate(DateUtil.getDateStr(q.getStartDate(),"yyyy-MM-dd"),"yyyy-MM-dd"));
//                        soMain.setDdate(DateUtil.parseStrToDate(DateUtil.getDateStr(new Date(),"yyyy-MM-dd"),"yyyy-MM-dd"));

                        //region ???????????????
                        U8Common ucom=new U8Common();
                        ucom.setCardNumber("17");
                        ucom.setcWhCode("");
                        ucom.setVendor("");
                        U8Common rum= u8commonMapper.GetVoucherNumber(ucom);
                        String str = "", str1 = "", strRule = "";  //str1:??????
                        int len = 0;

                        //??????1----------------------------------------------------------
                        str = rum.getPrefix1();
                        len = rum.getPrefix1Len();
                        strRule = rum.getPrefix1Rule();
                        if (str.equals("?????????"))
                        {

                            U8Common rum1= u8commonMapper.GetVouchercontrapose(ucom);
                            str1=rum1.getcCode();
                        }
                        else
                        {
                            str1= str1+ ConnectString.getPrefixByType(str,len,strRule,"");
                        }



                        //-------------------------------------------------------------

                        //??????1----------------------------------------------------------
                        str = rum.getPrefix2();
                        len = rum.getPrefix2Len();
                        strRule = rum.getPrefix2Rule();

                        if (str.equals("?????????"))
                        {

                            U8Common rum1= u8commonMapper.GetVouchercontrapose(ucom);
                            str1=rum1.getcCode();
                        }
                        else
                        {
                            str1= str1+ ConnectString.getPrefixByType(str,len,strRule,"");
                        }
                        //-------------------------------------------------------------

                        //??????1----------------------------------------------------------
                        str = rum.getPrefix3();
                        len = rum.getPrefix3Len();
                        strRule = rum.getPrefix3Rule();

                        if (str.equals("?????????"))
                        {

                            U8Common rum1= u8commonMapper.GetVouchercontrapose(ucom);
                            str1=rum1.getcCode();
                        }
                        else
                        {
                            str1= str1+ ConnectString.getPrefixByType(str,len,strRule,"");
                        }
                        //-------------------------------------------------------------

                        str = rum.getiStartNumber().toString();  //????????????

                        U8Common rg=ConnectString.GetGlide(ucom.getCardNumber(),rum.getGlide(),rum.getGlideRule(),"");
                        U8Common rg1= u8commonMapper.GetGlide(rg);
                        int cNumber=rum.getiStartNumber();
                        if(rg1!=null)
                        {
                            cNumber = rg1.getcNumber()+1;
                            rg.setcNumber(cNumber);
                            rg.setCardNumber(ucom.getCardNumber());
                            u8commonMapper.UpdateGlide(rg);
                        }
                        else
                        {
                            if(rg.getGlide().equals("??????")||rg.getGlide().equals("????????????"))
                            {
                                rg.setcNumber(cNumber);
                                rg.setCardNumber(ucom.getCardNumber());
                            }
                            else if(rg.getGlide().equals("?????????"))
                            {
                                rg.setcCode("");
                                rg.setcNumber(cNumber);
                                rg.setGlideRule(null);
                                rg.setCardNumber(ucom.getCardNumber());
                            }
                            else
                            {
                                rg.setcCode(null);
                            }
                            u8commonMapper.insertGlide(rg);

                        }
                        String cnum =String.valueOf(cNumber) ;
                        while (cnum.length() < rum.getGlideLen())
                        {
                            cnum = "0" + cnum;
                        }
                        str1 = str1 + cnum ;
                        String RdCode= str1;
//            String RdCode=  clib.GetU8NewVoucherIDByCardNumber(connectionString,"0411","","",true);
                        if (RdCode != "")
                            soMain.setCsocode(RdCode);
                        else
                            throw new Exception("??????cCode?????????????????????");
                        //endregion
//                        soMain.setCsocode(q.getField0006());
                        soMain.setCcuscode(customer.getCcuscode());
                        String depCode="112";
                        String depName="?????????";

                        if(listPerson!=null&&listPerson.size()>0) {
                            Person person = personMapper.selectById(listPerson.get(0).getCpersoncode());
                            if (person != null) {
                                Department department = departmentMapper.selectById(person.getCdepcode());
                                if (department != null) {
                                    depCode = department.getCdepcode();
                                    depName = department.getCdepname();
                                }

                            }
                        }
                        soMain.setCdepcode(depCode);
                        soMain.setCdefine3(depName);
                        soMain.setCcusoaddress(customer.getCcusoaddress());
                        soMain.setCcusname(customer.getCcusname());
                        soMain.setDpremodatebt(q.getField0056());
                        soMain.setDpredatebt(q.getField0057());
                        soMain.setCinvoicecompany(customer.getCinvoicecompany());
                        String  sysbarcode = "||" + "SA17" + soMain.getCsocode();
                        soMain.setCmaker(q.getCreateUser()==null?"demo":q.getCreateUser());
                        soMain.setCsysbarcode(sysbarcode);
                        soMain.setDverifydate(DateUtil.parseStrToDate(DateUtil.getDateStr(new Date(),"yyyy-MM-dd"),"yyyy-MM-dd"));
                        soMain.setDverifysystime(new Date());
                        soMain.setCverifier(q.getCreateUser()==null?"demo":q.getCreateUser());
                        soMain.setCdefine1(q.getField0006());

                        soMainMapper.insert(soMain);
                    }
                    else
                    {
                        soMain=listSo.get(0);
                    }


                    //??????id??????
                    List<OaSaleContract> list= listSync.stream()
                            .filter(p ->p.getId().toString().equals(q.getId().toString()))
                            .collect(
                                    Collectors.collectingAndThen(
                                            Collectors.toCollection(() -> new TreeSet<>(
                                                    Comparator.comparing(o -> o.getField0103())
                                            )), ArrayList::new
                                    )
                            );


//                    //??????
//                    List<OaSaleContract> list = listSync.stream()
//                            .filter(p ->p.getId().toString().equals(q.getId().toString()))
//                            .collect(Collectors.toList());
//                    int row=1;

                    for(OaSaleContract detail:list)
                    {

                        //region ??????????????????  ??????????????????
                        //?????????+??????  =???????????????????????????
                        Inventory inventory=inventoryMapper.selectById(q.getField0006()+"-"+detail.getField0103());
                        BasPart basPart=new BasPart();
                        if(inventory==null)
                        {
                            inventory=new Inventory();
                            inventory.setDefaultValue();
                            inventory.setCinvcode(q.getField0006()+"-"+detail.getField0103());
                            inventory.setCinvname(detail.getField0008());
                            inventory.setCinvstd(detail.getField0009());
                            inventory.setCinvccode("50101");


                            inventory.setCinvdefine6(detail.getDeviceClass());
                            inventory.setCinvdefine5(detail.getDeviceType());

                            LambdaQueryWrapper<ComputationUnit> select=new LambdaQueryWrapper<>();
                            select.eq(ComputationUnit::getCcomunitname, detail.getField0123());
                            select.apply(" cGroupCode  in (select cGroupCode from  ComputationGroup where iGroupType=0) ");
                            List<ComputationUnit> listUnit=unitMapper.selectList(select);
                            //??????????????????????????????????????????
                            if(listUnit!=null&&listUnit.size()>0)
                            {
                                inventory.setCcomunitcode(listUnit.get(0).getCcomunitcode());
                                inventory.setCgroupcode(listUnit.get(0).getCgroupcode());
                                inventory.setCshopunit(inventory.getCcomunitcode());
                                inventory.setIgrouptype(Byte.valueOf("0"));
                            }
                            else
                            {

                                LambdaQueryWrapper<ComputationUnit> selectUnit=new LambdaQueryWrapper<>();
                                selectUnit.eq(ComputationUnit::getCcomunitname, "???");
                                selectUnit.apply(" cGroupCode  in (select cGroupCode from  ComputationGroup where iGroupType=0) ");
                                List<ComputationUnit> listUnit1=unitMapper.selectList(selectUnit);
                                if(listUnit1!=null&&listUnit1.size()>0)
                                {
                                    inventory.setCcomunitcode(listUnit1.get(0).getCcomunitcode());
                                    inventory.setCgroupcode(listUnit1.get(0).getCgroupcode());
                                    inventory.setCshopunit(inventory.getCcomunitcode());
                                    inventory.setIgrouptype(Byte.valueOf("0"));
                                }
                                else
                                {
                                    throw new Exception("????????????????????????");
                                }

                            }


                            inventory.setBsale(true);
                            inventory.setBptomodel(true);
                            inventory.setBforeexpland(true);
                            inventory.setBinvmodel(true);
                            inventory.setBexpsale(false);
                            inventory.setBbommain(true);
                            inventory.setBbomsub(true);

                            //???????????????????????????
                            inventory.setBptomodel(false);
                            inventory.setBinvmodel(false);
                            inventory.setBself(true);
                            inventory.setBproducing(true);
                            inventory.setCdefwarehouse("03");
                            inventory.setCplanmethod("N");

                            //??????????????????
                            inventoryMapper.insert(inventory);

                            //??????????????????????????????
                            InventoryExtradefine inventoryExtradefine=new InventoryExtradefine();
                            inventoryExtradefine.setCinvcode(inventory.getCinvcode());
                            inventoryExtradefineMapper.insert(inventoryExtradefine);

                            //?????????????????????
                            InventorySub inventorySub=new InventorySub();
                            inventorySub.setDefaultValue();
                            inventorySub.setCinvsubcode(inventory.getCinvcode());
                            inventorySubMapper.insert(inventorySub);


                            basPart.setDefaultValue();
                            Integer u8fid = null;
                            u8fid=u8SystemUtils.getId(accId,"bas_part",10);
                            basPart.setPartid(u8fid);
                            basPart.setInvcode(inventory.getCinvcode());
                            basPart.setLlc(0);
                            basPart.setIsurenesstype(Short.valueOf("1"));
                            basPartMapper.insert(basPart);
                        }
                        else
                        {
                            //??????part
                            LambdaQueryWrapper<BasPart> select=new LambdaQueryWrapper<>();
                            select.eq(BasPart::getInvcode, inventory.getCinvcode());
                            List<BasPart> listPart=basPartMapper.selectList(select);
                            if(listPart!=null&&listPart.size()>0)
                            {
                                basPart=listPart.get(0);
                            }
                        }

                        //endregion




                        SoDetails soContractInv=new SoDetails();
                        LambdaQueryWrapper<SoDetails> selectSoDetail=new LambdaQueryWrapper<>();
                        selectSoDetail.eq(SoDetails::getId, soMain.getId());
                        selectSoDetail.eq(SoDetails::getCinvcode, q.getField0006()+"-"+detail.getField0103());
                        List<SoDetails> listSoDetail=soDetailsMapper.selectList(selectSoDetail);
                        BigDecimal itaxrate=BigDecimal.ZERO;
                        if(listSoDetail==null||listSoDetail.size()==0) {
                            Integer soContracyId=u8SystemUtils.getChildId(accId,"Somain",10,1);
                            soContractInv.setDefaultValue();
                            soContractInv.setId(soMain.getId());
                            soContractInv.setIsosid(soContracyId);
                            soContractInv.setCsocode(soMain.getCsocode());
                            soContractInv.setCinvcode(inventory.getCinvcode());
                            soContractInv.setCinvname(inventory.getCinvname());
//                            soContractInv.setDpredate(soMain.getDdate());
//                            soContractInv.setDpremodate(soMain.getDdate());
                            soContractInv.setIquantity(detail.getField0010());
                            soContractInv.setItaxunitprice(detail.getField0011()==null?BigDecimal.ZERO:detail.getField0011());
                            soContractInv.setIsum(detail.getField0012()==null?BigDecimal.ZERO:detail.getField0012());
                            itaxrate=BigDecimal.ONE.add((soContractInv.getItaxrate()).multiply(BigDecimal.valueOf(0.01)));
                            soContractInv.setIunitprice(soContractInv.getItaxunitprice().divide(itaxrate,2,BigDecimal.ROUND_HALF_UP));  //??????????????????
                            soContractInv.setImoney(soContractInv.getIsum().divide(itaxrate,2,BigDecimal.ROUND_HALF_UP));  //??????????????????
                            soContractInv.setItax(soContractInv.getIsum().subtract(soContractInv.getImoney()));

                            soContractInv.setInatunitprice(soContractInv.getIunitprice());
                            soContractInv.setInatmoney(soContractInv.getImoney());
                            soContractInv.setInattax(soContractInv.getItax());
                            soContractInv.setInatsum(soContractInv.getIsum());
                            soContractInv.setIrowno(Integer.valueOf(detail.getField0103()));
                            soContractInv.setBsaleprice(true);
                            String  csysbarcode = "||" + "SA17|" + soMain.getCsocode()+"|"+String.valueOf(soContractInv.getIrowno());
                            soContractInv.setCbsysbarcode(csysbarcode);
                            soContractInv.setCparentcode("{"+UUID.randomUUID().toString()+"}");
                            soContractInv.setDpremodate(detail.getField0056());
                            soContractInv.setDpredate(detail.getField0057());
                            soContractInv.setCdefine22(soMain.getCdefine1());

                            soContractInv.setCdefine32(detail.getDeviceName());
                            soContractInv.setCdefine33(detail.getDeviceStd());
                            soContractInv.setCmemo(detail.getRemark());
                            //??????OA??????id?????????????????????parent-
                            soContractInv.setCdefine30("parent-"+detail.getContrastId());
                            soDetailsMapper.insert(soContractInv);
//                            row++;



                            //?????????????????????oa
                            //??????U8?????????
                            OaSaleContractContrast oaSaleContractContrast=new OaSaleContractContrast();
                            oaSaleContractContrast.setId(detail.getContrastMainId());
                            oaSaleContractContrast.setField0033(soContractInv.getIsosid().toString());
                            oaSaleContractContrastMapper.updateSoCode(oaSaleContractContrast,ParamUtil.getParam("oaDatabase").toString());
                        }
                        else
                        {
                            soContractInv=listSoDetail.get(0);
                        }




                        //??????id??????
                        List<OaSaleContract> listMaterial= listSync.stream()
                                .filter(p ->p.getId().toString().equals(q.getId().toString())&&p.getField0103().equals(detail.getField0103()))
                                .collect(Collectors.toList());
                        List<OaSaleContractContrastDetail> listContrast=new ArrayList<>();
                        for (OaSaleContract mateerial:listMaterial)
                        {
                            OaSaleContractContrastDetail query=new OaSaleContractContrastDetail();
                            query.setId(mateerial.getContrastId());
                            List<OaSaleContractContrastDetail> listContrast1=oaSaleContractContrastDetailMapper.getList(query,ParamUtil.getParam("oaDatabase").toString());
                            if(listContrast1!=null)
                            {
                                listContrast.addAll(listContrast1);
                            }

                        }

                        //
                        if(listContrast==null||listContrast.size()==0)
                        {
                            throw new Exception("???????????????????????????");
                        }


                        for(OaSaleContractContrastDetail oaSaleContractContrastDetail:listContrast)
                        {
                            Integer sodId=u8SystemUtils.getChildId(accId,"Somain",10,1);
                            SoDetails soDetails=new SoDetails();
                            soDetails.setDefaultValue();
                            soDetails.setIsosid(sodId);
                            soDetails.setId(soMain.getId());
                            soDetails.setCsocode(soMain.getCsocode());
                            soDetails.setCinvcode(oaSaleContractContrastDetail.getField0018());
                            soDetails.setCinvname(oaSaleContractContrastDetail.getField0019());
                            soDetails.setDpredate(detail.getField0056());
                            soDetails.setDpremodate(detail.getField0057());
                            soDetails.setIquantity(oaSaleContractContrastDetail.getField0021());
                            soDetails.setItaxunitprice(BigDecimal.ZERO);
                            soDetails.setIsum(BigDecimal.ZERO);
                            soDetails.setIunitprice(BigDecimal.ZERO);  //??????????????????
                            soDetails.setImoney(BigDecimal.ZERO);  //??????????????????
                            soDetails.setItax(BigDecimal.ZERO);
                            soDetails.setInatunitprice(soDetails.getIunitprice());
                            soDetails.setInatmoney(soDetails.getImoney());
                            soDetails.setInattax(soDetails.getItax());
                            soDetails.setInatsum(soDetails.getIsum());


                            LambdaQueryWrapper<SoDetails> mCount=new LambdaQueryWrapper<>();
                            mCount.eq(SoDetails::getId, soMain.getId());
                            mCount.orderByDesc(SoDetails::getIrowno);
                            List<SoDetails> listCount=soDetailsMapper.selectList(mCount);
                            if(listCount!=null&&listCount.size()>0)
                            {
                                soDetails.setIrowno(listCount.get(0).getIrowno()+1);
                            }
                            else
                            {
                                soDetails.setIrowno(1);
                            }

                            soDetails.setBsaleprice(false);
                            String  csysbarcode = "||" + "SA17|" + soMain.getCsocode()+"|"+String.valueOf(soDetails.getIrowno());
                            soDetails.setCbsysbarcode(csysbarcode);
                            soDetails.setIppartid(basPart.getPartid());
                            soDetails.setIppartqty(soContractInv.getIquantity());
                            soDetails.setCchildcode(soContractInv.getCparentcode());


//                            BigDecimal qtySum = listContrast.stream().filter(p->  CustomStringUtils.isNotBlank(p.getField0021())).map(OaSaleContractContrastDetail::getField0021).reduce(BigDecimal.ZERO,BigDecimal::add);


                            //???????????????
                            Inventory inventoryChild=inventoryMapper.selectById(oaSaleContractContrastDetail.getField0018());
                            if(inventoryChild!=null)
                            {
                                Inventory invUpdate=new Inventory();
                                invUpdate.setCinvcode(inventoryChild.getCinvcode());
                                invUpdate.setCinvdefine5(inventoryChild.getCinvname());
                                inventoryMapper.updateById(invUpdate);
                            }


//                            //??????????????????????????????????????????
//                            LambdaQueryWrapper<SoDetails> mRate=new LambdaQueryWrapper<>();
//                            mRate.eq(SoDetails::getCchildcode, soContractInv.getCparentcode());
//                            mRate.eq(SoDetails::getId, soContractInv.getId());
//                            List<SoDetails> listRate=soDetailsMapper.selectList(mRate);
//                            if(listRate!=null&&listRate.size()>0)
//                            {
//                                BigDecimal qtySum1=  listRate.stream().filter(p->  CustomStringUtils.isNotBlank(p.getIquantity())).map(SoDetails::getIquantity).reduce(BigDecimal.ZERO,BigDecimal::add);
//                                qtySum=qtySum.add(qtySum1);
//                                //????????????
//                                for(SoDetails soDetails1:listRate)
//                                {
//                                    SoDetails updateD=new SoDetails();
//                                    updateD.setAutoid(soDetails1.getAutoid());
//                                    updateD.setFchildrate(soDetails1.getIquantity().divide(qtySum,7,BigDecimal.ROUND_HALF_UP));
//                                    soDetailsMapper.updateById(updateD);
//                                }
//                            }


                            soDetails.setFchildqty(soDetails.getIquantity());
//                            soDetails.setFchildrate(soDetails.getIquantity().divide(qtySum,7,BigDecimal.ROUND_HALF_UP));
                            soDetails.setCdefine22(soMain.getCdefine1());

                            soDetails.setDpremodate(detail.getField0056());
                            soDetails.setDpredate(detail.getField0057());
                            soDetails.setCmemo(soContractInv.getCmemo());
                            //??????OA??????id?????????????????????child-
                            soDetails.setCdefine30("child-"+oaSaleContractContrastDetail.getId());
                            soDetailsMapper.insert(soDetails);


                            //????????????
                            soDetailsMapper.updateRate(soDetails);



                            //?????????????????????oa
                            //??????U8?????????
                            OaSaleContractContrastDetail oaSaleContractContrast=new OaSaleContractContrastDetail();
                            oaSaleContractContrast.setId(oaSaleContractContrastDetail.getId());
                            oaSaleContractContrast.setField0046(soDetails.getIsosid().toString());
                            oaSaleContractContrastDetailMapper.updateSoCode(oaSaleContractContrast,ParamUtil.getParam("oaDatabase").toString());




                            //?????????????????????
                            OrderProductionSchedule mOrder=orderProductionScheduleMapper.selectByPrimaryKey(oaSaleContractContrastDetail.getId().toString(),ParamUtil.getParam("localDatabase").toString());
                            if(mOrder==null)
                            {
                                OrderProductionSchedule orderProductionSchedule=new OrderProductionSchedule();
//                            orderProductionSchedule.setId(String.valueOf(SnowFlakeUtils.getFlowIdInstance().nextId()));
                                orderProductionSchedule.setId(oaSaleContractContrastDetail.getId().toString());
                                orderProductionSchedule.setSoDetailId(soDetails.getIsosid());
                                orderProductionSchedule.setCustName(soMain.getCcusname());
                                orderProductionSchedule.setContractCode(soMain.getCdefine1());
                                orderProductionSchedule.setProductInvCode(soContractInv.getCinvcode());
                                orderProductionSchedule.setProductInvName(inventory.getCinvname());
                                orderProductionSchedule.setProductInvStd(inventory.getCinvstd());
                                orderProductionSchedule.setQty(soContractInv.getIquantity());
                                orderProductionSchedule.setOrderDate(soMain.getDdate());
                                orderProductionSchedule.setCreateDate(new Date());
                                orderProductionSchedule.setCreateUser("demo");
                                orderProductionSchedule.setIzDelete(false);
                                orderProductionSchedule.setPmcType("????????????");
                                orderProductionSchedule.setIprice(soContractInv.getItaxunitprice());
                                orderProductionSchedule.setIamount(soContractInv.getIsum());
                                orderProductionSchedule.setDeliveryDate(soDetails.getDpredate());

                                orderProductionSchedule.setInvCode(soDetails.getCinvcode());
                                orderProductionSchedule.setInvName(inventoryChild.getCinvname());
                                orderProductionSchedule.setInvStd(inventoryChild.getCinvstd());
                                orderProductionSchedule.setTqty(soDetails.getIquantity());
                                orderProductionSchedule.setPerson(oaSaleContractContrastDetail.getField0063());
                                orderProductionScheduleMapper.insertSelective(orderProductionSchedule,ParamUtil.getParam("localDatabase").toString());

                            }

                        }

                        row++;

                    }



                }
            }


        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }




    /**
     * ??????????????????
     * @return
     */
//    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult syncSaleContractEdit() {
        ResponseResult result = new ResponseResult();
        int row=1;
        try{

            //???????????????????????? ??????
            List<OaSaleContract> listSync=oaSaleContractMapper.getListBySyncEdit(new OaSaleContract(),ParamUtil.getParam("oaDatabase").toString());
            if(listSync!=null) {
                //???????????????????????????
                for (OaSaleContract detail : listSync) {
                    OaSaleContractContrastDetail query = new OaSaleContractContrastDetail();
                    query.setId(detail.getContrastId());
                    List<OaSaleContractContrastDetail> listContrast = oaSaleContractContrastDetailMapper.getList(query, ParamUtil.getParam("oaDatabase").toString());
                    //?????????????????????????????????
                    for (OaSaleContractContrastDetail oaSaleContractContrastDetail : listContrast) {

                        //??????????????????
                        LambdaQueryWrapper<SoMain> selectSo=new LambdaQueryWrapper<>();
                        selectSo.eq(SoMain::getCdefine1, detail.getField0006());
                        List<SoMain> listSo=soMainMapper.selectList(selectSo);
                        if(listSo!=null&&listSo.size()>0)
                        {
                            SoMain soMain=listSo.get(0);
                            //???????????????
                            LambdaQueryWrapper<SoDetails> selectSoDetail=new LambdaQueryWrapper<>();
                            selectSoDetail.eq(SoDetails::getId, soMain.getId());
                            selectSoDetail.eq(SoDetails::getCinvcode, detail.getField0006()+"-"+detail.getField0103());
                            List<SoDetails> listSoDetail=soDetailsMapper.selectList(selectSoDetail);
                            if(listSoDetail!=null&&listSoDetail.size()>0) {
                                SoDetails soContractInv=listSoDetail.get(0);

                                BasPart basPart=null;
                                LambdaQueryWrapper<BasPart> select=new LambdaQueryWrapper<>();
                                select.eq(BasPart::getInvcode, soContractInv.getCinvcode());
                                List<BasPart> listPart=basPartMapper.selectList(select);
                                if(listPart!=null&&listPart.size()>0)
                                {
                                    basPart=listPart.get(0);


                                    LambdaQueryWrapper<SoDetails> selectDetail=new LambdaQueryWrapper<>();
                                    selectDetail.eq(SoDetails::getId, soMain.getId());
                                    selectDetail.apply(" cdefine30!='' ");
                                    selectDetail.like(SoDetails::getCdefine30, oaSaleContractContrastDetail.getId());
                                    List<SoDetails> listDetail=soDetailsMapper.selectList(selectDetail);
                                    //??????
                                    if(listDetail==null||listDetail.size()==0) {

                                        Integer sodId=u8SystemUtils.getChildId(accId,"Somain",10,1);
                                        SoDetails soDetails=new SoDetails();
                                        soDetails.setDefaultValue();
                                        soDetails.setIsosid(sodId);
                                        soDetails.setId(soMain.getId());
                                        soDetails.setCsocode(soMain.getCsocode());
                                        soDetails.setCinvcode(oaSaleContractContrastDetail.getField0018());
                                        soDetails.setCinvname(oaSaleContractContrastDetail.getField0019());
                                        soDetails.setDpredate(soMain.getDpredatebt());
                                        soDetails.setDpremodate(soMain.getDpremodatebt());
                                        soDetails.setIquantity(oaSaleContractContrastDetail.getField0021());
                                        soDetails.setItaxunitprice(BigDecimal.ZERO);
                                        soDetails.setIsum(BigDecimal.ZERO);
                                        soDetails.setIunitprice(BigDecimal.ZERO);  //??????????????????
                                        soDetails.setImoney(BigDecimal.ZERO);  //??????????????????
                                        soDetails.setItax(BigDecimal.ZERO);
                                        soDetails.setInatunitprice(soDetails.getIunitprice());
                                        soDetails.setInatmoney(soDetails.getImoney());
                                        soDetails.setInattax(soDetails.getItax());
                                        soDetails.setInatsum(soDetails.getIsum());


                                        LambdaQueryWrapper<SoDetails> mCount=new LambdaQueryWrapper<>();
                                        mCount.eq(SoDetails::getId, soMain.getId());
                                        mCount.orderByDesc(SoDetails::getIrowno);
                                        List<SoDetails> listCount=soDetailsMapper.selectList(mCount);
                                        if(listCount!=null&&listCount.size()>0)
                                        {
                                            soDetails.setIrowno(listCount.get(0).getIrowno()+1);
                                        }
                                        else
                                        {
                                            soDetails.setIrowno(1);
                                        }

                                        soDetails.setBsaleprice(false);
                                        String  csysbarcode = "||" + "SA17|" + soMain.getCsocode()+"|"+String.valueOf(soDetails.getIrowno());
                                        soDetails.setCbsysbarcode(csysbarcode);
                                        soDetails.setIppartid(basPart.getPartid());
                                        soDetails.setIppartqty(soContractInv.getIquantity());
                                        soDetails.setCchildcode(soContractInv.getCparentcode());




                                        //???????????????
                                        Inventory inventoryChild=inventoryMapper.selectById(oaSaleContractContrastDetail.getField0018());
                                        if(inventoryChild!=null)
                                        {
                                            Inventory invUpdate=new Inventory();
                                            invUpdate.setCinvcode(inventoryChild.getCinvcode());
                                            invUpdate.setCinvdefine5(inventoryChild.getCinvname());
                                            inventoryMapper.updateById(invUpdate);
                                        }

                                        soDetails.setFchildqty(soDetails.getIquantity());
                                        soDetails.setCdefine22(soMain.getCdefine1());

                                        soDetails.setDpremodate(detail.getField0056());
                                        soDetails.setDpredate(detail.getField0057());
                                        soDetails.setCmemo(soContractInv.getCmemo());
                                        //??????OA??????id?????????????????????child-
                                        soDetails.setCdefine30("child-"+oaSaleContractContrastDetail.getId());
                                        soDetailsMapper.insert(soDetails);

                                        //????????????
                                        soDetailsMapper.updateRate(soDetails);

                                        //????????????????????????
                                        LambdaQueryWrapper<SoDetails> selectDetailMain=new LambdaQueryWrapper<>();
                                        selectDetailMain.eq(SoDetails::getCparentcode, soDetails.getCchildcode());
                                        List<SoDetails> listDetailMain=soDetailsMapper.selectList(selectDetailMain);
                                        if(listDetailMain!=null&&listDetailMain.size()>0)
                                        {
                                            SoDetails soDetails1=listDetailMain.get(0);
                                            soDetails1.setDpremodate(detail.getField0056());
                                            soDetails1.setDpredate(detail.getField0057());
                                            soDetailsMapper.updateById(soDetails1);
                                        }

                                        //?????????????????????oa
                                        //??????U8?????????
                                        OaSaleContractContrastDetail oaSaleContractContrast=new OaSaleContractContrastDetail();
                                        oaSaleContractContrast.setId(oaSaleContractContrastDetail.getId());
                                        oaSaleContractContrast.setField0046(soDetails.getIsosid().toString());
                                        oaSaleContractContrastDetailMapper.updateSoCode(oaSaleContractContrast,ParamUtil.getParam("oaDatabase").toString());




                                        //?????????????????????
                                        OrderProductionSchedule mOrder=orderProductionScheduleMapper.selectByPrimaryKey(oaSaleContractContrastDetail.getId().toString(),ParamUtil.getParam("localDatabase").toString());
                                        if(mOrder==null)
                                        {
                                            //?????????????????????
                                            OrderProductionSchedule orderProductionSchedule=new OrderProductionSchedule();
                                            orderProductionSchedule.setId(oaSaleContractContrastDetail.getId().toString());
//                                        orderProductionSchedule.setId(String.valueOf(SnowFlakeUtils.getFlowIdInstance().nextId()));
                                            orderProductionSchedule.setSoDetailId(soDetails.getIsosid());
                                            orderProductionSchedule.setCustName(soMain.getCcusname());
                                            orderProductionSchedule.setContractCode(soMain.getCdefine1());
                                            orderProductionSchedule.setProductInvCode(soContractInv.getCinvcode());
                                            Inventory inventoryParent=inventoryMapper.selectById(soContractInv.getCinvcode());
                                            orderProductionSchedule.setProductInvName(inventoryParent.getCinvname());
                                            orderProductionSchedule.setProductInvStd(inventoryParent.getCinvstd());
                                            orderProductionSchedule.setQty(soContractInv.getIquantity());
                                            orderProductionSchedule.setOrderDate(soMain.getDdate());
                                            orderProductionSchedule.setCreateDate(new Date());
                                            orderProductionSchedule.setCreateUser("demo");
                                            orderProductionSchedule.setIzDelete(false);
                                            orderProductionSchedule.setPmcType("????????????");
                                            orderProductionSchedule.setIprice(soContractInv.getItaxunitprice());
                                            orderProductionSchedule.setIamount(soContractInv.getIsum());
                                            orderProductionSchedule.setDeliveryDate(soDetails.getDpredate());

                                            orderProductionSchedule.setInvCode(soDetails.getCinvcode());
                                            orderProductionSchedule.setInvName(inventoryChild.getCinvname());
                                            orderProductionSchedule.setInvStd(inventoryChild.getCinvstd());
                                            orderProductionSchedule.setTqty(soDetails.getIquantity());
                                            orderProductionSchedule.setPerson(oaSaleContractContrastDetail.getField0063());
                                            orderProductionScheduleMapper.insertSelective(orderProductionSchedule,ParamUtil.getParam("localDatabase").toString());

                                        }


                                    }
                                    //??????
                                    else
                                    {

                                        SoDetails soDetails=listDetail.get(0);
                                        soDetails.setCinvcode(oaSaleContractContrastDetail.getField0018());
                                        soDetails.setCinvname(oaSaleContractContrastDetail.getField0019());
                                        soDetails.setIquantity(oaSaleContractContrastDetail.getField0021());
                                        soDetails.setDpremodate(detail.getField0056());
                                        soDetails.setDpredate(detail.getField0057());
                                        //???????????????
                                        Inventory inventoryChild=inventoryMapper.selectById(oaSaleContractContrastDetail.getField0018());
                                        if(inventoryChild!=null)
                                        {
                                            Inventory invUpdate=new Inventory();
                                            invUpdate.setCinvcode(inventoryChild.getCinvcode());
                                            invUpdate.setCinvdefine5(inventoryChild.getCinvname());
                                            inventoryMapper.updateById(invUpdate);
                                        }

                                        soDetails.setFchildqty(soDetails.getIquantity());
                                        soDetailsMapper.updateById(soDetails);

                                        //????????????????????????
                                        LambdaQueryWrapper<SoDetails> selectDetailMain=new LambdaQueryWrapper<>();
                                        selectDetailMain.eq(SoDetails::getCparentcode, soDetails.getCchildcode());
                                        List<SoDetails> listDetailMain=soDetailsMapper.selectList(selectDetailMain);
                                        if(listDetailMain!=null&&listDetailMain.size()>0)
                                        {
                                            SoDetails soDetails1=listDetailMain.get(0);
                                            soDetails1.setDpremodate(detail.getField0056());
                                            soDetails1.setDpredate(detail.getField0057());
                                            soDetailsMapper.updateById(soDetails1);
                                        }


                                        //????????????
                                        soDetailsMapper.updateRate(soDetails);

                                        //?????????????????????oa
                                        //??????U8?????????
                                        OaSaleContractContrastDetail oaSaleContractContrast=new OaSaleContractContrastDetail();
                                        oaSaleContractContrast.setId(oaSaleContractContrastDetail.getId());
                                        oaSaleContractContrast.setField0046(soDetails.getIsosid().toString());
                                        oaSaleContractContrastDetailMapper.updateSoCode(oaSaleContractContrast,ParamUtil.getParam("oaDatabase").toString());



                                        OrderProductionSchedule mOrder=orderProductionScheduleMapper.selectByPrimaryKey(oaSaleContractContrastDetail.getId().toString(),ParamUtil.getParam("localDatabase").toString());
                                        if(mOrder==null)
                                        {
//                                            //?????????????????????
//                                            OrderProductionSchedule orderProductionSchedule=new OrderProductionSchedule();
//                                            orderProductionSchedule.setId(oaSaleContractContrastDetail.getId().toString());
////                                        orderProductionSchedule.setId(String.valueOf(SnowFlakeUtils.getFlowIdInstance().nextId()));
//                                            orderProductionSchedule.setSoDetailId(soDetails.getIsosid());
//                                            orderProductionSchedule.setCustName(soMain.getCcusname());
//                                            orderProductionSchedule.setContractCode(soMain.getCdefine1());
//                                            orderProductionSchedule.setProductInvCode(soContractInv.getCinvcode());
//                                            Inventory inventoryParent=inventoryMapper.selectById(soContractInv.getCinvcode());
//                                            orderProductionSchedule.setProductInvName(inventoryParent.getCinvname());
//                                            orderProductionSchedule.setProductInvStd(inventoryParent.getCinvstd());
//                                            orderProductionSchedule.setQty(soContractInv.getIquantity());
//                                            orderProductionSchedule.setOrderDate(soMain.getDdate());
//                                            orderProductionSchedule.setCreateDate(new Date());
//                                            orderProductionSchedule.setCreateUser("demo");
//                                            orderProductionSchedule.setIzDelete(false);
//                                            orderProductionSchedule.setPmcType("????????????");
//                                            orderProductionSchedule.setIprice(soContractInv.getItaxunitprice());
//                                            orderProductionSchedule.setIamount(soContractInv.getIsum());
//                                            orderProductionSchedule.setDeliveryDate(soDetails.getDpredate());
//
//                                            orderProductionSchedule.setInvCode(soDetails.getCinvcode());
//                                            orderProductionSchedule.setInvName(inventoryChild.getCinvname());
//                                            orderProductionSchedule.setInvStd(inventoryChild.getCinvstd());
//                                            orderProductionSchedule.setTqty(soDetails.getIquantity());
//                                            orderProductionSchedule.setPerson(oaSaleContractContrastDetail.getField0063());
//                                            orderProductionScheduleMapper.insertSelective(orderProductionSchedule,ParamUtil.getParam("localDatabase").toString());

                                        }
                                        else{
                                            //??????????????????????????????
                                            OrderProductionSchedule orderProductionSchedule=new OrderProductionSchedule();
                                            orderProductionSchedule.setId(oaSaleContractContrastDetail.getId().toString());
//                                        orderProductionSchedule.setId(String.valueOf(SnowFlakeUtils.getFlowIdInstance().nextId()));
                                            orderProductionSchedule.setSoDetailId(soDetails.getIsosid());
                                            orderProductionSchedule.setCustName(soMain.getCcusname());
                                            orderProductionSchedule.setContractCode(soMain.getCdefine1());
                                            orderProductionSchedule.setProductInvCode(soContractInv.getCinvcode());
                                            Inventory inventoryParent=inventoryMapper.selectById(soContractInv.getCinvcode());
                                            orderProductionSchedule.setProductInvName(inventoryParent.getCinvname());
                                            orderProductionSchedule.setProductInvStd(inventoryParent.getCinvstd());
                                            orderProductionSchedule.setQty(soContractInv.getIquantity());
                                            orderProductionSchedule.setOrderDate(soMain.getDdate());
                                            orderProductionSchedule.setUpdateDate(new Date());
                                            orderProductionSchedule.setUpdateUser("demo");
//                                        orderProductionSchedule.setPmcType("????????????");
                                            orderProductionSchedule.setIprice(soContractInv.getItaxunitprice());
                                            orderProductionSchedule.setIamount(soContractInv.getIsum());
                                            orderProductionSchedule.setDeliveryDate(soDetails.getDpredate());

                                            orderProductionSchedule.setInvCode(soDetails.getCinvcode());
                                            orderProductionSchedule.setInvName(inventoryChild.getCinvname());
                                            orderProductionSchedule.setInvStd(inventoryChild.getCinvstd());
                                            orderProductionSchedule.setTqty(soDetails.getIquantity());
                                            orderProductionSchedule.setPerson(oaSaleContractContrastDetail.getField0063());
                                            orderProductionScheduleMapper.updateByPrimaryKeySelective(orderProductionSchedule,ParamUtil.getParam("localDatabase").toString());


                                        }

                                    }

                                }

                            }
                        }

                    }


                }

            }


        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }






    /**
     * ?????????????????? ???????????????
     * @return
     */
//    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult syncSaleContractDelete() {
        ResponseResult result = new ResponseResult();
        int row=1;
        try{
            //????????????????????????
            LambdaQueryWrapper<SoDetails> selectSoDetail=new LambdaQueryWrapper<>();
            selectSoDetail.apply(" and  cdefine30!='' and cdefine30 like 'child-%' and REPLACE(cDefine30,'child-','')  not in (select cast(id as varchar(36)) from  "+ParamUtil.getParam("oaDatabase").toString()+".dbo.formson_0111 where  field0018 !='')");
            List<SoDetails> listSoDetail=soDetailsMapper.selectList(selectSoDetail);
            if(listSoDetail!=null&&listSoDetail.size()>0) {
                for(SoDetails detail:listSoDetail)
                {

                    //????????????????????????
                    LambdaQueryWrapper<SoDetails> select=new LambdaQueryWrapper<>();
                    select.eq(SoDetails::getAutoid,detail.getAutoid());
                    select.apply("  iSOsID  not in (select iSOsID from  DispatchLists where iSOsID is not null) and  isosid not in (select SoDId from  mps_netdemand where SoDId is not null) ");
                    List<SoDetails> listExist=soDetailsMapper.selectList(select);
                    //???????????????,??????????????????
                    if(listExist!=null&&listExist.size()>0)
                    {

                        //??????
                        soDetailsMapper.deleteById(detail.getAutoid());
                        //????????????
                        soDetailsMapper.updateRate(detail);
                    }
                    //??????????????????????????????
                    else
                    {
                        //??????
                        detail.setCscloser("demo");
                        detail.setDbclosedate(new Date());
                        detail.setDbclosesystime(new Date());
                        detail.setCdefine30(detail.getCdefine30().replace("child-","c-"));
                        soDetailsMapper.updateById(detail);
                        //????????????
                        soDetailsMapper.updateRate(detail);
                    }

                    //?????????????????????
                    orderProductionScheduleMapper.udateBySodId(detail.getIsosid(),ParamUtil.getParam("localDatabase").toString());
                }

            }

        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }






    /**
     * ??????????????????  ????????????
     * @return
     */
//    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult syncSaleTrialContract() {
        ResponseResult result = new ResponseResult();
        int row=1;
        try{

            //????????????????????????
        List<OaSaleContract> listSync=oaSaleContractMapper.getListTrialContractBySync(new OaSaleContract(),ParamUtil.getParam("oaDatabase").toString());
            if(listSync!=null)
            {
                //???????????????????????????
                for(OaSaleContract q:listSync)
                {

                    if(CustomStringUtils.isBlank(q.getCustName()))
                    {
                        continue;
                    }

                    LambdaQueryWrapper<Customer> selectCust=new LambdaQueryWrapper<>();
                    selectCust.eq(Customer::getCcusname, q.getCustName());
                    List<Customer> listCust=customerMapper.selectList(selectCust);
                    if(listCust==null||listCust.size()==0)
                    {
                        continue;
                    }
                    Customer customer=listCust.get(0);

                    //?????????????????????????????????????????????????????????
                    LambdaQueryWrapper<SoMain> selectSo=new LambdaQueryWrapper<>();
                    selectSo.eq(SoMain::getCsocode, q.getTrialCode());
                    List<SoMain> listSo=soMainMapper.selectList(selectSo);
                    SoMain soMain=new SoMain();
                    if(listSo==null||listSo.size()==0)
                    {
                        //????????????????????????
                        Integer soId=u8SystemUtils.getFatherId(accId,"Somain",10);

                        soMain.setDefaultValue();
                        soMain.setId(soId);
                        //??????????????????03
                        soMain.setCstcode("03");
                        LambdaQueryWrapper<Person> selectPerson=new LambdaQueryWrapper<>();
                        selectPerson.eq(Person::getCpersonname, q.getPersonName());
                        List<Person> listPerson=personMapper.selectList(selectPerson);
                        if(listPerson!=null&&listPerson.size()>0)
                        {
                            soMain.setCpersoncode(listPerson.get(0).getCpersoncode());
                        }


                        soMain.setDdate(DateUtil.parseStrToDate(DateUtil.getDateStr(q.getStartDate(),"yyyy-MM-dd"),"yyyy-MM-dd"));
//                        soMain.setDdate(DateUtil.parseStrToDate(DateUtil.getDateStr(new Date(),"yyyy-MM-dd"),"yyyy-MM-dd"));


                        soMain.setCsocode(q.getTrialCode());
                        soMain.setCcuscode(customer.getCcuscode());
                        String depCode="112";
                        String depName="?????????";

                        if(listPerson!=null&&listPerson.size()>0) {
                            Person person = personMapper.selectById(listPerson.get(0).getCpersoncode());
                            if (person != null) {
                                Department department = departmentMapper.selectById(person.getCdepcode());
                                if (department != null) {
                                    depCode = department.getCdepcode();
                                    depName = department.getCdepname();
                                }

                            }
                        }
                        soMain.setCdepcode(depCode);
                        soMain.setCdefine3(depName);
                        soMain.setCcusoaddress(customer.getCcusoaddress());
                        soMain.setCcusname(customer.getCcusname());
                        soMain.setDpremodatebt(q.getField0056());
                        soMain.setDpredatebt(q.getField0057());
                        soMain.setCinvoicecompany(customer.getCinvoicecompany());
                        String  sysbarcode = "||" + "SA17" + soMain.getCsocode();
                        soMain.setCmaker(q.getCreateUser()==null?"demo":q.getCreateUser());
                        soMain.setCsysbarcode(sysbarcode);
                        soMain.setDverifydate(DateUtil.parseStrToDate(DateUtil.getDateStr(new Date(),"yyyy-MM-dd"),"yyyy-MM-dd"));
                        soMain.setDverifysystime(new Date());
                        soMain.setCverifier(q.getCreateUser()==null?"demo":q.getCreateUser());
                        soMain.setCdefine1(q.getContractCode());

                        soMainMapper.insert(soMain);
                    }
                    else
                    {
                        soMain=listSo.get(0);
                    }



                    Integer sodId=u8SystemUtils.getChildId(accId,"Somain",10,1);
                    SoDetails soDetails=new SoDetails();
                    soDetails.setDefaultValue();
                    soDetails.setIsosid(sodId);
                    soDetails.setId(soMain.getId());
                    soDetails.setCsocode(soMain.getCsocode());
                    soDetails.setCinvcode(q.getInvCode());
                    Inventory inventory=inventoryMapper.selectById(soDetails.getCinvcode());

                    soDetails.setCinvname(inventory.getCinvname());
                    soDetails.setIquantity(q.getQty());
                    soDetails.setItaxunitprice(BigDecimal.ZERO);
                    soDetails.setIsum(BigDecimal.ZERO);
                    soDetails.setIunitprice(BigDecimal.ZERO);  //??????????????????
                    soDetails.setImoney(BigDecimal.ZERO);  //??????????????????
                    soDetails.setItax(BigDecimal.ZERO);
                    soDetails.setInatunitprice(soDetails.getIunitprice());
                    soDetails.setInatmoney(soDetails.getImoney());
                    soDetails.setInattax(soDetails.getItax());
                    soDetails.setInatsum(soDetails.getIsum());


                    LambdaQueryWrapper<SoDetails> mCount=new LambdaQueryWrapper<>();
                    mCount.eq(SoDetails::getId, soMain.getId());
                    mCount.orderByDesc(SoDetails::getIrowno);
                    List<SoDetails> listCount=soDetailsMapper.selectList(mCount);
                    if(listCount!=null&&listCount.size()>0)
                    {
                        soDetails.setIrowno(listCount.get(0).getIrowno()+1);
                    }
                    else
                    {
                        soDetails.setIrowno(1);
                    }

                    soDetails.setBsaleprice(false);
                    String  csysbarcode = "||" + "SA17|" + soMain.getCsocode()+"|"+String.valueOf(soDetails.getIrowno());
                    soDetails.setCbsysbarcode(csysbarcode);
                    soDetails.setCdefine22(soMain.getCdefine1());

                    soDetails.setDpremodate(q.getField0056());
                    soDetails.setDpredate(q.getField0057());
                    soDetails.setCmemo(q.getRemark());
                    //??????OA??????id?????????????????????other-
                    soDetails.setCdefine30("other-"+q.getContrastId());
                    soDetailsMapper.insert(soDetails);


                    //?????????????????????oa
                    //??????U8?????????
                    OaSaleContractContrastDetail oaSaleContractContrast=new OaSaleContractContrastDetail();
                    oaSaleContractContrast.setId(q.getContrastId());
                    oaSaleContractContrast.setField0046(soDetails.getIsosid().toString());
                    oaSaleContractContrastDetailMapper.updateSoCode(oaSaleContractContrast,ParamUtil.getParam("oaDatabase").toString());

                    row++;
                }
            }


        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }




    /**
     * ??????????????????  ????????????   ??????
     * @return
     */
//    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult syncSaleTrialContractEdit() {
        ResponseResult result = new ResponseResult();
        int row=1;
        try{

            //??????????????????  ??????
            List<OaSaleContract> listSync=oaSaleContractMapper.getListTrialContractBySyncEdit(new OaSaleContract(),ParamUtil.getParam("oaDatabase").toString());
            if(listSync!=null) {

                //???????????????????????????
                for (OaSaleContract detail : listSync) {

                    //??????????????????
                    LambdaQueryWrapper<SoMain> selectSo=new LambdaQueryWrapper<>();
                    selectSo.eq(SoMain::getCsocode, detail.getTrialCode());
                    List<SoMain> listSo=soMainMapper.selectList(selectSo);
                    if(listSo!=null&&listSo.size()>0)
                    {
                        SoMain soMain=listSo.get(0);
                        //???????????????
                        LambdaQueryWrapper<SoDetails> selectDetail=new LambdaQueryWrapper<>();
                        selectDetail.eq(SoDetails::getId, soMain.getId());
                        selectDetail.eq(SoDetails::getCinvcode, detail.getInvCode());
                        List<SoDetails> listDetail=soDetailsMapper.selectList(selectDetail);
                        //??????
                        if(listDetail==null||listDetail.size()==0) {

                            Integer sodId=u8SystemUtils.getChildId(accId,"Somain",10,1);
                            SoDetails soDetails=new SoDetails();
                            soDetails.setDefaultValue();
                            soDetails.setIsosid(sodId);
                            soDetails.setId(soMain.getId());
                            soDetails.setCsocode(soMain.getCsocode());
                            soDetails.setCinvcode(detail.getInvCode());

                            Inventory inventory=inventoryMapper.selectById(detail.getInvCode());

                            soDetails.setCinvname(inventory.getCinvname());
                            soDetails.setIquantity(detail.getQty());
                            soDetails.setItaxunitprice(BigDecimal.ZERO);
                            soDetails.setIsum(BigDecimal.ZERO);
                            soDetails.setIunitprice(BigDecimal.ZERO);  //??????????????????
                            soDetails.setImoney(BigDecimal.ZERO);  //??????????????????
                            soDetails.setItax(BigDecimal.ZERO);
                            soDetails.setInatunitprice(soDetails.getIunitprice());
                            soDetails.setInatmoney(soDetails.getImoney());
                            soDetails.setInattax(soDetails.getItax());
                            soDetails.setInatsum(soDetails.getIsum());


                            LambdaQueryWrapper<SoDetails> mCount=new LambdaQueryWrapper<>();
                            mCount.eq(SoDetails::getId, soMain.getId());
                            mCount.orderByDesc(SoDetails::getIrowno);
                            List<SoDetails> listCount=soDetailsMapper.selectList(mCount);
                            if(listCount!=null&&listCount.size()>0)
                            {
                                soDetails.setIrowno(listCount.get(0).getIrowno()+1);
                            }
                            else
                            {
                                soDetails.setIrowno(1);
                            }

                            soDetails.setBsaleprice(false);
                            String  csysbarcode = "||" + "SA17|" + soMain.getCsocode()+"|"+String.valueOf(soDetails.getIrowno());
                            soDetails.setCbsysbarcode(csysbarcode);


                            soDetails.setCdefine22(soMain.getCdefine1());
                            soDetails.setDpremodate(detail.getField0056());
                            soDetails.setDpredate(detail.getField0057());
                            soDetails.setCmemo(detail.getRemark());
                            //??????OA??????id?????????????????????other-
                            soDetails.setCdefine30("other-"+detail.getContrastId());
                            soDetailsMapper.insert(soDetails);


                            //?????????????????????oa
                            //??????U8?????????
                            OaSaleContractContrastDetail oaSaleContractContrast=new OaSaleContractContrastDetail();
                            oaSaleContractContrast.setId(detail.getContrastId());
                            oaSaleContractContrast.setField0046(soDetails.getIsosid().toString());
                            oaSaleContractContrastDetailMapper.updateSoCode(oaSaleContractContrast,ParamUtil.getParam("oaDatabase").toString());



                        }
                        //??????
                        else
                        {

                            SoDetails soDetails=listDetail.get(0);

                            Inventory inventory=inventoryMapper.selectById(detail.getInvCode());

                            soDetails.setCinvcode(detail.getInvCode());
                            soDetails.setCinvname(inventory.getCinvname());
                            soDetails.setIquantity(detail.getQty());
                            soDetails.setDpremodate(detail.getField0056());
                            soDetails.setDpredate(detail.getField0057());

                            soDetailsMapper.updateById(soDetails);


                            //?????????????????????oa
                            //??????U8?????????
                            OaSaleContractContrastDetail oaSaleContractContrast=new OaSaleContractContrastDetail();
                            oaSaleContractContrast.setId(detail.getContrastId());
                            oaSaleContractContrast.setField0046(soDetails.getIsosid().toString());
                            oaSaleContractContrastDetailMapper.updateSoCode(oaSaleContractContrast,ParamUtil.getParam("oaDatabase").toString());

                        }

                    }




                }

            }

        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }





    /**
     * ??????????????????  ???????????? ???????????????
     * @return
     */
//    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult syncSaleTrialContractDelete() {
        ResponseResult result = new ResponseResult();
        int row=1;
        try{
            //????????????????????????
            LambdaQueryWrapper<SoDetails> selectSoDetail=new LambdaQueryWrapper<>();
            selectSoDetail.apply(" and  cdefine30!='' and cdefine30 like 'other-%' and REPLACE(cDefine30,'other-','')  not in (select cast(id as varchar(36)) from  "+ParamUtil.getParam("oaDatabase").toString()+".dbo.formson_0111)");
            List<SoDetails> listSoDetail=soDetailsMapper.selectList(selectSoDetail);
            if(listSoDetail!=null&&listSoDetail.size()>0) {
                for(SoDetails detail:listSoDetail)
                {

                    //????????????????????????
                    LambdaQueryWrapper<SoDetails> select=new LambdaQueryWrapper<>();
                    select.eq(SoDetails::getAutoid,detail.getAutoid());
                    select.apply("  iSOsID  not in (select iSOsID from  DispatchLists where iSOsID is not null) and  isosid not in (select SoDId from  mps_netdemand where SoDId is not null) ");
                    List<SoDetails> listExist=soDetailsMapper.selectList(select);
                    //???????????????,??????????????????
                    if(listExist!=null&&listExist.size()>0)
                    {
                        //??????
                        soDetailsMapper.deleteById(detail.getAutoid());
                    }
                    //??????????????????????????????
                    else
                    {
                        //??????
                        detail.setCscloser("demo");
                        detail.setDbclosedate(new Date());
                        detail.setDbclosesystime(new Date());
                        detail.setCdefine30(detail.getCdefine30().replace("other-","o-"));
                        soDetailsMapper.updateById(detail);
                    }


                    //????????????
                    LambdaQueryWrapper<SoDetails> selectCount=new LambdaQueryWrapper<>();
                    selectCount.eq(SoDetails::getId,detail.getId());
                    List<SoDetails> listCount=soDetailsMapper.selectList(selectCount);
                    if(listCount==null||listCount.size()==0)
                    {
                        soMainMapper.deleteById(detail.getId());
                    }
                }

            }

        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }



}
