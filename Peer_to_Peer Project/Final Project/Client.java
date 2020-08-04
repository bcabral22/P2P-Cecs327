
// https://www.tutorialspoint.com/java/java_networking.htm#:~:text=A%20client%20program%20creates%20a,and%20reading%20from%20the%20socket.

import java.net.*;
import java.io.*;

public class Client extends Thread {

   private Chord chord;
   private Socket socketClient;
   private String existingAddress;
   private int existingPort;

   public Client(Chord clientChord, String existingAddress, int existingPort) {
      this.chord = clientChord;
      this.existingAddress = existingAddress;
      this.existingPort = existingPort;
      try {
         socketClient = new Socket(this.existingAddress, this.existingPort);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }


   public void run(){
      try {
         System.out.println("I am a client :) ");
         System.out.println("Connecting to server " + existingAddress + " on port " + existingPort);
         // Socket client = new Socket(serverName, port);
         socketClient.setSoTimeout(70000);
         System.out.println("Connection successful to " + socketClient.getRemoteSocketAddress());
         
         OutputStream outToServer = socketClient.getOutputStream();
         DataOutputStream out = new DataOutputStream(outToServer);
         
         out.writeUTF("Hello from " + socketClient.getLocalSocketAddress());
         InputStream inFromServer = socketClient.getInputStream();
         DataInputStream in = new DataInputStream(inFromServer);
         
         System.out.println("Server says " + in.readUTF());
         socketClient.close();

      } catch (IOException e) {
         e.printStackTrace();
      }
      
   }


   public void startClient(){
      try {
         System.out.println("Client started");
         Thread thread = this;
         thread.start();
     } catch (Exception e) {
         e.printStackTrace();
     }

   }

}





   // original



   // public static void main(String [] args) {
   //    // String serverName = args[0];
   //    // int port = Integer.parseInt(args[1]);

   //    String serverName = "localhost";
   //    int port = 6601;

   //    try {
   //       System.out.println("Connecting to server " + serverName + " on port " + port);
   //       Socket client = new Socket(serverName, port);
   //       client.setSoTimeout(70000);
   //       System.out.println("Connection successful to " + client.getRemoteSocketAddress());
         
   //       OutputStream outToServer = client.getOutputStream();
   //       DataOutputStream out = new DataOutputStream(outToServer);
         
   //       out.writeUTF("Hello from " + client.getLocalSocketAddress());
   //       InputStream inFromServer = client.getInputStream();
   //       DataInputStream in = new DataInputStream(inFromServer);
         
   //       System.out.println("Server says " + in.readUTF());
   //       client.close();

   //    } catch (IOException e) {
   //       e.printStackTrace();
   //    }

   // }


