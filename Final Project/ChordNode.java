//created by:
//Brian Cabral 
//Shoraj Manandhar
//Cecs327
import java.io.PrintStream;

//this class will make the nodes for the chord
// this will create the successor and the prodecessor
public class ChordNode {

	
	//string to keep track of the node id
	private String nodeId;
	private ChordKey nodeKey;
	private ChordNode predecessor;
	private ChordNode successor;
	private FingerTable fingerTable;

	public ChordNode(String nodeId) {
		this.nodeId = nodeId;
		this.nodeKey = new ChordKey(nodeId);
		this.fingerTable = new FingerTable(this);
		this.create();
	}
	
//this will find the successor. 
//the successor the node that is the smallest identifier
	public ChordNode findSuccessor(String identifier) {
		ChordKey key = new ChordKey(identifier);
		return findSuccessor(key);
	}

	//this is returning the key of the successor
	public ChordNode findSuccessor(ChordKey key) {

		if (this == successor) {
			return this;
		}

		if (key.isBetween(this.getNodeKey(), successor.getNodeKey())
				|| key.compareTo(successor.getNodeKey()) == 0) {
			return successor;
		} else {
			ChordNode node = closestPrecedingNode(key);
			if (node == this) {
				return successor.findSuccessor(key);
			}
			return node.findSuccessor(key);
		}
	}

	//this will get the next precedding node
	private ChordNode closestPrecedingNode(ChordKey key) {
		for (int i = SHAHasher.size - 1; i >= 0; i--) {
			Node finger = fingerTable.getFinger(i);
			ChordKey fingerKey = finger.getNode().getNodeKey();
			if (fingerKey.isBetween(this.getNodeKey(), key)) {
				return finger.getNode();
			}
		}
		return this;
	}

	public void create() {
		predecessor = null;
		successor = this;
	}
//this will let the next node join
	public void join(ChordNode node) {
		predecessor = null;
		successor = node.findSuccessor(this.getNodeId());
	}

	public void stabilize() {
		ChordNode node = successor.getPredecessor();
		if (node != null) {
			ChordKey key = node.getNodeKey();
			if ((this == successor)
					|| key.isBetween(this.getNodeKey(), successor.getNodeKey())) {
				successor = node;
			}
		}
		successor.notifyPredecessor(this);
	}

	private void notifyPredecessor(ChordNode node) {
		ChordKey key = node.getNodeKey();
		if (predecessor == null
				|| key.isBetween(predecessor.getNodeKey(), this.getNodeKey())) {
			predecessor = node;
		}
	}

	public void fixFingers() {
		for (int i = 0; i < SHAHasher.size; i++) {
			Node finger = fingerTable.getFinger(i);
			ChordKey key = finger.getKey();
			finger.setNode(findSuccessor(key));
		}
	}



	// ttry
	public void removeFinger(int x){
		for (int i = 0; i < SHAHasher.size; i++) {
			if (i == x){
				Node finger = fingerTable.getFinger(x);
				finger = null;
			}
		}
	}


	
//striing that reads out the node
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ChordNode[");
		sb.append("ID = " + nodeId);
		//sb.append(", KEY = " + nodeKey);
		sb.append("]");
		return sb.toString(); 
	}
//string that prints out prodessor and succeosr
	public void printNeighbour(){
		System.out.println("Node: " + this);
		System.out.println("Predecessor: " + predecessor);
		System.out.println("Successor: " + successor + "\n");
	}

	// public void printFingerTable(PrintStream out) {
	public void printFingerTable() {
		for (int i = 0; i < SHAHasher.size; i++) {
			Node finger = fingerTable.getFinger(i);
			// out.println(finger.getStart() + "\t" + finger.getNode());
			System.out.println(finger.getKey() + "\t" + finger.getNode());
		}
	}
//return the node id
	public String getNodeId() {
		return nodeId;
	}
//sets the node id
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
//gets the node key
	public ChordKey getNodeKey() {
		return nodeKey;
	}
//sets the node key
	public void setNodeKey(ChordKey nodeKey) {
		this.nodeKey = nodeKey;
	}
//reurns the predecessor node
	public ChordNode getPredecessor() {
		return predecessor;
	}
//sets the predecssort node 
	public void setPredecessor(ChordNode predecessor) {
		this.predecessor = predecessor;
	}
//gets the successor
	public ChordNode getSuccessor() {
		return successor;
	}
//sets the sucesssor
	public void setSuccessor(ChordNode successor) {
		this.successor = successor;
	}
//gets the finger table 
	public FingerTable getFingerTable() {
		return fingerTable;
	}
//sets the finger table 
	public void setFingerTable(FingerTable fingerTable) {
		this.fingerTable = fingerTable;
	}

}
