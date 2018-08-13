import javax.swing.*;

import java.awt.*;

/**
 * This is the GUI class that draws the maze and the solution path of the maze,
 * provided taht the solution is marked inside the path.
 * @author Alexander Park
 * @version 1.0
 * @since 8-12-18
 */
public class RoomDrawer extends JPanel {

    private JFrame mainFrame;// the main JFrame
    private int startPoint = 25;// the start point which equals the offset
    private int roomDimension = 25;//the dimension of the square that makes a room
    private int maxFrameLength; // dimension of the frame
    private RoomEntry[][] maze;// the said maze

    /**
     * Initializes and draws the maze.
     * @param maze the 2D array of RoomEntries that represent the graph
     */
    public RoomDrawer(RoomEntry[][] maze){
        this.maze = maze;
        maxFrameLength = maze.length*(roomDimension+2);

        mainFrame = new JFrame();
        mainFrame.setTitle(maze.length>10? "This is gonna be tough..." : "This should be easy");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(maxFrameLength+startPoint*3,maxFrameLength+startPoint*3);
        //panel = new JPanel();
        setSize(maxFrameLength+startPoint*3,maxFrameLength+startPoint*3);
        mainFrame.add(this);
        mainFrame.setVisible(true);

    }
    @Override
    public void paint(Graphics g){
        Graphics2D canvas = (Graphics2D)g;
        canvas.setColor(Color.white);
        canvas.fillRect(0,0,maxFrameLength+startPoint*3,maxFrameLength+startPoint*3);
        canvas.setColor(Color.RED);
        for(int i=0;i<maze.length;i++){
            for(int j=0;j<maze.length;j++) {
                maze[i][j].draw(canvas, startPoint);
            }
        }

    }
}
