
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class Chord {

	List<ChordNode> nodeList = new ArrayList<ChordNode>();
	SortedMap<ChordKey, ChordNode> sortedNodeMap = new TreeMap<ChordKey, ChordNode>();
	Object[] sortedKeyArray;

	public void createNode(String nodeId) throws Exception {
		ChordNode node = new ChordNode(nodeId);
		nodeList.add(node);
		sortedNodeMap.put(node.getNodeKey(), node);
	}

	public ChordNode getNode(int i) {
		return (ChordNode) nodeList.get(i);
	}

	public ChordNode getSortedNode(int i) {
		if (sortedKeyArray == null) {
			sortedKeyArray = sortedNodeMap.keySet().toArray();
		}
		return (ChordNode) sortedNodeMap.get(sortedKeyArray[i]);
	}

	public void removeNodeList(ChordNode chord){
		Iterator<ChordNode> iterator = nodeList.iterator();

		chord.removeFinger(0);

		while(iterator.hasNext()){
			ChordNode remove = iterator.next();
			if(remove.equals(chord)){
				iterator.remove();
			}
		}
		

		// for (ChordNode chordNode : nodeList) {
		// 	if (chordNode.equals(chord)){

		// 	}
		// }
	}
}
