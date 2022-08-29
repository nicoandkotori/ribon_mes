package com.common.util;

import com.fasterxml.jackson.databind.deser.Deserializers;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author Yao
 * @date 2020/5/6.13:08
 */
public class ListBeanUtils <T> extends BeanUtils {

    /**
     *
     * @param obj   源对象
      * @param list2   目标对象
     * @param classObj   目标对象 class
     */
    public   void copyList(Object obj, List<T> list2, Class<T> classObj) {
        if ((!Objects.isNull(obj)) && (!Objects.isNull(list2))) {
            List list1 = (List) obj;
            list1.forEach(item -> {
                try {
                    T data = classObj.newInstance();
                    copyProperties(item, data);
                    list2.add(data);
                } catch (InstantiationException e) {
                } catch (IllegalAccessException e) {
                }


            });
        }
    }


}
