package test;

import java.util.Arrays;

public class IntToByteTest {

	public static void main(String[] args) {
		byte[] array = new byte[8];
		long i = 389749443739654L;
			longToByteArray(i, array, 0);
			long test = byteArrayToLong(array, 0);
			if(i != test){
				System.out.println("WRONG: Expected " + i + ", got " + test);
			}
			else{
				System.out.println("RIGHT:" + i);
			}
		
	}
	
	protected static void intToByteArray(int value, byte[] array, int start){
		for(int i = 0; i < 4; i++){
			array[i + start] = (byte) (value >>> (8 * i));
		}
	}
	
	protected static int byteArrayToInt(byte[] array, int start){
		int ret = 0;
		for(int i = start; i < start + 4; i++){
			ret |= ((array[i] & 0xff) << (8 * i));
		}
		return ret;
	}
	
	protected static void longToByteArray(long value, byte[] array, int start){
		for(int i = 0; i < 8; i++){
			array[i + start] = (byte) (value >>> (8 * i));
		}
		System.out.println(value + " " + Arrays.toString(array));
	}
	
	protected static long byteArrayToLong(byte[] array, int start){
		long ret = 0;
		for(int i = 0; i < 8; i++){
			ret += ((long)array[i + start] & 0xff) << (8 * i);
		}
		return ret;
	}

	
}
