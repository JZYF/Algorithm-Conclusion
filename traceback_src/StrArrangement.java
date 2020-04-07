import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class StrArrangement {
    public static void main(String[] args){
        String oriStr = "aab";
        StringBuffer sbf = new StringBuffer();
        List<String> res = new ArrayList<>();
        HashSet<Integer> set = new HashSet<>();
        backTrace(oriStr, sbf, res, set);
        HashSet<String> resSet = new HashSet<>();
        for (String temp : res){
            resSet.add(temp);
        }
        String[] strRes = new String[resSet.size()];
        resSet.toArray(strRes);
        System.out.println(resSet);
    }

    public static void backTrace(String oriStr, StringBuffer currSbf,
                                 List<String> res, HashSet<Integer> set){
        if (currSbf.length() >= oriStr.length()){
            String temp = currSbf.toString();
            res.add(temp);
            return;
        }
        for (int i = 0; i < oriStr.length(); i++){
            if (set.contains(i))
                continue;
            set.add(i);
            currSbf.append(oriStr.charAt(i));
            backTrace(oriStr,currSbf,res,set);
            //回溯
            set.remove(i);
            currSbf.delete(currSbf.length()-1, currSbf.length());
        }

    }
}
