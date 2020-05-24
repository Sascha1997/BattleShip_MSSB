package schiffeversenken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class StarterKlasse {
	
	private static BufferedReader in;	
	private static Writer out;
	private static String role;
	private static SpielHelfer helfer;
    
	public static void main(String[] args) throws IOException {
		
		
		SchiffeVersenken spiel = new SchiffeVersenken(); //Spielobjekt erzeugen
		String spielfeld; //String für die Spielfeldgröße und die Schiffsmengen
		String ipAdress = "";
		
		//Anfangs Gui-Fenster starten mit Modusauswahl etc
		
		
		final int port = 50001;
		
		//Hier über Gui, Moduswahl Ob Offline, Online etc .. 
		/*if(moduswahl==1) {
			//Onlinespiel
		}else {
			//KI Spielt
		}*/
		
		
		//HIer dann bereits GUi aufs nächste Fenster und Ladebalken oder Ladekreis mit "Warten auf Verbindung mit Gegner" 
		Socket s;
		
				
		if(args.length==0) {
			role = "Server";
			System.out.println("Start als Server");
			System.out.println("Waiting for client connection ...");
		    ServerSocket ss = new ServerSocket(port); //Serversocket erzeugen und an den Port binden
		    s = ss.accept();	//Warten auf Client
		    	
		}else {
			Enumeration<NetworkInterface> nis =
					    NetworkInterface.getNetworkInterfaces();
		    while (nis.hasMoreElements()) {
			NetworkInterface ni = nis.nextElement();
			Enumeration<InetAddress> ias = ni.getInetAddresses();
			while (ias.hasMoreElements()) {
			    	InetAddress ia = ias.nextElement();
			    	if (!ia.isLoopbackAddress()) {
			    		ipAdress=ipAdress + " "+ ia.getHostAddress();
			    		
			    		break;
			    	}
				}
		    }
		    
		    String parts[] = ipAdress.split(" ");
			role ="Client";
			System.out.println("Start als Client");
			s=new Socket(parts[1],port);
		}
		
		System.out.println("Connection established.");
		//In der Gui ausgeben, dass Verbindun besteht
		
		//Ein und Ausgabestrom des Sockets ermitteln und als BufferedReader bzw. Writerverpacken
		in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		out = new OutputStreamWriter(s.getOutputStream());
		helfer = new SpielHelfer();
		
		//Dem Client die Spielfeldgröße mitteilen bzw. diese vom Server bekommen
		if(role.equals("Server")) {
			 
			spielfeld = "10"; //Hier kommt unsere Benutzereingabe von der GUI wie groß das spielfeld sein soll, im Beispiel hier 10
			spielfeld = "setup "+spielfeld+" "+helfer.kalkuliereSchiffe(Integer.parseInt(spielfeld)); //Die Spielfeldgroeße an das Objekt der Helferklasse übergeben und die Schiffe ermitteln
			
			out.write(spielfeld+" \n");	
			out.flush();
			System.out.println("Dem Client die Spielgröße mitgeteilt");
		}else {
			
			spielfeld = in.readLine();	//Belegung des Spielfeldes vom Gegner lesen
			System.out.println("Vom Server die Spielgröße mitgeteilt bekommen");
			System.out.println("In.Readline(): "+spielfeld);
			
		}
			
		
		spiel.spieleinrichten(spielfeld);
		//Spielfeld einrichten und unser Spielfeld anzeigen auf dem Fenster für den Gegner ein leeres Fenster
		
		
		//Mitteilen, dass das Programm bereit ist zum Spielen
		out.write("confirmed\n");
		out.flush();
		
		//Hier wieder Ladekreis mit "Warten bis der Gegner Spielbereit ist" 
		if(in.readLine().equals("confirmed")) {
			//Beide Parteien sind bereit zum Spielen --> Spielbeginn
			System.out.println("BeideParteien sind bereit zum Spielen");
			//Hier eventuell noch eine kleine Animation mit "Das Spiel beginnt"
			spiel.beginneSpiel(StarterKlasse.in, StarterKlasse.out,StarterKlasse.role);
		}
		

	}

}
