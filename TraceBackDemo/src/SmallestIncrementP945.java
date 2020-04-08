import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class SmallestIncrementP945 {
    private static int minOfCount = Integer.MAX_VALUE;

    public static void main(String[] args){
        int[] A = {9,8,5,8,0,1,1};
        HashSet<Integer> set = new HashSet<>();
        List<Integer> nums = new ArrayList<>();
        for (int temp : A){
            if (set.contains(temp)){
                nums.add(temp);
            } else {
                set.add(temp);
            }
        }
        if (set.isEmpty())
            return;
        backTrace(0, nums, set, 0);
        System.out.println(minOfCount);
    }

    public static void backTrace(int i, List<Integer> nums, HashSet<Integer> set, int count){
        if (i >= nums.size()){
            minOfCount = Math.min(minOfCount,count);
            return;
        }
        nums.set(i,nums.get(i)+1);
        count += 1;
        if (set.contains(nums.get(i))){
            backTrace(i,nums,set,count);
        } else {
            set.add(nums.get(i));
            backTrace(i+1,nums,set,count);
        }


    }

}
