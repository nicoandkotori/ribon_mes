package com.web.u8system.util;

import com.web.u8system.entity.U8Common;
import com.web.u8system.mapper.U8CommonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * u8 账套工具类
 * Created by Yao on 2019/5/6.
 */
@Component
public class U8SystemUtils {
    @Autowired
    private U8CommonMapper u8CommonMapper;
    /**
     * 生成单据主表ID
     * @param accId
     * @param voucherType
     * @param idLen
     * @return
     * @throws Exception
     */
    public int getFatherId(String accId,String voucherType,int idLen) throws Exception{
        try {
            int u8fid;
            U8Common ucom = new U8Common();
            ucom.setcAcc_Id(accId);
            ucom.setcVouchType(voucherType);
            U8Common um = u8CommonMapper.GetU8ID(ucom);
            if (um == null) {
                u8CommonMapper.insertU8Identity(ucom);
                u8fid = genIdStyle(idLen)+1;
            }else{
                if (um.getiFatherId() != null) {
                    ucom.setiFatherId(um.getiFatherId() + 1);
                    u8fid = genIdStyle(idLen) + ucom.getiFatherId();
                    u8CommonMapper.UpdateU8FatherID(ucom);
                } else {
                    ucom.setiFatherId( 1);
                    u8CommonMapper.UpdateU8FatherID(ucom);
                    u8fid = genIdStyle(idLen)+1;
                }
            }

            return u8fid;

        }catch (Exception e){
            throw new Exception("获取ID出错！");
        }

    }

    /**
     * 生成单据的子表ID
     * @param accId
     * @param voucherType
     * @param idLen
     * @param IdSize  总共需要取多少个ID
     * @return
     * @throws Exception
     */
    public int getChildId(String accId,String voucherType,int idLen,int IdSize) throws Exception {
        try {
            int u8id;
            U8Common ucom = new U8Common();
            ucom.setcAcc_Id(accId);
            ucom.setcVouchType(voucherType);
            U8Common um = u8CommonMapper.GetU8ID(ucom);
            if (um == null) {
                u8CommonMapper.insertU8Identity(ucom);
                u8id = genIdStyle(idLen)+1;
            }else{
                if (um.getiChildId() != null) {
                    ucom.setiChildId(um.getiChildId() + IdSize);
                    u8id = genIdStyle(idLen)+um.getiChildId() + 1;
                    u8CommonMapper.UpdateU8ChildID(ucom);
                } else {
                    ucom.setiChildId(1);
                    u8CommonMapper.UpdateU8ChildID(ucom);
                    u8id = genIdStyle(idLen)+1;
                }
            }

            return u8id;
            //查询U8的子表id

        }catch (Exception e){
            throw new Exception("获取ID出错！");
        }
    }



    /**
     * 生成单据主表ID
     * @param accId
     * @param voucherType
     * @param idLen
     * @return
     * @throws Exception
     */
    public int getId(String accId,String voucherType,int idLen) throws Exception{
        try {
            int u8fid;
            U8Common ucom = new U8Common();
            ucom.setcAcc_Id(accId);
            ucom.setcVouchType(voucherType);
            U8Common um = u8CommonMapper.GetU8ID(ucom);
            if (um == null) {
                u8CommonMapper.insertU8Identity(ucom);
                u8fid = genIdStyle(idLen)+1;
            }else{
                if (um.getiFatherId() != null) {
                    ucom.setiFatherId(um.getiFatherId() + 1);
                    u8fid = genIdStyle(idLen) + (ucom.getiFatherId());
                    u8CommonMapper.UpdateU8ID(ucom);
                } else {
                    ucom.setiFatherId( 1);
                    u8CommonMapper.UpdateU8ID(ucom);
                    u8fid = genIdStyle(idLen)+1;
                }
            }

            return u8fid;

        }catch (Exception e){
            throw new Exception("获取ID出错！");
        }

    }


    /**
     * 生成id的格式
     * @param len
     * @return
     */
    private int genIdStyle(int len){
        int id=1;
        for (int i=1;i<len;i++){
            id=id*10;
        }
        return id;
    }




}
