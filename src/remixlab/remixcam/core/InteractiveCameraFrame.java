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

import remixlab.remixcam.event.*;
import remixlab.remixcam.geom.*;
import remixlab.remixcam.interactivity.DOF1Event;
import remixlab.remixcam.interactivity.DOF2Event;
import remixlab.remixcam.interactivity.DOF3Event;
import remixlab.remixcam.interactivity.DOF6Event;

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
 * {@link remixlab.remixcam.core.AbstractScene#deviceGrabberPool()} upon creation.
 */
public class InteractiveCameraFrame extends InteractiveDrivableFrame implements Copyable {
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
	protected DLVector arcballRefPnt;	
	protected DLVector worldAxis;

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
		removeFromDeviceGrabberPool();
		arcballRefPnt = new DLVector(0.0f, 0.0f, 0.0f);
		worldAxis = new DLVector(0, 0, 1);
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param otherFrame the other interactive camera frame
	 */
	protected InteractiveCameraFrame(InteractiveCameraFrame otherFrame) {
		super(otherFrame);
		this.viewport = otherFrame.viewport;
		this.arcballRefPnt = new DLVector();
		this.arcballRefPnt.set(otherFrame.arcballRefPnt );
		this.worldAxis = new DLVector();
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
	 * Overloading of {@link remixlab.remixcam.core.InteractiveFrame#spin()}.
	 * <p>
	 * Rotates the InteractiveCameraFrame around its #arcballReferencePoint()
	 * instead of its origin.
	 */
	@Override
	public void spin() {
		if(spinningFriction > 0) {
			if (deviceSpeed == 0) {
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
	 * {@link remixlab.remixcam.core.Camera#arcballReferencePoint()} also returns this
	 * value.
	 */
	public DLVector arcballReferencePoint() {
		return arcballRefPnt;
	}

	/**
	 * Sets the {@link #arcballReferencePoint()}, defined in the world coordinate
	 * system.
	 */
	public void setArcballReferencePoint(DLVector refP) {
		arcballRefPnt = refP;
	}
	
	@Override
	protected void execAction2D(GenericMotionEvent<?> event) {
	}
	
	@Override
	protected void execAction3D(GenericMotionEvent<?> e) {
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
		
		DOF6Event event6;
		DLVector t = new DLVector();
    Quaternion q = new Quaternion();
		switch (id) {
		case ZOOM: {
			float wheelSensitivityCoef = 8E-4f;
			float coef = 0;
			DLVector trans = new DLVector();
		  //TODO 1-DOF -> wheel
			if( e instanceof DOF1Event ) {
			  coef = Math.max(Math.abs((coordinatesOf(scene.camera().arcballReferencePoint())).vec[2] * magnitude().z()), 0.2f * scene.camera().sceneRadius());
				//trans = new Vector3D(0.0f, 0.0f, coef * ((DOFEvent)event).getX() * wheelSensitivity() * wheelSensitivityCoef);
			  trans = new DLVector(0.0f, 0.0f, coef * ((DOF1Event)e).getX() * -wheelSensitivity() * wheelSensitivityCoef);
			}			
		  //TODO higher dofs
			// /**
			else {
				event = (DOF2Event)e;
				coef = Math.max(Math.abs((coordinatesOf(scene.camera().arcballReferencePoint())).vec[2] * magnitude().z() ), 0.2f * scene.camera().sceneRadius());
			  //float coef = Math.max(Math.abs((vp.frame().coordinatesOf(vp.arcballReferencePoint())).vec[2]), 0.2f * vp.sceneRadius());
			  // Warning: same for left and right CoordinateSystemConvention:
			  trans = new DLVector(0.0f, 0.0f,	-coef	* ((int) (event.getY() - event.getPrevY())) / scene.camera().screenHeight());
			}
			// */
			
			//No Scl
			DLVector mag = magnitude();
			trans.div(mag);
			
			translate(inverseTransformOf(trans));
			break;
		}
		
		case ROTATE: {			
			event = (DOF2Event)e;
			DLVector trans = scene.camera().projectedCoordinatesOf(arcballReferencePoint());
			Quaternion rot = deformedBallQuaternion(event, trans.vec[0], trans.vec[1], scene.camera());	
			setSpinningQuaternion(rot);
			startSpinning(event);
			//spin();
			break;
			
			/**
			Vector3D trans = camera.projectedCoordinatesOf(arcballReferencePoint());
			Quaternion rot = deformedBallQuaternion((int) eventPoint.x, (int) eventPoint.y, trans.vec[0], trans.vec[1], camera);				
			// #CONNECTION# These two methods should go together (spinning detection and activation)				
			setSpinningQuaternion(rot);
			//computeDeviceSpeed(eventPoint);
			//spin();				
			startDampedSpinning(eventPoint);
			prevPos = eventPoint;
			break;
			*/
		}
		
		case TRANSLATE: {
			event = (DOF2Event)e;
			///**
			Point delta = new Point(-event.getDX(),
					                     scene.isRightHanded() ? -event.getDY() : event.getDY());
			//System.out.println("RC coord: dx: " + delta.x + " dy: " + delta.y);
			
			DLVector trans = new DLVector((int) delta.x, (int) -delta.y, 0.0f);
			//*/	
			
			/**
			Vector3D trans = new Vector3D(-event.getDX(),
					                           scene.isRightHanded() ? ((DOF2Event)event).getDY() : -((DOF2Event)event).getDY(),
					                           0.0f);
			//*/
			
			// Scale to fit the screen mouse displacement
			switch (scene.camera().type()) {
			case PERSPECTIVE:
				trans.mult(2.0f
						       * (float) Math.tan( scene.camera().fieldOfView() / 2.0f)
						       * Math.abs(coordinatesOf(arcballReferencePoint()).vec[2] * magnitude().z())
						       / scene.camera().screenHeight());
				break;
			case ORTHOGRAPHIC:
				float[] wh = scene.camera().getOrthoWidthHeight();
				trans.vec[0] *= 2.0f * wh[0] / scene.camera().screenWidth();
				trans.vec[1] *= 2.0f * wh[1] / scene.camera().screenHeight();
				break;
			}			
			
			translate(inverseTransformOf(DLVector.mult(trans, translationSensitivity()), false));
			
			break;
		}
		
		case GOOGLE_EARTH:
			event6 = (DOF6Event)e;
			float magic = 0.01f; // rotSens/transSens?
      
			//t = DLVector.mult(position(), -event6.getZ() * ( rotSens.z/transSens.z ) );
      t = DLVector.mult(position(), -event6.getZ() * (magic) );
      translate(t);

      //q.fromEulerAngles(-event6.getY() * ( rotSens.y/transSens.y ), event6.getX() * ( rotSens.x/transSens.x ), 0);
      q.fromEulerAngles(-event6.getY() * (magic), event6.getX() * (magic), 0);
      rotateAroundPoint(q, scene.camera().arcballReferencePoint());

      q.fromEulerAngles(0, 0, event6.yaw());
      rotateAroundPoint(q, scene.camera().arcballReferencePoint());

      q.fromEulerAngles(-event6.roll(), 0, 0);
      rotate(q);
			break;
			
		case NATURAL:
			event6 = (DOF6Event)e;
			translate(localInverseTransformOf(new DLVector(event6.getX(),event6.getY(),-event6.getZ()), false));
      // Rotate
      q.fromEulerAngles(-event6.roll(), -event6.pitch(), event6.yaw());
      rotate(q);
			break;
		
		default:
			break;
		}
	}
	
	/**
	 * Returns a Quaternion computed according to mouse motion. The Quaternion
	 * is computed as composition of two rotations (quaternions): 1. Mouse motion along
	 * the screen X Axis rotates the camera along the {@link #getCADAxis()}. 2.
	 * Mouse motion along the screen Y axis rotates the camera along its X axis.
	 * 
	 * @see #getCADAxis()
	 */	
	protected Quaternion computeCADQuaternion(DOF2Event event, float cx,	float cy, Pinhole camera) {
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
		DLVector axisX = new DLVector(1, 0, 0);
		//0,0,1 is given in the world and then transform to the camera frame
		
		//TODO broken when cam frame has scaling
		DLVector world2camAxis = camera.frame().transformOf(worldAxis);
		//Vector3D world2camAxis = camera.frame().transformOfNoScl(worldAxis);

		float angleWorldAxis = rotationSensitivity() * (scene.isLeftHanded() ? (dx - px) : (px - dx));
		float angleX = rotationSensitivity() * (dy - py);	

		Quaternion quatWorld = new Quaternion(world2camAxis, angleWorldAxis);		
		Quaternion quatX = new Quaternion(axisX, angleX);
		
		return Quaternion.multiply(quatWorld, quatX);
	}
	
	/**
	 * Set axis (defined in the world coordinate system) as the main
	 * rotation axis used in CAD rotation.
	 */
	public void setCADAxis(DLVector axis) {
		//non-zero
		if( Geom.zero(axis.mag()) )
			return;
		else
			worldAxis = axis.get();
		worldAxis.normalize();
	}
	
	/**
	 * Returns the main CAD rotation axis ((defined in the world coordinate system).
	 */
	public DLVector getCADAxis() {
		return worldAxis;
	}
}
