package com.sj.id3;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ID3Main {

    private static ArrayList<ArrayList<String>> trainDataSet = new ArrayList<>(); // 训练数据集
    private static ArrayList<Map<String, String>> testDataSet = new ArrayList<>(); // 测试数据集
    private static ArrayList<String> attributeList = new ArrayList<String>(); // 特征值
    private static ArrayList<String> realRes = new ArrayList<>(); // 测试集的真实结果集

    public static void main(String[] args) {
        String filePath = "data/car.txt"; // 汽车数据集文件路径
//        String filePath = "data/iris.csv"; // 鸢尾花数据集文件路径
//        String filePath = "data/lenses.data"; // 隐形眼镜数据集文件路径
        double splitPoint = 0.3; // 指定测试集的比重
        loadData(filePath,splitPoint); // 加载训练集和测试集
        ID3 dt = new ID3(); // 初始化决策树模型
        TreeNode root = dt.buildTree(trainDataSet, attributeList); // 进行模型的训练得到决策树
        dt.printTree(root);
        int trueCount = 0; // 记录预测对的样本数量
        for (int i = 0; i < testDataSet.size(); i++) {
            Map<String, String> item = testDataSet.get(i); // 获取一条测试样本
            String res = dt.searchTree(root, item); // 通过决策树进行预测
            System.out.println("第"+(i+1)+"条样本的预测结果:"+res);
            if (res.equals(realRes.get(i)))
                trueCount++; // 和真实值进行比较，如果正确trueCount加一
        }
        DecimalFormat df = new DecimalFormat("0.00%");
        double predictRes = (double)trueCount/testDataSet.size();
        System.out.println("正确率：" + df.format(predictRes));

    }

    public static void loadData(String filePath, double splitPoint) {
        File f = new File(filePath); // 加载文件
        BufferedReader reader = null; // 按行进行文件读取

        try {
            reader = new BufferedReader(new FileReader(f));
            String str = null; // 保存每行的数据，字符串类型
            try {
                str = reader.readLine(); // 第一行为特征值
                String[] attributes = str.split(",");

                for(int i = 0; i < attributes.length; i++) {
                    attributeList.add(attributes[i]); // 保存特征值
                }
//                System.out.println(attributeList);

                ArrayList<ArrayList<String>> dataSet = new ArrayList<ArrayList<String>>(); // 保存所有的数据的数据集
                while((str = reader.readLine()) != null) {
                    ArrayList<String> tmpList = new ArrayList<String>(); // 保存每一行的数据
                    String[] s = str.split(",");
                    for(int i = 0; i < s.length; i++) {
                        tmpList.add(s[i]);
                    }
                    dataSet.add(tmpList); // 保存每一条样本
                }
                int predictNum = (int) (splitPoint*dataSet.size()); // 测试集的数量
                Set<Integer> randoms = CalCulUtil.getNumbersOfRandom(predictNum, dataSet.size()); // 随机选择测试集的行数
                for (int i = 0; i < dataSet.size(); i++) {
                    if (randoms.contains(i)){ // 属于随机选择的样本
                        Map<String, String> item = new HashMap<>();
                        for (int j = 0; j < attributeList.size(); j++) { // 给样本的特征值对应的数据添加key值，即特征值
                            item.put(attributeList.get(j), dataSet.get(i).get(j));
                        }
                        realRes.add(dataSet.get(i).get(attributeList.size())); // 存储测试集的真实值
                        testDataSet.add(item); // 存储测试集的样本
                    }
                    else
                        trainDataSet.add(dataSet.get(i)); // 存储训练集的样本
                }

//                System.out.println(attributeList);
//                System.out.println(trainDataSet);
//                System.out.println(testDataSet);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
