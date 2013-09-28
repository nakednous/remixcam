/*******************************************************************************
 * FPSTiming (version 0.9.50)
 * Copyright (c) 2013 Jean Pierre Charalambos.
 * @author Jean Pierre Charalambos
 * https://github.com/remixlab
 *   
 * All rights reserved. Library that eases the creation of interactive
 * scenes released under the terms of the GNU Public License v3.0
 * which is available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package remixlab.fpstiming;

/**
 * A sequential timer holding a {@link #timerJob()} which defines
 * its callback method.
 */
public class SeqTaskableTimer extends SeqTimer {
	Taskable task;

	public SeqTaskableTimer(TimingHandler h, Taskable t) {
		super(h);
		task = t;
	}

	/**
	 * Returns the object defining the timer callback method.
	 */
	public Taskable timerJob() {
		return task;
	}

	@Override
	public void cancel() {
		super.cancel();
		handler.unregisterJob(this);
	}

	/**
	 * Executes the callback method defined by the {@link #timerJob()}.
	 * <p>
	 * <b>Note:</b> You should not call this method since it's done by the
	 * timing handler (see {@link remixlab.fpstiming.TimingHandler#handle()}).
	 */
	public boolean execute() {
		boolean result = isTrigggered();

		if (result) {
			task.execute();
			if (runOnlyOnce)
				inactivate();
		}

		return result;
	}
}
