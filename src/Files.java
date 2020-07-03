
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class Files {

    private boolean turn;
    private boolean[] bool = new boolean[3];
    private int destroyedOwn, destroyedEnemy, pullCount;
    private int[] ints = new int[5];
    private int[] ownShips = new int[4];
    private int[] enemyShips = new int[4];
    private int[][] ownField, enemyField, prob;
    private ArrayList<Point> cords = new ArrayList<>();
    private ArrayList<Point> neighbours = new ArrayList<>();
    private ArrayList<Point> gameFieldArrayList = new ArrayList<>();
    private ArrayList<Point> points = new ArrayList<>();
    private ArrayList<Ship> ships = new ArrayList<>();
    private BufferedReader br;
    private FileWriter fr;

    public static void makeDirectory() {
        //Erzeugt Dateisystem innerhalb des Projektordners, sollte ausgeführt werden wenn das Programm gestartet wird
        new File(System.getProperty("user.dir") + "\\saves").mkdir();
        new File(System.getProperty("user.dir") + "\\saves\\player").mkdir();
        new File(System.getProperty("user.dir") + "\\saves\\ki").mkdir();
    }

    //save-Methode für die KI
    //alle store und restore Methoden werden jeweils weiter unten erklärt
    public void save(String id, boolean turn, int[][] ownField, int[][] enemyField, int[][] prob, int[] ownShips,
                     int[] enemyShips, ArrayList<Point> cords, ArrayList<Point> neighbours,
                     ArrayList<Point> gameFieldArrayList, ArrayList<Point> points, ArrayList<Ship> ships, boolean[] bool, int[] ints){
        try {
            //erzeugt eine neue .txt Datei mit der übergebenen ID und speichert die in saves\ki
            this.fr = new FileWriter(System.getProperty("user.dir") + "\\saves\\ki\\" + id + ".txt");
            //speichert wer am Zug ist, von der Sicht der KI, true -> KI  ist drann, false -> Gegner ist dran
            this.fr.write(turn + "\n");
            storeField(ownField);
            storeField(enemyField);
            storeProb(prob);
            storeInts(ownShips);
            storeInts(enemyShips);
            storeArrayListPoints(cords);
            storeArrayListPoints(neighbours);
            storeArrayListPoints(gameFieldArrayList);
            storeArrayListPoints(points);
            storeArrayListShips(ships);
            storeBooleans(bool);
            storeInts(ints);
            //leert den Stream, FileWriter wird geschlossen
            this.fr.flush();
            this.fr.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //save-Methode für den Spieler
    public void save(long id, boolean turn, int[][] ownField, int[][] enemyField, ArrayList<Ship> ships, int destroyedOwn, int destroyedEnemy, int pullCount){
        try {
            //erzeugt eine neue .txt Datei mit der übergebenen ID und speichert die in saves\player
            this.fr = new FileWriter(System.getProperty("user.dir") + "\\saves\\player\\" + id + ".txt");
            //speichert wer am Zug ist, von der Sicht des Spielers, true -> Spieler ist drann, false -> Gegner ist dran
            this.fr.write(turn + "\n");
            storeField(ownField);
            storeField(enemyField);
            storeArrayListShips(ships);
            this.fr.write(destroyedOwn + "\n");
            this.fr.write(destroyedEnemy + "\n");
            this.fr.write(pullCount + "\n");
            //leert den Stream, FileWriter wird geschlossen
            this.fr.flush();
            this.fr.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //KI
    public void load(String id){
        try {
            this.br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "\\saves\\ki\\" + id + ".txt"));
            this.turn = Boolean.parseBoolean(this.br.readLine());
            restoreFields();
            restoreProb();
            restoreInts(this.ownShips);
            restoreInts(this.enemyShips);
            restoreArrayListPoints();
            restoreArrayListShips();
            restoreBooleans();
            restoreInts(this.ints);
            this.br.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //Player
    public void load(long id){
        try {
            this.br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "\\saves\\player\\" + id + ".txt"));
            this.turn = Boolean.parseBoolean(this.br.readLine());
            restoreFields();
            restoreArrayListShips();
            this.destroyedOwn = Integer.parseInt(this.br.readLine());
            this.destroyedEnemy = Integer.parseInt(this.br.readLine());
            this.pullCount = Integer.parseInt(this.br.readLine());
            this.br.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //Server
    public boolean ourTurn(String id){
        try {
            File f = new File(System.getProperty("user.dir") + "\\saves\\player\\" + id + ".txt");
            BufferedReader brr;
            if (f.exists()) {
                brr = new BufferedReader(new FileReader(System.getProperty("user.dir") + "\\saves\\player\\" + id + ".txt"));
            }else{
                brr = new BufferedReader(new FileReader(System.getProperty("user.dir") + "\\saves\\ki\\" + id + ".txt"));
            }
            return Boolean.parseBoolean(brr.readLine());
        }catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }

    //STORE--------------------------------------------------------------------------------------------------------STORE

    private void storeField(int[][] field) throws IOException {
        for (int[] value : field) {
            for (int j = 0; j < field.length; j++) {
                this.fr.write(value[j]+"");
            }
        }
        this.fr.write("\n");
    }

    private void storeProb(int[][] prob) throws IOException {
        for (int[] value : prob) {
            for (int j = 0; j < prob.length; j++) {
                this.fr.write(value[j] + ":");
            }
        }
        this.fr.write("\n");
    }

    private void storeInts(int[] ints) throws IOException {
        for (int anInt : ints) {
            this.fr.write(anInt + " ");
        }
        this.fr.write("\n");
    }

    private void storeArrayListPoints(ArrayList<Point> arr) throws IOException {
        for (Point point : arr) {
            this.fr.write(this.pointToString(point) + " ");
        }
        this.fr.write("\n");
    }

    private void storeArrayListShips(ArrayList<Ship> ships) throws IOException {
        for (Ship ship : ships) {
            if(ship.checkIfDead()) continue;
            for (int j = 0; j < ship.availableCords.size(); j++) {
                this.fr.write(pointToString(ship.availableCords.get(j)) + " ");
            }
            this.fr.write(ship.getInitialSize() + "$" + ship.getIdentification() + "|");
        }
        this.fr.write("\n");
    }

    private void storeBooleans(boolean[] bool) throws IOException {
        for (boolean b : bool) {
            this.fr.write(b + " ");
        }
        this.fr.write("\n");
    }

    private String pointToString(Point p){
        return p.x + ":" + p.y;
    }

    //RESTORE----------------------------------------------------------------------------------------------------RESTORE

    private void restoreFields() throws IOException {
        String line1 = this.br.readLine();
        String line2 = this.br.readLine();
        int size = (int) Math.sqrt(line1.length());

        this.ownField = new int[size][size];
        this.enemyField = new int[size][size];

        for(int i = 0; i < line1.length() ; i++){
            this.ownField[i / size][i % size] = Integer.parseInt(line1.charAt(i)+"");
            this.enemyField[i / size][i % size] = Integer.parseInt(line2.charAt(i)+"");
        }
    }

    public void restoreProb() throws IOException {
        String line = this.br.readLine();
        String[] arr = line.split(":");

        this.prob = new int[arr.length][arr.length];

        for(int i = 0; i < arr.length; i++){
            this.prob[i / arr.length][i % arr.length] = Integer.parseInt(arr[i]);
        }
    }

    private void restoreInts(int[] destination) throws IOException {
        String line = this.br.readLine();
        String[] arr = line.split(" ");

        for(int i = 0; i < arr.length; i++){
            destination[i] = Integer.parseInt(arr[i]);
        }
    }

    private void restoreArrayListPoints() throws IOException {

        for(int i = 0; i < 4; i++){
            String line = this.br.readLine();
            if(line.equals("")) continue;
            String[] cords = line.split(" ");
            for (String cord : cords) {
                String[] xy = cord.split(":");
                Point p = new Point(Integer.parseInt(xy[0]), Integer.parseInt(xy[1]));
                switch (i) {
                    case 0: this.cords.add(p); break;
                    case 1: this.neighbours.add(p); break;
                    case 2: this.points.add(p); break;
                    case 3: this.gameFieldArrayList.add(p);
                }
            }
        }
    }

    private void restoreArrayListShips() throws IOException {
        String line = this.br.readLine();
        String[] ships = line.split("[|]");
        ArrayList<Point> temp = new ArrayList<>();

        for (String ship : ships) {
            String[] parts = ship.split(" ");
            for (int j = 0; j < parts.length - 1; j++) {
                String[] xy = parts[j].split(":");
                temp.add(new Point(Integer.parseInt(xy[0]), Integer.parseInt(xy[1])));
            }
            String[] data = parts[parts.length - 1].split("[$]");
            this.ships.add(new Ship(temp, Integer.parseInt(data[0]), data[1]));
            temp.clear();
        }
    }

    public void restoreBooleans() throws IOException {
        String line = this.br.readLine();
        String[] parts = line.split(" ");

        for(int i = 0; i < parts.length; i++){
            this.bool[i] = Boolean.parseBoolean(parts[i]);
        }
    }

    //GETTER------------------------------------------------------------------------------------------------------GETTER

    public boolean getTurn(){
        return this.turn;
    }

    public boolean[] getBool(){
        return this.bool;
    }

    public int getDestroyedOwn(){
        return this.destroyedOwn;
    }

    public int getDestroyedEnemy(){
        return this.destroyedEnemy;
    }

    public int getPullCount(){
        return this.pullCount;
    }

    public int[] getInts(){
        return this.ints;
    }

    public int[] getOwnShips(){
        return this.ownShips;
    }

    public int[] getEnemyShips(){
        return this.enemyShips;
    }

    public int[][] getOwnField(){
        return this.ownField;
    }

    public int[][] getEnemyField(){
        return this.enemyField;
    }

    public int[][] getProb(){
        return this.prob;
    }

    public ArrayList<Point> getPoints(){
        return this.points;
    }

    public ArrayList<Point> getCords(){
        return this.cords;
    }

    public ArrayList<Point> getNeighbours(){
        return this.neighbours;
    }

    public ArrayList<Point> getGameFieldArrayList(){
        return this.gameFieldArrayList;
    }

    public ArrayList<Ship> getShips() {
        return this.ships;
    }

}