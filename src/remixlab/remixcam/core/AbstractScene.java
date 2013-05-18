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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import remixlab.remixcam.action.*;
import remixlab.remixcam.device.HIDevice;
import remixlab.remixcam.event.*;
import remixlab.remixcam.geom.*;
import remixlab.remixcam.profile.*;
import remixlab.remixcam.renderer.*;
import remixlab.remixcam.shortcut.KeyboardShortcut;
import remixlab.remixcam.util.*;

public abstract class AbstractScene implements Constants {	
	protected boolean dottedGrid;	
	
  //O B J E C T S
	protected Renderable renderer;
	protected Pinhole ph;
	protected InteractiveFrame glIFrame;
	protected boolean iFrameIsDrwn;
	protected Trackable trck;
	public boolean avatarIsInteractiveDrivableFrame;
	protected boolean avatarIsInteractiveAvatarFrame;
	
  //O B J E C T S
	//TODO pending
	protected EventDispatcher eventDispatcher;
	
  //E X C E P T I O N H A N D L I N G
	protected int startCoordCalls;
	
  // T i m e r P o o l
  //T I M E R S
  protected boolean singleThreadedTaskableTimers;
	protected ArrayList<AbstractTimerJob> timerPool;

	// M o u s e G r a b b e r
	protected List<DeviceGrabbable> msGrabberPool;
	protected DeviceGrabbable deviceGrbbr;
	public boolean deviceGrabberIsAnIFrame;	
	protected boolean deviceTrckn;

	// D I S P L A Y F L A G S
	protected boolean axisIsDrwn; // world axis
	protected boolean gridIsDrwn; // world XY grid
	protected boolean frameSelectionHintIsDrwn;
	protected boolean cameraPathsAreDrwn;
	
  //C O N S T R A I N T S
	//TODO deactivate globally, see iFrameprevConstraint
	//protected boolean withConstraint;
	
	// LEFT vs RIGHT_HAND
	protected boolean rightHanded;
	
  //A N I M A T I O N
	protected SingleThreadedTimer animationTimer;
	protected boolean animationStarted;
	public boolean animatedFrameWasTriggered;
	protected long animationPeriod;	
	
  //D E V I C E S	  &   E V E N T S
	protected ArrayList<HIDevice> devices;
	protected LinkedList<DLEvent> eventQueue;
	
	// L O C A L   T I M E R
	protected boolean arpFlag;
	protected boolean pupFlag;
	protected Vector3D pupVec;
	protected AbstractTimerJob timerFx;
	
	// S I Z E
	protected int width, height;
	
  //K E Y B O A R D A N D M O U S E
	protected boolean mouseHandling;
	protected boolean keyboardHandling;
	
  //c a m e r a p r o f i l e s
	protected HashMap<String, CameraProfile> cameraProfileMap;
	protected ArrayList<String> cameraProfileNames;
	protected CameraProfile currentCameraProfile;
	
  //S h o r t c u t k e y s
	protected Bindings<KeyboardShortcut, DOF_0Action> gProfile;
	
  //K E Y F R A M E S
	protected Bindings<Integer, Integer> pathKeys;
	public Integer addKeyFrameKeyboardModifier;
	public Integer deleteKeyFrameKeyboardModifier;
	
	//offscreen
	public Point upperLeftCorner;
	protected boolean offscreen;
	
	/**
   * The system variables <b>cursorX</b> and <b>cursorY</b> always contains the current horizontal
   * and vertical coordinates of the mouse.
   */ 
  public int cursorX, cursorY, pcursorX, pcursorY;
	
	protected long frameCount;
	protected float frameRate;
	protected long frameRateLastNanos;
	
	public AbstractScene() {		
		frameCount = 0;
		frameRate = 10;
		frameRateLastNanos = 0;
		
	  // E X C E P T I O N H A N D L I N G
	  startCoordCalls = 0;
			
	  // 1 ->
	  //TODO testing
		//To define the timers to be used pass the flag in the constructor?
		singleThreadedTaskableTimers = true;
		//drawing timer pool
		timerPool = new ArrayList<AbstractTimerJob>();
		timerFx = new AbstractTimerJob() {
			public void execute() {
				unSetTimerFlag();
				}
			};
		// TODO 
	  // bug 1: registration of this timer vs singleThreadedTaskableTimers state
		// has to do with calling abstract methods from here!
	  // otherwise need to be set from here
		// bug2: poor performance when doing: animation + camera kfi interpolation + drawing the cam path
	  // but luckyly seems to be only for P3D
		registerJob(timerFx);
		
		//mouse grabber pool
		msGrabberPool = new ArrayList<DeviceGrabbable>();
		//devices
		devices = new ArrayList<HIDevice>();
		//events
		eventQueue = new LinkedList<DLEvent>();
		// <- 1
		
		setDottedGrid(true);
		setRightHanded();
		
		gProfile = new Bindings<KeyboardShortcut, DOF_0Action>(this);
		pathKeys = new Bindings<Integer, Integer>(this);		
		setDefaultShortcuts();
	}
	
	protected void setRenderer(Renderable r) {
		renderer = r;
	}
		
	public Renderable renderer() {
		return renderer;
	}	
	
	public boolean gridIsDotted() {
		return dottedGrid;
	}
	
	public void setDottedGrid(boolean dotted) {
		dottedGrid = dotted;
	}
	
	/**
	 * Returns {@code true} if this Scene is associated to an offscreen 
	 * renderer and {@code false} otherwise.
	 * 
	 * @see #Scene(PApplet, PGraphicsOpenGL)
	 */	
	public boolean isOffscreen() {
		return offscreen;
	}	
	
	/**
	 * Sets the interactivity to the Scene {@link #interactiveFrame()} instance
	 * according to {@code draw}
	 */
	public void setDrawInteractiveFrame(boolean draw) {
		if (draw && (glIFrame == null))
			return;
		if (!draw && currentCameraProfile() instanceof ThirdPersonCameraProfile
				&& interactiveFrame().equals(avatar()))// more natural than to bypass it
			return;
		iFrameIsDrwn = draw;
	}
	
	// E V E N T   HA N D L I N G
	
	/**
	 * Internal method. Handles the different global keyboard actions.
	 */
	public void handleKeyboardAction(DOF_0Action id) {			
		//if( !keyboardIsHandled() )
			//return;
		switch (id) {
		case DRAW_AXIS:
			toggleAxisIsDrawn();
			break;
		case DRAW_GRID:
			toggleGridIsDrawn();
			break;
		case CAMERA_PROFILE:
			nextCameraProfile();
			break;
		case CAMERA_TYPE:
			toggleCameraType();
			break;
		case CAMERA_KIND:
			toggleCameraKind();
			break;
		case ANIMATION:
			toggleAnimation();
			break;
		case ARP_FROM_PIXEL:
			if (setArcballReferencePointFromPixel(new Point(cursorX, cursorY))) {
				arpFlag = true;
				timerFx.runOnce(1000);					
			}
			break;
		case RESET_ARP:
			pinhole().setArcballReferencePoint(new Vector3D(0, 0, 0));
			arpFlag = true;
			timerFx.runOnce(1000);				
			break;
		case GLOBAL_HELP:
			displayGlobalHelp();
			break;
		case CURRENT_CAMERA_PROFILE_HELP:
			displayCurrentCameraProfileHelp();
			break;
		case EDIT_CAMERA_PATH:
			toggleCameraPathsAreDrawn();
			break;
		case FOCUS_INTERACTIVE_FRAME:
			toggleDrawInteractiveFrame();
			break;
		case DRAW_FRAME_SELECTION_HINT:
			toggleFrameSelectionHintIsDrawn();
			break;
		case CONSTRAIN_FRAME:
			//toggleDrawWithConstraint();
			break;
		}
	}
	
	/**
	 * Internal method. Handles the different camera keyboard actions.
	 */
	public void handleCameraKeyboardAction(DOF_0Action id) {
		System.out.println("enter handleCameraKeyboardAction");
		//if( !keyboardIsHandled() )
			//return;
		if( !id.is2D() && this.is2D() )
			return;
		Vector3D trans;
		switch (id) {		
		case INTERPOLATE_TO_ZOOM_ON_PIXEL:
			if( this.is3D() ) {
				Camera.WorldPoint wP = camera().interpolateToZoomOnPixel(new Point(cursorX, cursorY));
				if (wP.found) {
					pupVec = wP.point;
					pupFlag = true;
					timerFx.runOnce(1000);
				}
			}
			else {
				viewWindow().interpolateToZoomOnPixel(new Point(cursorX, cursorY));
				pupVec = viewWindow().unprojectedCoordinatesOf(new Vector3D((float)cursorX, (float)cursorY, 0.5f));
				pupFlag = true;
				timerFx.runOnce(1000);
			}
			break;
		case INTERPOLATE_TO_FIT_SCENE:
			pinhole().interpolateToFitScene();
			break;
		case SHOW_ALL:
			showAll();
			break;
		case MOVE_CAMERA_LEFT:
			trans = new Vector3D(-10.0f * pinhole().flySpeed(), 0.0f, 0.0f);
			if(this.is3D())
				trans.div(camera().frame().magnitude());
			pinhole().frame().translate(pinhole().frame().inverseTransformOf(trans));			
			break;
		case MOVE_CAMERA_RIGHT:
			trans = new Vector3D(10.0f * pinhole().flySpeed(), 0.0f, 0.0f);
			if(this.is3D())
				trans.div(camera().frame().magnitude());
			pinhole().frame().translate(pinhole().frame().inverseTransformOf(trans));			
			break;
		case MOVE_CAMERA_UP:
			trans = pinhole().frame().inverseTransformOf(new Vector3D(0.0f, isRightHanded() ? 10.0f : -10.0f * pinhole().flySpeed(), 0.0f));
			if(this.is3D())
				trans.div(camera().frame().magnitude());
			pinhole().frame().translate(trans);					  
			break;
		case MOVE_CAMERA_DOWN:
			trans = pinhole().frame().inverseTransformOf(new Vector3D(0.0f, isRightHanded() ? -10.0f : 10.0f * pinhole().flySpeed(), 0.0f));
			if(this.is3D())
				trans.div(camera().frame().magnitude());
			pinhole().frame().translate(trans);			
			break;
		case INCREASE_ROTATION_SENSITIVITY:
			pinhole().setRotationSensitivity(pinhole().rotationSensitivity() * 1.2f);
			break;
		case DECREASE_ROTATION_SENSITIVITY:
			pinhole().setRotationSensitivity(pinhole().rotationSensitivity() / 1.2f);
			break;
		case INCREASE_CAMERA_FLY_SPEED:
			((Camera) pinhole()).setFlySpeed(((Camera) pinhole()).flySpeed() * 1.2f);
			break;
		case DECREASE_CAMERA_FLY_SPEED:
			((Camera) pinhole()).setFlySpeed(((Camera) pinhole()).flySpeed() / 1.2f);
			break;
		case INCREASE_AVATAR_FLY_SPEED:
			if (avatar() != null)
				if (avatarIsInteractiveDrivableFrame)
					((InteractiveDrivableFrame) avatar()).setFlySpeed(((InteractiveDrivableFrame) avatar()).flySpeed() * 1.2f);
			break;
		case DECREASE_AVATAR_FLY_SPEED:
			if (avatar() != null)
				if (avatarIsInteractiveDrivableFrame)
					((InteractiveDrivableFrame) avatar()).setFlySpeed(((InteractiveDrivableFrame) avatar()).flySpeed() / 1.2f);
			break;
		case INCREASE_AZYMUTH:
			if (avatar() != null)
				if (avatarIsInteractiveAvatarFrame)
					((InteractiveAvatarFrame) avatar()).setAzimuth(((InteractiveAvatarFrame) avatar()).azimuth() + PI / 64);
			break;
		case DECREASE_AZYMUTH:
			if (avatar() != null)
				if (avatarIsInteractiveAvatarFrame)
					((InteractiveAvatarFrame) avatar()).setAzimuth(((InteractiveAvatarFrame) avatar()).azimuth() - PI / 64);
			break;
		case INCREASE_INCLINATION:
			if (avatar() != null)
				if (avatarIsInteractiveAvatarFrame)
					((InteractiveAvatarFrame) avatar()).setInclination(((InteractiveAvatarFrame) avatar()).inclination() + PI / 64);
			break;
		case DECREASE_INCLINATION:
			if (avatar() != null)
				if (avatarIsInteractiveAvatarFrame)
					((InteractiveAvatarFrame) avatar()).setInclination(((InteractiveAvatarFrame) avatar()).inclination() - PI / 64);
			break;
		case INCREASE_TRACKING_DISTANCE:
			if (avatar() != null)
				if (avatarIsInteractiveAvatarFrame)
					((InteractiveAvatarFrame) avatar()).setTrackingDistance(((InteractiveAvatarFrame) avatar()).trackingDistance() + radius() / 50);
			break;
		case DECREASE_TRACKING_DISTANCE:
			if (avatar() != null)
				if (avatarIsInteractiveAvatarFrame)
					((InteractiveAvatarFrame) avatar()).setTrackingDistance(((InteractiveAvatarFrame) avatar()).trackingDistance() - radius() / 50);
			break;
		}
	}
	
