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

package remixlab.remixcam.device;

import java.util.ArrayList;
import java.util.List;

import remixlab.remixcam.core.*;
import remixlab.remixcam.event.*;

public abstract class AbstractDevice {
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
	
	/**
  //M o u s e G r a b b e r
	protected List<Grabbable> msGrabberPool;
	protected Grabbable deviceGrbbr;
	protected Actionable<?> lastDeviceGrbbrAction;
	public boolean deviceGrabberIsAnIFrame;//false by default, see: http://stackoverflow.com/questions/3426843/what-is-the-default-initialization-of-an-array-in-java
	protected boolean deviceTrckn;
	*/
	
	protected AbstractScene scene;
	protected String nm;
	protected List<Grabbable> grabbers;
	protected Grabbable deviceGrbbr;
	
	public AbstractDevice(AbstractScene scn, String n) {
		scene = scn;
		nm = n;
		grabbers = new ArrayList<Grabbable>();
		scene.registerDevice(this);		
	}
	
	public String name() {
		return nm;
	}
	
	public abstract void handle(GenericEvent<?> event);
	
	public GenericEvent<?> feed() {
		return null;
	}
	
	/**
	 * Returns a list containing references to all the active MouseGrabbers.
	 * <p>
	 * Used to parse all the MouseGrabbers and to check if any of them
	 * {@link remixlab.remixcam.core.Grabbable#grabsInput()} using
	 * {@link remixlab.remixcam.core.Grabbable#checkIfGrabsDevice(int, int, Camera)}.
	 * <p>
	 * You should not have to directly use this list. Use
	 * {@link #removeFromDeviceGrabberPool(Grabbable)} and
	 * {@link #addInDeviceGrabberPool(Grabbable)} to modify this list.
	 */
	public List<Grabbable> deviceGrabberPool() {
		return grabbers;
	}
	
	/**
	 * Removes the mouseGrabber from the {@link #deviceGrabberPool()}.
	 * <p>
	 * See {@link #addInDeviceGrabberPool(Grabbable)} for details. Removing a mouseGrabber
	 * that is not in {@link #deviceGrabberPool()} has no effect.
	 */
	public void removeFromDeviceGrabberPool(Grabbable deviceGrabber) {
		deviceGrabberPool().remove(deviceGrabber);
	}
	
	/**
	 * Clears the {@link #deviceGrabberPool()}.
	 * <p>
	 * Use this method only if it is faster to clear the
	 * {@link #deviceGrabberPool()} and then to add back a few MouseGrabbers
	 * than to remove each one independently.
	 */
	public void clearDeviceGrabberPool() {
		deviceGrabberPool().clear();
	}
	
	/**
	 * Returns true if the mouseGrabber is currently in the {@link #deviceGrabberPool()} list.
	 * <p>
	 * When set to false using {@link #removeFromDeviceGrabberPool(Grabbable)}, the Scene no longer
	 * {@link remixlab.remixcam.core.Grabbable#checkIfGrabsDevice(int, int, Camera)} on this mouseGrabber.
	 * Use {@link #addInDeviceGrabberPool(Grabbable)} to insert it back.
	 */
	public boolean isInDeviceGrabberPool(Grabbable deviceGrabber) {
		return deviceGrabberPool().contains(deviceGrabber);
	}
	
	/**
	 * Returns the current MouseGrabber, or {@code null} if none currently grabs
	 * mouse events.
	 * <p>
	 * When {@link remixlab.remixcam.core.Grabbable#grabsInput()}, the different
	 * mouse events are sent to it instead of their usual targets (
	 * {@link #pinhole()} or {@link #interactiveFrame()}).
	 */
	public Grabbable deviceGrabber() {
		return deviceGrbbr;
	}
	
	/**
	 * Adds the mouseGrabber in the {@link #deviceGrabberPool()}.
	 * <p>
	 * All created InteractiveFrames (which are MouseGrabbers) are automatically added in the
	 * {@link #deviceGrabberPool()} by their constructors. Trying to add a
	 * mouseGrabber that already {@link #isInDeviceGrabberPool(Grabbable)} has no effect.
	 * <p>
	 * Use {@link #removeFromDeviceGrabberPool(Grabbable)} to remove the mouseGrabber from
	 * the list, so that it is no longer tested with
	 * {@link remixlab.remixcam.core.Grabbable#checkIfGrabsDevice(int, int, Camera)}
	 * by the Scene, and hence can no longer grab mouse focus. Use
	 * {@link #isInDeviceGrabberPool(Grabbable)} to know the current state of the MouseGrabber.
	 */
	//TODO shoud be overriden in implementing classes
	//public abstract boolean addInDeviceGrabberPool(Grabbable deviceGrabber);	
	//Default implementation is nice in some cases
	public boolean addInDeviceGrabberPool(Grabbable deviceGrabber) {
		if(deviceGrabber == null)
			return false;
		if( !(deviceGrabber instanceof InteractiveCameraFrame) )
			if (!isInDeviceGrabberPool(deviceGrabber)) {
				deviceGrabberPool().add(deviceGrabber);
				return true;
			}
		return false;
	}
	
	/**
	public void addInDeviceGrabberPool(Grabbable deviceGrabber) {
		if (!isInDeviceGrabberPool(deviceGrabber))
			deviceGrabberPool().add(deviceGrabber);
	}
	*/
	
	/**
	 * Directly defines the {@link #deviceGrabber()}.
	 * <p>
	 * You should not call this method directly as it bypasses the
	 * {@link remixlab.remixcam.core.Grabbable#checkIfGrabsDevice(int, int, Camera)}
	 * test performed by parsing the mouse moved event.
	 */
	public void setDeviceGrabber(Grabbable deviceGrabber) {
		if( deviceGrabber == null )
			deviceGrbbr = null;
		else
			if( isInDeviceGrabberPool(deviceGrabber) )
				deviceGrbbr = deviceGrabber;

		//deviceGrabberIsAnIFrame = deviceGrabber instanceof InteractiveFrame;
	}
	
	//procedure
	public boolean updateGrabber(GenericEvent<?> event) {
		if( event == null )
			return false;
		
	  // fortunately selection mode doesn't need parsing
		if( event.getAction() != null ) {
			if(event.getAction().action() == event.getAction().selectionAction() ||
				 event.getAction().action() == event.getAction().deselectionAction()) {
				setDeviceGrabber(null);
				if(event.getAction().action() == event.getAction().selectionAction()) {
					for (Grabbable mg : deviceGrabberPool()) {
						// take whatever. Here the last one
						mg.checkIfGrabsInput(event);
						if (mg.grabsInput()) setDeviceGrabber(mg);
					}
				}				
				return true;
			}
			return false;
		}
		return false;
		
		/**
		if( event == null )
			return false;
		
	  // fortunately selection mode doesn't need parsing
		if( event.getAction() != null ) {
			if(event.getAction().action() == event.getAction().deselectionAction()) {
				setDeviceGrabber(null);
				return true;
			}				
			if(event.getAction().action() == event.getAction().selectionAction()) {
				setDeviceGrabber(null);
				for (Grabbable mg : deviceGrabberPool()) {
					// take whatever. Here the last one
					mg.checkIfGrabsInput(event);
					if (mg.grabsInput()) setDeviceGrabber(mg);
				}
				if(this.deviceGrabber() != null)
					return true;
				return false;
			}
	  }
		return false;
		// */
	}
}
