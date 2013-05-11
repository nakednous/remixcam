/**
 *                     RemixCam (version 1.0.0)      
 *      Copyright (c) 2012 by National University of Colombia
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

package remixlab.remixcam.core;

import remixlab.remixcam.geom.Point;

/**
 * Base class for mouse grabbers objects that implements the MouseGrabbable interface.
 * <p>
 * If you want to implement your own MouseGrabber objects you should derive from this
 * class (instead of implementing the MouseGrabbable interface), and implement the
 * {@link #checkIfGrabsDevice(int, int, Camera)} method and some of the provided
 * callback methods, such {@link #wheelMoved(int, Camera)}. 
 * <p>
 * <b>Note:</b> The InteractiveFrame object implements the MouseGrabbable interface.
 */
public class DeviceGrabber implements DeviceGrabbable {
	protected AbstractScene scene;
	protected boolean grabsMouse;
	protected boolean keepsGrabbingMouse;
	
	/**
	 * The constructor takes a scene instance and 
	 * {@link remixlab.remixcam.core.AbstractScene#addInDeviceGrabberPool(DeviceGrabbable)}
	 * this MouseGrabber object.
	 * 
	 * @param scn Scene instance
	 */
	public DeviceGrabber(AbstractScene scn) {
		scene = scn;
		grabsMouse = false;
		keepsGrabbingMouse = false;
		scene.addInDeviceGrabberPool(this);		
	}
	
	/**
	 * Main class method. Current implementation is empty.
	 * 
	 * @see remixlab.remixcam.core.DeviceGrabbable#checkIfGrabsDevice(int, int, Camera)
	 */
	@Override
	public void checkIfGrabsCursor(int x, int y) { }

	/**
	 * Returns true when the MouseGrabbable grabs the Scene mouse events.
	 */
	@Override
	public boolean grabsCursor() {
		return grabsMouse;
	}

	/**
	 * Callback method called when the MouseGrabber {@link #grabsCursor()} and a mouse button is clicked.
	 * <p>
	 * Current implementation is empty.
	 */
	@Override
	public void buttonClicked(Integer button, int numberOfClicks) { }

	/**
	 * Callback method called when the MouseGrabber {@link #grabsCursor()} and the
	 * mouse is moved while a button is pressed.
	 * <p>
	 * Current implementation is empty.
	 */
	@Override
	public void performInteraction(Point eventPoint) { }

	/**
	 * Callback method called when the MouseGrabber {@link #grabsCursor()} and a
	 * mouse button is pressed. Once a mouse grabber grabs the mouse and the mouse is
	 * pressed the default implementation will return that the mouse grabber
	 * keepsGrabbingMouse even if the mouse grabber loses focus.
	 * <p>
	 * The previous behavior is useful when you are planning to implement a mouse
	 * pressed event followed by mouse released event, e.g., 
	 * <p>
	 * The body of your {@code mousePressed(Point pnt, Camera cam)} method should look like: <br>
	 * {@code   super.mousePressed(pnt, cam); //sets the class variable keepsGrabbingMouse to true} <br>
	 * {@code   myMousePressedImplementation;} <br>
	 * {@code   ...} <br>
	 * <p>
	 * The body of your {@code mouseReleased(Point pnt, Camera cam)} method should look like: <br>
	 * {@code   super.mouseReleased(pnt, cam); //sets the class variable keepsGrabbingMouse to false} <br>
	 * {@code   myMouseReleasedImplementation;} <br>
	 * {@code   ...} <br>
	 * <p>
	 * Finally, the body of your {@code checkIfGrabsMouse(int x, int y, Camera camera)} method should look like: <br>
	 * {@code   setGrabsMouse( keepsGrabbingMouse	|| myCheckCondition); //note the use of the class variable keepsGrabbingMouse} <br>
	 * {@code   ...} <br>
	 * 
	 * @see #endAction(Point, Camera)
	 */
	@Override
	public void beginInteraction(Point eventPoint) {
		if (grabsCursor())
			keepsGrabbingMouse = true;
	}

	/**
	 * Mouse release event callback method.
	 * <p>
	 */
	@Override
	public void endInteraction(Point eventPoint) {
		keepsGrabbingMouse = false;
	}

	/**
	 * Callback method called when the MouseGrabber {@link #grabsDevice()} and the
	 * mouse wheel is used.
	 * <p>
	 * Current implementation is empty.
	 */
	/**
	@Override
	public void wheelMoved(float rotation) { }
	*/
	
	/**
	 * Sets the {@link #grabsCursor()} flag. Normally used by
	 * {@link #checkIfGrabsDevice(int, int, Camera)}.
	 *  
	 * @param grabs flag
	 */
	@Override
	public void setGrabsCursor(boolean grabs) {
		grabsMouse = grabs;
	}
}