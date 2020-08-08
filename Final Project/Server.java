// http://tutorials.jenkov.com/java-multithreaded-servers/multithreaded-server.html

//192.168.254.22    6601

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {

//Changed this from 6601 to free port
    protected int serverPort = 6604;
    protected ServerSocket serverSocket = null;
    protected boolean isStopped    = false;
    protected Thread runningThread= null;
    private ChordNode chordNode;

    public Server(ChordNode chord){
        this.chordNode = chord;
    }

    public void run(){
        System.out.println("Server Started");
        System.out.println("ChordNode ID: " + chordNode.getNodeId() + " Key: " + chordNode.getNodeKey());
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
                System.out.println("Accepted connection : " + clientSocket);
                //changed type for png
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


            } catch (IOException e) {
                if(isStopped()) {
                    System.out.println("Server Stopped.") ;
                    return;
                }
                throw new RuntimeException(
                    "Error accepting client connection", e);
            }
            // new Thread(new WorkerRunnable(clientSocket, "Multithreaded Server")).start();
            // start client thread here?? 
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
}