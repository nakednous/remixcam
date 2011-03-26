package remixlab.remixcam.util;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class TimerPool {
	public HashMap<Object, List<TimerJob>> timerPool;
	protected boolean needInit;
	
	public TimerPool() {
		timerPool = new HashMap<Object, List<TimerJob>>();
		needInit = false;
	}

	public boolean needInit() {
		/**
		if(!cached)
			for (List<TimerJob> list : timerPool.values())
				for ( TimerJob e : list )
					if ( e.timer() == null )
						needIt = true;
					else
						needIt = false;
						*/
		return needInit;
	}
	
	public HashMap<Object, List<TimerJob>> timerPool() {
		return timerPool;
	}	
	
	public void registerInTimerPool(Object o, TimerJob t) {		
		if( !timerPool.containsKey(o) ) {
			timerPool.put(o, new ArrayList<TimerJob>());
		} 
		if( !timerPool.get(o).contains(t) )
			timerPool.get(o).add(t);
		needInit = true;
	}
	
	public void unregisterFromTimerPool(Object t) {
		timerPool.remove(t);
	}
	
	public void unregisterFromTimerPool(Object o, TimerJob t) {
		if ( timerPool.get(o).contains(t) )
			timerPool.get(o).remove(t);
	}
	
	public void unregisterFromTimerPool(TimerJob t) {
		for (List<TimerJob> list : timerPool.values()) {
			if ( list.contains(t) ) {
				list.remove(t);
				return;
			}
		}
	}
	
	public boolean isRegisteredInTimerPool(Object o, TimerJob t) {
		if ( timerPool.get(o).contains(t) )
			return true;
		return false;
	}
	
	public boolean isRegisteredInTimerPool(TimerJob t) {
		//search the whole hash
		for (List<TimerJob> list : timerPool.values())
			if ( list.contains(t) )
				return true;
		return false;
	}
	
	public void clearTimerPool() {
		timerPool().clear();
	}
	
	public abstract void init();
}
