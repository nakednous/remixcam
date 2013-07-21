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

import remixlab.dandelion.geom.*;
import remixlab.tersehandling.core.Copyable;
import remixlab.tersehandling.core.Util;
import remixlab.tersehandling.event.DOF2Event;
import remixlab.tersehandling.generic.event.*;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

/**
 * The InteractiveCameraFrame class represents an InteractiveFrame with Camera
 * specific mouse bindings.
 * <p>
 * An InteractiveCameraFrame is a specialization of an InteractiveDrivableFrame
 * (hence it can "fly" in the Scene), designed to be set as the
 * {@link Camera#frame()}. Mouse motions are basically interpreted in a negated
 * way: when the mouse goes to the right, the InteractiveFrame (and also the
 * InteractiveDrivableFrame and the InteractiveAvatarFrame) translation goes to
 * the right, while the InteractiveCameraFrame has to go to the <i>left</i>, so
 * that the <i>scene</i> seems to move to the right.
 * <p>
 * An InteractiveCameraFrame rotates around its {@link #arcballReferencePoint()}
 * , which corresponds to the associated {@link Camera#arcballReferencePoint()}.
 * <p>
 * <b>Note:</b> The InteractiveCameraFrame is not added to the
 * {@link remixlab.dandelion.core.AbstractScene#deviceGrabberPool()} upon creation.
 */
public class InteractiveCameraFrame extends InteractiveFrame implements Copyable {
	@Override
	public int hashCode() {
    return new HashCodeBuilder(17, 37).
    appendSuper(super.hashCode()).
		append(arcballRefPnt).
		append(worldAxis).
    toHashCode();
	}

	@Override
	public boolean equals(Object obj) {		
		if (obj == null) return false;
		if (obj == this) return true;		
		if (obj.getClass() != getClass()) return false;
		
		InteractiveCameraFrame other = (InteractiveCameraFrame) obj;
	  return new EqualsBuilder()
    .appendSuper(super.equals(obj))		
		.append(arcballRefPnt, other.arcballRefPnt)
		.append(worldAxis, other.worldAxis)
		.isEquals();
	}
	
	protected Pinhole viewport;
	protected Vec arcballRefPnt;	
	protected Vec worldAxis;

