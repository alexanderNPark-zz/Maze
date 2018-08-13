import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.Scanner;
import java.util.Stack;

/**
 * The main class of the Maze Project that builds a maze randomly or builds from file.
 * This class also solves the maze via DFS and BFS.
 * Run it and try out different numbers- recommended 20-30 because the size of your window may be too small
 * @author Alexander Park
 * @version 1.0
 * @since 8-12-18
 */


public class RoomGenerator {

    private RoomEntry[][] maze;//the actual maze
    private RoomEntry enter, exit;//entry and exit points
    private QuickUnion unionS;

    private int dimensionN;//dimensions
    private int total;//total number of rooms
    private RoomEntry[] dfs, bfs; // dfs and bfs paths
    boolean onePath = false; // one door open in the exit only

    //translation map from file
    private HashMap<Integer, Integer> roomOrientation = new HashMap<Integer, Integer>(4);


    public static void main(String[] arg) {


        RoomGenerator rgen = null;
        Scanner s = new Scanner(System.in);
        if (arg.length == 0) { // default no filename

            System.out.println("Enter square maze dimension:");
            rgen = new RoomGenerator(s.nextInt());
        } else {//if file name is provided
            rgen = new RoomGenerator(arg[0]);
        }
        //rgen.printPhysicalMaze(); // deprecated as character font offset is not equal to special character offset
        //RoomDrawer rd = new RoomDrawer(rgen.getMaze());
        rgen.breadthFirstSearch();
        rgen.printBFS();

        rgen.depthFirstSearch();
        rgen.printDFS();

        RoomDrawer rd = new RoomDrawer(rgen.getMaze());

        System.out.println("Would you like to see the path on the GUI? Press Any key and hit enter.");
        s.next();
        RoomEntry.wantTOBeShown = true;
        rd.repaint();
    }

    /**
     * Standard maze initialization that both constructors use.
     * Builds the empty full grid maze, initializes the RoomEntry objects in the array.
     * Builds the QuickUnion and marks the entry and exit RoomEntry objects of the maze
     */
    public void stdInit(){

        maze = new RoomEntry[dimensionN][dimensionN]; // builds 2D array
        total = dimensionN * dimensionN;//calculates total number of elements
        unionS = new QuickUnion(total);//builds QuickUnion object for connectivity

        for (int i = 0; i < dimensionN; i++)
            for (int j = 0; j < dimensionN; j++)
                maze[i][j] = new RoomEntry(dimensionN * i + j, dimensionN);

        enter = maze[0][0];
        exit = maze[dimensionN - 1][dimensionN - 1];
    }

    /**
     * Constructor given no file name
     * @param n dimension of the square maze
     */
    public RoomGenerator(int n) {
        dimensionN = n;//assigns dimension
        stdInit();//initializes
        enter.outOfRange(-dimensionN);//opens enter to have entry
        exit.outOfRange(total - 1 + dimensionN);//opens exit to have entry
        buildMaze(); // randomly builds maze

    }

