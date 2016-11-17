package netcode.packet;

import java.util.HashMap;
import java.util.Map;

public class Acker {
	private int remotePacketID;
	private int ackBitfield;
	
	public void updateAckBitfield(int remotePacketID){
		this.remotePacketID = remotePacketID;
		int newACK = 0;
		int lastRemotePacketID = remotePacketID;
		for(int i = remotePacketID - lastRemotePacketID - 1; i < 32; i++){
			if(((ackBitfield >> i) & 1) == 1){
				newACK |= 1 << i;
			}
		}
		this.ackBitfield = newACK;
	}
	
	public int getRemotePacketID(){
		return remotePacketID;
	}
	
	public int getAckBitfield(){
		return ackBitfield;
	}
	
	public Map<Integer, Boolean> getReceivedPackets(){
		Map<Integer, Boolean> _res = new HashMap<Integer, Boolean>();
		for(int i = 0; i < 32; i++){
			_res.put(remotePacketID - 1 - i,  ((ackBitfield >> i) & 1) == 1);
		}
		return _res;
	}
}
