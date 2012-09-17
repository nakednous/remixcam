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

import remixlab.remixcam.constraints.Constraint;
import remixlab.remixcam.devices.DeviceGrabbable;
import remixlab.remixcam.geom.*;
import remixlab.remixcam.util.AbstractTimerJob;

import java.util.*;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

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
 * the {@link remixlab.remixcam.core.AbstractScene#mouseGrabberPool()}.
 */

public class InteractiveFrame extends VFrame implements DeviceGrabbable, Copyable {
	@Override
	public int hashCode() {
    return new HashCodeBuilder(17, 37).
    appendSuper(super.hashCode()).
		append(action).
		append(delay).
		append(dirIsFixed).
		append(grabsMouseThreshold).
		append(grbsMouse).
		append(horiz).
		append(isInCamPath).
		append(isSpng).
		append(keepsGrabbingMouse).
		append(mouseSpeed).
		append(pressPos).
		append(prevPos).
		append(rotSensitivity).
		append(spngQuat).
		append(spngSensitivity).
		append(startedTime).
		append(transSensitivity).
		append(wheelSensitivity).
    toHashCode();
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
		return new EqualsBuilder()
    .appendSuper(super.equals(obj))		
    .append(action,other.action != null)
		.append(delay , other.delay)
		.append(dirIsFixed , other.dirIsFixed)
		.append(grabsMouseThreshold , other.grabsMouseThreshold)
		.append(grbsMouse , other.grbsMouse)	
		.append(horiz , other.horiz)
		.append(isInCamPath , other.isInCamPath)
		.append(isSpng , other.isSpng)
		.append(keepsGrabbingMouse , other.keepsGrabbingMouse)
		.append(mouseSpeed,other.mouseSpeed)
		.append(pressPos,other.pressPos )
		.append(prevPos ,other.prevPos )
		.append(rotSensitivity, other.rotSensitivity)
		.append(spngQuat,other.spngQuat)
		.append(spngSensitivity,other.spngSensitivity)
		.append(startedTime , other.startedTime)
		.append(transSensitivity ,other.transSensitivity)
		.append(wheelSensitivity ,other.wheelSensitivity)
		.isEquals();
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
	private AbstractTimerJob spinningTimerJob;
	private int startedTime;
	private int delay;

	private Quaternion spngQuat;

	// Whether the SCREEN_TRANS direction (horizontal or vertical) is fixed or
	// not.
	private boolean dirIsFixed;

	// MouseGrabber
	protected boolean keepsGrabbingMouse;

	protected AbstractScene.MouseAction action;
	protected Constraint prevConstraint; // When manipulation is without
	// Constraint.
	// Previous mouse position (used for incremental updates) and mouse press
	// position.
	protected Point prevPos, pressPos;

	protected boolean grbsMouse;

	protected boolean isInCamPath;

	// P R O S C E N E A N D P R O C E S S I N G A P P L E T A N D O B J E C T S
	public AbstractScene scene;

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
	 * the {@link remixlab.remixcam.core.AbstractScene#mouseGrabberPool()}.
	 */
	public InteractiveFrame(AbstractScene scn) {
		super(scn.is3D());		
		scene = scn;		
		
		action = AbstractScene.MouseAction.NO_MOUSE_ACTION;
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
		
		spinningTimerJob = new AbstractTimerJob() {
			public void execute() {
				spin();
			}
		};	
		scene.registerJob(spinningTimerJob);
		
		// delay = 10;
	}
	
	/**
	 * Copy constructor.
	 * 
	 * @param otherFrame the other interactive frame
	 */
	protected InteractiveFrame(InteractiveFrame otherFrame) {
		super(otherFrame);
		this.scene = otherFrame.scene;
		this.action = otherFrame.action;
		this.horiz = otherFrame.horiz;
		
		this.addInMouseGrabberPool();
		this.isInCamPath = otherFrame.isInCamPath;
		this.grbsMouse = otherFrame.grbsMouse;
		
		if(this.isInCamPath) {
			this.setListeners(new ArrayList<KeyFrameInterpolator>());
			Iterator<KeyFrameInterpolator> it = otherFrame.listeners().iterator();
			while (it.hasNext())
				this.listeners().add(it.next());
		}

		this.setGrabsMouseThreshold( otherFrame.grabsMouseThreshold()  );
		this.setRotationSensitivity( otherFrame.rotationSensitivity() );
		this.setTranslationSensitivity( otherFrame.translationSensitivity() );
		this.setSpinningSensitivity( otherFrame.spinningSensitivity() );
		this.setWheelSensitivity( otherFrame.wheelSensitivity() );

		this.keepsGrabbingMouse = otherFrame.keepsGrabbingMouse;
		this.isSpng = otherFrame.isSpng;
		this.prevConstraint = otherFrame.prevConstraint; 
		this.startedTime = otherFrame.startedTime;
		
		this.spinningTimerJob = new AbstractTimerJob() {
			public void execute() {
				spin();
			}
		};		
		scene.registerJob(spinningTimerJob);
	}
  
	/**
	 * Calls {@link #InteractiveFrame(InteractiveFrame)} (which is protected) and returns a copy of
	 * {@code this} object.
	 * 
	 * @see #InteractiveFrame(InteractiveFrame)
	 */
	public InteractiveFrame get() {
		return new InteractiveFrame(this);
	}

	/**
	 * Ad-hoc constructor needed to make editable a Camera path defined by
	 * KeyFrameInterpolator.
	 * <p>
	 * Constructs a Frame from the the {@code iFrame} {@link #translation()} and
	 * {@link #orientation()} and immediately adds it to the
	 * {@link #mouseGrabberPool()}.
	 * <p>
	 * A call on {@link #isInCameraPath()} on this Frame will return {@code true}.
	 * 
	 * <b>Attention:</b> Internal use. You should not call this constructor in your
	 * own applications.
	 * 
	 * @see remixlab.remixcam.core.Camera#addKeyFrameToPath(int)
	 */
	public InteractiveFrame(AbstractScene scn, InteractiveCameraFrame iFrame) {
		super(iFrame.translation(), iFrame.rotation());
		scene = scn;
		action = AbstractScene.MouseAction.NO_MOUSE_ACTION;
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

		setListeners(new ArrayList<KeyFrameInterpolator>());
		Iterator<KeyFrameInterpolator> it = iFrame.listeners().iterator();
		while (it.hasNext())
			listeners().add(it.next());
				
		spinningTimerJob = new AbstractTimerJob() {
			public void execute() {
				spin();
			}
		};		
		scene.registerJob(spinningTimerJob);
	}	

	/**
	 * Convenience function that simply calls {@code applyTransformation(AbstractScene)}.
	 * 
	 * @see remixlab.remixcam.geom.VFrame#applyTransformation(AbstractScene)
	 */
	public void applyTransformation() {
		applyTransformation(scene);
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
	public void checkIfGrabsMouse(int x, int y, Pinhole camera) {
		Vector3D proj = camera.projectedCoordinatesOf(position());
		setGrabsMouse(keepsGrabbingMouse || ((Math.abs(x - proj.vec[0]) < grabsMouseThreshold()) && (Math.abs(y - proj.vec[1]) < grabsMouseThreshold())));
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
	 * @see remixlab.remixcam.core.AbstractScene#isInMouseGrabberPool(DeviceGrabbable)
	 */
	public boolean isInMouseGrabberPool() {
		return scene.isInMouseGrabberPool(this);
	}	

	/**
	 * Convenience wrapper function that simply calls {@code scene.addInMouseGrabberPool(this)}.
	 * 
	 * @see remixlab.remixcam.core.AbstractScene#addInMouseGrabberPool(DeviceGrabbable)
	 */
	public void addInMouseGrabberPool() {
		scene.addInMouseGrabberPool(this);
	}

	/**
	 * Convenience wrapper function that simply calls {@code scene.removeFromMouseGrabberPool(this)}.
	 * 
	 * @see remixlab.remixcam.core.AbstractScene#removeFromMouseGrabberPool(DeviceGrabbable)
	 */
	public void removeFromMouseGrabberPool() {
		scene.removeFromMouseGrabberPool(this);
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
	 * {@link remixlab.remixcam.geom.VFrame#transformOfFrom(Vector3D, VFrame)} to convert
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
		return action != AbstractScene.MouseAction.NO_MOUSE_ACTION;
	}

	/**
	 * Stops the spinning motion started using {@link #startSpinning(int)}.
	 * {@link #isSpinning()} will return {@code false} after this call.
	 */
	public final void stopSpinning() {		
		spinningTimerJob.stop();
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
		if(updateInterval>0)
			spinningTimerJob.run(updateInterval);
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
	 * {@link remixlab.remixcam.devices.DeviceGrabbable#mouseClicked(remixlab.remixcam.core.AbstractScene.Button, int, Camera)}.
	 * <p>
	 * Left button double click aligns the InteractiveFrame with the camera axis (see {@link #alignWithFrame(VFrame)}
	 * and {@link remixlab.remixcam.core.AbstractScene.ClickAction#ALIGN_FRAME}). Right button projects the InteractiveFrame on
	 * the camera view direction.
	 */
	public void mouseClicked(/**Point eventPoint,*/ AbstractScene.Button button, int numberOfClicks, Pinhole camera) {
		if(numberOfClicks != 2)
			return;
		switch (button) {
		case LEFT:  alignWithFrame(camera.frame()); break;
    case RIGHT:
    	if(scene.is3D())
    	projectOnLine(camera.position(), ((Camera) camera).viewDirection());
    	else {
    		//TODO implement 2D case
    	}
    break;
    default: break;
    }
	}

	/**
	 * Initiates the InteractiveFrame mouse manipulation. Overloading of
	 * {@link remixlab.remixcam.devices.DeviceGrabbable#mousePressed(Point, Camera)}.
	 * 
	 * The mouse behavior depends on which button is pressed.
	 * 
	 * @see #mouseDragged(Point, Camera)
	 * @see #mouseReleased(Point, Camera)
	 */
	public void mousePressed(Point eventPoint, Pinhole camera) {
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
	public void mouseDragged(Point eventPoint, Pinhole camera) {
		if( ( scene.is2D() ) && ( !action.is2D() ) )
			return;
		
		int deltaY = 0;
		if(action != AbstractScene.MouseAction.NO_MOUSE_ACTION)
			if( scene.isRightHanded() )
				deltaY = (int) (eventPoint.y - prevPos.y);
			else
				deltaY = (int) (prevPos.y - eventPoint.y);

		switch (action) {
		case TRANSLATE: {
			if( scene.is3D() ) {
			Point delta = new Point((eventPoint.x - prevPos.x), deltaY);
			Vector3D trans = new Vector3D((int) delta.getX(), (int) -delta.getY(), 0.0f);
			// Scale to fit the screen mouse displacement
			switch (((Camera) camera).type()) {
			case PERSPECTIVE:
				trans.mult(2.0f * (float) Math.tan(((Camera) camera).fieldOfView() / 2.0f)
						* Math.abs((camera.frame().coordinatesOf(position())).vec[2])
						/ camera.screenHeight());
				break;
			case ORTHOGRAPHIC: {
				float[] wh = camera.getOrthoWidthHeight();
				trans.vec[0] *= 2.0 * wh[0] / camera.screenWidth();
				trans.vec[1] *= 2.0 * wh[1] / camera.screenHeight();
				break;
			}
			}
			// Transform to world coordinate system.
			trans = camera.frame().orientation().rotate(Vector3D.mult(trans, translationSensitivity()));
			// And then down to frame
			if (referenceFrame() != null)
				trans = referenceFrame().transformOf(trans);
			translate(trans);
			prevPos = eventPoint;
			}
			else {
			  //TODO 2D case needs testing
				Point delta = new Point((eventPoint.x - prevPos.x), deltaY);
				Vector3D trans = new Vector3D((int) delta.getX(), (int) -delta.getY(), 0.0f);
				float[] wh = camera.getOrthoWidthHeight();
				trans.vec[0] *= 2.0 * wh[0] / camera.screenWidth();
				trans.vec[1] *= 2.0 * wh[1] / camera.screenHeight();
				// Transform to world coordinate system.
				trans = camera.frame().orientation().rotate(Vector3D.mult(trans, translationSensitivity()));
				// And then down to frame
				if (referenceFrame() != null)
					trans = referenceFrame().transformOf(trans);
				translate(trans);
				prevPos = eventPoint;
			}
			break;
		}

		case ZOOM: {
			if(scene.is3D()) {
			// #CONNECTION# wheelEvent ZOOM case
		  //Warning: same for left and right CoordinateSystemConvention:
			Vector3D trans = new Vector3D(0.0f, 0.0f, (Vector3D.sub(((Camera) camera).position(), position())).mag() * ((int) (eventPoint.y - prevPos.y)) / camera.screenHeight());
			trans = camera.frame().orientation().rotate(trans);
			if (referenceFrame() != null)
				trans = referenceFrame().transformOf(trans);
			translate(trans);
			prevPos = eventPoint;
			}
			else {
				//TODO implement 2D case
				//it just doesn't make any sense in 2D
			}				
			break;
		}

		case SCREEN_ROTATE: {
			if( scene.is3D() ) {
			// TODO: needs testing to see if it works correctly when left-handed is set
			Vector3D trans = ((Camera) camera).projectedCoordinatesOf(position());
			float prev_angle = (float) Math.atan2((int)prevPos.y - trans.vec[1], (int)prevPos.x - trans.vec[0]);
			float angle = (float) Math.atan2((int)eventPoint.y - trans.vec[1], (int)eventPoint.x - trans.vec[0]);
			Vector3D axis = transformOf(camera.frame().inverseTransformOf(new Vector3D(0.0f, 0.0f, -1.0f)));
			
			Quaternion rot;
			if( scene.isRightHanded() )
				rot = new Quaternion(axis, angle - prev_angle);
			else
				rot = new Quaternion(axis, prev_angle - angle);			
			
			// #CONNECTION# These two methods should go together (spinning detection and activation)
			computeMouseSpeed(eventPoint);
			setSpinningQuaternion(rot);
			spin();
			prevPos = eventPoint;
			}
			else {
			//TODO implement 2D case
			}
			break;			
		}

		case SCREEN_TRANSLATE: {
			if( scene.is3D() ) { 
			// TODO: needs testing to see if it works correctly when left-handed is set
			Vector3D trans = new Vector3D();
			int dir = mouseOriginalDirection(eventPoint);
			if (dir == 1)
				trans.set(((int)eventPoint.x - (int)prevPos.x), 0.0f, 0.0f);
			else if (dir == -1)
				trans.set(0.0f, -deltaY, 0.0f);
			switch (((Camera) camera).type()) {
			case PERSPECTIVE:
				trans.mult((float) Math.tan(((Camera) camera).fieldOfView() / 2.0f)
						* Math.abs((camera.frame().coordinatesOf(position())).vec[2])
						/ camera.screenHeight());
				break;
			case ORTHOGRAPHIC: {
				float[] wh = camera.getOrthoWidthHeight();
				trans.vec[0] *= 2.0 * wh[0] / camera.screenWidth();
				trans.vec[1] *= 2.0 * wh[1] / camera.screenHeight();
				break;
			}
			}
			// Transform to world coordinate system.
			trans = camera.frame().orientation().rotate(Vector3D.mult(trans, translationSensitivity()));
			// And then down to frame
			if (referenceFrame() != null)
				trans = referenceFrame().transformOf(trans);

			translate(trans);
			prevPos = eventPoint;
			}
			else {
			//TODO implement 2D case
			}
			break;
		}

		case ROTATE: {			
			if( scene.is3D() ) {
			Vector3D trans = camera.projectedCoordinatesOf(position());
			Quaternion rot = deformedBallQuaternion((int)eventPoint.x, (int)eventPoint.y,	trans.vec[0], trans.vec[1], (Camera) camera);
			trans.set(-rot.quat[0], -rot.quat[1], -rot.quat[2]);
			trans = camera.frame().orientation().rotate(trans);
			trans = transformOf(trans);
			rot.quat[0] = trans.vec[0];
			rot.quat[1] = trans.vec[1];
			rot.quat[2] = trans.vec[2];
			// #CONNECTION# These two methods should go together (spinning detection and activation)
			computeMouseSpeed(eventPoint);
			setSpinningQuaternion(rot);
			spin();
			prevPos = eventPoint;
			}
			else {
			//TODO implement 2D case
			}
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
	 * {@link remixlab.remixcam.devices.DeviceGrabbable#mouseReleased(Point, Camera)}.
	 * <p>
	 * If the action was ROTATE MouseAction, a continuous spinning is possible if
	 * the speed of the mouse cursor is larger than {@link #spinningSensitivity()}
	 * when the button is released. Press the rotate button again to stop
	 * spinning.
	 * 
	 * @see #startSpinning(int)
	 * @see #isSpinning()
	 */
	public void mouseReleased(Point event, Pinhole camera) {
		keepsGrabbingMouse = false;

		if (prevConstraint != null)
			setConstraint(prevConstraint);

		if (((action == AbstractScene.MouseAction.ROTATE) || (action == AbstractScene.MouseAction.SCREEN_ROTATE))
				&& (mouseSpeed >= spinningSensitivity()))
			startSpinning(delay);

		action = AbstractScene.MouseAction.NO_MOUSE_ACTION;
	}

	/**
	 * Overloading of
	 * {@link remixlab.remixcam.devices.DeviceGrabbable#mouseWheelMoved(int, Camera)}.
	 * <p>
	 * Using the wheel is equivalent to a {@link remixlab.remixcam.core.AbstractScene.MouseAction#ZOOM}.
	 * 
	 * @see #setWheelSensitivity(float)
	 */
	public void mouseWheelMoved(int rotation, Pinhole camera) {
		if( ( scene.is2D() ) && ( !action.is2D() ) )
			return;
		
		if (action == AbstractScene.MouseAction.ZOOM) {
			if(scene.is3D()) {
			float wheelSensitivityCoef = 8E-4f;
			// Vector3D trans(0.0, 0.0,
			// -event.delta()*wheelSensitivity()*wheelSensitivityCoef*(camera.position()-position()).norm());
			
			Vector3D trans;
			/**
			if(scene.isRightHanded())
				trans = new Vector3D(0.0f, 0.0f, -rotation * wheelSensitivity() * wheelSensitivityCoef * (Vector3D.sub(camera.position(), position())).mag());
			else
			*/
				trans = new Vector3D(0.0f, 0.0f, rotation * wheelSensitivity() * wheelSensitivityCoef * (Vector3D.sub(((Camera) camera).position(), position())).mag());
					
			// #CONNECTION# Cut-pasted from the mouseMoveEvent ZOOM case
			trans = camera.frame().orientation().rotate(trans);
			if (referenceFrame() != null)
				trans = referenceFrame().transformOf(trans);
			translate(trans);
			}
			else {
				//TODO implement 2D case
			}
		}

		// #CONNECTION# startAction should always be called before
		if (prevConstraint != null)
			setConstraint(prevConstraint);

		action = AbstractScene.MouseAction.NO_MOUSE_ACTION;
	}

	/**
	 * Protected method that simply calls {@code startAction(action, true)}.
	 * 
	 * @see #startAction(Scene.MouseAction, boolean)
	 */
	protected void startAction(AbstractScene.MouseAction action) {
		startAction(action, true);
	}
	
	/**
	 * Protected internal method used to handle mouse actions.
	 */
	public void startAction(AbstractScene.MouseAction act, boolean withConstraint) {
		action = act;
		
		if( ( scene.is2D() ) && ( !action.is2D() ) )
			return;

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
		float dist = (float) Point.distance(eventPoint.x, eventPoint.y, prevPos
				.getX(), prevPos.getY());

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
	protected Quaternion deformedBallQuaternion(int x, int y, float cx, float cy,	Camera camera) {
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
		float angle = 2.0f * (float) Math.asin((float) Math.sqrt(axis.squaredNorm() / p1.squaredNorm() / p2.squaredNorm()));

  	//lef-handed coordinate system correction
		if( scene.isLeftHanded() ) {
			axis.vec[1] = -axis.vec[1];
			angle = -angle;
	  }

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
		return d < size_limit ? (float) Math.sqrt(size2 - d) : size_limit	/ (float) Math.sqrt(d);
	}
}
