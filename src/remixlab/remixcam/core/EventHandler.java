package remixlab.remixcam.core;

import remixlab.remixcam.core.AbstractScene.*;
import remixlab.remixcam.event.DLEvent;
import remixlab.remixcam.event.DLKeyEvent;
import remixlab.remixcam.event.DLMouseEvent;
import remixlab.remixcam.event.HIDeviceEvent;
import remixlab.remixcam.geom.Point;

/**
 * This class provides low level java.awt.* based input event handling.
 * <p>
 * In order to handle input events generated by Processing with proscene, this object needs
 * to be registered  at the PApplet (which is done through
 * {@link remixlab.proscene.Scene#enableKeyboardHandling()} and
 * {@link remixlab.proscene.Scene#enableMouseHandling()}). Input events (keyboard and mouse)
 * generated via Processing will then be directed to {@link #DLDLKeyEvent(DLDLKeyEvent)} and to
 * {@link #handleMouseEvent(DLMouseEvent)}.  
 */
public class EventHandler implements Constants {
	protected AbstractScene scene;
	public DeviceAction camMouseAction;
	protected boolean keyHandled;
  //Z O O M _ O N _ R E G I O N
	public Point fCorner;// also used for SCREEN_ROTATE
	public Point lCorner;
	
	public EventHandler(AbstractScene s) {
		scene = s;
		camMouseAction = DeviceAction.NO_DEVICE_ACTION;
		keyHandled = false;
		fCorner = new Point();
		lCorner = new Point();
	}
	
	//TODO generalize me
	public void handle(DLEvent e) {		
		if( e instanceof DLKeyEvent ) {
			handleKeyEvent((DLKeyEvent) e);
		}
		if( e instanceof HIDeviceEvent ) {
			handleMouseEvent((DLMouseEvent) e); 
		}
	}
	
	// 1. KeyEvents
	
	/**
	 * Keyboard event handler.
	 * 
	 * @see remixlab.proscene.Scene#keyboardIsHandled()
	 * @see remixlab.proscene.Scene#enableKeyboardHandling(boolean)
	 */
	public void handleKeyEvent(DLKeyEvent e) {
		keyHandled = false;
		switch (e.getAction() ) {				
		case DLKeyEvent.PRESS:
			break;
		case DLKeyEvent.TYPE:
			keyTyped(e);
			break;
		case DLKeyEvent.RELEASE:
			keyReleased(e);
			break;			
		}
	}
	
	/**
	 * Implementation of the key typed event used to handle character shortcuts.
	 * <p>
	 * The handler queries the {@link remixlab.proscene.Scene#currentCameraProfile()}
	 * to see if there's a binding there first. If nothing is found,
	 * the handler look for it in the Scene then.
	 * 
	 * @see #keyTypedCameraKeyboardAction(DLKeyEvent)
	 * @see #keyTypedKeyboardAction(DLKeyEvent)
	 */
	protected void keyTyped(DLKeyEvent e) {
		boolean handled = false;		
		if (scene.currentCameraProfile() != null)
			handled = keyTypedCameraKeyboardAction(e);
		if (!handled)
			handled = keyTypedKeyboardAction(e);
		keyHandled = handled;
	}
	
	/**
	 * Implementation of the key released event used to handle complex shortcuts, i.e.,
	 * shortcuts involving a keycode plus a modifier mask.
	 * <p>
	 * The handler looks for a possible binding in the
	 * {@link remixlab.proscene.Scene#currentCameraProfile()} first.
	 * If the {@link remixlab.proscene.Scene#currentCameraProfile()} doesn't bind an action,
	 * the handler searches for it in the Scene.
	 * 
	 * @see #keyReleasedCameraKeyboardAction(DLKeyEvent)
	 * @see #keyReleasedKeyboardAction(DLKeyEvent)
	 */
	protected void keyReleased(DLKeyEvent e) {
		if(keyHandled)
			return;
		boolean handled = false;
		if (scene.currentCameraProfile() != null)
			handled = keyReleasedCameraKeyboardAction(e);
		if (!handled)
			keyReleasedKeyboardAction(e);
	}
	
