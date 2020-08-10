//created by:
//Brian Cabral 
//Shoraj Manandhar
//Cecs327

import java.util.*;
import java.util.zip.CRC32;
import java.security.MessageDigest;

public class SHAHasher{

    // 32 bits key length hash
    public static int size = 32; 
    public static String hashFunction = "CRC32";

    public static void setHashFunction(String x){
        if (x.equals("SHA-1")) {
			SHAHasher.size = 160;
        } else if (x.equals("CRC32")) {
			SHAHasher.size = 64;
        } else if (x.equals("Java")) {
			SHAHasher.size = 32;
        }
		SHAHasher.hashFunction = hashFunction;
    }

    // A method that returns the hashing function
    public static String getHashFunction(){
        return hashFunction;
    }

    // A method to return the size of hash length
    public static int getSize(){
        return size;
    }

    // A method to set the size of hash length
    public static void setSize(int s){
        size = s; 
    }
    
    // https://www.findbestopensource.com/product/joonion-jchord
    // get the 32 bit length hashed key
    public static byte[] hash(String x){
        if (hashFunction.equals("SHA-1")){
            try {
                MessageDigest msg = MessageDigest.getInstance(hashFunction);
                msg.reset();

                byte[] inByte = msg.digest(x.getBytes());
                byte[] value = new byte[size/8];

                int shrink = inByte.length / value.length;
                int bitCount = 1;

                for (int j = 0; j < inByte.length * 8; j++) {
					int currBit = ((inByte[j / 8] & (1 << (j % 8))) >> j % 8);
					if (currBit == 1)
						bitCount++;
					if (((j + 1) % shrink) == 0) {
						int shrinkBit = (bitCount % 2 == 0) ? 0 : 1;
						value[j / shrink / 8] |= (shrinkBit << ((j / shrink) % 8));
						bitCount = 1;
					}
				}
				return value;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (hashFunction.equals("CRC32")){
            CRC32 crc32 = new CRC32();
			crc32.reset();
			crc32.update(x.getBytes());
			long code = crc32.getValue();
			code &= (0xffffffffffffffffL >>> (64 - size));
			byte[] value = new byte[size / 8];
			for (int i = 0; i < value.length; i++) {
				value[value.length - i - 1] = (byte) ((code >> 8 * i) & 0xff);
			}
			return value;
        }

        if (hashFunction.equals("Java")) {
			int code = x.hashCode();
			code &= (0xffffffff >>> (32 - size));
			byte[] value = new byte[size / 8];
			for (int i = 0; i < value.length; i++) {
				value[value.length - i - 1] = (byte) ((code >> 8 * i) & 0xff);
			}
			return value;
		}

        return null;
    }



}


/*
References: 
    - https://www.findbestopensource.com/product/joonion-jchord
    - https://github.com/milesoldenburg/jchord/tree/master/chord/src/main/java/com/milesoldenburg/jchord/chord
*/

