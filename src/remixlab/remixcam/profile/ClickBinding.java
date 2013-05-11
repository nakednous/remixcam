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

package remixlab.remixcam.profile;

import com.flipthebird.gwthashcodeequals.*;
import remixlab.remixcam.core.Constants;
import remixlab.remixcam.event.DLEvent;

/**
 * This class represents mouse click shortcuts.
 * <p>
 * Mouse click shortcuts are defined with a specific number of clicks
 * and can be of one out of two forms: 1. A mouse button; and, 2. A mouse
 * button plus a key-modifier (such as the CTRL key).
 */
public class ClickBinding implements Constants {	
	@Override
	public int hashCode() {
    return new HashCodeBuilder(17, 37).		
		append(mask).
		append(numberOfClicks).
		append(button).
    toHashCode();		
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;		
		if (obj.getClass() != getClass()) return false;		
		
		ClickBinding other = (ClickBinding) obj;
	  return new EqualsBuilder()		
		.append(mask, other.mask)
		.append(numberOfClicks, other.numberOfClicks)
		.append(button, other.button)
		.isEquals();
	}	
	
  /**
   * Defines a mouse single click shortcut from the given mouse button. 
   * 
   * @param b mouse button
   */
  public ClickBinding(Integer b) {
  	this(0, b, 1);
  }
  
  /**
   * Defines a mouse single click shortcut from the given mouse button
   * and modifier mask.
   *      
   * @param m modifier mask
   * @param b mouse button
   */
  /**
  public ClickBinding(Integer m, Integer b) {
  	this(m, b, 1);
  }
  */
  
  /**
   * Defines a mouse click shortcut from the given mouse button and
   * number of clicks. 
   * 
   * @param b mouse button
   * @param c number of clicks
   */
  public ClickBinding(Integer b, Integer c) {
  	this(0, b, c);
  }
	
		
	/**
	 * Defines a mouse click shortcut from the given mouse button,
	 * modifier mask, and number of clicks.
	 * 
	 * @param m modifier mask
	 * @param b mouse button
	 * @param c bumber of clicks
	 */
	public ClickBinding(Integer m, Integer b, Integer c) {
		this.mask = m;
		this.button = b;
		if(c <= 0)
			this.numberOfClicks = 1;
		else
			this.numberOfClicks = c;
	}
	
	/**
	 * Returns a textual description of this click shortcut.
	 *  
	 * @return description
	 */
	public String description() {
		String description = new String();
		if(mask != 0)
			description += DLEvent.getModifiersText(mask) + " + ";
		switch (button) {
		case LEFT :
			description += "LEFT_BUTTON";
			break;
		case CENTER :
			description += "MIDDLE_BUTTON";
			break;
		case RIGHT :
			description += "RIGHT_BUTTON";
			break;		
		}
		if(numberOfClicks==1)
		  description += " + " + numberOfClicks.toString() + " click";
		else
			description += " + " + numberOfClicks.toString() + " clicks";
		return description;
	}
	
	private final Integer mask;
	private final Integer numberOfClicks;
	private final Integer button;
}