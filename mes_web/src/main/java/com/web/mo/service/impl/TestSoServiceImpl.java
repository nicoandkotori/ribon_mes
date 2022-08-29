package com.web.mo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.util.CustomStringUtils;
import com.common.util.ParamUtil;
import com.common.util.ResponseResult;
import com.common.util.SnowFlakeUtils;
import com.modules.security.util.SecurityUtil;
import com.util.EnumUtils;
import com.web.basicinfo.mapper.CustomerMapper;
import com.web.mo.dto.BomDTO;
import com.web.mo.entity.TestSo;
import com.web.mo.entity.TestSoList;
import com.web.mo.entity.U8MpsNetdemand;
import com.web.mo.mapper.BomMapper;
import com.web.mo.mapper.TestSoListMapper;
import com.web.mo.mapper.TestSoMapper;
import com.web.mo.service.ITestSoListService;
import com.web.mo.service.ITestSoService;
import com.web.po.entity.PoDetails;
import com.web.po.mapper.PoDetailsMapper;
import com.web.st.entity.CurrentStock;
import com.web.st.mapper.CurrentStockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author caihuan
 * @since 2022-06-28
 */
@Service
public class TestSoServiceImpl extends ServiceImpl<TestSoMapper, TestSo> implements ITestSoService {

    @Autowired
    private TestSoMapper testSoMapper;
    @Autowired
    private CurrentStockMapper currentStockMapper;
    @Autowired
    private TestSoListMapper testSoListMapper;
    @Autowired
    private PoDetailsMapper poDetailsMapper;
    @Autowired
    private BomMapper bomMapper;

