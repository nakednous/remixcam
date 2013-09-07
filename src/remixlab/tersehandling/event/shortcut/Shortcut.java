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



//import remixlab.dandelion.core.Constants;
import remixlab.tersehandling.core.Copyable;
import remixlab.tersehandling.core.EventConstants;
import remixlab.tersehandling.event.*;
import remixlab.tersehandling.gwthashcodeequals.EqualsBuilder;
import remixlab.tersehandling.gwthashcodeequals.HashCodeBuilder;

public class Shortcut implements EventConstants, Copyable {
	@Override
	public int hashCode() {
    return new HashCodeBuilder(17, 37).		
		append(mask).
    toHashCode();		
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;		
		if (obj.getClass() != getClass()) return false;		
		
		Shortcut other = (Shortcut) obj;
	  return new EqualsBuilder()		
		.append(mask, other.mask)
		.isEquals();
	}	
	
	//TODO pending ButtonShortcut fix!
	//protected final Integer mask;
	protected Integer mask;
	
	public Shortcut(Integer m) {
		mask = m;
	}
	
	public Shortcut() {
		mask = TH_NOMODIFIER_MASK;
	}
	
	protected Shortcut(Shortcut other) {
		this.mask = new Integer(other.mask);
	}
	
	@Override
	public Shortcut get() {
		return new Shortcut(this);
	}
	
	public String description() {
		return TerseEvent.getModifiersText(mask);
	}
}
