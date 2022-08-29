package com.common.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class EntityUtils {

    private static final Logger log = LoggerFactory.getLogger(EntityUtils.class);

    public static <T> T init(Class<T> tClass) throws  Exception {
        T t = null;
        try {
            t = tClass.newInstance();
        } catch (InstantiationException e) {
            log.error("对象拷贝异常-实例化类" + tClass.getName() + "失败", e);
            e.printStackTrace();
            throw new Exception( "对象拷贝异常-实例化类" + tClass.getName() + "失败");
        } catch (IllegalAccessException e) {
            log.error("对象拷贝异常-类" + tClass.getName() + "中有私有的字段", e);
            e.printStackTrace();
            throw new Exception( "对象拷贝异常-类" + tClass.getName() + "中有私有的字段");
        }
        return t;
    }

    /**
     * 拷贝对象a的属性到类B的实例
     *
     * @param a
     * @param bClass
     * @param <A>
     * @param <B>
     * @return
     */
    public static <A, B> B copyObject(A a, Class<B> bClass)  throws  Exception{
        if (a == null)
            return null;
        B b = null;
        try {
            b = bClass.newInstance();
        } catch (InstantiationException e) {
            log.error("对象拷贝异常-实例化类" + bClass.getName() + "失败", e);
            e.printStackTrace();
            throw new Exception("对象拷贝异常-实例化类" + bClass.getName() + "失败");
        } catch (IllegalAccessException e) {
            log.error("对象拷贝异常-类" + bClass.getName() + "中有私有的字段", e);
            e.printStackTrace();
            throw new Exception("对象拷贝异常-类" + bClass.getName() + "中有私有的字段");
        }
        BeanUtils.copyProperties(a, b);
        return b;
    }

    /**
     * 拷贝 LIST
     * @param aList
     * @param bClass
     * @param <A>
     * @param <B>
     * @return
     */
    public static <A,B> List<B> copyList(List<A> aList, Class<B> bClass) throws  Exception{
        if (CollectionUtils.isEmpty(aList))
            return null;
        List<B> bList = new ArrayList<>();
        for (A a : aList){
            try {
                B b = bClass.newInstance();
                BeanUtils.copyProperties(a,b);
                bList.add(b);
            } catch (InstantiationException e) {
                log.error("对象拷贝异常-实例化类" + bClass.getName() + "失败", e);
                e.printStackTrace();
                throw new Exception( "对象拷贝异常-实例化类" + bClass.getName() + "失败");
            } catch (IllegalAccessException e) {
                log.error("对象拷贝异常-类" + bClass.getName() + "中有私有的字段", e);
                e.printStackTrace();
                throw new Exception( "对象拷贝异常-类" + bClass.getName() + "中有私有的字段");
            }
        }
        return bList;
    }


}
