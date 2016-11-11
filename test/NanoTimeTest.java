package test;

public class NanoTimeTest {
	private long time;
	private Timer lastTimer;
	private boolean timeSet;
	
	public NanoTimeTest(){
		time = 0;
		timeSet = false;
	}
	
	public static void main(String[] args){
		NanoTimeTest t = new NanoTimeTest();
		new Thread(new Timer(t)).start();
		new Thread(new Timer(t)).start();
	}
	
	public void setTime(long time, Timer t){
		if(lastTimer != t){
			if(timeSet){
				System.out.println(time - this.time);
				this.time = 0;
				timeSet = false;
			}
			else{
				this.time = time;
				timeSet = true;
			}
			lastTimer = t;
		}
	}
	
	private static class Timer implements Runnable{
		NanoTimeTest nano;
		
		public Timer(NanoTimeTest nano){
			this.nano = nano;
		}
		
		

		@Override
		public void run() {
			while(true){
				nano.setTime(System.nanoTime(), this);
			}
		}
	}
}
