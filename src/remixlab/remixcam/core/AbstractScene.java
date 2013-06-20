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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import remixlab.remixcam.event.*;
import remixlab.remixcam.geom.*;
import remixlab.remixcam.interactivity.ClickEvent;
import remixlab.remixcam.interactivity.KeyboardEvent;
import remixlab.remixcam.device.*;
import remixlab.remixcam.renderer.*;
import remixlab.remixcam.util.*;

public abstract class AbstractScene implements Constants {
	/**
	protected class GrabbableWrap {
		protected Grabbable grabbable;
		protected Actionable<?> lastAction;
		public GrabbableWrap(Grabbable g) {
			this(g, null);
		}
		public GrabbableWrap(Grabbable g, Actionable<?> a) {
			this.grabbable = g;
			lastAction = a;
		}
		public void updateAction(Actionable<?> action) {
			lastAction = action;
		}
	}
	*/
	
  //M o u s e G r a b b e r
	protected List<Grabbable> msGrabberPool;
	protected Grabbable deviceGrbbr;
	protected Actionable<?> lastDeviceGrbbrAction;
	public boolean deviceGrabberIsAnIFrame;//false by default, see: http://stackoverflow.com/questions/3426843/what-is-the-default-initialization-of-an-array-in-java
	protected boolean deviceTrckn;
	
	protected boolean dottedGrid;	
	
  //O B J E C T S
	protected Renderable renderer;
	protected Pinhole ph;
	protected InteractiveFrame glIFrame;
	protected boolean iFrameIsDrwn;
	protected Trackable trck;
	public boolean avatarIsInteractiveDrivableFrame;
	protected boolean avatarIsInteractiveAvatarFrame;
	
  //E X C E P T I O N H A N D L I N G
	protected int startCoordCalls;
	
  // T i m e r P o o l
  //T I M E R S
  protected boolean singleThreadedTaskableTimers;
	protected ArrayList<AbstractTimerJob> timerPool;

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
  protected HashMap<String, AbstractDevice> devices;
	//protected ArrayList<HIDevice> devices;
	protected LinkedList<GenericEvent<?>> eventQueue;
	
	// L O C A L   T I M E R
	protected boolean arpFlag;
	protected boolean pupFlag;
	protected DLVector pupVec;
	protected AbstractTimerJob timerFx;
	
	// S I Z E
	protected int width, height;
	
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
		msGrabberPool = new ArrayList<Grabbable>();
		//devices
		//TODO pending device instantiation here
		devices = new HashMap<String, AbstractDevice>();
		//events
		eventQueue = new LinkedList<GenericEvent<?>>();
		// <- 1
		
		setDottedGrid(true);
		setRightHanded();
		
