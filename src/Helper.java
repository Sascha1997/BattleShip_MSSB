import java.awt.*;
import java.util.ArrayList;

public class Helper {

    public static int[][] patternFinding(int[][] enemy, int[] enemyShips){

        int[][] probs = new int[enemy.length][enemy.length];

        for(int o = enemyShips.length + 1; o >= 2; o--) {
            if(enemyShips[enemyShips.length + 1 - o] != 0) {
                ArrayList<Point> points = new ArrayList<>(o);
                for (int d = 0; d < 2; d++){
                    outer:
                    for (int i = 0; i < enemy.length; i++) {
                        for (int j = 0; j < enemy.length; j++) {
                            if ((enemy[i][j] != 0 && d == 0) || (enemy[j][i] != 0 && d == 1)) {
                                points.clear();
                                if(j + o > enemy.length) continue outer;
                                continue;
                            }

                            if(d == 0) points.add(new Point(i,j)); else points.add(new Point(j,i));

                            if (points.size() == o) {
                                for (Point p : points) probs[p.x][p.y]++;
                                points.remove(0);
                            }
                        }
                        points.clear();
                    }
                }
            }
        }

        return probs;
    }

    public static Point getProbabilityPoint(int[][] probs){

        ArrayList<Point> points = new ArrayList<>();

        int max = 0;
        for(int i = 0; i < probs.length; i++){
            for(int j = 0; j <probs.length; j++){
                if(probs[i][j] > max){
                    points.clear();
                    points.add(new Point(i,j));
                    max = probs[i][j];
                }else if(probs[i][j] == max){
                    points.add(new Point(i,j));
                }
            }
        }

        return new Point(points.get((int) (Math.random() * points.size())));
    }

    public static void printGame(int[][] probs, int[][] enemy){

        for(int i = 0; i < probs.length; i++){
            for(int j = 0; j < probs.length; j++){

                System.out.print(String.format("%3d",probs[i][j]));
            }
            System.out.print("   ");
            for(int j = 0; j < enemy.length; j++){

                System.out.print(String.format("%3d",enemy[i][j]));
            }
            System.out.println();
        }
        System.out.println();
    }

    public static  ArrayList<Point> getStartAndEndPoints(ArrayList<Point> arr){

        ArrayList<Point> startAndEnd = new ArrayList<>(2);

        for (int i = 0; i < arr.size() - 1; i++){
            for (int j = 0; j < arr.size() - i - 1; j++) {
                if (arr.get(j).x > arr.get(j + 1).x || arr.get(j).y > arr.get(j + 1).y){
                    Point temp = arr.get(j);
                    arr.set(j, arr.get(j + 1));
                    arr.set(j + 1, temp);
                }
            }
        }

        startAndEnd.add(arr.get(0));
        startAndEnd.add(arr.get(arr.size() - 1));

        return startAndEnd;
    }

    public static boolean pointOutOfBounds(Point p, int size){

        return p.x < 0 || p.x >= size || p.y < 0 || p.y >= size;
    }

    public static void setArray(int[][] arr, int x){

        for(int i = 0; i < arr.length; i++){
            for(int j = 0; j < arr.length; j++){
                arr[i][j] = x;
            }
        }
    }

    public static String pointArrayToString(ArrayList<Point> arr){

        StringBuilder s = new StringBuilder();
        for(Point p : arr){
            s.append(p.x).append(":").append(p.y).append(" ");
        }
        return s.toString();
    }

    public static String pointToString(Point p){
        ArrayList<Point> temp = new ArrayList<>();
        temp.add(p);
        return pointArrayToString(temp);
    }

}
