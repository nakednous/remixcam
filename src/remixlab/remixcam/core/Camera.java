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

import remixlab.remixcam.geom.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

/**
 * A perspective or orthographic camera.
 * <p>
 * A Camera defines some intrinsic parameters ({@link #fieldOfView()},
 * {@link #position()}, {@link #viewDirection()}, {@link #upVector()}...) and
 * useful positioning tools that ease its placement ({@link #showEntireScene()},
 * {@link #fitSphere(Vector3D, float)}, {@link #lookAt(Vector3D)}...). It exports
 * its associated processing projection and modelview matrices and it can
 * interactively be modified using the mouse.
 * <p>
 * Camera matrices can be directly set as references to the processing camera
 * matrices (default), or they can be set as independent Matrix3D objects
 * (which may be useful for off-screen computations).
 * <p>
 * There are to {@link #kind()} of Cameras: PROSCENE (default) and STANDARD. The
 * former kind dynamically sets up the {@link #zNear()} and {@link #zFar()}
 * values, in order to provide optimal precision of the z-buffer. The latter
 * kind provides fixed values to both of them ({@link #setStandardZNear(float)}
 * and {@link #setStandardZFar(float)}).
 * 
 */
public class Camera implements Constants, Copyable {
	@Override
	public int hashCode() {	
    return new HashCodeBuilder(17, 37).
    //append(attachedToPCam).
    append(fpCoefficientsUpdate).
    append(unprojectCacheOptimized).
    append(lastFrameUpdate).
    append(lastFPCoeficientsUpdateIssued).
    append(zClippingCoef).
		append(IODist).
		append(dist).
		append(fldOfView).
		append(focusDist).
		append(fpCoefficients).
		append(frm).
		append(interpolationKfi).
		append(knd).
		append(modelViewMat).
		append(normal).
		append(orthoCoef).
		append(orthoSize).
		append(physicalDist2Scrn).
		append(physicalScrnWidth).
		append(projectionMat).
		append(scnCenter).
		append(scnRadius).
		append(scrnHeight).
		append(scrnWidth).
		append(stdZFar).
		append(stdZNear).
		append(tempFrame).
		append(tp).
		append(viewport).
		append(zClippingCoef).
		append(zNearCoef).
    toHashCode();

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Camera other = (Camera) obj;
		
	   return new EqualsBuilder()
    .appendSuper(super.equals(obj))		
    //.append(attachedToPCam, other.attachedToPCam)
    .append(fpCoefficientsUpdate, other.fpCoefficientsUpdate)
    .append(unprojectCacheOptimized, other.unprojectCacheOptimized)
    .append(lastFrameUpdate, other.lastFrameUpdate)
    .append(lastFPCoeficientsUpdateIssued, other.lastFPCoeficientsUpdateIssued)
    .append(zClippingCoef, other.zClippingCoef)
		.append(IODist,other.IODist)
		.append(dist,other.dist)
		.append(fldOfView,other.fldOfView)
		.append(focusDist,other.focusDist)
		.append(fpCoefficients,other.fpCoefficients)
		.append(frm,other.frm)
		.append(interpolationKfi,other.interpolationKfi)
		.append(knd,other.knd)
		.append(modelViewMat,other.modelViewMat)
		.append(normal,other.normal)
		.append(orthoCoef,other.orthoCoef)
		.append(orthoSize,other.orthoSize)
		.append(physicalDist2Scrn,other.physicalDist2Scrn)
		.append(physicalScrnWidth,other.physicalScrnWidth)
		.append(projectionMat,other.projectionMat)
		.append(scnCenter,other.scnCenter)
		.append(scnRadius,other.scnRadius)
		.append(scrnHeight,other.scrnHeight)
		.append(scrnWidth,other.scrnWidth)
		.append(stdZFar,other.stdZFar)
		.append(stdZNear,other.stdZNear)
		.append(tempFrame,other.tempFrame)
		.append(tp,other.tp)
		.append(viewport,other.viewport)
		.append(zClippingCoef,other.zClippingCoef)
		.append(zNearCoef,other.zNearCoef)
		.isEquals();
	}
	
	
	/**
	 * Internal class that represents/holds a cone of normals.
	 * Typically needed to perform bfc.
	 */
	public class Cone {
		Vector3D axis;
		float angle;
		
		public Cone() {
			reset();
		}
		
		public Cone(Vector3D vec, float a) {
			set(vec, a);
		}
		
		public Cone(ArrayList<Vector3D> normals) {
			set(normals);
		}
		
		public Cone(Vector3D [] normals) {
			set(normals);
		}
		
		public Vector3D axis() {
			return axis;
		}
		
		public float angle() {
			return angle;
		}
		
		public void reset() {
			axis = new Vector3D(0,0,1);
			angle = 0;
		}
		
		public void set(Vector3D vec, float a) {
			axis = vec;
			angle = a;
		}
		
		public void set(ArrayList<Vector3D> normals) {
			set( normals.toArray( new Vector3D [normals.size()] ) );		
		}
		
		public void set(Vector3D [] normals) {
			axis = new Vector3D(0,0,0);
			if(normals.length == 0) {
				reset();
				return;
			}
			
			Vector3D [] n = new Vector3D [normals.length];
			for(int i=0; i<normals.length; i++ ) {
				n[i] = new Vector3D();
				n[i].set(normals[i]);
				n[i].normalize();
				axis = Vector3D.add(axis, n[i]);
			}
			
			if ( axis.mag() != 0 ) {
	      axis.normalize();
	    }
	    else {
	      axis.set(0,0,1);
	    }
			
			angle = 0;        
			for(int i=0; i<normals.length; i++ )		
				angle = Math.max( angle, (float) Math.acos( Vector3D.dot(n[i], axis)));		
		}	
	}
	
	/**
	 * Internal class provided to catch the output of
	 * {@link remixlab.remixcam.core.Camera#pointUnderPixel(Point)} (which should be
	 * implemented by an openGL based derived class Camera).
	 */
	public class WorldPoint {
		public WorldPoint(Vector3D p, boolean f) {
			point = p;
			found = f;
		}

		public Vector3D point;
		public boolean found;
	}

	int viewport[] = new int[4];
	// next variables are needed for frustum plane coefficients
	Vector3D normal[] = new Vector3D[6];
	float dist[] = new float[6];

	/**
	 * Enumerates the two possible types of Camera.
	 * <p>
	 * This type mainly defines different camera projection matrix. Many other
	 * methods take this Type into account.
	 */
	public enum Type {
		PERSPECTIVE, ORTHOGRAPHIC
	};

	/**
	 * Enumerates the different visibility state an object may have respect to the
	 * camera frustum.
	 * <p>
	 * This type mainly defines different camera projection matrix. Many other
	 * methods take this Type into account.
	 */
	public enum Visibility {
		VISIBLE, SEMIVISIBLE, INVISIBLE
	};

	/**
	 * Enumerates the Camera kind.
	 */
	public enum Kind {
		PROSCENE, STANDARD
	}

	// F r a m e
	private InteractiveCameraFrame frm;

	// C a m e r a p a r a m e t e r s
	private int scrnWidth, scrnHeight; // size of the window, in pixels
	private float fldOfView; // in radians
	private Vector3D scnCenter;
	private float scnRadius; // processing scene units
	private float zNearCoef;
	private float zClippingCoef;
	private float orthoCoef;
	private Type tp; // PERSPECTIVE or ORTHOGRAPHIC
	private Kind knd; // PROSCENE or STANDARD
	private float orthoSize;
	private float stdZNear;
	private float stdZFar;

	private Matrix3D modelViewMat;
	private Matrix3D projectionMat;
	
  //cache optimization
	public boolean projectCacheOptimized;
	private Matrix3D  projectionTimesModelview;
	public boolean unprojectCacheOptimized;
	private boolean projectionTimesModelviewHasInverse;
	private Matrix3D projectionTimesModelviewInverse;

	// S t e r e o p a r a m e t e r s
	private float IODist; // inter-ocular distance, in meters
	private float focusDist; // in scene units
	private float physicalDist2Scrn; // in meters
	private float physicalScrnWidth; // in meters

	// P o i n t s o f V i e w s a n d K e y F r a m e s
	protected HashMap<Integer, KeyFrameInterpolator> kfi;
	Iterator<Integer> itrtr;
	protected KeyFrameInterpolator interpolationKfi;
	protected InteractiveCameraFrame tempFrame;

	// F r u s t u m p l a n e c o e f f i c i e n t s
	protected float fpCoefficients[][];
	protected boolean fpCoefficientsUpdate;
	/**
   * Which was the last frame the camera changes.
   * <P>
   * Takes into account the {@link #frame()} (position and orientation of the camera)
   * and the camera {@link #type()} and {@link #kind()}.
   */
	public long lastFrameUpdate = 0;
	protected long lastFPCoeficientsUpdateIssued = -1;

	// A t t a c h e d S c e n e
	//private boolean attachedToPCam;

	// S C E N E   O B J E C T 
	public AbstractScene scene;
	//public PGraphics3D pg3d;
	
	/**
	public Camera(Scene scn) {
		this(scn, true);
	}
	*/

