/**
 *                     ProScene (version 1.2.0)      
 *    Copyright (c) 2010-2011 by National University of Colombia
 *                 @author Jean Pierre Charalambos      
 *           http://www.disi.unal.edu.co/grupos/remixlab/
 *                           
 * This java package provides classes to ease the creation of interactive 3D
 * scenes in Processing.
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

import java.util.Map.Entry;

import remixlab.remixcam.action.DOF_0Action;
import remixlab.remixcam.action.DOF_6Action;
import remixlab.remixcam.core.AbstractScene;
import remixlab.remixcam.core.Constants;
import remixlab.remixcam.event.*;
import remixlab.remixcam.renderer.*;
import remixlab.remixcam.shortcut.ClickShortcut;
import remixlab.remixcam.shortcut.ButtonShortcut;
import remixlab.remixcam.shortcut.KeyboardShortcut;

/**
 * This class encapsulates a set of camera keyboard shortcuts, and camera and
 * frame mouse bindings which together represent a "camera mode".
 * <p>
 * Proscene handle the following kind of actions:
 * <ol>
 * <li><b>Keyboard actions</b> which can be general such as draw the world axis
 * (see {@link remixlab.proscene.Scene#setShortcut(Character, remixlab.proscene.Scene.KeyboardAction)})
 * or can be associated to a camera profile to affect the camera behavior, such as
 * move the camera to the left. Different versions of {@code setShortcut()} are provided
 * to set camera keyboard shortcuts.
 * <li><b>Click actions</b> such as selecting an object. This class provide different versions of
 * {@code setClickBinding()} to bind click actions.
 * <li><b>Mouse actions (i.e., the mouse is clicked and then dragged)</b> which can be handle by
 * the camera or an interactive frame (such as rotating the camera or an interactive frame).
 * This class provide different versions of {@code setCameraMouseBinding()}, {@code setFrameMouseBinding()}
 * {@code setCameraWheelBinding()} and {@code setFrameWheelBinding()} to bind the mouse actions. 
 * </ol>
 * <p>
 * <b>Note:</b> Click and mouse actions can also be handled by MouseGrabbable objects, but their
 * implementation is entirely customizable and thus it depends on your application. The InteractiveFrame
 * class is actually a MouseGrabbable object and hence it provides one such implementation.
 * <p>
 * Once instantiated, to use a camera profile you need to {@link #register()} it
 * ({@link #unregister()} performs the inverse operation).
 * <p>
 * This class also provide some useful preset bindings which represents some common
 * "camera modes": ARCBALL, CAD, FIRST_PERSON, THIRD_PERSON, and CUSTOM
 * (see {@link #mode()}). A CUSTOM camera profiles represents an empty set of camera and
 * keyboard shortcuts. Most of the methods of this class provide means to define
 * (or overwrite) custom (or preset bindings). Default keyboard shortcuts and mouse bindings
 * for each CameraProfile preset are defined as follows:
 * <p>
 * <h3>ARCBALL</h3>
 * <ol>
 * <li><b>Keyboard shortcuts</b></li>
 * <ul>
 * <li>Up -> Move camera up</li>
 * <li>Down -> Move camera down</li>
 * <li>Left -> Move camera to the left</li>
 * <li>Right -> Move camera to the right</li>
 * <li>S -> Show the whole scene</li>
 * <li>s -> Interpolate the camera to fit the whole scene</li>
 * <li>+ -> Increase camera rotation sensitivity</li>
 * <li>- -> Decrease camera rotation sensitivity</li>
 * </ul>
 * <li><b>Camera mouse bindings</b></li>
 * <ul>
 * <li>Button1 -> Rotate frame</li>
 * <li>Shift+Button1 -> Zoom on region (camera or interactive drivable frame)</li> 
 * <li>Button2 -> Zoom</li>
 * <li>Button3 -> Translate frame (camera or interactive frame)</li>
 * <li>Shift+Button3 -> Screen rotate  (camera or interactive frame)</li>
 * </ul>
 * <li><b>Mouse click bindings</b></li>
 * <ul>
 * <li>Button1 + 2 clicks -> Align camera with world</li>
 * <li>Button2 + 2 clicks -> Show the whole scene</li>
 * <li>Button3 + 2 clicks -> Zoom to fit the scene</li>
 * </ul>
 * <li><b>Interactive frame mouse bindings</b></li>
 * <ul>
 * <li>Button1 -> Rotate frame (camera or interactive frame)</li>
 * <li>Button2 -> Zoom</li>
 * <li>Button3 -> Translate frame (camera or interactive frame)</li>
 * </ul>
 * </ol>
 * <p>
 * <h3>WHEELED_ARCBALL</h3>
 * <ol>
 * <li><b>Keyboard shortcuts</b></li>
 * <ul>
 * <li>Right -> Move camera to the right</li>
 * <li>Left -> Move camera to the left</li>
 * <li>Up -> Move camera up</li>
 * <li>Down -> Move camera down</li>
 * <li>S -> Show the whole scene</li>
 * <li>s -> Interpolate the camera to fit the whole scene</li>
 * <li>+ -> Increase camera rotation sensitivity</li>
 * <li>- -> Decrease camera rotation sensitivity</li>
 * </ul>
 * <li><b>Camera mouse bindings</b></li>
 * <ul>
 * <li>Button1 -> Rotate frame (camera or interactive frame) as with an ARCBALL</li>
 * <li>Shift+Button1 -> Zoom on region (camera or interactive drivable frame)</li> * 
 * <li>Button2 -> Zoom</li>
 * <li>Button3 -> Translate frame (camera or interactive frame)</li>
 * <li>Shift+Button3 -> Screen rotate (camera or interactive frame)</li>
 * </ul>
 * <li><b>Mouse click bindings</b></li>
 * <ul>
 * <li>Button1 + 2 clicks -> Align camera with world</li>
 * <li>Button2 + 2 clicks -> Show the whole scene</li>
 * <li>Button3 + 2 clicks -> Zoom to fit the scene</li>
 * </ul>
 * <li><b>Interactive frame mouse bindings</b></li>
 * <ul>
 * <li>Button1 -> Rotate frame (camera or interactive frame)</li>
 * <li>Button2 -> Zoom</li>
 * <li>Button3 -> Translate frame (camera or interactive frame)</li>
 * </ul>
 * <li><b>Camera mouse wheel bindings</b></li>
 * <ul>
 * <li>Wheel -> Zoom</li>
 * </ul>
 * <li><b>Interactive frame mouse wheel bindings</b></li>
 * <ul>
 * <li>Wheel -> Zoom</li>
 * </ul>
 * </ol>
 * <h3>CAD</h3>
 * <ol>
 * <li><b>Keyboard shortcuts</b></li>
 * <ul>
 * <li>Right -> Move camera to the right</li>
 * <li>Left -> Move camera to the left</li>
 * <li>Up -> Move camera up</li>
 * <li>Down -> Move camera down</li>
 * <li>S -> Show the whole scene</li>
 * <li>s -> Interpolate the camera to fit the whole scene</li>
 * <li>+ -> Increase camera rotation sensitivity</li>
 * <li>- -> Decrease camera rotation sensitivity</li>
 * </ul>
 * <li><b>Camera mouse bindings</b></li>
 * <ul>
 * <li>Button1 -> Rotate frame: camera is rotated as in CAD applications; interactive
 * frame is rotated as with an ARCBALL</li>
 * <li>Shift+Button1 -> Zoom on region (camera or interactive drivable frame)</li> * 
 * <li>Button2 -> Zoom</li>
 * <li>Button3 -> Translate frame (camera or interactive frame)</li>
 * <li>Shift+Button3 -> Screen rotate (camera or interactive frame)</li>
 * </ul>
 * <li><b>Mouse click bindings</b></li>
 * <ul>
 * <li>Button1 + 2 clicks -> Align camera with world</li>
 * <li>Button2 + 2 clicks -> Show the whole scene</li>
 * <li>Button3 + 2 clicks -> Zoom to fit the scene</li>
 * </ul>
 * <li><b>Interactive frame mouse bindings</b></li>
 * <ul>
 * <li>Button1 -> Rotate frame (camera or interactive frame)</li>
 * <li>Button2 -> Zoom</li>
 * <li>Button3 -> Translate frame (camera or interactive frame)</li>
 * </ul>
 * <li><b>Camera mouse wheel bindings</b></li>
 * <ul>
 * <li>Wheel -> Zoom</li>
 * </ul>
 * <li><b>Interactive frame mouse wheel bindings</b></li>
 * <ul>
 * <li>Wheel -> Zoom</li>
 * </ul>
 * </ol> 
 * <h3>FIRST_PERSON</h3>
 * <ol>
 * <li><b>Keyboard shortcuts</b></li>
 * <ul>
 * <li>S -> Show the whole scene</li>
 * <li>s -> Interpolate the camera to fit the whole scene</li>
 * <li>+ -> Increase camera fly speed</li>
 * <li>- -> Decrease camera fly speed</li>
 * </ul>
 * <li><b>Camera mouse bindings</b></li>
 * <ul>
 * <li>Button1 -> Move forward frame (camera or interactive frame)</li>
 * <li>Shift+Button1 -> Roll frame (camera or interactive drivable frame)</li>
 * <li>Button2 -> Look around with frame (camera or interactive drivable frame)</li>
 * <li>Button3 -> move backward frame (camera or interactive frame)</li>
 * <li>Shift+Button3 -> Drive (camera or interactive drivable frame)</li>
 * </ul>
 * <li><b>Interactive frame mouse bindings</b></li>
 * <ul>
 * <li>Button1 -> Rotate frame (camera or interactive frame)</li>
 * <li>Button2 -> Zoom</li>
 * <li>Button3 -> Translate frame (camera or interactive frame)</li>
 * </ul>
 * </ol>
 * <p>
 * <h3>THIRD_PERSON</h3>
 * <ol>
 * <li><b>Keyboard shortcuts</b></li>
 * <ul>
 * <li>A -> Decrease camera azymuth respect to the avatar</li>
 * <li>a -> Increase camera azymuth respect to the avatar</li>
 * <li>I -> Decrease camera inclination respect to the avatar</li>
 * <li>i -> Increase camera inclination respect to the avatar</li>
 * <li>t -> Increase camera tracking distance respect to the avatar</li>
 * <li>T -> Decrease camera tracking distance respect to the avatar</li>
 * <li>+ -> Increase avatar fly speed</li>
 * <li>- -> Decrease avatar fly speed</li>
 * </ul>
 * <li><b>Interactive frame mouse bindings</b></li>
 * <ul>
 * <li>Button1 -> Move forward frame (camera or interactive frame)</li>
 * <li>Shift+Button1 -> Roll frame (camera or interactive drivable frame)</li>
 * <li>Button2 -> Look around with frame (camera or interactive drivable frame)</li>
 * <li>Button3 -> move backward frame (camera or interactive frame)</li>
 * <li>Shift+Button3 -> Drive (camera or interactive drivable frame)</li>
 * </ul>
 * </ol>
 * <p>
 * The Scene provides proper methods to manage (and also register and unregister)
 * camera profiles, such as {@link remixlab.proscene.Scene#cameraProfile(String)},
 * {@link remixlab.proscene.Scene#currentCameraProfile()},
 * {@link remixlab.proscene.Scene#setCurrentCameraProfile(CameraProfile)}, among others.
 * <p>
 * <b>Attention:</b> Every instantiated scene provides two camera profiles by default:
 * ARCBALL and FIRST_PERSON. If you define an
 * {@link remixlab.proscene.Scene#avatar()} (for instance with 
 * {@link remixlab.proscene.Scene#setAvatar(Trackable)}) then you should register at
 * least one THIRD_PERSON camera profile to your Scene.
 */
