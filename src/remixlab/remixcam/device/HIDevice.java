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

package remixlab.remixcam.device;

import java.util.ArrayList;

import remixlab.remixcam.core.AbstractScene;
import remixlab.remixcam.geom.*;

/**
 * An HIDevice represents a Human Interface Device with (<=) 6 degrees of freedom.
 * <p>
 * An HIDevice has a type which can be RELATIVE (default) or ABSOLUTE. A RELATIVE HIDevice
 * has a neutral position that the device holds when it is not being manipulated. An ABSOLUTE HIDevice
 * has no such neutral position. Examples of RELATIVE devices are the space navigator and the joystick,
 * examples of ABSOLUTE devices are the wii or the kinect.
 */
public class HIDevice {
	/**
	 * This enum holds the device type.
	 *
	 */
	public enum Mode {RELATIVE, ABSOLUTE}
	
	public enum PointerMode {POINTER, POINTERLESS}
	
	protected Mode mode;
	
	protected PointerMode pmode;
	
	protected Object handlerObject;	
	protected String handlerMethodName;
	
	protected AbstractScene scene;
	
	protected class Button {
		static public final int PRESS = 1;
	  static public final int RELEASE = 2;
	  static public final int CLICK = 3;
	  static public final int DRAG = 4;
	  static public final int MOVE = 5;
	  static public final int ENTER = 6;
	  static public final int EXIT = 7;
	}
	
	protected class Wheel {
		protected Float amount;
	}
	
	ArrayList<Button> buttons;
	
	ArrayList<Wheel> wheels;

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
	 * Convenience constructor that simply calls {@code this(scn, Mode.RELATIVE)}.
	 * 
	 * @param scn The Scene object this Device belongs to.
	 * 
	 * @see #AbstractDevice(AbstractScene)
	 */
	public HIDevice(AbstractScene scn) {
		this(scn, Mode.RELATIVE);
	}

	/**
	 * Main constructor.
	 * 
	 * @param scn The Scene object this HIDevice belongs to.
	 * @param m The device {@link #mode()}.
	 */
	public HIDevice(AbstractScene scn, Mode m) {
		scene = scn;
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
    
    setMode(m);
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
		if ( mode() == Mode.ABSOLUTE )
			prevTranslation.set(translation);
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
		if ( mode() == Mode.ABSOLUTE )
			prevRotation.set(rotation);
		rotation.set(x, y, z);
	}
	
	/**
	 * Defines the device x-axis translation feed.
	 * <p>
	 * Useful when {@link #addHandler(Object, String)} has been called on this HIDevice.
	 */
	public void feedXTranslation(float t) {
		if ( mode() == Mode.ABSOLUTE )
			prevTranslation.vec[0] = translation.vec[0]; 
		translation.vec[0] = t;
	}

	/**
	 * Defines the device y-axis translation feed.
	 * <p>
	 * Useful when {@link #addHandler(Object, String)} has been called on this HIDevice.
	 */
	public void feedYTranslation(float t) {
		if ( mode() == Mode.ABSOLUTE ) {
			prevTranslation.vec[1] = translation.vec[1]; 
		}
		translation.vec[1] = t;
	}

	/**
	 * Defines the device z-axis translation feed.
	 * <p>
	 * Useful when {@link #addHandler(Object, String)} has been called on this HIDevice.
	 */
	public void feedZTranslation(float t) {
		if ( mode() == Mode.ABSOLUTE )
			prevTranslation.vec[2] = translation.vec[2]; 
		translation.vec[2] = t;
	}	

	/**
	 * Defines the device x-axis rotation feed.
	 * <p>
	 * Useful when {@link #addHandler(Object, String)} has been called on this HIDevice. 
	 */
	public void feedXRotation(float t) {
		if ( mode() == Mode.ABSOLUTE )
			prevRotation.vec[0] = rotation.vec[0]; 
		rotation.vec[0] = t;
	}

