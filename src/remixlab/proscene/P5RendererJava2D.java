package remixlab.proscene;

import processing.core.*;

import remixlab.remixcam.core.*;
import remixlab.remixcam.geom.*;
import remixlab.remixcam.renderers.BTransformationRenderer;
//import remixlab.remixcam.renderers.TransformationRenderer;

public class P5RendererJava2D extends BTransformationRenderer {
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