public abstract class CameraProfile implements Constants {
	//public enum Mode {ARCBALL, WHEELED_ARCBALL, CAD, FIRST_PERSON, THIRD_PERSON, CUSTOM}
	protected String name;
	protected AbstractScene scene;
	protected Bindings<KeyboardShortcut, DOF_0Action> keyboard;
	protected Bindings<ButtonShortcut, DOF_6Action> cameraActions;
	protected Bindings<ButtonShortcut, DOF_6Action> frameActions;
	// C L I C K A C T I O N S
	protected Bindings<ClickShortcut, DOF_0Action> clickActions;
	protected Bindings<Integer, DOF_6Action> cameraWheelActions;
	protected Bindings<Integer, DOF_6Action> frameWheelActions;
	
	/**
	 * Convenience constructor that simply calls {@code this(scn, n, Mode.CUSTOM)}.
	 */
	/**
	public CameraProfile(AbstractScene scn, String n) {
		this(scn, n, Mode.CUSTOM);
	}
	*/

	/**
	 * Main constructor.
	 * <p>
	 * This constructor takes the scene instance, the custom name of the camera profile
	 * and an enum type defining its mode which can be: ARCBALL, CAD,
	 * FIRST_PERSON, THIRD_PERSON, CUSTOM.
	 * <p>
	 * If you define a {@link remixlab.proscene.Scene#avatar()} (for instance with 
	 * {@link remixlab.proscene.Scene#setAvatar(Trackable)}) then you should register at
	 * least one THIRD_PERSON camera profile to your Scene. 
	 * <p>
	 * Except for CUSTOM, each camera mode loads some preset camera and keyboard shortcut
	 * bindings. A custom camera profiles represents an empty set of camera and keyboard
	 * shortcuts. 
	 * 
	 * @param scn the scene object
	 * @param n the camera profile name
	 * @param m the camera profile mode
	 */
	public CameraProfile(AbstractScene scn, String n) {
		scene = scn;		
		name = n;
		keyboard = new Bindings<KeyboardShortcut, DOF_0Action>(scene);
		cameraActions = new Bindings<ButtonShortcut, DOF_6Action>(scene);
		frameActions = new Bindings<ButtonShortcut, DOF_6Action>(scene);		
		clickActions = new Bindings<ClickShortcut, DOF_0Action>(scene);
		cameraWheelActions = new Bindings<Integer, DOF_6Action>(scene);
		frameWheelActions = new Bindings<Integer, DOF_6Action>(scene);		
	}
	
