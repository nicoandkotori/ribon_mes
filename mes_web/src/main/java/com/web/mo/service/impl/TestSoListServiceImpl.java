package com.web.mo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.util.CustomStringUtils;
import com.common.util.DateUtil;
import com.common.util.ParamUtil;
import com.common.util.ResponseResult;
import com.modules.data.mybatis.DBTypeEnum;
import com.modules.data.mybatis.DbContextHolder;
import com.modules.security.util.SecurityUtil;
import com.web.basicinfo.entity.*;
import com.web.basicinfo.mapper.*;
import com.web.mo.entity.TestSoList;
import com.web.mo.mapper.TestSoListMapper;
import com.web.mo.mapper.TestSoMapper;
import com.web.mo.service.ITestSoListService;
import com.web.st.entity.CurrentStock;
import com.web.st.entity.IaStUnAccountVouch09;
import com.web.st.entity.RdRecord09;
import com.web.st.entity.RdRecords09;
import com.web.st.mapper.IaStUnAccountVouch09Mapper;
import com.web.st.mapper.RdRecord09Mapper;
import com.web.st.mapper.RdRecords09Mapper;
import com.web.u8system.mapper.U8CommonMapper;
import com.web.u8system.util.U8CurrentStockUtils;
import com.web.u8system.util.U8SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author caihuan
 * @since 2022-06-28
 */
@Service
public class TestSoListServiceImpl extends ServiceImpl<TestSoListMapper, TestSoList> implements ITestSoListService {

    @Autowired
    private TestSoMapper testSoMapper;

    @Autowired
    private TestSoListMapper testSoListMapper;
    @Autowired
    private IaStUnAccountVouch09Mapper iaStUnAccountVouch09Mapper;
    @Autowired
    private U8SystemUtils u8SystemUtils;
    @Autowired
    private U8CurrentStockUtils u8CurrentStockUtils;
    @Autowired
    private RdRecord09Mapper rdRecord09Mapper;
    @Autowired
    private RdRecords09Mapper rdRecords09Mapper;

    @Value("${account.acountId}")
    private String accId;


    @Override
    public List<TestSoList> getDetailList(TestSoList query){
        return testSoListMapper.getDetailList(query);
    }
    @Override
    public List<TestSoList> getPrintListDevice(TestSoList query){
        return testSoListMapper.getPrintListDevice(query);
    }
    @Override
    public List<TestSoList> getDetailListByWhCode(TestSoList query){
        return testSoListMapper.getDetailListByWhCode(query);
    }


