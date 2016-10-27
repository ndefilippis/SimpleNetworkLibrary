package test;

import java.util.LinkedList;
import java.util.Queue;

import netcode.RunnableLoop;

public class QueueTest {
	public static void main(String[] args) throws InterruptedException{
		Send s = new Send();
		new Thread(s).start();
		//Thread.sleep(100);
		while(true){
			Integer x;
			if((x = s.get()) != null){
				System.out.println(x);
			}
		}
	}
	
	private static class Send extends RunnableLoop{
		Queue<Integer> list = new LinkedList<Integer>();
		
		
		@Override
		public void update() {
			list.offer(30);
		}
		
		public Integer get(){
			return list.poll();
		}
		
	}
}
