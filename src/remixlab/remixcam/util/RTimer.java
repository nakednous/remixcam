package remixlab.remixcam.util;

/**
public interface RTimer<K> {
	public void createTimer(K object);
*/
public interface RTimer {
	//public void scheduleTimer(RTimerTask task, long delay, long period); 
	public void runTimer(long period);
	public void cancelTimer();
}
