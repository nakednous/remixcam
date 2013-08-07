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
	public Vec cameraPosition();

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
	public void computeCameraPosition();
}
