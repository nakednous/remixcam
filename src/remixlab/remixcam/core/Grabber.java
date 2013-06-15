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

package remixlab.remixcam.core;

import remixlab.remixcam.event.GenericEvent;

public class Grabber implements Grabbable {
	protected AbstractScene scene;
  protected boolean grabsCursor;
  public boolean keepsGrabbingCursor;
  
  /**
   * The constructor takes a scene instance and
   * {@link remixlab.proscene.Scene#addInMouseGrabberPool(MouseGrabbable)} this MouseGrabber object.
   * 
   * @param scn Scene instance
   */
	public Grabber(AbstractScene scn) {
		scene = scn;
    grabsCursor = false;
    keepsGrabbingCursor = false;
    scene.addInDeviceGrabberPool(this);     
	}

	@Override
	public void checkIfGrabsInput() {}

	@Override
	public boolean grabsInput() {
		return grabsCursor;
	}

	@Override
	public void setGrabsInput(boolean grabs) {
		grabsCursor = grabs;		
	}

	/**
   * Callback method called when the MouseGrabber {@link #grabsMouse()} and a
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
   * {@code   setGrabsMouse( keepsGrabbingMouse   || myCheckCondition); //note the use of the class variable keepsGrabbingMouse} <br>
   * {@code   ...} <br>
   * 
   * @see #mouseReleased(Point, Camera)
   */
	@Override
	public void performInteraction(GenericEvent<?> motionEvent) {
		if (grabsInput()) keepsGrabbingCursor = true;		
	}

}
