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
package remixlab.dandelion.core;

import remixlab.dandelion.geom.*;

/**
 * Interface for objects that are to be tracked by a proscene Camera when its
 * mode is THIRD_PERSON.
 * <p>
 * <h3>How does it work ?</h3>
 * All objects that are to be tracked by the
 * {@link remixlab.dandelion.core.AbstractScene#viewport()} (known as avatars) should implement
 * this interface. To setup an avatar you should then call
 * {@link remixlab.dandelion.core.AbstractScene#setAvatar(Trackable)}. The avatar will be
 * tracked by the {@link remixlab.dandelion.core.AbstractScene#viewport()} when the camera
 * is in Third Person mode.
 */

public interface Trackable {
	/**
	 * Returns the position of the tracking Camera in the world coordinate system.
	 * 
	 * @return Vector3D holding the camera position defined in the world coordinate
	 *         system.
	 */
	public Vec viewportPosition();

	/**
	 * Returns the vector to be set as the
	 * {@link remixlab.dandelion.core.Camera#upVector()}.
	 * 
	 * @return Vector3D holding the camera up-vector defined in the world
	 *         coordinate system.
	 */
	public Vec upVector();

	/**
	 * Returns the target point to be set as the
	 * {@link remixlab.dandelion.core.Camera#lookAt(Vec)}.
	 * 
	 * @return Vector3D holding the camera look-at vector defined in the world
	 *         coordinate system.
	 */
	public Vec target();

	/**
	 * Computes the camera position according to some specific InteractiveFrame
	 * parameters which depends on the type of interaction that is to be
	 * implemented.
	 * <p>
	 * It is responsibility of the object implementing this interface to update
	 * the camera position by properly calling this method.
	 */
	public void computeViewportPosition();
}
