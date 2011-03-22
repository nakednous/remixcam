package remixlab.remixcam.util;

public abstract class TimerJob implements Taskable {
	protected RTimer tmr;
	
	public RTimer timer() {
		return tmr;
	}
	
	public void setTimer(RTimer t) {
		tmr = t;
	}
}
