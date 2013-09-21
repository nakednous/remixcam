/*******************************************************************************
 * TerseHandling (version 0.9.50)
 * Copyright (c) 2013 Jean Pierre Charalambos.
 * @author Jean Pierre Charalambos
 * https://github.com/remixlab
 * 
 * All rights reserved. Library that eases the creation of interactive
 * scenes released under the terms of the GNU Public License v3.0
 * which available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package remixlab.tersehandling.event;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

import remixlab.tersehandling.core.Util;

public class DOF6Event extends MotionEvent {
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).appendSuper(super.hashCode())
				.append(x)
				.append(dx)
				.append(y)
				.append(dy)
				.append(z)
				.append(dz)
				.append(rx)
				.append(drx)
				.append(ry)
				.append(dry)
				.append(rz)
				.append(drz).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (obj.getClass() != getClass())
			return false;

		DOF6Event other = (DOF6Event) obj;
		return new EqualsBuilder().appendSuper(super.equals(obj))
				.append(x, other.x)
				.append(dx, other.dx)
				.append(y, other.y)
				.append(dy, other.dy)
				.append(z, other.z)
				.append(dz, other.dz)
				.append(rx, other.rx)
				.append(drx, other.drx)
				.append(ry, other.ry)
				.append(dry, other.dry)
				.append(rz, other.rz)
				.append(drz, other.drz).isEquals();
	}

	protected Float x, dx;
	protected Float y, dy;
	protected Float z, dz;

	protected Float rx, drx;
	protected Float ry, dry;
	protected Float rz, drz;

	public DOF6Event(float x, float y, float z, float rx, float ry, float rz, int modifiers, int button) {
		super(modifiers, button);
		this.x = x;
		this.dx = 0f;
		this.y = y;
		this.dy = 0f;
		this.z = z;
		this.dz = 0f;
		this.rx = rx;
		this.drx = 0f;
		this.ry = ry;
		this.dry = 0f;
		this.rz = rz;
		this.drz = 0f;
	}

	public DOF6Event(DOF6Event prevEvent,
			         float x, float y, float z, float rx,
			         float ry, float rz, int modifiers, int button) {
		this(x, y, z, rx, ry, rz, modifiers, button);
		setPreviousEvent(prevEvent);
		/**
		 * if(prevEvent!=null) { distance = Util.distance(x, y, z, rx, ry, rz,
		 * prevEvent.getX(), prevEvent.getY(), prevEvent.getZ(),
		 * prevEvent.getRX(), prevEvent.getRY(), prevEvent.getRZ()); if(
		 * sameSequence(prevEvent) ) { this.dx = this.getX() - prevEvent.getX();
		 * this.dy = this.getY() - prevEvent.getY(); this.dz = this.getZ() -
		 * prevEvent.getZ(); this.drx = this.getRX() - prevEvent.getRX();
		 * this.dry = this.getRY() - prevEvent.getRY(); this.drz = this.getRZ()
		 * - prevEvent.getRZ(); this.action = prevEvent.getAction(); } }
		 */
	}

	// ready to be enqueued
	public DOF6Event(float x, float y, float z, float rx, float ry, float rz) {
		super();
		this.x = x;
		this.dx = 0f;
		this.y = y;
		this.dy = 0f;
		this.z = z;
		this.dz = 0f;
		this.rx = rx;
		this.drx = 0f;
		this.ry = ry;
		this.dry = 0f;
		this.rz = rz;
		this.drz = 0f;
		this.button = TH_NOBUTTON;
	}

	// idem
	public DOF6Event(DOF6Event prevEvent, float x, float y, float z, float rx,	float ry, float rz) {
		super();
		this.x = x;
		this.dx = 0f;
		this.y = y;
		this.dy = 0f;
		this.z = z;
		this.dz = 0f;
		this.rx = rx;
		this.drx = 0f;
		this.ry = ry;
		this.dry = 0f;
		this.rz = rz;
		this.drz = 0f;
		this.button = TH_NOBUTTON;
		setPreviousEvent(prevEvent);
		/**
		 * if(prevEvent!=null) { distance = Util.distance(x, y, z, rx, ry, rz,
		 * prevEvent.getX(), prevEvent.getY(), prevEvent.getZ(),
		 * prevEvent.getRX(), prevEvent.getRY(), prevEvent.getRZ()); if(
		 * sameSequence(prevEvent) ) { this.dx = this.getX() - prevEvent.getX();
		 * this.dy = this.getY() - prevEvent.getY(); this.dz = this.getZ() -
		 * prevEvent.getZ(); this.drx = this.getRX() - prevEvent.getRX();
		 * this.dry = this.getRY() - prevEvent.getRY(); this.drz = this.getRZ()
		 * - prevEvent.getRZ(); } }
		 */
	}

	protected DOF6Event(DOF6Event other) {
		super(other);
		this.x = new Float(other.x);
		this.dx = new Float(other.dx);
		this.y = new Float(other.y);
		this.dy = new Float(other.dy);
		this.z = new Float(other.z);
		this.dz = new Float(other.z);
		this.rx = new Float(other.rx);
		this.drx = new Float(other.drx);
		this.ry = new Float(other.ry);
		this.dry = new Float(other.dry);
		this.rz = new Float(other.rz);
		this.drz = new Float(other.drz);
	}

	@Override
	public DOF6Event get() {
		return new DOF6Event(this);
	}

	@Override
	public void setPreviousEvent(MotionEvent prevEvent) {
		super.setPreviousEvent(prevEvent);
		if (prevEvent != null)
			if (prevEvent instanceof DOF6Event) {
				rel = true;
				this.dx = this.getX() - ((DOF6Event) prevEvent).getX();
				this.dy = this.getY() - ((DOF6Event) prevEvent).getY();
				this.dz = this.getZ() - ((DOF6Event) prevEvent).getZ();
				this.drx = this.getRX() - ((DOF6Event) prevEvent).getRX();
				this.dry = this.getRY() - ((DOF6Event) prevEvent).getRY();
				this.drz = this.getRZ() - ((DOF6Event) prevEvent).getRZ();
				distance = Util.distance(x, y, z, rx, ry, rz,
						((DOF6Event) prevEvent).getX(),
						((DOF6Event) prevEvent).getY(),
						((DOF6Event) prevEvent).getZ(),
						((DOF6Event) prevEvent).getRX(),
						((DOF6Event) prevEvent).getRY(),
						((DOF6Event) prevEvent).getRZ());
				delay = this.timestamp() - prevEvent.timestamp();
				if (delay == 0)
					speed = distance;
				else
					speed = distance / (float) delay;
			} else {
				this.dx = 0f;
				this.dy = 0f;
				this.dz = 0f;
				this.drx = 0f;
				this.dry = 0f;
				this.drz = 0f;
				delay = 0;
				speed = 0;
				distance = 0;
			}
	}

	public float getX() {
		return x;
	}

	public float getDX() {
		return dx;
	}

	public float getPrevX() {
		return getX() - getDX();
	}

	public float getY() {
		return y;
	}

	public float getDY() {
		return dy;
	}

	public float getPrevY() {
		return getY() - getDY();
	}

	public float getZ() {
		return z;
	}

	public float getDZ() {
		return dz;
	}

	public float getPrevZ() {
		return getZ() - getDZ();
	}

	public float roll() {
		return getRX();
	}

	public float getRX() {
		return rx;
	}

	public float pitch() {
		return getRY();
	}

	public float getRY() {
		return ry;
	}

	public float yaw() {
		return getRZ();
	}

	public float getRZ() {
		return rz;
	}

	public float getDRX() {
		return drx;
	}

	public float getDRY() {
		return dry;
	}

	public float getDRZ() {
		return drz;
	}

	public float getPrevRX() {
		return getRX() - getDRX();
	}

	public float getPrevRY() {
		return getRY() - getDRY();
	}

	public float getPrevRZ() {
		return getRZ() - getDRZ();
	}

	@Override
	public void modulate(float[] sens) {
		if (sens != null)
			if (sens.length >= 6 && this.absolute()) {
				x = x * sens[0];
				y = y * sens[1];
				z = z * sens[2];
				rx = rx * sens[3];
				ry = ry * sens[4];
				rz = rz * sens[5];
			}
	}

	@Override
	public boolean isNull() {
		if (relative() && Util.zero(getDX()) && Util.zero(getDY())
				&& Util.zero(getDZ()) && Util.zero(getDRX())
				&& Util.zero(getDRY()) && Util.zero(getDRZ()))
			return true;
		if (absolute() && Util.zero(getX()) && Util.zero(getY())
				&& Util.zero(getZ()) && Util.zero(getRX())
				&& Util.zero(getRY()) && Util.zero(getRZ()))
			return true;
		return false;
	}

	public DOF3Event dof3Event() {
		return dof3Event(true);
	}

	public DOF3Event dof3Event(boolean fromTranslation) {
		DOF3Event pe3;
		DOF3Event e3;
		if (relative()) {
			if (fromTranslation) {
				pe3 = new DOF3Event(getPrevX(), getPrevY(), getPrevZ(),	getModifiers(), getButton());
				e3 = new DOF3Event(pe3, getX(), getY(), getZ(), getModifiers(),	getButton());
			} else {
				pe3 = new DOF3Event(getPrevRX(), getPrevRY(), getPrevRZ(), getModifiers(), getButton());
				e3 = new DOF3Event(pe3, getRX(), getRY(), getRZ(), getModifiers(), getButton());
			}
		} else {
			if (fromTranslation) {
				e3 = new DOF3Event(getX(), getY(), getZ(), getModifiers(), getButton());
			}
			else {
				e3 = new DOF3Event(getRX(), getRY(), getRZ(), getModifiers(), getButton());
			}
		}
		e3.timestamp = this.timestamp();
		e3.delay = this.delay();
		e3.speed = this.speed();
		e3.distance = this.distance();
		return e3;
	}
}
