package com.dimple.project.king.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author liusheng
 *
 */
public class TreeModelUtils {
    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list     分类表
     * @param parentId 传入的父节点ID
     * @return 
     * @return 
     * @return String
     */
    public static <T extends TreeModel> List<T> getChildPerms(List<T> list, int parentId) {
    	List<T> returnList = new ArrayList<T>();
        for (Iterator<T> iterator = list.iterator(); iterator.hasNext(); ) {
            T t = (T) iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId() == parentId) {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private static <T extends TreeModel> void recursionFn(List<T> list, T t) {
        // 得到子节点列表
        List<T> childList = getChildList(list, t);
        t.setChildList(childList);
        for (T tChild : childList) {
            if (hasChild(list, tChild)) {
                // 判断是否有子节点
                Iterator<T> it = childList.iterator();
                while (it.hasNext()) {
                    T n = (T) it.next();
                    recursionFn(list, n);
                }
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private static <T extends TreeModel> List<T> getChildList(List<T> list, T t) {

        List<T> tlist = new ArrayList<T>();
        Iterator<T> it = list.iterator();
        while (it.hasNext()) {
            T n = (T) it.next();
            if (n.getParentId().longValue() == t.getId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    List returnList = new ArrayList();

    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list   分类表
     * @param typeId 传入的父节点ID
     * @param prefix 子节点前缀
     */
    public <T extends TreeModel> List<T> getChildPerms(List<T> list, int typeId, String prefix) {
        if (list == null) {
            return null;
        }
        for (Iterator<T> iterator = list.iterator(); iterator.hasNext(); ) {
            T node = (T) iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (node.getParentId() == typeId) {
                recursionFn(list, node, prefix);
            }
            // 二、遍历所有的父节点下的所有子节点
            /*
             * if (node.getParentId()==0) { recursionFn(list, node); }
             */
        }
        return returnList;
    }

    private <T extends TreeModel> void recursionFn(List<T> list, T node, String p) {
        // 得到子节点列表
        List<T> childList = getChildList(list, node);
        if (hasChild(list, node)) {
            // 判断是否有子节点
            returnList.add(node);
            Iterator<T> it = childList.iterator();
            while (it.hasNext()) {
                T n = (T) it.next();
                n.setName(p + n.getName());
                recursionFn(list, n, p + p);
            }
        } else {
            returnList.add(node);
        }
    }

    /**
     * 判断是否有子节点
     */
    private static  <T extends TreeModel> boolean hasChild(List<T> list, T t) {
        return getChildList(list, t).size() > 0 ? true : false;
    }
}
