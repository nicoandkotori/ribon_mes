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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Transactional(rollbackFor = RuntimeException.class)
    public ResponseResult saveToMes(OmOrderMain main, List<OmOrderProductDTO> productList, List<OmOrderPartDTO> partList, List<OmOrderMaterialDTO> materialList) {
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
                materialService.save(material);
            });

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * mes中更新
     *
     * @param main         主要
     * @param productList  产品列表
     * @param partList     部件列表
     * @param materialList 材料清单
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult updateToMes(OmOrderMain main, List<OmOrderProductDTO> productList, List<OmOrderPartDTO> partList, List<OmOrderMaterialDTO> materialList) {
        ResponseResult result = new ResponseResult();
        String mainId = main.getId();
        result.setResult(mainId);
        this.updateById(main);
        //删除产品表
        LambdaQueryWrapper<OmOrderDetail> productWrapper = new LambdaQueryWrapper<OmOrderDetail>();
        productWrapper.eq(OmOrderDetail::getMainId,mainId);
        List<OmOrderDetail> deleteProductList = productService.list(productWrapper);
        deleteProductList.forEach( product ->{
            product.setDeleteInfo();
            productService.updateById(product);
        });
        //删除部件表
        LambdaQueryWrapper<OmOrderPart> partWrapper = new LambdaQueryWrapper<OmOrderPart>();
        partWrapper.eq(OmOrderPart::getMainId,mainId);
        List<OmOrderPart> deletePartList = partService.list(partWrapper);
        deletePartList.forEach( part ->{
            part.setDeleteInfo();
            partService.updateById(part);
        });
        //删除材料表
        LambdaQueryWrapper<OmOrderMaterial> materialWrapper = new LambdaQueryWrapper<>();
        materialWrapper.eq(OmOrderMaterial::getMainId,mainId);
        List<OmOrderMaterial> deleteMaterialList = materialService.list(materialWrapper);
        deleteMaterialList.forEach( material ->{
            material.setDeleteInfo();
            materialService.updateById(material);
        });
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
            materialService.save(material);
        });
        return result;
    }

    /**
     * 通过id作废委外订单
     *
     * @param id id
     * @return {@link ResponseResult}
     */
    @Override
    public ResponseResult deleteMainById(String id) {
        //删除订单
        OmOrderMain main = this.getById(id);
        main.setDeleteInfo();
        this.updateById(main);
        //删除产品表
        LambdaQueryWrapper<OmOrderDetail> productWrapper = new LambdaQueryWrapper<OmOrderDetail>();
        productWrapper.eq(OmOrderDetail::getMainId,id);
        List<OmOrderDetail> productList = productService.list(productWrapper);
        productList.forEach( product ->{
            product.setDeleteInfo();
            productService.updateById(product);
        });
        //删除部件表
        LambdaQueryWrapper<OmOrderPart> partWrapper = new LambdaQueryWrapper<OmOrderPart>();
        partWrapper.eq(OmOrderPart::getMainId,id);
        List<OmOrderPart> partList = partService.list(partWrapper);
        partList.forEach( part ->{
            part.setDeleteInfo();
            partService.updateById(part);
        });
        //删除材料表
        LambdaQueryWrapper<OmOrderMaterial> materialWrapper = new LambdaQueryWrapper<>();
        materialWrapper.eq(OmOrderMaterial::getMainId,id);
        List<OmOrderMaterial> materialList = materialService.list(materialWrapper);
        materialList.forEach( material ->{
            material.setDeleteInfo();
            materialService.updateById(material);
        });
        return ResponseResult.success();
    }

}
