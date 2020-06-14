import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Files {

    private static FileWriter ft;
    private static BufferedReader br;

    public Files(){

       new File(System.getProperty("user.dir") + "\\saves").mkdir();
       new File(System.getProperty("user.dir") + "\\saves\\player").mkdir();
       new File(System.getProperty("user.dir") + "\\saves\\ki").mkdir();

    }

    public static void save(String id, int[][][] fields, int[][] availableShips, ArrayList<ArrayList<Point>> pointArrays, Point[] points, ArrayList<ShipX> ships, boolean[] bool, int[] integer){

        try {
            ft = new FileWriter(System.getProperty("user.dir") + "\\saves\\ki\\" + id + ".txt");

            ft.write("Date/Time " + getDateTime() + "\n");
            for(int i = 0; i < Math.pow(integer[0], 2); i++) ft.write("_");

            ft.write("\n");

            storeFields(fields);
            storeAvailableShips(availableShips);
            storePointArrays(pointArrays);
            storePoints(points);
            storeShips(ships);
            storeBooleans(bool);
            storeIntegers(integer);

            ft.close();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public static Object[] load(String id){

        try {
            br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "\\saves\\ki\\" + id + ".txt"));

            br.readLine();
            br.readLine();

            int[][][] fields = restoreFields();
            int[][] availableShips = restoreAvailableShips();
            ArrayList<ArrayList<Point>> pointArrays = restorePointArrays();
            Point[] points = restorePoints();
            ArrayList<ShipX> ships = restoreShips();
            boolean[] bool = restoreBooleans();
            int[] integer = restoreIntegers();

            br.close();

            return new Object[]{fields, availableShips, pointArrays, points, ships, bool, integer};
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    private static void storeFields(int[][][] arr) throws IOException {

        int z = 1;

        for (int[][] i : arr) {
            for (int[] j : i) {
                for (int k = 0; k < i.length; k++) {
                    if(z != 2) ft.write(j[k] + ""); else ft.write(j[k] + ":");
                }
            }
            z++;
            ft.write("\n");
        }
    }

    private static void storeAvailableShips(int[][] arr) throws IOException {

        for (int[] i : arr) {
            for (int j : i) {
                ft.write(j + "");
            }
            ft.write(" ");
        }
        ft.write("\n");
    }

    private static void storePointArrays(ArrayList<ArrayList<Point>> arr) throws IOException {

        for (ArrayList<Point> points : arr) {
            ft.write(Helper.pointArrayToString(points) + "\n");
        }
    }

    private static void storePoints(Point[] arr) throws IOException {

        for (Point point : arr) {
            ft.write(Helper.pointToString(point));
        }
        ft.write("\n");
    }

    private static void storeShips(ArrayList<ShipX> arr) throws IOException {

        for (ShipX ship : arr) {
            if(!ship.checkIfDead()) ft.write(ship.arrayToString() + ship.getInitialSize() + "|");
        }
        ft.write("\n");
    }

    private static void storeBooleans(boolean[] arr) throws IOException {
        for (boolean b : arr) {
            int z;
            if(b) z = 1; else z = 0;
            ft.write(z + "");
        }
        ft.write("\n");
    }

    private static void storeIntegers(int[]arr) throws IOException {
        for (int i : arr) {
            ft.write(i + " ");
        }
        ft.write("\n");
    }

    private static int[][][] restoreFields() throws IOException{

        String[] lines = new String[3];
        for(int i = 0; i < lines.length; i++) lines[i] = br.readLine();

        int size = (int) Math.sqrt(lines[0].length());
        int[][][] fields = new int[3][size][size];
        String[] parts = lines[1].split(":");

        for(int i = 0; i < 3; i += 2){
            for(int j = 0; j < lines[i].length(); j++){
                fields[i][j / size][j % size] = Integer.parseInt(String.valueOf(lines[i].charAt(j)));
            }
            for(int k = 0; k < lines[0].length() && i == 0; k++){
                fields[i+1][k / size][k % size] = Integer.parseInt(parts[k]);
            }
        }

        return fields;
    }

    private static int[][] restoreAvailableShips() throws IOException{

        String line = br.readLine();
        String[] parts = line.split(" ");

        int[][] val = new int[2][4];

        for(int i = 0; i < parts.length; i++){
            for(int j = 0; j < parts[i].length(); j++){
                val[i][j] = Integer.parseInt(String.valueOf(parts[i].charAt(j)));
            }
        }
        return val;
    }

    private static ArrayList<ArrayList<Point>> restorePointArrays() throws IOException{

        ArrayList<ArrayList<Point>> arr = new ArrayList<>(2);
        ArrayList<Point> cords = new ArrayList<>();
        ArrayList<Point> neigbhours = new ArrayList<>();

        String[] part1 = br.readLine().split(" ");
        String[] part2 = br.readLine().split(" ");

        for (String s : part1) {
            Point p = getPoint(s);
            if (p == null) continue;
            cords.add(new Point(p));
        }

        for (String s : part2) {
            Point p = getPoint(s);
            if (p == null) continue;
            neigbhours.add(new Point(p));
        }

        arr.add(cords);
        arr.add(neigbhours);
        return arr;
    }

    private static Point[] restorePoints() throws IOException{

        int i = 0;
        Point[] points = new Point[3];

        String[] part1 = br.readLine().split(" ");

        for (String s : part1) {
            Point p = getPoint(s);
            if (p == null) continue;
            points[i++] = p;
        }

        return points;
    }

    private static ArrayList<ShipX> restoreShips() throws IOException{
        ArrayList<ShipX> ships = new ArrayList<>();
        ArrayList<Point> cords = new ArrayList<>();
        String[] parts = br.readLine().split("[|]");

        for(int i = 0; i < parts.length && !parts[0].equals(""); i++){
            String[] temp = parts[i].split(" ");
            for(int j = 0; j < temp.length - 1; j++){
                cords.add(getPoint(temp[j]));
            }
            ships.add(new ShipX(cords, Integer.parseInt(temp[temp.length - 1])));
            cords.clear();
        }

        return ships;
    }

    private static boolean[] restoreBooleans() throws IOException{

        String line = br.readLine();
        boolean[] booleans = new boolean[4];

        for(int i = 0; i < line.length(); i++){
            booleans[i] = line.charAt(i) == '1';
        }
        return booleans;
    }

    private static int[] restoreIntegers() throws IOException{

        String[] parts = br.readLine().split(" ");
        int[] integers = new int[5];

        for(int i = 0; i < parts.length; i++){
            integers[i] = Integer.parseInt(parts[i]);
        }

        return integers;
    }

    public static String getDateTime(){
        String s = "";
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        s = dateFormat.format(date);
        return s;
    }

    private static Point getPoint(String s){

        if(s == null || s.equals("")) return null;

        String[] parts = s.split(":");

        for(int i = 0; i < parts.length; i++){
            parts[i] = parts[i].replaceAll("\\s","");
        }

        return new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }


}
