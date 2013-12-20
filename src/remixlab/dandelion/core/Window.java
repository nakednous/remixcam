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

public class Window extends ViewPoint implements Copyable {
	//TODO things broken when farme().scaling() has negative values:
	//setUpVector and frustumEquations
	
  //next variables are needed for frustum plane coefficients
	Vec normal[] = new Vec[4];
	float dist[] = new float[4];
	
	static final float FAKED_ZNEAR = -10;  
  static final float FAKED_ZFAR = 10;	
	
	public Window(AbstractScene scn) {
		super(scn);
		if(scene.is3D())
			throw new RuntimeException("Use ViewWindow only for a 2D Scene");
		fpCoefficients = new float[4][3];		
		computeProjection();
		//flip();
	}
	
	protected Window(Window oVW) {
		super(oVW);		
	}
	
	/**
	 * Calls {@link #Window(Window)} (which is protected) and returns a copy of
	 * {@code this} object.
	 * 
	 * @see #Window(Window)
	 */	
	@Override
	public Window get() {
		return new Window(this);
	}
	
	@Override
	public void computeView() {		
		Rot q = (Rot)frame().orientation();
		
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

		Vec t = q.inverseRotate(frame().position());

		viewMat.mat[12] = -t.vec[0];
		viewMat.mat[13] = -t.vec[1];
		viewMat.mat[14] = -t.vec[2];
		viewMat.mat[15] = 1.0f;
	}
	
	@Override
	public void computeProjection() {
		float[] wh = getOrthoWidthHeight();
		projectionMat.mat[0] = 1.0f / wh[0];
		projectionMat.mat[5] = (scene.isLeftHanded() ? -1.0f : 1.0f ) / wh[1];
		projectionMat.mat[10] = -2.0f / (FAKED_ZFAR - FAKED_ZNEAR);
		projectionMat.mat[11] = 0.0f;
		projectionMat.mat[14] = -(FAKED_ZFAR + FAKED_ZNEAR) / (FAKED_ZFAR - FAKED_ZNEAR);
		projectionMat.mat[15] = 1.0f;
		// same as glOrtho( -w, w, -h, h, zNear(), zFar() );
	}
	
  //TODO test
	@Override
  public void fromView(Mat mv, boolean recompute) {
		Rot q = new Rot();
		q.fromMatrix(mv);
 	  setOrientation(q); 	  
 	  setPosition(Vec.mult(q.rotate(new Vec(mv.mat[12], mv.mat[13], mv.mat[14])), -1) );
 	  if(recompute)
 	  	this.computeView();
  }
	
	/*
  //TODO implement me
	@Override
	public void fromProjection(Mat proj, boolean recompute) {
		if(recompute)
 	  	this.computeProjection();
	}
	//*/
	
	@Override
	public float[] getOrthoWidthHeight(float[] target) {
		if ((target == null) || (target.length != 2)) {
			target = new float[2];
		}
		
		Vec vec = frame().magnitude();
		target[0] = ( vec.x() * this.screenWidth() )  / 2;
		target[1] = ( vec.y() * this.screenHeight() ) / 2;		

		return target;
	}
	
	///*
	@Override
	public Vec upVector() {
		return frame().yAxis();
	}
	//*/
	
  //TODO testing it as it's done in Camera
	/*
	@Override
	public Vec upVector() {
		return frame().magnitude().y() > 0 ? frame().yAxis() : frame().yAxis(false);
	}
	// */
	
	///*
	@Override
	public Vec rightVector() {
		return frame().xAxis();
	}
	//*/
	
	//TODO testing it as it's done in Camera
	/*
	@Override
	public Vec rightVector() {
		return frame().magnitude().x() > 0 ? frame().xAxis() : frame().xAxis(false);
	}
	//*/
	
	///*
	@Override
	public void setUpVector(Vec up, boolean noMove) {
		Rot r = new Rot(new Vec(0.0f, 1.0f), frame().transformOf(up));

		if (!noMove) 		
			frame().setPosition(Vec.sub(arcballReferencePoint(), (Rot.compose((Rot) frame().orientation(), r)).rotate(frame().coordinatesOf(arcballReferencePoint()))));		

		frame().rotate(r);
	}
	//*/
	
  //TODO testing it as it's done in Camera
	/*
	@Override
	public void setUpVector(Vec up, boolean noMove) {
		Rot r = new Rot(new Vec(0.0f, frame().magnitude().y() > 0 ? 1.0f : -1.0f), frame().transformOf(up));

		if (!noMove) 		
			frame().setPosition(Vec.sub(arcballReferencePoint(), (Rot.compose((Rot) frame().orientation(), r)).rotate(frame().coordinatesOf(arcballReferencePoint()))));		

		frame().rotate(r);
	}
	//*/
	
