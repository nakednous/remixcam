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

import remixlab.remixcam.event.*;
import remixlab.remixcam.geom.*;
import remixlab.remixcam.util.AbstractTimerJob;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

/**
 * The InteractiveDrivableFrame represents an InteractiveFrame that can "fly" in
 * the scene. It is the base class of all objects that are drivable in the
 * Scene: InteractiveAvatarFrame and InteractiveCameraFrame.
 * <p>
 * An InteractiveDrivableFrame basically moves forward, and turns according to
 * the mouse motion. See {@link #flySpeed()}, {@link #flyUpVector()} and the
 * {@link remixlab.remixcam.action.DOF_6Action#MOVE_FORWARD} and
 * {@link remixlab.remixcam.action.DOF_6Action#MOVE_BACKWARD}.
 */
public class InteractiveDrivableFrame extends InteractiveFrame implements Copyable {	
	@Override
	public int hashCode() {
    return new HashCodeBuilder(17, 37).
    appendSuper(super.hashCode()).    
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
		
		InteractiveDrivableFrame other = (InteractiveDrivableFrame) obj;
	  return new EqualsBuilder()
    .appendSuper(super.equals(obj))		
		.append(drvSpd,other.drvSpd)
		.append(flyDisp,other.flyDisp)
		.append(flySpd,other.flySpd)
		.append(flyUpVec,other.flyUpVec)
		.isEquals();
	}
	
	protected float flySpd;
	protected float drvSpd;
	protected AbstractTimerJob flyTimerJob;
	protected DLVector flyUpVec;
	protected DLVector flyDisp;
	protected static final long FLY_UPDATE_PERDIOD = 10;

