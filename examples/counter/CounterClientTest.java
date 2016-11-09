package examples.counter;

import java.io.IOException;

import examples.counter.netcode.CounterClient;

public class CounterClientTest {
	public static void main(String[] args) throws IOException, InterruptedException{
		CounterClient client = new CounterClient("localhost", 1337);
		new Thread(client).start();
	}
}
