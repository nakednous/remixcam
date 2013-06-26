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

import remixlab.remixcam.shortcut.*;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

// /**
public class GenericMotionEvent extends GenericEvent {
	@Override
	public int hashCode() {
    return new HashCodeBuilder(17, 37).
    appendSuper(super.hashCode()).
		append(button).
		append(delay).
		append(distance).
		append(speed).
    toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;		
		if (obj.getClass() != getClass()) return false;
		
		GenericMotionEvent other = (GenericMotionEvent) obj;
		return new EqualsBuilder()
    .appendSuper(super.equals(obj))
		.append(button, other.button)
		.append(delay, other.delay)
		.append(distance, other.distance)
		.append(speed, other.speed)
		.isEquals();
	}

	protected Integer button;
	
	//defaulting to zero:
  //http://stackoverflow.com/questions/3426843/what-is-the-default-initialization-of-an-array-in-java
	protected long delay;
	protected float distance, speed;
	
	public GenericMotionEvent() {
    super();
    this.button = NOBUTTON;
  }
	
	public GenericMotionEvent(int modifiers) {
    super(modifiers);
    this.button = NOBUTTON;
  }
	
	public GenericMotionEvent(int modifiers, int button) {
    super(modifiers);
    this.button = button;
  }
  
	// ---
	
  protected GenericMotionEvent(GenericMotionEvent other) {
  	super(other);
		this.button = new Integer(other.button);
		this.delay = other.delay;
		this.distance = other.distance;
		this.speed = other.speed;
	}
  
  @Override
	public GenericMotionEvent get() {
		return new GenericMotionEvent(this);
	}
  
  public void modulate(float [] sens) {}
  
	public int getButton() {
		return button;
	}
	
	@Override
	public ButtonShortcut shortcut() {
		if( getButton() == 0 )
			return new ButtonShortcut(getModifiers());
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
		return distance() != 0;
	}
	
	public boolean absolute() {
		return !relative();
	}
	
	public void setPreviousEvent(GenericMotionEvent prevEvent) {
		if(prevEvent == null) {
			delay = 0;
			speed = 0;
			distance = 0;
		}
		else {
			delay = this.timestamp() - prevEvent.timestamp();
			if(delay==0)
				speed = distance;
			else
				speed = distance / (float)delay;
		}
	}
	
	//--
	
	/**
	protected boolean sameSequence(GenericMotionEvent<?> prevEvent) {
		boolean result = false;
		long tThreshold = 5000;
		float dThreshold =  50;
		delay = this.timestamp() - prevEvent.timestamp();
		
		if(delay==0)
			speed = distance;
		else
			speed = distance / (float)delay;
		
		//if(prevEvent != null)
    	if( prevEvent.shortcut().equals(this.shortcut()) )
    		if( ( distance <= dThreshold) && ( delay <= tThreshold ) ) {    			
    			result = true;    			
    		} else {
    			delay = 0L;
        	speed = 0f;
        	distance = 0f;
    		}
		return result;
	}
	// */
}
