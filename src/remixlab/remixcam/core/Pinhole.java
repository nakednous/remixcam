package remixlab.remixcam.core;

import java.util.HashMap;
import java.util.Iterator;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

import remixlab.remixcam.geom.*;

public abstract class Pinhole {
	@Override
	public int hashCode() {	
    return new HashCodeBuilder(17, 37).
    append(fpCoefficientsUpdate).
    append(unprojectCacheOptimized).
    append(lastFrameUpdate).
    append(lastFPCoeficientsUpdateIssued).
    //append(zClippingCoef).
		//append(IODist).
		//append(dist).
		//append(fldOfView).
		//append(focusDist).
		append(fpCoefficients).
		append(frm).
		append(interpolationKfi).
		//append(knd).
		append(viewMat).
		//append(normal).
		//append(orthoCoef).
		append(orthoSize).
		//append(physicalDist2Scrn).
		//append(physicalScrnWidth).
		append(projectionMat).
		append(scnCenter).
		append(scnRadius).
		append(scrnHeight).
		append(scrnWidth).
		//append(stdZFar).
		//append(stdZNear).
		append(tempFrame).
		//append(tp).
		append(viewport).
		//append(zClippingCoef).
		//append(zNearCoef).
    toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		//TODO check me
		// /**
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		// */		
		/**
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		// */
		
		Pinhole other = (Pinhole) obj;
		
	  return new EqualsBuilder()
    .appendSuper(super.equals(obj))
    .append(fpCoefficientsUpdate, other.fpCoefficientsUpdate)
    .append(unprojectCacheOptimized, other.unprojectCacheOptimized)
    .append(lastFrameUpdate, other.lastFrameUpdate)
    .append(lastFPCoeficientsUpdateIssued, other.lastFPCoeficientsUpdateIssued)
    //.append(zClippingCoef, other.zClippingCoef)
		//.append(IODist,other.IODist)
		//.append(dist,other.dist)
		//.append(fldOfView,other.fldOfView)
		//.append(focusDist,other.focusDist)
		.append(fpCoefficients,other.fpCoefficients)
		//.append(frm,other.frm)
		.append(interpolationKfi,other.interpolationKfi)
		//.append(knd,other.knd)
		.append(viewMat,other.viewMat)
		//.append(normal,other.normal)
		//.append(orthoCoef,other.orthoCoef)
		.append(orthoSize,other.orthoSize)
		//.append(physicalDist2Scrn,other.physicalDist2Scrn)
		//.append(physicalScrnWidth,other.physicalScrnWidth)
		.append(projectionMat,other.projectionMat)
		.append(scnCenter,other.scnCenter)
		.append(scnRadius,other.scnRadius)
		.append(scrnHeight,other.scrnHeight)
		.append(scrnWidth,other.scrnWidth)
		//.append(stdZFar,other.stdZFar)
		//.append(stdZNear,other.stdZNear)
		.append(tempFrame,other.tempFrame)
		//.append(tp,other.tp)
		.append(viewport,other.viewport)
		//.append(zClippingCoef,other.zClippingCoef)
		//.append(zNearCoef,other.zNearCoef)
		.isEquals();
	}
	
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
	
  //F r a m e
	protected InteractiveCameraFrame frm;
	
  //S C E N E   O B J E C T 
	public AbstractScene scene;
	
  //C a m e r a p a r a m e t e r s
	protected int scrnWidth, scrnHeight; // size of the window, in pixels
	protected float orthoSize;
	protected Vector3D scnCenter;
	protected float scnRadius; // processing scene units	
	protected int viewport[] = new int[4];
	
	protected Matrix3D viewMat;
	protected Matrix3D projectionMat;
	protected Matrix3D projectionViewMat;
	
	protected Matrix3D projectionViewInverseMat;
	protected boolean unprojectCacheOptimized;
	protected boolean projectionViewMatHasInverse;
	
  //P o i n t s o f V i e w s a n d K e y F r a m e s
	protected HashMap<Integer, KeyFrameInterpolator> kfi;
	protected Iterator<Integer> itrtr;
	protected KeyFrameInterpolator interpolationKfi;
	protected InteractiveCameraFrame tempFrame;
	
  //F r u s t u m p l a n e c o e f f i c i e n t s
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
	
