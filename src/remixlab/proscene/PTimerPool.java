package remixlab.proscene;

import remixlab.remixcam.util.Taskable;
import remixlab.remixcam.util.TimerPool;

public class PTimerPool extends TimerPool {
	@Override
	public void init() {
		for (Taskable t : timerPool()) {
			if (!t.isTimerInit())
				init(t, new PTimerWrap(t));
		}
	}
}
