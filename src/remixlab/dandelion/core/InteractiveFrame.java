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

package remixlab.dandelion.core;

//import remixlab.remixcam.constraint.Constraint;
import remixlab.dandelion.event.DOF1Event;
import remixlab.dandelion.event.DOF2Event;
import remixlab.dandelion.event.DOF3Event;
import remixlab.dandelion.event.DOF6Event;
import remixlab.dandelion.geom.*;
import remixlab.dandelion.util.AbstractTimerJob;
import remixlab.duoable.profile.Actionable;
import remixlab.duoable.profile.Duoble;
import remixlab.tersehandling.core.AbstractAgent;
import remixlab.tersehandling.core.Copyable;
import remixlab.tersehandling.core.Grabbable;
import remixlab.tersehandling.event.*;

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
 * the {@link remixlab.dandelion.core.AbstractScene#deviceGrabberPool()}.
 */

public class InteractiveFrame extends GeomFrame implements Grabbable, Copyable {
	@Override
	public int hashCode() {
    return new HashCodeBuilder(17, 37).
    appendSuper(super.hashCode()).
		append(grabsDeviceThreshold).
		//append(grbsDevice).
		append(isInCamPath).
		append(isSpng).
		append(rotSensitivity).
		append(spngQuat).
		append(spngSensitivity).
		append(spinningFriction).
		append(sFriction).
		append(transSensitivity).
		append(wheelSensitivity).
    toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;		
		if (obj.getClass() != getClass()) return false;
		
		InteractiveFrame other = (InteractiveFrame) obj;
		return new EqualsBuilder()
    .appendSuper(super.equals(obj))
		.append(grabsDeviceThreshold, other.grabsDeviceThreshold)
		//.append(grbsDevice, other.grbsDevice)	
		.append(isInCamPath, other.isInCamPath)
		.append(isSpng, other.isSpng)
		.append(spinningFriction, other.spinningFriction)
		.append(sFriction, other.sFriction)
		.append(rotSensitivity, other.rotSensitivity)
		.append(spngQuat,other.spngQuat)
		.append(spngSensitivity,other.spngSensitivity)
		.append(transSensitivity, other.transSensitivity)
		.append(wheelSensitivity, other.wheelSensitivity)
		.isEquals();
	}
	
	//private boolean horiz;// Two simultaneous InteractiveFrame require two mice!
	private int grabsDeviceThreshold;
	private float rotSensitivity;
	private float transSensitivity;
	private float wheelSensitivity;
	
	// spinning stuff:
	protected float deviceSpeed;
	private float spngSensitivity;
	private boolean isSpng;
	private AbstractTimerJob spinningTimerJob;
	private Orientable spngQuat;
	protected float spinningFriction; //new	
	private float sFriction; //new

	// Whether the SCREEN_TRANS direction (horizontal or vertical) is fixed or not.
	//TODO how to deal with SCREEN_TRANS
	//private boolean dirIsFixed;

	// MouseGrabber
	//public boolean keepsGrabbingCursor;
	//TODO define if this shpuld go
	//protected Constraint prevConstraint; // When manipulation is without Constraint.
	// Previous mouse position (used for incremental updates) and mouse press position.
	//protected Point prevPos, pressPos;

	//protected boolean grbsDevice;

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
	 * the {@link remixlab.dandelion.core.AbstractScene#deviceGrabberPool()}.
	 */
	public InteractiveFrame(AbstractScene scn) {
		super(scn.is3D());		
		scene = scn;		

		scene.addInDeviceGrabberPool(this);
		isInCamPath = false;
		//grbsDevice = false;

		setGrabsDeviceThreshold(10);
		setRotationSensitivity(1.0f);
		setTranslationSensitivity(1.0f);
		setWheelSensitivity(20.0f);
		
		isSpng = false;
		setSpinningSensitivity(0.3f);
		setSpinningFriction(0.5f);
		
		spinningTimerJob = new AbstractTimerJob() {
			public void execute() {
				spin();
			}
		};	
		scene.registerJob(spinningTimerJob);
	}
	
	/**
	 * Copy constructor.
	 * 
	 * @param otherFrame the other interactive frame
	 */
	protected InteractiveFrame(InteractiveFrame otherFrame) {
		super(otherFrame);
		this.scene = otherFrame.scene;
		
		this.scene.addInDeviceGrabberPool(this);
		this.isInCamPath = otherFrame.isInCamPath;
		//this.grbsDevice = otherFrame.grbsDevice;
		
		/**
		// TODO need check?
		// always copy listeners in super
		if(this.isInCamPath) {
			this.setListeners(new ArrayList<KeyFrameInterpolator>());
			Iterator<KeyFrameInterpolator> it = otherFrame.listeners().iterator();
			while (it.hasNext())
				this.listeners().add(it.next());
		}
		*/

		this.setGrabsDeviceThreshold( otherFrame.grabsDeviceThreshold()  );
		this.setRotationSensitivity( otherFrame.rotationSensitivity() );
		this.setTranslationSensitivity( otherFrame.translationSensitivity() );
		this.setWheelSensitivity( otherFrame.wheelSensitivity() );

		//this.keepsGrabbingCursor = otherFrame.keepsGrabbingCursor;		
		//this.prevConstraint = otherFrame.prevConstraint; 
		
		this.isSpng = otherFrame.isSpng;
		this.setSpinningSensitivity( otherFrame.spinningSensitivity() );
		this.setSpinningFriction( otherFrame.spinningFriction() );
		
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
	 * {@link #deviceGrabberPool()}.
	 * <p>
	 * A call on {@link #isInCameraPath()} on this Frame will return {@code true}.
	 * 
	 * <b>Attention:</b> Internal use. You should not call this constructor in your
	 * own applications.
	 * 
	 * @see remixlab.dandelion.core.Camera#addKeyFrameToPath(int)
	 */
	public InteractiveFrame(AbstractScene scn, InteractiveCameraFrame iFrame) {
		super(iFrame.rotation(), iFrame.translation(), iFrame.scaling());
		scene = scn;

		scene.addInDeviceGrabberPool(this);
		isInCamPath = true;
		//grbsDevice = false;

		setGrabsDeviceThreshold(10);
		setRotationSensitivity(1.0f);
		setTranslationSensitivity(1.0f);
		setWheelSensitivity(20.0f);

		/**
		setListeners(new ArrayList<KeyFrameInterpolator>());
		Iterator<KeyFrameInterpolator> it = iFrame.listeners().iterator();
		while (it.hasNext())
			listeners().add(it.next());
		*/
		setListeners(iFrame);
		
		isSpng = false;
		setSpinningSensitivity(0.3f);
		setSpinningFriction(0.5f);
				
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
	 * @see remixlab.dandelion.geom.GeomFrame#applyTransformation(AbstractScene)
	 */
	public void applyTransformation() {
		applyTransformation(scene);
	}
	
	/**
	 * Convenience function that simply calls {@code applyWorldTransformation(Abstractscene)}
	 * 
	 * @see remixlab.dandelion.geom.GeomFrame#applyWorldTransformation(AbstractScene)
	 */
	public void applyWorldTransformation() {
		applyWorldTransformation(scene);
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
	 * {@link #checkIfGrabsDevice(int, int, Camera)}.
	 * 
	 * @see #setGrabsDeviceThreshold(int)
	 */
	public int grabsDeviceThreshold() {
		return grabsDeviceThreshold;
	}
	
	/**
	 * Sets the number of pixels that defined the {@link #checkIfGrabsDevice(int, int, Camera)}
	 * condition.
	 * 
	 * @param threshold number of pixels that defined the {@link #checkIfGrabsDevice(int, int, Camera)}
	 * condition. Default value is 10 pixels (which is set in the constructor). Negative values are
	 * silently ignored.
	 * 
	 * @see #grabsDeviceThreshold()
	 * @see #checkIfGrabsDevice(int, int, Camera)
	 */
	public void setGrabsDeviceThreshold( int threshold ) {
		if(threshold >= 0)
			grabsDeviceThreshold = threshold; 
	}

	/**
	 * Implementation of the MouseGrabber main method.
	 * <p>
	 * The InteractiveFrame {@link #grabsAgent()} when the mouse is within a {@link #grabsDeviceThreshold()}
	 * pixels region around its
	 * {@link remixlab.dandelion.core.Camera#projectedCoordinatesOf(Vec)}
	 * {@link #position()}.
	 */
	@Override
	public boolean checkIfGrabsInput(GenericEvent event) {
		int x=0, y=0;
		if(event instanceof GenericDOF2Event) {
			//x = scene.cursorX - scene.upperLeftCorner.getX();
			//y = scene.cursorY - scene.upperLeftCorner.getY();
			x = (int)((GenericDOF2Event)event).getX();
			y = (int)((GenericDOF2Event)event).getY();
		}
		Vec proj = scene.pinhole().projectedCoordinatesOf(position());
		return ((Math.abs(x - proj.vec[0]) < grabsDeviceThreshold()) && (Math.abs(y - proj.vec[1]) < grabsDeviceThreshold()));
		//setGrabsInput(keepsGrabbingCursor || ((Math.abs(x - proj.vec[0]) < grabsDeviceThreshold()) && (Math.abs(y - proj.vec[1]) < grabsDeviceThreshold())));
	}
	
	/**
	@Override
	public void checkIfGrabsInput(GenericEvent<?> event) {
		int x=0, y=0;
		if(event instanceof DOF2Event) {
			//x = scene.cursorX - scene.upperLeftCorner.getX();
			//y = scene.cursorY - scene.upperLeftCorner.getY();
			x = (int)((DOF2Event)event).getX();
			y = (int)((DOF2Event)event).getY();
		}
		DLVector proj = scene.pinhole().projectedCoordinatesOf(position());
		
		if( scene.interactiveFrameIsDrawn() && scene.interactiveFrame() == this)
			setGrabsInput(true);
		else
			setGrabsInput((Math.abs(x - proj.vec[0]) < grabsDeviceThreshold()) && (Math.abs(y - proj.vec[1]) < grabsDeviceThreshold()));
		//setGrabsInput(keepsGrabbingCursor || ((Math.abs(x - proj.vec[0]) < grabsDeviceThreshold()) && (Math.abs(y - proj.vec[1]) < grabsDeviceThreshold())));
	}
	*/

	/**
	 * Returns {@code true} when the MouseGrabber grabs the Scene's mouse events.
	 * <p>
	 * This flag is set with {@link #setGrabsInput(boolean)} by the
	 * {@link #checkIfGrabsDevice(int, int, Camera)} method.
	 */
	@Override
	public boolean grabsAgent(AbstractAgent agent) {
		return agent.trackedGrabber() == this;
	}

	/**
	 * Convenience wrapper function that simply returns {@code scene.isInMouseGrabberPool(this)}.
	 * 
	 * @see remixlab.dandelion.core.AbstractScene#isInDeviceGrabberPool(Grabbable)
	 */
	public boolean isInAgentPool(AbstractAgent agent) {
		return agent.isInPool(this);
	}

	/**
	 * Convenience wrapper function that simply calls {@code scene.addInMouseGrabberPool(this)}.
	 * 
	 * @see remixlab.dandelion.core.AbstractScene#addInDeviceGrabberPool(Grabbable)
	 */
	public void addInPool(AbstractAgent agent) {
		agent.addInPool(this);
	}

	/**
	 * Convenience wrapper function that simply calls {@code scene.removeFromMouseGrabberPool(this)}.
	 * 
	 * @see remixlab.dandelion.core.AbstractScene#removeFromDeviceGrabberPool(Grabbable)
	 */
	public void removeFromDeviceGrabberPool(AbstractAgent agent) {
		agent.removeFromPool(this);
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
	 * @see #tossingSensitivity()
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
	 * setting the {@link remixlab.dandelion.core.Camera#arcballReferencePoint()} to a
	 * point on the zoomed region).
	 * 
	 * @see #setTranslationSensitivity(float)
	 * @see #rotationSensitivity()
	 * @see #spinningSensitivity()
	 * @see #wheelSensitivity()
	 * @see #tossingSensitivity()
	 */
	public final float translationSensitivity() {
		return transSensitivity;
	}

	/**
	 * Returns the minimum mouse speed required (at button release) to make the
	 * InteractiveFrame {@link #spin()}.
	 * <p>
	 * See {@link #spin()}, {@link #spinningQuaternion()} and
	 * {@link #startSpinning(long)} for details.
	 * <p>
	 * Mouse speed is expressed in pixels per milliseconds. Default value is 0.3
	 * (300 pixels per second). Use {@link #setSpinningSensitivity(float)} to tune
	 * this value. A higher value will make spinning more difficult (a value of
	 * 100.0 forbids spinning in practice).
	 * 
	 * @see #setSpinningSensitivity(float)
	 * @see #setTossingSensitivity(float)
	 * @see #translationSensitivity()
	 * @see #rotationSensitivity()
	 * @see #wheelSensitivity()
	 * @see #tossingSensitivity()
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
	 * @see #tossingSensitivity()
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
	 * 
	 * @see #isTossing()
	 */
	public final boolean isSpinning() {
		return isSpng;
	}

	/**
	 * Returns the incremental rotation that is applied by {@link #spin()} to the
	 * InteractiveFrame orientation when it {@link #isSpinning()}.
	 * <p>
	 * Default value is a {@code null} rotation (identity Quaternion). Use
	 * {@link #setSpinningQuaternion(Quat)} to change this value.
	 * <p>
	 * The {@link #spinningQuaternion()} axis is defined in the InteractiveFrame
	 * coordinate system. You can use
	 * {@link remixlab.proscene.Frame#transformOfFrom(PVector, Frame)} to convert
	 * this axis from another Frame coordinate system.
	 * <p>
	 * <b>Attention: </b>Spinning may be decelerated according to {@link #spinningFriction()}
	 * till it stops completely.
	 * 
	 * @see #tossingDirection()
	 */
	public final Orientable spinningQuaternion() {
		return spngQuat;
	}

	/**
	 * Defines the {@link #spinningQuaternion()}. Its axis is defined in the
	 * InteractiveFrame coordinate system.
	 * 
	 * @see #setTossingDirection(PVector)
	 */
	public final void setSpinningQuaternion(Orientable spinningQuaternion) {
		spngQuat = spinningQuaternion;
	}

	/**
	 * Returns {@code true} when the InteractiveFrame is being manipulated with
	 * the mouse. Can be used to change the display of the manipulated object
	 * during manipulation.
	 */
	//TODO how does this fit new model? Maire is using it
	/**
	public boolean isInInteraction() {
		return action != DOF_6Action.NO_ACTION;
	}
	*/

	/**
	 * Stops the spinning motion started using {@link #startSpinning(long)}.
	 * {@link #isSpinning()} will return {@code false} after this call.
	 * <p>
	 * <b>Attention: </b>This method may be called by {@link #spin()}, since spinning may
	 * be decelerated according to {@link #spinningFriction()} till it stops completely.
	 * 
	 * @see #spinningFriction()
	 * @see #toss()
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
	 * <p>
	 * <b>Attention: </b>Spinning may be decelerated according to {@link #spinningFriction()}
	 * till it stops completely.
	 * 
	 * @see #spinningFriction()
	 * @see #toss()
	 */
	public void startSpinning(DOF2Event e) {
		deviceSpeed = e.speed();
		isSpng = true;
		int updateInterval = (int) e.delay();
		if(updateInterval>0)
			spinningTimerJob.run(updateInterval);
	}
	
	/**
	 * Rotates the InteractiveFrame by its {@link #spinningQuaternion()}. Called
	 * by a timer when the InteractiveFrame {@link #isSpinning()}. 
	 * <p>
	 * <b>Attention: </b>Spinning may be decelerated according to
	 * {@link #spinningFriction()} till it stops completely.
	 * 
	 * @see #spinningFriction()
	 * @see #toss()
	 */
	public void spin() {		
		if(spinningFriction() > 0) {
			if (deviceSpeed == 0) {
				stopSpinning();
				return;
			}
			rotate(spinningQuaternion());
			recomputeSpinningQuaternion();						
		}
		else
			rotate(spinningQuaternion());
	}
	
	/**
	 * Defines the {@link #spinningFriction()}. Values must be
	 * in the range [0..1].
	 */
	public void setSpinningFriction(float f) {
		if(f < 0 || f > 1)
			return;
		spinningFriction = f;
		setSpinningFrictionFx(spinningFriction);
	} 
	
	/**
	 * Defines the spinning deceleration.
	 * <p>
	 * Default value is 0.0, i.e., no spinning deceleration. Use
	 * {@link #setSpinningFriction(float)} to tune this value.
	 * A higher value will make spinning more difficult (a value of
	 * 1.0 forbids spinning).
	 * 
	 * @see #tossingFriction()
	 */
	public float spinningFriction() {
		return spinningFriction;
	}
	
	/**
	 * Internal use.
	 * <p>
	 * Computes and caches the value of the spinning friction used in
	 * {@link #recomputeSpinningQuaternion()}.
	 */
	protected void setSpinningFrictionFx(float spinningFriction) {
		sFriction = spinningFriction*spinningFriction*spinningFriction;
	}
	
	/**
	 * Internal use.
	 * <p>
	 * Returns the cached value of the spinning friction used in
	 * {@link #recomputeSpinningQuaternion()}.
	 */
	protected float spinningFrictionFx() {
		return sFriction;
	}
	
	/**
	 * Internal method. Recomputes the {@link #spinningQuaternion()}
	 * according to {@link #spinningFriction()}.
	 * 
	 * @see #recomputeTossingDirection()
	 */
	protected void recomputeSpinningQuaternion() {
		float prevSpeed = deviceSpeed;
		float damping = 1.0f - spinningFrictionFx();
		deviceSpeed *= damping;
		if (Math.abs(deviceSpeed) < .001f)
			deviceSpeed = 0;
		float currSpeed = deviceSpeed;
		if( scene.is3D() )
			((Quat)spinningQuaternion()).fromAxisAngle(((Quat)spinningQuaternion()).axis(), spinningQuaternion().angle() * (currSpeed / prevSpeed) );
		else
			this.setSpinningQuaternion(new Rotation(spinningQuaternion().angle() * (currSpeed / prevSpeed)));
	}
	
	/**
	 * Overloading of
	 * {@link remixlab.remixcam.core.DeviceGrabbable#buttonClicked(remixlab.remixcam.core.AbstractScene.Button, int, Camera)}.
	 * <p>
	 * Left button double click aligns the InteractiveFrame with the camera axis (see {@link #alignWithFrame(GeomFrame)}
	 * and {@link remixlab.remixcam.core.AbstractScene.ClickAction#ALIGN_FRAME}). Right button projects the InteractiveFrame on
	 * the camera view direction.
	 */
	/**
	@Override
	public void buttonClicked(DLClickEvent clickEvent) {
		Pinhole camera = scene.pinhole();
		if(numberOfClicks != 2)
			return;
		switch (button) {
		case LEFT:  alignWithFrame(camera.frame()); break;
    case RIGHT:
      //TODO test 2D case
    	projectOnLine(camera.position(), camera.viewDirection());
    break;
    default: break;
    }
	}
	*/
	
	@Override
	public void performInteraction(GenericEvent e) {
		stopSpinning();
		
		if(e == null) return;
		
		if(e instanceof GenericKeyboardEvent || e instanceof GenericClickEvent)
			scene.performInteraction(e);
		
		Duoble<?> event;
		//begin:
		//if (grabsInput()) keepsGrabbingCursor = true;
		//end:
		
		if(e instanceof Duoble)
			event = (Duoble<?>)e;
		else 
			return;	
		
		
		// same as no action
		if( event.getAction() == null )
			return;
		
		if( !(event instanceof DOF1Event) && ! (event instanceof DOF2Event) &&
				!(event instanceof DOF3Event) && ! (event instanceof DOF6Event))
			return;
		
		if( ( scene.is2D() ) && ( !event.getAction().is2D() ) )
			return;
		
		if( event instanceof GenericMotionEvent) {
			if( scene.is2D() )
				execAction2D((GenericMotionEvent)event);
			else
				execAction3D((GenericMotionEvent)event);
		}
	}
	
	//TODO implement me
	protected void execAction2D(GenericMotionEvent event) {
	}
	
  //TODO implement me
	protected void execAction3D(GenericMotionEvent e) {
		Actionable<DLAction> a=null;
		if(e instanceof DOF1Event)
			a = ((DOF1Event) e).getAction();
		if(e instanceof DOF2Event)
			a = ((DOF2Event) e).getAction();
		if(e instanceof DOF3Event)
			a = ((DOF3Event) e).getAction();
		if(e instanceof DOF6Event)
			a = ((DOF6Event) e).getAction();
		
		if(a == null) return;
		DLAction id = a.action();		
		
		DOF2Event event;
		float delta = 0;
		
		switch (id) {
		case ZOOM: {
			if( e instanceof DOF1Event ) {
				delta = ((DOF1Event)e).getX() * wheelSensitivity();	
			}
			else {
				event = (DOF2Event)e;
				delta = event.getDY(); /**((float)event.getY() - (float)event.getPrevY())*/;
			}
			
			if(delta >= 0)
				scale(1 + Math.abs(delta) / (float) scene.height());
			else
				inverseScale(1 + Math.abs(delta) / (float) scene.height());
			break;
		}
		
		case ROTATE: {
			event = (DOF2Event)e;
			Vec trans = scene.camera().projectedCoordinatesOf(position());
			Quat rot = deformedBallQuaternion(event, trans.x(), trans.y(), scene.camera());
			rot = iFrameQuaternion(rot, scene.camera());			
			setSpinningQuaternion(rot);
			//rotate(spinningQuaternion());
			startSpinning(event);
			break;			
		}
		
		case TRANSLATE: {
			event = (DOF2Event)e;
			//Point delta = new Point(event.getX(), scene.isRightHanded() ? event.getY() : -event.getY());
			Vec trans = new Vec(event.getDX(), scene.isRightHanded() ? -event.getDY() : event.getDY(), 0.0f);			
			
		  // Scale to fit the screen mouse displacement
			switch ( scene.camera().type() ) {
			case PERSPECTIVE:
				trans.mult(2.0f * (float) Math.tan(scene.camera().fieldOfView() / 2.0f)
						            * Math.abs((scene.camera().frame().coordinatesOf(position())).vec[2] * scene.camera().frame().magnitude().z())
								        //* Math.abs((scene.camera().frame().coordinatesOf(position())).vec[2])						            
								        / scene.camera().screenHeight());
				break;
				case ORTHOGRAPHIC: {
					float[] wh = scene.camera().getOrthoWidthHeight();
					trans.vec[0] *= 2.0 * wh[0] / scene.camera().screenWidth();
					trans.vec[1] *= 2.0 * wh[1] / scene.camera().screenHeight();
					break;
				}
			}
			// same as:
			trans = scene.camera().frame().orientation().rotate(Vec.mult(trans, translationSensitivity()));
			// but takes into account scaling
			//trans = scene.camera().frame().inverseTransformOf(Vector3D.mult(trans, translationSensitivity()));
			// And then down to frame
			if (referenceFrame() != null)
				trans = referenceFrame().transformOf(trans);
			translate(trans);
			break;
		}
		
		case NATURAL: {
			DOF6Event event6 = (DOF6Event)e;
			Vec t = new Vec();
	    Quat q = new Quat();
      // A. Translate the iFrame
	    // Transform to world coordinate system
	    t = scene.camera().frame().inverseTransformOf(new Vec(event6.getX(),event6.getY(),-event6.getZ()), false); //same as: t = cameraFrame.orientation().rotate(new PVector(tx,ty,-tz));
	    // And then down to frame
	    if (referenceFrame() != null)
	    	t = referenceFrame().transformOf(t, false);
	    translate(t);
	    // B. Rotate the iFrame
	    t = scene.camera().projectedCoordinatesOf(position()); 
	    q.fromEulerAngles(event6.roll(), event6.pitch(), -event6.yaw());
	    t.set(-q.x(), -q.y(), -q.z());
	    t = scene.camera().frame().orientation().rotate(t);
	    t = transformOf(t, false);
	    q.x(t.x());
	    q.y(t.y());
	    q.z(t.z());
	    rotate(q);
			break;
		}
		
		default:
			break;
		}
	}

	public boolean isFlipped() {
		return ( scene.isRightHanded() && !isInverted() ) || ( scene.isLeftHanded() && isInverted() );
	}
	
	/**
	 * Returns a Quaternion computed according to the mouse motion. Mouse positions
	 * are projected on a deformed ball, centered on ({@code cx}, {@code cy}).
	 */
	protected Quat deformedBallQuaternion(DOF2Event event, float cx, float cy, Camera camera) {
		float x = event.getX();
		float y = event.getY();
		float prevX = event.getPrevX();
		float prevY = event.getPrevY();
		// Points on the deformed ball		
    float px = rotationSensitivity() *                         ((int)prevX - cx)                       / camera.screenWidth();
    float py = rotationSensitivity() * (scene.isLeftHanded() ? ((int)prevY - cy) : ( cy - (int)prevY)) / camera.screenHeight();
    float dx = rotationSensitivity() *                         (x - cx)             / camera.screenWidth();
    float dy = rotationSensitivity() * (scene.isLeftHanded() ? (y - cy) : (cy - y)) / camera.screenHeight();    

		Vec p1 = new Vec(px, py, projectOnBall(px, py));
		Vec p2 = new Vec(dx, dy, projectOnBall(dx, dy));
		// Approximation of rotation angle Should be divided by the projectOnBall size, but it is 1.0
		Vec axis = p2.cross(p1);
		float angle = 2.0f * (float) Math.asin((float) Math.sqrt(axis.squaredNorm() / p1.squaredNorm() / p2.squaredNorm()));			
		return new Quat(axis, angle);
	}
	
	protected final Quat iFrameQuaternion(Quat rot, Camera camera) {
		Vec trans = new Vec();		
		trans = rot.axis();
		trans = camera.frame().orientation().rotate(trans);
		trans = transformOf(trans);
		//trans = transformOfFrom(trans, camera.frame());
		
		Vec res = new Vec(trans);			
		// perform conversion			
		if (scaling().x() < 0 )	res.x(-trans.x());
		if (scaling().y() < 0 )	res.y(-trans.y());
		if (scaling().z() < 0 )	res.z(-trans.z());
		
		return new Quat(res, isInverted() ? rot.angle() : -rot.angle());						
	}	

	/**
	 * Returns "pseudo-distance" from (x,y) to ball of radius size. For a point
	 * inside the ball, it is proportional to the euclidean distance to the ball.
	 * For a point outside the ball, it is proportional to the inverse of this
	 * distance (tends to zero) on the ball, the function is continuous.
	 */
	protected static float projectOnBall(float x, float y) {
		// If you change the size value, change angle computation in deformedBallQuaternion().
		float size = 1.0f;
		float size2 = size * size;
		float size_limit = size2 * 0.5f;

		float d = x * x + y * y;
		return d < size_limit ? (float) Math.sqrt(size2 - d) : size_limit	/ (float) Math.sqrt(d);
	}
}
