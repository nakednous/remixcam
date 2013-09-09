package remixlab.tersehandling.timing;

public interface Animatable {
	void animate();
	boolean externalAnimation();
	long animationPeriod();
	void setAnimationPeriod(long period);
	void setAnimationPeriod(long period, boolean restart);
	void stopAnimation();
	void startAnimation();
	void restartAnimation();
	void toggleAnimation();
	boolean animationIsStarted();
	void setTimingHandler(TimingHandler h);
	TimingHandler timingHandler();
	SeqTimer timer();
}
