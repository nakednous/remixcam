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

package remixlab.remixcam.profile;

import remixlab.remixcam.core.*;
import remixlab.remixcam.geom.*;

/**
 * An HIDevice represents a Human Interface Device with (<=) 6 degrees of freedom.
 * <p>
 * An HIDevice has a type which can be RELATIVE (default) or ABSOLUTE. A RELATIVE HIDevice
 * has a neutral position that the device holds when it is not being manipulated. An ABSOLUTE HIDevice
 * has no such neutral position. Examples of RELATIVE devices are the space navigator and the joystick,
 * examples of ABSOLUTE devices are the wii or the kinect.
 */
public abstract class HIDeviceProfile extends DOF2Profile {	
	//public enum PointerMode {POINTER, POINTERLESS}	
	//protected PointerMode pmode;
	
	protected Vector3D rotation, rotSens;
	protected Vector3D translation, transSens;
	
	//absolute mode
	protected Vector3D prevRotation, prevTranslation;
	
	protected Vector3D t;
	protected Quaternion q;
	protected float tx;
  protected float ty;
  protected float tz;
	protected float roll;
  protected float pitch;
  protected float yaw;

	protected Quaternion quaternion;

	/**
	 * Main constructor.
	 * 
	 * @param scn The Scene object this HIDevice belongs to.
	 * @param m The device {@link #mode()}.
	 */
	public HIDeviceProfile(AbstractScene scn, String n) {
		super(scn, n);
		translation = new Vector3D();
		transSens = new Vector3D(1, 1, 1);
		rotation = new Vector3D();
		rotSens = new Vector3D(1, 1, 1);		
		quaternion = new Quaternion();
		t = new Vector3D();
    q = new Quaternion();
    tx = translation.vec[0] * transSens.vec[0];
    ty = translation.vec[1] * transSens.vec[1];
    tz = translation.vec[2] * transSens.vec[2];
  	roll = rotation.vec[0] * rotSens.vec[0];
    pitch = rotation.vec[1] * rotSens.vec[1];
    yaw = rotation.vec[2] * rotSens.vec[2];
	}
	
	/**
	 * Feed the HIDevice with hardware output using a controller. The controller should be implemented
	 * by the application. This method simply calls {@code feedTranslation(tx,ty,tz)} and
	 * {@code feedRotation(rx,ry,rz)}.
	 * <p>
	 * Useful when {@link #addHandler(Object, String)} has been called on this HIDevice.
	 * 
	 * @see #feedTranslation(float, float, float)
	 * @see #feedRotation(float, float, float)
	 */
	public void feed(float tx, float ty, float tz, float rx, float ry, float rz) {
		feedTranslation(tx,ty,tz);
		feedRotation(rx,ry,rz);
	}

	/**
	 * Defines the device translation feed.
	 * <p> 
	 * Useful when {@link #addHandler(Object, String)} has been called on this HIDevice.
	 * 
	 * @param x x-axis translation
	 * @param y y-axis translation
	 * @param z z-axis translation
	 * 
	 * @see #feed(float, float, float, float, float, float)
	 * @see #feedRotation(float, float, float)
	 */
	public void feedTranslation(float x, float y, float z) {
		translation.set(x, y, z);
	}
	
	/**
	 * Defines the device rotation feed.
	 * <p>
	 * Useful when {@link #addHandler(Object, String)} has been called on this HIDevice.
	 * 
	 * @param x x-axis rotation
	 * @param y y-axis rotation
	 * @param z z-axis rotation
	 * 
	 * @see #feed(float, float, float, float, float, float)
	 * @see #feedTranslation(float, float, float)
	 */
	public void feedRotation(float x, float y, float z) {
		rotation.set(x, y, z);
	}
	
	/**
	 * Defines the device x-axis translation feed.
	 * <p>
	 * Useful when {@link #addHandler(Object, String)} has been called on this HIDevice.
	 */
	public void feedXTranslation(float t) {
		translation.vec[0] = t;
	}

	/**
	 * Defines the device y-axis translation feed.
	 * <p>
	 * Useful when {@link #addHandler(Object, String)} has been called on this HIDevice.
	 */
	public void feedYTranslation(float t) {
		translation.vec[1] = t;
	}

	/**
	 * Defines the device z-axis translation feed.
	 * <p>
	 * Useful when {@link #addHandler(Object, String)} has been called on this HIDevice.
	 */
	public void feedZTranslation(float t) {
		translation.vec[2] = t;
	}	

	/**
	 * Defines the device x-axis rotation feed.
	 * <p>
	 * Useful when {@link #addHandler(Object, String)} has been called on this HIDevice. 
	 */
	public void feedXRotation(float t) {
		rotation.vec[0] = t;
	}

	/**
	 * Defines the device y-axis rotation feed.
	 * <p>
	 * Useful when {@link #addHandler(Object, String)} has been called on this HIDevice. 
	 */
	public void feedYRotation(float t) {
		rotation.vec[1] = t;
	}

