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
import remixlab.tersehandling.event.ClickEvent;

public class GenericClickEvent <A extends Actionable<?>> extends ClickEvent implements Duoable<A> {
	Actionable<?> action;
	
	public GenericClickEvent(int b) {
		super(b);
	}
	
	public GenericClickEvent(int b, int clicks) {
		super(b, clicks);
	}
	
	public GenericClickEvent(Integer modifiers, int b) {
		super(modifiers, b);
	}
	
	public GenericClickEvent(Integer modifiers, int b, int clicks) {
		super(modifiers, b, clicks);
	}
	
	public GenericClickEvent(int b, Actionable<?> a) {
		super(b);
		action = a;
	}
	
	public GenericClickEvent(int b, int clicks, Actionable<?> a) {
		super(b, clicks);
		action = a;
	}
	
	public GenericClickEvent(Integer modifiers, int b, Actionable<?> a) {
		super(modifiers, b);
		action = a;
	}
	
	public GenericClickEvent(Integer modifiers, int b, int clicks, Actionable<?> a) {
		super(modifiers, b, clicks);
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
