package com.web.mo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.util.*;
import com.modules.security.util.SecurityUtil;
import com.util.EnumUtils;
import com.web.basicinfo.entity.BasPart;
import com.web.basicinfo.entity.ComputationUnit;
import com.web.basicinfo.entity.Inventory;
import com.web.basicinfo.entity.InventorySub;
import com.web.basicinfo.mapper.BasPartMapper;
import com.web.basicinfo.mapper.ComputationUnitMapper;
import com.web.basicinfo.mapper.InventoryMapper;
import com.web.basicinfo.mapper.InventorySubMapper;
import com.web.mo.dto.BomDTO;
import com.web.mo.entity.*;
import com.web.mo.mapper.*;
import com.web.mo.service.*;
import com.web.po.entity.PoDetails;
import com.web.po.mapper.PoDetailsMapper;
import com.web.st.entity.CurrentStock;
import com.web.st.mapper.CurrentStockMapper;
import com.web.system.entity.User;
import com.web.u8system.util.U8SystemUtils;
import org.springframework.beans.BeanUtils;
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
public class BomServiceImpl extends ServiceImpl<BomMapper, Bom> implements IBomService {

    @Autowired
    private U8SystemUtils u8SystemUtils;
    @Autowired
    private BomMapper bomMapper;
    @Autowired
    private BomOpcomponentMapper bomOpcomponentMapper;
    @Autowired
    private BomOpcomponentoptMapper bomOpcomponentoptMapper;
    @Autowired
    private InventoryMapper inventoryMapper;
    @Autowired
    private InventorySubMapper inventorySubMapper;
    @Autowired
    private BasPartMapper basPartMapper;
    @Autowired
    private BomParentMapper bomParentMapper;
    @Autowired
    private IBomParentService bomParentService;
    @Autowired
    private IBomOpcomponentoptService bomOpcomponentoptService;
    @Autowired
    private IBomOpcomponentService bomOpcomponentService;

    @Autowired
    private ComputationUnitMapper computationUnitMapper;
    @Value("${account.u8DB}")
    private String u8DB;
    @Value("${account.acountId}")
    private String accId;

    @Override
    public IPage<BomDTO> getList(BomDTO mainDTO, IPage<BomDTO> page) throws Exception {
        page.setRecords(bomMapper.getList(page,mainDTO));
        return page;
    }

    /**
     * ??????BOM???????????????
     * @param invCode
     * @return
     * @throws Exception
     */
    @Override
    public List<BomDTO> getBomListByParent(String invCode) throws Exception {
        List<BomDTO> list=new ArrayList<>();
        List<BomDTO> parentMenuList = new ArrayList<>();
        parentMenuList=bomMapper.selectParentMenuListByInv(invCode,u8DB);//?????????

        for (BomDTO parentMenu : parentMenuList) {
            int menuLevel = 0;
            parentMenu.setId(String.valueOf(SnowFlakeUtils.getFlowIdInstance().nextId()));
            parentMenu.setExpanded(true);
            parentMenu.setLoaded(true);
            parentMenu.setMenulevel(menuLevel);
            list.add(parentMenu);
            list = getChildMenuList(parentMenu,parentMenu.getInvCode(), list, menuLevel );
        }

        return list;
    }


    @Override
    public List<BomDTO> selectByMenu(String invCode) throws Exception {
        List<BomDTO> childMenuList = bomMapper.selectByMenu1(invCode,u8DB);
        return childMenuList;
    }
    @Override
    public List<BomDTO> selectParentMenuList(String invCode) throws Exception {
        List<BomDTO> childMenuList = bomMapper.selectParentMenuListByInv(invCode,u8DB);
        return childMenuList;
    }




    //????????????????????????????????????????????????
    public List<BomDTO> getChildMenuList(BomDTO parentMenu,String invCode, List<BomDTO> list, int menuLevel ) {


            List<BomDTO> childMenuList = bomMapper.selectByMenu1(invCode,u8DB);
            menuLevel++;
            if (childMenuList != null) {

                if (childMenuList.size() > 0) {


                    parentMenu.setIsleaf(false);
                } else {
                    if (menuLevel == 1) {
                        parentMenu.setIsleaf(false);
                    } else {
                        parentMenu.setIsleaf(true);
                    }
                }
            } else {
                parentMenu.setIsleaf(true);
            }

            if(childMenuList!=null&&childMenuList.size()>0)
            {
                parentMenu.setBomId(childMenuList.get(0).getBomId());
            }
            else
            {
                parentMenu.setBomId(null);
            }
            for (BomDTO menu : childMenuList) {

                BomDTO bomDTO=new BomDTO();
                BeanUtils.copyProperties(menu, bomDTO);
                bomDTO.setId(String.valueOf(SnowFlakeUtils.getFlowIdInstance().nextId()));
                bomDTO.setExpanded(true);
                bomDTO.setLoaded(true);
                bomDTO.setMenulevel(menuLevel);
                bomDTO.setParentId(parentMenu.getId());
                list.add(bomDTO);

                list = getChildMenuList(bomDTO, bomDTO.getInvCode(), list,menuLevel);

            }


        return list;
    }


    /**
     * ??????BOM???????????????
     * @param invCode
     * @return
     * @throws Exception
     */
    @Override
    public List<BomDTO> getInvChildListFirst(String invCode) throws Exception {
        List<BomDTO> list = bomMapper.selectByMenu1(invCode,u8DB);
        return list;
    }



