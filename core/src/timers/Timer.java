package timers;

public class Timer {
	
	protected int endTime;
	protected int counter;
	
	public Timer(int endTime){
		this.endTime = endTime;
		counter = endTime + 1;
	}
	
	public void restart(){ counter = 0; }
	
	public void countUp(){ counter++; }
	
	public void countDown(){ counter--; }
	
	public void setEndTime(int endTime){ this.endTime = endTime; }
	
	public boolean timeUp(){ return (counter > endTime); }
	
	public boolean timeJustUp() { return (counter == endTime); }
	
	public int getCounter(){ return counter; }
	
	public int getEndTime(){ return endTime; }
	
	public void end(){ counter = endTime + 1; }

	public void moveCounterForward(int i) { counter += i; }
	
}