/*******************************************************************************
 * dandelion (version 1.0.0-alpha.1)
 * Copyright (c) 2013 National University of Colombia, https://github.com/remixlab
 * @author Jean Pierre Charalambos, http://otrolado.info/
 *     
 * All rights reserved. Library that eases the creation of interactive
 * scenes, released under the terms of the GNU Public License v3.0
 * which is available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package remixlab.dandelion.constraint;

import remixlab.dandelion.core.ReferenceFrame;
import remixlab.dandelion.geom.*;

/**
 * An AxisPlaneConstraint defined in the Frame local coordinate system.
 * <p>
 * The {@link #translationConstraintDirection()} and
 * {@link #rotationConstraintDirection()} are expressed in the Frame local
 * coordinate system (see {@link remixlab.dandelion.core.ReferenceFrame#referenceFrame()}).
 */
public class LocalConstraint extends AxisPlaneConstraint {

	/**
	 * Depending on {@link #translationConstraintType()}, {@code constrain}
	 * translation to be along an axis or limited to a plane defined in the
	 * local coordinate system by {@link #translationConstraintDirection()}.
	 */
	@Override
	public Vec constrainTranslation(Vec translation, ReferenceFrame frame) {
		Vec res = new Vec(translation.vec[0], translation.vec[1], translation.vec[2]);
		Vec proj;
		switch (translationConstraintType()) {
		case FREE:
			break;
		case PLANE:
			proj = frame.rotation().rotate(translationConstraintDirection());
			//proj = frame.localInverseTransformOf(translationConstraintDirection());
			res = Vec.projectVectorOnPlane(translation, proj);
			break;
		case AXIS:
			proj = frame.rotation().rotate(translationConstraintDirection());
			//proj = frame.localInverseTransformOf(translationConstraintDirection());
			res = Vec.projectVectorOnAxis(translation, proj);
			break;			
		case FORBIDDEN:
			res = new Vec(0.0f, 0.0f, 0.0f);
			break;
		}
		return res;
	}

	/**
	 * When {@link #rotationConstraintType()} is of Type AXIS, constrain {@code
	 * rotation} to be a rotation around an axis whose direction is defined in the
	 * Frame local coordinate system by {@link #rotationConstraintDirection()}.
	 */
	@Override
	public Orientable constrainRotation(Orientable rotation, ReferenceFrame frame) {
		Orientable res = rotation.get();
		switch (rotationConstraintType()) {
		case FREE:
			break;
		case PLANE:
			break;
		case AXIS:
			if( rotation instanceof Quat) {
				Vec axis = rotationConstraintDirection();
				Vec quat = new Vec(((Quat)rotation).quat[0], ((Quat)rotation).quat[1], ((Quat)rotation).quat[2]);
				quat = Vec.projectVectorOnAxis(quat, axis);
				res = new Quat(quat, 2.0f * (float) Math.acos(((Quat)rotation).quat[3]));
			}
		break;
		case FORBIDDEN:
			if( rotation instanceof Quat)
				res = new Quat(); // identity
			else
				res = new Rot(); // identity
			break;
		}
		return res;
	}
}
