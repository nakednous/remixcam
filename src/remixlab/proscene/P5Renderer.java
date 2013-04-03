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
import processing.opengl.PGraphicsOpenGL;
/**
import remixlab.remixcam.core.*;
import remixlab.remixcam.geom.*;
*/
// /**

import remixlab.remixcam.core.Drawerable;
import remixlab.remixcam.geom.Matrix3D;
import remixlab.remixcam.renderers.BProjectionRenderer;

public abstract class P5Renderer extends BProjectionRenderer {
	PGraphicsOpenGL pg;
	Matrix3D proj;
	
	public P5Renderer(Scene scn, PGraphicsOpenGL renderer, Drawerable d) {
		super(scn, d);
		pg = renderer;
		proj = new Matrix3D();
	}
	
	public PGraphics pg() {
		return pg;
	}
	
	public PGraphicsOpenGL pggl() {
	  return (PGraphicsOpenGL) pg();	
	}	
	
	@Override
	public void pushProjection() {
		pggl().pushProjection();		
	}

	@Override
	public void popProjection() {
		pggl().popProjection();
	}

	@Override
	public void resetProjection() {
		pggl().resetProjection();
	}
	
	@Override
	public Matrix3D getProjection() {
		PMatrix3D pM = pggl().projection.get();
    return new Matrix3D(pM.get(new float[16]), true);// set it transposed
	}

	@Override
	public Matrix3D getProjection(Matrix3D target) {
		PMatrix3D pM = pggl().projection.get();
    target.setTransposed(pM.get(new float[16]));
    return target;
	}

	@Override
	public void applyProjection(Matrix3D source) {
		PMatrix3D pM = new PMatrix3D();
    pM.set(source.getTransposed(new float[16]));
    pggl().applyProjection(pM);		
	}

	@Override
	public void applyProjectionRowMajorOrder(float n00, float n01, float n02,
			float n03, float n10, float n11, float n12, float n13, float n20,
			float n21, float n22, float n23, float n30, float n31, float n32,
			float n33) {
		pggl().applyProjection(new PMatrix3D(n00, n01, n02, n03, n10, n11, n12, n13, n20, n21, n22, n23, n30, n31, n32, n33));
	}
}
