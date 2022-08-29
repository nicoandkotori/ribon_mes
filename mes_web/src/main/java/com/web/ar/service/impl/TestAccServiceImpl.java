package com.web.ar.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.util.CustomStringUtils;
import com.common.util.DateUtil;
import com.common.util.ParamUtil;
import com.common.util.ResponseResult;
import com.modules.data.mybatis.DBTypeEnum;
import com.modules.data.mybatis.DbContextHolder;
import com.modules.security.util.SecurityUtil;
import com.util.ConnectString;
import com.web.ar.entity.CostSimReport;
import com.web.ar.entity.TestAcc;
import com.web.ar.mapper.CostSimReportMapper;
import com.web.ar.mapper.TestAccListMapper;
import com.web.ar.mapper.TestAccMapper;
import com.web.ar.service.ITestAccService;
import com.web.basicinfo.entity.*;
import com.web.basicinfo.mapper.*;
import com.web.mo.dto.BomDTO;
import com.web.mo.entity.TestSo;
import com.web.mo.mapper.BomMapper;
import com.web.om.entity.OmMoDetails;
import com.web.om.mapper.OmMoDetailsMapper;
import com.web.om.mapper.OmMoMainMapper;
import com.web.sa.entity.*;
import com.web.u8system.entity.U8Common;
import com.web.u8system.mapper.U8CommonMapper;
import com.web.u8system.util.U8SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author caihuan
 * @since 2022-06-28
 */
@Service
public class TestAccServiceImpl extends ServiceImpl<TestAccMapper, TestAcc> implements ITestAccService {

    @Autowired
    TestAccListMapper testAccListMapper;
    @Autowired
    TestAccMapper testAccMapper;
    @Autowired
    CostSimReportMapper costSimReportMapper;
    @Value("${account.u8DB}")
    private String u8DB;
    @Autowired
    BomMapper bomMapper;

    @Autowired
    InventoryMapper inventoryMapper;

    @Autowired
    OmMoDetailsMapper omMoDetailsMapper;
    //计算金工成本

    //先不要事务了
    @Override
//    @Transactional(rollbackFor = Exception.class)
    public ResponseResult save(List<CostSimReport> list1,  String  iState) throws Exception{
        ResponseResult result = new ResponseResult();
        result.setMsg("");
        try{
            DbContextHolder.setDbType(DBTypeEnum.db1);
            //清空表中数据
            costSimReportMapper.deleteall();
            testAccListMapper.deleteall();
            //循环计算
            for(CostSimReport costSimReport:list1 )
            {
                //插入需要显示的数据
                costSimReportMapper.insert(costSimReport);

                //删除临时表数据
                testAccMapper.deleteall();
                //计算需求的bom数量
                DbContextHolder.setDbType(DBTypeEnum.db2);
                List<TestAcc>  list=  getTestAccList(costSimReport,iState);
                DbContextHolder.setDbType(DBTypeEnum.db1);
                for(TestAcc testSo : list)
                {
                    if((testSo.getBpro()==null?false:testSo.getBpro().equals(true))||(testSo.getBpu()==null?false:testSo.getBpu().equals(true)))
                    {
//                        if(testSo.getBpro().equals(true))
//                        {
//                            continue;
//                        }
                        if(testSo.getBpro().equals(true)&&testSo.getCinvdefine3()!=null&&!testSo.getCinvdefine3().equals("金工")&&!testSo.getCinvdefine3().equals("外协"))
//                        if(testSo.getBpro().equals(true)&&testSo.getCinvdefine3()!=null&&!testSo.getCinvdefine3().equals("金工"))
                        {
                            continue;
                        }
                        testSo.setCreateUser(SecurityUtil.getUser().getMyusername());
                        testSo.setCreateDate(new Date());
                        testSo.setId(UUID.randomUUID().toString());
                        testSo.setIzDelete(false);
//                        testSo.setStatusId(EnumUtils.StatusId.ADD.getValue());
                        testSo.setIprice(testSo.getIprice().setScale(3,BigDecimal.ROUND_HALF_UP));
                        testSo.setImonery(testSo.getImonery().setScale(3,BigDecimal.ROUND_HALF_UP));
                        testSo.setCprice(testSo.getCprice().setScale(3,BigDecimal.ROUND_HALF_UP));
                        testSo.setCmonery(testSo.getCmonery().setScale(3,BigDecimal.ROUND_HALF_UP));

                        int  n= testAccMapper.insertSelective(testSo);
                        if(n<=0)
                        {
                            throw new Exception("插入数据错误，请重新运算！");
                        }
                        if(testSo.getBpro().equals(true))
                        {
                            if(testSo.getCinvdefine3()!=null&&(testSo.getCinvdefine3().equals("金工")||testSo.getCinvdefine3().equals("外协")))
//                            if(testSo.getCinvdefine3()!=null&&(testSo.getCinvdefine3().equals("金工")))
                            {
                                if(testSo.getIzExist()!=null&&testSo.getIzExist().equals(true))
                                {
                                    if(iState.equals("0"))
                                    {
                                        if(testSo.getCmonery().compareTo(BigDecimal.ZERO)==0)
                                        {
                                            result.setMsg(result.getMsg()+"编码:"+testSo.getPsCode()+",名称"+testSo.getPsName()+"加工费金额不存在！");

                                        }
                                    }
                                    else
                                    {
                                        if(testSo.getImonery().compareTo(BigDecimal.ZERO)==0)
                                        {
                                            result.setMsg(result.getMsg()+"编码:"+testSo.getPsCode()+",名称"+testSo.getPsName()+"加工费金额不存在！");

                                        }
                                    }

                                }
                                else
                                {
                                    result.setMsg(result.getMsg()+"编码:"+testSo.getPsCode()+",名称:"+testSo.getPsName()+"是委外属性，没有匹配对应委外订单！");

                                }
                            }

                        }
                    }

                }

                //更新表数量这些
                costSimReportMapper.updateKQty(costSimReport);
                costSimReportMapper.updateMQty(costSimReport);
                costSimReportMapper.updateONum(costSimReport);
                costSimReportMapper.updateWNum(costSimReport);
                costSimReportMapper.updateTotalNum(costSimReport);
                //插入正式表
                testAccListMapper.insertTestAccList(costSimReport);

            }


        }catch (Exception e){
            throw e;
        }
        return result;
    }

