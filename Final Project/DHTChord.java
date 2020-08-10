//created by:
//Brian Cabral 
//Shoraj Manandhar
//Cecs327

import java.util.*;

public class DHTChord{

    // DHTList will contains all the nodes when they join the network or when created
    List<ChordNode> DHTList = new ArrayList<ChordNode>();
    SortedMap<ChordKey, ChordNode> sortedNodeMap = new TreeMap<ChordKey, ChordNode>();
    
    // To sort the list 
    Object[] arr;

    // Create a constructor that takes a node ID
    public void createNode(String id) throws Exception {
      ChordNode ChordNode = new ChordNode(id);
      DHTList.add(ChordNode);
      sortedNodeMap.put(ChordNode.getNodeKey(), ChordNode);
    }

    // Get a specific node from the DHT chord list with their index in the list
    public ChordNode getNode(int index) {
		  return DHTList.get(index);
    }
    
    // Remove the node from the list (not completed)
    public void removeNodeList(ChordNode chord){
      Iterator<ChordNode> iterator = DHTList.iterator();
      chord.removeFinger(0);
      while(iterator.hasNext()){
        ChordNode remove = iterator.next();
        if(remove.equals(chord)){
          iterator.remove();
        }
      }
    }
    
    // Sort the array based on their id
    public ChordNode getSortedNode(int index) {
		if (arr == null) {
         //   System.out.println("Test: Empty array list.");
			      arr = sortedNodeMap.keySet().toArray();
        }
		  return (ChordNode) sortedNodeMap.get(arr[index]);
    }

}