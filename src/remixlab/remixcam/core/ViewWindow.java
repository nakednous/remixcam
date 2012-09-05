package remixlab.remixcam.core;

import remixlab.remixcam.core.Camera.WorldPoint;
import remixlab.remixcam.geom.Point;
import remixlab.remixcam.geom.Quaternion;
import remixlab.remixcam.geom.Rectangle;
import remixlab.remixcam.geom.Vector3D;

public class ViewWindow extends Pinhole implements Constants, Copyable {
	public ViewWindow(AbstractScene scn) {
		super(scn);
		if(scene.is3D())
			throw new RuntimeException("Use ViewWindow only for a 2D Scene");
		fpCoefficients = new float[4][3];		
		computeProjectionMatrix();
		turnUpsideDown();
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
	public void computeProjectionMatrix() {
		float[] wh = getOrthoWidthHeight();
		projectionMat.mat[0] = 1.0f / wh[0];
		if( scene.isAP5Scene() )
			projectionMat.mat[5] = -1.0f / wh[1];
		else
			projectionMat.mat[5] = 1.0f / wh[1];
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
		float dist = sceneRadius() * standardOrthoFrustumSize();
		// #CONNECTION# fitScreenRegion
		// 1. halfWidth
		target[0] = dist * ((aspectRatio() < 1.0f) ? 1.0f : aspectRatio());
		// 2. halfHeight
		target[1] = dist * ((aspectRatio() < 1.0f) ? 1.0f / aspectRatio() : 1.0f);		

		return target;
	}
	
	@Override
	public void changeStandardOrthoFrustumSize(boolean augment) {
		lastFrameUpdate = scene.frameCount();
		if (augment)
			orthoSize *= 1.01f;
		else
			orthoSize /= 1.01f;
	}
	
	@Override
	public void setSceneRadius(float radius) {
		if (radius <= 0.0f) {
			System.out.println("Warning: Scene radius must be positive - Ignoring value");
			return;
		}
		
		scnRadius = radius;
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
		setSceneCenter(new Vector3D(pixel.x, pixel.y, 0));
		return true;		
	}
	
	public Visibility rectIsVisible(Vector3D p1, Vector3D p2) {
		//TODO implement me	
		return Visibility.SEMIVISIBLE;
	}

	@Override
	public void interpolateToZoomOnRegion(Rectangle rectangle) {
		// TODO Auto-generated method stub
		
	}	
	
	@Override
	public Vector3D cameraCoordinatesOf(Vector3D src) {
		//TODO needs test
		Vector3D res = frame().coordinatesOf(src);
		res.z(0);
		return res;
	}

	@Override
	public Vector3D worldCoordinatesOf(final Vector3D src) {
		//TODO needs test
		Vector3D res = frame().inverseCoordinatesOf(src);
		res.z(0);
		return res;
	}	
	
	public void fitBoundingRect(Vector3D min, Vector3D max) {
		float diameter = Math.max(Math.abs(max.vec[1] - min.vec[1]), Math.abs(max.vec[0] - min.vec[0]));
		diameter = Math.max(Math.abs(max.vec[2] - min.vec[2]), diameter);
		fitCircle(Vector3D.mult(Vector3D.add(min, max), 0.5f), 0.5f * diameter);
	}
	
	public void fitCircle(Vector3D center, float radius) {
		
	}
	
	@Override
	public void showEntireScene() {
		fitCircle(sceneCenter(), sceneRadius());
	}
	
	/**
	 * Similar to {@link #setSceneRadius(float)} and
	 * {@link #setSceneCenter(Vector3D)}, but the scene limits are defined by a
	 * (world axis aligned) bounding box.
	 */
	public void setSceneBoundingRect(Vector3D min, Vector3D max) {
		Vector3D mn = new Vector3D(min.x(), min.y(), 0);
		Vector3D mx = new Vector3D(max.x(), max.y(), 0);
		setSceneCenter(Vector3D.mult(Vector3D.add(mn, mx), 1 / 2.0f));
		setSceneRadius(0.5f * (Vector3D.sub(mx, mn)).mag());
	}
	
	@Override
	public void fitScreenRegion(Rectangle rectangle) { 
		
	}
	
	@Override
	public void centerScene() {
		//frame().projectOnLine(sceneCenter(), viewDirection());
	}

	@Override
	public void setUpVector(Vector3D up, boolean noMove) {
		Quaternion q = new Quaternion(new Vector3D(0.0f, 1.0f, 0.0f), frame().transformOf(up));

		if (!noMove)
			frame().setPosition(Vector3D.sub(arcballReferencePoint(), (Quaternion.multiply(frame().orientation(), q)).rotate(frame().coordinatesOf(arcballReferencePoint()))));

		frame().rotate(q);
	}

	@Override
	public void setOrientation(Quaternion q) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean pointIsVisible(Vector3D point) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public WorldPoint interpolateToZoomOnPixel(Point pixel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void lookAt(Vector3D target) {
		//frame().setPosition(target.x(), target.y(), FAKED_ZDISTANCE);
	}
	
	@Override
	public void setArcballReferencePoint(Vector3D rap) {
		Vector3D vec = new Vector3D(rap.x(), rap.y(), 0);
		frame().setArcballReferencePoint(vec);
	}

	@Override
	public boolean setArcballReferencePointFromPixel(Point pixel) {
		//TODO implement me
		return true;
	}

	@Override
	public void interpolateToFitScene() {
		// TODO Auto-generated method stub
		
	}
	
	public void turnUpsideDown() {		
		//setPosition(new Vector3D(0,0,-10));
		
		Vector3D direction = new Vector3D(0,0,1);
		Vector3D xAxis = direction.cross(upVector());
		if (xAxis.squaredNorm() < 1E-10) {
			// target is aligned with upVector, this means a rotation around X
			// axis
			// X axis is then unchanged, let's keep it !
			xAxis = frame().inverseTransformOf(new Vector3D(1.0f, 0.0f, 0.0f));
		}

		Quaternion q = new Quaternion();
		q.fromRotatedBasis(xAxis, xAxis.cross(direction), Vector3D.mult(direction, -1));
		frame().setOrientationWithConstraint(q);
		
		setUpVector(new Vector3D(0,-1,0));
	}
}
