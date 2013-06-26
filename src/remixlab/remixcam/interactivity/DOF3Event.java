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

package remixlab.remixcam.interactivity;

import remixlab.remixcam.event.GenericDOF3Event;

/**
import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;
*/

import remixlab.remixcam.core.Actionable;
import remixlab.remixcam.core.Constants;
import remixlab.remixcam.core.Duoble;

public class DOF3Event extends GenericDOF3Event implements Duoble<Constants.DOF_3Action> {
	DOF_3Action action;
	
	public DOF3Event(float x, float y, float z, int modifiers, int button) {
		super(x, y, z, modifiers, button);
	}
	
	public DOF3Event(float x, float y, float z, int modifiers, int button, DOF_3Action a) {
		super(x, y, z, modifiers, button);
		action = a;
	}

  public DOF3Event(float x, float y, float z, DOF_3Action a) {
		super(x, y, z);
		action = a;
	}

  public DOF3Event(GenericDOF3Event prevEvent, float x, float y, float z, int modifiers, int button) {
		super(prevEvent, x, y, z, modifiers, button);
	}
  
  public DOF3Event(GenericDOF3Event prevEvent, float x, float y, float z, int modifiers, int button, DOF_3Action a) {
		super(prevEvent, x, y, z, modifiers, button);
		action = a;
	}
	
  public DOF3Event(GenericDOF3Event prevEvent, float x, float y, float z, DOF_3Action a) {
		super(prevEvent, x, y, z);
		action = a;
	}
	
  protected DOF3Event(GenericDOF3Event other) {
		super(other);
	}

  @Override
	public DOF3Event get() {
		return new DOF3Event(this);
	}
  
  @Override
	public DOF_3Action getAction() {
  	return (DOF_3Action)action;
  }
  
  @Override
	public void setAction(Actionable<?> a) {
		if( a instanceof DOF_3Action ) action = (DOF_3Action)a;		
	}
  
  public DOF2Event dof2Event() {
  	return dof2Event(null);
  }
  
  public DOF2Event dof2Event(DOF_2Action a2) {
  	DOF2Event pe2;
  	DOF2Event e2;
  	if(relative()) {  		
  			pe2 = new DOF2Event(getPrevX(), getPrevY(), getModifiers(), getButton(), a2);
  			e2 = new DOF2Event(pe2, getX(), getY(), getModifiers(), getButton(), a2);  		
  	}
  	else {
  		e2 = new DOF2Event(getX(), getY(), getModifiers(), getButton(), a2); 
  	}
  	return e2;
  }
}
