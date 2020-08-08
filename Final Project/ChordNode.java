
import java.io.PrintStream;

public class ChordNode {

	String nodeId;
	ChordKey nodeKey;
	ChordNode predecessor;
	ChordNode successor;
	FingerTable fingerTable;

	public ChordNode(String nodeId) {
		this.nodeId = nodeId;
		this.nodeKey = new ChordKey(nodeId);
		this.fingerTable = new FingerTable(this);
		this.create();
	}

	public ChordNode findSuccessor(String identifier) {
		ChordKey key = new ChordKey(identifier);
		return findSuccessor(key);
	}

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

	private ChordNode closestPrecedingNode(ChordKey key) {
		for (int i = Hash.KEY_LENGTH - 1; i >= 0; i--) {
			Finger finger = fingerTable.getFinger(i);
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
		for (int i = 0; i < Hash.KEY_LENGTH; i++) {
			Finger finger = fingerTable.getFinger(i);
			ChordKey key = finger.getStart();
			finger.setNode(findSuccessor(key));
		}
	}



	// ttry
	public void removeFinger(int x){
		for (int i = 0; i < Hash.KEY_LENGTH; i++) {
			if (i == x){
				Finger finger = fingerTable.getFinger(x);
				finger = null;
			}
		}
	}


	

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ChordNode[");
		sb.append("ID = " + nodeId);
		sb.append(", KEY = " + nodeKey);
		sb.append("]");
		return sb.toString(); 
	}

	public void printNeighbour(){
		System.out.println("Node: " + this);
		System.out.println("Predecessor: " + predecessor);
		System.out.println("Successor: " + successor + "\n");
	}

	// public void printFingerTable(PrintStream out) {
	public void printFingerTable() {
		for (int i = 0; i < Hash.KEY_LENGTH; i++) {
			Finger finger = fingerTable.getFinger(i);
			// out.println(finger.getStart() + "\t" + finger.getNode());
			System.out.println(finger.getStart() + "\t" + finger.getNode());
		}
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public ChordKey getNodeKey() {
		return nodeKey;
	}

	public void setNodeKey(ChordKey nodeKey) {
		this.nodeKey = nodeKey;
	}

	public ChordNode getPredecessor() {
		return predecessor;
	}

	public void setPredecessor(ChordNode predecessor) {
		this.predecessor = predecessor;
	}

	public ChordNode getSuccessor() {
		return successor;
	}

	public void setSuccessor(ChordNode successor) {
		this.successor = successor;
	}

	public FingerTable getFingerTable() {
		return fingerTable;
	}

	public void setFingerTable(FingerTable fingerTable) {
		this.fingerTable = fingerTable;
	}

}
