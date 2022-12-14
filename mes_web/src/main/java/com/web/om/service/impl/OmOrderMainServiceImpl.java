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
import com.web.mo.dto.BomOpcomponentDTO;
import com.web.mo.entity.BomOpcomponent;
import com.web.mo.mapper.BomOpcomponentMapper;
import com.web.om.dto.*;
import com.web.om.entity.*;
import com.web.om.mapper.*;
import com.web.om.service.*;
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
public class OmOrderMainServiceImpl extends ServiceImpl<OmOrderMainMapper, OmOrderMain> implements IOmOrderMainService {


    @Autowired
    private OmOrderMainMapper omOrderMainMapper;

    @Autowired
    private OmOrderDetailMapper mesProductMapper;

    @Autowired
    private OmOrderMaterialMapper mesMaterialMapper;

    @Autowired
    IOmOrderDetailService productService;

    @Autowired
    IOmOrderPartService partService;

    @Autowired
    IOmOrderMaterialService materialService;

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
    private OmOrderPartMapper mesPartMapper;
    @Autowired
    private ComputationUnitMapper computationUnitMapper;
    @Autowired
    private VendorMapper vendorMapper;

    @Autowired
    private U8SystemUtils u8SystemUtils;

    @Autowired
    private BomOpcomponentMapper bomOpcomponentMapper;


    @Value("${account.acountId}")
    private String accId;

    @Override
    public IPage<OmOrderMainDTO> getMainList(OmOrderMainDTO mainDTO, IPage<OmOrderMainDTO> page) throws Exception {
        page.setRecords(omOrderMainMapper.getMainList(page, mainDTO));
        return page;
    }

    @Override
    public List<OmOrderMainDTO> getDetailList(OmOrderMainDTO mainDTO) throws Exception {
        return omOrderMainMapper.getDetailList(mainDTO);
    }

