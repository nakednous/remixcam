/**
 *                     ProScene (version 1.1.96)      
 *    Copyright (c) 2010-2012 by National University of Colombia
 *                 @author Jean Pierre Charalambos      
 *           http://www.disi.unal.edu.co/grupos/remixlab/
 *                           
 * This java package provides classes to ease the creation of interactive 3D
 * scenes in Processing.
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

package remixlab.remixcam.shortcut;

import com.flipthebird.gwthashcodeequals.*;
import remixlab.remixcam.core.Constants;
import remixlab.remixcam.event.DLEvent;

/**
 * This class represents mouse shortcuts.
 * <p>
 * Mouse shortcuts can be of one out of two forms: 1. Mouse buttons (e.g., 'LEFT');
 * 2. Mouse button + Key combinations (e.g., 'RIGHT' + CTRL key).
 */
public final class ButtonShortcut extends AbstractShortcut implements Constants {
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

	/**
	 * Defines a mouse shortcut from the given mouse button.
	 * 
	 * @param b mouse button
	 */
	public ButtonShortcut(Integer b) {
		this(0, b);
	}

	/**
	 * Defines a mouse shortcut from the given modifier mask and mouse button combination.
	 * 
	 * @param m the mask 
	 * @param b mouse button
	 */
	public ButtonShortcut(Integer m, Integer b) {
		super(m);
		this.button = b;
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
		String r = DLEvent.getModifiersText(mask);
		switch (b) {
		case LEFT:
			r += (r.length() > 0) ? "+LEFT_BUTTON" : "LEFT_BUTTON";
			break;
		case CENTER:
			r += (r.length() > 0) ? "+MIDDLE_BUTTON" : "MIDDLE_BUTTON";
			break;
		case RIGHT:
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