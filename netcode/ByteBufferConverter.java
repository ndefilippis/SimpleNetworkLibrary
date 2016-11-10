package netcode;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

public class ByteBufferConverter {
	public static Charset charset = Charset.forName("UTF-8");
	public static CharsetEncoder encoder = charset.newEncoder();
	public static CharsetDecoder decoder = charset.newDecoder();

	public static ByteBuffer str_to_bb(String msg){
	  try{
	    return encoder.encode(CharBuffer.wrap(msg));
	  }catch(Exception e){e.printStackTrace();}
	  return null;
	}

	public static String bb_to_str(ByteBuffer buffer){
	  String data = "";
	  try{
	    int old_position = buffer.position();
	    data = decoder.decode(buffer).toString();
	    // reset buffer's position to its original so it is not altered:
	    buffer.position(old_position);  
	  }catch (Exception e){
	    e.printStackTrace();
	    return "";
	  }
	  return data;
	}

	public static String bbArray(ByteBuffer buffer){
		String s = "";
		for(int i = 0; i < buffer.remaining(); i++){
			s += ", " + buffer.get(i);
		}
		return "[" + s.substring(2) + "]";
	}
	
	public static void serializeWriteIntArray(ByteBuffer buffer, int[] array){
		buffer.putInt(array.length);
		for(int i = 0; i < array.length; i++){
			buffer.putInt(array[i]);
		}
	} 
	
	public static <T extends Serializable> void serializeWriteArray(ByteBuffer buffer, T[] array){
		buffer.putInt(array.length);
		for(int i = 0; i < array.length; i++){
			array[i].serializeWrite(buffer);
		}
	} 
	
}
