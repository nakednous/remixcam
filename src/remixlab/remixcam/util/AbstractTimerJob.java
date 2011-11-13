package remixlab.remixcam.util;

public abstract class AbstractTimerJob implements Taskable {
	protected Timable tmr;	
	
	public Timable timer() {
		return tmr;
	}
	
	public void setTimer(Timable t) {
		tmr = t;
	}
	
	//Wrappers	
	
	public void run(long period) {
		if(timer()!=null) {
			timer().run(period);			
		}
	}
	
	public void runOnce(long period) {
		if(timer()!=null) {
			timer().runOnce(period);			
		} 
	}
	
	public void stop() {
		if(timer()!=null) {
			timer().stop();
		}
	}
	
	public void cancel() {
		if(timer()!=null) {
			timer().cancel();
		}
	}
	
	public void create() {
		if(timer()!=null) {
			timer().create();
		}
	}
}
