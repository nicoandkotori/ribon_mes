package com.web.event;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;

/**

 *                             //发布更新 库存事件
 *                             FineEventDTO<SaleOrderContent> dto = new FineEventDTO();
 *                             dto.setDbTypeEnum(DBTypeEnum.dbsource);
 *                             dto.setOperType(EnumEventTypeUtils..ADD);
 *                             dto.setEventType(EnumEventTypeUtils.EVENT_TYPE.SALEORDER);
 *                             dto.setUpdateQty(tpPurchaseArrivalB.OPER_TYPE.ADD);
 *
 *                             PurchaseArrivalContent content=new PurchaseArrivalContent();
 *                             BeanUtil.copyProperties(tpPurchaseArrivalB,content);
 *                             dto.setBizContent(content);
 *
 *
 *                             //发布事件，异步执行
 *                             applicationContext.publishEvent(new FineEvent(applicationContext, dto));
 */

public class FineEvent extends ApplicationEvent {

    private FineEventDTO  fineEventDTO;

    public FineEventDTO getFineEventDTO() {
        return fineEventDTO;
    }

    public void setFineEventDTO( FineEventDTO fineEventDTO) {
        this.fineEventDTO = fineEventDTO;
    }

    public FineEvent(ApplicationContext source,FineEventDTO fineEventDTO){
        super(source);
        this.fineEventDTO=fineEventDTO;
    }
}
