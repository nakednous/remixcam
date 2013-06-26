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
import remixlab.remixcam.core.KeyDuoble;
import remixlab.remixcam.event.GenericKeyboardEvent;

/**
import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;
*/

public class KeyboardEvent extends GenericKeyboardEvent implements KeyDuoble<Constants.DOF_0Action> {
	Constants.DOF_0Action action;
	
	public KeyboardEvent(GenericKeyboardEvent gEvent) {
		super(gEvent);
	}
	
	public KeyboardEvent(GenericKeyboardEvent gEvent, DOF_0Action a) {
		super(gEvent);
		action = a;
	}
	
	public KeyboardEvent() {
		super();
	}
	
	public KeyboardEvent(DOF_0Action a) {
		action = a;
	}
	
	public KeyboardEvent(Integer modifiers, Character c, Integer vk) {
		super(modifiers, c, vk);
	}
	
	public KeyboardEvent(Integer modifiers, Character c) {
		super(modifiers, c);
	}
	
	public KeyboardEvent(Integer modifiers, Integer vk) {
		super(modifiers, vk);
	}
	
	public KeyboardEvent(Character c) {
		super(c);
	}
	
	public KeyboardEvent(Integer modifiers, Character c, Integer vk, DOF_0Action a) {
		super(modifiers, c, vk);
		action = a;
	}
	
	public KeyboardEvent(Integer modifiers, Character c, DOF_0Action a) {
		super(modifiers, c);
		action = a;
	}
	
	public KeyboardEvent(Integer modifiers, Integer vk, DOF_0Action a) {
		super(modifiers, vk);
		action = a;
	}
	
	public KeyboardEvent(Character c, DOF_0Action a) {
		super(c);
		action = a;
	}
	
	protected KeyboardEvent(KeyboardEvent other) {
		super(other);
		this.action = other.action;
	}
	
	@Override
	public KeyboardEvent get() {
		return new KeyboardEvent(this);
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
