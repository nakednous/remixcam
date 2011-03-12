/**
 *                     ProScene (version 1.0.1)      
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

import processing.core.*;
import remixlab.remixcam.core.*;
import remixlab.remixcam.geom.*;

/**
 * Utility class that implements some drawing methods used among proscene
 * classes.
 */
public class DrawingUtils implements PConstants {
	// needed for drawCamera
	static protected GLFrame tmpFrame = new GLFrame();

	// 1. SCENE
	
	/**
	 * Convenience wrapper function that simply calls {@code cylinder((PGraphics3D) parent.g, w, h)}.
	 * 
	 * @see #cylinder(PGraphics3D, float, float) 
	 */
	public static void cylinder(PApplet parent, float w, float h) {
		cylinder((PGraphics3D) parent.g, w, h);
	}
	
	/**
	 * Draws a cylinder of width {@code w} and height {@code h}, along the {@code
	 * p3d} positive {@code z} axis.
	 * <p>
	 * Code adapted from http://www.processingblogs.org/category/processing-java/
	 */
	public static void cylinder(PGraphics3D p3d, float w, float h) {
		float px, py;

		p3d.beginShape(QUAD_STRIP);
		for (float i = 0; i < 13; i++) {
			px = PApplet.cos(PApplet.radians(i * 30)) * w;
			py = PApplet.sin(PApplet.radians(i * 30)) * w;
			p3d.vertex(px, py, 0);
			p3d.vertex(px, py, h);
		}
		p3d.endShape();

		p3d.beginShape(TRIANGLE_FAN);
		p3d.vertex(0, 0, 0);
		for (float i = 12; i > -1; i--) {
			px = PApplet.cos(PApplet.radians(i * 30)) * w;
			py = PApplet.sin(PApplet.radians(i * 30)) * w;
			p3d.vertex(px, py, 0);
		}
		p3d.endShape();

		p3d.beginShape(TRIANGLE_FAN);
		p3d.vertex(0, 0, h);
		for (float i = 0; i < 13; i++) {
			px = PApplet.cos(PApplet.radians(i * 30)) * w;
			py = PApplet.sin(PApplet.radians(i * 30)) * w;
			p3d.vertex(px, py, h);
		}
		p3d.endShape();
	}

	/**
	 * Convenience wrapper function that simply calls {@code cone((PGraphics3D) parent.g, det, r, h)}.
	 * 
	 * @see #cone(PGraphics3D, int, float, float)
	 */
	public static void cone(PApplet parent, int det, float r, float h) {
		cone((PGraphics3D) parent.g, det, r, h);
	}	
	
	/**
	 * Same as {@code cone(p3d, det, 0, 0, r, h);}
	 * 
	 * @see #cone(PGraphics3D, int, float, float, float, float)
	 */
	public static void cone(PGraphics3D p3d, int det, float r, float h) {
		cone(p3d, det, 0, 0, r, h);
	}

	/**
	 * Convenience wrapper function that simply calls {@code cone((PGraphics3D) parent.g, r, h)}.
	 * 
	 * @see #cone(PGraphics3D, float, float)
	 */
	public static void cone(PApplet parent, float r, float h) {
		cone((PGraphics3D) parent.g, r, h);
	}		
	
	/**
	 * Same as {@code cone(p3d, 12, 0, 0, r, h);}
	 * 
	 * @see #cone(PGraphics3D, int, float, float, float, float)
	 */
	public static void cone(PGraphics3D p3d, float r, float h) {
		cone(p3d, 12, 0, 0, r, h);
	}

	/**
	 * Convenience wrapper function that simply calls {@code cone((PGraphics3D) parent.g, detail, x, y, r, h)}.
	 * 
	 * @see #cone(PGraphics3D, int, float, float, float, float)
	 */
	public static void cone(PApplet parent, int detail, float x, float y,	float r, float h) {
		cone((PGraphics3D) parent.g, detail, x, y, r, h);
	}			
	
