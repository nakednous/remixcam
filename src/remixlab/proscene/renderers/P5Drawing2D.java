package remixlab.proscene.renderers;

import java.util.List;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import remixlab.proscene.Scene;
//import processing.core.PVector;
import remixlab.remixcam.core.AbstractScene;
import remixlab.remixcam.core.Camera;
import remixlab.remixcam.core.Drawerable;
import remixlab.remixcam.core.ViewWindow;
import remixlab.remixcam.geom.*;

public class P5Drawing2D implements Drawerable, PConstants {
	protected Scene scene;
	Matrix3D proj;

	public P5Drawing2D(Scene scn) {
		scene = scn;
		proj = new Matrix3D();
	}
	
	public Scene scene() {
		return scene;
	}
	
	public boolean isRightHanded() {
		return scene.isRightHanded();
	}
	
	public boolean isLeftHanded() {
		return scene.isLeftHanded();
	}
	
	public PGraphics pg() {
		return scene.pg();
	}	
	
	@Override
	public void drawAxis(float length) {
		final float charWidth = length / 40.0f;
		final float charHeight = length / 30.0f;
		final float charShift = 1.05f * length;
		
    pg().pushStyle();		
    pg().strokeWeight(2);
		pg().beginShape(LINES);	
		
		// The X		
		pg().stroke(200, 0, 0);		
		pg().vertex(charShift + charWidth, -charHeight);
		pg().vertex(charShift - charWidth, charHeight);
		pg().vertex(charShift - charWidth, -charHeight);
		pg().vertex(charShift + charWidth, charHeight);
		
		// The Y
		pg().stroke(0, 200, 0);
		pg().vertex(charWidth, charShift + charHeight);
		pg().vertex(0.0f, charShift + 0.0f);
		pg().vertex(-charWidth, charShift + charHeight);
		pg().vertex(0.0f, charShift + 0.0f);
		pg().vertex(0.0f, charShift + 0.0f);
		pg().vertex(0.0f, charShift + -charHeight);
		
		pg().endShape();		
		pg().popStyle();		
		
		pg().pushStyle();				
		pg().strokeWeight(2);			  
		
	  // X Axis
		pg().stroke(200, 0, 0);
		pg().line(0, 0, length, 0);
	  // Y Axis
		pg().stroke(0, 200, 0);		
		pg().line(0, 0, 0, length);		

		pg().popStyle();
	}

	public void drawGrid(float size, int nbSubdivisions) {
		pg().pushStyle();
		pg().stroke(170, 170, 170);
		pg().strokeWeight(1);
		pg().beginShape(LINES);
		for (int i = 0; i <= nbSubdivisions; ++i) {
			final float pos = size * (2.0f * i / nbSubdivisions - 1.0f);
			pg().vertex(pos, -size);
			pg().vertex(pos, +size);
			pg().vertex(-size, pos);
			pg().vertex(size, pos);
		}
		pg().endShape();
		pg().popStyle();
	}

	@Override
	public void drawDottedGrid(float size, int nbSubdivisions) {
		float posi, posj;
		pg().pushStyle();
		pg().stroke(170);
		pg().strokeWeight(2);
		pg().beginShape(POINTS);
		for (int i = 0; i <= nbSubdivisions; ++i) {
			posi = size * (2.0f * i / nbSubdivisions - 1.0f);
			for(int j = 0; j <= nbSubdivisions; ++j) {
				posj = size * (2.0f * j / nbSubdivisions - 1.0f);
				pg().vertex(posi, posj);
			}
		}
		pg().endShape();
		//pg().popStyle();
		
		int internalSub = 5;
		int subSubdivisions = nbSubdivisions * internalSub;
		//pg().pushStyle();
		pg().stroke(100);
		pg().strokeWeight(1);
		pg().beginShape(POINTS);
		for (int i = 0; i <= subSubdivisions; ++i) {
			posi = size * (2.0f * i / subSubdivisions - 1.0f);
			for(int j = 0; j <= subSubdivisions; ++j) {
				posj = size * (2.0f * j / subSubdivisions - 1.0f);
				if(( (i%internalSub) != 0 ) || ( (j%internalSub) != 0 ) )
					pg().vertex(posi, posj);
			}
		}
		pg().endShape();
		pg().popStyle();
	}	