	/**
	 * Internal use.
	 * <p>
	 * This method extracts the character associated with the key from the DLKeyEvent
	 * and then queries the {@link remixlab.proscene.Scene#currentCameraProfile()}
	 * to see if there's a binding for it.
	 * 
	 * @return true if a binding was found 
	 */
	protected boolean keyTypedCameraKeyboardAction(DLKeyEvent e) {
		CameraKeyboardAction kba = null;
		kba = scene.currentCameraProfile().shortcut( e.getKey() );
		if (kba == null)
			return false;
		else {
			scene.handleCameraKeyboardAction(kba);
			return true;
		}
	}
	
	/**
	 * Internal use.
	 * <p>
	 * This method extracts the character associated with the key from the DLKeyEvent
	 * and then queries the Scene to see if there's a binding for it.
	 * 
	 * @return true if a binding was found 
	 */
	protected boolean keyTypedKeyboardAction(DLKeyEvent e) {
		if (!e.isAltDown() /**&& !e.isAltGraphDown()*/ && !e.isControlDown()	&& !e.isShiftDown()) {
			Integer path = scene.path(e.getKey());
			if (path != null) {
				scene.pinhole().playPath(path);
				return true;
			}
		}
		
		KeyboardAction kba = null;
		kba = scene.shortcut(e.getKey());
		if (kba == null)
			return false;
		else {
			scene.handleKeyboardAction(kba);
			return true;
		}
	}
	
	/**
	 * Internal use.
	 * <p>
	 * This method extracts the key combination (keycode +  modifier mask) associated with
	 * the DLKeyEvent and then queries the {@link remixlab.proscene.Scene#currentCameraProfile()}
	 * to see if there's a binding for it.
	 * 
	 * @return true if a binding was found 
	 */
	protected boolean keyReleasedCameraKeyboardAction(DLKeyEvent e) {
		CameraKeyboardAction kba = null;
		kba = scene.currentCameraProfile().shortcut( e.getModifiers(), e.getKeyCode() );
		if (kba == null)
			return false;
		else {
			scene.handleCameraKeyboardAction(kba);
			return true;
		}
	}
	
	/**
	 * Internal use.
	 * <p>
	 * This method extracts the key combination (keycode +  modifier mask) associated with
	 * the DLKeyEvent and then queries the Scene to see if there's a binding for it.
	 * 
	 * @return true if a binding was found 
	 */
	protected boolean keyReleasedKeyboardAction(DLKeyEvent e) {
		// 1. Key-frames
		// 1.1. Need to add a key-frame?
		if (((scene.addKeyFrameKeyboardModifier == ALT) && (e.isAltDown()))
	   /**|| ((scene.addKeyFrameKeyboardModifier == ALT_GRAPH) && (e.isAltGraphDown()))*/
		 || ((scene.addKeyFrameKeyboardModifier == META) && (e.isMetaDown()))
		 || ((scene.addKeyFrameKeyboardModifier == CTRL) && (e.isControlDown()))
		 || ((scene.addKeyFrameKeyboardModifier == SHIFT) && (e.isShiftDown()))) {
			Integer path = scene.path(e.getKeyCode());
			if (path != null) {
				scene.pinhole().addKeyFrameToPath(path);
				return true;
			}
		}
  	// 1.2. Need to delete a key-frame?
		if (((scene.deleteKeyFrameKeyboardModifier == ALT) && (e.isAltDown()))
		 /**|| ((scene.deleteKeyFrameKeyboardModifier == ALT_GRAPH) && (e.isAltGraphDown()))*/
		 || ((scene.deleteKeyFrameKeyboardModifier == META) && (e.isMetaDown()))
		 || ((scene.deleteKeyFrameKeyboardModifier == CTRL) && (e.isControlDown()))
		 || ((scene.deleteKeyFrameKeyboardModifier == SHIFT) && (e.isShiftDown()))) {
			Integer path = scene.path(e.getKeyCode());
			if (path != null) {
				scene.pinhole().deletePath(path);
				return true;
			}
		}		
		// 2. General actions
		KeyboardAction kba = null;
		kba = scene.shortcut( e.getModifiers(), e.getKeyCode() );
		if (kba == null)
			return false;
		else {
			scene.handleKeyboardAction(kba);
			return true;
		}
	}
	
