package com.web.basicinfo.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.util.CustomStringUtils;
import com.common.util.R;
import com.common.util.StringUtils;
import com.web.basicinfo.entity.*;
import com.web.basicinfo.mapper.*;
import com.web.basicinfo.service.IComputationUnitService;
import com.web.basicinfo.service.IInventoryClassService;
import com.web.basicinfo.service.IInventoryService;
import com.web.mo.dto.BomDTO;
import com.web.u8system.util.U8SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @author caihuan
 * @since 2022-06-28
 */
@Service
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, Inventory> implements IInventoryService {

    @Autowired
    InventoryMapper inventoryMapper;
    @Autowired
    private IInventoryClassService inventoryClassService;
    @Autowired
    private IComputationUnitService computationUnitService;
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

    @Override
    public IPage<Inventory> getList(Inventory mainDTO, IPage<Inventory> page) throws Exception {
        page.setRecords(inventoryMapper.getList(page,mainDTO));
        return page;
    }

    @Override
    public Inventory getInfoById(String cinvcode) throws Exception{
        Inventory inventory= inventoryMapper.getInfoById(cinvcode);
        if(CustomStringUtils.isNotBlank(inventory))
        {
            inventory.setIinvncost(Double.valueOf(getPrice(cinvcode).toString()));
            return inventory;
        }
        else
        {
            return null;
        }
    }

    //获取委外单价
    @Override
    public BigDecimal getPrice(String cinvcode) throws Exception{
        Inventory inventoryFirst= inventoryMapper.getPriceFirst(cinvcode);
        //判断是否存在单价数据
        if(CustomStringUtils.isNotBlank(inventoryFirst)) {
            if(inventoryFirst!=null&&inventoryFirst.getIinvncost()!=null)
            {
                return BigDecimal.valueOf(inventoryFirst.getIinvncost());
            }
        }
        Inventory inventory= inventoryMapper.getPrice(cinvcode);
        //判断是否存在单价数据
        if(CustomStringUtils.isNotBlank(inventory))
        {
            //金额不存在的则查询存货档案价格
            if(inventory.getIinvncost()!=null)
            {
                return BigDecimal.valueOf(inventory.getIinvncost());
            }
            else
            {
                inventory= inventoryMapper.getInfoById(cinvcode);
                if(inventory!=null&&inventory.getIinvncost()!=null)
                {
                    return BigDecimal.valueOf(inventory.getIinvncost());
                }
                else{
                    return BigDecimal.ZERO;
                }
            }
        }
        else
        {
            inventory= inventoryMapper.getInfoById(cinvcode);
            if(inventory!=null&&inventory.getIinvncost()!=null)
            {
                return BigDecimal.valueOf(inventory.getIinvncost());
            }
            else{
                return BigDecimal.ZERO;
            }
        }
    }

    //查找所有菜单
    @Override
    public List<Inventory> getMenuList() {
        List<Inventory> parentMenuList = new ArrayList<>();
        List<Inventory> childMenuList = inventoryMapper.selectByMenu("");
        List<Inventory> newList1 = childMenuList.stream()
                .collect(
                        Collectors.collectingAndThen(
                                Collectors.toCollection(() -> new TreeSet<>(
                                        Comparator.comparing(o -> o.getParentCode())
                                )), ArrayList::new
                        )
                );
        List<Inventory> newList=JSON.parseArray(JSON.toJSONString(newList1),Inventory.class);
        for (Inventory parentMenu:newList)
        {
            List<Inventory>  list1 = childMenuList.stream().filter(g -> g.getParentCode().equals(parentMenu.getParentCode())).collect(Collectors.toList());
            for (Inventory menu : list1) {

                menu.setNodes(null);
                menu.setText(menu.getCinvcname());
                menu.setColor("#0366d6");
                menu.setChildren(null);
                menu.setElement("HTMLOptionElement");
            }

            parentMenu.setCinvccode(parentMenu.getParentCode());
            parentMenu.setCinvcname(parentMenu.getParentName());

            parentMenu.setNodes(list1);
            parentMenu.setText(parentMenu.getParentName());
            parentMenu.setColor("#0366d6");
            parentMenu.setChildren(list1);
            parentMenu.setElement("HTMLOptionElement");
            parentMenuList.add(parentMenu);
        }


        return parentMenuList;
    }

//    //查找所有菜单
//    @Override
//    public List<Inventory> getMenuList() {
//        List<Inventory> parentMenuList = inventoryMapper.selectParentMenuList();//父菜单
//        for (Inventory parentMenu : parentMenuList) {
//            String id = parentMenu.getCinvccode();
//            List<Inventory> childMenuList = getChildMenuList(id);
//            parentMenu.setNodes(childMenuList);
//            parentMenu.setText(parentMenu.getCinvcname());
//            parentMenu.setColor("#0366d6");
//            parentMenu.setChildren(childMenuList);
//            parentMenu.setElement("HTMLOptionElement");
//        }
//        return parentMenuList;
//    }
//
//    //查找子菜单，以及子菜单下的子菜单
//    public List<Inventory> getChildMenuList(String id) {
//        List<Inventory> childMenuList = inventoryMapper.selectByMenu(id);
//        for (Inventory menu : childMenuList) {
//            List<Inventory> childMenuList2 = getChildMenuList(menu.getCinvccode());
//            menu.setNodes(childMenuList2);
//            menu.setText(menu.getCinvcname());
//            menu.setColor("#0366d6");
//            menu.setChildren(childMenuList2);
//            menu.setElement("HTMLOptionElement");
//        }
//        return childMenuList;
//    }


    //region  导入保存

    @Transactional(rollbackFor = Exception.class)
    @Override
    public R saveImport( List<Inventory> list){
        try{
            //1. 检查 物料编码是否存在，  大类是否存在   计量单位编码 是否存在
            int rowNo=1;

            for (Inventory detail : list) {
                int n1=  this.count(Wrappers.<Inventory>lambdaQuery().eq(Inventory::getCinvaddcode,detail.getCinvcode()));
                if(n1>0){
                    //throw new Exception("第"+rowNo+"行，存货编码已存在！");
                    detail.setImportStatus("已存在");
                    continue;
                }
                int n=  this.count(Wrappers.<Inventory>lambdaQuery().eq(Inventory::getCinvcode,detail.getCinvcode()));
                if(n>0){
                    //throw new Exception("第"+rowNo+"行，存货编码已存在！");
                    detail.setImportStatus("已存在");
                    continue;
                }
                //查询计量单位组是否存在


                List<ComputationGroup> listUnitGroup=unitGroupMapper.selectList(Wrappers.<ComputationGroup>lambdaQuery()
                        .eq(ComputationGroup::getCgroupcode,detail.getCgroupcode()));
                if(listUnitGroup.size()<=0){
                    throw new Exception("第"+rowNo+"行，计量单位组不存在！");
                }

                int n2= inventoryClassService.count(Wrappers.<InventoryClass>lambdaQuery().eq(InventoryClass::getCInvCCode,detail.getCinvccode()));
                int n3= computationUnitService.count(Wrappers.<ComputationUnit>lambdaQuery().eq(ComputationUnit::getCcomunitcode,detail.getCcomunitcode()));
                if(n2<=0){
                    throw new Exception("第"+rowNo+"行，存货大类不存在！");
                }
                if(n3<=0){
                    throw new Exception("第"+rowNo+"行，计量单位编码不存在！");
                }
                //设置默认值
                detail.setImportValue();
                //是否外购
                if(detail.getBpurchase()==true)
                {

                    detail.setBcomsume(true);
                    detail.setIplandefault(Short.valueOf("3"));
                    detail.setBbomsub(true);
                }
                //是否自制
                if(detail.getBself())
                {

                    detail.setIplandefault(Short.valueOf("1"));
                    detail.setBcomsume(true);
                    detail.setBbommain(true);
                    detail.setBbomsub(true);
                    detail.setBproducing(true);
                    detail.setBproductbill(true);
                }

                //存货代码2开头的单独设置属性
                if(detail.getCinvccode().startsWith("2"))
                {
                    detail.setBpurchase(false);
                    detail.setBsale(true);
                    detail.setBself(true);
                    detail.setBproxyforeign(false);
                    detail.setBcomsume(true);
                    detail.setBproducing(true);
                    detail.setIplandefault(Short.valueOf("1"));
                    detail.setBbommain(true);
                    detail.setBbomsub(true);
                    detail.setBproductbill(true);
                }

                //插入存货自定义项目表
              /*  InventoryExtradefine inventoryExtradefine=new InventoryExtradefine();
                inventoryExtradefine.setCinvcode(detail.getCinvcode());
                int n4= inventoryExtradefineMapper.insert(inventoryExtradefine);
*/
                //插入存货下属表
                InventorySub inventorySub=new InventorySub();
                inventorySub.setDefaultValue();
                inventorySub.setCinvsubcode(detail.getCinvcode());
                int n5= inventorySubMapper.insert(inventorySub);

                int nPart=    basPartMapper.selectCount(Wrappers.<BasPart>lambdaQuery().eq(BasPart::getInvcode,detail.getCinvcode()));
                if(nPart>0){
                    throw new Exception("第"+rowNo+"行，BasPart物料编码已存在！");
                }
                BasPart basPart=new BasPart();
                basPart.setDefaultValue();
                Integer u8fid = null;
                u8fid=u8SystemUtils.getId(accId,"bas_part",10);
                basPart.setPartid(u8fid);
                basPart.setInvcode(detail.getCinvcode());
                basPart.setLlc(0);
                basPart.setIsurenesstype(Short.valueOf("1"));
                int n6=  basPartMapper.insert(basPart);
                //插入存货档案
                int n7=  inventoryMapper.insert(detail);
                if(n7>0){
                    detail.setImportStatus("导入成功");
                }else{
                    detail.setImportStatus("导入失败");
                }

                rowNo+=1;
            }

            return  R.ok(list);


        }catch (Exception e){
            return R.error(201,"保存异常，请重试："+e.getMessage());
        }
    }
    //endregion
}