	/**
	 * Internal use. Called by the constructor by ARCBALL and WHEELED_ARCBALL modes.
	 */
	protected void arcballDefaultShortcuts() {
		setShortcut(RIGHT, DOF_0Action.MOVE_CAMERA_RIGHT);		
	  setShortcut(LEFT, DOF_0Action.MOVE_CAMERA_LEFT);
		setShortcut(UP, DOF_0Action.MOVE_CAMERA_UP);
		setShortcut(DOWN, DOF_0Action.MOVE_CAMERA_DOWN);
		
		//TODO hack to prevent P5 java2d bug!
		if( scene.renderer() instanceof ProjectionRenderer ) {
			setCameraMouseBinding(CENTER, DOF_6Action.ZOOM);
			setCameraMouseBinding(RIGHT, DOF_6Action.TRANSLATE);
		}
		else {
			setCameraMouseBinding(ALT, CENTER, DOF_6Action.ZOOM);
			setCameraMouseBinding(META, RIGHT, DOF_6Action.TRANSLATE);
		}		
		
		setFrameMouseBinding(LEFT, DOF_6Action.ROTATE);
		
	  //TODO hack to prevent P5 java2d bug!
		if( scene.renderer() instanceof ProjectionRenderer ) {
			setFrameMouseBinding(CENTER, DOF_6Action.ZOOM);
			setFrameMouseBinding(RIGHT, DOF_6Action.TRANSLATE);
		}
		else {
			setFrameMouseBinding(ALT, CENTER, DOF_6Action.ZOOM);
			setFrameMouseBinding(META, RIGHT, DOF_6Action.TRANSLATE);
		}

		setCameraMouseBinding(SHIFT, LEFT, DOF_6Action.ZOOM_ON_REGION);		
		setCameraMouseBinding(SHIFT, RIGHT, DOF_6Action.SCREEN_ROTATE);

		setShortcut('+', DOF_0Action.INCREASE_ROTATION_SENSITIVITY);
		setShortcut('-', DOF_0Action.DECREASE_ROTATION_SENSITIVITY);

		setShortcut('s', DOF_0Action.INTERPOLATE_TO_FIT_SCENE);
		setShortcut('S', DOF_0Action.SHOW_ALL);
		
		setClickBinding(LEFT, 2, DOF_0Action.ALIGN_CAMERA);
		setClickBinding(CENTER, 2, DOF_0Action.SHOW_ALL);
		setClickBinding(RIGHT, 2, DOF_0Action.ZOOM_TO_FIT);
	}
	
  // 1. General stuff
	
	/**
	 * Returns the camera profile name.
	 * <p>
	 * The camera profile name is defined at instantiation time and cannot be modified later.
	 * 
	 * @see #CameraProfile(AbstractScene, String, Mode)
	 */
	public String name() {
		return name;
	}

	/**
	 * Returns true if the camera profile is registered at the scene and false otherwise.
	 * 
	 * @see #register()
	 * @see #unregister()
	 */
	public boolean isRegistered() {
		return scene.isCameraProfileRegistered(this);
	}

	/**
	 * Registers the camera profile at the scene. Returns true if registration succeeded
	 * (if there's a registered camera profile with the same name, registration will fail). 
	 * <p> 
	 * Convenience wrapper function that simple calls {@code scene.registerCameraProfile(this)}. 
	 */
	public boolean register() {
		return scene.registerCameraProfile(this);
	}

	/**
	 * Unregisters the camera profile from the scene. 
	 * <p> 
	 * Convenience wrapper function that simple calls {@code scene.unregisterCameraProfile(this)}. 
	 */
	public void unregister() {
		scene.unregisterCameraProfile(this);
	}
	
	// 2. AWT input event parsing, i.e., converts events to actions.
	
	public DOF_6Action cameraMouseAction(int modifiers, int button) {
		DOF_6Action camMouseAction = cameraMouseBinding( modifiers, button );	
		if (camMouseAction == null)
			camMouseAction = DOF_6Action.NO_ACTION;
		return camMouseAction;
	}
	
	/**
	 * Internal method. Parses the event to convert it to a Scene.MouseAction. Returns
	 * {@link remixlab.proscene.Scene.DOF_6Action#NO_ACTION} if no action was found.
	 * <p>
	 * Called by {@link remixlab.remixcam.core.EventDispatcher#mousePressed(MouseEvent)}.
	 */
	public DOF_6Action cameraMouseAction(DLMouseEvent e) {
		DOF_6Action camMouseAction = cameraMouseBinding( e.getModifiers(), e.getButton() );	
		if (camMouseAction == null)
			camMouseAction = DOF_6Action.NO_ACTION;
		return camMouseAction;		
		
	  //TODO debug		
//		MouseAction camMouseAction = cameraMouseBinding( e.getModifiers(), e.getButton() );	
//		if (camMouseAction == null)
//			camMouseAction = MouseAction.NO_MOUSE_ACTION;		
//		
//		String button = new String();
//		if(e.getButton() == RIGHT)
//			button = "RIGHT ";
//		else if (e.getButton() == LEFT)
//			button = "LEFT ";
//		else if (e.getButton() == CENTER)
//			button = "CENTER ";
//		System.out.println("Button: " + button + 
//				               ", modifiers: " + DesktopEvents.getModifiersText(e.getModifiers()) +
//	                     ", action bound: " + camMouseAction.description());         
//		return camMouseAction;
	}
	
