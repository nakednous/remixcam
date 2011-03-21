package remixlab.remixcam.util;

public interface Taskable {
	public void registerInTimerPool();
	public void execute();
	public RTimer timer();
	public void setTimer(RTimer timer);
	public boolean isTimerInit();
}
