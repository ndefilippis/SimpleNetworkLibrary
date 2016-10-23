package simplemovement.netcode;

import java.io.IOException;

public class ServerClientTest {
	public static void main(String[] args) throws IOException, InterruptedException{
		new Thread(new ServerThread()).start();
		Thread.sleep(500);
		new Thread(new ClientThread()).start();
		Thread.sleep(500);
		new Thread(new ClientThread()).start();
		Thread.sleep(500);
		new Thread(new ClientThread()).start();
		
	}
	
	private static class ServerThread implements Runnable{

		@Override
		public void run() {
			MoverServer s;
			try {
				s = new MoverServer(1337);
				s.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private static class ClientThread implements Runnable{

		@Override
		public void run() {
			MoverClient c;
			try {
				c = new MoverClient("localhost", 1337);
				c.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
}
