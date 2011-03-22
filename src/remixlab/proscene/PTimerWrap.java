package remixlab.proscene;

import java.util.*;

import remixlab.remixcam.util.RTimer;
import remixlab.remixcam.util.Taskable;

public class PTimerWrap implements RTimer {
	Timer timer;
	TimerTask timerTask;
	Taskable caller;
	
	public PTimerWrap(Taskable o) {
		caller = o;
		createTimer();
	}	
	
	public void createTimer() {
		if(timer != null) {
			timer.cancel();
			timer.purge();
		}
		timer=new Timer();
		timerTask = new TimerTask() {
			public void run() {
				caller.execute();
			}
		};
	}
  
  public void runTimer(long period) {
  	createTimer();
		timer.scheduleAtFixedRate(timerTask, 0, period);
  }
  
  public void runTimerOnce(long period) {
  	createTimer();
		timer.schedule(timerTask, period);
  }
  
  public void cancelTimer() {
  	if(timer != null) {
			timer.cancel();
			timer.purge();
		}
  }
}