    /**
     * Constructor given a filename
     * @param fileName the file name to which to load the maze
     */
    public RoomGenerator(String fileName) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName))));
            String line = br.readLine();//dimension reading
            dimensionN = Integer.parseInt(line);
            stdInit();//initializes maze

            //enter in translation based on position of 0 in the line of the file
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

    /**
     * Returns the 2D RoomEntry array
     * @return the maze or 2D array of RoomEntry
     */
    public RoomEntry[][] getMaze(){
        return maze;
    }

    /**
     * Initializes a room based on file line.
     * Only works if you build maze from file.
     * @param current id of the current room based on file line number
     * @param tokens the file line split by spaces (should be 0s or 1s)
     */
    private void generateRoomsIndexFromString(int current, String[] tokens) {

        int newI = 0;
        //RoomEntry object corresponding to room ID
        RoomEntry currentRoom = maze[current / dimensionN][current % dimensionN];

        for (int i = 0; i < tokens.length; i++) {

            if (Integer.parseInt(tokens[i]) == 0) {
                newI = current + roomOrientation.get(i);
                if (newI >= 0 && newI < total) {
                    currentRoom.add(maze[newI / dimensionN][newI % dimensionN]);
                } else {
                    currentRoom.outOfRange(newI); // for entry and exit points only
                }
            }
        }

    }


    /**
     * Prints out DFS Path from the array dfs attribute
     * Should print the same as BFS path
     */
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

    /**
     * Prints out the BFS Path from the bfs attribute.
     * Should print the same as DFS path
     */
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

    /**
     * Builds a random maze by randomly pairing rooms together
     * until there exists a path between exit and entry points of the maze.
     */
    public void buildMaze() {
        int rand = 0;
        int other = 0;
        while (!unionS.isConnected(enter.getID(), exit.getID())) {
            rand = (int) (Math.random() * total);//random index
            other = randomAdjacentRoom(rand);// random adjacent room from rand
            //System.out.println(rand+","+other);
            if (unionS.find(rand) != unionS.find(other)) {//check if there is a path already between them
                if(rand==exit.getID() || other==exit.getID()){
                    if(!onePath){//ensures one connection with exit and that's all
                        unionS.join(rand, other);//builds union path
                        buildPathInMaze(rand, other);//build physical path between
                        onePath=!onePath;
                    }
                }else{
                    unionS.join(rand, other);//builds union path between
                    buildPathInMaze(rand, other);//build physical path in maze
                }

            }

        }
        ensureOneConnectedComponent();
    }

    /**
     * This method ensures that all rooms in the maze are accessible.
     * This means the graph exists as a single connected component.
     */
    public void ensureOneConnectedComponent(){
        int mainHead = unionS.find(0);// the main path that will contain entry and exit points
        int newParent = 0;
        int currentRoom = 0;
        int newRoom = 0;
        boolean found = false;

        for(int i=0;i<dimensionN;i++){
            for(int j=0;j<dimensionN;j++){
                currentRoom = i*dimensionN+j;
                newParent = unionS.find(currentRoom);
                if(newParent!=mainHead){// not on the main path
                    //while(unionS.find(newRoom=randomAdjacentRoom(i*dimensionN+j))!=newParent);
                    for(int act = 0;act<4;act++){//check all adjacent rooms
                        if((newRoom=legit(act,currentRoom))>=0){//room that exists
                            if(unionS.find(newRoom)!=newParent){//not connected already
                                found=true;
                                break;
                            }
                        }
                    }
                    if(found){
                        unionS.join(i*dimensionN+j, newRoom);//join them
                        buildPathInMaze(i*dimensionN+j, newRoom);//physical build path
                        found = false;
                    }

                }
            }
        }

    }

    /**
     * Does DFS path solving
     */
    public void depthFirstSearch() {

        boolean[] visted = new boolean[total];//have visted the node already
        Stack<RoomEntry> rooms = new Stack<RoomEntry>();// the recursive stack
        RoomEntry currentRoom = enter;//starting room
        visted[currentRoom.getID()] = true;
        rooms.push(currentRoom);

        CustomLinkedList cll = new CustomLinkedList();//nodes visted
        //cll.add(currentRoom);
        HashMap<Integer, RoomEntry> parent = new HashMap<Integer, RoomEntry>();//path parent map


        while (rooms.size() > 0) {
            currentRoom = rooms.pop();
            cll.add(currentRoom);
            if (currentRoom.getID() == exit.getID()) {//if reached ending room
                break;
            }
            //moves all children not visted  onto stack
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
        //nodes visted printed
        System.out.println("DFS Nodes visited:");
        cll.printList();

        //print DFS path backwards
        System.out.println("DFS Path:");

        for (int i = 0; i < dfs.length; i++) {
            System.out.print(dfs[i].toString() + " ");
        }
        System.out.println();
        quickSort(dfs, 0, dfs.length - 1);
    }

    /**
     * Does BFS search path solving
     */
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

        //prints nodes visted
        System.out.println("BFS Nodes visited:");
        cll.printList();

        System.out.println("BFS Path:");
        for (int i = 0; i < bfs.length; i++) {
            System.out.print(bfs[i].toString() + " ");
        }
        System.out.println();
        quickSort(bfs, 0, bfs.length - 1);

    }

    /**
     * Builds physical path in the maze 2D array
     * @param room one room id
     * @param joiningRoom other room id
     */
    public void buildPathInMaze(int room, int joiningRoom) {
        /**
         * Builds physical path through maze once disjoint activates
         */
        maze[room / dimensionN][room % dimensionN].add(maze[joiningRoom / dimensionN][joiningRoom % dimensionN]);
        maze[joiningRoom / dimensionN][joiningRoom % dimensionN].add(maze[room / dimensionN][room % dimensionN]);
    }

    /**
     * Chooses a random room adjacent to this current room in the maze
     * @param currentRoom the current room id
     * @return the random adjacent room id to current room
     */
    public int randomAdjacentRoom(int currentRoom) {

        int action = (int) (Math.random() * 4);
        int newRoom = 0;
        while ((newRoom=legit(action,currentRoom))<0) {
            action = (int) (Math.random() * 4);
        }
        return newRoom;

    }

    /**
     * Checks whether the adjacent room via chosen action is possible in the maze
     * @param action go north, south, east, west? based on discrete numbers 0-3
     * @param currentRoom current room id
     * @return new room id or -1 if not legitimate index meaning not accessible in maze path
     */
    private int legit(int action, int currentRoom){
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

    /**
     * Quicksorts array recursively
     * @param array array to be sorted
     * @param start start index
     * @param end end index
     */
    public void quickSort(RoomEntry[] array, int start, int end) {

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
