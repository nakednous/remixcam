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

//import com.flipthebird.gwthashcodeequals.EqualsBuilder;
//import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

import remixlab.remixcam.core.Actionable;
import remixlab.remixcam.core.Constants;
import remixlab.remixcam.core.Duoble;
import remixlab.remixcam.event.GenericDOF1Event;
///**
public class DOF1Event extends GenericDOF1Event implements Duoble<Constants.DOF_1Action> {
	DOF_1Action action;
	
	public DOF1Event(GenericDOF1Event gEvent) {
		super(gEvent);
	}
	
	public DOF1Event(GenericDOF1Event gEvent, DOF_1Action a) {
		super(gEvent);
		action = a;
	}
	
	public DOF1Event(float x, DOF_1Action a) {
		super(x);
		action = a;
	}

	public DOF1Event(float x, int modifiers, int button) {
		super(x, modifiers, button);
	}
	
	public DOF1Event(float x, int modifiers, int button, DOF_1Action a) {
		super(x, modifiers, button);
		action = a;
	}
	
	public DOF1Event(DOF1Event prevEvent, float x, DOF_1Action a) {
		super(prevEvent, x);
		action = a;
	}

	public DOF1Event(DOF1Event prevEvent, float x, int modifiers, int button) {
		super(prevEvent, x, modifiers, button);
	}
	
	public DOF1Event(DOF1Event prevEvent, float x, int modifiers, int button, DOF_1Action a) {
		super(prevEvent, x, modifiers, button);
		action = a;
	}

	protected DOF1Event(DOF1Event other) {
		super(other);
	}
	
	@Override
	public DOF1Event get() {
		return new DOF1Event(this);
	}
	
	@Override
	public DOF_1Action getAction() {
  	return (DOF_1Action)action;
  }
	
	@Override
	public void setAction(Actionable<?> a) {
		if( a instanceof DOF_1Action ) action = (DOF_1Action)a;		
	}
}