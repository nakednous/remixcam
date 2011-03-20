package remixlab.remixcam.util;

import java.util.*;

public class RTimerWrap implements RTimer {
	Timer timer;
	TimerTask timerTask;
	RTimerCommand caller;
	
	public RTimerWrap(RTimerCommand o) {
		caller = o;
	}
  
  public void runTimer(long period) {
  	if(timer != null) {
			timer.cancel();
			timer.purge();
		}
		timer=new Timer();
		TimerTask timerTask = new TimerTask() {
			public void run() {
				caller.execute();
			}
		};
		timer.scheduleAtFixedRate(timerTask, 0, period);
  }
  
  public void cancelTimer() {
  	if(timer != null) {
			timer.cancel();
			timer.purge();
		}
  }
}