	/**
	 * Internal method. Handles the different mouse click actions.
	 */
	public void handleClickAction(DOF_0Action action) {
		// public enum ClickAction { NO_CLICK_ACTION, ZOOM_ON_PIXEL, ZOOM_TO_FIT,
		// SELECT, ARP_FROM_PIXEL, RESET_ARP,
		// CENTER_FRAME, CENTER_SCENE, SHOW_ALL, ALIGN_FRAME, ALIGN_CAMERA }
		//if( !mouseIsHandled() )
			//return;
		
		if( !action.is2D() && this.is2D() )
			return;
		
		switch (action) {
		case NO_ACTION:
			break;
		case ZOOM_ON_PIXEL:
			if (this.is2D()) {
				viewWindow().interpolateToZoomOnPixel(new Point(cursorX, cursorY));
				pupVec = viewWindow().unprojectedCoordinatesOf(new Vector3D((float)cursorX, (float)cursorY, 0.5f));
				pupFlag = true;
				timerFx.runOnce(1000);
			}				
			else {
				Camera.WorldPoint wP = camera().interpolateToZoomOnPixel(new Point(cursorX, cursorY));
				if (wP.found) {
					pupVec = wP.point;
					pupFlag = true;
					timerFx.runOnce(1000);						
				}
			}
			break;
		case ZOOM_TO_FIT:
			pinhole().interpolateToFitScene();
			break;
		case ARP_FROM_PIXEL:
			if (setArcballReferencePointFromPixel(new Point(cursorX, cursorY))) {			  
				arpFlag = true;
				timerFx.runOnce(1000);					
			}
			break;
		case RESET_ARP:		  
			pinhole().setArcballReferencePoint(new Vector3D(0, 0, 0));
			arpFlag = true;
			timerFx.runOnce(1000);				
			break;
		case CENTER_FRAME:
			// TODO test 2d case
			if (interactiveFrame() != null)
				interactiveFrame().projectOnLine(pinhole().position(), pinhole().viewDirection());
			break;
		case CENTER_SCENE:
			pinhole().centerScene();
			break;
		case SHOW_ALL:
			pinhole().showEntireScene();
			break;
		case ALIGN_FRAME:
			if (interactiveFrame() != null)
				interactiveFrame().alignWithFrame(pinhole().frame());
			break;
		case ALIGN_CAMERA:
			pinhole().frame().alignWithFrame(null, true);
			break;
		}
	}
	
	// 7. Camera profiles

	/**
	 * Internal method that defines the default camera profiles: WHEELED_ARCBALL
	 * and FIRST_PERSON.
	 */
	protected void initDefaultCameraProfiles() {
		cameraProfileMap = new HashMap<String, CameraProfile>();
		cameraProfileNames = new ArrayList<String>();
		currentCameraProfile = null;
		// register here the default profiles
		//registerCameraProfile(new CameraProfile(this, "ARCBALL", CameraProfile.Mode.ARCBALL));
		registerCameraProfile( new WheeledArcballCameraProfile(this, "WHEELED_ARCBALL") );
		registerCameraProfile( new FirstPersonCameraProfile(this, "FIRST_PERSON") );
		//setCurrentCameraProfile("ARCBALL");
		setCurrentCameraProfile("WHEELED_ARCBALL");
	}

	/**
	 * Registers a camera profile. Returns true if succeeded. If there's a
	 * registered camera profile with the same name, registration will fail. 
	 * <p>
	 * <b>Attention:</b> This method doesn't make current {@code cp}. For that call
	 * {@link #setCurrentCameraProfile(CameraProfile)}.
	 * 
	 * @param cp camera profile
	 * 
	 * @see #setCurrentCameraProfile(CameraProfile)
	 * @see #unregisterCameraProfile(CameraProfile) 
	 */
	public boolean registerCameraProfile(CameraProfile cp) {
		// if(!isCameraProfileRegistered(cp)) {
		if (cp == null)
			return false;
		if (!isCameraProfileRegistered(cp)) {
			cameraProfileNames.add(cp.name());
			cameraProfileMap.put(cp.name(), cp);
			return true;
		}
		return false;
	}

	/**
	 * Convenience function that simply returns {@code unregisterCameraProfile(cp.name())}.
	 */
	public boolean unregisterCameraProfile(CameraProfile cp) {
		return unregisterCameraProfile(cp.name());
	}

	/**
	 * Unregisters the given camera profile by its name. Returns true if succeeded.
	 * Registration will fail in two cases: no camera profile is registered under
	 * the provided name, or the camera profile is the only registered camera profile which
	 * mode is different than THIRD_PERSON.
	 * <p>
	 * The last condition above guarantees that there should always be registered at least
	 * one camera profile which mode is different than THIRD_PERSON. 
	 * 
	 * @param cp camera profile
	 * @return true if succeeded
	 */
	public boolean unregisterCameraProfile(String cp) {
		if (!isCameraProfileRegistered(cp))
			return false;

		CameraProfile cProfile = cameraProfile(cp);
		int instancesDifferentThanThirdPerson = 0;

		for (CameraProfile camProfile : cameraProfileMap.values())
			if (!(camProfile instanceof ThirdPersonCameraProfile))
				instancesDifferentThanThirdPerson++;

		if (!(cProfile instanceof ThirdPersonCameraProfile)
				&& (instancesDifferentThanThirdPerson == 1))
			return false;

		if (isCurrentCameraProfile(cp))
			nextCameraProfile();

		if (cameraProfileNames.remove(cp)) {
			cameraProfileMap.remove(cp);
			return true;
		}

		return false;
	}

	/**
	 * Returns the camera profile which name matches the one provided.
	 * Returns null if there's no camera profile registered by this name.
	 * 
	 * @param name camera profile name
	 * @return camera profile object
	 */
	public CameraProfile cameraProfile(String name) {
		return cameraProfileMap.get(name);
	}
	
	/**
	 * Returns an array of the camera profile objects that are currently
	 * registered at the Scene.
	 */
	public CameraProfile [] getCameraProfiles() {		
		return cameraProfileMap.values().toArray(new CameraProfile[0]);
	}

	/**
	 * Returns true the given camera profile is currently registered.
	 */
	public boolean isCameraProfileRegistered(CameraProfile cp) {
		return cameraProfileMap.containsValue(cp);
	}

	/**
	 * Returns true if currently there's a camera profile registered by
	 * the given name.
	 */
	public boolean isCameraProfileRegistered(String name) {
		return cameraProfileMap.containsKey(name);
	}

	/**
	 * Returns the current camera profile object. Never null.
	 */
	public CameraProfile currentCameraProfile() {
		return currentCameraProfile;
	}

	/**
	 * Returns true if the {@link #currentCameraProfile()} matches 
	 * the one by the given name.
	 */
	boolean isCurrentCameraProfile(String cp) {
		return isCurrentCameraProfile(cameraProfileMap.get(cp));
	}

	/**
	 * Returns true if the {@link #currentCameraProfile()} matches 
	 * the one given.
	 */
	boolean isCurrentCameraProfile(CameraProfile cp) {
		return currentCameraProfile() == cp;
	}

	/**
	 * Set current the given camera profile. Returns true if succeeded.
	 * <p>
	 * Registers first the given camera profile if it is not registered.
	 */
	public boolean setCurrentCameraProfile(CameraProfile cp) {
		if (cp == null) {
			return false;
		}
		if (!isCameraProfileRegistered(cp))
			if (!registerCameraProfile(cp))
				return false;

		return setCurrentCameraProfile(cp.name());
	}
	
	/**
	 * Set current the camera profile associated to the given name.
	 * Returns true if succeeded.
	 * <p>
	 * This method triggers smooth transition animations
	 * when switching between camera profile modes.
	 */
	public boolean setCurrentCameraProfile(String cp) {
		CameraProfile camProfile = cameraProfileMap.get(cp);
		if (camProfile == null)
			return false;
		if ((camProfile instanceof ThirdPersonCameraProfile) && (avatar() == null))
			return false;
		else {
			if (camProfile instanceof ThirdPersonCameraProfile) {
				setDrawInteractiveFrame();
				setCameraType(Camera.Type.PERSPECTIVE);
				if (avatarIsInteractiveDrivableFrame)
					((InteractiveDrivableFrame) avatar()).removeFromDeviceGrabberPool();
				pinhole().frame().updateFlyUpVector();// ?
				pinhole().frame().stopDampedSpinning();
				if (avatarIsInteractiveDrivableFrame) {
					((InteractiveDrivableFrame) (avatar())).updateFlyUpVector();
					((InteractiveDrivableFrame) (avatar())).stopDampedSpinning();
				}
				// perform small animation ;)
				if (pinhole().anyInterpolationIsStarted())
					pinhole().stopAllInterpolations();
				// /**
				// TODO should Pinhole be non-abstract?
				Pinhole cm;
				if(is3D())
				  cm = camera().get();
				else
					cm = viewWindow().get();
				// */
				cm.setPosition(avatar().cameraPosition());
				cm.setUpVector(avatar().upVector());
				cm.lookAt(avatar().target());
				pinhole().interpolateTo(cm.frame());
				currentCameraProfile = camProfile;
			} else {
				pinhole().frame().updateFlyUpVector();
				pinhole().frame().stopDampedSpinning();
				
				if(currentCameraProfile != null)
					if (currentCameraProfile instanceof ThirdPersonCameraProfile)
						pinhole().interpolateToFitScene();
        
				currentCameraProfile = camProfile;        
				
				setDrawInteractiveFrame(false);
				if (avatarIsInteractiveDrivableFrame)
					((InteractiveDrivableFrame) avatar()).addInDeviceGrabberPool();
			}
			return true;
		}
	}

	/**
	 * Sets the next registered camera profile as current.
	 * <p>
	 * Camera profiles are ordered by their registration order.
	 */
	public void nextCameraProfile() {
		int currentCameraProfileIndex = cameraProfileNames.indexOf(currentCameraProfile().name());
		nextCameraProfile(++currentCameraProfileIndex);
	}

	/**
	 * Internal use. Used by {@link #nextCameraProfile()}.
	 */
	private void nextCameraProfile(int index) {
		if (!cameraProfileNames.isEmpty()) {
			if (index == cameraProfileNames.size())
				index = 0;

			if (!setCurrentCameraProfile(cameraProfileNames.get(index)))
				nextCameraProfile(++index);
			// debug:
			else
				System.out.println("Camera profile changed to: "
						+ cameraProfileNames.get(index));
		}
	}
	
	/**
	 * Returns a String with the {@link #currentCameraProfile()} keyboard and mouse bindings.
	 * 
	 * @see remixlab.remixcam.profile.CameraProfile#cameraDeviceBindingsDescription()
	 * @see remixlab.remixcam.profile.CameraProfile#frameDeviceBindingsDescription()
	 * @see remixlab.remixcam.profile.CameraProfile#deviceClickBindingsDescription()
	 * @see remixlab.remixcam.profile.CameraProfile#keyboardShortcutsDescription()
	 * @see remixlab.remixcam.profile.CameraProfile#cameraWheelBindingsDescription()
	 * @see remixlab.remixcam.profile.CameraProfile#frameWheelBindingsDescription()
	 */
	public String currentCameraProfileHelp() {
		String description = new String();
		description += currentCameraProfile().name() + " camera profile keyboard shortcuts and mouse bindings\n";
		int index = 1;
		if( currentCameraProfile().keyboardShortcutsDescription().length() != 0 ) {
			description += index + ". " + "Keyboard shortcuts\n";
			description += currentCameraProfile().keyboardShortcutsDescription();
			index++;
		}
		if( currentCameraProfile().cameraDeviceBindingsDescription().length() != 0 ) {
			description += index + ". " + "Camera mouse bindings\n";
			description += currentCameraProfile().cameraDeviceBindingsDescription();
			index++;
		}
		if( currentCameraProfile().deviceClickBindingsDescription().length() != 0 ) {
			description += index + ". " + "Mouse click bindings\n";
			description += currentCameraProfile().deviceClickBindingsDescription();
			index++;
		}
		if( currentCameraProfile().frameDeviceBindingsDescription().length() != 0 ) {
			description += index + ". " + "Interactive frame mouse bindings\n";
			description += currentCameraProfile().frameDeviceBindingsDescription();
			index++;
		}
		if( currentCameraProfile().cameraWheelBindingsDescription().length() != 0 ) {
			description += index + ". " + "Camera mouse wheel bindings\n";
			description += currentCameraProfile().cameraWheelBindingsDescription();
			index++;
		}
		if( currentCameraProfile().frameWheelBindingsDescription().length() != 0 ) {
			description += index + ". " + "Interactive frame mouse wheel bindings\n";
			description += currentCameraProfile().frameWheelBindingsDescription();
			index++;
		}
		return description;
	}
	
	/**
	 * Convenience funstion that simply calls {@code displayGlobalHelp(true)}.
	 * 
	 * @see #displayGlobalHelp(boolean)
	 */
	public void displayGlobalHelp() {
		displayGlobalHelp(true);
	}
	
	public abstract void displayGlobalHelp(boolean onConsole);
	
	/**
	 * Convenience function that simply calls {@code displayCurrentCameraProfileHelp(true)}.
	 * 
	 * @see #displayCurrentCameraProfileHelp(boolean)
	 */
	public void displayCurrentCameraProfileHelp() {
		displayCurrentCameraProfileHelp(true);
	}
	
	public void displayCurrentCameraProfileHelp(boolean onConsole) {
		if (onConsole)
			System.out.println(currentCameraProfileHelp());
		else
			AbstractScene.showMissingImplementationWarning("displayCurrentCameraProfileHelp");
	}
	