	/**
	 * Draws a cone along the {@code p3d} positive {@code z} axis, with its
	 * base centered at {@code (x,y)}, height {@code h}, and radius {@code r}.
	 * <p>
	 * The code of this function was adapted from
	 * http://processinghacks.com/hacks:cone Thanks to Tom Carden.
	 * 
	 * @see #cone(PGraphics3D, int, float, float, float, float, float)
	 */
	public static void cone(PGraphics3D p3d, int detail, float x, float y, float r, float h) {
		float unitConeX[] = new float[detail + 1];
		float unitConeY[] = new float[detail + 1];

		for (int i = 0; i <= detail; i++) {
			float a1 = TWO_PI * i / detail;
			unitConeX[i] = r * (float) Math.cos(a1);
			unitConeY[i] = r * (float) Math.sin(a1);
		}

		p3d.pushMatrix();
		p3d.translate(x, y);
		p3d.beginShape(TRIANGLE_FAN);
		p3d.vertex(0, 0, h);
		for (int i = 0; i <= detail; i++) {
			p3d.vertex(unitConeX[i], unitConeY[i], 0.0f);
		}
		p3d.endShape();
		p3d.popMatrix();
	}

	/**
	 * Convenience wrapper function that simply calls
	 * {@code cone((PGraphics3D) parent.g, det, r1, r2, h)}.
	 * 
	 * @see #cone(PGraphics3D, int, float, float, float)
	 */
	public static void cone(PApplet parent, int det, float r1, float r2, float h) {
		cone((PGraphics3D) parent.g, det, r1, r2, h);
	}
	
	/**
	 * Same as {@code cone(p3d, det, 0, 0, r1, r2, h);}
	 * 
	 * @see #cone(PApplet, int, float, float, float, float, float)
	 */
	public static void cone(PGraphics3D p3d, int det, float r1, float r2, float h) {
		cone(p3d, det, 0, 0, r1, r2, h);
	}

	/**
	 * Convenience wrapper function that simply calls {@code cone((PGraphics3D) parent.g, r1, r2, h)}.
	 * 
	 * @see #cone(PGraphics3D, float, float, float)
	 */
	public static void cone(PApplet parent, float r1, float r2, float h) {
		cone((PGraphics3D) parent.g, r1, r2, h);
	}	
	
	/**
	 * Same as {@code cone(p3d, 18, 0, 0, r1, r2, h);}
	 * 
	 * @see #cone(PApplet, int, float, float, float, float, float)
	 */
	public static void cone(PGraphics3D p3d, float r1, float r2, float h) {
		cone(p3d, 18, 0, 0, r1, r2, h);
	}

	/**
	 * Convenience wrapper function that simply calls {@code cone((PGraphics3D) parent.g, detail, x, y, r, h)}.
	 * 
	 * @see #cone(PGraphics3D, int, float, float, float, float)
	 */
	public static void cone(PApplet parent, int detail, float x, float y,	float r1, float r2, float h) {
		cone((PGraphics3D) parent.g, detail, x, y, r1, r2, h);
	}		
	
	/**
	 * Draws a truncated cone along the {@code parent} positive {@code z} axis,
	 * with its base centered at {@code (x,y)}, height {@code h}, and radii
	 * {@code r1} and {@code r2} (basis and height respectively).
	 * 
	 * @see #cone(PApplet, int, float, float, float, float)
	 */
	public static void cone(PGraphics3D p3d, int detail, float x, float y,
			float r1, float r2, float h) {
		float firstCircleX[] = new float[detail + 1];
		float firstCircleY[] = new float[detail + 1];
		float secondCircleX[] = new float[detail + 1];
		float secondCircleY[] = new float[detail + 1];

		for (int i = 0; i <= detail; i++) {
			float a1 = TWO_PI * i / detail;
			firstCircleX[i] = r1 * (float) Math.cos(a1);
			firstCircleY[i] = r1 * (float) Math.sin(a1);
			secondCircleX[i] = r2 * (float) Math.cos(a1);
			secondCircleY[i] = r2 * (float) Math.sin(a1);
		}

		p3d.pushMatrix();
		p3d.translate(x, y);
		p3d.beginShape(QUAD_STRIP);
		for (int i = 0; i <= detail; i++) {
			p3d.vertex(firstCircleX[i], firstCircleY[i], 0);
			p3d.vertex(secondCircleX[i], secondCircleY[i], h);
		}
		p3d.endShape();
		p3d.popMatrix();
	}

	/**
	 * Convenience wrapper function that simply calls {@code drawAxis((PGraphics3D) parent.g, 100)}.
	 * 
	 * @see #drawAxis(PGraphics3D, float)
	 */
	public static void drawAxis(PApplet parent) {
		drawAxis((PGraphics3D) parent.g, 100);
	}	
	
