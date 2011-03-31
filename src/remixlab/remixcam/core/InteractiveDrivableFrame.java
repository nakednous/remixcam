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

import remixlab.remixcam.devices.Actions.MouseAction;
import remixlab.remixcam.geom.*;
import remixlab.remixcam.util.TimerJob;

/**
 * The InteractiveDrivableFrame represents an InteractiveFrame that can "fly" in
 * the scene. It is the base class of all objects that are drivable in the
 * Scene: InteractiveAvatarFrame and InteractiveCameraFrame.
 * <p>
 * An InteractiveDrivableFrame basically moves forward, and turns according to
 * the mouse motion. See {@link #flySpeed()}, {@link #flyUpVector()} and the
 * {@link remixlab.proscene.Scene.MouseAction#MOVE_FORWARD} and
 * {@link remixlab.proscene.Scene.MouseAction#MOVE_BACKWARD}.
 */
public class InteractiveDrivableFrame extends InteractiveFrame implements Copyable {
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Float.floatToIntBits(drvSpd);
		result = prime * result + ((flyDisp == null) ? 0 : flyDisp.hashCode());
		result = prime * result + Float.floatToIntBits(flySpd);
		result = prime * result + ((flyUpVec == null) ? 0 : flyUpVec.hashCode());
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
		InteractiveDrivableFrame other = (InteractiveDrivableFrame) obj;
		if (Float.floatToIntBits(drvSpd) != Float.floatToIntBits(other.drvSpd))
			return false;
		if (flyDisp == null) {
			if (other.flyDisp != null)
				return false;
		} else if (!flyDisp.equals(other.flyDisp))
			return false;
		if (Float.floatToIntBits(flySpd) != Float.floatToIntBits(other.flySpd))
			return false;
		if (flyUpVec == null) {
			if (other.flyUpVec != null)
				return false;
		} else if (!flyUpVec.equals(other.flyUpVec))
			return false;
		return true;
	}

	protected float flySpd;
	protected float drvSpd;
	protected TimerJob flyTimerJob;
	protected Vector3D flyUpVec;
	protected Vector3D flyDisp;

	/**
	 * Default constructor.
	 * <p>
	 * {@link #flySpeed()} is set to 0.0 and {@link #flyUpVector()} is (0,1,0).
	 */
	public InteractiveDrivableFrame(RCScene scn) {
		super(scn);
		drvSpd = 0.0f;
		flyUpVec = new Vector3D(0.0f, 1.0f, 0.0f);
		flyDisp = new Vector3D(0.0f, 0.0f, 0.0f);
		setFlySpeed(0.0f);

		flyTimerJob = new TimerJob() {
			public void execute() {
				flyUpdate();
			}
		};		
		scene.timerPool.registerInTimerPool(this, flyTimerJob);
	}
	
	protected InteractiveDrivableFrame(InteractiveDrivableFrame otherFrame) {		
		super(otherFrame);
		this.drvSpd = otherFrame.drvSpd;
		this.flyUpVec = new Vector3D();
		this.flyUpVec.set(otherFrame.flyUpVector());
		this.flyDisp = new Vector3D();
		this.flyDisp.set(otherFrame.flyDisp);
		this.setFlySpeed( otherFrame.flySpeed() );
		
		this.flyTimerJob = new TimerJob() {
			public void execute() {
				flyUpdate();
			}
		};		
		scene.timerPool.registerInTimerPool(this, this.flyTimerJob);
	}
	
	public InteractiveDrivableFrame getCopy() {
		return new InteractiveDrivableFrame(this);
	}	

	/**
	 * Returns the fly speed, expressed in processing scene units.
	 * <p>
	 * It corresponds to the incremental displacement that is periodically applied
	 * to the InteractiveDrivableFrame position when a
	 * {@link remixlab.proscene.Scene.MouseAction#MOVE_FORWARD} or
	 * {@link remixlab.proscene.Scene.MouseAction#MOVE_BACKWARD} Scene.MouseAction is proceeded.
	 * <p>
	 * <b>Attention:</b> When the InteractiveDrivableFrame is set as the
	 * {@link remixlab.remixcam.core.Camera#frame()} (which indeed is an instance of
	 * the InteractiveCameraFrame class) or when it is set as the
	 * {@link remixlab.proscene.Scene#avatar()} (which indeed is an instance of
	 * the InteractiveAvatarFrame class), this value is set according to the
	 * {@link remixlab.proscene.Scene#radius()} by
	 * {@link remixlab.proscene.Scene#setRadius(float)}.
	 */
	public float flySpeed() {
		return flySpd;
	}

	/**
	 * Sets the {@link #flySpeed()}, defined in processing scene units.
	 * <p>
	 * Default value is 0.0, but it is modified according to the
	 * {@link remixlab.proscene.Scene#radius()} when the InteractiveDrivableFrame
	 * is set as the {@link remixlab.remixcam.core.Camera#frame()} (which indeed is an
	 * instance of the InteractiveCameraFrame class) or when the
	 * InteractiveDrivableFrame is set as the
	 * {@link remixlab.proscene.Scene#avatar()} (which indeed is an instance of
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
	 * {@link remixlab.proscene.Scene.MouseAction#MOVE_FORWARD} and
	 * {@link remixlab.proscene.Scene.MouseAction#MOVE_BACKWARD} Scene.MouseAction
	 * bindings. In these modes, horizontal displacements of the mouse rotate the
	 * InteractiveDrivableFrame around this vector. Vertical displacements rotate
	 * always around the frame {@code X} axis.
	 * <p>
	 * Default value is (0,1,0), but it is updated by the Camera when set as its
	 * {@link remixlab.remixcam.core.Camera#frame()}.
	 * {@link remixlab.remixcam.core.Camera#setOrientation(Quaternion)} and
	 * {@link remixlab.remixcam.core.Camera#setUpVector(Vector3D)} modify this value and
	 * should be used instead.
	 */
	public Vector3D flyUpVector() {
		return flyUpVec;
	}

	/**
	 * Sets the {@link #flyUpVector()}, defined in the world coordinate system.
	 * <p>
	 * Default value is (0,1,0), but it is updated by the Camera when set as its
	 * {@link remixlab.remixcam.core.Camera#frame()}. Use
	 * {@link remixlab.remixcam.core.Camera#setUpVector(Vector3D)} instead in that case.
	 */
	public void setFlyUpVector(Vector3D up) {
		flyUpVec = up;
	}

	/**
	 * Called for continuous frame motion in first person mode (see
	 * {@link remixlab.proscene.Scene.MouseAction#MOVE_FORWARD}).
	 */
	public void flyUpdate() {
		flyDisp.set(0.0f, 0.0f, 0.0f);
		switch (action) {
		case MOVE_FORWARD:
			flyDisp.z = -flySpeed();
			translate(localInverseTransformOf(flyDisp));
			break;
		case MOVE_BACKWARD:
			flyDisp.z = flySpeed();
			translate(localInverseTransformOf(flyDisp));
			break;
		case DRIVE:
			flyDisp.z = flySpeed() * drvSpd;
			translate(localInverseTransformOf(flyDisp));
			break;
		default:
			break;
		}
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
	public void startAction(MouseAction a, boolean withConstraint) {
		super.startAction(a, withConstraint);
		switch (action) {
		case MOVE_FORWARD:
		case MOVE_BACKWARD:
		case DRIVE:
			if( flyTimerJob.timer() != null )
				flyTimerJob.timer().runTimer(10);
			break;
		default:
			break;
		}
	}
	
	/**
	 * Non-overloaded version of {@link #mouseDragged(Point, Camera)}.
	 */	
	public final void iDrivableMouseDragged(Point eventPoint, Camera camera) {
		if ((action == MouseAction.TRANSLATE)
				|| (action == MouseAction.ZOOM)
				|| (action == MouseAction.SCREEN_ROTATE)
				|| (action == MouseAction.SCREEN_TRANSLATE)
				|| (action == MouseAction.ROTATE)
				|| (action == MouseAction.NO_MOUSE_ACTION))
			super.mouseDragged(eventPoint, camera);
		else {		
			int	deltaY = (int) (eventPoint.y - prevPos.y);
		  //right_handed coordinate system should go like this:
			//int deltaY = (int) (prevPos.y - eventPoint.y);
			
			switch (action) {
			case MOVE_FORWARD: {
				Quaternion rot = pitchYawQuaternion((int)eventPoint.x, (int)eventPoint.y, camera);
				rotate(rot);
				// #CONNECTION# wheelEvent MOVE_FORWARD case
				// actual translation is made in flyUpdate().
				// translate(inverseTransformOf(Vec(0.0, 0.0, -flySpeed())));
				prevPos = eventPoint;
				break;
			}

			case MOVE_BACKWARD: {
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
				float angle = (float) Math.PI * ((int)eventPoint.x - (int)prevPos.x) / camera.screenWidth();
				
			  //lef-handed coordinate system correction
				angle = -angle;
				
				Quaternion rot = new Quaternion(new Vector3D(0.0f, 0.0f, 1.0f), angle);
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
	 * {@link remixlab.remixcam.core.InteractiveFrame#mouseDragged(Point, Camera)}.
	 * <p>
	 * Motion depends on mouse binding. The resulting displacements are basically
	 * the same of those of an InteractiveFrame, but moving forward and backward
	 * and turning actions are implemented.
	 */
	public void mouseDragged(Point eventPoint, Camera camera) {
		iDrivableMouseDragged(eventPoint, camera);
	}

	/**
	 * Overloading of
	 * {@link remixlab.remixcam.core.InteractiveFrame#mouseReleased(Point, Camera)}.
	 */
	public void mouseReleased(Point eventPoint, Camera camera) {
		iDrivableMouseReleased(eventPoint, camera);
	}
	
	/**
	 * Non-overloaded version of {@link #mouseReleased(Point, Camera)}.
	 */	
	public final void iDrivableMouseReleased(Point eventPoint, Camera camera) {
		if ((action == MouseAction.MOVE_FORWARD)
				|| (action == MouseAction.MOVE_BACKWARD)
				|| (action == MouseAction.DRIVE)) {
			if( flyTimerJob.timer() != null )
				flyTimerJob.timer().cancelTimer();			
		}

		super.mouseReleased(eventPoint, camera);
	}
	
	/**
	 * Overloading of
	 * {@link remixlab.remixcam.core.InteractiveFrame#mouseWheelMoved(int, Camera)}.
	 * <p>
	 * The wheel behavior depends on the wheel binded action. Current possible
	 * actions are {@link remixlab.proscene.Scene.MouseAction#ZOOM},
	 * {@link remixlab.proscene.Scene.MouseAction#MOVE_FORWARD} and
	 * {@link remixlab.proscene.Scene.MouseAction#MOVE_BACKWARD}.
	 * {@link remixlab.proscene.Scene.MouseAction#ZOOM} speed depends on
	 * #wheelSensitivity() the other two depend on #flySpeed().
	 */
	public void mouseWheelMoved(int rotation, Camera camera) {
		iDrivableMouseWheelMoved(rotation, camera);
	}

	/**
	 * Non-overloaded version of {@link #mouseWheelMoved(int, Camera)}.
	 */	
	public final void iDrivableMouseWheelMoved(int rotation, Camera camera) {
		switch (action) {
		case ZOOM: {
			float wheelSensitivityCoef = 8E-4f;
			
			Vector3D trans = new Vector3D(0.0f, 0.0f, rotation * wheelSensitivity()	* wheelSensitivityCoef * (Vector3D.sub(camera.position(), position())).mag());
		  //right_handed coordinate system should go like this:
			//Vector3D trans = new Vector3D(0.0f, 0.0f, -rotation * wheelSensitivity()	* wheelSensitivityCoef * (Vector3D.sub(camera.position(), position())).mag());
			
			// #CONNECTION# Cut-pasted from the mouseMoveEvent ZOOM case
			trans = camera.frame().orientation().rotate(trans);
			if (referenceFrame() != null)
				trans = referenceFrame().transformOf(trans);
			translate(trans);
			break;
		}
		case MOVE_FORWARD:
		case MOVE_BACKWARD:
			// #CONNECTION# mouseMoveEvent() MOVE_FORWARD case
			translate(inverseTransformOf(new Vector3D(0.0f, 0.0f, 0.2f * flySpeed()
					* (-rotation))));
			break;
		default:
			break;
		}

		// #CONNECTION# startAction should always be called before
		if (prevConstraint != null)
			setConstraint(prevConstraint);

		int finalDrawAfterWheelEventDelay = 400;
		
	  // Starts (or prolungates) the timer.
		if( flyTimerJob.timer() != null )
			flyTimerJob.timer().runTimerOnce(finalDrawAfterWheelEventDelay);

		action = MouseAction.NO_MOUSE_ACTION;
	}

	/**
	 * This method will be called by the Camera when its orientation is changed,
	 * so that the {@link #flyUpVector()} (private) is changed accordingly. You
	 * should not need to call this method.
	 */
	public final void updateFlyUpVector() {
		flyUpVec = inverseTransformOf(new Vector3D(0.0f, 1.0f, 0.0f));
	}

	/**
	 * Returns a Quaternion that is a rotation around current camera Y,
	 * proportional to the horizontal mouse position.
	 */
	protected final Quaternion turnQuaternion(int x, Camera camera) {
		return new Quaternion(new Vector3D(0.0f, 1.0f, 0.0f), rotationSensitivity()
				* ((int)prevPos.x - x) / camera.screenWidth());
	}

	/**
	 * Returns a Quaternion that is the composition of two rotations, inferred
	 * from the mouse pitch (X axis) and yaw ({@link #flyUpVector()} axis).
	 */
	protected final Quaternion pitchYawQuaternion(int x, int y, Camera camera) {
		int deltaY = (int) (y - prevPos.y);
  	//right_handed coordinate system should go like this:
		//deltaY = (int) (prevPos.y - y);
		
		Quaternion rotX = new Quaternion(new Vector3D(1.0f, 0.0f, 0.0f),
				rotationSensitivity() * deltaY / camera.screenHeight());
		Quaternion rotY = new Quaternion(transformOf(flyUpVector()),
				rotationSensitivity() * ((int)prevPos.x - x) / camera.screenWidth());
		return Quaternion.multiply(rotY, rotX);
	}
}
