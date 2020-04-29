import javax.swing.*;
import java.awt.*;

public class Tile extends JPanel {

    private int state = 0;
    private final int cellsize = 40;

    public Tile(){
        setSize(this.cellsize,this.cellsize);
        setLayout(null);
        setBorder(BorderFactory.createLineBorder(Color.black));
    }

    public int getState(){
        return this.state;
    }
}
