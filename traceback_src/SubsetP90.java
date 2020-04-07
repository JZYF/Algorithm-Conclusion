import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class SubsetP90 {
    public static void main(String[] args){
        int[] nums = {2,1,2};
        Arrays.sort(nums);
        List<Integer> items = new ArrayList<>();
        List<List<Integer>> allItems = new ArrayList<>();
        HashSet<List<Integer>> set = new HashSet<>();
        allItems.add(items);
        traceBack(0,nums,set,items,allItems);
        System.out.println(allItems);
    }

    public static void traceBack(int i, int[] nums, HashSet<List<Integer>> set,
                                 List<Integer> items,
                                 List<List<Integer>> allItems){
        if (i >= nums.length)
            return;
        items.add(nums[i]);
        if (!set.contains(items)){
            allItems.add(new ArrayList<>(items));
            set.add(new ArrayList<>(items));
        }
        traceBack(i+1,nums,set,items,allItems);
        //回溯
        items.remove(items.size()-1);
        traceBack(i+1,nums,set,items,allItems);

    }

}