    /**
     * ??????bom
     * @param main
     * @param list

     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult save(BomDTO main, List<BomDTO> list) {
        ResponseResult result = new ResponseResult();
        try{

            Bom bom=null;
            //region ????????????
            if(CustomStringUtils.isBlank(main.getBomId())) {
                LambdaQueryWrapper<Bom> select = new LambdaQueryWrapper<>();
                select.apply(" bomid in (select bomid from bom_parent t left join bas_part t1 on t.parentId=t1.partId where t1.invCode='"+main.getInvCode()+"')");
                List<Bom> boms = bomMapper.selectList(select);
                if(boms!=null&&boms.size()>0)
                {
                    bom=boms.get(0);
                }
            }
            else {
                Bom m=bomMapper.selectById(main.getBomId());
                if(m!=null) {
                    bom=m;
                }
            }

            //??????????????????
            if(CustomStringUtils.isNotBlank(main.getCinvdefine3()))
            {
                Inventory inventory=new Inventory();
                inventory.setCinvcode(main.getInvCode());
                inventory.setCinvdefine3(main.getCinvdefine3());
                if(main.getCinvdefine3().equals("??????")||main.getCinvdefine3().equals("??????"))
                {
                    inventory.setBproxyforeign(true);
                    inventory.setBpurchase(false);
                    inventory.setBself(false);

                    inventory.setBproductbill(true);
                    inventory.setBbommain(true);
                    inventory.setBbomsub(true);
                    inventory.setBcomsume(true);
                    inventory.setBproducing(true);
                    inventory.setIplandefault(Short.valueOf("2"));
                    int n= inventoryMapper.updateById(inventory);
                    if(n<=0)
                    {
                        throw new  Exception("???????????????????????????");
                    }

                }
                else if(main.getCinvdefine3().equals("??????")||main.getCinvdefine3().equals("?????????"))
                {
                    inventory.setBpurchase(true);
                    inventory.setBproxyforeign(false);
                    inventory.setBself(false);

                    inventory.setBproductbill(false);
                    inventory.setBbommain(false);
                    inventory.setBbomsub(true);
                    inventory.setIplandefault(Short.valueOf("3"));
                    inventory.setBcomsume(true);
                    inventory.setBproducing(false);
                    int n= inventoryMapper.updateById(inventory);
                    if(n<=0)
                    {
                        throw new  Exception("???????????????????????????");
                    }
                }

                else
                {
                    inventory.setBpurchase(false);
                    inventory.setBproxyforeign(false);
                    inventory.setBself(true);

                    inventory.setBproductbill(true);
                    inventory.setBbommain(true);
                    inventory.setBbomsub(true);
                    inventory.setIplandefault(Short.valueOf("1"));
                    inventory.setBcomsume(true);
                    inventory.setBproducing(true);
                    int n= inventoryMapper.updateById(inventory);
                    if(n<=0)
                    {
                        throw new  Exception("???????????????????????????");
                    }

                }


            }
            else
            {
                Inventory inventory=new Inventory();
                inventory.setCinvcode(main.getInvCode());
                inventory.setCinvdefine3("");
                inventory.setBpurchase(false);
                inventory.setBproxyforeign(false);
                inventory.setBself(true);

                inventory.setBproductbill(true);
                inventory.setBbommain(true);
                inventory.setBbomsub(true);
                inventory.setIplandefault(Short.valueOf("1"));
                inventory.setBcomsume(true);
                inventory.setBproducing(true);
                //?????????????????????
                int n= inventoryMapper.updateById(inventory);
                if(n<=0)
                {
                    throw new  Exception("???????????????????????????");
                }
            }


            if(bom==null) {
                bom=new Bom();
                bom.setDefValue();

                Integer u8fid_bom = null;
                //??????U8Bom???id
                u8fid_bom=u8SystemUtils.getId(accId,"bom_bom",1);
                bom.setBomid(u8fid_bom);
                bom.setCreateuser(SecurityUtil.getUser().getMyusername());
                bom.setRelsuser(SecurityUtil.getUser().getMyusername());
                main.setBomId(u8fid_bom);

                //bom??????????????????bom???1
                bom.setBomtype(Byte.valueOf("1"));
                bom.setVersion(10);
                bom.setVersiondesc(main.getInvCode());

                bom.setVtid(30442);

                //??????bom???
                bomMapper.insert(bom);

                //?????????????????????
                LambdaQueryWrapper<BasPart> select = new LambdaQueryWrapper<>();
                select.eq(BasPart::getInvcode,main.getInvCode());
                List<BasPart> basParts = basPartMapper.selectList(select);
                if(basParts==null||basParts.size()==0)
                {
                    throw new Exception("??????????????????????????????");
                }

                //bom??????????????????
                BomParent bomParent=new BomParent();
                bomParent.setAutoid(UUID.randomUUID().toString());
                bomParent.setBomid(bom.getBomid());
                bomParent.setParentid(basParts.get(0).getPartid());
                bomParent.setParentscrap(BigDecimal.ZERO);
                bomParent.setSharingpartid(0);
                bomParentMapper.insert(bomParent);
            }

            //??????????????????
            int row=10;
            for(BomDTO bomDTO:list)
            {

                if(CustomStringUtils.isBlank(bomDTO.getDefine23()))
                {
                    bomDTO.setDefine23("");
                }
                if(CustomStringUtils.isBlank(bomDTO.getCinvdefine2()))
                {
                    bomDTO.setCinvdefine2("");
                }
                if(CustomStringUtils.isBlank(bomDTO.getInvCode()))
                {
                    throw new  Exception("????????????????????????");
                }
                if(CustomStringUtils.isBlank(bomDTO.getBaseQtyN()))
                {
                    throw new  Exception("????????????????????????");
                }
                if(bomDTO.getBaseQtyN().compareTo(BigDecimal.ZERO)<=0)
                {
                    throw new  Exception("????????????????????????0???");
                }
                if(CustomStringUtils.isBlank(bomDTO.getBaseQtyD()))
                {
                    throw new  Exception("????????????????????????");
                }
                if(bomDTO.getBaseQtyD().compareTo(BigDecimal.ZERO)<=0)
                {
                    throw new  Exception("????????????????????????0???");
                }
                if(CustomStringUtils.isBlank(bomDTO.getCwhcode()))
                {
                    throw new  Exception("??????????????????");
                }

                String strSize="";
                //??????
                if(CustomStringUtils.isNotBlank(bomDTO.getDefine28()))
                {
                    strSize="??"+bomDTO.getDefine28();
                }
                else
                {
                    bomDTO.setDefine28("");
                }
                //???
                if(CustomStringUtils.isNotBlank(bomDTO.getDefine29()))
                {
                    if(CustomStringUtils.isNotBlank(strSize))
                    {
                        strSize=strSize+",L"+bomDTO.getDefine29();
                    }
                    else
                    {
                        strSize=strSize+"L"+bomDTO.getDefine29();
                    }

                }
                else
                {
                    bomDTO.setDefine29("");
                }
                //???
                if(CustomStringUtils.isNotBlank(bomDTO.getDefine30()))
                {
                    if(CustomStringUtils.isNotBlank(strSize))
                    {
                        strSize=strSize+",W"+bomDTO.getDefine30();
                    }
                    else
                    {
                        strSize=strSize+"W"+bomDTO.getDefine30();
                    }

                }
                else
                {
                    bomDTO.setDefine30("");
                }
                //??????
                if(CustomStringUtils.isNotBlank(bomDTO.getDefine31()))
                {
                    if(CustomStringUtils.isNotBlank(strSize))
                    {
                        strSize=strSize+",?????"+bomDTO.getDefine31();
                    }
                    else
                    {
                        strSize=strSize+"?????"+bomDTO.getDefine31();
                    }

                }
                else
                {
                    bomDTO.setDefine31("");
                }
                //??????
                if(CustomStringUtils.isNotBlank(bomDTO.getDefine32()))
                {
                    if(CustomStringUtils.isNotBlank(strSize))
                    {
                        strSize=strSize+",?????"+bomDTO.getDefine32();
                    }
                    else
                    {
                        strSize=strSize+"?????"+bomDTO.getDefine32();
                    }

                }
                else
                {
                    bomDTO.setDefine32("");
                }

                bomDTO.setDefine22(strSize);



                //??????????????????
                if(CustomStringUtils.isNotBlank(bomDTO.getCinvdefine3()))
                {
                    Inventory inventory=new Inventory();
                    inventory.setCinvcode(bomDTO.getInvCode());
                    inventory.setCinvdefine3(bomDTO.getCinvdefine3());
                    if(bomDTO.getCinvdefine3().equals("??????")||bomDTO.getCinvdefine3().equals("??????")||bomDTO.getCinvdefine3().equals("????????????")||bomDTO.getCinvdefine3().equals("???????????????")||bomDTO.getCinvdefine3().equals("???????????????"))
                    {
                        inventory.setBproxyforeign(true);
                        inventory.setBpurchase(false);
                        inventory.setBself(false);

                        inventory.setBproductbill(true);
                        inventory.setBbommain(true);
                        inventory.setBbomsub(true);
                        inventory.setBcomsume(true);
                        inventory.setBproducing(true);
                        inventory.setIplandefault(Short.valueOf("2"));
                        int n= inventoryMapper.updateById(inventory);
                        if(n<=0)
                        {
                            throw new  Exception("???????????????????????????");
                        }
                    }
                    else if(bomDTO.getCinvdefine3().equals("??????")||bomDTO.getCinvdefine3().equals("?????????"))
                    {
                        inventory.setBpurchase(true);
                        inventory.setBproxyforeign(false);
                        inventory.setBself(false);

                        inventory.setBproductbill(false);
                        inventory.setBbommain(false);
                        inventory.setBbomsub(true);
                        inventory.setIplandefault(Short.valueOf("3"));
                        inventory.setBcomsume(true);
                        inventory.setBproducing(false);
                        int n= inventoryMapper.updateById(inventory);
                        if(n<=0)
                        {
                            throw new  Exception("???????????????????????????");
                        }
                    }

                    else
                    {
                        inventory.setBpurchase(false);
                        inventory.setBproxyforeign(false);
                        inventory.setBself(true);

                        inventory.setBproductbill(true);
                        inventory.setBbommain(true);
                        inventory.setBbomsub(true);
                        inventory.setIplandefault(Short.valueOf("1"));
                        inventory.setBcomsume(true);
                        inventory.setBproducing(true);
                        int n= inventoryMapper.updateById(inventory);
                        if(n<=0)
                        {
                            throw new  Exception("???????????????????????????");
                        }

                    }


                }
                else
                {
                    Inventory inventory=new Inventory();
                    inventory.setCinvcode(bomDTO.getInvCode());
                    inventory.setCinvdefine3("");
                    inventory.setBpurchase(false);
                    inventory.setBproxyforeign(false);
                    inventory.setBself(true);

                    inventory.setBproductbill(true);
                    inventory.setBbommain(true);
                    inventory.setBbomsub(true);
                    inventory.setIplandefault(Short.valueOf("1"));
                    inventory.setBcomsume(true);
                    inventory.setBproducing(true);
                    //?????????????????????
                    int n= inventoryMapper.updateById(inventory);
                    if(n<=0)
                    {
                        throw new  Exception("???????????????????????????");
                    }
                }


                if(CustomStringUtils.isBlank(bomDTO.getOpComponentId()))
                {
                    if(CustomStringUtils.isBlank(bomDTO.getInvCode()))
                    {
                        throw new Exception("????????????????????????");
                    }
                    if(bomDTO.getInvCode().equals(main.getInvCode()))
                    {
                        throw new Exception("??????????????????????????????????????????");
                    }
                    //??????????????????
                    Inventory inv= inventoryMapper.selectById(bomDTO.getInvCode());
                    if(inv==null)
                    {
                        throw new Exception("????????????:"+bomDTO.getInvCode()+"??????????????????");
                    }

                    //????????????????????????
                    InventorySub invSub= inventorySubMapper.selectById(bomDTO.getInvCode());
                    if(invSub==null)
                    {
                        throw new Exception("????????????1:"+bomDTO.getInvCode()+"??????????????????");
                    }

                    //?????????????????????
                    ComputationUnit unit=null;
                    if(CustomStringUtils.isNotBlank(inv.getCpucomunitcode()))
                    {
                        unit=computationUnitMapper.selectById(inv.getCpucomunitcode());
                        if(unit==null)
                        {
                            throw new Exception("????????????:"+bomDTO.getInvCode()+"?????????????????????????????????");
                        }
                    }
                    //?????????????????????
                    LambdaQueryWrapper<BasPart> selectInv = new LambdaQueryWrapper<>();
                    selectInv.eq(BasPart::getInvcode,bomDTO.getInvCode());
                    List<BasPart> basPartsInv = basPartMapper.selectList(selectInv);
                    if(basPartsInv==null||basPartsInv.size()==0)
                    {
                        throw new Exception("????????????:"+bomDTO.getInvCode()+"????????????????????????");
                    }


                    Integer u8cid_bom_opcomponent = null;
                    Integer u8cid_bom_opcomponentopt = null;
                    //??????U8?????????id
                    u8cid_bom_opcomponent=u8SystemUtils.getChildId(accId,"bom_opcomponent",10,1);

                    //??????U8?????????id
                    u8cid_bom_opcomponentopt=u8SystemUtils.getChildId(accId,"bom_opcomponentopt",10,1);



                    //????????????????????????1
                    BomOpcomponentopt bomOpcomponentopt=new BomOpcomponentopt();
                    bomOpcomponentopt.setDefValue();
                    bomOpcomponentopt.setOptionsid(u8cid_bom_opcomponentopt);
                    bomOpcomponentopt.setOffset(0);
                    bomOpcomponentopt.setWiptype(bomDTO.getWipType());
                    bomOpcomponentopt.setWhcode(bomDTO.getCwhcode());
                    bomOpcomponentoptMapper.insert(bomOpcomponentopt);


                    //????????????????????????
                    BomOpcomponent bomOpcomponent=new BomOpcomponent();
                    bomOpcomponent.setDefValue();
                    bomOpcomponent.setOpcomponentid(u8cid_bom_opcomponent);
                    bomOpcomponent.setBomid(bom.getBomid());
                    bomOpcomponent.setSortseq(row);
                    bomOpcomponent.setComponentid(basPartsInv.get(0).getPartid());
                    bomOpcomponent.setBaseqtyd(bomDTO.getBaseQtyD());
                    bomOpcomponent.setBaseqtyn(bomDTO.getBaseQtyN());

                    bomOpcomponent.setOptionsid(bomOpcomponentopt.getOptionsid());



                    bomOpcomponent.setDefine35(Integer.valueOf(1));
                    bomOpcomponent.setDefine33(bomDTO.getDefine33());
                    bomOpcomponent.setDefine28(bomDTO.getDefine28());
                    bomOpcomponent.setDefine29(bomDTO.getDefine29());
                    bomOpcomponent.setDefine30(bomDTO.getDefine30());
                    bomOpcomponent.setDefine31(bomDTO.getDefine31());
                    bomOpcomponent.setDefine32(bomDTO.getDefine32());
                    bomOpcomponent.setDefine23(bomDTO.getDefine23());
                    bomOpcomponent.setDefine22(bomDTO.getDefine22());
                    bomOpcomponent.setDefine25(bomDTO.getDefine25());

                    //??????????????????????????????
                    if(unit!=null)
                    {
                        bomOpcomponent.setAuxunitcode(unit.getCcomunitcode());
                        bomOpcomponent.setChangerate(unit.getIchangrate());
                        bomOpcomponent.setAuxbaseqtyn(bomOpcomponent.getBaseqtyn().divide(bomOpcomponent.getChangerate(),5,BigDecimal.ROUND_HALF_UP));
                    }

                    bomOpcomponentMapper.insert(bomOpcomponent);

                    //??????????????????????????????
                    LambdaQueryWrapper<BomOpcomponent> selectOp = new LambdaQueryWrapper<>();
                    selectOp.eq(BomOpcomponent::getBomid,bomOpcomponent.getBomid());
                    selectOp.eq(BomOpcomponent::getComponentid,bomOpcomponent.getComponentid());
                    selectOp.ne(BomOpcomponent::getOpcomponentid,bomOpcomponent.getOpcomponentid());
                    List<BomOpcomponent> basOp = bomOpcomponentMapper.selectList(selectOp);
                    if(basOp!=null&&basOp.size()>0)
                    {
                        throw new Exception("????????????:"+bomDTO.getInvCode()+"???????????????????????????");
                    }


                }
                else
                {
                    if(CustomStringUtils.isBlank(bomDTO.getInvCode()))
                    {
                        throw new Exception("????????????????????????");
                    }
                    if(bomDTO.getInvCode().equals(main.getInvCode()))
                    {
                        throw new Exception("??????????????????????????????????????????");
                    }
                    //??????????????????
                    Inventory inv= inventoryMapper.selectById(bomDTO.getInvCode());
                    if(inv==null)
                    {
                        throw new Exception("????????????:"+bomDTO.getInvCode()+"??????????????????");
                    }

                    //????????????????????????
                    InventorySub invSub= inventorySubMapper.selectById(bomDTO.getInvCode());
                    if(invSub==null)
                    {
                        throw new Exception("????????????1:"+bomDTO.getInvCode()+"??????????????????");
                    }

                    //?????????????????????
                    ComputationUnit unit=null;
                    if(CustomStringUtils.isNotBlank(inv.getCpucomunitcode()))
                    {
                        unit=computationUnitMapper.selectById(inv.getCpucomunitcode());
                        if(unit==null)
                        {
                            throw new Exception("????????????:"+bomDTO.getInvCode()+"?????????????????????????????????");
                        }
                    }
                    //?????????????????????
                    LambdaQueryWrapper<BasPart> selectInv = new LambdaQueryWrapper<>();
                    selectInv.eq(BasPart::getInvcode,bomDTO.getInvCode());
                    List<BasPart> basPartsInv = basPartMapper.selectList(selectInv);
                    if(basPartsInv==null||basPartsInv.size()==0)
                    {
                        throw new Exception("????????????:"+bomDTO.getInvCode()+"????????????????????????");
                    }


                    BomOpcomponent bomOpcomponent=bomOpcomponentMapper.selectById(bomDTO.getOpComponentId());
                    bomOpcomponent.setDefine35(Integer.valueOf(2));
                    bomOpcomponent.setDefine33(bomDTO.getDefine33());
                    bomOpcomponent.setDefine28(bomDTO.getDefine28());
                    bomOpcomponent.setDefine29(bomDTO.getDefine29());
                    bomOpcomponent.setDefine30(bomDTO.getDefine30());
                    bomOpcomponent.setDefine31(bomDTO.getDefine31());
                    bomOpcomponent.setDefine32(bomDTO.getDefine32());
                    bomOpcomponent.setDefine23(bomDTO.getDefine23());
                    bomOpcomponent.setDefine22(bomDTO.getDefine22());
                    bomOpcomponent.setDefine25(bomDTO.getDefine25());
                    bomOpcomponent.setBaseqtyd(bomDTO.getBaseQtyD());
                    bomOpcomponent.setBaseqtyn(bomDTO.getBaseQtyN());
                    bomOpcomponent.setComponentid(basPartsInv.get(0).getPartid());

                    bomOpcomponent.setSortseq(row);
                    //??????????????????????????????
                    if(unit!=null)
                    {
                        bomOpcomponent.setAuxunitcode(unit.getCcomunitcode());
                        bomOpcomponent.setChangerate(unit.getIchangrate());
                        bomOpcomponent.setAuxbaseqtyn(bomOpcomponent.getBaseqtyn().divide(bomOpcomponent.getChangerate(),5,BigDecimal.ROUND_HALF_UP));
                    }
                    bomOpcomponentMapper.updateById(bomOpcomponent);



                    //????????????????????????1
                    BomOpcomponentopt bomOpcomponentopt=bomOpcomponentoptMapper.selectById(bomOpcomponent.getOptionsid());
//                    bomOpcomponentopt.setOffset(invSub.getIacceptearlydays());
                    bomOpcomponentopt.setWhcode(bomDTO.getCwhcode());
                    bomOpcomponentoptMapper.updateById(bomOpcomponentopt);

                    //??????????????????????????????
                    LambdaQueryWrapper<BomOpcomponent> selectOp = new LambdaQueryWrapper<>();
                    selectOp.eq(BomOpcomponent::getBomid,bomOpcomponent.getBomid());
                    selectOp.eq(BomOpcomponent::getComponentid,bomOpcomponent.getComponentid());
                    selectOp.ne(BomOpcomponent::getOpcomponentid,bomOpcomponent.getOpcomponentid());
                    List<BomOpcomponent> basOp = bomOpcomponentMapper.selectList(selectOp);
                    if(basOp!=null&&basOp.size()>0)
                    {
                        throw new Exception("????????????:"+bomDTO.getInvCode()+"???????????????????????????");
                    }

                }
                row=row+10;
            }



            //endregion
            result.setResult(main);
            result.setMsg("???????????????");
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());

        }
        return result;
    }


    /**
     * ??????BOM
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult bomImportU8(List<BomDTO> list) throws Exception{
        ResponseResult result = new ResponseResult();

        try{

            //region ????????????
            if(list == null || list.size() == 0){
                throw new Exception("???????????????");
            }

            for(BomDTO bomDTO: list){
                if(CustomStringUtils.isBlank(bomDTO.getParentInvCode())){
                    throw new Exception("???????????????????????????");
                }
                if(CustomStringUtils.isBlank(bomDTO.getInvCode()))
                {
                    throw new  Exception("???????????????????????????");
                }
                if(bomDTO.getInvCode().equals(bomDTO.getParentInvCode()))
                {
                    throw new Exception("???????????? ???????????? ???????????????");
                }
                if(CustomStringUtils.isBlank(bomDTO.getCwhcode()))
                {
                    throw new  Exception("???????????????????????????");
                }
                if(CustomStringUtils.isBlank(bomDTO.getBaseQtyN()))
                {
                    throw new  Exception("?????????????????????????????????");
                }
                if(bomDTO.getBaseQtyN().compareTo(BigDecimal.ZERO)<=0)
                {
                    throw new  Exception("??????????????????????????????0???");
                }
                if(CustomStringUtils.isBlank(bomDTO.getBaseQtyD()))
                {
                    throw new  Exception("?????????????????????????????????");
                }
                if(bomDTO.getBaseQtyD().compareTo(BigDecimal.ZERO)<=0)
                {
                    throw new  Exception("??????????????????????????????0???");
                }

                if(bomDTO.getInvCode().startsWith("1"))
                {
                    LambdaQueryWrapper<Inventory> select = new LambdaQueryWrapper<>();
                    select.select(Inventory::getCinvcode, Inventory::getCinvdefine3, Inventory::getCpucomunitcode)
                            .eq(Inventory::getCinvaddcode, bomDTO.getInvCode());
                    Inventory inv = inventoryMapper.selectOne(select);
                    if(inv!=null)
                    {
                        bomDTO.setInvCode(inv.getCinvcode());
                    }
                }
                if(bomDTO.getCwhcode().length() == 1){
                    bomDTO.setCwhcode("0" + bomDTO.getCwhcode());
                }
            }
            //endregion

            //region ????????????
            //????????????????????????????????? ??????
            list = list.stream()
                    .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(
                            Comparator.comparing(o -> o.getParentInvCode() + ";" + o.getInvCode())
                    )), ArrayList::new));


            Map<String, Inventory> invMap = new HashMap<>();

            //?????????????????????
            List<String> parentInvCodeList = list.stream().map(BomDTO::getParentInvCode).distinct().collect(Collectors.toList());

            //?????????????????????
            List<String> childInvCodeList = list.stream().map(BomDTO::getInvCode).distinct().collect(Collectors.toList());

            //???????????????????????????????????????
            List<String> allInvCodeList = parentInvCodeList;
            allInvCodeList.addAll(childInvCodeList);
            allInvCodeList = allInvCodeList.stream().distinct().collect(Collectors.toList());

            for(String invCode: allInvCodeList){

                LambdaQueryWrapper<Inventory> select = new LambdaQueryWrapper<>();
                select.select(Inventory::getCinvcode, Inventory::getCinvdefine3, Inventory::getCpucomunitcode)
                        .eq(Inventory::getCinvcode, invCode);
                Inventory inv = inventoryMapper.selectOne(select);

                if(inv == null){
                    throw new  Exception("???????????????" + invCode + " ???U8???????????????????????????");
                }
                if(inv.getCinvdefine3()==null)
                {
                    inv.setCinvdefine3("");
                }
                invMap.put(invCode, inv);


            }

            //?????????????????? Cinvdefine3  =  ?????????  ?????????
            for(int i=list.size()-1; i>-1; i--){
                String cinvdefine3 = invMap.get(list.get(i).getInvCode()).getCinvdefine3();

                if("?????????".equals(cinvdefine3)){
                    list.remove(i);
                }
            }
            //endregion

            //region ????????????
            List<Bom> bomInsList = new ArrayList<>();
            List<BomParent> bomParentInsList = new ArrayList<>();

            Map<String, Integer> bomIdMap = new HashMap<>();

            //?????????????????????
            List<String> parentInvCodeList1 = list.stream().map(BomDTO::getParentInvCode).distinct().collect(Collectors.toList());

            for(String partentInvCode: parentInvCodeList1){

                //????????????????????????
                Integer bomId = bomMapper.getBomCountByParentInvCode(partentInvCode);

                //????????????
                if(CustomStringUtils.isBlank(bomId)){
                    Bom bom=new Bom();
                    bom.setDefValue();

                    Integer u8fid_bom = null;
                    //??????U8Bom???id
                    u8fid_bom=u8SystemUtils.getId(accId,"bom_bom",10);
                    bom.setBomid(u8fid_bom);
                    bom.setCreateuser(SecurityUtil.getUser().getMyusername());
                    bom.setRelsuser(SecurityUtil.getUser().getMyusername());

                    //bom??????????????????bom???1
                    bom.setBomtype(Byte.valueOf("1"));
                    bom.setVersion(10);
                    bom.setVersiondesc(partentInvCode);

                    bom.setVtid(30442);

                    //??????bom???
                    bomInsList.add(bom);

                    //??????bas_part
                    LambdaQueryWrapper<BasPart> select = new LambdaQueryWrapper<>();
                    select.eq(BasPart::getInvcode, partentInvCode);
                    BasPart basPart = basPartMapper.selectOne(select);

                    if(basPart == null){
                        throw new  Exception("???????????????" + partentInvCode + " ???bas_part??????????????????");
                    }

                    //bom??????????????????
                    BomParent bomParent=new BomParent();
                    bomParent.setAutoid(UUID.randomUUID().toString());
                    bomParent.setBomid(bom.getBomid());
                    bomParent.setParentid(basPart.getPartid());
                    bomParent.setParentscrap(BigDecimal.ZERO);
                    bomParent.setSharingpartid(0);

                    //??????bom??????????????????
                    bomParentInsList.add(bomParent);

                    bomIdMap.put(partentInvCode, bom.getBomid());
                }else{
                    bomIdMap.put(partentInvCode, bomId);

                    //region ????????????
                    //????????????
                    LambdaQueryWrapper<BomOpcomponent> childSelect = new LambdaQueryWrapper<>();
                    childSelect.eq(BomOpcomponent::getBomid, bomId);
                    List<BomOpcomponent> childList = bomOpcomponentService.list(childSelect);

                    if(childList != null && childList.size() > 0){

                        List<Integer> childIdList = new ArrayList<>();
                        List<Integer> childOptIdList = new ArrayList<>();

                        for(BomOpcomponent child: childList){
                            //?????????????????????
                            BasPart part = basPartMapper.selectById(child.getComponentid());

                            Inventory inv = invMap.get(part.getInvcode());
                            if(inv == null){
                                LambdaQueryWrapper<Inventory> select = new LambdaQueryWrapper<>();
                                select.select(Inventory::getCinvcode, Inventory::getCinvdefine3, Inventory::getCpucomunitcode)
                                        .eq(Inventory::getCinvcode, part.getInvcode());
                                inv = inventoryMapper.selectOne(select);

                                if (inv == null) {
                                    throw new Exception("???????????????" + part.getInvcode() + " ???U8???????????????????????????");
                                }

                            }

                            //??????????????????
                            if(!"?????????".equals(inv.getCinvdefine3())){
                                childIdList.add(child.getOpcomponentid());
                                childOptIdList.add(child.getOptionsid());
                            }
                        }

                        //????????????
                        if(childIdList != null && childIdList.size() > 0){
                            bomOpcomponentService.removeByIds(childIdList);
                        }
                        //????????????opt
                        if(childOptIdList != null && childOptIdList.size() > 0){
                            bomOpcomponentoptService.removeByIds(childOptIdList);
                        }
                    }
                    //endregion
                }

            }

            if(bomInsList != null && bomInsList.size() > 0){
                this.saveBatch(bomInsList);
            }

            if(bomParentInsList != null && bomParentInsList.size() > 0){
                bomParentService.saveBatch(bomParentInsList);
            }
            //endregion

            //????????????????????????
            LambdaQueryWrapper<ComputationUnit> select = new LambdaQueryWrapper<>();
            select.select(ComputationUnit::getCcomunitcode, ComputationUnit::getIchangrate);
            List<ComputationUnit> unitList = computationUnitMapper.selectList(select);

            //region ????????????

            List<BomOpcomponentopt> bomOpcomponentoptInsList = new ArrayList<>();
            List<BomOpcomponent> bomOpcomponentInsList = new ArrayList<>();
            List<BomOpcomponentopt> bomOpcomponentoptUpList = new ArrayList<>();
            List<BomOpcomponent> bomOpcomponentUpList = new ArrayList<>();

            int row=10;
            for(BomDTO bomDTO:list)
            {

                if(CustomStringUtils.isBlank(bomDTO.getDefine23()))
                {
                    bomDTO.setDefine23("");
                }
                if(CustomStringUtils.isBlank(bomDTO.getCinvdefine2()))
                {
                    bomDTO.setCinvdefine2("");
                }

                //????????????????????????
                Integer opComponentId = bomMapper.getBomChildCountByInvCode(bomDTO.getParentInvCode(), bomDTO.getInvCode());

                if(CustomStringUtils.isBlank(opComponentId))
                {

                    //????????????????????????
                    InventorySub invSub= inventorySubMapper.selectById(bomDTO.getInvCode());
                    if(invSub==null)
                    {
                        throw new Exception("????????????:"+bomDTO.getInvCode()+"??????????????????");
                    }

                    //?????????????????????
                    ComputationUnit unit=null;
                    String cpucomunitcode = invMap.get(bomDTO.getInvCode()).getCpucomunitcode();

                    if(CustomStringUtils.isNotBlank(cpucomunitcode)){
                        unit = unitList.stream().filter(g->g.getCcomunitcode().equals(cpucomunitcode)).findAny().orElse(null);
                    }

                    //?????????????????????
                    LambdaQueryWrapper<BasPart> selectInv = new LambdaQueryWrapper<>();
                    selectInv.eq(BasPart::getInvcode,bomDTO.getInvCode());
                    List<BasPart> basPartsInv = basPartMapper.selectList(selectInv);
                    if(basPartsInv==null||basPartsInv.size()==0)
                    {
                        throw new Exception("????????????:"+bomDTO.getInvCode()+"????????????????????????");
                    }


                    Integer u8cid_bom_opcomponent = null;
                    Integer u8cid_bom_opcomponentopt = null;
                    //??????U8?????????id
                    u8cid_bom_opcomponent=u8SystemUtils.getChildId(accId,"bom_opcomponent",10,1);

                    //??????U8?????????id
                    u8cid_bom_opcomponentopt=u8SystemUtils.getChildId(accId,"bom_opcomponentopt",10,1);

                    //????????????????????????1
                    BomOpcomponentopt bomOpcomponentopt=new BomOpcomponentopt();
                    bomOpcomponentopt.setDefValue();
                    bomOpcomponentopt.setMutexrule((byte)2);
                    bomOpcomponentopt.setOptionsid(u8cid_bom_opcomponentopt);
                    bomOpcomponentopt.setOffset(0);
                    bomOpcomponentopt.setWiptype(3);
                    bomOpcomponentopt.setWhcode(bomDTO.getCwhcode());

                    bomOpcomponentoptInsList.add(bomOpcomponentopt);

                    //????????????????????????
                    BomOpcomponent bomOpcomponent=new BomOpcomponent();
                    bomOpcomponent.setDefValue();
                    bomOpcomponent.setOpcomponentid(u8cid_bom_opcomponent);
                    bomOpcomponent.setBomid(bomIdMap.get(bomDTO.getParentInvCode()));
                    bomOpcomponent.setSortseq(row);
                    bomOpcomponent.setComponentid(basPartsInv.get(0).getPartid());
                    bomOpcomponent.setBaseqtyd(bomDTO.getBaseQtyD());
                    bomOpcomponent.setBaseqtyn(bomDTO.getBaseQtyN());

                    bomOpcomponent.setOptionsid(bomOpcomponentopt.getOptionsid());

                    bomOpcomponent.setDefine35(Integer.valueOf(1));

                    //??????????????????????????????
                    if(unit!=null)
                    {
                        bomOpcomponent.setAuxunitcode(unit.getCcomunitcode());
                        bomOpcomponent.setChangerate(unit.getIchangrate());
                        bomOpcomponent.setAuxbaseqtyn(bomOpcomponent.getBaseqtyn().divide(bomOpcomponent.getChangerate(),5,BigDecimal.ROUND_HALF_UP));
                    }else{
                        bomOpcomponent.setChangerate(null);
                        bomOpcomponent.setAuxbaseqtyn(null);
                    }

                    bomOpcomponentInsList.add(bomOpcomponent);
                }
                else
                {

                    //????????????????????????
                    InventorySub invSub= inventorySubMapper.selectById(bomDTO.getInvCode());
                    if(invSub==null)
                    {
                        throw new Exception("????????????1:"+bomDTO.getInvCode()+"??????????????????");
                    }

                    //?????????????????????
                    ComputationUnit unit=null;
                    String cpucomunitcode = invMap.get(bomDTO.getInvCode()).getCpucomunitcode();

                    if(CustomStringUtils.isNotBlank(cpucomunitcode)){
                        unit = unitList.stream().filter(g->g.getCcomunitcode().equals(cpucomunitcode)).findAny().orElse(null);
                    }

                    //?????????????????????
                    LambdaQueryWrapper<BasPart> selectInv = new LambdaQueryWrapper<>();
                    selectInv.eq(BasPart::getInvcode,bomDTO.getInvCode());
                    List<BasPart> basPartsInv = basPartMapper.selectList(selectInv);
                    if(basPartsInv==null||basPartsInv.size()==0)
                    {
                        throw new Exception("????????????:"+bomDTO.getInvCode()+"????????????????????????");
                    }


                    BomOpcomponent bomOpcomponent=bomOpcomponentMapper.selectById(opComponentId);
                    bomOpcomponent.setDefine35(Integer.valueOf(2));

                    bomOpcomponent.setBaseqtyd(bomDTO.getBaseQtyD());
                    bomOpcomponent.setBaseqtyn(bomDTO.getBaseQtyN());
                    bomOpcomponent.setComponentid(basPartsInv.get(0).getPartid());

                    bomOpcomponent.setSortseq(row);
                    //??????????????????????????????
                    if(unit!=null)
                    {
                        bomOpcomponent.setAuxunitcode(unit.getCcomunitcode());
                        bomOpcomponent.setChangerate(unit.getIchangrate());
                        bomOpcomponent.setAuxbaseqtyn(bomOpcomponent.getBaseqtyn().divide(bomOpcomponent.getChangerate(),5,BigDecimal.ROUND_HALF_UP));
                    }else{
                        bomOpcomponent.setChangerate(null);
                        bomOpcomponent.setAuxbaseqtyn(null);
                    }

                    bomOpcomponentUpList.add(bomOpcomponent);

                    //????????????????????????
                    BomOpcomponentopt bomOpcomponentopt=bomOpcomponentoptMapper.selectById(bomOpcomponent.getOptionsid());
                    //bomOpcomponentopt.setOffset(invSub.getIacceptearlydays());
                    bomOpcomponentopt.setWhcode(bomDTO.getCwhcode());

                    bomOpcomponentoptUpList.add(bomOpcomponentopt);
                }
                row=row+10;
            }

            if(bomOpcomponentoptInsList != null && bomOpcomponentoptInsList.size() > 0){
                bomOpcomponentoptService.saveBatch(bomOpcomponentoptInsList);
            }

            if(bomOpcomponentInsList != null && bomOpcomponentInsList.size() > 0){
                bomOpcomponentService.saveBatch(bomOpcomponentInsList);
            }

            if(bomOpcomponentoptUpList != null && bomOpcomponentoptUpList.size() > 0){
                bomOpcomponentoptService.updateBatchById(bomOpcomponentoptUpList);
            }

            if(bomOpcomponentUpList != null && bomOpcomponentUpList.size() > 0){
                bomOpcomponentService.updateBatchById(bomOpcomponentUpList);
            }

            //endregion
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());

        }
        return result;
    }


    /**
     * ????????????
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer opComponentId) throws Exception{

        BomOpcomponent bomOpcomponent=bomOpcomponentMapper.selectById(opComponentId);
        if(bomOpcomponent!=null)
        {
            bomOpcomponentMapper.deleteById(opComponentId);
            bomOpcomponentoptMapper.deleteById(bomOpcomponent.getOptionsid());


            //???????????????????????????bom??????
            LambdaQueryWrapper<BomOpcomponent> selectOp = new LambdaQueryWrapper<>();
            selectOp.eq(BomOpcomponent::getBomid,bomOpcomponent.getBomid());
            List<BomOpcomponent> basOp = bomOpcomponentMapper.selectList(selectOp);
            if(basOp==null||basOp.size()==0)
            {
                Bom bom=bomMapper.selectById(bomOpcomponent.getBomid());
                bomMapper.deleteById(bom.getBomid());


                LambdaQueryWrapper<BomParent> selectParent = new LambdaQueryWrapper<>();
                selectParent.eq(BomParent::getBomid,bomOpcomponent.getBomid());
                List<BomParent> parents = bomParentMapper.selectList(selectParent);
                if(parents!=null&&parents.size()>0)
                {
                    for(BomParent bomParent:parents)
                    {
                        bomParentMapper.deleteById(bomParent.getAutoid());
                    }
                }
            }

        }
        else
        {
            throw new RuntimeException("???????????????,????????????????????????");
        }


    }
}
