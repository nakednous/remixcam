/*******************************************************************************
 * FPSTiming (version 0.9.50)
 * Copyright (c) 2013 Jean Pierre Charalambos.
 * @author Jean Pierre Charalambos
 * https://github.com/remixlab
 * 
 * All rights reserved. Library that eases the creation of interactive
 * scenes released under the terms of the GNU Public License v3.0
 * which available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package remixlab.fpstiming;

public class SeqTaskableTimer extends SeqTimer {
	Taskable caller;
	
	public SeqTaskableTimer(TimingHandler scn, Taskable t) {
		super(scn);
		caller = t;
	}
	
	public Taskable timerJob() {
		return caller;
	}
	
	@Override
	public void cancel() {
		super.cancel();
		scene.unregisterJob(this);
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
