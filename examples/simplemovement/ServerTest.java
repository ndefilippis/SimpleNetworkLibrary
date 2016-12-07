package examples.simplemovement;

import java.io.IOException;

import examples.simplemovement.netcode.MoverServer;

public class ServerTest {
	public static void main(String[] args) throws IOException, InterruptedException{
		new Thread(new MoverServer(1337)).start();
	}
}