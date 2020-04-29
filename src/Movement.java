import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Movement implements MouseListener, MouseMotionListener {


    private int x;
    private int y;


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {


            if(SwingUtilities.isLeftMouseButton(e)){
                x = e.getX();
                y = e.getY();
            }else{
                Ship temp = (Ship) e.getComponent();
                temp.turn(e.getLocationOnScreen(), temp.getLocation());
            }


}

    @Override
    public void mouseReleased(MouseEvent e) {
        //e.getComponent().setLocation(xf,yf);
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

        if(SwingUtilities.isLeftMouseButton(e))
            e.getComponent().setLocation((e.getX() + e.getComponent().getX() - x),(e.getY() + e.getComponent().getY() - y));
    }

    @Override
    public void mouseMoved(MouseEvent e) {
       System.out.println(e.getLocationOnScreen());
    }
}