		//TODO pending
		//setDefaultShortcuts();
	}
	
	public InteractiveFrame aliveInteractiveFrame() {
		if (deviceGrabber() != null) {
			if (deviceGrabberIsAnIFrame) { //covers also the case when mouseGrabberIsADrivableFrame
				return (InteractiveFrame) deviceGrabber();
			} else
			return null;
		}
		if (interactiveFrameIsDrawn()) {
			return interactiveFrame();
		}
		return null;
	}
	
	/**
	 * Returns an array of the camera profile objects that are currently
	 * registered at the Scene.
	 */
	public AbstractDevice [] getDevices() {
		return devices.values().toArray(new AbstractDevice[0]);
	}
	
	/**
	 * Adds an HIDevice to the scene.
	 * 
	 * @see #unregisterProfile(HIDevice)
	 * @see #removeAllDevices()
	 */
	public void registerDevice(AbstractDevice device) {
		if(!isDeviceRegistered(device))
			devices.put(device.name(), device);
		else {
			System.out.println("Nothing done. A device with the same name is already registered. Current profile names are:");
			for (AbstractDevice dev : devices.values())
				System.out.println(dev.name());
		}
	}
	
	public boolean isDeviceRegistered(AbstractDevice device) {
		return devices.containsKey(device.name());
	}
	
	public boolean isDeviceRegistered(String name) {
		return devices.containsKey(name);
	}
	
	public AbstractDevice getDevice(String name) {
		return devices.get(name);
	}
	
	/**
	 * Removes the device from the scene.
	 * 
	 * @see #registerProfile(HIDevice)
	 * @see #removeAllDevices()
	 */
	public AbstractDevice unregisterDevice(AbstractDevice device) {
		return devices.remove(device.name());
	}

	public AbstractDevice unregisterDevice(String name) {
		return devices.remove(name);
	}
	
	/**
	 * Removes all registered devices from the scene.
	 * 
	 * @see #registerProfile(HIDevice)
	 * @see #unregisterProfile(HIDevice)
	 */
	public void unregisterAllDevices() {
		devices.clear();
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
		//TODO pending
		//if (!draw && currentCameraProfile() instanceof ThirdPersonCameraProfile	&& interactiveFrame().equals(avatar()))// more natural than to bypass it
			//return;
		iFrameIsDrwn = draw;
	}
	
	// E V E N T   HA N D L I N G
	
	/**
	 * Internal method. Handles the different global keyboard actions.
	 */
	//public void handleKeyboardAction(DOF_0Action id) {
	public void handleEvent(GenericEvent<?> event) {
		if( !(event instanceof ClickEvent) && ! (event instanceof KeyboardEvent))
			return;
		
		Actionable<DLAction> a=null;
		if(event instanceof ClickEvent)
			a = ((ClickEvent) event).getAction();
		if(event instanceof KeyboardEvent)
			a = ((KeyboardEvent) event).getAction();		
		if(a == null) return;
		DLAction id = a.action();
		//if(id == null) return;
		
	  //TODO debug
		/*+
		if( event instanceof DLKeyEvent )
			System.out.println("Key event " + id.description());
		else
			if( event instanceof DLClickEvent )
				System.out.println("click event " + id.description());
		*/
		
		if( !id.is2D() && this.is2D() )
			return;
		DLVector trans;
		switch (id) {
		//case NO_ACTION:	break;
		case DRAW_AXIS:
			toggleAxisIsDrawn();
			break;
		case DRAW_GRID:
			toggleGridIsDrawn();
			break;
		case CAMERA_PROFILE:
			//TODO pending
			//nextCameraProfile();
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
		// --
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
				pupVec = viewWindow().unprojectedCoordinatesOf(new DLVector((float)cursorX, (float)cursorY, 0.5f));
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
			trans = new DLVector(-10.0f * pinhole().flySpeed(), 0.0f, 0.0f);
			if(this.is3D())
				trans.div(camera().frame().magnitude());
			pinhole().frame().translate(pinhole().frame().inverseTransformOf(trans));			
			break;
		case MOVE_CAMERA_RIGHT:
			trans = new DLVector(10.0f * pinhole().flySpeed(), 0.0f, 0.0f);
			if(this.is3D())
				trans.div(camera().frame().magnitude());
			pinhole().frame().translate(pinhole().frame().inverseTransformOf(trans));			
			break;
		case MOVE_CAMERA_UP:
			trans = pinhole().frame().inverseTransformOf(new DLVector(0.0f, isRightHanded() ? 10.0f : -10.0f * pinhole().flySpeed(), 0.0f));
			if(this.is3D())
				trans.div(camera().frame().magnitude());
			pinhole().frame().translate(trans);					  
			break;
		case MOVE_CAMERA_DOWN:
			trans = pinhole().frame().inverseTransformOf(new DLVector(0.0f, isRightHanded() ? -10.0f : 10.0f * pinhole().flySpeed(), 0.0f));
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
		// ---
		case ZOOM_ON_PIXEL:
			if (this.is2D()) {
				viewWindow().interpolateToZoomOnPixel(new Point(cursorX, cursorY));
				pupVec = viewWindow().unprojectedCoordinatesOf(new DLVector((float)cursorX, (float)cursorY, 0.5f));
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
			pinhole().setArcballReferencePoint(new DLVector(0, 0, 0));
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
		case ALIGN_FRAME:
			if (interactiveFrame() != null)
				interactiveFrame().alignWithFrame(pinhole().frame());
			break;
		case ALIGN_CAMERA:
			pinhole().frame().alignWithFrame(null, true);
			break;		
		default: 
			System.out.println("Action cannot be handled here!");
    break;
		}
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
		//TODO pending
		/**
		if (onConsole)
			System.out.println(currentCameraProfileHelp());
		else
			AbstractScene.showMissingImplementationWarning("displayCurrentCameraProfileHelp");
		*/
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
	/**
	//TODO pend: implement me when you are done with the keyboard profile
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
	*/
	
	/**
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
	*/
	
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
	
  //Should be called after updating cursor position
	/**
	protected void updateGrabber() {
		updateCursor();
		setDeviceGrabber(null);
		if( isTrackingDevice() )
			for (Grabbable mg : deviceGrabberPool()) {
				mg.checkIfGrabsInput();
				if (mg.grabsInput())
					setDeviceGrabber(mg);
			}
	}
	*/
	
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
		// 5. Grid and axis drawing
		if (gridIsDrawn()) {
			if(gridIsDotted())
				drawDottedGrid(pinhole().sceneRadius());
			else
				drawGrid(pinhole().sceneRadius());
		}
		if (axisIsDrawn())
			drawAxis(pinhole().sceneRadius());		
		
		// 6. Display visual hints
		displayVisualHints(); // abstract
		
		//updateFrameRate();
		
		// -1 ?
		updateCursor();
		
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
		
		//4 INTERACTIVITY
	  // 4a. HIDevices
		//updateGrabber();
		
		// 4b. Devices (external stuff -> Feedable)
		for (AbstractDevice device : devices.values())
			device.handle(device.feed());
		
		//TODO implement me
			
		// 4c. Events
		//TODO testing: are there more than one event per frame?
		//System.out.println("total number of events: " + eventQueue.size());
		
		GenericEvent<?> event;	
    while( !eventQueue.isEmpty() ) {
    	event = eventQueue.remove();
    	
    	// replaces updateGrabber() !!!
    	if( event.getAction() == null ) {
    		setDeviceGrabber(null);
    		if( isTrackingDevice() )
    			for (Grabbable mg : deviceGrabberPool()) {
    				mg.checkIfGrabsInput(event);
    				if (mg.grabsInput())
    					setDeviceGrabber(mg);
    			}
    		return;
    	}
    	else {
    		if( this.lastDeviceGrbbrAction!=null && this.lastDeviceGrbbrAction!=event.getAction() ) {
    			if( this.deviceGrabber() != null ) {
    				this.deviceGrabber().checkIfGrabsInput(event);
    				if (!this.deviceGrabber().grabsInput())
    					setDeviceGrabber(null);
    				else
    					if( event instanceof GenericMotionEvent ) //different motion sequence
    						((GenericMotionEvent<?>)event).setPreviousEvent(null);
    			} 			
    		}
    	  //same motion sequence
    		/**
    		if( this.lastDeviceGrbbrAction!=null && this.lastDeviceGrbbrAction==event.getAction() ) {
    			
    		} */
    		this.lastDeviceGrbbrAction = event.getAction();
    	}
    	
    	if (deviceGrabber() != null )
    		deviceGrabber().performInteraction(event);
    	else
    		if( event instanceof KeyboardEvent || event instanceof ClickEvent )
    			this.handleEvent(event);
    		else
    			if( event instanceof GenericMotionEvent )
    				if( this.aliveInteractiveFrame() != null )
    					aliveInteractiveFrame().performInteraction((GenericMotionEvent<?>)event);
    				else
    					camera().frame().performInteraction((GenericMotionEvent<?>)event);
    	
    	/**
    	if (deviceGrabber() != null && !deviceGrabberIsAnIFrame )
    		deviceGrabber().performInteraction(event);
    	else
    		if( event instanceof KeyboardEvent || event instanceof ClickEvent )
    			this.handleEvent(event);
    		else
    			if( event instanceof GenericMotionEvent )
    				if( this.aliveInteractiveFrame() != null )
    					aliveInteractiveFrame().performInteraction((GenericMotionEvent<?>)event);
    				else
    					camera().frame().performInteraction((GenericMotionEvent<?>)event);
    	*/
    }
	}	
	
	protected abstract void updateCursor();
	
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
	public DLMatrix getModelViewMatrix() {		
		DLMatrix modelview;
  	modelview = getMatrix();
  	modelview.preApply(getViewMatrix());
  	return modelview;
	}
	
	public DLMatrix getViewMatrix() {
		DLMatrix view = pinhole().getViewMatrix();  	
  	return view;
	}
	
	public DLMatrix getModelMatrix() {		
		DLMatrix model;
  	model = getMatrix();  	
  	return model;
	}
	
	public DLMatrix getProjectionMatrix() {		
		DLMatrix projection;  	
  	projection = getProjection();
  	return projection;
	}
	
	/**
	 * This method restores the matrix mode.
	 */
	public DLMatrix getModelViewProjectionMatrix() {		
		DLMatrix PVM;  	
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
	 * Returns a String with the {@link #currentCameraProfile()} keyboard and mouse bindings.
	 * 
	 * @see remixlab.remixcam.deprecatedprofile.DeprecatedCameraProfile#cameraDeviceBindingsDescription()
	 * @see remixlab.remixcam.deprecatedprofile.DeprecatedCameraProfile#frameDeviceBindingsDescription()
	 * @see remixlab.remixcam.deprecatedprofile.DeprecatedCameraProfile#deviceClickBindingsDescription()
	 * @see remixlab.remixcam.deprecatedprofile.DeprecatedCameraProfile#keyboardShortcutsDescription()
	 * @see remixlab.remixcam.deprecatedprofile.DeprecatedCameraProfile#cameraWheelBindingsDescription()
	 * @see remixlab.remixcam.deprecatedprofile.DeprecatedCameraProfile#frameWheelBindingsDescription()
	 */
	/**
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
	*/
	
	/**
	public List<AbstractProfile<?>> getActivatedDevices() {
		//TODO test me
		// see: http://viralpatel.net/blogs/convert-arraylist-to-arrays-in-java/
		List<AbstractProfile<?>> list = Arrays.asList(profiles.values().toArray(new AbstractProfile<?>[0]));
		
		Iterator<AbstractProfile<?>> iterator = list.iterator();
		while (iterator.hasNext()) {
			if(!iterator.next().isActive()) 
				iterator.remove();
		}		
		return list;
	}
	*/
	
	/**
	public void activateAllDevices() {
		Iterator<AbstractProfile<?>> iterator = profiles.values().iterator();
		while (iterator.hasNext()) {
			iterator.next().activate();
		}	
	}
	
	public void deactivateAllDevices() {
		Iterator<AbstractProfile<?>> iterator = profiles.values().iterator();
		while (iterator.hasNext()) {
			iterator.next().deactivate();
		}	
	}
	*/
	
  // Event registration
	
	public boolean isEventRegistered(GenericEvent<?> event) {
		return eventQueue.contains(event);
	}
	
	/**
	 * Adds an HIDevice to the scene.
	 * 
	 * @see #unregisterProfile(AbstractHIDevice)
	 * @see #removeAllDevices()
	 */
	public void enqueueEvent(GenericEvent<?> event) {
		if(!isEventRegistered(event))
			if( !event.isNull() )
				eventQueue.add(event);
	}
	
	/**
	 * Removes the device from the scene.
	 * 
	 * @see #registerProfile(AbstractHIDevice)
	 * @see #removeAllDevices()
	 */
	public void removeEvent(GenericEvent<?> event) {
		eventQueue.remove(event);
	}
	
	/**
	 * Removes all registered devices from the scene.
	 * 
	 * @see #registerProfile(AbstractHIDevice)
	 * @see #unregisterProfile(AbstractHIDevice)
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
  
  public void applyMatrix(DLMatrix source) {
  	if( renderer instanceof StackRenderer )
  		renderer.applyMatrix(source);
  	else
  		AbstractScene.showMissingImplementationWarning("applyMatrix");
  }
  
  public void applyProjection(DLMatrix source) {
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

  public DLMatrix getMatrix() {
  	if( renderer instanceof StackRenderer )
  		return renderer.getMatrix();
  	else {
  		AbstractScene.showMissingImplementationWarning("getMatrix");
  		return null;
  	}
  }
  
  public DLMatrix getProjection() {
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
  public DLMatrix getMatrix(DLMatrix target) {
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
  public DLMatrix getProjection(DLMatrix target) {
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
  public void setMatrix(DLMatrix source) {
  	renderer.setMatrix(source);
  }
  
  /**
   * Set the current projection matrix to the contents of another.
   */
  public void setProjection(DLMatrix source) {
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
  
  public void drawFilledCircle(int subdivisions, DLVector center, float radius) {
  	renderer.drawFilledCircle(subdivisions, center, radius);
  }
  
  public void drawFilledSquare(DLVector center, float edge) {
  	renderer.drawFilledSquare(center, edge);
  }
  
  public void drawShooterTarget(DLVector center, float length) {
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
	 * {@link remixlab.remixcam.core.Grabbable#grabsInput()} using
	 * {@link remixlab.remixcam.core.Grabbable#checkIfGrabsDevice(int, int, Camera)}.
	 * <p>
	 * You should not have to directly use this list. Use
	 * {@link #removeFromDeviceGrabberPool(Grabbable)} and
	 * {@link #addInDeviceGrabberPool(Grabbable)} to modify this list.
	 */
	public List<Grabbable> deviceGrabberPool() {
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
				deviceGrabber().setGrabsInput(false);
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
	 * When {@link remixlab.remixcam.core.Grabbable#grabsInput()}, the different
	 * mouse events are sent to it instead of their usual targets (
	 * {@link #pinhole()} or {@link #interactiveFrame()}).
	 */
	public Grabbable deviceGrabber() {
		return deviceGrbbr;
	}
	
	/**
	 * Directly defines the {@link #deviceGrabber()}.
	 * <p>
	 * You should not call this method directly as it bypasses the
	 * {@link remixlab.remixcam.core.Grabbable#checkIfGrabsDevice(int, int, Camera)}
	 * test performed by parsing the mouse moved event.
	 */
	public void setDeviceGrabber(Grabbable deviceGrabber) {
		deviceGrbbr = deviceGrabber;

		deviceGrabberIsAnIFrame = deviceGrabber instanceof InteractiveFrame;
	}
	
	// 3. Device grabber handling
	
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
	public void addInDeviceGrabberPool(Grabbable deviceGrabber) {
		if (!isInDeviceGrabberPool(deviceGrabber))
			deviceGrabberPool().add(deviceGrabber);
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
	 * @see #setCenter(DLVector) {@link #radius()}
	 */
	public DLVector center() {
		return pinhole().sceneCenter();
	}

	/**
	 * Returns the arcball reference point.
	 * <p>
	 * Convenience wrapper function that simply returns {@code
	 * camera().arcballReferencePoint()}
	 * 
	 * @see #setCenter(DLVector) {@link #radius()}
	 */
	public DLVector arcballReferencePoint() {
		return pinhole().arcballReferencePoint();
	}

	/**
	 * Sets the {@link #radius()} of the Scene.
	 * <p>
	 * Convenience wrapper function that simply returns {@code
	 * camera().setSceneRadius(radius)}
	 * 
	 * @see #setCenter(DLVector)
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
	public void setCenter(DLVector center) {
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
	 * @see #setCenter(DLVector)
	 */
	public void setBoundingBox(DLVector min, DLVector max) {
		if( this.is2D() )
			System.out.println("setBoundingBox is available only in 3D. Use setBoundingRect instead");
		else
			((Camera) pinhole()).setSceneBoundingBox(min, max);
	}
	
	public void setBoundingRect(DLVector min, DLVector max) {
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
	 * Use {@link #drawArrow(DLVector, DLVector, float)} to place the arrow
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
	public void drawArrow(DLVector from, DLVector to,	float radius) {
		pushMatrix();
		translate(from.x(), from.y(), from.z());
		applyMatrix(new Quaternion(new DLVector(0, 0, 1), DLVector.sub(to,	from)).matrix());
		drawArrow(DLVector.sub(to, from).mag(), radius);
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
	 * @see #drawFilledCircle(int, DLVector, float)
	 */
	public void drawFilledCircle(DLVector center, float radius) {
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
