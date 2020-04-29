import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class KI {

    private int[][] game, probs, enemy;
    private int[] ships = new int[4];
    private ArrayList<Point> cords = new ArrayList<>();
    private Point focusPoint = new Point(-1,-1);
    private Point probPoint = new Point(0,0);
    private boolean hit = false, deadEnd = false, gameEnd = false;
    private int size = 0;
    private int shots = 0, hits = 0;
    private int direction = 0;
    private PrintWriter out;
    private BufferedReader in;

    public KI(boolean first) throws IOException, ArrayIndexOutOfBoundsException{

        Socket socket = new Socket("localhost", 614);
        System.out.println("Connection established.");
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream());

        if(first){

            distribute(in.readLine());

            while(true){

                if(this.gameEnd){
                    socket.close();
                    break;
                }

                play();

                if(!this.gameEnd) System.out.println("Warte at play");
                //distribute(in.readLine());
            }
        }


    }

    private void distribute(String s) throws IOException, ArrayIndexOutOfBoundsException {

        String[] data = s.split(" ");

        switch(data[0]){
            case "size": setUpGame(data);
                         distribute(in.readLine());
                break;
            case "ships": setUpShips(data);
                break;
            case "shot": //this.out.write("answer " + shot(data) + "\n");
                break;
            case "answer": answers(data);
                break;
            case "load":
                break;
            case "save":
                break;
            default: System.out.println("Falsche Eingabe");
        }
    }

    private void play() throws IOException, ArrayIndexOutOfBoundsException {

        if (this.hit && this.direction == 0) {
            nextMove(this.probPoint);
        }

        if(!this.hit && !this.gameEnd){
            patternFinding();
            setProbabilityPoint(this.probPoint);
            printGame();
            kiShot(this.probPoint);
        }
    }

    private void checkIfGameOver() throws IOException{

        for (int ship : this.ships) if (ship != 0) return;

        this.gameEnd = true;
        System.out.println("Spiel ist vorbei!");
        System.out.println("Shots: " + this.shots);
        System.out.println("davon Hits: " + this.hits);
        System.out.printf("Trefferquote: %.2f%%%n", ((double) this.hits / this.shots) * 100);
    }

    private void setUpGame(String[] data) {

        this.size = Integer.parseInt(data[1]);
        this.game = new int[this.size][this.size];
        this.enemy = new int[this.size][this.size];
        this.probs = new int[this.size][this.size];

        setArrayTo0(this.enemy);
    }

    private void setUpShips(String[] data){

        for(int i = 1; i < data.length; i++){
            switch(data[i]){
                case "5": this.ships[0]++;
                    break;
                case "4": this.ships[1]++;
                    break;
                case "3": this.ships[2]++;
                    break;
                case "2": this.ships[3]++;
            }
        }
    }

    private String shot(String[] data){

        String s = "";
        int x = Integer.parseInt(data[1]);
        int y = Integer.parseInt(data[2]);

        if(this.game[x - 1][y - 1] == 0)
            s = "0";
        else
            s = "1";

        return s;
    }

    private void kiShot(Point p) throws IOException, ArrayIndexOutOfBoundsException {

        if(!this.gameEnd){
            this.shots++;
            this.out.write("shot " + (p.x + 1) + " " + (p.y + 1) + "\n");
            this.out.flush();
            distribute(in.readLine());
        }
    }

    private void answers(String[] data) throws IOException, ArrayIndexOutOfBoundsException {

        switch(data[1]){
            case "0": this.enemy[this.probPoint.x][this.probPoint.y] = 1;
                      System.out.println("Kein Hit");
                      printGame();
                      if(this.cords.size() != 0) this.deadEnd = true;
                      System.out.println("Warte");
                      //dis
                break;
            case "1": this.enemy[this.probPoint.x][this.probPoint.y] = 2;
                      System.out.println("Hit");
                      printGame();
                      if(this.hit && this.direction == 0) setDirection();
                      patternFinding();
                      this.hits++;

                      Point temp = new Point();
                      temp.setLocation(this.probPoint);
                      this.cords.add(temp);

                      this.hit = true;
                      play();
                break;
            case "2": this.hit = false;
                      this.enemy[this.probPoint.x][this.probPoint.y] = 2;
                      System.out.println("Hit - Versenkt");
                      printGame();

                      Point tempx = new Point();
                      tempx.setLocation(this.probPoint);
                      this.cords.add(tempx);

                      this.hits++;

                      this.deadEnd = true;
                      setBorders();
                      deleteShip();
                      this.direction = 0;
                      checkIfGameOver();
        }
    }

    private void patternFinding(){

        setArrayTo0(this.probs);
        for(int o = this.ships.length + 1; o >= 2 ; o--){
            if(this.ships[this.ships.length + 1 - o] != 0){
                int k, l;
                Point[] ar = new Point[o];
                for(int m = 0; m < 2; m++){
                    for(int i = 0; i < this.enemy.length; i++){
                        inner:
                        for(int j = 0; j < this.enemy.length - o + 1; j++) {
                            k = 0;
                            l = j;
                            while(l < this.enemy.length && k < o){
                                if ((this.enemy[i][l] != 0 && m == 0)|| (this.enemy[l][i] != 0 && m == 1)){
                                    j = l;
                                    continue inner;
                                }
                                if(m == 0)
                                    ar[k++] = new Point(i, l++);
                                else
                                    ar[k++] = new Point(l++, i);
                            }
                            for(Point p : ar) this.probs[p.x][p.y]++;
                        }
                    }
                }
            }
        }
    }

    private void printGame() throws ArrayIndexOutOfBoundsException{

        for(int i = 0; i < this.probs.length; i++){
            for(int j = 0; j < this.probs.length; j++){

                System.out.print(String.format("%3d", this.probs[i][j]));
            }
            System.out.print("   ");
            for(int j = 0; j < this.probs.length; j++){

                System.out.print(String.format("%3d", this.enemy[i][j]));
            }
            System.out.println();
        }
        System.out.println();
    }

    private void setProbabilityPoint(Point p){

        ArrayList<Point> points = new ArrayList<>();

        int max = 0;
        for(int i = 0; i < this.probs.length; i++){
            for(int j = 0; j < this.probs.length; j++){
                if(this.probs[i][j] > max){
                    points.clear();
                    points.add(new Point(i,j));
                    max = this.probs[i][j];
                }else if(this.probs[i][j] == max){
                    points.add(new Point(i,j));
                }
            }
        }

        p.setLocation(points.get((int) (Math.random() * points.size())));
        System.out.println(p.getLocation());
    }

    private void setArrayTo0(int[][] arr){

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
               arr[i][j] = 0;
            }
        }
    }

    private void nextMove(Point p) throws IOException {

        Point pmax;

        if(this.direction == 0){

            ArrayList<Point> temp = new ArrayList<>(4);
            temp.add(new Point(p.x - 1,p.y));
            temp.add(new Point(p.x + 1,p.y));
            temp.add(new Point(p.x,p.y - 1));
            temp.add(new Point(p.x,p.y + 1));

            for(int i = 0; i < temp.size(); i++){
                Point tempPoint = temp.get(i);
                if(tempPoint.x < 0 || tempPoint.x >= this.size || tempPoint.y < 0 || tempPoint.y >= this.size || this.enemy[tempPoint.x][tempPoint.y] == 1){
                    temp.remove(tempPoint);
                }
            }

            this.focusPoint = p;

            while(this.direction == 0 && this.hit){
                patternFinding();
                pmax = getMaxPoint(temp);
                this.probPoint = pmax;
                kiShot(this.probPoint);
            }
        }

        this.probPoint = p;

        for(int i = -1; i <= 1 && this.direction == 2 && this.hit; i += 2)  move(p, i, 0);

        for(int i = -1; i <= 1 && this.direction == 1 && this.hit; i += 2)  move(p, 0, i);

    }

    private Point getMaxPoint(ArrayList<Point> arr){

        int max = 0;
        Point p = new Point();

        for(int i = 0; i < arr.size(); i++){
            if(this.probs[arr.get(i).x][arr.get(i).y] >= max){
                max = this.probs[arr.get(i).x][arr.get(i).y];
                p = arr.get(i);
            }
        }
        return p;
    }

    private void setDirection(){

        if(this.probPoint.x == this.focusPoint.x) this.direction = 1;
        if(this.probPoint.y == this.focusPoint.y) this.direction = 2;
    }

    private void move(Point p, int cordx, int cordy) throws IOException {

        this.deadEnd = false;
        this.probPoint = p;

        while(!this.deadEnd){

            this.probPoint.x += cordx;
            this.probPoint.y += cordy;


            if(this.probPoint.x < 0 || this.probPoint.x >= this.size || this.probPoint.y < 0 || this.probPoint.y >= this.size || this.enemy[this.probPoint.x][this.probPoint.y] == 1){

                this.deadEnd = true;
                break;

            }else if(this.enemy[this.probPoint.x][this.probPoint.y] == 2){
                continue;
            }

            kiShot(this.probPoint);
        }
    }

    private void deleteShip() throws IOException {

        switch(this.cords.size()){

            case 5: this.ships[0]--; break;
            case 4: this.ships[1]--; break;
            case 3: this.ships[2]--; break;
            case 2: this.ships[3]--; break;
        }
        this.cords.clear();
    }

    private void setBorders(){

        for(int i = 0; i < this.cords.size(); i++){

            ArrayList<Point> temp = new ArrayList<>();
            for(int j = -1; j <= 1; j++){
                for(int k = -1; k <= 1; k++){
                    Point tempPoint = this.cords.get(i);
                    if(tempPoint.x + j >= 0 && tempPoint.x + j < this.size && tempPoint.y + k >= 0 && tempPoint.y + k < this.size)
                        temp.add(new Point(tempPoint.x + j, tempPoint.y + k));
                }
            }

            for(Point p : temp){
                if(this.enemy[p.x][p.y] != 2) this.enemy[p.x][p.y] = 1;
            }
        }
    }

    public static void main(String[] args){

        try{ new KI(true); } catch(Exception e){ e.printStackTrace(); }
    }
}
