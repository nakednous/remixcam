package remixlab.tersehandling.timing;

import java.util.ArrayList;

public class TimingHandler {
	//protected boolean singleThreadedTaskableTimers;
  //T i m e r P o o l
	protected ArrayList<AbstractTimerJob> timerPool;
	protected long frameCount;
	protected float frameRate;
	protected long frameRateLastNanos;
	
  //A N I M A T I O N
	protected ArrayList<Animatable> animationPool;
	
	public TimingHandler() {
		frameCount = 0;
		frameRate = 10;
		frameRateLastNanos = 0;
		//drawing timer pool
		timerPool = new ArrayList<AbstractTimerJob>();
		animationPool = new ArrayList<Animatable>();
	}
	
	public TimingHandler(Animatable aObject) {
		this();
		this.registerAnimatable(aObject);
	}
	
	public void handle() {
		updateFrameRate();
		for ( AbstractTimerJob tJob : timerPool )
			if (tJob.timer() != null)
				if (tJob.timer() instanceof SeqTaskableTimer)
					((SeqTaskableTimer)tJob.timer()).execute();
		// Animation
		for ( Animatable aObj : animationPool )
			if( aObj.animationIsStarted() )
				if( aObj.timer().isTrigggered() )
					if(!aObj.externalAnimation())
						aObj.animate();
	}
	
	public ArrayList<AbstractTimerJob> timerPool() {
		return timerPool;
	}
	
	public void registerJob(AbstractTimerJob job) {
		job.setTimer(new SeqTaskableTimer(this, job));
		timerPool.add(job);
	}
	
	public void registerJob(AbstractTimerJob job, Timable timer) {
		job.setTimer(timer);
		timerPool.add(job);
	}
	
	public void unregisterJob(SeqTaskableTimer t) {		
			timerPool.remove( t.timerJob() );
	}
	
	public void unregisterJob(AbstractTimerJob job) {
		timerPool.remove(job);		
	}  
	
	public boolean isJobRegistered(AbstractTimerJob job) {
		return timerPool.contains(job);
	}
	
	protected void updateFrameRate() {
		long now = System.nanoTime();		
		if(frameCount > 1) {
			// update the current frameRate
			double rate = 1000000.0 / ((now - frameRateLastNanos) / 1000000.0);
      float instantaneousRate = (float) rate / 1000.0f;
      frameRate = (frameRate * 0.9f) + (instantaneousRate * 0.1f);	     
		}			
		frameRateLastNanos = now;
		frameCount++;
	}
	
	/**
	 * Returns the approximate frame rate of the software as it executes.
	 * The initial value is 10 fps and is updated with each frame.
	 * The value is averaged (integrated) over several frames.
	 * As such, this value won't be valid until after 5-10 frames.
	 */
	public float frameRate() {
		return frameRate;
	}
	//public abstract float frameRate();
	
	/**
	 * Returns the number of frames displayed since the program started.
	 */
	public long frameCount() {
		return frameCount;
	}
	
	public void restoreTimers() {
		//if( timersAreSingleThreaded() )	return;
		
		boolean isActive;
		
		for ( AbstractTimerJob job : timerPool ) {
			long period = 0;
			boolean rOnce = false;
			isActive = job.isActive();
			if(isActive) {
				period = job.period();
				rOnce = job.timer().isSingleShot();
			}
			job.stop();
			job.setTimer(new SeqTaskableTimer(this, job));			
			if(isActive) {
				if(rOnce)
					job.runOnce(period);
				else
					job.run(period);
			}
		}
	
		System.out.println("single threaded timers set");
	}
	
	// Animation -->
	
	public ArrayList<Animatable> animationPool() {
		return animationPool;
	}
	
	public void registerAnimatable(Animatable object) {
		if( object.timingHandler() != this )
			object.setTimingHandler(this);
		animationPool.add(object);
	}
	
	public void unregisterAnimatable(Animatable object) {		
		animationPool.remove( object );
	} 
	
	public boolean isAnimatableRegistered(Animatable object) {
		return animationPool.contains(object);
	}
}