	/**
	 * Defines the device y-axis rotation feed.
	 * <p>
	 * Useful when {@link #addHandler(Object, String)} has been called on this HIDevice. 
	 */
	public void feedYRotation(float t) {
		if ( mode() == Mode.ABSOLUTE )
			prevRotation.vec[1] = rotation.vec[1];
		rotation.vec[1] = t;
	}

	/**
	 * Defines the device z-axis rotation feed.
	 * <p>
	 * Useful when {@link #addHandler(Object, String)} has been called on this HIDevice. 
	 */
	public void feedZRotation(float t) {
		if ( mode() == Mode.ABSOLUTE )
			prevRotation.vec[2] = rotation.vec[2];
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
	 * Attempt to add a 'feed' handler method to the HIDevice. The default feed
	 * handler is a method that returns void and has one single HIDevice parameter.
	 * 
	 * @param obj the object to handle the feed
	 * @param methodName the method to execute the feed in the object handler class
	 * 
	 * @see #removeHandler()
	 * @see #invoke()
	 */
	public void addHandler(Object obj, String methodName) {
		AbstractScene.showMissingImplementationWarning("addHandler");
	}
	
	/**
	 * Unregisters the 'feed' handler method (if any has previously been added to
	 * the HIDevice).
	 * 
	 * @see #addHandler(Object, String)
	 * @see #invoke()
	 */
	public void removeHandler() {
		AbstractScene.showMissingImplementationWarning("removeHandler");
	}
	
	/**
	 * called by {@link #handle()}. Invokes the method added by
	 * {@link #addHandler(Object, String)}. Returns {@code true} if
	 * succeeded and {@code false} otherwise (e.g., no method was added).
	 * 
	 * @see #addHandler(Object, String)
	 * @see #removeHandler()
	 */
	public boolean invoke() {
		AbstractScene.showMissingImplementationWarning("invoke");
		return false;
	}

	/**
	 * Handle the feed by properly calling {@link #handleCamera()} or {@link #handleIFrame()}.
	 * 
	 * <b>Attention</b>: Handled by the scene. You should not call this method by yourself.
	 */
	public void handle() {
		// TODO it should produce a remix event to be enqueued by the scene and then process by the EventHandler
		if(!invoke()) {			
			feedXTranslation(feedXTranslation());
			feedYTranslation(feedYTranslation());
			feedZTranslation(feedZTranslation());
			feedXRotation(feedXRotation());
			feedYRotation(feedYRotation());
			feedZRotation(feedZRotation());
		}
		
		if ( mode() == Mode.ABSOLUTE ) {
			tx = (translation.vec[0] - prevTranslation.vec[0]) * transSens.vec[0];
			if( scene.isRightHanded() )
				ty = (prevTranslation.vec[1] - translation.vec[1]) * transSens.vec[1];
			else
				ty = (translation.vec[1] - prevTranslation.vec[1]) * transSens.vec[1];
			tz = (translation.vec[2] - prevTranslation.vec[2]) * transSens.vec[2];
			roll = (rotation.vec[0] - prevRotation.vec[0]) * rotSens.vec[0];			
			if( scene.isRightHanded() )
				pitch = (prevRotation.vec[1] - rotation.vec[1]) * rotSens.vec[1];
			else
				pitch = (rotation.vec[1] - prevRotation.vec[1]) * rotSens.vec[1];
			yaw = (rotation.vec[2] - prevRotation.vec[2]) * rotSens.vec[2];
		}
		else {
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
  }
  
  /**
   * Sets the device type.
   */
  public void setMode(Mode m) {
  	if(m == Mode.ABSOLUTE) {
  		if(prevTranslation == null)
  			prevTranslation = new Vector3D();
  		if(prevRotation == null)
  			prevRotation = new Vector3D();
    }
  	mode = m;
  }
  
  /**
   * Return the device type.
   */
  public Mode mode() {
  	return mode;
  }
	
  /**
   * Overload this method in your HIDevice derived class if you plan to define your own camera handle method.
   * Default implementation is empty.
   */
	protected void customCameraHandle() {}
	
	/**
   * Overload this method in your HIDevice derived class if you plan to define your own
   * interactive frame handle method. Default implementation is empty.
   */
  protected void customIFrameHandle() {}
}