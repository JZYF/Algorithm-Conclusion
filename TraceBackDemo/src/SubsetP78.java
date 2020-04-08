import java.util.ArrayList;
import java.util.List;

public class SubsetP78 {
    public static void main(String[] args){
        int[] nums = {1,2,3};
        List<Integer> items = new ArrayList<>();
        List<List<Integer>> allItems = new ArrayList<>();
        allItems.add(items);
        traceBack(0,items,nums,allItems);
        System.out.println(allItems);
        System.out.println(operation(nums));
    }

    public static void traceBack(int i, List<Integer> items,
                                 int[] nums,List<List<Integer>> allItems){
        if (i >= nums.length)
            return;
        items.add(nums[i]);
        allItems.add(new ArrayList<>(items));
        traceBack(i+1, items, nums, allItems);
        //某一枝已经到最底层,需要回溯
        items.remove(items.size()-1);
        traceBack(i+1,items,nums,allItems);

    }

    public static List<List<Integer>> operation(int[] nums){
        int max = 1<<nums.length;
        List<List<Integer>> allItems = new ArrayList<>();
        for (int i = 0; i < max; i++){
            List<Integer> items = new ArrayList<>();
            for (int j = 0; j < nums.length; j++){
                if ((i & (1<<j)) != 0)
                    items.add(nums[j]);
            }
            allItems.add(items);
        }
        return allItems;
    }

}