    //计算金工成本
    @Override
//    @Transactional(rollbackFor = Exception.class)
    public ResponseResult save1(List<CostSimReport> list1,String  iState) throws Exception{
        ResponseResult result = new ResponseResult();
        result.setMsg("");
        try{
            //清空表中数据
            costSimReportMapper.deleteall();
            testAccListMapper.deleteall();
            //循环计算
            for(CostSimReport costSimReport:list1 )
            {
                //插入需要显示的数据
                costSimReportMapper.insert(costSimReport);

                //删除临时表数据
                testAccMapper.deleteall();
                //计算需求的bom数量
                DbContextHolder.setDbType(DBTypeEnum.db2);
                List<TestAcc>  list=  getTestAccList(costSimReport,iState);
                DbContextHolder.setDbType(DBTypeEnum.db1);
                for(TestAcc testSo : list)
                {
                    if((testSo.getBpro()==null?false:testSo.getBpro().equals(true))||(testSo.getBpu()==null?false:testSo.getBpu().equals(true)))
                    {

                        testSo.setCreateUser(SecurityUtil.getUser().getMyusername());
                        testSo.setCreateDate(new Date());
                        testSo.setId(UUID.randomUUID().toString());
                        testSo.setIzDelete(false);
//                        testSo.setStatusId(EnumUtils.StatusId.ADD.getValue());
                        testSo.setIprice(testSo.getIprice().setScale(3,BigDecimal.ROUND_HALF_UP));
                        testSo.setImonery(testSo.getImonery().setScale(3,BigDecimal.ROUND_HALF_UP));
                        testSo.setCprice(testSo.getCprice().setScale(3,BigDecimal.ROUND_HALF_UP));
                        testSo.setCmonery(testSo.getCmonery().setScale(3,BigDecimal.ROUND_HALF_UP));
                        int  n= testAccMapper.insertSelective(testSo);
                        if(n<=0)
                        {
                            throw new Exception("插入数据错误，请重新运算！");
                        }

                        if((testSo.getBpro()==null?false:testSo.getBpro().equals(true)))
                        {
                            if(testSo.getIzExist()!=null&&testSo.getIzExist().equals(true))
                            {
                                if(iState.equals("0"))
                                {
                                    if(testSo.getCmonery().compareTo(BigDecimal.ZERO)==0)
                                    {
                                        result.setMsg(result.getMsg()+"编码:"+testSo.getPsCode()+",名称"+testSo.getPsName()+"加工费金额不存在！");

                                    }

                                }
                                else
                                {
                                    if(testSo.getImonery().compareTo(BigDecimal.ZERO)==0)
                                    {
                                        result.setMsg(result.getMsg()+"编码:"+testSo.getPsCode()+",名称"+testSo.getPsName()+"加工费金额不存在！");

                                    }
                                }

                            }
                            else
                            {
                                result.setMsg(result.getMsg()+"编码:"+testSo.getPsCode()+",名称:"+testSo.getPsName()+"是委外属性，没有匹配对应委外订单！");

                            }
                        }

                    }



                }

                //更新表数量这些
                costSimReportMapper.updateKQty(costSimReport);
                costSimReportMapper.updateMQty(costSimReport);
                costSimReportMapper.updateONum(costSimReport);
                costSimReportMapper.updateWNum(costSimReport);
                costSimReportMapper.updateTotalNum(costSimReport);
                //插入正式表
                testAccListMapper.insertTestAccList(costSimReport);

            }


        }catch (Exception e){
            throw e;
        }
        return result;
    }