	/**
	 * Default constructor.
	 * <p>
	 * {@link #flySpeed()} is set to 0.0 and {@link #flyUpVector()} is (0,1,0).
	 * The {@link #arcballReferencePoint()} is set to (0,0,0).
	 * <p>
	 * <b>Attention:</b> Created object is {@link #removeFromDeviceGrabberPool()}.
	 */
	public InteractiveCameraFrame(Pinhole vp) {
		super(vp.scene);
		viewport = vp;
		scene.terseHandler().removeFromAllAgentPools(this);
		arcballRefPnt = new Vec(0.0f, 0.0f, 0.0f);
		worldAxis = new Vec(0, 0, 1);
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param otherFrame the other interactive camera frame
	 */
	protected InteractiveCameraFrame(InteractiveCameraFrame otherFrame) {
		super(otherFrame);
		this.viewport = otherFrame.viewport;
		this.arcballRefPnt = new Vec();
		this.arcballRefPnt.set(otherFrame.arcballRefPnt );
		this.worldAxis = new Vec();
		this.worldAxis.set(otherFrame.worldAxis );
	}
	
	/**
	 * Calls {@link #InteractiveCameraFrame(InteractiveCameraFrame)} (which is protected)
	 * and returns a copy of {@code this} object.
	 * 
	 * @see #InteractiveCameraFrame(InteractiveCameraFrame)
	 */
	public InteractiveCameraFrame get() {
		return new InteractiveCameraFrame(this);
	}	
	
	public Pinhole pinhole() {
		return viewport;
	}

	/**
	 * Updates the {@link remixlab.remixcam.core.Camera#lastFrameUpdate} variable when
	 * the frame changes and then calls {@code super.modified()}.
	 */
	/**
	@Override
	protected void modified() {
		viewport.lastFrameUpdate = scene.frameCount();
		super.modified();
	}
	*/

	/**
	 * Overloading of {@link remixlab.dandelion.core.InteractiveFrame#spin()}.
	 * <p>
	 * Rotates the InteractiveCameraFrame around its #arcballReferencePoint()
	 * instead of its origin.
	 */
	@Override
	public void spin() {
		if(spinningFriction > 0) {
			if (eventSpeed == 0) {
				stopSpinning();
				return;
			}
			rotateAroundPoint(spinningQuaternion(), arcballReferencePoint());
			recomputeSpinningQuaternion();						
		}
		else
			rotateAroundPoint(spinningQuaternion(), arcballReferencePoint());
	}
	
	/**
	@Override
	public void spin() {
		rotateAroundPoint(spinningQuaternion(), arcballReferencePoint());
	}
	*/

	/**
	 * Returns the point the InteractiveCameraFrame revolves around when rotated.
	 * <p>
	 * It is defined in the world coordinate system. Default value is (0,0,0).
	 * <p>
	 * When the InteractiveCameraFrame is associated to a Camera,
	 * {@link remixlab.dandelion.core.Camera#arcballReferencePoint()} also returns this
	 * value.
	 */
	public Vec arcballReferencePoint() {
		return arcballRefPnt;
	}

	/**
	 * Sets the {@link #arcballReferencePoint()}, defined in the world coordinate
	 * system.
	 */
	public void setArcballReferencePoint(Vec refP) {
		arcballRefPnt = refP;
	}
	
	@Override
	protected void execAction2D(DandelionAction a) {
		if(a==null) return;
	}
	
	@Override
	protected void execAction3D(DandelionAction a) {
		if(a==null) return;
		Vec trans;
		Quat q;
		switch(a) {
		case CUSTOM:
		case DRIVE:
		case LOOK_AROUND:
		case MOVE_BACKWARD:
		case MOVE_FORWARD:
		case ROLL:
			super.execAction3D(a);
			break;
		case ROTATE:
			if(e2.absolute()) {
				AbstractScene.showEventVariationWarning(a);
				break;
			}
			trans = scene.camera().projectedCoordinatesOf(arcballReferencePoint());			
			if( scene.camera().isArcBallRotate() )
				setSpinningQuaternion(deformedBallQuaternion(e2, trans.vec[0], trans.vec[1], scene.camera()));
			else
				setSpinningQuaternion(cadQuaternion(e2,  trans.vec[0], trans.vec[1], scene.camera()));
			startSpinning(e2);
			break;		
		case ROTATE3:
			q = new Quat();
			if(e3.absolute())
				q.fromEulerAngles(-e3.getX(), -e3.getDY(), e3.getDZ());
			else
				q.fromEulerAngles(-e3.getDX(), -e3.getDY(), e3.getDZ());
      rotate(q);
			break;
		case SCREEN_ROTATE:
			break;
		case SCREEN_TRANSLATE:
			break;
		case TRANSLATE:
			Point pDelta;
			if( e2.relative() )
				pDelta = new Point(-e2.getDX(), scene.isRightHanded() ? -e2.getDY() : e2.getDY());
			else
				pDelta = new Point(-e2.getX(), scene.isRightHanded() ? -e2.getY() : e2.getY());
			trans = new Vec((int) pDelta.x, (int) -pDelta.y, 0.0f);
			// Scale to fit the screen mouse displacement
			switch (scene.camera().type()) {
			case PERSPECTIVE:
				trans.mult(2.0f * (float) Math.tan( scene.camera().fieldOfView() / 2.0f)
                        * Math.abs(coordinatesOf(arcballReferencePoint()).vec[2] * magnitude().z())
                        / scene.camera().screenHeight());
				break;
			case ORTHOGRAPHIC:
				float[] wh = scene.camera().getOrthoWidthHeight();
				trans.vec[0] *= 2.0f * wh[0] / scene.camera().screenWidth();
				trans.vec[1] *= 2.0f * wh[1] / scene.camera().screenHeight();
				break;
			}
			translate(inverseTransformOf(Vec.mult(trans, translationSensitivity()), false));
			break;
		case TRANSLATE3:
			if(e3.absolute())
				translate(localInverseTransformOf(new Vec(e3.getX(),e3.getY(),-e3.getZ()), false));
			else
				translate(localInverseTransformOf(new Vec(e3.getDX(),e3.getDY(),-e3.getDZ()), false));
			break;
		case TRANSLATE_ROTATE:
			if(e6.absolute())
				translate(localInverseTransformOf(new Vec(e6.getX(),e6.getY(),-e6.getZ()), false));
			else
				translate(localInverseTransformOf(new Vec(e6.getDX(),e6.getDY(),-e6.getDZ()), false));
		  // Rotate
			q = new Quat();
			if(e6.absolute())
				q.fromEulerAngles(-e6.roll(), -e6.pitch(), e6.yaw());
			else
				q.fromEulerAngles(-e6.getDRX(), -e6.getDRY(), e6.getDRZ());
      rotate(q);
			break;
		case ZOOM:
			float wheelSensitivityCoef = 8E-4f;
			float coef = Math.max(Math.abs((coordinatesOf(scene.camera().arcballReferencePoint())).vec[2] * magnitude().z() ), 0.2f * scene.camera().sceneRadius());
			float delta;
			if( currentEvent instanceof GenericDOF1Event ) //its a wheel wheel :P
				delta = coef * e1.getX() * -wheelSensitivity() * wheelSensitivityCoef;
			else
				if( e1.absolute() )
				  delta = -coef	* e1.getX() / scene.camera().screenHeight();
				else
					delta = -coef	* e1.getDX() / scene.camera().screenHeight();
			trans = new Vec(0.0f, 0.0f,	delta);
			//No Scl
			Vec mag = magnitude();
			trans.div(mag);			
			translate(inverseTransformOf(trans));
			break;
		case ZOOM_ON_REGION:
			break;
		default:
			AbstractScene.showMissingImplementationWarning(a);
			break;
		}
		
		/**
		//TODO implement me as an example
		case GOOGLE_EARTH:
		  Vec t = new Vec();
	    Quat q = new Quat();
	    
			event6 = (GenericDOF6Event<?>)e;
			float magic = 0.01f; // rotSens/transSens?
      
			//t = DLVector.mult(position(), -event6.getZ() * ( rotSens.z/transSens.z ) );
      t = Vec.mult(position(), -event6.getZ() * (magic) );
      translate(t);

      //q.fromEulerAngles(-event6.getY() * ( rotSens.y/transSens.y ), event6.getX() * ( rotSens.x/transSens.x ), 0);
      q.fromEulerAngles(-event6.getY() * (magic), event6.getX() * (magic), 0);
      rotateAroundPoint(q, scene.camera().arcballReferencePoint());

      q.fromEulerAngles(0, 0, event6.yaw());
      rotateAroundPoint(q, scene.camera().arcballReferencePoint());

      q.fromEulerAngles(-event6.roll(), 0, 0);
      rotate(q);
			break;
			// */
	}
	
	/**
	 * Returns a Quaternion computed according to mouse motion. The Quaternion
	 * is computed as composition of two rotations (quaternions): 1. Mouse motion along
	 * the screen X Axis rotates the camera along the {@link #getCADAxis()}. 2.
	 * Mouse motion along the screen Y axis rotates the camera along its X axis.
	 * 
	 * @see #getCADAxis()
	 */	
	protected Quat cadQuaternion(DOF2Event event, float cx,	float cy, Pinhole camera) {
		if(! (camera instanceof Camera) )
			throw new RuntimeException("CAD cam is oly available in 3D");
		
		float x = event.getX();
		float y = event.getY();
		float prevX = event.getPrevX();
		float prevY = event.getPrevY();
		
		// Points on the deformed ball
		float px = rotationSensitivity() * ((int) prevX - cx)	/ camera.screenWidth();
		float py = rotationSensitivity() * (scene.isLeftHanded() ? ((int) prevY - cy) : ((cy - (int) prevY))) / camera.screenHeight();
		float dx = rotationSensitivity() * (x - cx) / camera.screenWidth();
		float dy = rotationSensitivity() * (scene.isLeftHanded() ? (y - cy) : (cy - y)) / camera.screenHeight();
		
		//1,0,0 is given in the camera frame
		Vec axisX = new Vec(1, 0, 0);
		//0,0,1 is given in the world and then transform to the camera frame
		
		//TODO broken when cam frame has scaling, maybe should go like this:?
		//Vec world2camAxis = camera.frame().transformOf(worldAxis, false);
		Vec world2camAxis = camera.frame().transformOf(worldAxis);
		//Vector3D world2camAxis = camera.frame().transformOfNoScl(worldAxis);

		float angleWorldAxis = rotationSensitivity() * (scene.isLeftHanded() ? (dx - px) : (px - dx));
		float angleX = rotationSensitivity() * (dy - py);	

		Quat quatWorld = new Quat(world2camAxis, angleWorldAxis);		
		Quat quatX = new Quat(axisX, angleX);
		
		return Quat.multiply(quatWorld, quatX);
	}
	
	/**
	 * Set axis (defined in the world coordinate system) as the main
	 * rotation axis used in CAD rotation.
	 */
	public void setCADAxis(Vec axis) {
		//non-zero
		if( Util.zero(axis.mag()) )
			return;
		else
			worldAxis = axis.get();
		worldAxis.normalize();
	}
	
	/**
	 * Returns the main CAD rotation axis ((defined in the world coordinate system).
	 */
	public Vec getCADAxis() {
		return worldAxis;
	}
}