	public Pinhole(AbstractScene scn) {
		scene = scn;
		
		optimizeUnprojectCache(false);
		enableFrustumEquationsUpdate(false);	
		
		// /**
		//TODO subido
		// KeyFrames
		interpolationKfi = new KeyFrameInterpolator(scene, frame());
		kfi = new HashMap<Integer, KeyFrameInterpolator>();
		
		setFrame(new InteractiveCameraFrame(this));
		
		setSceneRadius(100);
		// */
		
		// /**
	  //TODO subido
		// Also defines the arcballReferencePoint(), which changes orthoCoef.
		setSceneCenter(new Vector3D(0.0f, 0.0f, 0.0f));
		// */
		
		// /**
		//TODO subido
		// Dummy values
		setScreenWidthAndHeight(600, 400);
		// */
		
		// /**
		//TODO subido
		viewMat = new Matrix3D();
		projectionMat = new Matrix3D();
		projectionMat.set(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		//computeProjectionMatrix();
		projectionViewMat = new Matrix3D();
		// */
	}
	
	/**
	 * Copy constructor 
	 * 
	 * @param oVP the viewport object to be copied
	 */
	protected Pinhole(Pinhole oVP) {
		this.scene = oVP.scene;
		
		this.orthoSize = oVP.orthoSize;
		this.unprojectCacheOptimized = oVP.unprojectCacheOptimized;
		this.fpCoefficientsUpdate = oVP.fpCoefficientsUpdate;
		
		if(scene.is2D()) {
			this.fpCoefficients = new float[4][3];
			for (int i=0; i<4; i++)
				for (int j=0; j<3; j++)
					this.fpCoefficients[i][j] = oVP.fpCoefficients[i][j];
		}
		else {
			this.fpCoefficients = new float[6][4];
			for (int i=0; i<6; i++)
				for (int j=0; j<4; j++)
					this.fpCoefficients[i][j] = oVP.fpCoefficients[i][j];
		}
				
		this.frm = oVP.frame().get();		
		this.interpolationKfi = oVP.interpolationKfi.get();		
		this.kfi = new HashMap<Integer, KeyFrameInterpolator>();
		
		itrtr = oVP.kfi.keySet().iterator();
		while (itrtr.hasNext()) {
			Integer key = itrtr.next();
			this.kfi.put(new Integer(key.intValue()), oVP.kfi.get(key).get());
		}
		
		this.setSceneRadius(oVP.sceneRadius());		
		this.setSceneCenter(oVP.sceneCenter());		
		this.setScreenWidthAndHeight(oVP.screenWidth(), oVP.screenHeight());		
		this.viewMat = new Matrix3D(oVP.viewMat);
		this.projectionMat = new Matrix3D(oVP.projectionMat);
		this.projectionViewMat = new Matrix3D(oVP.projectionViewMat);
	}

	/**
	@Override
	public Object get(); {
		new Pinhole(this);
	}
	// */
	
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
	
  //2. POSITION AND ORIENTATION

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
	
	public abstract void lookAt(Vector3D target);
	
	// 6. ASSOCIATED FRAME AND FRAME WRAPPER FUNCTIONS

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
			frame().setPosition(Vector3D.sub(arcballReferencePoint(), (Quaternion.multiply(frame().orientation(), q)).rotate(frame().coordinatesOf(arcballReferencePoint()))));

		frame().rotate(q);

		// Useful in fly mode to keep the horizontal direction.
		// not really needed in 2d though
		frame().updateFlyUpVector();
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
	
	public abstract void setOrientation(Quaternion q);
	
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
	
	public abstract void setSceneRadius(float radius);	

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
	public abstract boolean setSceneCenterFromPixel(Point pixel);
	
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
	 * {@link remixlab.remixcam.core.AbstractScene#pinhole()}. Default value is (0,0,0) (world
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
	public abstract void setArcballReferencePoint(Vector3D rap);
	
	public abstract boolean setArcballReferencePointFromPixel(Point pixel);
	
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
	 * Convinience function that simply calls {@code loadProjectionMatrix(true)}.
	 * 
	 * @see #loadProjectionMatrix(boolean)
	 * @see #loadProjectionMatrixStereo(boolean)
	 * @see #loadViewMatrix()
	 * @see #loadViewMatrix(boolean)
	 * @see #loadViewMatrixStereo(boolean)
	 */
	public void loadProjectionMatrix() {
		loadProjectionMatrix(true);
	} 
	
	/**
	 * // TODO complete the doc
	 * Loads the PROJECTION matrix with the Camera projection matrix.
	 * <p>
	 * The Camera projection matrix is computed using {@link #computeProjectionMatrix()}.
	 * <p>
	 * When reset {@code true} (default), the method clears the previous projection matrix
	 * by calling {@link remixlab.remixcam.core.AbstractScene#loadIdentity()} before setting
	 * the matrix. Setting reset {@code false} is useful for SELECT mode, to combine the
	 * pushed matrix with a picking matrix. See Scene.beginSelection() (pending) for details.
	 * <p>
	 * This method is used by QGLViewer::preDraw() (called before user's QGLViewer::draw()
	 * method) to set the PROJECTION matrix according to the viewer's QGLViewer::camera()
	 * settings.
	 * <p>
	 * Use {@link #getProjectionMatrix()} to retrieve this matrix. Overload this method if
	 * you want your Camera to se an exotic projection matrix.. 
	 * <p>
	 * \attention \c glMatrixMode is set to \c GL_PROJECTION.
	 * \attention If you use several OpenGL contexts and bypass the Qt main refresh loop, you should call
	 * QGLWidget::makeCurrent() before this method in order to activate the right OpenGL context.
	 *
	 * @see #loadProjectionMatrix()
	 * @see #loadProjectionMatrixStereo(boolean)
	 * @see #loadViewMatrix()
	 * @see #loadViewMatrix(boolean)
	 * @see #loadViewMatrixStereo(boolean)
	 */
	public void loadProjectionMatrix(boolean reset) {
	  if (reset)
	  	scene.resetProjection();

	  computeProjectionMatrix();
	  scene.multiplyProjection(projectionMat);
	}	
	
	/**
	 * Convenience function that simply returns {@code getProjectionMatrix(false)}
	 * 
	 * 
	 */
	public Matrix3D getProjectionMatrix() {
		return getProjectionMatrix(false);
	}
	
	public Matrix3D getProjectionMatrix(boolean recompute) {
		return getProjectionMatrix(new Matrix3D(), recompute);
	}
	
	public Matrix3D getProjectionMatrix(Matrix3D m) {
		return getProjectionMatrix(m, false);
	}

	/**
	 * Fills {@code m} with the Camera projection matrix values and returns it. If
	 * {@code m} is {@code null} a new Matrix3D will be created.
	 * <p>
	 * If {@code recompute} is {@code true} first calls {@link #computeProjectionMatrix()}
	 * to define the Camera projection matrix. Otherwise it returns the projection matrix
	 * previously computed, e.g., as with {@link #loadProjectionMatrix()}.
	 * 
	 * @see #getViewMatrix(Matrix3D, boolean)
	 */
	public Matrix3D getProjectionMatrix(Matrix3D m, boolean recompute) {
		if (m == null)
			m = new Matrix3D();

		if(recompute)
			// May not be needed, but easier and more robust like this.
			computeProjectionMatrix();
		m.set(projectionMat);

		return m;
	}	
	
	/**
	 * Fills the projection matrix with the {@code proj} matrix values.
	 * 
	 * @see #setProjectionMatrix(float[])
	 * @see #setProjectionMatrix(float[], boolean)
	 * @see #setViewMatrix(Matrix3D)
	 * @see #setViewMatrix(float[])
	 * @see #setViewMatrix(float[], boolean)
	 */
	public void setProjectionMatrix(Matrix3D proj) {
		projectionMat.set(proj);
	}
	
	/**
	 * Convenience function that simply calls {@code setProjectionMatrix(source, false)}.
	 * 
	 * @see #setProjectionMatrix(Matrix3D)
	 * @see #setProjectionMatrix(float[], boolean) 
	 * @see #setViewMatrix(Matrix3D)
	 * @see #setViewMatrix(float[])
	 * @see #setViewMatrix(float[], boolean)
	 */
	public void setProjectionMatrix(float[] source) {
		setProjectionMatrix(source, false);
	}

	/**
	 * Fills the projection matrix with the {@code source} matrix values
	 * (defined in row-major order).
	 * 
	 * @see #setProjectionMatrix(Matrix3D)
	 * @see #setProjectionMatrix(float[]) 
	 * @see #setViewMatrix(Matrix3D)
	 * @see #setViewMatrix(float[])
	 * @see #setViewMatrix(float[], boolean)
	 */
	public void setProjectionMatrix(float[] source, boolean transpose) {
		if(transpose)
			projectionMat.setTransposed(source);
		else
			projectionMat.set(source);
	}	
	
	public abstract void computeProjectionMatrix();
	
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
	public abstract float[] getOrthoWidthHeight(float[] target);

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
	
	/**
	 * Computes the View matrix associated with the Camera's
	 * {@link #position()} and {@link #orientation()}.
	 * <p>
	 * This matrix converts from the world coordinates system to the Camera
	 * coordinates system, so that coordinates can then be projected on screen
	 * using the projection matrix (see {@link #computeProjectionMatrix()}).
	 * <p>
	 * Use {@link #getViewMatrix()} to retrieve this matrix.
	 * <p>
	 * <b>Note:</b> You must call this method if your Camera is not associated
	 * with a Scene and is used for offscreen computations (using {@code
	 * projectedCoordinatesOf()} for instance).
	 */
	public void computeViewMatrix() {
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

		Vector3D t = q.inverseRotate(frame().position());

		viewMat.mat[12] = -t.vec[0];
		viewMat.mat[13] = -t.vec[1];
		viewMat.mat[14] = -t.vec[2];
		viewMat.mat[15] = 1.0f;
	}
	
	/**
	 * Convenience function that simply returns {@code getViewMatrix(false)}
	 * 
	 * @see #getViewMatrix(boolean)
	 * @see #getViewMatrix(Matrix3D)
	 * @see #getViewMatrix(Matrix3D, boolean)
	 * @see #getProjectionMatrix()
	 * @see #getProjectionMatrix(boolean)
	 * @see #getProjectionMatrix(Matrix3D)
	 * @see #getProjectionMatrix(Matrix3D, boolean) 
	 */
	public Matrix3D getViewMatrix() {
		return getViewMatrix(false);
	}
	
	/**
	 * Convenience function that simply returns {@code getViewMatrix(new Matrix3D(), recompute)}
	 * 
	 * @see #getViewMatrix()
	 * @see #getViewMatrix(Matrix3D)
	 * @see #getViewMatrix(Matrix3D, boolean)
	 * @see #getProjectionMatrix()
	 * @see #getProjectionMatrix(boolean)
	 * @see #getProjectionMatrix(Matrix3D)
	 * @see #getProjectionMatrix(Matrix3D, boolean)
	 */
	public Matrix3D getViewMatrix(boolean recompute) {
		return getViewMatrix(new Matrix3D(), recompute);
	}
	
	/**
	 * Convenience function that simply returns {@code getViewMatrix(m, false)}
	 * 
	 * @see #getViewMatrix()
	 * @see #getViewMatrix(boolean)
	 * @see #getViewMatrix(Matrix3D, boolean)
	 * @see #getProjectionMatrix()
	 * @see #getProjectionMatrix(boolean)
	 * @see #getProjectionMatrix(Matrix3D)
	 * @see #getProjectionMatrix(Matrix3D, boolean)
	 */
	public Matrix3D getViewMatrix(Matrix3D m) {
		return getViewMatrix(m, false);
	}

	/**
	 * Fills {@code m} with the Camera View matrix values and returns it. If
	 * {@code m} is {@code null} a new Matrix3D will be created.
	 * <p>
	 * If {@code recompute} is {@code true} first calls {@link #computeViewMatrix()}
	 * to define the Camera view matrix. Otherwise it returns the view matrix
	 * previously computed, e.g., as with {@link #loadViewMatrix()}.
	 * 
	 * @see #getViewMatrix()
	 * @see #getViewMatrix(boolean)
	 * @see #getViewMatrix(Matrix3D)
	 * @see #getProjectionMatrix()
	 * @see #getProjectionMatrix(boolean)
	 * @see #getProjectionMatrix(Matrix3D, boolean) 
	 */
	public Matrix3D getViewMatrix(Matrix3D m, boolean recompute) {
		if (m == null)
			m = new Matrix3D();
		if(recompute)
			// May not be needed, but easier like this.
			// Prevents from retrieving matrix in stereo mode -> overwrites shifted value.
			computeViewMatrix();
		m.set(viewMat);
		return m;
	}
	
	/**
	 * Fills the view matrix with the {@code view} matrix values.
	 * 
	 * @see #setProjectionMatrix(Matrix3D)
	 * @see #setProjectionMatrix(float[]) 
	 * @see #setViewMatrix(float[])
	 * @see #setViewMatrix(float[], boolean)
	 * @see #setViewMatrix(Matrix3D, boolean)
	 */
	public void setViewMatrix(Matrix3D view) {
			viewMat.set(view);
	}
	
	/**
	 * Convenience function that simply calls {@code setViewMatrix(source, false)}.
	 * 
	 * @see #setProjectionMatrix(Matrix3D)
	 * @see #setProjectionMatrix(float[]) 
	 * @see #setViewMatrix(float[], boolean)
	 * @see #setViewMatrix(Matrix3D, boolean)
	 * @see #setViewMatrix(Matrix3D)
	 */
	public void setViewMatrix(float [] source) {
		setViewMatrix(source, false);
	}
	
	/**
	 * Fills the view matrix with the {@code source} matrix values
	 * (defined in row-major order).
	 * 
	 * @see #setProjectionMatrix(Matrix3D)
	 * @see #setProjectionMatrix(float[]) 
	 * @see #setViewMatrix(float[])
	 * @see #setViewMatrix(Matrix3D, boolean)
	 * @see #setViewMatrix(Matrix3D)
	 */
	public void setViewMatrix(float [] source, boolean transpose) {
		if(transpose)
			viewMat.setTransposed(source);
		else
			viewMat.set(source);
	}
	
	/**
	 * Convenience funtion that simply calls {@code loadViewMatrix(true)}.
	 * 
	 * @see #loadProjectionMatrix()
	 * @see #loadProjectionMatrix(boolean)
	 * @see #loadProjectionMatrixStereo(boolean)
	 * @see #loadViewMatrix(boolean)
	 * @see #loadViewMatrixStereo(boolean)
	 */
	public void loadViewMatrix() {
		loadViewMatrix(true);
	}
	
	/*! Loads the OpenGL \c GL_MODELVIEW matrix with the modelView matrix corresponding to the Camera.

	 Calls computeModelViewMatrix() to compute the Camera's modelView matrix.

	 This method is used by QGLViewer::preDraw() (called before user's QGLViewer::draw() method) to
	 set the \c GL_MODELVIEW matrix according to the viewer's QGLViewer::camera() position() and
	 orientation().

	 As a result, the vertices used in QGLViewer::draw() can be defined in the so called world
	 coordinate system. They are multiplied by this matrix to get converted to the Camera coordinate
	 system, before getting projected using the \c GL_PROJECTION matrix (see loadProjectionMatrix()).

	 When \p reset is \c true (default), the method loads (overwrites) the \c GL_MODELVIEW matrix. Setting
	 \p reset to \c false simply calls \c glMultMatrixd (might be useful for some applications).

	 Overload this method or simply call glLoadMatrixd() at the beginning of QGLViewer::draw() if you
	 want your Camera to use an exotic modelView matrix. See also loadProjectionMatrix().

	 getModelViewMatrix() returns the 4x4 modelView matrix.

	 \attention glMatrixMode is set to \c GL_MODELVIEW

	 \attention If you use several OpenGL contexts and bypass the Qt main refresh loop, you should call
	 QGLWidget::makeCurrent() before this method in order to activate the right OpenGL context. */
	public void loadViewMatrix(boolean reset) {	  
	  computeViewMatrix();
	  if (reset)
	    scene.loadMatrix(viewMat);
	  else
	    scene.multiplyMatrix(viewMat);
	}
	
  //TODO find a better name for this:
	public void resetViewMatrix() {
		computeViewMatrix();	  	  
	  scene.resetMatrix();	  
	}
	
	public Matrix3D getProjectionViewMatrix() {
		return getProjectionViewMatrix(false);
	}
	
	public Matrix3D getProjectionViewMatrix(boolean recompute) {
		return getProjectionViewMatrix(new Matrix3D(), recompute);
	}
	
	public Matrix3D getProjectionViewMatrix(Matrix3D m) {
		return getProjectionViewMatrix(m, false);
	}
	
	public Matrix3D getProjectionViewMatrix(Matrix3D m, boolean recompute) {
		if (m == null)
			m = new Matrix3D();
		if(recompute) {
			// May not be needed, but easier like this.
			// Prevents from retrieving matrix in stereo mode -> overwrites shifted value.
			computeProjectionMatrix();
			computeViewMatrix();
		}
		m.set(projectionViewMat);
		return m;
	}
	
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
		Matrix3D.mult(projectionMat, viewMat, projectionViewMat);
		
		// 2. unproject
		if(unprojectCacheIsOptimized()) {
			if(projectionViewInverseMat == null)
				projectionViewInverseMat = new Matrix3D();
			projectionViewMatHasInverse = projectionViewMat.invert(projectionViewInverseMat);
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
	 * (see {@link #getViewMatrix()}, {@link #getProjectionMatrix()} and
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
			project(tmp.vec[0], tmp.vec[1], tmp.vec[2], viewMat, projectionMat, viewport, xyz);
		} else
			project(src.vec[0], src.vec[1], src.vec[2], viewMat, projectionMat, viewport, xyz);

		/**
		// TODO needs further testing
  	//left-handed coordinate system correction
		if( scene.isLeftHanded() )
			xyz[1] = screenHeight() - xyz[1];
		*/

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
	 * Similar to {@code gluProject}: map object coordinates to window
	 * coordinates.
	 * 
	 * @param objx
	 *          Specify the object x coordinate.
	 * @param objy
	 *          Specify the object y coordinate.
	 * @param objz
	 *          Specify the object z coordinate.
	 * @param view
	 *          Specifies the current view matrix.
	 * @param projection
	 *          Specifies the current projection matrix.
	 * @param viewport
	 *          Specifies the current viewport.
	 * @param windowCoordinate
	 *          Return the computed window coordinates.
	 */
	public boolean project(float objx, float objy, float objz, Matrix3D view,
			                   Matrix3D projection, int[] viewport, float[] windowCoordinate) {
		float in[] = new float[4];
		float out[] = new float[4];
		
		in[0] = objx;
		in[1] = objy;
		in[2] = objz;
		in[3] = 1.0f;
					
		out[0]=projectionViewMat.mat[0]*in[0] + projectionViewMat.mat[4]*in[1] + projectionViewMat.mat[8]*in[2] + projectionViewMat.mat[12]*in[3];
		out[1]=projectionViewMat.mat[1]*in[0] + projectionViewMat.mat[5]*in[1] + projectionViewMat.mat[9]*in[2] + projectionViewMat.mat[13]*in[3];
		out[2]=projectionViewMat.mat[2]*in[0] + projectionViewMat.mat[6]*in[1] + projectionViewMat.mat[10]*in[2] + projectionViewMat.mat[14]*in[3];
		out[3]=projectionViewMat.mat[3]*in[0] + projectionViewMat.mat[7]*in[1] + projectionViewMat.mat[11]*in[2] + projectionViewMat.mat[15]*in[3];
			
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
		/**
		  // compute without projectionViewMat
			view.mult(in, out);
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
		// */
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
	
  //7. KEYFRAMED PATHS

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
	
	public abstract float[][] computeFrustumEquations();
	
	public abstract float[][] computeFrustumEquations(float[][] coef);
	
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
		Vector3D myVec = new Vector3D(fpCoefficients[index][0],	fpCoefficients[index][1], fpCoefficients[index][2]);
		return Vector3D.dot(pos, myVec) - fpCoefficients[index][3];
	}
	
	public abstract boolean pointIsVisible(Vector3D point);	
	
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
	
	public abstract void fitScreenRegion(Rectangle rectangle);
	
	/**
	 * Moves the Camera so that the entire scene is visible.
	 * <p>
	 * Simply calls {@link #fitSphere(Vector3D, float)} on a sphere defined by
	 * {@link #sceneCenter()} and {@link #sceneRadius()}.
	 * <p>
	 * You will typically use this method at init time after you defined a new
	 * {@link #sceneRadius()}.
	 */
	public abstract void showEntireScene();
	
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
	
	public abstract Vector3D viewDirection();
	
  //TODO experimental	
	
	public Matrix3D projection() {
		return projectionMat;
	}
	
	public Matrix3D view() {
		return viewMat;
	}
}