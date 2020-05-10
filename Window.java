package schiffeversenken;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Window implements ActionListener {

    private int size = 10;
    private JButton button = new JButton("Refresh");
    private Matchfield field, enemyfield;

    public Window(){

        //Fenster wird erstellt
        JFrame window = new JFrame("Window");
        window.setSize(1000,500);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setLayout(null);
        window.setResizable(false);
        {

            //Container vom Fenster, Komponente wie Button oder Panels werden da platziert
            //Ein Panel ist auch ein Container
            Container c = window.getContentPane();

            //Layout wird auf null gesetzt, weil alle Komponente absolut platziert werden sollen
            c.setLayout(null);
            {
                //Button nur zum testen, erzeugt neue Matchfields
                button.setBounds(441, 180, 99, 40);
                button.addActionListener(this);
                c.add(button);

                //Matchfields werden erstellt und auf dem Container (vom Fenster) platziert
                field = new Matchfield(this.size);
                JPanel p1 = field.getPanel();
                p1.setLocation(40, 20);
                c.add(p1);

                enemyfield = new Matchfield(this.size);
                JPanel p2 = enemyfield.getPanel();
                p2.setLocation(540, 20);
                c.add(p2);
            }

            //Menüleiste für Speicherug und Öffnung von Speicherständen
            JMenuBar bar = new JMenuBar();
            {
                JMenu menu = new JMenu("File");

                JMenuItem open = new JMenuItem("Open");
                open.addActionListener(e -> Filechooser.open(field, enemyfield));
                menu.add(open);

                JMenuItem save = new JMenuItem("Save");
                save.addActionListener(e -> Filechooser.save(field.toString(), enemyfield.toString(), this.size));
                menu.add(save);

                bar.add(menu);
                window.setJMenuBar(bar);
            }
        }
        window.setVisible(true);
    }

    public static void main(String[] args){

        SwingUtilities.invokeLater(
                Window::new
        );
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        field.setTileStateRandom();
        enemyfield.setTileStateRandom();
    }
}
