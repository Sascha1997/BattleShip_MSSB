import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Ship {

    private boolean[] hits;
    private ArrayList<Point> cords = new ArrayList<>();

    public Ship(ArrayList<Point> cordss, int d){

        for(int i = 0; i < cordss.size(); i++){
            this.cords.add(new Point(cordss.get(i)));
        }


        this.hits = new boolean[cordss.size()];
        setHits();
    }



    public void printCords(){
        for(int i = 0; i < this.cords.size(); i++){
            System.out.println(this.hits[i]);
        }
    }

    public int getSize(){
        return this.cords.size();
    }

    public boolean checkIfDead(){
        for(int i = 0; i < this.hits.length; i++){
            if(!this.hits[i]) return false;
        }
        return true;
    }

    public boolean containsPoint(Point p){

        for(int i = 0; i < this.cords.size(); i++){
            if(this.cords.get(i).x == p.x && this.cords.get(i).y == p.y){
                return true;
            }
        }
        return false;
    }

    public void setHit(Point p){

        for(int i = 0; i < this.cords.size(); i++){
            if(this.cords.get(i).x == p.x && this.cords.get(i).y == p.y){
                this.hits[i] = true;
            }
        }
    }

    private void setHits(){
        Arrays.fill(this.hits, false);
    }
}
