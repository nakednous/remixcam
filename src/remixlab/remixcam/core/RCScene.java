package remixlab.remixcam.core;

import java.util.List;

import remixlab.remixcam.util.*;

public abstract class RCScene {
  //M o u s e G r a b b e r
	protected MouseGrabberPool mouseGrabberPool;
	protected TimerPool timerPool;
	
	public RCScene() {
		mouseGrabberPool = new MouseGrabberPool();
	}	

	public TimerPool timerPool() {
		return timerPool;
	}
	
	/**
	 * Returns an object containing references to all the active MouseGrabbers.
	 * <p>
	 * Used to parse all the MouseGrabbers and to check if any of them
	 * {@link remixlab.proscene.MouseGrabbable#grabsMouse()} using
	 * {@link remixlab.proscene.MouseGrabbable#checkIfGrabsMouse(int, int, Camera)}.
	 * <p>
	 * Use {@link #removeFromMouseGrabberPool(MouseGrabbable)} and
	 * {@link #addInMouseGrabberPool(MouseGrabbable)} to modify this list.
	 */
	// TODO refactor the name
	public MouseGrabberPool mouseGrabberPoolObject() {
		return mouseGrabberPool;
	}
	
	//TODO: document me
	public List<MouseGrabbable> mouseGrabberPool() {
		return mouseGrabberPool.mouseGrabberPool();
	}
	
	public MouseGrabbable mouseGrabber() {
		return mouseGrabberPoolObject().mouseGrabber();
	}	
	
	public boolean mouseGrabberIsAnIFrame() {
		return mouseGrabberPoolObject().mouseGrabberIsAnIFrame();
	}
	
	public boolean mouseGrabberIsADrivableFrame() {
		return mouseGrabberPoolObject().mouseGrabberIsADrivableFrame();
	}
	
  // 3. Mouse grabber handling
	
	public boolean isInMouseGrabberPool(MouseGrabbable mouseGrabber) {
		return mouseGrabberPoolObject().isInMouseGrabberPool(mouseGrabber);
	}
	
	public void addInMouseGrabberPool(MouseGrabbable mouseGrabber) {
		mouseGrabberPoolObject().addInMouseGrabberPool(mouseGrabber);
	}

	public void removeFromMouseGrabberPool(MouseGrabbable mouseGrabber) {
		mouseGrabberPoolObject().removeFromMouseGrabberPool(mouseGrabber);
	}

	public void clearMouseGrabberPool() {
		mouseGrabberPoolObject().clearMouseGrabberPool();
	}
}
