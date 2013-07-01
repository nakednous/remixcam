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

package remixlab.tersehandling.duoable.event;

import remixlab.tersehandling.duoable.profile.Actionable;
import remixlab.tersehandling.duoable.profile.KeyDuoable;
import remixlab.tersehandling.event.KeyboardEvent;

public class GenericKeyboardEvent<A extends Actionable<?>> extends KeyboardEvent implements KeyDuoable<A> {
	Actionable<?> action;
	
	public GenericKeyboardEvent() {
		super();
	}
	
	public GenericKeyboardEvent(Integer modifiers, Character c, Integer vk) {
		super(modifiers, c, vk);
	}
	
	public GenericKeyboardEvent(Integer modifiers, Character c) {
		super(modifiers, c);
	}
	
	public GenericKeyboardEvent(Integer modifiers, Integer vk) {
		super(modifiers, vk);
	}
	
	public GenericKeyboardEvent(Character c) {
		super(c);
	}
	
	public GenericKeyboardEvent(Actionable<?> a) {
		super();
		action = a;
	}
	
	public GenericKeyboardEvent(Integer modifiers, Character c, Integer vk, Actionable<?> a) {
		super(modifiers, c, vk);
		action = a;
	}
	
	public GenericKeyboardEvent(Integer modifiers, Character c, Actionable<?> a) {
		super(modifiers, c);
		action = a;
	}
	
	public GenericKeyboardEvent(Integer modifiers, Integer vk, Actionable<?> a) {
		super(modifiers, vk);
		action = a;
	}
	
	public GenericKeyboardEvent(Character c, Actionable<?> a) {
		super(c);
		action = a;
	}
	
	protected GenericKeyboardEvent(GenericKeyboardEvent<A> other) {
		super(other);
		action = other.action;
	}
	
	@Override
	public Actionable<?> action() {
		return action;
	}
	
	@Override
	public void setAction(Actionable<?> a) {
		if( a instanceof Actionable<?> ) action = a;
	}
	
	@Override
	public GenericKeyboardEvent<A> get() {
		return new GenericKeyboardEvent<A>(this);
	}
}
