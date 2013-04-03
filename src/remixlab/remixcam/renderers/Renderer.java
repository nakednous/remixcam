package remixlab.remixcam.renderers;

import java.util.List;

import remixlab.remixcam.core.*;
import remixlab.remixcam.geom.*;

public abstract class Renderer implements Renderable {
	protected AbstractScene scene;
	protected Drawerable d;
	
	public Renderer(AbstractScene scn, Drawerable dw) {
		scene = scn;
		d = dw;
	}
	
	@Override
	public AbstractScene scene() {
		return scene;
	}
	
	public Drawerable drawer() {
		return d;
	}
	
	public void setDrawerable(Drawerable dw) {
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
	public Matrix3D getMatrix() {
		if( !(this instanceof StackRenderer) )
			return scene.getMatrix();
		return null;
  }
  
	@Override
  public Matrix3D getMatrix(Matrix3D target) {
		if( !(this instanceof StackRenderer) )
			return scene.getMatrix(target);
  	return null;
  }
	
	@Override
  public void setMatrix(Matrix3D source) {
		if( !(this instanceof StackRenderer) )
			scene.setMatrix(source);
  }
	
	@Override
  public void printMatrix() {
		if( !(this instanceof StackRenderer) )
			scene.printMatrix();  	
  }
	
	@Override
	public void applyMatrix(Matrix3D source) {
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
  public Matrix3D getProjection() {
		AbstractScene.showMissingImplementationWarning("getProjection");
		return null;
	}
  
	@Override
  public Matrix3D getProjection(Matrix3D target) {
		AbstractScene.showMissingImplementationWarning("getProjection");
		return null;
	}
  
	@Override
  public void setProjection(Matrix3D source) {
		AbstractScene.showMissingImplementationWarning("setProjection");
	}
  
	@Override
  public void printProjection() {
		AbstractScene.showMissingImplementationWarning("printProjection");
	}
	
	@Override
	public void applyProjection(Matrix3D source) {
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
 	public void hollowCylinder(int detail, float w, float h, Vector3D m, Vector3D n) {
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
	public void drawViewWindow(ViewWindow window, float scale) {
		d.drawViewWindow(window, scale);
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
	public void drawFilledCircle(int subdivisions, Vector3D center, float radius) {
		d.drawFilledCircle(subdivisions, center, radius);
	}

	@Override
	public void drawFilledSquare(Vector3D center, float edge) {
		d.drawFilledSquare(center, edge);
	}

	@Override
	public void drawShooterTarget(Vector3D center, float length) {
		d.drawShooterTarget(center, length);
	}

	@Override
	public void drawPath(List<GeomFrame> path, int mask, int nbFrames, int nbSteps, float scale) {
		d.drawPath(path, mask, nbFrames, nbSteps, scale);		
	}
	
	@Override
	public void bindMatrices() {
		setProjectionMatrix();
		setModelViewMatrix();
		scene.pinhole().cacheProjViewInvMat();
	}
	
	protected void setProjectionMatrix() {
		AbstractScene.showMissingImplementationWarning("setProjectionMatrix");
	}
	
	protected void setModelViewMatrix() {
		AbstractScene.showMissingImplementationWarning("setModelViewMatrix");
	}
	
	@Override
	public void beginScreenDrawing() {
		AbstractScene.showMissingImplementationWarning("beginScreenDrawing");
	}
	
	@Override
	public void endScreenDrawing() {
		popProjection();  
		popMatrix();
	}
}
