package test;

import simplemovement.GameLoop;
import simplemovement.mvc.MoverPlane;

public class ThreadTest {
	public static int a;
	
	public static void main(String[] args){
		MoverPlane mover = new MoverPlane();
		GameLoop loop = new GameLoop(mover);
		Thread gameThread = new Thread(loop);
		System.out.println("BEFORE THREAD");
		gameThread.start();
		System.out.println("AFTER THREAD");
	}
}
