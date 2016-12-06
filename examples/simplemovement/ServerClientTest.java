package examples.simplemovement;

import java.io.IOException;

import examples.simplemovement.netcode.MoverClient;
import examples.simplemovement.netcode.MoverServer;

public class ServerClientTest {
	public static void main(String[] args) throws IOException, InterruptedException{
		new Thread(new MoverServer(1337)).start();
		new Thread(new MoverClient("localhost", 1337)).start();
	}
}