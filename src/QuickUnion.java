import java.util.Stack;

/**
 * This class is a weighted quick union.
 * It determines connectivity between nodes.
 * @author Alexander Park
 * @version 1.0
 * @since 8-12-18
 */
public class QuickUnion {

    /**
     * The array of integer ids.
     */
    private int[] disjointSet;


    /**
     * Constructor for the Quick Union structure
     * @param n the number of elements for the disjoint set
     */
    public QuickUnion(int n){

        disjointSet = new int[n];
        for(int i=0;i<n;i++)disjointSet[i]=-1;
    }

    /**
     * Returns the parent of a particular id/node.
     * Performs path compression after finding parent.
     * @param n the id/node of a set
     * @return the parent of the set this id/node is in
     */
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

    /**
     * Joins two disjoint sets via one element from each set.
     * Performs weighted set height addition
     * @param v1 id of the first node
     * @param v2 id of the second node
     */
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

    /**
     * Checks whether path exists or not.
     * @param v1 id of the first node
     * @param v2 id of the second node
     * @return is it connected or not
     */
    public boolean isConnected(int v1, int v2){
         return find(v1) == find(v2);
    }


}
