/*******************************************************************************
 * TerseHandling (version 0.9.50)
 * Copyright (c) 2013 National University of Colombia, https://github.com/remixlab
 * @author Jean Pierre Charalambos, http://otrolado.info/
 *    
 * All rights reserved. Library that eases the creation of interactive
 * scenes, released under the terms of the GNU Public License v3.0
 * which is available at http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package remixlab.tersehandling.event.shortcut;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

import remixlab.tersehandling.core.Copyable;
import remixlab.tersehandling.core.EventConstants;
import remixlab.tersehandling.event.*;

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
