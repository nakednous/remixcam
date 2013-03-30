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

import processing.core.*;
/**
import remixlab.remixcam.core.*;
import remixlab.remixcam.geom.*;
*/
// /**

import remixlab.remixcam.core.Drawerable;
import remixlab.remixcam.geom.Matrix3D;
import remixlab.remixcam.renderers.MatrixRenderer;

public abstract class P5Renderer extends MatrixRenderer implements PConstants {
	PGraphics pg;
	Matrix3D proj;
	
	public P5Renderer(Scene scn, PGraphics renderer, Drawerable d) {
		super(scn, d);
		pg = renderer;
		proj = new Matrix3D();
	}
	
	/**
	public P5Renderer(Scene scn, PGraphics renderer) {
		super(scn, new Drawing2D(scn));
		pg = renderer;
		proj = new Matrix3D();
	}	
	*/
	
	public PGraphics pg() {
		return pg;
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
	public void printMatrix() {
		pg().printMatrix();
	}
	
	@Override
	public void printProjection() {
		pg().printProjection();
	}
	
	@Override
	public void frustum(float left, float right, float bottom, float top,	float znear, float zfar) {
		pg().frustum(left, right, bottom, top, znear, zfar);
	}
	
	@Override
	public void ortho(float left, float right, float bottom, float top,	float near, float far) {
		pg().ortho(left, right, bottom, top,	near, far);
	}

	@Override
	public void perspective(float fov, float aspect, float zNear, float zFar) {
		pg().perspective(fov, aspect, zNear, zFar);	
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
}
