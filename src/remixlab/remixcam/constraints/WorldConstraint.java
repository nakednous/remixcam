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

import remixlab.remixcam.core.*;
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
	 * translation to be along an axis or limited to a plane defined in the Frame
	 * world coordinate system by {@link #translationConstraintDirection()}.
	 */
	@Override
	public Vector3D constrainTranslation(Vector3D translation, SimpleFrame frame) {
		Vector3D res = new Vector3D(translation.x, translation.y, translation.z);
		Vector3D proj;
		switch (translationConstraintType()) {
		case FREE:
			break;
		case PLANE:
			if (frame.referenceFrame() != null) {
				proj = frame.referenceFrame().transformOf(translationConstraintDirection());
				res = Vector3D.projectVectorOnPlane(translation, proj);
			} else
				res = Vector3D.projectVectorOnPlane(translation,	translationConstraintDirection());
			break;
		case AXIS:
			if (frame.referenceFrame() != null) {
				proj = frame.referenceFrame().transformOf(translationConstraintDirection());
				res = Vector3D.projectVectorOnAxis(translation, proj);
			} else
				res = Vector3D.projectVectorOnAxis(translation, translationConstraintDirection());
			break;
		case FORBIDDEN:
			res = new Vector3D(0.0f, 0.0f, 0.0f);
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
	public Quaternion constrainRotation(Quaternion rotation, SimpleFrame frame) {
		Quaternion res = rotation.get();
		switch (rotationConstraintType()) {
		case FREE:
			break;
		case PLANE:
			break;
		case AXIS: {
			Vector3D quat = new Vector3D(rotation.quat[0], rotation.quat[1], rotation.quat[2]);
			Vector3D axis = frame.transformOf(rotationConstraintDirection());
			quat = Vector3D.projectVectorOnAxis(quat, axis);
			res = new Quaternion(quat, 2.0f * (float) Math.acos(rotation.quat[3]));
			break;
		}
		case FORBIDDEN:
			res = new Quaternion(); // identity
			break;
		}
		return res;
	}
}
