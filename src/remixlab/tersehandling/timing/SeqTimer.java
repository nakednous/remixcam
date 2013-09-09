/**
 *                     Dandelion (version 0.70.0)      
 *          Copyright (c) 2013 by Jean Pierre Charalambos
 *                 @author Jean Pierre Charalambos      
 *              https://github.com/nakednous/remixcam
 *                           
 * This library provides classes to ease the creation of interactive
 * frame-based, 2d and 3d scenes.
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

package remixlab.tersehandling.timing;

/**
 * @author pierre
 *
 */
public class SeqTimer implements Timable {	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result + (int) (counter ^ (counter >>> 32));
		result = prime * result + (int) (prd ^ (prd >>> 32));
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
		SeqTimer other = (SeqTimer) obj;
		if (active != other.active)
			return false;
		if (counter != other.counter)
			return false;
		if (prd != other.prd)
			return false;
		if (runOnlyOnce != other.runOnlyOnce)
			return false;
		if (startTime != other.startTime)
			return false;
		return true;
	}

	protected TimingHandler scene;
	protected boolean active;
	protected boolean runOnlyOnce;
	private long counter;
	private long prd;
	private long startTime;	
	
	public SeqTimer(TimingHandler scn) {
		this(scn, false);
	}
	
	public SeqTimer(TimingHandler scn, boolean singleShot) {
		scene = scn;
		runOnlyOnce = singleShot;
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
		prd = period;
		run();
	}	
	
	@Override
	public void run() {
		if(prd <= 0)
  		return;
  	
  	inactivate();  	
  	counter = 1;  	
  	active = true;  	
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
		/**
		prd = 0;
		runOnlyOnce = false;
		*/
  	active = false;
  }	
	
	public boolean isTrigggered() {
  	if(!active)
  		return false;	 	 
  	  	
  	long elapsedTime = System.currentTimeMillis() - startTime;  	
  	
  	float timePerFrame = (1 / scene.frameRate()) * 1000;  	
  	long threshold = counter * prd;  	
  	
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
  		/**
  		if( prd < timePerFrame )
  		System.out.println("Your current frame rate (~" + scene.frameRate() + " fps) is not high enough " +
          "to run the timer and reach the specified " + prd + " ms period, " + timePerFrame
          + " ms period will be used instead. If you want to sustain a lower timer " +
          "period, define a higher frame rate (minimum of " + 1000f/prd + " fps) " +
          "before running the timer (you may need to simplify your drawing to achieve it.)");
      // */
  	}
  	
  	return result;  	
	}

	@Override
	public long period() {
		return prd;
	}
	
	@Override
	public void setPeriod(long period) {
		prd = period;		
	}
	
	@Override
	public boolean isSingleShot() {
		return runOnlyOnce;
	}	

	@Override
	public void setSingleShot(boolean singleShot) {
		runOnlyOnce = singleShot;		
	}
}