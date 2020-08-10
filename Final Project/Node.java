//created by:
//Brian Cabral 
//Shoraj Manandhar
//Cecs327


public class Node{

    // varaibles key and node
    ChordKey chordKey;
     ChordNode chordNode;
     int id; 

    // public Node(){ }

    // Constructor to initialize the variables 
    public Node(ChordKey c, ChordNode n){
        this.chordKey = c;
        this.chordNode = n;
    }

    // Setter method to set the key
    public  void  setKey(ChordKey c){
        this.chordKey = c;
    }

    // Getter method to get the key
    public ChordKey getKey(){
        return chordKey;
    }

    // Setter method to set the node in the chord
    public void setNode(ChordNode n){
        this.chordNode = n;
    }

    // Getter method to get the node in the chord
    public  ChordNode getNode(){
        return chordNode;
		
    }


}