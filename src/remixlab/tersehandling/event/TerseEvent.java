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

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

import remixlab.tersehandling.core.Copyable;
import remixlab.tersehandling.core.EventConstants;
import remixlab.tersehandling.event.shortcut.Shortcut;

public class TerseEvent implements EventConstants, Copyable {
	@Override
	public int hashCode() {
    return new HashCodeBuilder(17, 37).		
		//append(action).
		append(modifiers).
		append(timestamp).
    toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;		
		if (obj.getClass() != getClass()) return false;		
		
		TerseEvent other = (TerseEvent) obj;
	  return new EqualsBuilder()		
		//.append(action, other.action)
		.append(modifiers, other.modifiers)
		.append(timestamp, other.timestamp)
		.isEquals();
	}
	
  protected final Integer modifiers;
  protected Long timestamp;
  
  public TerseEvent() {
    this.modifiers = 0;
    timestamp = System.currentTimeMillis();
  }
 
  public TerseEvent(Integer modifiers) {
    this.modifiers = modifiers;
    //this.action = null;
    timestamp = System.currentTimeMillis();
  }  
  
  protected TerseEvent(TerseEvent other) {
		this.modifiers = new Integer(other.modifiers);
		this.timestamp = new Long(other.timestamp);
	}  
  
  @Override
	public TerseEvent get() {
		return new TerseEvent(this);
	}
  
  public Shortcut shortcut() {
  	return new Shortcut(getModifiers());
  }
  
  public Integer getModifiers() {
    return modifiers;
  }
  
  public long timestamp() {
  	return timestamp;
  }
  
  public boolean isNull() {
  	return false;
  }

  public boolean isShiftDown() {
    return (modifiers & TH_SHIFT) != 0;
  }

  public boolean isControlDown() {
    return (modifiers & TH_CTRL) != 0;
  }

  public boolean isMetaDown() {
    return (modifiers & TH_META) != 0;
  }

  public boolean isAltDown() {
    return (modifiers & TH_ALT) != 0;
  }
  
  public boolean isAltGraph() {
    return (modifiers & TH_ALT_GRAPH) != 0;
  }
  
	public static String getModifiersText(int mask) {
		String r = new String();
		if((TH_ALT & mask)       == TH_ALT) r += "ALT";						
		if((TH_SHIFT & mask)     == TH_SHIFT) r += (r.length() > 0) ? "+SHIFT" : "SHIFT";
		if((TH_CTRL & mask)      == TH_CTRL) r += (r.length() > 0) ? "+CTRL" : "CTRL";
		if((TH_META & mask)      == TH_META) r += (r.length() > 0) ? "+META" : "META";
		if((TH_ALT_GRAPH & mask) == TH_ALT_GRAPH) r += (r.length() > 0) ? "+ALT_GRAPH" : "ALT_GRAPH";
		return r;
	}
}
