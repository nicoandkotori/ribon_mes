package com.web.u8system.util;


import com.modules.data.mybatis.DBTypeEnum;
import com.modules.data.mybatis.DbContextHolder;
import com.web.u8system.entity.U8CurrentStock;
import com.web.u8system.entity.U8ScmItem;
import com.web.u8system.mapper.U8CurrentStockMapper;
import com.web.u8system.mapper.U8ScmItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * 现存量
 * Created by sunyin on 2019/5/20.
 */
@Component
public class U8CurrentStockUtils {
    @Autowired
    private U8ScmItemMapper u8ScmItem1Mapper;

    @Autowired
    private U8CurrentStockMapper u8CurrentStock1Mapper;


    @Transactional(rollbackFor = Exception.class)
    public void updateQuanity( String whCode, String invCode, String cBatch,String cfree1,String cfree2,String cfree3, BigDecimal iQuantity, BigDecimal iNum, String addOrDel) throws Exception{
        DbContextHolder.setDbType(DBTypeEnum.db2);
        try{
            int n = 0;
            boolean bInsert = false;
            //更新现存量汇总表  结存数量（iQuantity）
            int count = u8CurrentStock1Mapper.selectCount(whCode, invCode,cfree1,cfree2,cfree3, cBatch);
            if (count > 0){
                if("add".equals(addOrDel)){
                    n = u8CurrentStock1Mapper.updateQuanity(whCode, invCode,cfree1, cfree2,cfree3,cBatch, iQuantity,iNum);
                }else if("del".equals(addOrDel)){
                    //判断库存量是否满足
                    U8CurrentStock currentStock = u8CurrentStock1Mapper.selectStock(whCode, invCode,cfree1,cfree2,cfree3, cBatch);
                    if(currentStock!=null)
                    {
                        if(currentStock.getiQuantity().compareTo(iQuantity)==-1)
                        {
                            throw new Exception("现存量不足！"+invCode+","+cBatch+","+cfree1+","+cfree2+","+cfree3);
                        }
                    }
                    n = u8CurrentStock1Mapper.updateQuanity1(whCode, invCode, cfree1,cfree2,cfree3,cBatch, iQuantity,iNum);
                }
            }else{
                bInsert = true;
            }

            if (bInsert){
                U8CurrentStock u8CurrentStock = newCurrentStock(whCode, invCode,cfree1, cfree2,cfree3,cBatch);
                u8CurrentStock.setiQuantity(iQuantity);
                u8CurrentStock.setiNum(iNum);
                u8CurrentStock.setfInQuantity(BigDecimal.ZERO);
                u8CurrentStock.setfAvaQuantity(BigDecimal.ZERO);

                if("del".equals(addOrDel)){
                    u8CurrentStock.setiQuantity(BigDecimal.ZERO.subtract(iQuantity));
                    u8CurrentStock.setfInQuantity(BigDecimal.ZERO);
                    if(u8CurrentStock.getiQuantity().compareTo(BigDecimal.ZERO)==-1)
                    {
                        throw new Exception("现存量不足！"+invCode+","+cBatch+","+cfree1+","+cfree2+","+cfree3);

                    }

                }

                n = insertCurrentStockAndGaugeValue(invCode, cfree1,cfree2,cfree3, u8CurrentStock);
                if (n <= 0){
                    throw new Exception("新增现存量表出错！");
                }
            }else{
                if (n <= 0){
                    throw new Exception("更新现存量表出错！");
                }
            }
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());

        }
    }
    private U8CurrentStock newCurrentStock(String whCode, String invCode,String cfree1,String cfree2,String cfree3,String cBatch){
        U8CurrentStock u8CurrentStock = new U8CurrentStock();
        u8CurrentStock.setcInvCode(invCode);
        u8CurrentStock.setCfree1(cfree1);
        u8CurrentStock.setCfree2(cfree2);
        u8CurrentStock.setCfree3(cfree3);
        u8CurrentStock.setcWhCode(whCode);
        u8CurrentStock.setcBatch(cBatch);
        u8CurrentStock.setiSoType(0);
        u8CurrentStock.setiSodid("");

        return u8CurrentStock;
    }

    @Transactional(rollbackFor = Exception.class)
    public int insertCurrentStockAndGaugeValue(String invCode,String cfree1, String cfree2,String cfree3, U8CurrentStock currentStock) throws Exception{
        DbContextHolder.setDbType(DBTypeEnum.db2);
        try {
            //查计算存量用临时表，存在，就直接使用， 否则新加一条记录到计算存量用临时表，再查出itemId
            Integer itemId = getScmItemId( invCode, cfree1,cfree2,cfree3);
            currentStock.setItemId(itemId);
            int n = 0;
            n = u8CurrentStock1Mapper.insertSelective(currentStock);
            return n;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }



    /**
     * 获取计算存量用临时表的id
     * @param invCode
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer getScmItemId(String invCode,String cfree1, String cfree2,String cfree3) throws Exception{
        DbContextHolder.setDbType(DBTypeEnum.db2);
        try {
            U8ScmItem item = null;
            item = u8ScmItem1Mapper.selectByInvCode(invCode,cfree1, cfree2,cfree3);


            Integer itemId = null;
            if (item != null) {
                itemId = item.getId();
            } else {
                //没有itemId  就新插入一条， 再查出
                item = new U8ScmItem();
                item.setcInvCode(invCode);
                item.setCfree1(cfree1);
                item.setCfree2(cfree2);
                item.setCfree3(cfree3);

                int n = u8ScmItem1Mapper.insert(item);
                if (n <= 0) {
                    throw new Exception("新增计算存量用临时表出错！");
                }
                item = u8ScmItem1Mapper.selectByInvCode(invCode,cfree1, cfree2,cfree3);

                if (item != null){
                    itemId = item.getId();
                }
            }

            return itemId;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }


}
