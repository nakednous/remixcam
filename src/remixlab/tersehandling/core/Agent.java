/**
 *                  TerseHandling (version 0.70.0)      
 *           Copyright (c) 2013 by Jean Pierre Charalambos
 *                 @author Jean Pierre Charalambos      
 *              https://github.com/nakednous/remixcam
 *                           
 * This library provides classes to ease the creation of interactive scenes.
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

package remixlab.tersehandling.core;

import java.util.ArrayList;
import java.util.List;

import remixlab.tersehandling.event.*;

public class Agent {	
	/**
	protected Object handlerObject;	
	protected String handlerMethodName;
	*/
	
	/**
	 * Attempt to add a 'feed' handler method to the HIDevice. The default feed
	 * handler is a method that returns void and has one single HIDevice parameter.
	 * 
	 * @param obj the object to handle the feed
	 * @param methodName the method to execute the feed in the object handler class
	 * 
	 * @see #removeHandler()
	 * @see #invoke()
	 */
	/**
	public void addHandler(Object obj, String methodName) {
		AbstractScene.showMissingImplementationWarning("addHandler");
	}
	*/
	
	/**
	 * Unregisters the 'feed' handler method (if any has previously been added to
	 * the HIDevice).
	 * 
	 * @see #addHandler(Object, String)
	 * @see #invoke()
	 */
	/**
	public void removeHandler() {
		AbstractScene.showMissingImplementationWarning("removeHandler");
	}
	*/		
	
	protected TerseHandler handler;
	protected String nm;
	protected List<Grabbable> grabbers;
	protected Grabbable trackedGrabber;
	protected Grabbable defaultGrabber;
	
	//protected boolean enforcedGrabber;
	//public boolean deviceGrabberIsAnIFrame;//false by default, see: http://stackoverflow.com/questions/3426843/what-is-the-default-initialization-of-an-array-in-java
	protected boolean deviceTrckn;
	
	public Agent(TerseHandler hndlr, String n) {
		handler = hndlr;
		nm = n;
		grabbers = new ArrayList<Grabbable>();
		//enforcedGrabber = false;
		setTracking(true);
		handler.registerAgent(this);
	}
	
	public String name() {
		return nm;
	}
	
	/**
	* Returns {@code true}
	* if {@link remixlab.proscene.DesktopEvents#mouseMoved(processing.event.MouseEvent)}
	* is called even when no mouse button is pressed.
	* <p>
	* You need to setMouseTracking() to \c true in order to use MouseGrabber (see mouseGrabber()).
	*/
	public boolean isTracking() {
		return deviceTrckn;
	}
	
	public void enableTracking() {
		setTracking(true);
	}
	
	public void disableTracking() {
		setTracking(false);
	}

	/**
	* Sets the {@link #isTracking()} value.
	*/
	public void setTracking(boolean enable) {
		deviceTrckn = enable;
		if(!isTracking())
			setTrackedGrabber(null);
	}

	/**
	* Calls {@link #setTracking(boolean)} to toggle the {@link #isTracking()} value.
	*/
	public void toggleTracking() {
		setTracking(!isTracking());
	}
	
	public void updateGrabber(BaseEvent event) {
		if( event == null || !handler.isAgentRegistered(this) || !isTracking() )
			return;
		
		setTrackedGrabber(null);
		for (Grabbable mg : deviceGrabberPool()) {
			// take whatever. Here the first one
			if(mg.checkIfGrabsInput(event)) {
				setTrackedGrabber(mg);
				//System.out.println("oooops");
				return;
			}
		}
		return;
	}
	
	//just enqueue grabber
	public void handle(BaseEvent event) {
		if(event == null || !handler.isAgentRegistered(this)) return;
		handler.enqueueEventTuple(new EventGrabberTuple(event, grabber()));
	}
	
	public BaseEvent feed() {
		return null;
	}
	
	public TerseHandler terseHandler() {
		return handler;
	}
	
	/**
	 * Returns a list containing references to all the active MouseGrabbers.
	 * <p>
	 * Used to parse all the MouseGrabbers and to check if any of them
	 * {@link remixlab.tersehandling.core.Grabbable#grabsAgent()} using
	 * {@link remixlab.tersehandling.core.Grabbable#checkIfGrabsDevice(int, int, Camera)}.
	 * <p>
	 * You should not have to directly use this list. Use
	 * {@link #removeFromPool(Grabbable)} and
	 * {@link #addInPool(Grabbable)} to modify this list.
	 */
	public List<Grabbable> deviceGrabberPool() {
		return grabbers;
	}
	
	/**
	 * Removes the mouseGrabber from the {@link #deviceGrabberPool()}.
	 * <p>
	 * See {@link #addInPool(Grabbable)} for details. Removing a mouseGrabber
	 * that is not in {@link #deviceGrabberPool()} has no effect.
	 */
	public void removeFromPool(Grabbable deviceGrabber) {
		deviceGrabberPool().remove(deviceGrabber);
	}
	
	/**
	 * Clears the {@link #deviceGrabberPool()}.
	 * <p>
	 * Use this method only if it is faster to clear the
	 * {@link #deviceGrabberPool()} and then to add back a few MouseGrabbers
	 * than to remove each one independently.
	 */
	public void clearPool() {
		deviceGrabberPool().clear();
	}
	
	/**
	 * Returns true if the mouseGrabber is currently in the {@link #deviceGrabberPool()} list.
	 * <p>
	 * When set to false using {@link #removeFromPool(Grabbable)}, the Scene no longer
	 * {@link remixlab.tersehandling.core.Grabbable#checkIfGrabsDevice(int, int, Camera)} on this mouseGrabber.
	 * Use {@link #addInPool(Grabbable)} to insert it back.
	 */
	public boolean isInPool(Grabbable deviceGrabber) {
		return deviceGrabberPool().contains(deviceGrabber);
	}
	
	/**
	 * Returns the current MouseGrabber, or {@code null} if none currently grabs
	 * mouse events.
	 * <p>
	 * When {@link remixlab.tersehandling.core.Grabbable#grabsAgent()}, the different
	 * mouse events are sent to it instead of their usual targets (
	 * {@link #pinhole()} or {@link #interactiveFrame()}).
	 */
	public Grabbable trackedGrabber() {
		return trackedGrabber;
	}
	
	public Grabbable grabber() {
		if (trackedGrabber() != null)
			return trackedGrabber();
		else
			return defaultGrabber();
	}
	
	public Grabbable defaultGrabber() {
		return defaultGrabber;
	}
	
	public void setDefaultGrabber(Grabbable g) {
		defaultGrabber = g;
	}
	
	/**
	 * Adds the mouseGrabber in the {@link #deviceGrabberPool()}.
	 * <p>
	 * All created InteractiveFrames (which are MouseGrabbers) are automatically added in the
	 * {@link #deviceGrabberPool()} by their constructors. Trying to add a
	 * mouseGrabber that already {@link #isInPool(Grabbable)} has no effect.
	 * <p>
	 * Use {@link #removeFromPool(Grabbable)} to remove the mouseGrabber from
	 * the list, so that it is no longer tested with
	 * {@link remixlab.tersehandling.core.Grabbable#checkIfGrabsDevice(int, int, Camera)}
	 * by the Scene, and hence can no longer grab mouse focus. Use
	 * {@link #isInPool(Grabbable)} to know the current state of the MouseGrabber.
	 */
	//TODO shoud be overriden in implementing classes
	//public abstract boolean addInDeviceGrabberPool(Grabbable deviceGrabber);	
	//Default implementation is nice in some cases
	public boolean addInPool(Grabbable deviceGrabber) {
		if(deviceGrabber == null)
			return false;
		//if( !(deviceGrabber instanceof InteractiveCameraFrame) )
			if (!isInPool(deviceGrabber)) {
				deviceGrabberPool().add(deviceGrabber);
				return true;
			}
		return false;
	}
	
  /**
	 * Directly defines the {@link #trackedGrabber()}.
	 * <p>
	 * You should not call this method directly as it bypasses the
	 * {@link remixlab.tersehandling.core.Grabbable#checkIfGrabsDevice(int, int, Camera)}
	 * test performed by parsing the mouse moved event.
	 */
	/**
	protected boolean setTrackedGrabber(Grabbable deviceGrabber) {
		//if(!isTracking())	return false;
		if( deviceGrabber == null ) {
			trackedGrabber = null;
			return true;
		}
		else
			if( isInPool(deviceGrabber) ) {
				trackedGrabber = deviceGrabber;
				return true;
			}
		return false;
		//deviceGrabberIsAnIFrame = deviceGrabber instanceof InteractiveFrame;
	}
	*/
	private void setTrackedGrabber(Grabbable deviceGrabber) {
		//if(!isTracking())	return false;
		if( deviceGrabber == null ) {
			trackedGrabber = null;
		}
		else
			if( isInPool(deviceGrabber) ) {
				trackedGrabber = deviceGrabber;
			}
		//deviceGrabberIsAnIFrame = deviceGrabber instanceof InteractiveFrame;
	}
}
