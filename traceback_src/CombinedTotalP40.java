import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class CombinedTotalP40 {
    private static int target;
    public static void main(String[] args){
        int[] nums = {10,1,2,7,6,1,5};
        Arrays.sort(nums);
        int t = 8;
        target = t;
        List<Integer> items = new ArrayList<>();
        List<List<Integer>> allItems = new ArrayList<>();
        HashSet<List<Integer>> set = new HashSet<>();
        traceBack(0, nums, set, items, allItems, 0);
        System.out.println(allItems);

    }
    public static void traceBack(int i, int[] candidates, HashSet<List<Integer>> set,
                                 List<Integer> items, List<List<Integer>> allItems,
                                 int currTarget){
        if (i >= candidates.length || currTarget > target) //第二个条件是剪枝
            return;
        items.add(candidates[i]);
        currTarget += candidates[i];
        if (currTarget == target && !set.contains(items)){
            allItems.add(new ArrayList<>(items));
            set.add(new ArrayList<>(items));
        }
        traceBack(i+1, candidates, set, items, allItems, currTarget);
        //回溯
        items.remove(items.size()-1);
        currTarget -= candidates[i];
        traceBack(i+1, candidates, set, items, allItems, currTarget);
    }
}
