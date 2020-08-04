
// Copied this client-server model frm link below
// https://www.tutorialspoint.com/java/java_networking.htm#:~:text=A%20client%20program%20creates%20a,and%20reading%20from%20the%20socket.
// *** initial compile: java Client localhost 6066 (changed)

import java.net.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.io.*;

public class Server extends Thread{

    private ServerSocket socket;
    protected Socket server;
    private Node node; 
    private Chord chord;


    // private ArrayList<SocketAddress> listofclient;
    public static Collection<Socket> activeClient = new ConcurrentLinkedQueue<>();


    public Server(Chord c) throws IOException{
        // this.node = n;
        this.chord = c;
        socket = new ServerSocket(this.chord.getPort());
        socket.setSoTimeout(70000);
        // listofclient = new ArrayList<>();
    }

    public void run(){
        while(true){
            try {
                System.out.println("Waiting for client on port "
                     + socket.getLocalPort() + "....");

                server = socket.accept();
            
/*
                // when a client gets accepted then do below stuff......
                System.out.println("Connected to " + server.getRemoteSocketAddress() + "\n");

                // listofclient.add(server.getRemoteSocketAddress());      // add the client address into an arraylist?? 
                activeClient.add(server);

                // this will get replaced?
                DataInputStream input = new DataInputStream(server.getInputStream());
                System.out.println(input.readUTF());
                DataOutputStream out = new DataOutputStream(server.getOutputStream());
                out.writeUTF("Thank you for connecting to " + server.getLocalSocketAddress()
                     + "\nGoodbye!");
                
                server.close();
                // socket.close(); // this socket needs to be closed somewhere? for the port to reopen
*/
            } catch (SocketTimeoutException s) {
                System.out.println("Socket timed out!");
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            } finally{
                try {
                    server.close();
                    // socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } 
        }
    }

    public void startServer(){
        // int port = Integer.parseInt(args[0]);
        // int port = 6601;

        try {
            System.out.println("Server started");
            Thread thread = this;
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // public static void main(String[] args) {
    //     // int port = Integer.parseInt(args[0]);
    //     int port = 6600;
    //     try {
    //         System.out.println("Server started");
    //         Thread thread = new Server(port);
    //         thread.start();
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    // }


}