	/**
	 * Convenience function that simply calls {@code drawAxis(p3d, 100)}
	 */
	public static void drawAxis(PGraphics3D p3d) {
		drawAxis(p3d, 100);
	}

	/**
	 * Convenience wrapper function that simply calls {@code drawAxis((PGraphics3D) parent.g, length)}.
	 * 
	 * @see #drawAxis(PGraphics3D, float)
	 */
	public static void drawAxis(PApplet parent, float length) {
		drawAxis((PGraphics3D) parent.g, length);
	}		
	
	/**
	 * Draws an axis of length {@code length} which origin correspond to the
	 * {@code parent}'s world coordinate system origin.
	 * 
	 * @see #drawGrid(PApplet, float, int)
	 */
	public static void drawAxis(PGraphics3D p3d, float length) {
		final float charWidth = length / 40.0f;
		final float charHeight = length / 30.0f;
		final float charShift = 1.04f * length;

		// p3d.noLights();

		p3d.pushStyle();
		
		p3d.beginShape(LINES);		
		p3d.strokeWeight(2);
		// The X
		p3d.stroke(255, 178, 178);
		p3d.vertex(charShift, charWidth, -charHeight);
		p3d.vertex(charShift, -charWidth, charHeight);
		p3d.vertex(charShift, -charWidth, -charHeight);
		p3d.vertex(charShift, charWidth, charHeight);
		// The Y
		p3d.stroke(178, 255, 178);
		p3d.vertex(charWidth, charShift, charHeight);
		p3d.vertex(0.0f, charShift, 0.0f);
		p3d.vertex(-charWidth, charShift, charHeight);
		p3d.vertex(0.0f, charShift, 0.0f);
		p3d.vertex(0.0f, charShift, 0.0f);
		p3d.vertex(0.0f, charShift, -charHeight);
		// The Z
		p3d.stroke(178, 178, 255);
		
		//left_handed
		p3d.vertex(-charWidth, -charHeight, charShift);
		p3d.vertex(charWidth, -charHeight, charShift);
		p3d.vertex(charWidth, -charHeight, charShift);
		p3d.vertex(-charWidth, charHeight, charShift);
		p3d.vertex(-charWidth, charHeight, charShift);
		p3d.vertex(charWidth, charHeight, charShift);
	  //right_handed coordinate system should go like this:
		//p3d.vertex(-charWidth, charHeight, charShift);
		//p3d.vertex(charWidth, charHeight, charShift);
		//p3d.vertex(charWidth, charHeight, charShift);
		//p3d.vertex(-charWidth, -charHeight, charShift);
		//p3d.vertex(-charWidth, -charHeight, charShift);
		//p3d.vertex(charWidth, -charHeight, charShift);
		
		p3d.endShape();

		// Z axis
		p3d.noStroke();
		p3d.fill(178, 178, 255);
		drawArrow(p3d, length, 0.01f * length);

		// X Axis
		p3d.fill(255, 178, 178);
		p3d.pushMatrix();
		p3d.rotateY(HALF_PI);
		drawArrow(p3d, length, 0.01f * length);
		p3d.popMatrix();

		// Y Axis
		p3d.fill(178, 255, 178);
		p3d.pushMatrix();
		p3d.rotateX(-HALF_PI);
		drawArrow(p3d, length, 0.01f * length);
		p3d.popMatrix();

		p3d.popStyle();
	}

	/**
	 * Convenience wrapper function that simply calls {@code drawArrow((PGraphics3D) parent.g, length)}.
	 * 
	 * @see #drawArrow(PGraphics3D, float)
	 */
	public static void drawArrow(PApplet parent, float length) {
		drawArrow((PGraphics3D) parent.g, length);
	}			
	
	/**
	 * Simply calls {@code drawArrow(p3d, length, 0.05f * length)}
	 * 
	 * @see #drawArrow(PApplet, float, float)
	 */
	public static void drawArrow(PGraphics3D p3d, float length) {
		drawArrow(p3d, length, 0.05f * length);
	}

	/**
	 * Convenience wrapper function that simply calls {@code drawArrow((PGraphics3D) parent.g, length, radius)}.
	 * 
	 * @see #drawArrow(PGraphics3D, float, float)
	 */
	public static void drawArrow(PApplet parent, float length, float radius) {
		drawArrow((PGraphics3D) parent.g, length, radius);
	}		
	
