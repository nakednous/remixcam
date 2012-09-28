/**
 *                     ProScene (version 1.9.90)      
 *    Copyright (c) 2010-2011 by National University of Colombia
 *                 @author Jean Pierre Charalambos      
 *           http://www.disi.unal.edu.co/grupos/remixlab/
 *                           
 * This java package provides classes to ease the creation of interactive 3D
 * scenes in Processing.
 * 
 * This source file is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option)
 * any later version.
 * 
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 * 
 * A copy of the GNU General Public License is available on the World Wide Web
 * at <http://www.gnu.org/copyleft/gpl.html>. You can also obtain it by
 * writing to the Free Software Foundation, 51 Franklin Street, Suite 500
 * Boston, MA 02110-1335, USA.
 */

package remixlab.proscene;

import java.util.List;

import processing.core.*;
import processing.opengl.*;
/**
import remixlab.remixcam.core.*;
import remixlab.remixcam.geom.*;
*/
// /**
import remixlab.remixcam.core.AbstractScene;
import remixlab.remixcam.core.Renderable;
import remixlab.remixcam.core.Camera;
import remixlab.remixcam.core.ViewWindow;
import remixlab.remixcam.geom.Matrix3D;
import remixlab.remixcam.geom.VFrame;
import remixlab.remixcam.geom.Vector3D;

public class Renderer implements Renderable, PConstants {
	protected AbstractScene scene;
	protected PGraphics pg;

	public Renderer(AbstractScene scn, PGraphics renderer) {
		pg = renderer;
		scene = scn;		
	}
	
	@Override
	public AbstractScene scene() {
		return scene;
	}
	
	public PGraphics pg() {
		return pg;
	}
	
	@Override
	public void bindMatrices() {
		// TODO Auto-generated method stub		
	}
	
	@Override
	public void beginScreenDrawing() {
		// TODO Auto-generated method stub		
	}

	@Override
	public void endScreenDrawing() {
		// TODO Auto-generated method stub		
	}
	
	@Override
  public boolean is2D() {
  	return !is3D();
  }
	
  @Override
	public boolean is3D() {
		if( pg instanceof PGraphics3D )
  		return true;
  	return false;		
	}

	@Override
	public void pushMatrix() {
		pg().pushMatrix();
	}

	@Override
	public void popMatrix() {
		pg().popMatrix();
	}	

	@Override
	public void translate(float tx, float ty) {
		pg().translate(tx, ty);
	}

	@Override
	public void translate(float tx, float ty, float tz) {
		pg().translate(tx, ty, tz);
	}

	@Override
	public void rotate(float angle) {
		pg().rotate(angle);
	}

	@Override
	public void rotateX(float angle) {
		pg().rotateX(angle);
	}

	@Override
	public void rotateY(float angle) {
		pg().rotateY(angle);
	}

	@Override
	public void rotateZ(float angle) {
		pg().rotateZ(angle);
	}

	@Override
	public void rotate(float angle, float vx, float vy, float vz) {
		pg().rotate(angle, vx, vy, vz);
	}

	@Override
	public void scale(float s) {
		pg().scale(s);
	}

	@Override
	public void scale(float sx, float sy) {
		pg().scale(sx, sy);
	}

	@Override
	public void scale(float x, float y, float z) {
		pg().scale(x, y, z);
	}	

	@Override
	public void resetMatrix() {
		pg().resetMatrix();
	}	

	@Override
	public void loadMatrix(Matrix3D source) {
		PMatrix3D pM = new PMatrix3D();
		pM.set(source.getTransposed(new float[16]));
		pg().setMatrix(pM);
	}
		
	@Override
	public void multiplyMatrix(Matrix3D source) {
		this.applyMatrix(source);
	}
	
	@Override
	public void multiplyProjection(Matrix3D source) {
		this.applyProjection(source);
	}

	@Override
	public void applyMatrix(Matrix3D source) {
		PMatrix3D pM = new PMatrix3D();
		pM.set(source.getTransposed(new float[16]));
		pg().applyMatrix(pM);
	}
	
	@Override
	public void applyMatrixRowMajorOrder(float n00, float n01, float n02, float n03,
			                                 float n10, float n11, float n12, float n13,
			                                 float n20, float n21, float n22, float n23,
			                                 float n30, float n31, float n32, float n33) {
		pg().applyMatrix(n00, n01, n02, n03, n10, n11, n12, n13, n20, n21, n22,	n23, n30, n31, n32, n33);
	}
	
