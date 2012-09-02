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

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

/**
 * A perspective or orthographic camera.
 * <p>
 * A Camera defines some intrinsic parameters ({@link #fieldOfView()},
 * {@link #position()}, {@link #viewDirection()}, {@link #upVector()}...) and
 * useful positioning tools that ease its placement ({@link #showEntireScene()},
 * {@link #fitSphere(Vector3D, float)}, {@link #lookAt(Vector3D)}...). It exports
 * its associated processing projection and view matrices and it can
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
public class Camera extends Pinhole implements Constants, Copyable {
	@Override
	public int hashCode() {	
    return new HashCodeBuilder(17, 37).
    //append(fpCoefficientsUpdate).
    append(unprojectCacheOptimized).
    //append(lastFrameUpdate).
    //append(lastFPCoeficientsUpdateIssued).
    append(zClippingCoef).
		append(IODist).
		append(dist).
		append(fldOfView).
		append(focusDist).
		//append(fpCoefficients).
		append(frm).
		//append(interpolationKfi).
		append(knd).
		//append(viewMat).
		append(normal).
		append(orthoCoef).
		//append(orthoSize).
		append(physicalDist2Scrn).
		append(physicalScrnWidth).
		//append(projectionMat).
		//append(scnCenter).
		//append(scnRadius).
		//append(scrnHeight).
		//append(scrnWidth).
		append(stdZFar).
		append(stdZNear).
		//append(tempFrame).
		append(tp).
		//append(viewport).
		append(zClippingCoef).
		append(zNearCoef).
    toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		//TODO check me
		/**
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		*/
		//*/
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		// */
		
		Camera other = (Camera) obj;
		
	  return new EqualsBuilder()
    .appendSuper(super.equals(obj))
    //.append(fpCoefficientsUpdate, other.fpCoefficientsUpdate)
    .append(unprojectCacheOptimized, other.unprojectCacheOptimized)
    //.append(lastFrameUpdate, other.lastFrameUpdate)
    //.append(lastFPCoeficientsUpdateIssued, other.lastFPCoeficientsUpdateIssued)
    .append(zClippingCoef, other.zClippingCoef)
		.append(IODist,other.IODist)
		.append(dist,other.dist)
		.append(fldOfView,other.fldOfView)
		.append(focusDist,other.focusDist)
		//.append(fpCoefficients,other.fpCoefficients)
		.append(frm,other.frm)
		//.append(interpolationKfi,other.interpolationKfi)
		.append(knd,other.knd)
		//.append(viewMat,other.viewMat)
		.append(normal,other.normal)
		.append(orthoCoef,other.orthoCoef)
		//.append(orthoSize,other.orthoSize)
		.append(physicalDist2Scrn,other.physicalDist2Scrn)
		.append(physicalScrnWidth,other.physicalScrnWidth)
		//.append(projectionMat,other.projectionMat)
		//.append(scnCenter,other.scnCenter)
		//.append(scnRadius,other.scnRadius)
		//.append(scrnHeight,other.scrnHeight)
		//.append(scrnWidth,other.scrnWidth)
		.append(stdZFar,other.stdZFar)
		.append(stdZNear,other.stdZNear)
		//.append(tempFrame,other.tempFrame)
		.append(tp,other.tp)
		//.append(viewport,other.viewport)
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
	 * Enumerates the Camera kind.
	 */
	public enum Kind {
		PROSCENE, STANDARD
	}	

	// C a m e r a p a r a m e t e r s
	private float fldOfView; // in radians	
	private float zNearCoef;
	private float zClippingCoef;	
	private Type tp; // PERSPECTIVE or ORTHOGRAPHIC
	private Kind knd; // PROSCENE or STANDARD
	private float orthoCoef;
	private float stdZNear;
	private float stdZFar;	  
	
	protected Matrix3D projectionViewInverseMat;
	public boolean unprojectCacheOptimized;
	private boolean projectionViewMatHasInverse;

	// S t e r e o p a r a m e t e r s
	private float IODist; // inter-ocular distance, in meters
	private float focusDist; // in scene units
	private float physicalDist2Scrn; // in meters
	private float physicalScrnWidth; // in meters				

	/**
	 * Main constructor.
	 * <p>
	 * {@link #sceneCenter()} is set to (0,0,0) and {@link #sceneRadius()} is set
	 * to 100. {@link #type()} Camera.PERSPECTIVE, with a {@code PI/4}
	 * {@link #fieldOfView()}.
	 * <p>
	 * Camera matrices (projection and view) are created and computed according
	 * to remaining default Camera parameters.
	 * <p>
	 * See {@link #IODistance()}, {@link #physicalDistanceToScreen()},
	 * {@link #physicalScreenWidth()} and {@link #focusDistance()} documentations
	 * for default stereo parameter values. 
	 */
	public Camera(AbstractScene scn) {
		super(scn);
		
		if(scene.is2D())
			throw new RuntimeException("Use Camera only for a 3D Scene");
		
		enableFrustumEquationsUpdate(false);
		optimizeUnprojectCache(false);

		for (int i = 0; i < normal.length; i++)
			normal[i] = new Vector3D();

		fldOfView = (float) Math.PI / 4.0f;

		fpCoefficients = new float[6][4];

		/**
		//TODO subido
		// KeyFrames
		interpolationKfi = new KeyFrameInterpolator(scene, frame());
		kfi = new HashMap<Integer, KeyFrameInterpolator>();
		
		setFrame(new InteractiveCameraFrame(this));
		
		// Requires fieldOfView() to define focusDistance()
		setSceneRadius(100);
		// */		

		// Initial value (only scaled after this)
		orthoCoef = (float) Math.tan(fieldOfView() / 2.0f);

		/**
		//TODO subido
		// Also defines the arcballReferencePoint(), which changes orthoCoef.
		setSceneCenter(new Vector3D(0.0f, 0.0f, 0.0f));
		// */

		setKind(Kind.PROSCENE);		
		setStandardZNear(0.001f);// only for standard kind, but we initialize it
															// here
		setStandardZFar(1000.0f);// only for standard kind, but we initialize it
															// here

		// Requires fieldOfView() when called with ORTHOGRAPHIC. Attention to
		// projectionMat below.
		setType(Camera.Type.PERSPECTIVE);

		setZNearCoefficient(0.005f);
		setZClippingCoefficient((float) Math.sqrt(3.0f));

		/**
		//TODO subido
		// Dummy values
		setScreenWidthAndHeight(600, 400);
		// */

		// Stereo parameters
		setIODistance(0.062f);
		setPhysicalDistanceToScreen(0.5f);
		setPhysicalScreenWidth(0.4f);
		// focusDistance is set from setFieldOfView()
		
		/**
		//TODO subido
		viewMat = new Matrix3D();
		projectionMat = new Matrix3D();
		projectionMat.set(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		computeProjectionMatrix();
		projectionViewMat = new Matrix3D();
		// */
		computeProjectionMatrix();
	}
	
	/**
	 * Copy constructor 
	 * 
	 * @param oCam the camera object to be copied
	 */
	protected Camera(Camera oCam) {
		super(oCam);
				
		this.unprojectCacheOptimized = oCam.unprojectCacheOptimized;
		
		for (int i = 0; i < normal.length; i++)
			this.normal[i] = new Vector3D(oCam.normal[i].vec[0], oCam.normal[i].vec[1], oCam.normal[i].vec[2] );
		
		this.fldOfView = oCam.fldOfView;
		this.orthoCoef = oCam.orthoCoef;		
		this.setKind(oCam.kind());		
		this.setStandardZNear(oCam.standardZNear());
		this.setStandardZFar(oCam.standardZFar());
		this.setType(oCam.type());
		this.setZNearCoefficient(oCam.zNearCoefficient());
		this.setZClippingCoefficient(oCam.zClippingCoefficient());		
		this.setIODistance( oCam.IODistance() );
		this.setPhysicalDistanceToScreen(oCam.physicalDistanceToScreen());
		this.setPhysicalScreenWidth( oCam.physicalScreenWidth() );
	}
	
	/**
	 * Calls {@link #Camera(Camera)} (which is protected) and returns a copy of
	 * {@code this} object.
	 * 
	 * @see #Camera(Camera)
	 */	
	@Override
	public Camera get() {
		return new Camera(this);
	}

	// 2. POSITION AND ORIENTATION		

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
	@Override
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
	@Override
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
	@Override
	public void changeStandardOrthoFrustumSize(boolean augment) {
		if( (kind() == Camera.Kind.STANDARD) && (type() == Camera.Type.ORTHOGRAPHIC) )
			lastFrameUpdate = scene.frameCount();
		if (augment)
			orthoSize *= 1.01f;
		else
			orthoSize /= 1.01f;
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
	@Override
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
	 * {@link remixlab.remixcam.core.AbstractScene#setViewPort(Camera)}.
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
	@Override
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
	@Override
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
	@Override
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
    
  /**
  // Only works in ortho mode. Perspective needs to take into account the
  // translation of the vector and hence needs more info 
  public boolean faceIsBackFacing(Vector3D normal) {
  	// http://stackoverflow.com/questions/724219/how-to-convert-a-3d-point-into-2d-perspective-projection
  	
  	getProjectionViewMatrix(true); //TODO testing
  	float [] normal_array = new float [3];
  	float [] normal_array_homogeneous = new float [4];
  	normal.get(normal_array);
  	normal_array_homogeneous[0] = normal_array[0];
  	normal_array_homogeneous[1] = normal_array[1];
  	normal_array_homogeneous[2] = normal_array[2];
  	normal_array_homogeneous[3] = 0;// key is the value of 0 here
  	float [] result = new float [4];  	
  	projectionViewMat.mult(normal_array_homogeneous, result);  	
  	
  	if(result[2] >= 0)
  		return true;
  	else
  		return false;
  	  	
  	// same as above  	
  	//if(projectionViewMat.mat[2]*normal.x() +
  		// projectionViewMat.mat[6]*normal.y() +
  		// projectionViewMat.mat[10]*normal.z() >= 0)
  		//return true;
  	//else
  		//return false;
  }
  */

	// 4. SCENE RADIUS AND CENTER	

	/**
	 * Sets the {@link #sceneRadius()} value. Negative values are ignored.
	 * <p>
	 * <b>Attention:</b> This methods also sets {@link #focusDistance()} to
	 * {@code sceneRadius() / tan(fieldOfView()/2)} and {@link #flySpeed()} to 1%
	 * of {@link #sceneRadius()} (if there's an Scene
	 * {@link remixlab.remixcam.core.AbstractScene#avatar()} and it is an instance of
	 * InteractiveDrivableFrame it also sets {@code flySpeed} to the same value).
	 */
	@Override
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
	
	@Override
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
	 * Changes the {@link #arcballReferencePoint()} to {@code rap} (defined in the
	 * world coordinate system).
	 */
	@Override
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
	@Override
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
	@Override
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
		return scene.pointUnderPixel(pixel);
	}
	
	// 8. PROCESSING MATRICES

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
	@Override
	public void computeProjectionMatrix() {
		float ZNear = zNear();
		float ZFar = zFar();
		
		switch (type()) {
		case PERSPECTIVE: {
			// #CONNECTION# all non null coefficients were set to 0.0 in
			// constructor.
			float f = 1.0f / (float) Math.tan(fieldOfView() / 2.0f);
			projectionMat.mat[0] = f / aspectRatio();
			if( scene.isAP5Scene() )
				projectionMat.mat[5] = -f;
			else
				projectionMat.mat[5] = f;
			projectionMat.mat[10] = (ZNear + ZFar) / (ZNear - ZFar);
			projectionMat.mat[11] = -1.0f;
			projectionMat.mat[14] = 2.0f * ZNear * ZFar / (ZNear - ZFar);
			projectionMat.mat[15] = 0.0f;
			// same as gluPerspective( 180.0*fieldOfView()/M_PI, aspectRatio(), zNear(), zFar() );
			break;
		}
		case ORTHOGRAPHIC: {
			float[] wh = getOrthoWidthHeight();
			projectionMat.mat[0] = 1.0f / wh[0];
			if( scene.isAP5Scene() )
				projectionMat.mat[5] = -1.0f / wh[1];
			else
				projectionMat.mat[5] = 1.0f / wh[1];
			projectionMat.mat[10] = -2.0f / (ZFar - ZNear);
			projectionMat.mat[11] = 0.0f;
			projectionMat.mat[14] = -(ZFar + ZNear) / (ZFar - ZNear);
			projectionMat.mat[15] = 1.0f;
			// same as glOrtho( -w, w, -h, h, zNear(), zFar() );
			break;
		}
		}
	}	
	
  //TODO needed for screen drawing
	public void ortho(float left, float right, float bottom, float top, float near, float far) {
		float x = +2.0f / (right - left);
		float y = +2.0f / (top - bottom);
		float z = -2.0f / (far - near);
		
		float tx = -(right + left) / (right - left);
		float ty = -(top + bottom) / (top - bottom);
		float tz = -(far + near)   / (far - near);
			
		if( scene.isAP5Scene() )
		  // The minus sign is needed to invert the Y axis.
			projectionMat.set(x,  0, 0, tx,
                        0, -y, 0, ty,
                        0,  0, z, tz,
                        0,  0, 0,  1);
		else
			projectionMat.set(x,  0, 0, tx,
                        0,  y, 0, ty,
                        0,  0, z, tz,
                        0,  0, 0,  1);
	}	
	
	public void perspective(float fov, float aspect, float zNear, float zFar) {
    float ymax = zNear * (float) Math.tan(fov / 2);
    float ymin = -ymax;
    float xmin = ymin * aspect;
    float xmax = ymax * aspect;
    frustum(xmin, xmax, ymin, ymax, zNear, zFar);
  }
	
	public void frustum(float left, float right, float bottom, float top,  float znear, float zfar) {
		float n2 = 2 * znear;
		float w = right - left;
		float h = top - bottom;
		float d = zfar - znear;
		
		if( scene.isAP5Scene() )
			projectionMat.set(n2 / w,       0,  (right + left) / w,                0,
                             0, -n2 / h,  (top + bottom) / h,                0,
                             0,       0, -(zfar + znear) / d, -(n2 * zfar) / d,
                             0,       0,                  -1,                0);
		else
			projectionMat.set(n2 / w,       0,  (right + left) / w,                0,
                             0,  n2 / h,  (top + bottom) / h,                0,
                             0,       0, -(zfar + znear) / d, -(n2 * zfar) / d,
                             0,       0,                  -1,                0);
	}
	
	//TODO needed for screen drawing
	/**
	public void camera() {
    camera(cameraX, cameraY, cameraZ, cameraX, cameraY, 0, 0, 1, 0);
  }
	
	public void camera(float eyeX, float eyeY, float eyeZ,
			               float centerX, float centerY, float centerZ,
                     float upX, float upY, float upZ) {
		
		// Calculating Z vector
		float z0 = eyeX - centerX;
		float z1 = eyeY - centerY;
		float z2 = eyeZ - centerZ;
		float mag = (float)Math.sqrt(z0 * z0 + z1 * z1 + z2 * z2);
		if (nonZero(mag)) {
			z0 /= mag;
			z1 /= mag;
			z2 /= mag;
		}
		cameraEyeX = eyeX;
		cameraEyeY = eyeY;
		cameraEyeZ = eyeZ;
		
		// Calculating Y vector
		float y0 = upX;
		float y1 = upY;
		float y2 = upZ;
		
		// Computing X vector as Y cross Z
		float x0 =  y1 * z2 - y2 * z1;
		float x1 = -y0 * z2 + y2 * z0;
		float x2 =  y0 * z1 - y1 * z0;
		
		// Recompute Y = Z cross X
		y0 =  z1 * x2 - z2 * x1;
		y1 = -z0 * x2 + z2 * x0;
		y2 =  z0 * x1 - z1 * x0;
		
		// Cross product gives area of parallelogram, which is < 1.0 for
		// non-perpendicular unit-length vectors; so normalize x, y here:
		mag = (float) Math.sqrt(x0 * x0 + x1 * x1 + x2 * x2);
		if (nonZero(mag)) {
			x0 /= mag;
			x1 /= mag;
			x2 /= mag;
		}
		
		mag = (float) Math.sqrt(y0 * y0 + y1 * y1 + y2 * y2);
		if (nonZero(mag)) {
			y0 /= mag;
			y1 /= mag;
			y2 /= mag;
		}
		
		modelview.set(x0, x1, x2, 0,
				          y0, y1, y2, 0,
				          z0, z1, z2, 0,
				          0,  0,  0, 1);
		
		float tx = -eyeX;
		float ty = -eyeY;
		float tz = -eyeZ;
		modelview.translate(tx, ty, tz);
	}
	*/
	
	@Override
	public Matrix3D getProjectionViewMatrix(Matrix3D m, boolean recompute) {
		if (m == null)
			m = new Matrix3D();
		if(recompute) {
			// May not be needed, but easier like this.
			// Prevents from retrieving matrix in stereo mode -> overwrites shifted value.
			computeProjectionMatrix();
			computeViewMatrix();
			cacheMatrices();
		}
		m.set(projectionViewMat);
		return m;
	}	
	
	/*! Same as loadProjectionMatrix() but for a stereo setup.

	 Only the Camera::PERSPECTIVE type() is supported for stereo mode. See
	 QGLViewer::setStereoDisplay().

	 Uses focusDistance(), IODistance(), and physicalScreenWidth() to compute cameras
	 offset and asymmetric frustums.

	 When \p leftBuffer is \c true, computes the projection matrix associated to the left eye (right eye
	 otherwise). See also loadViewMatrixStereo().

	 See the <a href="../examples/stereoViewer.html">stereoViewer</a> and the <a
	 href="../examples/contribs.html#anaglyph">anaglyph</a> examples for an illustration.

	 To retrieve this matrix, use a code like:
	 \code
	 glMatrixMode(GL_PROJECTION);
	 glPushMatrix();
	 loadProjectionMatrixStereo(left_or_right);
	 glGetFloatv(GL_PROJECTION_MATRIX, m);
	 glPopMatrix();
	 \endcode
	 Note that getProjectionMatrix() always returns the mono-vision matrix.

	 \attention glMatrixMode is set to \c GL_PROJECTION. */
	public void loadProjectionMatrixStereo(boolean leftBuffer) {
	  float left, right, bottom, top;
	  float screenHalfWidth, halfWidth, side, shift, delta;

	  scene.resetProjection();

	  switch (type())  {
	    case PERSPECTIVE:
	      // compute half width of screen,
	      // corresponding to zero parallax plane to deduce decay of cameras
	      screenHalfWidth = focusDistance() * (float) Math.tan(horizontalFieldOfView() / 2.0f);
	      shift = screenHalfWidth * IODistance() / physicalScreenWidth();
	      // should be * current y  / y total
	      // to take into account that the window doesn't cover the entire screen

	      // compute half width of "view" at znear and the delta corresponding to
	      // the shifted camera to deduce what to set for asymmetric frustums
	      halfWidth = zNear() * (float) Math.tan(horizontalFieldOfView() / 2.0f);
	      delta  = shift * zNear() / focusDistance();
	      side   = leftBuffer ? -1.0f : 1.0f;

	      left   = -halfWidth + side * delta;
	      right  =  halfWidth + side * delta;
	      top    = halfWidth / aspectRatio();
	      bottom = -top;
	      scene.frustum(left, right, bottom, top, zNear(), zFar());
	      break;

	    case ORTHOGRAPHIC:
	      //qWarning("Camera::setProjectionMatrixStereo: Stereo not available with Ortho mode");
	      break;
	    }
	}  

	/*! Same as loadModelViewMatrix() but for a stereo setup.

	 Only the Camera::PERSPECTIVE type() is supported for stereo mode. See
	 QGLViewer::setStereoDisplay().

	 The modelView matrix is almost identical to the mono-vision one. It is simply translated along its
	 horizontal axis by a value that depends on stereo parameters (see focusDistance(),
	 IODistance(), and physicalScreenWidth()).

	 When \p leftBuffer is \c true, computes the modelView matrix associated to the left eye (right eye
	 otherwise).

	 loadProjectionMatrixStereo() explains how to retrieve to resulting matrix.

	 See the <a href="../examples/stereoViewer.html">stereoViewer</a> and the <a
	 href="../examples/contribs.html#anaglyph">anaglyph</a> examples for an illustration.

	 \attention glMatrixMode is set to \c GL_MODELVIEW. */
	public void loadViewMatrixStereo(boolean leftBuffer) {	  
	  float halfWidth = focusDistance() * (float) Math.tan(horizontalFieldOfView() / 2.0f);
	  float shift     = halfWidth * IODistance() / physicalScreenWidth(); // * current window width / full screen width

	  computeViewMatrix();
	  if (leftBuffer)
	    viewMat.mat[12] = viewMat.mat[12]-shift;
	  else
	  	viewMat.mat[12] = viewMat.mat[12]+shift;
	  scene.loadMatrix(viewMat);
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
	@Override
	public final Vector3D cameraCoordinatesOf(Vector3D src) {
		return frame().coordinatesOf(src);
	}

	/**
	 * Returns the world coordinates of the point whose position {@code src} is
	 * defined in the Camera coordinate system.
	 * <p>
	 * {@link #cameraCoordinatesOf(Vector3D)} performs the inverse transformation.
	 */
	@Override
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
		if( scene.isLeftHanded() )
			pixel.y = screenHeight() - pixelInput.y;
		
		switch (type()) {
		case PERSPECTIVE:
			orig.set(position());
			dir.set(new Vector3D(((2.0f * (int)pixel.x / screenWidth()) - 1.0f)	* (float) Math.tan(fieldOfView() / 2.0f) * aspectRatio(),
					                 ((2.0f * (screenHeight() - (int)pixel.y) / screenHeight()) - 1.0f) * (float) Math.tan(fieldOfView() / 2.0f),
					                   -1.0f));
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
	 * {@link #getViewMatrix()}, {@link #getProjectionMatrix()} and
	 * {@link #getViewport()}) and is completely independent of the Processing
	 * matrices. You can hence define a virtual Camera and use this method to
	 * compute un-projections out of a classical rendering context.
	 * <p>
	 * <b>Attention:</b> However, if your Camera is not attached to a Scene (used
	 * for offscreen computations for instance), make sure the Camera matrices are
	 * updated before calling this method (use {@link #computeViewMatrix()},
	 * {@link #computeProjectionMatrix()}).
	 * <p>
	 * This method is not computationally optimized. If you call it several times
	 * with no change in the matrices, you should buffer the entire inverse
	 * projection matrix (view, projection and then viewport) to speed-up the
	 * queries. See the gluUnProject man page for details.
	 * 
	 * @see #projectedCoordinatesOf(Vector3D, SimpleFrame)
	 * @see #setScreenWidthAndHeight(int, int)
	 */
	public final Vector3D unprojectedCoordinatesOf(Vector3D src, SimpleFrame frame) {
		float xyz[] = new float[3];
		viewport = getViewport();
		
		/**
		// TODO needs further testing
	  if( scene.isRightHanded() )
	  	unproject(src.vec[0], src.vec[1], src.vec[2], viewMat, projectionMat, viewport, xyz);
	  else
	  	unproject(src.vec[0], (screenHeight() - src.vec[1]), src.vec[2], viewMat,	projectionMat, viewport, xyz);
	  */
		
		unproject(src.vec[0], src.vec[1], src.vec[2], viewMat, projectionMat, viewport, xyz);
		
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
	@Override
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
	@Override
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
	
	@Override
	public void showEntireScene() {
		fitSphere(sceneCenter(), sceneRadius());
	}
	
	@Override
	public void centerScene() {
		frame().projectOnLine(sceneCenter(), viewDirection());
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
	@Override
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
	@Override
	public void cacheMatrices() {
		// 1. project
		super.cacheMatrices();		
		
		// 2. unproject
		if(unprojectCacheIsOptimized()) {
			if(projectionViewInverseMat == null)
				projectionViewInverseMat = new Matrix3D();
			projectionViewMatHasInverse = projectionViewMat.invert(projectionViewInverseMat);
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
	 * Similar to {@code gluUnProject}: map window coordinates to object
	 * coordinates.
	 * 
	 * @param winx
	 *          Specify the window x coordinate.
	 * @param winy
	 *          Specify the window y coordinate.
	 * @param winz
	 *          Specify the window z coordinate.
	 * @param view
	 *          Specifies the current view matrix.
	 * @param projection
	 *          Specifies the current projection matrix.
	 * @param viewport
	 *          Specifies the current viewport.
	 * @param objCoordinate
	 *          Return the computed object coordinates.
	 */
	public boolean unproject(float winx, float winy, float winz, Matrix3D view,
			                     Matrix3D projection, int viewport[], float[] objCoordinate) {		
		if(!unprojectCacheOptimized) {			
			if(projectionViewInverseMat == null)
				projectionViewInverseMat = new Matrix3D();
			projectionViewMatHasInverse = projectionViewMat.invert(projectionViewInverseMat);						
		}		
		
		if (!projectionViewMatHasInverse)
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

		projectionViewInverseMat.mult(in, out);
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
}