	public DOF_6Action frameMouseAction(int modifiers, int button) {
		DOF_6Action iFrameMouseAction = frameMouseBinding( modifiers, button );
		if (iFrameMouseAction == null)
			iFrameMouseAction = DOF_6Action.NO_ACTION;
		return iFrameMouseAction;
	}
	
	/**
	 * Internal method. Parses the event to convert it to a Scene.MouseAction. Returns
	 * {@link remixlab.proscene.Scene.DOF_6Action#NO_ACTION} if no action was found.
	 * <p>
	 * Called by {@link remixlab.remixcam.core.EventDispatcher#mousePressed(MouseEvent)}.
	 */
	public DOF_6Action frameMouseAction(DLMouseEvent e) {
		DOF_6Action iFrameMouseAction = frameMouseBinding( e.getModifiers(), e.getButton() );
		if (iFrameMouseAction == null)
			iFrameMouseAction = DOF_6Action.NO_ACTION;
		return iFrameMouseAction;
	}
	
	public DOF_6Action cameraWheelMouseAction(int modifiers) {
		DOF_6Action wMouseAction = cameraWheelBinding(modifiers);
		if (wMouseAction == null)
			wMouseAction = DOF_6Action.NO_ACTION;
		return wMouseAction;
	}
	
	/**
	 * Internal method. Parses the event to convert it to a Scene.MouseAction. Returns
	 * {@link remixlab.proscene.Scene.DOF_6Action#NO_ACTION} if no action was found.
	 * <p>
	 * Called by {@link remixlab.remixcam.core.EventDispatcher#mouseWheelMoved(MouseWheelEvent)}.
	 */
	public DOF_6Action cameraWheelMouseAction(DLMouseEvent e) {
		DOF_6Action wMouseAction = cameraWheelBinding(e.getModifiers());
		if (wMouseAction == null)
			wMouseAction = DOF_6Action.NO_ACTION;
		return wMouseAction;
	}
	
	public DOF_6Action frameWheelMouseAction(int modifiers) {
		DOF_6Action fMouseAction = frameWheelBinding( modifiers );
		if (fMouseAction == null)
			fMouseAction = DOF_6Action.NO_ACTION;
		return fMouseAction;
	}
	
	/**
	 * Internal method. Parses the event to convert it to a Scene.MouseAction. Returns
	 * {@link remixlab.proscene.Scene.DOF_6Action#NO_ACTION} if no action was found.
	 * <p>
	 * Called by {@link remixlab.remixcam.core.EventDispatcher#mouseWheelMoved(MouseWheelEvent)}.
	 */
	public DOF_6Action frameWheelMouseAction(DLMouseEvent e) {
		DOF_6Action fMouseAction = frameWheelBinding( e.getModifiers() );
		if (fMouseAction == null)
			fMouseAction = DOF_6Action.NO_ACTION;
		return fMouseAction;
	}
	
	/**
	 * Returns a String containing the camera mouse bindings' descriptions.
	 */
	public String cameraDeviceBindingsDescription() {
		String description = new String();
		for (Entry<ButtonShortcut, DOF_6Action> entry : cameraActions.map().entrySet())
      description += entry.getKey().description() + " -> " + entry.getValue().description() + "\n";     
		return description;
	}
	
	/**
	 * Returns a String containing the interactive frame mouse bindings' descriptions.
	 */
	public String frameDeviceBindingsDescription() {
		String description = new String();
		for (Entry<ButtonShortcut, DOF_6Action> entry : frameActions.map().entrySet())
      description += entry.getKey().description() + " -> " + entry.getValue().description() + "\n";
		return description;
	}
	
	/**
	 * Returns a String containing the camera mouse-click bindings' descriptions.
	 */
	public String deviceClickBindingsDescription() {
		String description = new String();
		for (Entry<ClickShortcut, DOF_0Action> entry : clickActions.map().entrySet())
      description += entry.getKey().description() + " -> " + entry.getValue().description() + "\n";
		return description;
	}
	
	/**
	 * Returns a String containing the camera keyboard bindings' descriptions.
	 */
	public String keyboardShortcutsDescription() {
		String description = new String();
		for (Entry<KeyboardShortcut, DOF_0Action> entry : keyboard.map().entrySet())
      description += entry.getKey().description() + " -> " + entry.getValue().description() + "\n";
		return description;
	}
	
	/**
	 * Returns a String containing the camera mouse wheel bindings' descriptions.
	 */
	public String cameraWheelBindingsDescription() {
		String description = new String();
		for (Entry<Integer, DOF_6Action> entry : cameraWheelActions.map().entrySet()) {
			if (DLEvent.getModifiersText(entry.getKey()).length() != 0 )
				description += "Wheel " + DLEvent.getModifiersText(entry.getKey()) + " -> " + entry.getValue().description() + "\n";
			else
				description += "Wheel -> " + entry.getValue().description() + "\n";
		}
		return description;
	}
	
	/**
	 * Returns a String containing the interactive frame mouse wheel bindings' descriptions.
	 */
	public String frameWheelBindingsDescription() {
		String description = new String();
		for (Entry<Integer, DOF_6Action> entry : frameWheelActions.map().entrySet())
			if (DLEvent.getModifiersText(entry.getKey()).length() != 0 )
				description += "Wheel " + DLEvent.getModifiersText(entry.getKey()) + " -> " + entry.getValue().description() + "\n";
			else
				description += "Wheel -> " + entry.getValue().description() + "\n";
		return description;
	}

	// 3. Bindings
	
	// 3.1 keyboard wrappers
	
