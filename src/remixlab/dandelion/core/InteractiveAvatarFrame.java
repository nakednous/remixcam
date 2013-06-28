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

package remixlab.dandelion.core;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

import remixlab.dandelion.geom.*;
import remixlab.tersehandling.core.Copyable;

/**
 * The InteractiveAvatarFrame class represents an InteractiveDrivableFrame that
 * can be tracked by a Camera, i.e., it implements the Trackable interface.
 * <p>
 * The {@link #cameraPosition()} of the camera that is to be tracking the frame
 * (see the documentation of the Trackable interface) is defined in spherical
 * coordinates ({@link #azimuth()}, {@link #inclination()} and
 * {@link #trackingDistance()}) respect to the {@link #position()} (which
 * defines its {@link #target()}) of the InteractiveAvatarFrame.
 */
public class InteractiveAvatarFrame extends InteractiveDrivableFrame implements	Constants, Trackable, Copyable {
	@Override
	public int hashCode() {
    return new HashCodeBuilder(17, 37).
    appendSuper(super.hashCode()).
		append(q).
		append(trackingDist).
		append(camRelPos).
    toHashCode();
	}

	@Override
	public boolean equals(Object obj) {		
		if (obj == null) return false;
		if (obj == this) return true;		
		if (obj.getClass() != getClass()) return false;
		
		InteractiveAvatarFrame other = (InteractiveAvatarFrame) obj;
	  return new EqualsBuilder()
    .appendSuper(super.equals(obj))		
		.append(q, other.q)
		.append(trackingDist, other.trackingDist)
		.append(camRelPos, other.camRelPos)
		.isEquals();
	}
	
	private Quat q;
	private float trackingDist;
	private Vec camRelPos;

	/**
	 * Constructs an InteractiveAvatarFrame and sets its
	 * {@link #trackingDistance()} to {@link remixlab.dandelion.core.AbstractScene#radius()}/5,
	 * {@link #azimuth()} to 0, and {@link #inclination()} to 0.
	 * 
	 * @see remixlab.dandelion.core.AbstractScene#setAvatar(Trackable)
	 * @see remixlab.dandelion.core.AbstractScene#setInteractiveFrame(InteractiveFrame)
	 */
	public InteractiveAvatarFrame(AbstractScene scn) {
		super(scn);
		q = new Quat();
		q.fromTaitBryan(QUARTER_PI, 0, 0);
		camRelPos = new Vec();
		setTrackingDistance(scene.radius() / 5);
	}
	
	/**
	 * Copy constructor.
	 * 
	 * @param otherFrame the other interactive avatar frame
	 */
	protected InteractiveAvatarFrame(InteractiveAvatarFrame otherFrame) {
		super(otherFrame);
		this.q = otherFrame.q.get();
		this.camRelPos = new Vec();
		this.camRelPos.set( otherFrame.camRelPos );
		this.setTrackingDistance(otherFrame.trackingDistance());
	}
	
	/**
	 * Calls {@link #InteractiveAvatarFrame(InteractiveAvatarFrame)} (which is protected)
	 * and returns a copy of {@code this} object.
	 * 
	 * @see #InteractiveAvatarFrame(InteractiveAvatarFrame)
	 */
	public InteractiveAvatarFrame get() {
		return new InteractiveAvatarFrame(this);
	}

	/**
	 * Returns the distance between the frame and the tracking camera.
	 */
	public float trackingDistance() {
		return trackingDist;
	}

	/**
	 * Sets the distance between the frame and the tracking camera.
	 */
	public void setTrackingDistance(float d) {
		trackingDist = d;
		computeCameraPosition();
	}

	/**
	 * Returns the azimuth of the tracking camera measured respect to the frame's
	 * {@link #zAxis()}.
	 */
	public float azimuth() {
		// azimuth <-> pitch
		return q.taitBryanAngles().vec[1];
	}

	/**
	 * Sets the {@link #azimuth()} of the tracking camera.
	 */
	public void setAzimuth(float a) {
		float roll = q.taitBryanAngles().vec[0];
		q.fromTaitBryan(roll, a, 0);
		computeCameraPosition();
	}

	/**
	 * Returns the inclination of the tracking camera measured respect to the
	 * frame's {@link #yAxis()}.
	 */
	public float inclination() {
		// inclination <-> roll
		return q.taitBryanAngles().vec[0];
	}

	/**
	 * Sets the {@link #inclination()} of the tracking camera.
	 */
	public void setInclination(float i) {
		float pitch = q.taitBryanAngles().vec[1];
		q.fromTaitBryan(i, pitch, 0);
		computeCameraPosition();
	}

	// Interface implementation

	/**
	 * Overloading of {@link remixlab.dandelion.core.Trackable#cameraPosition()}.
	 * Returns the world coordinates of the camera position computed in
	 * {@link #computeCameraPosition()}.
	 */
	public Vec cameraPosition() {
		return inverseCoordinatesOf(camRelPos);
	}

	/**
	 * Overloading of {@link remixlab.dandelion.core.Trackable#upVector()}. Simply
	 * returns the frame {@link #yAxis()}.
	 */
	public Vec upVector() {
		return yAxis();
	}

	/**
	 * Overloading of {@link remixlab.dandelion.core.Trackable#target()}. Simply returns
	 * the frame {@link #position()}.
	 */
	public Vec target() {
		return position();
	}

	/**
	 * Overloading of {@link remixlab.dandelion.core.Trackable#computeCameraPosition()}.
	 * <p>
	 * The {@link #cameraPosition()} of the camera that is to be tracking the
	 * frame (see the documentation of the Trackable interface) is defined in
	 * spherical coordinates by means of the {@link #azimuth()}, the
	 * {@link #inclination()} and {@link #trackingDistance()}) respect to the
	 * {@link #position()}.
	 */
	public void computeCameraPosition() {
		camRelPos = q.rotate(new Vec(0, 0, 1));
		camRelPos.mult(trackingDistance());
	}
}
