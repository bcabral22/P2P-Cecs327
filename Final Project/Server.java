// http://tutorials.jenkov.com/java-multithreaded-servers/multithreaded-server.html

//created by:
//Brian Cabral 
//Shoraj Manandhar
//Cecs327

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

// import org.graalvm.compiler.graph.NodeList;

public class Server implements Runnable {

    // Initialize all our variables
    protected ServerSocket serverSocket = null;
    protected boolean isStopped    = false;
    protected Thread runningThread = null;
    private ChordNode chordNode;

    // Note to self:  Changed this from 6601 to free port
    protected int serverPort = 6604;

    // Get this chordList from the ChordNode class
    // This will consist of all the nodes that are currently in the chord ring network
    List<ChordNode> chordList;  

    // private Chord myChord;

    public Server(ChordNode chord, List<ChordNode> cnode){
        this.chordNode = chord;
        this.chordList = cnode;
    }

    public void run(){
        System.out.println("Server Started");
        System.out.println("ChordNode ID: " + chordNode.getNodeId()); //+ " Key: " + chordNode.getNodeKey());

        synchronized(this){
            this.runningThread = Thread.currentThread();
        }

        openServerSocket(); // open a new socket connection that will be listening to uncoming clients 

        // run the server in an infinite loop
        while(! isStopped()){
            System.out.println("Waiting for client to connect on " + chordNode.getNodeId() + "...");
            Socket clientSocket = null;

            try {

                // First we want to verify if the random client that is connecting to this server is 
                //      present in the chord network. The client is only able to receive the file
                //      if that client belongs to this network

                // In order to do so, we have to verify the client first.
                clientSocket = this.serverSocket.accept();

                // First Client Socket Accepted
                System.out.println("Accepted connection : " + clientSocket);        // this should be inside the if statment, for now its try

                // receive the client's ip address and port number and see if that is present in the chord or not
                // if it is not present then it will not receive the file
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String msg = "";
                boolean makeSure = false;
                
                // read every string that the client sends using a this while loop
                while ((msg = in.readLine()) != null) {
                    System.out.println("Received: " + msg);
                    
                    System.out.println("Is the client in the network? "+ containsAKeyword(msg, chordList));
                    makeSure = containsAKeyword(msg, chordList);
                }

                // close the socket and connection
                in.close();
                clientSocket.close();

                // sleep the thread
                Thread.sleep(2000);

                // Open a client connection again in order to receive the requested file
                // we have to close the previous connection to be able to receive the file
                
                // After verification of the client, it is able to receive the file from the server only
                // if the client is present in the network
                clientSocket = serverSocket.accept();

                if(makeSure){  // if true, client will receive the full file
                    System.out.println("Client is present in the network");
                    System.out.println(clientSocket + " is connected");

                    File transferFile = new File ("cool.png");                  // name of the file the server has access to
                    byte [] bytearray  = new byte [(int)transferFile.length()]; // read the file in binary 
                    FileInputStream fin = new FileInputStream(transferFile);
                    BufferedInputStream bin = new BufferedInputStream(fin);
                    bin.read(bytearray,0,bytearray.length);

                    OutputStream os = clientSocket.getOutputStream();           // send the file
                    System.out.println("Sending Files...");
                    os.write(bytearray,0,bytearray.length);

                    // flush the output stream and close the socket after completion 
                    os.flush();
                    clientSocket.close();
                    System.out.println("File transfer complete\n");
                    bin.close();
                    
                } else{     // 
                    System.out.println(clientSocket + " is not present in the network");
                }

            } catch (IOException e) {
                if(isStopped()) {
                    System.out.println("Server Stopped.") ;
                    return;
                }
                throw new RuntimeException(
                    "Error accepting client connection", e);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        System.out.println("Server Stopped.") ;
    }


    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    // stop the server
    public synchronized void stop(){
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    // open a new socket connection when requested
    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port! ", e);
        }
    }

    // check the ChordNode list & see if the client is present in the chord network
    public boolean containsAKeyword(String myString, List<ChordNode> list){
        for(ChordNode key : list){
            // System.out.println("NodeID are: " + key.getNodeId().toString());
            // System.out.println("The message is: " + myString);
           if(key.getNodeId().equals(myString)){
              return true;
           } 
        }
        return false; 
     }


}
