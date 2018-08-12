import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.Scanner;
import java.util.Stack;

public class RoomGenerator {

    private RoomEntry[][] maze;
    private RoomEntry enter, exit;
    private QuickUnion unionS;

    private int dimensionN;
    private int total;
    private RoomEntry[] dfs, bfs;

    private HashMap<Integer, Integer> roomOrientation = new HashMap<Integer, Integer>(4);

    public static void main(String[] arg) {


        RoomGenerator rgen = null;
        if (arg.length == 0) {
            Scanner s = new Scanner(System.in);
            System.out.println("Enter square maze dimension:");
            rgen = new RoomGenerator(s.nextInt());
        } else {
            rgen = new RoomGenerator(arg[0]);
        }
        //rgen.printPhysicalMaze(); // deprecated as character font offset is not equal to special character offset
        //RoomDrawer rd = new RoomDrawer(rgen.getMaze());
        rgen.breadthFirstSearch();
        rgen.printBFS();

        rgen.depthFirstSearch();
        rgen.printDFS();
        RoomDrawer rd = new RoomDrawer(rgen.getMaze());
    }


    public void stdInit(){

        maze = new RoomEntry[dimensionN][dimensionN];
        total = dimensionN * dimensionN;
        unionS = new QuickUnion(total);

        for (int i = 0; i < dimensionN; i++)
            for (int j = 0; j < dimensionN; j++)
                maze[i][j] = new RoomEntry(dimensionN * i + j, dimensionN);

        enter = maze[0][0];
        exit = maze[dimensionN - 1][dimensionN - 1];
    }

    public RoomGenerator(int n) {
        dimensionN = n;
        stdInit();
        enter.outOfRange(-dimensionN);
        exit.outOfRange(total - 1 + dimensionN);
        buildMaze();

    }


