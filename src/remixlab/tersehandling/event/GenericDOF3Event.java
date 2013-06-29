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

import remixlab.dandelion.geom.Geom;

public class GenericDOF3Event extends GenericMotionEvent {
	@Override
	public int hashCode() {
    return new HashCodeBuilder(17, 37).
    appendSuper(super.hashCode()).
		append(x).
		append(dx).
		append(y).
		append(dy).
		append(z).
		append(dz).
    toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;		
		if (obj.getClass() != getClass()) return false;
		
		GenericDOF3Event other = (GenericDOF3Event) obj;
		return new EqualsBuilder()
    .appendSuper(super.equals(obj))		
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
  
	public GenericDOF3Event(float x, float y, float z, int modifiers, int button) {
    super(modifiers, button);
		this.x = x;
		this.dx = 0f;
    this.y = y;
    this.dy = 0f;
    this.z = z;
    this.dz = 0f;
  }
	
	public GenericDOF3Event(GenericDOF3Event prevEvent, float x, float y, float z, int modifiers, int button) {
    this(x, y, z, modifiers, button);
    setPreviousEvent(prevEvent);
    /**
    if(prevEvent!=null) {
    	distance = Geom.distance(x, y, z, prevEvent.getX(), prevEvent.getY(), prevEvent.getZ());
    	if( sameSequence(prevEvent) ) {
    		this.dx = this.getX() - prevEvent.getX();
    		this.dy = this.getY() - prevEvent.getY();
    		this.dz = this.getZ() - prevEvent.getZ();
    		this.action = prevEvent.getAction();
    	}
    }
    */
  }
	
	//ready to be enqueued
	public GenericDOF3Event(float x, float y, float z) {
    super();
    this.x = x;
		this.dx = 0f;
    this.y = y;
    this.dy = 0f;
    this.z = z;
    this.dz = 0f;
    this.button = NOBUTTON;
	}

	//idem
	public GenericDOF3Event(GenericDOF3Event prevEvent, float x, float y, float z) {
    super();
    this.x = x;
		this.dx = 0f;
    this.y = y;
    this.dy = 0f;
    this.z = z;
    this.dz = 0f;
    this.button = NOBUTTON;
    setPreviousEvent(prevEvent);
	}
	 
  protected GenericDOF3Event(GenericDOF3Event other) {
  	super(other);
		this.x = new Float(other.x);
		this.dx = new Float(other.dx);
  	this.y = new Float(other.y);
  	this.dy = new Float(other.dy);
  	this.z = new Float(other.z);
  	this.dz = new Float(other.z);
	}
  
  @Override
	public GenericDOF3Event get() {
		return new GenericDOF3Event(this);
	}
  
  @Override
  public void setPreviousEvent(GenericMotionEvent prevEvent) {
  	super.setPreviousEvent(prevEvent);
  	if(prevEvent!=null)
  		if(prevEvent instanceof GenericDOF3Event)	{
  			this.dx = this.getX() - ((GenericDOF3Event) prevEvent).getX();
  			this.dy = this.getY() - ((GenericDOF3Event) prevEvent).getY();
  			this.dz = this.getZ() - ((GenericDOF3Event) prevEvent).getZ();
  			distance = Geom.distance(x, y, z, ((GenericDOF3Event) prevEvent).getX(), ((GenericDOF3Event) prevEvent).getY(), ((GenericDOF3Event) prevEvent).getZ());
  			delay = this.timestamp() - prevEvent.timestamp();
  			if(delay==0)
  				speed = distance;
  			else
  				speed = distance / (float)delay;
  		}
  	else {
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
	public void modulate(float [] sens) {
		if(sens != null)
		if(sens.length>=3 && this.absolute()) {
			x = x*sens[0];
			y = y*sens[1];
			z = z*sens[2];
		}
	}
	
	@Override
	public boolean isNull() {
  	if(relative() && Geom.zero(getDX()) && Geom.zero(getDY()) && Geom.zero(getDZ()))
  			return true;
  	if(absolute() && Geom.zero(getX()) && Geom.zero(getY()) && Geom.zero(getZ()))
  		return true;
  	return false;
  }
}
