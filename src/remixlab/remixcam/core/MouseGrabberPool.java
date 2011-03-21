package remixlab.remixcam.core;

import java.util.ArrayList;
import java.util.List;

public class MouseGrabberPool {
  //M o u s e G r a b b e r
	protected List<MouseGrabbable> mouseGrabberPool;
	MouseGrabbable mouseGrbbr;
	boolean mouseGrabberIsAnIFrame;
	boolean mouseGrabberIsADrivableFrame;
	
	public MouseGrabberPool() {
		//mouse grabber pool
		mouseGrabberPool = new ArrayList<MouseGrabbable>();
		mouseGrabberIsAnIFrame = false;
		mouseGrabberIsADrivableFrame = false;
	}	
	
	/**
	 * Returns a list containing references to all the active MouseGrabbers.
	 * <p>
	 * Used to parse all the MouseGrabbers and to check if any of them
	 * {@link remixlab.remixcam.core.MouseGrabbable#grabsMouse()} using
	 * {@link remixlab.remixcam.core.MouseGrabbable#checkIfGrabsMouse(int, int, Camera)}.
	 * <p>
	 * You should not have to directly use this list. Use
	 * {@link #removeFromMouseGrabberPool(MouseGrabbable)} and
	 * {@link #addInMouseGrabberPool(MouseGrabbable)} to modify this list.
	 */
	public List<MouseGrabbable> mouseGrabberPool() {
		return mouseGrabberPool;
	}	

	/**
	 * Returns the current MouseGrabber, or {@code null} if none currently grabs
	 * mouse events.
	 * <p>
	 * When {@link remixlab.remixcam.core.MouseGrabbable#grabsMouse()}, the different
	 * mouse events are sent to it instead of their usual targets (
	 * {@link #camera()} or {@link #interactiveFrame()}).
	 */
	public MouseGrabbable mouseGrabber() {
		return mouseGrbbr;
	}

	/**
	 * Directly defines the {@link #mouseGrabber()}.
	 * <p>
	 * You should not call this method directly as it bypasses the
	 * {@link remixlab.remixcam.core.MouseGrabbable#checkIfGrabsMouse(int, int, Camera)}
	 * test performed by {@link #mouseMoved(MouseEvent)}.
	 */
	public void setMouseGrabber(MouseGrabbable mouseGrabber, Camera camera) {
		mouseGrbbr = mouseGrabber;

		mouseGrabberIsAnIFrame = mouseGrabber instanceof InteractiveFrame;
		mouseGrabberIsADrivableFrame = ((mouseGrabber != camera.frame()) && (mouseGrabber instanceof InteractiveDrivableFrame));
	}
	
	// 3. Mouse grabber handling
	
	/**
	 * Returns true if the mouseGrabber is currently in the {@link #mouseGrabberPool()} list.
	 * <p>
	 * When set to false using {@link #removeFromMouseGrabberPool(MouseGrabbable)}, the Scene no longer
	 * {@link remixlab.remixcam.core.MouseGrabbable#checkIfGrabsMouse(int, int, Camera)} on this mouseGrabber.
	 * Use {@link #addInMouseGrabberPool(MouseGrabbable)} to insert it back.
	 */
	public boolean isInMouseGrabberPool(MouseGrabbable mouseGrabber) {
		return mouseGrabberPool().contains(mouseGrabber);
	}
	
	/**
	 * Adds the mouseGrabber in the {@link #mouseGrabberPool()}.
	 * <p>
	 * All created InteractiveFrames (which are MouseGrabbers) are automatically added in the
	 * {@link #mouseGrabberPool()} by their constructors. Trying to add a
	 * mouseGrabber that already {@link #isInMouseGrabberPool(MouseGrabbable)} has no effect.
	 * <p>
	 * Use {@link #removeFromMouseGrabberPool(MouseGrabbable)} to remove the mouseGrabber from
	 * the list, so that it is no longer tested with
	 * {@link remixlab.remixcam.core.MouseGrabbable#checkIfGrabsMouse(int, int, Camera)}
	 * by the Scene, and hence can no longer grab mouse focus. Use
	 * {@link #isInMouseGrabberPool(MouseGrabbable)} to know the current state of the MouseGrabber.
	 */
	public void addInMouseGrabberPool(MouseGrabbable mouseGrabber) {
		if (!isInMouseGrabberPool(mouseGrabber))
			mouseGrabberPool().add(mouseGrabber);
	}

	/**
	 * Removes the mouseGrabber from the {@link #mouseGrabberPool()}.
	 * <p>
	 * See {@link #addInMouseGrabberPool(MouseGrabbable)} for details. Removing a mouseGrabber
	 * that is not in {@link #mouseGrabberPool()} has no effect.
	 */
	public void removeFromMouseGrabberPool(MouseGrabbable mouseGrabber) {
		mouseGrabberPool().remove(mouseGrabber);
	}

	/**
	 * Clears the {@link #mouseGrabberPool()}.
	 * <p>
	 * Use this method only if it is faster to clear the
	 * {@link #mouseGrabberPool()} and then to add back a few MouseGrabbers
	 * than to remove each one independently.
	 */
	public void clearMouseGrabberPool() {
		mouseGrabberPool().clear();
	}
	
  public boolean mouseGrabberIsAnIFrame() {
  	return mouseGrabberIsAnIFrame;
	}
	
	public boolean mouseGrabberIsADrivableFrame() {
		return mouseGrabberIsADrivableFrame;
	}
}
