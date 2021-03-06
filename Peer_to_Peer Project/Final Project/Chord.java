
// https://github.com/milesoldenburg/jchord/tree/master/chord/src/main/java/com/milesoldenburg/jchord/chord

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class Chord {

    private String address;
    private int port; 
    private String existingNodeAddress = null;
    private int existingNodePort;

    private Server server;
    // private Client client; 

    private Node firstPredecessor;
    private Node secondPredecessor;
    private Node firstSuccessor;
    private Node secondSuccessor;

    private Map<Integer, Node> fingers = new HashMap<>();
    private long id;
    // private String hex;

    private Semaphore semaphore = new Semaphore(1);


    public Chord(String address, int port){
        this.address = address;
        this.port = port;

        System.out.println("Create A New Chord Ring");
        System.out.println("Server started");
        System.out.println("You are listening on "  + this.address + " on port "+ this.port);

        this.initializeNode();
        this.initializeSuccessors();

        try {
            server = new Server(this);
            server.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(new Stabilize(this)).start();
    }

    public Chord(String address, String port, String existingNodeAddress, int existingNodePort) {
        this.address = address;
        this.port = Integer.valueOf(port);

        this.existingNodeAddress = existingNodeAddress;
        this.existingNodePort = Integer.valueOf(existingNodePort);

        System.out.println("Joining the Chord ring");
        System.out.println("You are listening on port " + this.port);
        System.out.println("Connected to existing node " + this.existingNodeAddress + ":" + this.existingNodePort);

        this.initializeNode();
        this.initializeSuccessors();

        try {
            server = new Server(this);
            server.startServer();
            // Client client = new Client(this);
            // client.startClient();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Client client = new Client(this, existingNodeAddress, existingNodePort);
        // client.startClient();


        new Thread(new Stabilize(this)).start();
    }


    private void initializeNode(){
        if (this.existingNodeAddress == null) {
            for (int i = 0; i < 32; i++) {
                this.fingers.put(i, new Node(this.address, this.port));
            }
        } else {
            try{
                Socket socket = new Socket(this.existingNodeAddress, this.existingNodePort);
                
                PrintWriter socketWriter = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                BigInteger bigQuery = BigInteger.valueOf(2L);
                BigInteger bigSelfId = BigInteger.valueOf(this.id);

                for (int i = 0; i < 32; i++) {
                    BigInteger bigResult = bigQuery.pow(i);
                    bigResult = bigResult.add(bigSelfId);

                    // socketWriter.println("Find Node:" + bigResult.longValue());
                    // System.out.println("Sent: " + "Find Node:" + bigResult.longValue());

                    String serverResponse = socketReader.readLine();

                    String[] serverResponseFragments = serverResponse.split(":", 2);
                    String[] addressFragments = serverResponseFragments[1].split(":");

                    this.fingers.put(i, new Node(addressFragments[0], Integer.valueOf(addressFragments[1])));

                    // System.out.println("Received: " + serverResponse);
                }

                socketWriter.close();
                socketReader.close();
                socket.close();

            } catch (IOException e) {
                System.out.println("Error: Could not open connection to existing node");
                e.printStackTrace();
            }
        }
    }

    private void initializeSuccessors(){
        this.firstSuccessor = this.fingers.get(0);
        this.secondSuccessor = this.fingers.get(1);

        this.firstPredecessor = new Node(this.address, this.port);
        this.secondPredecessor = new Node(this.address, this.port);

        if (!this.address.equals(this.firstSuccessor.getAddress()) || (this.port != this.firstSuccessor.getPort())) {
            try {
                Socket socket = new Socket(this.firstSuccessor.getAddress(), this.firstSuccessor.getPort());

                PrintWriter socketWriter = new PrintWriter(socket.getOutputStream(), true);

                // socketWriter.println("NEW PREDECESSOR:" + this.getAddress() + ":" + this.getPort());
                // System.out.println("Sent: " + "NEW_PREDECESSOR :" + this.getAddress() + ":" + this.getPort() + " to " + this.firstSuccessor.getAddress() + ":" + this.firstSuccessor.getPort());

                socketWriter.close();
                socket.close();

            } catch (IOException e) {
                System.out.println("Error: Could not open connection to first successor");
                e.printStackTrace();
            }
        }
    }

    public Map<Integer, Node> getFingers() {
        return this.fingers;
    }

    public int getPort() {
        return this.port;
    }

    public String getAddress() {
        return this.address;
    }

    public long getId() {
        return this.id;
    }

    public Node getFirstSuccessor() {
        return this.firstSuccessor;
    }

    public void setFirstSuccessor(Node firstSuccessor) {
        this.firstSuccessor = firstSuccessor;
    }

    public Node getFirstPredecessor() {
        return this.firstPredecessor;
    }

    public void setFirstPredecessor(Node firstPredecessor) {
        this.firstPredecessor = firstPredecessor;
    }

    public Node getSecondSuccessor() {
        return secondSuccessor;
    }

    public void setSecondSuccessor(Node secondSuccessor) {
        this.secondSuccessor = secondSuccessor;
    }

    public Node getSecondPredecessor() {
        return secondPredecessor;
    }

    public void setSecondPredecessor(Node secondPredecessor) {
        this.secondPredecessor = secondPredecessor;
    }

    public Semaphore getSemaphore() {
        return semaphore;
    }

    public void acquire() {
        try {
            this.semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void release() {
        this.semaphore.release();
    }
    
}
