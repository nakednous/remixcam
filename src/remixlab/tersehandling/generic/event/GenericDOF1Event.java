/**
 *                  TerseHandling (version 0.70.0)
 *           Copyright (c) 2013 by Jean Pierre Charalambos
 *                 @author Jean Pierre Charalambos
 *             https://github.com/nakednous/tersehandling
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

package remixlab.tersehandling.generic.event;

import remixlab.tersehandling.event.DOF1Event;
import remixlab.tersehandling.generic.profile.Actionable;
import remixlab.tersehandling.generic.profile.Duoable;

public class GenericDOF1Event<A extends Actionable<?>> extends DOF1Event implements Duoable<A> {
	Actionable<?> action;
	
	public GenericDOF1Event(float x, int modifiers, int button) {
		super(x,modifiers, button);
	}
	
	public GenericDOF1Event(GenericDOF1Event<A> prevEvent, float x, int modifiers, int button) {
		super(prevEvent, x, modifiers, button);
	}
	
	public GenericDOF1Event(float x) {
		super(x);
	}
	
	public GenericDOF1Event(GenericDOF1Event<A> prevEvent, float x) {
		super(prevEvent, x);
	}
	
	public GenericDOF1Event(float x, int modifiers, int button, Actionable<?> a) {
		super(x,modifiers, button);
		action = a;
	}
	
	public GenericDOF1Event(GenericDOF1Event<A> prevEvent, float x, int modifiers, int button, Actionable<?> a) {
		super(prevEvent, x, modifiers, button);
		action = a;
	}
	
	public GenericDOF1Event(float x, Actionable<?> a) {
		super(x);
		action = a;
	}
	
	public GenericDOF1Event(GenericDOF1Event<A> prevEvent, float x, Actionable<?> a) {
		super(prevEvent, x);
		action = a;
	}
	
	protected GenericDOF1Event(GenericDOF1Event<A> other) {
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
	public GenericDOF1Event<A> get() {
		return new GenericDOF1Event<A>(this);
	}
}
