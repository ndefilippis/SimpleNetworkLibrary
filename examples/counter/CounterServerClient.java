package examples.counter;

import java.io.IOException;

import examples.counter.netcode.CounterClient;
import examples.counter.netcode.CounterServer;

public class CounterServerClient {
	public static void main(String[] args) throws IOException, InterruptedException{
		CounterServer server  = new CounterServer(1337);	
		new Thread(server).start();
		CounterClient client = new CounterClient("localhost", 1337);
		new Thread(client).start();
		client = new CounterClient("localhost", 1337);
		new Thread(client).start();
		Thread.sleep(100);
		client = new CounterClient("localhost", 1337);
		new Thread(client).start();
	}
}
