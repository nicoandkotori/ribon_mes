package com.web.pd.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.common.util.ResponseResult;
import com.common.util.TableResult;
import com.web.om.dto.OmOrderMainDTO;
import com.web.om.entity.OmOrderMain;
import com.web.pd.dto.PdOrderDetailDTO;
import com.web.pd.dto.PdOrderMainDTO;
import com.web.pd.entity.PdOrderDetail;
import com.web.pd.entity.PdOrderMain;
import com.baomidou.mybatisplus.extension.service.IService;
import com.web.pd.entity.PdOrderMaterial;
import com.web.pd.entity.PdOrderPart;

import java.util.List;

/**
* @author mijiahao
* @description 针对表【pd_order_main】的数据库操作Service
* @createDate 2022-09-27 08:40:57
*/
public interface PdOrderMainService extends IService<PdOrderMain> {

    IPage<PdOrderMainDTO> getMainList(PdOrderMainDTO query, IPage<PdOrderMainDTO> page) throws Exception;

    /**
     * 保存委外订单到mes数据库
     *
     * @return {@link ResponseResult}
     */
    ResponseResult saveToMes(PdOrderMain main,
                             List<PdOrderDetail> productList,
                             List<PdOrderPart> partList,
                             List<PdOrderMaterial> materialList);

    /**
     * mes中更新,因为是先删除所有子表记录再插入，所以性能较差
     *
     * @param main         主表
     * @param productList  产品列表
     * @param partList     部件列表
     * @param materialList 材料列表
     * @return {@link ResponseResult}
     */
    ResponseResult updateToMes(PdOrderMain main,
                               List<PdOrderDetail> productList,
                               List<PdOrderPart> partList,
                               List<PdOrderMaterial> materialList);


    /**
     * 通过id作废委外订单
     *
     * @param mesId id
     * @return {@link TableResult}<{@link OmOrderMain}>
     */
    ResponseResult deleteMainById(String mesId);

    /**
     * 得到所有主要数据通过id
     *
     * @param id id
     * @return {@link TableResult}
     */
    TableResult<PdOrderMain> getAllMainDataById(String id);

    /**
     * 查询主表join产品表的数据
     *
     * @param query 查询
     * @return {@link List}<{@link OmOrderMainDTO}>
     * @throws Exception 异常
     */
    List<PdOrderDetailDTO> getMainAndProductList(PdOrderMainDTO query) throws Exception;

}
