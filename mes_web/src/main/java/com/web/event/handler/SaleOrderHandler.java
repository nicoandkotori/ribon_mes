package com.web.event.handler;


import com.common.util.R;
import com.web.event.FineEventHandler;
import com.web.event.FineEventDTO;
import com.web.event.content.SaleOrderContent;
import org.springframework.stereotype.Component;

/**
 * SALEORDER事件处理器
 *
 **/

@Component("SALEORDER")
public class SaleOrderHandler extends  BaseHandler implements FineEventHandler<SaleOrderContent> {


    @Override
    public R execute(FineEventDTO<SaleOrderContent> dto) {

        SaleOrderContent content = dto.getBizContent();

        //业务处理逻辑


        //销售订单只有 更新 一条记录
         // R r= tpNewCurrentStockService.updateStock(flag,tpNewCurrentStock);


        return R.ok();
    }
}
