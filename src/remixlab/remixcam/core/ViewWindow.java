package remixlab.remixcam.core;

import remixlab.remixcam.geom.*;

public class ViewWindow extends Pinhole implements Copyable {
	
	static final float FAKED_ZNEAR = -10;  
  static final float FAKED_ZFAR = 10;	
	
	public ViewWindow(AbstractScene scn) {
		super(scn);
		if(scene.is3D())
			throw new RuntimeException("Use ViewWindow only for a 2D Scene");
		fpCoefficients = new float[4][3];		
		computeProjectionMatrix();
		//flip();
	}
	
	protected ViewWindow(ViewWindow oVW) {
		super(oVW);		
	}
	
	/**
	 * Calls {@link #Camera(Camera)} (which is protected) and returns a copy of
	 * {@code this} object.
	 * 
	 * @see #Camera(Camera)
	 */	
	@Override
	public ViewWindow get() {
		return new ViewWindow(this);
	}
	
	@Override
	public void computeViewMatrix() {		
		Rotation q = (Rotation)frame().orientation();
		
		float cosB = (float)Math.cos((double)q.angle());
		float sinB = (float)Math.sin((double)q.angle());
		
		viewMat.mat[0] = cosB;
		viewMat.mat[1] = -sinB;
		viewMat.mat[2] = 0.0f;
		viewMat.mat[3] = 0.0f;

		viewMat.mat[4] = sinB;
		viewMat.mat[5] = cosB;
		viewMat.mat[6] = 0.0f;
		viewMat.mat[7] = 0.0f;

		viewMat.mat[8] = 0.0f;
		viewMat.mat[9] = 0.0f;
		viewMat.mat[10] = 1.0f;
		viewMat.mat[11] = 0.0f;

		DLVector t = q.inverseRotate(frame().position());

		viewMat.mat[12] = -t.vec[0];
		viewMat.mat[13] = -t.vec[1];
		viewMat.mat[14] = -t.vec[2];
		viewMat.mat[15] = 1.0f;
	}
	
	@Override
	public void computeProjectionMatrix() {
		float[] wh = getOrthoWidthHeight();
		projectionMat.mat[0] = 1.0f / wh[0];
		projectionMat.mat[5] = (scene.isLeftHanded() ? -1.0f : 1.0f ) / wh[1];
		projectionMat.mat[10] = -2.0f / (FAKED_ZFAR - FAKED_ZNEAR);
		projectionMat.mat[11] = 0.0f;
		projectionMat.mat[14] = -(FAKED_ZFAR + FAKED_ZNEAR) / (FAKED_ZFAR - FAKED_ZNEAR);
		projectionMat.mat[15] = 1.0f;
		// same as glOrtho( -w, w, -h, h, zNear(), zFar() );
	}	
	
	@Override
	public float[] getOrthoWidthHeight(float[] target) {
		if ((target == null) || (target.length != 2)) {
			target = new float[2];
		}
		
		DLVector vec = frame().magnitude();
		target[0] = ( vec.x() * this.screenWidth() )  / 2;
		target[1] = ( vec.y() * this.screenHeight() ) / 2;		

		return target;
	}
	
	public void setScaling(float s) {
		frame().setScaling(s);
	}
	
	public void setScaling(float sx, float sy) {
		frame().setScaling(sx, sy);
	}
	
	public void setScaling(DLVector s) {
		frame().setScaling(s);
	}
	
	/**
	public float scaleFactor() {
		return frame().scaling().x();
		//return frame().scaling().y();
	}
	// */
	
	/**
	public float size() {
		return orthoSize;
	}
	
	public void setSize(float s) {
		orthoSize = s;
	}
	
	public void changeSize(boolean augment) {
		lastFrameUpdate = scene.frameCount();
		if (augment)
			orthoSize *= 1.01f;
		else
			orthoSize /= 1.01f;
	}		
	*/
	
	@Override
	public DLVector rightVector() {
		return frame().xAxis();
	}
	
	@Override
	public DLVector upVector() {
		return frame().yAxis();
	}
	
	@Override
	public void setUpVector(DLVector up, boolean noMove) {
		Quaternion q = new Quaternion(new DLVector(0.0f, 1.0f, 0.0f), frame().transformOf(up));

		if (!noMove) 		
			frame().setPosition(DLVector.sub(arcballReferencePoint(), (Rotation.compose((Rotation) frame().orientation(), q)).rotate(frame().coordinatesOf(arcballReferencePoint()))));		

		frame().rotate(q);
	}
	
	@Override
	public float[][] computeFrustumEquations() {
		return computeFrustumEquations(new float[4][3]);
	}

	@Override
	public float[][] computeFrustumEquations(float[][] coef) {
		// TODO Auto-generated method stub
		return null;
	}	
	
	@Override
	public boolean setSceneCenterFromPixel(Point pixel) {
		setSceneCenter(new DLVector(pixel.x, pixel.y, 0));
		return true;		
	}
	
	public Visibility rectIsVisible(DLVector p1, DLVector p2) {
		//TODO implement me	
		return Visibility.SEMIVISIBLE;
	}
	
