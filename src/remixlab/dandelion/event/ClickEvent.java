/**
 *                     Dandelion (version 0.70.0)      
 *          Copyright (c) 2013 by Jean Pierre Charalambos
 *                 @author Jean Pierre Charalambos      
 *              https://github.com/nakednous/remixcam
 *                           
 * This library provides classes to ease the creation of interactive
 * frame-based, 2d and 3d scenes.
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
import remixlab.tersehandling.duoable.profile.Actionable;
import remixlab.tersehandling.duoable.profile.Duoble;
import remixlab.tersehandling.event.THClickEvent;

/**
import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;
*/

//TODO implement equals and hash including the action
// see commented lines in GenericEvent
public class ClickEvent extends THClickEvent implements Constants, Duoble<Constants.DOF_0Action> {
	Constants.DOF_0Action action;
	
	public ClickEvent(THClickEvent gEvent) {
		super(gEvent);
	}
	
	public ClickEvent(THClickEvent gEvent, DOF_0Action a) {
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
