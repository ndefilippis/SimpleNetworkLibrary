package counter;

import java.io.IOException;

import counter.netcode.CounterServer;

public class CounterServerTest {
	public static void main(String[] args) throws IOException{
		CounterServer server  = new CounterServer(1337);	
		new Thread(server).start();
	}
}