	/**
	 * Defines a camera keyboard shortcut to bind the given action.
	 * 
	 * @param key shortcut
	 * @param action action to be binded
	 */
	public void setShortcut(Character key, DOF_0Action action) {
		if ( isKeyInUse(key) ) {
			DOF_0Action a = shortcut(key);
			System.out.println("Warning: overwritting shortcut which was previously binded to " + a);
		}
		keyboard.setBinding(new KeyboardShortcut(key), action);
	}
	
	/**
	 * Defines a camera keyboard shortcut to bind the given action.
	 * <p>
	 * High-level version of {@link #setShortcut(Integer, Integer, DOF_0Action)}.
	 * 
	 * @param mask modifier mask defining the shortcut
	 * @param key character (internally converted to a key coded) defining the shortcut
	 * @param action action to be binded
	 * 
	 * @see #setShortcut(Integer, Integer, DOF_0Action)
	 */
	public void setShortcut(Integer mask, Character key, DOF_0Action action) {
		setShortcut(mask, DLKeyEvent.getKeyCode(key), action);
	}
	
	/**
	 * Defines a camera keyboard shortcut to bind the given action.
	 * <p>
	 * Low-level version of {@link #setShortcut(Integer, Character, DOF_0Action)}.
	 * 
	 * @param mask modifier mask defining the shortcut
	 * @param vKey coded key defining the shortcut
	 * @param action action to be binded
	 * 
	 * @see #setShortcut(Integer, Character, DOF_0Action)
	 */
	public void setShortcut(Integer mask, Integer vKey, DOF_0Action action) {
		if ( isKeyInUse(mask, vKey) ) {
			DOF_0Action a = shortcut(mask, vKey);
			System.out.println("Warning: overwritting shortcut which was previously binded to " + a);
		}
		keyboard.setBinding(new KeyboardShortcut(mask, vKey), action);
	}
	
	/**
	 * Defines a camera keyboard shortcut to bind the given action.
	 * 
	 * @param vKey coded key (such PApplet.UP) that defines the shortcut
	 * @param action action to be binded
	 */
	public void setShortcut(Integer vKey, DOF_0Action action) {
		if ( isKeyInUse(vKey) ) {
			DOF_0Action a = shortcut(vKey);
			System.out.println("Warning: overwritting shortcut which was previously binded to " + a);
		}
		keyboard.setBinding(new KeyboardShortcut(vKey), action);
	}

	/**
	 * Removes all camera keyboard shortcuts.
	 */
	public void removeAllShortcuts() {
		keyboard.removeAllBindings();
	}
	
	/**
	 * Removes the camera keyboard shortcut.
	 * 
	 * @param key shortcut
	 */
	public void removeShortcut(Character key) {
		keyboard.removeBinding(new KeyboardShortcut(key));
	}
	
	/**
	 * Removes the camera keyboard shortcut.
	 * <p>
	 * High-level version of {@link #removeShortcut(Integer, Integer)}.
	 * 
	 * @param mask modifier mask that defining the shortcut
	 * @param key character (internally converted to a key coded) defining the shortcut
	 * 
	 * @see #removeShortcut(Integer, Integer)
	 */
	public void removeShortcut(Integer mask, Character key) {
		removeShortcut(mask, DLKeyEvent.getKeyCode(key));
	}
	
	/**
	 * Removes the camera keyboard shortcut.
	 * <p>
	 * low-level version of {@link #removeShortcut(Integer, Character)}.
	 * 
	 * @param mask modifier mask that defining the shortcut
	 * @param vKey coded key defining the shortcut
	 * 
	 * @see #removeShortcut(Integer, Character)
	 */
	public void removeShortcut(Integer mask, Integer vKey) {
		keyboard.removeBinding(new KeyboardShortcut(mask, vKey));
	}

	/**
	 * Removes the camera keyboard shortcut.
	 * 
	 * @param vKey coded key (such PApplet.UP) that defines the shortcut
	 */
	public void removeShortcut(Integer vKey) {
		keyboard.removeBinding(new KeyboardShortcut(vKey));
	}

	/**
	 * Returns the action that is binded to the given camera keyboard shortcut.
	 * 
	 * @param key shortcut
	 * @return action
	 */
	public DOF_0Action shortcut(Character key) {
		return keyboard.binding(new KeyboardShortcut(key));
	}
	
  /**
   * Returns the action that is binded to the given camera keyboard shortcut.
   * <p>
   * High-level version of {@link #shortcut(Integer, Integer)}
   * 
   * @param mask modifier mask defining the shortcut
	 * @param key character (internally converted to a coded key) defining the shortcut
   * @return action
   * 
   * @see #shortcut(Integer, Integer)
   */
	public DOF_0Action shortcut(Integer mask, Character key) {
		return shortcut(mask, DLKeyEvent.getKeyCode(key));
	}

	/**
   * Returns the action that is binded to the given camera keyboard shortcut.
   * <p>
   * Low-level version of {@link #shortcut(Integer, Character)}
   * 
   * @param mask modifier mask defining the shortcut
	 * @param vKey coded key defining the shortcut
   * @return action
   * 
   * @see #shortcut(Integer, Character)
   */
	public DOF_0Action shortcut(Integer mask, Integer vKey) {
		return keyboard.binding(new KeyboardShortcut(mask, vKey));
	}

	/**
	 * Returns the action that is binded to the given camera keyboard shortcut.
	 * 
	 * @param vKey coded key (such PApplet.UP) that defines the shortcut
	 * @return action
	 */
	public DOF_0Action shortcut(Integer vKey) {
		return keyboard.binding(new KeyboardShortcut(vKey));
	}

	/**
	 * Returns true if the given camera keyboard shortcut binds an action.
	 * 
	 * @param key shortcut
	 */
	public boolean isKeyInUse(Character key) {
		return keyboard.isShortcutInUse(new KeyboardShortcut(key));
	}
	
	/**
	 * Returns true if the given camera keyboard shortcut binds an action.
	 * <p>
	 * High-level version of {@link #isKeyInUse(Integer, Integer)}.
   * 
   * @param mask modifier mask defining the shortcut
	 * @param key character (internally converted to a coded key) defining the shortcut
	 * 
	 * @see #isKeyInUse(Integer, Integer)
	 */
	public boolean isKeyInUse(Integer mask, Character key) {
		return isKeyInUse(mask, DLKeyEvent.getKeyCode(key));
	}
	
