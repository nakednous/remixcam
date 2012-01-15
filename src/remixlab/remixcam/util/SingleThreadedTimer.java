/**
 *                     RemixCam (version 1.0.0)      
 *      Copyright (c) 2012 by National University of Colombia
 *                 @author Jean Pierre Charalambos      
 *              https://github.com/nakednous/remixcam
 *                           
 * This java library provides classes to ease the creation of interactive 3D
 * scenes in various languages and frameworks such as JOGL, WebGL and Processing.
 * 
 * This source file is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option)
 * any later version.
 * 
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 * 
 * A copy of the GNU General Public License is available on the World Wide Web
 * at <http://www.gnu.org/copyleft/gpl.html>. You can also obtain it by
 * writing to the Free Software Foundation, 51 Franklin Street, Suite 500
 * Boston, MA 02110-1335, USA.
 */

package remixlab.remixcam.util;

import remixlab.remixcam.core.*;

/**
 * this timer should belong to all P5 versions. Don't know yet if it
 * should belong to jogl too.
 * 
 * @author pierre
 *
 */
public class SingleThreadedTimer implements Timable {
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result + (int) (counter ^ (counter >>> 32));
		result = prime * result + (int) (period ^ (period >>> 32));
		result = prime * result + (runOnlyOnce ? 1231 : 1237);
		result = prime * result + (int) (startTime ^ (startTime >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SingleThreadedTimer other = (SingleThreadedTimer) obj;
		if (active != other.active)
			return false;
		if (counter != other.counter)
			return false;
		if (period != other.period)
			return false;
		if (runOnlyOnce != other.runOnlyOnce)
			return false;
		if (startTime != other.startTime)
			return false;
		return true;
	}

	protected AbstractScene scene;
	protected boolean active;
	protected boolean runOnlyOnce;
	private long counter;
	private long period;
	private long startTime;	
	
	public SingleThreadedTimer(AbstractScene scn) {
		scene = scn;
		create();
	}
	
	@Override
	public void cancel() {
		stop();
	}	

	@Override
	public void create() {
		inactivate();		
	}
	
	@Override
	public void run(long period) {
		run(period, false);
	}	

	@Override
	public void runOnce(long period) {
		run(period, true);
	}
	
	protected void run(long p, boolean rOnce) {
		if(p <= 0)
  		return;
  	
  	inactivate(); 	
  	
  	period = p;
  	counter = 1;
  	
  	active = true;
  	runOnlyOnce = rOnce;
  	
  	startTime = System.currentTimeMillis();
	}

	@Override
	public void stop() {
		inactivate();
	}
	
	@Override
	public boolean isActive() {
		return active;
	}
	
	// others
	
	public void inactivate() {  	
  	active = false;
  }	
	
	public boolean isTrigggered() {
  	if(!active)
  		return false;	 	 
  	  	
  	long elapsedTime = System.currentTimeMillis() - startTime;  	
  	
  	float timePerFrame = (1 / scene.frameRate()) * 1000;  	
  	long threshold = counter * period;  	
  	
  	boolean result = false;
  	if( threshold >= elapsedTime) {
  		long diff = elapsedTime + (long)timePerFrame - threshold;
  		if( diff >= 0) {
  			if( ( threshold - elapsedTime) <  diff ) {		
  				result = true;
  			}
  		}
  	}
  	else {  		
  		result = true;
  	}
  	
  	if(result) {
  		counter++;
  		if( period < timePerFrame )
  		System.out.println("Your current frame rate (~" + scene.frameRate() + " fps) is not high enough " +
          "to run the timer and reach the specified " + period + " ms period, " + timePerFrame
          + " ms period will be used instead. If you want to sustain a lower timer " +
          "period, define a higher frame rate (minimum of " + 1000f/period + " fps) " +
          "before running the timer (you may need to simplify your drawing to achieve it.");
  	}
  	
  	return result;  	
	}
}