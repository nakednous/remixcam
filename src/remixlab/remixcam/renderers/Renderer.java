package remixlab.remixcam.renderers;

import java.util.List;

import remixlab.remixcam.core.*;
import remixlab.remixcam.geom.*;

public abstract class Renderer implements Renderable, Constants {
	protected AbstractScene scene;
	protected Drawerable d;
	
	public Renderer(AbstractScene scn) {
		scene = scn;
	}
	
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
}