	@Override
	public void drawZoomWindowHint() {
		//TODO hack
		float p1x = (float) ((Scene)scene).dE.fCorner.getX();
		float p1y = (float) ((Scene)scene).dE.fCorner.getY();
		float p2x = (float) ((Scene)scene).dE.lCorner.getX();
		float p2y = (float) ((Scene)scene).dE.lCorner.getY();
		scene.beginScreenDrawing();
		pg().pushStyle();
		pg().stroke(255, 255, 255);
		pg().strokeWeight(2);
		pg().noFill();
		pg().beginShape();
		pg().vertex(p1x, p1y);
		pg().vertex(p2x, p1y);
		pg().vertex(p2x, p2y);		
		pg().vertex(p1x, p2y);
		pg().endShape(CLOSE);
		pg().popStyle();
		scene.endScreenDrawing();		
	}

	@Override
	public void drawScreenRotateLineHint() {
		float p1x = (float) ((Scene)scene).dE.fCorner.getX();
		float p1y = (float) ((Scene)scene).dE.fCorner.getY();
		Vector3D p2 = scene.pinhole().projectedCoordinatesOf(scene.arcballReferencePoint());
		scene.beginScreenDrawing();
		pg().pushStyle();
		pg().stroke(255, 255, 255);
		pg().strokeWeight(2);
		pg().noFill();
		pg().line(p2.x(), p2.y(), p1x, p1y);
		pg().popStyle();
		scene.endScreenDrawing();
	}

	@Override
	public void drawArcballReferencePointHint() {
		Vector3D p = scene.pinhole().projectedCoordinatesOf(scene.arcballReferencePoint());
		pg().pushStyle();
		pg().stroke(255);
		pg().strokeWeight(3);
		scene.drawCross(p.vec[0], p.vec[1]);
		pg().popStyle();
	}

	@Override
	public void drawCross(float px, float py, float size) {
		scene.beginScreenDrawing();
		pg().pushStyle();		
		//pg().stroke(color);
		//pg().strokeWeight(strokeWeight);
		pg().noFill();
		pg().beginShape(LINES);
		pg().vertex(px - size, py);
		pg().vertex(px + size, py);
		pg().vertex(px, py - size);
		pg().vertex(px, py + size);
		pg().endShape();
		pg().popStyle();
		scene.endScreenDrawing();		
	}

	@Override
	public void drawFilledCircle(int subdivisions, Vector3D center, float radius) {
		float precision = TWO_PI/subdivisions;
		float x = center.x();
		float y = center.y();
		float angle, x2, y2;
		scene.beginScreenDrawing();
		pg().pushStyle();
		pg().noStroke();
		//pg().fill(color);
		pg().beginShape(TRIANGLE_FAN);		
		pg().vertex(x, y);
		for (angle = 0.0f; angle <= TWO_PI + 1.1*precision; angle += precision) {			
			x2 = x + PApplet.sin(angle) * radius;
			y2 = y + PApplet.cos(angle) * radius;			
			pg().vertex(x2, y2);
		}
		pg().endShape();
		pg().popStyle();
		scene.endScreenDrawing();
	}

	@Override
	public void drawFilledSquare(Vector3D center, float edge) {
		float x = center.x();
		float y = center.y();
		scene.beginScreenDrawing();		
		pg().pushStyle();
		pg().noStroke();
		//pg().fill(color);
		pg().beginShape(QUADS);
		pg().vertex(x - edge, y + edge);
		pg().vertex(x + edge, y + edge);
		pg().vertex(x + edge, y - edge);
		pg().vertex(x - edge, y - edge);
		pg().endShape();
		pg().popStyle();
		scene.endScreenDrawing();
	}

	@Override
	public void drawShooterTarget(Vector3D center, float length) {
		float x = center.x();
		float y = center.y();
		scene.beginScreenDrawing();
		
		pg().pushStyle();

		//pg().stroke(color);
		//pg().strokeWeight(strokeWeight);
		pg().noFill();

		pg().beginShape();
		pg().vertex((x - length), (y - length) + (0.6f * length));
		pg().vertex((x - length), (y - length));
		pg().vertex((x - length) + (0.6f * length), (y - length));
		pg().endShape();

		pg().beginShape();
		pg().vertex((x + length) - (0.6f * length), (y - length));
		pg().vertex((x + length), (y - length));
		pg().vertex((x + length), ((y - length) + (0.6f * length)));
		pg().endShape();
		
		pg().beginShape();
		pg().vertex((x + length), ((y + length) - (0.6f * length)));
		pg().vertex((x + length), (y + length));
		pg().vertex(((x + length) - (0.6f * length)), (y + length));
		pg().endShape();

		pg().beginShape();
		pg().vertex((x - length) + (0.6f * length), (y + length));
		pg().vertex((x - length), (y + length));
		pg().vertex((x - length), ((y + length) - (0.6f * length)));
		pg().endShape();

		pg().popStyle();
		scene.endScreenDrawing();

		drawCross(center.x(), center.y(), 0.6f * length);
	}

