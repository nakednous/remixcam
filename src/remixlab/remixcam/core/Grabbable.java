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

import remixlab.remixcam.event.*;

/**
 * Interface for objects that grab mouse focus in a Scene.
 * <p>
 * MouseGrabber are objects which react to the mouse cursor, usually when it
 * hovers over them.
 * <p>
 * <h3>How does it work ?</h3>
 * All the created MouseGrabbers are grouped in a mouse grabber pool. The Scene
 * parses this pool, calling all the MouseGrabbers'
 * {@link #checkIfGrabsDevice(int, int, Camera)} methods that
 * {@link #setGrabsInput(boolean)} if desired (method calls should actually be
 * performed on concrete class instances such as InteractiveFrame).
 * <p>
 * When a MouseGrabber {@link #grabsInput()}, it becomes the 
 * {@link remixlab.remixcam.core.AbstractScene#deviceGrabber()}. All the mouse events are then
 * transmitted to it instead of being normally processed. This continues while
 * {@link #grabsInput()} (updated using
 * {@link #checkIfGrabsDevice(int, int, Camera)}) returns {@code true}.
 * <p>
 * If you want to (temporarily) disable a specific MouseGrabbers, you can remove
 * it from this pool using 
 * {@link remixlab.remixcam.core.AbstractScene#removeFromDeviceGrabberPool(DeviceGrabbable)}.
 */
public interface Grabbable {
	/**
	 * Called by the Scene before it tests if the MouseGrabber
	 * {@link #grabsInput()}. Should {@link #setGrabsInput(boolean)} according to
	 * the mouse position.
	 * <p>
	 * This is the core method of the MouseGrabber. Its goal is to update the
	 * {@link #grabsInput()} flag according to the mouse and MouseGrabber current
	 * positions, using {@link #setGrabsInput(boolean)}.
	 * <p>
	 * {@link #grabsInput()} is usually set to {@code true} when the mouse cursor
	 * is close enough to the MouseGrabber position. It should also be set to
	 * {@code false} when the mouse cursor leaves this region in order to release
	 * the mouse focus.
	 * <p>
	 * {@code x} and {@code y} are the mouse cursor coordinates ((0,0) corresponds
	 * to the upper left corner).
	 * <p>
	 * A typical implementation will look like:
	 * <p>
	 * {@code // (posX,posY) is the position of the MouseGrabber on screen.} <br>
	 * {@code // Here, distance to mouse must be less than 10 pixels to activate
	 * the MouseGrabber.} <br>
	 * {@code setGrabsMouse( PApplet.sqrt((x-posX)*(x-posX) + (y-posY)*(y-posY)) <
	 * 10);} <br>
	 * <p>
	 * If the MouseGrabber position is defined in 3D, use the {@code camera}
	 * parameter, corresponding to the calling Scene Camera. Project on screen and
	 * then compare the projected coordinates:
	 * <p>
	 * {@code PVector proj = new PVector
	 * (camera.projectedCoordinatesOf(myMouseGrabber.frame().position());} <br>
	 * {@code setGrabsMouse((PApplet.abs(x-proj.x) < 5) && (PApplet.(y-proj.y) <
	 * 2)); // Rectangular region} <br>
	 */
	void checkIfGrabsInput();

	/**
	 * Should return true when the MouseGrabbable grabs the Scene mouse events.
	 */
	boolean grabsInput();
	
	/**
	 * Should sets the {@link #grabsInput()} flag. Normally used by
	 * {@link #checkIfGrabsDevice(int, int, Camera)}.
	 *  
	 * @param grabs flag
	 */
	void setGrabsInput(boolean grabs);
	
	/**
	 * Callback method called when the MouseGrabber {@link #grabsInput()} and the
	 * mouse is moved while a button is pressed.
	 * <p>
	 * This method will typically update the state of the MouseGrabber from the
	 * mouse displacement. See the {@link #initAction(Point, Camera)}
	 * documentation for details.
	 */
	void performInteraction(GenericEvent<?> motionEvent);
}
