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

import remixlab.remixcam.geom.Geom;

public class GenericDOF2Event extends GenericMotionEvent {
	@Override
	public int hashCode() {
    return new HashCodeBuilder(17, 37).
    appendSuper(super.hashCode()).
		appendSuper(super.hashCode()).
		append(x).
		append(dx).
		append(y).
		append(dy).
    toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;		
		if (obj.getClass() != getClass()) return false;
		
		GenericDOF2Event other = (GenericDOF2Event) obj;
		return new EqualsBuilder()
    .appendSuper(super.equals(obj))		
    .append(x, other.x)
		.append(dx, other.dx)
		.append(y, other.y)
		.append(dy, other.dy)
		.isEquals();
	}

	protected Float x, dx;
  protected Float y, dy;
  
	public GenericDOF2Event(float x, float y, int modifiers, int button) {
    super(modifiers, button);
		this.x = x;
		this.dx = 0f;
    this.y = y;
    this.dy = 0f;
  }
	
	public GenericDOF2Event(GenericDOF2Event prevEvent, float x, float y, int modifiers, int button) {
		this(x, y, modifiers, button);
		setPreviousEvent(prevEvent);
		
		/**
		if(prevEvent!=null) {
			distance = Geom.distance(x, y, prevEvent.getX(), prevEvent.getY()); 
			if( sameSequence(prevEvent) ) {
				this.dx = this.getX() - prevEvent.getX();
				this.dy = this.getY() - prevEvent.getY();
				this.action = prevEvent.getAction();
  		}
		}
    // */
		
    /**
    //TODO debug
    if( sameSequence(prevEvent) ) {
    	this.dx = this.getX() - prevEvent.getX();
  		this.dy = this.getY() - prevEvent.getY();
  		this.action = prevEvent.getAction();
  		System.out.println("Current event: x: " + getX() + " y: " + getY());
  		System.out.println("Prev event: x: " + getPrevX() + " y: " + getPrevY());
  		//System.out.println("Distance: " + distance());
  		//System.out.println("Delay: " + delay());
  		//System.out.println("Speed: " + speed());
    }
    else {
    	System.out.println("different sequence!");
    }
    // */
  }
	
  //ready to be enqueued
	public GenericDOF2Event(float x, float y) {
    super();
    this.x = x;
		this.dx = 0f;
    this.y = y;
    this.dy = 0f;
    this.button = NOBUTTON;
	}

	//idem
	public GenericDOF2Event(GenericDOF2Event prevEvent, float x, float y) {
    super();
		this.x = x;
		this.dx = 0f;
    this.y = y;
    this.dy = 0f;
    this.button = NOBUTTON;
    setPreviousEvent(prevEvent);
  }
  
  protected GenericDOF2Event(GenericDOF2Event other) {
  	super(other);
		this.x = new Float(other.x);
		this.dx = new Float(other.dx);
  	this.y = new Float(other.y);
  	this.dy = new Float(other.dy);
	}
  
  @Override
	public GenericDOF2Event get() {
		return new GenericDOF2Event(this);
	}
  
  @Override
  public void setPreviousEvent(GenericMotionEvent prevEvent) {
  	if(prevEvent!=null)
  		if(prevEvent instanceof GenericDOF2Event)	{
  			this.dx = this.getX() - ((GenericDOF2Event) prevEvent).getX();
  			this.dy = this.getY() - ((GenericDOF2Event) prevEvent).getY();
  			distance = Geom.distance(x, y, ((GenericDOF2Event) prevEvent).getX(), ((GenericDOF2Event) prevEvent).getY());  			
  			delay = this.timestamp() - prevEvent.timestamp();
  			if(delay==0)
  				speed = distance;
  			else
  				speed = distance / (float)delay;
  		}
  	else {
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
	public void modulate(float [] sens) {
		if(sens != null)
		if(sens.length>=2 && this.absolute()) {
			x = x*sens[0];
			y = y*sens[1];
		}
	}
	
	@Override
	public boolean isNull() {
  	if(relative() && Geom.zero(getDX()) && Geom.zero(getDY()))
  			return true;
  	if(absolute() && Geom.zero(getX()) && Geom.zero(getY()))
  		return true;
  	return false;
  }

}
