/**
 *                     RemixCam (version 0.70.0)      
 *      Copyright (c) 2013 by National University of Colombia
 *                 @author Jean Pierre Charalambos      
 *              https://github.com/nakednous/remixcam
 *                           
 * This java library provides classes to ease the creation of interactive 3D
 * scenes in various languages and frameworks such as JOGL, WebGL and Processing.
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

package remixlab.dandelion.geom;

import remixlab.tersehandling.core.Copyable;


public interface Orientable extends Copyable {	
	@Override
	public Orientable get();	
	public float angle();
	public void negate();
	public void compose(Orientable o);
	public Orientable inverse();
	public Vec rotate(Vec v);
	public Vec inverseRotate(Vec v);	
	public float[][] rotationMatrix();
	public float[][] inverseRotationMatrix();
	public Mat matrix();
	public Mat inverseMatrix();
	public void fromMatrix(Mat glMatrix);
	public void fromRotationMatrix(float[][] m);
	public float normalize();
	public void fromTo(Vec from, Vec to);
}
