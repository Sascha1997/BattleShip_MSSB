import javax.swing.*;
import java.awt.*;

public class Matchfield extends JPanel{

    private int size;
    private int panelSize = 400;
    private final Tile[][] arr;
    private JPanel p1,p2,p3,p4;

    public Matchfield(int size){

        this.size = size;
        this.arr = new Tile[size][size];
        setShips();
        setupField();

        setLayout(null);
        setSize(900, 400);
        setBorder(BorderFactory.createLineBorder(Color.black));
        Movement mv = new Movement();
        addMouseMotionListener(mv);


    }

    public void setupField(){

        for(int i = 0; i < this.size; i++){
            for(int j = 0; j < this.size; j++){
                Tile temp = new Tile();
                temp.setOpaque(false);
                temp.setLocation(j * 40,i * 40);
                add(temp);
                this.arr[i][j] = temp;
            }
        }
    }

    public void drawField(){

        for(int i = 0; i < this.size; i++){
            for(int j = 0; j < this.size; j++){
                if(this.arr[i][j].getState() == 0){
                    this.arr[i][j].setBackground(Color.WHITE);

                }else{
                    this.arr[i][j].setBackground(Color.BLACK);
                }
            }
        }
    }

    public JPanel getPanel(){
        return this;
    }

    public void setShips(){

        Ship z = new Ship(2, 500 ,175);
        p1 = z.getPanel();
        p1.setLocation(500,175);
        add(p1);

        Ship d = new Ship(3,500,130);
        p2 = d.getPanel();
        p2.setLocation(500,130);
        add(p2);

        Ship v = new Ship(4,500,85);
        p3 = v.getPanel();
        p3.setLocation(500,85);
        add(p3);

        Ship f = new Ship(5,500,40);
        p4 = f.getPanel();
        p4.setLocation(500,40);
        add(p4);
    }
}
