/**
 *                     RemixCam (version 1.0.0)      
 *      Copyright (c) 2012 by National University of Colombia
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

package remixlab.remixcam.constraints;

import remixlab.remixcam.geom.*;

/**
 * An abstract class for Frame constraints defined by an axis or a plane.
 * <p>
 * AxisPlaneConstraint is an interface for (translation and/or rotation)
 * Constraint that are defined by a direction.
 * {@link #translationConstraintType()} and {@link #rotationConstraintType()}
 * define how this direction should be interpreted: as an axis or as a plane
 * normal.
 * <p>
 * The three implementations of this class: LocalConstraint, WorldConstraint and
 * CameraConstraint differ by the coordinate system in which this direction is
 * expressed.
 */

public abstract class AxisPlaneConstraint extends Constraint {
	/**
	 * Type lists the different types of translation and rotation constraints that
	 * are available.
	 * <p>
	 * It specifies the meaning of the constraint direction (see
	 * {@link #translationConstraintDirection()} and
	 * {@link #rotationConstraintDirection()}): as an axis direction or a plane
	 * normal. {@link Type#FREE} means no constraint while {@link Type#FORBIDDEN}
	 * completely forbids the translation and/or the rotation. <b>Attention: </b>
	 * The {@link Type#PLANE} Type is not valid for rotational constraint.
	 * <p>
	 * New derived classes can use their own extended {@code enum} for specific
	 * constraints.
	 */
	public enum Type {
		FREE, AXIS, PLANE, FORBIDDEN
	};

	private Type transConstraintType;
	private Type rotConstraintType;
	private Type sclConstraintType;
	private Vector3D transConstraintDir;
	private Vector3D rotConstraintDir;
	private Vector3D sclConstraintDir;

	/**
	 * 
	 * Default constructor.
	 * <p>
	 * {@link #translationConstraintType()} and {@link #rotationConstraintType()}
	 * are set to {@link Type#FREE}. {@link #translationConstraintDirection()} and
	 * {@link #rotationConstraintDirection()} are set to (0,0,0).
	 */
	public AxisPlaneConstraint() {
		// Do not use set since setRotationConstraintType needs a read.
		this.transConstraintType = AxisPlaneConstraint.Type.FREE;
		this.rotConstraintType = AxisPlaneConstraint.Type.FREE;
		this.sclConstraintType = AxisPlaneConstraint.Type.FREE;
		transConstraintDir = new Vector3D(0.0f, 0.0f, 0.0f);
		rotConstraintDir = new Vector3D(0.0f, 0.0f, 0.0f);
		sclConstraintDir = new Vector3D(0.0f, 0.0f, 0.0f);
	}

	/**
	 * Returns the translation constraint Type.
	 * <p>
	 * Depending on this value, the Frame will freely translate ({@link Type#FREE}
	 * ), will only be able to translate along an axis direction (
	 * {@link Type#AXIS}), will be forced to stay into a plane ({@link Type#PLANE}
	 * ) or will not able to translate at all ({@link Type#FORBIDDEN}).
	 * <p>
	 * Use {@link remixlab.remixcam.geom.VFrame#setPosition(Vector3D)} to define the
	 * position of the constrained Frame before it gets constrained.
	 */
	public Type translationConstraintType() {
		return transConstraintType;
	}

	/**
	 * 
	 * Returns the direction used by the translation constraint.
	 * <p>
	 * It represents the axis direction ({@link Type#AXIS}) or the plane normal (
	 * {@link Type#PLANE}) depending on the {@link #translationConstraintType()}.
	 * It is undefined for ({@link Type#FREE}) or ({@link Type#FORBIDDEN}).
	 * <p>
	 * The AxisPlaneConstraint derived classes express this direction in different
	 * coordinate system (camera for CameraConstraint, local for LocalConstraint,
	 * and world for WorldConstraint). This value can be modified with
	 * {@link #setRotationConstraintDirection(Vector3D)}.
	 */
	public Vector3D translationConstraintDirection() {
		return transConstraintDir;
	}

	/**
	 * Returns the rotation constraint Type.
	 */
	public Type rotationConstraintType() {
		return rotConstraintType;
	}

	/**
	 * Returns the axis direction used by the rotation constraint.
	 * <p>
	 * This direction is defined only when {@link #rotationConstraintType()} is
	 * {@link Type#AXIS}.
	 * <p>
	 * The AxisPlaneConstraint derived classes express this direction in different
	 * coordinate system (camera for CameraConstraint, local for LocalConstraint,
	 * and world for WorldConstraint). This value can be modified with
	 * {@link #setRotationConstraintDirection(Vector3D)}.
	 */
	public Vector3D rotationConstraintDirection() {
		return rotConstraintDir;
	}
	
