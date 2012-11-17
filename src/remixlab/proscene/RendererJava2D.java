package remixlab.proscene;

import java.util.List;

import processing.core.*;

import remixlab.remixcam.core.*;
import remixlab.remixcam.geom.*;

public class RendererJava2D extends Renderer {
	public RendererJava2D(AbstractScene scn, PGraphicsJava2D renderer) {
		super(scn, renderer);
	}
	
	public PGraphicsJava2D pgj2d() {
	  return (PGraphicsJava2D) pg();	
	}
	
	/**
	@Override
	public void bindMatrices() {
		scene.viewWindow().computeProjectionMatrix();
		scene.viewWindow().computeViewMatrix();		
		Vector3D pos = scene.viewWindow().position();
		Orientable quat = scene.viewWindow().frame().orientation();
		
		translate(scene.width()/2, scene.height()/2);
		scale(scene.viewWindow().frame().inverseMagnitude().x(),
				  scene.viewWindow().frame().inverseMagnitude().y());
		rotate(-quat.angle());
		translate(-pos.x(), -pos.y());
	}
	
	@Override
	public void beginScreenDrawing() {
		Vector3D pos = scene.viewWindow().position();
		Orientable quat = scene.viewWindow().frame().orientation();
		
		pushMatrix();
		translate(pos.x(), pos.y());
		rotate(quat.angle());		
		scale(scene.viewWindow().frame().magnitude().x(),
		      scene.viewWindow().frame().magnitude().y());		
		translate(-scene.width()/2, -scene.height()/2);		
	}
	
	@Override
	public void endScreenDrawing() {
		popMatrix();
	}
	*/

	@Override
	public void pushProjection() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void popProjection() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetProjection() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadProjection(Matrix3D source) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applyProjection(Matrix3D source) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void applyProjectionRowMajorOrder(float n00, float n01, float n02,
			float n03, float n10, float n11, float n12, float n13, float n20,
			float n21, float n22, float n23, float n30, float n31, float n32,
			float n33) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Matrix3D getProjection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Matrix3D getProjection(Matrix3D target) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cylinder(float w, float h) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cone(int detail, float x, float y, float r, float h) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cone(int detail, float x, float y, float r1, float r2, float h) {
		// TODO Auto-generated method stub
		
	}

	/**
	@Override
	public void drawCamera(Camera camera, boolean drawFarPlane, float scale) {
		// TODO Auto-generated method stub		
	}
	// */

	@Override
	public void drawKFIViewport(float scale) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawPath(List<VFrame> path, int mask, int nbFrames,
			int nbSteps, float scale) {
		// TODO Auto-generated method stub
		
	}	
}
