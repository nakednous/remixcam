/*******************************************************************************
 * Dandelion (version 0.9.50)
 * Copyright (c) 2013 Jean Pierre Charalambos.
 * @author Jean Pierre Charalambos
 * https://github.com/remixlab
 * 
 * All rights reserved. Library that eases the creation of interactive
 * scenes released under the terms of the GNU Public License v3.0
 * which available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package remixlab.dandelion.constraint;

import remixlab.dandelion.geom.*;
import remixlab.tersehandling.core.Util;

/**
 * An interface class for Frame constraints.
 * <p>
 * This class defines the interface for the constraint that can be applied to a
 * Frame to limit its motion. Use
 * {@link remixlab.dandelion.geom.RefFrame#setConstraint(Constraint)} to associate a
 * Constraint to a Frame (default is a {@code null}
 * {@link remixlab.dandelion.geom.RefFrame#constraint()}.
 */
public abstract class Constraint {
	private Vec sclConstraintValues = new Vec(1,1,1);
	
	/**
	 * Filters the translation applied to the Frame. This default implementation
	 * is empty (no filtering).
	 * <p>
	 * Overload this method in your own Constraint class to define a new
	 * translation constraint. {@code frame} is the Frame to which is applied the
	 * translation. You should refrain from directly changing its value in the
	 * constraint. Use its {@link remixlab.dandelion.geom.RefFrame#position()} and update
	 * the translation accordingly instead.
	 * <p>
	 * {@code translation} is expressed in the local Frame coordinate system. Use
	 * {@link remixlab.dandelion.geom.RefFrame#inverseTransformOf(Vec)} to express it
	 * in the world coordinate system if needed.
	 */
	public Vec constrainTranslation(Vec translation, RefFrame frame) {
		return new Vec(translation.vec[0], translation.vec[1], translation.vec[2]);
	}

	/**
	 * Filters the rotation applied to the {@code frame}. This default
	 * implementation is empty (no filtering).
	 * <p>
	 * Overload this method in your own Constraint class to define a new rotation
	 * constraint. See {@link #constrainTranslation(Vec, RefFrame)} for details.
	 * <p>
	 * Use {@link remixlab.dandelion.geom.RefFrame#inverseTransformOf(Vec)} on the
	 * {@code rotation} {@link remixlab.dandelion.geom.Quat#axis()} to express
	 * {@code rotation} in the world coordinate system if needed.
	 */
	public Orientable constrainRotation(Orientable rotation, RefFrame frame) {
		return rotation.get();
	}
	
	public Vec scalingConstraintValues() {
		return sclConstraintValues;
	}
	
	public void setScalingConstraintValues(float x, float y) {
		setScalingConstraintValues(new Vec(x,y,1));
	}
	
	public void setScalingConstraintValues(float x, float y, float z) {
		setScalingConstraintValues(new Vec(x,y,z));
	}
	
	public void setScalingConstraintValues(Vec values) {
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
	 * constraint. Use its {@link remixlab.dandelion.geom.RefFrame#position()} and update
	 * the translation accordingly instead.
	 * <p>
	 * {@code scaling} is expressed in the local Frame coordinate system.
	 */	
	public Vec constrainScaling(Vec scaling, RefFrame frame) {
		Vec res = new Vec(scaling.x(), scaling.y(), scaling.z());		
		// special case
		if( Util.zero(res.x()) ) res.x(1);
		if( Util.zero(res.y()) ) res.y(1);
		if( Util.zero(res.z()) ) res.z(1);
		
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
