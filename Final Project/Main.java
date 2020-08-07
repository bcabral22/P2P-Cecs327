
//192.168.254.22    6601


import java.io.PrintStream;
import java.net.InetAddress;
import java.net.URL;
import java.util.Scanner;

public class Main {

	public static final String HASH_FUNCTION = "SHA-1";
	public static final int KEY_LENGTH = 160;
	public static final int NUM_OF_NODES = 2;

	// public static int NUM_OF_NODES = 0;

	public static void menuOptions() {
		System.out.println();
		System.out.println("1. Join network");
		System.out.println("2. Leave network");
		System.out.println("3. Start server");
		System.out.println("4. Show Finger table");
		System.out.println("5. Show Predecessor & successors ");
		System.out.println("6. Upload files");
		System.out.println("7. Download files");
		System.out.println("-1. To exit");
	}

	public static void main(String[] args) throws Exception {

		Scanner scan = new Scanner(System.in);

		PrintStream out = System.out;
		out = new PrintStream("result.log");

		String host = InetAddress.getLocalHost().getHostAddress();
		// int port = 9000;

		Hash.setFunction(HASH_FUNCTION);
		Hash.setKeyLength(KEY_LENGTH);

		Chord chord = new Chord();

		String p = "6601";
		System.out.println("First Node is auto generated to start the chord.");
		System.out.println("ChordNode ID: " + host + " port: " + p);
		String addr = host + p;
		chord.createNode(addr);
		// NUM_OF_NODES++;


		// lets start with a menu
		while(true){
			menuOptions();

			int choice = scan.nextInt();
			scan.nextLine();

			System.out.println("Choose: " + choice);

			if (choice == 1) {		// gets index out of bound error when adding more than or less than 2 nodes
				System.out.println("Joining a network");

				System.out.print("Enter IP address to connect to network: " );
				String inputIP = scan.nextLine();	// 192.168.254.22 

				System.out.print("Enter the port number: ");
				p = scan.nextLine();			// 5000
				System.out.println();

				String addr2 = inputIP + p;
				chord.createNode(addr2);
				System.out.println( addr2 + " has been created");
				System.out.println("ChordNode ID: " + host + " port: " + p);
				// NUM_OF_NODES++;
				
				// out.println(NUM_OF_NODES + " nodes are created.");
				System.out.println(NUM_OF_NODES + " nodes are created.");

				for (int i = 0; i < NUM_OF_NODES; i++) {
					ChordNode node = chord.getSortedNode(i);
					// out.println(node);
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
				// out.println("Chord ring is established.");
				System.out.println("Chord ring is created");

				for (int i = 0; i < NUM_OF_NODES; i++) {
					ChordNode node = chord.getNode(i);
					node.fixFingers();
				}
				// out.println("Finger Tables are fixed.");
				System.out.println("Finger Tables are fixed.");

	
			} else if (choice == 2) {
				System.out.println("Leave the network");
				System.out.println("This needs to be implemented!!!!");
				
			} else if (choice == 3) {
				System.out.println("Start the server");
				
				ChordNode node = chord.getNode(0);
				System.out.println("The first node will be acting as our server contains the file");
				Server myServer = new Server(node);
				new Thread(myServer).start();

			} else if (choice == 4) {
				System.out.println("Printing Finger table");
				for (int i = 0; i < NUM_OF_NODES; i++) {
					ChordNode node1 = chord.getSortedNode(i);
					node1.printFingerTable(out);
				} 

			} else if (choice == 5) {
				System.out.println("Printing Predecessor & successors ");
				for (int i = 0; i < NUM_OF_NODES; i++) {
					ChordNode node2 = chord.getSortedNode(i);
					node2.printNeighbour();
				}

			} else if (choice == 6) {
				System.out.println("Upload files");


			} else if (choice == 7) {
				System.out.println("Download files");
				
			} else if (choice == -1){
				System.out.println("Exit the program");
				System.exit(0);
			}
			else
				System.out.println("Pick from the given options!");
		}


	}


}
