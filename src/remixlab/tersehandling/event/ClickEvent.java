/*******************************************************************************
 * TerseHandling (version 0.9.50)
 * Copyright (c) 2013 Jean Pierre Charalambos.
 * @author Jean Pierre Charalambos
 * https://github.com/remixlab
 * 
 * All rights reserved. Library that eases the creation of interactive
 * scenes released under the terms of the GNU Public License v3.0
 * which available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package remixlab.tersehandling.event;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

import remixlab.tersehandling.event.shortcut.ClickShortcut;

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
