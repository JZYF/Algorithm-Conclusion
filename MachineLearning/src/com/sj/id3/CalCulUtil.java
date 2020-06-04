package com.sj.id3;

import java.util.*;

/**
 * 计算工具类
 */
public class CalCulUtil {

    /**
     * 获取指定数据集中指定属性列的各个类别,即特征值的具体取值
     * @param dataSet 指定数据集
     * @param columnIndex 指定列的索引
     * @return
     */
    public static ArrayList<String> getTypes(ArrayList<ArrayList<String>> dataSet, int columnIndex) {
        ArrayList<String> list = new ArrayList<String>();
        for(ArrayList<String> data : dataSet) {
            if(!list.contains(data.get(columnIndex))) {
                list.add(data.get(columnIndex));
            }
        }
        return list;
    }

    /**
     * 获取指定数据集中指定属性列的各个类别及其计数
     * @param dataSet 指定数据集
     * @param columnIndex 指定列的索引
     * @return
     */
    public static Map<String, Integer> getTypeCounts(ArrayList<ArrayList<String>> dataSet, int columnIndex) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        for(ArrayList<String> data : dataSet) {
            String key = data.get(columnIndex); // data为一条样本，key为指定列的具体值
            if(map.containsKey(key)) { // 已经保存过指定特征值的该具体取值
                map.put(key, map.get(key) + 1);
            } else {
                map.put(key, 1);
            }
        }
        return map;
    }

    /**
     * 获取指定列上指定类别的数据集合(分裂后的数据子集)
     * @param dataSet
     * @param columnIndex
     * @param attributeClass 指定的特征值具体取值
     * @return
     */
    public static ArrayList<ArrayList<String>> getDataSet(ArrayList<ArrayList<String>> dataSet,
                                                          int columnIndex, String attributeClass) {
        ArrayList<ArrayList<String>> splitDataSet = new ArrayList<ArrayList<String>>();
        for(ArrayList<String> data : dataSet) {
            if(data.get(columnIndex).equals(attributeClass)) {
                splitDataSet.add(data);
            }
        }

        return splitDataSet;
    }

    /**
     * 计算指定列(特征值)的信息熵
     * @param dataSet
     * @param columnIndex 指定列的索引
     * @return
     */
    public static double computeEntropy(ArrayList<ArrayList<String>> dataSet, int columnIndex) {
        Map<String, Integer> map = getTypeCounts(dataSet, columnIndex);
        int dataSetSize = dataSet.size();
        Iterator<String> keyIter = map.keySet().iterator();
        double entropy = 0; // 信息熵
        while(keyIter.hasNext()) {
            double prob = (double)map.get((String)keyIter.next()) / (double)dataSetSize; // 概率
            entropy += (-1) * prob * Math.log(prob) / Math.log(2); // -p*log2(p)求和

        }
        return entropy;
    }


    /**
     * 计算基于指定属性列对目标属性的条件信息熵
     * @param dataSet
     * @param columnIndex
     * @return
     */
    public static double computeConditionalEntropy(ArrayList<ArrayList<String>> dataSet,
                                                   int columnIndex) {
        Map<String, Integer> map = getTypeCounts(dataSet, columnIndex);  // 获取该属性列的所有列别及其计数

        double conditionalEntropy = 0; // 条件熵

        // 获取根据每个类别分割后的数据集合
        Iterator<String> iter = map.keySet().iterator();
        while(iter.hasNext()) {
            ArrayList<ArrayList<String>> splitDataSet = getDataSet(dataSet, columnIndex, (String)iter.next());
            // 计算目标属性列的列索引
            int desColumn = 0;
            if(splitDataSet.get(0).size() > 0) {
                desColumn = splitDataSet.get(0).size() - 1;
            }

            double probY = (double)splitDataSet.size() / (double)dataSet.size();

            Map<String, Integer> map1 = getTypeCounts(splitDataSet, desColumn); //根据分割后的子集计算后验熵
            Iterator<String> iter1 = map1.keySet().iterator();
            double proteriorEntropy = 0;
            while(iter1.hasNext()) {
                String key = (String)iter1.next(); // 目标属性列中的一个分类
                double posteriorProb = (double)map1.get(key) / (double)splitDataSet.size();
                proteriorEntropy += (-1) * posteriorProb * Math.log(posteriorProb) / Math.log(2);
            }

            conditionalEntropy += probY * proteriorEntropy; // 基于某个分割属性计算条件熵
        }
        return conditionalEntropy;
    }

    /**
     * 根据指定的范围和个数生成随机数
     * @param size 随机数的个数
     * @param scope 随机数的范围
     * @return
     */
    public static Set<Integer> getNumbersOfRandom(int size, int scope) {
        Random ran = new Random();
        HashSet hs = new HashSet();
        for(;;){
            int tmp = ran.nextInt(scope);
            hs.add(tmp); // 通过set避免重复的随机数出现
            if(hs.size() == size)
                break;
        }
        return hs;
    }
}
