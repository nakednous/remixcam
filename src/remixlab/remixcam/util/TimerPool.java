package remixlab.remixcam.util;

import java.util.ArrayList;
import java.util.List;

public abstract class TimerPool {
	//to be implemented as a hashtable
	
	protected List<Taskable> timerPool;	

	public boolean needInit() {
		for (Taskable t : timerPool())
			if (!t.isTimerInit())
				return true;
		return false;
	}
	
	public List<Taskable> timerPool() {
		return timerPool;
	}	
	
	public TimerPool() {
		timerPool = new ArrayList<Taskable>();
	}
	
	public void registerInTimerPool(Taskable t, boolean reinit) {
		if ( timerPool.contains(t) )
			return;
		timerPool.add(t);
		if(reinit)
			t.setTimer(null);
	}
	
	public void registerInTimerPool(Taskable t) {
		if ( timerPool.contains(t) )
			return;
		timerPool.add(t);
	}
	
	public RTimer timer(Taskable t) {
		int index = timerPool.lastIndexOf(t);
		if( index != -1 )
			return timerPool.get(index).timer();
		return null;
	}
	
	public void unregisterFromTimerPool(Taskable t) {
		timerPool.remove(t);
	}
	
	public boolean isRegisteredInTimerPool(Taskable t) {
		return timerPool().contains(t);
	}
	
	public void clearTimerPool() {
		timerPool().clear();
	}
	
	public void init(Taskable t, RTimer timer) {
		t.setTimer(timer);
	}
	
	public abstract void init();
}
