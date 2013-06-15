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
	public DLVector constrainTranslation(DLVector translation, GeomFrame frame) {
		DLVector res = new DLVector(translation.vec[0], translation.vec[1], translation.vec[2]);
		DLVector proj;
		switch (translationConstraintType()) {
		case FREE:
			break;
		case PLANE:
			if (frame.referenceFrame() != null) {
				proj = frame.referenceFrame().transformOf(translationConstraintDirection());
				res = DLVector.projectVectorOnPlane(translation, proj);
			} else
				res = DLVector.projectVectorOnPlane(translation, translationConstraintDirection());
			break;
		case AXIS:
			if (frame.referenceFrame() != null) {
				proj = frame.referenceFrame().transformOf(translationConstraintDirection());
				res = DLVector.projectVectorOnAxis(translation, proj);
			} else
				res = DLVector.projectVectorOnAxis(translation, translationConstraintDirection());
			break;
		case FORBIDDEN:
			res = new DLVector(0.0f, 0.0f, 0.0f);
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
			if (rotation instanceof Quaternion) {
				DLVector quat = new DLVector(((Quaternion)rotation).quat[0], ((Quaternion)rotation).quat[1], ((Quaternion)rotation).quat[2]);
				DLVector axis = frame.transformOf(rotationConstraintDirection());
				quat = DLVector.projectVectorOnAxis(quat, axis);
				res = new Quaternion(quat, 2.0f * (float) Math.acos(((Quaternion)rotation).quat[3]));
			}
			break;
		case FORBIDDEN:
			if (rotation instanceof Quaternion)
				res = new Quaternion(); // identity
			else
				res = new Rotation(); // identity
			break;
		}
		return res;
	}
}
