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

import remixlab.tersehandling.event.ClickEvent;
import remixlab.tersehandling.generic.profile.Actionable;
import remixlab.tersehandling.generic.profile.Duoable;

public class GenericClickEvent <A extends Actionable<?>> extends ClickEvent implements Duoable<A> {
	Actionable<?> action;
	
	public GenericClickEvent(float x, float y, int b) {
		super(x, y, b);
	}
	
	public GenericClickEvent(float x, float y, int b, int clicks) {
		super(x, y, b, clicks);
	}
	
	public GenericClickEvent(float x, float y, Integer modifiers, int b) {
		super(x, y, modifiers, b);
	}
	
	public GenericClickEvent(float x, float y, Integer modifiers, int b, int clicks) {
		super(x, y, modifiers, b, clicks);
	}
	
	public GenericClickEvent(float x, float y, int b, Actionable<?> a) {
		super(x, y, b);
		action = a;
	}
	
	public GenericClickEvent(float x, float y, int b, int clicks, Actionable<?> a) {
		super(x, y, b, clicks);
		action = a;
	}
	
	public GenericClickEvent(float x, float y, Integer modifiers, int b, Actionable<?> a) {
		super(x, y, modifiers, b);
		action = a;
	}
	
	public GenericClickEvent(float x, float y, Integer modifiers, int b, int clicks, Actionable<?> a) {
		super(x, y, modifiers, b, clicks);
		action = a;
	}
	
	protected GenericClickEvent(GenericClickEvent<A> other) {
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
	public GenericClickEvent<A> get() {
		return new GenericClickEvent<A>(this);
	}
}
