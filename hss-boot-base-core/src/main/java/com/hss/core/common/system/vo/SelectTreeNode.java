package com.hss.core.common.system.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 下拉树 model
 *
 * @author jeecg-boot
 */
@Data
public class SelectTreeNode implements Serializable {

    private String key;
    private String title;
    private String value;
    private String pid;
    private String name;

    private String description;

    private String className;

    private String classId;

    /**
     * 父Id
     */
    private String id;
    /**
     * 是否是叶节点
     */
    private boolean isLeaf;
    /**
     * 子节点
     */
    private List<SelectTreeNode> children = new ArrayList<>();

    public void addChild(SelectTreeNode child){
        if (this.children == null){
            this.children = new ArrayList<>();
        }
        this.children.add(child);
    }

    public static SelectTreeNode from(String id, String name, String description, String className, String classId, boolean isLeaf, String key, String title, String value, String pid) {
        SelectTreeNode node = new SelectTreeNode();
        node.setId(id);
        node.setName(name);
        node.setDescription(description);
        node.setClassName(className);
        node.setClassId(id);
        node.setLeaf(isLeaf);
        node.setKey(key);
        node.setTitle(title);
        node.setValue(value);
        node.setPid(pid);
        return node;
    }

    public static SelectTreeNode from(String id, String name, String pid) {
        SelectTreeNode node = new SelectTreeNode();
        node.setId(id);
        node.setName(name);
        node.setPid(pid);
        return node;
    }

    public static SelectTreeNode from(String id, String name, String pid, String title) {
        SelectTreeNode node = new SelectTreeNode();
        node.setId(id);
        node.setName(name);
        node.setPid(pid);
        node.setTitle(title);
        return node;
    }
}
