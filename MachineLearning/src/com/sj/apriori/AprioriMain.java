package com.sj.apriori;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 *
 * 0. 获取C1的时候，因为没有L(0)频繁集，所以单独处理一个函数
 * 1. 已经频繁集L(k-1)，{}自连接+剪枝}求出精简后的候选集C(k)
 * 2. 根据精简后的候选集C(k)根据求出频繁集L(k)
 *
 * 数据结构：
 * 1. 使用List<String> 存储整个数据库的数据，string代表每一行的数据
 * 2. 使用HashMap<String,Integer> 存储项集以及项集的重复次数，便于查找。是{项集}的集合。
 * 3. 每个项集用String类型存储。项集中的每一项使用String.split(" ")方法变成数据，以便取用项集中的每一项
 *
 * 给定的数据格式：
 * 1. 每个数据以空格隔开
 * 2. 每一行代表一个事务T,事务编号就是行号
 *
 * 示例数据：
 * 1. {11，12，13}是一个频繁项集，是属于HashMap<String,Integer>中的一个key值元素，value为支持度
 * 2. Set<String> = HashMap.keySet(); 用来存储HashMap的所有key的值，也就是存储所有的项集（不包括项集的重复次数）
 *
 */


public class AprioriMain {

    private static double SUPPORT_PERCENT = 0.1; // 指定的支持度
    private static List<String> dataList = new ArrayList<>();


    public static void main(String[] args) {
//        String filePath = "data/retail.dat"; // 零售数据集
        String filePath = "data/mushroom.dat"; // 蘑菇数据集
//        String filePath = "data/chess.dat"; // 国际象棋数据集
        //1. 导入数据
        loadData(filePath);

        //2. 算法处理
        apriori();
    }



    /**
     * Apriori算法主程序，需要递归处理
     *
     * @return
     */
    public static void apriori(){


        //扫描整个数据库D，对每一项进行计数，获得一项的{候选项集合}
        Map<String, Integer> stepFrequentSetMap = new HashMap<>();
        System.out.println("\n=====================第" + 1 + "次扫描的频繁项集列表======================" + "\n");


        stepFrequentSetMap.putAll(getFrequentSets(findCandidateOneSets())); // 获取1项集中的频繁项集(key)及对应的支持度数量(value)
        Set<String> stringSet = stepFrequentSetMap.keySet(); // 所有的1项集的频繁项集list

        for (String string: stringSet){
            System.out.println("频繁集：" + string +  "支持度:" + stepFrequentSetMap.get(string));
        }
        System.out.println("频繁项集的个数：" + stringSet.size());

        int i = 1; // 扫描次数
        //当生成的频繁项集为空的时候，退出循环
        while(stepFrequentSetMap != null && stepFrequentSetMap.size()>0){

            i++;

            //打印当前的频繁项集的信息
            System.out.println("\n=====================第" + i + "次扫描的频繁项集列表======================" + "\n");

            stepFrequentSetMap = getFrequentSets(getMinCandidate(stepFrequentSetMap));

            if (stepFrequentSetMap != null){
                stringSet = stepFrequentSetMap.keySet();
                for (String string: stringSet){
                    System.out.println("频繁集：" + string +  "支持度:" + stepFrequentSetMap.get(string));
                }
                System.out.println("\n频繁项集的个数：" + stringSet.size());
            }

        }
    }

    /**
     * 导入数据
     *
     * @return 数据的list集合
     */
    private static void loadData(String filePath){

        File file = new File(filePath);

        try {
            //文件存在且为文件类型执行接下来的操作
            if (file.isFile() && file.exists()){
                InputStreamReader reader = new InputStreamReader(new FileInputStream(file),
                        "UTF-8"); // 文件读取流
                BufferedReader bufferedReader = new BufferedReader(reader);
                String lineTxt;
                while ((lineTxt = bufferedReader.readLine()) != null){//读取文件中的一行
                    dataList.add(lineTxt);
                }
                reader.close();
            }else {
                System.err.println("找不到指定文件！");
            }
        }catch (Exception e){
            System.err.println("读取文件内容出错！");
            e.printStackTrace();
        }

    }


    /**
     * 寻找1项的候选集C1（起始化特殊的处理）
     *
     * @return 返回map集合，这个集合是每一项以及对应的重复次数：key->value
     *
     * key：每一个候选项/频繁项
     * value：相应候选项/频繁项的重复次数
     */
    private static HashMap<String, Integer> findCandidateOneSets()
    {
        HashMap<String, Integer> resultSetMap = new HashMap<>(); // 初始化结果集

        for(String dataList : dataList) // 每一个项集
        {
            String[] dataString = dataList.split(" "); // 一个项集中的所有项的list
            //查询map集合中是否有该元素，如果没有，加入该元素，否则给该元素对于的value(重复次数）+1
            for (String string :dataString){
                string += " ";
                if (resultSetMap.get(string) == null){
                    resultSetMap.put(string,1);
                }else {
                    resultSetMap.put(string,resultSetMap.get(string) + 1);
                }
            }
        }
        return resultSetMap;
    }



