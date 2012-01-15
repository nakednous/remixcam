package remixlab.remixcam.util;

import remixlab.remixcam.core.*;

public class SingleThreadedTaskableTimer extends SingleThreadedTimer {
	Taskable caller;
	
	public SingleThreadedTaskableTimer(AbstractScene scn, Taskable t) {
		super(scn);
		caller = t;
	}
	
	public Taskable timerJob() {
		return caller;
	}
	
	@Override
	public void cancel() {
		super.cancel();
		scene.unregisterFromTimerPool(this);
	}
	
	public boolean execute() {
		boolean result = isTrigggered();
		
		if(result) {
			caller.execute();
			if(runOnlyOnce)
				inactivate();		
		}
		
		return result;
	}	
}
