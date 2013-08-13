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
import remixlab.dandelion.util.AbstractTimerJob;
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
	
	protected Viewport viewport;
	protected Vec arcballRefPnt;	
	protected Vec worldAxis;
	
  //L O C A L   T I M E R
	public boolean arpFlag;
	public boolean pupFlag;
	public Vec pupVec;
	protected AbstractTimerJob timerFx;

	/**
	 * Default constructor.
	 * <p>
	 * {@link #flySpeed()} is set to 0.0 and {@link #flyUpVector()} is (0,1,0).
	 * The {@link #arcballReferencePoint()} is set to (0,0,0).
	 * <p>
	 * <b>Attention:</b> Created object is {@link #removeFromDeviceGrabberPool()}.
	 */
	public InteractiveCameraFrame(Viewport vp) {
		super(vp.scene);
		viewport = vp;
		scene.terseHandler().removeFromAllAgentPools(this);
		arcballRefPnt = new Vec(0.0f, 0.0f, 0.0f);
		worldAxis = new Vec(0, 0, 1);
		
		timerFx = new AbstractTimerJob() {
			public void execute() {
				unSetTimerFlag();
				}
			};
		scene.registerJob(timerFx);
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
		this.scene.terseHandler().removeFromAllAgentPools(this);
		this.timerFx = new AbstractTimerJob() {
			public void execute() {
				unSetTimerFlag();
			}
		};		
		this.scene.registerJob(timerFx);
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
	
	public Viewport pinhole() {
		return viewport;
	}
	
	// 2. Local timer
	/**
	 * Called from the timer to stop displaying the point under pixel and arcball
	 * reference point visual hints.
	 */
	protected void unSetTimerFlag() {
		arpFlag = false;
		pupFlag = false;
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
		if(dampFriction > 0) {
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
		Window viewWindow = (Window) viewport;
		Vec trans;
		float deltaX, deltaY;
		Orientable rot;
		switch(a) {
		case CUSTOM:
			break;
		case ROTATE:	
		case SCREEN_ROTATE:
			trans = viewWindow.projectedCoordinatesOf(arcballReferencePoint());			
			if(e2.relative()) {
				Point prevPos = new Point(e2.getPrevX(), e2.getPrevY());
				Point curPos= new Point(e2.getX(), e2.getY());
				rot = new Rot(new Point(trans.x(), trans.y()), prevPos, curPos);
				rot = new Rot(rot.angle() * rotationSensitivity());
			}
			else
				rot = new Rot(e2.getX() * rotationSensitivity());			
			if ( !isFlipped() ) rot.negate();
			//but its not enough to cover all different cases, so:
			if (scene.window().frame().magnitude().x() * scene.window().frame().magnitude().y() < 0 ) rot.negate();		
			if(e2.relative()) {
				setSpinningQuaternion(rot);
				startSpinning(e2);
			} else //absolute needs testing
				rotate(rot);
			break;
		case SCREEN_TRANSLATE:
			trans = new Vec();
			int dir = originalDirection(e2);			
			deltaX = (e2.relative()) ? e2.getDX() : e2.getX();
			if(e2.relative())
				deltaY = scene.isRightHanded() ? e2.getDY() : -e2.getDY();
			else
				deltaY = scene.isRightHanded() ? e2.getY() : -e2.getY();
			if (dir == 1)
				trans.set(-deltaX, 0.0f, 0.0f);
			else if (dir == -1)
				trans.set(0.0f, deltaY, 0.0f);	
			
			float[] wh = viewWindow.getOrthoWidthHeight();
			trans.vec[0] *= 2.0f * wh[0] / viewWindow.screenWidth();
			trans.vec[1] *= 2.0f * wh[1] / viewWindow.screenHeight();			
			translate(inverseTransformOf(Vec.mult(trans, translationSensitivity())));
			break;
		case TRANSLATE:
			deltaX = (e2.relative()) ? e2.getDX() : e2.getX();
			if(e2.relative())
				deltaY = scene.isRightHanded() ? -e2.getDY() : e2.getDY();
			else
				deltaY = scene.isRightHanded() ? -e2.getY() : e2.getY();			
			trans = new Vec(-deltaX, -deltaY, 0.0f);			
			trans = viewWindow.frame().inverseTransformOf(Vec.mult(trans, translationSensitivity()));				
			// And then down to frame
			if (referenceFrame() != null)
				trans = referenceFrame().transformOf(trans);
			translate(trans);
			break;
		case TRANSLATE_ROTATE:
			//translate:
			deltaX = (e6.relative()) ? e6.getDX() : e6.getX();
			if(e6.relative())
				deltaY = scene.isRightHanded() ? -e6.getDY() : e6.getDY();
			else
				deltaY = scene.isRightHanded() ? -e6.getY() : e6.getY();			
			trans = new Vec(-deltaX, -deltaY, 0.0f);			
			trans = viewWindow.frame().inverseTransformOf(Vec.mult(trans, translationSensitivity()));				
			// And then down to frame
			if (referenceFrame() != null)
				trans = referenceFrame().transformOf(trans);
			translate(trans);
			//rotate:
			trans = viewWindow.projectedCoordinatesOf(arcballReferencePoint());
		  //TODO "relative" is experimental here.
			//Hard to think of a DOF6 relative device in the first place.
			if(e6.relative()) 
				rot = new Rot(e6.getDRX() * rotationSensitivity());	
			else
				rot = new Rot(e6.getRX() * rotationSensitivity());			
			if ( !isFlipped() ) rot.negate();
			//but its not enough to cover all different cases, so:
			if (scene.window().frame().magnitude().x() * scene.window().frame().magnitude().y() < 0 ) rot.negate();		
			if(e6.relative()) {
				setSpinningQuaternion(rot);
				startSpinning(e6);
			} else //absolute needs testing
				rotate(rot);
			break;
		case ZOOM:
			float delta;
			if( e1 instanceof GenericDOF1Event ) //its a wheel wheel :P
				delta = e1.getX() * wheelSensitivity();
			else
				if( e1.absolute() )
					delta = e1.getX();
				else
					delta = e1.getDX();
			if(delta >= 0)
				scale(1 + Math.abs(delta) / (float) -scene.height());
			else
				inverseScale(1 + Math.abs(delta) / (float) -scene.height());
			break;
		case ZOOM_ON_REGION:
			if(e2.absolute()) {
				AbstractScene.showEventVariationWarning(a);
				break;
			}
			int w = (int) Math.abs(e2.getDX());
			int tlX = (int) e2.getPrevX() < (int) e2.getX() ? (int) e2.getPrevX() : (int) e2.getX();
			int h = (int) Math.abs(e2.getDY());
			int tlY = (int) e2.getPrevY() < (int) e2.getY() ? (int) e2.getPrevY() : (int) e2.getY();
			// viewWindow.fitScreenRegion( new Rectangle (tlX, tlY, w, h) );			
			viewWindow.interpolateToZoomOnRegion(new Rect(tlX, tlY, w, h));
			break;
		case CENTER_FRAME:
			viewWindow.centerScene();
			break;
		case ALIGN_FRAME:
			viewWindow.frame().alignWithFrame(null, true);
			break;
			//TODO these timer actions need testing
		case ZOOM_ON_PIXEL:
				viewWindow.interpolateToZoomOnPixel(new Point(cEvent.getX(), cEvent.getY()));
				pupVec = viewWindow.unprojectedCoordinatesOf(new Vec(cEvent.getX(), cEvent.getY(), 0.5f));
				pupFlag = true;
				timerFx.runOnce(1000);
			break;
		case ARP_FROM_PIXEL:
			if (viewWindow.setArcballReferencePointFromPixel( new Point(cEvent.getX(), cEvent.getY()) )) {			  
				arpFlag = true;
				timerFx.runOnce(1000);					
			}
			break;
		default:
			//AbstractScene.showMissingImplementationWarning(a);
			AbstractScene.showVariationWarning(a);
			break;
		}
	}
	
	@Override
	protected void execAction3D(DandelionAction a) {
		if(a==null) return;
		Camera camera = (Camera) viewport;
		Vec trans;
		Quat q;
		Camera.WorldPoint wP;
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
			trans = camera.projectedCoordinatesOf(arcballReferencePoint());			
			if( camera.isArcBallRotate() )
				setSpinningQuaternion(deformedBallQuaternion(e2, trans.vec[0], trans.vec[1], camera));
			else
				setSpinningQuaternion(cadQuaternion(e2,  trans.vec[0], trans.vec[1], camera));
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
			if(e2.absolute()) {
				AbstractScene.showEventVariationWarning(a);
				break;
			}
			trans = camera.projectedCoordinatesOf(arcballReferencePoint());
			float angle = (float) Math.atan2(e2.getY() - trans.vec[1], e2.getX() - trans.vec[0])
					        - (float) Math.atan2(e2.getPrevY() - trans.vec[1], e2.getPrevX() - trans.vec[0]);
			// lef-handed coordinate system correction
			//if( scene.isLeftHanded() )
			if( !isFlipped() )
				angle = -angle;
			Orientable rot = new Quat(new Vec(0.0f, 0.0f, 1.0f), angle);
			setSpinningQuaternion(rot);
			startSpinning(e2);
			updateFlyUpVector();
			break;
		case SCREEN_TRANSLATE:
			trans = new Vec();
			int dir = originalDirection(e2);
			if (dir == 1)
				if( e2.absolute() )
					trans.set(-e2.getX(), 0.0f, 0.0f);
				else
					trans.set(-e2.getDX(), 0.0f, 0.0f);
			else if (dir == -1)
				if( e2.absolute() )
					trans.set(0.0f, -e2.getY(), 0.0f);
				else
					trans.set(0.0f, -e2.getDY(), 0.0f);	
			switch (camera.type()) {
			case PERSPECTIVE:
				trans.mult(2.0f
						* (float) Math.tan( camera.fieldOfView() / 2.0f)
						* Math.abs(coordinatesOf(arcballReferencePoint()).vec[2] * magnitude().z())
						//* Math.abs((camera.frame().coordinatesOf(arcballReferencePoint())).vec[2])
						//* Math.abs((camera.frame().coordinatesOfNoScl(arcballReferencePoint())).vec[2])
						/ camera.screenHeight());
				break;
			case ORTHOGRAPHIC:
				float[] wh = camera.getOrthoWidthHeight();
				trans.vec[0] *= 2.0f * wh[0] / camera.screenWidth();
				trans.vec[1] *= 2.0f * wh[1] / camera.screenHeight();
				break;
			}
			trans = Vec.mult(trans, translationSensitivity());				
			trans.div(magnitude());
			translate(inverseTransformOf(trans));
			//translate(inverseTransformOf(trans, false));
			break;
		case TRANSLATE:
			Point pDelta;
			if( e2.relative() )
				pDelta = new Point(-e2.getDX(), scene.isRightHanded() ? -e2.getDY() : e2.getDY());
			else
				pDelta = new Point(-e2.getX(), scene.isRightHanded() ? -e2.getY() : e2.getY());
			trans = new Vec((int) pDelta.x, (int) -pDelta.y, 0.0f);
			// Scale to fit the screen mouse displacement
			switch (camera.type()) {
			case PERSPECTIVE:
				trans.mult(2.0f * (float) Math.tan( camera.fieldOfView() / 2.0f)
                        * Math.abs(coordinatesOf(arcballReferencePoint()).vec[2] * magnitude().z())
                        / camera.screenHeight());
				break;
			case ORTHOGRAPHIC:
				float[] wh = camera.getOrthoWidthHeight();
				trans.vec[0] *= 2.0f * wh[0] / camera.screenWidth();
				trans.vec[1] *= 2.0f * wh[1] / camera.screenHeight();
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
			//translate
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
			float coef = Math.max(Math.abs((coordinatesOf(camera.arcballReferencePoint())).vec[2] * magnitude().z() ), 0.2f * camera.sceneRadius());
			float delta;
			if( e1 instanceof GenericDOF1Event ) //its a wheel wheel :P
				delta = coef * e1.getX() * -wheelSensitivity() * wheelSensitivityCoef;
			else
				if( e1.absolute() )
				  delta = -coef	* e1.getX() / camera.screenHeight();
				else
					delta = -coef	* e1.getDX() / camera.screenHeight();
			trans = new Vec(0.0f, 0.0f,	delta);
			//No Scl
			Vec mag = magnitude();
			trans.div(mag);			
			translate(inverseTransformOf(trans));
			break;
		case ZOOM_ON_REGION:
			if(e2.absolute()) {
				AbstractScene.showEventVariationWarning(a);
				break;
			}
			int w = (int) Math.abs(e2.getDX());
			int tlX = (int) e2.getPrevX() < (int) e2.getX() ? (int) e2.getPrevX() : (int) e2.getX();
			int h = (int) Math.abs(e2.getDY());
			int tlY = (int) e2.getPrevY() < (int) e2.getY() ? (int) e2.getPrevY() : (int) e2.getY();
			// camera.fitScreenRegion( new Rectangle (tlX, tlY, w, h) );			
			camera.interpolateToZoomOnRegion(new Rect(tlX, tlY, w, h));
			break;
		case CENTER_FRAME:
			camera.centerScene();
			break;
		case ALIGN_FRAME:
			camera.frame().alignWithFrame(null, true);
			break;			
		case ZOOM_ON_PIXEL:
				wP = camera.interpolateToZoomOnPixel(new Point(cEvent.getX(), cEvent.getY()));
				if (wP.found) {
					pupVec = wP.point;
					pupFlag = true;
					timerFx.runOnce(1000);						
				}
			break;
		case ARP_FROM_PIXEL:
			if (camera.setArcballReferencePointFromPixel(new Point(cEvent.getX(), cEvent.getY()))) {			  
				arpFlag = true;
				timerFx.runOnce(1000);					
			}
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
	protected Quat cadQuaternion(DOF2Event event, float cx,	float cy, Viewport camera) {
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
