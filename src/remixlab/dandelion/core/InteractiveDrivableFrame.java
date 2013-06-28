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

import remixlab.dandelion.event.DOF1Event;
import remixlab.dandelion.event.DOF2Event;
import remixlab.dandelion.geom.*;
import remixlab.tersehandling.core.Copyable;
import remixlab.tersehandling.util.AbstractTimerJob;

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
	
	//TODO re-implement me!
	public void flyUpdate() {
		
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
	 * {@link remixlab.remixcam.action.DOF_6Action#MOVE_FORWARD} and
	 * {@link remixlab.remixcam.action.DOF_6Action#MOVE_BACKWARD} Scene.MouseAction
	 * bindings. In these modes, horizontal displacements of the mouse rotate the
	 * InteractiveDrivableFrame around this vector. Vertical displacements rotate
	 * always around the frame {@code X} axis.
	 * <p>
	 * Default value is (0,1,0), but it is updated by the Camera when set as its
	 * {@link remixlab.dandelion.core.Camera#frame()}.
	 * {@link remixlab.dandelion.core.Camera#setOrientation(Quaternion)} and
	 * {@link remixlab.dandelion.core.Camera#setUpVector(DLVector)} modify this value and
	 * should be used instead.
	 */
	public DLVector flyUpVector() {
		return flyUpVec;
	}

	/**
	 * Sets the {@link #flyUpVector()}, defined in the world coordinate system.
	 * <p>
	 * Default value is (0,1,0), but it is updated by the Camera when set as its
	 * {@link remixlab.dandelion.core.Camera#frame()}. Use
	 * {@link remixlab.dandelion.core.Camera#setUpVector(DLVector)} instead in that case.
	 */
	public void setFlyUpVector(DLVector up) {
		flyUpVec = up;
	}

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
	protected final Quaternion turnQuaternion(DOF1Event event, Camera camera) {
		float x = event.getX();
		float prevX = event.getPrevX();
		return new Quaternion(new DLVector(0.0f, 1.0f, 0.0f), rotationSensitivity()	* ((int)prevX - x) / camera.screenWidth());
	}

	/**
	 * Returns a Quaternion that is the composition of two rotations, inferred
	 * from the mouse pitch (X axis) and yaw ({@link #flyUpVector()} axis).
	 */
	protected final Quaternion pitchYawQuaternion(DOF2Event event, Camera camera) {
		float x = event.getX();
		float y = event.getY();
		float prevX = event.getPrevX();
		float prevY = event.getPrevY();
		
		int deltaY;
		if( scene.isRightHanded() )
			deltaY = (int) (prevY - y);
		else
			deltaY = (int) (y - prevY);
		
		Quaternion rotX = new Quaternion(new DLVector(1.0f, 0.0f, 0.0f), rotationSensitivity() * deltaY / camera.screenHeight());
		//Quaternion rotY = new Quaternion(transformOf(flyUpVector()), rotationSensitivity() * ((int)prevPos.x - x) / camera.screenWidth());	
		Quaternion rotY = new Quaternion(transformOf(flyUpVector(), false), rotationSensitivity() * ((int)prevX - x) / camera.screenWidth());
		return Quaternion.multiply(rotY, rotX);
	}
}