	/**
	 * Returns true if the given camera keyboard shortcut binds an action.
	 * <p>
	 * Low-level version of {@link #isKeyInUse(Integer, Character)}.
	 * 
	 * @param mask modifier mask defining the shortcut
	 * @param vKey coded key defining the shortcut
	 * 
	 * @see #isKeyInUse(Integer, Character)
	 */
	public boolean isKeyInUse(Integer mask, Integer vKey) {
		return keyboard.isShortcutInUse(new KeyboardShortcut(mask, vKey));
	}
	
	/**
	 * Returns true if the given camera keyboard shortcut binds an action.
	 * 
	 * @param vKey coded key (such PApplet.UP) that defines the shortcut
	 */
	public boolean isKeyInUse(Integer vKey) {
		return keyboard.isShortcutInUse(new KeyboardShortcut(vKey));
	}

	/**
	 * Returns true if there is a camera keyboard shortcut for the given action.
	 */
	public boolean isKeyboardActionBinded(DOF_0Action action) {
		return keyboard.isActionMapped(action);
	}

	// camera wrappers:
	
	/**
	 * Removes all camera mouse-action bindings.
	 */
	public void removeAllCameraMouseBindings() {
		cameraActions.removeAllBindings();
	}

	/**
	 * Returns true if the given binding binds a camera mouse-action.
	 * 
	 * @param button
	 */	
	public boolean isCameraMouseBindingInUse(Integer button) {
		return cameraActions.isShortcutInUse(new ButtonShortcut(button));
	}
	
	/**
	 * Returns true if the given binding binds a camera mouse-action.
	 * 
	 * @param mask
	 * @param button
	 */
	public boolean isCameraMouseBindingInUse(Integer mask, Integer button) {
		return cameraActions.isShortcutInUse(new ButtonShortcut(mask, button));
	}

	/**
	 * Returns true if the given camera mouse-action is binded.
	 */
	public boolean isCameraMouseActionBinded(DOF_6Action action) {
		return cameraActions.isActionMapped(action);
	}

	/**
	 * Binds the camera mouse-action to the given binding
	 * 
	 * @param button
	 * @param action 
	 */
	public void setCameraMouseBinding(Integer button, DOF_6Action action) {
		if ( isCameraMouseBindingInUse(button) ) {
			DOF_6Action a = cameraMouseBinding(button);
			System.out.println("Warning: overwritting binding which was previously associated to " + a);
		}
		cameraActions.setBinding(new ButtonShortcut(button), action);
	}
	
	/**
	 * Binds the camera mouse-action to the given binding
	 * 
	 * @param mask
	 * @param button
	 * @param action
	 * 
	 * <b>Attention:</b> Mac users should avoid using the CTRL modifier key, since its use is
	 * reserved to emulate the right button of the mouse.
	 */
	public void setCameraMouseBinding(Integer mask, Integer button, DOF_6Action action) {
		if ( isCameraMouseBindingInUse(mask, button) ) {
			DOF_6Action a = cameraMouseBinding(mask, button);
			System.out.println("Warning: overwritting binding which was previously associated to " + a);
		}
		cameraActions.setBinding(new ButtonShortcut(mask, button), action);
	}

	/**
	 * Removes the camera mouse-action binding.
	 * 
	 * @param button
	 */
	public void removeCameraMouseBinding(Integer button) {
		cameraActions.removeBinding(new ButtonShortcut(button));
	}
	
	/**
	 * Removes the camera mouse-action binding.
	 * 
	 * @param mask
	 * @param button
	 */
	public void removeCameraMouseBinding(Integer mask, Integer button) {
		cameraActions.removeBinding(new ButtonShortcut(mask, button));
	}	
	
	/**
	 * Returns the camera mouse-action associated to the given binding.
	 * 
	 * @param button
	 */
	public DOF_6Action cameraMouseBinding(Integer button) {
		return cameraActions.binding(new ButtonShortcut(button));
	}
	
	/**
	 * Returns the camera mouse-action associated to the given binding.
	 * 
	 * @param mask
	 * @param button
	 */
	public DOF_6Action cameraMouseBinding(Integer mask, Integer button) {
		return cameraActions.binding(new ButtonShortcut(mask, button));
	}

	// iFrame wrappers:
	
	/**
	 * Removes all frame mouse-action bindings.
	 */
	public void removeAllFrameMouseBindings() {
		frameActions.removeAllBindings();
	}

	/**
	 * Returns true if the given binding binds a frame mouse-action.
	 * 
	 * @param button
	 */	
	public boolean isFrameMouseBindingInUse(Integer button) {
		return frameActions.isShortcutInUse(new ButtonShortcut(button));
	}
	
	/**
	 * Returns true if the given binding binds a frame mouse-action.
	 * 
	 * @param mask
	 * @param button
	 */
	public boolean isFrameMouseBindingInUse(Integer mask, Integer button) {
		return frameActions.isShortcutInUse(new ButtonShortcut(mask, button));
	}

	/**
	 * Returns true if the given frame mouse-action is binded.
	 */
	public boolean isFrameMouseActionBinded(DOF_6Action action) {
		return frameActions.isActionMapped(action);
	}

	/**
	 * Binds the frame mouse-action to the given binding
	 * 
	 * @param button
	 * @param action 
	 */
	public void setFrameMouseBinding(Integer button,DOF_6Action action) {
		if ( isFrameMouseBindingInUse(button) ) {
			DOF_6Action a = frameMouseBinding(button);
			System.out.println("Warning: overwritting binding which was previously associated to " + a);
		}
		frameActions.setBinding(new ButtonShortcut(button), action);
	}
	
	/**
	 * Binds the frame mouse-action to the given binding
	 * 
	 * @param mask
	 * @param button
	 * @param action
	 * 
	 * <b>Attention:</b> Mac users should avoid using the CTRL modifier key, since its use is
	 * reserved to emulate the right button of the mouse.
	 */
	public void setFrameMouseBinding(Integer mask, Integer button,DOF_6Action action) {
		if ( isFrameMouseBindingInUse(mask, button) ) {
			DOF_6Action a = frameMouseBinding(mask, button);
			System.out.println("Warning: overwritting binding which was previously associated to " + a);
		}
		frameActions.setBinding(new ButtonShortcut(mask, button), action);
	}

	/**
	 * Removes the frame mouse-action binding.
	 * 
	 * @param button
	 */
	public void removeFrameMouseBinding(Integer button) {
		frameActions.removeBinding(new ButtonShortcut(button));
	}
	
