

public class FingerTable {

	Finger[] fingers;

	public FingerTable(ChordNode node) {
		this.fingers = new Finger[Hash.KEY_LENGTH];
		for (int i = 0; i < fingers.length; i++) {
			ChordKey start = node.getNodeKey().createStartKey(i);
			fingers[i] = new Finger(start, node);
		}
	}

	public Finger getFinger(int i) {
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
