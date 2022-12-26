package com.kcwl.framework.utils;

import cn.hutool.core.bean.BeanUtil;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.util.*;
import java.util.stream.Collectors;

public class TreeUtils {

    /**
     * 把列表转换为树结构
     *
     * @param originalList      原始list数据
     * @param keyName           作为唯一标示的字段名称
     * @param parentFieldName   父节点字段名称
     * @param childrenFieldName 子节点字段名称
     * @return 组装后的集合
     */
    public static <T> List<T> getTree(List<T> originalList, String keyName, String parentFieldName, String childrenFieldName) throws Exception {
        if (CollectionUtil.isEmpty(originalList)) {
            return originalList;
        }
        // 获取根节点，即找出父节点为空的对象
        Set<T> topSet = new HashSet<>();

        //追溯顶层节点
        findTopList(topSet,originalList,keyName,parentFieldName);


        List<T> topList = new ArrayList<>(topSet);

        // 将根节点从原始list移除，减少下次处理数据
        originalList.removeAll(topList);

        // 递归封装树
        fillTree(topList, originalList, keyName, parentFieldName, childrenFieldName);

        return topList;
    }


    private static <T> Set<T> findTopList(Set<T> topSet, List<T> originalList, String keyName, String parentFieldName) {
        Map<String, T> objMap = originalList.stream().collect(Collectors.toMap(item -> BeanUtil.getProperty(item, keyName), s -> s, (s1, s2) -> s1));
        for (T t : originalList) {
            T top = findTop(objMap, t, parentFieldName);
            topSet.add(top);
        }
        return topSet;
    }


    private static <T> T findTop(Map<String, T> objMap, T obj, String parentFieldName) {
        String parentId = BeanUtil.getProperty(obj, parentFieldName);
        if (StringUtil.isBlank(parentId) || parentId.equals("0")) {
            return obj;
        }
        T t = objMap.get(parentId);
        if (t == null) {
            return obj;
        }
        return findTop(objMap,t,parentFieldName);
    }


    /**
     * 封装树
     *
     * @param parentList        要封装为树的父对象集合
     * @param originalList      原始list数据
     * @param keyName           作为唯一标示的字段名称
     * @param parentFieldName   模型中作为parent字段名称
     * @param childrenFieldName 模型中作为children的字段名称
     */
    public static <T> void fillTree(List<T> parentList, List<T> originalList, String keyName, String parentFieldName, String childrenFieldName) throws Exception {
        for (int i = 0; i < parentList.size(); i++) {
            List<T> children = fillChildren(parentList.get(i), originalList, keyName, parentFieldName, childrenFieldName);
            if (children.isEmpty()) {
                continue;
            }
            originalList.removeAll(children);
            fillTree(children, originalList, keyName, parentFieldName, childrenFieldName);
        }
    }

    /**
     * 封装子对象
     *
     * @param parent            父对象
     * @param originalList      待处理对象集合
     * @param keyName           作为唯一标示的字段名称
     * @param parentFieldName   模型中作为parent字段名称
     * @param childrenFieldName 模型中作为children的字段名称
     */
    public static <T> List<T> fillChildren(T parent, List<T> originalList, String keyName, String parentFieldName, String childrenFieldName) throws Exception {
        List<T> childList = new ArrayList<>();
        String parentId = BeanUtil.getProperty(parent, keyName);
        for (int i = 0; i < originalList.size(); i++) {
            T t = originalList.get(i);
            String childParentId = BeanUtil.getProperty(t, parentFieldName);
            if (parentId.equals(childParentId)) {
                childList.add(t);
            }
        }
        if (!childList.isEmpty()) {
            FieldUtils.writeDeclaredField(parent, childrenFieldName, childList, true);
        }
        return childList;
    }
}