	// Shortcuts
	
	/**
   * Defines a global keyboard shortcut to bind the given action.
   * 
   * @param key shortcut
   * @param action keyboard action
   */
	public void setShortcut(Character key, DOF_0Action action) {
		if ( isKeyInUse(key) ) {
			DOF_0Action a = shortcut(key);
			System.out.println("Warning: overwritting shortcut which was previously binded to " + a);
		}
		gProfile.setBinding(new KeyboardShortcut(key), action);
	}
	
  /**
   * Defines a global keyboard shortcut to bind the given action. High-level version
   * of {@link #setShortcut(Integer, Integer, DOF_0Action)}.
   * 
   * @param mask modifier mask defining the shortcut
   * @param key character (internally converted to a coded key) defining the shortcut
   * @param action keyboard action
   * 
   * @see #setShortcut(Integer, Integer, DOF_0Action)
   */
	public void setShortcut(Integer mask, Character key, DOF_0Action action) {
		setShortcut(mask, DLKeyEvent.getKeyCode(key), action);
	}
	
  /**
   * Defines a global keyboard shortcut to bind the given action. High-level version
   * of {@link #setShortcut(Integer, Character, DOF_0Action)}.
   * 
   * @param mask modifier mask defining the shortcut
   * @param vKey coded key defining the shortcut
   * @param action keyboard action
   * 
   * @see #setShortcut(Integer, Character, DOF_0Action)
   */
	public void setShortcut(Integer mask, Integer vKey, DOF_0Action action) {
		if ( isKeyInUse(mask, vKey) ) {
			DOF_0Action a = shortcut(mask, vKey);
			System.out.println("Warning: overwritting shortcut which was previously binded to " + a);
		}
		gProfile.setBinding(new KeyboardShortcut(mask, vKey), action);
	}

	/**
	 * Defines a global keyboard shortcut to bind the given action.
	 * 
	 * @param vKey coded key defining the shortcut
	 * @param action keyboard action
	 */
	public void setShortcut(Integer vKey, DOF_0Action action) {
		if ( isKeyInUse(vKey) ) {
			DOF_0Action a = shortcut(vKey);
			System.out.println("Warning: overwritting shortcut which was previously binded to " + a);
		}
		gProfile.setBinding(new KeyboardShortcut(vKey), action);
	}

	/**
	 * Removes all global keyboard shortcuts.
	 */
	public void removeAllShortcuts() {
		gProfile.removeAllBindings();
	}
	
	/**
	 * Removes the global keyboard shortcut.
	 * 
	 * @param key shortcut
	 */
	public void removeShortcut(Character key) {
		gProfile.removeBinding(new KeyboardShortcut(key));
	}
	
  /**
   * Removes the global keyboard shortcut. High-level version
   * of {@link #removeShortcut(Integer, Integer)}.
   * 
   * @param mask modifier mask defining the shortcut
   * @param key character (internally converted to a coded key) defining the shortcut
   * 
   * @see #removeShortcut(Integer, Integer)
   */
	public void removeShortcut(Integer mask, Character key) {
		removeShortcut(mask, DLKeyEvent.getKeyCode(key));
	}

	/**
   * Removes the global keyboard shortcut. Low-level version
   * of {@link #removeShortcut(Integer, Character)}.
   * 
   * @param mask modifier mask defining the shortcut
   * @param vKey virtual coded-key defining the shortcut
   * 
   * @see #removeShortcut(Integer, Character)
   */
	public void removeShortcut(Integer mask, Integer vKey) {
		gProfile.removeBinding(new KeyboardShortcut(mask, vKey));
	}

	/**
	 * Removes the global keyboard shortcut.
	 * 
	 * @param vKey virtual coded-key defining the shortcut
	 */
	public void removeShortcut(Integer vKey) {
		gProfile.removeBinding(new KeyboardShortcut(vKey));
	}
	
	/**
	 * Returns the action that is binded to the given global keyboard shortcut.
	 * 
	 * @param key shortcut
	 */
	public DOF_0Action shortcut(Character key) {
		return gProfile.binding(new KeyboardShortcut(key));
	}
	
  /**
   * Returns the action that is binded to the given global keyboard shortcut.
   * High-level version of {@link #shortcut(Integer, Integer)}.
   * 
   * @param mask modifier mask defining the shortcut
   * @param key character (internally converted to a coded key) defining the shortcut
   * 
   * @see #shortcut(Integer, Integer)
   */
	public DOF_0Action shortcut(Integer mask, Character key) {
		return shortcut(mask, DLKeyEvent.getKeyCode(key));
	}

	/**
   * Returns the action that is binded to the given global keyboard shortcut.
   * Low-level version of {@link #shortcut(Integer, Character)}.
   * 
   * @param mask modifier mask defining the shortcut
   * @param vKey virtual coded-key defining the shortcut
   * 
   * @see #shortcut(Integer, Character)
   */
	public DOF_0Action shortcut(Integer mask, Integer vKey) {
		return gProfile.binding(new KeyboardShortcut(mask, vKey));
	}

	/**
	 * Returns the action that is binded to the given global keyboard shortcut.
	 * 
	 * @param vKey virtual coded-key defining the shortcut
	 */
	public DOF_0Action shortcut(Integer vKey) {
		return gProfile.binding(new KeyboardShortcut(vKey));
	}

	/**
	 * Returns true if the given global keyboard shortcut binds an action.
	 * 
	 * @param key shortcut
	 */
	public boolean isKeyInUse(Character key) {
		return gProfile.isShortcutInUse(new KeyboardShortcut(key));
	}
	
  /**
   * Returns true if the given global keyboard shortcut binds an action.
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
   * Returns true if the given global keyboard shortcut binds an action.
   * Low-level version of {@link #isKeyInUse(Integer, Character)}.
   * 
   * @param mask modifier mask defining the shortcut
   * @param vKey virtual coded-key defining the shortcut
   * 
   * @see #isKeyInUse(Integer, Character)
   */
	public boolean isKeyInUse(Integer mask, Integer vKey) {
		return gProfile.isShortcutInUse(new KeyboardShortcut(mask, vKey));
	}
	
	/**
	 * Returns true if the given global keyboard shortcut binds an action.
	 * 
	 * @param vKey virtual coded-key defining the shortcut
	 */
	public boolean isKeyInUse(Integer vKey) {
		return gProfile.isShortcutInUse(new KeyboardShortcut(vKey));
	}

	/**
	 * Returns true if there is a global keyboard shortcut for the given action.
	 */
	public boolean isActionBinded(DOF_0Action action) {
		return gProfile.isActionMapped(action);
	}
	
	
	/**
	 * Returns {@code true} if the keyboard is currently being handled by proscene
	 * and {@code false} otherwise. Set keyboard handling with
	 * {@link #enableMouseHandling(boolean)}.
	 * <p>
	 * Keyboard handling is enable by default.
	 */
	public boolean keyboardIsHandled() {
		return keyboardHandling;
	}

	/**
	 * Toggles the state of {@link #keyboardIsHandled()}
	 */
	public void toggleKeyboardHandling() {
		enableKeyboardHandling(!keyboardHandling);
	}

	/**
	 * Enables or disables proscene keyboard handling according to {@code enable}
	 * 
	 * @see #keyboardIsHandled()
	 */
	public void enableKeyboardHandling(boolean enable) {
		if (enable)
			enableKeyboardHandling();
		else
			disableKeyboardHandling();
	}
	
	/**
	 * Enables Proscene keyboard handling.
	 * 
	 * @see #keyboardIsHandled()
	 * @see #enableMouseHandling()
	 * @see #disableKeyboardHandling()
	 */
	public void enableKeyboardHandling() {
		if( keyboardIsHandled() )
			return;
		keyboardHandling = true;
	}

	/**
	 * Disables Proscene keyboard handling.
	 * 
	 * @see #keyboardIsHandled()
	 */
	public void disableKeyboardHandling() {
		if( !keyboardIsHandled() )
			return;
		keyboardHandling = false;
	}
	
	/**
	 * Sets global default keyboard shortcuts and the default key-frame shortcut keys.
	 * <p>
	 * Default global keyboard shortcuts are:
	 * <p>
	 * <ul>
	 * <li><b>'a'</b>: {@link remixlab.proscene.Scene.DOF_0Action#DRAW_AXIS}.
	 * <li><b>'e'</b>: {@link remixlab.proscene.Scene.DOF_0Action#CAMERA_TYPE}.
	 * <li><b>'g'</b>: {@link remixlab.proscene.Scene.DOF_0Action#DRAW_GRID}.
	 * <li><b>'h'</b>: {@link remixlab.proscene.Scene.DOF_0Action#GLOBAL_HELP}
	 * <li><b>'H'</b>: {@link remixlab.proscene.Scene.DOF_0Action#CURRENT_CAMERA_PROFILE_HELP}
	 * <li><b>'r'</b>: {@link remixlab.proscene.Scene.DOF_0Action#EDIT_CAMERA_PATH}.
	 * <li><b>space bar</b>: {@link remixlab.proscene.Scene.DOF_0Action#CAMERA_PROFILE}.
	 * </ul> 
	 * <p>
	 * Default key-frame shortcuts keys are:
	 * <ul>
	 * <li><b>'[1..5]'</b>: Play path [1..5]. 
	 * <li><b>'CTRL'+'[1..5]'</b>: Add key-frame to path [1..5].   
	 * <li><b>'ALT'+'[1..5]'</b>: Remove path [1..5].
	 * </ul> 
	 */
	public void setDefaultShortcuts() {
		// D e f a u l t s h o r t c u t s		
		setShortcut('a', DOF_0Action.DRAW_AXIS);
		setShortcut('g', DOF_0Action.DRAW_GRID);
		setShortcut(' ', DOF_0Action.CAMERA_PROFILE);
		setShortcut('e', DOF_0Action.CAMERA_TYPE);		
		setShortcut('h', DOF_0Action.GLOBAL_HELP);
		setShortcut('H', DOF_0Action.CURRENT_CAMERA_PROFILE_HELP);
		setShortcut('r', DOF_0Action.EDIT_CAMERA_PATH);

		// K e y f r a m e s s h o r t c u t k e y s
		setAddKeyFrameKeyboardModifier(CTRL);
		setDeleteKeyFrameKeyboardModifier(ALT);
		setPathKey('1', 1);
		setPathKey('2', 2);
		setPathKey('3', 3);
		setPathKey('4', 4);
		setPathKey('5', 5);
	}

	/**
	 * Associates key-frame interpolator path to key. High-level version
	 * of {@link #setPathKey(Integer, Integer)}.
	 *  
	 * @param key character (internally converted to a key coded) defining the shortcut
	 * @param path key-frame interpolator path
	 * 
	 * @see #setPathKey(Integer, Integer)
	 */
	public void setPathKey(Character key, Integer path) {
		setPathKey(DLKeyEvent.getKeyCode(key), path);
	}
	
	/**
	 * Associates key-frame interpolator path to the given virtual key. Low-level version
	 * of {@link #setPathKey(Character, Integer)}.
	 * 
	 * @param vKey shortcut
	 * @param path key-frame interpolator path
	 * 
	 * @see #setPathKey(Character, Integer)
	 */
	public void setPathKey(Integer vKey, Integer path) {
		if ( isPathKeyInUse(vKey) ) {
			Integer p = path(vKey);
			System.out.println("Warning: overwritting path key which was previously binded to path " + p);
		}
		pathKeys.setBinding(vKey, path);
	}

	/**
	 * Returns the key-frame interpolator path associated with key. High-level version
	 * of {@link #path(Integer)}.
	 * 
	 * @param key character (internally converted to a key coded) defining the shortcut
	 * 
	 * @see #path(Integer)
	 */
	public Integer path(Character key) {
		return path(DLKeyEvent.getKeyCode(key));
	}
	
	/**
	 * Returns the key-frame interpolator path associated with key. Low-level version
	 * of {@link #path(Character)}.
	 * 
	 * @param vKey shortcut
	 * 
	 * @see #path(Character)
	 */
	public Integer path(Integer vKey) {
		return pathKeys.binding(vKey);
	}

	/**
	 * Removes the key-frame interpolator shortcut. High-level version
	 * of {@link #removePathKey(Integer)}.
	 * 
	 * @param key character (internally converted to a key coded) defining the shortcut
	 * 
	 * @see #removePathKey(Integer)
	 */
	public void removePathKey(Character key) {
		removePathKey(DLKeyEvent.getKeyCode(key));
	}
	
	/**
	 * Removes the key-frame interpolator shortcut. Low-level version
	 * of {@link #removePathKey(Character)}.
	 * 
	 * @param vKey shortcut
	 * 
	 * @see #removePathKey(Character)
	 */
	public void removePathKey(Integer vKey) {
		pathKeys.removeBinding(vKey);
	}
	
	/**
	 * Returns true if the given key binds a key-frame interpolator path. High-level version
	 * of {@link #isPathKeyInUse(Integer)}.
	 * 
	 * @param key character (internally converted to a key coded) defining the shortcut
	 * 
	 * @see #isPathKeyInUse(Integer)
	 */
	public boolean isPathKeyInUse(Character key) {
		return isPathKeyInUse(DLKeyEvent.getKeyCode(key));
	}
	