	/**
	 * Draws a 3D arrow along the {@code parent} positive Z axis.
	 * <p>
	 * {@code length} and {@code radius} define its geometry.
	 * <p>
	 * Use {@link #drawArrow(PApplet, PVector, PVector, float)} to place the arrow
	 * in 3D.
	 */
	public static void drawArrow(PGraphics3D p3d, float length, float radius) {
		float head = 2.5f * (radius / length) + 0.1f;
		float coneRadiusCoef = 4.0f - 5.0f * head;

		DrawingUtils.cylinder(p3d, radius, length
				* (1.0f - head / coneRadiusCoef));
		p3d.translate(0.0f, 0.0f, length * (1.0f - head));
		DrawingUtils.cone(p3d, coneRadiusCoef * radius, head * length);
		p3d.translate(0.0f, 0.0f, -length * (1.0f - head));
	}

	/**
	 * Convenience wrapper function that simply calls {@code drawArrow((PGraphics3D) parent.g, from, to, radius)}.
	 * 
	 * @see #drawArrow(PGraphics3D, PVector, PVector, float)
	 */
	public static void drawArrow(PApplet parent, PVector from, PVector to, float radius) {
		drawArrow((PGraphics3D) parent.g, from, to, radius);
	}		
	
	/**
	 * Draws a 3D arrow between the 3D point {@code from} and the 3D point {@code
	 * to}, both defined in the current {@code parent} ModelView coordinates
	 * system.
	 * 
	 * @see #drawArrow(PApplet, float, float)
	 */
	public static void drawArrow(PGraphics3D p3d, PVector from, PVector to,	float radius) {		
		p3d.pushMatrix();
		p3d.translate(from.x, from.y, from.z);
	  // TODO: fix data conversion in an stronger way:
		p3d.applyMatrix(MathUtils.fromMatrix3D(new Quaternion(new Vector3D(0, 0, 1), Vector3D.sub(new Vector3D(to.x, to.y, to.z), new Vector3D(from.x, from.y, from.z))).matrix()));
		drawArrow(p3d, PVector.sub(to, from).mag(), radius);
		p3d.popMatrix();
	}

	/**
	 * Convenience wrapper function that simply calls {@code drawGrid((PGraphics3D) parent.g)}.
	 * 
	 * @see #drawGrid(PGraphics3D)
	 */
	public static void drawGrid(PApplet parent) {
		drawGrid((PGraphics3D) parent.g);
	}			
	
	/**
	 * Convenience function that simply calls {@code drawGrid(p3d, 100, 10)}
	 * 
	 * @see #drawGrid(PApplet, float, int)
	 */
	public static void drawGrid(PGraphics3D p3d) {
		drawGrid(p3d, 100, 10);
	}

	/**
	 * Convenience wrapper function that simply calls {@code drawGrid((PGraphics3D) parent.g, size)}.
	 * 
	 * @see #drawGrid(PGraphics3D, float)
	 */
	public static void drawGrid(PApplet parent, float size) {
		drawGrid((PGraphics3D) parent.g, size);
	}	
	
	/**
	 * Convenience function that simply calls {@code drawGrid(p3d, size, 10)}
	 * 
	 * @see #drawGrid(PApplet, float, int)
	 */
	public static void drawGrid(PGraphics3D p3d, float size) {
		drawGrid(p3d, size, 10);
	}

	/**
	 * Convenience wrapper function that simply calls {@code drawGrid((PGraphics3D) parent.g, nbSubdivisions)}.
	 * 
	 * @see #drawGrid(PGraphics3D, int)
	 */
	public static void drawGrid(PApplet parent, int nbSubdivisions) {
		drawGrid((PGraphics3D) parent.g, nbSubdivisions);
	}
	
	/**
	 * Convenience function that simply calls {@code drawGrid(p3d, 100,
	 * nbSubdivisions)}
	 * 
	 * @see #drawGrid(PApplet, float, int)
	 */
	public static void drawGrid(PGraphics3D p3d, int nbSubdivisions) {
		drawGrid(p3d, 100, nbSubdivisions);
	}

	/**
	 * Convenience wrapper function that simply calls {@code drawGrid((PGraphics3D) parent.g, size, nbSubdivisions)}.
	 * 
	 * @see #drawGrid(PGraphics3D, float, int)
	 */
	public static void drawGrid(PApplet parent, float size, int nbSubdivisions) {
		drawGrid((PGraphics3D) parent.g, size, nbSubdivisions);
	}	
	
