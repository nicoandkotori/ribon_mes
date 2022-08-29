package com.task;


import com.common.util.DateUtil;
import com.modules.data.mybatis.DBTypeEnum;
import com.modules.data.mybatis.DbContextHolder;
import com.sun.corba.se.impl.protocol.giopmsgheaders.IORAddressingInfo;
import com.web.basicinfo.service.IOaInventoryApplyService;
import com.web.sa.service.IOaSaleContractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableScheduling
public class Scheduler {

    @Autowired
    private IOaSaleContractService oaSaleContractService;

    @Autowired
    private IOaInventoryApplyService oaInventoryApplyService;

    private static final Logger log = LoggerFactory.getLogger(Scheduler.class);



    /**
     * 功能：同步OA合同数据到U8
     * 定时：从第1分钟开始，每10分钟执行一次
     */
//    @Scheduled(cron = "0 1/10 * * * ? ")
    @Scheduled(cron = "0 0/5 * * * ? ")
    public void syncSaleOrder() {
        log.info("定时任务(同步订单)执行时间：" + DateUtil.getDateStr(new Date(), "yyyy-MM-dd hh:mm:ss"));
        try {
            //DB2是U8连接
            DbContextHolder.setDbType(DBTypeEnum.db2);
            oaSaleContractService.syncSaleContractData();
            //调用Service方法
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Scheduled(cron = "0 0/5 * * * ? ")
    public void syncSaleOrderEdit() {
        log.info("定时任务(同步订单修改的情况)执行时间：" + DateUtil.getDateStr(new Date(), "yyyy-MM-dd hh:mm:ss"));
        try {
            //DB2是U8连接
            DbContextHolder.setDbType(DBTypeEnum.db2);
            oaSaleContractService.syncSaleContractDataEdit();
            //调用Service方法
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }


    @Scheduled(cron = "0 0/5 * * * ? ")
    public void syncSaleOrderDelete() {
        log.info("定时任务(同步订单删除的情况)执行时间：" + DateUtil.getDateStr(new Date(), "yyyy-MM-dd hh:mm:ss"));
        try {
            //DB2是U8连接
            DbContextHolder.setDbType(DBTypeEnum.db2);
            oaSaleContractService.syncSaleContractDataDelete();
            //调用Service方法
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 功能：同步OA存貨U8
     * 定时：从第0分钟开始，每20分钟执行一次
     */
    @Scheduled(cron = "0 0/5 * * * ? ")
    public void syncInventory() {
        log.info("定时任务(同步存货)执行时间：" + DateUtil.getDateStr(new Date(), "yyyy-MM-dd hh:mm:ss"));
        try {
            //DB2是U8连接
            DbContextHolder.setDbType(DBTypeEnum.db2);
            oaInventoryApplyService.syncInventoryApplyData();
            //调用Service方法
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
