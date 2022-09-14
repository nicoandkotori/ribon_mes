package com.util;

import com.common.util.SnowFlakeUtils;
import com.modules.security.PreSecurityUser;
import com.modules.security.util.SecurityUtil;

import java.util.Date;

/**
 * 实体抽象类
 *
 * @author mijiahao
 * @date 2022/07/12 11:11
 */
public abstract class BaseEntity {


    /**
     * 得到id
     *
     * @return {@link String}
     */
    public abstract String getId();

    /**
     * 组id
     *
     * @param id id
     */
    public abstract void setId(String id);

    /**
     * 设置创建用户
     *
     * @param userName 用户名
     */
    public abstract void setCreateUser(String userName);

    /**
     * 设置创建日期
     *
     * @param date 日期
     */
    public abstract void setCreateDate(Date date);

    /**
     * 设置是否删除标志
     *
     * @param flag 国旗
     */
    public abstract void setIzDelete(Integer flag);

    /**
     * 设置更新日期
     *
     * @param date 日期
     */
    public abstract void setUpdateDate(Date date);

    /**
     * 设置更新用户
     *
     * @param userName 用户名
     */
    public abstract void setUpdateUser(String userName);

    /**
     * 设置删除用户
     *
     * @param userName 用户名
     */
    public abstract void setDeleteUser(String userName);





    /**
     * 设置创建信息
     *
     */
    public void setCreateInfo(){
        PreSecurityUser user = SecurityUtil.getUser();
        setId(String.valueOf(SnowFlakeUtils.getFlowIdInstance().nextId()));
        setCreateUser(user.getMyusername());
        setCreateDate(new Date());
        setIzDelete(0);
    }

    /**
     * 设置创建信息
     *
     */
    public void setCreateInfo(String id){
        PreSecurityUser user = SecurityUtil.getUser();
        setId(id);
        setCreateUser(user.getMyusername());
        setCreateDate(new Date());
        setIzDelete(0);
    }

    /**
     * 设置更新信息
     *
     */
    public void setUpdateInfo(){
        PreSecurityUser user = SecurityUtil.getUser();
        setUpdateDate(new Date());
        setUpdateUser(user.getMyusername());
    }

    /**
     * 设置删除信息
     */
    public void setDeleteInfo(){
        PreSecurityUser user = SecurityUtil.getUser();
        setIzDelete(1);
        setDeleteUser(user.getMyusername());
    }
}
