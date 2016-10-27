package counter;

import java.io.IOException;

import counter.netcode.CounterClient;
import counter.netcode.CounterServer;

public class CounterServerClient {
	public static void main(String[] args) throws IOException, InterruptedException{
		CounterServer server  = new CounterServer(1337);	
		new Thread(server).start();
		Thread.sleep(100);
		CounterClient client = new CounterClient("localhost", 1337);
		new Thread(client).start();
	}
}