    /**
     * ??????
     * @param query
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult delete(TestSoList query) throws Exception{
        ResponseResult result = new ResponseResult();
        try{
            List<TestSoList> testSoList=testSoListMapper.getListByCode(query);
            //?????????????????????????????????????????????
            if(CustomStringUtils.isNotBlank(testSoList))
            {
                for(TestSoList m:testSoList)
                {
                    TestSoList query1 =new TestSoList();
                    query1.setOrderNo(m.getOrderNo());
                    query1.setProductInvCode(m.getProductInvCode());
                    TestSoList testSoList1=testSoListMapper.getInfoByCode(query);
                    //?????????????????????????????????????????????
                    if(CustomStringUtils.isNotBlank(testSoList1)) {
                        if (testSoList1.getYqty() == null) {
                            testSoList1.setYqty(BigDecimal.ZERO);
                        }
                        if (testSoList1.getYqty().compareTo(BigDecimal.ZERO) == 1) {
                            throw new Exception("??????????????????????????????????????????????????????????????????");
                        }
                    }
                    int n=testSoListMapper.deleteByCode(m);
                    if(n<=0)
                    {
                        throw new Exception("?????????????????????");
                    }
                }

            }
            else{
                throw new Exception("??????????????????");
            }



        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }



    /**
     * ???????????????,?????????+001
     * @return
     */
    public String getU8MaxCode(String headCode) throws Exception {

        String head =  headCode+ DateUtil.getDateStr(new Date(), "yyyyMMdd");
        String sortNum = "001";
        int sortNumLength = 3;

        LambdaQueryWrapper<RdRecord09> select=new LambdaQueryWrapper<>();
        select.select(RdRecord09::getCcode)
                .like(RdRecord09::getCcode, head);
        select.orderByDesc(RdRecord09::getCcode);
        List<RdRecord09> mainList = rdRecord09Mapper.selectList(select);
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
    /**
     * ??????u8?????????
     * @param list
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult saveU8(List<TestSoList> list) throws Exception{
        ResponseResult result = new ResponseResult();
        try{
            //?????????????????????
            RdRecord09 rdrecord=new RdRecord09();
            rdrecord.setDefaultValueForOtherOut();
            rdrecord.setCwhcode("3");
            rdrecord.setDdate(new Date());
            rdrecord.setCmaker(SecurityUtil.getUser().getMyusername());
            String cCode=getU8MaxCode("CK");
            rdrecord.setCcode(cCode);
            Integer u8fid = null;
            u8fid=u8SystemUtils.getFatherId(accId,"rd",10);
            rdrecord.setId(Long.valueOf(u8fid));

            Integer u8cid = null;
            u8cid=u8SystemUtils.getChildId(accId,"rd",10,list.size());

            List<RdRecords09> rdrecordss=new ArrayList<>();
            int row=1;
            for(TestSoList m: list)
            {

                TestSoList testSoList=testSoListMapper.getInfoById(m.getId(), ParamUtil.getParam("localDatabase").toString());
                //????????????
                if(CustomStringUtils.isNotBlank(testSoList.getFromNo()))
                {
                    rdrecord.setCmemo(testSoList.getFromNo());
                }

                //?????????????????????????????????????????????
                if(CustomStringUtils.isNotBlank(m))
                {
                    if(m.getNowAdQty()==null)
                    {
                        m.setNowAdQty(BigDecimal.ZERO);
                    }
                    if(m.getNowAdQty().compareTo(BigDecimal.ZERO)<=0)
                    {
                        continue;
                    }

                    //??????????????????
                    RdRecords09 q=new RdRecords09();
                    q.setDefault();
                    q.setId(rdrecord.getId());
                    q.setIquantity(m.getNowAdQty());
                    q.setCinvcode(m.getPsCode());
                    q.setAutoid(Long.valueOf(u8cid));
                    q.setIrowno(row);
                    rdrecordss.add(q);



                    //??????testsolist????????????
                    m.setAdQty(m.getNowAdQty());
                    int n=testSoListMapper.updateyQty1(m, ParamUtil.getParam("localDatabase").toString());
                    if(n<=0)
                    {
                        throw new Exception("???????????????????????????");
                    }


                }
                else{
                    throw new Exception("??????????????????");
                }
                u8cid++;
                row++;

            }
            if(rdrecordss.size()>0)
            {
                int n=rdRecord09Mapper.insert(rdrecord);
                if(n<=0)
                {
                    throw new Exception("?????????????????????");
                }
                for(RdRecords09 m :rdrecordss)
                {

                    //????????????
                    IaStUnAccountVouch09 iaStUnAccountVouch09=new IaStUnAccountVouch09();
                    iaStUnAccountVouch09.setIdun(m.getId());
                    iaStUnAccountVouch09.setIdsun(m.getAutoid());
                    iaStUnAccountVouch09.setCvoutypeun(rdrecord.getCvouchtype());
                    iaStUnAccountVouch09.setCbustypeun(rdrecord.getCbustype());
                    iaStUnAccountVouch09Mapper.insert(iaStUnAccountVouch09);

                    //??????????????????????????????????????????
                    u8CurrentStockUtils.updateQuanity(rdrecord.getCwhcode(), m.getCinvcode(), "", "", "", "", m.getIquantity(), null, "del");
//                        //?????????????????????????????????
//                        if(n==0)
//                        {
//                            throw new Exception("????????????????????????");
//                        }

                    //??????????????????
                    n=rdRecords09Mapper.insert(m);
                    if(n<=0)
                    {
                        throw new Exception("?????????????????????");
                    }
                }


            }
            else
            {
                throw new Exception("???????????????????????????");
            }



        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }



    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateSufaceWay(List<TestSoList> list) throws Exception{
        ResponseResult result = new ResponseResult();
        try{

            for(TestSoList m: list)
            {
                testSoListMapper.updateSufaceWay(m);
            }

        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }


}
