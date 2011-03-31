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

package remixlab.remixcam.constraint;

import remixlab.remixcam.core.*;
import remixlab.remixcam.geom.*;

/**
 * An interface class for Frame constraints.
 * <p>
 * This class defines the interface for the constraint that can be applied to a
 * Frame to limit its motion. Use
 * {@link remixlab.remixcam.core.GLFrame#setConstraint(Constraint)} to associate a
 * Constraint to a Frame (default is a {@code null}
 * {@link remixlab.remixcam.core.GLFrame#constraint()}.
 */
public class Constraint {
	/**
	 * Filters the translation applied to the Frame. This default implementation
	 * is empty (no filtering).
	 * <p>
	 * Overload this method in your own Constraint class to define a new
	 * translation constraint. {@code frame} is the Frame to which is applied the
	 * translation. Use its {@link remixlab.remixcam.core.GLFrame#position()} and update
	 * the translation accordingly instead.
	 * <p>
	 * {@code translation} is expressed in the local Frame coordinate system. Use
	 * {@link remixlab.remixcam.core.GLFrame#inverseTransformOf(Vector3D)} to express it
	 * in the world coordinate system if needed.
	 */
	public Vector3D constrainTranslation(Vector3D translation, GLFrame frame) {
		return new Vector3D(translation.x, translation.y, translation.z);
	}

	/**
	 * Filters the rotation applied to the {@code frame}. This default
	 * implementation is empty (no filtering).
	 * <p>
	 * Overload this method in your own Constraint class to define a new rotation
	 * constraint. See {@link #constrainTranslation(Vector3D, GLFrame)} for details.
	 * <p>
	 * Use {@link remixlab.remixcam.core.GLFrame#inverseTransformOf(Vector3D)} on the
	 * {@code rotation} {@link remixlab.remixcam.geom.Quaternion#axis()} to express
	 * {@code rotation} in the world coordinate system if needed.
	 */
	public Quaternion constrainRotation(Quaternion rotation, GLFrame frame) {
		return rotation.getCopy();
	}
}