    //计算需求的bom数量
    public List<TestAcc> getTestAccList(CostSimReport costSimReport,String iState) throws Exception {

        DbContextHolder.setDbType(DBTypeEnum.db2);

        List<BomDTO> parentList = bomMapper.selectParentMenuListByInv(costSimReport.getInvCode(),u8DB);//最上级节点
        List<TestAcc> list = new ArrayList<TestAcc>();
        for (BomDTO parent : parentList) {
            parent.setTotalQty(new BigDecimal(1));
            parent.setTdqtyd(new BigDecimal(1));
            parent.setChildTotalQty(costSimReport.getIqty());
            parent.setDevelop(parent.getInvCode());
            parent.setCfree1("0");
            list = getChildList(parent, list,costSimReport,iState);
        }

        return list;
    }


    //查找子菜单，以及子菜单下的子菜单明细
    public List<TestAcc> getChildList(BomDTO parent, List<TestAcc> list,CostSimReport costSimReport,String iState) throws Exception{
        List<BomDTO> childList = bomMapper.selectByMenu1(parent.getInvCode(),u8DB);
        for (BomDTO child : childList) {
            TestAcc testSo=new TestAcc();

            //需求总数
            child.setTotalQty(child.getQty());
            //级次关系
            child.setDevelop(parent.getDevelop()+"->"+child.getInvCode());
            //先设置本次单个需求量和总需求量
            testSo.setQty(child.getTotalQty().multiply(parent.getTdqtyd()));
            testSo.setQtys(testSo.getQty().multiply(parent.getChildTotalQty()) );
            //母件编码
            testSo.setPspCode(parent.getInvCode());
            //当前编码
            testSo.setPsCode(child.getInvCode());
            testSo.setPsName(child.getInvName());
            testSo.setCinvdefine3(child.getCinvdefine3());
            //级次关系
            testSo.setLj(child.getDevelop());
            testSo.setCsid(costSimReport.getId());
            Inventory inventory=inventoryMapper.getInfoById(child.getInvCode());
//            if(child.getInvCode().equals("R22Y170B-180000"))
//            {
//               String aa="1";
//            }
            if(inventory==null)
            {
                throw new Exception("存货编码:"+child.getInvCode()+"不存在！");
            }
            //设置委外状态
            if(inventory.getBproxyforeign()==null||inventory.getBproxyforeign().equals(false))
            {
                testSo.setBpro(false);
            }
            else
            {
                testSo.setBpro(true);
            }
            if(inventory.getBpurchase()==null||inventory.getBpurchase().equals(false))
            {
                testSo.setBpu(false);
            }
            else
            {
                testSo.setBpu(true);
            }
            testSo.setUnit(inventory.getCcomunitname());
            testSo.setCprice(BigDecimal.ZERO);
            testSo.setCmonery(BigDecimal.ZERO);
            //设置单价
            Inventory inventory1=inventoryMapper.getPriceFirst(child.getInvCode());
            if(inventory1!=null&&inventory1.getIinvncost()!=null)
            {
                testSo.setIprice(new BigDecimal(inventory1.getIinvncost()));
            }
            else
            {
                testSo.setIprice(BigDecimal.ZERO);
            }
            //是采购属性，且没有价格的设置存货档案价格
            if(testSo.getBpu().equals(true)&&testSo.getIprice().compareTo(BigDecimal.ZERO)==0&&inventory.getIinvncost()!=null)
            {
                testSo.setIprice(new BigDecimal(inventory.getIinvncost()));
            }
            //设置金额
            testSo.setImonery(testSo.getIprice().multiply(testSo.getQtys()));
            Boolean izExist=false;
            if(testSo.getBpro().equals(true))
            {

                if(iState.equals("0"))
                {
                    testSo.setStatusId("*");
                    //当年的委外金额
                    OmMoDetails omPoMain=omMoDetailsMapper.getPrice(child.getInvCode(),u8DB);
                    if(omPoMain!=null&&omPoMain.getItaxprice()!=null)
                    {
                        izExist=true;
                        testSo.setIzExist(true);
                        testSo.setCprice(omPoMain.getItaxprice());
                    }
                    else
                    {
                        testSo.setCprice(BigDecimal.ZERO);
                    }



                    //没有金额取去年的委外金额
                    if(testSo.getCprice().compareTo(BigDecimal.ZERO)==0)
                    {
                        OmMoDetails omPoMain1=omMoDetailsMapper.getPriceOld1(child.getInvCode());
                        if(omPoMain1!=null&&omPoMain1.getItaxprice()!=null)
                        {
                            izExist=true;
                            testSo.setIzExist(true);
                            testSo.setCprice(omPoMain1.getItaxprice());
                        }

                    }



                    //去年的委外金额不存在，取前年的
                    if(testSo.getCprice().compareTo(BigDecimal.ZERO)==0)
                    {
                        OmMoDetails omPoMain2=omMoDetailsMapper.getPriceOld2(child.getInvCode());
                        if(omPoMain2!=null&&omPoMain2.getItaxprice()!=null)
                        {
                            izExist=true;
                            testSo.setIzExist(true);
                            testSo.setCprice(omPoMain2.getItaxprice());
                        }

                    }


                    testSo.setCmonery(testSo.getCprice().multiply(testSo.getQtys()));



                    if(omPoMain!=null&&omPoMain.getCdefine27()!=null)
                    {

                        testSo.setIprice(omPoMain.getCdefine27());
                    }
                    else
                    {
                        testSo.setIprice(BigDecimal.ZERO);
                    }
                    //没有金额取去年的委外金额
                    if(testSo.getIprice().compareTo(BigDecimal.ZERO)==0)
                    {
                        OmMoDetails omPoMain1=omMoDetailsMapper.getPrice(child.getInvCode(),u8DB);
                        if(omPoMain1!=null&&omPoMain1.getCdefine27()!=null)
                        {

                            testSo.setIprice(omPoMain1.getCdefine27());
                        }

                    }
                    //去年的委外金额不存在，取前年的
                    if(testSo.getIprice().compareTo(BigDecimal.ZERO)==0)
                    {
                        OmMoDetails omPoMain2=omMoDetailsMapper.getPriceOld1(child.getInvCode());
                        if(omPoMain2!=null&&omPoMain2.getCdefine27()!=null)
                        {

                            testSo.setIprice(omPoMain2.getCdefine27());
                        }

                    }

                    //加上下级的是委外的材料的单价
                    List<BomDTO> childList1 = bomMapper.selectByMenu1(child.getInvCode(),u8DB);

                    if(childList1!=null)
                    {
                        BigDecimal totalPrice=BigDecimal.ZERO;

                        for (BomDTO child1 : childList1) {
                            Boolean izExist1=false;
                            Inventory inventory2=inventoryMapper.getInfoById(child1.getInvCode());
                            if(inventory2==null)
                            {
                                throw new Exception("存货编码:"+child1.getInvCode()+"不存在！");
                            }
                            //判断委外状态
                            if(inventory2.getBproxyforeign()!=null&&inventory2.getBproxyforeign().equals(true))
                            {

                                //下级存在委外件的，上级设置一个标识符

                                TestAcc testSo1=new TestAcc();
                                testSo1.setStatusId("**"+testSo.getPsCode());
                                //先设置本次单个需求量和总需求量
                                testSo1.setQty(child1.getQty().multiply(parent.getTdqtyd()));
                                testSo1.setQtys(testSo1.getQty().multiply(parent.getChildTotalQty()) );
                                //母件编码
                                testSo1.setPspCode(child.getInvCode());
                                //当前编码
                                testSo1.setPsCode(child1.getInvCode());
                                testSo1.setPsName(child1.getInvName());
                                testSo1.setCinvdefine3(child1.getCinvdefine3());
                                //级次关系
                                testSo1.setLj(child.getDevelop()+"->"+child1.getInvCode());
                                testSo1.setCsid(costSimReport.getId());

                                //设置委外状态
                                if(inventory2.getBproxyforeign()==null||inventory2.getBproxyforeign().equals(false))
                                {
                                    testSo1.setBpro(false);
                                }
                                else
                                {
                                    testSo1.setBpro(true);
                                }

                                testSo1.setUnit(inventory2.getCcomunitname());
                                testSo1.setCprice(BigDecimal.ZERO);
                                testSo1.setCmonery(BigDecimal.ZERO);
                                testSo1.setIprice(BigDecimal.ZERO);
                                testSo1.setImonery(BigDecimal.ZERO);


                                BigDecimal cPrice=BigDecimal.ZERO,iPrice=BigDecimal.ZERO;
                                //当年的委外金额
                                OmMoDetails omPoMain1=omMoDetailsMapper.getPrice(child1.getInvCode(),u8DB);
                                //加工费
                                if(omPoMain1!=null&&omPoMain1.getItaxprice()!=null)
                                {
                                    izExist1=true;
                                    testSo1.setIzExist(true);
                                    testSo1.setCprice(omPoMain1.getItaxprice());
                                    iPrice=omPoMain1.getItaxprice();
                                }
                                else
                                {
                                    testSo1.setCprice(BigDecimal.ZERO);
                                }
                                //材料费
                                if(omPoMain1!=null&&omPoMain1.getCdefine27()!=null)
                                {
                                    testSo1.setIprice(omPoMain1.getCdefine27());
                                    cPrice=omPoMain1.getCdefine27();

                                }
                                else{
                                    testSo1.setIprice(BigDecimal.ZERO);
                                }






                                OmMoDetails omPoMain2=omMoDetailsMapper.getPriceOld1(child1.getInvCode());
                                //没有金额取去年的委外金额  加工费
                                if(iPrice.compareTo(BigDecimal.ZERO)==0)
                                {

                                    if(omPoMain2!=null&&omPoMain2.getItaxprice()!=null)
                                    {
                                        izExist1=true;
                                        testSo1.setIzExist(true);
                                        iPrice=omPoMain2.getItaxprice();
                                        testSo1.setCprice(omPoMain2.getItaxprice());
                                    }

                                }
                                //没有金额取去年的委外金额  材料费
                                if(cPrice.compareTo(BigDecimal.ZERO)==0)
                                {

                                    if(omPoMain2!=null&&omPoMain2.getCdefine27()!=null)
                                    {
                                        cPrice=omPoMain2.getCdefine27();
                                        testSo1.setIprice(omPoMain2.getCdefine27());
                                    }

                                }



                                OmMoDetails omPoMain3=omMoDetailsMapper.getPriceOld2(child.getInvCode());
                                //没有金额取的委外金额，取前年的  加工费
                                if(iPrice.compareTo(BigDecimal.ZERO)==0)
                                {

                                    if(omPoMain3!=null&&omPoMain3.getItaxprice()!=null)
                                    {
                                        izExist1=true;
                                        testSo1.setIzExist(true);
                                        iPrice=omPoMain3.getItaxprice();
                                        testSo1.setCprice(omPoMain3.getItaxprice());
                                    }

                                }

                                //委外金额不存在，取前年的 材料费
                                if(cPrice.compareTo(BigDecimal.ZERO)==0)
                                {

                                    if(omPoMain3!=null&&omPoMain3.getCdefine27()!=null)
                                    {
                                        cPrice=omPoMain3.getCdefine27();
                                        testSo1.setIprice(omPoMain3.getCdefine27());
                                    }

                                }
                                testSo1.setImonery(testSo1.getIprice().multiply(testSo1.getQtys()));
                                testSo1.setCmonery(testSo1.getCprice().multiply(testSo1.getQtys()));
                                list.add(testSo1);
//                                totalPrice=totalPrice.add(cPrice);
                            }


                        }
                        //添加到上级的价格中
//                        testSo.setIprice(testSo.getIprice().add(totalPrice));

                    }


                    testSo.setImonery(testSo.getIprice().multiply(testSo.getQtys()));
                }
                else
                {

                    //当年的委外金额
                    OmMoDetails omPoMain=omMoDetailsMapper.getPrice(child.getInvCode(),u8DB);
                    if(omPoMain!=null&&omPoMain.getItaxprice()!=null)
                    {
                        izExist=true;
                        testSo.setIzExist(true);
                        testSo.setIprice(omPoMain.getItaxprice());
                    }
                    else
                    {
                        testSo.setIprice(BigDecimal.ZERO);
                    }



                    //没有金额取去年的委外金额
                    if(testSo.getIprice().compareTo(BigDecimal.ZERO)==0)
                    {
                        OmMoDetails omPoMain1=omMoDetailsMapper.getPriceOld1(child.getInvCode());
                        if(omPoMain1!=null&&omPoMain1.getItaxprice()!=null)
                        {
                            izExist=true;
                            testSo.setIzExist(true);
                            testSo.setIprice(omPoMain1.getItaxprice());
                        }

                    }



                    //去年的委外金额不存在，取前年的
                    if(testSo.getIprice().compareTo(BigDecimal.ZERO)==0)
                    {
                        OmMoDetails omPoMain2=omMoDetailsMapper.getPriceOld2(child.getInvCode());
                        if(omPoMain2!=null&&omPoMain2.getItaxprice()!=null)
                        {
                            izExist=true;
                            testSo.setIzExist(true);
                            testSo.setIprice(omPoMain2.getItaxprice());
                        }

                    }


                    testSo.setImonery(testSo.getIprice().multiply(testSo.getQtys()));


                }

            }


            //                判断可用数量满足的话，本次分配量的=总需求数量，否则分配数量为可用数量，并且
            if(testSo.getBpro().equals(true))
            {
                //                if(izExist.equals(false))
                //                {
                //                    throw new Exception("编码:"+child.getInvCode()+",名称:"+child.getInvName()+"是委外属性，没有匹配对应委外订单！");
                //
                //                }
                //                else{
                //                    if(testSo.getImonery().compareTo(BigDecimal.ZERO)==0)
                //                    {
                //                        throw new Exception("编码:"+child.getInvCode()+",名称:"+child.getInvName()+"材料费金额不存在！");
                //                    }
                //
                //                }
                //满足的条件不需要下级展开
                list.add(testSo);
            }
            else
            {

                //下级计算量为需求总数-当前可满足数量
                child.setTdqtyd(child.getQty().multiply(parent.getTdqtyd()));
                child.setChildTotalQty(parent.getChildTotalQty());
                //不满足的条件需要下级展开
                list.add(testSo);
                list = getChildList(child, list,costSimReport,iState);
            }





        }
        return list;
    }










