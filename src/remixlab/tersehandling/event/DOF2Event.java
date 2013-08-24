/**
 *                  TerseHandling (version 0.70.0)
 *           Copyright (c) 2013 by Jean Pierre Charalambos
 *                 @author Jean Pierre Charalambos
 *             https://github.com/nakednous/tersehandling
 *           
 * This library provides classes to ease the creation of interactive scenes.
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

package remixlab.tersehandling.event;

import remixlab.util.Util;
import remixlab.util.gwthashcodeequals.EqualsBuilder;
import remixlab.util.gwthashcodeequals.HashCodeBuilder;

public class DOF2Event extends MotionEvent {
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).appendSuper(super.hashCode())
				.appendSuper(super.hashCode())
				.append(x)
				.append(dx)
				.append(y)
				.append(dy)
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

		DOF2Event other = (DOF2Event) obj;
		return new EqualsBuilder().appendSuper(super.equals(obj))
				.append(x, other.x)
				.append(dx, other.dx)
				.append(y, other.y)
				.append(dy, other.dy)
				.isEquals();
	}

	protected Float x, dx;
	protected Float y, dy;

	public DOF2Event(float x, float y, int modifiers, int button) {
		super(modifiers, button);
		this.x = x;
		this.dx = 0f;
		this.y = y;
		this.dy = 0f;
	}

	public DOF2Event(DOF2Event prevEvent, float x, float y, int modifiers, int button) {
		this(x, y, modifiers, button);
		setPreviousEvent(prevEvent);

		/**
		 * if(prevEvent!=null) { distance = Util.distance(x, y,
		 * prevEvent.getX(), prevEvent.getY()); if( sameSequence(prevEvent) ) {
		 * this.dx = this.getX() - prevEvent.getX(); this.dy = this.getY() -
		 * prevEvent.getY(); this.action = prevEvent.getAction(); } } //
		 */

		/**
		 * //TODO debug if( sameSequence(prevEvent) ) { this.dx = this.getX() -
		 * prevEvent.getX(); this.dy = this.getY() - prevEvent.getY();
		 * this.action = prevEvent.getAction();
		 * System.out.println("Current event: x: " + getX() + " y: " + getY());
		 * System.out.println("Prev event: x: " + getPrevX() + " y: " +
		 * getPrevY()); //System.out.println("Distance: " + distance());
		 * //System.out.println("Delay: " + delay());
		 * //System.out.println("Speed: " + speed()); } else {
		 * System.out.println("different sequence!"); } //
		 */
	}

	// ready to be enqueued
	public DOF2Event(float x, float y) {
		super();
		this.x = x;
		this.dx = 0f;
		this.y = y;
		this.dy = 0f;
		this.button = TH_NOBUTTON;
	}

	// idem
	public DOF2Event(DOF2Event prevEvent, float x, float y) {
		super();
		this.x = x;
		this.dx = 0f;
		this.y = y;
		this.dy = 0f;
		this.button = TH_NOBUTTON;
		setPreviousEvent(prevEvent);
	}

	protected DOF2Event(DOF2Event other) {
		super(other);
		this.x = new Float(other.x);
		this.dx = new Float(other.dx);
		this.y = new Float(other.y);
		this.dy = new Float(other.dy);
	}

	@Override
	public DOF2Event get() {
		return new DOF2Event(this);
	}

	@Override
	public void setPreviousEvent(MotionEvent prevEvent) {
		if (prevEvent != null)
			if (prevEvent instanceof DOF2Event) {
				rel = true;
				this.dx = this.getX() - ((DOF2Event) prevEvent).getX();
				this.dy = this.getY() - ((DOF2Event) prevEvent).getY();
				distance = Util.distance(x, y, ((DOF2Event) prevEvent).getX(), ((DOF2Event) prevEvent).getY());
				delay = this.timestamp() - prevEvent.timestamp();
				if (delay == 0)
					speed = distance;
				else
					speed = distance / (float) delay;
			} else {
				this.dx = 0f;
				this.dy = 0f;
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

	@Override
	public void modulate(float[] sens) {
		if (sens != null)
			if (sens.length >= 2 && this.absolute()) {
				x = x * sens[0];
				y = y * sens[1];
			}
	}

	@Override
	public boolean isNull() {
		if (relative() && Util.zero(getDX()) && Util.zero(getDY()))
			return true;
		if (absolute() && Util.zero(getX()) && Util.zero(getY()))
			return true;
		return false;
	}

	public DOF1Event dof1Event() {
		return dof1Event(true);
	}

	public DOF1Event dof1Event(boolean fromX) {
		DOF1Event pe1;
		DOF1Event e1;
		if (fromX) {
			if (relative()) {
				pe1 = new DOF1Event(getPrevX(), getModifiers(), getButton());				
				e1 = new DOF1Event(pe1, getX(), getModifiers(), getButton());
			} else {
				e1 = new DOF1Event(getX(), getModifiers(), getButton());
			}
		} else {
			if (relative()) {
				pe1 = new DOF1Event(getPrevY(), getModifiers(), getButton());
				e1 = new DOF1Event(pe1, getY(), getModifiers(), getButton());
			} else {
				e1 = new DOF1Event(getY(), getModifiers(), getButton());
			}
		}
		e1.timestamp = this.timestamp();
		e1.delay = this.delay();
		e1.speed = this.speed();
		e1.distance = this.distance();
		return e1;
	}
}
