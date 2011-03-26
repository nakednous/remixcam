/**
 *                     ProScene (version 1.0.1)      
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

package remixlab.remixcam.core;

import java.util.ArrayList;
import java.util.Iterator;

import remixlab.remixcam.devices.Actions.MouseAction;
import remixlab.remixcam.devices.Mouse.Button;
import remixlab.remixcam.constraint.*;
import remixlab.remixcam.geom.*;
import remixlab.remixcam.util.TimerJob;

/**
 * A InteractiveFrame is a Frame that can be rotated and translated using the
 * mouse.
 * <p>
 * It converts the mouse motion into a translation and an orientation updates. A
 * InteractiveFrame is used to move an object in the scene. Combined with object
 * selection, its MouseGrabber properties and a dynamic update of the scene, the
 * InteractiveFrame introduces a great reactivity in your processing
 * applications.
 * <p>
 * <b>Note:</b> Once created, the InteractiveFrame is automatically added to
 * the {@link remixlab.proscene.Scene#mouseGrabberPool()}.
 */

public class InteractiveFrame extends GLFrame implements MouseGrabbable, Cloneable {
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result + delay;
		result = prime * result + (dirIsFixed ? 1231 : 1237);
		result = prime * result + grabsMouseThreshold;
		result = prime * result + (grbsMouse ? 1231 : 1237);
		result = prime * result + (horiz ? 1231 : 1237);
		result = prime * result + (isInCamPath ? 1231 : 1237);
		result = prime * result + (isSpng ? 1231 : 1237);
		result = prime * result + (keepsGrabbingMouse ? 1231 : 1237);
		result = prime * result + Float.floatToIntBits(mouseSpeed);
		result = prime * result + ((pressPos == null) ? 0 : pressPos.hashCode());
		result = prime * result + ((prevPos == null) ? 0 : prevPos.hashCode());
		result = prime * result + Float.floatToIntBits(rotSensitivity);
		result = prime * result + ((spngQuat == null) ? 0 : spngQuat.hashCode());
		result = prime * result + Float.floatToIntBits(spngSensitivity);
		result = prime * result + startedTime;
		result = prime * result + Float.floatToIntBits(transSensitivity);
		result = prime * result + Float.floatToIntBits(wheelSensitivity);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		InteractiveFrame other = (InteractiveFrame) obj;
		if (action == null) {
			if (other.action != null)
				return false;
		} else if (!action.equals(other.action))
			return false;
		if (delay != other.delay)
			return false;
		if (dirIsFixed != other.dirIsFixed)
			return false;
		if (grabsMouseThreshold != other.grabsMouseThreshold)
			return false;
		if (grbsMouse != other.grbsMouse)
			return false;
		if (horiz != other.horiz)
			return false;
		if (isInCamPath != other.isInCamPath)
			return false;
		if (isSpng != other.isSpng)
			return false;
		if (keepsGrabbingMouse != other.keepsGrabbingMouse)
			return false;
		if (Float.floatToIntBits(mouseSpeed) != Float
				.floatToIntBits(other.mouseSpeed))
			return false;
		if (pressPos == null) {
			if (other.pressPos != null)
				return false;
		} else if (!pressPos.equals(other.pressPos))
			return false;
		if (prevPos == null) {
			if (other.prevPos != null)
				return false;
		} else if (!prevPos.equals(other.prevPos))
			return false;
		if (Float.floatToIntBits(rotSensitivity) != Float
				.floatToIntBits(other.rotSensitivity))
			return false;
		if (spngQuat == null) {
			if (other.spngQuat != null)
				return false;
		} else if (!spngQuat.equals(other.spngQuat))
			return false;
		if (Float.floatToIntBits(spngSensitivity) != Float
				.floatToIntBits(other.spngSensitivity))
			return false;
		if (startedTime != other.startedTime)
			return false;
		if (Float.floatToIntBits(transSensitivity) != Float
				.floatToIntBits(other.transSensitivity))
			return false;
		if (Float.floatToIntBits(wheelSensitivity) != Float
				.floatToIntBits(other.wheelSensitivity))
			return false;
		return true;
	}

	private boolean horiz;// Two simultaneous InteractiveFrame require two mice!
	private int grabsMouseThreshold;
	private float rotSensitivity;
	private float transSensitivity;
	private float spngSensitivity;
	private float wheelSensitivity;

	// Mouse speed:
	private float mouseSpeed;
	// spinning stuff:
	private boolean isSpng;
	private TimerJob timerFx1;
	private int startedTime;
	private int delay;

	private Quaternion spngQuat;

	// Whether the SCREEN_TRANS direction (horizontal or vertical) is fixed or
	// not.
	private boolean dirIsFixed;

	// MouseGrabber
	protected boolean keepsGrabbingMouse;

	protected MouseAction action;
	protected Constraint prevConstraint; // When manipulation is without
	// Constraint.
	// Previous mouse position (used for incremental updates) and mouse press
	// position.
	protected Point prevPos, pressPos;

	protected boolean grbsMouse;

	protected boolean isInCamPath;

	// P R O S C E N E A N D P R O C E S S I N G A P P L E T A N D O B J E C T S
	public RCScene scene;
	protected MouseGrabberPool mouseGrabberPool;

	/**
	 * Default constructor.
	 * <p>
	 * The {@link #translation()} is set to (0,0,0), with an identity
	 * {@link #rotation()} (0,0,0,1) (see Frame constructor for details). The
	 * different sensitivities are set to their default values (see
	 * {@link #rotationSensitivity()} , {@link #translationSensitivity()},
	 * {@link #spinningSensitivity()} and {@link #wheelSensitivity()}).
	 * <p>
	 * <b>Note:</b> the InteractiveFrame is automatically added to
	 * the {@link remixlab.proscene.Scene#mouseGrabberPool()}.
	 */
	public InteractiveFrame(RCScene scn) {
		scene = scn;
		mouseGrabberPool = scene.mouseGrabberPool;
		
		action = MouseAction.NO_MOUSE_ACTION;
		horiz = true;

		addInMouseGrabberPool();
		isInCamPath = false;
		grbsMouse = false;

		setGrabsMouseThreshold(10);
		setRotationSensitivity(1.0f);
		setTranslationSensitivity(1.0f);
		setSpinningSensitivity(0.3f);
		setWheelSensitivity(20.0f);

		keepsGrabbingMouse = false;
		isSpng = false;
		prevConstraint = null;
		startedTime = 0;
		
		timerFx1 = new TimerJob() {
			public void execute() {
				spin();
			}
		};		
		scene.timerPool.registerInTimerPool(this, timerFx1);
		// delay = 10;
	}

	/**
	 * Ad-hoc constructor needed to make editable a Camera path defined by
	 * KeyFrameInterpolator.
	 * <p>
	 * Constructs a Frame from the the {@code iFrame} {@link #translation()} and
	 * {@link #orientation()} and immediately adds it to the
	 * {@link remixlab.proscene.Scene#mouseGrabberPool()}.
	 * <p>
	 * A call on {@link #isInCameraPath()} on this Frame will return {@code true}.
	 * 
	 * <b>Attention:</b> Internal use. You should not call this constructor in your
	 * own applications.
	 * 
	 * @see remixlab.remixcam.core.Camera#addKeyFrameToPath(int)
	 */
	public InteractiveFrame(RCScene scn, InteractiveCameraFrame iFrame) {
		super(iFrame.translation(), iFrame.rotation());
		scene = scn;
		mouseGrabberPool = scene.mouseGrabberPool;
		action = MouseAction.NO_MOUSE_ACTION;
		horiz = true;

		addInMouseGrabberPool();
		isInCamPath = true;
		grbsMouse = false;

		setGrabsMouseThreshold(10);
		setRotationSensitivity(1.0f);
		setTranslationSensitivity(1.0f);
		setSpinningSensitivity(0.3f);
		setWheelSensitivity(20.0f);

		keepsGrabbingMouse = false;
		isSpng = false;
		prevConstraint = null;
		startedTime = 0;

		list = new ArrayList<KeyFrameInterpolator>();
		Iterator<KeyFrameInterpolator> it = iFrame.listeners().iterator();
		while (it.hasNext())
			list.add(it.next());
		
		timerFx1 = new TimerJob() {
			public void execute() {
				spin();
			}
		};		
		scene.timerPool.registerInTimerPool(this, timerFx1);
	}	

	/**
	 * Returns {@code true} if the InteractiveFrame forms part of a Camera path
	 * and {@code false} otherwise.
	 * 
	 */
	public boolean isInCameraPath() {
		return isInCamPath;
	}

	/**
	 * Implementation of the clone method.
	 * <p>
	 * Calls {@link remixlab.remixcam.core.GLFrame#clone()} and makes a deep copy of the
	 * remaining object attributes except for {@link #prevConstraint} (which is
	 * shallow copied).
	 * 
	 * @see remixlab.remixcam.core.GLFrame#clone()
	 */
	public InteractiveFrame clone() {
		InteractiveFrame clonedIFrame = (InteractiveFrame) super.clone();
		//TODO check if timer needs to be clone
		TimerJob clonedTimerFx1 = new TimerJob() {
			public void execute() {
				spin();
			}
		};		
		scene.timerPool.registerInTimerPool(clonedIFrame, clonedTimerFx1);
		return clonedIFrame;
	}

	/**
	 * Returns the grabs mouse threshold which is used by this interactive frame to
	 * {@link #checkIfGrabsMouse(int, int, Camera)}.
	 * 
	 * @see #setGrabsMouseThreshold(int)
	 */
	public int grabsMouseThreshold() {
		return grabsMouseThreshold;
	}
	
	/**
	 * Sets the number of pixels that defined the {@link #checkIfGrabsMouse(int, int, Camera)}
	 * condition.
	 * 
	 * @param threshold number of pixels that defined the {@link #checkIfGrabsMouse(int, int, Camera)}
	 * condition. Default value is 10 pixels (which is set in the constructor). Negative values are
	 * silently ignored.
	 * 
	 * @see #grabsMouseThreshold()
	 * @see #checkIfGrabsMouse(int, int, Camera)
	 */
	public void setGrabsMouseThreshold( int threshold ) {
		if(threshold >= 0)
			grabsMouseThreshold = threshold; 
	}

	/**
	 * Implementation of the MouseGrabber main method.
	 * <p>
	 * The InteractiveFrame {@link #grabsMouse()} when the mouse is within a {@link #grabsMouseThreshold()}
	 * pixels region around its
	 * {@link remixlab.remixcam.core.Camera#projectedCoordinatesOf(Vector3D)}
	 * {@link #position()}.
	 */
	public void checkIfGrabsMouse(int x, int y, Camera camera) {
		Vector3D proj = camera.projectedCoordinatesOf(position());
		setGrabsMouse(keepsGrabbingMouse || ((Math.abs(x - proj.x) < grabsMouseThreshold()) && (Math.abs(y - proj.y) < grabsMouseThreshold())));
	}

	/**
	 * Returns {@code true} when the MouseGrabber grabs the Scene's mouse events.
	 * <p>
	 * This flag is set with {@link #setGrabsMouse(boolean)} by the
	 * {@link #checkIfGrabsMouse(int, int, Camera)} method.
	 */
	public boolean grabsMouse() {
		return grbsMouse;
	}

	/**
	 * Sets the {@link #grabsMouse()} flag. Normally used by
	 * {@link #checkIfGrabsMouse(int, int, Camera)}.
	 */
	public void setGrabsMouse(boolean grabs) {
		grbsMouse = grabs;
	}

	/**
	 * Convenience wrapper function that simply returns {@code scene.isInMouseGrabberPool(this)}.
	 * 
	 * @see remixlab.proscene.Scene#isInMouseGrabberPool(MouseGrabbable)
	 */
	public boolean isInMouseGrabberPool() {
		return mouseGrabberPool.isInMouseGrabberPool(this);
	}	

	/**
	 * Convenience wrapper function that simply calls {@code scene.addInMouseGrabberPool(this)}.
	 * 
	 * @see remixlab.proscene.Scene#addInMouseGrabberPool(MouseGrabbable)
	 */
	public void addInMouseGrabberPool() {
		mouseGrabberPool.addInMouseGrabberPool(this);
	}

	/**
	 * Convenience wrapper function that simply calls {@code scene.removeFromMouseGrabberPool(this)}.
	 * 
	 * @see remixlab.proscene.Scene#removeFromMouseGrabberPool(MouseGrabbable)
	 */
	public void removeFromMouseGrabberPool() {
		mouseGrabberPool.removeFromMouseGrabberPool(this);
	}

	/**
	 * Defines the {@link #rotationSensitivity()}.
	 */
	public final void setRotationSensitivity(float sensitivity) {
		rotSensitivity = sensitivity;
	}

	/**
	 * Defines the {@link #translationSensitivity()}.
	 */
	public final void setTranslationSensitivity(float sensitivity) {
		transSensitivity = sensitivity;
	}

	/**
	 * Defines the {@link #spinningSensitivity()}.
	 */
	public final void setSpinningSensitivity(float sensitivity) {
		spngSensitivity = sensitivity;
	}

	/**
	 * Defines the {@link #wheelSensitivity()}.
	 */
	public final void setWheelSensitivity(float sensitivity) {
		wheelSensitivity = sensitivity;
	}

	/**
	 * Returns the influence of a mouse displacement on the InteractiveFrame
	 * rotation.
	 * <p>
	 * Default value is 1.0. With an identical mouse displacement, a higher value
	 * will generate a larger rotation (and inversely for lower values). A 0.0
	 * value will forbid InteractiveFrame mouse rotation (see also
	 * {@link #constraint()}).
	 * 
	 * @see #setRotationSensitivity(float)
	 * @see #translationSensitivity()
	 * @see #spinningSensitivity()
	 * @see #wheelSensitivity()
	 */
	public final float rotationSensitivity() {
		return rotSensitivity;
	}

	/**
	 * Returns the influence of a mouse displacement on the InteractiveFrame
	 * translation.
	 * <p>
	 * Default value is 1.0. You should not have to modify this value, since with
	 * 1.0 the InteractiveFrame precisely stays under the mouse cursor.
	 * <p>
	 * With an identical mouse displacement, a higher value will generate a larger
	 * translation (and inversely for lower values). A 0.0 value will forbid
	 * InteractiveFrame mouse translation (see also {@link #constraint()}).
	 * <p>
	 * <b>Note:</b> When the InteractiveFrame is used to move a <i>Camera</i> (see
	 * the InteractiveCameraFrame class documentation), after zooming on a small
	 * region of your scene, the camera may translate too fast. For a camera, it
	 * is the Camera.arcballReferencePoint() that exactly matches the mouse
	 * displacement. Hence, instead of changing the
	 * {@link #translationSensitivity()}, solve the problem by (temporarily)
	 * setting the {@link remixlab.remixcam.core.Camera#arcballReferencePoint()} to a
	 * point on the zoomed region).
	 * 
	 * @see #setTranslationSensitivity(float)
	 * @see #rotationSensitivity()
	 * @see #spinningSensitivity()
	 * @see #wheelSensitivity()
	 */
	public final float translationSensitivity() {
		return transSensitivity;
	}

	/**
	 * Returns the minimum mouse speed required (at button release) to make the
	 * InteractiveFrame {@link #spin()}.
	 * <p>
	 * See {@link #spin()}, {@link #spinningQuaternion()} and
	 * {@link #startSpinning(int)} for details.
	 * <p>
	 * Mouse speed is expressed in pixels per milliseconds. Default value is 0.3
	 * (300 pixels per second). Use setSpinningSensitivity() to tune this value. A
	 * higher value will make spinning more difficult (a value of 100.0 forbids
	 * spinning in practice).
	 * 
	 * @see #setSpinningSensitivity(float)
	 * @see #translationSensitivity()
	 * @see #rotationSensitivity()
	 * @see #wheelSensitivity()
	 */
	public final float spinningSensitivity() {
		return spngSensitivity;
	}

	/**
	 * Returns the mouse wheel sensitivity.
	 * <p>
	 * Default value is 20.0. A higher value will make the wheel action more
	 * efficient (usually meaning a faster zoom). Use a negative value to invert
	 * the zoom in and out directions.
	 * 
	 * @see #setWheelSensitivity(float)
	 * @see #translationSensitivity()
	 * @see #rotationSensitivity()
	 * @see #spinningSensitivity()
	 */
	public float wheelSensitivity() {
		return wheelSensitivity;
	}

	/**
	 * Returns {@code true} when the InteractiveFrame is spinning.
	 * <p>
	 * During spinning, {@link #spin()} rotates the InteractiveFrame by its
	 * {@link #spinningQuaternion()} at a frequency defined when the
	 * InteractiveFrame {@link #startSpinning(int)}.
	 * <p>
	 * Use {@link #startSpinning(int)} and {@link #stopSpinning()} to change this
	 * state. Default value is {@code false}.
	 */
	public final boolean isSpinning() {
		return isSpng;
	}

	/**
	 * Returns the incremental rotation that is applied by {@link #spin()} to the
	 * InteractiveFrame orientation when it {@link #isSpinning()}.
	 * <p>
	 * Default value is a {@code null} rotation (identity Quaternion). Use
	 * {@link #setSpinningQuaternion(Quaternion)} to change this value.
	 * <p>
	 * The {@link #spinningQuaternion()} axis is defined in the InteractiveFrame
	 * coordinate system. You can use
	 * {@link remixlab.remixcam.core.GLFrame#transformOfFrom(Vector3D, GLFrame)} to convert
	 * this axis from an other Frame coordinate system.
	 */
	public final Quaternion spinningQuaternion() {
		return spngQuat;
	}

	/**
	 * Defines the {@link #spinningQuaternion()}. Its axis is defined in the
	 * InteractiveFrame coordinate system.
	 */
	public final void setSpinningQuaternion(Quaternion spinningQuaternion) {
		spngQuat = spinningQuaternion;
	}

	/**
	 * Returns {@code true} when the InteractiveFrame is being manipulated with
	 * the mouse. Can be used to change the display of the manipulated object
	 * during manipulation.
	 */
	public boolean isInInteraction() {
		return action != MouseAction.NO_MOUSE_ACTION;
	}

	/**
	 * Stops the spinning motion started using {@link #startSpinning(int)}.
	 * {@link #isSpinning()} will return {@code false} after this call.
	 */
	public final void stopSpinning() {
		if( timerFx1.timer() != null )
			timerFx1.timer().cancelTimer();
		isSpng = false;
	}

	/**
	 * Starts the spinning of the InteractiveFrame.
	 * <p>
	 * This method starts a timer that will call {@link #spin()} every {@code
	 * updateInterval} milliseconds. The InteractiveFrame {@link #isSpinning()}
	 * until you call {@link #stopSpinning()}.
	 */
	public void startSpinning(int updateInterval) {
		isSpng = true;
		if(updateInterval>0) {
			if( timerFx1.timer() != null )
				timerFx1.timer().runTimer(updateInterval);
		}
	}

	/**
	 * Rotates the InteractiveFrame by its {@link #spinningQuaternion()}. Called
	 * by a timer when the InteractiveFrame {@link #isSpinning()}.
	 */
	public void spin() {
		rotate(spinningQuaternion());
	}
	
	/**
	 * Overloading of
	 * {@link remixlab.remixcam.core.MouseGrabbable#mouseClicked(remixlab.proscene.Scene.Button, int, Camera)}.
	 * <p>
	 * Left button double click aligns the InteractiveFrame with the camera axis (see {@link #alignWithFrame(GLFrame)}
	 * and {@link remixlab.proscene.Scene.ClickAction#ALIGN_FRAME}). Right button projects the InteractiveFrame on
	 * the camera view direction.
	 */
	public void mouseClicked(/**Point eventPoint,*/ Button button, int numberOfClicks, Camera camera) {
		if(numberOfClicks != 2)
			return;
		switch (button) {
		case LEFT:  alignWithFrame(camera.frame()); break;
    case RIGHT: projectOnLine(camera.position(), camera.viewDirection()); break;
    default: break;
    }
	}

	/**
	 * Initiates the InteractiveFrame mouse manipulation. Overloading of
	 * {@link remixlab.remixcam.core.MouseGrabbable#mousePressed(Point, Camera)}.
	 * 
	 * The mouse behavior depends on which button is pressed.
	 * 
	 * @see #mouseDragged(Point, Camera)
	 * @see #mouseReleased(Point, Camera)
	 */
	public void mousePressed(Point eventPoint, Camera camera) {
		if (grabsMouse())
			keepsGrabbingMouse = true;

		prevPos = pressPos = eventPoint;
	}	

	/**
	 * Modifies the InteractiveFrame according to the mouse motion.
	 * <p>
	 * Actual behavior depends on mouse bindings. See the Scene documentation for
	 * details.
	 * <p>
	 * The {@code camera} is used to fit the mouse motion with the display
	 * parameters.
	 * 
	 * @see remixlab.remixcam.core.Camera#screenWidth()
	 * @see remixlab.remixcam.core.Camera#screenHeight()
	 * @see remixlab.remixcam.core.Camera#fieldOfView()
	 */
	public void mouseDragged(Point eventPoint, Camera camera) {
		int deltaY = 0;
		if(action != MouseAction.NO_MOUSE_ACTION)
			deltaY = (int) (prevPos.y - eventPoint.y);
	    //right_handed coordinate system should go like this:
		  //deltaY = (int) (eventPoint.y - prevPos.y);

		switch (action) {
		case TRANSLATE: {
			Point delta = new Point((eventPoint.x - prevPos.x), deltaY);
			Vector3D trans = new Vector3D((int) delta.getX(), (int) -delta.getY(),
					0.0f);
			// Scale to fit the screen mouse displacement
			switch (camera.type()) {
			case PERSPECTIVE:
				trans.mult(2.0f * (float) Math.tan(camera.fieldOfView() / 2.0f)
						* Math.abs((camera.frame().coordinatesOf(position())).z)
						/ camera.screenHeight());
				break;
			case ORTHOGRAPHIC: {
				float[] wh = camera.getOrthoWidthHeight();
				trans.x *= 2.0 * wh[0] / camera.screenWidth();
				trans.y *= 2.0 * wh[1] / camera.screenHeight();
				break;
			}
			}
			// Transform to world coordinate system.
			trans = camera.frame().orientation().rotate(
					Vector3D.mult(trans, translationSensitivity()));
			// And then down to frame
			if (referenceFrame() != null)
				trans = referenceFrame().transformOf(trans);
			translate(trans);
			prevPos = eventPoint;
			break;
		}

		case ZOOM: {
			// #CONNECTION# wheelEvent ZOOM case
		  //Warning: same for left and right CoordinateSystemConvention:
			Vector3D trans = new Vector3D(0.0f, 0.0f, (Vector3D.sub(camera.position(), position())).mag() * ((int) (eventPoint.y - prevPos.y)) / camera.screenHeight());
			trans = camera.frame().orientation().rotate(trans);
			if (referenceFrame() != null)
				trans = referenceFrame().transformOf(trans);
			translate(trans);
			prevPos = eventPoint;
			break;
		}

		case SCREEN_ROTATE: {
			// TODO: needs testing to see if it works correctly when left-handed is
			// set
			Vector3D trans = camera.projectedCoordinatesOf(position());
			float prev_angle = (float) Math.atan2((int)prevPos.y - trans.y, (int)prevPos.x - trans.x);
			float angle = (float) Math.atan2((int)eventPoint.y - trans.y, (int)eventPoint.x
					- trans.x);
			Vector3D axis = transformOf(camera.frame().inverseTransformOf(
					new Vector3D(0.0f, 0.0f, -1.0f)));
			 
			Quaternion rot = new Quaternion(axis, prev_angle - angle);
		  //right_handed coordinate system should go like this:
			//Quaternion rot = new Quaternion(axis, angle - prev_angle);
			
			// #CONNECTION# These two methods should go together (spinning detection
			// and activation)
			computeMouseSpeed(eventPoint);
			setSpinningQuaternion(rot);
			spin();
			prevPos = eventPoint;
			break;
		}

		case SCREEN_TRANSLATE: {
			// TODO: needs testing to see if it works correctly when left-handed is
			// set
			Vector3D trans = new Vector3D();
			int dir = mouseOriginalDirection(eventPoint);
			if (dir == 1)
				trans.set(((int)eventPoint.x - (int)prevPos.x), 0.0f, 0.0f);
			else if (dir == -1)
				trans.set(0.0f, -deltaY, 0.0f);
			switch (camera.type()) {
			case PERSPECTIVE:
				trans.mult((float) Math.tan(camera.fieldOfView() / 2.0f)
						* Math.abs((camera.frame().coordinatesOf(position())).z)
						/ camera.screenHeight());
				break;
			case ORTHOGRAPHIC: {
				float[] wh = camera.getOrthoWidthHeight();
				trans.x *= 2.0 * wh[0] / camera.screenWidth();
				trans.y *= 2.0 * wh[1] / camera.screenHeight();
				break;
			}
			}
			// Transform to world coordinate system.
			trans = camera.frame().orientation().rotate(
					Vector3D.mult(trans, translationSensitivity()));
			// And then down to frame
			if (referenceFrame() != null)
				trans = referenceFrame().transformOf(trans);

			translate(trans);
			prevPos = eventPoint;
			break;
		}

		case ROTATE: {
			Vector3D trans = camera.projectedCoordinatesOf(position());
			Quaternion rot = deformedBallQuaternion((int)eventPoint.x, (int)eventPoint.y,
					trans.x, trans.y, camera);
			trans.set(-rot.x, -rot.y, -rot.z);
			trans = camera.frame().orientation().rotate(trans);
			trans = transformOf(trans);
			rot.x = trans.x;
			rot.y = trans.y;
			rot.z = trans.z;
			// #CONNECTION# These two methods should go together (spinning detection
			// and activation)
			computeMouseSpeed(eventPoint);
			setSpinningQuaternion(rot);
			spin();
			prevPos = eventPoint;
			break;
		}

		case NO_MOUSE_ACTION:
			// Possible when the InteractiveFrame is a MouseGrabber. This method is
			// then called without startAction
			// because of mouseTracking.
			break;

		default:
			prevPos = eventPoint;
			break;
		}
	}

	/**
	 * Stops the InteractiveFrame mouse manipulation.
	 * <p>
	 * Overloading of
	 * {@link remixlab.remixcam.core.MouseGrabbable#mouseReleased(Point, Camera)}.
	 * <p>
	 * If the action was ROTATE MouseAction, a continuous spinning is possible if
	 * the speed of the mouse cursor is larger than {@link #spinningSensitivity()}
	 * when the button is released. Press the rotate button again to stop
	 * spinning.
	 * 
	 * @see #startSpinning(int)
	 * @see #isSpinning()
	 */
	public void mouseReleased(Point event, Camera camera) {
		keepsGrabbingMouse = false;

		if (prevConstraint != null)
			setConstraint(prevConstraint);

		if (((action == MouseAction.ROTATE) || (action == MouseAction.SCREEN_ROTATE))
				&& (mouseSpeed >= spinningSensitivity()))
			startSpinning(delay);

		action = MouseAction.NO_MOUSE_ACTION;
	}

	/**
	 * Overloading of
	 * {@link remixlab.remixcam.core.MouseGrabbable#mouseWheelMoved(int, Camera)}.
	 * <p>
	 * Using the wheel is equivalent to a {@link remixlab.proscene.Scene.MouseAction#ZOOM}.
	 * 
	 * @see #setWheelSensitivity(float)
	 */
	public void mouseWheelMoved(int rotation, Camera camera) {
		if (action == MouseAction.ZOOM) {
			float wheelSensitivityCoef = 8E-4f;
			// Vector3D trans(0.0, 0.0,
			// -event.delta()*wheelSensitivity()*wheelSensitivityCoef*(camera.position()-position()).norm());
			
			Vector3D	trans = new Vector3D(0.0f, 0.0f, rotation * wheelSensitivity() * wheelSensitivityCoef * (Vector3D.sub(camera.position(), position())).mag());
		  //right_handed coordinate system should go like this:
			//Vector3D trans = new Vector3D(0.0f, 0.0f, -rotation * wheelSensitivity() * wheelSensitivityCoef * (Vector3D.sub(camera.position(), position())).mag());
			
			// #CONNECTION# Cut-pasted from the mouseMoveEvent ZOOM case
			trans = camera.frame().orientation().rotate(trans);
			if (referenceFrame() != null)
				trans = referenceFrame().transformOf(trans);
			translate(trans);
		}

		// #CONNECTION# startAction should always be called before
		if (prevConstraint != null)
			setConstraint(prevConstraint);

		action = MouseAction.NO_MOUSE_ACTION;
	}

	/**
	 * Protected method that simply calls {@code startAction(action, true)}.
	 * 
	 * @see #startAction(Scene.MouseAction, boolean)
	 */
	protected void startAction(MouseAction action) {
		startAction(action, true);
	}
	
	/**
	 * Protected internal method used to handle mouse actions.
	 */
	public void startAction(MouseAction act, boolean withConstraint) {
		action = act;

		if (withConstraint)
			prevConstraint = null;
		else {
			prevConstraint = constraint();
			setConstraint(null);
		}

		switch (action) {
		case ROTATE:
		case SCREEN_ROTATE:
			mouseSpeed = 0.0f;
			stopSpinning();
			break;

		case SCREEN_TRANSLATE:
			dirIsFixed = false;
			break;

		default:
			break;
		}
	}

	/**
	 * Updates mouse speed, measured in pixels/milliseconds. Should be called by
	 * any method which wants to use mouse speed. Currently used to trigger
	 * spinning in {@link #mouseReleased(Point, Camera)}.
	 */
	protected void computeMouseSpeed(Point eventPoint) {
		float dist = (float) Point.distance(eventPoint.x, eventPoint.y, prevPos.getX(), prevPos.getY());

		if (startedTime == 0) {
			delay = 0;
			startedTime = (int) System.currentTimeMillis();
		} else {
			delay = (int) System.currentTimeMillis() - startedTime;
			startedTime = (int) System.currentTimeMillis();
		}

		if (delay == 0)
			// Less than a millisecond: assume delay = 1ms
			mouseSpeed = dist;
		else
			mouseSpeed = dist / delay;
	}

	/**
	 * Return 1 if mouse motion was started horizontally and -1 if it was more
	 * vertical. Returns 0 if this could not be determined yet (perfect diagonal
	 * motion, rare).
	 */
	protected int mouseOriginalDirection(Point eventPoint) {
		if (!dirIsFixed) {
			Point delta = new Point((eventPoint.x - pressPos.x),
					(eventPoint.y - pressPos.y));
			dirIsFixed = Math.abs((int)delta.x) != Math.abs((int)delta.y);
			horiz = Math.abs((int)delta.x) > Math.abs((int)delta.y);
		}

		if (dirIsFixed)
			if (horiz)
				return 1;
			else
				return -1;
		else
			return 0;
	}

	/**
	 * Returns a Quaternion computed according to the mouse motion. Mouse
	 * positions are projected on a deformed ball, centered on ({@code cx},
	 * {@code cy}).
	 */
	protected Quaternion deformedBallQuaternion(int x, int y, float cx, float cy,
			Camera camera) {
		// Points on the deformed ball
		float px = rotationSensitivity() * ((int)prevPos.x - cx) / camera.screenWidth();
		float py = rotationSensitivity() * (cy - (int)prevPos.y) / camera.screenHeight();
		float dx = rotationSensitivity() * (x - cx) / camera.screenWidth();
		float dy = rotationSensitivity() * (cy - y) / camera.screenHeight();

		Vector3D p1 = new Vector3D(px, py, projectOnBall(px, py));
		Vector3D p2 = new Vector3D(dx, dy, projectOnBall(dx, dy));
		// Approximation of rotation angle
		// Should be divided by the projectOnBall size, but it is 1.0
		Vector3D axis = p2.cross(p1);

		float angle = 2.0f * (float) Math.asin((float) Math.sqrt(Vector3D.squaredNorm(axis)
				/ Vector3D.squaredNorm(p1) / Vector3D.squaredNorm(p2)));

  	//lef-handed coordinate system correction (next two lines)
	  axis.y = -axis.y;
	  angle = -angle;

		return new Quaternion(axis, angle);
	}

	/**
	 * Returns "pseudo-distance" from (x,y) to ball of radius size. For a point
	 * inside the ball, it is proportional to the euclidean distance to the ball.
	 * For a point outside the ball, it is proportional to the inverse of this
	 * distance (tends to zero) on the ball, the function is continuous.
	 */
	static float projectOnBall(float x, float y) {
		// If you change the size value, change angle computation in
		// deformedBallQuaternion().
		float size = 1.0f;
		float size2 = size * size;
		float size_limit = size2 * 0.5f;

		float d = x * x + y * y;
		return d < size_limit ? (float) Math.sqrt(size2 - d) : size_limit
				/ (float) Math.sqrt(d);
	}
}