	// 2. Mouse Events
	
	/**
	 * Mouse event handler.
	 * 
	 * @see remixlab.proscene.Scene#mouseIsHandled()
	 * @see remixlab.proscene.Scene#enableMouseHandling(boolean)
	 */
	public void handleMouseEvent(DLMouseEvent e) {
		scene.mouseX = e.getX();
		scene.mouseY = e.getY();
		switch (e.getAction() ) {
		case DLMouseEvent.CLICK:
			mouseClicked(e);
			break;
		case DLMouseEvent.DRAG:
			mouseDragged(e);
			break;
		case DLMouseEvent.MOVE:
			mouseMoved(e);
			break;
		case DLMouseEvent.PRESS:
			mousePressed(e);
			break;
		case DLMouseEvent.RELEASE:
			mouseReleased(e);
			break;
		case DLMouseEvent.WHEEL:
			mouseWheelMoved(e);
		}		
	}
	
  /**
   * The action generated when the user clicks the mouse is handled by the
   * {@link remixlab.proscene.Scene#deviceGrabber()} (if any). Otherwise
   * looks in the {@link remixlab.proscene.Scene#currentCameraProfile()} to see if there's
   * a binding for this click event, taking into account the button, the modifier mask, and
   * the number of clicks.
   */
	protected void mouseClicked(DLMouseEvent event) {		
		if (scene.deviceGrabber() != null)
			scene.deviceGrabber().buttonClicked(/**event.getPoint(),*/ event.getButton(), event.getClickCount(), scene.pinhole());
		else {
			ClickAction ca = scene.currentCameraProfile().clickBinding(event.getModifiers(), event.getButton(), event.getClickCount());
			if (ca != null)
				scene.handleClickAction(ca);
		}
	}
	
	/**
	 * {@link remixlab.proscene.Scene#setDeviceGrabber(MouseGrabbable)} to the MouseGrabber that grabs the
	 * mouse (or to {@code null} if none of them grab it).
	 */
	public void mouseMoved(DLMouseEvent e) {
		Point event = new Point((e.getX() - scene.upperLeftCorner.getX()), (e.getY() - scene.upperLeftCorner.getY()));
		scene.setDeviceGrabber(null);
		if( scene.isTrackingDevice() )
			for (DeviceGrabbable mg : scene.deviceGrabberPool()) {
				mg.checkIfGrabsDevice(event.getX(), event.getY(), scene.pinhole());
				if (mg.grabsDevice())
					scene.setDeviceGrabber(mg);
			}
	}
	
