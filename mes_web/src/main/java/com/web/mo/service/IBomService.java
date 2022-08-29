package com.web.mo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.common.util.ResponseResult;
import com.web.mo.dto.BomDTO;
import com.web.mo.entity.Bom;
import com.web.mo.entity.TestSo;
import com.web.mo.entity.TestSoList;
import com.web.mo.entity.U8MpsNetdemand;
import com.web.system.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @author caihuan
 * @since 2022-06-28
 */
public interface IBomService extends IService<Bom> {
    IPage<BomDTO> getList(BomDTO query, IPage<BomDTO> page) throws Exception;
    List<BomDTO> getBomListByParent(String invCode) throws Exception;
    List<BomDTO> getInvChildListFirst(String invCode) throws Exception;

    List<BomDTO> selectByMenu(String invCode) throws Exception;
    List<BomDTO> selectParentMenuList(String invCode) throws Exception;

    //保存bom
    ResponseResult save(BomDTO main, List<BomDTO> detailList);

    //导入bom
    ResponseResult bomImportU8(List<BomDTO> list) throws Exception;

    void delete(Integer id) throws Exception;
}
