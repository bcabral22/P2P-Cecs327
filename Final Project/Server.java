// http://tutorials.jenkov.com/java-multithreaded-servers/multithreaded-server.html

//192.168.254.22    6601

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

//Changed this from 6601 to free port
    protected int serverPort = 6604;
    protected ServerSocket serverSocket = null;
    protected boolean isStopped    = false;
    protected Thread runningThread = null;
    private ChordNode chordNode;

    // private Chord myChord;
    List<ChordNode> chordList;  

    public Server(ChordNode chord, List<ChordNode> cnode){
        this.chordNode = chord;
        this.chordList = cnode;
    }

    public void run(){
        System.out.println("Server Started");
        System.out.println("ChordNode ID: " + chordNode.getNodeId() + " Key: " + chordNode.getNodeKey());

        // System.out.println("This shit will get removed~! " + Arrays.toString(chordList.toArray()));

        synchronized(this){
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
        while(! isStopped()){
            System.out.println("Waiting for client to connect on " + chordNode.getNodeId() + "...");
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();

                // send a file
                System.out.println("Accepted connection : " + clientSocket);        // this should be inside the if statment, for now its try


                // receive an ip address and port number and see if that is present in the chord or not
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String msg = "";
                boolean makeSure = false; 
                while ((msg = in.readLine()) != null) {
                    System.out.println("Received: " + msg);
                    System.out.println(containsAKeyword(msg, chordList));
                    makeSure = containsAKeyword(msg, chordList);
                }

                in.close();
                clientSocket.close();

                Thread.sleep(2000);

                clientSocket = serverSocket.accept();
                if(makeSure){  
                // if(containsAKeyword(msg, chordList)){   // if true, then we send the file
                    System.out.println("Client is present in the network");
                    System.out.println(clientSocket + " is connected");

                    File transferFile = new File ("cool.png");
                    byte [] bytearray  = new byte [(int)transferFile.length()];
                    FileInputStream fin = new FileInputStream(transferFile);
                    BufferedInputStream bin = new BufferedInputStream(fin);
                    bin.read(bytearray,0,bytearray.length);
                    OutputStream os = clientSocket.getOutputStream();
                    System.out.println("Sending Files...");
                    os.write(bytearray,0,bytearray.length);
                    os.flush();
                    clientSocket.close();
                    System.out.println("File transfer complete");
                    bin.close();
                    
                } else{
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

    public synchronized void stop(){
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port 6601", e);
        }
    }


    public boolean containsAKeyword(String myString, List<ChordNode> list){
        for(ChordNode key : list){
            // System.out.println("NodeID are: " + key.getNodeId().toString());
            // System.out.println("The message is: " + myString);
           if(key.getNodeId().equals(myString)){
              return true;
           } 
        //    else if(key.getNodeId().equals("192.168.254.229000")){
        //        return true;
        //    } 
        }
        return false; 
     }


}