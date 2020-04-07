

import java.util.*;

class PairP365{
    public int fst;
    public int snd;
    public PairP365(int var1, int var2){
        this.fst = var1;
        this.snd = var2;
    }

    public boolean equals(Object o){
        if(o == null || this.getClass() != o.getClass())
            return false;
        if(o == this)
            return true;
        PairP365 PairP365 = (PairP365)o;
        return fst == PairP365.fst && snd == PairP365.snd;
    }

    public int hashCode(){
        return Objects.hash(fst, snd);
    }
}

public class KettleProblemP365 {

    private static int maxX;
    private static int maxY;


    public static void main(String[] args){
//        1
//        22003
//        31237
        boolean res = BFS(22003,31237,1);
        System.out.println(res);
//        int temp = Objects.hash(2,6);
//        System.out.println(temp);
    }

    public static boolean BFS(int x, int y, int z){
        if (x+y < z || z < 0)
            return false;
        if (z == 0)
            return true;
        PairP365 startState = new PairP365(0,0);
        Queue<PairP365> queue = new LinkedList<>();

        Set<PairP365> visited = new HashSet<>();
        maxX = x;
        maxY = y;
        visited.add(startState);

        queue.offer(startState);
        while (!queue.isEmpty()){
            PairP365 currState = queue.poll();
            if (currState.fst+currState.snd == z || currState.fst == z
                    || currState.snd == z)
                return true;
            List<PairP365> nextStates = getNextStates(currState.fst,
                    currState.snd);
            for (PairP365 temp : nextStates){
                if (!visited.contains(temp)){
                    queue.offer(temp);
                    visited.add(temp);
                }

            }
        }
        return false;
    }

    public static List<PairP365> getNextStates(int currX, int currY){
        List<PairP365> nextStates = new ArrayList<>();
        //把A灌满
        if (currX < maxX){
            PairP365 PairP365 = new PairP365(maxX,currY);
            nextStates.add(PairP365);
        }
        //把B灌满
        if (currY < maxY){
            PairP365 PairP365 = new PairP365(currX,maxY);
            nextStates.add(PairP365);
        }
        //把A倒空
        if (currX > 0){
            PairP365 PairP365 = new PairP365(0,currY);
            nextStates.add(PairP365);
        }
        //把B倒空
        if (currY > 0){
            PairP365 PairP365 = new PairP365(currX,0);
            nextStates.add(PairP365);
        }

        //A->B
        if (currX > 0){
            int detaY = maxY-currY;
            //A空，B不满(满)
            if (currX <= detaY){
                PairP365 PairP365 = new PairP365(0,currY+currX);
                nextStates.add(PairP365);
            }
            //A不空，B满
            if (currX > detaY){
                PairP365 PairP365 = new PairP365(currX-detaY,maxY);
                nextStates.add(PairP365);
            }
        }

        //B->A
        if (currY > 0){
            int detaX = maxX-currX;
            //B空，A不满(满)
            if (currY <= detaX){
                PairP365 PairP365 = new PairP365(currX+currY,0);
                nextStates.add(PairP365);
            }
            //B不满，A满
            if (currY > detaX){
                PairP365 PairP365 = new PairP365(maxX,currY-detaX);
                nextStates.add(PairP365);
            }
        }
        return nextStates;
    }

}
