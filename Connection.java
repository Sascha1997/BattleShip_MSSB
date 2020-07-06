package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class Connection {
	private Socket s;
	private ServerSocket ss;
	private String ip;
	private Writer out;
	private BufferedReader in;
	
	//Unserer IP Adresse ermitteln, die dann später als Client ans Socket übergeben wird
	public String getIp() {

		String ipAdress = "";
 		
    	Enumeration<NetworkInterface> nis;
		try {
			nis = NetworkInterface.getNetworkInterfaces();
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
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	 	
    	 String parts[] = ipAdress.split(" ");
    	 ip = parts[1];
    	 return ip;
	}

	public boolean closeConnection() {
		try {
			//Unter umständen ist bei zurück Buttons noch keine Connection entstanden, diese muss aber geschlossen werden falls. Null abfrage wird den entsprechenden Fall erkennen
			
			
			if(s!=null) {
				this.s.close();
			}
			if(StarterKlasse.server&&this.ss!=null) {
				this.ss.close();
			}
		
			
		} catch (IOException e) {
			
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean connectAsClient()throws UnknownHostException,IOException {
		
		this.s = new Socket (this.getIp(),50000);
		this.in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		this.out = new OutputStreamWriter(s.getOutputStream());
		
		return true;
		
	}
	public boolean createConnection() {
		try {
			this.ss=new ServerSocket(50000);
			this.s = this.ss.accept();
			this.in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			this.out = new OutputStreamWriter(s.getOutputStream());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean write(String s) {
		try {
			this.out.write(s+"\n");
			this.out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public String read() {
		String s;
		try {
			s= this.in.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return s;
	}
	
}