	/**
	 * Returns true if the given virtual key binds a key-frame interpolator path. Low-level version
	 * of {@link #isPathKeyInUse(Character)}.
	 * 
	 * @param vKey shortcut
	 * 
	 * @see #isPathKeyInUse(Character)
	 */
	public boolean isPathKeyInUse(Integer vKey) {
		return pathKeys.isShortcutInUse(vKey);
	}

	/**
	 * Sets the modifier key needed to play the key-frame interpolator paths.
	 * 
	 * @param modifier
	 */
	public void setAddKeyFrameKeyboardModifier(Integer modifier) {
		addKeyFrameKeyboardModifier = modifier;
	}

	/**
	 * Sets the modifier key needed to delete the key-frame interpolator paths.
	 * 
	 * @param modifier
	 */
	public void setDeleteKeyFrameKeyboardModifier(Integer modifier) {
		deleteKeyFrameKeyboardModifier = modifier;
	}
	
	public String globalHelp() {
		String description = new String();
		description += "GLOBAL keyboard shortcuts\n";
		for (Entry<KeyboardShortcut, DOF_0Action> entry : gProfile.map().entrySet()) {			
			Character space = ' ';
			if (!entry.getKey().description().equals(space.toString())) 
				description += entry.getKey().description() + " -> " + entry.getValue().description() + "\n";
			else
				description += "space_bar" + " -> " + entry.getValue().description() + "\n";
		}
		
		for (Entry<Integer, Integer> entry : pathKeys.map().entrySet())
			description += DLKeyEvent.getKeyText(entry.getKey()) + " -> plays camera path " + entry.getValue().toString() + "\n";
		description += DLEvent.getModifiersText(addKeyFrameKeyboardModifier) + " + one of the above keys -> adds keyframe to the camera path \n";
		description += DLEvent.getModifiersText(deleteKeyFrameKeyboardModifier) + " + one of the above keys -> deletes the camera path \n";
		
		return description;		
	}
	
	/**
	 * Toggles the {@link #interactiveFrame()} interactivity on and off.
	 */
	public void toggleDrawInteractiveFrame() {
		if (interactiveFrameIsDrawn())
			setDrawInteractiveFrame(false);
		else
			setDrawInteractiveFrame(true);
	}
	
	/**
	 * Convenience function that simply calls {@code setDrawInteractiveFrame(true)}.
	 * 
	 * @see #setDrawInteractiveFrame(boolean)
	 */
	public void setDrawInteractiveFrame() {
		setDrawInteractiveFrame(true);
	}
	
	/**
	 * Returns the {@link PApplet#width} to {@link PApplet#height} aspect ratio of
	 * the processing display window.
	 */
	public float aspectRatio() {
		return (float) width() / (float) height();
	}
		
	/**
	 * Returns {@code true} if the mouse is currently being handled by proscene and
	 * {@code false} otherwise. Set mouse handling with
	 * {@link #enableMouseHandling(boolean)}.
	 * <p>
	 * Mouse handling is enable by default.
	 */
	public boolean mouseIsHandled() {
		return mouseHandling;
	}

	/**
	 * Toggles the state of {@link #mouseIsHandled()}
	 */
	public void toggleMouseHandling() {
		enableMouseHandling(!mouseHandling);
	}

	/**
	 * Enables or disables proscene mouse handling according to {@code enable}
	 * 
	 * @see #mouseIsHandled()
	 */
	public void enableMouseHandling(boolean enable) {
		if (enable)
			enableMouseHandling();
		else
			disableMouseHandling();
	}

	/**
	 * Enables Proscene mouse handling.
	 * 
	 * @see #mouseIsHandled()
	 * @see #disableMouseHandling()
	 * @see #enableKeyboardHandling()
	 */
	public void enableMouseHandling() {
		if( mouseIsHandled() )
			return;
		mouseHandling = true;
	}

	/**
	 * Disables Proscene mouse handling.
	 * 
	 * @see #mouseIsHandled()
	 */
	public void disableMouseHandling() {
		if( !mouseIsHandled() )
			return;
		mouseHandling = false;
	}
	
	// D R A W I N G   M E T H O D S
	
	public void preDraw() {
		/**
		// TODO: how to handle this?
		if ((currentCameraProfile().mode() == CameraProfile.Mode.THIRD_PERSON)
				&& (!camera().anyInterpolationIsStarted())) {
			camera().setPosition(avatar().cameraPosition());
			camera().setUpVector(avatar().upVector());
			camera().lookAt(avatar().target());
		}
		*/
		
		// TODO if leave it here it gives results very close to P5	  
		updateFrameRate();		
		bindMatrices();
		if (frustumEquationsUpdateIsEnable())
			pinhole().updateFrustumEquations();
	}
	
	/**
	 * Internal method. Called by {@link #draw()} and {@link #endDraw()}.
	 * <p>
	 * First performs any scheduled animation, then calls {@link #proscenium()}
	 * which is the main drawing method that could be overloaded. Then, if
	 * there's an additional drawing method registered at the Scene, calls it (see
	 * {@link #addDrawHandler(Object, String)}). Finally, displays the
	 * {@link #displayGlobalHelp()}, the axis, the grid, the interactive frames' selection
	 * hints and camera paths, and some visual hints (such {@link #drawZoomWindowHint()},
	 * {@link #drawScreenRotateLineHint()} and {@link #drawArcballReferencePointHint()})
	 * according to user interaction and flags.
	 * 
	 * @see #proscenium()
	 * @see #addDrawHandler(Object, String)
	 * @see #gridIsDrawn()
	 * @see #axisIsDrwn
	 * @see #addDrawHandler(Object, String)
	 * @see #addAnimationHandler(Object, String)
	 */
	public void postDraw() {
	  // 0. Alternative use only
		proscenium();
			
		// 1. timers
		if (timersAreSingleThreaded())
			handleTimers();
		
		// 2. Animation
		if( animationIsStarted() )
			performAnimation(); //abstract	
		
		// 3. Draw external registered method (only in java sub-classes)
		invokeRegisteredMethod(); // abstract
		
	  // 4. HIDevices
		updateCursorPosition();
		
		for (HIDevice device : devices)
			device.handle();
			
		// 5. Events
    while( !eventQueue.isEmpty() ) 
    	eventDispatcher.handle(eventQueue.remove());
		
		// 6. Grid and axis drawing
		if (gridIsDrawn()) {
			if(gridIsDotted())
				drawDottedGrid(pinhole().sceneRadius());
			else
				drawGrid(pinhole().sceneRadius());
		}
		if (axisIsDrawn())
			drawAxis(pinhole().sceneRadius());		
		
		// 7. Display visual hints
		displayVisualHints(); // abstract
		
		//updateFrameRate();
	}	
	
	protected abstract void updateCursorPosition();
	
	private void updateFrameRate() {
		long now = System.nanoTime();
		
		if(frameCount > 1) {
		  // update the current frameRate
	     double rate = 1000000.0 / ((now - frameRateLastNanos) / 1000000.0);
	     float instantaneousRate = (float) rate / 1000.0f;
	     frameRate = (frameRate * 0.9f) + (instantaneousRate * 0.1f);
		}
			
		frameRateLastNanos = now;
		frameCount++;
	}
	
	protected abstract void invokeRegisteredMethod();
	
	/**
	 * Bind processing matrices to proscene matrices.
	 */	
	protected void bindMatrices() {
		// we should simply go:
		renderer.bindMatrices();
		/**
	  // TODO implement stereo
		if(this.isAP5Scene()) {
			renderer.bindMatrices();
			pinhole().cacheProjViewInvMat();
		}
		else {
			//TODO weird to separate these two cases
			//maybe these three sets should belong to the renderer even if it's opengl?
			// and hence isAP5Scene should be out of the question
			setProjectionMatrix();
			setModelViewMatrix();
			setProjectionModelViewMatrix();
			pinhole().cacheProjViewInvMat();
		}
		*/
	}
	
	/**
	 * Sets the processing camera projection matrix from {@link #pinhole()}. Calls
	 * {@code PApplet.perspective()} or {@code PApplet.orhto()} depending on the
	 * {@link remixlab.remixcam.core.Camera#type()}.
	 */
	/**
	protected void setProjectionMatrix() {
		pinhole().loadProjectionMatrix();
	}
	*/
	
	/**
	 * Sets the processing camera matrix from {@link #pinhole()}. Simply calls
	 * {@code PApplet.camera()}.
	 */
	/**
	protected void setModelViewMatrix() {		
		// TODO find a better name for this:
		pinhole().resetViewMatrix(); // model is separated from view always
		//  alternative is
			//camera().loadViewMatrix();	  
	}
	*/
	
	protected void setProjectionModelViewMatrix() {
		pinhole().computeProjectionViewMatrix();
	}
	
	// TODO try to optimize assignments in all three matrix getters?
	// Requires allowing returning references as well as copies of the matrices,
	// but it seems overkill. 
	public Matrix3D getModelViewMatrix() {		
		Matrix3D modelview;
  	modelview = getMatrix();
  	modelview.preApply(getViewMatrix());
  	return modelview;
	}
	
	public Matrix3D getViewMatrix() {
		Matrix3D view = pinhole().getViewMatrix();  	
  	return view;
	}
	
	public Matrix3D getModelMatrix() {		
		Matrix3D model;
  	model = getMatrix();  	
  	return model;
	}
	
	public Matrix3D getProjectionMatrix() {		
		Matrix3D projection;  	
  	projection = getProjection();
  	return projection;
	}
	
	/**
	 * This method restores the matrix mode.
	 */
	public Matrix3D getModelViewProjectionMatrix() {		
		Matrix3D PVM;  	
  	PVM = getMatrix();//model  	
    //PVM.preApply(camera().projectionViewMat);
  	PVM.preApply(pinhole().getProjectionViewMatrix());  	
  	return PVM;
	}
	
	protected abstract void displayVisualHints();
	
	protected void handleTimers() {
		for ( AbstractTimerJob tJob : timerPool )
			if (tJob.timer() != null)
				if (tJob.timer() instanceof SingleThreadedTaskableTimer)
					((SingleThreadedTaskableTimer)tJob.timer()).execute();
	}	
	
  //1. Scene overloaded
	
	/**
	 * This method is called before the first drawing happen and should be overloaded to
	 * initialize stuff not initialized in {@code PApplet.setup()}. The default
	 * implementation is empty.
	 * <p>
	 * Typical usage include {@link #pinhole()} initialization ({@link #showAll()})
	 * and Scene state setup ({@link #setAxisIsDrawn(boolean)} and
	 * {@link #setGridIsDrawn(boolean)}.
	 */
	public void init() {}
	
	/**
	 * The method that actually defines the scene.
	 * <p>
	 * If you build a class that inherits from Scene, this is the method you
	 * should overload, but no if you instantiate your own Scene object (in this
	 * case you should just overload {@code PApplet.draw()} to define your scene).
	 * <p>
	 * The processing camera set in {@link #pre()} converts from the world to the
	 * camera coordinate systems. Vertices given in {@link #draw()} can then be
	 * considered as being given in the world coordinate system. The camera is
	 * moved in this world using the mouse. This representation is much more
	 * intuitive than a camera-centric system (which for instance is the standard
	 * in OpenGL).
	 */
	public void proscenium() {}
	
	// ANIMATION
	
	protected abstract void performAnimation();
	
	/**
	 * Return {@code true} when the animation loop is started.
	 * <p>
	 * Proscene animation loop relies on processing drawing loop. The {@link #draw()} function will
	 * check when {@link #animationIsStarted()} and then called the animation handler method
	 * (set with {@link #addAnimationHandler(Object, String)}) or {@link #animate()} (if no handler
	 * has been added to the scene) every {@link #animationPeriod()} milliseconds. In addition,
	 * During the drawing loop, the variable {@link #animatedFrameWasTriggered} is set
   * to {@code true} each time an animated frame is triggered (and {@code false} otherwise),
   * which is useful to notify to the outside world when an animation event occurs. 
	 * <p>
	 * Be sure to call {@code loop()} before an animation is started.
	 * <p>
	 * <b>Note:</b> The drawing frame rate may be modified when {@link #startAnimation()} is called,
	 * depending on the {@link #animationPeriod()}.   
	 * <p>
	 * Use {@link #startAnimation()}, {@link #stopAnimation()} or {@link #toggleAnimation()}
	 * to change this value.
	 * 
	 * @see #startAnimation()
	 * @see #addAnimationHandler(Object, String)
	 * @see #animate()
	 */
	public boolean animationIsStarted() {
		return animationStarted;
	}
	
	/**
	 * The animation loop period, in milliseconds. When {@link #animationIsStarted()}, this is
	 * the delay that takes place between two consecutive iterations of the animation loop.
	 * <p>
	 * This delay defines a target frame rate that will only be achieved if your
	 * {@link #animate()} and {@link #draw()} methods are fast enough. If you want to know
	 * the maximum possible frame rate of your machine on a given scene,
	 * {@link #setAnimationPeriod(float)} to {@code 1}, and {@link #startAnimation()}. The display
	 * will then be updated as often as possible, and the frame rate will be meaningful.  
	 * <p>
	 * Default value is 16.6666 milliseconds (60 Hz) which matches <b>processing</b> default
	 * frame rate.
	 * <p>
	 * <b>Note:</b> This value is taken into account only the next time you call
	 * {@link #startAnimation()}. If {@link #animationIsStarted()}, you should
	 * {@link #stopAnimation()} first. See {@link #restartAnimation()} and
	 * {@link #setAnimationPeriod(float, boolean)}.
	 * 
	 * @see #setAnimationPeriod(float, boolean)
	 */
	public long animationPeriod() {
		return animationPeriod;
	}
	
