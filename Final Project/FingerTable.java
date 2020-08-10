//created by:
//Brian Cabral 
//Shoraj Manandhar
//Cecs327

public class FingerTable {

	// An array which takes in Node objects and puts it in the finger table
	private Node[] fingers;

	// Constructor: add the Node/Finger to the fingertable everytime the class is called
	public FingerTable(ChordNode node) {
		this.fingers = new Node[SHAHasher.size];
		for (int i = 0; i < fingers.length; i++) {
			ChordKey start = node.getNodeKey().createStartKey(i);
			fingers[i] = new Node(start, node);
		}
	}

	// get the node position in the finger table
	public Node getFinger(int i) {
		return fingers[i];
	}

	/*public void removeFinger(int i){
		// for(int j = fingers.length - 1 ; j >= 0 ; j--){
		// 	if(j == i){
		// 		this.fingers[i] = fingers[j+1]; 
		// 	}
		// }

		for (int j = 0; j < fingers.length; j++) {
			// if (j == i){
			// 	for (int z = j; z < fingers.length - 1; z++) {
			// 		fingers[z] = fingers[z+1];
			// 	}
			// }

			fingers[i] = null;
			
		}
	}*/




}
