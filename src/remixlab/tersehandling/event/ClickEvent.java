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

package remixlab.tersehandling.event;

import remixlab.tersehandling.event.shortcut.ClickShortcut;
import remixlab.util.gwthashcodeequals.EqualsBuilder;
import remixlab.util.gwthashcodeequals.HashCodeBuilder;

public class ClickEvent extends TerseEvent {
	@Override
	public int hashCode() {
    return new HashCodeBuilder(17, 37).
    appendSuper(super.hashCode()).
    append(x).
    append(y).
    append(button).
		append(numberOfClicks).
    toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;		
		if (obj.getClass() != getClass()) return false;
		
		ClickEvent other = (ClickEvent) obj;
		return new EqualsBuilder()
    .appendSuper(super.equals(obj))
    .append(button, other.button)
    .append(numberOfClicks, other.numberOfClicks)
    .append(x, other.x)
    .append(y, other.y)
		.isEquals();
	}
	
	protected Float x, y;
	protected final Integer numberOfClicks;
	protected final Integer button;
	
	public ClickEvent(float x, float y, int b) {
		this.x = x;
	  	this.y = y;
		this.button = b;
		this.numberOfClicks = 1; 
  }
	
	public ClickEvent(float x, float y, int b, int clicks) {
		this.x = x;
	  	this.y = y;
		this.button = b;
		this.numberOfClicks = clicks;
  }
	
	//--
	
	public ClickEvent(float x, float y, Integer modifiers, int b) {
		super(modifiers);
		this.x = x;
	  	this.y = y;
		this.button = b;
		this.numberOfClicks = 1;  	
  }
	
	public ClickEvent(float x, float y, Integer modifiers, int b, int clicks) {
		super(modifiers);
		this.x = x;
	  	this.y = y;
		this.button = b;
		this.numberOfClicks = clicks;
  }
	
	protected ClickEvent(ClickEvent other) {
		super(other);
		this.x = new Float(other.x);
	  	this.y = new Float(other.y);
		this.button = new Integer(other.button);
		this.numberOfClicks = new Integer(other.numberOfClicks);		
	}
	
	@Override
	public ClickEvent get() {
		return new ClickEvent(this);
	}
	
	@Override
	public ClickShortcut shortcut() {
		return new ClickShortcut(getModifiers(), getButton(), getClickCount());
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public int getClickCount() {
		return numberOfClicks;
	}
	
	public int getButton() {
		return button;
	}
}
