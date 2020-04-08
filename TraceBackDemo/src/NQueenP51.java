import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NQueenP51 {
    private static int[] rowSteps = {-1,1,0,0,-1,1,-1,1}; //上下左右 左上 左下 右上 右下
    private static int[] colSteps = {0,0,-1,1,-1,-1,1,1};
    public static void main(String[] args){
        int N = 8;
        char[][] visited = new char[N][N];
        char[][] checks = new char[N][N];
        List<List<String>> res = new ArrayList<>();
        fillArr(visited,'0');
        fillArr(checks,'.');
        backTrace(0,visited,checks,res);
        System.out.println(res);
    }

    public static void fillArr(char[][] arr, char ch){
        for (int i = 0; i < arr.length; i++){
            for (int j = 0; j < arr[0].length; j++)
                arr[i][j] = ch;
        }
    }


    public static void backTrace(int numOfQueen, char[][] visited, char[][] checks,
                                 List<List<String>> res){
        if (numOfQueen >= checks.length){
            //将结果添加到res
            List<String> tempList = new ArrayList<>();
            for (int i = 0; i < checks.length; i++){
                StringBuffer sbf = new StringBuffer();
                for (int j = 0; j < checks.length; j++){
                    sbf.append(checks[i][j]);
                }
                tempList.add(sbf.toString());
            }
            res.add(new ArrayList<>(tempList));
            return;
        }
        for (int col = 0; col < checks[0].length; col++){
            if (visited[numOfQueen][col] == '0'){

                char[][] temp = copyArr(visited);
                //放置皇后
                checks[numOfQueen][col] = 'Q';
                //标记攻击位置
                putQueen(numOfQueen,col,visited);

                backTrace(numOfQueen+1,visited,checks,res);

                //回溯
                checks[numOfQueen][col] = '.';
                visited = temp;
            }
        }
    }

    public static void putQueen(int numOfQueen,int col,char[][] visited){
        //从当前位置走1~n-1步
        for (int step = 1; step < visited.length; step++){
            for (int j = 0; j < 8; j++){
                int currRStep = step*rowSteps[j]+numOfQueen;
                int currCStep = step*colSteps[j]+col;
                if (currRStep >= 0 && currRStep < visited.length
                && currCStep >= 0 && currCStep < visited.length){
                    visited[currRStep][currCStep] = '1';
                }
            }
        }
    }

    public static char[][] copyArr(char[][] oriArr){
        char[][] res = new char[oriArr.length][oriArr[0].length];
        for (int i = 0; i < oriArr.length; i++){
            for (int j = 0; j < oriArr[0].length; j++){
                res[i][j] = oriArr[i][j];
            }
        }
        return res;
    }



}
