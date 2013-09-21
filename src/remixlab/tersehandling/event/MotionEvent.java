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

import remixlab.tersehandling.event.shortcut.*;

// /**
public class MotionEvent extends TerseEvent {
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).appendSuper(super.hashCode())
				.append(button)
				.append(delay)
				.append(distance)
				.append(speed)
				.append(rel)
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

		MotionEvent other = (MotionEvent) obj;
		return new EqualsBuilder().appendSuper(super.equals(obj))
				.append(button, other.button)
				.append(delay, other.delay)
				.append(distance, other.distance)
				.append(speed, other.speed)
				.append(rel, other.rel)
				.isEquals();
	}

	protected Integer button;
	protected boolean rel;

	// defaulting to zero:
	// http://stackoverflow.com/questions/3426843/what-is-the-default-initialization-of-an-array-in-java
	protected long delay;
	protected float distance, speed;

	public MotionEvent() {
		super();
		this.button = TH_NOBUTTON;
	}

	public MotionEvent(int modifiers) {
		super(modifiers);
		this.button = TH_NOBUTTON;
	}

	public MotionEvent(int modifiers, int button) {
		super(modifiers);
		this.button = button;
	}

	// ---

	protected MotionEvent(MotionEvent other) {
		super(other);
		this.button = new Integer(other.button);
		this.delay = other.delay;
		this.distance = other.distance;
		this.speed = other.speed;
		this.rel = other.rel;
	}

	@Override
	public MotionEvent get() {
		return new MotionEvent(this);
	}

	public void modulate(float[] sens) {
	}

	public int getButton() {
		return button;
	}

	@Override
	public ButtonShortcut shortcut() {
		return new ButtonShortcut(getModifiers(), getButton());
	}

	public long delay() {
		return delay;
	}

	public float distance() {
		return distance;
	}

	public float speed() {
		return speed;
	}

	public boolean relative() {
		// return distance() != 0;
		return rel;
	}

	public boolean absolute() {
		return !relative();
	}

	public void setPreviousEvent(MotionEvent prevEvent) {
		if (prevEvent == null) {
			delay = 0;
			speed = 0;
			distance = 0;
		} else {
			rel = true;
			delay = this.timestamp() - prevEvent.timestamp();
			if (delay == 0)
				speed = distance;
			else
				speed = distance / (float) delay;
		}
	}

	// --

	/**
	 * protected boolean sameSequence(GenericMotionEvent<?> prevEvent) { boolean
	 * result = false; long tThreshold = 5000; float dThreshold = 50; delay =
	 * this.timestamp() - prevEvent.timestamp();
	 * 
	 * if(delay==0) speed = distance; else speed = distance / (float)delay;
	 * 
	 * //if(prevEvent != null) if( prevEvent.shortcut().equals(this.shortcut())
	 * ) if( ( distance <= dThreshold) && ( delay <= tThreshold ) ) { result =
	 * true; } else { delay = 0L; speed = 0f; distance = 0f; } return result; }
	 * //
	 */
}
