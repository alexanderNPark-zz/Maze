import java.util.ArrayList;
import java.util.HashMap;

import java.util.Stack;

public class RoomGenerator {

    private RoomEntry[][] maze;
    private RoomEntry enter,exit;
    private QuickUnion unionS;

    private int dimensionN;
    private int total;
    private RoomEntry[] dfs,bfs;

    public static void main(String[] arg){
        int testInput =5;
        RoomGenerator rgen = new RoomGenerator(testInput);
        rgen.buildMaze();
        rgen.depthFirstSearch();
        System.out.println("______________________");
        rgen.breadthFirstSearch();
        rgen.printPath();
    }

    public RoomGenerator(int n){
        dimensionN = n;
        maze = new RoomEntry[dimensionN][dimensionN];
        unionS = new QuickUnion(n*n);
        total = dimensionN*dimensionN;
        for(int i=0;i<n;i++)
            for(int j=0;j<n;j++)
                maze[i][j] = new RoomEntry(n*i+j);

        enter = maze[0][0];
        exit = maze[dimensionN-1][dimensionN-1];


    }


    public void printPath(){
        int index=0;

        for(int i=0;i<dimensionN;i++){
            for(int j=0; j<dimensionN;j++){
                if(bfs[index].getID()==dimensionN*i+j){
                    System.out.print("X ");
                    index++;
                }
                else{
                    System.out.print("  ");
                }
            }
            System.out.println();
        }

        System.out.println("_______________________");
        index = 0;
        for(int i=0;i<dimensionN;i++){
            for(int j=0; j<dimensionN;j++){
                if(dfs[index].getID()==dimensionN*i+j){
                    System.out.print("X ");
                    index++;
                }
                else{
                    System.out.print("  ");
                }
            }
            System.out.println();
        }
    }


    public void buildMaze(){
        int rand = 0;
        int other = 0;
        while(!unionS.isConnected(enter.getID(),exit.getID())){
            rand  = (int)(Math.random()*total);
            other = randomAdjacentRoom(rand);
            //System.out.println(rand+","+other);
            if(unionS.find(rand)!=unionS.find(other)){
                unionS.join(rand,other);
                buildPathInMaze(rand,other);
            }

        }
    }


    public void depthFirstSearch(){

        boolean[] visted = new boolean[total];
        Stack<RoomEntry> rooms = new Stack<RoomEntry>();
        RoomEntry currentRoom = enter;
        visted[currentRoom.getID()] = true;
        rooms.push(currentRoom);

        HashMap<Integer,RoomEntry> parent = new HashMap<Integer, RoomEntry>();


        while(rooms.size()>0){
            currentRoom = rooms.pop();
            if(currentRoom.getID()==exit.getID()){
                break;
            }
            for(int i=0;i<currentRoom.getOtherPos().size();i++){
                RoomEntry child = currentRoom.getOtherPos().get(i);
                if(!visted[child.getID()]){
                    rooms.push(child);
                    visted[child.getID()] = true;
                    parent.put(child.getID(),currentRoom);
                }

            }
        }

        ArrayList<RoomEntry> pathReverse = new ArrayList<RoomEntry>();
        pathReverse.add(currentRoom);
        while(currentRoom!=enter){
            currentRoom=parent.get(currentRoom.getID());
            pathReverse.add(currentRoom);
        }
        dfs = new RoomEntry[pathReverse.size()];
        dfs = pathReverse.toArray(dfs);
        //test code
        System.out.println("DFS:");
        quickSort(dfs,0,dfs.length-1);
        for(int i=0;i<dfs.length;i++){
            System.out.println(dfs[i]);
        }

    }

    public void breadthFirstSearch(){

        boolean[] visted = new boolean[total];
        MyQueue<RoomEntry> rooms = new MyQueue<RoomEntry>();


        HashMap<Integer,RoomEntry> parent = new HashMap<Integer, RoomEntry>();

        RoomEntry currentRoom = enter;
        visted[currentRoom.getID()] = true;
        rooms.enqueue(currentRoom);

        while(rooms.size()>0){
            currentRoom = rooms.dequeue();

            if(currentRoom.getID()==exit.getID()){
                break;
            }
            for(int i=0;i<currentRoom.getOtherPos().size();i++){
                RoomEntry child = currentRoom.getOtherPos().get(i);
                if(!visted[child.getID()]){
                    rooms.enqueue(child);
                    visted[child.getID()] = true;
                    parent.put(child.getID(),currentRoom);
                }

            }
        }

        ArrayList<RoomEntry> pathReverse = new ArrayList<RoomEntry>();
        pathReverse.add(currentRoom);
        while(currentRoom!=enter){
            currentRoom=parent.get(currentRoom.getID());
            pathReverse.add(currentRoom);
        }
        bfs = new RoomEntry[pathReverse.size()];
        bfs = pathReverse.toArray(bfs);
        //test code
        System.out.println("BFS:");
        quickSort(bfs,0,bfs.length-1);
        for(int i=0;i<bfs.length;i++){
            System.out.println(bfs[i]);
        }


    }



    public void buildPathInMaze(int room, int joiningRoom){
        /**
         * Builds physical path through maze once disjoint activates
         */
        maze[room/dimensionN][room%dimensionN].add(maze[joiningRoom/dimensionN][joiningRoom%dimensionN]);
        maze[joiningRoom/dimensionN][joiningRoom%dimensionN].add(maze[room/dimensionN][room%dimensionN]);
    }

    public int randomAdjacentRoom(int currentRoom){
        boolean legit=false;
        int action = 0;
        while(!legit){
            action =(int)(Math.random()*4);
            switch(action){
                case 0:
                    if((currentRoom%dimensionN)-1<0)break;
                    else{
                        currentRoom-=1;
                        legit=true;
                    }
                    break;
                case 1:
                    if((currentRoom%dimensionN)+1>=dimensionN)break;
                    else{
                        currentRoom+=1;
                        legit=true;
                    }
                    break;
                case 2:
                    if(currentRoom+dimensionN>=total)break;
                    else{
                        currentRoom+=dimensionN;
                        legit=true;
                    }
                    break;
                case 3:
                    if(currentRoom-dimensionN<0)break;
                    else{
                        currentRoom-=dimensionN;
                        legit=true;
                    }
                    break;

            }
        }
        return currentRoom;

    }

    public void quickSort(RoomEntry[] array, int start, int end){
        if(start>=end){
            return;
        }
        int endPointer=end;
        int beginPointer = start;
        int pivotIndex = start+(end-start)/2;

        while(endPointer>beginPointer){
            while(endPointer>beginPointer && array[endPointer].compareTo(array[pivotIndex])>=0) endPointer--;
            while(endPointer>beginPointer && array[beginPointer].compareTo(array[pivotIndex])<0) beginPointer++;
            if(endPointer>beginPointer){
                RoomEntry temp = array[endPointer];
                array[endPointer] = array[beginPointer];
                array[beginPointer] = temp;
                beginPointer++;
                endPointer--;
            }
        }



        quickSort(array,start,endPointer-1);
        quickSort(array,beginPointer,end);


    }

}
