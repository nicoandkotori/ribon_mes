package com.common.util;

import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 树形获取子集
 * 需注意字段命名规则
 * @author chen
 * @date 2022/3/2
 */
public class TreeBeanUtils<T> extends BeanUtils {

    /**
     * 查找子级 （U8用）
     * @param id 父级编码
     * @param grade 下级编码
     * @param fullList 完整集合
     * @param classObj class对象
     * @return List<TreeList>
     * @throws IllegalAccessException
     */
    public List<TreeList> getChildList(String id, String grade, List<T> fullList, Class<T> classObj) throws IllegalAccessException {
        List<TreeList> treeList = new ArrayList<>();
        int nextGrade = Integer.parseInt(grade) + 1;
        Field[] fields = classObj.getDeclaredFields();
        Field codeFiled = Arrays.stream(fields).filter(i -> i.getName().contains("Code")).findAny().orElse(null);
        Field gradeFiled = Arrays.stream(fields).filter(i -> i.getName().contains("Grade")).findAny().orElse(null);
        Field nameFiled = Arrays.stream(fields).filter(i -> i.getName().contains("Name")).findAny().orElse(null);

        if (codeFiled == null || gradeFiled == null || nameFiled == null) {
            return new ArrayList<>();
        }
        codeFiled.setAccessible(true);
        gradeFiled.setAccessible(true);
        nameFiled.setAccessible(true);
        List<T> childList = fullList.stream().filter(i -> {
            try {
                return codeFiled.get(i).toString().startsWith(id) && Integer.toString(nextGrade).equals(gradeFiled.get(i).toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return false;
            }

        }).collect(Collectors.toList());

        for (T childrenDep : childList) {
            List<TreeList> childList2 = getChildList(codeFiled.get(childrenDep).toString(), gradeFiled.get(childrenDep).toString(), fullList, classObj);
            TreeList childTree = new TreeList();
            childTree.setId(codeFiled.get(childrenDep).toString());
            childTree.setTitle(nameFiled.get(childrenDep).toString());
            childTree.setName(nameFiled.get(childrenDep).toString());
            childTree.setCode(codeFiled.get(childrenDep).toString());
            if (childList2.size() > 0) {
                childTree.setOpen(true);
                childTree.setChildren(childList2);
            } else {
                childTree.setOpen(false);
            }
            childTree.setSpread(false);
            treeList.add(childTree);
        }
        return treeList;
    }

}
