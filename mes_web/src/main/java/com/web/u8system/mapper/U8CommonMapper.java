package com.web.u8system.mapper;

import com.web.u8system.entity.U8Common;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Li on 2019/4/18.
 */
public interface U8CommonMapper {
    U8Common GetU8ID(U8Common query);

    int insertU8Identity(U8Common query);

    int UpdateU8FatherID(U8Common query);

    int UpdateU8ChildID(U8Common query);
    int UpdateU8ID(U8Common query);



    U8Common GetVoucherNumber(@Param("main") U8Common query);
    U8Common GetVouchercontrapose(@Param("main")U8Common query);
    U8Common GetGlide(@Param("main")U8Common query);

    int UpdateGlide(@Param("main")U8Common query);

    int insertGlide(@Param("main")U8Common query);
}
