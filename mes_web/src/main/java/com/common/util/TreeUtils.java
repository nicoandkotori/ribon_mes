package com.common.util;



import com.web.system.dto.TreeNode;

import java.util.List;
import java.util.Objects;


/**
 * <p>
 * Tree工具类
 * </p>
 *
 * @author Caratacus
 */
public abstract class TreeUtils {

    /**
     * 递归查找子节点
     *
     * @param treeNodes
     * @return
     */
    public static <T extends TreeNode> T findChildren(T treeNode, List<T> treeNodes) {
        treeNodes.stream().filter(e -> Objects.equals(treeNode.getMenuCode(), e.getParentMenuCode())).forEach(e -> treeNode.getChildren().add(findChildren(e, treeNodes)));
        return treeNode;
    }
}
