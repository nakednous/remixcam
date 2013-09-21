/*******************************************************************************
 * Dandelion (version 0.9.50)
 * Copyright (c) 2013 Jean Pierre Charalambos.
 * @author Jean Pierre Charalambos
 * https://github.com/remixlab
 * 
 * All rights reserved. Library that eases the creation of interactive
 * scenes released under the terms of the GNU Public License v3.0
 * which available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package remixlab.dandelion.renderer;

import java.util.List;

import remixlab.dandelion.core.*;
import remixlab.dandelion.geom.*;

public abstract class Renderer implements Renderable, Constants {
	protected AbstractScene scene;
	protected Depictable d;
	
	public Renderer(AbstractScene scn, Depictable dw) {
		scene = scn;
		d = dw;
	}
	
	@Override
	public AbstractScene scene() {
		return scene;
	}
	
	public Depictable drawer() {
		return d;
	}
	
	public void setDrawerable(Depictable dw) {
		d = dw;
	}
	
	public boolean isRightHanded() {
		return scene.isRightHanded();
	}
	
	public boolean isLeftHanded() {
		return scene.isLeftHanded();
	}
	
	@Override
  public boolean is2D() {
  	return !is3D();
  }
	
  public int width() {
  	return scene.width();
  }
  
  public int height() {
  	return scene.height();
  }
	
  //matrix stuff
	
	@Override
	public void pushMatrix() {
		if( !(this instanceof StackRenderer) )
			scene.pushMatrix();
	}
	
	@Override
	public void popMatrix() {
		if( !(this instanceof StackRenderer) )
			scene.popMatrix();
	}
	
	@Override
	public void resetMatrix() {
		if( !(this instanceof StackRenderer) )
			scene.resetMatrix();
	}
	
	@Override
	public Mat getMatrix() {
		if( !(this instanceof StackRenderer) )
			return scene.getMatrix();
		return null;
  }
  
	@Override
  public Mat getMatrix(Mat target) {
		if( !(this instanceof StackRenderer) )
			return scene.getMatrix(target);
  	return null;
  }
	
	@Override
  public void setMatrix(Mat source) {
		if( !(this instanceof StackRenderer) )
			scene.setMatrix(source);
  }
	
	@Override
  public void printMatrix() {
		if( !(this instanceof StackRenderer) )
			scene.printMatrix();  	
  }
	
	@Override
	public void applyMatrix(Mat source) {
		if( !(this instanceof StackRenderer) )
			scene.applyMatrix(source);
	}
	
	@Override
	public void applyMatrixRowMajorOrder(float n00, float n01, float n02, float n03,
      float n10, float n11, float n12, float n13,
      float n20, float n21, float n22, float n23,
      float n30, float n31, float n32, float n33) {
		if( !(this instanceof StackRenderer) )
			scene.applyMatrixRowMajorOrder(n00, n01, n02, n03, n10, n11, n12, n13, n20, n21, n22, n23, n30, n31, n32, n33);
	}
	
	//perspective
	
	@Override
	public void pushProjection() {
		AbstractScene.showMissingImplementationWarning("pushProjection");
	}
	
	@Override
	public void popProjection() {
		AbstractScene.showMissingImplementationWarning("popProjection");
	}	
	
	@Override
	public void resetProjection() {
		AbstractScene.showMissingImplementationWarning("resetProjection");
	}
	
	@Override
  public Mat getProjection() {
		AbstractScene.showMissingImplementationWarning("getProjection");
		return null;
	}
  
	@Override
  public Mat getProjection(Mat target) {
		AbstractScene.showMissingImplementationWarning("getProjection");
		return null;
	}
  
	@Override
  public void setProjection(Mat source) {
		AbstractScene.showMissingImplementationWarning("setProjection");
	}
  
	@Override
  public void printProjection() {
		AbstractScene.showMissingImplementationWarning("printProjection");
	}
	
	@Override
	public void applyProjection(Mat source) {
		AbstractScene.showMissingImplementationWarning("applyProjection");
	}
  
  @Override
	public void applyProjectionRowMajorOrder(float n00, float n01, float n02,
			float n03, float n10, float n11, float n12, float n13, float n20,
			float n21, float n22, float n23, float n30, float n31, float n32,
			float n33) {
		AbstractScene.showMethodWarning("applyProjectionRowMajorOrder");
	}  
	
	//---
	
	@Override
	public void translate(float tx, float ty) {
		if( !(this instanceof StackRenderer) )
			scene.translate(tx, ty);
	}

	@Override
	public void translate(float tx, float ty, float tz) {
		if( !(this instanceof StackRenderer) )
			scene.translate(tx, ty, tz);
	}
	
	@Override
	public void rotate(float angle) {
		if( !(this instanceof StackRenderer) )
			scene.rotate(angle);
	}

	@Override
	public void rotateX(float angle) {
		if( !(this instanceof StackRenderer) )
			scene.rotateX(angle);
	}

	@Override
	public void rotateY(float angle) {
		if( !(this instanceof StackRenderer) )
			scene.rotateY(angle);
	}

	@Override
	public void rotateZ(float angle) {
		if( !(this instanceof StackRenderer) )
			scene.rotateZ(angle);
	}
	
	@Override
	public void rotate(float angle, float vx, float vy, float vz) {
		if( !(this instanceof StackRenderer) )
			scene.rotate(angle, vx, vy, vz);
	}
	
	@Override
	public void scale(float s) {
		if( !(this instanceof StackRenderer) )
			scene.scale(s);
	}

	@Override
	public void scale(float sx, float sy) {
		if( !(this instanceof StackRenderer) )
			scene.scale(sx, sy);
	}

	@Override
	public void scale(float x, float y, float z) {
		if( !(this instanceof StackRenderer) )
			scene.scale(x, y, z);
	}
	
	// -- Drawing
	
  // 3d methods just print a warning by default
  
  @Override
  public void cylinder(float w, float h) {
  	d.cylinder(w, h);
  }
  
  @Override
 	public void hollowCylinder(int detail, float w, float h, Vec m, Vec n) {
  	d.hollowCylinder(detail, w, h, m, n);
 	}
  
  @Override
  public void cone(int detail, float x, float y, float r, float h) {
  	d.cone(detail, x, y, r, h);
 	}
  
  @Override
  public void cone(int detail, float x, float y, float r1, float r2, float h) {
  	d.cone(detail, x, y, r1, r2, h);
 	}
  
  @Override
  public void drawCamera(Camera camera, boolean drawFarPlane, float scale) {
  	d.drawCamera(camera, drawFarPlane, scale);
 	}

  @Override
  public void drawKFIViewport(float scale) {
  	d.drawKFIViewport(scale);
 	}

	@Override
	public void drawAxis(float length) {
		d.drawAxis(length);
	}

	@Override
	public void drawGrid(float size, int nbSubdivisions) {
		d.drawGrid(size, nbSubdivisions);		
	}

	@Override
	public void drawDottedGrid(float size, int nbSubdivisions) {
		d.drawDottedGrid(size, nbSubdivisions);
	}

	@Override
	public void drawWindow(Window window, float scale) {
		d.drawWindow(window, scale);
	}

	@Override
	public void drawZoomWindowHint() {
		d.drawZoomWindowHint();
	}

	@Override
	public void drawScreenRotateLineHint() {
		d.drawScreenRotateLineHint();
	}

	@Override
	public void drawArcballReferencePointHint() {
		d.drawArcballReferencePointHint();
	}

	@Override
	public void drawCross(float px, float py, float size) {
		d.drawCross(px, py, size);
	}

	@Override
	public void drawFilledCircle(int subdivisions, Vec center, float radius) {
		d.drawFilledCircle(subdivisions, center, radius);
	}

	@Override
	public void drawFilledSquare(Vec center, float edge) {
		d.drawFilledSquare(center, edge);
	}

	@Override
	public void drawShooterTarget(Vec center, float length) {
		d.drawShooterTarget(center, length);
	}

	@Override
	public void drawPath(List<RefFrame> path, int mask, int nbFrames, int nbSteps, float scale) {
		d.drawPath(path, mask, nbFrames, nbSteps, scale);		
	}
	
	//--
	
	@Override
	public void bindMatrices() {
		scene.viewport().computeProjectionMatrix();
		scene.viewport().computeViewMatrix();
		scene.viewport().computeProjectionViewMatrix();

		Vec pos = scene.viewport().position();
		Orientable quat = scene.viewport().frame().orientation();

		if( scene.is2D() ) {
			translate(scene.width() / 2, scene.height() / 2);
			if(scene.isRightHanded()) scale(1,-1);		
			scale(scene.viewport().frame().inverseMagnitude().x(), 
				    scene.viewport().frame().inverseMagnitude().y());		
			rotate(-quat.angle());		
			translate(-pos.x(), -pos.y());
		}
		else {
			//TODO how to handle 3d case without projection, seems impossible
			//setProjection();
			Vec axis = ((Quat)quat).axis();
			// third value took from P5 docs, (see: http://processing.org/reference/camera_.html)
			// Also changed default cam fov (i.e., Math.PI / 3.0f) to match that of P5 (see: http://processing.org/reference/perspective_.html)
			// previously it was: 
			translate(scene.width() / 2, scene.height() / 2,  (scene.height() / 2) / (float) Math.tan(PI / 6));
			if(scene.isRightHanded()) scale(1,-1,1);
			rotate(-quat.angle(), axis.x(), axis.y(), axis.z());		
			translate(-pos.x(), -pos.y(), -pos.z());
		}
	}
	
	@Override
	public void beginScreenDrawing() {
		Vec pos = scene.viewport().position();
		Orientable quat = scene.viewport().frame().orientation();		
		
		pushMatrix();
		
		if( scene.is2D() ) {
		  translate(pos.x(), pos.y());
		  rotate(quat.angle());		
		  scale(scene.window().frame().magnitude().x(),
		        scene.window().frame().magnitude().y());
	    if(scene.isRightHanded()) scale(1,-1);
		  translate(-scene.width()/2, -scene.height()/2);			
		}
		else {
			Vec axis = ((Quat)quat).axis();
			translate(pos.x(), pos.y(), pos.z());
			rotate(quat.angle(), axis.x(), axis.y(), axis.z());
			//projection
		  //TODO how to handle 3d case without projection, seems imposible
			//unsetProjection();
		  if(scene.isRightHanded()) scale(1,-1,1);
		  translate(-scene.width() / 2, -scene.height() / 2,  -(scene.height() / 2) / (float) Math.tan(PI / 6));
		}
	}
	
	@Override
	public void endScreenDrawing() {
		popMatrix();
	}
	
	protected void setProjectionMatrix() {
		AbstractScene.showMissingImplementationWarning("setProjectionMatrix");
	}
	
	protected void setModelViewMatrix() {
		AbstractScene.showMissingImplementationWarning("setModelViewMatrix");
	}
}
