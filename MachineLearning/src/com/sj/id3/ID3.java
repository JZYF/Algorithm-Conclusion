package com.sj.id3;

import java.util.*;

/**
 * 通过ID3算法建立决策树模型
 */
public class ID3 {

    /**
     * 建立树
     * @param dataSet 数据集
     * @param attributeSet 特征集合
     * @return
     */
    public TreeNode buildTree(ArrayList<ArrayList<String>> dataSet, ArrayList<String> attributeSet) {

        TreeNode node = new TreeNode(); // 初始化节点
        node.setDataSet(dataSet);
        node.setAttributeSet(attributeSet);

        int index = -1; // 存储最终的划分特征值索引
        double gain = 0; // 信息增益量Gain
        double maxGain = -Double.MAX_VALUE;
        double baseEntropy = CalCulUtil.computeEntropy(dataSet, attributeSet.size() - 1); // 最初的信息熵
        for(int i = 0; i < attributeSet.size(); i++) {
            gain = baseEntropy - CalCulUtil.computeConditionalEntropy(dataSet, i); // 计算每个按特征值划分的信息增益量
            if(gain > maxGain) {
                index = i; // 保存当前最大的信息增益量对于的特征值索引
                maxGain = gain; // 保存当前最大的信息增益量
            }
        }

        ArrayList<String> splitAttributes = CalCulUtil.getTypes(dataSet, index); // 获取该节点下的分裂特征值
        node.setSplitAttributes(splitAttributes);
        node.setNodeName(attributeSet.get(index)); // 保存当前的划分特征

        // 判断每个特征值列是否需要继续分裂
        for(int i = 0; i < splitAttributes.size(); i++) {
            ArrayList<ArrayList<String>> splitDataSet = CalCulUtil.getDataSet(dataSet, index, splitAttributes.get(i));

            // 判断这个分裂子数据集的目标属性是否纯净，如果纯净则结束，否则继续分裂  
            int desColumn = splitDataSet.get(0).size() - 1; // 目标特征值列所在的列号
            ArrayList<String> desAttributes = CalCulUtil.getTypes(splitDataSet, desColumn); // 获取特征值的具体取值
            TreeNode childNode = new TreeNode();
            if(desAttributes.size() == 1) {
                childNode.setNodeName(desAttributes.get(0)); // 代表叶子节点
            } else {
                ArrayList<String> newAttributeSet = new ArrayList<String>(); // 保存未被当做决策树节点的特征
                for(String s : attributeSet) { // 删除新属性集合中已作为决策树节点的属性值  
                    if(!s.equals(attributeSet.get(index))) {
                        newAttributeSet.add(s);
                    }
                }

                ArrayList<ArrayList<String>> newDataSet = new ArrayList<ArrayList<String>>(); // 每条样本中不包含已当做节点的特征值对应的特征数据
                for(ArrayList<String> data : splitDataSet) { // 除掉columnIndex参数指定的特征值对应的特征数据，即删除某一列
                    ArrayList<String> tmp = new ArrayList<String>();
                    for(int j = 0; j < data.size(); j++) {
                        if(j != index) {
                            tmp.add(data.get(j));
                        }
                    }
                    newDataSet.add(tmp);
                }

                childNode = buildTree(newDataSet, newAttributeSet); // 递归建树  
            }
            node.getChildrenNodes().add(childNode);
        }
        return node;
    }

    /**
     * 打印建好的树 
     * @param root
     */
    public void printTree(TreeNode root) {
        System.out.println("----------------");
        if(null != root.getSplitAttributes()) {
            System.out.print("分裂节点：" + root.getNodeName());
            for(String attr : root.getSplitAttributes()) {
                System.out.print("(" + attr + ") ");
            }
        } else {
            System.out.print("分裂节点：" + root.getNodeName());
        }

        if(null != root.getChildrenNodes()) {
            for(TreeNode node : root.getChildrenNodes()) {
                printTree(node);
            }
        }

    }

    /**
     * 根据给定的样本遍历决策树预测分类
     * @param root 决策树的根节点
     * @param searchNode 给定样本
     * @return
     */
    public String searchTree(TreeNode root, Map<String, String> searchNode) {
        int keyCounts = searchNode.keySet().size(); // 样本中特征值的个数
        String res = null; // 保存当前一次遍历的结果
        for (int i = 0; i < keyCounts; i++) {
            res = root.getNodeName();
            String val = searchNode.get(root.getNodeName()); // 根据决策树当前节点的特征值获取样本的具体值并进行向下搜索
            int index = 0; // 记录当前的分支索引
            if (root.getSplitAttributes() == null)
                break;
            for (; index < root.getSplitAttributes().size(); index++) {
                if (val.equals(root.getSplitAttributes().get(index))){ // 找到了具体分支
//                    System.out.println(index);
                    break;
                }

            }
            if (root.getChildrenNodes() == null || root.getChildrenNodes().size() <= index)
                break;
            root = root.getChildrenNodes().get(index); // 根据分支索引找到下一个节点
        }
        return res;

    }
}
