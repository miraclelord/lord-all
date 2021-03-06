package com.lord.biz.service;

import com.lord.common.dto.Category;
import com.lord.common.dto.TreeNode;
import com.lord.utils.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能：分类的公共属性的通用设置方法
 *
 * @author xiaocheng
 * @version 1.0
 * @Date 2017年05月10日 18:25
 */
public abstract class CategoryServiceImpl {

    /**
     * 设置新增分类的公共属性
     *
     * @param pageObj 需要保存的对象
     * @param parent  父节点
     */
    public void setAddCategory(Category pageObj, Category parent) {
        pageObj.setLeaf(true);//新增节点都是叶子节点
        if (parent == null) {
            setFirstCategory(pageObj);
        } else {
            Preconditions.checkNotNull(parent, "父节点不存在");
            if (StringUtils.isNotEmpty(parent.getParentIds())) {
                pageObj.setParentIds(parent.getParentIds() + parent.getId() + ",");
            } else {
                pageObj.setParentIds(parent.getId() + ",");
            }
            pageObj.setLevel(parent.getLevel() + 1);
            pageObj.setParentId(parent.getId());
            pageObj.setParentName(parent.getName());
        }
    }

    /**
     * 设置更新分类的公共属性
     *
     * @param pageObj 需要保存的对象
     * @param parent  父节点
     * @param dbObj   数据库数据
     */
    public void setUpdateCategory(Category pageObj, Category parent, Category dbObj) {
        pageObj.setParentId(dbObj.getParentId());
        pageObj.setChildrenIds(dbObj.getChildrenIds());
        pageObj.setLeaf(dbObj.isLeaf());
        if (parent != null) {
            pageObj.setLevel(parent.getLevel() + 1);
            pageObj.setParentId(parent.getId());
            pageObj.setParentName(parent.getName());
            if (StringUtils.isNotEmpty(parent.getParentIds())) {
                pageObj.setParentIds(parent.getParentIds() + parent.getId() + ",");
            } else {
                pageObj.setParentIds(parent.getId() + ",");
            }
        } else {
            setFirstCategory(pageObj);
        }
    }

    private void setFirstCategory(Category pageObj) {
        pageObj.setLevel(1);
        pageObj.setParentId(null);
        pageObj.setParentName(null);
        pageObj.setParentIds(null);
    }

    /**
     * 是否需要更新父节点信息
     *
     * @param pageObj 需要保存的对象
     * @param parent  父节点
     */
    public boolean needUpdateParent(Category pageObj, Category parent) {
        if (parent == null) {
            return false;
        }
        String childrenIds = parent.getChildrenIds();
        String idStr = pageObj.getId() + ",";
        if (StringUtils.isEmpty(childrenIds)) {
            parent.setChildrenIds(idStr);
            parent.setLeaf(false);
            return true;
        }
        if (!childrenIds.contains(idStr)) {
            parent.setChildrenIds(childrenIds + idStr);
            parent.setLeaf(false);
            return true;
        }
        return false;
    }

    public List<TreeNode> getTreeNodes() {
        List<Category> categoryList = findAllCategory();
        List<TreeNode> list = new ArrayList<>();
        if (categoryList == null || categoryList.size() < 1) {
            return list;
        }

        long rootParentId = 0L;
        Map<Long, List<Category>> parentMap = new HashMap<>();
        for (Category category : categoryList) {
            Long parentId = category.getParentId();
            if (parentId == null) {
                parentId = rootParentId;
            }
            if (parentMap.get(parentId) == null) {
                parentMap.put(parentId, new ArrayList<Category>());
            }
            parentMap.get(parentId).add(category);
        }

        List<Category> rootList = parentMap.get(rootParentId);
        for (Category sub : rootList) {
            TreeNode treeNode = setTreeNode(sub, parentMap);
            list.add(treeNode);
        }
        return list;
    }

    private TreeNode setTreeNode(Category sub, Map<Long, List<Category>> parentMap) {
        TreeNode treeNode = parseTreeNode(sub);
        List<Category> subList = parentMap.get(sub.getId());
        if (subList != null && subList.size() > 0) {
            List<TreeNode> sList = new ArrayList<>();
            for (Category category : subList) {
                TreeNode sNode = setTreeNode(category, parentMap);
                sList.add(sNode);
            }
            treeNode.setChildren(sList);
        }
        return treeNode;
    }

    private TreeNode parseTreeNode(Category sub) {
        TreeNode treeNode = new TreeNode();
        treeNode.setId(sub.getId());
        treeNode.setName(sub.getName());
        treeNode.setLetter(sub.getLetter());
        treeNode.setIcon(sub.getIcon());
        return treeNode;
    }

    protected abstract List<Category> findAllCategory();
}
