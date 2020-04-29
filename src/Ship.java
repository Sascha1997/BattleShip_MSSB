import javax.swing.*;
import java.awt.*;

public class Ship extends JPanel{

    private int size;
    private Tile[] tiles;
    private boolean vertikal = false;
    private Movement mv = new Movement();


    public Ship(int size,int x, int y){
        this.size = size;
        this.tiles = new Tile[size];

        setLayout(null);
        setBackground(Color.BLACK);
        setSize(this.size * 40, 40);

        addMouseListener(mv);
        addMouseMotionListener(mv);

    }

    public void turn(Point p, Point c){


        if(!vertikal){
            setSize(40, this.size * 40);
            setLocation((int) p.getX() - (int)c.getX() ,(int) p.getY() - (int)c.getY());
            vertikal = true;
        }else{
            setSize(this.size * 40, 40);
            setLocation((int)c.getX() - (int) p.getX() ,(int)c.getY() - (int) p.getY());
            vertikal = false;

        }
    }

    public JPanel getPanel(){
        return this;
    }



}
