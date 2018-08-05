import java.util.ArrayList;

public class RoomEntry implements Comparable<RoomEntry>{

    public static final int MAX_ROOMS=4;
    private int index;
    private ArrayList<RoomEntry> otherPos= new ArrayList<RoomEntry>(4);

    public RoomEntry(int _index){
        index=_index;
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
        return true;
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