	/**
	 * Convenience function that simply calls {@code setAnimationPeriod(period, true)}.
	 * 
	 * @see #setAnimationPeriod(float, boolean)
	 */
	public void setAnimationPeriod(long period) {
		setAnimationPeriod(period, true);
	}
	
	/**
	 * Sets the {@link #animationPeriod()}, in milliseconds. If restart is {@code true}
	 * and {@link #animationIsStarted()} then {@link #restartAnimation()} is called.
	 * <p>
	 * <b>Note:</b> The drawing frame rate could be modified when {@link #startAnimation()} is called
	 * depending on the {@link #animationPeriod()}.
	 * 
	 * @see #startAnimation()
	 */
	public void setAnimationPeriod(long period, boolean restart) {
		if(period>0) {
			animationPeriod = period;
			if(animationIsStarted() && restart)				
				restartAnimation();
		}
	}
	
	/**
	 * Stops animation.
	 * <p>
	 * <b>Warning:</b> Restores the {@code PApplet} frame rate to its default value,
	 * i.e., calls {@code parent.frameRate(60)}. 
	 * 
	 * @see #animationIsStarted()
	 */
	public void stopAnimation()	{
		animationStarted = false;
		animatedFrameWasTriggered = false;
		animationTimer.stop();
	}
	
	/**
	 * Starts the animation loop.
	 * <p>
	 * Syncs the drawing frame rate according to {@link #animationPeriod()}: If the animation
	 * frame rate (which value depends on the {@link #animationPeriod()})
	 * is higher than the current {@link #frameRate()}, the frame rate is modified to match it,
	 * i.e., each drawing frame will trigger exactly one animation event. If the animation
	 * frame rate is lower than the {@link #frameRate()}, the frame rate is left unmodified,
	 * and the animation frames will be interleaved among the drawing frames in intervals
	 * needed to achieve the target {@link #animationPeriod()} (provided that your
	 * {@link #animate()} and {@link #draw()} methods are fast enough).
	 * 
	 * @see #animationIsStarted()
	 */
	public void startAnimation() {
		animationStarted = true;	
		animationTimer.run(animationPeriod);
	}
	
	/**
	 * Restart the animation.
	 * <p>
	 * Simply calls {@link #stopAnimation()} and then {@link #startAnimation()}.
	 */
  public void restartAnimation() {
  	stopAnimation();
  	startAnimation();
	}
  
  /**
	 * Scene animation method.
	 * <p>
	 * When {@link #animationIsStarted()}, this method defines how your scene evolves over time.
	 * <p>
	 * Overload it as needed. Default implementation is empty. You may
	 * {@link #addAnimationHandler(Object, String)} instead.
	 * <p>
	 * <b>Note</b> that remixlab.proscene.KeyFrameInterpolator (which regularly updates a Frame)
	 * do not use this method.
	 * 
	 * @see #addAnimationHandler(Object, String).
	 */
	public void animate() {
	}
	
	/**
	 * Calls {@link #startAnimation()} or {@link #stopAnimation()}, depending on
	 * {@link #animationIsStarted()}.
	 */
	public void toggleAnimation() {
		if (animationIsStarted()) stopAnimation(); else startAnimation();
	}
	
  //Device registration
	
	/**
	 * Adds an HIDevice to the scene.
	 * 
	 * @see #removeDevice(HIDevice)
	 * @see #removeAllDevices()
	 */
	public void addDevice(HIDevice device) {
		if(!isDeviceRegistered(device))
			devices.add(device);
	}
	
	public boolean isDeviceRegistered(HIDevice device) {
		return devices.contains(device);
	}
	
	/**
	 * Removes the device from the scene.
	 * 
	 * @see #addDevice(HIDevice)
	 * @see #removeAllDevices()
	 */
	public void removeDevice(HIDevice device) {
		devices.remove(device);
	}
	
	/**
	 * Removes all registered devices from the scene.
	 * 
	 * @see #addDevice(HIDevice)
	 * @see #removeDevice(HIDevice)
	 */
	public void removeAllDevices() {
		devices.clear();
	}
	
  // Event registration
	
	public boolean isEventRegistered(DLEvent event) {
		return eventQueue.contains(event);
	}
	
	/**
	 * Adds an HIDevice to the scene.
	 * 
	 * @see #removeDevice(AbstractHIDevice)
	 * @see #removeAllDevices()
	 */
	public void enqueueEvent(DLEvent event) {
		if(!isEventRegistered(event))
			eventQueue.add(event);
	}
	
	/**
	 * Removes the device from the scene.
	 * 
	 * @see #addDevice(AbstractHIDevice)
	 * @see #removeAllDevices()
	 */
	public void removeEvent(DLEvent event) {
		eventQueue.remove(event);
	}
	
	/**
	 * Removes all registered devices from the scene.
	 * 
	 * @see #addDevice(AbstractHIDevice)
	 * @see #removeDevice(AbstractHIDevice)
	 */
	public void removeAllEvents() {
		eventQueue.clear();
	}
	
	// WRAPPERS
	
  public boolean is2D() {
  	return renderer.is2D();
  }
	
	public boolean is3D() {
		return renderer.is3D();
	}
	
	/**
	 * Push a copy of the modelview matrix onto the stack.
   */
	public void pushMatrix() {
		if( renderer instanceof StackRenderer )
			renderer.pushMatrix();
		else
			AbstractScene.showMissingImplementationWarning("pushMatrix");
	}
	
	/**
	 * Replace the current modelview matrix with the top of the stack.
	 */
	public void popMatrix() {
		if( renderer instanceof StackRenderer )
			renderer.popMatrix();
		else
			AbstractScene.showMissingImplementationWarning("popMatrix");
	}
	
	/**
	 * Push a copy of the projection matrix onto the stack.
   */
	public void pushProjection() {
		if( renderer instanceof StackRenderer )
			renderer.pushProjection();
		else
			AbstractScene.showMissingImplementationWarning("pushProjection");
	}
	
	/**
	 * Replace the current projection matrix with the top of the stack.
	 */
	public void popProjection() {
		if( renderer instanceof StackRenderer )
			renderer.popProjection();
		else
			AbstractScene.showMissingImplementationWarning("popProjection");
	}
	
  /**
   * Translate in X and Y.
   */
  public void translate(float tx, float ty) {    
  	if( renderer instanceof StackRenderer )
  		renderer.translate(tx, ty);
  	else
			AbstractScene.showMissingImplementationWarning("translate");
  }

  /**
   * Translate in X, Y, and Z.
   */
  public void translate(float tx, float ty, float tz) {
  	if( renderer instanceof StackRenderer )
  		renderer.translate(tx, ty, tz);
  	else
			AbstractScene.showMissingImplementationWarning("translate");
  }

  /**
   * Two dimensional rotation.
   *
   * Same as rotateZ (this is identical to a 3D rotation along the z-axis)
   * but included for clarity. It'd be weird for people drawing 2D graphics
   * to be using rotateZ. And they might kick our a-- for the confusion.
   *
   * <A HREF="http://www.xkcd.com/c184.html">Additional background</A>.
   */
  public void rotate(float angle) {
  	if( renderer instanceof StackRenderer )
  		renderer.rotate(angle);
  	else
			AbstractScene.showMissingImplementationWarning("rotate");
  }

  /**
   * Rotate around the X axis.
   */
  public void rotateX(float angle) { 
  	if( renderer instanceof StackRenderer )
  		renderer.rotateX(angle);
  	else
			AbstractScene.showMissingImplementationWarning("rotateX");
  }

  /**
   * Rotate around the Y axis.
   */
  public void rotateY(float angle) {
  	if( renderer instanceof StackRenderer )
  		renderer.rotateY(angle);
  	else
  		AbstractScene.showMissingImplementationWarning("rotateY");
  }

  /**
   * Rotate around the Z axis.
   *
   * The functions rotate() and rotateZ() are identical, it's just that it make
   * sense to have rotate() and then rotateX() and rotateY() when using 3D;
   * nor does it make sense to use a function called rotateZ() if you're only
   * doing things in 2D. so we just decided to have them both be the same.
   */
  public void rotateZ(float angle) {
  	if( renderer instanceof StackRenderer )
  		renderer.rotateZ(angle);
  	else
  		AbstractScene.showMissingImplementationWarning("rotateZ");
  }

  /**
   * Rotate about a vector in space. Same as the glRotatef() function.
   */
  public void rotate(float angle, float vx, float vy, float vz) {
  	if( renderer instanceof StackRenderer )
  		renderer.rotate(angle, vx, vy, vz);
  	else
  		AbstractScene.showMissingImplementationWarning("rotate");
  }

  /**
   * Scale in all dimensions.
   */
  public void scale(float s) {
  	if( renderer instanceof StackRenderer )
  		renderer.scale(s);
  	else
  		AbstractScene.showMissingImplementationWarning("scale");
  }

  /**
   * Scale in X and Y. Equivalent to scale(sx, sy, 1).
   *
   * Not recommended for use in 3D, because the z-dimension is just
   * scaled by 1, since there's no way to know what else to scale it by.
   */
  public void scale(float sx, float sy) {
  	if( renderer instanceof StackRenderer )
  		renderer.scale(sx, sy);
  	else
  		AbstractScene.showMissingImplementationWarning("scale");
  }

  /**
   * Scale in X, Y, and Z.
   */
  public void scale(float x, float y, float z) {
  	if( renderer instanceof StackRenderer )
  		renderer.scale(x, y, z);
  	else
  		AbstractScene.showMissingImplementationWarning("scale");
  }  
  
  /**
   * Set the current modelview matrix to identity.
   */
  public void resetMatrix() {
  	if( renderer instanceof StackRenderer )
  		renderer.resetMatrix();
  	else
  		AbstractScene.showMissingImplementationWarning("resetMatrix");
  }
  
  /**
   * Set the current projection matrix to identity.
   */
  public void resetProjection() {
  	if( renderer instanceof StackRenderer )
  		renderer.resetProjection();
  	else
  		AbstractScene.showMissingImplementationWarning("resetProjection");
  }  
  
  public void applyMatrix(Matrix3D source) {
  	if( renderer instanceof StackRenderer )
  		renderer.applyMatrix(source);
  	else
  		AbstractScene.showMissingImplementationWarning("applyMatrix");
  }
  
  public void applyProjection(Matrix3D source) {
  	if( renderer instanceof StackRenderer )
  		renderer.applyProjection(source);
  	else
  		AbstractScene.showMissingImplementationWarning("applyProjection");
  }

  /**
   * Apply a 4x4 modelview matrix.
   */
  public void applyMatrixRowMajorOrder(float n00, float n01, float n02, float n03,
                                       float n10, float n11, float n12, float n13,
                                       float n20, float n21, float n22, float n23,
                                       float n30, float n31, float n32, float n33) {    
  	renderer.applyMatrixRowMajorOrder(n00, n01, n02, n03, n10, n11, n12, n13, n20, n21, n22, n23, n30, n31, n32, n33);
  }
  
  /**
   * Apply a 4x4 projection matrix.
   */
  public void applyProjectionRowMajorOrder(float n00, float n01, float n02, float n03,
                                       float n10, float n11, float n12, float n13,
                                       float n20, float n21, float n22, float n23,
                                       float n30, float n31, float n32, float n33) {    
  	renderer.applyProjectionRowMajorOrder(n00, n01, n02, n03, n10, n11, n12, n13, n20, n21, n22, n23, n30, n31, n32, n33);
  }
  
  /**
  public void frustum(float left, float right, float bottom, float top, float znear, float zfar) {
  	renderer.frustum(left, right, bottom, top, znear, zfar);
  }
  */

  public Matrix3D getMatrix() {
  	if( renderer instanceof StackRenderer )
  		return renderer.getMatrix();
  	else {
  		AbstractScene.showMissingImplementationWarning("getMatrix");
  		return null;
  	}
  }
  
  public Matrix3D getProjection() {
  	if( renderer instanceof StackRenderer )
  		return renderer.getProjection();
  	else {
  		AbstractScene.showMissingImplementationWarning("getProjection");
  		return null;
  	}
  }

  /**
   * Copy the current modelview matrix into the specified target.
   * Pass in null to create a new matrix.
   */
  public Matrix3D getMatrix(Matrix3D target) {
  	if( renderer instanceof StackRenderer )
  		return renderer.getMatrix(target);
  	else {
  		AbstractScene.showMissingImplementationWarning("getMatrix");
  		return null;
  	}
  }
  
  /**
   * Copy the current projection matrix into the specified target.
   * Pass in null to create a new matrix.
   */
  public Matrix3D getProjection(Matrix3D target) {
  	if( renderer instanceof StackRenderer )
  		return renderer.getProjection(target);
  	else {
  		AbstractScene.showMissingImplementationWarning("getProjection");
  		return null;
  	}
  }

  /**
   * Set the current modelview matrix to the contents of another.
   */
  public void setMatrix(Matrix3D source) {
  	renderer.setMatrix(source);
  }
  
  /**
   * Set the current projection matrix to the contents of another.
   */
  public void setProjection(Matrix3D source) {
  	renderer.setProjection(source);
  }

