package remixlab.remixcam.util;

import remixlab.remixcam.core.*;

//import remixlab.proscene.*;

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
	
	public void cancel() {
		stop();
	}	

	public void create() {
		inactivate();		
	}
	
	public void run(long period) {
		run(period, false);
	}	

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

	public void stop() {
		inactivate();
	}
	
	// others
	
	public void inactivate() {  	
  	active = false;
  }
	
	public boolean isActive() {
		return active;
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