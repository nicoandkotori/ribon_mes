package com.web.om.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.util.ResponseResult;
import com.common.util.SnowFlakeUtils;
import com.common.util.TableResult;
import com.web.basicinfo.mapper.InventoryMapper;
import com.web.om.dto.*;
import com.web.om.entity.*;
import com.web.om.mapper.OmMoDetailsMapper;
import com.web.om.mapper.OmMoMainMapper;
import com.web.om.mapper.OmOrderMainMapper;
import com.web.om.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    IOmOrderDetailService productService;

    @Autowired
    IOmOrderPartService partService;

    @Autowired
    IOmOrderMaterialService materialService;


    @Value("${account.acountId}")
    private String accId;

    @Override
    public IPage<OmOrderMainDTO> getMainList(OmOrderMainDTO mainDTO, IPage<OmOrderMainDTO> page) throws Exception {
        page.setRecords(omOrderMainMapper.getMainList(page,mainDTO));
        return page;
    }

    @Override
    public List<OmOrderMainDTO> getDetailList(OmOrderMainDTO mainDTO) throws Exception {
        return omOrderMainMapper.getDetailList(mainDTO);
    }

    /**
     * 保存委外订单到到mes中，大部分逻辑为表ID关系的处理
     *
     * @param main         主要
     * @param productList  产品列表
     * @param partList     部件列表
     * @param materialList 材料列表
     * @return {@link ResponseResult}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult saveToMes(OmOrderMain main, List<OmOrderDetail> productList, List<OmOrderPart> partList, List<OmOrderMaterial> materialList) {
        ResponseResult result = null;
        try {
            result = new ResponseResult();
            //生成主表ID
            String mainId = String.valueOf(SnowFlakeUtils.getFlowIdInstance().nextId());
            result.setResult(mainId);
            main.setId(mainId);
            main.setCreateInfo(mainId);
            this.save(main);
            //产品表ID的map，key是recordID，value是产品记录的ID
            Map<String,String> productIdMap = new HashMap<>();
            //部件表的ID的mao，key是partRowId,value是部件记录的ID
            Map<String,String> partIdMap = new HashMap<>();
            //先遍历产品列表
            productList.forEach(product ->{
                String productId = String.valueOf(SnowFlakeUtils.getFlowIdInstance().nextId());
                product.setId(productId);
                product.setMainId(mainId);
                product.setCreateInfo(productId);
                productIdMap.put(product.getRecordId(),productId);
                productService.save(product);
            });
            //部件表不是必传的
            if (partList != null){
                //再遍历部件列表
                partList.forEach(part ->{
                    String partId = String.valueOf(SnowFlakeUtils.getFlowIdInstance().nextId());
                    part.setId(partId);
                    part.setMainId(mainId);
                    part.setDetailId(productIdMap.get(part.getRecordId()));
                    part.setCreateInfo(partId);
                    partIdMap.put(part.getPartRowId(),partId);
                    partService.save(part);
                });
            }
            //最后遍历材料列表
            materialList.forEach(material ->{
                String materialId = String.valueOf(SnowFlakeUtils.getFlowIdInstance().nextId());
                material.setId(materialId);
                material.setMainId(mainId);
                material.setDetailId(productIdMap.get(material.getRecordId()));
                material.setPartId(partIdMap.get(material.getPartRowId()));
                material.setCreateInfo(materialId);

            });
            materialService.saveBatch(materialList);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * mes中更新,因为是先删除所有子表记录再插入，所以性能较差
     *
     * @param main         主要
     * @param productList  产品列表
     * @param partList     部件列表
     * @param materialList 材料列表
     * @return {@link ResponseResult}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateToMes(OmOrderMain main, List<OmOrderDetail> productList, List<OmOrderPart> partList, List<OmOrderMaterial> materialList) {
        ResponseResult result = null;
        try {
            /**
             * DATE: 2022/9/17
             * mijiahao TODO: 有空优化一下
             */
            result = new ResponseResult();
            String mainId = main.getId();
            result.setResult(mainId);
            this.updateById(main);
            //删除产品表
            LambdaQueryWrapper<OmOrderDetail> productWrapper = new LambdaQueryWrapper<OmOrderDetail>();
            productWrapper.eq(OmOrderDetail::getMainId,mainId);
            List<OmOrderDetail> deleteProductList = productService.list(productWrapper);
            List<String> deleteProductIdList = new ArrayList<>();
            deleteProductList.forEach( product ->{
                deleteProductIdList.add(product.getId());
            });
            productService.removeByIds(deleteProductIdList);
            //删除部件表
            LambdaQueryWrapper<OmOrderPart> partWrapper = new LambdaQueryWrapper<OmOrderPart>();
            partWrapper.eq(OmOrderPart::getMainId,mainId);
            List<OmOrderPart> deletePartList = partService.list(partWrapper);
            List<String> deletePartIdList = new ArrayList<>();
            deletePartList.forEach( part ->{
                deletePartIdList.add(part.getId());
            });
            partService.removeByIds(deletePartIdList);
            //删除材料表
            LambdaQueryWrapper<OmOrderMaterial> materialWrapper = new LambdaQueryWrapper<>();
            materialWrapper.eq(OmOrderMaterial::getMainId,mainId);
            List<OmOrderMaterial> deleteMaterialList = materialService.list(materialWrapper);
            List<String> deleteMaterialIdList = new ArrayList<>();
            deleteMaterialList.forEach( material ->{
                deleteMaterialIdList.add(material.getId());
            });
            materialService.removeByIds(deleteMaterialIdList);
            //产品表ID的map，key是recordID，value是产品记录的ID
            Map<String,String> productIdMap = new HashMap<>();
            //部件表的ID的mao，key是partRowId,value是部件记录的ID
            Map<String,String> partIdMap = new HashMap<>();
            //先遍历产品列表
            productList.forEach(product ->{
                String productId = String.valueOf(SnowFlakeUtils.getFlowIdInstance().nextId());
                product.setId(productId);
                product.setMainId(mainId);
                product.setCreateInfo(productId);
                productIdMap.put(product.getRecordId(),productId);
            });
            productService.saveBatch(productList);
            //部件表不是必传的
            if (partList != null){
                //再遍历部件列表
                partList.forEach(part ->{
                    String partId = String.valueOf(SnowFlakeUtils.getFlowIdInstance().nextId());
                    part.setId(partId);
                    part.setMainId(mainId);
                    part.setDetailId(productIdMap.get(part.getRecordId()));
                    part.setCreateInfo(partId);
                    partIdMap.put(part.getPartRowId(),partId);
                });
            }
            partService.saveBatch(partList);
            //最后遍历材料列表
            materialList.forEach(material ->{
                String materialId = String.valueOf(SnowFlakeUtils.getFlowIdInstance().nextId());
                material.setId(materialId);
                material.setMainId(mainId);
                material.setDetailId(productIdMap.get(material.getRecordId()));
                material.setPartId(partIdMap.get(material.getPartRowId()));
                material.setCreateInfo(materialId);
            });
            materialService.saveBatch(materialList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 通过id作废委外订单
     *
     * @param id id
     * @return {@link ResponseResult}
     */
    @Override
    @Transactional (rollbackFor = Exception.class)
    public ResponseResult deleteMainById(String id) {
        //删除订单
        try {
            OmOrderMain main = this.getById(id);
            main.setDeleteInfo();
            this.updateById(main);
            //删除产品表
            LambdaQueryWrapper<OmOrderDetail> productWrapper = new LambdaQueryWrapper<OmOrderDetail>();
            productWrapper.eq(OmOrderDetail::getMainId,id);
            productWrapper.eq(OmOrderDetail::getIzDelete,0);
            List<OmOrderDetail> productList = productService.list(productWrapper);
            productList.forEach( product ->{
                product.setDeleteInfo();
                productService.updateById(product);
            });
            //删除部件表
            LambdaQueryWrapper<OmOrderPart> partWrapper = new LambdaQueryWrapper<OmOrderPart>();
            partWrapper.eq(OmOrderPart::getMainId,id);
            partWrapper.eq(OmOrderPart::getIzDelete,0);
            List<OmOrderPart> partList = partService.list(partWrapper);
            partList.forEach( part ->{
                part.setDeleteInfo();
                partService.updateById(part);
            });
            //删除材料表
            LambdaQueryWrapper<OmOrderMaterial> materialWrapper = new LambdaQueryWrapper<>();
            materialWrapper.eq(OmOrderMaterial::getMainId,id);
            materialWrapper.eq(OmOrderMaterial::getIzDelete,0);
            List<OmOrderMaterial> materialList = materialService.list(materialWrapper);
            materialList.forEach( material ->{
                material.setDeleteInfo();
                materialService.updateById(material);
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseResult.success();
    }
    /**
     * 得到所有主要数据通过id
     *
     * @param id id
     * @return {@link TableResult}
     */
    @Override
    public TableResult<OmOrderMain> getAllMainDataById(String id) {
        TableResult<OmOrderMain> result = new TableResult<>();
        OmOrderMain main = this.getById(id);
        //获取所有产品数据
        LambdaQueryWrapper<OmOrderDetail> productWrapper = new LambdaQueryWrapper<OmOrderDetail>();
        productWrapper.eq(OmOrderDetail::getMainId,id);
        productWrapper.eq(OmOrderDetail::getIzDelete,0);
        List<OmOrderDetail> productList = productService.list(productWrapper);
        //获取所有部件数据
        LambdaQueryWrapper<OmOrderPart> partWrapper = new LambdaQueryWrapper<OmOrderPart>();
        partWrapper.eq(OmOrderPart::getMainId,id);
        partWrapper.eq(OmOrderPart::getIzDelete,0);
        List<OmOrderPart> partList = partService.list(partWrapper);
        //获取所有材料数据
        LambdaQueryWrapper<OmOrderMaterial> materialWrapper = new LambdaQueryWrapper<>();
        materialWrapper.eq(OmOrderMaterial::getMainId,id);
        materialWrapper.eq(OmOrderMaterial::getIzDelete,0);
        List<OmOrderMaterial> materialList = materialService.list(materialWrapper);
        //按recordId升序排序
        List<OmOrderMaterial> sortedMaterialList = materialList
                .stream()
                //排序第一字段：产品表ID，同一产品下的材料记录放一起
                .sorted(Comparator.comparing(OmOrderMaterial::getDetailId)
                        //排序第二字段：部件表ID，没有部件表ID的放前面
                        .thenComparing(OmOrderMaterial::getPartId,Comparator.nullsFirst(String::compareTo)))
                .collect(Collectors.toList());
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("main",main);
        dataMap.put("productList",productList);
        dataMap.put("partList",partList);
        dataMap.put("materialList",sortedMaterialList);
        result.setData(dataMap);
        return result;
    }

}
