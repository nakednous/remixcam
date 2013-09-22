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

public class DOF1Event extends MotionEvent {
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).appendSuper(super.hashCode())
				.append(x)
				.append(dx)
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

		DOF1Event other = (DOF1Event) obj;
		return new EqualsBuilder().appendSuper(super.equals(obj))
				.append(x, other.x)
				.append(dx, other.dx)
				.isEquals();
	}

	protected Float x, dx;

	public DOF1Event(float x, int modifiers, int button) {
		super(modifiers, button);
		this.x = x;
		this.dx = 0f;
	}

	public DOF1Event(DOF1Event prevEvent, float x, int modifiers, int button) {
		this(x, modifiers, button);
		setPreviousEvent(prevEvent);
		/**
		 * if(prevEvent!=null) { distance = this.getX() - prevEvent.getX(); if
		 * (sameSequence(prevEvent)) { this.action = prevEvent.getAction();
		 * this.dx = this.getX() - prevEvent.getX(); } }
		 */
	}

	// ready to be enqueued
	public DOF1Event(float x) {
		super();
		this.x = x;
		this.dx = 0f;
		this.button = TH_NOBUTTON;
	}

	// idem
	public DOF1Event(DOF1Event prevEvent, float x) {
		super();
		this.x = x;
		this.dx = 0f;
		this.button = TH_NOBUTTON;
		setPreviousEvent(prevEvent);
	}

	// ---

	protected DOF1Event(DOF1Event other) {
		super(other);
		this.x = new Float(other.x);
		this.dx = new Float(other.dx);
	}

	@Override
	public DOF1Event get() {
		return new DOF1Event(this);
	}

	@Override
	public void setPreviousEvent(MotionEvent prevEvent) {
		if (prevEvent != null)
			if (prevEvent instanceof DOF1Event) {
				rel = true;
				this.dx = this.getX() - ((DOF1Event) prevEvent).getX();
				distance = this.getX() - ((DOF1Event) prevEvent).getX();
				delay = this.timestamp() - prevEvent.timestamp();
				if (delay == 0)
					speed = distance;
				else
					speed = distance / (float) delay;
			} else {
				this.dx = 0f;
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

	@Override
	public void modulate(float[] sens) {
		if (sens != null)
			if (sens.length >= 1 && this.absolute())
				x = x * sens[0];
	}

	@Override
	public boolean isNull() {
		if (relative() && Util.zero(getDX()))
			return true;
		if (absolute() && Util.zero(getX()))
			return true;
		return false;
	}
}