	/**
	 * Draws a grid in the XY plane, centered on (0,0,0) (defined in the current
	 * coordinate system).
	 * <p>
	 * {@code size} (processing scene units) and {@code nbSubdivisions} define its
	 * geometry.
	 * 
	 * @see #drawAxis(PApplet, float)
	 */
	public static void drawGrid(PGraphics3D p3d, float size, int nbSubdivisions) {
		p3d.pushStyle();
		p3d.stroke(170, 170, 170);
		p3d.strokeWeight(1);
		p3d.beginShape(LINES);
		for (int i = 0; i <= nbSubdivisions; ++i) {
			final float pos = size * (2.0f * i / nbSubdivisions - 1.0f);
			p3d.vertex(pos, -size);
			p3d.vertex(pos, +size);
			p3d.vertex(-size, pos);
			p3d.vertex(size, pos);
		}
		p3d.endShape();
		p3d.popStyle();
	}
	
	// 2. CAMERA
	
	/**
	 * Convenience wrapper function that simply calls {@code drawCamera((PGraphics3D) parent.g, camera, 170, true, 1.0f)}.
	 * 
	 * @see #drawCamera(PGraphics3D, Camera, int, boolean, float)
	 */
	public static void drawCamera(PApplet parent, Camera camera) {
		drawCamera((PGraphics3D) parent.g, camera, 170, true, 1.0f);
	}

	/**
	 * Convenience function that simply calls {@code drawCamera(p3d, camera,
	 * 170, true, 1.0f)}
	 * 
	 * @see #drawCamera(PGraphics3D p3d, Camera, int, boolean, float)
	 */
	public static void drawCamera(PGraphics3D p3d, Camera camera) {
		drawCamera(p3d, camera, 170, true, 1.0f);
	}
	
	/**
	 * Convenience wrapper function that simply calls {@code drawCamera((PGraphics3D) parent.g, camera, 170, true, scale)}.
	 * 
	 * @see #drawCamera(PGraphics3D, Camera, int, boolean, float)
	 */
	public static void drawCamera(PApplet parent, Camera camera, float scale) {
		drawCamera((PGraphics3D) parent.g, camera, 170, true, scale);
	}

	/**
	 * Convenience function that simply calls {@code drawCamera(p3d, camera,
	 * 170, true, scale)}
	 * 
	 * @see #drawCamera(PGraphics3D, Camera, int, boolean, float)
	 */
	public static void drawCamera(PGraphics3D p3d, Camera camera, float scale) {
		drawCamera(p3d, camera, 170, true, scale);
	}
	
	/**
	 * Convenience wrapper function that simply calls {@code drawCamera((PGraphics3D) parent.g, camera, color, true, 1.0f)}.
	 * 
	 * @see #drawCamera(PGraphics3D, Camera, int, boolean, float)
	 */
	public static void drawCamera(PApplet parent, Camera camera, int color) {
		drawCamera((PGraphics3D) parent.g, camera, color, true, 1.0f);
	}

	/**
	 * Convenience function that simply calls {@code drawCamera(p3d, camera,
	 * color, true, 1.0f)}
	 * 
	 * @see #drawCamera(PGraphics3D, Camera, int, boolean, float)
	 */
	public static void drawCamera(PGraphics3D p3d, Camera camera, int color) {
		drawCamera(p3d, camera, color, true, 1.0f);
	}
	
	/**
	 * Convenience wrapper function that simply calls {@code drawCamera((PGraphics3D) parent.g, camera, 170, drawFarPlane, 1.0f)}.
	 * 
	 * @see #drawCamera(PGraphics3D, Camera, int, boolean, float)
	 */
	public static void drawCamera(PApplet parent, Camera camera,	boolean drawFarPlane) {
		drawCamera((PGraphics3D) parent.g, camera, 170, drawFarPlane, 1.0f);
	}

	/**
	 * Convenience function that simply calls {@code drawCamera(p3d, camera,
	 * 170, drawFarPlane, 1.0f)}
	 * 
	 * @see #drawCamera(PGraphics3D, Camera, int, boolean, float)
	 */
	public static void drawCamera(PGraphics3D p3d, Camera camera,	boolean drawFarPlane) {
		drawCamera(p3d, camera, 170, drawFarPlane, 1.0f);
	}
	
