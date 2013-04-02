package remixlab.proscene;

import processing.core.*;

import remixlab.remixcam.core.*;
import remixlab.remixcam.geom.*;
import remixlab.remixcam.renderers.TransformationRenderer;

public class P5RendererJava2D extends TransformationRenderer {
	PGraphics pg;
	Matrix3D proj;
	
	public P5RendererJava2D(Scene scn, PGraphics renderer, Drawerable d) {
		super(scn, d);
		pg = renderer;
	}
	
	public P5RendererJava2D(Scene scn, PGraphics renderer) {
		super(scn, new P5Drawing2D(scn));
		pg = renderer;
	}		
	
	public PGraphics pg() {
		return pg;
	}
	
	public PGraphicsJava2D pgj2d() {
	  return (PGraphicsJava2D) pg();	
	}

	@Override
	public boolean is3D() {
		return false;
	}
	
	/**
	@Override
	public void applyMatrix(Matrix3D source) {
		PMatrix3D pM = new PMatrix3D();
		pM.set(source.getTransposed(new float[16]));
		pg().applyMatrix(pM);
	}
	*/

	@Override
	public void setProjection() {
		AbstractScene.showDepthWarning("setProjection");
	}

	@Override
	public void unsetProjection() {
		AbstractScene.showDepthWarning("setProjection");
	}

	@Override
	public void applyMatrixRowMajorOrder(float n00, float n01, float n02,
			float n03, float n10, float n11, float n12, float n13, float n20,
			float n21, float n22, float n23, float n30, float n31, float n32,
			float n33) {
		pgj2d().applyMatrix(n00, n01, n02, n03, n10, n11, n12, n13, n20, n21, n22,	n23, n30, n31, n32, n33);
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
}
