/**
 *                     RemixCam (version 0.70.0)      
 *      Copyright (c) 2013 by National University of Colombia
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
