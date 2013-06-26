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

import remixlab.remixcam.core.Actionable;
import remixlab.remixcam.core.Constants;
import remixlab.remixcam.core.Duoble;
import remixlab.remixcam.event.GenericClickEvent;

/**
import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;
*/

public class ClickEvent extends GenericClickEvent implements Duoble<Constants.DOF_0Action> {
	Constants.DOF_0Action action;
	
	public ClickEvent(GenericClickEvent gEvent) {
		super(gEvent);
	}
	
	public ClickEvent(GenericClickEvent gEvent, DOF_0Action a) {
		super(gEvent);
		action = a;
	}
	
	public ClickEvent(Integer modifiers, int b, int clicks, DOF_0Action a) {
		super(modifiers, b, clicks);
		action = a;
	}

	public ClickEvent(Integer modifiers, int b, int clicks) {
		super(modifiers, b, clicks);
	}
	
	public ClickEvent(Integer modifiers, int b, DOF_0Action a) {
		super(modifiers, b);
		action = a;
	}
	
	public ClickEvent(Integer modifiers, int b) {
		super(modifiers, b);
	}
	
	public ClickEvent(int b, int clicks, DOF_0Action a) {
		super(b, clicks);
		action = a;
	}
	
	public ClickEvent(int b, int clicks) {
		super(b, clicks);
	}
	
	public ClickEvent(int b, DOF_0Action a) {
		super(b);
		action = a;
	}
	
	public ClickEvent(int b) {
		super(b);
	}
	
	protected ClickEvent(ClickEvent other) {
		super(other);
		this.action = other.action;
	}
	
	@Override
	public ClickEvent get() {
		return new ClickEvent(this);
	}
	
	@Override
	public DOF_0Action getAction() {
  	return (DOF_0Action)action;
  }

	@Override
	public void setAction(Actionable<?> a) {
		if( a instanceof DOF_0Action ) action = (DOF_0Action)a;		
	}
}
