package test;

public class ThreadTest {
	public static int a;
	
	public static void main(String[] args){
		while(true){
			Thread m = new Thread();
			m.start();
			if(a % 100 == 0){
				System.out.println(a);
			}
		}
	}
	
	public class PooPooTest extends Thread{
		
		@Override
		public void run(){
			while(true){
				a += 1;
			}
		}
	}
}
