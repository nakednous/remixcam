/**
 *                     RemixCam (version 0.70.0)      
 *      Copyright (c) 2013 by National University of Colombia
 *                 @author Jean Pierre Charalambos      
 *              https://github.com/nakednous/remixcam
 *                           
 * This java library provides classes to ease the creation of interactive 3D
 * scenes in various languages and frameworks such as JOGL, WebGL and Processing.
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
import remixlab.tersehandling.core.TConstants;
import remixlab.tersehandling.shortcut.Shortcut;

public class GenericEvent implements TConstants, Copyable {
	//TODO fix modifiers!
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
		
		GenericEvent other = (GenericEvent) obj;
	  return new EqualsBuilder()		
		//.append(action, other.action)
		.append(modifiers, other.modifiers)
		.append(timestamp, other.timestamp)
		.isEquals();
	}
	
  //protected Actionable<?> action;
  protected final Integer modifiers;
  protected final Long timestamp;
  
  public GenericEvent() {
    this.modifiers = 0;
    //this.action = null;
    timestamp = System.currentTimeMillis();
  }
 
  public GenericEvent(Integer modifiers) {
    this.modifiers = modifiers;
    //this.action = null;
    timestamp = System.currentTimeMillis();
  }  
  
  protected GenericEvent(GenericEvent other) {
		this.modifiers = new Integer(other.modifiers);
		//this.action = other.action;
		this.timestamp = new Long(System.currentTimeMillis());
	}  
  
  @Override
	public GenericEvent get() {
		return new GenericEvent(this);
	}
  
  public Shortcut shortcut() {
  	//System.out.println("calling DLEvent shortcut");
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
    return (modifiers & SHIFT) != 0;
  }

  public boolean isControlDown() {
    return (modifiers & CTRL) != 0;
  }

  public boolean isMetaDown() {
    return (modifiers & META) != 0;
  }

  public boolean isAltDown() {
    return (modifiers & ALT) != 0;
  }
  
  public boolean isAltGraph() {
    return (modifiers & ALT_GRAPH) != 0;
  }
  
	public static String getModifiersText(int mask) {
		String r = new String();
		if((ALT & mask)       == ALT) r += "ALT";						
		if((SHIFT & mask)     == SHIFT) r += (r.length() > 0) ? "+SHIFT" : "SHIFT";
		if((CTRL & mask)      == CTRL) r += (r.length() > 0) ? "+CTRL" : "CTRL";
		if((META & mask)      == META) r += (r.length() > 0) ? "+META" : "META";
		if((ALT_GRAPH & mask) == ALT_GRAPH) r += (r.length() > 0) ? "+ALT_GRAPH" : "ALT_GRAPH";
		return r;
	}
}
