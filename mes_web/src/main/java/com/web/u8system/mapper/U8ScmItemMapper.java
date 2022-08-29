package com.web.u8system.mapper;

import com.web.u8system.entity.U8ScmItem;
import org.apache.ibatis.annotations.Param;

/**
 * Created by sunyin on 2019/5/7.
 */
public interface U8ScmItemMapper {
    U8ScmItem selectByInvCode(@Param("cInvCode") String cInvCode, @Param("cfree1") String cfree1,@Param("cfree2") String cfree2, @Param("cfree3") String cfree3);
    int insert(U8ScmItem record);

}
