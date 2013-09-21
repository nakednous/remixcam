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
			timer().setSingleShot(false);
			timer().run(period);			
		}
	}
	
	public void runOnce(long period) {
		if(timer()!=null) {
			timer().setSingleShot(true);
			timer().run(period);			
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
	
	public boolean isActive() {
		if(timer()!=null) {
			return timer().isActive();
		}
		return false;
	}
		
	public long period() {
		return timer().period();
	}
	
	/**
	public void setPeriod(long period) {
		timer().setPeriod(period);
	}
	
	public boolean isSingleShot() {
		return timer().isSingleShot();
	}
	
	public void setSingleShot(boolean singleShot) {
		timer().setSingleShot(singleShot);
	}
	*/
}