  /**
   * Print the current modelview matrix.
   */
  public void printMatrix() {
  	if( renderer instanceof StackRenderer )
  		renderer.printMatrix();
  	else
  		AbstractScene.showMissingImplementationWarning("printMatrix");
  }
  
  /**
   * Print the current projection matrix.
   */
  public void printProjection() {
  	if( renderer instanceof StackRenderer )
  		renderer.printProjection();
  	else
  		AbstractScene.showMissingImplementationWarning("printMatrix");
  }
  
  /**
	 * Draws a cylinder of width {@code w} and height {@code h}, along the 
	 * positive {@code z} axis. 
	 */
  public void cylinder(float w, float h) {
  	renderer.cylinder(w, h);
  }
  
  public void cone(int detail, float x, float y, float r, float h) {
  	renderer.cone(detail, x, y, r, h);
  }
  
  public void cone(int detail, float x, float y, float r1, float r2, float h) {
  	renderer.cone(detail, x, y, r1, r2, h);
  }
  
  public void drawAxis(float length) {
  	renderer.drawAxis(length);
  }
  
  public void drawGrid(float size, int nbSubdivisions) {
  	renderer.drawGrid(size, nbSubdivisions);
  }
  
  public void drawDottedGrid(float size, int nbSubdivisions) {
  	renderer.drawDottedGrid(size, nbSubdivisions);
  }
    
  public void drawViewWindow(ViewWindow window, float scale) {
  	renderer.drawViewWindow(window, scale);
  }
  
  public void drawCamera(Camera camera, boolean drawFarPlane, float scale) {
  	renderer.drawCamera(camera, drawFarPlane, scale);
  }
  
  public void drawKFIViewport(float scale) {
  	renderer.drawKFIViewport(scale);
  }
  
  public void drawZoomWindowHint() {
  	renderer.drawZoomWindowHint();
  }
  
  public void drawScreenRotateLineHint() {
  	renderer.drawScreenRotateLineHint();
  }
  
  public void drawArcballReferencePointHint() {
  	renderer.drawArcballReferencePointHint();
  }
  
  public void drawCross(float px, float py, float size) {
  	renderer.drawCross(px, py, size);
  }
  
  public void drawFilledCircle(int subdivisions, Vector3D center, float radius) {
  	renderer.drawFilledCircle(subdivisions, center, radius);
  }
  
  public void drawFilledSquare(Vector3D center, float edge) {
  	renderer.drawFilledSquare(center, edge);
  }
  
  public void drawShooterTarget(Vector3D center, float length) {
		renderer.drawShooterTarget(center, length);
	}
	
	public void drawPath(List<GeomFrame> path, int mask, int nbFrames, int nbSteps, float scale) {
		renderer.drawPath(path, mask, nbFrames, nbSteps, scale);
	}
	
  public void beginScreenDrawing() {
  	if (startCoordCalls != 0)
			throw new RuntimeException("There should be exactly one beginScreenDrawing() call followed by a "
							                 + "endScreenDrawing() and they cannot be nested. Check your implementation!");
		
		startCoordCalls++;
		
		disableDepthTest();
		renderer.beginScreenDrawing();
		
		 /**
		if( this.isAP5Scene() )
			renderer.beginScreenDrawing();
		else { // TODO needs implementation and testing pushProjection();
			
			//resetProjection();
			//camera().ortho(0f, width(), height(), 0.0f, 0.0f, -1.0f);
			//multiplyProjection(camera().getProjectionMatrix());
			
			// next two same as the prv three?
			if( this.is3D() )
				((Camera) pinhole()).ortho(0f, width(), height(), 0.0f, 0.0f, -1.0f);
			else {
			//TODO implement 2D case
			}
			loadProjection(pinhole().getProjectionMatrix());
			pushMatrix();
			resetMatrix();
		}
		//*/
  }
	
	public void endScreenDrawing() {
		startCoordCalls--;
		if (startCoordCalls != 0)
			throw new RuntimeException("There should be exactly one beginScreenDrawing() call followed by a "
							                 + "endScreenDrawing() and they cannot be nested. Check your implementation!");
		
		renderer.endScreenDrawing();
		enableDepthTest();
		
		/**
		if( this.isAP5Scene() )
			renderer.endScreenDrawing();
		else {
			popProjection();
			popMatrix();			
		}		
		*/
	}
	
	public abstract void disableDepthTest();
	
	public abstract void enableDepthTest();
	
	// end wrapper
	
	public boolean isLeftHanded() {
		return !rightHanded;
	}
	
	public boolean isRightHanded() {
		return rightHanded;
	}
	
	public void setRightHanded() {
		rightHanded = true;
	}
	
	public void setLeftHanded() {
		rightHanded = false;
	}	
	
	// 0. Optimization stuff
	
	// TODO fix documentation
	/**
	 * Apply the transformation defined by {@code frame}.
	 * The Frame is first translated and then rotated around the new translated origin.
	 * <p>
	 * Same as:
	 * <p>
	 * {@code renderer().translate(translation().x, translation().y, translation().z);} <br>
	 * {@code renderer().rotate(rotation().angle(), rotation().axis().x,
	 * rotation().axis().y, rotation().axis().z);} <br>
	 * <p>
	 * This method may be used to modify the modelview matrix from a Frame hierarchy.
	 * For example, with this Frame hierarchy:
	 * <p>
	 * {@code Frame body = new Frame();} <br>
	 * {@code Frame leftArm = new Frame();} <br>
	 * {@code Frame rightArm = new Frame();} <br>
	 * {@code leftArm.setReferenceFrame(body);} <br>
	 * {@code rightArm.setReferenceFrame(body);} <br>
	 * <p>
	 * The associated processing drawing code should look like:
	 * <p>
	 * {@code pushMatrix();} <br>
	 * {@code applyTransformation(body);} <br>
	 * {@code drawBody();} <br>
	 * {@code pushMatrix();} <br>
	 * {@code applyTransformation(leftArm);} <br>
	 * {@code drawArm();} <br>
	 * {@code popMatrix();} <br>
	 * {@code pushMatrix();} <br>
	 * {@code applyTransformation(rightArm);} <br>
	 * {@code drawArm();} <br>
	 * {@code popMatrix();} <br>
	 * {@code popMatrix();} <br>
	 * <p>
	 * If the frame hierarchy to be drawn should be applied to a different renderer
	 * context than the PApplet's (e.g., an off-screen rendering context), you may
	 * call {@code renderer().pushMatrix();} and {@code renderer().popMatrix();} above.
	 * <p> 
	 * Note the use of nested {@code pushMatrix()} and {@code popMatrix()} blocks
	 * to represent the frame hierarchy: {@code leftArm} and {@code rightArm} are
	 * both correctly drawn with respect to the {@code body} coordinate system.
	 * <p>
	 * <b>Attention:</b> When drawing a frame hierarchy as above, this method
	 * should be used whenever possible.
	 */
	public void applyTransformation(GeomFrame frame) {
		if( is2D() ) {
			translate(frame.translation().x(), frame.translation().y());
			rotate(frame.rotation().angle());
			scale(frame.scaling().x(), frame.scaling().y());
		}
		else {
			translate( frame.translation().vec[0], frame.translation().vec[1], frame.translation().vec[2] );
			rotate( frame.rotation().angle(), ((Quaternion)frame.rotation()).axis().vec[0], ((Quaternion)frame.rotation()).axis().vec[1], ((Quaternion)frame.rotation()).axis().vec[2]);
			scale(frame.scaling().x(), frame.scaling().y(), frame.scaling().z());
		}
	}
	
	public void applyWorldTransformation(GeomFrame frame) {
		GeomFrame refFrame = frame.referenceFrame();
		if(refFrame != null) {
			applyWorldTransformation(refFrame);
			applyTransformation(frame);
		}
		else {
			applyTransformation(frame);
		}
	}	
	
	/**
	 * Returns the approximate frame rate of the software as it executes.
	 * The initial value is 10 fps and is updated with each frame.
	 * The value is averaged (integrated) over several frames.
	 * As such, this value won't be valid until after 5-10 frames.
	 */
	public float frameRate() {
		return frameRate;
	}
	//public abstract float frameRate();
	
	/**
	 * Returns the number of frames displayed since the program started.
	 */
	public long frameCount() {
		return frameCount;
	}
	//public abstract long frameCount();
	
	// 1. Associated objects
	
	public boolean timersAreSingleThreaded() {
		return singleThreadedTaskableTimers;
	}
	
	/**
	 * Returns the associated Camera, never {@code null}.
	 */
	public Pinhole pinhole() {
		return ph;
	}
	
	/**
	 * Returns the associated Camera, never {@code null}.
	 */
	public Camera camera() {
		if (this.is3D())
			return (Camera) ph;
		else 
			throw new RuntimeException("Camera type is only available in 3D");
	}
	
	public ViewWindow viewWindow() {
		if (this.is2D())
			return (ViewWindow) ph;
		else 
			throw new RuntimeException("ViewWindow type is only available in 2D");
	}
	
	//TODO change review API here
	/**
	public float viewWindowSize() {
		if (is2D())
			return viewWindow().scaleFactor();
		else
			throw new RuntimeException("size is only available in 2D");
	}
	*/
	
	public void setViewWindowSize(float s) {
		if (is2D())
			viewWindow().setScaling(s);
		else
			throw new RuntimeException("size is only available in 2D");		
	}

	/**
	 * Replaces the current {@link #pinhole()} with {@code vp}
	 */
	public void setViewPort(Pinhole vp)  {
		if (vp == null)
			return;

		vp.setSceneRadius(radius());		
		vp.setSceneCenter(center());

		vp.setScreenWidthAndHeight(width(), height());
    
		ph = vp;		

		showAll();
	}
	
	/**
	 * Returns {@code true} if automatic update of the camera frustum plane
	 * equations is enabled and {@code false} otherwise. Computation of the
	 * equations is expensive and hence is disabled by default.
	 * 
	 * @see #toggleFrustumEquationsUpdate()
	 * @see #disableFrustumEquationsUpdate()
	 * @see #enableFrustumEquationsUpdate()
	 * @see #enableFrustumEquationsUpdate(boolean)
	 * @see remixlab.remixcam.core.Camera#updateFrustumEquations()
	 */
	public boolean frustumEquationsUpdateIsEnable() {
		return pinhole().frustumEquationsUpdateIsEnable();
	}

	/**
	 * Toggles automatic update of the camera frustum plane equations every frame.
	 * Computation of the equations is expensive and hence is disabled by default.
	 * 
	 * @see #frustumEquationsUpdateIsEnable()
	 * @see #disableFrustumEquationsUpdate()
	 * @see #enableFrustumEquationsUpdate()
	 * @see #enableFrustumEquationsUpdate(boolean)
	 * @see remixlab.remixcam.core.Camera#updateFrustumEquations()
	 */
	public void toggleFrustumEquationsUpdate() {
		if ( frustumEquationsUpdateIsEnable() )
			disableFrustumEquationsUpdate();
		else
			enableFrustumEquationsUpdate();
	}

	/**
	 * Disables automatic update of the camera frustum plane equations every
	 * frame. Computation of the equations is expensive and hence is disabled by
	 * default.
	 * 
	 * @see #frustumEquationsUpdateIsEnable()
	 * @see #toggleFrustumEquationsUpdate()
	 * @see #enableFrustumEquationsUpdate()
	 * @see #enableFrustumEquationsUpdate(boolean)
	 * @see remixlab.remixcam.core.Camera#updateFrustumEquations()
	 */
	public void disableFrustumEquationsUpdate() {
		enableFrustumEquationsUpdate(false);
	}

	/**
	 * Enables automatic update of the camera frustum plane equations every frame.
	 * Computation of the equations is expensive and hence is disabled by default.
	 * 
	 * @see #frustumEquationsUpdateIsEnable()
	 * @see #toggleFrustumEquationsUpdate()
	 * @see #disableFrustumEquationsUpdate()
	 * @see #enableFrustumEquationsUpdate(boolean)
	 * @see remixlab.remixcam.core.Camera#updateFrustumEquations()
	 */
	public void enableFrustumEquationsUpdate() {
		enableFrustumEquationsUpdate(true);
	}

	/**
	 * Enables or disables automatic update of the camera frustum plane equations
	 * every frame according to {@code flag}. Computation of the equations is
	 * expensive and hence is disabled by default.
	 * 
	 * @see #frustumEquationsUpdateIsEnable()
	 * @see #toggleFrustumEquationsUpdate()
	 * @see #disableFrustumEquationsUpdate()
	 * @see #enableFrustumEquationsUpdate()
	 * @see remixlab.remixcam.core.Camera#updateFrustumEquations()
	 */
	public void enableFrustumEquationsUpdate(boolean flag) {
		pinhole().enableFrustumEquationsUpdate(flag);
	}
	
	/**
	 * Toggles the {@link #pinhole()} type between PERSPECTIVE and ORTHOGRAPHIC.
	 */
	public void toggleCameraType() {
		if( this.is2D() ) {
			System.out.println("Warning: Camera Type is only available in 3D");
		}
		else {
		if (((Camera) pinhole()).type() == Camera.Type.PERSPECTIVE)
			setCameraType(Camera.Type.ORTHOGRAPHIC);
		else
			setCameraType(Camera.Type.PERSPECTIVE);
		}
	}