	/**
	 * Main constructor. {@code p} will be used for rendering purposes.
	 * <p>
	 * {@link #sceneCenter()} is set to (0,0,0) and {@link #sceneRadius()} is set
	 * to 100. {@link #type()} Camera.PERSPECTIVE, with a {@code PI/4}
	 * {@link #fieldOfView()}.
	 * <p>
	 * If {@code attachedToScene} is true then the Camera matrices are set as
	 * references to the processing camera matrices. Otherwise newly matrices are
	 * created. In both cases the matrices are computed according to remaining
	 * default Camera parameters.
	 * <p>
	 * See {@link #IODistance()}, {@link #physicalDistanceToScreen()},
	 * {@link #physicalScreenWidth()} and {@link #focusDistance()} documentations
	 * for default stereo parameter values. 
	 */
	public Camera(AbstractScene scn /**, boolean attachedToScene */) {
		scene = scn;
		//pg3d = scene.pg3d;
		//attachedToPCam = attachedToScene;
		
		enableFrustumEquationsUpdate(false);
		optimizeUnprojectCache(false);

		for (int i = 0; i < normal.length; i++)
			normal[i] = new Vector3D();

		fldOfView = (float) Math.PI / 4.0f;

		fpCoefficients = new float[6][4];

		// KeyFrames
		interpolationKfi = new KeyFrameInterpolator(scene, frame());
		kfi = new HashMap<Integer, KeyFrameInterpolator>();

		setFrame(new InteractiveCameraFrame(this));

		// Requires fieldOfView() to define focusDistance()
		setSceneRadius(100);

		// Initial value (only scaled after this)
		orthoCoef = (float) Math.tan(fieldOfView() / 2.0f);

		// Also defines the arcballReferencePoint(), which changes orthoCoef.
		setSceneCenter(new Vector3D(0.0f, 0.0f, 0.0f));

		setKind(Kind.PROSCENE);
		orthoSize = 1;// only for standard kind, but we initialize it here
		setStandardZNear(0.001f);// only for standard kind, but we initialize it
															// here
		setStandardZFar(1000.0f);// only for standard kind, but we initialize it
															// here

		// Requires fieldOfView() when called with ORTHOGRAPHIC. Attention to
		// projectionMat below.
		setType(Camera.Type.PERSPECTIVE);

		setZNearCoefficient(0.005f);
		setZClippingCoefficient((float) Math.sqrt(3.0f));

		// Dummy values
		setScreenWidthAndHeight(600, 400);

		// Stereo parameters
		setIODistance(0.062f);
		setPhysicalDistanceToScreen(0.5f);
		setPhysicalScreenWidth(0.4f);
		// focusDistance is set from setFieldOfView()

		/**
		if (isAttachedToP5Camera()) {
			projectionMat = pg3d.projection;
			modelViewMat = pg3d.modelview;
			computeProjectionMatrix();
			computeModelViewMatrix();
		} else {
		*/
			modelViewMat = new Matrix3D();
			projectionMat = new Matrix3D();
			projectionMat.set(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
			computeProjectionMatrix();
		//}
	}
	
	/**
	 * Copy constructor 
	 * 
	 * @param oCam the camera object to be copied
	 */
	protected Camera(Camera oCam) {
		this.scene = oCam.scene;
		//this.pg3d = oCam.pg3d;
		//this.attachedToPCam = oCam.attachedToPCam;
		
		this.fpCoefficientsUpdate = oCam.fpCoefficientsUpdate;
		this.unprojectCacheOptimized = oCam.unprojectCacheOptimized;
		
		for (int i = 0; i < normal.length; i++)
			this.normal[i] = new Vector3D(oCam.normal[i].vec[0], oCam.normal[i].vec[1], oCam.normal[i].vec[2] );
		
		this.fldOfView = oCam.fldOfView;
		
		this.fpCoefficients = new float[6][4];
		for (int i=0; i<6; i++)
			for (int j=0; j<4; j++)
				this.fpCoefficients[i][j] = oCam.fpCoefficients[i][j];
				
		this.frm = oCam.frame().get();		
		this.interpolationKfi = oCam.interpolationKfi.get();
		
		this.kfi = new HashMap<Integer, KeyFrameInterpolator>();
		
		itrtr = oCam.kfi.keySet().iterator();
		while (itrtr.hasNext()) {
			Integer key = itrtr.next();
			this.kfi.put(new Integer(key.intValue()), oCam.kfi.get(key).get());
		}
		
		this.setSceneRadius(oCam.sceneRadius());
		this.orthoCoef = oCam.orthoCoef;
		this.setSceneCenter( oCam.sceneCenter() );
		this.setKind(oCam.kind());
		this.orthoSize = oCam.orthoSize;
		this.setStandardZNear(oCam.standardZNear());
		this.setStandardZFar(oCam.standardZFar());
		this.setType(oCam.type());
		this.setZNearCoefficient(oCam.zNearCoefficient());
		this.setZClippingCoefficient(oCam.zClippingCoefficient());
		this.setScreenWidthAndHeight(oCam.screenWidth(), oCam.screenHeight());
		this.setIODistance( oCam.IODistance() );
		this.setPhysicalDistanceToScreen(oCam.physicalDistanceToScreen());
		this.setPhysicalScreenWidth( oCam.physicalScreenWidth() );		
		
		this.modelViewMat = new Matrix3D(oCam.modelViewMat);
		this.projectionMat = new Matrix3D(oCam.projectionMat);
	}
	
	/**
	 * Calls {@link #Camera(Camera)} (which is protected) and returns a copy of
	 * {@code this} object.
	 * 
	 * @see #Camera(Camera)
	 */	
	public Camera get() {
		return new Camera(this);
	}

	// 1. ATTACHED PCAMERA MATRICES

	/**
	 * Convenience function that simply returns {@code !isAttachedToScene()}
	 * 
	 * @see #isAttachedToP5Camera()
	 */
	
	/**
	public boolean isDetachedFromP5Camera() {
		return !isAttachedToP5Camera();
	}
	*/

	/**
	 * Returns {@code true} if the Camera matrices are set as references to the
	 * processing camera matrices and {@code false} if not.
	 * 
	 * @see #isDetachedFromP5Camera()
	 */
	
	/**
	public boolean isAttachedToP5Camera() {
		return attachedToPCam;
	}
	*/

	/**
	 * Set the Camera matrices as references to the processing camera matrices. If
	 * the references are already set ({@link #isAttachedToP5Camera()}), silently
	 * ignores the call.
	 * <p>
	 * <b>Note:</b> Since it is only one Scene per PApplet, there's no need to
	 * specify it.
	 * 
	 * @see #detachFromP5Camera()
	 * @see #isAttachedToP5Camera()
	 */
	
	/**
	public void attachToP5Camera() {
		if (!isAttachedToP5Camera()) {
			attachedToPCam = true;
			projectionMat = pg3d.projection;
			modelViewMat = pg3d.modelview;
			computeProjectionMatrix();
			computeModelViewMatrix();
		}
	}
	*/

	/**
	 * Create new independent Camera matrices' objects (i.e., Camera matrices are
	 * no longer set as references to processing camera matrices). If the Camera
	 * is already detached, silently ignores the call.
	 * <p>
	 * The values of the newly created matrices are set with
	 * {@link #computeProjectionMatrix()} and {@link #computeModelViewMatrix()}.
	 * 
	 * @see #attachToP5Camera()
	 * @see #isAttachedToP5Camera()
	 */
	
	/**
	public void detachFromP5Camera() {
		if (isAttachedToP5Camera()) {
			attachedToPCam = false;
			modelViewMat = new Matrix3D();
			projectionMat = new Matrix3D();
			projectionMat.set(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
			computeProjectionMatrix();
			computeModelViewMatrix();
		}
	}	
	*/

	// 2. POSITION AND ORIENTATION

	/**
	 * Returns the Camera position (the eye), defined in the world coordinate
	 * system.
	 * <p>
	 * Use {@link #setPosition(Vector3D)} to set the Camera position. Other
	 * convenient methods are showEntireScene() or fitSphere(). Actually returns
	 * {@link remixlab.remixcam.core.SimpleFrame#position()}.
	 * <p>
	 * This position corresponds to the projection center of a Camera.PERSPECTIVE
	 * camera. It is not located in the image plane, which is at a zNear()
	 * distance ahead.
	 */
	public final Vector3D position() {
		return frame().position();
	}

	/**
	 * Sets the Camera {@link #position()} (the eye), defined in the world
	 * coordinate system.
	 */
	public void setPosition(Vector3D pos) {
		frame().setPosition(pos);
	}

	/**
	 * Returns the normalized up vector of the Camera, defined in the world
	 * coordinate system.
	 * <p>
	 * Set using {@link #setUpVector(Vector3D)} or
	 * {@link #setOrientation(Quaternion)}. It is orthogonal to
	 * {@link #viewDirection()} and to {@link #rightVector()}.
	 * <p>
	 * It corresponds to the Y axis of the associated {@link #frame()} (actually
	 * returns {@code frame().yAxis()}
	 */
	public Vector3D upVector() {
		return frame().yAxis();
	}

	/**
	 * Convenience function that simply calls {@code setUpVector(up, true)}.
	 * 
	 * @see #setUpVector(Vector3D, boolean)
	 */
	public void setUpVector(Vector3D up) {
		setUpVector(up, true);
	}

	/**
	 * Rotates the Camera so that its {@link #upVector()} becomes {@code up}
	 * (defined in the world coordinate system).
	 * <p>
	 * The Camera is rotated around an axis orthogonal to {@code up} and to the
	 * current {@link #upVector()} direction.
	 * <p>
	 * Use this method in order to define the Camera horizontal plane.
	 * <p>
	 * When {@code noMove} is set to {@code false}, the orientation modification
	 * is compensated by a translation, so that the
	 * {@link #arcballReferencePoint()} stays projected at the same position on
	 * screen. This is especially useful when the Camera is an observer of the
	 * scene (default mouse binding).
	 * <p>
	 * When {@code noMove} is true, the Camera {@link #position()} is left
	 * unchanged, which is an intuitive behavior when the Camera is in a
	 * walkthrough fly mode.
	 * 
	 * @see #setViewDirection(Vector3D)
	 * @see #lookAt(Vector3D)
	 * @see #setOrientation(Quaternion)
	 */
	public void setUpVector(Vector3D up, boolean noMove) {
		Quaternion q = new Quaternion(new Vector3D(0.0f, 1.0f, 0.0f), frame().transformOf(up));

		if (!noMove)
			frame().setPosition(
					Vector3D.sub(arcballReferencePoint(), (Quaternion.multiply(frame()
							.orientation(), q)).rotate(frame().coordinatesOf(
							arcballReferencePoint()))));

		frame().rotate(q);

		// Useful in fly mode to keep the horizontal direction.
		frame().updateFlyUpVector();
	}

	/**
	 * Returns the normalized view direction of the Camera, defined in the world
	 * coordinate system.
	 * <p>
	 * Change this value using {@link #setViewDirection(Vector3D)},
	 * {@link #lookAt(Vector3D)} or {@link #setOrientation(Quaternion)}. It is
	 * orthogonal to {@link #upVector()} and to {@link #rightVector()}.
	 * <p>
	 * This corresponds to the negative Z axis of the {@link #frame()} ( {@code
	 * frame().inverseTransformOf(new Vector3D(0.0f, 0.0f, -1.0f))} ).
	 */
	public Vector3D viewDirection() {
		return frame().inverseTransformOf(new Vector3D(0.0f, 0.0f, -1.0f));
	}

	/**
	 * Rotates the Camera so that its {@link #viewDirection()} is {@code
	 * direction} (defined in the world coordinate system).
	 * <p>
	 * The Camera {@link #position()} is not modified. The Camera is rotated so
	 * that the horizon (defined by its {@link #upVector()}) is preserved.
	 * 
	 * @see #lookAt(Vector3D)
	 * @see #setUpVector(Vector3D)
	 */
	public void setViewDirection(Vector3D direction) {
		if (direction.squaredNorm() < 1E-10)
			return;

		Vector3D xAxis = direction.cross(upVector());
		if (xAxis.squaredNorm() < 1E-10) {
			// target is aligned with upVector, this means a rotation around X
			// axis
			// X axis is then unchanged, let's keep it !
			xAxis = frame().inverseTransformOf(new Vector3D(1.0f, 0.0f, 0.0f));
		}

		Quaternion q = new Quaternion();
		q.fromRotatedBasis(xAxis, xAxis.cross(direction), Vector3D.mult(direction,	-1));
		frame().setOrientationWithConstraint(q);
	}

	/**
	 * Returns the normalized right vector of the Camera, defined in the world
	 * coordinate system.
	 * <p>
	 * This vector lies in the Camera horizontal plane, directed along the X axis
	 * (orthogonal to {@link #upVector()} and to {@link #viewDirection()}. Set
	 * using {@link #setUpVector(Vector3D)}, {@link #lookAt(Vector3D)} or
	 * {@link #setOrientation(Quaternion)}.
	 * <p>
	 * Simply returns {@code frame().xAxis()}.
	 */
	public Vector3D rightVector() {
		return frame().xAxis();
	}

	/**
	 * Returns the Camera orientation, defined in the world coordinate system.
	 * <p>
	 * Actually returns {@code frame().orientation()}. Use
	 * {@link #setOrientation(Quaternion)}, {@link #setUpVector(Vector3D)} or
	 * {@link #lookAt(Vector3D)} to set the Camera orientation.
	 */
	public Quaternion orientation() {
		return frame().orientation();
	}

	/**
	 * Sets the {@link #orientation()} of the Camera using polar coordinates.
	 * <p>
	 * {@code theta} rotates the Camera around its Y axis, and then {@code phi}
	 * rotates it around its X axis.
	 * <p>
	 * The polar coordinates are defined in the world coordinates system: {@code
	 * theta = phi = 0} means that the Camera is directed towards the world Z
	 * axis. Both angles are expressed in radians.
	 * <p>
	 * The {@link #position()} of the Camera is unchanged, you may want to call
	 * {@link #showEntireScene()} after this method to move the Camera.
	 * 
	 * @see #setUpVector(Vector3D)
	 */
	public void setOrientation(float theta, float phi) {
		// TODO: need check.
		Vector3D axis = new Vector3D(0.0f, 1.0f, 0.0f);
		Quaternion rot1 = new Quaternion(axis, theta);
		axis.set(-(float) Math.cos(theta), 0.0f, (float) Math.sin(theta));
		Quaternion rot2 = new Quaternion(axis, phi);
		setOrientation(Quaternion.multiply(rot1, rot2));
	}

	/**
	 * Sets the Camera {@link #orientation()}, defined in the world coordinate
	 * system.
	 */
	public void setOrientation(Quaternion q) {
		frame().setOrientation(q);
		frame().updateFlyUpVector();
	}

	// 3. FRUSTUM

	/**
	 * Returns the Camera.Type of the Camera.
	 * <p>
	 * Set by {@link #setType(Type)}.
	 * <p>
	 * A {@link remixlab.remixcam.core.Camera.Type#PERSPECTIVE} Camera uses a classical
	 * projection mainly defined by its {@link #fieldOfView()}.
	 * <p>
	 * With a {@link remixlab.remixcam.core.Camera.Type#ORTHOGRAPHIC} {@link #type()},
	 * the {@link #fieldOfView()} is meaningless and the width and height of the
	 * Camera frustum are inferred from the distance to the
	 * {@link #arcballReferencePoint()} using {@link #getOrthoWidthHeight()}.
	 * <p>
	 * Both types use {@link #zNear()} and {@link #zFar()} (to define their
	 * clipping planes) and {@link #aspectRatio()} (for frustum shape).
	 */
	public final Type type() {
		return tp;
	}

	/**
	 * Returns the kind of the Camera: PROSCENE or STANDARD.
	 */
	public final Kind kind() {
		return knd;
	}

	/**
	 * Sets the kind of the Camera: PROSCENE or STANDARD.
	 */
	public void setKind(Kind k) {
		if(k!=knd)
			lastFrameUpdate = scene.frameCount();
		knd = k;		
	}

	/**
	 * Sets the value of the {@link #zNear()}. Meaningful only when the Camera
	 * {@link #kind()} is STANDARD. This value is set to 0.001 by default.
	 * 
	 * @see #zNear()
	 * @see #zFar()
	 */
	public void setStandardZNear(float zN) {
		if( (kind() == Camera.Kind.STANDARD) && (zN != stdZNear) )
			lastFrameUpdate = scene.frameCount();
		stdZNear = zN;
	}

	/**
	 * Returns the value of the {@link #zNear()}. Meaningful only when the Camera
	 * {@link #kind()} is STANDARD.
	 * 
	 * @see #zNear()
	 * @see #zFar()
	 */
	public float standardZNear() {
		return stdZNear;
	}

	/**
	 * Sets the value of the {@link #zFar()}. Meaningful only when the Camera
	 * {@link #kind()} is STANDARD. This value is set to 1000 by default.
	 * 
	 * @see #zNear()
	 * @see #zFar()
	 */
	public void setStandardZFar(float zF) {
		if( (kind() == Camera.Kind.STANDARD) && (zF != stdZFar) )
			lastFrameUpdate = scene.frameCount();
		stdZFar = zF;
	}

	/**
	 * Returns the value of the {@link #zFar()}. Meaningful only when the Camera
	 * {@link #kind()} is STANDARD.
	 * 
	 * @see #zNear()
	 * @see #zFar()
	 */
	public float standardZFar() {
		return stdZFar;
	}

	/**
	 * Changes the size of the frustum. If {@code augment} is true the frustum
	 * size is augmented, otherwise it is diminished. Meaningful only when the
	 * Camera {@link #kind()} is STANDARD and the Camera {@link #type()} is
	 * ORTHOGRAPHIC.
	 * 
	 * @see #standardOrthoFrustumSize()
	 */
	public void changeStandardOrthoFrustumSize(boolean augment) {
		if( (kind() == Camera.Kind.STANDARD) && (type() == Camera.Type.ORTHOGRAPHIC) )
			lastFrameUpdate = scene.frameCount();
		if (augment)
			orthoSize *= 1.01f;
		else
			orthoSize /= 1.01f;
	}

	/**
	 * Returns the frustum size. This value is used to set
	 * {@link #getOrthoWidthHeight()}. Meaningful only when the Camera
	 * {@link #kind()} is STANDARD and the Camera {@link #type()} is ORTHOGRAPHIC.
	 * 
	 * @see #getOrthoWidthHeight()
	 */
	public float standardOrthoFrustumSize() {
		return orthoSize;
	}

	/**
	 * Defines the Camera {@link #type()}.
	 * <p>
	 * Changing the Camera Type alters the viewport and the objects' size can be
	 * changed. This method guarantees that the two frustum match in a plane
	 * normal to {@link #viewDirection()}, passing through the arcball reference
	 * point.
	 */
	public final void setType(Type type) {
		// make ORTHOGRAPHIC frustum fit PERSPECTIVE (at least in plane normal
		// to viewDirection(), passing
		// through RAP). Done only when CHANGING type since orthoCoef may have
		// been changed with a
		// setArcballReferencePoint in the meantime.
		if( type != type() )
			lastFrameUpdate = scene.frameCount();
		if ((type == Camera.Type.ORTHOGRAPHIC) && (type() == Camera.Type.PERSPECTIVE))
			orthoCoef = (float) Math.tan(fieldOfView() / 2.0f);

		this.tp = type;
	}

	/**
	 * Returns the vertical field of view of the Camera (in radians).
	 * <p>
	 * Value is set using {@link #setFieldOfView(float)}. Default value is pi/4
	 * radians. This value is meaningless if the Camera {@link #type()} is
	 * {@link remixlab.remixcam.core.Camera.Type#ORTHOGRAPHIC}.
	 * <p>
	 * The field of view corresponds the one used in {@code gluPerspective} (see
	 * manual). It sets the Y (vertical) aperture of the Camera. The X
	 * (horizontal) angle is inferred from the window aspect ratio (see
	 * {@link #aspectRatio()} and {@link #horizontalFieldOfView()}).
	 * <p>
	 * Use {@link #setFOVToFitScene()} to adapt the {@link #fieldOfView()} to a
	 * given scene.
	 */
	public float fieldOfView() {
		return fldOfView;
	}

	/**
	 * Sets the vertical {@link #fieldOfView()} of the Camera (in radians).
	 * <p>
	 * Note that {@link #focusDistance()} is set to {@link #sceneRadius()} / tan(
	 * {@link #fieldOfView()}/2) by this method.
	 */
	public void setFieldOfView(float fov) {
		fldOfView = fov;
		setFocusDistance(sceneRadius() / (float) Math.tan(fov / 2.0f));
	}

	/**
	 * Changes the Camera {@link #fieldOfView()} so that the entire scene (defined
	 * by {@link remixlab.remixcam.core.AbstractScene#center()} and 
	 * {@link remixlab.remixcam.core.AbstractScene#radius()} is visible from the Camera
	 * {@link #position()}.
	 * <p>
	 * The {@link #position()} and {@link #orientation()} of the Camera are not
	 * modified and you first have to orientate the Camera in order to actually
	 * see the scene (see {@link #lookAt(Vector3D)}, {@link #showEntireScene()} or
	 * {@link #fitSphere(Vector3D, float)}).
	 * <p>
	 * This method is especially useful for <i>shadow maps</i> computation. Use
	 * the Camera positioning tools ({@link #setPosition(Vector3D)},
	 * {@link #lookAt(Vector3D)}) to position a Camera at the light position. Then
	 * use this method to define the {@link #fieldOfView()} so that the shadow map
	 * resolution is optimally used:
	 * <p>
	 * {@code // The light camera needs size hints in order to optimize its
	 * fieldOfView} <br>
	 * {@code lightCamera.setSceneRadius(sceneRadius());} <br>
	 * {@code lightCamera.setSceneCenter(sceneCenter());} <br>
	 * {@code // Place the light camera} <br>
	 * {@code lightCamera.setPosition(lightFrame.position());} <br>
	 * {@code lightCamera.lookAt(sceneCenter());} <br>
	 * {@code lightCamera.setFOVToFitScene();} <br>
	 * <p>
	 * <b>Attention:</b> The {@link #fieldOfView()} is clamped to M_PI/2.0. This
	 * happens when the Camera is at a distance lower than sqrt(2.0) *
	 * sceneRadius() from the sceneCenter(). It optimizes the shadow map
	 * resolution, although it may miss some parts of the scene.
	 */
	public void setFOVToFitScene() {
		if (distanceToSceneCenter() > (float) Math.sqrt(2.0f) * sceneRadius())
			setFieldOfView(2.0f * (float) Math.asin(sceneRadius() / distanceToSceneCenter()));
		else
			setFieldOfView((float) Math.PI / 2.0f);
	}

	/**
	 * Convenience function that simply returns {@code getOrthoWidthHeight(new
	 * float[2])}.
	 * 
	 * @see #getOrthoWidthHeight(float[])
	 */
	public float[] getOrthoWidthHeight() {
		return getOrthoWidthHeight(new float[2]);
	}

	/**
	 * Fills in {@code target} with the {@code halfWidth} and {@code halfHeight}
	 * of the Camera orthographic frustum and returns it.
	 * <p>
	 * While {@code target[0]} holds {@code halfWidth}, {@code target[1]} holds
	 * {@code halfHeight}.
	 * <p>
	 * These values are only valid and used when the Camera is of {@link #type()}
	 * ORTHOGRAPHIC and they are expressed in processing scene units.
	 * <p>
	 * When the Camera {@link #kind()} is PROSCENE, these values are proportional
	 * to the Camera (z projected) distance to the
	 * {@link #arcballReferencePoint()}. When zooming on the object, the Camera is
	 * translated forward \e and its frustum is narrowed, making the object appear
	 * bigger on screen, as intuitively expected.
	 * <p>
	 * When the Camera {@link #kind()} is STANDARD, these values are defined as
	 * {@link #sceneRadius()} * {@link #standardOrthoFrustumSize()}.
	 * <p>
	 * Overload this method to change this behavior if desired.
	 */
	public float[] getOrthoWidthHeight(float[] target) {
		if ((target == null) || (target.length != 2)) {
			target = new float[2];
		}

		if (kind() == Kind.STANDARD) {
			float dist = sceneRadius() * standardOrthoFrustumSize();
			// 1. halfWidth
			target[0] = dist * ((aspectRatio() < 1.0f) ? 1.0f : aspectRatio());
			// 2. halfHeight
			target[1] = dist * ((aspectRatio() < 1.0f) ? 1.0f / aspectRatio() : 1.0f);
		} else {
			float dist = orthoCoef
					* Math.abs(cameraCoordinatesOf(arcballReferencePoint()).vec[2]);
			// #CONNECTION# fitScreenRegion
			// 1. halfWidth
			target[0] = dist * ((aspectRatio() < 1.0f) ? 1.0f : aspectRatio());
			// 2. halfHeight
			target[1] = dist * ((aspectRatio() < 1.0f) ? 1.0f / aspectRatio() : 1.0f);
		}

		return target;
	}

	/**
	 * Returns the horizontal field of view of the Camera (in radians).
	 * <p>
	 * Value is set using {@link #setHorizontalFieldOfView(float)} or
	 * {@link #setFieldOfView(float)}. These values are always linked by: {@code
	 * horizontalFieldOfView() = 2.0 * atan ( tan(fieldOfView()/2.0) *
	 * aspectRatio() )}.
	 */
	public float horizontalFieldOfView() {
		return 2.0f * (float) Math.atan((float) Math.tan(fieldOfView() / 2.0f) * aspectRatio());
	}

	/**
	 * Sets the {@link #horizontalFieldOfView()} of the Camera (in radians).
	 * <p>
	 * {@link #horizontalFieldOfView()} and {@link #fieldOfView()} are linked by
	 * the {@link #aspectRatio()}. This method actually calls {@code
	 * setFieldOfView(( 2.0 * atan (tan(hfov / 2.0) / aspectRatio()) ))} so that a
	 * call to {@link #horizontalFieldOfView()} returns the expected value.
	 */
	public void setHorizontalFieldOfView(float hfov) {
		setFieldOfView(2.0f * (float) Math.atan((float) Math.tan(hfov / 2.0f) / aspectRatio()));
	}

	/**
	 * Returns the Camera aspect ratio defined by {@link #screenWidth()} /
	 * {@link #screenHeight()}.
	 * <p>
	 * When the Camera is attached to a Scene, these values and hence the
	 * aspectRatio() are automatically fitted to the viewer's window aspect ratio
	 * using setScreenWidthAndHeight().
	 */
	public float aspectRatio() {
		return (float) scrnWidth / (float) scrnHeight;
	}

	/**
	 * Defines the Camera {@link #aspectRatio()}.
	 * <p>
	 * This value is actually inferred from the {@link #screenWidth()} /
	 * {@link #screenHeight()} ratio. You should use
	 * {@link #setScreenWidthAndHeight(int, int)} instead.
	 * <p>
	 * This method might however be convenient when the Camera is not associated
	 * with a Scene. It actually sets the {@link #screenHeight()} to 100 and the
	 * {@link #screenWidth()} accordingly. See also {@link #setFOVToFitScene()}.
	 */
	public void setAspectRatio(float aspect) {
		setScreenWidthAndHeight((int) (100.0 * aspect), 100);
	}

	/**
	 * Sets Camera {@link #screenWidth()} and {@link #screenHeight()} (expressed
	 * in pixels).
	 * <p>
	 * You should not call this method when the Camera is associated with a Scene,
	 * since the latter automatically updates these values when it is resized
	 * (hence overwriting your values).
	 * <p>
	 * Non-positive dimension are silently replaced by a 1 pixel value to ensure
	 * frustum coherence.
	 * <p>
	 * If your Camera is used without a Scene (offscreen rendering, shadow maps),
	 * use {@link #setAspectRatio(float)} instead to define the projection matrix.
	 */
	public void setScreenWidthAndHeight(int width, int height) {
		// Prevent negative and zero dimensions that would cause divisions by zero.
		if( (width != scrnWidth) && (height != scrnHeight) )
			lastFrameUpdate = scene.frameCount();
		scrnWidth = width > 0 ? width : 1;
		scrnHeight = height > 0 ? height : 1;
	}

	/**
	 * Returns the width (in pixels) of the Camera screen.
	 * <p>
	 * Set using {@link #setScreenWidthAndHeight(int, int)}. This value is
	 * automatically fitted to the Scene's window dimensions when the Camera is
	 * attached to a Scene.
	 */
	public final int screenWidth() {
		return scrnWidth;
	}

	/**
	 * Returns the height (in pixels) of the Camera screen.
	 * <p>
	 * Set using {@link #setScreenWidthAndHeight(int, int)}. This value is
	 * automatically fitted to the Scene's window dimensions when the Camera is
	 * attached to a Scene.
	 */
	public final int screenHeight() {
		return scrnHeight;
	}

	/**
	 * Convenience function that simply calls {@code return}
	 * {@link #getViewport(int[])}.
	 */
	public int[] getViewport() {
		return getViewport(new int[4]);
	}

	/**
	 * Fills {@code viewport} with the Camera viewport and returns it. If viewport
	 * is null (or not the correct size), a new array will be created.
	 * <p>
	 * This method is mainly used in conjunction with
	 * {@code project(float, float, float, Matrix3D, Matrix3D, int[], float[])}
	 * , which requires such a viewport. Returned values are (0,
	 * {@link #screenHeight()}, {@link #screenWidth()}, -{@link #screenHeight()}),
	 * so that the origin is located in the upper left corner of the window.
	 */
	public int[] getViewport(int[] viewport) {
		if ((viewport == null) || (viewport.length != 4)) {
			viewport = new int[4];
		}
		viewport[0] = 0;
		viewport[1] = screenHeight();
		viewport[2] = screenWidth();
		viewport[3] = -screenHeight();
		return viewport;
	}

	/**
	 * Returns the near clipping plane distance used by the Camera projection
	 * matrix. The returned value depends on the Camera {@link #kind()}:
	 * <p>
	 * <h3>1. If the camera kind is PROSCENE</h3>
	 * The clipping planes' positions depend on the {@link #sceneRadius()} and
	 * {@link #sceneCenter()} rather than being fixed small-enough and
	 * large-enough values. A good scene dimension approximation will hence result
	 * in an optimal precision of the z-buffer.
	 * <p>
	 * The near clipping plane is positioned at a distance equal to
	 * {@link #zClippingCoefficient()} * {@link #sceneRadius()} in front of the
	 * {@link #sceneCenter()}: {@code distanceToSceneCenter() -
	 * zClippingCoefficient() * sceneRadius()}
	 * <p>
	 * In order to prevent negative or too small {@link #zNear()} values (which
	 * would degrade the z precision), {@link #zNearCoefficient()} is used when
	 * the Camera is inside the {@link #sceneRadius()} sphere:
	 * <p>
	 * {@code zMin = zNearCoefficient() * zClippingCoefficient() * sceneRadius();}
	 * <br>
	 * {@code zNear = zMin;}<br>
	 * {@code // With an ORTHOGRAPHIC type, the value is simply clamped to 0.0}
	 * <br>
	 * <p>
	 * See also the {@link #zFar()}, {@link #zClippingCoefficient()} and
	 * {@link #zNearCoefficient()} documentations.
	 * <p>
	 * <h3>2. If the camera kind is STANDARD</h3>
	 * Simply returns {@link #standardZNear()}
	 * <P>
	 * If you need a completely different zNear computation, overload the
	 * {@link #zNear()} and {@link #zFar()} methods in a new class that publicly
	 * inherits from Camera and use
	 * {@link remixlab.remixcam.core.AbstractScene#setCamera(Camera)}.
	 * <p>
	 * <b>Attention:</b> The value is always positive although the clipping plane
	 * is positioned at a negative z value in the Camera coordinate system. This
	 * follows the {@code gluPerspective} standard.
	 * 
	 * @see #zFar()
	 */
	public float zNear() {
		if (kind() == Kind.STANDARD)
			return standardZNear();

		float z = distanceToSceneCenter() - zClippingCoefficient() * sceneRadius();

		// Prevents negative or null zNear values.
		final float zMin = zNearCoefficient() * zClippingCoefficient()
				* sceneRadius();
		if (z < zMin)
			switch (type()) {
			case PERSPECTIVE:
				z = zMin;
				break;
			case ORTHOGRAPHIC:
				z = 0.0f;
				break;
			}
		return z;
	}

	/**
	 * Returns the far clipping plane distance used by the Camera projection
	 * matrix. The returned value depends on the Camera {@link #kind()}:
	 * <p>
	 * <h3>1. If the camera kind is PROSCENE</h3>
	 * The far clipping plane is positioned at a distance equal to {@code
	 * zClippingCoefficient() * sceneRadius()} behind the {@link #sceneCenter()}:
	 * <p>
	 * {@code zFar = distanceToSceneCenter() +
	 * zClippingCoefficient()*sceneRadius()}<br>
	 * <h3>2. If the camera kind is STANDARD</h3>
	 * Simply returns {@link #standardZFar()}.
	 * 
	 * @see #zNear()
	 */
	public float zFar() {
		if (kind() == Kind.STANDARD)
			return this.standardZFar();
		return distanceToSceneCenter() + zClippingCoefficient() * sceneRadius();
	}

	/**
	 * Returns the coefficient which is used to set {@link #zNear()} when the
	 * Camera is inside the sphere defined by {@link #sceneCenter()} and
	 * {@link #zClippingCoefficient()} * {@link #sceneRadius()}.
	 * <p>
	 * In that case, the {@link #zNear()} value is set to {@code
	 * zNearCoefficient() * zClippingCoefficient() * sceneRadius()}. See the
	 * {@code zNear()} documentation for details.
	 * <p>
	 * Default value is 0.005, which is appropriate for most applications. In case
	 * you need a high dynamic ZBuffer precision, you can increase this value
	 * (~0.1). A lower value will prevent clipping of very close objects at the
	 * expense of a worst Z precision.
	 * <p>
	 * Only meaningful when Camera type is PERSPECTIVE.
	 */
	public float zNearCoefficient() {
		return zNearCoef;
	}

	/**
	 * Sets the {@link #zNearCoefficient()} value.
	 */
	public void setZNearCoefficient(float coef) {
		if(coef != zNearCoef)
			lastFrameUpdate = scene.frameCount();
		zNearCoef = coef;
	}

	/**
	 * Returns the coefficient used to position the near and far clipping planes.
	 * <p>
	 * The near (resp. far) clipping plane is positioned at a distance equal to
	 * {@code zClippingCoefficient() * sceneRadius()} in front of (resp. behind)
	 * the {@link #sceneCenter()}. This guarantees an optimal use of the z-buffer
	 * range and minimizes aliasing. See the {@link #zNear()} and {@link #zFar()}
	 * documentations.
	 * <p>
	 * Default value is square root of 3.0 (so that a cube of size
	 * {@link #sceneRadius()} is not clipped).
	 * <p>
	 * However, since the {@link #sceneRadius()} is used for other purposes (see
	 * showEntireScene(), flySpeed(), ...) and you may want to change this value
	 * to define more precisely the location of the clipping planes. See also
	 * {@link #zNearCoefficient()}.
	 */
	public float zClippingCoefficient() {
		return zClippingCoef;
	}

	/**
	 * Sets the {@link #zClippingCoefficient()} value.
	 */
	public void setZClippingCoefficient(float coef) {
		if(coef != zClippingCoef)
			lastFrameUpdate = scene.frameCount();
		zClippingCoef = coef;
	}

	/**
	 * Returns the ratio between pixel and processing scene units at {@code
	 * position}.
	 * <p>
	 * A line of {@code n * pixelP5Ratio()} processing scene units, located at
	 * {@code position} in the world coordinates system, will be projected with a
	 * length of {@code n} pixels on screen.
	 * <p>
	 * Use this method to scale objects so that they have a constant pixel size on
	 * screen. The following code will draw a 20 pixel line, starting at
	 * {@link #sceneCenter()} and always directed along the screen vertical
	 * direction:
	 * <p>
	 * {@code beginShape(LINES);}<br>
	 * {@code vertex(sceneCenter().x, sceneCenter().y, sceneCenter().z);}<br>
	 * {@code Vector3D v = Vector3D.add(sceneCenter(), Vector3D.mult(upVector(), 20 *
	 * pixelP5Ratio(sceneCenter())));}<br>
	 * {@code vertex(v.x, v.y, v.z);}<br>
	 * {@code endShape();}<br>
	 */
	public float pixelP5Ratio(Vector3D position) {
		switch (type()) {
		case PERSPECTIVE:
			return 2.0f * Math.abs((frame().coordinatesOf(position)).vec[2])
					* (float) Math.tan(fieldOfView() / 2.0f) / screenHeight();
		case ORTHOGRAPHIC: {
			float[] wh = getOrthoWidthHeight();
			return 2.0f * wh[1] / screenHeight();
		}
		}
		return 1.0f;
	}

	/**
	 * Returns the signed distance between point {@code pos} and plane {@code
	 * index}. The distance is negative if the point lies in the planes's frustum
	 * halfspace, and positive otherwise.
	 * <p>
	 * {@code index} is a value between {@code 0} and {@code 5} which respectively
	 * correspond to the left, right, near, far, top and bottom Camera frustum
	 * planes.
	 * <p>
	 * <b>Attention:</b> The camera frustum plane equations should be updated
	 * before calling this method. You may compute them explicitly (by calling
	 * {@link #computeFrustumEquations()} ) or enable them to be automatic updated
	 * in your Scene setup (with
	 * {@link remixlab.remixcam.core.AbstractScene#enableFrustumEquationsUpdate()}).
	 * 
	 * @see #pointIsVisible(Vector3D)
	 * @see #sphereIsVisible(Vector3D, float)
	 * @see #aaBoxIsVisible(Vector3D, Vector3D)
	 * @see #computeFrustumEquations()
	 * @see #updateFrustumEquations()
	 * @see #getFrustumEquations()
	 * @see remixlab.remixcam.core.AbstractScene#enableFrustumEquationsUpdate()
	 */
	public float distanceToFrustumPlane(int index, Vector3D pos) {
		if (!scene.frustumEquationsUpdateIsEnable())
			System.out.println("The camera frustum plane equations (needed by distanceToFrustumPlane) may be outdated. Please "
							+ "enable automatic updates of the equations in your PApplet.setup "
							+ "with Scene.enableFrustumEquationsUpdate()");
		Vector3D myVec = new Vector3D(fpCoefficients[index][0],
				fpCoefficients[index][1], fpCoefficients[index][2]);
		return Vector3D.dot(pos, myVec) - fpCoefficients[index][3];
	}

	/**
	 * Returns {@code true} if {@code point} is visible (i.e, lies within the
	 * frustum) and {@code false} otherwise.
	 * <p>
	 * <b>Attention:</b> The camera frustum plane equations should be updated
	 * before calling this method. You may compute them explicitly (by calling
	 * {@link #computeFrustumEquations()} ) or enable them to be automatic updated
	 * in your Scene setup (with
	 * {@link remixlab.remixcam.core.AbstractScene#enableFrustumEquationsUpdate()}).
	 * 
	 * @see #distanceToFrustumPlane(int, Vector3D)
	 * @see #sphereIsVisible(Vector3D, float)
	 * @see #aaBoxIsVisible(Vector3D, Vector3D)
	 * @see #computeFrustumEquations()
	 * @see #updateFrustumEquations()
	 * @see #getFrustumEquations()
	 * @see remixlab.remixcam.core.AbstractScene#enableFrustumEquationsUpdate()
	 */
	public boolean pointIsVisible(Vector3D point) {
		if (!scene.frustumEquationsUpdateIsEnable())
			System.out.println("The camera frustum plane equations (needed by pointIsVisible) may be outdated. Please "
							+ "enable automatic updates of the equations in your PApplet.setup "
							+ "with Scene.enableFrustumEquationsUpdate()");
		for (int i = 0; i < 6; ++i)
			if (distanceToFrustumPlane(i, point) > 0)
				return false;
		return true;
	}

	/**
	 * Returns {@link remixlab.remixcam.core.Camera.Visibility#VISIBLE},
	 * {@link remixlab.remixcam.core.Camera.Visibility#INVISIBLE}, or
	 * {@link remixlab.remixcam.core.Camera.Visibility#SEMIVISIBLE}, depending whether
	 * the sphere (of radius {@code radius} and center {@code center}) is visible,
	 * invisible, or semi-visible, respectively.
	 * <p>
	 * <b>Attention:</b> The camera frustum plane equations should be updated
	 * before calling this method. You may compute them explicitly (by calling
	 * {@link #computeFrustumEquations()} ) or enable them to be automatic updated
	 * in your Scene setup (with
	 * {@link remixlab.remixcam.core.AbstractScene#enableFrustumEquationsUpdate()}).
	 * 
	 * @see #distanceToFrustumPlane(int, Vector3D)
	 * @see #pointIsVisible(Vector3D)
	 * @see #aaBoxIsVisible(Vector3D, Vector3D)
	 * @see #computeFrustumEquations()
	 * @see #updateFrustumEquations()
	 * @see #getFrustumEquations()
	 * @see remixlab.remixcam.core.AbstractScene#enableFrustumEquationsUpdate()
	 */
	public Visibility sphereIsVisible(Vector3D center, float radius) {
		if (!scene.frustumEquationsUpdateIsEnable())
			System.out.println("The camera frustum plane equations (needed by sphereIsVisible) may be outdated. Please "
							+ "enable automatic updates of the equations in your PApplet.setup "
							+ "with Scene.enableFrustumEquationsUpdate()");
		boolean allInForAllPlanes = true;
		for (int i = 0; i < 6; ++i) {
			float d = distanceToFrustumPlane(i, center);
			if (d > radius)
				return Camera.Visibility.INVISIBLE;
			if ((d > 0) || (-d < radius))
				allInForAllPlanes = false;
		}
		if(allInForAllPlanes)
			return Camera.Visibility.VISIBLE;
		return Camera.Visibility.SEMIVISIBLE;
	}

	/**
	 * Returns {@link remixlab.remixcam.core.Camera.Visibility#VISIBLE},
	 * {@link remixlab.remixcam.core.Camera.Visibility#INVISIBLE}, or
	 * {@link remixlab.remixcam.core.Camera.Visibility#SEMIVISIBLE}, depending whether
	 * the axis aligned box (defined by corners {@code p1} and {@code p2}) is
	 * visible, invisible, or semi-visible, respectively.
	 * <p>
	 * <b>Attention:</b> The camera frustum plane equations should be updated
	 * before calling this method. You may compute them explicitly (by calling
	 * {@link #computeFrustumEquations()} ) or enable them to be automatic updated
	 * in your Scene setup (with
	 * {@link remixlab.remixcam.core.AbstractScene#enableFrustumEquationsUpdate()}).
	 * 
	 * @see #distanceToFrustumPlane(int, Vector3D)
	 * @see #pointIsVisible(Vector3D)
	 * @see #sphereIsVisible(Vector3D, float)
	 * @see #computeFrustumEquations()
	 * @see #updateFrustumEquations()
	 * @see #getFrustumEquations()
	 * @see remixlab.remixcam.core.AbstractScene#enableFrustumEquationsUpdate()
	 */
	public Visibility aaBoxIsVisible(Vector3D p1, Vector3D p2) {
		if (!scene.frustumEquationsUpdateIsEnable())
			System.out.println("The camera frustum plane equations (needed by aaBoxIsVisible) may be outdated. Please "
							+ "enable automatic updates of the equations in your PApplet.setup "
							+ "with Scene.enableFrustumEquationsUpdate()");
		boolean allInForAllPlanes = true;
		for (int i = 0; i < 6; ++i) {
			boolean allOut = true;
			for (int c = 0; c < 8; ++c) {
				Vector3D pos = new Vector3D(((c & 4) != 0) ? p1.vec[0] : p2.vec[0],
						((c & 2) != 0) ? p1.vec[1] : p2.vec[1], ((c & 1) != 0) ? p1.vec[2] : p2.vec[2]);
				if (distanceToFrustumPlane(i, pos) > 0.0)
					allInForAllPlanes = false;
				else
					allOut = false;
			}
			// The eight points are on the outside side of this plane
			if (allOut)
				return Camera.Visibility.INVISIBLE;
		}

		if (allInForAllPlanes)
			return Camera.Visibility.VISIBLE;

		// Too conservative, but tangent cases are too expensive to detect
		return Camera.Visibility.SEMIVISIBLE;
	}

	/**
	 * Updates the frustum plane equations according to the current camera setup /
	 * {@link #type()}, {@link #position()}, {@link #orientation()},
	 * {@link #zNear()}, and {@link #zFar()} values), by simply calling
	 * {@link #computeFrustumEquations()}.
	 * <p>
	 * <b>Attention:</b> You should not call this method explicitly, unless you
	 * need the frustum equations to be updated only occasionally (rare). Use
	 * {@link remixlab.remixcam.core.AbstractScene#enableFrustumEquationsUpdate()} which
	 * automatically update the frustum equations every frame instead.
	 * 
	 * @see #distanceToFrustumPlane(int, Vector3D)
	 * @see #pointIsVisible(Vector3D)
	 * @see #sphereIsVisible(Vector3D, float)
	 * @see #aaBoxIsVisible(Vector3D, Vector3D)
	 * @see #computeFrustumEquations()
	 * @see #getFrustumEquations()
	 * @see remixlab.remixcam.core.AbstractScene#enableFrustumEquationsUpdate()
	 */
	public void updateFrustumEquations() {
		if( lastFrameUpdate != lastFPCoeficientsUpdateIssued )	{		  
			computeFrustumEquations(fpCoefficients);
			lastFPCoeficientsUpdateIssued = lastFrameUpdate;		  
		}
	}

	/**
	 * Returns the frustum plane equations.
	 * <p>
	 * The six 4-component vectors returned by this method, respectively
	 * correspond to the left, right, near, far, top and bottom Camera frustum
	 * planes. Each vector holds a plane equation of the form:
	 * <p>
	 * {@code a*x + b*y + c*z + d = 0}
	 * <p>
	 * where {@code a}, {@code b}, {@code c} and {@code d} are the 4 components of
	 * each vector, in that order.
	 * <p>
	 * <b>Attention:</b> The camera frustum plane equations should be updated
	 * before calling this method. You may compute them explicitly (by calling
	 * {@link #computeFrustumEquations()} ) or enable them to be automatic updated
	 * in your Scene setup (with
	 * {@link remixlab.remixcam.core.AbstractScene#enableFrustumEquationsUpdate()}).
	 * 
	 * @see #distanceToFrustumPlane(int, Vector3D)
	 * @see #pointIsVisible(Vector3D)
	 * @see #sphereIsVisible(Vector3D, float)
	 * @see #aaBoxIsVisible(Vector3D, Vector3D)
	 * @see #computeFrustumEquations()
	 * @see #updateFrustumEquations()
	 * @see remixlab.remixcam.core.AbstractScene#enableFrustumEquationsUpdate()
	 */
	public float[][] getFrustumEquations() {
		if (!scene.frustumEquationsUpdateIsEnable())
			System.out.println("The camera frustum plane equations may be outdated. Please "
							+ "enable automatic updates of the equations in your PApplet.setup "
							+ "with Scene.enableFrustumEquationsUpdate()");
		return fpCoefficients;
	}

	/**
	 * Convenience function that simply returns {@code
	 * computeFrustumPlanesCoefficients(new float [6][4])}
	 * <p>
	 * <b>Attention:</b> You should not call this method explicitly, unless you
	 * need the frustum equations to be updated only occasionally (rare). Use
	 * {@link remixlab.remixcam.core.AbstractScene#enableFrustumEquationsUpdate()} which
	 * automatically update the frustum equations every frame instead.
	 * 
	 * @see #computeFrustumEquations(float[][])
	 */
	public float[][] computeFrustumEquations() {
		return computeFrustumEquations(new float[6][4]);
	}

	/**
	 * Fills {@code coef} with the 6 plane equations of the camera frustum and
	 * returns it.
	 * <p>
	 * The six 4-component vectors of {@code coef} respectively correspond to the
	 * left, right, near, far, top and bottom Camera frustum planes. Each vector
	 * holds a plane equation of the form:
	 * <p>
	 * {@code a*x + b*y + c*z + d = 0}
	 * <p>
	 * where {@code a}, {@code b}, {@code c} and {@code d} are the 4 components of
	 * each vector, in that order.
	 * <p>
	 * This format is compatible with the {@code gl.glClipPlane()} function. One
	 * camera frustum plane can hence be applied in an other viewer to visualize
	 * the culling results:
	 * <p>
	 * {@code // Retrieve place equations}<br>
	 * {@code float [][] coef =
	 * mainViewer.camera().getFrustumPlanesCoefficients();}<br>
	 * {@code
	 * // These two additional clipping planes (which must have been enabled)}<br>
	 * {@code // will reproduce the mainViewer's near and far clipping.}<br>
	 * {@code gl.glClipPlane(GL.GL_CLIP_PLANE0, coef[2]);}<br>
	 * {@code gl.glClipPlane(GL.GL_CLIP_PLANE1, coef[3]);}<br>
	 * <p>
	 * <b>Attention:</b> You should not call this method explicitly, unless you
	 * need the frustum equations to be updated only occasionally (rare). Use
	 * {@link remixlab.remixcam.core.AbstractScene#enableFrustumEquationsUpdate()} which
	 * automatically update the frustum equations every frame instead.
	 * 
	 * @see #computeFrustumEquations()
	 */
	public float[][] computeFrustumEquations(float[][] coef) {		
		// soft check:
		if (coef == null || (coef.length == 0))
			coef = new float[6][4];
		else if ((coef.length != 6) || (coef[0].length != 4))
			coef = new float[6][4];

		// Computed once and for all
		Vector3D pos = position();
		Vector3D viewDir = viewDirection();
		Vector3D up = upVector();
		Vector3D right = rightVector();
		float posViewDir = Vector3D.dot(pos, viewDir);

		switch (type()) {
		case PERSPECTIVE: {
			float hhfov = horizontalFieldOfView() / 2.0f;
			float chhfov = (float) Math.cos(hhfov);
			float shhfov = (float) Math.sin(hhfov);
			normal[0] = Vector3D.mult(viewDir, -shhfov);
			normal[1] = Vector3D.add(normal[0], Vector3D.mult(right, chhfov));
			normal[0] = Vector3D.add(normal[0], Vector3D.mult(right, -chhfov));
			normal[2] = Vector3D.mult(viewDir, -1);
			normal[3] = viewDir;

			float hfov = fieldOfView() / 2.0f;
			float chfov = (float) Math.cos(hfov);
			float shfov = (float) Math.sin(hfov);
			normal[4] = Vector3D.mult(viewDir, -shfov);
			normal[5] = Vector3D.add(normal[4], Vector3D.mult(up, -chfov));
			normal[4] = Vector3D.add(normal[4], Vector3D.mult(up, chfov));

			for (int i = 0; i < 2; ++i)
				dist[i] = Vector3D.dot(pos, normal[i]);
			for (int j = 4; j < 6; ++j)
				dist[j] = Vector3D.dot(pos, normal[j]);

			// Natural equations are:
			// dist[0,1,4,5] = pos * normal[0,1,4,5];
			// dist[2] = (pos + zNear() * viewDir) * normal[2];
			// dist[3] = (pos + zFar() * viewDir) * normal[3];

			// 2 times less computations using expanded/merged equations. Dir vectors
			// are normalized.
			float posRightCosHH = chhfov * Vector3D.dot(pos, right);
			dist[0] = -shhfov * posViewDir;
			dist[1] = dist[0] + posRightCosHH;
			dist[0] = dist[0] - posRightCosHH;
			float posUpCosH = chfov * Vector3D.dot(pos, up);
			dist[4] = -shfov * posViewDir;
			dist[5] = dist[4] - posUpCosH;
			dist[4] = dist[4] + posUpCosH;
			break;
		}
		case ORTHOGRAPHIC:
			normal[0] = Vector3D.mult(right, -1);
			normal[1] = right;
			normal[4] = up;
			normal[5] = Vector3D.mult(up, -1);

			float[] wh = getOrthoWidthHeight();
			dist[0] = Vector3D.dot(Vector3D.sub(pos, Vector3D.mult(right, wh[0])),
					normal[0]);
			dist[1] = Vector3D.dot(Vector3D.add(pos, Vector3D.mult(right, wh[0])),
					normal[1]);
			dist[4] = Vector3D.dot(Vector3D.add(pos, Vector3D.mult(up, wh[1])),
					normal[4]);
			dist[5] = Vector3D.dot(Vector3D.sub(pos, Vector3D.mult(up, wh[1])),
					normal[5]);
			break;
		}

		// Front and far planes are identical for both camera types.
		normal[2] = Vector3D.mult(viewDir, -1);
		normal[3] = viewDir;
		dist[2] = -posViewDir - zNear();
		dist[3] = posViewDir + zFar();

		for (int i = 0; i < 6; ++i) {
			coef[i][0] = normal[i].vec[0];
			coef[i][1] = normal[i].vec[1];
			coef[i][2] = normal[i].vec[2];
			coef[i][3] = dist[i];
		}
		
		return coef;
	}
	
	/**
	 * Enables or disables automatic update of the camera frustum plane equations
	 * every frame according to {@code flag}. Computation of the equations is
	 * expensive and hence is disabled by default.
	 * 
	 * @see #updateFrustumEquations()
	 */
	// TODO should be protected
	public void enableFrustumEquationsUpdate(boolean flag) {
		fpCoefficientsUpdate = flag;
	}
	
	/**
	 * Returns {@code true} if automatic update of the camera frustum plane
	 * equations is enabled and {@code false} otherwise. Computation of the
	 * equations is expensive and hence is disabled by default.
	 * 
	 * @see #updateFrustumEquations()
	 */
  // TODO should be protected
	public boolean frustumEquationsUpdateIsEnable() {
		return fpCoefficientsUpdate;
	}
		
	/**
	 * Convenience function that simply calls {@code coneIsBackFacing(new Cone(normals))}.
	 * 
	 * @see #coneIsBackFacing(Cone)
	 * @see #coneIsBackFacing(Vector3D[])
	 */
	public boolean coneIsBackFacing(ArrayList<Vector3D> normals) {
		return coneIsBackFacing(new Cone(normals));
	}
	
	/**
	 * Convenience function that simply calls
	 * {coneIsBackFacing(viewDirection, new Cone(normals))}.
	 * 
	 * @param viewDirection Cached camera view direction.
	 * @param normals cone of normals.
	 */
	public boolean coneIsBackFacing(Vector3D viewDirection, ArrayList<Vector3D> normals) {
		return coneIsBackFacing(viewDirection, new Cone(normals));
	}
	
	/**
	 * Convenience function that simply calls {@code coneIsBackFacing(new Cone(normals))}.
	 * 
	 * @see #coneIsBackFacing(Cone)
	 * @see #coneIsBackFacing(ArrayList)
	 */
	public boolean coneIsBackFacing(Vector3D [] normals) {
		return coneIsBackFacing(new Cone(normals));
	}
	
	/**
	 * Convenience function that simply returns
	 * {@code coneIsBackFacing(viewDirection, new Cone(normals))}.
	 * 
	 * @param viewDirection Cached camera view direction.
	 * @param normals cone of normals.
	 */
	public boolean coneIsBackFacing(Vector3D viewDirection, Vector3D [] normals) {
		return coneIsBackFacing(viewDirection, new Cone(normals));
	}
	
	/**
	 * Convenience function that simply returns {@code coneIsBackFacing(cone.axis(), cone.angle())}.
	 * 
	 * @see #coneIsBackFacing(Vector3D, float)
	 * @see #faceIsBackFacing(Vector3D, Vector3D, Vector3D)
	 */
	public boolean coneIsBackFacing(Cone cone) {
		return coneIsBackFacing(cone.axis(), cone.angle());
	}
	
	/**
	 * Convenience function that simply returns 
	 * {@code coneIsBackFacing(viewDirection, cone.axis(), cone.angle())}.
	 * 
	 * @param viewDirection cached camera view direction.
	 * @param cone cone of normals
	 */
	public boolean coneIsBackFacing(Vector3D viewDirection, Cone cone) {
		return coneIsBackFacing(viewDirection, cone.axis(), cone.angle());
	}
	
	/**
	 * Convinience funtion that simply returns
	 * {@code coneIsBackFacing(viewDirection(), axis, angle)}.
	 * <p>
	 * Non-cached version of {@link #coneIsBackFacing(Vector3D, Vector3D, float)}
	 */
	public boolean coneIsBackFacing(Vector3D axis, float angle) {
		return coneIsBackFacing(viewDirection(), axis, angle);
	}
	
	/**
	 * Returns {@code true} if the given cone is back facing the camera.
	 * Otherwise returns {@code false}.
	 * 
	 * @param viewDirection cached view direction
	 * @param axis normalized cone axis
	 * @param angle cone angle
	 * 
	 * @see #coneIsBackFacing(Cone)
	 * @see #faceIsBackFacing(Vector3D, Vector3D, Vector3D)
	 */
	public boolean coneIsBackFacing(Vector3D viewDirection, Vector3D axis, float angle) {		
		if( angle < HALF_PI ) {			
			float phi = (float) Math.acos ( Vector3D.dot(axis, viewDirection ) );
			if(phi >= HALF_PI)
				return false;
			if( (phi+angle) >= HALF_PI)
				return false;
			return true;
		}
		return false;
	}
	
	/**
	 * Returns {@code true} if the given face is back facing the camera.
	 * Otherwise returns {@code false}.
	 * <p>
	 * <b>Attention:</b> This method is not computationally optimized.
	 * If you call it several times with no change in the matrices, you should
	 * buffer the matrices (modelview, projection and then viewport) to speed-up the
	 * queries.
	 * 
	 * @param a first face vertex
	 * @param b second face vertex
	 * @param c third face vertex
	 */
  public boolean faceIsBackFacing(Vector3D a, Vector3D b, Vector3D c) {
  	Vector3D v1 = Vector3D.sub(projectedCoordinatesOf(a), projectedCoordinatesOf(b));
    Vector3D v2 = Vector3D.sub(projectedCoordinatesOf(b), projectedCoordinatesOf(c));
    return v1.cross(v2).vec[2] <= 0;
  }

	// 4. SCENE RADIUS AND CENTER

	/**
	 * Returns the radius of the scene observed by the Camera.
	 * <p>
	 * You need to provide such an approximation of the scene dimensions so that
	 * the Camera can adapt its {@link #zNear()} and {@link #zFar()} values. See
	 * the {@link #sceneCenter()} documentation.
	 * <p>
	 * Note that Scene.sceneRadius() (resp. Scene.setSceneRadius()) simply call
	 * this method on its associated Camera.
	 * 
	 * @see #setSceneBoundingBox(Vector3D, Vector3D)
	 */
	public float sceneRadius() {
		return scnRadius;
	}

	/**
	 * Sets the {@link #sceneRadius()} value. Negative values are ignored.
	 * <p>
	 * <b>Attention:</b> This methods also sets {@link #focusDistance()} to
	 * {@code sceneRadius() / tan(fieldOfView()/2)} and {@link #flySpeed()} to 1%
	 * of {@link #sceneRadius()} (if there's an Scene
	 * {@link remixlab.remixcam.core.AbstractScene#avatar()} and it is an instance of
	 * InteractiveDrivableFrame it also sets {@code flySpeed} to the same value).
	 */
	public void setSceneRadius(float radius) {
		if (radius <= 0.0f) {
			System.out.println("Warning: Scene radius must be positive - Ignoring value");
			return;
		}
		
		scnRadius = radius;

		setFocusDistance(sceneRadius() / (float) Math.tan(fieldOfView() / 2.0f));

		setFlySpeed(0.01f * sceneRadius());

		// if there's an avatar we change its fly speed as well
		if (scene.avatarIsInteractiveDrivableFrame)
			((InteractiveDrivableFrame) scene.avatar()).setFlySpeed(0.01f * scene.radius());
	}

	/**
	 * 
	 * Returns the position of the scene center, defined in the world coordinate
	 * system.
	 * <p>
	 * The scene observed by the Camera should be roughly centered on this
	 * position, and included in a {@link #sceneRadius()} sphere. This approximate
	 * description of the scene permits a {@link #zNear()} and {@link #zFar()}
	 * clipping planes definition, and allows convenient positioning methods such
	 * as {@link #showEntireScene()}.
	 * <p>
	 * Note that {@link remixlab.remixcam.core.AbstractScene#center()} (resp.
	 * remixlab.remixcam.core.AbstractScene{@link #setSceneCenter(Vector3D)}) simply call this
	 * method (resp. {@link #setSceneCenter(Vector3D)}) on its associated
	 * {@link remixlab.remixcam.core.AbstractScene#camera()}. Default value is (0,0,0) (world
	 * origin). Use {@link #setSceneCenter(Vector3D)} to change it.
	 * 
	 * @see #setSceneBoundingBox(Vector3D, Vector3D)
	 */
	public Vector3D sceneCenter() {
		return scnCenter;
	}

	/**
	 * Sets the {@link #sceneCenter()}.
	 * <p>
	 * <b>Attention:</b> This method also sets the
	 * {@link #arcballReferencePoint()} to {@link #sceneCenter()}.
	 */
	public void setSceneCenter(Vector3D center) {
		scnCenter = center;
		setArcballReferencePoint(sceneCenter());
	}

	/**
	 * Returns the distance from the Camera center to {@link #sceneCenter()},
	 * projected along the Camera Z axis.
	 * <p>
	 * Used by {@link #zNear()} and {@link #zFar()} to optimize the Z range.
	 */
	public float distanceToSceneCenter() {
		return Math.abs((frame().coordinatesOf(sceneCenter())).vec[2]);
	}

	/**
	 * Similar to {@link #setSceneRadius(float)} and
	 * {@link #setSceneCenter(Vector3D)}, but the scene limits are defined by a
	 * (world axis aligned) bounding box.
	 */
	public void setSceneBoundingBox(Vector3D min, Vector3D max) {
		setSceneCenter(Vector3D.mult(Vector3D.add(min, max), 1 / 2.0f));
		setSceneRadius(0.5f * (Vector3D.sub(max, min)).mag());
	}

	// 5. ARCBALL REFERENCE POINT

	/**
	 * The point the Camera revolves around with the ROTATE mouse binding. Defined
	 * in world coordinate system.
	 * <p>
	 * Default value is the {@link #sceneCenter()}.
	 * <p>
	 * <b>Attention:</b> {@link #setSceneCenter(Vector3D)} changes this value.
	 */
	public final Vector3D arcballReferencePoint() {
		return frame().arcballReferencePoint();
	}

	/**
	 * Changes the {@link #arcballReferencePoint()} to {@code rap} (defined in the
	 * world coordinate system).
	 */
	public void setArcballReferencePoint(Vector3D rap) {
		float prevDist = Math.abs(cameraCoordinatesOf(arcballReferencePoint()).vec[2]);

		frame().setArcballReferencePoint(rap);

		// orthoCoef is used to compensate for changes of the
		// arcballReferencePoint, so that the image does
		// not change when the arcballReferencePoint is changed in ORTHOGRAPHIC
		// mode.
		float newDist = Math.abs(cameraCoordinatesOf(arcballReferencePoint()).vec[2]);
		// Prevents division by zero when rap is set to camera position
		if ((prevDist > 1E-9) && (newDist > 1E-9))
			orthoCoef *= prevDist / newDist;
	}

	/**
	 * The {@link #arcballReferencePoint()} is set to the point located under
	 * {@code pixel} on screen. Returns {@code true} if a point was found under
	 * {@code pixel} and {@code false} if none was found (in this case no
	 * {@link #arcballReferencePoint()} is set).
	 * <p>
	 * Override {@link #pointUnderPixel(Point)} in your jogl-based camera class.
	 * <p>
	 * Current implementation always returns {@code false}, meaning that no point
	 * was set.
	 */
	public boolean setArcballReferencePointFromPixel(Point pixel) {
		WorldPoint wP = pointUnderPixel(pixel);
		if (wP.found)
			setArcballReferencePoint(wP.point);
		return wP.found;
	}

	/**
	 * The {@link #setSceneCenter(Vector3D)} is set to the point located under
	 * {@code pixel} on screen. Returns {@code true} if a point was found under
	 * {@code pixel} and {@code false} if none was found (in this case no
	 * {@link #sceneCenter()} is set).
	 * <p>
	 * Override {@link #pointUnderPixel(Point)} in your jogl-based camera class.
	 * <p>
	 * Current implementation always returns {@code false}, meaning that no point
	 * was set.
	 */
	public boolean setSceneCenterFromPixel(Point pixel) {
		WorldPoint wP = pointUnderPixel(pixel);
		if (wP.found)
			setSceneCenter(wP.point);
		return wP.found;
	}

	/**
	 * Returns the coordinates of the 3D point located at {@code pixel} (x,y) on
	 * screen.
	 * <p>
	 * Override this method in your jogl-based camera class.
	 * <p>
	 * Current implementation always returns {@code WorlPoint.found = false}
	 * (dummy value), meaning that no point was found under pixel.
	 */
	public WorldPoint pointUnderPixel(Point pixel) {
		return new WorldPoint(new Vector3D(0, 0, 0), false);
	}

	// 6. ASSOCIATED FRAME AND FRAME WRAPPER FUNCTIONS

	/**
	 * Returns the InteractiveCameraFrame attached to the Camera.
	 * <p>
	 * This InteractiveCameraFrame defines its {@link #position()} and
	 * {@link #orientation()} and can translate mouse events into Camera
	 * displacement. Set using {@link #setFrame(InteractiveCameraFrame)}.
	 */
	public InteractiveCameraFrame frame() {
		return frm;
	}

	/**
	 * Sets the Camera {@link #frame()}.
	 * <p>
	 * If you want to move the Camera, use {@link #setPosition(Vector3D)} and
	 * {@link #setOrientation(Quaternion)} or one of the Camera positioning
	 * methods ({@link #lookAt(Vector3D)}, {@link #fitSphere(Vector3D, float)},
	 * {@link #showEntireScene()}...) instead.
	 * <p>
	 * This method is actually mainly useful if you derive the
	 * InteractiveCameraFrame class and want to use an instance of your new class
	 * to move the Camera.
	 * <p>
	 * A {@code null} {@code icf} reference will silently be ignored.
	 */
	public final void setFrame(InteractiveCameraFrame icf) {
		if (icf == null)
			return;	
		
		frm = icf;
		interpolationKfi.setFrame(frame());			
	}

	/**
	 * Convenience wrapper function that simply returns {@code
	 * frame().spinningSensitivity()}
	 * 
	 * @see remixlab.remixcam.core.InteractiveFrame#spinningSensitivity()
	 */
	public final float spinningSensitivity() {
		return frame().spinningSensitivity();
	}

	/**
	 * Convenience wrapper function that simply calls {@code
	 * frame().setSpinningSensitivity(sensitivity)}
	 * 
	 * @see remixlab.remixcam.core.InteractiveFrame#setSpinningSensitivity(float)
	 */
	public final void setSpinningSensitivity(float sensitivity) {
		frame().setSpinningSensitivity(sensitivity);
	}

	/**
	 * Convenience wrapper function that simply returns {@code
	 * frame().rotationSensitivity()}
	 * 
	 * @see remixlab.remixcam.core.InteractiveFrame#rotationSensitivity()
	 */
	public final float rotationSensitivity() {
		return frame().rotationSensitivity();
	}

	/**
	 * Convenience wrapper function that simply calls {@code
	 * frame().setRotationSensitivity(sensitivity)}
	 * 
	 * @see remixlab.remixcam.core.InteractiveFrame#setRotationSensitivity(float)
	 */
	public final void setRotationSensitivity(float sensitivity) {
		frame().setRotationSensitivity(sensitivity);
	}

	/**
	 * Convenience wrapper function that simply returns {@code
	 * frame().translationSensitivity()}
	 * 
	 * @see remixlab.remixcam.core.InteractiveFrame#translationSensitivity()
	 */
	public final float translationSensitivity() {
		return frame().translationSensitivity();
	}

	/**
	 * Convenience wrapper function that simply calls {@code
	 * frame().setTranslationSensitivity(sensitivity)}
	 * 
	 * @see remixlab.remixcam.core.InteractiveFrame#setTranslationSensitivity(float)
	 */
	public final void setTranslationSensitivity(float sensitivity) {
		frame().setTranslationSensitivity(sensitivity);
	}

	// 7. KEYFRAMED PATHS

	/**
	 * Returns the KeyFrameInterpolator that defines the Camera path number
	 * {@code key}.
	 * <p>
	 * The returned KeyFrameInterpolator may be null (if no path is defined for
	 * key {@code key}).
	 */
	public KeyFrameInterpolator keyFrameInterpolator(int key) {
		return kfi.get(key);
	}

	/**
	 * Sets the KeyFrameInterpolator that defines the Camera path of index {@code
	 * key}.
	 */
	public void setKeyFrameInterpolator(int key, KeyFrameInterpolator k) {
		if (k != null)
			kfi.put(key, k);
		else
			kfi.remove(key);
	}

	/**
	 * Convenience function that simply calls {@code addKeyFrameToPath(key, true)}
	 * .
	 * <p>
	 * The resulting created camera path will be editable.
	 * 
	 * @see #addKeyFrameToPath(int, boolean)
	 */
	public void addKeyFrameToPath(int key) {
		addKeyFrameToPath(key, true);
	}

	/**
	 * Adds the current Camera {@link #position()} and {@link #orientation()} as a
	 * keyFrame to path {@code key}. If {@code editablePath} is {@code true},
	 * builds an InteractiveFrame (from the current Camera {@link #position()} and
	 * {@link #orientation()}) before adding it (see
	 * {@link remixlab.remixcam.core.InteractiveFrame#InteractiveFrame(AbstractScene, InteractiveCameraFrame)}
	 * ). In the latter mode the resulting created path will be editable.
	 * <p>
	 * This method can also be used if you simply want to save a Camera point of
	 * view (a path made of a single keyFrame). Use {@link #playPath(int)} to make
	 * the Camera play the keyFrame path (resp. restore the point of view). Use
	 * {@link #deletePath(int)} to clear the path.
	 * <p>
	 * The default keyboard shortcuts for this method are keys [1-5].
	 * <p>
	 * If you use directly this method and the {@link #keyFrameInterpolator(int)}
	 * does not exist, a new one is created.
	 */
	public void addKeyFrameToPath(int key, boolean editablePath) {
		boolean info = true;
		if (!kfi.containsKey(key)) {
			setKeyFrameInterpolator(key, new KeyFrameInterpolator(scene, frame()));
			System.out.println("Position " + key + " saved");
			info = false;
		}

		if (editablePath)
			kfi.get(key).addKeyFrame(new InteractiveFrame(scene, frame()));
		else
			kfi.get(key).addKeyFrame(frame(), false);

		if (info)
			System.out.println("Path " + key + ", position "
					+ kfi.get(key).numberOfKeyFrames() + " added");
	}

	/**
	 * Makes the Camera follow the path of keyFrameInterpolator() number {@code
	 * key}.
	 * <p>
	 * If the interpolation is started, it stops it instead.
	 * <p>
	 * This method silently ignores undefined (empty) paths (see
	 * keyFrameInterpolator()).
	 * <p>
	 * The default keyboard shortcuts for this method are keys [1-5].
	 */
	public void playPath(int key) {
		if (kfi.containsKey(key)) {
			if (kfi.get(key).interpolationIsStarted()) {
				kfi.get(key).stopInterpolation();
				System.out.println("Path " + key + " stopped");
			} else {
				if (anyInterpolationIsStarted())
					stopAllInterpolations();
				kfi.get(key).startInterpolation();
				System.out.println("Path " + key + " started");
			}
		}
	}

	/**
	 * Deletes the {@link #keyFrameInterpolator(int)} of index {@code key}.
	 */
	public void deletePath(int key) {
		if (kfi.containsKey(key)) {
			kfi.get(key).stopInterpolation();
			kfi.get(key).deletePath();
			kfi.remove(key);
			System.out.println("Path " + key + " deleted");
		}
	}

	/**
	 * Resets the path of the {@link #keyFrameInterpolator(int)} number {@code
	 * key}.
	 * <p>
	 * If this path is not being played (see {@link #playPath(int)} and
	 * {@link remixlab.remixcam.core.KeyFrameInterpolator#interpolationIsStarted()}),
	 * resets it to its starting position (see
	 * {@link remixlab.remixcam.core.KeyFrameInterpolator#resetInterpolation()}). If
	 * the path is played, simply stops interpolation.
	 */
	public void resetPath(int key) {
		if (kfi.containsKey(key)) {
			if ((kfi.get(key).interpolationIsStarted()))
				kfi.get(key).stopInterpolation();
			else {
				kfi.get(key).resetInterpolation();
				kfi.get(key).interpolateAtTime(kfi.get(key).interpolationTime());
			}
		}
	}

	/**
	 * Draws all the Camera paths defined by {@link #keyFrameInterpolator(int)}
	 * and makes them editable by adding all its Frames to the mouse grabber pool.
	 * <p>
	 * First calls
	 * {@link remixlab.remixcam.core.KeyFrameInterpolator#addFramesToMouseGrabberPool()}
	 * and then
	 * {@link remixlab.remixcam.core.KeyFrameInterpolator#drawPath(int, int, float)}
	 * for all the defined paths.
	 * 
	 * @see #hideAllPaths()
	 */
	public void drawAllPaths() {
		itrtr = kfi.keySet().iterator();
		while (itrtr.hasNext()) {
			Integer key = itrtr.next();
			kfi.get(key).addFramesToMouseGrabberPool();
			kfi.get(key).drawPath(3, 5, sceneRadius());
		}
	}

	/**
	 * Hides all the Camera paths defined by {@link #keyFrameInterpolator(int)} by
	 * provisionally removing all its Frames from the mouse grabber pool.
	 * <p>
	 * Simply calls
	 * {@link remixlab.remixcam.core.KeyFrameInterpolator#removeFramesFromMouseGrabberPool()}.
	 * 
	 * @see #drawAllPaths()
	 */
	public void hideAllPaths() {
		itrtr = kfi.keySet().iterator();
		while (itrtr.hasNext()) {
			Integer key = itrtr.next();
			kfi.get(key).removeFramesFromMouseGrabberPool();
		}
	}

	// 8. PROCESSING MATRICES

	/**
	 * Convenience function that simply returns {@code getProjectionMatrix(new
	 * Matrix3D())}
	 * 
	 * @see #getProjectionMatrix(Matrix3D)
	 */
	public Matrix3D getProjectionMatrix() {
		return getProjectionMatrix(new Matrix3D());
	}

	/**
	 * Fills {@code m} with the Camera projection matrix values and returns it. If
	 * {@code m} is {@code null} a new Matrix3D will be created.
	 * <p>
	 * First calls {@link #computeProjectionMatrix()} to define the Camera
	 * projection matrix.
	 * <p>
	 * 
	 * @see #getModelViewMatrix()
	 */
	public Matrix3D getProjectionMatrix(Matrix3D m) {
		if (m == null)
			m = new Matrix3D();

		// May not be needed, but easier and more robust like this.
		computeProjectionMatrix();
		m.set(projectionMat);

		return m;
	}

	/**
	 * Computes the projection matrix associated with the Camera.
	 * <p>
	 * If {@link #type()} is PERSPECTIVE, defines a projection matrix similar to
	 * what would {@code perspective()} do using the {@link #fieldOfView()},
	 * window {@link #aspectRatio()}, {@link #zNear()} and {@link #zFar()}
	 * parameters.
	 * <p>
	 * If {@link #type()} is ORTHOGRAPHIC, the projection matrix is as what
	 * {@code ortho()} would do. Frustum's width and height are set using
	 * {@link #getOrthoWidthHeight()}.
	 * <p>
	 * Both types use {@link #zNear()} and {@link #zFar()} to place clipping
	 * planes. These values are determined from sceneRadius() and sceneCenter() so
	 * that they best fit the scene size.
	 * <p>
	 * Use {@link #getProjectionMatrix()} to retrieve this matrix.
	 * <p>
	 * <b>Note:</b> You must call this method if your Camera is not associated
	 * with a Scene and is used for offscreen computations (using {@code
	 * projectedCoordinatesOf()} for instance).
	 * 
	 * @see #setProjectionMatrix(Matrix3D)
	 */
	public void computeProjectionMatrix() {
		float ZNear = zNear();
		float ZFar = zFar();

		switch (type()) {
		case PERSPECTIVE: {
			// #CONNECTION# all non null coefficients were set to 0.0 in
			// constructor.
			float f = 1.0f / (float) Math.tan(fieldOfView() / 2.0f);
			projectionMat.mat[0] = f / aspectRatio();
			projectionMat.mat[5] = f;
			projectionMat.mat[10] = (ZNear + ZFar) / (ZNear - ZFar);
			projectionMat.mat[14] = -1.0f;
			projectionMat.mat[11] = 2.0f * ZNear * ZFar / (ZNear - ZFar);
			projectionMat.mat[15] = 0.0f;
			// same as gluPerspective( 180.0*fieldOfView()/M_PI, aspectRatio(),
			// zNear(), zFar() );
			break;
		}
		case ORTHOGRAPHIC: {
			float[] wh = getOrthoWidthHeight();
			projectionMat.mat[0] = 1.0f / wh[0];
			projectionMat.mat[5] = 1.0f / wh[1];
			projectionMat.mat[10] = -2.0f / (ZFar - ZNear);
			projectionMat.mat[14] = 0.0f;
			projectionMat.mat[11] = -(ZFar + ZNear) / (ZFar - ZNear);
			projectionMat.mat[15] = 1.0f;
			// same as glOrtho( -w, w, -h, h, zNear(), zFar() );
			break;
		}
		}
	}

	/**
	 * Fills the projection matrix with the {@code proj} matrix values.
	 * 
	 * @see #setModelViewMatrix(Matrix3D)
	 */
	public void setProjectionMatrix(Matrix3D proj) {
		//if (isDetachedFromP5Camera())
			projectionMat.set(proj);
	}

	/**
	 * Convenience function that simply returns {@code getModelViewMatrix(new
	 * Matrix3D())}
	 */
	public Matrix3D getModelViewMatrix() {
		return getModelViewMatrix(new Matrix3D());
	}

	/**
	 * Fills {@code m} with the Camera modelView matrix values and returns it. If
	 * {@code m} is {@code null} a new Matrix3D will be created.
	 * <p>
	 * First calls {@link #computeModelViewMatrix()} to define the Camera
	 * modelView matrix.
	 * 
	 * @see #getProjectionMatrix(Matrix3D)
	 */
	public Matrix3D getModelViewMatrix(Matrix3D m) {
		if (m == null)
			m = new Matrix3D();
		// May not be needed, but easier like this.
		// Prevents from retrieving matrix in stereo mode -> overwrites shifted
		// value.
		computeModelViewMatrix();
		m.set(modelViewMat);
		return m;
	}

	/**
	 * Computes the modelView matrix associated with the Camera's
	 * {@link #position()} and {@link #orientation()}.
	 * <p>
	 * This matrix converts from the world coordinates system to the Camera
	 * coordinates system, so that coordinates can then be projected on screen
	 * using the projection matrix (see {@link #computeProjectionMatrix()}).
	 * <p>
	 * Use {@link #getModelViewMatrix()} to retrieve this matrix.
	 * <p>
	 * <b>Note:</b> You must call this method if your Camera is not associated
	 * with a Scene and is used for offscreen computations (using {@code
	 * projectedCoordinatesOf()} for instance).
	 */
	public void computeModelViewMatrix() {
		Quaternion q = frame().orientation();

		float q00 = 2.0f * q.quat[0] * q.quat[0];
		float q11 = 2.0f * q.quat[1] * q.quat[1];
		float q22 = 2.0f * q.quat[2] * q.quat[2];

		float q01 = 2.0f * q.quat[0] * q.quat[1];
		float q02 = 2.0f * q.quat[0] * q.quat[2];
		float q03 = 2.0f * q.quat[0] * q.quat[3];

		float q12 = 2.0f * q.quat[1] * q.quat[2];
		float q13 = 2.0f * q.quat[1] * q.quat[3];
		float q23 = 2.0f * q.quat[2] * q.quat[3];

		modelViewMat.mat[0] = 1.0f - q11 - q22;
		modelViewMat.mat[4] = q01 - q23;
		modelViewMat.mat[8] = q02 + q13;
		modelViewMat.mat[12] = 0.0f;

		modelViewMat.mat[1] = q01 + q23;
		modelViewMat.mat[5] = 1.0f - q22 - q00;
		modelViewMat.mat[9] = q12 - q03;
		modelViewMat.mat[13] = 0.0f;

		modelViewMat.mat[2] = q02 - q13;
		modelViewMat.mat[6] = q12 + q03;
		modelViewMat.mat[10] = 1.0f - q11 - q00;
		modelViewMat.mat[14] = 0.0f;

		Vector3D t = q.inverseRotate(frame().position());

		modelViewMat.mat[3] = -t.vec[0];
		modelViewMat.mat[7] = -t.vec[1];
		modelViewMat.mat[11] = -t.vec[2];
		modelViewMat.mat[15] = 1.0f;
	}

	/**
	 * Fills the modelview matrix with the {@code modelview} matrix values.
	 * 
	 * @see #setProjectionMatrix(Matrix3D)
	 */
	public void setModelViewMatrix(Matrix3D modelview) {
		//if (isDetachedFromP5Camera())
			modelViewMat.set(modelview);
	}

	// 9. WORLD -> CAMERA

	/**
	 * Returns the Camera frame coordinates of a point {@code src} defined in
	 * world coordinates.
	 * <p>
	 * {@link #worldCoordinatesOf(Vector3D)} performs the inverse transformation.
	 * <p>
	 * Note that the point coordinates are simply converted in a different
	 * coordinate system. They are not projected on screen. Use
	 * {@link #projectedCoordinatesOf(Vector3D, SimpleFrame)} for that.
	 */
	public final Vector3D cameraCoordinatesOf(Vector3D src) {
		return frame().coordinatesOf(src);
	}

	/**
	 * Returns the world coordinates of the point whose position {@code src} is
	 * defined in the Camera coordinate system.
	 * <p>
	 * {@link #cameraCoordinatesOf(Vector3D)} performs the inverse transformation.
	 */
	public Vector3D worldCoordinatesOf(final Vector3D src) {
		return frame().inverseCoordinatesOf(src);
	}

	// 10. 2D -> 3D

	/**
	 * Gives the coefficients of a 3D half-line passing through the Camera eye and
	 * pixel (x,y).
	 * <p>
	 * The origin of the half line (eye position) is stored in {@code orig}, while
	 * {@code dir} contains the properly oriented and normalized direction of the
	 * half line.
	 * <p>
	 * {@code x} and {@code y} are expressed in Processing format (origin in the
	 * upper left corner). Use {@link #screenHeight()} - y to convert to
	 * processing scene units.
	 * <p>
	 * This method is useful for analytical intersection in a selection method.
	 */
	public void convertClickToLine(final Point pixelInput, Vector3D orig, Vector3D dir) {
		Point pixel = new Point(pixelInput.getX(), pixelInput.getY());
		
		//lef-handed coordinate system correction
		pixel.y = screenHeight() - pixelInput.y;
		
		switch (type()) {
		case PERSPECTIVE:
			orig.set(position());
			dir.set(new Vector3D(((2.0f * (int)pixel.x / screenWidth()) - 1.0f)
					* (float) Math.tan(fieldOfView() / 2.0f) * aspectRatio(),
					((2.0f * (screenHeight() - (int)pixel.y) / screenHeight()) - 1.0f)
							* (float) Math.tan(fieldOfView() / 2.0f), -1.0f));
			dir.set(Vector3D.sub(worldCoordinatesOf(dir), orig));
			dir.normalize();
			break;

		case ORTHOGRAPHIC: {
			float[] wh = getOrthoWidthHeight();
			orig.set(new Vector3D((2.0f * (int)pixel.x / screenWidth() - 1.0f) * wh[0],
					-(2.0f * (int)pixel.y / screenHeight() - 1.0f) * wh[1], 0.0f));
			orig.set(worldCoordinatesOf(orig));
			dir.set(viewDirection());
			break;
		}
		}
	}

	/**
	 * Convenience function that simply returns {@code projectedCoordinatesOf(src,
	 * null)}
	 * 
	 * @see #projectedCoordinatesOf(Vector3D, SimpleFrame)
	 */
	public final Vector3D projectedCoordinatesOf(Vector3D src) {
		return projectedCoordinatesOf(src, null);
	}

	/**
	 * Returns the screen projected coordinates of a point {@code src} defined in
	 * the {@code frame} coordinate system.
	 * <p>
	 * When {@code frame} is {@code null}, {@code src} is expressed in the world
	 * coordinate system. See {@link #projectedCoordinatesOf(Vector3D)}.
	 * <p>
	 * The x and y coordinates of the returned Vector3D are expressed in pixel,
	 * (0,0) being the upper left corner of the window. The z coordinate ranges
	 * between 0.0 (near plane) and 1.0 (excluded, far plane). See the {@code
	 * gluProject} man page for details.
	 * <p>
	 * <b>Attention:</b> This method only uses the intrinsic Camera parameters
	 * (see {@link #getModelViewMatrix()}, {@link #getProjectionMatrix()} and
	 * {@link #getViewport()}) and is completely independent of the processing
	 * matrices. You can hence define a virtual Camera and use this method to
	 * compute projections out of a classical rendering context.
	 * 
	 * @see #unprojectedCoordinatesOf(Vector3D, SimpleFrame)
	 */
	public final Vector3D projectedCoordinatesOf(Vector3D src, SimpleFrame frame) {
		float xyz[] = new float[3];
		viewport = getViewport();

		if (frame != null) {
			Vector3D tmp = frame.inverseCoordinatesOf(src);
			project(tmp.vec[0], tmp.vec[1], tmp.vec[2], modelViewMat, projectionMat, viewport, xyz);
		} else
			project(src.vec[0], src.vec[1], src.vec[2], modelViewMat, projectionMat, viewport, xyz);

  	//lef-handed coordinate system correction
		xyz[1] = screenHeight() - xyz[1];

		return new Vector3D((float) xyz[0], (float) xyz[1], (float) xyz[2]);
	}

	/**
	 * Convenience function that simply returns {@code return
	 * unprojectedCoordinatesOf(src, null)}
	 * 
	 * #see {@link #unprojectedCoordinatesOf(Vector3D, SimpleFrame)}
	 */
	public final Vector3D unprojectedCoordinatesOf(Vector3D src) {
		return this.unprojectedCoordinatesOf(src, null);
	}

	/**
	 * Returns the world unprojected coordinates of a point {@code src} defined in
	 * the screen coordinate system.
	 * <p>
	 * The {@code src.x} and {@code src.y} input values are expressed in pixels,
	 * (0,0) being the upper left corner of the window. {@code src.z} is a depth
	 * value ranging in [0..1] (near and far plane respectively). See the {@code
	 * gluUnProject} man page for details.
	 * <p>
	 * The result is expressed in the {@code frame} coordinate system. When
	 * {@code frame} is {@code null}, the result is expressed in the world
	 * coordinates system. The possible {@code frame}
	 * {@link remixlab.remixcam.core.SimpleFrame#referenceFrame()} are taken into account.
	 * <p>
	 * {@link #projectedCoordinatesOf(Vector3D, SimpleFrame)} performs the inverse
	 * transformation.
	 * <p>
	 * This method only uses the intrinsic Camera parameters (see
	 * {@link #getModelViewMatrix()}, {@link #getProjectionMatrix()} and
	 * {@link #getViewport()}) and is completely independent of the Processing
	 * matrices. You can hence define a virtual Camera and use this method to
	 * compute un-projections out of a classical rendering context.
	 * <p>
	 * <b>Attention:</b> However, if your Camera is not attached to a Scene (used
	 * for offscreen computations for instance), make sure the Camera matrices are
	 * updated before calling this method (use {@link #computeModelViewMatrix()},
	 * {@link #computeProjectionMatrix()}).
	 * <p>
	 * This method is not computationally optimized. If you call it several times
	 * with no change in the matrices, you should buffer the entire inverse
	 * projection matrix (modelview, projection and then viewport) to speed-up the
	 * queries. See the gluUnProject man page for details.
	 * 
	 * @see #projectedCoordinatesOf(Vector3D, SimpleFrame)
	 * @see #setScreenWidthAndHeight(int, int)
	 */
	public final Vector3D unprojectedCoordinatesOf(Vector3D src, SimpleFrame frame) {
		float xyz[] = new float[3];
		viewport = getViewport();
		
		unproject(src.vec[0], (screenHeight() - src.vec[1]), src.vec[2], modelViewMat,	projectionMat, viewport, xyz);		
		//right_handed coordinate system should go like this:
		//unproject(src.x, src.y, src.z, modelViewMat, projectionMat, viewport, xyz);
		
		if (frame != null)
			return frame.coordinatesOf(new Vector3D((float) xyz[0], (float) xyz[1],
					(float) xyz[2]));
		else
			return new Vector3D((float) xyz[0], (float) xyz[1], (float) xyz[2]);
	}

	// 11. FLYSPEED

	/**
	 * Returns the fly speed of the Camera.
	 * <p>
	 * Simply returns {@code frame().flySpeed()}. See the
	 * {@link remixlab.remixcam.core.InteractiveCameraFrame#flySpeed()} documentation.
	 * This value is only meaningful when the MouseAction bindings is
	 * Scene.MOVE_FORWARD or is Scene.MOVE_BACKWARD.
	 * <p>
	 * Set to 0.5% of the {@link #sceneRadius()} by {@link #setSceneRadius(float)}.
	 * 
	 * @see #setFlySpeed(float)
	 */
	public float flySpeed() {
		return frame().flySpeed();
	}

	/**
	 * Sets the Camera {@link #flySpeed()}.
	 * <p>
	 * <b>Attention:</b> This value is modified by {@link #setSceneRadius(float)}.
	 */
	public void setFlySpeed(float speed) {
		frame().setFlySpeed(speed);
	}

	// 12. POSITION TOOLS

	/**
	 * Sets the Camera {@link #orientation()}, so that it looks at point {@code
	 * target} (defined in the world coordinate system).
	 * <p>
	 * The Camera {@link #position()} is not modified. Simply
	 * {@link #setViewDirection(Vector3D)}.
	 * 
	 * @see #at()
	 * @see #setUpVector(Vector3D)
	 * @see #setOrientation(Quaternion)
	 * @see #showEntireScene()
	 * @see #fitSphere(Vector3D, float)
	 * @see #fitBoundingBox(Vector3D, Vector3D)
	 */
	public void lookAt(Vector3D target) {
		setViewDirection(Vector3D.sub(target, position()));
	}

	/**
	 * Returns a point defined in the world coordinate system where the camera is
	 * pointing at (just in front of {@link #viewDirection()}). Useful for setting
	 * the Processing camera() which uses a similar approach of that found in
	 * gluLookAt.
	 * 
	 * @see #lookAt(Vector3D)
	 */
	public Vector3D at() {
		return Vector3D.add(position(), viewDirection());
	}

	/**
	 * Moves the Camera so that the sphere defined by {@code center} and {@code
	 * radius} is visible and fits the window.
	 * <p>
	 * The Camera is simply translated along its {@link #viewDirection()} so that
	 * the sphere fits the screen. Its {@link #orientation()} and its
	 * {@link #fieldOfView()} are unchanged.
	 * <p>
	 * You should therefore orientate the Camera before you call this method.
	 * <p>
	 * <b>Attention:</b> If the Camera {@link #kind()} is STANDARD, simply resets
	 * the {@link #standardOrthoFrustumSize()} to 1 and then calls {@code
	 * lookAt(sceneCenter())}.
	 * 
	 * @see #lookAt(Vector3D)
	 * @see #setOrientation(Quaternion)
	 * @see #setUpVector(Vector3D, boolean)
	 */
	public void fitSphere(Vector3D center, float radius) {
		if ((kind() == Kind.STANDARD) && (type() == Type.ORTHOGRAPHIC)) {
			orthoSize = 1;
			lookAt(sceneCenter());
			return;
		}

		float distance = 0.0f;
		switch (type()) {
		case PERSPECTIVE: {
			float yview = radius / (float) Math.sin(fieldOfView() / 2.0f);
			float xview = radius / (float) Math.sin(horizontalFieldOfView() / 2.0f);
			distance = Math.max(xview, yview);
			break;
		}
		case ORTHOGRAPHIC: {
			distance = Vector3D.dot(Vector3D.sub(center, arcballReferencePoint()),
					viewDirection())
					+ (radius / orthoCoef);
			break;
		}
		}

		Vector3D newPos = Vector3D.sub(center, Vector3D.mult(viewDirection(), distance));
		frame().setPositionWithConstraint(newPos);
	}

	/**
	 * Moves the Camera so that the (world axis aligned) bounding box ({@code min}
	 * , {@code max}) is entirely visible, using
	 * {@link #fitSphere(Vector3D, float)}.
	 */
	public void fitBoundingBox(Vector3D min, Vector3D max) {
		float diameter = Math.max(Math.abs(max.vec[1] - min.vec[1]), Math.abs(max.vec[0] - min.vec[0]));
		diameter = Math.max(Math.abs(max.vec[2] - min.vec[2]), diameter);
		fitSphere(Vector3D.mult(Vector3D.add(min, max), 0.5f), 0.5f * diameter);
	}

	/**
	 * Moves the Camera so that the rectangular screen region defined by {@code
	 * rectangle} (pixel units, with origin in the upper left corner) fits the
	 * screen.
	 * <p>
	 * The Camera is translated (its {@link #orientation()} is unchanged) so that
	 * {@code rectangle} is entirely visible. Since the pixel coordinates only
	 * define a <i>frustum</i> in 3D, it's the intersection of this frustum with a
	 * plane (orthogonal to the {@link #viewDirection()} and passing through the
	 * {@link #sceneCenter()}) that is used to define the 3D rectangle that is
	 * eventually fitted.
	 */
	public void fitScreenRegion(Rectangle rectangle) {
		Vector3D vd = viewDirection();
		float distToPlane = distanceToSceneCenter();

		Point center = new Point((int) rectangle.getCenterX(), (int) rectangle.getCenterY());

		Vector3D orig = new Vector3D();
		Vector3D dir = new Vector3D();
		convertClickToLine(center, orig, dir);
		Vector3D newCenter = Vector3D.add(orig, Vector3D.mult(dir,
				(distToPlane / Vector3D.dot(dir, vd))));

		convertClickToLine(new Point(rectangle.x, center.y), orig, dir);
		final Vector3D pointX = Vector3D.add(orig, Vector3D.mult(dir,
				(distToPlane / Vector3D.dot(dir, vd))));

		convertClickToLine(new Point(center.x, rectangle.y), orig, dir);
		final Vector3D pointY = Vector3D.add(orig, Vector3D.mult(dir,
				(distToPlane / Vector3D.dot(dir, vd))));

		float distance = 0.0f;
		switch (type()) {
		case PERSPECTIVE: {
			final float distX = Vector3D.dist(pointX, newCenter)
					/ (float) Math.sin(horizontalFieldOfView() / 2.0f);
			final float distY = Vector3D.dist(pointY, newCenter)
					/ (float) Math.sin(fieldOfView() / 2.0f);

			distance = Math.max(distX, distY);
			break;
		}
		case ORTHOGRAPHIC: {
			final float dist = Vector3D.dot(Vector3D.sub(newCenter,
					arcballReferencePoint()), vd);
			final float distX = Vector3D.dist(pointX, newCenter) / orthoCoef
					/ ((aspectRatio() < 1.0) ? 1.0f : aspectRatio());
			final float distY = Vector3D.dist(pointY, newCenter) / orthoCoef
					/ ((aspectRatio() < 1.0) ? 1.0f / aspectRatio() : 1.0f);

			distance = dist + Math.max(distX, distY);

			break;
		}
		}

		frame().setPositionWithConstraint(Vector3D.sub(newCenter, Vector3D.mult(vd, distance)));
	}

	/**
	 * Moves the Camera so that the entire scene is visible.
	 * <p>
	 * Simply calls {@link #fitSphere(Vector3D, float)} on a sphere defined by
	 * {@link #sceneCenter()} and {@link #sceneRadius()}.
	 * <p>
	 * You will typically use this method at init time after you defined a new
	 * {@link #sceneRadius()}.
	 */
	public void showEntireScene() {
		fitSphere(sceneCenter(), sceneRadius());
	}

	/**
	 * Moves the Camera so that its {@link #sceneCenter()} is projected on the
	 * center of the window. The {@link #orientation()} and {@link #fieldOfView()}
	 * are unchanged.
	 * <p>
	 * Simply projects the current position on a line passing through
	 * {@link #sceneCenter()}.
	 * 
	 * @see #showEntireScene()
	 */
	public void centerScene() {
		frame().projectOnLine(sceneCenter(), viewDirection());
	}

	/**
	 * Smoothly moves the Camera so that the rectangular screen region defined by
	 * {@code rectangle} (pixel units, with origin in the upper left corner) fits
	 * the screen.
	 * <p>
	 * The Camera is translated (its {@link #orientation()} is unchanged) so that
	 * {@code rectangle} is entirely visible. Since the pixel coordinates only
	 * define a <i>frustum</i> in 3D, it's the intersection of this frustum with a
	 * plane (orthogonal to the {@link #viewDirection()} and passing through the
	 * {@link #sceneCenter()}) that is used to define the 3D rectangle that is
	 * eventually fitted.
	 * 
	 * @see #fitScreenRegion(Rectangle)
	 */
	public void interpolateToZoomOnRegion(Rectangle rectangle) {
		// if (interpolationKfi.interpolationIsStarted())
		// interpolationKfi.stopInterpolation();
		if (anyInterpolationIsStarted())
			stopAllInterpolations();

		interpolationKfi.deletePath();
		interpolationKfi.addKeyFrame(frame(), false);

		// Small hack: attach a temporary frame to take advantage of fitScreenRegion
		// without modifying frame
		tempFrame = new InteractiveCameraFrame(this);
		InteractiveCameraFrame originalFrame = frame();
		tempFrame.setPosition(new Vector3D(frame().position().vec[0],	frame().position().vec[1], frame().position().vec[2]));
		tempFrame.setOrientation( frame().orientation().get() );
		setFrame(tempFrame);
		fitScreenRegion(rectangle);
		setFrame(originalFrame); 	

		interpolationKfi.addKeyFrame(tempFrame, false);
		interpolationKfi.startInterpolation();		
	}

	/**
	 * Makes the Camera smoothly zoom on the {@link #pointUnderPixel(Point)}
	 * {@code pixel} and returns the world coordinates of the
	 * {@link #pointUnderPixel(Point)}.
	 * <p>
	 * Nothing happens if no {@link #pointUnderPixel(Point)} is found. Otherwise a
	 * KeyFrameInterpolator is created that animates the Camera on a one second
	 * path that brings the Camera closer to the point under {@code pixel}.
	 * <p>
	 * <b>Attention:</b> Override this method in your jogl-based camera class. See
	 * {@link #pointUnderPixel(Point)}.
	 * 
	 * @see #interpolateToFitScene()
	 */
	public WorldPoint interpolateToZoomOnPixel(Point pixel) {
		float coef = 0.1f;

		WorldPoint target = pointUnderPixel(pixel);

		if (!target.found)
			return target;

		// if (interpolationKfi.interpolationIsStarted())
		// interpolationKfi.stopInterpolation();
		if (anyInterpolationIsStarted())
			stopAllInterpolations();

		interpolationKfi.deletePath();
		interpolationKfi.addKeyFrame(frame(), false);

		interpolationKfi.addKeyFrame(new SimpleFrame(Vector3D.add(Vector3D.mult(frame()
				.position(), 0.3f), Vector3D.mult(target.point, 0.7f)), frame()
				.orientation()), 0.4f, false);

		// Small hack: attach a temporary frame to take advantage of lookAt without
		// modifying frame
		tempFrame = new InteractiveCameraFrame(this);
		InteractiveCameraFrame originalFrame = frame();
		tempFrame.setPosition(Vector3D.add(Vector3D.mult(frame().position(), coef),
				Vector3D.mult(target.point, (1.0f - coef))));
		tempFrame.setOrientation( frame().orientation().get() );
		setFrame(tempFrame);
		lookAt(target.point);
		setFrame(originalFrame);

		interpolationKfi.addKeyFrame(tempFrame, 1.0f, false);
		interpolationKfi.startInterpolation();		

		return target;
	}

	/**
	 * Interpolates the Camera on a one second KeyFrameInterpolator path so that
	 * the entire scene fits the screen at the end.
	 * <p>
	 * The scene is defined by its {@link #sceneCenter()} and its
	 * {@link #sceneRadius()}. See {@link #showEntireScene()}.
	 * <p>
	 * The {@link #orientation()} of the Camera is not modified.
	 * 
	 * @see #interpolateToZoomOnPixel(Point)
	 */
	public void interpolateToFitScene() {
		// if (interpolationKfi.interpolationIsStarted())
		// interpolationKfi.stopInterpolation();
		if (anyInterpolationIsStarted())
			stopAllInterpolations();

		interpolationKfi.deletePath();
		interpolationKfi.addKeyFrame(frame(), false);

		// Small hack: attach a temporary frame to take advantage of showEntireScene
		// without modifying frame
		tempFrame = new InteractiveCameraFrame(this);
		InteractiveCameraFrame originalFrame = frame();
		tempFrame.setPosition(new Vector3D(frame().position().vec[0],	frame().position().vec[1], frame().position().vec[2]));
		tempFrame.setOrientation( frame().orientation().get() );
		setFrame(tempFrame);
		showEntireScene();
		setFrame(originalFrame);

		interpolationKfi.addKeyFrame(tempFrame, false);
		interpolationKfi.startInterpolation();
	}

	/**
	 * Convenience function that simply calls {@code interpolateTo(fr, 1)}.
	 * 
	 * @see #interpolateTo(SimpleFrame, float)
	 */
	public void interpolateTo(SimpleFrame fr) {
		interpolateTo(fr, 1);
	}

	/**
	 * Smoothly interpolates the Camera on a KeyFrameInterpolator path so that it
	 * goes to {@code fr}.
	 * <p>
	 * {@code fr} is expressed in world coordinates. {@code duration} tunes the
	 * interpolation speed.
	 * 
	 * @see #interpolateTo(SimpleFrame)
	 * @see #interpolateToFitScene()
	 * @see #interpolateToZoomOnPixel(Point)
	 */
	public void interpolateTo(SimpleFrame fr, float duration) {
		// if (interpolationKfi.interpolationIsStarted())
		// interpolationKfi.stopInterpolation();
		if (anyInterpolationIsStarted())
			stopAllInterpolations();

		interpolationKfi.deletePath();
		interpolationKfi.addKeyFrame(frame(), false);
		interpolationKfi.addKeyFrame(fr, duration, false);

		interpolationKfi.startInterpolation();
	}

	/**
	 * Returns {@code true} if any interpolation associated with this Camera
	 * is currently being performed (and {@code false} otherwise).
	 */
	public boolean anyInterpolationIsStarted() {
		itrtr = kfi.keySet().iterator();
		while (itrtr.hasNext()) {
			Integer key = itrtr.next();
			if (kfi.get(key).interpolationIsStarted())
				return true;
		}
		return interpolationKfi.interpolationIsStarted();
	}

	/**
	 * Stops all interpolations currently being performed
	 * associated with this Camera.
	 */
	public void stopAllInterpolations() {
		itrtr = kfi.keySet().iterator();
		while (itrtr.hasNext()) {
			Integer key = itrtr.next();
			if (kfi.get(key).interpolationIsStarted())
				kfi.get(key).stopInterpolation();
		}
		if (interpolationKfi.interpolationIsStarted())
			interpolationKfi.stopInterpolation();
	}

	// 13. STEREO PARAMETERS

	/**
	 * Returns the user's inter-ocular distance (in meters). Default value is
	 * 0.062m, which fits most people.
	 * 
	 * @see #setIODistance(float)
	 */
	public float IODistance() {
		return IODist;
	}

	/**
	 * Sets the {@link #IODistance()}.
	 */
	public void setIODistance(float distance) {
		IODist = distance;
	}

	/**
	 * Returns the physical distance between the user's eyes and the screen (in
	 * meters).
	 * <p>
	 * Default value is 0.5m.
	 * <p>
	 * Value is set using {@link #setPhysicalDistanceToScreen(float)}.
	 * <p>
	 * physicalDistanceToScreen() and {@link #focusDistance()} represent the same
	 * distance. The first one is expressed in physical real world units, while
	 * the latter is expressed in processing virtual world units. Use their ratio
	 * to convert distances between these worlds.
	 */
	public float physicalDistanceToScreen() {
		return physicalDist2Scrn;
	}

	/**
	 * Sets the {@link #physicalDistanceToScreen()}.
	 */
	public void setPhysicalDistanceToScreen(float distance) {
		physicalDist2Scrn = distance;
	}

	/**
	 * Returns the physical screen width, in meters. Default value is 0.4m
	 * (average monitor).
	 * <p>
	 * Used for stereo display only. Set using
	 * {@link #setPhysicalScreenWidth(float)}.
	 * <p>
	 * See {@link #physicalDistanceToScreen()} for reality center automatic
	 * configuration.
	 */
	public float physicalScreenWidth() {
		return physicalScrnWidth;
	}

	/**
	 * Sets the physical screen (monitor or projected wall) width (in meters).
	 */
	public void setPhysicalScreenWidth(float width) {
		physicalScrnWidth = width;
	}

	/**
	 * Returns the focus distance used by stereo display, expressed in processing
	 * units.
	 * <p>
	 * This is the distance in the virtual world between the Camera and the plane
	 * where the horizontal stereo parallax is null (the stereo left and right
	 * images are superimposed).
	 * <p>
	 * This distance is the virtual world equivalent of the real-world
	 * {@link #physicalDistanceToScreen()}.
	 * <p>
	 * <b>attention:</b> This value is modified by Scene.setSceneRadius(),
	 * setSceneRadius() and {@link #setFieldOfView(float)}. When one of these
	 * values is modified, {@link #focusDistance()} is set to
	 * {@link #sceneRadius()} / tan({@link #fieldOfView()}/2), which provides good
	 * results.
	 */
	public float focusDistance() {
		return focusDist;
	}

	/**
	 * Sets the focusDistance(), in processing scene units.
	 */
	public void setFocusDistance(float distance) {
		if(distance != focusDist)
			lastFrameUpdate = scene.frameCount();
		focusDist = distance;
	}

	// 14. Implementation of glu utility functions
	
	/**
	 * Cache {@code (P x M)} and {@code inv (P x M)} under the following circumstances:
	 * <p>
	 * i) If {@code scene.mouseGrabberPool().size() > 3 && scene.hasMouseTracking()} then
	 * {@code (P x M)} is cached so that
	 * {@code project(float, float, float, Matrix3D, Matrix3D, int[], float[])} 
	 * is speeded up.
	 * <p>
	 * ii) If {@code #unprojectCacheIsOptimized()} {@code inv (P x M)} is cached (and hence
	 * {@code (P x M)} is cached too) so that {@code unproject(float, float, float, Matrix3D, Matrix3D, int[], float[])}
	 * is speeded up.
	 * 
	 * @see #unprojectCacheIsOptimized()
	 * @see #optimizeUnprojectCache(boolean)
	 */
	public void cacheMatrices() {
		// 1. project
		if( ( (scene.mouseGrabberPool().size() > 3) && scene.hasMouseTracking() ) || unprojectCacheIsOptimized()) {
			projectCacheOptimized = true;
			if(projectionTimesModelview == null)
				projectionTimesModelview = new Matrix3D();
			Matrix3D.mult(projectionMat, modelViewMat, projectionTimesModelview);
		}
		else
			projectCacheOptimized = false;
		
		// 2. unproject
		if(unprojectCacheIsOptimized()) {
			if(projectionTimesModelviewInverse == null)
				projectionTimesModelviewInverse = new Matrix3D();
			projectionTimesModelviewHasInverse = projectionTimesModelview.invert(projectionTimesModelviewInverse);
		}
	}
	
	/**
	 * Returns {@code true} if {@code P x M} and {@code inv (P x M)} are being cached,
	 * and {@code false} otherwise.
	 * 
	 * @see #cacheMatrices()
	 * @see #optimizeUnprojectCache(boolean)
	 */
	public boolean unprojectCacheIsOptimized() {
		return unprojectCacheOptimized;
	}
	
	/**
	 * Cache {@code inv (P x M)} (and also {@code (P x M)} ) so that
	 * {@code project(float, float, float, Matrx3D, Matrx3D, int[], float[])}
	 * (and also {@code unproject(float, float, float, Matrx3D, Matrx3D, int[], float[])})
	 * is optimised.
	 * 
	 * @see #unprojectCacheIsOptimized()
	 * @see #cacheMatrices()
	 */
	public void optimizeUnprojectCache(boolean optimise) {
		unprojectCacheOptimized = optimise;
	}
	
	/**
	 * Similar to {@code gluProject}: map object coordinates to window
	 * coordinates.
	 * 
	 * @param objx
	 *          Specify the object x coordinate.
	 * @param objy
	 *          Specify the object y coordinate.
	 * @param objz
	 *          Specify the object z coordinate.
	 * @param modelview
	 *          Specifies the current modelview matrix.
	 * @param projection
	 *          Specifies the current projection matrix.
	 * @param viewport
	 *          Specifies the current viewport.
	 * @param windowCoordinate
	 *          Return the computed window coordinates.
	 */
	public boolean project(float objx, float objy, float objz, Matrix3D modelview,
			                   Matrix3D projection, int[] viewport, float[] windowCoordinate) {
		float in[] = new float[4];
		float out[] = new float[4];
		
		in[0] = objx;
		in[1] = objy;
		in[2] = objz;
		in[3] = 1.0f;
		
		if(projectCacheOptimized) {	
			out[0]=projectionTimesModelview.mat[0]*in[0] + projectionTimesModelview.mat[1]*in[1] + projectionTimesModelview.mat[2]*in[2] + projectionTimesModelview.mat[3]*in[3];
			out[1]=projectionTimesModelview.mat[4]*in[0] + projectionTimesModelview.mat[5]*in[1] + projectionTimesModelview.mat[6]*in[2] + projectionTimesModelview.mat[7]*in[3];
			out[2]=projectionTimesModelview.mat[8]*in[0] + projectionTimesModelview.mat[9]*in[1] + projectionTimesModelview.mat[10]*in[2] + projectionTimesModelview.mat[11]*in[3];
			out[3]=projectionTimesModelview.mat[12]*in[0] + projectionTimesModelview.mat[13]*in[1] + projectionTimesModelview.mat[14]*in[2] + projectionTimesModelview.mat[15]*in[3];
			
			if (out[3] == 0.0)
				return false;
			
			out[0] /= out[3];
			out[1] /= out[3];
			out[2] /= out[3];
			// Map x, y and z to range 0-1
			out[0] = out[0] * 0.5f + 0.5f;
			out[1] = out[1] * 0.5f + 0.5f;
			out[2] = out[2] * 0.5f + 0.5f;
			
			// Map x,y to viewport
			out[0] = out[0] * viewport[2] + viewport[0];
			out[1] = out[1] * viewport[3] + viewport[1];
			
			windowCoordinate[0] = out[0];
			windowCoordinate[1] = out[1];
			windowCoordinate[2] = out[2];
			return true;	
		}		
		else {
			modelview.mult(in, out);
			projection.mult(out, in);
			if (in[3] == 0.0)
				return false;
			in[0] /= in[3];
			in[1] /= in[3];
			in[2] /= in[3];
			// Map x, y and z to range 0-1
			in[0] = in[0] * 0.5f + 0.5f;
			in[1] = in[1] * 0.5f + 0.5f;
			in[2] = in[2] * 0.5f + 0.5f;			
			// Map x,y to viewport
			in[0] = in[0] * viewport[2] + viewport[0];
			in[1] = in[1] * viewport[3] + viewport[1];
			windowCoordinate[0] = in[0];
			windowCoordinate[1] = in[1];
			windowCoordinate[2] = in[2];
			return true;
		}
	}

	/**
	 * Similar to {@code gluUnProject}: map window coordinates to object
	 * coordinates.
	 * 
	 * @param winx
	 *          Specify the window x coordinate.
	 * @param winy
	 *          Specify the window y coordinate.
	 * @param winz
	 *          Specify the window z coordinate.
	 * @param modelview
	 *          Specifies the current modelview matrix.
	 * @param projection
	 *          Specifies the current projection matrix.
	 * @param viewport
	 *          Specifies the current viewport.
	 * @param objCoordinate
	 *          Return the computed object coordinates.
	 */
	public boolean unproject(float winx, float winy, float winz, Matrix3D modelview,
			                     Matrix3D projection, int viewport[], float[] objCoordinate) {		
		if(!unprojectCacheOptimized) {
			if(!projectCacheOptimized) {	
				if(projectionTimesModelview == null)
					projectionTimesModelview = new Matrix3D();
				Matrix3D.mult(projectionMat, modelViewMat, projectionTimesModelview);
			}
			if(projectionTimesModelviewInverse == null)
				projectionTimesModelviewInverse = new Matrix3D();
			projectionTimesModelviewHasInverse = projectionTimesModelview.invert(projectionTimesModelviewInverse);						
		}		
		
		if (!projectionTimesModelviewHasInverse)
			return false;
		
		float in[] = new float[4];
		float out[] = new float[4];

		in[0] = winx;
		in[1] = winy;
		in[2] = winz;
		in[3] = 1.0f;

		/* Map x and y from window coordinates */
		in[0] = (in[0] - viewport[0]) / viewport[2];
		in[1] = (in[1] - viewport[1]) / viewport[3];

		/* Map to range -1 to 1 */
		in[0] = in[0] * 2 - 1;
		in[1] = in[1] * 2 - 1;
		in[2] = in[2] * 2 - 1;

		projectionTimesModelviewInverse.mult(in, out);
		if (out[3] == 0.0)
			return false;

		out[0] /= out[3];
		out[1] /= out[3];
		out[2] /= out[3];

		objCoordinate[0] = out[0];
		objCoordinate[1] = out[1];
		objCoordinate[2] = out[2];

		return true;
	}
	
  //TODO experimental
	
	public Matrix3D projection() {
		return projectionMat;
	}
	
	public Matrix3D modelview() {
		return modelViewMat;
	}
}
