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

package remixlab.tersehandling.shortcut;

import com.flipthebird.gwthashcodeequals.*;

import remixlab.tersehandling.core.Copyable;
import remixlab.tersehandling.event.BaseEvent;

/**
 * This class represents mouse shortcuts.
 * <p>
 * Mouse shortcuts can be of one out of two forms: 1. Mouse buttons (e.g., 'LEFT');
 * 2. Mouse button + Key combinations (e.g., 'RIGHT' + CTRL key).
 */
public final class ButtonShortcut extends Shortcut implements Copyable {
	@Override
	public int hashCode() {
   return new HashCodeBuilder(17, 37).		
    appendSuper(super.hashCode()).
		append(button).
   toHashCode();		
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;		
		if (obj.getClass() != getClass()) return false;		
		
		ButtonShortcut other = (ButtonShortcut) obj;
	  return new EqualsBuilder()		
	  .appendSuper(super.equals(obj))
		.append(button, other.button)
		.isEquals();
	}
	
	public ButtonShortcut() {
		this(TH_NOMODIFIER_MASK, TH_NOBUTTON);
	}

	/**
	 * Defines a mouse shortcut from the given mouse button.
	 * 
	 * @param b mouse button
	 */
	public ButtonShortcut(Integer b) {
		this(TH_NOMODIFIER_MASK, b);
	}

	/**
	 * Defines a mouse shortcut from the given modifier mask and mouse button combination.
	 * 
	 * @param m the mask 
	 * @param b mouse button
	 */
	public ButtonShortcut(Integer m, Integer b) {
		//super(m);
		//this.button = b;
		
		// /**
	  //TODO HACK see issue: https://github.com/processing/processing/issues/1693
		this.button = b;	  
		//ALT
		if(button == TH_CENTER) {
			mask = (TH_ALT | m);
		}
		//META
		else if(button == TH_RIGHT) {
    	mask = (TH_META | m);
		}
		else
			mask = m;
		// */
	}
	
	protected ButtonShortcut(ButtonShortcut other) {
		super(other);
		this.button = new Integer(other.button);
	}
	
	@Override
	public ButtonShortcut get() {
		return new ButtonShortcut(this);
	}
	
	/**
	 * Returns a textual description of this mouse shortcut.
	 *  
	 * @return description
	 */
	public String description() {
		return description(button);
	}	
	
	/**
	 * Internal. Low-level description() function.
	 */
	protected String description(Integer b) {
		//TODO: NO_BUTTON should be defined -> e.g., mouse move
		String r = BaseEvent.getModifiersText(mask);
		switch (b) {
		case TH_LEFT:
			r += (r.length() > 0) ? "+LEFT_BUTTON" : "LEFT_BUTTON";
			break;
		case TH_CENTER:
			r += (r.length() > 0) ? "+MIDDLE_BUTTON" : "MIDDLE_BUTTON";
			break;
		case TH_RIGHT:
			r += (r.length() > 0) ? "+RIGHT_BUTTON" : "RIGHT_BUTTON";
			break;			
		default:
			r += (r.length() > 0) ? "+NO_MOUSE_BUTTON" : "NO_MOUSE_BUTTON";
			break;
		}		
		return r;
	}
	
	/**
	 * Internal convenience function.
	 */
	/**
	protected String description(MouseEvent e) {
		return description(e.getButton());
	}	
	*/

	protected final Integer button;
}