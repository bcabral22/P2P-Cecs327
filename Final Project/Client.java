// http://mrbool.com/file-transfer-between-2-computers-with-java/24516

import java.net.*;
import java.io.*;
public class Client {

	public static void main (String [] args ) throws IOException {
		
		//made this so that it gets the host ip
		String host = InetAddress.getLocalHost().getHostAddress();
	    int filesize=1022386; 
	    int bytesRead;
	    int currentTot = 0;
	    //change the host to dynamic and i also changed the port  
	    Socket socket = new Socket(host, 6604);
	    byte [] bytearray  = new byte [filesize];
	    InputStream is = socket.getInputStream();
	    //changed the file 
	    FileOutputStream fos = new FileOutputStream("copy.png");
	    BufferedOutputStream bos = new BufferedOutputStream(fos);
	    bytesRead = is.read(bytearray,0,bytearray.length);
	    currentTot = bytesRead;

	    do {
	       bytesRead =
	          is.read(bytearray, currentTot, (bytearray.length-currentTot));
	       if(bytesRead >= 0) currentTot += bytesRead;
	    } while(bytesRead > -1);

	    bos.write(bytearray, 0 , currentTot);
	    bos.flush();
	    bos.close();
	    socket.close();
	  }
}