package netcode;

import java.nio.ByteBuffer;

public interface Serializable {
	
	public void serializeRead(ByteBuffer buffer);
	
	public void serializeWrite(ByteBuffer buffer);
}
