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

public class Window extends Viewport implements Copyable {
	
	static final float FAKED_ZNEAR = -10;  
  static final float FAKED_ZFAR = 10;	
	
	public Window(AbstractScene scn) {
		super(scn);
		if(scene.is3D())
			throw new RuntimeException("Use ViewWindow only for a 2D Scene");
		fpCoefficients = new float[4][3];		
		computeProjectionMatrix();
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
	public void computeViewMatrix() {		
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
		
		Vec vec = frame().magnitude();
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
	
	public void setScaling(Vec s) {
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
	public Vec rightVector() {
		return frame().xAxis();
	}
	
	@Override
	public Vec upVector() {
		return frame().yAxis();
	}
	
	@Override
	public void setUpVector(Vec up, boolean noMove) {
		Quat q = new Quat(new Vec(0.0f, 1.0f, 0.0f), frame().transformOf(up));

		if (!noMove) 		
			frame().setPosition(Vec.sub(arcballReferencePoint(), (Rot.compose((Rot) frame().orientation(), q)).rotate(frame().coordinatesOf(arcballReferencePoint()))));		

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
		setSceneCenter(new Vec(pixel.x, pixel.y, 0));
		return true;		
	}
	
	public Visibility rectIsVisible(Vec p1, Vec p2) {
		//TODO implement me	
		return Visibility.SEMIVISIBLE;
	}
	
	public void fitBoundingRect(Vec min, Vec max) {
		float diameter = Math.max(Math.abs(max.vec[1] - min.vec[1]), Math.abs(max.vec[0] - min.vec[0]));
		diameter = Math.max(Math.abs(max.vec[2] - min.vec[2]), diameter);
		fitCircle(Vec.mult(Vec.add(min, max), 0.5f), 0.5f * diameter);
	}
	
	public void fitCircle(Vec center, float radius) {
	  Vec scl = frame().scaling();
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
		Orientable quat = new Rot(angle);
		frame().setOrientation(quat);
		frame().updateFlyUpVector();
	}
	
	@Override
	public float pixelP5Ratio(Vec position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean pointIsVisible(Vec point) {
		// TODO Auto-generated method stub
		return false;
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
