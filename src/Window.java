import javax.swing.*;
import java.awt.*;

public class Window {

    public Window(){

        JFrame window = new JFrame("Window");
        window.setSize(1000,500);
        window.setLayout(null);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Container c = window.getContentPane();

        Matchfield field = new Matchfield(10);
        JPanel p1 = field.getPanel();
        p1.setLocation(40,20);
        c.add(p1);


        window.setVisible(true);
    }

    public static void main(String[] args){
        new Window();
    }
}
