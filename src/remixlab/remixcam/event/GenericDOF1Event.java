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

package remixlab.remixcam.event;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

import remixlab.remixcam.core.Actionable;
import remixlab.remixcam.geom.Geom;

public class GenericDOF1Event<A extends Actionable<?>> extends GenericMotionEvent<A> {
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).
				appendSuper(super.hashCode()).
				append(x).
				append(dx).
				toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (obj.getClass() != getClass())
			return false;

		GenericDOF1Event<?> other = (GenericDOF1Event<?>) obj;
		return new EqualsBuilder()
		.appendSuper(super.equals(obj))
		.append(x, other.x)
		.append(dx, other.dx)
		.isEquals();
	}

	protected Float x, dx;

	public GenericDOF1Event(float x, int modifiers, int button) {
		super(modifiers, button);
		this.x = x;
		this.dx = 0f;
	}

	public GenericDOF1Event(GenericDOF1Event<A> prevEvent, float x, int modifiers, int button) {
		this(x, modifiers, button);
		if(prevEvent!=null) {
			distance = this.getX() - prevEvent.getX();
			if (sameSequence(prevEvent)) {
				this.action = prevEvent.getAction();
				this.dx = this.getX() - prevEvent.getX();
			}
		}
	}

	// ready to be enqueued
	public GenericDOF1Event(float x, A a) {
		super(a);
		this.x = x;
		this.dx = 0f;
		this.button = NOBUTTON;
	}

	// idem
	public GenericDOF1Event(GenericDOF1Event<A> prevEvent, float x, A a) {
		super(a);		
		this.x = x;
		this.dx = 0f;
		this.button = NOBUTTON;
		if(prevEvent!=null) {
			distance = this.getX() - prevEvent.getX();
			if (sameSequence(prevEvent))
				this.dx = this.getX() - prevEvent.getX();
		}
	}

	// ---

	protected GenericDOF1Event(GenericDOF1Event<A> other) {
		super(other);
		this.x = new Float(other.x);
		this.dx = new Float(other.dx);
	}

	@Override
	public GenericDOF1Event<A> get() {
		return new GenericDOF1Event<A>(this);
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
	public void modulate(float [] sens) {
		if(sens != null)
		if(sens.length>=1 && this.absolute())
			x = x*sens[0];
	}
	
	@Override
	public boolean isNull() {
  	if(relative() && Geom.zero(getDX()) )
  			return true;
  	if(absolute() && Geom.zero(getX()))
  		return true;
  	return false;
  }
}
