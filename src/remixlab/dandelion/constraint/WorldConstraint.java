/**
 *                     Dandelion (version 0.70.0)      
 *          Copyright (c) 2013 by Jean Pierre Charalambos
 *                 @author Jean Pierre Charalambos      
 *              https://github.com/nakednous/remixcam
 *                           
 * This library provides classes to ease the creation of interactive
 * frame-based, 2d and 3d scenes.
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

package remixlab.dandelion.constraint;

import remixlab.dandelion.geom.*;

/**
 * An AxisPlaneConstraint defined in the world coordinate system.
 * <p>
 * The {@link #translationConstraintDirection()} and
 * {@link #rotationConstraintDirection()} are expressed in the world coordinate system.
 */
public class WorldConstraint extends AxisPlaneConstraint {
	/**
	 * Depending on {@link #translationConstraintType()}, {@code constrain}
	 * translation to be along an axis or limited to a plane defined in the
	 * world coordinate system by {@link #translationConstraintDirection()}.
	 */
	@Override
	public Vec constrainTranslation(Vec translation, GeomFrame frame) {
		Vec res = new Vec(translation.vec[0], translation.vec[1], translation.vec[2]);
		Vec proj;
		switch (translationConstraintType()) {
		case FREE:
			break;
		case PLANE:
			if (frame.referenceFrame() != null) {
				proj = frame.referenceFrame().transformOf(translationConstraintDirection());
				res = Vec.projectVectorOnPlane(translation, proj);
			} else
				res = Vec.projectVectorOnPlane(translation, translationConstraintDirection());
			break;
		case AXIS:
			if (frame.referenceFrame() != null) {
				proj = frame.referenceFrame().transformOf(translationConstraintDirection());
				res = Vec.projectVectorOnAxis(translation, proj);
			} else
				res = Vec.projectVectorOnAxis(translation, translationConstraintDirection());
			break;
		case FORBIDDEN:
			res = new Vec(0.0f, 0.0f, 0.0f);
			break;
		}
		return res;
	}

	/**
	 * When {@link #rotationConstraintType()} is of type AXIS, constrain {@code
	 * rotation} to be a rotation around an axis whose direction is defined in the
	 * Frame world coordinate system by {@link #rotationConstraintDirection()}.
	 */
	@Override
	public Orientable constrainRotation(Orientable rotation, GeomFrame frame) {
		Orientable res = rotation.get();
		switch (rotationConstraintType()) {
		case FREE:
			break;
		case PLANE:
			break;
		case AXIS:
			if (rotation instanceof Quat) {
				Vec quat = new Vec(((Quat)rotation).quat[0], ((Quat)rotation).quat[1], ((Quat)rotation).quat[2]);
				Vec axis = frame.transformOf(rotationConstraintDirection());
				quat = Vec.projectVectorOnAxis(quat, axis);
				res = new Quat(quat, 2.0f * (float) Math.acos(((Quat)rotation).quat[3]));
			}
			break;
		case FORBIDDEN:
			if (rotation instanceof Quat)
				res = new Quat(); // identity
			else
				res = new Rotation(); // identity
			break;
		}
		return res;
	}
}
