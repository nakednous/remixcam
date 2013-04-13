package remixlab.proscene.renderers;

import processing.core.*;

import remixlab.proscene.Scene;
import remixlab.remixcam.core.*;
import remixlab.remixcam.geom.*;
import remixlab.remixcam.renderers.TransformationRenderer;
//import remixlab.remixcam.renderers.TransformationRenderer;

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
}
