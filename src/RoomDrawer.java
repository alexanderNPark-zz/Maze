import javax.swing.*;
import java.awt.*;

public class RoomDrawer extends JPanel {

    private JFrame mainFrame;
    private JPanel panel;
    private final int startPoint = 50;
    private final int roomDimension = 50;
    private int maxFrameLength;
    private RoomEntry[][] maze;


    public RoomDrawer(RoomEntry[][] maze){
        this.maze = maze;
        maxFrameLength = maze.length*(roomDimension+2);

        mainFrame = new JFrame();
        mainFrame.setSize(maxFrameLength+startPoint*2,maxFrameLength+startPoint*2);
        //panel = new JPanel();
        setSize(maxFrameLength,maxFrameLength);
        mainFrame.add(this);
        mainFrame.setVisible(true);

    }

    public void paint(Graphics g){
        Graphics2D canvas = (Graphics2D)g;
        canvas.setColor(Color.RED);
        for(int i=0;i<maze.length;i++){
            for(int j=0;j<maze.length;j++) {
                maze[i][j].draw(canvas, startPoint);
            }
        }


    }
}