	@Override
	public void drawViewWindow(ViewWindow camera, float scale) {
		pg().pushMatrix();
		
		/**
		VFrame tmpFrame = new VFrame(scene.is3D());
		tmpFrame.fromMatrix(camera.frame().worldMatrix(), camera.frame().magnitude());		
		scene().applyTransformation(tmpFrame);
		// */
		//Same as above, but easier ;)
	  scene().applyWorldTransformation(camera.frame());

		//upper left coordinates of the near corner
		Vector3D upperLeft = new Vector3D();
		
		pg().pushStyle();
		
		/**
		float[] wh = camera.getOrthoWidthHeight();
		upperLeft.x = scale * wh[0];
		upperLeft.y = scale * wh[1];
		*/
		
		upperLeft.x(scale * scene.width() / 2);
		upperLeft.y(scale * scene.height() / 2);
						
		pg().noStroke();		
		pg().beginShape(PApplet.QUADS);				
		pg().vertex(upperLeft.x(), upperLeft.y());
		pg().vertex(-upperLeft.x(), upperLeft.y());
		pg().vertex(-upperLeft.x(), -upperLeft.y());
		pg().vertex(upperLeft.x(), -upperLeft.y());		
		pg().endShape();

		// Up arrow
		float arrowHeight = 1.5f * upperLeft.y();
		float baseHeight = 1.2f * upperLeft.y();
		float arrowHalfWidth = 0.5f * upperLeft.x();
		float baseHalfWidth = 0.3f * upperLeft.x();
		
	  // Base
		pg().beginShape(PApplet.QUADS);		
		if( camera.scene.isLeftHanded() ) {
			pg().vertex(-baseHalfWidth, -upperLeft.y());
			pg().vertex(baseHalfWidth, -upperLeft.y());
			pg().vertex(baseHalfWidth, -baseHeight);
			pg().vertex(-baseHalfWidth, -baseHeight);	
		}
		else {
			pg().vertex(-baseHalfWidth, upperLeft.y());
			pg().vertex(baseHalfWidth, upperLeft.y());
			pg().vertex(baseHalfWidth, baseHeight);
			pg().vertex(-baseHalfWidth, baseHeight);
		}
		pg().endShape();
		
	  // Arrow
		pg().beginShape(PApplet.TRIANGLES);
		if( camera.scene.isLeftHanded() ) {
			pg().vertex(0.0f, -arrowHeight);
			pg().vertex(-arrowHalfWidth, -baseHeight);
			pg().vertex(arrowHalfWidth, -baseHeight);
		}
		else {
			pg().vertex(0.0f, arrowHeight);
			pg().vertex(-arrowHalfWidth, baseHeight);
			pg().vertex(arrowHalfWidth, baseHeight);
		}
		pg().endShape();		
		
		pg().popStyle();
		pg().popMatrix();
	}
	
	 @Override
	  public void cylinder(float w, float h) {
	  	AbstractScene.showDepthWarning("cylinder");
	  }
	  
	  @Override
	 	public void hollowCylinder(int detail, float w, float h, Vector3D m, Vector3D n) {
	  	AbstractScene.showDepthWarning("cylinder");
	 	}
	  
	  @Override
	  public void cone(int detail, float x, float y, float r, float h) {
	  	AbstractScene.showDepthWarning("cylinder");
	 	}
	  
	  @Override
	  public void cone(int detail, float x, float y, float r1, float r2, float h) {
	  	AbstractScene.showDepthWarning("cylinder");
	 	}
	  
	  @Override
	  public void drawCamera(Camera camera, boolean drawFarPlane, float scale) {
	  	AbstractScene.showDepthWarning("cylinder");
	 	}

	  @Override
	  public void drawKFIViewport(float scale) {
	  	AbstractScene.showDepthWarning("cylinder");
	 	}

	@Override
	public void drawPath(List<GeomFrame> path, int mask, int nbFrames,	int nbSteps, float scale) {
		// TODO pend		
	}
}
