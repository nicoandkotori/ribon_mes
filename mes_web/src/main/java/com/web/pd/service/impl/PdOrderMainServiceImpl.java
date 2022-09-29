package com.web.pd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.util.ResponseResult;
import com.common.util.SnowFlakeUtils;
import com.common.util.TableResult;
import com.web.pd.dto.PdOrderDetailDTO;
import com.web.pd.dto.PdOrderMainDTO;
import com.web.pd.entity.PdOrderDetail;
import com.web.pd.entity.PdOrderMain;
import com.web.pd.entity.PdOrderMaterial;
import com.web.pd.entity.PdOrderPart;
import com.web.pd.service.PdOrderDetailService;
import com.web.pd.service.PdOrderMainService;
import com.web.pd.mapper.PdOrderMainMapper;
import com.web.pd.service.PdOrderMaterialService;
import com.web.pd.service.PdOrderPartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
* @author mijiahao
* @description 针对表【pd_order_main】的数据库操作Service实现
* @createDate 2022-09-27 08:40:57
*/
@Service
public class PdOrderMainServiceImpl extends ServiceImpl<PdOrderMainMapper, PdOrderMain>
    implements PdOrderMainService{

    @Autowired
    PdOrderMainMapper pdOrderMainMapper;
    
    @Autowired
    PdOrderDetailService productService;

    @Autowired
    PdOrderPartService partService;

    @Autowired
    PdOrderMaterialService materialService;

    @Override
    public IPage<PdOrderMainDTO> getMainList(PdOrderMainDTO query, IPage<PdOrderMainDTO> page) throws Exception {
        page.setRecords(pdOrderMainMapper.findMainByPage(page, query));
        return page;
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
    public ResponseResult saveToMes(PdOrderMain main, List<PdOrderDetail> productList, List<PdOrderPart> partList, List<PdOrderMaterial> materialList) {
        ResponseResult result = null;
        try {
            result = new ResponseResult();
            //生成主表ID
            String mainId = String.valueOf(SnowFlakeUtils.getFlowIdInstance().nextId());
            result.setResult(mainId);
            main.setId(mainId);
            main.setCreateInfo(mainId);
            main.setStatusId("未审核");
            this.save(main);
            //产品表ID的map，key是recordID，value是产品记录的ID
            Map<String, String> productIdMap = new HashMap<>();
            //部件表的ID的mao，key是partRowId,value是部件记录的ID
            Map<String, String> partIdMap = new HashMap<>();
            //先遍历产品列表
            productList.forEach(product -> {
                String productId = String.valueOf(SnowFlakeUtils.getFlowIdInstance().nextId());
                product.setId(productId);
                product.setMainId(mainId);
                product.setCreateInfo(productId);
                productIdMap.put(product.getRecordId(), productId);
                productService.save(product);
            });
            //部件表不是必传的
            if (partList != null) {
                //再遍历部件列表
                partList.forEach(part -> {
                    String partId = String.valueOf(SnowFlakeUtils.getFlowIdInstance().nextId());
                    part.setId(partId);
                    part.setMainId(mainId);
                    part.setDetailId(productIdMap.get(part.getRecordId()));
                    part.setCreateInfo(partId);
                    partIdMap.put(part.getPartRowId(), partId);
                    partService.save(part);
                });
            }
            //最后遍历材料列表
            materialList.forEach(material -> {
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
    public ResponseResult updateToMes(PdOrderMain main, List<PdOrderDetail> productList, List<PdOrderPart> partList, List<PdOrderMaterial> materialList) {
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
            LambdaQueryWrapper<PdOrderDetail> productWrapper = new LambdaQueryWrapper<PdOrderDetail>();
            productWrapper.eq(PdOrderDetail::getMainId, mainId);
            List<PdOrderDetail> deleteProductList = productService.list(productWrapper);
            List<String> deleteProductIdList = new ArrayList<>();
            deleteProductList.forEach(product -> {
                deleteProductIdList.add(product.getId());
            });
            productService.removeByIds(deleteProductIdList);
            //删除部件表
            LambdaQueryWrapper<PdOrderPart> partWrapper = new LambdaQueryWrapper<PdOrderPart>();
            partWrapper.eq(PdOrderPart::getMainId, mainId);
            List<PdOrderPart> deletePartList = partService.list(partWrapper);
            if (deletePartList.size() != 0) {
                List<String> deletePartIdList = new ArrayList<>();
                deletePartList.forEach(part -> {
                    deletePartIdList.add(part.getId());
                });

                partService.removeByIds(deletePartIdList);
            }
            //删除材料表
            LambdaQueryWrapper<PdOrderMaterial> materialWrapper = new LambdaQueryWrapper<>();
            materialWrapper.eq(PdOrderMaterial::getMainId, mainId);
            List<PdOrderMaterial> deleteMaterialList = materialService.list(materialWrapper);
            if (deleteMaterialList.size() != 0) {
                List<String> deleteMaterialIdList = new ArrayList<>();
                deleteMaterialList.forEach(material -> {
                    deleteMaterialIdList.add(material.getId());
                });
                materialService.removeByIds(deleteMaterialIdList);
            }
            //产品表ID的map，key是recordID，value是产品记录的ID
            Map<String, String> productIdMap = new HashMap<>();
            //部件表的ID的mao，key是partRowId,value是部件记录的ID
            Map<String, String> partIdMap = new HashMap<>();
            //先遍历产品列表
            productList.forEach(product -> {
                String productId = String.valueOf(SnowFlakeUtils.getFlowIdInstance().nextId());
                product.setId(productId);
                product.setMainId(mainId);
                product.setCreateInfo(productId);
                productIdMap.put(product.getRecordId(), productId);
            });
            //该方法的效率很低，因为底层还是一个一个insert
            productService.saveBatch(productList);
            //部件表不是必传的
            if (partList != null) {
                //再遍历部件列表
                partList.forEach(part -> {
                    String partId = String.valueOf(SnowFlakeUtils.getFlowIdInstance().nextId());
                    part.setId(partId);
                    part.setMainId(mainId);
                    part.setDetailId(productIdMap.get(part.getRecordId()));
                    part.setCreateInfo(partId);
                    partIdMap.put(part.getPartRowId(), partId);
                });
            }
            partService.saveBatch(partList);
            //最后遍历材料列表
            materialList.forEach(material -> {
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
     * @param mesId id
     * @return {@link ResponseResult}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteMainById(String mesId) {
        try {
            PdOrderMain mesMain = this.getById(mesId);
            mesMain.setDeleteInfo();
            this.updateById(mesMain);
            //删除产品表
            LambdaQueryWrapper<PdOrderDetail> productWrapper = new LambdaQueryWrapper<PdOrderDetail>();
            productWrapper.eq(PdOrderDetail::getMainId, mesId);
            productWrapper.eq(PdOrderDetail::getIzDelete, 0);
            List<PdOrderDetail> productList = productService.list(productWrapper);
            productList.forEach(product -> {
                product.setDeleteInfo();
                productService.updateById(product);
            });
            //删除部件表
            LambdaQueryWrapper<PdOrderPart> partWrapper = new LambdaQueryWrapper<PdOrderPart>();
            partWrapper.eq(PdOrderPart::getMainId, mesId);
            partWrapper.eq(PdOrderPart::getIzDelete, 0);
            List<PdOrderPart> partList = partService.list(partWrapper);
            partList.forEach(part -> {
                part.setDeleteInfo();
                partService.updateById(part);
            });
            //删除材料表
            LambdaQueryWrapper<PdOrderMaterial> materialWrapper = new LambdaQueryWrapper<>();
            materialWrapper.eq(PdOrderMaterial::getMainId, mesId);
            materialWrapper.eq(PdOrderMaterial::getIzDelete, 0);
            List<PdOrderMaterial> materialList = materialService.list(materialWrapper);
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
     * 得到所有主要数据通过id
     *
     * @param id id
     * @return {@link TableResult}
     */
    @Override
    public TableResult<PdOrderMain> getAllMainDataById(String id) {
        TableResult<PdOrderMain> result = new TableResult<>();
        PdOrderMain main = this.getById(id);
        //获取所有产品数据
        LambdaQueryWrapper<PdOrderDetail> productWrapper = new LambdaQueryWrapper<PdOrderDetail>();
        productWrapper.eq(PdOrderDetail::getMainId, id);
        productWrapper.eq(PdOrderDetail::getIzDelete, 0);
        List<PdOrderDetail> productList = productService.list(productWrapper);
        //获取所有部件数据
        LambdaQueryWrapper<PdOrderPart> partWrapper = new LambdaQueryWrapper<PdOrderPart>();
        partWrapper.eq(PdOrderPart::getMainId, id);
        partWrapper.eq(PdOrderPart::getIzDelete, 0);
        List<PdOrderPart> partList = partService.list(partWrapper);
        //获取所有材料数据
        LambdaQueryWrapper<PdOrderMaterial> materialWrapper = new LambdaQueryWrapper<>();
        materialWrapper.eq(PdOrderMaterial::getMainId, id);
        materialWrapper.eq(PdOrderMaterial::getIzDelete, 0);
        List<PdOrderMaterial> materialList = materialService.list(materialWrapper);
        //按recordId升序排序
        List<PdOrderMaterial> sortedMaterialList = materialList
                .stream()
                //排序第一字段：产品表ID，同一产品下的材料记录放一起
                .sorted(Comparator.comparing(PdOrderMaterial::getDetailId)
                        //排序第二字段：部件表ID，没有部件表ID的放前面
                        .thenComparing(PdOrderMaterial::getPartId, Comparator.nullsFirst(String::compareTo)))
                .collect(Collectors.toList());
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("main", main);
        dataMap.put("productList", productList);
        dataMap.put("partList", partList);
        dataMap.put("materialList", sortedMaterialList);
        result.setData(dataMap);
        return result;
    }

    @Override
    public List<PdOrderDetailDTO> getMainAndProductList(PdOrderMainDTO query) throws Exception {
        return pdOrderMainMapper.getMainAndProductList(query);
    }
}




