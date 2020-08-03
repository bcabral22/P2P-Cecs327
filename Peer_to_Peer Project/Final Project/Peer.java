
/*
    This one is useless 

*/

import java.net.*;
import java.util.Scanner;
import java.io.*;

public class Peer{

    public static void main(String[] args) {
        
        String myPeer;
        Scanner scan = new Scanner(System.in);

        System.out.println("Welcome to P2P network");
        System.out.println("Do you wish to receive a file? (Y/N): ");
    
        myPeer = scan.nextLine();

        if (myPeer.equals("Y")) {
            System.out.println("You are a client.");
            System.out.println("The name of the file you want to receive... blah balh ");

            try {
                Process clientProcess = Runtime.getRuntime().exec("java Client");
            // Process clientProcess = Runtime.getRuntime().exec("java Client");
            } catch (Exception e) {
                e.printStackTrace();
            }

        } 
        else {
            System.out.println("You are a Server.");

            try {
                // Process serverProcess = Runtime.getRuntime().exec("java Server");
                Server server = new Server(5000);
                server.startServer();

            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
    }
    
}