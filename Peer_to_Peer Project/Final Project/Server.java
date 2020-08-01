
// Copied this client-server model frm link below
// https://www.tutorialspoint.com/java/java_networking.htm#:~:text=A%20client%20program%20creates%20a,and%20reading%20from%20the%20socket.
// *** initial compile: java Client localhost 6066 (changed)

/**
 * How to compile: 
 * 
 * 1.compile server first:
 *      javac Server.java
 *      java Server
 * 2.Now compile client:
 *      javac Client.java
 *      java Client
 */


import java.net.*;
import java.io.*;

public class Server extends Thread{

    private ServerSocket socket;

    public Server(int port) throws IOException{
        socket = new ServerSocket(port);
        // socket.setSoTimeout(10000);
    }

    public void run(){
        while(true){
            try {
                System.out.println("Waiting for client on port "
                     + socket.getLocalPort() + "....");
                Socket server = socket.accept();
                
                System.out.println("Connected to " + server.getRemoteSocketAddress() + "\n");
                DataInputStream input = new DataInputStream(server.getInputStream());
                System.out.println(input.readUTF());
                
                DataOutputStream out = new DataOutputStream(server.getOutputStream());
                
                out.writeUTF("Thank you for connecting to " + server.getLocalSocketAddress()
                     + "\nGoodbye!");
                server.close();
                
            } catch (SocketTimeoutException s) {
                System.out.println("Socket timed out!");
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }

        }

    }

    public static void main(String[] args) {
        // int port = Integer.parseInt(args[0]);
        int port = 6601;
        try {
            System.out.println("Server started");
            Thread thread = new Server(port);
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}