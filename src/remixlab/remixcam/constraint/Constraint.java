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

package remixlab.remixcam.constraint;

import remixlab.remixcam.geom.*;

/**
 * An interface class for Frame constraints.
 * <p>
 * This class defines the interface for the constraint that can be applied to a
 * Frame to limit its motion. Use
 * {@link remixlab.remixcam.geom.GeomFrame#setConstraint(Constraint)} to associate a
 * Constraint to a Frame (default is a {@code null}
 * {@link remixlab.remixcam.geom.GeomFrame#constraint()}.
 */
public abstract class Constraint {
	private DLVector sclConstraintValues = new DLVector(1,1,1);
	
	/**
	 * Filters the translation applied to the Frame. This default implementation
	 * is empty (no filtering).
	 * <p>
	 * Overload this method in your own Constraint class to define a new
	 * translation constraint. {@code frame} is the Frame to which is applied the
	 * translation. You should refrain from directly changing its value in the
	 * constraint. Use its {@link remixlab.remixcam.geom.GeomFrame#position()} and update
	 * the translation accordingly instead.
	 * <p>
	 * {@code translation} is expressed in the local Frame coordinate system. Use
	 * {@link remixlab.remixcam.geom.GeomFrame#inverseTransformOf(DLVector)} to express it
	 * in the world coordinate system if needed.
	 */
	public DLVector constrainTranslation(DLVector translation, GeomFrame frame) {
		return new DLVector(translation.vec[0], translation.vec[1], translation.vec[2]);
	}

	/**
	 * Filters the rotation applied to the {@code frame}. This default
	 * implementation is empty (no filtering).
	 * <p>
	 * Overload this method in your own Constraint class to define a new rotation
	 * constraint. See {@link #constrainTranslation(DLVector, GeomFrame)} for details.
	 * <p>
	 * Use {@link remixlab.remixcam.geom.GeomFrame#inverseTransformOf(DLVector)} on the
	 * {@code rotation} {@link remixlab.remixcam.geom.Quaternion#axis()} to express
	 * {@code rotation} in the world coordinate system if needed.
	 */
	public Orientable constrainRotation(Orientable rotation, GeomFrame frame) {
		return rotation.get();
	}
	
	public DLVector scalingConstraintValues() {
		return sclConstraintValues;
	}
	
	public void setScalingConstraintValues(float x, float y) {
		setScalingConstraintValues(new DLVector(x,y,1));
	}
	
	public void setScalingConstraintValues(float x, float y, float z) {
		setScalingConstraintValues(new DLVector(x,y,z));
	}
	
	public void setScalingConstraintValues(DLVector values) {
		sclConstraintValues.set(Math.abs(values.x()), Math.abs(values.y()), Math.abs(values.z()));
		float min = Math.min(Math.max(values.x(), values.y()), values.z());
		if( min != 0 )
			sclConstraintValues.div(min);
	}
	
	/**
	 * Filters the scaling applied to the Frame. This default implementation
	 * is empty (no filtering).
	 * <p>
	 * Overload this method in your own Constraint class to define a new
	 * translation constraint. {@code frame} is the Frame to which is applied the
	 * scaling. You should refrain from directly changing its value in the
	 * constraint. Use its {@link remixlab.remixcam.geom.GeomFrame#position()} and update
	 * the translation accordingly instead.
	 * <p>
	 * {@code scaling} is expressed in the local Frame coordinate system.
	 */	
	public DLVector constrainScaling(DLVector scaling, GeomFrame frame) {
		DLVector res = new DLVector(scaling.x(), scaling.y(), scaling.z());		
		// special case
		if( Geom.zero(res.x()) ) res.x(1);
		if( Geom.zero(res.y()) ) res.y(1);
		if( Geom.zero(res.z()) ) res.z(1);
		
		//sclConstraintValues is of the shape (0:1:-1, 0:1:-1, 0:1:-1)
		//forbids scaling		
		
		if( sclConstraintValues.x() == 0 ) res.x(1);
		if( sclConstraintValues.y() == 0 ) res.y(1);
		if( sclConstraintValues.z() == 0 ) res.z(1);			
		
		if( sclConstraintValues.x() == 1 ) {
			if( sclConstraintValues.y() != 0 && sclConstraintValues.y() != 1 ) res.y(scaling.x() * sclConstraintValues.y());
			if( sclConstraintValues.z() != 0 && sclConstraintValues.z() != 1 ) res.z(scaling.x() * sclConstraintValues.z());
		}
		else
			if( sclConstraintValues.y() == 1 ) {
				if( sclConstraintValues.x() != 0 && sclConstraintValues.x() != 1 ) res.x(scaling.y() * sclConstraintValues.x());
				if( sclConstraintValues.z() != 0 && sclConstraintValues.z() != 1 ) res.z(scaling.y() * sclConstraintValues.z());
			}
			else
				if( sclConstraintValues.z() == 1 ) {
					if( sclConstraintValues.x() != 0 && sclConstraintValues.x() != 1 ) res.x(scaling.z() * sclConstraintValues.x());
					if( sclConstraintValues.y() != 0 && sclConstraintValues.y() != 1 ) res.y(scaling.z() * sclConstraintValues.y());
				}
		return res;
	}
}
