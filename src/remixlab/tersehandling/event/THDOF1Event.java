/**
 *                  TerseHandling (version 0.70.0)      
 *           Copyright (c) 2013 by Jean Pierre Charalambos
 *                 @author Jean Pierre Charalambos      
 *              https://github.com/nakednous/remixcam
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

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

import remixlab.tersehandling.core.Util;

public class THDOF1Event extends THMotionEvent {
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

		THDOF1Event other = (THDOF1Event) obj;
		return new EqualsBuilder()
		.appendSuper(super.equals(obj))
		.append(x, other.x)
		.append(dx, other.dx)
		.isEquals();
	}

	protected Float x, dx;

	public THDOF1Event(float x, int modifiers, int button) {
		super(modifiers, button);
		this.x = x;
		this.dx = 0f;
	}

	public THDOF1Event(THDOF1Event prevEvent, float x, int modifiers, int button) {
		this(x, modifiers, button);
		setPreviousEvent(prevEvent);
		/**
		if(prevEvent!=null) {
			distance = this.getX() - prevEvent.getX();
			if (sameSequence(prevEvent)) {
				this.action = prevEvent.getAction();
				this.dx = this.getX() - prevEvent.getX();
			}
		}
		*/
	}
	
  //ready to be enqueued
	public THDOF1Event(float x) {
		super();
		this.x = x;
		this.dx = 0f;
		this.button = TH_NOBUTTON;
	}

	// idem
	public THDOF1Event(THDOF1Event prevEvent, float x) {
		super();		
		this.x = x;
		this.dx = 0f;
		this.button = TH_NOBUTTON;
		setPreviousEvent(prevEvent);
	}

	// ---

	protected THDOF1Event(THDOF1Event other) {
		super(other);
		this.x = new Float(other.x);
		this.dx = new Float(other.dx);
	}

	@Override
	public THDOF1Event get() {
		return new THDOF1Event(this);
	}
	
	@Override
  public void setPreviousEvent(THMotionEvent prevEvent) {
  	if(prevEvent!=null)
  		if(prevEvent instanceof THDOF1Event)	{  			
  			this.dx = this.getX() - ((THDOF1Event) prevEvent).getX();
  			distance = this.getX() - ((THDOF1Event) prevEvent).getX();
  			delay = this.timestamp() - prevEvent.timestamp();
  			if(delay==0)
  				speed = distance;
  			else
  				speed = distance / (float)delay;
  		}
  	else {
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
	public void modulate(float [] sens) {
		if(sens != null)
		if(sens.length>=1 && this.absolute())
			x = x*sens[0];
	}
	
	@Override
	public boolean isNull() {
  	if(relative() && Util.zero(getDX()) )
  			return true;
  	if(absolute() && Util.zero(getX()))
  		return true;
  	return false;
  }
}
