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

import remixlab.dandelion.core.*;
import remixlab.dandelion.geom.*;

/**
 * An AxisPlaneConstraint defined in the camera coordinate system.
 * <p>
 * The {@link #translationConstraintDirection()} and
 * {@link #rotationConstraintDirection()} are expressed in the associated
 * {@link #camera()} coordinate system.
 */
public class CameraConstraint extends AxisPlaneConstraint {

	private Viewport camera;

	/**
	 * Creates a CameraConstraint, whose constrained directions are defined in the
	 * {@link #camera()} coordinate system.
	 */
	public CameraConstraint(Viewport cam) {
		super();
		camera = cam;
	}

	/**
	 * Returns the associated Camera. Set using the CameraConstraint constructor.
	 */
	public Viewport camera() {
		return camera;
	}

	/**
	 * Depending on {@link #translationConstraintType()}, {@code constrain}
	 * translation to be along an axis or limited to a plane defined in the
	 * {@link #camera()} coordinate system by
	 * {@link #translationConstraintDirection()}.
	 */
	@Override
	public Vec constrainTranslation(Vec translation, RefFrame frame) {
		Vec res = new Vec(translation.vec[0], translation.vec[1], translation.vec[2]);
		Vec proj;
		switch (translationConstraintType()) {
		case FREE:
			break;
		case PLANE:
			proj = camera().frame().inverseTransformOf(translationConstraintDirection());
			if (frame.referenceFrame() != null)
				proj = frame.referenceFrame().transformOf(proj);
			res = Vec.projectVectorOnPlane(translation, proj);
			break;
		case AXIS:
			proj = camera().frame().inverseTransformOf(translationConstraintDirection());
			if (frame.referenceFrame() != null)
				proj = frame.referenceFrame().transformOf(proj);
			res = Vec.projectVectorOnAxis(translation, proj);
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
	 * {@link #camera()} coordinate system by
	 * {@link #rotationConstraintDirection()}.
	 */
	@Override
	public Orientable constrainRotation(Orientable rotation, RefFrame frame) {
		Orientable res = rotation.get();
		switch (rotationConstraintType()) {
		case FREE:
			break;
		case PLANE:
			break;
		case AXIS:
			if(rotation instanceof Quat) {
				Vec axis = frame.transformOf(camera().frame().inverseTransformOf(rotationConstraintDirection()));
				Vec quat = new Vec(((Quat)rotation).quat[0], ((Quat)rotation).quat[1], ((Quat)rotation).quat[2]);
				quat = Vec.projectVectorOnAxis(quat, axis);
				res = new Quat(quat, 2.0f * (float) Math.acos(((Quat)rotation).quat[3]));
			}
			break;
		case FORBIDDEN:
			if(rotation instanceof Quat)
				res = new Quat(); // identity
			else
				res = new Rot(); // identity
			break;
		}
		return res;
	}
}