	/**
	 * The action generated when the user clicks and drags the mouse is handled by the
	 * {@link remixlab.proscene.Scene#deviceGrabber()} (if any), or the
	 * {@link remixlab.proscene.Scene#interactiveFrame()}
	 * (if @link remixlab.proscene.Scene#interactiveFrameIsDrawn()), or the
	 * {@link remixlab.proscene.Scene#pinhole()} (checks are performed in that order).
	 * <p>
	 * Mouse displacements are interpreted according to the
	 * {@link remixlab.proscene.Scene#currentCameraProfile()} mouse bindings.
	 * 
	 * @see #awtMouseDragged(DLMouseEvent)
	 * @see #awtMouseReleased(DLMouseEvent)
	 * @see #mouseWheelMoved(MouseWheelEvent)
	 */
	public void mousePressed(DLMouseEvent e) {
		Point event = new Point((e.getX() - scene.upperLeftCorner.getX()), (e.getY() - scene.upperLeftCorner.getY()));
		if (scene.deviceGrabber() != null) {
			if (scene.deviceGrabberIsAnIFrame) { //covers also the case when mouseGrabberIsADrivableFrame
				InteractiveFrame iFrame = (InteractiveFrame) scene.deviceGrabber();
				iFrame.startAction(scene.currentCameraProfile().frameMouseAction(e), scene.drawIsConstrained());
				iFrame.initAction(new Point(event.getX(), event.getY()), scene.pinhole());
			} else
				scene.deviceGrabber().initAction(new Point(event.getX(), event.getY()), scene.pinhole());
			return;
		}
		if (scene.interactiveFrameIsDrawn()) {
			//scene.interactiveFrame().startAction(scene.currentCameraProfile().frameMouseAction(e), scene.drawIsConstrained());
			scene.interactiveFrame().startAction(scene.currentCameraProfile().frameMouseAction(e.getModifiers(), e.getButton()), scene.drawIsConstrained());
			scene.interactiveFrame().initAction(new Point(event.getX(), event.getY()), scene.pinhole());
			return;
		}
		//camMouseAction = scene.currentCameraProfile().cameraMouseAction(e);
		camMouseAction = scene.currentCameraProfile().cameraMouseAction(e.getModifiers(), e.getButton());
		if (camMouseAction == DeviceAction.ZOOM_ON_REGION) {
			fCorner.set(event.getX(), event.getY());
			lCorner.set(event.getX(), event.getY());
		}
		if (camMouseAction == DeviceAction.SCREEN_ROTATE)
			fCorner.set(event.getX(), event.getY());
		scene.pinhole().frame().startAction(camMouseAction, scene.drawIsConstrained());
		scene.pinhole().frame().initAction(new Point(event.getX(), event.getY()), scene.pinhole());
	}

	/**
	 * The mouse dragged event is sent to the {@link remixlab.proscene.Scene#deviceGrabber()}
	 * or the {@link remixlab.proscene.Scene#interactiveFrame()}, or to the
	 * {@link remixlab.proscene.Scene#pinhole()}, according to the action started at
	 * {@link #awtMousePressed(DLMouseEvent)}.
	 * <p>
	 * Mouse displacements are interpreted according to the
	 * {@link remixlab.proscene.Scene#currentCameraProfile()} mouse bindings.
	 * 
	 * @see #awtMousePressed(DLMouseEvent)
	 * @see #awtMouseReleased(DLMouseEvent)
	 */
	public void mouseDragged(DLMouseEvent e) {
		Point event = new Point((e.getX() - scene.upperLeftCorner.getX()), (e.getY() - scene.upperLeftCorner.getY()));
		if (scene.deviceGrabber() != null) {
			scene.deviceGrabber().checkIfGrabsDevice(event.getX(), event.getY(), scene.pinhole());
			if (scene.deviceGrabber().grabsDevice())
				if (scene.deviceGrabberIsAnIFrame) //covers also the case when mouseGrabberIsADrivableFrame
					((InteractiveFrame) scene.deviceGrabber()).execAction(new Point(event.getX(), event.getY()), scene.pinhole());	
				else
					scene.deviceGrabber().execAction(new Point(event.getX(), event.getY()), scene.pinhole());
			else
				scene.setDeviceGrabber(null);
			return;
		}
		if (scene.interactiveFrameIsDrawn()) {
		  scene.interactiveFrame().execAction(new Point(event.getX(), event.getY()), scene.pinhole());
			return;
		}
		if (camMouseAction == DeviceAction.ZOOM_ON_REGION)
			lCorner.set(event.getX(), event.getY());
		else {
			if (camMouseAction == DeviceAction.SCREEN_ROTATE)
				fCorner.set(event.getX(), event.getY());
			scene.pinhole().frame().execAction(new Point(event.getX(), event.getY()), scene.pinhole());
		}
	}
	