	@Override
	public boolean setSceneCenterFromPixel(Point pixel) {
		setSceneCenter(new Vec(pixel.x, pixel.y, 0));
		return true;		
	}
	
	public void fitBoundingRect(Vec min, Vec max) {
		float diameter = Math.max(Math.abs(max.vec[1] - min.vec[1]), Math.abs(max.vec[0] - min.vec[0]));
		diameter = Math.max(Math.abs(max.vec[2] - min.vec[2]), diameter);
		fitCircle(Vec.mult(Vec.add(min, max), 0.5f), 0.5f * diameter);
	}
	
	@Override
	public void showEntireScene() {
		fitCircle(sceneCenter(), sceneRadius());		
	}
	
	public void fitCircle(Vec center, float radius) {
	  Vec mag = frame().magnitude();
	  
	  float size = Math.min(scene.width(), scene.height());
  	frame().setMagnitude(mag.x() > 0 ? 2*radius / size : -2*radius / size,
                         mag.y() > 0 ? 2*radius / size : -2*radius / size);
  	
		lookAt(center);
	}
	
	/**
	 * Similar to {@link #setSceneRadius(float)} and
	 * {@link #setSceneCenter(Vec)}, but the scene limits are defined by a
	 * (world axis aligned) bounding box.
	 */
	public void setSceneBoundingRect(Vec min, Vec max) {
		Vec mn = new Vec(min.x(), min.y(), 0);
		Vec mx = new Vec(max.x(), max.y(), 0);
		setSceneCenter(Vec.mult(Vec.add(mn, mx), 1 / 2.0f));
		setSceneRadius(0.5f * (Vec.sub(mx, mn)).mag());
	}
	
	@Override
	public void fitScreenRegion(Rect rectangle) {
		float rectRatio = (float)rectangle.width / (float)rectangle.height;
		
		float sclX = frame().magnitude().x();
		float sclY = frame().magnitude().y();		
		
		if(aspectRatio() < 1.0f) {
			if( aspectRatio () < rectRatio )
				frame().setMagnitude(sclX * (float)rectangle.width / screenWidth(), sclY * (float)rectangle.width / screenWidth());
			else
				frame().setMagnitude(sclX * (float)rectangle.height / screenHeight(), sclY * (float)rectangle.height / screenHeight());
		} else {
			if( aspectRatio () < rectRatio )
				frame().setMagnitude(sclX * (float)rectangle.width / screenWidth(), sclY * (float)rectangle.width / screenWidth());
			else
				frame().setMagnitude(sclX * (float)rectangle.height / screenHeight(), sclY * (float)rectangle.height / screenHeight());
		}		
		lookAt(unprojectedCoordinatesOf(new Vec(rectangle.getCenterX(), rectangle.getCenterY(), 0)));
	}	
	
	@Override
	public Vec viewDirection() {
		return new Vec( 0, 0, ( frame().zAxis().z() > 0 ) ? -1 : 1 );
	}

	@Override
	public void setOrientation(Orientable q) {
		setOrientation(q.angle());
	}
	
	public void setOrientation(float angle) {
		Orientable r = new Rot(angle);
		frame().setOrientation(r);
		frame().updateFlyUpVector();
	}
	
	@Override
	public float[][] computeFrustumEquations() {
		return computeFrustumEquations(new float[4][3]);
	}

	@Override
	public float[][] computeFrustumEquations(float[][] coef) {
		if (coef == null || (coef.length == 0))
			coef = new float[4][3];
		else if ((coef.length != 4) || (coef[0].length != 3))
			coef = new float[4][3];

		// Computed once and for all
		Vec pos = position();
		//Vec viewDir = viewDirection();
		Vec up = upVector();
		Vec right = rightVector();
		
		normal[0] = Vec.mult(right, -1);
		normal[1] = right;
		normal[2] = up;
		normal[3] = Vec.mult(up, -1);

		float[] wh = getOrthoWidthHeight();
		
		dist[0] = Vec.dot(Vec.sub(pos, Vec.mult(right, wh[0])),	normal[0]);
		dist[1] = Vec.dot(Vec.add(pos, Vec.mult(right, wh[0])),	normal[1]);
		dist[2] = Vec.dot(Vec.add(pos, Vec.mult(up, wh[1])), normal[2]);
		dist[3] = Vec.dot(Vec.sub(pos, Vec.mult(up, wh[1])), normal[3]);
		
		for (int i = 0; i < 4; ++i) {
			coef[i][0] = normal[i].vec[0];
			coef[i][1] = normal[i].vec[1];
			//Change respect to Camera occurs here:
			coef[i][2] = -dist[i];
		}
		
		return coef;
	}
	
