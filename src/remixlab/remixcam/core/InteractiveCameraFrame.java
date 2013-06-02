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

import remixlab.remixcam.event.DLEvent;
import remixlab.remixcam.event.DOF1Event;
import remixlab.remixcam.event.DOF2Event;
import remixlab.remixcam.geom.Geom;
import remixlab.remixcam.geom.Orientable;
import remixlab.remixcam.geom.Point;
import remixlab.remixcam.geom.Quaternion;
import remixlab.remixcam.geom.Rectangle;
import remixlab.remixcam.geom.Rotation;
import remixlab.remixcam.geom.Vector3D;

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
	protected Vector3D arcballRefPnt;	
	protected Vector3D worldAxis;

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
		arcballRefPnt = new Vector3D(0.0f, 0.0f, 0.0f);
		worldAxis = new Vector3D(0, 0, 1);
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param otherFrame the other interactive camera frame
	 */
	protected InteractiveCameraFrame(InteractiveCameraFrame otherFrame) {
		super(otherFrame);
		this.viewport = otherFrame.viewport;
		this.arcballRefPnt = new Vector3D();
		this.arcballRefPnt.set(otherFrame.arcballRefPnt );
		this.worldAxis = new Vector3D();
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
	 * Overloading of {@link remixlab.remixcam.core.InteractiveFrame#dampedSpin()}.
	 * <p>
	 * Rotates the InteractiveCameraFrame around its #arcballReferencePoint()
	 * instead of its origin.
	 */
	@Override
	public void dampedSpin() {
		if(spinningFriction > 0) {
			if (deviceSpeed == 0) {
				stopDampedSpinning();
				return;
			}
			rotateAroundPoint(spinningQuaternion(), arcballReferencePoint());
			recomputeSpinningQuaternion();						
		}
		else
			rotateAroundPoint(spinningQuaternion(), arcballReferencePoint());
	}
	
	@Override
	public void spin() {
		rotateAroundPoint(spinningQuaternion(), arcballReferencePoint());
	}

	/**
	 * Returns the point the InteractiveCameraFrame revolves around when rotated.
	 * <p>
	 * It is defined in the world coordinate system. Default value is (0,0,0).
	 * <p>
	 * When the InteractiveCameraFrame is associated to a Camera,
	 * {@link remixlab.remixcam.core.Camera#arcballReferencePoint()} also returns this
	 * value.
	 */
	public Vector3D arcballReferencePoint() {
		return arcballRefPnt;
	}

	/**
	 * Sets the {@link #arcballReferencePoint()}, defined in the world coordinate
	 * system.
	 */
	public void setArcballReferencePoint(Vector3D refP) {
		arcballRefPnt = refP;
	}
	
  //TODO should be protected
	@Override
	public void execAction2D(DOF1Event event) {
	}
	
  //TODO should be protected
	@Override
	public void execAction3D(DOF1Event event) {
		DLAction a = event.getAction();
		switch (a) {
		case ZOOM: {
			float wheelSensitivityCoef = 8E-4f;
			float coef = 0;
			Vector3D trans = new Vector3D();
		  //TODO 1-DOF -> wheel
			if( a.dofs() == 1 ) {
			  coef = Math.max(Math.abs((coordinatesOf(scene.camera().arcballReferencePoint())).vec[2] * magnitude().z()), 0.2f * scene.camera().sceneRadius());
				//trans = new Vector3D(0.0f, 0.0f, coef * ((DOFEvent)event).getX() * wheelSensitivity() * wheelSensitivityCoef);
			  trans = new Vector3D(0.0f, 0.0f, coef * ((DOF1Event)event).getX() * wheelSensitivity() * wheelSensitivityCoef);
			}			
		  //TODO higher dofs
			/**
			else {
				coef = Math.max(Math.abs((coordinatesOf(scene.camera().arcballReferencePoint())).vec[2] * magnitude().z() ), 0.2f * camera.sceneRadius());
			  //float coef = Math.max(Math.abs((vp.frame().coordinatesOf(vp.arcballReferencePoint())).vec[2]), 0.2f * vp.sceneRadius());
			  // Warning: same for left and right CoordinateSystemConvention:
			  trans = new Vector3D(0.0f, 0.0f,	-coef	* ((int) (eventPoint.y - prevPos.y)) / scene.camera().screenHeight());
			}
			*/
			
			//No Scl
			Vector3D mag = magnitude();
			trans.div(mag);
			
			translate(inverseTransformOf(trans));
			break;
		}
		
		case ROTATE: {
			Vector3D trans = scene.camera().projectedCoordinatesOf(arcballReferencePoint());
			Quaternion rot = deformedBallQuaternion((DOF2Event)event, trans.vec[0], trans.vec[1], scene.camera());	
			setSpinningQuaternion(rot);
			//startDampedSpinning(eventPoint);
			spin();
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
			///**
			Point delta = new Point(-event.getDX(),
					                     scene.isRightHanded() ? -((DOF2Event)event).getDY() : ((DOF2Event)event).getDY());
			//System.out.println("RC coord: dx: " + delta.x + " dy: " + delta.y);
			
			Vector3D trans = new Vector3D((int) delta.x, (int) -delta.y, 0.0f);
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
			
			setTossingDirection(inverseTransformOf(Vector3D.mult(trans, translationSensitivity()), false));
			translate(tossingDirection());
			
			break;
		}
		
		default:
			break;
		}
	}
	
	@Override
	protected void execAction2D(Point eventPoint, ViewWindow viewWindow) {
		if ((action == DOF_6Action.MOVE_FORWARD)
				|| (action == DOF_6Action.MOVE_BACKWARD)
				|| (action == DOF_6Action.DRIVE)
				|| (action == DOF_6Action.LOOK_AROUND)
				|| (action == DOF_6Action.ROLL)
				|| (action == DOF_6Action.ZOOM_ON_REGION)
				|| (action == DOF_6Action.NO_ACTION))
			super.execAction2D(eventPoint, viewWindow);
		else {
			int deltaY = (int) (eventPoint.y - prevPos.y);//as it were LH
			if( scene.isRightHanded() )
				deltaY = -deltaY;
			
			switch (action) {
			case TRANSLATE: {
				Point delta = new Point(prevPos.x - eventPoint.x, deltaY);
				Vector3D trans = new Vector3D((int) delta.x, (int) -delta.y, 0.0f);
				// No need to scale to fit the screen mouse displacement
				
				computeDeviceSpeed(eventPoint);
				setTossingDirection(inverseTransformOf(Vector3D.mult(trans, translationSensitivity())));
				toss();
				
				prevPos = eventPoint;				
				break;
			}

			case ZOOM: {				
		 	  //TODO 1-DOF -> wheel
				//float delta = -rotation * wheelSensitivity();
				float delta = ((float)eventPoint.y - (float)prevPos.y);
				if(delta < 0)
					scale(1 + Math.abs(delta) / (float) scene.height());
				else
					inverseScale(1 + Math.abs(delta) / (float) scene.height());
				
				prevPos = eventPoint;				
				break;
			}

			case ROTATE: {
				Vector3D trans = viewWindow.projectedCoordinatesOf(arcballReferencePoint());
				Orientable rot;
				rot = new Rotation(new Point(trans.x(), trans.y()), prevPos, eventPoint);
				rot = new Rotation(rot.angle() * rotationSensitivity());
				if( !isFlipped() )
					rot.negate();				
				// #CONNECTION# These two methods should go together (spinning detection and activation)				
				setSpinningQuaternion(rot);
				//computeDeviceSpeed(eventPoint);
				//spin();
				startDampedSpinning(eventPoint);
				prevPos = eventPoint;
				break;
			}

			case SCREEN_ROTATE: {		
				Vector3D trans = viewWindow.projectedCoordinatesOf(arcballReferencePoint());
				float angle = (float) Math.atan2((int) eventPoint.y - trans.vec[1],
						                             (int) eventPoint.x - trans.vec[0])
						                  - (float) Math.atan2((int) prevPos.y - trans.vec[1], (int) prevPos.x
								              - trans.vec[0]);

				// lef-handed coordinate system correction
				//if( scene.isLeftHanded() )
				if( !isFlipped() )
					angle = -angle;

				Orientable rot = new Rotation(angle);
				// #CONNECTION# These two methods should go together (spinning detection and activation)				
				setSpinningQuaternion(rot);
			  //computeDeviceSpeed(eventPoint);
				//spin();
				startDampedSpinning(eventPoint);
				updateFlyUpVector();
				prevPos = eventPoint;				
				break;
			}

			case SCREEN_TRANSLATE: {
				Vector3D trans = new Vector3D();
				int dir = deviceOriginalDirection(eventPoint);
				if (dir == 1)
					trans.set(((int) prevPos.x - (int) eventPoint.x), 0.0f, 0.0f);
				else if (dir == -1)
					trans.set(0.0f, -deltaY, 0.0f);
				float[] wh = viewWindow.getOrthoWidthHeight();
				trans.vec[0] *= 2.0f * wh[0] / viewWindow.screenWidth();
				trans.vec[1] *= 2.0f * wh[1] / viewWindow.screenHeight();				
				setTossingDirection(inverseTransformOf(Vector3D.mult(trans, translationSensitivity())));
				computeDeviceSpeed(eventPoint);
				toss();
				//startTossing(eventPoint);
				prevPos = eventPoint;
				
				break;
			}

			default:
				prevPos = eventPoint;
				break;
			}
		}
	}
	
	@Override
	protected void execAction3D(Point eventPoint, Camera camera) {
		if ((action == DOF_6Action.MOVE_FORWARD)
				|| (action == DOF_6Action.MOVE_BACKWARD)
				|| (action == DOF_6Action.DRIVE)
				|| (action == DOF_6Action.LOOK_AROUND)
				|| (action == DOF_6Action.ROLL)
				|| (action == DOF_6Action.ZOOM_ON_REGION)
				|| (action == DOF_6Action.NO_ACTION))
			super.execAction3D(eventPoint, camera);
		else {
			int deltaY = (int) (eventPoint.y - prevPos.y);//as it were LH
			if( scene.isRightHanded() )
				deltaY = -deltaY;
			
			switch (action) {			
			case TRANSLATE: {		
				Point delta = new Point(prevPos.x - eventPoint.x, deltaY);
				Vector3D trans = new Vector3D((int) delta.x, (int) -delta.y, 0.0f);						
				
				// Scale to fit the screen mouse displacement
				switch (camera.type()) {
				case PERSPECTIVE:
					trans.mult(2.0f
							       * (float) Math.tan( camera.fieldOfView() / 2.0f)
							       //* Math.abs((camera.frame().coordinatesOf(arcballReferencePoint())).vec[2])
							       * Math.abs((camera.frame().coordinatesOf(arcballReferencePoint())).vec[2] * magnitude().z())
							       //* Math.abs((camera.frame().coordinatesOfNoScl(arcballReferencePoint())).vec[2])
							       / camera.screenHeight());
					break;
				case ORTHOGRAPHIC: {
					float[] wh = camera.getOrthoWidthHeight();
					trans.vec[0] *= 2.0f * wh[0] / camera.screenWidth();
					trans.vec[1] *= 2.0f * wh[1] / camera.screenHeight();
					break;
				}
				}
				//translate(inverseTransformOf(Vector3D.mult(trans, translationSensitivity())));								
				
				setTossingDirection(inverseTransformOf(Vector3D.mult(trans, translationSensitivity()), false));
				computeDeviceSpeed(eventPoint);
				toss();
				//startTossing(eventPoint);
			
				prevPos = eventPoint;
				
				break;
			}

			case ZOOM: {
			  //TODO 1-DOF -> wheel
				//float coef = Math.max(Math.abs((vp.frame().coordinatesOf(vp.arcballReferencePoint())).vec[2] * magnitude().z()), 0.2f * vp.sceneRadius());
				//Vector3D trans = new Vector3D(0.0f, 0.0f, coef * rotation * wheelSensitivity() * wheelSensitivityCoef);
				
				// #CONNECTION# wheelEvent() ZOOM case
			  float coef = Math.max(Math.abs((camera.frame().coordinatesOf(camera.arcballReferencePoint())).vec[2] * magnitude().z() ), 0.2f * camera.sceneRadius());
				//float coef = Math.max(Math.abs((vp.frame().coordinatesOf(vp.arcballReferencePoint())).vec[2]), 0.2f * vp.sceneRadius());
				// Warning: same for left and right CoordinateSystemConvention:
				Vector3D trans = new Vector3D(0.0f, 0.0f,	-coef	* ((int) (eventPoint.y - prevPos.y)) / camera.screenHeight());
				
				//No Scl
				Vector3D mag = magnitude();
				trans.div(mag);
				
				translate(inverseTransformOf(trans));
				prevPos = eventPoint;				
				break;
			}

			case ROTATE: {
				Vector3D trans = camera.projectedCoordinatesOf(arcballReferencePoint());
				Quaternion rot = deformedBallQuaternion((int) eventPoint.x, (int) eventPoint.y, trans.vec[0], trans.vec[1], camera);				
				// #CONNECTION# These two methods should go together (spinning detection and activation)				
				setSpinningQuaternion(rot);
				//computeDeviceSpeed(eventPoint);
				//spin();				
				startDampedSpinning(eventPoint);
				prevPos = eventPoint;
				break;
			}
			
			case CAD_ROTATE: {
				Vector3D trans = camera.projectedCoordinatesOf(arcballReferencePoint());				
				// the following line calls setSpinningQuaternion
				Quaternion rot = computeCADQuaternion((int) eventPoint.x, (int) eventPoint.y, trans.x(), trans.y(), camera);
				// #CONNECTION# These two methods should go together (spinning detection and activation)				
				setSpinningQuaternion(rot);
				//computeDeviceSpeed(eventPoint);
				//spin();
				startDampedSpinning(eventPoint);
				prevPos = eventPoint;
				break;
			}

			case SCREEN_ROTATE: {		
				Vector3D trans = camera.projectedCoordinatesOf(arcballReferencePoint());
				float angle = (float) Math.atan2((int) eventPoint.y - trans.vec[1],
						                             (int) eventPoint.x - trans.vec[0])
						                  - (float) Math.atan2((int) prevPos.y - trans.vec[1], (int) prevPos.x
								              - trans.vec[0]);

				// lef-handed coordinate system correction
				//if( scene.isLeftHanded() )
				if( !isFlipped() )
					angle = -angle;

				Orientable rot = new Quaternion(new Vector3D(0.0f, 0.0f, 1.0f), angle);
				// #CONNECTION# These two methods should go together (spinning detection and activation)				
				setSpinningQuaternion(rot);
				//computeDeviceSpeed(eventPoint);
				//spin();
				startDampedSpinning(eventPoint);
				updateFlyUpVector();
				prevPos = eventPoint;	
				break;
			}

			case SCREEN_TRANSLATE: {
				Vector3D trans = new Vector3D();
				int dir = deviceOriginalDirection(eventPoint);
				if (dir == 1)
					trans.set(((int) prevPos.x - (int) eventPoint.x), 0.0f, 0.0f);
				else if (dir == -1)
					trans.set(0.0f, -deltaY, 0.0f);
								
				switch (camera.type()) {
				case PERSPECTIVE:
					trans.mult(2.0f
							* (float) Math.tan( camera.fieldOfView() / 2.0f)
							* Math.abs((camera.frame().coordinatesOf(arcballReferencePoint())).vec[2] * magnitude().z())
							//* Math.abs((camera.frame().coordinatesOf(arcballReferencePoint())).vec[2])
							//* Math.abs((camera.frame().coordinatesOfNoScl(arcballReferencePoint())).vec[2])
							/ camera.screenHeight());
					break;
				case ORTHOGRAPHIC: {
					float[] wh = camera.getOrthoWidthHeight();
					trans.vec[0] *= 2.0f * wh[0] / camera.screenWidth();
					trans.vec[1] *= 2.0f * wh[1] / camera.screenHeight();
					break;
				}
				}
				trans = Vector3D.mult(trans, translationSensitivity());				
				trans.div(magnitude());
				
				//translate(inverseTransformOf(Vector3D.mult(trans, translationSensitivity())));				
				setTossingDirection(inverseTransformOf(Vector3D.mult(trans, translationSensitivity()), false));
				computeDeviceSpeed(eventPoint);
				toss();
				//startTossing(eventPoint);
				
				prevPos = eventPoint;			
			  
				break;
			}

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
		
		// Added by pierre: #CONNECTION# seems that startAction should always be called before :)
		if (action == DOF_6Action.ZOOM_ON_REGION) {
			// the rectangle needs to be normalized!
			int w = Math.abs((int) eventPoint.x - (int) pressPos.x);
			int tlX = (int) pressPos.x < (int) eventPoint.x ? (int) pressPos.x : (int) eventPoint.x;
			int h = Math.abs((int) eventPoint.y - (int) pressPos.y);
			int tlY = (int) pressPos.y < (int) eventPoint.y ? (int) pressPos.y : (int) eventPoint.y;

			// overkill:
			// if (event.getButton() == MouseEvent.BUTTON3)//right button
			// camera.fitScreenRegion( new Rectangle (tlX, tlY, w, h) );
			// else
			scene.pinhole().interpolateToZoomOnRegion(new Rectangle(tlX, tlY, w, h));
		}

		super.endInteraction(eventPoint);
	}

	/**
	 * Overloading of
	 * {@link remixlab.remixcam.core.InteractiveDrivableFrame#wheelMoved(int, Pinhole)}
	 * .
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
			float wheelSensitivityCoef = 8E-4f;			
			
			if( scene.is3D() ) {
			  // #CONNECTION# mouseMoveEvent() ZOOM case
				//float coef = Math.max(Math.abs((vp.frame().coordinatesOf(vp.arcballReferencePoint())).vec[2]), 0.2f * vp.sceneRadius());
				float coef = Math.max(Math.abs((vp.frame().coordinatesOf(vp.arcballReferencePoint())).vec[2] * magnitude().z()), 0.2f * vp.sceneRadius());
				Vector3D trans = new Vector3D(0.0f, 0.0f, coef * rotation * wheelSensitivity() * wheelSensitivityCoef);
				
			  //No Scl
				Vector3D mag = magnitude();
				trans.div(mag);
				
				translate(inverseTransformOf(trans));
			}
			else {			
				float delta = -rotation * wheelSensitivity();
				if(delta < 0)
					scale(1 + Math.abs(delta) / (float) scene.height());
				else
					inverseScale(1 + Math.abs(delta) / (float) scene.height());
			}
			
			break;
		}
		case MOVE_FORWARD:
		case MOVE_BACKWARD:
			// #CONNECTION# mouseMoveEvent() MOVE_FORWARD case
			translate(inverseTransformOf(new Vector3D(0.0f, 0.0f, 0.2f * flySpeed()	* (-rotation))));
			break;
		default:
			break;
		}

		int finalDrawAfterWheelEventDelay = 400;

		// Starts (or prolungates) the timer.
		if( flyTimerJob.timer() != null )
			flyTimerJob.runOnce(finalDrawAfterWheelEventDelay);

		action = DLDeviceAction.NO_ACTION;
	}
	*/
	
	/**
	 * Returns a Quaternion computed according to mouse motion. The Quaternion
	 * is computed as composition of two rotations (quaternions): 1. Mouse motion along
	 * the screen X Axis rotates the camera along the {@link #getCADAxis()}. 2.
	 * Mouse motion along the screen Y axis rotates the camera along its X axis.
	 * 
	 * @see #getCADAxis()
	 */
	protected Quaternion computeCADQuaternion(int x, int y, float cx,	float cy, Pinhole camera) {
		if(! (camera instanceof Camera) )
			throw new RuntimeException("CAD cam is oly available in 3D");
		
		// Points on the deformed ball
		float px = rotationSensitivity() * ((int) prevPos.x - cx)	/ camera.screenWidth();
		float py = rotationSensitivity() * (scene.isLeftHanded() ? ((int) prevPos.y - cy) : ((cy - (int) prevPos.y))) / camera.screenHeight();
		float dx = rotationSensitivity() * (x - cx) / camera.screenWidth();
		float dy = rotationSensitivity() * (scene.isLeftHanded() ? (y - cy) : (cy - y)) / camera.screenHeight();
		
		//1,0,0 is given in the camera frame
		Vector3D axisX = new Vector3D(1, 0, 0);
		//0,0,1 is given in the world and then transform to the camera frame
		
		//TODO broken when cam frame has scaling
		Vector3D world2camAxis = camera.frame().transformOf(worldAxis);
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
	public void setCADAxis(Vector3D axis) {
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
	public Vector3D getCADAxis() {
		return worldAxis;
	}
}
