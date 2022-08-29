package com.web.basicinfo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.util.CustomStringUtils;
import com.common.util.ParamUtil;
import com.common.util.ResponseResult;
import com.modules.data.mybatis.DBTypeEnum;
import com.modules.data.mybatis.DbContextHolder;
import com.web.basicinfo.entity.*;
import com.web.basicinfo.mapper.*;
import com.web.basicinfo.service.IInventoryService;
import com.web.basicinfo.service.IOaInventoryApplyService;
import com.web.u8system.util.U8SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author caihuan
 * @since 2022-06-28
 */
@Service
public class OaInventoryApplyServiceImpl extends ServiceImpl<OaInventoryApplyMapper, OaInventoryApply> implements IOaInventoryApplyService {

    @Autowired
    private OaInventoryApplyDetailMapper oaInventoryApplyDetailMapper;
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
    private BasPartMapper basPartMapper;
    @Autowired
    private U8SystemUtils u8SystemUtils;

    @Value("${account.acountId}")
    private String accId;
    /**
     * 同步存货档案
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncInventoryApplyData()
    {
        //DB2是U8连接
        DbContextHolder.setDbType(DBTypeEnum.db2);
        syncInventory();
    }

    /**
     * 同步存货档案
     * @return
     */
//    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult syncInventory() {
        ResponseResult result = new ResponseResult();
        try{

            List<OaInventoryApplyDetail> listSync=oaInventoryApplyDetailMapper.getListBySync(new OaInventoryApplyDetail(),ParamUtil.getParam("oaDatabase").toString());
            if(listSync!=null)
            {
                for(OaInventoryApplyDetail q:listSync)
                {


                    Inventory inventory=new Inventory();
                    inventory.setDefaultValue();


                    //存貨编码取大类最大值递增
                    LambdaQueryWrapper<Inventory> selectInv=new LambdaQueryWrapper<>();
                    selectInv.eq(Inventory::getCinvccode, q.getField0022());
                    selectInv.apply(" cInvCode like '"+ q.getField0022()+"%'");
                    selectInv.orderByDesc(Inventory::getCinvcode);
                    List<Inventory> listInv=inventoryMapper.selectList(selectInv);
                    if(listInv==null||listInv.size()==0)
                    {
                        inventory.setCinvcode(q.getField0022()+"00001");
                    }
                    else
                    {
                        String strNum=listInv.get(0).getCinvcode().substring(q.getField0022().length(),listInv.get(0).getCinvcode().length());
                        Integer num=Integer.valueOf(strNum);
                        num=num+1;
                        String sortNum = String.valueOf(num);
                        while (sortNum.length() < 4) {
                            sortNum = "0" + sortNum;
                        }

                        inventory.setCinvcode(q.getField0022()+sortNum);

                    }

                    if(CustomStringUtils.isNotBlank(q.getMclass())&&q.getMclass().equals("不锈钢原材料"))
                    {
                        inventory.setCinvname(q.getField0006()+"["+q.getSurfaceMethod()+"]");
                    }
                    else
                    {
                        inventory.setCinvname(q.getField0006());
                    }

                    inventory.setCinvstd(q.getField0007());
                    inventory.setCinvccode(q.getField0022());
                    //国标号
                    inventory.setCinvdefine4(q.getField0009());
                    //技术部材料
                    inventory.setCinvdefine7(q.getField0010());
                    //技术部图名
                    inventory.setCinvdefine9(q.getField0008());

                    //品牌
                    inventory.setCaddress(q.getField0024());

                    inventory.setCquality(q.getSurfaceMethod());


                    if(CustomStringUtils.isBlank(q.getField0012()))
                    {
                        throw new Exception("计量单位不存在！");
                    }

                    //第一种情况只填主计量单位
                    //第二种情况2个都填
                    if(CustomStringUtils.isBlank(q.getField0013()))
                    {
                        LambdaQueryWrapper<ComputationGroup> selectGroup=new LambdaQueryWrapper<>();
                        selectGroup.eq(ComputationGroup::getIgrouptype, 0);
                        List<ComputationGroup> listUnitGroup=unitGroupMapper.selectList(selectGroup);
                        if(listUnitGroup==null||listUnitGroup.size()==0)
                        {
                            throw new Exception("计量单位组不存在！");
                        }

                        LambdaQueryWrapper<ComputationUnit> select=new LambdaQueryWrapper<>();
                        select.eq(ComputationUnit::getCcomunitname, q.getField0012());
                        select.apply(" cGroupCode  in (select cGroupCode from  ComputationGroup where iGroupType=0) ");
                        List<ComputationUnit> listUnit=unitMapper.selectList(select);
                        //存在计量单位的取值，否则新增
                        if(listUnit!=null&&listUnit.size()>0)
                        {
                            inventory.setCcomunitcode(listUnit.get(0).getCcomunitcode());
                            inventory.setCgroupcode(listUnit.get(0).getCgroupcode());
                            inventory.setIgrouptype(listUnitGroup.get(0).getIgrouptype());
                        }
                        else
                        {
                            throw new Exception("计量单位不存在！");
                        }
                    }
                    else
                    {
                        //查询计量单位组是否存在
                        String unitGroup=q.getField0013() +"["+q.getField0014().stripTrailingZeros().toPlainString()+q.getField0012()+"-"+q.getField0013()+"]";
                        LambdaQueryWrapper<ComputationGroup> selectGroup=new LambdaQueryWrapper<>();
                        selectGroup.eq(ComputationGroup::getIgrouptype, 1);
                        selectGroup.eq(ComputationGroup::getCgroupname,unitGroup);
                        List<ComputationGroup> listUnitGroup=unitGroupMapper.selectList(selectGroup);
                        if(listUnitGroup!=null&&listUnitGroup.size()>0)
                        {
                            inventory.setCgroupcode(listUnitGroup.get(0).getCgroupcode());
                            inventory.setIgrouptype(listUnitGroup.get(0).getIgrouptype());

                            //查询计量单位，bMainUnit=1,是主计量，否则是福计量
                            LambdaQueryWrapper<ComputationUnit> select=new LambdaQueryWrapper<>();
                            select.eq(ComputationUnit::getCgroupcode, listUnitGroup.get(0).getCgroupcode());
                            List<ComputationUnit> listUnit=unitMapper.selectList(select);
                            for(ComputationUnit computationUnit:listUnit)
                            {
                                if(computationUnit.getBmainunit()==true)
                                {
                                    inventory.setCcomunitcode(computationUnit.getCcomunitcode());
                                }
                                else
                                {
                                    inventory.setCsacomunitcode(computationUnit.getCcomunitcode());
                                    inventory.setCpucomunitcode(computationUnit.getCcomunitcode());
                                    inventory.setCstcomunitcode(computationUnit.getCcomunitcode());
                                    inventory.setCcacomunitcode(computationUnit.getCcomunitcode());
                                    inventory.setCproductunit(computationUnit.getCcomunitcode());
                                    inventory.setCshopunit(computationUnit.getCcomunitcode());
                                }
                            }
                        }
                        else
                        {
                            //查询最大编号的计量单位组，+1
                            ComputationGroup newGroup=new ComputationGroup();

                            LambdaQueryWrapper<ComputationGroup> selectGroup1=new LambdaQueryWrapper<>();
                            selectGroup1.eq(ComputationGroup::getIgrouptype, 1);
                            selectGroup1.orderByDesc(ComputationGroup::getCgroupcode);
                            listUnitGroup=unitGroupMapper.selectList(selectGroup1);
                            if(listUnitGroup==null||listUnitGroup.size()==0)
                            {
                                newGroup.setCgroupcode("1001");
                            }
                            else
                            {
                                newGroup.setCgroupcode(String.valueOf(Integer.valueOf(listUnitGroup.get(0).getCgroupcode())+1));
                            }
                            newGroup.setCgroupname(unitGroup);
                            newGroup.setIgrouptype(Byte.valueOf("1"));
                            newGroup.setBdefaultgroup(false);
                            //插入计量单位组
                            unitGroupMapper.insert(newGroup);

                            //插入主计量
                            ComputationUnit unitMain=new ComputationUnit();
                            unitMain.setCcomunitcode(newGroup.getCgroupcode()+"01");
                            unitMain.setCcomunitname(q.getField0012());
                            unitMain.setCgroupcode(newGroup.getCgroupcode());
                            unitMain.setBmainunit(true);
                            unitMain.setIchangrate(BigDecimal.ONE);
                            unitMapper.insert(unitMain);
                            //插入辅计量
                            ComputationUnit unitAss=new ComputationUnit();
                            unitAss.setCcomunitcode(newGroup.getCgroupcode()+"02");
                            unitAss.setCcomunitname(q.getField0013());
                            unitAss.setCgroupcode(newGroup.getCgroupcode());
                            unitAss.setBmainunit(false);
                            unitAss.setIchangrate(q.getField0014());
                            unitMapper.insert(unitAss);

                            //存货档案计量单位
                            inventory.setCgroupcode(newGroup.getCgroupcode());
                            inventory.setIgrouptype(newGroup.getIgrouptype());
                            inventory.setCcomunitcode(unitMain.getCcomunitcode());
                            inventory.setCsacomunitcode(unitAss.getCcomunitcode());
                            inventory.setCpucomunitcode(unitAss.getCcomunitcode());
                            inventory.setCstcomunitcode(unitAss.getCcomunitcode());
                            inventory.setCcacomunitcode(unitAss.getCcomunitcode());
                            inventory.setCproductunit(unitAss.getCcomunitcode());
                            inventory.setCshopunit(unitAss.getCcomunitcode());

                        }

                    }
                    //是否外购
                    if(CustomStringUtils.isNotBlank(q.getIzPo())&&q.getIzPo().equals("是"))
                    {
                        inventory.setBpurchase(true);
                        inventory.setBcomsume(true);
                        inventory.setIplandefault(Short.valueOf("3"));
                        inventory.setBbomsub(true);
                    }
                    //是否委外
                    if(CustomStringUtils.isNotBlank(q.getIzForeign())&&q.getIzForeign().equals("是"))
                    {
                        inventory.setBforeexpland(true);
                        inventory.setBcomsume(true);
                        inventory.setIplandefault(Short.valueOf("3"));
                        inventory.setBbomsub(true);
                        inventory.setBbommain(true);
                    }
                    //是否自制
                    if(CustomStringUtils.isNotBlank(q.getIzSelf())&&q.getIzSelf().equals("是"))
                    {
                        inventory.setBself(true);
                        inventory.setIplandefault(Short.valueOf("1"));
                        inventory.setBcomsume(true);
                        inventory.setBbommain(true);
                        inventory.setBbomsub(true);
                        inventory.setBproducing(true);
                        inventory.setBproductbill(true);
                    }
                    //是否销售
                    if(CustomStringUtils.isNotBlank(q.getIzSale())&&q.getIzSale().equals("是"))
                    {
                        inventory.setBsale(true);

                    }
                    //判断是否启动批次
                    if(inventory.getCinvccode().startsWith("101")||inventory.getCinvccode().startsWith("102")||inventory.getCinvccode().startsWith("10301"))
                    {
                        inventory.setBinvbatch(true);
                    }


                    //插入存货档案
                    inventoryMapper.insert(inventory);

                    //插入存货自定义项目表
                    InventoryExtradefine inventoryExtradefine=new InventoryExtradefine();
                    inventoryExtradefine.setCinvcode(inventory.getCinvcode());
                    inventoryExtradefineMapper.insert(inventoryExtradefine);

                    //插入存货下属表
                    InventorySub inventorySub=new InventorySub();
                    inventorySub.setDefaultValue();
                    inventorySub.setCinvsubcode(inventory.getCinvcode());
                    inventorySubMapper.insert(inventorySub);

                    BasPart basPart=new BasPart();
                    basPart.setDefaultValue();
                    Integer u8fid = null;
                    u8fid=u8SystemUtils.getId(accId,"bas_part",10);
                    basPart.setPartid(u8fid);
                    basPart.setInvcode(inventory.getCinvcode());
                    basPart.setLlc(0);
                    basPart.setIsurenesstype(Short.valueOf("1"));
                    basPartMapper.insert(basPart);


                    //更新存货信息到oa
                    //设置存货编码
                    q.setField0015(inventory.getCinvcode());
                    oaInventoryApplyDetailMapper.updateInvCode(q,ParamUtil.getParam("oaDatabase").toString());
                }
            }

        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }

}
