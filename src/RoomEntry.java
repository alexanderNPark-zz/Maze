import java.util.ArrayList;
import java.util.HashMap;

public class RoomEntry implements Comparable<RoomEntry>{

    public static final int MAX_ROOMS=4;
    private int index;
    private ArrayList<RoomEntry> otherPos= new ArrayList<RoomEntry>(MAX_ROOMS);
    private boolean[] openDoorsForDrawing = new boolean[MAX_ROOMS];
    private int dimensionN;


    private boolean NORTH_OPEN=false,SOUTH_OPEN=false,EAST_OPEN=false,WEST_OPEN = false;



    public RoomEntry(int _index, int widthOfMaze){
        index=_index;
        dimensionN = widthOfMaze;

    }

    /*
    public boolean canGoTo(int v){
        for(int i=0;i<otherPos.size();i++)
            if(otherPos.get(i)==v)return true;

        return false;
    }

*/

    public ArrayList<RoomEntry> getOtherPos() {
        return otherPos;
    }

    public int getID(){
        return index;
    }

    public boolean add(RoomEntry re){
        if(otherPos.size()>=MAX_ROOMS){
            return false;
        }
        otherPos.add(re);
        if(re.getID()==index+1){
            WEST_OPEN=true;
        }
        else if(re.getID()==index-1){
            EAST_OPEN = true;
        }
        else if(re.getID()==index+dimensionN){
            SOUTH_OPEN = true;
        }
        else{
            NORTH_OPEN = true;
        }
        return true;
    }

    public boolean outOfRange(int negative){
        if(index==0){
            if(negative==-dimensionN){
                NORTH_OPEN=true;
            }
            if(negative==-1){
                WEST_OPEN=true;
            }
            return true;
        }
        if(index==dimensionN*dimensionN-1){
            if(negative==index+1){
                EAST_OPEN=true;
            }
            if(negative==index+dimensionN){
                SOUTH_OPEN=true;
            }
            return true;
        }
        return false;

    }


    public String toString(){
        /*
        String childs = "(";
        for(int i=0; i<otherPos.size();i++){
            childs+=otherPos.get(i).getID()+", ";
        }
        */
        return ""+index;
    }


    @Override
    public int compareTo(RoomEntry o) {
        if(o.getID()<this.getID()){
            return 1;
        }
        else if (o.getID()>this.getID()){
            return -1;
        }
        return 0;
    }
}
