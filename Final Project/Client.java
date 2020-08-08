// http://mrbool.com/file-transfer-between-2-computers-with-java/24516

import java.net.*;
import java.util.Scanner;
import java.io.*;

public class Client {

	public static void main(String[] args) throws IOException {

		Scanner scan = new Scanner(System.in);


		System.out.println("Input your IP address: ");
		// String clientAddress = scan.nextLine();
		String clientAddress = "192.168.254.22";

		System.out.println("Enter your port number");
		// String clientPort = scan.nextLine();
		String clientPort = "9000";


		// ask for the server its tryna connect with
		System.out.println("Enter IP address of the server you want to connect to: ");
		String host = InetAddress.getLocalHost().getHostAddress();
		int filesize = 1022386;
		int bytesRead;
		int currentTot = 0;

		Socket socket = new Socket(host, 6604);
		System.out.println("Connecting...");
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		out.println(clientAddress + clientPort);	// send this ip address to verify from server
		out.flush();
		socket.close();

	    //change the host to dynamic and i also changed the port  
		socket = new Socket(host, 6604);

		System.out.println("Receiving file...");
		byte [] bytearray  = new byte [filesize];
		InputStream is = socket.getInputStream();
		
	    //changed the file 
	    FileOutputStream fos = new FileOutputStream("copy.png");
	    BufferedOutputStream bos = new BufferedOutputStream(fos);
	    bytesRead = is.read(bytearray,0,bytearray.length);
	    currentTot = bytesRead;

	    do {
	       bytesRead = is.read(bytearray, currentTot, (bytearray.length-currentTot));
	       if(bytesRead >= 0) currentTot += bytesRead;
	    } while(bytesRead > -1);

	    bos.write(bytearray, 0 , currentTot);
	    bos.flush();
		bos.close();

		System.out.println("File Received!");
	
		socket.close();
		
	  }

	  
}