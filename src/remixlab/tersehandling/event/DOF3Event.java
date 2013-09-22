/*******************************************************************************
 * TerseHandling (version 0.9.50)
 * Copyright (c) 2013 Jean Pierre Charalambos.
 * @author Jean Pierre Charalambos
 * https://github.com/remixlab
 *   
 * All rights reserved. Library that eases the creation of interactive
 * scenes released under the terms of the GNU Public License v3.0
 * which is available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package remixlab.tersehandling.event;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

import remixlab.tersehandling.core.Util;

public class DOF3Event extends MotionEvent {
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).appendSuper(super.hashCode())
				.append(x)
				.append(dx)
				.append(y)
				.append(dy)
				.append(z)
				.append(dz)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (obj.getClass() != getClass())
			return false;

		DOF3Event other = (DOF3Event) obj;
		return new EqualsBuilder().appendSuper(super.equals(obj))
				.append(x, other.x)
				.append(dx, other.dx)
				.append(y, other.y)
				.append(dy, other.dy)
				.append(z, other.z)
				.append(dz, other.dz)
				.isEquals();
	}

	protected Float x, dx;
	protected Float y, dy;
	protected Float z, dz;

	public DOF3Event(float x, float y, float z, int modifiers, int button) {
		super(modifiers, button);
		this.x = x;
		this.dx = 0f;
		this.y = y;
		this.dy = 0f;
		this.z = z;
		this.dz = 0f;
	}

	public DOF3Event(DOF3Event prevEvent, float x, float y, float z, int modifiers, int button) {
		this(x, y, z, modifiers, button);
		setPreviousEvent(prevEvent);
		/**
		 * if(prevEvent!=null) { distance = Util.distance(x, y, z,
		 * prevEvent.getX(), prevEvent.getY(), prevEvent.getZ()); if(
		 * sameSequence(prevEvent) ) { this.dx = this.getX() - prevEvent.getX();
		 * this.dy = this.getY() - prevEvent.getY(); this.dz = this.getZ() -
		 * prevEvent.getZ(); this.action = prevEvent.getAction(); } }
		 */
	}

	// ready to be enqueued
	public DOF3Event(float x, float y, float z) {
		super();
		this.x = x;
		this.dx = 0f;
		this.y = y;
		this.dy = 0f;
		this.z = z;
		this.dz = 0f;
		this.button = TH_NOBUTTON;
	}

	// idem
	public DOF3Event(DOF3Event prevEvent, float x, float y, float z) {
		super();
		this.x = x;
		this.dx = 0f;
		this.y = y;
		this.dy = 0f;
		this.z = z;
		this.dz = 0f;
		this.button = TH_NOBUTTON;
		setPreviousEvent(prevEvent);
	}

	protected DOF3Event(DOF3Event other) {
		super(other);
		this.x = new Float(other.x);
		this.dx = new Float(other.dx);
		this.y = new Float(other.y);
		this.dy = new Float(other.dy);
		this.z = new Float(other.z);
		this.dz = new Float(other.z);
	}

	@Override
	public DOF3Event get() {
		return new DOF3Event(this);
	}

	@Override
	public void setPreviousEvent(MotionEvent prevEvent) {
		super.setPreviousEvent(prevEvent);
		if (prevEvent != null)
			if (prevEvent instanceof DOF3Event) {
				rel = true;
				this.dx = this.getX() - ((DOF3Event) prevEvent).getX();
				this.dy = this.getY() - ((DOF3Event) prevEvent).getY();
				this.dz = this.getZ() - ((DOF3Event) prevEvent).getZ();
				distance = Util.distance(x, y, z,
						((DOF3Event) prevEvent).getX(),
						((DOF3Event) prevEvent).getY(),
						((DOF3Event) prevEvent).getZ());
				delay = this.timestamp() - prevEvent.timestamp();
				if (delay == 0)
					speed = distance;
				else
					speed = distance / (float) delay;
			} else {
				this.dx = 0f;
				this.dy = 0f;
				this.dz = 0f;
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

	@Override
	public void modulate(float[] sens) {
		if (sens != null)
			if (sens.length >= 3 && this.absolute()) {
				x = x * sens[0];
				y = y * sens[1];
				z = z * sens[2];
			}
	}

	@Override
	public boolean isNull() {
		if (relative() && Util.zero(getDX()) && Util.zero(getDY()) && Util.zero(getDZ()))
			return true;
		if (absolute() && Util.zero(getX()) && Util.zero(getY()) && Util.zero(getZ()))
			return true;
		return false;
	}

	public DOF2Event dof2Event() {
		DOF2Event pe2;
		DOF2Event e2;
		if (relative()) {
			pe2 = new DOF2Event(getPrevX(), getPrevY(), getModifiers(),	getButton());
			e2 = new DOF2Event(pe2, getX(), getY(), getModifiers(), getButton());
		} else {
			e2 = new DOF2Event(getX(), getY(), getModifiers(), getButton());
		}
		e2.timestamp = this.timestamp();
		e2.delay = this.delay();
		e2.speed = this.speed();
		e2.distance = this.distance();
		return e2;
	}
}
