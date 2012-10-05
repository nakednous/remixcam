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

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

import remixlab.remixcam.geom.*;

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
 * {@link remixlab.remixcam.core.AbstractScene#mouseGrabberPool()} upon creation.
 */
public class InteractiveCameraFrame extends InteractiveDrivableFrame implements Copyable {
	@Override
	public int hashCode() {
    return new HashCodeBuilder(17, 37).
    appendSuper(super.hashCode()).
		append(arcballRefPnt).
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
		InteractiveCameraFrame other = (InteractiveCameraFrame) obj;
	  return new EqualsBuilder()
    .appendSuper(super.equals(obj))		
		.append(arcballRefPnt, other.arcballRefPnt)
		.isEquals();
	}
	
	protected Pinhole viewport;
	protected Vector3D arcballRefPnt;	

	/**
	 * Default constructor.
	 * <p>
	 * {@link #flySpeed()} is set to 0.0 and {@link #flyUpVector()} is (0,1,0).
	 * The {@link #arcballReferencePoint()} is set to (0,0,0).
	 * <p>
	 * <b>Attention:</b> Created object is {@link #removeFromMouseGrabberPool()}.
	 */
	public InteractiveCameraFrame(Pinhole vp) {
		super(vp.scene);
		viewport = vp;
		removeFromMouseGrabberPool();
		arcballRefPnt = new Vector3D(0.0f, 0.0f, 0.0f);
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

	/**
	 * Overloading of
	 * {@link remixlab.remixcam.core.InteractiveDrivableFrame#mouseDragged(Point, Pinhole)}
	 * .
	 * <p>
	 * Motion depends on mouse binding. The resulting displacements are basically
	 * inverted from those of an InteractiveFrame.
	 */
	public void mouseDragged(Point eventPoint, Pinhole vp) {
		if( ( scene.is2D() ) && ( !action.is2D() ) )
			return;
		
		if ((action == AbstractScene.MouseAction.MOVE_FORWARD)
				|| (action == AbstractScene.MouseAction.MOVE_BACKWARD)
				|| (action == AbstractScene.MouseAction.DRIVE)
				|| (action == AbstractScene.MouseAction.LOOK_AROUND)
				|| (action == AbstractScene.MouseAction.ROLL)
				|| (action == AbstractScene.MouseAction.ZOOM_ON_REGION)
				|| (action == AbstractScene.MouseAction.NO_MOUSE_ACTION))
			super.mouseDragged(eventPoint, vp);
		else {
			int deltaY;
			if( scene.isRightHanded() )
				deltaY = (int) (prevPos.y - eventPoint.y);
			else
				deltaY = (int) (eventPoint.y - prevPos.y);
			switch (action) {
			case TRANSLATE: {
				if( scene.is3D() ) {
				Point delta = new Point(prevPos.x - eventPoint.x, deltaY);
				Vector3D trans = new Vector3D((int) delta.x, (int) -delta.y, 0.0f);
				// Scale to fit the screen mouse displacement
				switch (((Camera) vp).type()) {
				case PERSPECTIVE:
					trans.mult(2.0f
							* (float) Math.tan(((Camera) vp).fieldOfView() / 2.0f)
							* Math.abs((vp.frame().coordinatesOf(arcballReferencePoint())).vec[2])
							/ vp.screenHeight());
					break;
				case ORTHOGRAPHIC: {
					float[] wh = vp.getOrthoWidthHeight();
					trans.vec[0] *= 2.0f * wh[0] / vp.screenWidth();
					trans.vec[1] *= 2.0f * wh[1] / vp.screenHeight();
					break;
				}
				}
				translate(inverseTransformOf(Vector3D.mult(trans, translationSensitivity())));
				prevPos = eventPoint;
				
				}
				else {
					Point delta = new Point(prevPos.x - eventPoint.x, deltaY);
					Vector3D trans = new Vector3D((int) delta.x, (int) -delta.y, 0.0f);
					// No need to scale to fit the screen mouse displacement
					translate(inverseTransformOf(Vector3D.mult(trans, translationSensitivity())));
					prevPos = eventPoint;
				}
				break;
			}

			case ZOOM: {
				if(scene.is3D()) {
				// #CONNECTION# wheelEvent() ZOOM case
				float coef = Math.max(Math.abs((vp.frame().coordinatesOf(vp.arcballReferencePoint())).vec[2]), 0.2f * vp.sceneRadius());
				// Warning: same for left and right CoordinateSystemConvention:
				Vector3D trans = new Vector3D(0.0f, 0.0f,	-coef	* ((int) (eventPoint.y - prevPos.y)) / vp.screenHeight());
				translate(inverseTransformOf(trans));
				prevPos = eventPoint;
				}
				else {
				  //TODO implement 2D case
					float delta = ((float)eventPoint.y - (float)prevPos.y);
					if(delta < 0)
						scale(1 + Math.abs(delta) / (float) scene.height());
					else
						inverseScale(1 + Math.abs(delta) / (float) scene.height());			
					prevPos = eventPoint;
					/**
					float coef = (float) (eventPoint.y - prevPos.y) / (float) vp.screenHeight();
					if(coef>0)
						((ViewWindow)vp).changeSize(true);
					else
						((ViewWindow)vp).changeSize(false);
					prevPos = eventPoint;
					*/					
				}
				break;
			}

			case ROTATE: {
				Vector3D trans = vp.projectedCoordinatesOf(arcballReferencePoint());
				Orientable rot;
				if(scene.is3D())
					rot = deformedBallQuaternion((int) eventPoint.x, (int) eventPoint.y, trans.vec[0], trans.vec[1], (Camera) vp);
				else {
					rot = new Rotation(new Point(trans.x(), trans.y()), prevPos, eventPoint);
					rot = new Rotation(rot.angle() * rotationSensitivity());
					if( scene.isLeftHanded() )
						rot.negate();
				}
				// #CONNECTION# These two methods should go together (spinning detection and activation)
				computeMouseSpeed(eventPoint);
				setSpinningQuaternion(rot);
				spin();
				prevPos = eventPoint;
				break;
			}

			case SCREEN_ROTATE: {		
				Vector3D trans = vp.projectedCoordinatesOf(arcballReferencePoint());
				float angle = (float) Math.atan2((int) eventPoint.y - trans.vec[1],
						                             (int) eventPoint.x - trans.vec[0])
						                  - (float) Math.atan2((int) prevPos.y - trans.vec[1], (int) prevPos.x
								              - trans.vec[0]);

				// lef-handed coordinate system correction
				if( scene.isLeftHanded() )
					angle = -angle;

				Orientable rot;
				if(scene.is3D())
					rot = new Quaternion(new Vector3D(0.0f, 0.0f, 1.0f), angle);
				else
					rot = new Rotation(angle);
				// #CONNECTION# These two methods should go together (spinning detection
				// and activation)
				computeMouseSpeed(eventPoint);
				setSpinningQuaternion(rot);
				spin();
				updateFlyUpVector();
				prevPos = eventPoint;				
				break;
			}

			case SCREEN_TRANSLATE: {
				if(scene.is3D()) {				
				Vector3D trans = new Vector3D();
				int dir = mouseOriginalDirection(eventPoint);
				if (dir == 1)
					trans.set(((int) prevPos.x - (int) eventPoint.x), 0.0f, 0.0f);
				else if (dir == -1)
					trans.set(0.0f, -deltaY, 0.0f);
				switch (((Camera) vp).type()) {
				case PERSPECTIVE:
					trans.mult(2.0f
							* (float) Math.tan(((Camera) vp).fieldOfView() / 2.0f)
							* Math.abs((vp.frame().coordinatesOf(arcballReferencePoint())).vec[2])
							/ vp.screenHeight());
					break;
				case ORTHOGRAPHIC: {
					float[] wh = vp.getOrthoWidthHeight();
					trans.vec[0] *= 2.0f * wh[0] / vp.screenWidth();
					trans.vec[1] *= 2.0f * wh[1] / vp.screenHeight();
					break;
				}
				}
				translate(inverseTransformOf(Vector3D.mult(trans, translationSensitivity())));
				prevPos = eventPoint;				
			  }
				else {
					//TODO implement 2D case
					Vector3D trans = new Vector3D();
					int dir = mouseOriginalDirection(eventPoint);
					if (dir == 1)
						trans.set(((int) prevPos.x - (int) eventPoint.x), 0.0f, 0.0f);
					else if (dir == -1)
						trans.set(0.0f, -deltaY, 0.0f);
					float[] wh = vp.getOrthoWidthHeight();
					trans.vec[0] *= 2.0f * wh[0] / vp.screenWidth();
					trans.vec[1] *= 2.0f * wh[1] / vp.screenHeight();
					translate(inverseTransformOf(Vector3D.mult(trans, translationSensitivity())));
					prevPos = eventPoint;
				}
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
	 * {@link remixlab.remixcam.core.InteractiveFrame#mouseReleased(Point, Camera)}.
	 */
	public void mouseReleased(Point eventPoint, Pinhole vp) {
		if( ( scene.is2D() ) && ( !action.is2D() ) )
			return;
		
		// Added by pierre: #CONNECTION# seems that startAction should always be
		// called before :)
		if (action == AbstractScene.MouseAction.ZOOM_ON_REGION) {
			// the rectangle needs to be normalized!
			int w = Math.abs((int) eventPoint.x - (int) pressPos.x);
			int tlX = (int) pressPos.x < (int) eventPoint.x ? (int) pressPos.x
					: (int) eventPoint.x;
			int h = Math.abs((int) eventPoint.y - (int) pressPos.y);
			int tlY = (int) pressPos.y < (int) eventPoint.y ? (int) pressPos.y
					: (int) eventPoint.y;

			// overkill:
			// if (event.getButton() == MouseEvent.BUTTON3)//right button
			// camera.fitScreenRegion( new Rectangle (tlX, tlY, w, h) );
			// else
			vp.interpolateToZoomOnRegion(new Rectangle(tlX, tlY, w, h));
		}

		super.mouseReleased(eventPoint, vp);
	}

	/**
	 * Overloading of
	 * {@link remixlab.remixcam.core.InteractiveDrivableFrame#mouseWheelMoved(int, Pinhole)}
	 * .
	 * <p>
	 * The wheel behavior depends on the wheel binded action. Current possible
	 * actions are {@link remixlab.remixcam.core.AbstractScene.MouseAction#ZOOM},
	 * {@link remixlab.remixcam.core.AbstractScene.MouseAction#MOVE_FORWARD} and
	 * {@link remixlab.remixcam.core.AbstractScene.MouseAction#MOVE_BACKWARD}.
	 * {@link remixlab.remixcam.core.AbstractScene.MouseAction#ZOOM} speed depends on
	 * #wheelSensitivity() the other two depend on #flySpeed().
	 */
	public void mouseWheelMoved(int rotation, Pinhole vp) {
		if( ( scene.is2D() ) && ( !action.is2D() ) )
			return;
		
		switch (action) {
		case ZOOM: {
			float wheelSensitivityCoef = 8E-4f;
			// #CONNECTION# mouseMoveEvent() ZOOM case
			float coef = Math.max(Math.abs((vp.frame().coordinatesOf(vp.arcballReferencePoint())).vec[2]), 0.2f * vp.sceneRadius());
			Vector3D trans;
			
			//TODO check rhanded
			/**
			if( scene.isRightHanded() )
				trans = new Vector3D(0.0f, 0.0f, coef * rotation * wheelSensitivity() * wheelSensitivityCoef);
			else
			*/
				trans = new Vector3D(0.0f, 0.0f, coef * (-rotation) * wheelSensitivity() * wheelSensitivityCoef);
			
			if( scene.is3D() ) {
				translate(inverseTransformOf(trans));
			}
			else {			
				float delta = rotation * wheelSensitivity();
				if(delta < 0)
					scale(1 + Math.abs(delta) / (float) scene.height());
				else
					inverseScale(1 + Math.abs(delta) / (float) scene.height());					
			  //TODO implement 2D case
				/**
				if(trans.z()>0)
					((ViewWindow)vp).changeSize(true);
				else
					((ViewWindow)vp).changeSize(false);
				*/
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

		// #CONNECTION# startAction should always be called before
		if (prevConstraint != null)
			setConstraint(prevConstraint);

		int finalDrawAfterWheelEventDelay = 400;

		// Starts (or prolungates) the timer.
		if( flyTimerJob.timer() != null )
			flyTimerJob.runOnce(finalDrawAfterWheelEventDelay);

		action = AbstractScene.MouseAction.NO_MOUSE_ACTION;
	}
}
