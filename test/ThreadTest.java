package test;

import examples.simplemovement.mvc.MoverPlane;
import threading.GameLoop;

public class ThreadTest {
	public static int a;
	
	public static void main(String[] args){
		MoverPlane mover = new MoverPlane();
		GameLoop<MoverPlane> loop = new GameLoop<MoverPlane>(mover, 16);
		Thread gameThread = new Thread(loop);
		System.out.println("BEFORE THREAD");
		gameThread.start();
		System.out.println("AFTER THREAD");
	}
}