    @Value("${account.u8DB}")
    private String u8DB;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult calculate(List<U8MpsNetdemand> list1) throws Exception{
        ResponseResult result = new ResponseResult();
        try{

            for(U8MpsNetdemand srpMainProducts:list1 )
            {
                TestSoList query =new TestSoList();
                query.setOrderNo(srpMainProducts.getPlancode());
                query.setProductInvCode(srpMainProducts.getCinvcode());
                TestSoList testSoList=testSoListMapper.getInfoByCode(query);
                //判断正式表中是否存在且已经审核
                if(CustomStringUtils.isNotBlank(testSoList))
                {
                    if(testSoList.getYqty()==null)
                    {
                        testSoList.setYqty(BigDecimal.ZERO);
                    }
                    if(testSoList.getYqty().compareTo(BigDecimal.ZERO)==1)
                    {
                        throw new Exception("对应数据已经存在半成品抵扣数据，不允许重复运算！");
                    }
                    if(!testSoList.getStatusId().equals(EnumUtils.StatusId.ADD.getValue()))
                    {
                        throw new Exception("对应数据已经运算且审核，不允许重复运算！");
                    }
                    //删除正式表数据
                    query.setStatusId(EnumUtils.StatusId.CLOSE.getValue());

//                    int  n=testSoListMapper.deleteByCode(query);
                    int  n=testSoListMapper.updateByCode(query);
                    if(n<=0)
                    {
                        throw new Exception("删除正式表数据错误！");
                    }
                }

                //删除当前登录人员的临时表
                testSoMapper.deleteByUser(SecurityUtil.getUser().getMyusername());

                //计算需求的bom数量
                List<TestSo>  list=  getTestSoList(srpMainProducts);
                for(TestSo testSo : list)
                {
                    testSo.setCreateUser(SecurityUtil.getUser().getMyusername());
                    testSo.setCreateDate(new Date());
                    testSo.setId(String.valueOf(SnowFlakeUtils.getFlowIdInstance().nextId()));
                    testSo.setIzDelete(false);
                    testSo.setStatusId(EnumUtils.StatusId.ADD.getValue());
                    int  n= testSoMapper.insert(testSo);
                    if(n<=0)
                    {
                        throw new Exception("插入数据错误，请重新运算！");
                    }

                }

                //更新部件
                TestSo query1 =new TestSo();
                query1.setCreateUser(SecurityUtil.getUser().getMyusername());
                query1.setU8DB(u8DB);
                testSoMapper.updatePartName1(query1);
                testSoMapper.updatePartName2(query1);
                testSoMapper.updatePartName3(query1);
                //更新下料尺寸
                testSoMapper.updateMatSize1(query1);
                testSoMapper.updateMatSize2(query1);
                testSoMapper.updateMatSize3(query1);

                //插入正式表
                testSoMapper.insertTestSoList(query1);

            }

        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }

    //计算需求的bom数量
    public List<TestSo> getTestSoList(U8MpsNetdemand srpMainProducts) throws Exception {

        List<BomDTO> parentList = bomMapper.selectParentMenuListByInv(srpMainProducts.getCinvcode(),u8DB);//最上级节点
        List<TestSo> list = new ArrayList<TestSo>();
        for (BomDTO parent : parentList) {
            parent.setTotalQty(new BigDecimal(1));
            parent.setChildTotalQty(srpMainProducts.getPlanqty());
            parent.setDevelop(parent.getInvCode());
            list = getChildList(parent, list,srpMainProducts);
        }

        return list;
    }

    //查找子菜单，以及子菜单下的子菜单
    public List<TestSo> getChildList(BomDTO parent, List<TestSo> list,U8MpsNetdemand srpMainProducts) {
        List<BomDTO> childList = bomMapper.selectByMenu1(parent.getInvCode(),u8DB);
        for (BomDTO child : childList) {
            TestSo testSo=new TestSo();
            //需求总数
            child.setTotalQty(child.getQty());
            //级次关系
            child.setDevelop(parent.getDevelop()+"->"+child.getInvCode());
            //先设置本次单个需求量和总需求量
            testSo.setQty(child.getTotalQty());
            testSo.setQtys(child.getQty().multiply(parent.getChildTotalQty()) );
            //母件编码
            testSo.setPspCode(parent.getInvCode());
            //当前编码
            testSo.setPsCode(child.getInvCode());
            //级次关系
            testSo.setDevelop(child.getDevelop());
            //运算单数量
            testSo.setSoQty(srpMainProducts.getPlanqty());
            //备注
            testSo.setMemo(child.getDefine25());
            //加工类型
            testSo.setProType(child.getCinvdefine3());
            //合同号
            testSo.setConNo(srpMainProducts.getCdefine1());
            //运算单号
            testSo.setOrderNo(srpMainProducts.getPlancode());

            //客户名称
            testSo.setCustName(srpMainProducts.getCcusname());

            //最上级编码
            testSo.setProductInvCode(srpMainProducts.getCinvcode());
            //最上级名称
            testSo.setProductInvName(srpMainProducts.getCinvname());
            //来源单号
            testSo.setFromNo(srpMainProducts.getCsocode());
            //已出库数量
            testSo.setYqty(BigDecimal.ZERO);
            //库存量
            CurrentStock currentstock=currentStockMapper.getSumQtyByInvCode(child.getInvCode(),u8DB);
            if(CustomStringUtils.isNotBlank(currentstock)&&CustomStringUtils.isNotBlank(currentstock.getIquantity()))
            {
                testSo.setCurQty(currentstock.getIquantity());
            }
            else
            {
                testSo.setCurQty(BigDecimal.ZERO);
            }
            //采购在途量
            PoDetails poDetails=poDetailsMapper.getSumQtyByInvCode(child.getInvCode(),u8DB);
            if(CustomStringUtils.isNotBlank(poDetails)&&CustomStringUtils.isNotBlank(poDetails.getIquantity()))
            {
                testSo.setPoQty(poDetails.getIquantity());
            }
            else
            {
                testSo.setPoQty(BigDecimal.ZERO);
            }

            //已经保存到正式表的未出库的已分配数量,计算可用数量
            TestSoList testSoList=testSoListMapper.getSumQtyByInvCode(child.getInvCode());
            BigDecimal yQty=BigDecimal.ZERO;
//            //历史未出库已分配数量
            if(CustomStringUtils.isNotBlank(testSoList)&&CustomStringUtils.isNotBlank(testSoList.getYqty()))
            {
                yQty=testSoList.getYqty();
            }
            //当前计算的已分配数量
            BigDecimal nowyQty =list.stream().filter(p -> p.getPsCode().equals(testSo.getPsCode())).map(TestSo::getAdQty).reduce(BigDecimal.ZERO, BigDecimal::add);
            //可用数量
            testSo.setKyQty(testSo.getCurQty().add(testSo.getPoQty()).subtract(yQty).subtract(nowyQty));
            //如果可用数量小于0，设置为0
            if(testSo.getKyQty().compareTo(BigDecimal.ZERO)==-1)
            {
                testSo.setKyQty(BigDecimal.ZERO);
            }

            //判断可用数量满足的话，本次分配量的=总需求数量，否则分配数量为可用数量，并且
            if(testSo.getKyQty().compareTo(testSo.getQtys())>=0)
            {
                testSo.setAdQty(testSo.getQtys());
                //满足的条件不需要下级展开
                list.add(testSo);
            }
            else
            {
                if(child.getCinvdefine3()!=null&&child.getCinvdefine3().equals("半成品"))
                {
                    testSo.setAdQty(testSo.getQtys());
                }
                else{
                    testSo.setAdQty(testSo.getKyQty());
                }
                //注释6-4  半成品抵扣量存在
//                testSo.setAdQty(testSo.getKyQty());
                //下级计算量为需求总数-当前可满足数量
                child.setChildTotalQty(testSo.getQtys().subtract(testSo.getKyQty()));
                //不满足的条件需要下级展开
                list.add(testSo);
                list = getChildList(child, list,srpMainProducts);
            }



        }
        return list;
    }

}
