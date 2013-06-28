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

package remixlab.dandelion.event;

import remixlab.dandelion.core.Constants;
import remixlab.duoable.profile.Actionable;
import remixlab.duoable.profile.Duoble;
import remixlab.tersehandling.event.GenericDOF6Event;

/**
import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;
*/

//TODO implement equals and hash including the action
//see commented lines in GenericEvent
public class DOF6Event extends GenericDOF6Event implements Constants, Duoble<Constants.DOF_6Action> {
	DOF_6Action action;
	
	public DOF6Event(float x, float y, float z, float rx, float ry, float rz,	int modifiers, int button) {
		super(x, y, z, rx, ry, rz, modifiers, button);
	}

	public DOF6Event(float x, float y, float z, float rx, float ry, float rz,	DOF_6Action a) {
		super(x, y, z, rx, ry, rz);
		action = a;
	}
	
	public DOF6Event(GenericDOF6Event prevEvent, float x, float y, float z, float rx, float ry, float rz, int modifiers, int button) {
		super(prevEvent, x, y, z, rx, ry, rz, modifiers, button);
	}
	
	public DOF6Event(GenericDOF6Event prevEvent, float x, float y, float z, float rx, float ry, float rz, DOF_6Action a) {
		super(prevEvent, x, y, z, rx, ry, rz);
		action = a;
	}
	
	public DOF6Event(GenericDOF6Event prevEvent, float x, float y, float z, float rx, float ry, float rz) {
		super(prevEvent, x, y, z, rx, ry, rz);
	}
	
	protected DOF6Event(GenericDOF6Event other) {
		super(other);
	}

  @Override
	public DOF6Event get() {
		return new DOF6Event(this);
	}
	
  @Override
	public DOF_6Action getAction() {
  	return (DOF_6Action)action;
  }
  
  @Override
	public void setAction(Actionable<?> a) {
		if( a instanceof DOF_6Action ) action = (DOF_6Action)a;		
	}
  
  public DOF3Event dof3Event(DOF_3Action a3) {
  	return dof3Event(true, a3);
  }
  
  public DOF3Event dof3Event(boolean fromTranslation) {
  	return dof3Event(fromTranslation, null);
  }
  
  public DOF3Event dof3Event() {
  	return dof3Event(true);
  }
  
  public DOF3Event dof3Event(boolean fromTranslation, DOF_3Action a3) {
  	DOF3Event pe3;
  	DOF3Event e3;
  	if(relative()) {
  		if(fromTranslation) {
  			pe3 = new DOF3Event(getPrevX(), getPrevY(), getPrevZ(), getModifiers(), getButton(), a3);
  			e3 = new DOF3Event(pe3, getX(), getY(), getZ(), getModifiers(), getButton(), a3);
  		}
  		else {
  			pe3 = new DOF3Event(getPrevRX(), getPrevRY(), getPrevRZ(), getModifiers(), getButton(), a3);
  			e3 = new DOF3Event(pe3, getRX(), getRY(), getRZ(), getModifiers(), getButton(), a3);
  		}
  	}
  	else {
  		if(fromTranslation)
    		e3 = new DOF3Event(getX(), getY(), getZ(), getModifiers(), getButton(), a3);
  		else
  			e3 = new DOF3Event(getRX(), getRY(), getRZ(), getModifiers(), getButton(), a3);  		
  	}
  	return e3;
  }
}