	/**
	 * Defines the device z-axis rotation feed.
	 * <p>
	 * Useful when {@link #addHandler(Object, String)} has been called on this HIDevice. 
	 */
	public void feedZRotation(float t) {
		rotation.vec[2] = t;
	}
	
	/**
	 * Overload this method to define the x-axis translation feed this method
	 * if you plan to implement your own HIDevice. Otherwise use {@link #feedXTranslation(float)}
	 * and {@link #addHandler(Object, String)} to the HIDevice.
	 */
	public float feedXTranslation() {
		return 0;
	}

	/**
	 * Overload this method to define the y-axis translation feed this method
	 * if you plan to implement your own HIDevice. Otherwise use {@link #feedYTranslation(float)}
	 * and {@link #addHandler(Object, String)} to the HIDevice.
	 */
	public float feedYTranslation() {
		return 0;
	}

	/**
	 * Overload this method to define the z-axis translation feed this method
	 * if you plan to implement your own HIDevice. Otherwise use {@link #feedZTranslation(float)}
	 * and {@link #addHandler(Object, String)} to the HIDevice.
	 */
	public float feedZTranslation() {
		return 0;
	}

	/**
	 * Overload this method to define the x-axis rotation feed this method
	 * if you plan to implement your own HIDevice. Otherwise use {@link #feedXRotation(float)}
	 * and {@link #addHandler(Object, String)} to the HIDevice.
	 */
	public float feedXRotation() {
		return 0;
	}

	/**
	 * Overload this method to define the y-axis rotation feed this method
	 * if you plan to implement your own HIDevice. Otherwise use {@link #feedYRotation(float)}
	 * and {@link #addHandler(Object, String)} to the HIDevice.
	 */
	public float feedYRotation() {
		return 0;
	}

	/**
	 * Overload this method to define the z-axis rotation feed this method
	 * if you plan to implement your own HIDevice. Otherwise use {@link #feedZRotation(float)}
	 * and {@link #addHandler(Object, String)} to the HIDevice.
	 */
	public float feedZRotation() {
		return 0;
	}

	/**
	 * Sets the translation sensitivity.
	 * 
	 * @param sx x-axis translation sensitivity
	 * @param sy y-axis translation sensitivity
	 * @param sz z-axis translation sensitivity
	 */
	public void setTranslationSensitivity(float sx, float sy, float sz) {
		transSens.set(sx, sy, sz);
	}
	
	/**
	 * Sets the rotation sensitivity.
	 * 
	 * @param sx x-axis rotation sensitivity
	 * @param sy y-axis rotation sensitivity
	 * @param sz z-axis rotation sensitivity
	 */
	public void setRotationSensitivity(float sx, float sy, float sz) {
		rotSens.set(sx, sy, sz);
	}
	
	/**
	 * Sets the x-axis translation sensitivity.
	 */
	public void setXTranslationSensitivity(float sensitivity) {
		transSens.vec[0] = sensitivity;
	}

	/**
	 * Sets the y-axis translation sensitivity.
	 */
	public void setYTranslationSensitivity(float sensitivity) {
		transSens.vec[1] = sensitivity;
	}

	/**
	 * Sets the z-axis translation sensitivity.
	 */
	public void setZTranslationSensitivity(float sensitivity) {
		transSens.vec[2] = sensitivity;
	}	

	/**
	 * Sets the x-axis rotation sensitivity.
	 */
	public void setXRotationSensitivity(float sensitivity) {
		rotSens.vec[0] = sensitivity;
	}

	/**
	 * Sets the y-axis rotation sensitivity.
	 */
	public void setYRotationSensitivity(float sensitivity) {
		rotSens.vec[1] = sensitivity;
	}

	/**
	 * Sets the z-axis rotation sensitivity.
	 */
	public void setZRotationSensitivity(float sensitivity) {
		rotSens.vec[2] = sensitivity;
	}	

	/**
	 * Handle the feed by properly calling {@link #handleCamera()} or {@link #handleIFrame()}.
	 * 
	 * <b>Attention</b>: Handled by the scene. You should not call this method by yourself.
	 */
	/**
	//TODO que cha rala mbon ;)
	public void handleLocal() {
		// TODO it should produce a remix event to be enqueued by the scene and then process by the EventHandler
		if(!invoke()) {			
			feedXTranslation(feedXTranslation());
			feedYTranslation(feedYTranslation());
			feedZTranslation(feedZTranslation());
			feedXRotation(feedXRotation());
			feedYRotation(feedYRotation());
			feedZRotation(feedZRotation());
		}
		
		tx = translation.vec[0] * transSens.vec[0];
		if( scene.isRightHanded() )
			ty = translation.vec[1] * (- transSens.vec[1]);
		else
			ty = translation.vec[1] * transSens.vec[1];
		tz = translation.vec[2] * transSens.vec[2];
		roll = rotation.vec[0] * rotSens.vec[0];
		if( scene.isRightHanded() )
			pitch = rotation.vec[1] * (- rotSens.vec[1]);
		else
			pitch = rotation.vec[1] * rotSens.vec[1];
		yaw = rotation.vec[2] * rotSens.vec[2];
  }
  */
}