	/**
	 * Convenience wrapper function that simply calls {@code drawCamera((PGraphics3D) parent.g, camera, 170, drawFarPlane, scale)}.
	 * 
	 * @see #drawCamera(PGraphics3D, Camera, int, boolean, float)
	 */
	public static void drawCamera(PApplet parent, Camera camera,	boolean drawFarPlane, float scale) {
		drawCamera((PGraphics3D) parent.g, camera, 170, drawFarPlane, scale);
	}

	/**
	 * Convenience function that simply calls {@code drawCamera(p3d, camera, 170, drawFarPlane, scale)}
	 * 
	 * @see #drawCamera(PGraphics3D, Camera, int, boolean, float)
	 */
	public static void drawCamera(PGraphics3D p3d, Camera camera,	boolean drawFarPlane, float scale) {
		drawCamera(p3d, camera, 170, drawFarPlane, scale);
	}
	
	/**
	 * Convenience wrapper function that simply calls {@code drawCamera((PGraphics3D) parent.g, camera, color, true, scale)}.
	 * 
	 * @see #drawCamera(PGraphics3D, Camera, int, boolean, float)
	 */
	public static void drawCamera(PApplet parent, Camera camera, int color,	float scale) {
		drawCamera((PGraphics3D) parent.g, camera, color, true, scale);
	}

	/**
	 * Convenience function that simply calls {@code drawCamera(p3d, camera, color, true, scale)}
	 * 
	 * @see #drawCamera(PGraphics3D, Camera, int, boolean, float)
	 */
	public static void drawCamera(PGraphics3D p3d, Camera camera, int color,	float scale) {
		drawCamera(p3d, camera, color, true, scale);
	}

	/**
	 * Convenience wrapper function that simply calls {@code drawCamera((PGraphics3D) parent.g, camera, color, drawFarPlane, 1.0f)}.
	 * 
	 * @see #drawCamera(PGraphics3D, Camera, int, boolean, float)
	 */
	public static void drawCamera(PApplet parent, Camera camera, int color,	boolean drawFarPlane) {
		drawCamera((PGraphics3D) parent.g, camera, color, drawFarPlane, 1.0f);
	}
	
	/**
	 * Convenience function that simply calls {@code drawCamera(parent, camera,
	 * color, drawFarPlane, 1.0f)}
	 * 
	 * @see #drawCamera(PGraphics3D, Camera, int, boolean, float)
	 */
	public static void drawCamera(PGraphics3D p3d, Camera camera, int color,	boolean drawFarPlane) {
		drawCamera(p3d, camera, color, drawFarPlane, 1.0f);
	}
	
	/**
	 * Convenience wrapper function that simply calls {@code drawCamera((PGraphics3D) parent.g, camera, color, drawFarPlane, scale)}.
	 * 
	 * @see #drawCamera(PGraphics3D, Camera, int, boolean, float)
	 */
	public static void drawCamera(PApplet parent, Camera camera, int color, boolean drawFarPlane, float scale) {
		drawCamera((PGraphics3D) parent.g, camera, color, drawFarPlane, scale);
	}

