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
import remixlab.tersehandling.event.GenericDOF2Event;
/**
import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;
*/

//TODO implement equals and hash including the action
//see commented lines in GenericEvent
public class DOF2Event extends GenericDOF2Event implements Constants, Duoble<Constants.DOF_2Action> {	
	DOF_2Action action;
	
	public DOF2Event(GenericDOF2Event prevEvent, float x, float y) {
		super(prevEvent, x, y);
	}
	
	public DOF2Event(GenericDOF2Event prevEvent, float x, float y, DOF_2Action a) {
		super(prevEvent, x, y);
		action = a;
	}
	
	public DOF2Event(float x, float y, int modifiers, int button) {
		super(x, y, modifiers, button);
	}
	
	public DOF2Event(float x, float y, int modifiers, int button, DOF_2Action a) {
		super(x, y, modifiers, button);
		action = a;
	}
	
	public DOF2Event(GenericDOF2Event prevEvent, float x, float y, int modifiers, int button) {
		super(prevEvent, x, y, modifiers, button);
	}
	
	public DOF2Event(GenericDOF2Event prevEvent, float x, float y, int modifiers, int button, DOF_2Action a) {
		super(prevEvent, x, y, modifiers, button);
		action = a;
	}

	public DOF2Event(float x, float y, DOF_2Action a) {
		super(x, y);
		action = a;
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
	
	@Override
	public void setAction(Actionable<?> a) {
		if( a instanceof DOF_2Action ) action = (DOF_2Action)a;		
	}
	
	public DOF1Event dof1Event(DOF_1Action a1) {
  	return dof1Event(true, a1);
  }
	
	public DOF1Event dof1Event(boolean fromX) {
		return dof1Event(fromX, null);
	}
  
  public DOF1Event dof1Event() {
  	return dof1Event(true);
  }
  
  public DOF1Event dof1Event(boolean fromX, DOF_1Action a1) {
  	DOF1Event pe1;
  	DOF1Event e1;
  	if(fromX) {
  		if(relative()) {
  			pe1 = new DOF1Event(getPrevX(), getModifiers(), getButton(), a1);
  			e1 = new DOF1Event(pe1, getX(), getModifiers(), getButton(), a1);
  		}
  		else {
  			e1 = new DOF1Event(getX(), getModifiers(), getButton(), a1);
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
