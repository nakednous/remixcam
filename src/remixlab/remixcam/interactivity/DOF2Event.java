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

import remixlab.remixcam.event.GenericDOF2Event;
/**
import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;
*/

import remixlab.remixcam.core.Constants;

public class DOF2Event extends GenericDOF2Event<Constants.DOF_2Action> {	
	public DOF2Event(GenericDOF2Event<DOF_2Action> prevEvent, float x, float y, DOF_2Action a) {
		super(prevEvent, x, y, a);
	}
	
	public DOF2Event(float x, float y, int modifiers, int button) {
		super(x, y, modifiers, button);
	}
	
	public DOF2Event(GenericDOF2Event<DOF_2Action> prevEvent, float x, float y,	int modifiers, int button) {
		super(prevEvent, x, y, modifiers, button);
	}

	public DOF2Event(float x, float y, DOF_2Action a) {
		super(x, y, a);
	}
	
	protected DOF2Event(DOF2Event other) {
		super(other);
	}
	
	@Override
	public DOF2Event get() {
		return new DOF2Event(this);
	}
	
	@Override
	public DOF_2Action getAction() {
  	return (DOF_2Action)action;
  }
	
	public DOF1Event dof1Event(DOF_1Action a1) {
  	return dof1Event(true, a1);
  }
  
  public DOF1Event dof1Event(boolean fromX, DOF_1Action a1) {
  	DOF1Event e1 = dof1Event(fromX);
  	e1.setAction(a1);
  	return e1;
  }  
  
  public DOF1Event dof1Event() {
  	return dof1Event(true);
  }
  
  public DOF1Event dof1Event(boolean fromX) {
  	DOF1Event pe1;
  	DOF1Event e1;
  	if(fromX) {
  		if(relative()) {
  			pe1 = new DOF1Event(getPrevX(), getModifiers(), getButton());
  			e1 = new DOF1Event(pe1, getX(), getModifiers(), getButton());
  		}
  		else {
  			e1 = new DOF1Event(getX(), getModifiers(), getButton());
  		}
  	}
  	else {
  		if(relative()) {
  			pe1 = new DOF1Event(getPrevY(), getModifiers(), getButton());
  			e1 = new DOF1Event(pe1, getY(), getModifiers(), getButton());
  		}
  		else {
  			e1 = new DOF1Event(getY(), getModifiers(), getButton());
  		}
  	}
  	return e1;
  }
}