    /**
     * 从L(k-1)频繁集合中得到精简的C(k)候选集
     *
     * 先验原则：若一个项集的子集不是频繁项集，则该项集也不是频繁项集。
     * （换句话说，非频繁项集的超集也是非频繁项集）
     *
     * 1. 根据先验原则压缩候选集大小后得到精简的候选集集合
     * 2. 对精简过的候选项集进行累加计数
     *
     * @param frequentMapSet
     * @return 返回C(k)候选集合
     */
    private static Map<String, Integer> getMinCandidate(Map<String, Integer> frequentMapSet){

        //需要返回的精简过后的候选项集
        Map<String,Integer> minCandidateMapSet = new HashMap<>();

        //导入(k-1)的频繁项集
        Set<String> frequentSet = frequentMapSet.keySet();

        /**
         * 1. 自连接过程:产生候选项集
         *
         * 对于每一个项集，拆开成项的数组。将该项集的每一项与其他项集的每一项比较，
         * 如果有不重复的项，就将该项与原项集链接起来，组成K项的候选项项集。
         */

        for (String frequentItemList1: frequentSet){
            for (String frequentItemList2: frequentSet){
                String[] itemArray1 =  frequentItemList1.split(" ");
                String[] itemArray2 = frequentItemList2.split(" ");

                //自连接生成的候选项集
                String linkString = "";
                boolean flag = true;//是否可以连接
                /**
                 * 判断是否可以自连接的条件：
                 * 1. 前K-1项必须相同
                 * 2. itemArray1的最后一项必须小于第二个项集的最后一项
                 * 自连接过程：
                 * 将第一个项集与第二个项集的最后一项连接起来
                 */

                for (int i =0;i<itemArray1.length -1 ;i++){
                    if (itemArray1[i].equals(itemArray2[i])){
                        flag = false;
                        break;
                    }
                }
                if (flag && itemArray1[itemArray1.length - 1].compareTo(itemArray2[itemArray1.length -1]) < 0){
                    linkString = frequentItemList1  + itemArray2[itemArray2.length - 1] + " ";
                }

                /**
                 * 2. {剪枝过程，也就是先验规则的使用}
                 *
                 * 找出该候选集的所有子集，并判断每个子集是否属于频繁子集
                 */
                boolean hasInfrequentSubSet = false;// 是否有非频繁子项集，默认无
                if (!linkString.equals("")){//自连接成功
                    //System.out.println(linkString);
                    //候选项集的所有项的数组
                    String[] itemArray = linkString.split(" ");
                    //找出该候选集的所有子集，实际操作的时候只需要找出比候选集少一项的子集集合
                    //因为这都是不断的递归上来的，项数更小的项集肯定是频繁项集
                    for (int i = 0; i <itemArray.length; i++){
                        String subString = "";
                        for (int j = 0;j<itemArray.length; j++){
                            if (j!=i){
                                subString += itemArray[j] + " ";
                            }
                        }
                        if (frequentMapSet.get(subString) == null){
                            hasInfrequentSubSet = true;
                            break;
                        }
                    }
                }else{
                    hasInfrequentSubSet = true;//这里并不是代表存在非频繁子集，只是表示没有自连接成功，没有找到候选集
                }
                //自连接成功，加入到候选集集合中
                if (!hasInfrequentSubSet){
                    minCandidateMapSet.put(linkString,0);
                }
            }
        }

        /**
         * 3. {对生成的候选集进行统计支持度}
         */

        Set<String> minCandidateSet = minCandidateMapSet.keySet(); // 所有的候选集


        // 将每一行的候选项集，由String数据变成字符串数组。
        // 将每一行的事务，由String转换成List<String>
        for (String itemList: minCandidateSet){
            String[] strings = itemList.split(" "); // 项集中的具体项的list
            for (String data: dataList){
                List<String> dataList = Arrays.asList(data.split(" "));

                Boolean flag = true; // 判断当前项集是否被包含在交易数据中
                //如果候选项集中有一项在当前事务中找不到，支持度则不会增加
                for (int i =0;i < strings.length;i++){

                    if (!dataList.contains(strings[i])){

                        flag = false;
                        break;
                    }
                }
                if (flag){
                    minCandidateMapSet.put(itemList,minCandidateMapSet.get(itemList) + 1);
                }
            }

        }

        return minCandidateMapSet;
    }


    /**
     * 从候选集中得到频繁项集
     *
     * {统计精简后的候选集C(k)的重复次数}  = {最后得到L(k)频繁集}
     *
     * 1. 对精简过的候选集进行判断（之前已经做好了计数工作），不满足支持度的进行排除
     *
     * @param minCandidateMapSet
     * @return
     */
    private static Map<String, Integer> getFrequentSets(Map<String, Integer> minCandidateMapSet){

        if (minCandidateMapSet == null){
            //精简后的候选集为空，表示当前项数的候选集不存在，此时需要结束该算法了
            System.err.println("候选项集为空");
            return null;
        }else{
            Map<String,Integer> frequentMapSet = new HashMap<>();//需要返回的频繁项集(key)及对应的支持度(value)

            Set<String> minCandidateSet = minCandidateMapSet.keySet();//获取候选项集的KEY，也就是具体的项集

            int SUPPORT = (int)(dataList.size() * SUPPORT_PERCENT);//最小支持度对应的项集数量
            System.out.println("最小支持度对应的项集数量为：" + SUPPORT + " 候选项集的大小为：" + minCandidateMapSet.size() + "\n");
            for (String itemListString: minCandidateSet){
                //如果该项集的重复次数大于或者等于最小支持度，就把该项加入到频繁项即中
                if (minCandidateMapSet.get(itemListString) >= SUPPORT){
                    frequentMapSet.put(itemListString,minCandidateMapSet.get(itemListString));
                }
            }
            if (frequentMapSet.size() == 0){
                //计算得到的频繁项集为空
                return null;
            }else{
                return frequentMapSet;
            }
        }
    }

}