    //计算金工成本
    @Override
//    @Transactional(rollbackFor = Exception.class)
    public ResponseResult saveByInvCode(String  invCode) throws Exception{
        ResponseResult result = new ResponseResult();
        result.setMsg("");
        try{
            //清空表中数据
            costSimReportMapper.deleteall();
            testAccListMapper.deleteall();
            //循环计算
             CostSimReport costSimReport=new CostSimReport();
             costSimReport.setId("1");
             costSimReport.setInvCode(invCode);
             costSimReport.setIqty(BigDecimal.ONE);
            //插入需要显示的数据
            costSimReportMapper.insert(costSimReport);

            //删除临时表数据
            testAccMapper.deleteall();
            //计算需求的bom数量
            DbContextHolder.setDbType(DBTypeEnum.db2);
            List<TestAcc>  list=  getTestAccMaterialList(costSimReport);
            DbContextHolder.setDbType(DBTypeEnum.db1);
            for(TestAcc testSo : list)
            {
                if((testSo.getBpro()==null?false:testSo.getBpro().equals(true))||(testSo.getBpu()==null?false:testSo.getBpu().equals(true)))
                {

                    testSo.setCreateUser(SecurityUtil.getUser().getMyusername());
                    testSo.setCreateDate(new Date());
                    testSo.setId(UUID.randomUUID().toString());
                    testSo.setIzDelete(false);
//                        testSo.setStatusId(EnumUtils.StatusId.ADD.getValue());
                    testSo.setIprice(testSo.getIprice().setScale(3,BigDecimal.ROUND_HALF_UP));
                    testSo.setImonery(testSo.getImonery().setScale(3,BigDecimal.ROUND_HALF_UP));
                    testSo.setCprice(testSo.getCprice().setScale(3,BigDecimal.ROUND_HALF_UP));
                    testSo.setCmonery(testSo.getCmonery().setScale(3,BigDecimal.ROUND_HALF_UP));
                    int  n= testAccMapper.insertSelective(testSo);
                    if(n<=0)
                    {
                        throw new Exception("插入数据错误，请重新运算！");
                    }

                    if((testSo.getBpro()==null?false:testSo.getBpro().equals(true)))
                    {
                        if(testSo.getIzExist()!=null&&testSo.getIzExist().equals(true))
                        {

                            if(testSo.getCmonery().compareTo(BigDecimal.ZERO)==0)
                            {
                                result.setMsg(result.getMsg()+"编码:"+testSo.getPsCode()+",名称"+testSo.getPsName()+"加工费金额不存在！");

                            }



                        }
                        else
                        {
                            result.setMsg(result.getMsg()+"编码:"+testSo.getPsCode()+",名称:"+testSo.getPsName()+"是委外属性，没有匹配对应委外订单！");

                        }
                    }

                }



            }

            //更新表数量这些
            costSimReportMapper.updateKQty(costSimReport);
            costSimReportMapper.updateMQty(costSimReport);
            costSimReportMapper.updateONum(costSimReport);
            costSimReportMapper.updateWNum(costSimReport);
            costSimReportMapper.updateTotalNum(costSimReport);
            //插入正式表
            testAccListMapper.insertTestAccList(costSimReport);




        }catch (Exception e){
            throw e;
        }
        return result;
    }