	public void fitBoundingRect(DLVector min, DLVector max) {
		float diameter = Math.max(Math.abs(max.vec[1] - min.vec[1]), Math.abs(max.vec[0] - min.vec[0]));
		diameter = Math.max(Math.abs(max.vec[2] - min.vec[2]), diameter);
		fitCircle(DLVector.mult(DLVector.add(min, max), 0.5f), 0.5f * diameter);
	}
	
	public void fitCircle(DLVector center, float radius) {
	  DLVector scl = frame().scaling();
		setScaling(scl.x() > 0 ? radius / sceneRadius() : -radius / sceneRadius(),
				       scl.y() > 0 ? radius / sceneRadius() : -radius / sceneRadius());				
		lookAt(center);
	}
	
	@Override
	public void showEntireScene() {
		fitCircle(sceneCenter(), sceneRadius());		
	}
	
	/**
	 * Similar to {@link #setSceneRadius(float)} and
	 * {@link #setSceneCenter(DLVector)}, but the scene limits are defined by a
	 * (world axis aligned) bounding box.
	 */
	public void setSceneBoundingRect(DLVector min, DLVector max) {
		DLVector mn = new DLVector(min.x(), min.y(), 0);
		DLVector mx = new DLVector(max.x(), max.y(), 0);
		setSceneCenter(DLVector.mult(DLVector.add(mn, mx), 1 / 2.0f));
		setSceneRadius(0.5f * (DLVector.sub(mx, mn)).mag());
	}
	
	@Override
	public void fitScreenRegion(Rectangle rectangle) {
		float rectRatio = (float)rectangle.width / (float)rectangle.height;
		//TODO needs testing
		
		float sclX = frame().scaling().x();
		float sclY = frame().scaling().y();		
		
		if(aspectRatio() < 1.0f) {
			if( aspectRatio () < rectRatio )
				setScaling(sclX * (float)rectangle.width / screenWidth(), sclY * (float)rectangle.width / screenWidth());
			else
				setScaling(sclX * (float)rectangle.height / screenHeight(), sclY * (float)rectangle.height / screenHeight());
		} else {
			if( aspectRatio () < rectRatio )
				setScaling(sclX * (float)rectangle.width / screenWidth(), sclY * (float)rectangle.width / screenWidth());
			else
				setScaling(sclX * (float)rectangle.height / screenHeight(), sclY * (float)rectangle.height / screenHeight());
		}
		
		/**
		if(aspectRatio() < 1.0f) {
			if( aspectRatio () < rectRatio )
				setScaling(scaleFactor() * (float)rectangle.width / screenWidth());
			else
				setScaling(scaleFactor() * (float)rectangle.height / screenHeight());
		} else {
			if( aspectRatio () < rectRatio )
				setScaling(scaleFactor() * (float)rectangle.width / screenWidth());
			else
				setScaling(scaleFactor() * (float)rectangle.height / screenHeight());
		}
		*/
	
		/**
		if(aspectRatio() < 1.0f) {
			if( aspectRatio () < rectRatio )
				setSize(size() * (float)rectangle.width / screenWidth());
			else
				setSize(size() * (float)rectangle.height / screenHeight());
		} else {
			if( aspectRatio () < rectRatio )
				setSize(size() * (float)rectangle.width / screenWidth());
			else
				setSize(size() * (float)rectangle.height / screenHeight());
		}
		// */
		
		lookAt(unprojectedCoordinatesOf(new DLVector(rectangle.getCenterX(), rectangle.getCenterY(), 0)));
	}	
	
	@Override
	public DLVector viewDirection() {
		return new DLVector( 0, 0, ( frame().zAxis().z() > 0 ) ? -1 : 1 );
	}

	@Override
	public void setOrientation(Quaternion q) {
		setOrientation(q.angle());
	}
	
	public void setOrientation(float angle) {
		Quaternion quat = new Quaternion(viewDirection(), angle);
		frame().setOrientation(quat);
		frame().updateFlyUpVector();
	}
	
	@Override
	public float pixelP5Ratio(DLVector position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean pointIsVisible(DLVector point) {
		// TODO Auto-generated method stub
		return false;
	}	

	@Override
	public void lookAt(DLVector target) {
		frame().setPosition(target.x(), target.y(), 0);
	}
	
	@Override
	public void setArcballReferencePoint(DLVector rap) {
		DLVector vec = new DLVector(rap.x(), rap.y(), 0);
		frame().setArcballReferencePoint(vec);
	}

	@Override
	public boolean setArcballReferencePointFromPixel(Point pixel) {		
		setArcballReferencePoint(unprojectedCoordinatesOf(new DLVector((float) pixel.x, (float) pixel.y, 0.5f)));
		return true;
	}	
	
	public void flip() {
		if( scene.isLeftHanded() )
			scene.setRightHanded();
		else
			scene.setLeftHanded();
	}
	
	public void interpolateToZoomOnPixel(Point pixel) {
		float winW = this.screenWidth()/3;
		float winH = this.screenHeight()/3;
		float cX = (float)pixel.x - winW/2;
		float cY = (float)pixel.y - winH/2;
		Rectangle rect = new Rectangle((int)cX, (int)cY, (int)winW, (int)winH);
		this.interpolateToZoomOnRegion(rect);		
	}	
}
