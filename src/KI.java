import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class KI extends Thread{

    private int size;
    private int[][] gameField, probField, enemyField;
    private int[] availableShipsEnemy = new int[4], availableShipsKI = new int[4];
    private ArrayList<Point> cords = new ArrayList<>(), neighbours = new ArrayList<>();
    private ArrayList<Ship> shipsKI = new ArrayList<>();
    private Point focusPoint = new Point(-1,-1), probPoint = new Point(-1,-1), hitPoint = new Point(-1,-1);
    private boolean hit = false, gameEnd = false, turn, neighborsSet = false;
    private int shots = 0, hits = 0, direction = 0, shipSum = 0;
    private PrintWriter out;
    private Files f = new Files();
    private Socket socket;
    private String setup;

    public KI(Socket socket, boolean first){
        this.turn = first;
        this.socket = socket;
    }

    public KI(Socket socket, boolean first, String setup){
        this(socket, first);
        this.setup = setup;
    }

    public void run(){

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.out = new PrintWriter(this.socket.getOutputStream());

            if(!this.turn) {
                this.setup = in.readLine();
            }else {
                this.out.write(this.setup + "\n");
                this.out.flush();
            }

            distribute(this.setup);

            if(this.turn && !in.readLine().equals("confirmed")){
                System.out.println("Something is wrong");
                System.exit(0);
            }

            if(setup.startsWith("s")) probField = Helper.patternFinding(this.enemyField, this.availableShipsEnemy);
            Helper.printGame(this.probField, this.enemyField);

            while (true) {
                if (this.gameEnd) {
                    this.socket.close();
                    break;
                }

                if (this.turn)
                    play();
                else
                    distribute(in.readLine());

                if (this.turn) distribute(in.readLine());
            }

            Helper.printGame(this.gameField, this.enemyField);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void distribute(String s){

        String[] data = s.split(" ");

        switch(data[0]){
            case "setup": setUpGame(data);
                break;
            case "shot": shotEnemy(data);
                break;
            case "answer": answers(data);
                break;
            case "load": load(data);
                break;
            case "save": save(data);
                this.out.write("pass\n");
                this.out.flush();
                break;
            case "pass": return;
            default: System.out.println("Falsche Eingabe");
                this.out.write("pass\n");
                this.out.flush();
        }
    }

    private void load(String[] data){
        this.f.load(data[1]);

        this.turn = this.f.getTurn();
        this.gameField = this.f.getOwnField();
        this.enemyField = this.f.getEnemyField();
        this.probField = this.f.getProb();
        this.availableShipsKI = this.f.getOwnShips();
        this.availableShipsEnemy = this.f.getEnemyShips();
        this.cords = this.f.getCords();
        this.neighbours = this.f.getNeighbours();

        ArrayList<Point> temp = this.f.getPoints();

        this.focusPoint = temp.get(0);
        this.probPoint = temp.get(1);
        this.hitPoint = temp.get(2);

        this.shipsKI = this.f.getShips();

        boolean[] bool = this.f.getBool();

        this.hit = bool[0];
        this.gameEnd = bool[1];
        this.neighborsSet = bool[2];

        int[] ints = this.f.getInts();

        this.size = ints[0];
        this.shots = ints[1];
        this.hits = ints[2];
        this.direction = ints[3];
        this.shipSum = ints[4];

        if(!this.turn){
            this.out.write("confirmed\n");
            this.out.flush();
        }
    }

    private void save(String[] data) {

        ArrayList<Point> points = new ArrayList<>(3);
        points.add(this.focusPoint);
        points.add(this.probPoint);
        points.add(this.hitPoint);

        boolean[] bool = {this.hit, this.gameEnd, this.neighborsSet};
        int[] integer = {this.size, this.shots, this.hits, this.direction, this.shipSum};

        this.f.save(data[1], this.turn, gameField, enemyField, probField, this.availableShipsKI, this.availableShipsEnemy, this.cords, this.neighbours, points, this.shipsKI, bool, integer);
    }

    private void play(){

        if(this.hit) nextMove(this.probPoint);

        if(!this.hit && !this.gameEnd){
            this.probField = Helper.patternFinding(this.enemyField, this.availableShipsEnemy);
            this.probPoint = Helper.getProbabilityPoint(this.probField);
            kiShot();
        }
    }

    private void checkIfGameOver(){

        int s1 = 0, s2 = 0;

        for(int i = 0; i < this.availableShipsKI.length; i++){
            s1 += this.availableShipsKI[i];
            s2 += this.availableShipsEnemy[i];
        }

        if(s1 == 0 || s2 == 0) this.gameEnd = true;
    }

    private void setUpGame(String[] data) {

        this.size = Integer.parseInt(data[1]);
        this.gameField = new int[this.size][this.size];
        this.enemyField = new int[this.size][this.size];
        this.probField = new int[this.size][this.size];

        clearArray(this.enemyField, 0);

        for(int i = 2; i < data.length; i++){
            int zahl = Integer.parseInt(data[i]);
            this.availableShipsEnemy[i-2] = zahl;
            this.availableShipsKI[i-2] = zahl;
            this.shipSum += zahl;
        }

        placeShips();
        Helper.printGame(this.gameField, this.enemyField);
        System.out.println("Confirmed");

        if(!this.turn){
            this.out.write("confirmed\n");
            this.out.flush();
        }
    }

    public void clearArray(int[][] arr, int digit){
        for(int i = 0; i < arr.length; i++){
            for(int j = 0; j < arr.length; j++){
                if(arr[i][j] == digit) arr[i][j] = 0;
            }
        }
    }

    private void shotEnemy(String[] data){

        String line;

        int index = -1;
        Point p = new Point(Integer.parseInt(data[1]), Integer.parseInt(data[2]));

        for(int i = 0; i < this.shipsKI.size(); i++){
            if(this.shipsKI.get(i).contains(p)){
                this.shipsKI.get(i).removeCord(p);
                index = i;
                break;
            }
        }

        if(index == -1){
            line = "answer 0";
            this.turn = true;
            this.gameField[p.x][p.y] = 1;
        }else{
            if(this.shipsKI.get(index).checkIfDead()){
                line = "answer 2";
                this.availableShipsKI[5 - this.shipsKI.get(index).getInitialSize()]--;
                checkIfGameOver();
            }else{
                line = "answer 1";
            }
            this.gameField[p.x][p.y] = 2;
            this.turn = false;
        }
        this.out.write(line + "\n");
        this.out.flush();
    }

    private void kiShot(){
        this.shots++;
        this.out.write("shot " + this.probPoint.x + " " + this.probPoint.y + "\n");
        this.out.flush();
    }

    private void answers(String[] data){

        switch(data[1]){
            case "0": this.enemyField[this.probPoint.x][this.probPoint.y] = 1;
                System.out.println(this.probPoint);
                this.turn = false;
                System.out.println("Kein Hit");
                if(!this.hit) this.probField = Helper.patternFinding(this.enemyField, this.availableShipsEnemy);
                Helper.printGame(this.probField, this.enemyField);
                this.out.write("pass\n");
                this.out.flush();
                break;
            case "1": this.enemyField[this.probPoint.x][this.probPoint.y] = 2;
                System.out.println(this.probPoint);
                System.out.println("Hit");
                this.turn = true;
                if(!this.hit) this.probField = Helper.patternFinding(this.enemyField, this.availableShipsEnemy);
                Helper.printGame(this.probField, this.enemyField);
                this.cords.add(new Point(this.probPoint));
                if(this.cords.size() == 2) setDirection();
                this.hits++;
                this.hit = true;
                break;
            case "2": this.hit = false;
                this.enemyField[this.probPoint.x][this.probPoint.y] = 2;
                System.out.println(this.probPoint);
                System.out.println("Hit - Versenkt");
                this.turn = true;
                neighbours.clear();
                neighborsSet = false;
                this.cords.add(new Point(this.probPoint));
                this.hits++;
                setBorders(this.cords, this.enemyField);
                this.probField = Helper.patternFinding(this.enemyField, this.availableShipsEnemy);
                Helper.printGame(this.probField, this.enemyField);
                deleteShip();
                this.direction = 0;
                checkIfGameOver();
        }
    }

    private void nextMove(Point p){

        if(this.direction == 0){

            if(!this.neighborsSet){

                this.hitPoint = p;
                this.neighbours.add(new Point(p.x - 1, p.y));
                this.neighbours.add(new Point(p.x + 1, p.y));
                this.neighbours.add(new Point(p.x, p.y - 1));
                this.neighbours.add(new Point(p.x, p.y + 1));

                removeNeighbours();
                this.neighborsSet = true;
            }

            this.focusPoint = this.hitPoint;

            ArrayList <Point> cordI = new ArrayList<>();
            ArrayList <Point> cordJ = new ArrayList<>();

            int[][] arr = new int[this.size][this.size];
            Helper.setArray(arr, 1);

            cordI.add(this.hitPoint);
            cordJ.add(this.hitPoint);

            for(int i = -1; i < 2; i += 2){
                getSizeFromToPoint(cordI, i, 0);
                getSizeFromToPoint(cordJ,0,i);
            }

            for (Point point : cordI) arr[point.x][point.y] = 0;

            for (Point point : cordJ) arr[point.x][point.y] = 0;

            this.probField = Helper.patternFinding(arr, this.availableShipsEnemy);
            this.probPoint = getMaxPoint(this.neighbours);
            kiShot();

        }else{
            move2();
        }
    }

    private void removeNeighbours(){

        //Point out of Bounds and field == 1
        for(int i = 0; i < this.neighbours.size(); i++){
            Point tempPoint = this.neighbours.get(i);
            if(Helper.pointOutOfBounds(tempPoint, this.size) || this.enemyField[tempPoint.x][tempPoint.y] == 1){
                this.neighbours.remove(tempPoint);
                i--;
            }
        }

        ArrayList <Point> cordI = new ArrayList<>();
        ArrayList <Point> cordJ = new ArrayList<>();

        cordI.add(this.hitPoint);
        cordJ.add(this.hitPoint);

        for(int i = -1; i < 2; i += 2){
            getSizeFromToPoint(cordI, i, 0);
            getSizeFromToPoint(cordJ,0, i);
        }

        removeNeighbourHelper(cordI);
        removeNeighbourHelper(cordJ);
    }

    private void removeNeighbourHelper(ArrayList<Point> arr){

        int focus = 0;
        for(int i = 0; i < this.availableShipsEnemy.length; i++){
            if(this.availableShipsEnemy[3 - i] != 0){
                focus = 2 + i;
                break;
            }
        }

        for(int i = 0; i < this.availableShipsEnemy.length; i++) {
            if(this.availableShipsEnemy[i] != 0 && 5 - i == focus){
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

        while(!Helper.pointOutOfBounds(p, this.size) && this.enemyField[p.x][p.y] == 0){
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

        kiShot();
    }

    private Point getMaxPoint(Point one ,Point two){

        if(Helper.pointOutOfBounds(one, this.size) || this.enemyField[one.x][one.y] == 1) return two;
        if(Helper.pointOutOfBounds(two, this.size) || this.enemyField[two.x][two.y] == 1) return one;

        ArrayList<Point> temp = new ArrayList<>(2);
        temp.add(one);
        temp.add(two);
        return this.getMaxPoint(temp);
    }

    private Point getMaxPoint(ArrayList<Point> arr){

        int max = 0;
        Point p = new Point();

        for (Point point : arr) {
            if (this.probField[point.x][point.y] >= max && this.enemyField[point.x][point.y] == 0) {
                max = this.probField[point.x][point.y];
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
            case 5: this.availableShipsEnemy[0]--; break;
            case 4: this.availableShipsEnemy[1]--; break;
            case 3: this.availableShipsEnemy[2]--; break;
            case 2: this.availableShipsEnemy[3]--; break;
        }

        for(Point p : cords) this.enemyField[p.x][p.y] = 3;

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
                if (field[p.x][p.y] != 3) field[p.x][p.y] = 1;
            }
        }
    }

    private void placeShips(){
        int count = 0;
        for(int i = 0; i < this.availableShipsKI.length; i++){
            if (this.availableShipsKI[i] != 0){
                for (int j = 0; j < this.availableShipsKI[i]; j++){
                    ArrayList<Point> cordsShip = new ArrayList<>(5 - i);
                    boolean success = false;
                    Point place = new Point(-1, -1);
                    int d, x = 0;

                    if((int) (Math.random() * 6 + 1) <= 3){
                        d = 1;
                    }else{
                        d = 2;
                    }

                    while(!success){
                        x++;
                        place.setLocation((int) (Math.random() * this.size), (int) (Math.random() * this.size));
                        success = true;
                        while(success && cordsShip.size() < 5 - i){
                            if(this.gameField[place.x][place.y] == 0){
                                cordsShip.add(place);

                                if(d == 2){
                                    place = new Point(place.x + 1, place.y);
                                }else{
                                    place = new Point(place.x, place.y + 1);
                                }

                                if(place.x < 0 || place.x >= this.size || place.y < 0 || place.y >= this.size){
                                    success = false;
                                    cordsShip.clear();
                                }

                                if(cordsShip.size() > 0 && ((d == 2 && place.x == this.size - 1) || (d == 1 && place.y == this.size - 1))){
                                    success = false;
                                    cordsShip.clear();
                                }

                            }else{
                                success = false;
                                cordsShip.clear();
                            }
                        }
                        if(x > 1000){
                            count = -1;
                            break;
                        }
                    }
                    for (Point p : cordsShip) this.gameField[p.x][p.y] = 3;
                    this.shipsKI.add(new Ship(cordsShip, cordsShip.size(), "KI"));
                    setBorders(cordsShip, this.gameField);
                    cordsShip.clear();
                    count++;
                }
            }
        }

        clearArray(this.gameField, 1);

        if(count != this.shipSum){
            Helper.printGame(this.gameField, this.enemyField);
            System.out.println("Retry");
            clearArray(this.gameField, 3);
            this.shipsKI.clear();
            placeShips();
        }
    }
}

