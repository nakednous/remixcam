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
		flip();
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
		/**
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
		// */
	}	
	
	@Override
	public Vector3D viewDirection() {
		return new Vector3D(0, 0, ( frame().zAxis().z() > 0 ) ? -1 : 1 );
	}

	@Override
	public void setOrientation(Quaternion q) {
		Vector3D axis = q.axis();
		float angle = q.angle();
		axis.x(0);
		axis.y(0);
		if( axis.z() > 0 )
			axis.z(1);
		else
			axis.z(-1);
		
		Quaternion quat = new Quaternion(axis, angle);
		frame().setOrientation(quat);
		frame().updateFlyUpVector();
	}
	
	public void setOrientation(float angle) {
		if( viewDirection().z() > 0 )
			setOrientation(new Quaternion(new Vector3D(0,0,1), angle));
		else
			setOrientation(new Quaternion(new Vector3D(0,0,-1), angle));
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
	
	public void flip() {		
		Vector3D direction = new Vector3D(0, 0, ( frame().zAxis().z() > 0 ) ? 1 : -1 );
		
		Vector3D xAxis = direction.cross(upVector());

		Quaternion q = new Quaternion();
		q.fromRotatedBasis(xAxis, xAxis.cross(direction), Vector3D.mult(direction, -1));
		frame().setOrientationWithConstraint(q);
		
		Vector3D up = upVector();
		up.y(-up.y());
		setUpVector(up);
	}
}
