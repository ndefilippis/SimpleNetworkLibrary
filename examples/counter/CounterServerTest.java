package examples.counter;

import java.io.IOException;

import examples.counter.netcode.CounterServer;

public class CounterServerTest {
	public static void main(String[] args) throws IOException{
		CounterServer server  = new CounterServer(1337);	
		new Thread(server).start();
	}
}
