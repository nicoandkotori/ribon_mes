package com.web.ar.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.util.ResponseResult;
import com.web.ar.entity.CostSimPrice;
import com.web.ar.entity.CostSimReport;
import com.web.ar.mapper.CostSimPriceMapper;
import com.web.ar.mapper.CostSimReportMapper;
import com.web.ar.mapper.TestAccListMapper;
import com.web.ar.service.ICostSimPriceService;
import com.web.ar.service.ICostSimReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author caihuan
 * @since 2022-06-28
 */
@Service
public class CostSimReportServiceImpl extends ServiceImpl<CostSimReportMapper, CostSimReport> implements ICostSimReportService {
    @Autowired
    CostSimPriceMapper costSimPriceMapper;

    @Autowired
    CostSimReportMapper costSimReportMapper;
    @Value("${account.u8DB}")
    private String u8DB;
    @Override
    public List<CostSimReport> getList(CostSimReport query){
        return costSimReportMapper.getList(query);
    }

    @Override
    public List<CostSimReport> getMetalList(CostSimReport query){
        return costSimReportMapper.getMetalList(query,u8DB);
    }

    @Override
    public List<CostSimReport> getMetalListForSave(CostSimReport query){
        return costSimReportMapper.getMetalListForSave(query,u8DB);
    }
    @Override
    public List<CostSimReport> getDetailList(CostSimReport query){
        return costSimReportMapper.getDetailList(query,u8DB);
    }
    @Override
    public List<CostSimReport> getMetalDetailList(CostSimReport query){
        return costSimReportMapper.getMetalDetailList(query,u8DB);
    }
    /**
     * 删除全部数据
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult delete() throws Exception{
        ResponseResult result = new ResponseResult();
        try {
            costSimReportMapper.deleteall();



        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }

    /**
     * 插入订单数据

     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult insertSo(CostSimReport query) throws Exception{
        ResponseResult result = new ResponseResult();
        try{
            costSimReportMapper.insertSo(query,u8DB);

        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }
    /**
     * 插入订单数据

     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult insertRd(CostSimReport query) throws Exception{
        ResponseResult result = new ResponseResult();
        try{
            costSimReportMapper.insertRd(query,u8DB);

        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }


    /**
     * 插入订单数据

     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult insertRd1(CostSimReport query) throws Exception{
        ResponseResult result = new ResponseResult();
        try{
            costSimReportMapper.insertRd1(query,u8DB);

        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult saveprice(List<CostSimReport> list ) throws Exception{
        ResponseResult result = new ResponseResult();
        try{

            for(CostSimReport m:list)
            {
                CostSimPrice  costSimPrice=costSimPriceMapper.getInfo(m);
                if(costSimPrice!=null)
                {
                    costSimPrice.setSaleAmount(m.getSaleAmount());
                    int n=costSimPriceMapper.updateByPrimaryKey(costSimPrice);
                }
                else
                {
                    if(m.getSaleAmount()!=null)
                    {
                        costSimPrice=new CostSimPrice();
                        costSimPrice.setSaleAmount(m.getSaleAmount());
                        costSimPrice.setCustCode(m.getCustCode());
                        costSimPrice.setId(UUID.randomUUID().toString());
                        costSimPrice.setImonth(m.getImonth());
                        costSimPrice.setIyear(m.getIyear());
                        costSimPrice.setInvCode(m.getInvCode());
                        costSimPrice.setOrderNo(m.getOrderNo());
                        costSimPrice.setWhName(m.getWhName());
                        int n=costSimPriceMapper.insertSelective(costSimPrice);
                    }

                }
            }
        }catch (Exception e){
            throw e;
        }
        return result;
    }
}
