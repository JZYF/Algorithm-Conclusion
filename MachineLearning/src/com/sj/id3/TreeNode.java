package com.sj.id3;

import java.util.ArrayList;
import java.util.List;

/**
 * 树节点类
 */
public class TreeNode {
    private String nodeName; // 决策树节点名称
    private List<String> splitAttributes; // 分裂特征值的名称
    private ArrayList<TreeNode> childrenNodes; // 决策树的子节点
    private ArrayList<ArrayList<String>> dataSet; // 划分到该节点的数据集
    private ArrayList<String> attributeSet; // 数据集所有属性

    /**
     * 构造器，创建节点
     */
    public TreeNode() {
        childrenNodes = new ArrayList<TreeNode>();
    }

    public String getNodeName() {
        return nodeName;
    }
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
    public List<String> getSplitAttributes() {
        return splitAttributes;
    }
    public void setSplitAttributes(List<String> splitAttributes) {
        this.splitAttributes = splitAttributes;
    }
    public ArrayList<TreeNode> getChildrenNodes() {
        return childrenNodes;
    }
    public void setChildrenNodes(ArrayList<TreeNode> childrenNodes) {
        this.childrenNodes = childrenNodes;
    }
    public ArrayList<ArrayList<String>> getDataSet() {
        return dataSet;
    }
    public void setDataSet(ArrayList<ArrayList<String>> dataSet) {
        this.dataSet = dataSet;
    }
    public ArrayList<String> getAttributeSet() {
        return attributeSet;
    }
    public void setAttributeSet(ArrayList<String> attributeSet) {
        this.attributeSet = attributeSet;
    }
}