	@Override
	public void frustum(float left, float right, float bottom, float top,	float znear, float zfar) {
		//pg().frustum(left, right, bottom, top, znear, zfar);
		//TODO
	}

	@Override
	public Matrix3D getMatrix() {
		PMatrix3D pM = (PMatrix3D) pg().getMatrix();
		return new Matrix3D(pM.get(new float[16]), true);// set it transposed
	}
	
	@Override
	public Matrix3D getMatrix(Matrix3D target) {
		PMatrix3D pM = (PMatrix3D) pg().getMatrix();
		target.setTransposed(pM.get(new float[16]));
		return target;
	}
	
	@Override
	public void setMatrix(Matrix3D source) {
		resetMatrix();
		applyMatrix(source);
	}
	
	@Override
	public void setProjection(Matrix3D source) {
		resetProjection();
		applyProjection(source);
	}

	@Override
	public void printMatrix() {
		pg().printMatrix();
	}
	
	@Override
	public void printProjection() {
		pg().printProjection();
	}
	
	//---

	// /**
	@Override
	public void cylinder(float w, float h) {
		// TODO Auto-generated method stub		
	}
	
	@Override
	public void hollowCylinder(int detail, float w, float h, Vector3D m, Vector3D n) {
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
		//pg().hint(ENABLE_STROKE_PERSPECTIVE);
	}

	@Override
	public void drawGrid(float size, int nbSubdivisions) {
		//pg().hint(DISABLE_STROKE_PERSPECTIVE);
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
		//pg().hint(ENABLE_STROKE_PERSPECTIVE);
	}
	
	@Override
	public void drawDottedGrid(float size, int nbSubdivisions) {
		float posi, posj;
		//pg().hint(DISABLE_STROKE_PERSPECTIVE);
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
		//pg().hint(ENABLE_STROKE_PERSPECTIVE);
	}	

	@Override
	public void drawCamera(Camera camera, boolean drawFarPlane, float scale) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawKFIViewport(float scale) {
		// TODO Auto-generated method stub
		
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
	public void drawPath(List<VFrame> path, int mask, int nbFrames, int nbSteps, float scale) {
		// TODO Auto-generated method stub
		
	}
	// */

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
	public void drawViewWindow(ViewWindow camera, float scale) {
		pg().pushMatrix();
		
		/**
		VFrame tmpFrame = new VFrame(scene.is3D());
		tmpFrame.fromMatrix(camera.frame().worldMatrix());
		scene().applyTransformation(tmpFrame);
		// */
		//Same as above, but easier ;)
		scene().applyTransformation(camera.frame());

		//upper left coordinates of the near corner
		PVector upperLeft = new PVector();
		
		pg().pushStyle();
		
		float[] wh = camera.getOrthoWidthHeight();
		upperLeft.x = scale * wh[0];
		upperLeft.y = scale * wh[1];
						
		pg().noStroke();		
		pg().beginShape(PApplet.QUADS);				
		pg().vertex(upperLeft.x, upperLeft.y);
		pg().vertex(-upperLeft.x, upperLeft.y);
		pg().vertex(-upperLeft.x, -upperLeft.y);
		pg().vertex(upperLeft.x, -upperLeft.y);		
		pg().endShape();

		// Up arrow
		float arrowHeight = 1.5f * upperLeft.y;
		float baseHeight = 1.2f * upperLeft.y;
		float arrowHalfWidth = 0.5f * upperLeft.x;
		float baseHalfWidth = 0.3f * upperLeft.x;
		
		// Base
		pg().beginShape(PApplet.QUADS);		
		pg().vertex(-baseHalfWidth, -upperLeft.y);
		pg().vertex(baseHalfWidth, -upperLeft.y);
		pg().vertex(baseHalfWidth, -baseHeight);
		pg().vertex(-baseHalfWidth, -baseHeight);		
		pg().endShape();

		// Arrow
		pg().beginShape(PApplet.TRIANGLES);		
		pg().vertex(0.0f, -arrowHeight);
		pg().vertex(-arrowHalfWidth, -baseHeight);
		pg().vertex(arrowHalfWidth, -baseHeight);		
		pg().endShape();
		
		pg().popStyle();
		pg().popMatrix();
	}			
}
