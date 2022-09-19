package com.web.om.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.common.util.ResponseResult;
import com.common.util.TableResult;
import com.web.om.dto.*;
import com.web.om.entity.*;

import java.util.List;

/**
 *
 * @author caihuan
 * @since 2022-06-28
 */
public interface IOmOrderMainService extends IService<OmOrderMain> {

    IPage<OmOrderMainDTO> getMainList(OmOrderMainDTO query, IPage<OmOrderMainDTO> page) throws Exception;

    List<OmOrderMainDTO> getDetailList(OmOrderMainDTO query) throws Exception;

    /**
     * 保存委外订单到mes数据库
     *
     * @return {@link ResponseResult}
     */
    ResponseResult saveToMes(OmOrderMain main,
                             List<OmOrderDetail> productList,
                             List<OmOrderPart> partList,
                             List<OmOrderMaterial> materialList);

    /**
     * mes中更新,因为是先删除所有子表记录再插入，所以性能较差
     *
     * @param main         主表
     * @param productList  产品列表
     * @param partList     部件列表
     * @param materialList 材料列表
     * @return {@link ResponseResult}
     */
    ResponseResult updateToMes(OmOrderMain main,
                               List<OmOrderDetail> productList,
                               List<OmOrderPart> partList,
                               List<OmOrderMaterial> materialList);


    /**
     * 通过id作废委外订单
     *
     * @param id id
     * @return {@link TableResult}<{@link OmOrderMain}>
     */
    ResponseResult deleteMainById(String id);

    /**
     * 得到所有主要数据通过id
     *
     * @param id id
     * @return {@link TableResult}
     */
    TableResult<OmOrderMain> getAllMainDataById(String id);

    /**
     * 审核数据
     *
     * @return {@link ResponseResult}
     */
    ResponseResult audit(OmMoMain omProductPo, List<OmProductVM> list, List<OmProductVM> listDetail, OmOrderMain mesMain, List<OmOrderDetail> mesProduct, List<OmOrderMaterial> mesMaterialList) throws Exception;


    /**
     * 变更
     *
     * @param main         主表
     * @param productList  产品列表
     * @param materialList 材料列表
     * @return {@link ResponseResult}
     */
    ResponseResult change(OmOrderMain main,
                           List<OmOrderDetail> productList,
                           List<OmOrderMaterial> materialList) ;
}



