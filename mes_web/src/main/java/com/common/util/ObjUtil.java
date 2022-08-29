package com.common.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by chen on 2021-10-24.
 */
public class ObjUtil {
    /**
     * 将 Object转换成 List
     *
     * @param obj
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> castList(Object obj, Class<T> clazz) {
        List<T> result = new ArrayList<T>();
        if (obj instanceof List<?>) {
            for (Object o : (List<?>) obj) {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }

    /**
     * 将一个 map组成的 list转成实体类 bean 组成的list
     *
     * @param mapList 存了 map 对象的list
     * @param clazz   需要将这些 map 转成哪个实体类对象
     * @return
     */
    public static <T> List<T> convertMapListToBeanList(List<Map> mapList, Class<T> clazz) {
        List<T> list = new ArrayList<T>();
        for (Map map : mapList) {
            try {
                T obj = clazz.newInstance();//创建bean的实例对象
                for (Object o : map.keySet()) {//遍历map的key
                    for (Method m : clazz.getMethods()) {//遍历bean的类中的方法，找到set方法进行赋值
                        if (m.getName().toLowerCase().equals("set" + o.toString().toLowerCase())) {
                            m.invoke(obj, map.get(o));
                        }
                    }
                }
                list.add(obj);
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 获取字段类型
     *
     * @param a
     * @return
     */
    public static String getType(Object a) {
        return a.getClass().toString();

    }
}
