import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CombinedTotalP39 {
    private static int target;
    public static void main(String[] args){
        int[] candidates = {2,3,5};
        target = 8;
        List<Integer> items = new ArrayList<>();
        List<List<Integer>> allItems = new ArrayList<>();
        HashSet<List<Integer>> set = new HashSet<>();
        backTrace(0, candidates, set, items, allItems, 0);
        System.out.println(allItems);
    }

    public static void backTrace(int i, int[] candidates,
                                 HashSet<List<Integer>> set,
                                 List<Integer> items,
                                 List<List<Integer>> allItems,
                                 int currTarget){
        if (i >= candidates.length || currTarget > target)
            return;
        items.add(candidates[i]);
        currTarget += candidates[i];
        if (currTarget == target && !set.contains(items)){
            allItems.add(new ArrayList<>(items));
            set.add(new ArrayList<>(items));
        }
        backTrace(i,candidates,set,items,allItems,currTarget);
        //回溯
        items.remove(items.size()-1);
        currTarget -= candidates[i];
        backTrace(i+1,candidates,set,items,allItems,currTarget);
    }
}