	/**
	 * Draws a representation of the {@code camera} in the {@code p3d} 3D
	 * virtual world using {@code color}.
	 * <p>
	 * The near and far planes are drawn as quads, the frustum is drawn using
	 * lines and the camera up vector is represented by an arrow to disambiguate
	 * the drawing.
	 * <p>
	 * When {@code drawFarPlane} is {@code false}, only the near plane is drawn.
	 * {@code scale} can be used to scale the drawing: a value of 1.0 (default)
	 * will draw the Camera's frustum at its actual size.
	 * <p>
	 * <b>Note:</b> The drawing of a Scene's own Scene.camera() should not be
	 * visible, but may create artifacts due to numerical imprecisions.
	 */
	public static void drawCamera(PGraphics3D p3d, Camera camera, int color, boolean drawFarPlane, float scale) {
		p3d.pushMatrix();

		// p3d.applyMatrix(camera.frame().worldMatrix());
		// same as the previous line, but maybe more efficient
		tmpFrame.fromMatrix(camera.frame().worldMatrix());
		//tmpFrame.applyTransformation(p3d);// TODO: fix me?		
		p3d.translate(tmpFrame.translation().x, tmpFrame.translation().y, tmpFrame.translation().z);
		p3d.rotate(tmpFrame.rotation().angle(), tmpFrame.rotation().axis().x, tmpFrame.rotation().axis().y, tmpFrame.rotation().axis().z);

		// 0 is the upper left coordinates of the near corner, 1 for the far one
		PVector[] points = new PVector[2];
		points[0] = new PVector();
		points[1] = new PVector();

		points[0].z = scale * camera.zNear();
		points[1].z = scale * camera.zFar();

		switch (camera.type()) {
		case PERSPECTIVE: {
			points[0].y = points[0].z * PApplet.tan(camera.fieldOfView() / 2.0f);
			points[0].x = points[0].y * camera.aspectRatio();
			float ratio = points[1].z / points[0].z;
			points[1].y = ratio * points[0].y;
			points[1].x = ratio * points[0].x;
			break;
		}
		case ORTHOGRAPHIC: {
			float[] wh = camera.getOrthoWidthHeight();
			points[0].x = points[1].x = scale * wh[0];
			points[0].y = points[1].y = scale * wh[1];
			break;
		}
		}

		int farIndex = drawFarPlane ? 1 : 0;

		// Near and (optionally) far plane(s)
		p3d.pushStyle();
		p3d.noStroke();
		p3d.fill(color);
		p3d.beginShape(PApplet.QUADS);
		for (int i = farIndex; i >= 0; --i) {
			p3d.normal(0.0f, 0.0f, (i == 0) ? 1.0f : -1.0f);
			p3d.vertex(points[i].x, points[i].y, -points[i].z);
			p3d.vertex(-points[i].x, points[i].y, -points[i].z);
			p3d.vertex(-points[i].x, -points[i].y, -points[i].z);
			p3d.vertex(points[i].x, -points[i].y, -points[i].z);
		}
		p3d.endShape();

		// Up arrow
		float arrowHeight = 1.5f * points[0].y;
		float baseHeight = 1.2f * points[0].y;
		float arrowHalfWidth = 0.5f * points[0].x;
		float baseHalfWidth = 0.3f * points[0].x;

		// p3d.noStroke();
		p3d.fill(color);
		// Base
		p3d.beginShape(PApplet.QUADS);
		
		p3d.vertex(-baseHalfWidth, -points[0].y, -points[0].z);
		p3d.vertex(baseHalfWidth, -points[0].y, -points[0].z);
		p3d.vertex(baseHalfWidth, -baseHeight, -points[0].z);
		p3d.vertex(-baseHalfWidth, -baseHeight, -points[0].z);
  	//right_handed coordinate system should go like this:
		//p3d.vertex(-baseHalfWidth, points[0].y, -points[0].z);
		//p3d.vertex(baseHalfWidth, points[0].y, -points[0].z);
		//p3d.vertex(baseHalfWidth, baseHeight, -points[0].z);
		//p3d.vertex(-baseHalfWidth, baseHeight, -points[0].z);
		
		p3d.endShape();

		// Arrow
		p3d.fill(color);
		p3d.beginShape(PApplet.TRIANGLES);
		
		p3d.vertex(0.0f, -arrowHeight, -points[0].z);
		p3d.vertex(-arrowHalfWidth, -baseHeight, -points[0].z);
		p3d.vertex(arrowHalfWidth, -baseHeight, -points[0].z);
  	//right_handed coordinate system should go like this:
		//p3d.vertex(0.0f, arrowHeight, -points[0].z);
		//p3d.vertex(-arrowHalfWidth, baseHeight, -points[0].z);
		//p3d.vertex(arrowHalfWidth, baseHeight, -points[0].z);
		
		p3d.endShape();

		// Frustum lines
		p3d.stroke(color);
		p3d.strokeWeight(2);
		switch (camera.type()) {
		case PERSPECTIVE:
			p3d.beginShape(PApplet.LINES);
			p3d.vertex(0.0f, 0.0f, 0.0f);
			p3d
					.vertex(points[farIndex].x, points[farIndex].y, -points[farIndex].z);
			p3d.vertex(0.0f, 0.0f, 0.0f);
			p3d.vertex(-points[farIndex].x, points[farIndex].y,
					-points[farIndex].z);
			p3d.vertex(0.0f, 0.0f, 0.0f);
			p3d.vertex(-points[farIndex].x, -points[farIndex].y,
					-points[farIndex].z);
			p3d.vertex(0.0f, 0.0f, 0.0f);
			p3d.vertex(points[farIndex].x, -points[farIndex].y,
					-points[farIndex].z);
			p3d.endShape();
			break;
		case ORTHOGRAPHIC:
			if (drawFarPlane) {
				p3d.beginShape(PApplet.LINES);
				p3d.vertex(points[0].x, points[0].y, -points[0].z);
				p3d.vertex(points[1].x, points[1].y, -points[1].z);
				p3d.vertex(-points[0].x, points[0].y, -points[0].z);
				p3d.vertex(-points[1].x, points[1].y, -points[1].z);
				p3d.vertex(-points[0].x, -points[0].y, -points[0].z);
				p3d.vertex(-points[1].x, -points[1].y, -points[1].z);
				p3d.vertex(points[0].x, -points[0].y, -points[0].z);
				p3d.vertex(points[1].x, -points[1].y, -points[1].z);
				p3d.endShape();
			}
		}

		p3d.popStyle();

		p3d.popMatrix();
	}