	/**
	 * Returns the signed distance between point {@code pos} and plane {@code
	 * index}. The distance is negative if the point lies in the planes's frustum
	 * halfspace, and positive otherwise.
	 * <p>
	 * {@code index} is a value between {@code 0} and {@code 3} which respectively
	 * correspond to the left, right, top and bottom Camera frustum
	 * planes.
	 * <p>
	 * <b>Attention:</b> The camera frustum plane equations should be updated
	 * before calling this method. You may compute them explicitly (by calling
	 * {@link #computeFrustumEquations()} ) or enable them to be automatic updated
	 * in your Scene setup (with
	 * {@link remixlab.dandelion.core.AbstractScene#enableFrustumEquationsUpdate()}).
	 * 
	 * @see #pointIsVisible(Vec)
	 * @see #sphereIsVisible(Vec, float)
	 * @see #aaBoxIsVisible(Vec, Vec)
	 * @see #computeFrustumEquations()
	 * @see #updateFrustumEquations()
	 * @see #getFrustumEquations()
	 * @see remixlab.dandelion.core.AbstractScene#enableFrustumEquationsUpdate()
	 */
	public float distanceToFrustumPlane(int index, Vec pos) {
		if (!scene.frustumEquationsUpdateIsEnable())
			System.out.println("The camera frustum plane equations (needed by distanceToFrustumPlane) may be outdated. Please "
							+ "enable automatic updates of the equations in your PApplet.setup "
							+ "with Scene.enableFrustumEquationsUpdate()");
		//check this: http://en.wikipedia.org/wiki/Distance_from_a_point_to_a_line
		return (fpCoefficients[index][0] * pos.x() + fpCoefficients[index][1] * pos.y() + fpCoefficients[index][2]) /
				   (float)Math.sqrt(fpCoefficients[index][0]*fpCoefficients[index][0] + fpCoefficients[index][1]*fpCoefficients[index][1]);
	}
	
	public Visibility rectIsVisible(Vec p1, Vec p2) {
		if (!scene.frustumEquationsUpdateIsEnable())
			System.out.println("The camera frustum plane equations (needed by aaBoxIsVisible) may be outdated. Please "
							+ "enable automatic updates of the equations in your PApplet.setup "
							+ "with Scene.enableFrustumEquationsUpdate()");
		boolean allInForAllPlanes = true;
		
		for (int i = 0; i < 4; ++i) {
			boolean allOut = true;
			for (int c = 0; c < 4; ++c) {
				Vec pos = new Vec(((c & 2) != 0) ? p1.vec[0] : p2.vec[0],
						              ((c & 1) != 0) ? p1.vec[1] : p2.vec[1]);
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
	
	public Visibility circleIsVisible(Vec center, float radius) {
		if (!scene.frustumEquationsUpdateIsEnable())
			System.out.println("The camera frustum plane equations (needed by sphereIsVisible) may be outdated. Please "
							+ "enable automatic updates of the equations in your PApplet.setup "
							+ "with Scene.enableFrustumEquationsUpdate()");
		boolean allInForAllPlanes = true;
		for (int i = 0; i < 4; ++i) {
			float d = distanceToFrustumPlane(i, center);
			if (d > radius)
				return ViewPoint.Visibility.INVISIBLE;
			if ((d > 0) || (-d < radius))
				allInForAllPlanes = false;
		}
		if(allInForAllPlanes)
			return ViewPoint.Visibility.VISIBLE;
		return ViewPoint.Visibility.SEMIVISIBLE;
	}

	@Override
	public boolean pointIsVisible(Vec point) {
		if (!scene.frustumEquationsUpdateIsEnable())
			System.out.println("The camera frustum plane equations (needed by pointIsVisible) may be outdated. Please "
							+ "enable automatic updates of the equations in your PApplet.setup "
							+ "with Scene.enableFrustumEquationsUpdate()");
		for (int i = 0; i < 4; ++i)
			if (distanceToFrustumPlane(i, point) > 0)
				return false;
		return true;
	}
	
	@Override
	public float pixelSceneRatio(Vec position) {
		float[] wh = getOrthoWidthHeight();
		return 2.0f * wh[1] / screenHeight();
	}

	@Override
	public void lookAt(Vec target) {
		frame().setPosition(target.x(), target.y(), 0);
	}
	
	@Override
	public void setArcballReferencePoint(Vec rap) {
		Vec vec = new Vec(rap.x(), rap.y(), 0);
		frame().setArcballReferencePoint(vec);
	}

	@Override
	public boolean setArcballReferencePointFromPixel(Point pixel) {		
		setArcballReferencePoint(unprojectedCoordinatesOf(new Vec((float) pixel.x, (float) pixel.y, 0.5f)));
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
		Rect rect = new Rect((int)cX, (int)cY, (int)winW, (int)winH);
		this.interpolateToZoomOnRegion(rect);		
	}	
}