	/**
	 * Removes the frame mouse-action binding.
	 * 
	 * @param mask
	 * @param button
	 */
	public void removeFrameMouseBinding(Integer mask, Integer button) {
		frameActions.removeBinding(new ButtonShortcut(mask, button));
	}	
	
	/**
	 * Returns the frame mouse-action associated to the given binding.
	 * 
	 * @param button
	 */
	public DOF_6Action frameMouseBinding(Integer button) {
		return frameActions.binding(new ButtonShortcut(button));
	}
	
	/**
	 * Returns the frame mouse-action associated to the given binding.
	 * 
	 * @param mask
	 * @param button
	 */
	public DOF_6Action frameMouseBinding(Integer mask, Integer button) {
		return frameActions.binding(new ButtonShortcut(mask, button));
	}
	
	// click wrappers:
	
  /**
   * Removes all camera mouse-click bindings.
   */
  public void removeAllClickBindings() {
  	clickActions.removeAllBindings();
  }
  
  /**
   * Returns true if the given binding binds a click-action.
   *      
   * @param button binding
   */
  public boolean isClickBindingInUse(Integer button) {
          return clickActions.isShortcutInUse(new ClickShortcut(button));
  }
  
  /**
   * Returns true if the given binding binds a click-action.
   * 
   * @param mask modifier mask defining the binding
   * @param button button defining the binding
   */
  /**
  public boolean isClickBindingInUse(Integer mask, Integer button) {
          return clickActions.isShortcutInUse(new ClickBinding(mask, button));
  }
  */
  
  /**
   * Returns true if the given binding binds a click-action.
   * 
   * @param button button defining the binding
   * @param nc number of clicks defining the binding
   */
  public boolean isClickBindingInUse(Integer button, Integer nc) {
          return clickActions.isShortcutInUse(new ClickShortcut(button, nc));
  }

  /**
   * Returns true if the given binding binds a click-action.
   * 
   * @param mask modifier mask defining the binding
   * @param button button defining the binding
   * @param nc number of clicks defining the binding
   */
  public boolean isClickBindingInUse(Integer mask, Integer button, Integer nc) {
          return clickActions.isShortcutInUse(new ClickShortcut(mask, button, nc)); 
  }

  /** 
   * Returns true if the given click-action is binded.
   */
  public boolean isClickActionBinded(DOF_0Action action) {
          return clickActions.isActionMapped(action);
  }
  
  /**
   * Binds the click-action to the given binding.
   * 
   * @param button binding
   * @param action action to be binded
   */
  public void setClickBinding(Integer button, DOF_0Action action) {
          if ( isClickBindingInUse(button) ) {
                  DOF_0Action a = clickBinding(button);
                  System.out.println("Warning: overwritting binding which was previously associated to " + a);
          }
          clickActions.setBinding(new ClickShortcut(button), action);
  }

  /**
   * Binds the click-action to the given binding.
   * 
   * @param mask modifier mask defining the binding
   * @param button mouse button defining the binding
   * @param action action to be binded
   * 
   * <b>Attention:</b> Mac users should avoid using the CTRL modifier key, since its use is
   * reserved to emulate the right button of the mouse.
   */
  /**
  public void setClickBinding(Integer mask, Integer button, DLClickAction action) {
          if ( isClickBindingInUse(mask, button) ) {
                  ClickAction a = clickBinding(mask, button);
                  System.out.println("Warning: overwritting bindings which was previously associated to " + a);
          }
          clickActions.setBinding(new ClickBinding(mask, button), action);
  }
  */
  
  /**
   * Binds the click-action to the given binding.
   * 
   * @param button mouse button defining the binding
   * @param nc number of clicks that defines the binding
   * @param action action to be binded
   */
  public void setClickBinding(Integer button, Integer nc, DOF_0Action action) {
          if ( isClickBindingInUse(button, nc) ) {
                  DOF_0Action a = clickBinding(button, nc);
                  System.out.println("Warning: overwritting binding which was previously associated to " + a);
          }
          clickActions.setBinding(new ClickShortcut(button, nc), action);
  }

  /**
   * Binds the click-action to the given binding.
   * 
   * @param mask modifier mask defining the binding
   * @param button mouse button defining the binding
   * @param nc number of clicks that defines the binding
   * @param action action to be binded
   * 
   * <b>Attention:</b> Mac users should avoid using the CTRL modifier key, since its use is
   * reserved to emulate the right button of the mouse.
   */
  public void setClickBinding(Integer mask, Integer button, Integer nc, DOF_0Action action) {
          if ( isClickBindingInUse(mask, button, nc) ) {
                  DOF_0Action a = clickBinding(mask, button, nc);
                  System.out.println("Warning: overwritting binding which was previously associated to " + a);
          }
          clickActions.setBinding(new ClickShortcut(mask, button, nc), action);
  }
  
  /**
   * Removes the mouse-click binding.
   * 
   * @param button binding
   */
  public void removeClickBinding(Integer button) {
          clickActions.removeBinding(new ClickShortcut(button));
  }

  /**
   * Removes the mouse-click binding.
   * 
   * @param mask modifier mask defining the binding
   * @param button mouse button defining the binding
   */
  /**
  public void removeClickBinding(Integer mask, Integer button) {
          clickActions.removeBinding(new ClickBinding(mask, button));
  }
  */
  
  /**
   * Removes the mouse-click binding.
   * 
   * @param button mouse button defining the binding
   * @param nc number of clicks defining the binding
   */
  public void removeClickBinding(Integer button, Integer nc) {
          clickActions.removeBinding(new ClickShortcut(button, nc));
  }
  
  /**
   * Removes the mouse-click binding.
   * 
   * @param mask modifier mask defining the binding
   * @param button mouse button defining the binding
   * @param nc number of clicks defining the binding
   */
  public void removeClickBinding(Integer mask, Integer button, Integer nc) {
          clickActions.removeBinding(new ClickShortcut(mask, button, nc));
  }
  
  /**
   * Returns the click-action associated to the given binding.
   * 
   * @param button binding
   */
  public DOF_0Action clickBinding(Integer button) {
          return clickActions.binding(new ClickShortcut(button));
  }

  /**
   * Returns the click-action associated to the given binding.
   * 
   * @param mask modifier mask defining the binding
   * @param button mouse button defining the binding
   */
  /**
  public Scene.ClickAction clickBinding(Integer mask, Integer button) {
          return clickActions.binding(new ClickBinding(mask, button));
  }
  */
  
