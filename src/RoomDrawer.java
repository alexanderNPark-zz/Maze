import javax.swing.*;
import java.awt.*;

public class RoomDrawer extends JPanel {

    private JFrame mainFrame;
    private JPanel panel;
    private int startPoint = 25;
    private int roomDimension = 25;
    private int maxFrameLength;
    private RoomEntry[][] maze;


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
