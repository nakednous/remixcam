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
import remixlab.tersehandling.duoable.profile.Duoable;
import remixlab.tersehandling.event.DOF2Event;

public class GenericDOF2Event<A extends Actionable<?>> extends DOF2Event implements Duoable<A> {
	Actionable<?> action;
	
	public GenericDOF2Event(float x, float y) {
		super(x, y);
	}
	
	public GenericDOF2Event(GenericDOF2Event<A> prevEvent, float x, float y) {
		super(prevEvent, x, y);
	}
	
	public GenericDOF2Event(GenericDOF2Event<A> prevEvent, float x, float y, int modifiers, int button) {
		super(prevEvent, x, y, modifiers, button);
	}
	
	public GenericDOF2Event(float x, float y, int modifiers, int button) {
		super(x, y, modifiers, button);
	}
	
	public GenericDOF2Event(float x, float y, Actionable<?> a) {
		super(x, y);
		action = a;
	}
	
	public GenericDOF2Event(GenericDOF2Event<A> prevEvent, float x, float y, Actionable<?> a) {
		super(prevEvent, x, y);
		action = a;
	}
	
	public GenericDOF2Event(GenericDOF2Event<A> prevEvent, float x, float y, int modifiers, int button, Actionable<?> a) {
		super(prevEvent, x, y, modifiers, button);
		action = a;
	}
	
	public GenericDOF2Event(float x, float y, int modifiers, int button, Actionable<?> a) {
		super(x, y, modifiers, button);
		action = a;
	}
	
	protected GenericDOF2Event(GenericDOF2Event<A> other) {
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
	public GenericDOF2Event<A> get() {
		return new GenericDOF2Event<A>(this);
	}
}
