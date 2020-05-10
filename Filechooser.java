package schiffeversenken;

import javax.swing.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Filechooser extends JFileChooser {

    private static JFileChooser chooser = new JFileChooser();
    private static int returnVal ;

    public static void save(String myfield, String enemyfield,int size){

        //Ort wo die Spielstände gespeichert werden
        chooser.setCurrentDirectory(new File("C:/Users/seang/IdeaProjects/Speicherstaende"));

        //Fenster zur Speicherung wird geöffnet
        returnVal = chooser.showSaveDialog(null);

        //Wenn mit ok oder yes bestätigt wird, wird der Code ausgeführt
        //Zur Verständlichkeit Programm ausführen
        if (returnVal  == JFileChooser.APPROVE_OPTION) {

            try{

                //Name der Datei wird gewählt und erstellt -> normale .txt Datei
                FileWriter fw = new FileWriter(chooser.getSelectedFile() + ".txt");

                //Informationen werden in die Datei gespeichert, z.b Datum/Größe des Spielfeldes/Inhalte der Spielfelder
                fw.write("start=" + getDateTime() + "\n" + "size=" + size + "\n" + "m=" + myfield + "\n" + "e=" + enemyfield + "\n");
                fw.close();
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void open(Matchfield field, Matchfield enemyfield){

        //Ort wo die Spielstände geöffnet werden
        chooser.setCurrentDirectory(new File("C:/Users/seang/IdeaProjects/Speicherstaende"));

        //Fenster zur Öffnung eines Spielstandes wird geöffnet
        returnVal = chooser.showOpenDialog(null);

        //Wenn mit ok oder yes bestätigt wird, wird der Code ausgeführt
        //Zur Verständlichkeit Programm ausführen
        if (returnVal  == JFileChooser.APPROVE_OPTION) {

            try{
                FileReader fw = new FileReader(chooser.getSelectedFile());

                String s = "";
                int data = fw.read();   //reader liest Zeichen für Zeichen ein
                                        //chars werden als int zurückgegeben -> ASCII Tabelle, z.b 65 = A
                while(data != -1) {     //Wird solange ausgeführt bis kein Zeichen mehr in der Datei vorhanden ist

                    s += (char) data;   //int-Wert wird als char gecastet und in den String gespeichert
                    data = fw.read();
                }

                String[] temp = setupField(s);      //String wird separiert in einzelne Zeilen und in einem Array gespeichert
                                                    //siehe Speicherdatei -> 1.Zeile = Datum/Uhrzeit 2. = Größe des Spielfeldes, usw..
                field.setField(temp[2]);            //Matchfields werden synchronisiert
                enemyfield.setField(temp[3]);
                fw.close();
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static String getDateTime(){                 //gibt das Datum und die Uhrzeit an, wann die Datei gespeichert wurde
        String s = "";                              //Wird geändert zu, wann das Spiel angefangen hat

        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        s = dateFormat.format(date);

        return s;
    }

    public static String[] setupField(String s){        //String wird separiert in einzelne Zeilen und in einem Array gespeichert

        int last = 0; //10
        String[] data = new String[4];
        int z = 0;

        for(int i = 0; i < s.length(); i++){
            if((int) s.charAt(i) == 10){
                data[z++] = s.substring(last, i);
                last = i + 1;
            }
        }

        System.out.println(data[2]);
        System.out.println(data[3]);

        data[2] = data[2].substring(2);
        data[3] = data[3].substring(2);
        return data;
    }
}
