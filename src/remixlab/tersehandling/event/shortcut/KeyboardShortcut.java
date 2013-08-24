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

package remixlab.tersehandling.event.shortcut;

import remixlab.tersehandling.event.TerseEvent;
import remixlab.tersehandling.event.KeyboardEvent;
import remixlab.util.*;
import remixlab.util.gwthashcodeequals.EqualsBuilder;
import remixlab.util.gwthashcodeequals.HashCodeBuilder;

/**
 * This class represents keyboard shortcuts.
 * <p>
 * Keyboard shortcuts can be of one out of three forms: 1. Characters (e.g., 'a');
 * 2. Virtual keys (e.g., right arrow key); or, 3. Key combinations (e.g., 'a' + CTRL key).
 */
public final class KeyboardShortcut extends Shortcut implements Copyable {
	@Override
	public int hashCode() {
    return new HashCodeBuilder(17, 37).		
    appendSuper(super.hashCode()).
		append(vKey).
		append(key).
    toHashCode();		
	}
	
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;		
		if (obj.getClass() != getClass()) return false;
		
		KeyboardShortcut rhs = (KeyboardShortcut) obj;
		return new EqualsBuilder()
		.appendSuper(super.equals(obj))
    .append(vKey, rhs.vKey)
    .append(key, rhs.key)
    .isEquals();
	}		
	
	/**
	 * Defines a keyboard shortcut from the given character.
	 *  
	 * @param k the character that defines the keyboard shortcut.
	 */
	public KeyboardShortcut(Character k) {
		super();
		this.key = k;
		//this.mask = null; //TODO test
		this.vKey = null;
	}
	
	/**
	 * Defines a keyboard shortcut from the given virtual key.
	 * 
	 * @param vk the virtual key that defines the keyboard shortcut.
	 */
	public KeyboardShortcut(Integer vk) {
		this(0, vk);
	}

	/**
	 * Defines a keyboard shortcut from the given modifier mask and virtual key combination.
	 * 
	 * @param m the mask 
	 * @param vk the virtual key that defines the keyboard shortcut.
	 */
	public KeyboardShortcut(Integer m, Integer vk) {
		super(m);
		this.vKey = vk;
		this.key = null;
	}
	
	protected KeyboardShortcut(KeyboardShortcut other) {
		super(other);
		this.vKey = new Integer(other.vKey);
		this.key = new Character(other.key);
	}
	
	@Override
	public KeyboardShortcut get() {
		return new KeyboardShortcut(this);
	}
	
	/**
	 * Returns a textual description of this keyboard shortcut.
	 *  
	 * @return description
	 */
	public String description() {
		String description = new String();
		if(key != null)
			description = key.toString();
		else {
			if(mask == 0)
				description = KeyboardEvent.getKeyText(vKey);
			else
				description = TerseEvent.getModifiersText(mask) + "+" + KeyboardEvent.getKeyText(vKey);
		}			
		return description;
	}

	protected final Integer vKey;
	protected final Character key;
}