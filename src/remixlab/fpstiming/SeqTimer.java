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
