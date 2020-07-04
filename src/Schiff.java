import java.awt.*;
import java.util.ArrayList;

public class Schiff {

    public ArrayList<Point> availableCords = new ArrayList<>();
    private final int initialSize;
    private final String ident;

    public Schiff(ArrayList<Point> c, int ini, String ident){
        for(Point p : c){
            availableCords.add(new Point(p));
        }
        this.initialSize = ini;
        this.ident = ident;
    }

    public boolean contains(Point p){
        for(Point x : this.availableCords){
            if(x.x == p.x && x.y == p.y) return true;
        }
        return false;
    }

    public void removeCord(Point p){
        for(int i = 0; i < this.availableCords.size(); i++){
            if(this.availableCords.get(i).x == p.x && this.availableCords.get(i).y == p.y){
                this.availableCords.remove(i);
                break;
            }
        }
    }

    public boolean checkIfDead(){
        return this.availableCords.size() == 0;
    }

    public int getInitialSize(){
        return this.initialSize;
    }

    public String getIdentification(){
        return this.ident;
    }
}

