package com.web.om.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.util.*;
import com.modules.data.mybatis.DBTypeEnum;
import com.modules.data.mybatis.DbContextHolder;
import com.web.basicinfo.entity.Vendor;
import com.web.basicinfo.service.IVendorService;
import com.web.common.controller.BasicController;
import com.web.om.dto.OmOrderMaterialDTO;
import com.web.om.dto.OmOrderPartDTO;
import com.web.om.dto.OmProductVM;
import com.web.om.entity.OmMoMain;
import com.web.om.entity.OmOrderMain;
import com.web.om.entity.OmOrderPart;
import com.web.om.service.IOmMoMainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;


/**
 * Created by caihuan on 2021-04-13.
 */
@RestController
@RequestMapping(value = "/om/metalworkcommittee")
public class MetalWorkCommitteeController extends BasicController {

    @Autowired
    private IOmMoMainService omMainService;

    @Autowired
    private IVendorService vendorService;

    /**
     * 查询最大的单据号
     */
    @RequestMapping(value = "/getmaxcode")
    public ResponseResult getMaxCode() throws Exception {
        ResponseResult result = new ResponseResult();
        try{
            DbContextHolder.setDbType(DBTypeEnum.db2);
            String head = "OM" + DateUtil.getDateStr(new Date(), "yyyyMMdd");
            String sortNum = "001";
            int sortNumLength = 3;

            LambdaQueryWrapper<OmMoMain> select=new LambdaQueryWrapper<>();
            select.select(OmMoMain::getCcode)
                    .like(OmMoMain::getCcode, head);
            select.orderByDesc(OmMoMain::getCcode);
            List<OmMoMain> mainList = omMainService.list(select);
            if(mainList.size() > 0){
                String maxCode = mainList.get(0).getCcode();
                if (CustomStringUtils.isNotBlank(maxCode)) {
                    sortNum = Integer.parseInt(maxCode.substring(maxCode.length() - sortNumLength)) + 1 + "";
                    while (sortNum.length() < sortNumLength) {
                        sortNum = "0" + sortNum;
                    }
                }
            }
            result.setResult( head + sortNum);
            result.setSuccess(true);
        }catch(Exception e){

            e.printStackTrace();
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        }
        return result;

    }

    /**
     * bom列表分页数据
     * @param page 当前页
     * @param rows 每页数据条数
     * @param querystr 查询参数
     * @return
     */
    @RequestMapping(value = "/getmainlistbypage")
    public TableResult<OmProductVM> findPage(int page, int rows, String querystr) {
        DbContextHolder.setDbType(DBTypeEnum.db2);
        TableResult<OmProductVM> result = new TableResult<>();
        IPage<OmProductVM> page1 = new Page<>(page, rows);
        IPage<OmProductVM> resultPage = new Page<>(page, rows);
        try{
            OmProductVM data = JSON.parseObject(querystr, OmProductVM.class);
            if(data == null){
                data = new OmProductVM();
            }
            resultPage =  omMainService.getMainList(data,page1);

        }catch(Exception e){

            e.printStackTrace();
        }
        result.setRecords(resultPage.getTotal());
        result.setTotal((int)Math.ceil(resultPage.getTotal()/(double)resultPage.getSize()));
        result.setRows(resultPage.getRecords());
        return result;

    }


