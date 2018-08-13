
import java.awt.*;
import java.util.ArrayList;

/**
 * This class is part of the Maze project. It represents the single Room of a maze.
 * @author Alexander Park
 * @version 1.0
 * @since 8-12-18
 */

public class RoomEntry implements Comparable<RoomEntry>{


    public static final int MAX_ROOMS=4; // each room has 4 doors max
    private int index; // the id of the room
    private ArrayList<RoomEntry> otherPos= new ArrayList<RoomEntry>(MAX_ROOMS);// container to hold other Rooms
    private int dimensionN;//dimension of the total maze
    private boolean partOfPath = false;//is it part of the solution
    public static boolean wantTOBeShown = false;//does the user want the path to show on the GUI


    public boolean NORTH_OPEN=false,SOUTH_OPEN=false,EAST_OPEN=false,WEST_OPEN = false;//door states


    /**
     * Constructor for Room Entry requires it's id and the dimension of the maze
     * @param _index id of this room
     * @param widthOfMaze dimension of the maze
     */
    public RoomEntry(int _index, int widthOfMaze){
        index=_index;
        dimensionN = widthOfMaze;

    }

    /**
     * Returns the list of RoomEntry's accessible to this one
     * @return the list of RoomEntry's
     */
    public ArrayList<RoomEntry> getOtherPos() {
        return otherPos;
    }

    /**
     * Returns this RoomEntry's id
     * @return the RoomEntry id
     */
    public int getID(){
        return index;
    }

    /**
     * Adds a RoomEntry into this current room's list of rooms it can go to.
     * @param re RoomEntry to be added
     * @return   whether or not is was possible to add
     *
     */
    public boolean add(RoomEntry re){

        if(otherPos.size()>=MAX_ROOMS){
            return false;
        }
        otherPos.add(re);
        setDoorState(re.getID());
        return true;
    }

    /**
     * Sets the door state based on the added rooms that this room can go to
     * @param newID  the id of the new RoomEntry that has been added
     */
    private void setDoorState(int newID){
        if(newID==index-1){
            WEST_OPEN=true;
        }
        else if(newID==index+1){
            EAST_OPEN = true;
        }
        else if(newID==index+dimensionN){
            SOUTH_OPEN = true;
        }
        else if (newID==index-dimensionN){
            NORTH_OPEN = true;
        }
    }

    /**
     * RoomEntry's can have opening doors to the entrance of the maze
     * or the exit of the maze, however these out of range ids can not be
     * graph-wise accessed since they don't exist. This method sets the door
     * states as if they do, but don't add a RoomEntry object to its list of
     * accessible rooms.
     * @param negative  the out of range id which is for drawing purposes
     * @return whether or not it is valid to add into out of range rooms
     */
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

    @Override
    public String toString(){
        /*
        //test code
        String childs = "(";
        for(int i=0; i<otherPos.size();i++){
            childs+=otherPos.get(i).getID()+", ";
        }
        */
        return ""+index;
    }

    /**
     * Sets this current RoomEntry as part of the found solution path
     * for drawing purposes
     * @param partOfPath whether or not it is part of the path
     */
    public void setPartOfPath(boolean partOfPath) {
        this.partOfPath = partOfPath;
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

    /**
     * Draws this current RoomEntry as part of the overall maze and/or solution path.
     * This uses offsets based on its position in the maze using the dimension of the maze.
     * @param g graphics provided by the GUI
     * @param offset starting point of the GUI and represents the dimensions of the room
     */
    public void draw(Graphics2D g,int offset){
        int startPoint=offset;
        if(!NORTH_OPEN){
            g.drawLine(startPoint+offset*(index%dimensionN),startPoint+offset*(index/dimensionN), startPoint+offset*(index%dimensionN)+offset, startPoint+offset*(index/dimensionN));
        }
        if(!SOUTH_OPEN){
            g.drawLine(startPoint+offset*(index%dimensionN),startPoint+offset*(index/dimensionN)+offset, startPoint+offset*(index%dimensionN)+offset, startPoint+offset*(index/dimensionN)+offset);
        }
        if(!EAST_OPEN){
            g.drawLine(startPoint+offset*(index%dimensionN)+offset,startPoint+offset*(index/dimensionN), startPoint+offset*(index%dimensionN)+offset, startPoint+offset*(index/dimensionN)+offset);
        }
        if(!WEST_OPEN){
            g.drawLine(startPoint+offset*(index%dimensionN),startPoint+offset*(index/dimensionN), startPoint+offset*(index%dimensionN), startPoint+offset*(index/dimensionN)+offset);
        }
        if(partOfPath && wantTOBeShown){
            g.fillOval(startPoint+offset*(index%dimensionN)+offset/4,startPoint+offset*(index/dimensionN)+offset/4,offset/2,offset/2);
        }
        //g.drawRect(offset+offset*(index/dimensionN),offset+offset*(index%dimensionN),offset,offset);

    }
}