	// 3. KEYFRAMEINTERPOLATOR CAMERA
	
	public static void drawKFICamera(PApplet parent, float scale) {
		drawKFICamera(parent, 170, scale);
	}

	public static void drawKFICamera(PGraphics3D p3d, float scale) {
		drawKFICamera(p3d, 170, scale);
	}
	
	public static void drawKFICamera(PApplet parent, int color, float scale) {
		drawKFICamera((PGraphics3D) parent.g, color, scale);
	}

	public static void drawKFICamera(PGraphics3D p3d, int color, float scale) {
		float halfHeight = scale * 0.07f;
		float halfWidth = halfHeight * 1.3f;
		float dist = halfHeight / PApplet.tan(PApplet.PI / 8.0f);

		float arrowHeight = 1.5f * halfHeight;
		float baseHeight = 1.2f * halfHeight;
		float arrowHalfWidth = 0.5f * halfWidth;
		float baseHalfWidth = 0.3f * halfWidth;

		// Frustum outline
		p3d.pushStyle();

		p3d.noFill();
		p3d.stroke(color);
		p3d.beginShape();
		p3d.vertex(-halfWidth, halfHeight, -dist);
		p3d.vertex(-halfWidth, -halfHeight, -dist);
		p3d.vertex(0.0f, 0.0f, 0.0f);
		p3d.vertex(halfWidth, -halfHeight, -dist);
		p3d.vertex(-halfWidth, -halfHeight, -dist);
		p3d.endShape();
		p3d.noFill();
		p3d.beginShape();
		p3d.vertex(halfWidth, -halfHeight, -dist);
		p3d.vertex(halfWidth, halfHeight, -dist);
		p3d.vertex(0.0f, 0.0f, 0.0f);
		p3d.vertex(-halfWidth, halfHeight, -dist);
		p3d.vertex(halfWidth, halfHeight, -dist);
		p3d.endShape();

		// Up arrow
		p3d.noStroke();
		p3d.fill(color);
		// Base
		p3d.beginShape(PApplet.QUADS);
		
		p3d.vertex(baseHalfWidth, -halfHeight, -dist);
		p3d.vertex(-baseHalfWidth, -halfHeight, -dist);
		p3d.vertex(-baseHalfWidth, -baseHeight, -dist);
		p3d.vertex(baseHalfWidth, -baseHeight, -dist);
  	//right_handed coordinate system should go like this:
		//p3d.vertex(-baseHalfWidth, halfHeight, -dist);
		//p3d.vertex(baseHalfWidth, halfHeight, -dist);
		//p3d.vertex(baseHalfWidth, baseHeight, -dist);
		//p3d.vertex(-baseHalfWidth, baseHeight, -dist);
		
		p3d.endShape();
		// Arrow
		p3d.beginShape(PApplet.TRIANGLES);
		
		p3d.vertex(0.0f, -arrowHeight, -dist);
		p3d.vertex(arrowHalfWidth, -baseHeight, -dist);
		p3d.vertex(-arrowHalfWidth, -baseHeight, -dist);
	  //right_handed coordinate system should go like this:
		//p3d.vertex(0.0f, arrowHeight, -dist);
		//p3d.vertex(-arrowHalfWidth, baseHeight, -dist);
		//p3d.vertex(arrowHalfWidth, baseHeight, -dist);
		
		p3d.endShape();

		p3d.popStyle();
	}
}
