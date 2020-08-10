//created by:
//Brian Cabral 
//Shoraj Manandhar
//Cecs327 

import java.util.ArrayList;
import java.util.List;


public class ChordKey implements Comparable {

	// Initialize the variables
	private String value;
	private byte[] key;

	// Constructor to initialize the key
	public ChordKey(byte[] key) {
		this.key = key;
	}

	// Constructor to initialize the value
	public ChordKey(String value) {
		this.value = value;
		this.key = SHAHasher.hash(value);
	}

	// Get the value
	public String getIdentifier() {
		return value;
	}

	// Set the value
	public void setIdentifier(String value) {
		this.value = value;
	}

	// Get the key
	public byte[] getKey() {
		return key;
	}

	// Set the key
	public void setKey(byte[] key) {
		this.key = key;
	}

	// get the node key and hash it and put it in a finger table
	public ChordKey createStartKey(int index) {
		byte[] newKey = new byte[key.length];
		System.arraycopy(key, 0, newKey, 0, key.length);
		int carry = 0;
		for (int i = (SHAHasher.size - 1) / 8; i >= 0; i--) {
			int value = key[i] & 0xff;
			value += (1 << (index % 8)) + carry;
			newKey[i] = (byte) value;
			if (value <= 0xff) {
				break;
			}
			carry = (value >> 8) & 0xff;
		}
		return new ChordKey(newKey);
	}

	// compare the hashed key and see where it fits in the finger table
	public boolean isBetween(ChordKey fromKey, ChordKey toKey) {
		if (fromKey.compareTo(toKey) < 0) {
			if (this.compareTo(fromKey) > 0 && this.compareTo(toKey) < 0) {
				return true;
			}
		} else if (fromKey.compareTo(toKey) > 0) {
			if (this.compareTo(toKey) < 0 || this.compareTo(fromKey) > 0) {
				return true;
			}
		}
		return false;
	}

	// compares two objects, two hashed keys in our case
	public int compareTo(Object obj) {
		ChordKey targetKey = (ChordKey) obj;
		for (int i = 0; i < key.length; i++) {
			int loperand = (this.key[i] & 0xff);
			int roperand = (targetKey.getKey()[i] & 0xff);
			if (loperand != roperand) {
				return (loperand - roperand);
			}
		}
		return 0;
	}

	// toString method for the hashed key
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (key.length > 4) {
			for (int i = 0; i < key.length; i++) {
				sb.append(Integer.toString(((int) key[i]) & 0xff) + ".");
			}
		} else {
			long n = 0;
			for (int i = key.length-1,j=0; i >= 0 ; i--, j++) {
				n |= ((key[i]<<(8*j)) & (0xffL<<(8*j)));
			}
			sb.append(Long.toString(n));
		}
		return sb.substring(0, sb.length() - 1).toString();
	}

}
