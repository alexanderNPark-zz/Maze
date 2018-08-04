public class RoomGenerator {

    private RoomEntry[][] maze;
    private QuickUnion unionS;

    private int dimensionN;
    private int total;

    public RoomGenerator(int n){
        dimensionN = n;
        maze = new RoomEntry[dimensionN][dimensionN];
        unionS = new QuickUnion(n*n);
        total = dimensionN*dimensionN;
        for(int i=0;i<n;i++)
            for(int j=0;j<n;j++)
                maze[i][j] = new RoomEntry(n*i+j);



    }

    public void buildMaze(){
        int rand = 0;
        int other = 0;
        while(!unionS.isConnected(0,24)){
            rand  = (int)Math.random()*total;
            other = randomAdjacentRoom(rand);
            unionS.join(rand,other);
            buildPathInMaze(rand,other);
        }
    }



    public void buildPathInMaze(int room, int joiningRoom){
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
                case 1:
                    if((currentRoom%dimensionN)+1>=dimensionN)break;
                    else{
                        currentRoom+=1;
                        legit=true;
                    }
                case 2:
                    if(currentRoom+dimensionN>=total)break;
                    else{
                        currentRoom+=dimensionN;
                        legit=true;
                    }
                case 3:
                    if(currentRoom-dimensionN<0)break;
                    else{
                        currentRoom-=dimensionN;
                        legit=true;
                    }

            }
        }
        return currentRoom;

    }

}
