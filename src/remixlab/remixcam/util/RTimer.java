package remixlab.remixcam.util;

public interface RTimer { 
	public void runTimer(long period);
	public void runTimerOnce(long period);
	public void cancelTimer();
	public void createTimer();
}