    /**
     * ????????????????????????mes???????????????????????????ID???????????????
     *
     * @param main         ??????
     * @param productList  ????????????
     * @param partList     ????????????
     * @param materialList ????????????
     * @return {@link ResponseResult}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult saveToMes(OmOrderMain main, List<OmOrderDetail> productList, List<OmOrderPart> partList, List<OmOrderMaterial> materialList) {
        ResponseResult result = null;
        try {
            result = new ResponseResult();
            //????????????ID
            String mainId = String.valueOf(SnowFlakeUtils.getFlowIdInstance().nextId());
            result.setResult(mainId);
            main.setId(mainId);
            main.setCreateInfo(mainId);
            main.setStatusId("?????????");
            this.save(main);
            //?????????ID???map???key???recordID???value??????????????????ID
            Map<String, String> productIdMap = new HashMap<>();
            //????????????ID???mao???key???partRowId,value??????????????????ID
            Map<String, String> partIdMap = new HashMap<>();
            //?????????????????????
            for (OmOrderDetail product: productList){
                if (productIdMap.containsKey(product.getRecordId())){
                    continue;
                }
                String productId = String.valueOf(SnowFlakeUtils.getFlowIdInstance().nextId());
                product.setId(productId);
                product.setMainId(mainId);
                product.setCreateInfo(productId);
                productIdMap.put(product.getRecordId(), productId);
                product.setRecordId(productId);
                productService.save(product);
            }
            //????????????????????????
            if (partList != null) {
                //?????????????????????
                partList.forEach(part -> {
                    String partId = String.valueOf(SnowFlakeUtils.getFlowIdInstance().nextId());
                    part.setId(partId);
                    part.setMainId(mainId);
                    part.setDetailId(productIdMap.get(part.getRecordId()));
                    part.setRecordId(productIdMap.get(part.getRecordId()));
                    part.setCreateInfo(partId);
                    partIdMap.put(part.getPartRowId(), partId);
                    part.setPartRowId(partId);
                    partService.save(part);
                });
            }
            //????????????????????????
            materialList.forEach(material -> {
                String materialId = String.valueOf(SnowFlakeUtils.getFlowIdInstance().nextId());
                material.setId(materialId);
                material.setMainId(mainId);
                material.setDetailId(productIdMap.get(material.getRecordId()));
                material.setRecordId(productIdMap.get(material.getRecordId()));
                material.setPartId(partIdMap.get(material.getPartRowId()));
                material.setPartRowId(partIdMap.get(material.getPartRowId()));
                material.setCreateInfo(materialId);
            });
            mesMaterialMapper.insertBatch(materialList);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * mes?????????,??????????????????????????????????????????????????????????????????
     *
     * @param main         ??????
     * @param productList  ????????????
     * @param partList     ????????????
     * @param materialList ????????????
     * @return {@link ResponseResult}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateToMes(OmOrderMain main, List<OmOrderDetail> productList, List<OmOrderPart> partList, List<OmOrderMaterial> materialList) {
        ResponseResult result = null;
        try {
            /**
             * DATE: 2022/9/17
             * mijiahao TODO: ??????????????????
             */
            result = new ResponseResult();
            String mainId = main.getId();
            result.setResult(mainId);
            this.updateById(main);
            //???????????????
            LambdaQueryWrapper<OmOrderDetail> productWrapper = new LambdaQueryWrapper<OmOrderDetail>();
            productWrapper.eq(OmOrderDetail::getMainId, mainId);
            List<OmOrderDetail> deleteProductList = productService.list(productWrapper);
            List<String> deleteProductIdList = new ArrayList<>();
            deleteProductList.forEach(product -> {
                deleteProductIdList.add(product.getId());
            });
            productService.removeByIds(deleteProductIdList);
            //???????????????
            LambdaQueryWrapper<OmOrderPart> partWrapper = new LambdaQueryWrapper<OmOrderPart>();
            partWrapper.eq(OmOrderPart::getMainId, mainId);
            List<OmOrderPart> deletePartList = partService.list(partWrapper);
            if (deletePartList.size() != 0) {
                List<String> deletePartIdList = new ArrayList<>();
                deletePartList.forEach(part -> {
                    deletePartIdList.add(part.getId());
                });

                partService.removeByIds(deletePartIdList);
            }
            //???????????????
            LambdaQueryWrapper<OmOrderMaterial> materialWrapper = new LambdaQueryWrapper<>();
            materialWrapper.eq(OmOrderMaterial::getMainId, mainId);
            List<OmOrderMaterial> deleteMaterialList = materialService.list(materialWrapper);
            if (deleteMaterialList.size() != 0) {
                List<String> deleteMaterialIdList = new ArrayList<>();
                deleteMaterialList.forEach(material -> {
                    deleteMaterialIdList.add(material.getId());
                });
                materialService.removeByIds(deleteMaterialIdList);
            }
            //?????????ID???map???key???recordID???value??????????????????ID
            Map<String, String> productIdMap = new HashMap<>();
            //????????????ID???mao???key???partRowId,value??????????????????ID
            Map<String, String> partIdMap = new HashMap<>();
            //?????????????????????
            for (OmOrderDetail product: productList){
                if (productIdMap.containsKey(product.getRecordId())){
                    continue;
                }
                String productId = String.valueOf(SnowFlakeUtils.getFlowIdInstance().nextId());
                product.setId(productId);
                product.setMainId(mainId);
                product.setCreateInfo(productId);
                productIdMap.put(product.getRecordId(), productId);
                product.setRecordId(productId);
                productService.save(product);
            }
            //????????????????????????
            if (partList != null) {
                //?????????????????????
                for (OmOrderPart part : partList){
                    if (partIdMap.containsKey(part.getPartRowId())){
                        continue;
                    }
                    String partId = String.valueOf(SnowFlakeUtils.getFlowIdInstance().nextId());
                    part.setId(partId);
                    part.setMainId(mainId);
                    part.setDetailId(productIdMap.get(part.getRecordId()));
                    part.setRecordId(productIdMap.get(part.getRecordId()));
                    partIdMap.put(part.getPartRowId(), partId);
                    part.setPartRowId(partId);
                    part.setCreateInfo(partId);
                    partService.save(part);
                }
            }
            //????????????????????????
            materialList.forEach(material -> {
                String materialId = String.valueOf(SnowFlakeUtils.getFlowIdInstance().nextId());
                material.setId(materialId);
                material.setMainId(mainId);
                material.setDetailId(productIdMap.get(material.getRecordId()));
                material.setRecordId(productIdMap.get(material.getRecordId()));
                material.setPartId(partIdMap.get(material.getPartRowId()));
                material.setPartRowId(partIdMap.get(material.getPartRowId()));
                material.setCreateInfo(materialId);
            });
            mesMaterialMapper.insertBatch(materialList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * ??????id??????????????????
     *
     * @param mesId id
     * @return {@link ResponseResult}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteMainById(String mesId) {
        try {
            OmOrderMain mesMain = this.getById(mesId);
            mesMain.setDeleteInfo();
            this.updateById(mesMain);
            //???????????????
            LambdaQueryWrapper<OmOrderDetail> productWrapper = new LambdaQueryWrapper<OmOrderDetail>();
            productWrapper.eq(OmOrderDetail::getMainId, mesId);
            productWrapper.eq(OmOrderDetail::getIzDelete, 0);
            List<OmOrderDetail> productList = productService.list(productWrapper);
            productList.forEach(product -> {
                product.setDeleteInfo();
                productService.updateById(product);
            });
            //???????????????
            LambdaQueryWrapper<OmOrderPart> partWrapper = new LambdaQueryWrapper<OmOrderPart>();
            partWrapper.eq(OmOrderPart::getMainId, mesId);
            partWrapper.eq(OmOrderPart::getIzDelete, 0);
            List<OmOrderPart> partList = partService.list(partWrapper);
            partList.forEach(part -> {
                part.setDeleteInfo();
                partService.updateById(part);
            });
            //???????????????
            LambdaQueryWrapper<OmOrderMaterial> materialWrapper = new LambdaQueryWrapper<>();
            materialWrapper.eq(OmOrderMaterial::getMainId, mesId);
            materialWrapper.eq(OmOrderMaterial::getIzDelete, 0);
            List<OmOrderMaterial> materialList = materialService.list(materialWrapper);
            materialList.forEach(material -> {
                material.setDeleteInfo();
                materialService.updateById(material);
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseResult.success();
    }

    /**
     * ??????????????????????????????id
     *
     * @param id id
     * @return {@link TableResult}
     */
    @Override
    public TableResult<OmOrderMain> getAllMainDataById(String id) {
        TableResult<OmOrderMain> result = new TableResult<>();
        OmOrderMain main = this.getById(id);
        //????????????????????????
        List<OmOrderDetail> productList = mesProductMapper.getProductJoinMaterials(id);
        //????????????????????????
        List<OmOrderPart> partList = mesPartMapper.getPartsJoinMaterials(id);
        //????????????????????????
        LambdaQueryWrapper<OmOrderMaterial> materialWrapper = new LambdaQueryWrapper<>();
        materialWrapper.eq(OmOrderMaterial::getMainId, id);
        materialWrapper.eq(OmOrderMaterial::getIzDelete, 0);
        List<OmOrderMaterial> materialList = materialService.list(materialWrapper);
        //???recordId????????????
        List<OmOrderMaterial> sortedMaterialList = materialList
                .stream()
                //??????????????????????????????ID??????????????????????????????????????????
                .sorted(Comparator.comparing(OmOrderMaterial::getDetailId)
                        //??????????????????????????????ID??????????????????ID????????????
                        .thenComparing(OmOrderMaterial::getPartId, Comparator.nullsFirst(String::compareTo)))
                .collect(Collectors.toList());
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("main", main);
        dataMap.put("productList", productList);
        dataMap.put("partList", partList);
        dataMap.put("materialList", sortedMaterialList);
        result.setData(dataMap);
        return result;
    }

    /**
     * ??????
     *
     * @param u8ProductList   u8????????????
     * @param u8MaterialList  u8????????????
     * @param mesMain         mes????????????
     * @param omProductPo     om????????????
     * @param mesProductList  mes????????????
     * @param mesMaterialList mes????????????
     * @return {@link ResponseResult}
     * @throws Exception ??????
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult audit(OmMoMain omProductPo,
                                List<OmProductVM> u8ProductList,
                                List<OmProductVM> u8MaterialList,
                                OmOrderMain mesMain,
                                List<OmOrderDetail> mesProductList, List<OmOrderMaterial> mesMaterialList) throws Exception {
        ResponseResult result = new ResponseResult();
        try {

            if (CustomStringUtils.isBlank(omProductPo.getDdate())) {
                throw new Exception("???????????????????????????");
            }

            if (CustomStringUtils.isBlank(omProductPo.getCdepcode())) {
                throw new Exception("?????????????????????");
            }
            if (CustomStringUtils.isBlank(omProductPo.getCpersoncode())) {
                throw new Exception("????????????????????????");
            }
            if (CustomStringUtils.isBlank(omProductPo.getCvencode())) {
                throw new Exception("????????????????????????");
            }

            if (u8ProductList == null || u8ProductList.size() == 0) {
                throw new Exception("???????????????????????????");
            }


            if (CustomStringUtils.isNotBlank(omProductPo.getMoid())) {
                OmMoMain omMoMain = omMoMainMapper.selectById(omProductPo.getMoid());
                if (omMoMain != null) {
                    if (!omMoMain.getCstate().toString().equals("0")) {
                        throw new Exception("?????????????????????????????????????????????");
                    }
                }


                LambdaQueryWrapper<OmMoMaterials> selectM = new LambdaQueryWrapper<>();
                selectM.apply("  modetailsid in (select modetailsid from OM_MODetails where moid='" + omProductPo.getMoid() + "' )");
                List<OmMoMaterials> listM = omMoMaterialsMapper.selectList(selectM);
                if (listM != null) {
                    for (OmMoMaterials omMoMaterials : listM) {
                        omMoMaterialsMapper.deleteById(omMoMaterials.getMomaterialsid());
                    }
                }

                LambdaQueryWrapper<OmMoDetails> selectD = new LambdaQueryWrapper<>();
                selectD.eq(OmMoDetails::getMoid, omProductPo.getMoid());
                List<OmMoDetails> listD = omMoDetailsMapper.selectList(selectD);
                if (listD != null) {
                    for (OmMoDetails omMoDetails : listD) {
                        omMoDetailsMapper.deleteById(omMoDetails.getModetailsid());
                    }
                }


                omMoMainMapper.deleteById(omProductPo.getMoid());
            }


            omProductPo.setMoid(null);

            //??????   ????????????????????????????????????
            if (CustomStringUtils.isNotBlank(omProductPo.getMoid())) {
                OmMoMain omProductPo1 = omMoMainMapper.selectById(omProductPo.getMoid());
                if (omProductPo1 != null && omProductPo1.getCstate().compareTo(Byte.valueOf("1")) != 0) {
                    throw new Exception("???????????????????????????");
                }
                //?????????????????????
                Vendor vendor = vendorMapper.selectById(omProductPo.getCvencode());
                omProductPo.setCvenaccount(vendor.getCvenaccount());
                omProductPo.setCvenbank(vendor.getCvenbank());
                omProductPo.setCvenperson(vendor.getCvenperson());
                int rate = 13;
                if (!CustomStringUtils.isBlank(vendor.getCvendefine11())) {
                    rate = vendor.getCvendefine11();
                }
                omProductPo.setItaxrate(BigDecimal.valueOf(rate));
                omProductPo.setCexchName(vendor.getCvenexchName());
                int n = omMoMainMapper.updateById(omProductPo);
                if (n < 0) {
                    throw new Exception("?????????????????????");
                }
                //????????????????????????
                for (OmProductVM t : u8ProductList) {
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
//                    if(CustomStringUtils.isBlank(t.getItaxprice()))
//                    {
//                        throw new Exception("??????????????????????????????");
//                    }
//                    if(t.getItaxprice().compareTo(BigDecimal.ZERO)<0)
//                    {
//                        throw new Exception("??????????????????????????????0???");
//                    }
                    OmMoDetails omPoMain = new OmMoDetails();

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

                    BigDecimal itaxrate = BigDecimal.ONE.add((omPoMain.getIpertaxrate()).multiply(BigDecimal.valueOf(0.01)));

                    omPoMain.setIunitprice(omPoMain.getItaxprice().divide(itaxrate, 8, BigDecimal.ROUND_HALF_UP));  //??????????????????
                    omPoMain.setImoney(omPoMain.getIsum().divide(itaxrate, 8, BigDecimal.ROUND_HALF_UP));  //??????????????????

                    omPoMain.setInatunitprice(omPoMain.getItaxprice().divide(itaxrate, 8, BigDecimal.ROUND_HALF_UP));  //??????????????????
                    omPoMain.setInatmoney(omPoMain.getIsum().divide(itaxrate, 8, BigDecimal.ROUND_HALF_UP));  //??????????????????


                    omPoMain.setItax(omPoMain.getIsum().subtract(omPoMain.getImoney()));  //????????????
                    omPoMain.setInattax(omPoMain.getIsum().subtract(omPoMain.getImoney()));  //????????????


//                    omPoMain.setInatsum(t.getInatsum());

                    if (CustomStringUtils.isNotBlank(t.getModetailsid())) {

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
                        if (n < 0) {
                            throw new Exception("?????????????????????");
                        }
                    } else {
                        omPoMain.setDefaultValueForJinGong();
                        omPoMain.setMoid(omProductPo.getMoid());


                        Integer u8fid = null;
                        u8fid = u8SystemUtils.getChildId(accId, "OM_MO", 10, 1);
                        u8SystemUtils.getFatherId(accId, "OM_Materials", 10);
                        omPoMain.setModetailsid(u8fid);

                        t.setModetailsid(omPoMain.getModetailsid());
                        n = omMoDetailsMapper.insert(omPoMain);
                        if (n < 0) {
                            throw new Exception("?????????????????????");
                        }

                    }

                }
                //????????????????????????
                for (OmProductVM t : u8MaterialList) {

                    if (CustomStringUtils.isNotBlank(t.getCinvcodes())) {
                        if (CustomStringUtils.isBlank(t.getFbaseqtyn())) {
                            throw new Exception("?????????????????????");
                        }

                        OmMoMaterials omPoDetails = new OmMoMaterials();
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
                        if (CustomStringUtils.isNotBlank(inventory.getCstcomunitcode())) {
                            ComputationUnit unit = computationUnitMapper.selectById(inventory.getCstcomunitcode());
                            if (unit == null) {
                                throw new Exception("U8??????????????????????????????????????????");
                            }
                            omPoDetails.setCunitid(unit.getCcomunitcode());
                            omPoDetails.setFbasenumn(unit.getIchangrate());
                            omPoDetails.setIunitnum(unit.getIchangrate().multiply(t.getIquantity()));
                        }

                        for (OmProductVM q : u8ProductList) {
                            if (q.getRecordId().equals(t.getRecordId())) {
                                omPoDetails.setModetailsid(q.getModetailsid());
                                omPoDetails.setDrequireddate(q.getDarrivedate());
                                break;
                            }
                        }
                        if (omPoDetails.getModetailsid() == null) {
                            throw new Exception("?????????????????????????????????");
                        }
                        if (CustomStringUtils.isNotBlank(t.getMomaterialsid())) {

                            omPoDetails.setMomaterialsid(t.getMomaterialsid());
                            n = omMoMaterialsMapper.updateById(omPoDetails);
                            if (n < 0) {
                                throw new Exception("????????????????????????");
                            }
                        } else {

                            omPoDetails.setDefaultValueForJinGong();

                            Integer u8fid = null;
                            u8fid = u8SystemUtils.getChildId(accId, "OM_Materials", 10, 1);

                            //??????id

                            omPoDetails.setMomaterialsid(u8fid);

                            n = omMoMaterialsMapper.insert(omPoDetails);
                            if (n < 0) {
                                throw new Exception("????????????????????????");
                            }
                        }
                        //???????????????????????????
                        Inventory inventoryUpdate = new Inventory();
                        inventoryUpdate.setCinvcode(t.getCinvcodes());
                        if (CustomStringUtils.isNotBlank(t.getCinvdefine2())) {
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
            else {
                omProductPo.setDefaultValueForJinGong();
                Integer u8fid = null;
                u8fid = u8SystemUtils.getFatherId(accId, "OM_MO", 10);
                omProductPo.setMoid(u8fid);
                //??????????????????
                omProductPo.setCstate(Byte.valueOf("1"));
                omProductPo.setIverifystatenew(2);
                omProductPo.setCverifier(SecurityUtil.getUser().getMyusername());
                omProductPo.setDverifydate(DateUtil.parseStrToDate(DateUtil.getDateStr(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd"));
                omProductPo.setDverifytime(new Date());
                //????????????

                omProductPo.setCmaker(SecurityUtil.getUser().getMyusername());
                //?????????????????????
                Vendor vendor = vendorMapper.selectById(omProductPo.getCvencode());
                omProductPo.setCvenaccount(vendor.getCvenaccount());
                omProductPo.setCvenbank(vendor.getCvenbank());
                omProductPo.setCvenperson(vendor.getCvenperson());
                int rate = 13;
                if (!CustomStringUtils.isBlank(vendor.getCvendefine11())) {
                    rate = vendor.getCvendefine11();
                }
                omProductPo.setItaxrate(BigDecimal.valueOf(rate));
                omProductPo.setCexchName(vendor.getCvenexchName());
                String sysbarcode = "||" + "ommo" + "|" + omProductPo.getCcode();
                omProductPo.setCsysbarcode(sysbarcode);
                omProductPo.setCmemo(omProductPo.getCmemo());
                int n = omMoMainMapper.insert(omProductPo);
                if (n < 0) {
                    throw new Exception("?????????????????????");
                }
                //??????mes????????????????????????
                if (mesMain != null) {
                    OmOrderMain updateMain = new OmOrderMain();
                    updateMain.setId(mesMain.getId());
                    updateMain.setStatusId("?????????");
                    updateMain.setU8Id(u8fid);
                    Integer updateNum = omOrderMainMapper.updateWithDbName(updateMain, ParamUtil.getParam("localDatabase").toString());
                    if (updateNum < 1) {
                        throw new Exception("??????mes???????????????????????????");
                    }
                }

                //????????????????????????
                int row = 1;
                for (OmProductVM t : u8ProductList) {

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
                    omPoMain.setItaxprice(t.getItaxprice() == null ? BigDecimal.ZERO : t.getItaxprice());
                    omPoMain.setIpertaxrate(BigDecimal.valueOf(rate));
                    omPoMain.setInatsum(t.getInatsum() == null ? BigDecimal.ZERO : t.getInatsum());  //??????????????????
                    omPoMain.setIsum(omPoMain.getInatsum());  //??????????????????
                    omPoMain.setDstartdate(t.getDstartdate());
                    omPoMain.setDarrivedate(t.getDarrivedate());
                    omPoMain.setIquantity(t.getIquantity() == null ? BigDecimal.ZERO : t.getIquantity());
                    omPoMain.setImrpqty(t.getIquantity() == null ? BigDecimal.ZERO : t.getIquantity());
                    omPoMain.setCdefine26(t.getCdefine26() == null ? BigDecimal.ZERO : t.getCdefine26());
                    omPoMain.setCdefine27(t.getCdefine27() == null ? BigDecimal.ZERO : t.getCdefine27());
                    omPoMain.setIvouchrowno(row);
                    sysbarcode = "||" + "ommo" + "|" + omProductPo.getCcode() + "|" + row;
                    omPoMain.setCbsysbarcode(sysbarcode);
                    BigDecimal itaxrate = BigDecimal.ONE.add((omPoMain.getIpertaxrate()).multiply(BigDecimal.valueOf(0.01)));

                    omPoMain.setIunitprice(omPoMain.getItaxprice().divide(itaxrate, 8, BigDecimal.ROUND_HALF_UP));  //??????????????????
                    omPoMain.setImoney(omPoMain.getIsum().divide(itaxrate, 8, BigDecimal.ROUND_HALF_UP));  //??????????????????

                    omPoMain.setInatunitprice(omPoMain.getItaxprice().divide(itaxrate, 8, BigDecimal.ROUND_HALF_UP));  //??????????????????
                    omPoMain.setInatmoney(omPoMain.getIsum().divide(itaxrate, 8, BigDecimal.ROUND_HALF_UP));  //??????????????????


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
                    String mesProductId = t.getCfree3();
                    OmOrderDetail product = new OmOrderDetail();
                    product.setId(mesProductId);
                    product.setU8MoDetailId(omPoMain.getMoid());
                    mesProductMapper.updateWithDBName(product, ParamUtil.getParam("localDatabase").toString());

                    int izExist = 0;
                    for (OmProductVM material : u8MaterialList) {
                        if (material.getRecordId().equals(t.getRecordId())) {
                            izExist = 1;
                            break;
                        }
                    }
                    if (izExist == 0) {
                        OmMoMaterials omPoDetails = new OmMoMaterials();
                        omPoDetails.setDefaultValueForJinGong();
                        omPoDetails.setMoid(t.getMoid());
                        omPoDetails.setModetailsid(t.getModetailsid());
                        omPoDetails.setDrequireddate(t.getDarrivedate());
                        sysbarcode = "||" + "ommo" + "|" + omProductPo.getCcode() + "|" + row + "|1";
                        omPoDetails.setCsubsysbarcode(sysbarcode);

                        u8fid = null;
                        u8fid = u8SystemUtils.getChildId(accId, "OM_Materials", 10, 1);
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
                row = 1;
                for (OmProductVM t : u8MaterialList) {

                    if (CustomStringUtils.isNotBlank(t.getCinvcodes())) {
                        if (CustomStringUtils.isBlank(t.getFbaseqtyn()) || t.getFbaseqtyn().compareTo(BigDecimal.ZERO) == 0) {
                            throw new Exception("?????????????????????0???");
                        }

                        OmMoMaterials omPoDetails = new OmMoMaterials();
                        omPoDetails.setDefaultValueForJinGong();
                        for (OmProductVM q : u8ProductList) {
                            if (q.getRecordId().equals(t.getRecordId())) {
                                omPoDetails.setMoid(q.getMoid());

                                omPoDetails.setModetailsid(q.getModetailsid());
                                omPoDetails.setDrequireddate(q.getDarrivedate());
                                sysbarcode = "||" + "ommo" + "|" + omProductPo.getCcode() + "|" + q.getRowNo() + "|" + row;

                                omPoDetails.setCsubsysbarcode(sysbarcode);

                                break;
                            }

                        }
                        if (omPoDetails.getMoid() == null) {
                            throw new Exception("?????????????????????????????????");
                        }
                        u8fid = null;
                        u8fid = u8SystemUtils.getChildId(accId, "OM_Materials", 10, 1);
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
                        if (CustomStringUtils.isNotBlank(inventory.getCstcomunitcode())) {
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
                        String mesId = t.getCfree1();
                        OmOrderMaterial mesMaterial = new OmOrderMaterial();
                        mesMaterial.setId(mesId);
                        mesMaterial.setU8MoMaterialId(omPoDetails.getMoid());
                        mesMaterialMapper.updateWithDBName(mesMaterial, ParamUtil.getParam("localDatabase").toString());
                        row++;
                    }

                }

            }

            result.setResult(omProductPo.getMoid());
            result.setResult1(omProductPo.getCcode());
            synchronizeBomComponent(mesProductList,mesMaterialList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }

    /**
     * ??????
     *
     * @param main         ??????
     * @param productList  ????????????
     * @param materialList ????????????
     * @return {@link ResponseResult}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult change(OmOrderMain main, List<OmOrderDetail> productList, List<OmOrderMaterial> materialList) {
        try {
            ResponseResult result = new ResponseResult();
            Integer u8Id = main.getU8Id();
            if (u8Id == null || u8Id <= 0) {
                return ResponseResult.error("???????????????");
            }
            //??????????????????????????????
            LambdaQueryWrapper<OmMoDetails> selectD = new LambdaQueryWrapper<>();
            selectD.eq(OmMoDetails::getMoid, u8Id);
            List<OmMoDetails> listD = omMoDetailsMapper.selectList(selectD);
            if (listD.size() != 0) {
                for (OmMoDetails product : listD) {
                    BigDecimal inputProductQty = product.getIreceivedqty() == null ? BigDecimal.ZERO : product.getIreceivedqty();
                    //????????????????????????????????????????????????????????????????????????
                    //???????????????????????????
                    BigDecimal updateProductQty = null;
                    for (int i = 0; i < productList.size(); i++) {
                        OmOrderDetail mesProduct = productList.get(i);
                        if (mesProduct.getU8MoDetailId().equals(product.getMoid())) {
                            updateProductQty = mesProduct.getProductQty();
                            break;
                        }
                    }
                    //
                    if (updateProductQty == null) {
                        throw new Exception("mes???????????????u8?????????????????????");
                    }
                    if (updateProductQty.compareTo(inputProductQty) >= 0) {
                        validateMaterialOutput(main, productList, materialList);
                        //??????????????????????????????????????????
                    } else {
                        //???????????????
                        throw new Exception("??????????????????????????????????????????????????????");
                    }


                }
            } else {
                throw new Exception("???????????????????????????");
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * ????????????????????????
     *
     * @return boolean
     */
    boolean validateMaterialOutput(OmOrderMain main, List<OmOrderDetail> productList, List<OmOrderMaterial> materialList) throws Exception {
        LambdaQueryWrapper<OmMoMaterials> selectM = new LambdaQueryWrapper<>();
        selectM.apply("  modetailsid in (select modetailsid from OM_MODetails where moid='" + main.getU8Id() + "' )");
        List<OmMoMaterials> listM = omMoMaterialsMapper.selectList(selectM);
        if (listM.size() != 0) {
            for (OmMoMaterials u8Material : listM) {
                BigDecimal outputMaterialQty = u8Material.getIsendqty() == null ? BigDecimal.ZERO : u8Material.getIsendqty();
                //????????????????????????????????????????????????????????????

                BigDecimal updateMaterialQty = null;
                for (int i = 0; i < materialList.size(); i++) {
                    OmOrderMaterial mesMaterial = materialList.get(i);
                    if (mesMaterial.getU8MoMaterialId().equals(u8Material.getMoid())) {
                        updateMaterialQty = mesMaterial.getTqty();
                    }
                }
                if (updateMaterialQty.compareTo(outputMaterialQty) >= 0) {
                    //???????????????????????????
                    changeData(main, productList, materialList);
                } else {
                    //???????????????
                    throw new Exception("??????????????????????????????????????????????????????");
                }
            }
        }
        return true;
    }

    /**
     * ????????????
     *
     * @param main         ??????
     * @param productList  ????????????????????????
     * @param materialList ????????????????????????
     * @return boolean
     */
    void changeData(OmOrderMain main, List<OmOrderDetail> productList, List<OmOrderMaterial> materialList) throws Exception {
        productList.forEach(product -> {
            materialList.forEach(material -> {
                BigDecimal productQty = product.getProductQty();
                BigDecimal materialTqty = material.getTqty();
                //??????u8????????????
                OmMoDetails u8Product = new OmMoDetails();
                LambdaQueryWrapper<OmMoDetails> u8ProductWrapper = new LambdaQueryWrapper<>();
                u8ProductWrapper.eq(OmMoDetails::getMoid, product.getU8MoDetailId());
                u8Product.setIquantity(productQty);
                int u8ProductUpdateNum = omMoDetailsMapper.update(u8Product, u8ProductWrapper);
                //??????u8????????????
                OmMoMaterials u8Material = new OmMoMaterials();
                LambdaQueryWrapper<OmMoMaterials> u8MaterialWrapper = new LambdaQueryWrapper<>();
                u8MaterialWrapper.eq(OmMoMaterials::getMoid, material.getU8MoMaterialId());
                u8Material.setIquantity(materialTqty);
                int u8MaterialUpdateNum = omMoMaterialsMapper.update(u8Material, u8MaterialWrapper);
                //??????mes????????????
                OmOrderDetail mesProduct = new OmOrderDetail();
                mesProduct.setId(product.getId());
                mesProduct.setProductQty(productQty);
                int mesProductUpdateNum = mesProductMapper.updateWithDBName(mesProduct, ParamUtil.getParam("localDatabase").toString());
                //??????mes????????????
                OmOrderMaterial mesMaterial = new OmOrderMaterial();
                mesMaterial.setId(material.getId());
                mesMaterial.setTqty(materialTqty);
                int mesMaterialUpdateNum = mesMaterialMapper.updateWithDBName(mesMaterial, ParamUtil.getParam("localDatabase").toString());
                try {
                    if (u8MaterialUpdateNum == 0 || u8ProductUpdateNum == 0 || mesMaterialUpdateNum == 0 || mesProductUpdateNum == 0) {
                        throw new Exception("??????????????????");
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            });
        });
    }

    /**
     * ???????????????
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult unCheck(Integer id, String mesId) throws Exception {
        ResponseResult result = new ResponseResult();
        try {
            OmMoMain omProductPo = omMoMainMapper.selectById(id);
            if (omProductPo != null) {
                if (!omProductPo.getCstate().toString().equals("1")) {
                    result.setSuccess(false);
                    result.setMsg("??????????????????????????????????????????");
                    return result;
                }


                LambdaQueryWrapper<OmMoMaterials> selectM = new LambdaQueryWrapper<>();
                selectM.apply("  modetailsid in (select modetailsid from OM_MODetails where moid='" + id + "' )");
                List<OmMoMaterials> listM = omMoMaterialsMapper.selectList(selectM);
                if (listM != null) {
                    for (OmMoMaterials omMoMaterials : listM) {
                        if (omMoMaterials.getIsendqty() != null && omMoMaterials.getIsendqty().compareTo(BigDecimal.ZERO) > 0) {
                            result.setSuccess(false);
                            result.setMsg("???????????????????????????????????????????????????");
                            return result;
                        }
                    }
                }

                LambdaQueryWrapper<OmMoDetails> selectD = new LambdaQueryWrapper<>();
                selectD.eq(OmMoDetails::getMoid, id);
                List<OmMoDetails> listD = omMoDetailsMapper.selectList(selectD);
                if (listD != null) {
                    for (OmMoDetails omMoDetails : listD) {
                        if (omMoDetails.getIreceivedqty() != null && omMoDetails.getIreceivedqty().compareTo(BigDecimal.ZERO) > 0) {
                            result.setSuccess(false);
                            result.setMsg("???????????????????????????????????????????????????");
                            return result;
                        }
                    }
                }
                //??????????????????
                LambdaQueryWrapper<OmMoMaterials> materialWrapper = new LambdaQueryWrapper<>();
                materialWrapper.eq(OmMoMaterials::getMoid,id);
                List<OmMoMaterials> u8MaterialList = omMoMaterialsMapper.selectList(materialWrapper);
                if (u8MaterialList.size() == 0){
                    throw new Exception("???????????????????????????");
                }
                List<Integer> deleteU8MaterialIdList = new ArrayList<>();
                u8MaterialList.forEach(material->{
                    deleteU8MaterialIdList.add(material.getMomaterialsid());
                });
                if (omMoMaterialsMapper.deleteBatchIds(deleteU8MaterialIdList) <= 0){
                    throw new Exception("?????????????????????????????????");
                };
                //??????????????????
                LambdaQueryWrapper<OmMoDetails> detailWrapper = new LambdaQueryWrapper<>();
                detailWrapper.eq(OmMoDetails::getMoid,id);
                List<OmMoDetails> u8ProductList = omMoDetailsMapper.selectList(detailWrapper);
                if (u8ProductList.size() == 0){
                    throw new Exception("???????????????????????????");
                }
                List<Integer> deleteU8ProductIdList = new ArrayList<>();
                u8ProductList.forEach(product->{
                    deleteU8ProductIdList.add(product.getModetailsid());
                });
                if (omMoDetailsMapper.deleteBatchIds(deleteU8ProductIdList) <=0){
                    throw new Exception("?????????????????????????????????");
                };
                //????????????
                OmMoMain m = new OmMoMain();
                m.setMoid(id);
                if (omMoMainMapper.deleteById(m.getMoid()) <= 0){
                    throw new Exception("???????????????");
                };
                OmOrderMain mesMain = new OmOrderMain();
                if (StringUtils.isNotBlank(mesId)) {
                    mesMain.setId(mesId);
                    mesMain.setStatusId("?????????");
                    mesMain.setU8Id(-1);
                    /**
                     * DATE: 2022/9/22
                     * mijiahao TODO: ????????????
                     */
                    omOrderMainMapper.updateWithDbName(mesMain, ParamUtil.getParam("localDatabase").toString());
                }
            } else {
                result.setSuccess(false);
                result.setMsg("??????????????????");
                return result;
            }


        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }

    /**
     * ??????bom????????????
     *
     */
    void synchronizeBomComponent(List<OmOrderDetail> productList,List<OmOrderMaterial> materialList) throws Exception{
        for (OmOrderDetail product : productList){
            String invCode = product.getProductInvCode();
            //????????????????????????????????????????????????????????????????????????????????????????????????
            List<BomOpcomponentDTO> componentList = bomOpcomponentMapper.getComponentWithParentInvCode(invCode);
            if (componentList.size() == 0){
                continue;
            }
            for (BomOpcomponentDTO component : componentList){
                for (OmOrderMaterial material : materialList){
                    if (component.getInvCode().equals(material.getInvCode())){
                        //????????????
                        component.setDefine28(material.getInvLand());
                        //????????????
                        component.setDefine29(material.getInvLen());
                        //????????????
                        component.setDefine30(material.getInvWidth());
                        //????????????
                        component.setDefine31(material.getInvExternalDiameter());
                        //????????????
                        component.setDefine32(material.getInvInternalDiameter());
                        int n = bomOpcomponentMapper.updateById(component);
                        if (n <=0){
                            throw new Exception("BOM??????????????????");
                        }
                    }
                }
            }

        }
    }



}