  /**
   * Returns the click-action associated to the given binding.
   * 
   * @param button mouse button defining the binding
   * @param nc number of clicks defining the binding
   */
  public DOF_0Action clickBinding(Integer button, Integer nc) {
          return clickActions.binding(new ClickShortcut(button, nc));
  }

  /**
   * Returns the click-action associated to the given binding.
   * 
   * @param mask modifier mask defining the binding
   * @param button mouse button defining the binding
   * @param nc number of clicks defining the binding
   */
  public DOF_0Action clickBinding(Integer mask, Integer button, Integer nc) {
  	return clickActions.binding(new ClickShortcut(mask, button, nc));
  }
  
	
	// wheel
	//Camera Wheel
	
	/**
	 * Removes all camera wheel-action bindings.
	 */
	public void removeAllCameraWheelBindings() {
		cameraWheelActions.removeAllBindings();
	}
	
	/**
	 * Returns true if the given binding binds a camera wheel-action.
	 * 
	 * @param mask binding
	 */
	public boolean isCameraWheelBindingInUse(Integer mask) {
		return cameraWheelActions.isShortcutInUse(mask);
	}

	/**
	 * Returns true if the given camera wheel-action is binded.
	 * 
	 * @param action
	 */
	public boolean isCameraWheelActionBinded(DOF_6Action action) {
		return cameraWheelActions.isActionMapped(action);
	}
	
	/**
	 * Convenience function that simply calls {@code setCameraWheelShortcut(0, action)}
	 * 
	 * @see #setCameraWheelBinding(Integer, DOF_6Action)
	 */
	public void setCameraWheelBinding(DOF_6Action action) {
		setCameraWheelBinding(0, action);
	}

	/**
	 * Binds the camera wheel-action to the given binding.
	 * 
	 * @param mask modifier mask defining the binding
	 * 
	 * @see #setCameraWheelBinding(DOF_6Action)
	 * 
	 * <b>Attention:</b> Mac users should avoid using the CTRL modifier key, since its use is
	 * reserved to emulate the right button of the mouse.
	 */
	public void setCameraWheelBinding(Integer mask, DOF_6Action action) {
		if ( isCameraWheelBindingInUse(mask) ) {
			DOF_6Action a = cameraWheelBinding(mask);
			System.out.println("Warning: overwritting binding which was previously associated to " + a);
		}
		cameraWheelActions.setBinding(mask, action);
	}
	
	/**
	 * Convenience function that simply calls {@code removeCameraWheelShortcut(0)}.
	 * 
	 * @see #removeCameraWheelBinding(Integer)
	 */
	public void removeCameraWheelBinding() {
		removeCameraWheelBinding(0);
	}

	/**
	 * Removes the camera wheel-action binding.
	 * 
	 * @param mask shortcut
	 * 
	 * @see #removeCameraWheelBinding()
	 */
	public void removeCameraWheelBinding(Integer mask) {
		cameraWheelActions.removeBinding(mask);
	}

	/**
	 * Convenience function that simply returns {@code cameraWheelActions.binding(0)}.
	 * 
	 * @see #cameraWheelBinding(Integer)
	 */
	public DOF_6Action cameraWheelBinding() {
		return cameraWheelActions.binding(0);
	}
	
	/**
	 * Returns the camera wheel-action associated to the given binding.
	 * 
	 * @param mask binding
	 * 
	 * @see #cameraWheelBinding()
	 */
	public DOF_6Action cameraWheelBinding(Integer mask) {
		return cameraWheelActions.binding(mask);
	}
	
  //Frame Wheel
	
	/**
	 * Removes all frame wheel-action bindings.
	 */
	public void removeAllFrameWheelBindings() {
		frameWheelActions.removeAllBindings();
	}

	/**
	 * Returns true if the given binding binds a frame wheel-action.
	 * 
	 * @param mask shortcut
	 */
	public boolean isFrameWheelBindingInUse(Integer mask) {
		return frameWheelActions.isShortcutInUse(mask);
	}

	/**
	 * Returns true if the given camera wheel-action is binded.
	 * 
	 * @param action
	 */
	public boolean isFrameWheelActionBinded(DOF_6Action action) {
		return frameWheelActions.isActionMapped(action);
	}

	/**
	 * Convenience function that simply calls {@code setFrameWheelShortcut(0, action)}
	 * 
	 * @see #setCameraWheelBinding(Integer, DOF_6Action)
	 */
	public void setFrameWheelBinding(DOF_6Action action) {
		setFrameWheelBinding(0, action);
	}
	
	/**
	 * Binds the camera wheel-action to the given binding.
	 * 
	 * @param mask modifier mask defining the binding
	 * 
	 * @see #setFrameWheelBinding(DOF_6Action)
	 * 
	 * <b>Attention:</b> Mac users should avoid using the CTRL modifier key, since its use is
	 * reserved to emulate the right button of the mouse.
	 */
	public void setFrameWheelBinding(Integer mask, DOF_6Action action) {
		if ( isFrameWheelBindingInUse(mask) ) {
			DOF_6Action a = frameWheelBinding(mask);
			System.out.println("Warning: overwritting binding which was previously associated to " + a);
		}
		frameWheelActions.setBinding(mask, action);
	}
	
	/**
	 * Convenience function that simply calls {@code removeFrameWheelShortcut(0)}.
	 * 
	 * @see #removeFrameWheelBinding(Integer)
	 */
	public void removeFrameWheelBinding() {
		removeFrameWheelBinding(0);
	}

	/**
	 * Removes the frame wheel-action binding.
	 * 
	 * @param mask binding
	 * 
	 * @see #removeFrameWheelBinding()
	 */
	public void removeFrameWheelBinding(Integer mask) {
		frameWheelActions.removeBinding(mask);
	}
	
	/**
	 * Convenience function that simply returns {@code cameraFrameActions.binding(0)}.
	 * 
	 * @see #frameWheelBinding(Integer)
	 */
	public DOF_6Action frameWheelBinding() {
		return frameWheelBinding(0);
	}
	
	/**
	 * Returns the frame wheel-action associated to the given binding.
	 * 
	 * @param mask binding
	 * 
	 * @see #frameWheelBinding()
	 */
	public DOF_6Action frameWheelBinding(Integer mask) {
		return frameWheelActions.binding(mask);
	}
}