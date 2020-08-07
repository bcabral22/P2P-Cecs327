

import java.io.PrintStream;
import java.net.InetAddress;
import java.net.URL;
import java.util.Scanner;

public class Main {

	public static final String HASH_FUNCTION = "SHA-1";
	public static final int KEY_LENGTH = 160;
	public static final int NUM_OF_NODES = 2;

	public static void main(String[] args) throws Exception {

		// write to a file
		PrintStream out = System.out;
		out = new PrintStream("result.log");

		String host = InetAddress.getLocalHost().getHostAddress();
		// int port = 9000;

		Hash.setFunction(HASH_FUNCTION);
		Hash.setKeyLength(KEY_LENGTH);

		Chord chord = new Chord();

		String p = "6601"; 
		System.out.println("ChordNode ID: " + host + " port: " + p);
		String addr = host + p;
		chord.createNode(addr);


		// this will be user input?
		p = "5000";
		String addr2 = host + p;
		System.out.println("ChordNode ID: " + host + " port: " + p);
		chord.createNode(addr2);


		out.println(NUM_OF_NODES + " nodes are created.");
		System.out.println(NUM_OF_NODES + " nodes are created.");

		for (int i = 0; i < NUM_OF_NODES; i++) {
			ChordNode node = chord.getSortedNode(i);
			out.println(node);
			System.out.println(node);
		}

		for (int i = 1; i < NUM_OF_NODES; i++) {
			ChordNode node = chord.getNode(i);
			node.join(chord.getNode(0));
			ChordNode preceding = node.getSuccessor().getPredecessor();
			node.stabilize();
			if (preceding == null) {
				node.getSuccessor().stabilize();
			} else {
				preceding.stabilize();
			}
		}
		out.println("Chord ring is established.");
		System.out.println("Chord ring is created");

		for (int i = 0; i < NUM_OF_NODES; i++) {
			ChordNode node = chord.getNode(i);
			node.fixFingers();
		}
		out.println("Finger Tables are fixed.");
		System.out.println("Finger Tables are fixed.");

		// for (int i = 0; i < NUM_OF_NODES; i++) {
		// 	ChordNode node = chord.getSortedNode(i);
		// 	node.printFingerTable(out);
		// }


		// start up the server
		// now server will just wait for the client to connect... 

		ChordNode node = chord.getNode(0);
		System.out.println("The first node will be acting as our server contains the file");
		Server myServer = new Server(node);
		new Thread(myServer).start();


		// meanwhile server is waiting, we can do some options like add a new peer to the network
		// delete a node from the network, ... etc
		// while(true){
		// 	menuOptions();

		// 	Scanner scan = new Scanner(System.in);
		// 	int choice = scan.nextInt();

		// 	System.out.println("Choose: " + choice);

		// 	if (choice == 1) {
		// 		System.out.println("Leave Network");
		// 		System.out.println("I guess we will just make the server leave the network" +
		// 		"but re-connect it cuz we only have 2 nodes");
				
		// 	} else if (choice == 2) {
		// 		System.out.println("Finger table");
		// 		for (int i = 0; i < NUM_OF_NODES; i++) {
		// 			ChordNode node1 = chord.getSortedNode(i);
		// 			node1.printFingerTable(out);
		// 		}
				
		// 	} else if (choice == 3) {
		// 		System.out.println("Predecessor & successors ");
		// 		for (int i = 0; i < NUM_OF_NODES; i++) {
		// 			ChordNode node2 = chord.getSortedNode(i);
		// 			node2.printNeighbour();
		// 		}

		// 	} else if (choice == 4) {
		// 		System.out.println("Upload files");
				

		// 	} else if (choice == 5) {
		// 		System.out.println("Download files");
				
		// 	}
		// 	else
		// 		System.out.println("Shit?");
		// }


		



	}

	public static void menuOptions() {
		System.out.println();
		System.out.println("1. Leave network");
		System.out.println("2. Show Finger table");
		System.out.println("3. Show Predecessor & successors ");
		System.out.println("4. Upload files");
		System.out.println("5. Download files");
	}
}
