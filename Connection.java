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
/**
 * Connectionklasse für die Kommunikation zwischen Server und Client
 *
 */
public class Connection {
	/**
	 * Objekt-Attribute
	 */
	private Socket s;
	private ServerSocket ss;
	private String ip;
	private Writer out;
	private BufferedReader in;
	
	/**
	 * IP Adresse ermitteln, die dann später als Client ans Socket übergeben wird
	 * @return IP-Adresse
	 */
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
	/**
	 * Schließen der Connection, wenn das Spiel aufhört
	 * @return true bei Erfolg
	 */
	public boolean closeConnection() {
		try {
			
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
	/**
	 * Connection erstellen, wenn man als Client joint
	 *
	 * @param ip
	 * @return true bei Erfolg
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public boolean connectAsClient(String ip)throws UnknownHostException,IOException{
		
		this.s = new Socket (ip,50000);
		this.in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		this.out = new OutputStreamWriter(s.getOutputStream());
		return true;
		
	}
	/**
	 * Connection erstellen als Server
	 * @return true bei Erfolg
	 */
	public boolean createConnection() {
		try {
			this.ss=new ServerSocket(50000);
			this.s = this.ss.accept();
			this.in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			this.out = new OutputStreamWriter(s.getOutputStream());
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Schreiben an den Server 
	 * @param Nachricht
	 * @return true bei Erfolg
	 */
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
	/**
	 * Lesen vom Server
	 * @return Nachricht
	 */
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