    /**
     * 根据id查询到款单主表信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/getbyid")
    public OmMoMain getById(Integer id) {
        DbContextHolder.setDbType(DBTypeEnum.db2);
        try {
            if (CustomStringUtils.isBlank(id)) {
                throw new Exception("参数异常");
            }
            OmMoMain m =omMainService.getById(id);
            if(m!=null) {
                Vendor vendor= vendorService.getById(m.getCvencode());
                if(vendor!=null)
                {
                    m.setCvenname(vendor.getCvenname());
                    m.setCvenabbname(vendor.getCvenabbname());
                    m.setCvenperson(vendor.getCvenperson());
                    m.setCvenphone(vendor.getCvenphone());
                }
            }
            return m;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 查询明细表
     * @param id
     * @return
     */
    @RequestMapping(value = "/getdetaillist")
    @ResponseBody
    public List<OmProductVM> getDetailList(Integer id){
        List<OmProductVM> list=new ArrayList<>();
        try{
            DbContextHolder.setDbType(DBTypeEnum.db2);
            if(CustomStringUtils.isNotBlank(id))
            {
                OmProductVM m=new OmProductVM();
                m.setMoid(id);
                list=omMainService.getDetailList(m);
                if(list!=null)
                {
                    int idd=-1;
                    for(OmProductVM q: list)
                    {
                        if(q.getModetailsid()==null)
                        {
                            q.setMomaterialsid(idd);
                            idd--;
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }




    /**
     * 查询明细表
     * @param id
     * @return
     */
    @RequestMapping(value = "/getdetaillist1")
    @ResponseBody
    public Map<String, Object> getDetailList1(String id){

        Map<String, Object> map = new HashMap<String, Object>();
        try{
            DbContextHolder.setDbType(DBTypeEnum.db2);
            if(CustomStringUtils.isNotBlank(id)) {
                OmProductVM m=new OmProductVM();
                m.setMoid(Integer.valueOf(id));
                map =omMainService.getDetailList1(m);
            }
            else{
                map.put("mData", null);
                map.put("mDatas", null);
            }

        }catch (Exception e){
            map.put("mData", null);
            map.put("mDatas", null);
            e.printStackTrace();
        }
        return map;
    }
    /**
     * 根据编码查询历史最新的记录
     * @param cinvcode
     * @return
     */
    @RequestMapping(value = "/gethistorybycinvcode1")
    @ResponseBody
    public Map<String, Object> getHistoryByCinvcode1(String cinvcode){
        Map<String, Object> map = new HashMap<String, Object>();
        try{
            DbContextHolder.setDbType(DBTypeEnum.db2);
            OmProductVM query=new OmProductVM();
            query.setCinvcode(cinvcode);
            map=omMainService.getHistoryByCinvcode1(query);


        }catch (Exception e){
            map.put("mData", null);
            map.put("mDatas", null);
            e.printStackTrace();
        }
        return map;
    }





    /**
     * 删除明细数据
     * @param moid
     * @return
     */
    @RequestMapping(value = "/deletebydetailmainid")
    @ResponseBody
    public ResponseResult deletebydetailmainid(Integer modetailsid,Integer moid,Integer momaterialsid){
        ResponseResult result = new ResponseResult();
        try{
            DbContextHolder.setDbType(DBTypeEnum.db2);
            result=  omMainService.deleteByDetailMainId(moid,modetailsid,momaterialsid);

        }catch (Exception e){
            result.setSuccess(false);
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 删除明细数据
     * @param moid
     * @return
     */
    @RequestMapping(value = "/deletebydetailsubid")
    @ResponseBody
    public ResponseResult deletebydetailsubid(Integer modetailsid,Integer moid,Integer momaterialsid){
        ResponseResult result = new ResponseResult();
        try{
            DbContextHolder.setDbType(DBTypeEnum.db2);
            result=  omMainService.deleteByDetailSubId(moid,modetailsid,momaterialsid);

        }catch (Exception e){
            result.setSuccess(false);
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }



    /**
     * 保存
     * @param mData
     * @param mDatas

     * @return
     */
    @RequestMapping(value = "/save1")
    @ResponseBody
    public ResponseResult save1(String mData,String mDatas,String mDataDetail) {
        ResponseResult result = new ResponseResult();
        try{
            DbContextHolder.setDbType(DBTypeEnum.db2);
            if (CustomStringUtils.isBlank(mData)) {
                result.setSuccess(false);
                result.setMsg("参数异常");
            }
            OmMoMain m = JSON.parseObject(mData, OmMoMain.class);
            if(CustomStringUtils.isBlank(m.getMoid()))
            {
                m.setCcode(getMaxCode().getResult().toString());
            }
            List<OmProductVM> list = JSON.parseArray(mDatas, OmProductVM.class);
            List<OmProductVM> listDetail = JSON.parseArray(mDataDetail, OmProductVM.class);
            result=omMainService.save1(m,list,listDetail);
        }catch (Exception e){
            result.setSuccess(false);
            result.setMsg(e.getMessage());
        }
        return result;
    }



    /**
     * 删除明细数据
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public ResponseResult delete(Integer id){
        ResponseResult result = new ResponseResult();
        try{
            DbContextHolder.setDbType(DBTypeEnum.db2);
            result=  omMainService.delete(id);

        }catch (Exception e){
            result.setSuccess(false);
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 审核
     * @param id
     * @return
     */
    @RequestMapping(value = "/check")
    @ResponseBody
    public ResponseResult check(Integer id){
        ResponseResult result = new ResponseResult();
        try{
            DbContextHolder.setDbType(DBTypeEnum.db2);
            result=  omMainService.check(id);

        }catch (Exception e){
            result.setSuccess(false);
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 弃审
     * @param id
     * @return
     */
    @RequestMapping(value = "/uncheck")
    @ResponseBody
    public ResponseResult unCheck(Integer id){
        ResponseResult result = new ResponseResult();
        try{
            DbContextHolder.setDbType(DBTypeEnum.db2);
            result=  omMainService.unCheck(id);

        }catch (Exception e){
            result.setSuccess(false);
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

}
