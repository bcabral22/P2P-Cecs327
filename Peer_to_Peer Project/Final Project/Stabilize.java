
// https://github.com/ChuanXia/Chord/blob/master/Node.java


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;


public class Stabilize extends Thread {

    private Chord chord;

    public Stabilize(Chord chord){
        this.chord = chord;
    }

    public void run() {
        try {
            // Initially sleep
            int delaySeconds = 10;
            Thread.sleep(delaySeconds * 1000);

            Socket socket = null;
            PrintWriter socketWriter = null;
            BufferedReader socketReader = null;

            while (true) {
                // Only open a connection to the successor if it is not ourselves
                if (!this.chord.getAddress().equals(this.chord.getFirstSuccessor().getAddress()) || (this.chord.getPort() != this.chord.getFirstSuccessor().getPort())) {
                    // Open socket to successor
                    socket = new Socket(this.chord.getFirstSuccessor().getAddress(), this.chord.getFirstSuccessor().getPort());

                    // Open reader/writer to chord node
                    socketWriter = new PrintWriter(socket.getOutputStream(), true);
                    socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    // Submit a request for the predecessor
                    // socketWriter.println("REQUEST_PREDECESSOR :" + this.chord.getId() + " asking " + this.chord.getFirstSuccessor().getPort());
                    // System.out.println("Sent: " + "REQUEST_PREDECESSOR:" + this.chord.getId() + " asking " + this.chord.getFirstSuccessor().getPort());

                    // Read response from chord
                    String serverResponse = socketReader.readLine();
                    // System.out.println("Received: " + serverResponse);

                    // Parse server response for address and port
                    String[] predecessorFragments = serverResponse.split(":");
                    String predecessorAddress = predecessorFragments[0];
                    int predecessorPort = Integer.valueOf(predecessorFragments[1]);

                    // If the address:port that was returned from the server is not ourselves then we need to adopt it as our new successor
                    if (!this.chord.getAddress().equals(predecessorAddress) || (this.chord.getPort() != predecessorPort)) {
                        this.chord.acquire();

                        Node newSuccessor = new Node(predecessorAddress, predecessorPort);

                        // Update finger table entries to reflect new successor
                        this.chord.getFingers().put(1, this.chord.getFingers().get(0));
                        this.chord.getFingers().put(0, newSuccessor);

                        // Update successor entries to reflect new successor
                        this.chord.setSecondSuccessor(this.chord.getFirstSuccessor());
                        this.chord.setFirstSuccessor(newSuccessor);

                        this.chord.release();

                        // Close connections
                        socketWriter.close();
                        socketReader.close();
                        socket.close();

                        // Inform new successor that we are now their predecessor
                        socket = new Socket(newSuccessor.getAddress(), newSuccessor.getPort());

                        // Open writer/reader to new successor node
                        socketWriter = new PrintWriter(socket.getOutputStream(), true);
                        socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                        // Tell successor that this node is its new predecessor
                        // socketWriter.println("NEW_PREDECESSOR: " + this.chord.getAddress() + ":" + this.chord.getPort());
                        // System.out.println("Sent: " + "NEW_PREDECESSOR: " + this.chord.getAddress() + ":" + this.chord.getPort());
                    }

                    BigInteger bigQuery = BigInteger.valueOf(2L);
                    BigInteger bigSelfId = BigInteger.valueOf(this.chord.getId());

                    this.chord.acquire();

                    // Refresh all fingers by asking successor for nodes
                    for (int i = 0; i < 32; i++) {
                        BigInteger bigResult = bigQuery.pow(i);
                        bigResult = bigResult.add(bigSelfId);

                        // Send query to chord
                        socketWriter.println("FIND_NODE: " + bigResult.longValue());
                        System.out.println("Sent: " + " FIND_NODE:" + bigResult.longValue());

                        // Read response from chord
                        serverResponse = socketReader.readLine();

                        // Parse out address and port
                        String[] serverResponseFragments = serverResponse.split(":", 2);
                        String[] addressFragments = serverResponseFragments[1].split(":");

                        // Add response finger to table
                        this.chord.getFingers().put(i, new Node(addressFragments[0], Integer.valueOf(addressFragments[1])));
                        this.chord.setFirstSuccessor(this.chord.getFingers().get(0));
                        this.chord.setSecondSuccessor(this.chord.getFingers().get(1));

                        // System.out.println("Received: " + serverResponse);
                    }

                    this.chord.release();

                    // Close connections
                    socketWriter.close();
                    socketReader.close();
                    socket.close();

                } else if (!this.chord.getAddress().equals(this.chord.getFirstPredecessor().getAddress()) || (this.chord.getPort() != this.chord.getFirstPredecessor().getPort())) {
                    // Open socket to successor
                    socket = new Socket(this.chord.getFirstPredecessor().getAddress(), this.chord.getFirstPredecessor().getPort());

                    // Open reader/writer to chord node
                    socketWriter = new PrintWriter(socket.getOutputStream(), true);
                    socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    BigInteger bigQuery = BigInteger.valueOf(2L);
                    BigInteger bigSelfId = BigInteger.valueOf(this.chord.getId());

                    this.chord.acquire();

                    // Refresh all fingers by asking successor for nodes
                    for (int i = 0; i < 32; i++) {
                        BigInteger bigResult = bigQuery.pow(i);
                        bigResult = bigResult.add(bigSelfId);

                        // Send query to chord
                        // socketWriter.println("FIND_NODE :" + bigResult.longValue());
                        // System.out.println("Sent: " + " FIND_NODE:" + bigResult.longValue());

                        // Read response from chord
                        String serverResponse = socketReader.readLine();

                        // Parse out address and port
                        String[] serverResponseFragments = serverResponse.split(":", 2);
                        String[] addressFragments = serverResponseFragments[1].split(":");

                        // Add response finger to table
                        this.chord.getFingers().put(i, new Node(addressFragments[0], Integer.valueOf(addressFragments[1])));
                        this.chord.setFirstSuccessor(this.chord.getFingers().get(0));
                        this.chord.setSecondSuccessor(this.chord.getFingers().get(1));

                        System.out.println("Received: " + serverResponse);
                    }

                    this.chord.release();

                    // Close connections
                    socketWriter.close();
                    socketReader.close();
                    socket.close();
                }

                // Stabilize again after delay
                Thread.sleep(delaySeconds * 1000);
            }
            
        } catch (InterruptedException e) {
            System.err.println("stabilize() thread interrupted");
            e.printStackTrace();
        } catch (UnknownHostException e) {
            System.err.println("stabilize() could not find host of first successor");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("stabilize() could not connect to first successor");
            e.printStackTrace();
        }
    }

}