	/**
	 * The mouse released event (which ends a mouse action) is sent to the
	 * {@link remixlab.proscene.Scene#deviceGrabber()} or the
	 * {@link remixlab.proscene.Scene#interactiveFrame()}, or to the
	 * {@link remixlab.proscene.Scene#pinhole()}, according to the action started at
	 * {@link #mousePressed(DLMouseEvent)}.
	 * <p>
	 * Mouse displacements are interpreted according to the
	 * {@link remixlab.proscene.Scene#currentCameraProfile()} mouse bindings.
	 * 
	 * @see #mousePressed(DLMouseEvent)
	 * @see #mouseDragged(DLMouseEvent)
	 */
	public void mouseReleased(DLMouseEvent e) {
		Point event = new Point((e.getX() - scene.upperLeftCorner.getX()), (e.getY() - scene.upperLeftCorner.getY()));
		if (scene.deviceGrabber() != null) {
			if (scene.deviceGrabberIsAnIFrame) //covers also the case when mouseGrabberIsADrivableFrame
				((InteractiveFrame) scene.deviceGrabber()).endAction(new Point(event.getX(), event.getY()), scene.pinhole());
			else
				scene.deviceGrabber().endAction(new Point(event.getX(), event.getY()), scene.pinhole());
			scene.deviceGrabber().checkIfGrabsDevice(event.getX(), event.getY(), scene.pinhole());
			if (!(scene.deviceGrabber().grabsDevice()))
				scene.setDeviceGrabber(null);
			// iFrameMouseAction = MouseAction.NO_MOUSE_ACTION;
			return;
		}
		if (scene.interactiveFrameIsDrawn()) {
			scene.interactiveFrame().endAction(new Point(event.getX(), event.getY()), scene.pinhole());
			// iFrameMouseAction = MouseAction.NO_MOUSE_ACTION;
			return;
		}

		if ((camMouseAction == DeviceAction.ZOOM_ON_REGION)
				|| (camMouseAction == DeviceAction.SCREEN_ROTATE)
				|| (camMouseAction == DeviceAction.SCREEN_TRANSLATE))
			lCorner.set(event.getX(), event.getY());
		scene.pinhole().frame().endAction(new Point(event.getX(), event.getY()), scene.pinhole());
		camMouseAction = DeviceAction.NO_DEVICE_ACTION;
		// iFrameMouseAction = MouseAction.NO_MOUSE_ACTION;
	}
	
	/**
	 * The action generated when the user start rotating the mouse wheel is handled by the
	 * {@link remixlab.proscene.Scene#deviceGrabber()} (if any), or the
	 * {@link remixlab.proscene.Scene#interactiveFrame()}
	 * (if @link remixlab.proscene.Scene#interactiveFrameIsDrawn()), or the
	 * {@link remixlab.proscene.Scene#pinhole()} (checks are performed in that order).
	 * <p>
	 * Mouse wheel rotation is interpreted according to the
	 * {@link remixlab.proscene.Scene#currentCameraProfile()} mouse wheel bindings.
	 * 
	 * @see #mousePressed(DLMouseEvent)
	 */
	public void mouseWheelMoved(DLMouseEvent event) {
		//if(!scene.mouseIsHandled())
			//return;
		if (scene.deviceGrabber() != null) {
			if (scene.deviceGrabberIsAnIFrame) { //covers also the case when mouseGrabberIsADrivableFrame
				InteractiveFrame iFrame = (InteractiveFrame) scene.deviceGrabber();
				iFrame.startAction(scene.currentCameraProfile().frameWheelMouseAction(event), scene.drawIsConstrained());
				iFrame.wheelMoved(event.getAmount(), scene.pinhole());				
			} else
				scene.deviceGrabber().wheelMoved(event.getAmount(), scene.pinhole());
			return;
		}
		if (scene.interactiveFrameIsDrawn()) {
			scene.interactiveFrame().startAction(scene.currentCameraProfile().frameWheelMouseAction(event.getModifiers()), scene.drawIsConstrained());
			scene.interactiveFrame().wheelMoved(event.getAmount(), scene.pinhole());
			return;
		}
		//scene.pinhole().frame().startAction(scene.currentCameraProfile().cameraWheelMouseAction(event), scene.drawIsConstrained());
		scene.pinhole().frame().startAction(scene.currentCameraProfile().cameraWheelMouseAction(event.getModifiers()), scene.drawIsConstrained());
		scene.pinhole().frame().wheelMoved(event.getAmount(), scene.pinhole());	  
	}
}

