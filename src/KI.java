import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class KI {

    private int[][] game, probs, enemy;
    private int[] shipsEnemy = new int[4];
    private int[] ships = new int[4];
    private ArrayList<Point> cords = new ArrayList<>();
    private ArrayList<Point> neighbours = new ArrayList<>();
    private ArrayList<Ship> myShips = new ArrayList<>();
    private Point focusPoint = new Point(-1,-1);
    private Point probPoint = new Point(-1,-1);
    private Point hitPoint = new Point(-1,-1);
    private boolean hit = false;
    private boolean gameEnd = false;
    private boolean turn;
    private boolean neighborsSet = false;
    private int size = 0;
    private int shots = 0 ;
    private int hits = 0;
    private int direction = 0;
    private int shipSum;
    private PrintWriter out;
    private BufferedReader in;

    public KI (String ip, boolean first) throws IOException{

        Socket socket = new Socket(ip, 50000);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream());
        this.turn = first;

        distribute(in.readLine());
        this.probs = Helper.patternFinding(this.enemy, this.shipsEnemy);
        Helper.printGame(this.probs, this.enemy);

        while(true){

            if(this.gameEnd){ socket.close(); break;}

            if(this.turn)
                play();
            else
                distribute(in.readLine());

            if(this.turn) distribute(in.readLine());
        }
    }

    private void distribute(String s){

         String[] data = s.split(" ");

        switch(data[0]){
            case "setup": setUpGame(data);
                break;
            case "shot":  shot(data);
                break;
            case "answer": answers(data);
                break;
            case "load":
                break;
            case "save":
                break;
            case "pass": return;

            default: System.out.println("Falsche Eingabe");
        }
    }

    private void play(){

        if(this.hit){
            nextMove(this.probPoint);
        }

        if(!this.hit && !this.gameEnd){

            this.probs = Helper.patternFinding(this.enemy, this.shipsEnemy);
            this.probPoint = Helper.getProbabilityPoint(this.probs);
            kiShot(this.probPoint);
        }
    }

    private void checkIfGameOver(){

        int s1 = 0;
        int s2 = 0;

        for(int i = 0; i < this.ships.length; i++){
            s1 += this.ships[i];
            s2 += this.shipsEnemy[i];
        }

        if(s1 == 0 || s2 == 0){
            this.gameEnd = true;
            if (s2 == 0) {
                System.out.println("Gewonnen!");
            } else {
                System.out.println("Verloren!");
            }
            System.out.println("Shots: " + this.shots);
            System.out.println("davon Hits: " + this.hits);
            System.out.println("Fehlertreffer: " + (this.shots - this.hits));
            System.out.printf("Trefferquote: %.2f%%%n", ((double) this.hits / this.shots) * 100);
        }
    }

    private void setUpGame(String[] data) {

        this.size = Integer.parseInt(data[1]);
        this.game = new int[this.size][this.size];
        this.enemy = new int[this.size][this.size];
        this.probs = new int[this.size][this.size];

        clearArray(this.enemy, 0);

        for(int i = 2; i < data.length; i++){

            int zahl = Integer.parseInt(data[i]);
            this.shipsEnemy[i-2] = zahl;
            this.ships[i-2] = zahl;
            this.shipSum += zahl;
        }

        placeShips();
        Helper.printGame(this.game, this.enemy);
        System.out.println("Confirmed");
        this.out.write("confirmed\n");
        this.out.flush();
    }

    public void clearArray(int[][] arr, int digit){

        for(int i = 0; i < arr.length; i++){
            for(int j = 0; j < arr.length; j++){
                if(arr[i][j] == digit) arr[i][j] = 0;
            }
        }
    }

    private void shot(String[] data){

        String line;

        int index = -1;
        Point p = new Point(Integer.parseInt(data[1]), Integer.parseInt(data[2]));

        for(int i = 0; i < this.myShips.size(); i++){
            if(this.myShips.get(i).containsPoint(p)){
                this.myShips.get(i).setHit(p);
                index = i;
                break;
            }
        }

        if(index == -1){
            line = "answer 0";
            this.turn = true;
        }else{
            if(this.myShips.get(index).checkIfDead()){
                line = "answer 2";
                this.turn = false;
                this.ships[5 - this.myShips.get(index).getSize()]--;
                checkIfGameOver();
            }else{
                line = "answer 1";
                this.turn = false;
            }
        }
        this.out.write(line + "\n");
        this.out.flush();
    }

    private void kiShot(Point p){

        this.shots++;
        this.out.write("shot " + p.x + " " + p.y + "\n");
        this.out.flush();
    }

    private void answers(String[] data){

        switch(data[1]){
            case "0": this.enemy[this.probPoint.x][this.probPoint.y] = 1;
                      System.out.println(this.probPoint);
                      this.turn = false;
                      System.out.println("Kein Hit");
                      if(!this.hit) this.probs = Helper.patternFinding(this.enemy, this.shipsEnemy);
                      Helper.printGame(this.probs, this.enemy);
                      this.out.write("pass\n");
                      this.out.flush();
                break;
            case "1": this.enemy[this.probPoint.x][this.probPoint.y] = 2;
                      System.out.println(this.probPoint);
                      System.out.println("Hit");
                      this.turn = true;
                      if(!this.hit) this.probs = Helper.patternFinding(this.enemy, this.shipsEnemy);
                      Helper.printGame(this.probs, this.enemy);
                      this.cords.add(new Point(this.probPoint));
                      if(this.cords.size() == 2) setDirection();
                      this.hits++;
                      this.hit = true;
                break;
            case "2": this.hit = false;
                      this.enemy[this.probPoint.x][this.probPoint.y] = 2;
                      System.out.println(this.probPoint);
                      System.out.println("Hit - Versenkt");
                      this.turn = true;
                      neighbours.clear();
                      neighborsSet = false;
                      this.cords.add(new Point(this.probPoint));
                      this.hits++;
                      setBorders(this.cords, this.enemy);
                      this.probs = Helper.patternFinding(this.enemy, this.shipsEnemy);
                      Helper.printGame(this.probs, this.enemy);
                      deleteShip();
                      this.direction = 0;
                      checkIfGameOver();
        }

    }

    private void nextMove(Point p){

        if(this.direction == 0){

            if(!neighborsSet){

                this.hitPoint = p;
                this.neighbours.add(new Point(p.x - 1, p.y));
                this.neighbours.add(new Point(p.x + 1, p.y));
                this.neighbours.add(new Point(p.x, p.y - 1));
                this.neighbours.add(new Point(p.x, p.y + 1));

                removeNeighbours();
                neighborsSet = true;
            }

            this.focusPoint = this.hitPoint;

            ArrayList <Point> cordi = new ArrayList<>();
            ArrayList <Point> cordj = new ArrayList<>();

            int[][] arr = new int[this.size][this.size];
            Helper.setArray(arr, 1);

            cordi.add(this.hitPoint);
            cordj.add(this.hitPoint);

            for(int i = -1; i < 2; i += 2){
                getSizeFromToPoint(cordi, i, 0);
                getSizeFromToPoint(cordj,0,i);
            }

            for (Point point : cordi) arr[point.x][point.y] = 0;

            for (Point point : cordj) arr[point.x][point.y] = 0;

            this.probs = Helper.patternFinding(arr, this.shipsEnemy);
            this.probPoint = getMaxPoint(this.neighbours);
            kiShot(this.probPoint);

        }else{
            move2();
        }
    }

    private void removeNeighbours(){

        //Point out of Bounds and field == 1
        for(int i = 0; i < this.neighbours.size(); i++){
            Point tempPoint = this.neighbours.get(i);
            if(Helper.pointOutOfBounds(tempPoint, this.size) || this.enemy[tempPoint.x][tempPoint.y] == 1){
                this.neighbours.remove(tempPoint);
                i--;
            }
        }

        ArrayList <Point> cordi = new ArrayList<>();
        ArrayList <Point> cordj = new ArrayList<>();

        cordi.add(this.hitPoint);
        cordj.add(this.hitPoint);

        for(int i = -1; i < 2; i += 2){
            getSizeFromToPoint(cordi, i, 0);
            getSizeFromToPoint(cordj,0, i);
        }

        removeNeighbourHelper(cordi);
        removeNeighbourHelper(cordj);
    }

    private void removeNeighbourHelper(ArrayList<Point> arr){

        int focus = 0;
        for(int i = 0; i < this.shipsEnemy.length; i++){
            if(this.shipsEnemy[3 - i] != 0){
                focus = 2 + i;
                break;
            }
        }

        for(int i = 0; i < this.shipsEnemy.length; i++) {
            if(this.shipsEnemy[i] != 0 && 5 - i == focus){
                if (arr.size() < 5 - i && arr.size() != 1) {
                    outer:
                    for (int j = 0; j < this.neighbours.size(); j++) {
                        for (Point point : arr) {
                            if (this.neighbours.get(j).x == point.x && this.neighbours.get(j).y == point.y) {
                                this.neighbours.remove(j--);
                                continue outer;
                            }
                        }
                    }
                }
            }
        }
    }

    private void getSizeFromToPoint(ArrayList<Point> arr, int i, int j){

        Point p = new Point(this.hitPoint.x + i, this.hitPoint.y + j);

        while(!Helper.pointOutOfBounds(p, this.size) && this.enemy[p.x][p.y] == 0){
            arr.add(new Point(p));
            p = new Point(p.x += i, p.y += j);
        }
    }

    private void move2(){

        ArrayList<Point> points = Helper.getStartAndEndPoints(this.cords);

        Point one = points.get(0);
        Point two = points.get(points.size() - 1);

        if(this.direction == 2) this.probPoint = getMaxPoint(new Point(one.x - 1, one.y), new Point(two.x + 1, two.y));
        if(this.direction == 1) this.probPoint = getMaxPoint(new Point(one.x, one.y - 1), new Point(two.x , two.y + 1));

        kiShot(this.probPoint);
    }

    private Point getMaxPoint(Point one ,Point two){

        if(Helper.pointOutOfBounds(one, this.size) || this.enemy[one.x][one.y] == 1) return two;
        if(Helper.pointOutOfBounds(two, this.size) || this.enemy[two.x][two.y] == 1) return one;

        ArrayList<Point> temp = new ArrayList<>(2);
        temp.add(one);
        temp.add(two);
        return this.getMaxPoint(temp);
    }

    private Point getMaxPoint(ArrayList<Point> arr){

        int max = 0;
        Point p = new Point();

        for (Point point : arr) {
            if (this.probs[point.x][point.y] >= max && this.enemy[point.x][point.y] == 0) {
                max = this.probs[point.x][point.y];
                p = point;
            }
        }
        return p;
    }

    private void setDirection(){

        if(this.probPoint.x == this.focusPoint.x) this.direction = 1;
        if(this.probPoint.y == this.focusPoint.y) this.direction = 2;
    }

    private void deleteShip(){

        switch(this.cords.size()){
            case 5: this.shipsEnemy[0]--; break;
            case 4: this.shipsEnemy[1]--; break;
            case 3: this.shipsEnemy[2]--; break;
            case 2: this.shipsEnemy[3]--; break;
        }
        this.cords.clear();
    }

    private void setBorders(ArrayList<Point> arr, int[][] field){

        for (Point point : arr) {

            ArrayList<Point> temp = new ArrayList<>();
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    if (point.x + j >= 0 && point.x + j < this.size && point.y + k >= 0 && point.y + k < this.size)
                        temp.add(new Point(point.x + j, point.y + k));
                }
            }

            for (Point p : temp) {
                if (field[p.x][p.y] != 2) field[p.x][p.y] = 1;
            }
        }
    }

    private void placeShips() {

        int count = 0;

        for(int i = 0; i < this.ships.length; i++) {
            if (this.ships[i] != 0) {
                for (int j = 0; j < this.ships[i]; j++) {
                    ArrayList<Point> cordsShip = new ArrayList<>(5 - i);
                    boolean success = false;
                    Point place = new Point(-1, -1);
                    int d;

                    if ((int) (Math.random() * 6 + 1) <= 3) d = 1;
                    else d = 2;
                    int x = 0;

                    while(!success){
                        x++;
                        place.setLocation((int) (Math.random() * this.size), (int) (Math.random() * this.size));
                        success = true;
                        while (success && cordsShip.size() < 5 - i) {

                            if (this.game[place.x][place.y] == 0) {
                                cordsShip.add(place);

                                if (d == 2)
                                    place = new Point(place.x + 1, place.y);
                                else
                                    place = new Point(place.x, place.y + 1);


                                if (place.x < 0 || place.x >= this.size || place.y < 0 || place.y >= this.size) {
                                    success = false;
                                    cordsShip.clear();
                                }


                                if (cordsShip.size() > 0 && ((d == 2 && place.x == this.size - 1) || (d == 1 && place.y == this.size - 1))) {
                                    success = false;
                                    cordsShip.clear();
                                }


                            } else {
                                success = false;
                                cordsShip.clear();
                            }

                        }

                        if(x > 1000){
                            count = -1;
                            break;
                        }

                    }
                    for (Point p : cordsShip) this.game[p.x][p.y] = 2;
                    this.myShips.add(new Ship(cordsShip, d));
                    setBorders(cordsShip, this.game);
                    cordsShip.clear();
                    count++;
                }
            }
        }
        clearArray(this.game, 1);

        if(count != this.shipSum){
            Helper.printGame(this.game, this.enemy);
            System.out.println("Retry");

            clearArray(this.game, 2);
            this.myShips.clear();
            placeShips();
        }
    }

    public static void main(String[] args) throws IOException {

        new KI("127.0.0.1",true);
    }
}
