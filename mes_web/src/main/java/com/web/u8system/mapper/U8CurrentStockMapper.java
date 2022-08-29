package com.web.u8system.mapper;


import com.web.u8system.entity.U8CurrentStock;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * Created by sunyin on 2019/4/19.
 */
public interface U8CurrentStockMapper {
    int selectCount(@Param("whCode") String whCode, @Param("invCode") String invCode, @Param("cfree1") String cfree1, @Param("cfree2") String cfree2, @Param("cfree3") String cfree3, @Param("cBatch") String cBatch);
    U8CurrentStock selectStock(@Param("whCode") String whCode, @Param("invCode") String invCode,@Param("cfree1") String cfree1, @Param("cfree2") String cfree2, @Param("cfree3") String cfree3,@Param("cBatch") String cBatch);
    int insertSelective(U8CurrentStock record);
    int updateQuanity(@Param("whCode") String whCode, @Param("invCode") String invCode ,@Param("cfree1") String cfree1,@Param("cfree2") String cfree2, @Param("cfree3") String cfree3, @Param("cBatch") String cBatch, @Param("iQuantity") BigDecimal iQuantity, @Param("iNum") BigDecimal iNum);
    int updateQuanity1(@Param("whCode") String whCode, @Param("invCode") String invCode ,@Param("cfree1") String cfree1,@Param("cfree2") String cfree2, @Param("cfree3") String cfree3, @Param("cBatch") String cBatch, @Param("iQuantity") BigDecimal iQuantity, @Param("iNum") BigDecimal iNum);
}
