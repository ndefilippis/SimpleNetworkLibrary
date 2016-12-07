package examples.simplemovement;

import java.io.IOException;

import examples.simplemovement.netcode.MoverClient;

public class ClientTest {
	public static void main(String[] args) throws IOException, InterruptedException{
		new Thread(new MoverClient("localhost", 1337)).start();
	}
}