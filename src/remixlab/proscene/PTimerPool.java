package remixlab.proscene;

import java.util.List;

import remixlab.remixcam.util.TimerJob;
import remixlab.remixcam.util.TimerPool;

public class PTimerPool extends TimerPool {
	@Override
	public void init() {
		for (List<TimerJob> list : timerPool.values())
			for ( TimerJob e : list )
				if ( e.timer() == null )
					e.setTimer(new PTimerWrap(e));
	}
}