    public RoomGenerator(String fileName) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName))));
            String line = br.readLine();//dimension reading
            dimensionN = Integer.parseInt(line);
            stdInit();

            roomOrientation.put(0, -dimensionN);
            roomOrientation.put(1, dimensionN);
            roomOrientation.put(2, 1);
            roomOrientation.put(3, -1);


            int current = 0;
            while (current < total && (line = br.readLine()) != null) {
                //System.out.println(current+" "+ line);
                generateRoomsIndexFromString(current, line.split(" "));
                current++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RoomEntry[][] getMaze(){
        return maze;
    }

    public void generateRoomsIndexFromString(int current, String[] tokens) {

        int newI = 0;
        RoomEntry currentRoom = maze[current / dimensionN][current % dimensionN];

        for (int i = 0; i < tokens.length; i++) {

            if (Integer.parseInt(tokens[i]) == 0) {
                newI = current + roomOrientation.get(i);
                if (newI >= 0 && newI < total) {
                    currentRoom.add(maze[newI / dimensionN][newI % dimensionN]);
                } else {
                    currentRoom.outOfRange(newI);
                }
            }
        }

    }



    public void printDFS() {
        int index = 0;
        for (int i = 0; i < dimensionN; i++) {
            for (int j = 0; j < dimensionN; j++) {
                if (dfs[index].getID() == dimensionN * i + j) {
                    System.out.print("X ");
                    index++;
                } else {
                    System.out.print("  ");
                }
            }
            System.out.println();
        }
    }

    public void printBFS() {
        int index = 0;

        for (int i = 0; i < dimensionN; i++) {
            for (int j = 0; j < dimensionN; j++) {
                if (bfs[index].getID() == dimensionN * i + j) {
                    System.out.print("X ");
                    bfs[index].setPartOfPath(true);
                    index++;
                } else {
                    System.out.print("  ");
                }
            }
            System.out.println();
        }
    }


    public void buildMaze() {
        int rand = 0;
        int other = 0;
        while (!unionS.isConnected(enter.getID(), exit.getID())) {
            rand = (int) (Math.random() * total);
            other = randomAdjacentRoom(rand);
            //System.out.println(rand+","+other);
            if (unionS.find(rand) != unionS.find(other)) {
                unionS.join(rand, other);
                buildPathInMaze(rand, other);
            }

        }
        ensureOneConnectedComponent();
    }

    public void ensureOneConnectedComponent(){
        int mainHead = unionS.find(0);
        int newParent = 0;
        int currentRoom = 0;
        int newRoom = 0;
        boolean found = false;

        for(int i=0;i<dimensionN;i++){
            for(int j=0;j<dimensionN;j++){
                currentRoom = i*dimensionN+j;
                newParent = unionS.find(currentRoom);
                if(newParent!=mainHead){
                    //while(unionS.find(newRoom=randomAdjacentRoom(i*dimensionN+j))!=newParent);
                    for(int act = 0;act<4;act++){
                        if((newRoom=legit(act,currentRoom))>=0){
                            if(unionS.find(newRoom)!=newParent){
                                found=true;
                                break;
                            }
                        }
                    }
                    if(found){
                        unionS.join(i*dimensionN+j, newRoom);
                        buildPathInMaze(i*dimensionN+j, newRoom);
                        found = false;
                    }

                }
            }
        }

    }


    public void depthFirstSearch() {

        boolean[] visted = new boolean[total];
        Stack<RoomEntry> rooms = new Stack<RoomEntry>();
        RoomEntry currentRoom = enter;
        visted[currentRoom.getID()] = true;
        rooms.push(currentRoom);

        CustomLinkedList cll = new CustomLinkedList();
        //cll.add(currentRoom);
        HashMap<Integer, RoomEntry> parent = new HashMap<Integer, RoomEntry>();


        while (rooms.size() > 0) {
            currentRoom = rooms.pop();
            cll.add(currentRoom);
            if (currentRoom.getID() == exit.getID()) {
                break;
            }
            for (int i = 0; i < currentRoom.getOtherPos().size(); i++) {
                RoomEntry child = currentRoom.getOtherPos().get(i);
                if (!visted[child.getID()]) {
                    rooms.push(child);
                    visted[child.getID()] = true;
                    parent.put(child.getID(), currentRoom);

                }

            }
        }

        ArrayList<RoomEntry> pathReverse = new ArrayList<RoomEntry>();
        pathReverse.add(currentRoom);
        while (currentRoom != enter) {
            currentRoom = parent.get(currentRoom.getID());
            pathReverse.add(currentRoom);
        }
        dfs = new RoomEntry[pathReverse.size()];
        dfs = pathReverse.toArray(dfs);
        //test code
        System.out.println("DFS Nodes visited:");
        cll.printList();

        System.out.println("DFS Path:");

        for (int i = 0; i < dfs.length; i++) {
            System.out.print(dfs[i].toString() + " ");
        }
        System.out.println();
        quickSort(dfs, 0, dfs.length - 1);
    }

    public void breadthFirstSearch() {

        boolean[] visted = new boolean[total];
        MyQueue<RoomEntry> rooms = new MyQueue<RoomEntry>();


        HashMap<Integer, RoomEntry> parent = new HashMap<Integer, RoomEntry>();
        CustomLinkedList cll = new CustomLinkedList();

        RoomEntry currentRoom = enter;
        visted[currentRoom.getID()] = true;

        rooms.enqueue(currentRoom);

        while (rooms.size() > 0) {
            currentRoom = rooms.dequeue();
            cll.add(currentRoom);
            if (currentRoom.getID() == exit.getID()) {
                break;
            }
            for (int i = 0; i < currentRoom.getOtherPos().size(); i++) {
                RoomEntry child = currentRoom.getOtherPos().get(i);
                if (!visted[child.getID()]) {
                    rooms.enqueue(child);
                    visted[child.getID()] = true;
                    parent.put(child.getID(), currentRoom);

                }

            }
        }

        ArrayList<RoomEntry> pathReverse = new ArrayList<RoomEntry>();
        pathReverse.add(currentRoom);
        while (currentRoom != enter) {
            currentRoom = parent.get(currentRoom.getID());
            pathReverse.add(currentRoom);
        }
        bfs = new RoomEntry[pathReverse.size()];
        bfs = pathReverse.toArray(bfs);
        //test code

        System.out.println("BFS Nodes visited:");
        cll.printList();

        System.out.println("BFS Path:");
        for (int i = 0; i < bfs.length; i++) {
            System.out.print(bfs[i].toString() + " ");
        }
        System.out.println();
        quickSort(bfs, 0, bfs.length - 1);

    }


    public void buildPathInMaze(int room, int joiningRoom) {
        /**
         * Builds physical path through maze once disjoint activates
         */
        maze[room / dimensionN][room % dimensionN].add(maze[joiningRoom / dimensionN][joiningRoom % dimensionN]);
        maze[joiningRoom / dimensionN][joiningRoom % dimensionN].add(maze[room / dimensionN][room % dimensionN]);
    }

    public int randomAdjacentRoom(int currentRoom) {

        int action = (int) (Math.random() * 4);
        int newRoom = 0;
        while ((newRoom=legit(action,currentRoom))<0) {
            action = (int) (Math.random() * 4);
        }
        return newRoom;

    }

    public int legit(int action, int currentRoom){
        switch (action) {
            case 0:
                if ((currentRoom % dimensionN) - 1 < 0) return -1;
                else {
                    currentRoom -= 1;
                    return currentRoom;
                }

            case 1:
                if ((currentRoom % dimensionN) + 1 >= dimensionN) return -1;
                else {
                    currentRoom += 1;
                    return currentRoom;
                }

            case 2:
                if (currentRoom + dimensionN >= total) return -1;
                else {
                    currentRoom += dimensionN;
                    return currentRoom;
                }

            case 3:
                if (currentRoom - dimensionN < 0) return -1;
                else {
                    currentRoom -= dimensionN;
                    return currentRoom;
                }


        }
        return -1;
    }

    public void quickSort(RoomEntry[] array, int start, int end) {
        /**
         * Quicksorts using RoomEntry's compareTo method, sorts in increasing order
         *
         */
        if (start >= end) {
            return;
        }
        int endPointer = end;
        int beginPointer = start;
        RoomEntry pivot = array[start + (end - start) / 2];


        while (endPointer >= beginPointer) {
            while (endPointer >= beginPointer && array[endPointer].compareTo(pivot) > 0) endPointer--;
            while (endPointer >= beginPointer && array[beginPointer].compareTo(pivot) < 0) beginPointer++;
            if (endPointer >= beginPointer) {
                RoomEntry temp = array[endPointer];
                array[endPointer] = array[beginPointer];
                array[beginPointer] = temp;
                beginPointer++;
                endPointer--;
            }
        }


        quickSort(array, start, endPointer);
        quickSort(array, beginPointer, end);


    }

}
