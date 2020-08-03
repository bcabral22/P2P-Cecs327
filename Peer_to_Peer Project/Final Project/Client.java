
// https://www.tutorialspoint.com/java/java_networking.htm#:~:text=A%20client%20program%20creates%20a,and%20reading%20from%20the%20socket.

import java.net.*;
import java.io.*;

public class Client {

   public static void main(String [] args) {
      // String serverName = args[0];
      // int port = Integer.parseInt(args[1]);

      String serverName = "localhost";
      int port = 6601;

      try {
         System.out.println("Connecting to server " + serverName + " on port " + port);
         Socket client = new Socket(serverName, port);
         client.setSoTimeout(70000);
         System.out.println("Connection successful to " + client.getRemoteSocketAddress());
         
         OutputStream outToServer = client.getOutputStream();
         DataOutputStream out = new DataOutputStream(outToServer);
         
         out.writeUTF("Hello from " + client.getLocalSocketAddress());
         InputStream inFromServer = client.getInputStream();
         DataInputStream in = new DataInputStream(inFromServer);
         
         System.out.println("Server says " + in.readUTF());
         client.close();

      } catch (IOException e) {
         e.printStackTrace();
      }

   }
}