    //计算需求的bom数量
    public List<TestAcc> getTestAccMaterialList(CostSimReport costSimReport) throws Exception {

        DbContextHolder.setDbType(DBTypeEnum.db2);

        List<BomDTO> parentList = bomMapper.selectParentMenuListByInv(costSimReport.getInvCode(),u8DB);//最上级节点
        List<TestAcc> list = new ArrayList<TestAcc>();
        for (BomDTO parent : parentList) {
            parent.setTotalQty(new BigDecimal(1));
            parent.setTdqtyd(new BigDecimal(1));
            parent.setChildTotalQty(costSimReport.getIqty());
            parent.setDevelop(parent.getInvCode());
            parent.setCfree1("0");
            list = getChildMaterialList(parent, list,costSimReport);
        }

        return list;
    }


    //查找子菜单，以及子菜单下的子菜单明细
    public List<TestAcc> getChildMaterialList(BomDTO parent, List<TestAcc> list,CostSimReport costSimReport) throws Exception{
        List<BomDTO> childList = bomMapper.selectByMenu1(parent.getInvCode(),u8DB);
        for (BomDTO child : childList) {
            TestAcc testSo=new TestAcc();

            //需求总数
            child.setTotalQty(child.getQty());
            //级次关系
            child.setDevelop(parent.getDevelop()+"->"+child.getInvCode());
            //先设置本次单个需求量和总需求量
            testSo.setQty(child.getTotalQty().multiply(parent.getTdqtyd()));
            testSo.setQtys(testSo.getQty().multiply(parent.getChildTotalQty()) );
            //母件编码
            testSo.setPspCode(parent.getInvCode());
            //当前编码
            testSo.setPsCode(child.getInvCode());
            testSo.setPsName(child.getInvName());
            testSo.setCinvdefine3(child.getCinvdefine3());
            //级次关系
            testSo.setLj(child.getDevelop());
            testSo.setCsid(costSimReport.getId());


            Inventory inventory=inventoryMapper.getInfoById(child.getInvCode());
//            if(child.getInvCode().equals("R22Y170B-180000"))
//            {
//               String aa="1";
//            }
            if(inventory==null)
            {
                throw new Exception("存货编码:"+child.getInvCode()+"不存在！");
            }
            //设置委外状态
            if(inventory.getBproxyforeign()==null||inventory.getBproxyforeign().equals(false))
            {
                testSo.setBpro(false);
            }
            else
            {
                testSo.setBpro(true);
            }
            if(inventory.getBpurchase()==null||inventory.getBpurchase().equals(false))
            {
                testSo.setBpu(false);
            }
            else
            {
                testSo.setBpu(true);
            }
            testSo.setUnit(inventory.getCcomunitname());
            testSo.setCprice(BigDecimal.ZERO);
            testSo.setCmonery(BigDecimal.ZERO);
            //设置单价
            Inventory inventory1=inventoryMapper.getPriceFirst(child.getInvCode());
            if(inventory1!=null&&inventory1.getIinvncost()!=null&&testSo.getBpu().equals(true))
            {
                testSo.setIprice(new BigDecimal(inventory1.getIinvncost()));
            }
            else
            {
                testSo.setIprice(BigDecimal.ZERO);
            }
            //是采购属性，且没有价格的设置存货档案价格
            if(testSo.getBpu().equals(true)&&testSo.getIprice().compareTo(BigDecimal.ZERO)==0&&inventory.getIinvncost()!=null)
            {
                testSo.setIprice(new BigDecimal(inventory.getIinvncost()));
            }
            //设置金额
            testSo.setImonery(testSo.getIprice().multiply(testSo.getQtys()));
            Boolean izExist=false;
            if(testSo.getBpro().equals(true))
            {

                testSo.setStatusId("*");
                //当年的委外金额
                OmMoDetails omPoMain=omMoDetailsMapper.getPrice(child.getInvCode(),u8DB);
                if(omPoMain!=null&&omPoMain.getItaxprice()!=null)
                {
                    izExist=true;
                    testSo.setIzExist(true);
                    testSo.setCprice(omPoMain.getItaxprice());
                }
                else
                {
                    testSo.setCprice(BigDecimal.ZERO);
                }



                //没有金额取去年的委外金额
                if(testSo.getCprice().compareTo(BigDecimal.ZERO)==0)
                {
                    OmMoDetails omPoMain1=omMoDetailsMapper.getPriceOld1(child.getInvCode());
                    if(omPoMain1!=null&&omPoMain1.getItaxprice()!=null)
                    {
                        izExist=true;
                        testSo.setIzExist(true);
                        testSo.setCprice(omPoMain1.getItaxprice());
                    }

                }



                //去年的委外金额不存在，取前年的
                if(testSo.getCprice().compareTo(BigDecimal.ZERO)==0)
                {
                    OmMoDetails omPoMain2=omMoDetailsMapper.getPriceOld2(child.getInvCode());
                    if(omPoMain2!=null&&omPoMain2.getItaxprice()!=null)
                    {
                        izExist=true;
                        testSo.setIzExist(true);
                        testSo.setCprice(omPoMain2.getItaxprice());
                    }

                }


                testSo.setCmonery(testSo.getCprice().multiply(testSo.getQtys()));



//                if(omPoMain!=null&&omPoMain.getCdefine27()!=null)
//                {
//
//                    testSo.setIprice(omPoMain.getCdefine27());
//                }
//                else
//                {
//                    testSo.setIprice(BigDecimal.ZERO);
//                }
//                //没有金额取去年的委外金额
//                if(testSo.getIprice().compareTo(BigDecimal.ZERO)==0)
//                {
//                    OmMoDetails omPoMain1=omMoDetailsMapper.getPrice(child.getInvCode(),u8DB);
//                    if(omPoMain1!=null&&omPoMain1.getCdefine27()!=null)
//                    {
//
//                        testSo.setIprice(omPoMain1.getCdefine27());
//                    }
//
//                }
//                //去年的委外金额不存在，取前年的
//                if(testSo.getIprice().compareTo(BigDecimal.ZERO)==0)
//                {
//                    OmMoDetails omPoMain2=omMoDetailsMapper.getPriceOld1(child.getInvCode());
//                    if(omPoMain2!=null&&omPoMain2.getCdefine27()!=null)
//                    {
//
//                        testSo.setIprice(omPoMain2.getCdefine27());
//                    }
//
//                }
//
//
//                testSo.setImonery(testSo.getIprice().multiply(testSo.getQtys()));

            }


            //下级计算量为需求总数-当前可满足数量
            child.setTdqtyd(child.getQty().multiply(parent.getTdqtyd()));
            child.setChildTotalQty(parent.getChildTotalQty());
            //不满足的条件需要下级展开
            list.add(testSo);
            list = getChildMaterialList(child, list,costSimReport);






        }
        return list;
    }







}
