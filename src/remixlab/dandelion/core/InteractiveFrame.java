/**
 *                     Dandelion (version 0.70.0)      
 *          Copyright (c) 2013 by Jean Pierre Charalambos
 *                 @author Jean Pierre Charalambos      
 *              https://github.com/nakednous/remixcam
 *                           
 * This library provides classes to ease the creation of interactive
 * frame-based, 2d and 3d scenes.
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
import remixlab.dandelion.geom.*;
import remixlab.dandelion.util.AbstractTimerJob;
import remixlab.tersehandling.core.Agent;
import remixlab.tersehandling.core.Copyable;
import remixlab.tersehandling.core.Grabbable;
import remixlab.tersehandling.generic.event.*;
import remixlab.tersehandling.generic.profile.Duoable;
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
		append(grabsInputThreshold).
		append(isInCamPath).
		append(isSpng).
		append(rotSensitivity).
		append(spngQuat).
		append(spngSensitivity).
		append(dampFriction).
		append(sFriction).
		append(transSensitivity).
		append(wheelSensitivity).
		append(drvSpd).
		append(flyDisp).
		append(flySpd).
		append(flyUpVec).
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
		.append(grabsInputThreshold, other.grabsInputThreshold)
		.append(isInCamPath, other.isInCamPath)
		.append(isSpng, other.isSpng)
		.append(dampFriction, other.dampFriction)
		.append(sFriction, other.sFriction)
		.append(rotSensitivity, other.rotSensitivity)
		.append(spngQuat,other.spngQuat)
		.append(spngSensitivity,other.spngSensitivity)
		.append(transSensitivity, other.transSensitivity)
		.append(wheelSensitivity, other.wheelSensitivity)	
		.append(drvSpd,other.drvSpd)
		.append(flyDisp,other.flyDisp)
		.append(flySpd,other.flySpd)
		.append(flyUpVec,other.flyUpVec)
		.isEquals();
	}
	
	private int grabsInputThreshold;
	private float rotSensitivity;
	private float transSensitivity;
	private float wheelSensitivity;
	
	// spinning stuff:
	protected float eventSpeed;
	private float spngSensitivity;
	
	//TODO: remove this flag?:
	private boolean isSpng;
	private AbstractTimerJob spinningTimerJob;
	private Orientable spngQuat;
	protected float dampFriction; //new
	//TODO decide whether or not toss should have its own damp var
	// currently its share among the two -> test behavior
	private float sFriction; //new

	// Whether the SCREEN_TRANS direction (horizontal or vertical) is fixed or not.
	public boolean dirIsFixed;
  private boolean horiz = true;// Two simultaneous InteractiveFrame require two mice!

	// MouseGrabber
	//public boolean keepsGrabbingCursor;
	//TODO define if this shpuld go
	//protected Constraint prevConstraint; // When manipulation is without Constraint.
	// Previous mouse position (used for incremental updates) and mouse press position.
	//protected Point prevPos, pressPos;

	protected boolean isInCamPath;
	
	// " D R I V A B L E "   S T U F F :
	protected Vec tDir;
	protected float flySpd;
	protected float drvSpd;
	protected AbstractTimerJob flyTimerJob;
	protected Vec flyUpVec;
	protected Vec flyDisp;
	protected static final long FLY_UPDATE_PERDIOD = 10;

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

		scene.terseHandler().addInAllAgentPools(this);
		isInCamPath = false;

		setGrabsInputThreshold(10);
		setRotationSensitivity(1.0f);
		setTranslationSensitivity(1.0f);
		setWheelSensitivity(20.0f);
		
		isSpng = false;
		setSpinningSensitivity(0.3f);
		setDampingFriction(0.5f);
		
		spinningTimerJob = new AbstractTimerJob() {
			public void execute() {
				spin();
			}
		};	
		scene.registerJob(spinningTimerJob);
		
		// Drivable stuff:
		drvSpd = 0.0f;
		flyUpVec = new Vec(0.0f, 1.0f, 0.0f);

		flyDisp = new Vec(0.0f, 0.0f, 0.0f);

		setFlySpeed(0.0f);

		flyTimerJob = new AbstractTimerJob() {
			public void execute() {
				toss();
			}
		};		
		scene.registerJob(flyTimerJob);
	}
	
	/**
	 * Copy constructor.
	 * 
	 * @param otherFrame the other interactive frame
	 */
	protected InteractiveFrame(InteractiveFrame otherFrame) {
		super(otherFrame);
		this.scene = otherFrame.scene;
		
		this.scene.terseHandler().addInAllAgentPools(this);
		this.isInCamPath = otherFrame.isInCamPath;
		
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

		this.setGrabsInputThreshold( otherFrame.grabsInputThreshold()  );
		this.setRotationSensitivity( otherFrame.rotationSensitivity() );
		this.setTranslationSensitivity( otherFrame.translationSensitivity() );
		this.setWheelSensitivity( otherFrame.wheelSensitivity() );

		//this.prevConstraint = otherFrame.prevConstraint; 
		
		this.isSpng = otherFrame.isSpng;
		this.setSpinningSensitivity( otherFrame.spinningSensitivity() );
		this.setDampingFriction( otherFrame.dampingFriction() );
		
		this.spinningTimerJob = new AbstractTimerJob() {
			public void execute() {
				spin();
			}
		};		
		this.scene.registerJob(spinningTimerJob);
		
		// Drivable stuff:
		this.drvSpd = otherFrame.drvSpd;
		this.flyUpVec = new Vec();
		this.flyUpVec.set(otherFrame.flyUpVector());
		this.flyDisp = new Vec();
		this.flyDisp.set(otherFrame.flyDisp);
		this.setFlySpeed( otherFrame.flySpeed() );
		
		this.flyTimerJob = new AbstractTimerJob() {
			public void execute() {
				toss();
			}
		};		
		this.scene.registerJob(flyTimerJob);
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

		scene.terseHandler().addInAllAgentPools(this);
		isInCamPath = true;

		setGrabsInputThreshold(10);
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
		setDampingFriction(0.5f);
				
		spinningTimerJob = new AbstractTimerJob() {
			public void execute() {
				spin();
			}
		};		
		scene.registerJob(spinningTimerJob);
		
		// Drivable stuff:
		drvSpd = 0.0f;
		flyUpVec = new Vec(0.0f, 1.0f, 0.0f);
		flyDisp = new Vec(0.0f, 0.0f, 0.0f);
		setFlySpeed(0.0f);
		flyTimerJob = new AbstractTimerJob() {
			public void execute() {
				toss();
			}
		};
		scene.registerJob(flyTimerJob);
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
	 * @see #setGrabsInputThreshold(int)
	 */
	public int grabsInputThreshold() {
		return grabsInputThreshold;
	}
	
	/**
	 * Sets the number of pixels that defined the {@link #checkIfGrabsDevice(int, int, Camera)}
	 * condition.
	 * 
	 * @param threshold number of pixels that defined the {@link #checkIfGrabsDevice(int, int, Camera)}
	 * condition. Default value is 10 pixels (which is set in the constructor). Negative values are
	 * silently ignored.
	 * 
	 * @see #grabsInputThreshold()
	 * @see #checkIfGrabsDevice(int, int, Camera)
	 */
	public void setGrabsInputThreshold( int threshold ) {
		if(threshold >= 0)
			grabsInputThreshold = threshold; 
	}

	/**
	 * Implementation of the MouseGrabber main method.
	 * <p>
	 * The InteractiveFrame {@link #grabsAgent()} when the mouse is within a {@link #grabsInputThreshold()}
	 * pixels region around its
	 * {@link remixlab.dandelion.core.Camera#projectedCoordinatesOf(Vec)}
	 * {@link #position()}.
	 */
	@Override
	public boolean checkIfGrabsInput(TerseEvent event) {
		int x=0, y=0;
		if(event instanceof DOF2Event) {
			//x = scene.cursorX - scene.upperLeftCorner.getX();
			//y = scene.cursorY - scene.upperLeftCorner.getY();
			x = (int)((DOF2Event)event).getX();
			y = (int)((DOF2Event)event).getY();
		}
		Vec proj = scene.pinhole().projectedCoordinatesOf(position());
		return ((Math.abs(x - proj.vec[0]) < grabsInputThreshold()) && (Math.abs(y - proj.vec[1]) < grabsInputThreshold()));
		//setGrabsInput(keepsGrabbingCursor || ((Math.abs(x - proj.vec[0]) < grabsDeviceThreshold()) && (Math.abs(y - proj.vec[1]) < grabsDeviceThreshold())));
	}

	/**
	 * Returns {@code true} when the MouseGrabber grabs the Scene's mouse events.
	 * <p>
	 * This flag is set with {@link #setGrabsInput(boolean)} by the
	 * {@link #checkIfGrabsDevice(int, int, Camera)} method.
	 */
	@Override
	public boolean grabsAgent(Agent agent) {
		return agent.grabber() == this;
	}

	/**
	 * Convenience wrapper function that simply returns {@code scene.isInMouseGrabberPool(this)}.
	 * 
	 * @see remixlab.dandelion.core.AbstractScene#isInDeviceGrabberPool(Grabbable)
	 */
	public boolean isInAgentPool(Agent agent) {
		return agent.isInPool(this);
	}

	/**
	 * Convenience wrapper function that simply calls {@code scene.addInMouseGrabberPool(this)}.
	 * 
	 * @see remixlab.dandelion.core.AbstractScene#addInDeviceGrabberPool(Grabbable)
	 */
	public void addInAgentPool(Agent agent) {
		agent.addInPool(this);
	}

	/**
	 * Convenience wrapper function that simply calls {@code scene.removeFromMouseGrabberPool(this)}.
	 * 
	 * @see remixlab.dandelion.core.AbstractScene#removeFromAgentPool(Grabbable)
	 */
	public void removeFromAgentPool(Agent agent) {
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
	 * <b>Attention: </b>Spinning may be decelerated according to {@link #dampingFriction()}
	 * till it stops completely.
	 * 
	 * @see #tossingDirection()
	 */
	public final Orientable spinningQuaternion() {
		return spngQuat;
	}
	
	public final Vec tossingDirection() {
		return tDir;
	}
	
	public final void setTossingDirection(Vec dir) {
		tDir = dir;
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
	
	public final void stopTossing() {
		this.flyTimerJob.stop();
	}
	
	/**
	 * Stops the spinning motion started using {@link #startSpinning(long)}.
	 * {@link #isSpinning()} will return {@code false} after this call.
	 * <p>
	 * <b>Attention: </b>This method may be called by {@link #spin()}, since spinning may
	 * be decelerated according to {@link #dampingFriction()} till it stops completely.
	 * 
	 * @see #dampingFriction()
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
	 * <b>Attention: </b>Spinning may be decelerated according to {@link #dampingFriction()}
	 * till it stops completely.
	 * 
	 * @see #dampingFriction()
	 * @see #toss()
	 */
	public void startSpinning(DOF2Event e) {
		eventSpeed = e.speed();
		isSpng = true;
		int updateInterval = (int) e.delay();
		if(updateInterval>0)
			spinningTimerJob.run(updateInterval);
	}
	
	public void startTossing(DOF2Event e) {
		eventSpeed = e.speed();
		flyTimerJob.run(FLY_UPDATE_PERDIOD);
	}
	
	/**
	 * Rotates the InteractiveFrame by its {@link #spinningQuaternion()}. Called
	 * by a timer when the InteractiveFrame {@link #isSpinning()}. 
	 * <p>
	 * <b>Attention: </b>Spinning may be decelerated according to
	 * {@link #dampingFriction()} till it stops completely.
	 * 
	 * @see #dampingFriction()
	 * @see #toss()
	 */
	public void spin() {		
		if(dampingFriction() > 0) {
			if (eventSpeed == 0) {
				stopSpinning();
				return;
			}
			rotate(spinningQuaternion());
			recomputeSpinningQuaternion();						
		}
		else
			rotate(spinningQuaternion());
	}
	
	public void toss() {		
		if(dampingFriction() > 0) {
			if (eventSpeed == 0) {
				stopTossing();
				return;
			}
			translate(tossingDirection());
			recomputeTossingDirection();						
		}
		else
			translate(tossingDirection());
	}
	
	/**
	 * Defines the {@link #dampingFriction()}. Values must be
	 * in the range [0..1].
	 */
	public void setDampingFriction(float f) {
		if(f < 0 || f > 1)
			return;
		dampFriction = f;
		setDampingFrictionFx(dampFriction);
	} 
	
	/**
	 * Defines the spinning deceleration.
	 * <p>
	 * Default value is 0.0, i.e., no spinning deceleration. Use
	 * {@link #setDampingFriction(float)} to tune this value.
	 * A higher value will make spinning more difficult (a value of
	 * 1.0 forbids spinning).
	 * 
	 * @see #tossingFriction()
	 */
	public float dampingFriction() {
		return dampFriction;
	}
	
	/**
	 * Internal use.
	 * <p>
	 * Computes and caches the value of the spinning friction used in
	 * {@link #recomputeSpinningQuaternion()}.
	 */
	protected void setDampingFrictionFx(float spinningFriction) {
		sFriction = spinningFriction*spinningFriction*spinningFriction;
	}
	
	/**
	 * Internal use.
	 * <p>
	 * Returns the cached value of the spinning friction used in
	 * {@link #recomputeSpinningQuaternion()}.
	 */
	protected float dampingFrictionFx() {
		return sFriction;
	}
	
	/**
	 * Internal method. Recomputes the {@link #spinningQuaternion()}
	 * according to {@link #dampingFriction()}.
	 * 
	 * @see #recomputeTossingDirection()
	 */
	protected void recomputeSpinningQuaternion() {
		float prevSpeed = eventSpeed;
		float damping = 1.0f - dampingFrictionFx();
		eventSpeed *= damping;
		if (Math.abs(eventSpeed) < .001f)
			eventSpeed = 0;
		//float currSpeed = eventSpeed;
		if( scene.is3D() )
			((Quat)spinningQuaternion()).fromAxisAngle(((Quat)spinningQuaternion()).axis(), spinningQuaternion().angle() * (eventSpeed / prevSpeed) );
		else
			this.setSpinningQuaternion(new Rotation(spinningQuaternion().angle() * (eventSpeed / prevSpeed)));
	}
	
	protected void recomputeTossingDirection() {
		float prevSpeed = eventSpeed;
		float damping = 1.0f - dampingFrictionFx();
		eventSpeed *= damping;
		if (Math.abs(eventSpeed) < .001f)
			eventSpeed = 0;
		
		flyDisp.z(flyDisp.z() * (eventSpeed / prevSpeed));
		
		if(scene.is2D())
			setTossingDirection(localInverseTransformOf(flyDisp));
		else
			setTossingDirection(rotation().rotate(flyDisp));
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
	public void performInteraction(TerseEvent e) {
		stopSpinning();
		stopTossing();
		
		if(e == null) return;
		
		if(e instanceof KeyboardEvent || e instanceof ClickEvent) {
			scene.performInteraction(e);
			return;
		}
		
		// then it's a MotionEvent		
		Duoable<?> event;
		
		if(e instanceof Duoable)
			event = (Duoable<?>)e;
		else 
			return;	
		
		// same as no action
		if( event.action() == null )
			return;
		
		if( ( scene.is2D() ) && ( ((DandelionAction)event.action().referenceAction()).is2D() ) )
			execAction2D( reduceEvent( (MotionEvent)e ));
		else
			if(scene.is3D())
				execAction3D( reduceEvent( (MotionEvent)e ));
	}
	
	MotionEvent currentEvent;
	DOF1Event e1;
	DOF2Event e2;
	DOF3Event e3;
	DOF6Event e6;
	DandelionAction currentAction;
	
	protected DandelionAction reduceEvent(MotionEvent e) {
		currentEvent = e;
		if( !(e instanceof Duoable) )	return null;
		
		currentAction = (DandelionAction) ((Duoable<?>) e).action().referenceAction();		
		if( currentAction == null ) return null;

		int dofs = currentAction.dofs();
		
		switch(dofs) {
		case 1:
			if( e instanceof DOF1Event )
				e1 = (DOF1Event) e.get();
			else if( e instanceof DOF2Event )
				e1 = ((DOF2Event)e).dof1Event(false);
			else if( e instanceof DOF3Event )
				e1 = ((DOF3Event)e).dof2Event().dof1Event(false);
			else if( e instanceof DOF6Event )
				e1 = ((DOF6Event)e).dof3Event().dof2Event().dof1Event(false);
			break;
		case 2:
			if( e instanceof DOF2Event )
				e2 = ((DOF2Event)e).get();
			else if( e instanceof DOF3Event )
				e2 = ((DOF3Event)e).dof2Event();
			else if( e instanceof DOF6Event )
				e2 = ((DOF6Event)e).dof3Event().dof2Event();
			break;
		case 3:
			if( e instanceof DOF3Event )
				e3 = ((DOF3Event)e).get();
			else if( e instanceof DOF3Event )
				e3 = ((DOF6Event)e).dof3Event();
			break;
		case 6:
			if( e instanceof DOF6Event )
			  e6 = ((DOF6Event)e).get();
			break;
		default:
		  break;
		}
		return currentAction;		
	}
	
	//TODO implement me
	protected void execAction2D(DandelionAction a) {
		if(a==null) return;
		switch(a) {
		case CUSTOM:
			break;
		case DRIVE:
			break;
		case LOOK_AROUND:
			break;
		case MOVE_BACKWARD:
			break;
		case MOVE_FORWARD:
			break;
		//TODO: seems only one DRIVABLE action having 2d version
			// it calls spinning -> no flyUpdate is needed
		case ROLL:
			break;
		case ROTATE:
			break;
		case ROTATE3:
			break;
		case SCREEN_ROTATE:
			break;
		case SCREEN_TRANSLATE:
			break;
		case TRANSLATE:
			break;
		case TRANSLATE3:
			break;
		case TRANSLATE_ROTATE:
			break;
		case ZOOM:
			float delta;
			if( e1.absolute() )
			  delta = e1.getX();
			else
				delta = e1.getDX();
			if(delta >= 0)
				scale(1 + Math.abs(delta) / (float) scene.height());
			else
				inverseScale(1 + Math.abs(delta) / (float) scene.height());
			break;
		case ZOOM_ON_REGION:
			break;
		default:
			break;
		}
	}
	
	protected void execAction3D(DandelionAction a) {
		if(a==null) return;
		Quat q, rot;
		Vec trans;
		Vec t;
		float angle;
		switch(a) {
		case CUSTOM:
			AbstractScene.showMissingImplementationWarning(a);
			break;
		case DRIVE:
			rotate(turnQuaternion(e2, scene.camera()));
			if( e2.absolute() )
				drvSpd = 0.01f * -e2.getY();
			else
				drvSpd = 0.01f * -e2.getDY();
			flyDisp.set(0.0f, 0.0f, flySpeed() * drvSpd);
			if(scene.is2D())
				trans = localInverseTransformOf(flyDisp);
			else
				trans = rotation().rotate(flyDisp);			
			setTossingDirection(trans);
			startTossing(e2);
			break;
		case LOOK_AROUND:
			rotate(pitchYawQuaternion(e2, scene.camera()));
			break;
		case MOVE_BACKWARD:
			rotate(pitchYawQuaternion(e2, scene.camera()));
			flyDisp.set(0.0f, 0.0f, flySpeed());
			if(scene.is2D())
				trans = localInverseTransformOf(flyDisp);
			else
				trans = rotation().rotate(flyDisp);			
			setTossingDirection(trans);
			startTossing(e2);
			break;
		case MOVE_FORWARD:
			rotate(pitchYawQuaternion(e2, scene.camera()));
			flyDisp.set(0.0f, 0.0f, -flySpeed());
			if(scene.is2D())
				trans = localInverseTransformOf(flyDisp);
			else
				trans = rotation().rotate(flyDisp);			
			setTossingDirection(trans);
			startTossing(e2);
			break;
		case ROLL:
			if( e2.absolute() )
				angle = (float) Math.PI * e2.getX()	/ scene.camera().screenWidth();
			else
        angle = (float) Math.PI * e2.getDX()/ scene.camera().screenWidth();
			
		  //lef-handed coordinate system correction
			if ( scene.isLeftHanded() )
				angle = -angle;
			
			q = new Quat(new Vec(0.0f, 0.0f, 1.0f), angle);
			rotate(q);
			setSpinningQuaternion(q);
			updateFlyUpVector();
			break;
		case ROTATE:
			if(e2.absolute()) {
				AbstractScene.showEventVariationWarning(a);
				break;
			}
			trans = scene.camera().projectedCoordinatesOf(position());
			rot = deformedBallQuaternion(e2, trans.x(), trans.y(), scene.camera());
			rot = iFrameQuaternion(rot, scene.camera());			
			setSpinningQuaternion(rot);
			startSpinning(e2);
			break;		
		case ROTATE3:
			q = new Quat();
			t = scene.camera().projectedCoordinatesOf(position());
	    if(e3.absolute())
	    	q.fromEulerAngles(e3.getX(), e3.getY(), -e3.getZ());
	    else
	    	q.fromEulerAngles(e3.getDX(), e3.getDY(), -e3.getDZ());
	    t.set(-q.x(), -q.y(), -q.z());
	    t = scene.camera().frame().orientation().rotate(t);
	    t = transformOf(t, false);
	    q.x(t.x());
	    q.y(t.y());
	    q.z(t.z());
	    rotate(q);
			break;
		case SCREEN_ROTATE:
			if(e2.absolute()) {
				AbstractScene.showEventVariationWarning(a);
				break;
			}
			trans = scene.camera().projectedCoordinatesOf(position());
			float prev_angle = (float) Math.atan2(e2.getPrevY() - trans.vec[1], e2.getPrevX() - trans.vec[0]);
			angle = (float) Math.atan2(e2.getY() - trans.vec[1], e2.getX() - trans.vec[0]);			
			Vec axis = transformOf(scene.camera().frame().inverseTransformOf(new Vec(0.0f, 0.0f, -1.0f)));			
			//TODO testing handed
			if( scene.isRightHanded() )
				rot = new Quat(axis, angle - prev_angle);
			else
				rot = new Quat(axis, prev_angle - angle);
			setSpinningQuaternion(rot);
			startSpinning(e2);
			break;
		case SCREEN_TRANSLATE:
		  // TODO: needs testing to see if it works correctly when left-handed is set
			int dir = originalDirection(e2);
			trans = new Vec();
			if (dir == 1)
				if( e2.absolute() )
					trans.set(e2.getX(), 0.0f, 0.0f);
				else
					trans.set(e2.getDX(), 0.0f, 0.0f);
			else if (dir == -1)
				if( e2.absolute() )
					trans.set(0.0f, e2.getY(), 0.0f);
				else
					trans.set(0.0f, e2.getDY(), 0.0f);	
			switch ( scene.camera().type() ) {
			case PERSPECTIVE:
				trans.mult(2.0f * (float) Math.tan(scene.camera().fieldOfView() / 2.0f)
						            * Math.abs((scene.camera().frame().coordinatesOf(position())).vec[2] * scene.camera().frame().magnitude().z())
						            //* Math.abs((camera.frame().coordinatesOf(position())).vec[2])						            
						            / scene.camera().screenHeight());
				break;
			case ORTHOGRAPHIC:
				float[] wh = scene.camera().getOrthoWidthHeight();
				trans.vec[0] *= 2.0 * wh[0] / scene.camera().screenWidth();
				trans.vec[1] *= 2.0 * wh[1] / scene.camera().screenHeight();
				break;
			}
			trans = scene.camera().frame().orientation().rotate(Vec.mult(trans, translationSensitivity()));
			if (referenceFrame() != null)
				trans = referenceFrame().transformOf(trans);
			translate(trans);
			break;
		case TRANSLATE:
			if(e2.relative())
			  trans = new Vec(e2.getDX(), scene.isRightHanded() ? -e2.getDY() : e2.getDY(), 0.0f);
			else
				trans = new Vec(e2.getX(), scene.isRightHanded() ? -e2.getY() : e2.getY(), 0.0f);
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
		case TRANSLATE3:
			t = new Vec();
      // A. Translate the iFrame
	    // Transform to world coordinate system
	    if(e3.absolute())
	    	t = scene.camera().frame().inverseTransformOf(new Vec(e3.getX(),e3.getY(),-e3.getZ()), false); //same as: t = cameraFrame.orientation().rotate(new PVector(tx,ty,-tz));
	    else
	    	t = scene.camera().frame().inverseTransformOf(new Vec(e3.getDX(),e3.getDY(),-e3.getDZ()), false); //same as: t = cameraFrame.orientation().rotate(new PVector(tx,ty,-tz));
	    // And then down to frame
	    if (referenceFrame() != null)
	    	t = referenceFrame().transformOf(t, false);
	    translate(t);
			break;
		case TRANSLATE_ROTATE:
			t = new Vec();
	    q = new Quat();
      // A. Translate the iFrame
	    // Transform to world coordinate system
	    if(e6.absolute())
	    	t = scene.camera().frame().inverseTransformOf(new Vec(e6.getX(),e6.getY(),-e6.getZ()), false); //same as: t = cameraFrame.orientation().rotate(new PVector(tx,ty,-tz));
	    else
	    	t = scene.camera().frame().inverseTransformOf(new Vec(e6.getDX(),e6.getDY(),-e6.getDZ()), false); //same as: t = cameraFrame.orientation().rotate(new PVector(tx,ty,-tz));
	    // And then down to frame
	    if (referenceFrame() != null)
	    	t = referenceFrame().transformOf(t, false);
	    translate(t);
	    // B. Rotate the iFrame
	    t = scene.camera().projectedCoordinatesOf(position());
	    if(e6.absolute())
	    	q.fromEulerAngles(e6.roll(), e6.pitch(), -e6.yaw());
	    else
	    	q.fromEulerAngles(e6.getDRX(), e6.getDRY(), -e6.getDRZ());
	    t.set(-q.x(), -q.y(), -q.z());
	    t = scene.camera().frame().orientation().rotate(t);
	    t = transformOf(t, false);
	    q.x(t.x());
	    q.y(t.y());
	    q.z(t.z());
	    rotate(q);
			break;
		case ZOOM:
			float delta;
			if( currentEvent instanceof GenericDOF1Event ) //its a wheel wheel :P
				delta = -e1.getX() * wheelSensitivity();
			else
				if( e1.absolute() )
				  delta = e1.getX();
				else
					delta = e1.getDX();	
			if(delta >= 0)
				scale(1 + Math.abs(delta) / (float) scene.height());
			else
				inverseScale(1 + Math.abs(delta) / (float) scene.height());
			break;
		default:
			AbstractScene.showMissingImplementationWarning(a);
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
		//TODO absolute events!?
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
	
	/**
	 * Returns the fly speed, expressed in processing scene units.
	 * <p>
	 * It corresponds to the incremental displacement that is periodically applied
	 * to the InteractiveDrivableFrame position when a
	 * {@link remixlab.DOF6Action.action.DOF_6Action#MOVE_FORWARD} or
	 * {@link remixlab.DOF6Action.action.DOF_6Action#MOVE_BACKWARD} Scene.MouseAction is proceeded.
	 * <p>
	 * <b>Attention:</b> When the InteractiveDrivableFrame is set as the
	 * {@link remixlab.dandelion.core.Camera#frame()} (which indeed is an instance of
	 * the InteractiveCameraFrame class) or when it is set as the
	 * {@link remixlab.dandelion.core.AbstractScene#avatar()} (which indeed is an instance of
	 * the InteractiveAvatarFrame class), this value is set according to the
	 * {@link remixlab.dandelion.core.AbstractScene#radius()} by
	 * {@link remixlab.dandelion.core.AbstractScene#setRadius(float)}.
	 */
	public float flySpeed() {
		return flySpd;
	}

	/**
	 * Sets the {@link #flySpeed()}, defined in processing scene units.
	 * <p>
	 * Default value is 0.0, but it is modified according to the
	 * {@link remixlab.dandelion.core.AbstractScene#radius()} when the InteractiveDrivableFrame
	 * is set as the {@link remixlab.dandelion.core.Camera#frame()} (which indeed is an
	 * instance of the InteractiveCameraFrame class) or when the
	 * InteractiveDrivableFrame is set as the
	 * {@link remixlab.dandelion.core.AbstractScene#avatar()} (which indeed is an instance of
	 * the InteractiveAvatarFrame class).
	 */
	public void setFlySpeed(float speed) {
		flySpd = speed;
	}

	/**
	 * Returns the up vector used in fly mode, expressed in the world coordinate
	 * system.
	 * <p>
	 * Fly mode corresponds to the
	 * {@link remixlab.DOF6Action.action.DOF_6Action#MOVE_FORWARD} and
	 * {@link remixlab.DOF6Action.action.DOF_6Action#MOVE_BACKWARD} Scene.MouseAction
	 * bindings. In these modes, horizontal displacements of the mouse rotate the
	 * InteractiveDrivableFrame around this vector. Vertical displacements rotate
	 * always around the frame {@code X} axis.
	 * <p>
	 * Default value is (0,1,0), but it is updated by the Camera when set as its
	 * {@link remixlab.dandelion.core.Camera#frame()}.
	 * {@link remixlab.dandelion.core.Camera#setOrientation(Quat)} and
	 * {@link remixlab.dandelion.core.Camera#setUpVector(Vec)} modify this value and
	 * should be used instead.
	 */
	public Vec flyUpVector() {
		return flyUpVec;
	}

	/**
	 * Sets the {@link #flyUpVector()}, defined in the world coordinate system.
	 * <p>
	 * Default value is (0,1,0), but it is updated by the Camera when set as its
	 * {@link remixlab.dandelion.core.Camera#frame()}. Use
	 * {@link remixlab.dandelion.core.Camera#setUpVector(Vec)} instead in that case.
	 */
	public void setFlyUpVector(Vec up) {
		flyUpVec = up;
	}

	/**
	 * This method will be called by the Camera when its orientation is changed,
	 * so that the {@link #flyUpVector()} (private) is changed accordingly. You
	 * should not need to call this method.
	 */
	public final void updateFlyUpVector() {
		//flyUpVec = inverseTransformOf(new Vector3D(0.0f, 1.0f, 0.0f));
		flyUpVec = inverseTransformOf(new Vec(0.0f, 1.0f, 0.0f), false);
	}

	/**
	 * Returns a Quaternion that is a rotation around current camera Y,
	 * proportional to the horizontal mouse position.
	 */
	protected final Quat turnQuaternion(DOF2Event event, Camera camera) {
		float deltaX = event.absolute() ? event.getX() : event.getDX();
		return new Quat(new Vec(0.0f, 1.0f, 0.0f), rotationSensitivity()	* (-deltaX) / camera.screenWidth());
	}

	/**
	 * Returns a Quaternion that is the composition of two rotations, inferred
	 * from the mouse pitch (X axis) and yaw ({@link #flyUpVector()} axis).
	 */
	protected final Quat pitchYawQuaternion(DOF2Event event, Camera camera) {
		float deltaX = event.absolute() ? event.getX() : event.getDX();
		float deltaY = event.absolute() ? event.getY() : event.getDY();
			
		if( scene.isRightHanded() )
			deltaY = -deltaY;
		
		Quat rotX = new Quat(new Vec(1.0f, 0.0f, 0.0f), rotationSensitivity() * deltaY / camera.screenHeight());
		//Quaternion rotY = new Quaternion(transformOf(flyUpVector()), rotationSensitivity() * ((int)prevPos.x - x) / camera.screenWidth());	
		Quat rotY = new Quat(transformOf(flyUpVector(), false), rotationSensitivity() * (-deltaX) / camera.screenWidth());
		return Quat.multiply(rotY, rotX);
	}
	
	/**
	 * Return 1 if mouse motion was started horizontally and -1 if it was more
	 * vertical. Returns 0 if this could not be determined yet (perfect diagonal
	 * motion, rare).
	 */
	protected int originalDirection(DOF2Event event) {
		if (!dirIsFixed) {
			Point delta;
			if( event.absolute() )
				delta = new Point(event.getX(), event.getY());
			else
				delta = new Point(event.getDX(), event.getDY());
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
}
