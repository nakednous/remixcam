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

public class GenericDOF6Event extends GenericMotionEvent {
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
		append(rx).
		append(drx).
		append(ry).
		append(dry).
		append(rz).		
		append(drz).
    toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;		
		if (obj.getClass() != getClass()) return false;
		
		GenericDOF6Event other = (GenericDOF6Event) obj;
		return new EqualsBuilder()
    .appendSuper(super.equals(obj))
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
		.append(drz, other.drz)
		.isEquals();
	}

	protected Float x, dx;
  protected Float y, dy;
  protected Float z, dz;
  
  protected Float rx, drx;
  protected Float ry, dry;
  protected Float rz, drz;  

	public GenericDOF6Event(float x, float y, float z, float rx, float ry, float rz, int modifiers, int button) {
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
	
	public GenericDOF6Event(GenericDOF6Event prevEvent, float x, float y, float z, float rx, float ry, float rz, int modifiers, int button) {
    this(x, y, z, rx, ry, rz, modifiers, button);
    setPreviousEvent(prevEvent);
    /**
    if(prevEvent!=null) {
    	distance = Geom.distance(x, y, z, rx, ry, rz,
                 prevEvent.getX(), prevEvent.getY(), prevEvent.getZ(), prevEvent.getRX(), prevEvent.getRY(), prevEvent.getRZ());
    	if( sameSequence(prevEvent) ) {
    		this.dx = this.getX() - prevEvent.getX();
    		this.dy = this.getY() - prevEvent.getY();
    		this.dz = this.getZ() - prevEvent.getZ();
    		this.drx = this.getRX() - prevEvent.getRX();
    		this.dry = this.getRY() - prevEvent.getRY();
    		this.drz = this.getRZ() - prevEvent.getRZ();
    		this.action = prevEvent.getAction();
    	}
    }
    */
  }
	
	//ready to be enqueued
	public GenericDOF6Event(float x, float y, float z, float rx, float ry, float rz) {
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
    this.button = NOBUTTON;
	}

	//idem
	public GenericDOF6Event(GenericDOF6Event prevEvent, float x, float y, float z, float rx, float ry, float rz) {
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
    this.button = NOBUTTON;
    setPreviousEvent(prevEvent);
    /**
    if(prevEvent!=null) {
    	distance = Geom.distance(x, y, z, rx, ry, rz,
                 prevEvent.getX(), prevEvent.getY(), prevEvent.getZ(), prevEvent.getRX(), prevEvent.getRY(), prevEvent.getRZ());
    	if( sameSequence(prevEvent) ) {
    		this.dx = this.getX() - prevEvent.getX();
    		this.dy = this.getY() - prevEvent.getY();
    		this.dz = this.getZ() - prevEvent.getZ();
    		this.drx = this.getRX() - prevEvent.getRX();
    		this.dry = this.getRY() - prevEvent.getRY();
    		this.drz = this.getRZ() - prevEvent.getRZ();
    	}
    }
    */
	}
	  
  protected GenericDOF6Event(GenericDOF6Event other) {
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
	public GenericDOF6Event get() {
		return new GenericDOF6Event(this);
	}
  
  @Override
  public void setPreviousEvent(GenericMotionEvent prevEvent) {
  	super.setPreviousEvent(prevEvent);
  	if(prevEvent!=null)
  		if(prevEvent instanceof GenericDOF6Event)	{  			
  			this.dx = this.getX() - ((GenericDOF6Event) prevEvent).getX();
  			this.dy = this.getY() - ((GenericDOF6Event) prevEvent).getY();
  			this.dz = this.getZ() - ((GenericDOF6Event) prevEvent).getZ();
  			this.drx = this.getRX() - ((GenericDOF6Event) prevEvent).getRX();
  			this.dry = this.getRY() - ((GenericDOF6Event) prevEvent).getRY();
  			this.drz = this.getRZ() - ((GenericDOF6Event) prevEvent).getRZ();
  			distance = Geom.distance(x, y, z, rx, ry, rz,
            ((GenericDOF6Event) prevEvent).getX(), ((GenericDOF6Event) prevEvent).getY(), ((GenericDOF6Event) prevEvent).getZ(), ((GenericDOF6Event) prevEvent).getRX(), ((GenericDOF6Event) prevEvent).getRY(), ((GenericDOF6Event) prevEvent).getRZ());
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
	public void modulate(float [] sens) {
		if(sens != null)
		if(sens.length>=6 && this.absolute()) {
			x = x*sens[0];
			y = y*sens[1];
			z = z*sens[2];
			rx = rx*sens[3];
			ry = ry*sens[4];
			rz = rz*sens[5];
		}
	}
	
	@Override
	public boolean isNull() {
  	if(relative() && Geom.zero(getDX()) && Geom.zero(getDY()) && Geom.zero(getDZ()) && Geom.zero(getDRX()) && Geom.zero(getDRY()) && Geom.zero(getDRZ()))
  			return true;
  	if(absolute() && Geom.zero(getX()) && Geom.zero(getY()) && Geom.zero(getZ()) && Geom.zero(getRX()) && Geom.zero(getRY()) && Geom.zero(getRZ()))
  		return true;
  	return false;
  }
}