	/**
	 * Toggles the {@link #pinhole()} kind between PROSCENE and STANDARD.
	 */
	public void toggleCameraKind() {
		if( this.is2D() ) {
			System.out.println("Warning: Camera Kind is only available in 3D");
		}
		else {
		if (((Camera) pinhole()).kind() == Camera.Kind.PROSCENE)
			setCameraKind(Camera.Kind.STANDARD);
		else
			setCameraKind(Camera.Kind.PROSCENE);
		}
	}
	
	/**
	 * Returns the current {@link #pinhole()} type.
	 */
	public final Camera.Type cameraType() {
		if( this.is2D() ) {
			System.out.println("Warning: Camera Type is only available in 3D");
			return null;
		}
		else
			return ((Camera) pinhole()).type();		
	}

	/**
	 * Sets the {@link #pinhole()} type.
	 */
	public void setCameraType(Camera.Type type) {
		if( this.is2D() ) {
			System.out.println("Warning: Camera Type is only available in 3D");			
		}
		else
			if (type != ((Camera) pinhole()).type())
				((Camera) pinhole()).setType(type);
	}

	/**
	 * Returns the current {@link #pinhole()} kind.
	 */
	public final Camera.Kind cameraKind() {
		if( this.is2D() ) {
			System.out.println("Warning: Camera Kype is only available in 3D");
			return null;
		}
		return ((Camera) pinhole()).kind();
	}

	/**
	 * Sets the {@link #pinhole()} kind.
	 */
	public void setCameraKind(Camera.Kind kind) {
		if( this.is2D() ) {
			System.out.println("Warning: Camera Kind is only available in 3D");			
		}
		else {
		if (kind != ((Camera) pinhole()).kind()) {
			((Camera) pinhole()).setKind(kind);
			if (kind == Camera.Kind.PROSCENE)
				System.out.println("Changing camera kind to Proscene");
			else
				System.out.println("Changing camera kind to Standard");
		}
		}
	}
	
	/**
	 * Returns a list containing references to all the active MouseGrabbers.
	 * <p>
	 * Used to parse all the MouseGrabbers and to check if any of them
	 * {@link remixlab.remixcam.core.DeviceGrabbable#grabsCursor()} using
	 * {@link remixlab.remixcam.core.DeviceGrabbable#checkIfGrabsDevice(int, int, Camera)}.
	 * <p>
	 * You should not have to directly use this list. Use
	 * {@link #removeFromDeviceGrabberPool(DeviceGrabbable)} and
	 * {@link #addInDeviceGrabberPool(DeviceGrabbable)} to modify this list.
	 */
	public List<DeviceGrabbable> deviceGrabberPool() {
		return msGrabberPool;
	}
	
	/**
	 * Returns {@code true} if a mouse moved event  is called even when no mouse button is pressed.
	 * <p>
	 * You need to {@link #setDeviceTracking(boolean)} to {@code true} in order to use MouseGrabber
	 * (see {@link #deviceGrabber()}).
	 */
	public boolean isTrackingDevice() {
		return deviceTrckn;
	}
	
	/**
	 * Sets the {@link #isTrackingDevice()} value.
	 */
	public void setDeviceTracking(boolean enable) {		
		if(!enable) {
			if( deviceGrabber() != null )
				deviceGrabber().setGrabsCursor(false);
			setDeviceGrabber(null);
		}
		deviceTrckn = enable;
	}
	
	/**
	 * Calls {@link #setDeviceTracking(boolean)} to toggle the {@link #isTrackingDevice()} value.
	 */
	public void toggleDeviceTracking() {
		setDeviceTracking(!isTrackingDevice());
	}
	  
	public void registerJob(AbstractTimerJob job) {
		job.setTimer(new SingleThreadedTaskableTimer(this, job));
		timerPool.add(job);
	}
	
	// TODO need this methods here?
  // need it here (or it should just go into proscene.js)? need to be overloaded?
	// it was previously set in proscene.Scene
	public void unregisterJob(SingleThreadedTaskableTimer t) {		
			timerPool.remove( t.timerJob() );
	}
	
	public void unregisterJob(AbstractTimerJob job) {
		timerPool.remove(job);		
	}  
	
	public boolean isJobRegistered(AbstractTimerJob job) {
		return timerPool.contains(job);
	}		
	
  //2. Associated objects
	
	/**
	 * Returns the InteractiveFrame associated to this Scene. It could be null if
	 * there's no InteractiveFrame associated to this Scene.
	 * 
	 * @see #setInteractiveFrame(InteractiveFrame)
	 */
	public InteractiveFrame interactiveFrame() {
		return glIFrame;
	}
	
	/**
	 * Returns the avatar object to be tracked by the Camera when it
	 * is in Third Person mode.
	 * <p>
	 * Simply returns {@code null} if no avatar has been set.
	 */
	public Trackable avatar() {
		return trck;
	}

	/**
	 * Sets the avatar object to be tracked by the Camera when it is in Third
	 * Person mode.
	 * 
	 * @see #unsetAvatar()
	 */
	public void setAvatar(Trackable t) {
		trck = t;
		avatarIsInteractiveAvatarFrame = false;
		avatarIsInteractiveDrivableFrame = false;
		if (avatar() instanceof InteractiveAvatarFrame) {
			avatarIsInteractiveAvatarFrame = true;
			avatarIsInteractiveDrivableFrame = true;
			if (interactiveFrame() != null)
				((InteractiveDrivableFrame) interactiveFrame()).setFlySpeed(0.01f * radius());
		} else if (avatar() instanceof InteractiveDrivableFrame) {
			avatarIsInteractiveAvatarFrame = false;
			avatarIsInteractiveDrivableFrame = true;
			if (interactiveFrame() != null)
				((InteractiveDrivableFrame) interactiveFrame()).setFlySpeed(0.01f * radius());
		}
	}

	/**
	 * If there's a avatar unset it.
	 * 
	 * @see #setAvatar(Trackable)
	 */
	public void unsetAvatar() {
		trck = null;
		avatarIsInteractiveAvatarFrame = false;
		avatarIsInteractiveDrivableFrame = false;
	}

	/**
	 * Sets {@code frame} as the InteractiveFrame associated to this Scene. If
	 * {@code frame} is instance of Trackable it is also automatically set as the
	 * Scene {@link #avatar()} (by automatically calling {@code
	 * setAvatar((Trackable) frame)}).
	 * 
	 * @see #interactiveFrame()
	 * @see #setAvatar(Trackable)
	 */
	public void setInteractiveFrame(InteractiveFrame frame) {
		glIFrame = frame;		
		if (glIFrame == null)
			iFrameIsDrwn = false;
		else if (glIFrame instanceof Trackable)
			setAvatar((Trackable) glIFrame);
	}

	/**
	 * Returns the current MouseGrabber, or {@code null} if none currently grabs
	 * mouse events.
	 * <p>
	 * When {@link remixlab.remixcam.core.DeviceGrabbable#grabsCursor()}, the different
	 * mouse events are sent to it instead of their usual targets (
	 * {@link #pinhole()} or {@link #interactiveFrame()}).
	 */
	public DeviceGrabbable deviceGrabber() {
		return deviceGrbbr;
	}
	
	/**
	 * Directly defines the {@link #deviceGrabber()}.
	 * <p>
	 * You should not call this method directly as it bypasses the
	 * {@link remixlab.remixcam.core.DeviceGrabbable#checkIfGrabsDevice(int, int, Camera)}
	 * test performed by parsing the mouse moved event.
	 */
	public void setDeviceGrabber(DeviceGrabbable deviceGrabber) {
		deviceGrbbr = deviceGrabber;

		deviceGrabberIsAnIFrame = deviceGrabber instanceof InteractiveFrame;
	}
	
	// 3. Device grabber handling
	
	/**
	 * Returns true if the mouseGrabber is currently in the {@link #deviceGrabberPool()} list.
	 * <p>
	 * When set to false using {@link #removeFromDeviceGrabberPool(DeviceGrabbable)}, the Scene no longer
	 * {@link remixlab.remixcam.core.DeviceGrabbable#checkIfGrabsDevice(int, int, Camera)} on this mouseGrabber.
	 * Use {@link #addInDeviceGrabberPool(DeviceGrabbable)} to insert it back.
	 */
	public boolean isInDeviceGrabberPool(DeviceGrabbable deviceGrabber) {
		return deviceGrabberPool().contains(deviceGrabber);
	}
	
	/**
	 * Adds the mouseGrabber in the {@link #deviceGrabberPool()}.
	 * <p>
	 * All created InteractiveFrames (which are MouseGrabbers) are automatically added in the
	 * {@link #deviceGrabberPool()} by their constructors. Trying to add a
	 * mouseGrabber that already {@link #isInDeviceGrabberPool(DeviceGrabbable)} has no effect.
	 * <p>
	 * Use {@link #removeFromDeviceGrabberPool(DeviceGrabbable)} to remove the mouseGrabber from
	 * the list, so that it is no longer tested with
	 * {@link remixlab.remixcam.core.DeviceGrabbable#checkIfGrabsDevice(int, int, Camera)}
	 * by the Scene, and hence can no longer grab mouse focus. Use
	 * {@link #isInDeviceGrabberPool(DeviceGrabbable)} to know the current state of the MouseGrabber.
	 */
	public void addInDeviceGrabberPool(DeviceGrabbable deviceGrabber) {
		if (!isInDeviceGrabberPool(deviceGrabber))
			deviceGrabberPool().add(deviceGrabber);
	}

