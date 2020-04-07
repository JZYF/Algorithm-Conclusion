import java.util.ArrayList;
import java.util.List;

public class ParenthesesP22 {
    public static void main(String[] args){
        StringBuffer sbf = new StringBuffer();
        List<String> res = new ArrayList<>();
        int n = 2;
        backTrace(sbf, n, n, res);
        System.out.println(res);
    }

    public static void backTrace(StringBuffer sbf, int remindLeft, int remindRight,
                                 List<String> res){
        if (remindLeft == 0 && remindRight == 0){
            res.add(sbf.toString());
            return;
        }
        if (remindLeft > 0){
            sbf.append("(");
            backTrace(sbf,remindLeft-1,remindRight,res);
            sbf.delete(sbf.length()-1,sbf.length());
        }

        if (remindLeft < remindRight) {//即当前使用到的左括号多于右括号，才可能让左右括号匹配成功
            sbf.append(")");
            backTrace(sbf,remindLeft,remindRight-1,res);
            sbf.delete(sbf.length()-1,sbf.length());
        }

    }
}
