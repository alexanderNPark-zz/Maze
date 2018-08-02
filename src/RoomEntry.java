import java.util.ArrayList;

public class RoomEntry {

    public static final int MAX_ROOMS=4;
    private int index;
    private ArrayList<Integer> otherPos= new ArrayList<Integer>(4);

    public RoomEntry(int _index){
        index=_index;
    }

    public boolean canGoTo(int v){
        for(int i=0;i<otherPos.size();i++)
            if(otherPos.get(i)==v)return true;

        return false;
    }

    public boolean add(int v){
        if(otherPos.size()>=MAX_ROOMS){
            return false;
        }
        otherPos.add(v);
        return true;
    }


}