	/**
	 * Removes the mouseGrabber from the {@link #deviceGrabberPool()}.
	 * <p>
	 * See {@link #addInDeviceGrabberPool(DeviceGrabbable)} for details. Removing a mouseGrabber
	 * that is not in {@link #deviceGrabberPool()} has no effect.
	 */
	public void removeFromDeviceGrabberPool(DeviceGrabbable deviceGrabber) {
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
	
	// 2. Local timer
	/**
	 * Called from the timer to stop displaying the point under pixel and arcball
	 * reference point visual hints.
	 */
	protected void unSetTimerFlag() {
		arpFlag = false;
		pupFlag = false;
	}
	
	// 3. Scene dimensions

	/**
	 * Returns the scene radius.
	 * <p>
	 * Convenience wrapper function that simply calls {@code
	 * camera().sceneRadius()}
	 * 
	 * @see #setRadius(float)
	 * @see #center()
	 */
	public float radius() {
		return pinhole().sceneRadius();
	}

	/**
	 * Returns the scene center.
	 * <p>
	 * Convenience wrapper function that simply returns {@code
	 * camera().sceneCenter()}
	 * 
	 * @see #setCenter(Vector3D) {@link #radius()}
	 */
	public Vector3D center() {
		return pinhole().sceneCenter();
	}

	/**
	 * Returns the arcball reference point.
	 * <p>
	 * Convenience wrapper function that simply returns {@code
	 * camera().arcballReferencePoint()}
	 * 
	 * @see #setCenter(Vector3D) {@link #radius()}
	 */
	public Vector3D arcballReferencePoint() {
		return pinhole().arcballReferencePoint();
	}

	/**
	 * Sets the {@link #radius()} of the Scene.
	 * <p>
	 * Convenience wrapper function that simply returns {@code
	 * camera().setSceneRadius(radius)}
	 * 
	 * @see #setCenter(Vector3D)
	 */
	public void setRadius(float radius) {
		pinhole().setSceneRadius(radius);
	}

	/**
	 * Sets the {@link #center()} of the Scene.
	 * <p>
	 * Convenience wrapper function that simply calls {@code }
	 * 
	 * @see #setRadius(float)
	 */
	public void setCenter(Vector3D center) {
		pinhole().setSceneCenter(center);
	}

	/**
	 * Sets the {@link #center()} and {@link #radius()} of the Scene from the
	 * {@code min} and {@code max} vectors.
	 * <p>
	 * Convenience wrapper function that simply calls {@code
	 * camera().setSceneBoundingBox(min,max)}
	 * 
	 * @see #setRadius(float)
	 * @see #setCenter(Vector3D)
	 */
	public void setBoundingBox(Vector3D min, Vector3D max) {
		if( this.is2D() )
			System.out.println("setBoundingBox is available only in 3D. Use setBoundingRect instead");
		else
			((Camera) pinhole()).setSceneBoundingBox(min, max);
	}
	
	public void setBoundingRect(Vector3D min, Vector3D max) {
		if( this.is3D() )
			System.out.println("setBoundingRect is available only in 2D. Use setBoundingBox instead");
		else
			((ViewWindow) pinhole()).setSceneBoundingRect(min, max);
	}

	/**
	 * Convenience wrapper function that simply calls {@code
	 * camera().showEntireScene()}
	 * 
	 * @see remixlab.remixcam.core.Camera#showEntireScene()
	 */
	public void showAll() {
		pinhole().showEntireScene();
	}

	/**
	 * Convenience wrapper function that simply returns {@code
	 * camera().setArcballReferencePointFromPixel(pixel)}.
	 * <p>
	 * Current implementation set no
	 * {@link remixlab.remixcam.core.Camera#arcballReferencePoint()}. Override
	 * {@link remixlab.remixcam.core.Camera#pointUnderPixel(Point)} in your openGL
	 * based camera for this to work.
	 * 
	 * @see remixlab.remixcam.core.Camera#setArcballReferencePointFromPixel(Point)
	 * @see remixlab.remixcam.core.Camera#pointUnderPixel(Point)
	 */
	public boolean setArcballReferencePointFromPixel(Point pixel) {
		return pinhole().setArcballReferencePointFromPixel(pixel);
	}
	
	/**
	 * Convenience wrapper function that simply returns {@code
	 * camera().setSceneCenterFromPixel(pixel)}
	 * <p>
	 * Current implementation set no
	 * {@link remixlab.remixcam.core.Camera#sceneCenter()}. Override
	 * {@link remixlab.remixcam.core.Camera#pointUnderPixel(Point)} in your openGL
	 * based camera for this to work.
	 * 
	 * @see remixlab.remixcam.core.Camera#setSceneCenterFromPixel(Point)
	 * @see remixlab.remixcam.core.Camera#pointUnderPixel(Point)
	 */
	public boolean setCenterFromPixel(Point pixel) {
		return pinhole().setSceneCenterFromPixel(pixel);
	}
	
	// * Control what is drawing
	
	/**
	 * Toggles the state of {@link #axisIsDrawn()}.
	 * 
	 * @see #axisIsDrawn()
	 * @see #setAxisIsDrawn(boolean)
	 */
	public void toggleAxisIsDrawn() {
		setAxisIsDrawn(!axisIsDrawn());
	}

	/**
	 * Toggles the state of {@link #gridIsDrawn()}.
	 * 
	 * @see #setGridIsDrawn(boolean)
	 */
	public void toggleGridIsDrawn() {
		setGridIsDrawn(!gridIsDrawn());
	}

	/**
	 * Toggles the state of {@link #frameSelectionHintIsDrawn()}.
	 * 
	 * @see #setFrameSelectionHintIsDrawn(boolean)
	 */
	public void toggleFrameSelectionHintIsDrawn() {
		setFrameSelectionHintIsDrawn(!frameSelectionHintIsDrawn());
	}

	/**
	 * Toggles the state of {@link #cameraPathsAreDrawn()}.
	 * 
	 * @see #setCameraPathsAreDrawn(boolean)
	 */
	public void toggleCameraPathsAreDrawn() {
		setCameraPathsAreDrawn(!cameraPathsAreDrawn());
	}	

	/**
	 * Toggles the draw with constraint on and off.
	 */
	/**
	public void toggleDrawWithConstraint() {
		if (drawIsConstrained())
			setDrawWithConstraint(false);
		else
			setDrawWithConstraint(true);
	}	
	*/
	
	/**
	 * Returns {@code true} if axis is currently being drawn and {@code false}
	 * otherwise.
	 */
	public boolean axisIsDrawn() {
		return axisIsDrwn;
	}

	/**
	 * Returns {@code true} if grid is currently being drawn and {@code false}
	 * otherwise.
	 */
	public boolean gridIsDrawn() {
		return gridIsDrwn;
	}

	/**
	 * Returns {@code true} if the frames selection visual hints are currently
	 * being drawn and {@code false} otherwise.
	 */
	public boolean frameSelectionHintIsDrawn() {
		return frameSelectionHintIsDrwn;
	}

	/**
	 * Returns {@code true} if the camera key frame paths are currently being
	 * drawn and {@code false} otherwise.
	 */
	public boolean cameraPathsAreDrawn() {
		return cameraPathsAreDrwn;
	}

	/**
	 * Returns {@code true} if axis is currently being drawn and {@code false}
	 * otherwise.
	 */
	public boolean interactiveFrameIsDrawn() {
		return iFrameIsDrwn;
	}

	/**
	 * Convenience function that simply calls {@code setAxisIsDrawn(true)}
	 */
	public void setAxisIsDrawn() {
		setAxisIsDrawn(true);
	}

	/**
	 * Sets the display of the axis according to {@code draw}
	 */
	public void setAxisIsDrawn(boolean draw) {
		axisIsDrwn = draw;
	}

	/**
	 * Convenience function that simply calls {@code setGridIsDrawn(true)}
	 */
	public void setGridIsDrawn() {
		setGridIsDrawn(true);
	}

	/**
	 * Sets the display of the grid according to {@code draw}
	 */
	public void setGridIsDrawn(boolean draw) {
		gridIsDrwn = draw;
	}

	/**
	 * Sets the display of the interactive frames' selection hints according to
	 * {@code draw}
	 */
	public void setFrameSelectionHintIsDrawn(boolean draw) {
		frameSelectionHintIsDrwn = draw;
	}

	/**
	 * Sets the display of the camera key frame paths according to {@code draw}
	 */
	public void setCameraPathsAreDrawn(boolean draw) {
		cameraPathsAreDrwn = draw;
	}
	
	/**
	 * Returns {@code true} if drawn is currently being constrained and {@code
	 * false} otherwise.
	 */
	/**
	public boolean drawIsConstrained() {
		return withConstraint;
	}
	*/

	/**
	 * Constrain frame displacements according to {@code wConstraint}
	 */
	/**
	public void setDrawWithConstraint(boolean wConstraint) {
		withConstraint = wConstraint;
	}
	*/
	
	// Abstract drawing methods
		
	/**
	 * Same as {@code cone(det, 0, 0, r, h);}
	 * 
	 * @see #cone(int, float, float, float, float)
	 */
	public void cone(int det, float r, float h) {
		cone(det, 0, 0, r, h);
	}		
	
	/**
	 * Same as {@code cone(12, 0, 0, r, h);}
	 * 
	 * @see #cone(int, float, float, float, float)
	 */
	public void cone(float r, float h) {
		cone(12, 0, 0, r, h);
	}	
	
	/**
	 * Same as {@code cone(det, 0, 0, r1, r2, h);}
	 * 
	 * @see #cone(int, float, float, float, float, float)
	 */
	public void cone(int det, float r1, float r2, float h) {
		cone(det, 0, 0, r1, r2, h);
	}	
	
	/**
	 * Same as {@code cone(18, 0, 0, r1, r2, h);}
	 * 
	 * @see #cone(int, float, float, float, float, float)
	 */
	public void cone(float r1, float r2, float h) {
		cone(18, 0, 0, r1, r2, h);
	}	
	
	/**
	 * Convenience function that simply calls {@code drawAxis(100)}.
	 */
	public void drawAxis() {
		drawAxis(100);
	}	
	
	/**
	 * Simply calls {@code drawArrow(length, 0.05f * length)}
	 * 
	 * @see #drawArrow(float, float)
	 */
	public void drawArrow(float length) {
		drawArrow(length, 0.05f * length);
	}
	
	/**
	 * Draws a 3D arrow along the positive Z axis.
	 * <p>
	 * {@code length} and {@code radius} define its geometry.
	 * <p>
	 * Use {@link #drawArrow(Vector3D, Vector3D, float)} to place the arrow
	 * in 3D.
	 */
	public void drawArrow(float length, float radius) {
		float head = 2.5f * (radius / length) + 0.1f;
		float coneRadiusCoef = 4.0f - 5.0f * head;

		cylinder(radius, length * (1.0f - head / coneRadiusCoef));
		translate(0.0f, 0.0f, length * (1.0f - head));
		cone(coneRadiusCoef * radius, head * length);
		translate(0.0f, 0.0f, -length * (1.0f - head));
	}
	
	/**
	 * Draws a 3D arrow between the 3D point {@code from} and the 3D point {@code
	 * to}, both defined in the current world coordinate system.
	 * 
	 * @see #drawArrow(float, float)
	 */
	public void drawArrow(Vector3D from, Vector3D to,	float radius) {
		pushMatrix();
		translate(from.x(), from.y(), from.z());
		applyMatrix(new Quaternion(new Vector3D(0, 0, 1), Vector3D.sub(to,	from)).matrix());
		drawArrow(Vector3D.sub(to, from).mag(), radius);
		popMatrix();
	}
	
	public void drawDottedGrid() {
		drawDottedGrid(100, 10);
	}
	
	/**
	 * Convenience function that simply calls {@code drawGrid(100, 10)}
	 * 
	 * @see #drawGrid(float, int)
	 */
	public void drawGrid() {
		drawGrid(100, 10);
	}
	
	public void drawDottedGrid(float size) {
		drawDottedGrid(size, 10);
	}
		
	/**
	 * Convenience function that simply calls {@code drawGrid(size, 10)}
	 * 
	 * @see #drawGrid(float, int)
	 */
	public void drawGrid(float size) {
		drawGrid(size, 10);
	}
	
	public void drawDottedGrid(int nbSubdivisions) {
		drawDottedGrid(100, nbSubdivisions);
	}
	
	/**
	 * Convenience function that simply calls {@code drawGrid(100, nbSubdivisions)}
	 * 
	 * @see #drawGrid(float, int)
	 */
	public void drawGrid(int nbSubdivisions) {
		drawGrid(100, nbSubdivisions);
	}
	
	/**
	 * Convenience function that simply calls {@code drawCamera(camera, true, 1.0f)}
	 * 
	 * @see #drawCamera(Camera, boolean, float)
	 */
	public void drawCamera(Camera camera) {
		drawCamera(camera, true, 1.0f);
	}		

	/**
	 * Convenience function that simply calls {@code drawCamera(camera, true, scale)}
	 * 
	 * @see #drawCamera(Camera, boolean, float)
	 */
	public void drawCamera(Camera camera, float scale) {
		drawCamera(camera, true, scale);
	}
	
	/**
	 * Convenience function that simply calls {@code drawCamera(camera, drawFarPlane, 1.0f)}
	 * 
	 * @see #drawCamera(Camera, boolean, float)
	 */
	public void drawCamera(Camera camera, boolean drawFarPlane) {
		drawCamera(camera, drawFarPlane, 1.0f);
	}
	
	/**
	 * Convenience function that simply calls {@code drawViewWindow(camera, 1)}
	 * 
	 * @see #drawViewWindow(ViewWindow, float)
	 */
	public void drawViewWindow(ViewWindow vWindow) {
		drawViewWindow(vWindow, 1);
	}
		
	/**
	 * Draws all InteractiveFrames' selection regions: a shooter target
	 * visual hint of {@link remixlab.remixcam.core.InteractiveFrame#grabsDeviceThreshold()} pixels size.
	 * 
	 * <b>Attention:</b> If the InteractiveFrame is part of a Camera path draws
	 * nothing.
	 * 
	 * @see #drawCameraPathSelectionHints()
	 */
	protected abstract void drawSelectionHints();
	
	/**
	 * Draws the selection regions (a shooter target visual hint of
	 * {@link remixlab.remixcam.core.InteractiveFrame#grabsDeviceThreshold()} pixels size) of all
	 * InteractiveFrames forming part of the Camera paths.
	 * 
	 * @see #drawSelectionHints()
	 */
	protected abstract void drawCameraPathSelectionHints();
		
	/**
	 * Convenience function that simply calls
	 * {@code drawCross(pg3d.color(255, 255, 255), px, py, 15, 3)}.
	 */
	public void drawCross(float px, float py) {
		drawCross(px, py, 15);
	}
		
	/**
	 * Convenience function that simply calls
	 * {@code drawFilledCircle(40, center, radius)}.
	 * 
	 * @see #drawFilledCircle(int, Vector3D, float)
	 */
	public void drawFilledCircle(Vector3D center, float radius) {
		drawFilledCircle(40, center, radius);
	}
	
	protected abstract Camera.WorldPoint pointUnderPixel(Point pixel);
	
  //dimensions
  public abstract int width();
  
  public abstract int height();
  

  // WARNINGS and EXCEPTIONS
     
  static protected HashMap<String, Object> warnings;

  /**
   * Show a renderer error, and keep track of it so that it's only shown once.
   * @param msg the error message (which will be stored for later comparison)
   */
  static public void showWarning(String msg) {  // ignore
    if (warnings == null) {
      warnings = new HashMap<String, Object>();
    }
    if (!warnings.containsKey(msg)) {
      System.err.println(msg);
      warnings.put(msg, new Object());
    }
  }
  
  /**
   * Display a warning that the specified method is only available with 3D.
   * @param method The method name (no parentheses)
   */
  static public void showDepthWarning(String method) {
    showWarning(method + "() can only be used with a renderer that supports 3D, such as P3D or OPENGL.");
  }

  /**
   * Display a warning that the specified method that takes x, y, z parameters
   * can only be used with x and y parameters in this renderer.
   * @param method The method name (no parentheses)
   */
  static public void showDepthWarningXYZ(String method) {
    showWarning(method + "() with x, y, and z coordinates " +
                "can only be used with a renderer that " +
                "supports 3D, such as P3D or OPENGL. " +
                "Use a version without a z-coordinate instead.");
  }

  /**
   * Display a warning that the specified method is simply unavailable.
   */
  static public void showMethodWarning(String method) {
    showWarning(method + "() is not available with this renderer.");
  }

  /**
   * Error that a particular variation of a method is unavailable (even though
   * other variations are). For instance, if vertex(x, y, u, v) is not
   * available, but vertex(x, y) is just fine.
   */
  static public void showVariationWarning(String str) {
    showWarning(str + " is not available with this renderer.");
  }

  /**
   * Display a warning that the specified method is not implemented, meaning
   * that it could be either a completely missing function, although other
   * variations of it may still work properly.
   */
  static public void showMissingWarning(String method) {
    showWarning(method + "(), or this particular variation of it, " +
                "is not available with this renderer.");
  }
  
  /**
   * Display a warning that the specified method lacks implementation.
   */
  static public void showMissingImplementationWarning(String method) {
    showWarning(method + "(), should be implemented by your AbstractScene, " +
                "derived class.");
  }
}
