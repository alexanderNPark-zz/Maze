import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class QuickUnion {

    private int[] disjointSet;


    public QuickUnion(int n){

        disjointSet = new int[n];
        for(int i=0;i<n;i++)disjointSet[i]=-1;
    }

    public int find(int n){
        Stack<Integer> stack = new Stack<Integer>();
        while(!(disjointSet[n]<0)){
            stack.push(n);
            n=disjointSet[n];
        }
        while(stack.size()>0){
            disjointSet[stack.pop()] = n;
        }
        return n;
    }

    public void join(int v1, int v2){
        int parent1 = find(v1);
        int parent2 = find(v2);
        if(parent1==parent2){
            return;
        }
        int height1 = disjointSet[parent1];
        int height2 = disjointSet[parent2];
        if(height1>height2){//height2 has more because they are all negative
            disjointSet[parent1] = parent2;
            disjointSet[parent2]+=height1;
        }else{
            disjointSet[parent2] = parent1;
            disjointSet[parent1]+=height2;
        }
    }

    public boolean isConnected(int v1, int v2){
         return find(v1) == find(v2);
    }





}