	public Type scalingConstraintType() {
		return sclConstraintType;
	}
	
	public Vector3D scalingConstraintDirection() {
		return sclConstraintDir;
	}

	/**
	 * Simply calls {@link #setTranslationConstraintType(Type)} and
	 * {@link #setTranslationConstraintDirection(Vector3D)}.
	 */
	public void setTranslationConstraint(Type type, Vector3D direction) {
		setTranslationConstraintType(type);
		setTranslationConstraintDirection(direction);
	}

	/**
	 * Defines the {@link #translationConstraintDirection()}. The coordinate
	 * system where {@code direction} is expressed depends on your class
	 * implementation.
	 */
	public void setTranslationConstraintDirection(Vector3D direction) {
		if ((translationConstraintType() != AxisPlaneConstraint.Type.FREE) && (translationConstraintType() != AxisPlaneConstraint.Type.FORBIDDEN)) {
			float norm = direction.mag();
			if (Geom.zero(norm)) {
				System.out.println("Warning: AxisPlaneConstraint.setTranslationConstraintDir: null vector for translation constraint");
				transConstraintType = AxisPlaneConstraint.Type.FREE;
			} else
				transConstraintDir = Vector3D.mult(direction, (1.0f / norm));
		}
	}

	/**
	 * Simply calls {@link #setRotationConstraintType(Type)} and
	 * {@link #setRotationConstraintDirection(Vector3D)}.
	 */
	public void setRotationConstraint(Type type, Vector3D direction) {
		setRotationConstraintType(type);
		setRotationConstraintDirection(direction);
	}

	/**
	 * Defines the {@link #rotationConstraintDirection()}. The coordinate system
	 * where {@code direction} is expressed depends on your class implementation.
	 */
	public void setRotationConstraintDirection(Vector3D direction) {
		if ((rotationConstraintType() != AxisPlaneConstraint.Type.FREE)	&& (rotationConstraintType() != AxisPlaneConstraint.Type.FORBIDDEN)) {
			float norm = direction.mag();
			if (Geom.zero(norm)) {
				System.out.println("Warning: AxisPlaneConstraint.setRotationConstraintDir: null vector for rotation constraint");
				rotConstraintType = AxisPlaneConstraint.Type.FREE;
			} else
				rotConstraintDir = Vector3D.mult(direction, (1.0f / norm));
		}
	}
	
	public void setScalingConstraint(Type type, Vector3D direction) {
		setScalingConstraintType(type);
		setScalingConstraintDirection(direction);
	}
	
	public void setScalingConstraintDirection(Vector3D direction) {
		if ((scalingConstraintType() != AxisPlaneConstraint.Type.FREE)	&& (scalingConstraintType() != AxisPlaneConstraint.Type.FORBIDDEN)) {
			float norm = direction.mag();
			if (Geom.zero(norm)) {
				System.out.println("Warning: AxisPlaneConstraint.setScalingConstraintDir: null vector for rotation constraint");
				sclConstraintType = AxisPlaneConstraint.Type.FREE;
			} else
				sclConstraintDir = Vector3D.mult(direction, (1.0f / norm));
		}
	}

	/**
	 * Sets the Type() of the {@link #translationConstraintType()}. Default is
	 * {@link Type#FREE}
	 */
	public void setTranslationConstraintType(Type type) {
		transConstraintType = type;
	}

	/**
	 * Set the Type of the {@link #rotationConstraintType()}. Default is
	 * {@link Type#FREE}.
	 * <p>
	 * Depending on this value, the Frame will freely rotate ({@link Type#FREE}),
	 * will only be able to rotate around an axis ({@link Type#AXIS}), or will not
	 * able to rotate at all {@link Type#FORBIDDEN}.
	 * <p>
	 * Use {@link remixlab.remixcam.geom.VFrame#setOrientation(Quaternion)} to define
	 * the orientation of the constrained Frame before it gets constrained.
	 * <p>
	 * <b>Attention:</b> An {@link Type#PLANE} Type is not meaningful for
	 * rotational constraints and will be ignored.
	 */
	public void setRotationConstraintType(Type type) {
		if (rotationConstraintType() == AxisPlaneConstraint.Type.PLANE) {
			System.out.println("Warning: AxisPlaneConstraint.setRotationConstraintType: the PLANE type cannot be used for a rotation constraints");
			return;
		}

		rotConstraintType = type;
	}
	
	public void setScalingConstraintType(Type type) {
		sclConstraintType = type;
	}
}
