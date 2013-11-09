/*******************************************************************************
 * dandelion (version 1.0.0-alpha.1)
 * Copyright (c) 2013 National University of Colombia, https://github.com/remixlab
 * @author Jean Pierre Charalambos, http://otrolado.info/
 *     
 * All rights reserved. Library that eases the creation of interactive
 * scenes, released under the terms of the GNU Public License v3.0
 * which is available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package remixlab.dandelion.core;

import remixlab.dandelion.geom.*;
import remixlab.tersehandling.core.Copyable;
import remixlab.tersehandling.core.Util;

import java.util.ArrayList;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

/**
 * A perspective or orthographic camera.
 * <p>
 * A Camera defines some intrinsic parameters ({@link #fieldOfView()},
 * {@link #position()}, {@link #viewDirection()}, {@link #upVector()}...) and
 * useful positioning tools that ease its placement ({@link #showEntireScene()},
 * {@link #fitSphere(Vec, float)}, {@link #lookAt(Vec)}...). It exports
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
public class Camera extends Viewport implements Constants, Copyable {
	@Override
	public int hashCode() {	
    return new HashCodeBuilder(17, 37).
    appendSuper(super.hashCode()).
    //append(fpCoefficientsUpdate).
    //append(unprojectCacheOptimized).
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
		append(orthoSize).
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
		append(cadRotate).
    toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		//TODO check me		
		/**
		if (this == obj) return true;
		if (!super.equals(obj))	return false;
		if (getClass() != obj.getClass())	return false;
		// */
		
		if (obj == null) return false;
		if (obj == this) return true;		
		if (obj.getClass() != getClass()) return false;
		
		Camera other = (Camera) obj;		
	  return new EqualsBuilder()
    .appendSuper(super.equals(obj))
    //.append(fpCoefficientsUpdate, other.fpCoefficientsUpdate)
    //.append(unprojectCacheOptimized, other.unprojectCacheOptimized)
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
		.append(orthoSize,other.orthoSize)
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
		.append(cadRotate,other.cadRotate)
		.isEquals();
	}	
	
	/**
	 * Internal class that represents/holds a cone of normals.
	 * Typically needed to perform bfc.
	 */
	public class Cone {
		Vec axis;
		float angle;
		
		public Cone() {
			reset();
		}
		
		public Cone(Vec vec, float a) {
			set(vec, a);
		}
		
		public Cone(ArrayList<Vec> normals) {
			set(normals);
		}
		
		public Cone(Vec [] normals) {
			set(normals);
		}
		
		public Vec axis() {
			return axis;
		}
		
		public float angle() {
			return angle;
		}
		
		public void reset() {
			axis = new Vec(0,0,1);
			angle = 0;
		}
		
		public void set(Vec vec, float a) {
			axis = vec;
			angle = a;
		}
		
		public void set(ArrayList<Vec> normals) {
			set( normals.toArray( new Vec [normals.size()] ) );		
		}
		
		public void set(Vec [] normals) {
			axis = new Vec(0,0,0);
			if(normals.length == 0) {
				reset();
				return;
			}
			
			Vec [] n = new Vec [normals.length];
			for(int i=0; i<normals.length; i++ ) {
				n[i] = new Vec();
				n[i].set(normals[i]);
				n[i].normalize();
				axis = Vec.add(axis, n[i]);
			}
			
			if ( Util.nonZero(axis.mag()) ) {
	      axis.normalize();
	    }
	    else {
	      axis.set(0,0,1);
	    }
			
			angle = 0;        
			for(int i=0; i<normals.length; i++ )		
				angle = Math.max( angle, (float) Math.acos( Vec.dot(n[i], axis)));		
		}	
	}
	
	/**
	 * Internal class provided to catch the output of
	 * {@link remixlab.dandelion.core.Camera#pointUnderPixel(Point)} (which should be
	 * implemented by an openGL based derived class Camera).
	 */
	public class WorldPoint {
		public WorldPoint(Vec p, boolean f) {
			point = p;
			found = f;
		}

		public Vec point;
		public boolean found;
	}
	
	// next variables are needed for frustum plane coefficients
	Vec normal[] = new Vec[6];
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
	private float orthoSize;
	private float orthoCoef;
	private float stdZNear;
	private float stdZFar;

	// S t e r e o p a r a m e t e r s
	private float IODist; // inter-ocular distance, in meters
	private float focusDist; // in scene units
	private float physicalDist2Scrn; // in meters
	private float physicalScrnWidth; // in meters			
	
	protected boolean cadRotate;

	/**
	 * Main constructor.
	 * <p>
	 * {@link #sceneCenter()} is set to (0,0,0) and {@link #sceneRadius()} is set
	 * to 100. {@link #type()} Camera.PERSPECTIVE, with a {@code PI/3}
	 * {@link #fieldOfView()} (same value used in P5 by default).
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

		for (int i = 0; i < normal.length; i++)
			normal[i] = new Vec();

		fldOfView = (float) Math.PI / 3.0f; //in Proscene 1.x it was Pi/4

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
		orthoSize = 1;// only for standard kind, but we initialize it here
		setStandardZNear(0.001f);// only for standard kind, but we initialize it here
		setStandardZFar(1000.0f);// only for standard kind, but we initialize it here

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
		computeProjection();
	}
	
	/**
	 * Copy constructor 
	 * 
	 * @param oCam the camera object to be copied
	 */
	protected Camera(Camera oCam) {
		super(oCam);	
		
		for (int i = 0; i < normal.length; i++)
			this.normal[i] = new Vec(oCam.normal[i].vec[0], oCam.normal[i].vec[1], oCam.normal[i].vec[2] );
		
		this.fldOfView = oCam.fldOfView;
		this.orthoCoef = oCam.orthoCoef;
		this.orthoSize = oCam.orthoSize;
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
	
	public boolean isArcBallRotate() {
		return !isCadRotate();
	}
	
	public boolean isCadRotate() {
		return cadRotate;
	}
	
	public void setCadRotate() {
		cadRotate = true;
	}
	
	public void setArcBallRotate() {
		cadRotate = false;
	}

	// 2. POSITION AND ORIENTATION
	
	@Override
	public Vec rightVector() {
		return frame().magnitude().x() > 0 ? frame().xAxis() : frame().xAxis(false);
	}
	
	@Override
	public Vec upVector() {
		return frame().magnitude().y() > 0 ? frame().yAxis() : frame().yAxis(false);
	}
	
	@Override
	public void setUpVector(Vec up, boolean noMove) {
		Quat q = new Quat(new Vec(0.0f, frame().magnitude().y() > 0 ? 1.0f : -1.0f, 0.0f), frame().transformOf(up));

		if (!noMove && scene.is3D())
			frame().setPosition(Vec.sub(arcballReferencePoint(), 
							            (Quat.multiply((Quat)frame().orientation(), q)).rotate(frame().coordinatesOf(arcballReferencePoint()))));
		
		frame().rotate(q);

		// Useful in fly mode to keep the horizontal direction.
		frame().updateFlyUpVector();
	}

	/**
	 * Returns the normalized view direction of the Camera, defined in the world
	 * coordinate system.
	 * <p>
	 * Change this value using {@link #setViewDirection(Vec)},
	 * {@link #lookAt(Vec)} or {@link #setOrientation(Quat)}. It is
	 * orthogonal to {@link #upVector()} and to {@link #rightVector()}.
	 * <p>
	 * This corresponds to the negative Z axis of the {@link #frame()} ( {@code
	 * frame().inverseTransformOf(new Vector3D(0.0f, 0.0f, -1.0f))} ).
	 */
	@Override
	public Vec viewDirection() {
		return frame().magnitude().z() > 0 ? frame().zAxis(false) : frame().zAxis();
	}	

	/**
	 * Rotates the Camera so that its {@link #viewDirection()} is {@code
	 * direction} (defined in the world coordinate system).
	 * <p>
	 * The Camera {@link #position()} is not modified. The Camera is rotated so
	 * that the horizon (defined by its {@link #upVector()}) is preserved.
	 * 
	 * @see #lookAt(Vec)
	 * @see #setUpVector(Vec)
	 */
	public void setViewDirection(Vec direction) {
		if (Util.zero(direction.squaredNorm()))
			return;

		Vec xAxis = direction.cross(upVector());
		if (Util.zero(xAxis.squaredNorm())) {
			// target is aligned with upVector, this means a rotation around X axis
			// X axis is then unchanged, let's keep it !
			xAxis = frame().inverseTransformOf(new Vec(1.0f, 0.0f, 0.0f));
		}

		Quat q = new Quat();
		q.fromRotatedBasis(xAxis, xAxis.cross(direction), Vec.mult(direction,	-1));
		frame().setOrientationWithConstraint(q);
	}

	/**
	 * Returns the Camera orientation, defined in the world coordinate system.
	 * <p>
	 * Actually returns {@code frame().orientation()}. Use
	 * {@link #setOrientation(Quat)}, {@link #setUpVector(Vec)} or
	 * {@link #lookAt(Vec)} to set the Camera orientation.
	 */
	public Orientable orientation() {
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
	 * @see #setUpVector(Vec)
	 */
	public void setOrientation(float theta, float phi) {
		// TODO: need check.
		Vec axis = new Vec(0.0f, 1.0f, 0.0f);
		Quat rot1 = new Quat(axis, theta);
		axis.set(-(float) Math.cos(theta), 0.0f, (float) Math.sin(theta));
		Quat rot2 = new Quat(axis, phi);
		setOrientation(Quat.multiply(rot1, rot2));
	}

	/**
	 * Sets the Camera {@link #orientation()}, defined in the world coordinate
	 * system.
	 */
	@Override
	public void setOrientation(Orientable q) {
		frame().setOrientation(q);
		frame().updateFlyUpVector();
	}

	// 3. FRUSTUM

	/**
	 * Returns the Camera.Type of the Camera.
	 * <p>
	 * Set by {@link #setType(Type)}.
	 * <p>
	 * A {@link remixlab.dandelion.core.Camera.Type#PERSPECTIVE} Camera uses a classical
	 * projection mainly defined by its {@link #fieldOfView()}.
	 * <p>
	 * With a {@link remixlab.dandelion.core.Camera.Type#ORTHOGRAPHIC} {@link #type()},
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
			lastFrameUpdate = scene.timerHandler().frameCount();
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
			lastFrameUpdate = scene.timerHandler().frameCount();
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
			lastFrameUpdate = scene.timerHandler().frameCount();
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
			lastFrameUpdate = scene.timerHandler().frameCount();
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
			lastFrameUpdate = scene.timerHandler().frameCount();
		if ((type == Camera.Type.ORTHOGRAPHIC) && (type() == Camera.Type.PERSPECTIVE))
			orthoCoef = (float) Math.tan(fieldOfView() / 2.0f);

		this.tp = type;
	}

	/**
	 * Returns the vertical field of view of the Camera (in radians).
	 * <p>
	 * Value is set using {@link #setFieldOfView(float)}. Default value is pi/4
	 * radians. This value is meaningless if the Camera {@link #type()} is
	 * {@link remixlab.dandelion.core.Camera.Type#ORTHOGRAPHIC}.
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
	 * by {@link remixlab.dandelion.core.AbstractScene#center()} and 
	 * {@link remixlab.dandelion.core.AbstractScene#radius()} is visible from the Camera
	 * {@link #position()}.
	 * <p>
	 * The {@link #position()} and {@link #orientation()} of the Camera are not
	 * modified and you first have to orientate the Camera in order to actually
	 * see the scene (see {@link #lookAt(Vec)}, {@link #showEntireScene()} or
	 * {@link #fitSphere(Vec, float)}).
	 * <p>
	 * This method is especially useful for <i>shadow maps</i> computation. Use
	 * the Camera positioning tools ({@link #setPosition(Vec)},
	 * {@link #lookAt(Vec)}) to position a Camera at the light position. Then
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
	 * translated forward and its frustum is narrowed, making the object appear
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
		
		float dist = (kind() == Kind.STANDARD)
				       ? sceneRadius() * standardOrthoFrustumSize()
				       : orthoCoef * distanceToARP();
		
		// 1. halfWidth
		target[0] = dist * ((aspectRatio() < 1.0f) ? 1.0f : aspectRatio());
		// 2. halfHeight
		target[1] = dist * ((aspectRatio() < 1.0f) ? 1.0f / aspectRatio() : 1.0f);		

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
	 * {@link remixlab.dandelion.core.AbstractScene#setViewPort(Camera)}.
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
		final float zMin = zNearCoefficient() * zClippingCoefficient() * sceneRadius();
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
			lastFrameUpdate = scene.timerHandler().frameCount();
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
			lastFrameUpdate = scene.timerHandler().frameCount();
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
	@Override
	public float pixelP5Ratio(Vec position) {
		switch (type()) {
		case PERSPECTIVE:
			return 2.0f * Math.abs((frame().coordinatesOf(position, false)).vec[2]) * (float) Math.tan(fieldOfView() / 2.0f) / screenHeight();
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
	 * {@link remixlab.dandelion.core.AbstractScene#enableFrustumEquationsUpdate()}).
	 * 
	 * @see #distanceToFrustumPlane(int, Vec)
	 * @see #sphereIsVisible(Vec, float)
	 * @see #aaBoxIsVisible(Vec, Vec)
	 * @see #computeFrustumEquations()
	 * @see #updateFrustumEquations()
	 * @see #getFrustumEquations()
	 * @see remixlab.dandelion.core.AbstractScene#enableFrustumEquationsUpdate()
	 */
	@Override
	public boolean pointIsVisible(Vec point) {
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
	 * Returns {@link remixlab.dandelion.core.Viewport.Visibility#VISIBLE},
	 * {@link remixlab.dandelion.core.Viewport.Visibility#INVISIBLE}, or
	 * {@link remixlab.dandelion.core.Viewport.Visibility#SEMIVISIBLE}, depending whether
	 * the sphere (of radius {@code radius} and center {@code center}) is visible,
	 * invisible, or semi-visible, respectively.
	 * <p>
	 * <b>Attention:</b> The camera frustum plane equations should be updated
	 * before calling this method. You may compute them explicitly (by calling
	 * {@link #computeFrustumEquations()} ) or enable them to be automatic updated
	 * in your Scene setup (with
	 * {@link remixlab.dandelion.core.AbstractScene#enableFrustumEquationsUpdate()}).
	 * 
	 * @see #distanceToFrustumPlane(int, Vec)
	 * @see #pointIsVisible(Vec)
	 * @see #aaBoxIsVisible(Vec, Vec)
	 * @see #computeFrustumEquations()
	 * @see #updateFrustumEquations()
	 * @see #getFrustumEquations()
	 * @see remixlab.dandelion.core.AbstractScene#enableFrustumEquationsUpdate()
	 */
	public Visibility sphereIsVisible(Vec center, float radius) {
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
	 * Returns {@link remixlab.dandelion.core.Camera.Visibility#VISIBLE},
	 * {@link remixlab.dandelion.core.Camera.Visibility#INVISIBLE}, or
	 * {@link remixlab.dandelion.core.Camera.Visibility#SEMIVISIBLE}, depending whether
	 * the axis aligned box (defined by corners {@code p1} and {@code p2}) is
	 * visible, invisible, or semi-visible, respectively.
	 * <p>
	 * <b>Attention:</b> The camera frustum plane equations should be updated
	 * before calling this method. You may compute them explicitly (by calling
	 * {@link #computeFrustumEquations()} ) or enable them to be automatic updated
	 * in your Scene setup (with
	 * {@link remixlab.dandelion.core.AbstractScene#enableFrustumEquationsUpdate()}).
	 * 
	 * @see #distanceToFrustumPlane(int, Vec)
	 * @see #pointIsVisible(Vec)
	 * @see #sphereIsVisible(Vec, float)
	 * @see #computeFrustumEquations()
	 * @see #updateFrustumEquations()
	 * @see #getFrustumEquations()
	 * @see remixlab.dandelion.core.AbstractScene#enableFrustumEquationsUpdate()
	 */
	public Visibility aaBoxIsVisible(Vec p1, Vec p2) {
		if (!scene.frustumEquationsUpdateIsEnable())
			System.out.println("The camera frustum plane equations (needed by aaBoxIsVisible) may be outdated. Please "
							+ "enable automatic updates of the equations in your PApplet.setup "
							+ "with Scene.enableFrustumEquationsUpdate()");
		boolean allInForAllPlanes = true;
		for (int i = 0; i < 6; ++i) {
			boolean allOut = true;
			for (int c = 0; c < 8; ++c) {
				Vec pos = new Vec(((c & 4) != 0) ? p1.vec[0] : p2.vec[0],
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
	 * {@link remixlab.dandelion.core.AbstractScene#enableFrustumEquationsUpdate()} which
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
	 * {@link remixlab.dandelion.core.AbstractScene#enableFrustumEquationsUpdate()} which
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
		Vec pos = position();
		Vec viewDir = viewDirection();
		Vec up = upVector();
		Vec right = rightVector();
		
		float posViewDir = Vec.dot(pos, viewDir);

		switch (type()) {
		case PERSPECTIVE: {
			float hhfov = horizontalFieldOfView() / 2.0f;
			float chhfov = (float) Math.cos(hhfov);
			float shhfov = (float) Math.sin(hhfov);
			normal[0] = Vec.mult(viewDir, -shhfov);
			normal[1] = Vec.add(normal[0], Vec.mult(right, chhfov));
			normal[0] = Vec.add(normal[0], Vec.mult(right, -chhfov));
			normal[2] = Vec.mult(viewDir, -1);
			normal[3] = viewDir;

			float hfov = fieldOfView() / 2.0f;
			float chfov = (float) Math.cos(hfov);
			float shfov = (float) Math.sin(hfov);
			normal[4] = Vec.mult(viewDir, -shfov);
			normal[5] = Vec.add(normal[4], Vec.mult(up, -chfov));
			normal[4] = Vec.add(normal[4], Vec.mult(up, chfov));

			for (int i = 0; i < 2; ++i)
				dist[i] = Vec.dot(pos, normal[i]);
			for (int j = 4; j < 6; ++j)
				dist[j] = Vec.dot(pos, normal[j]);

			// Natural equations are:
			// dist[0,1,4,5] = pos * normal[0,1,4,5];
			// dist[2] = (pos + zNear() * viewDir) * normal[2];
			// dist[3] = (pos + zFar() * viewDir) * normal[3];

			// 2 times less computations using expanded/merged equations. Dir vectors
			// are normalized.
			float posRightCosHH = chhfov * Vec.dot(pos, right);
			dist[0] = -shhfov * posViewDir;
			dist[1] = dist[0] + posRightCosHH;
			dist[0] = dist[0] - posRightCosHH;
			float posUpCosH = chfov * Vec.dot(pos, up);
			dist[4] = -shfov * posViewDir;
			dist[5] = dist[4] - posUpCosH;
			dist[4] = dist[4] + posUpCosH;
			break;
		}
		case ORTHOGRAPHIC:
			normal[0] = Vec.mult(right, -1);
			normal[1] = right;
			normal[4] = up;
			normal[5] = Vec.mult(up, -1);

			float[] wh = getOrthoWidthHeight();
			dist[0] = Vec.dot(Vec.sub(pos, Vec.mult(right, wh[0])),
					normal[0]);
			dist[1] = Vec.dot(Vec.add(pos, Vec.mult(right, wh[0])),
					normal[1]);
			dist[4] = Vec.dot(Vec.add(pos, Vec.mult(up, wh[1])),
					normal[4]);
			dist[5] = Vec.dot(Vec.sub(pos, Vec.mult(up, wh[1])),
					normal[5]);
			break;
		}

		// Front and far planes are identical for both camera types.
		normal[2] = Vec.mult(viewDir, -1);
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
	 * @see #coneIsBackFacing(Vec[])
	 */
	public boolean coneIsBackFacing(ArrayList<Vec> normals) {
		return coneIsBackFacing(new Cone(normals));
	}
	
	/**
	 * Convenience function that simply calls
	 * {coneIsBackFacing(viewDirection, new Cone(normals))}.
	 * 
	 * @param viewDirection Cached camera view direction.
	 * @param normals cone of normals.
	 */
	public boolean coneIsBackFacing(Vec viewDirection, ArrayList<Vec> normals) {
		return coneIsBackFacing(viewDirection, new Cone(normals));
	}
	
	/**
	 * Convenience function that simply calls {@code coneIsBackFacing(new Cone(normals))}.
	 * 
	 * @see #coneIsBackFacing(Cone)
	 * @see #coneIsBackFacing(ArrayList)
	 */
	public boolean coneIsBackFacing(Vec [] normals) {
		return coneIsBackFacing(new Cone(normals));
	}
	
	/**
	 * Convenience function that simply returns
	 * {@code coneIsBackFacing(viewDirection, new Cone(normals))}.
	 * 
	 * @param viewDirection Cached camera view direction.
	 * @param normals cone of normals.
	 */
	public boolean coneIsBackFacing(Vec viewDirection, Vec [] normals) {
		return coneIsBackFacing(viewDirection, new Cone(normals));
	}
	
	/**
	 * Convenience function that simply returns {@code coneIsBackFacing(cone.axis(), cone.angle())}.
	 * 
	 * @see #coneIsBackFacing(Vec, float)
	 * @see #faceIsBackFacing(Vec, Vec, Vec)
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
	public boolean coneIsBackFacing(Vec viewDirection, Cone cone) {
		return coneIsBackFacing(viewDirection, cone.axis(), cone.angle());
	}
	
	/**
	 * Convinience funtion that simply returns
	 * {@code coneIsBackFacing(viewDirection(), axis, angle)}.
	 * <p>
	 * Non-cached version of {@link #coneIsBackFacing(Vec, Vec, float)}
	 */
	public boolean coneIsBackFacing(Vec axis, float angle) {
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
	 * @see #faceIsBackFacing(Vec, Vec, Vec)
	 */
	public boolean coneIsBackFacing(Vec viewDirection, Vec axis, float angle) {		
		if( angle < HALF_PI ) {			
			float phi = (float) Math.acos ( Vec.dot(axis, viewDirection ) );
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
  public boolean faceIsBackFacing(Vec a, Vec b, Vec c) {
  	Vec v1 = Vec.sub(projectedCoordinatesOf(a), projectedCoordinatesOf(b));
    Vec v2 = Vec.sub(projectedCoordinatesOf(b), projectedCoordinatesOf(c));
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
	 * {@link remixlab.dandelion.core.AbstractScene#avatar()} and it is an instance of
	 * InteractiveDrivableFrame it also sets {@code flySpeed} to the same value).
	 */
	@Override
	public void setSceneRadius(float radius) {
		super.setSceneRadius(radius);
		setFocusDistance(sceneRadius() / (float) Math.tan(fieldOfView() / 2.0f));
	}
		
	/**
	 * Returns the distance from the Camera center to {@link #sceneCenter()},
	 * projected along the Camera Z axis.
	 * <p>
	 * Used by {@link #zNear()} and {@link #zFar()} to optimize the Z range.
	 */
	public float distanceToSceneCenter() {
		//return Math.abs((frame().coordinatesOf(sceneCenter())).vec[2]);//before scln
		//Vector3D zCam = frame().zAxis();
		Vec zCam = frame().magnitude().z() > 0 ? frame().zAxis() : frame().zAxis(false);
		zCam.normalize();
		Vec cam2SceneCenter = Vec.sub(position(), sceneCenter());
		return Math.abs(Vec.dot(cam2SceneCenter, zCam));		
	}

	/**
	 * Returns the distance from the Camera center to {@link #arcballReferencePoint()}
	 * projected along the Camera Z axis.
	 * <p>
	 * Used by {@link #getOrthoWidthHeight(float[])} so that when the Camera is
	 * translated forward then its frustum is narrowed, making the object appear
	 * bigger on screen, as intuitively expected.
	 */
	public float distanceToARP() {
		//return Math.abs(cameraCoordinatesOf(arcballReferencePoint()).vec[2]);//before scln
		//Vector3D zCam = frame().zAxis();
		Vec zCam = frame().magnitude().z() > 0 ? frame().zAxis() : frame().zAxis(false);
		zCam.normalize();
		Vec cam2arp = Vec.sub(position(), arcballReferencePoint());
		return Math.abs(Vec.dot(cam2arp, zCam));
	}
		
	/**
	 * Similar to {@link #setSceneRadius(float)} and
	 * {@link #setSceneCenter(Vec)}, but the scene limits are defined by a
	 * (world axis aligned) bounding box.
	 */
	public void setSceneBoundingBox(Vec min, Vec max) {
		setSceneCenter(Vec.mult(Vec.add(min, max), 1 / 2.0f));
		setSceneRadius(0.5f * (Vec.sub(max, min)).mag());
	}

	// 5. ARCBALL REFERENCE POINT
	
	/**
	@Override
	public Vector3D worldCoordinatesOf(final Vector3D src) {
		return worldCoordinatesOf(src, true);
	}
	
  //TODO fix API
	public Vector3D worldCoordinatesOf(final Vector3D src, boolean flag) {
		if(flag)
			if( Util.diff(frame().magnitude().x(), 1) || Util.diff(frame().magnitude().y(), 1) || Util.diff(frame().magnitude().z(), 1))
				return frame().inverseCoordinatesOf(Vector3D.div(src, frame().magnitude()));
		return frame().inverseCoordinatesOf(src);
	}
	
	@Override
	public final Vector3D cameraCoordinatesOf(Vector3D src) {
		return cameraCoordinatesOf(src, true);
	}
	
	//TODO fix API
	public final Vector3D cameraCoordinatesOf(Vector3D src, boolean flag) {
		if(flag)
			if( Util.diff(frame().magnitude().x(), 1) || Util.diff(frame().magnitude().y(), 1) || Util.diff(frame().magnitude().z(), 1))
				return frame().coordinatesOf(Vector3D.div(src, frame().magnitude()));
		return frame().coordinatesOf(src);
	}
	*/

	/**
	 * Changes the {@link #arcballReferencePoint()} to {@code rap} (defined in the
	 * world coordinate system).
	 */
	@Override
	public void setArcballReferencePoint(Vec rap) {
		float prevDist = Math.abs(frame().coordinatesOf(arcballReferencePoint(), false).vec[2]);

		frame().setArcballReferencePoint(rap);

		// orthoCoef is used to compensate for changes of the
		// arcballReferencePoint, so that the image does
		// not change when the arcballReferencePoint is changed in ORTHOGRAPHIC
		// mode.
		float newDist = Math.abs(frame().coordinatesOf(arcballReferencePoint(), false).vec[2]);
		// Prevents division by zero when rap is set to camera position
		if ((Util.nonZero(prevDist)) && (Util.nonZero(newDist)))
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
	 * The {@link #setSceneCenter(Vec)} is set to the point located under
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
	
	@Override
	public void computeView() {
		Quat q = (Quat) frame().orientation();

		float q00 = 2.0f * q.quat[0] * q.quat[0];
		float q11 = 2.0f * q.quat[1] * q.quat[1];
		float q22 = 2.0f * q.quat[2] * q.quat[2];

		float q01 = 2.0f * q.quat[0] * q.quat[1];
		float q02 = 2.0f * q.quat[0] * q.quat[2];
		float q03 = 2.0f * q.quat[0] * q.quat[3];

		float q12 = 2.0f * q.quat[1] * q.quat[2];
		float q13 = 2.0f * q.quat[1] * q.quat[3];
		float q23 = 2.0f * q.quat[2] * q.quat[3];

		viewMat.mat[0] = 1.0f - q11 - q22;
		viewMat.mat[1] = q01 - q23;
		viewMat.mat[2] = q02 + q13;
		viewMat.mat[3] = 0.0f;

		viewMat.mat[4] = q01 + q23;
		viewMat.mat[5] = 1.0f - q22 - q00;
		viewMat.mat[6] = q12 - q03;
		viewMat.mat[7] = 0.0f;

		viewMat.mat[8] = q02 - q13;
		viewMat.mat[9] = q12 + q03;
		viewMat.mat[10] = 1.0f - q11 - q00;
		viewMat.mat[11] = 0.0f;

		Vec t = q.inverseRotate(frame().position());

		viewMat.mat[12] = -t.vec[0];
		viewMat.mat[13] = -t.vec[1];
		viewMat.mat[14] = -t.vec[2];
		viewMat.mat[15] = 1.0f;
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
	 * Use {@link #getProjection()} to retrieve this matrix.
	 * <p>
	 * <b>Note:</b> You must call this method if your Camera is not associated
	 * with a Scene and is used for offscreen computations (using {@code
	 * projectedCoordinatesOf()} for instance).
	 * 
	 * @see #setProjection(Matrix3D)
	 */
	@Override
	public void computeProjection() {
		float ZNear = zNear();
		float ZFar = zFar();
		
		switch (type()) {
		case PERSPECTIVE: {
			// #CONNECTION# all non null coefficients were set to 0.0 in
			// constructor.
			float f = 1.0f / (float) Math.tan(fieldOfView() / 2.0f);
			projectionMat.mat[0] = f / aspectRatio();
			projectionMat.mat[5] = scene.isLeftHanded() ? -f : f;
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
			projectionMat.mat[5] = ( scene.isLeftHanded() ? -1.0f : 1.0f ) / wh[1];
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

	// 9. WORLD -> CAMERA	

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
	public void convertClickToLine(final Point pixelInput, Vec orig, Vec dir) {
		Point pixel = new Point(pixelInput.getX(), pixelInput.getY());
		
		//lef-handed coordinate system correction
		if( scene.isLeftHanded() )
			pixel.y = screenHeight() - pixelInput.y;
		
		switch (type()) {
		case PERSPECTIVE:
			orig.set(position());
			dir.set(new Vec(((2.0f * pixel.x / screenWidth()) - 1.0f)	* (float) Math.tan(fieldOfView() / 2.0f) * aspectRatio(),
					                 ((2.0f * (screenHeight() - pixel.y) / screenHeight()) - 1.0f) * (float) Math.tan(fieldOfView() / 2.0f),
					                   -1.0f));
			dir.set(Vec.sub(frame().inverseCoordinatesOf(dir, false), orig));
			dir.normalize();
			break;

		case ORTHOGRAPHIC: {
			float[] wh = getOrthoWidthHeight();
			orig.set(new Vec((2.0f * pixel.x / screenWidth() - 1.0f) * wh[0],
					-(2.0f * pixel.y / screenHeight() - 1.0f) * wh[1], 0.0f));
			orig.set(frame().inverseCoordinatesOf(orig, false));
			dir.set(viewDirection());
			break;
		}
		}
	}	

	// 12. POSITION TOOLS

	/**
	 * Sets the Camera {@link #orientation()}, so that it looks at point {@code
	 * target} (defined in the world coordinate system).
	 * <p>
	 * The Camera {@link #position()} is not modified. Simply
	 * {@link #setViewDirection(Vec)}.
	 * 
	 * @see #at()
	 * @see #setUpVector(Vec)
	 * @see #setOrientation(Quat)
	 * @see #showEntireScene()
	 * @see #fitSphere(Vec, float)
	 * @see #fitBoundingBox(Vec, Vec)
	 */
	@Override
	public void lookAt(Vec target) {
		setViewDirection(Vec.sub(target, position()));
	}

	/**
	 * Returns a point defined in the world coordinate system where the camera is
	 * pointing at (just in front of {@link #viewDirection()}). Useful for setting
	 * the Processing camera() which uses a similar approach of that found in
	 * gluLookAt.
	 * 
	 * @see #lookAt(Vec)
	 */
	public Vec at() {
		return Vec.add(position(), viewDirection());
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
	 * @see #lookAt(Vec)
	 * @see #setOrientation(Quat)
	 * @see #setUpVector(Vec, boolean)
	 */
	public void fitSphere(Vec center, float radius) {
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
			distance = Vec.dot(Vec.sub(center, arcballReferencePoint()), viewDirection())	+ (radius / orthoCoef);
			break;
		}
		}

		Vec newPos = Vec.sub(center, Vec.mult(viewDirection(), distance));
		frame().setPositionWithConstraint(newPos);
	}

	/**
	 * Moves the Camera so that the (world axis aligned) bounding box ({@code min}
	 * , {@code max}) is entirely visible, using
	 * {@link #fitSphere(Vec, float)}.
	 */
	public void fitBoundingBox(Vec min, Vec max) {
		float diameter = Math.max(Math.abs(max.vec[1] - min.vec[1]), Math.abs(max.vec[0] - min.vec[0]));
		diameter = Math.max(Math.abs(max.vec[2] - min.vec[2]), diameter);
		fitSphere(Vec.mult(Vec.add(min, max), 0.5f), 0.5f * diameter);
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
	public void fitScreenRegion(Rect rectangle) {
		Vec vd = viewDirection();
		float distToPlane = distanceToSceneCenter();

		Point center = new Point((int) rectangle.getCenterX(), (int) rectangle.getCenterY());

		Vec orig = new Vec();
		Vec dir = new Vec();
		convertClickToLine(center, orig, dir);
		Vec newCenter = Vec.add(orig, Vec.mult(dir, (distToPlane / Vec.dot(dir, vd))));

		convertClickToLine(new Point(rectangle.x, center.y), orig, dir);
		final Vec pointX = Vec.add(orig, Vec.mult(dir,	(distToPlane / Vec.dot(dir, vd))));

		convertClickToLine(new Point(center.x, rectangle.y), orig, dir);
		final Vec pointY = Vec.add(orig, Vec.mult(dir,	(distToPlane / Vec.dot(dir, vd))));

		float distance = 0.0f;
		switch (type()) {
		case PERSPECTIVE: {
			final float distX = Vec.dist(pointX, newCenter)
					/ (float) Math.sin(horizontalFieldOfView() / 2.0f);
			final float distY = Vec.dist(pointY, newCenter)
					/ (float) Math.sin(fieldOfView() / 2.0f);

			distance = Math.max(distX, distY);
			break;
		}
		case ORTHOGRAPHIC: {
			final float dist = Vec.dot(Vec.sub(newCenter,
					arcballReferencePoint()), vd);
			final float distX = Vec.dist(pointX, newCenter) / orthoCoef
					/ ((aspectRatio() < 1.0) ? 1.0f : aspectRatio());
			final float distY = Vec.dist(pointY, newCenter) / orthoCoef
					/ ((aspectRatio() < 1.0) ? 1.0f / aspectRatio() : 1.0f);

			distance = dist + Math.max(distX, distY);

			break;
		}
		}

		frame().setPositionWithConstraint(Vec.sub(newCenter, Vec.mult(vd, distance)));
	}
	
	@Override
	public void showEntireScene() {
		fitSphere(sceneCenter(), sceneRadius());
	}

	/**
	 * Makes the Camera smoothly zoom on the {@link #pointUnderPixel(Point)}
	 * {@code pixel} and returns the world coordinates of the
	 * {@link #pointUnderPixel(Point)}.
	 * <p>
	 * Nothing happens if no {@link #pointUnderPixel(Point)} is found. Otherwise a
	 * KeyFrameInterpolator is created that animates the Camera on a one second
	 * path that brings the Camera closer to the point under {@code pixel}.
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

		interpolationKfi.addKeyFrame(new RefFrame(frame().orientation(),
				                                    Vec.add(Vec.mult(frame().position(),
				                         0.3f), Vec.mult(target.point, 0.7f))), 0.4f, false);

		// Small hack: attach a temporary frame to take advantage of lookAt without
		// modifying frame
		tempFrame = new InteractiveCameraFrame(this);
		InteractiveCameraFrame originalFrame = frame();
		tempFrame.setPosition(Vec.add(Vec.mult(frame().position(), coef),	Vec.mult(target.point, (1.0f - coef))));
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
			lastFrameUpdate = scene.timerHandler().frameCount();
		focusDist = distance;
	}  
}