	/**
	 * Default constructor.
	 * <p>
	 * {@link #flySpeed()} is set to 0.0 and {@link #flyUpVector()} is (0,1,0).
	 */
	public InteractiveDrivableFrame(AbstractScene scn) {
		super(scn);
		drvSpd = 0.0f;
		flyUpVec = new DLVector(0.0f, 1.0f, 0.0f);

		flyDisp = new DLVector(0.0f, 0.0f, 0.0f);

		setFlySpeed(0.0f);

		flyTimerJob = new AbstractTimerJob() {
			public void execute() {
				flyUpdate();
			}
		};		
		scene.registerJob(flyTimerJob);    
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param otherFrame the other interactive drivable frame
	 */
	protected InteractiveDrivableFrame(InteractiveDrivableFrame otherFrame) {		
		super(otherFrame);
		this.drvSpd = otherFrame.drvSpd;
		this.flyUpVec = new DLVector();
		this.flyUpVec.set(otherFrame.flyUpVector());
		this.flyDisp = new DLVector();
		this.flyDisp.set(otherFrame.flyDisp);
		this.setFlySpeed( otherFrame.flySpeed() );
		
		this.flyTimerJob = new AbstractTimerJob() {
			public void execute() {
				flyUpdate();
			}
		};		
		scene.registerJob(flyTimerJob);
	}
	
	/**
	 * Calls {@link #InteractiveDrivableFrame(InteractiveDrivableFrame)} (which is protected)
	 * and returns a copy of {@code this} object.
	 * 
	 * @see #InteractiveDrivableFrame(InteractiveDrivableFrame)
	 */
	public InteractiveDrivableFrame get() {
		return new InteractiveDrivableFrame(this);
	}

	/**
	 * Returns the fly speed, expressed in processing scene units.
	 * <p>
	 * It corresponds to the incremental displacement that is periodically applied
	 * to the InteractiveDrivableFrame position when a
	 * {@link remixlab.remixcam.action.DOF_6Action#MOVE_FORWARD} or
	 * {@link remixlab.remixcam.action.DOF_6Action#MOVE_BACKWARD} Scene.MouseAction is proceeded.
	 * <p>
	 * <b>Attention:</b> When the InteractiveDrivableFrame is set as the
	 * {@link remixlab.remixcam.core.Camera#frame()} (which indeed is an instance of
	 * the InteractiveCameraFrame class) or when it is set as the
	 * {@link remixlab.remixcam.core.AbstractScene#avatar()} (which indeed is an instance of
	 * the InteractiveAvatarFrame class), this value is set according to the
	 * {@link remixlab.remixcam.core.AbstractScene#radius()} by
	 * {@link remixlab.remixcam.core.AbstractScene#setRadius(float)}.
	 */
	public float flySpeed() {
		return flySpd;
	}

	/**
	 * Sets the {@link #flySpeed()}, defined in processing scene units.
	 * <p>
	 * Default value is 0.0, but it is modified according to the
	 * {@link remixlab.remixcam.core.AbstractScene#radius()} when the InteractiveDrivableFrame
	 * is set as the {@link remixlab.remixcam.core.Camera#frame()} (which indeed is an
	 * instance of the InteractiveCameraFrame class) or when the
	 * InteractiveDrivableFrame is set as the
	 * {@link remixlab.remixcam.core.AbstractScene#avatar()} (which indeed is an instance of
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
	 * {@link remixlab.remixcam.action.DOF_6Action#MOVE_FORWARD} and
	 * {@link remixlab.remixcam.action.DOF_6Action#MOVE_BACKWARD} Scene.MouseAction
	 * bindings. In these modes, horizontal displacements of the mouse rotate the
	 * InteractiveDrivableFrame around this vector. Vertical displacements rotate
	 * always around the frame {@code X} axis.
	 * <p>
	 * Default value is (0,1,0), but it is updated by the Camera when set as its
	 * {@link remixlab.remixcam.core.Camera#frame()}.
	 * {@link remixlab.remixcam.core.Camera#setOrientation(Quaternion)} and
	 * {@link remixlab.remixcam.core.Camera#setUpVector(DLVector)} modify this value and
	 * should be used instead.
	 */
	public DLVector flyUpVector() {
		return flyUpVec;
	}

	/**
	 * Sets the {@link #flyUpVector()}, defined in the world coordinate system.
	 * <p>
	 * Default value is (0,1,0), but it is updated by the Camera when set as its
	 * {@link remixlab.remixcam.core.Camera#frame()}. Use
	 * {@link remixlab.remixcam.core.Camera#setUpVector(DLVector)} instead in that case.
	 */
	public void setFlyUpVector(DLVector up) {
		flyUpVec = up;
	}

	/**
	 * Called for continuous frame motion in first person mode (see
	 * {@link remixlab.remixcam.action.DOF_6Action#MOVE_FORWARD}).
	 */
	public void flyUpdate() {
		if( ( scene.is2D() ) && ( !action.is2D() ) )
			return;
		
		flyDisp.set(0.0f, 0.0f, 0.0f);
		DLVector trans;
		switch (action) {
		case MOVE_FORWARD:
			flyDisp.vec[2] = -flySpeed();
			if(scene.is2D())
				trans = localInverseTransformOf(flyDisp);
			else
				trans = rotation().rotate(flyDisp);
			translate(trans);
			setTossingDirection(trans);
			break;
		case MOVE_BACKWARD:
			flyDisp.vec[2] = flySpeed();
			if(scene.is2D())
				trans = localInverseTransformOf(flyDisp);
			else
				trans = rotation().rotate(flyDisp);
			translate(trans);
			setTossingDirection(trans);
			break;
		case DRIVE:
			flyDisp.vec[2] = flySpeed() * drvSpd;
			if(scene.is2D())
				trans = localInverseTransformOf(flyDisp);
			else
				trans = rotation().rotate(flyDisp);
			translate(trans);
			setTossingDirection(trans);
			break;
		default:
			break;
		}
	}
	
	/**
	 * Protected internal method used to handle mouse actions.
	 */
	public void beginAction(DOF_6Action a) {
		super.beginAction(a);
		switch (action) {
		case MOVE_FORWARD:
		case MOVE_BACKWARD:
		case DRIVE:
			deviceSpeed = 0.0f;
			stopTossing();
			flyTimerJob.run(FLY_UPDATE_PERDIOD);
			break;
		default:
			break;
		}
	}
	
	@Override	
	protected void execAction2D(Point eventPoint, ViewWindow viewWindow) {
		//TODO implement 2d actions
	}
	
	/**
	 * Overloading of
	 * {@link remixlab.remixcam.core.InteractiveFrame#execAction(Point, Pinhole)}.
	 * <p>
	 * Motion depends on mouse binding. The resulting displacements are basically
	 * the same of those of an InteractiveFrame, but moving forward and backward
	 * and turning actions are implemented.
	 */
	@Override
	protected void execAction3D(Point eventPoint, Camera camera) {		
		if ((action == DOF_6Action.TRANSLATE)
				|| (action == DOF_6Action.ZOOM)
				|| (action == DOF_6Action.SCREEN_ROTATE)
				|| (action == DOF_6Action.SCREEN_TRANSLATE)
				|| (action == DOF_6Action.ROTATE)
				|| (action == DOF_6Action.NO_ACTION))
			super.execAction3D(eventPoint, camera);
		else {
			int deltaY = (int) (eventPoint.y - prevPos.y);//as it were LH
			if( scene.isRightHanded() )
				deltaY = -deltaY;
			
			switch (action) {
			case MOVE_FORWARD: {
			  //TODO 1-DOF -> wheel
				//translate(inverseTransformOf(new Vector3D(0.0f, 0.0f, 0.2f * flySpeed() * (-rotation))));
				
				Quaternion rot = pitchYawQuaternion((int)eventPoint.x, (int)eventPoint.y, camera);
				rotate(rot);
				// #CONNECTION# wheelEvent MOVE_FORWARD case
				// actual translation is made in flyUpdate().
				// translate(inverseTransformOf(Vec(0.0, 0.0, -flySpeed())));
				prevPos = eventPoint;
				break;
			}

			case MOVE_BACKWARD: {
			  //TODO 1-DOF -> wheel
				//translate(inverseTransformOf(new Vector3D(0.0f, 0.0f, 0.2f * flySpeed() * (-rotation))));
				
				Quaternion rot = pitchYawQuaternion((int)eventPoint.x, (int)eventPoint.y, camera);
				rotate(rot);
				// actual translation is made in flyUpdate().
				// translate(inverseTransformOf(Vec(0.0, 0.0, flySpeed())));
				prevPos = eventPoint;
				break;
			}

			case DRIVE: {				
				Quaternion rot = turnQuaternion((int)eventPoint.x, camera);
				rotate(rot);
				// actual translation is made in flyUpdate().
				drvSpd = 0.01f * -deltaY;
				prevPos = eventPoint;
				break;
			}

			case LOOK_AROUND: {
				Quaternion rot = pitchYawQuaternion((int)eventPoint.x, (int)eventPoint.y, camera);
				rotate(rot);
				prevPos = eventPoint;
				break;
			}

			case ROLL: {
				float angle = (float) Math.PI * ((int)eventPoint.x - (int)prevPos.x)	/ camera.screenWidth();
				
			  //lef-handed coordinate system correction
				if ( scene.isLeftHanded() )
					angle = -angle;
				
				Quaternion rot = new Quaternion(new DLVector(0.0f, 0.0f, 1.0f), angle);
				rotate(rot);
				setSpinningQuaternion(rot);
				updateFlyUpVector();
				prevPos = eventPoint;
				break;
			}

			case ZOOM_ON_REGION:
				break;

			default:
				prevPos = eventPoint;
				break;
			}
		}
	}	

	/**
	 * Overloading of
	 * {@link remixlab.remixcam.core.InteractiveFrame#endAction(Point, Camera)}.
	 */
	@Override
	public void endInteraction(Point eventPoint) {
		if( ( scene.is2D() ) && ( !action.is2D() ) )
			return;
		
		if ((action == DOF_6Action.MOVE_FORWARD)
				|| (action == DOF_6Action.MOVE_BACKWARD)
				|| (action == DOF_6Action.DRIVE)) {
			flyTimerJob.stop();
		}
		
		if (((action == DOF_6Action.MOVE_FORWARD) || (action == DOF_6Action.MOVE_BACKWARD) || (action == DOF_6Action.DRIVE) ) && (deviceSpeed >= tossingSensitivity()) )
			startTossing(FLY_UPDATE_PERDIOD);

		super.endInteraction(eventPoint);
	}
	
	/**
	 * Overloading of
	 * {@link remixlab.remixcam.core.InteractiveFrame#wheelMoved(int, Camera)}.
	 * <p>
	 * The wheel behavior depends on the wheel binded action. Current possible
	 * actions are {@link remixlab.remixcam.core.DLDeviceAction#ZOOM},
	 * {@link remixlab.remixcam.core.DLDeviceAction#MOVE_FORWARD} and
	 * {@link remixlab.remixcam.core.DLDeviceAction#MOVE_BACKWARD}.
	 * {@link remixlab.remixcam.core.DLDeviceAction#ZOOM} speed depends on
	 * #wheelSensitivity() the other two depend on #flySpeed().
	 */
	/**
	@Override
	public void wheelMoved(float rotation) {
		Pinhole vp = scene.pinhole();
		if( ( scene.is2D() ) && ( !action.is2D() ) )
			return;
		
		switch (action) {
		case ZOOM: {
			if( scene.is3D() ) {
			float wheelSensitivityCoef = 8E-4f;
			
			Vector3D trans = new Vector3D(0.0f, 0.0f, -rotation * wheelSensitivity() * wheelSensitivityCoef * (Vector3D.sub(((Camera) vp).position(), position())).mag());
						
			// #CONNECTION# Cut-pasted from the mouseMoveEvent ZOOM case
			trans = vp.frame().orientation().rotate(trans);
			if (referenceFrame() != null)
				trans = referenceFrame().transformOf(trans);
			translate(trans);
			}
			else {
			//TODO implement 2D case
			}
			break;
		}
		case MOVE_FORWARD:
		case MOVE_BACKWARD:
			// #CONNECTION# mouseMoveEvent() MOVE_FORWARD case
			translate(inverseTransformOf(new Vector3D(0.0f, 0.0f, 0.2f * flySpeed() * (-rotation))));
			break;
		default:
			break;
		}

		// #CONNECTION# startAction should always be called before
		//if (prevConstraint != null)
			//setConstraint(prevConstraint);

		int finalDrawAfterWheelEventDelay = 400;
		
	  // Starts (or prolungates) the timer.
		flyTimerJob.runOnce(finalDrawAfterWheelEventDelay);

		action = DLDeviceAction.NO_ACTION;
	}
	*/

	/**
	 * This method will be called by the Camera when its orientation is changed,
	 * so that the {@link #flyUpVector()} (private) is changed accordingly. You
	 * should not need to call this method.
	 */
	public final void updateFlyUpVector() {
		//flyUpVec = inverseTransformOf(new Vector3D(0.0f, 1.0f, 0.0f));
		flyUpVec = inverseTransformOf(new DLVector(0.0f, 1.0f, 0.0f), false);
	}

	/**
	 * Returns a Quaternion that is a rotation around current camera Y,
	 * proportional to the horizontal mouse position.
	 */
	protected final Quaternion turnQuaternion(int x, Camera camera) {
		return new Quaternion(new DLVector(0.0f, 1.0f, 0.0f), rotationSensitivity()	* ((int)prevPos.x - x) / camera.screenWidth());
	}

	/**
	 * Returns a Quaternion that is the composition of two rotations, inferred
	 * from the mouse pitch (X axis) and yaw ({@link #flyUpVector()} axis).
	 */
	protected final Quaternion pitchYawQuaternion(int x, int y, Camera camera) {
		int deltaY;
		if( scene.isRightHanded() )
			deltaY = (int) (prevPos.y - y);
		else
			deltaY = (int) (y - prevPos.y);
		
		Quaternion rotX = new Quaternion(new DLVector(1.0f, 0.0f, 0.0f), rotationSensitivity() * deltaY / camera.screenHeight());
		//Quaternion rotY = new Quaternion(transformOf(flyUpVector()), rotationSensitivity() * ((int)prevPos.x - x) / camera.screenWidth());	
		Quaternion rotY = new Quaternion(transformOf(flyUpVector(), false), rotationSensitivity() * ((int)prevPos.x - x) / camera.screenWidth());
		return Quaternion.multiply(rotY, rotX);
	}
}
