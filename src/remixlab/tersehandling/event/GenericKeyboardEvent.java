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

import java.util.HashMap;
import java.util.Map.Entry;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

import remixlab.tersehandling.shortcut.KeyboardShortcut;

public class GenericKeyboardEvent extends GenericEvent {
	@Override
	public int hashCode() {
    return new HashCodeBuilder(17, 37).
    appendSuper(super.hashCode()).
		append(key).
		append(vKey).
    toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;		
		if (obj.getClass() != getClass()) return false;
		
		GenericKeyboardEvent other = (GenericKeyboardEvent) obj;
		return new EqualsBuilder()
    .appendSuper(super.equals(obj))		
    .append(key, other.key)
		.append(vKey, other.vKey)
		.isEquals();
	}
	
	public static HashMap<Character, Integer> map = new HashMap<Character, Integer>() {	
		private static final long serialVersionUID = 1L;
		{
			put(' ', 32);
			put('0', 48);
			put('1', 49);
			put('2', 50);
			put('3', 51);
			put('4', 52);
			put('5', 53);
			put('6', 54);
			put('7', 55);
			put('8', 56);
			put('9', 57);
			put('a', 65);
	    put('b', 66);
	    put('c', 67);
	    put('d', 68);
	    put('e', 69);
	    put('f', 70);
	    put('g', 71);
	    put('h', 72);
	    put('i', 73);
	    put('j', 74);
	    put('k', 75);
	    put('l', 76);
	    put('m', 77);
	    put('n', 78);
	    put('o', 79);
	    put('p', 80);
	    put('q', 81);
	    put('r', 82);
	    put('s', 83);
	    put('t', 84);
	    put('u', 85);
	    put('v', 86);
	    put('w', 87);
	    put('x', 88);
	    put('y', 89);
	    put('z', 90);
	    put('A', 65);
	    put('B', 66);
	    put('C', 67);
	    put('D', 68);
	    put('E', 69);
	    put('F', 70);
	    put('G', 71);
	    put('H', 72);
	    put('I', 73);
	    put('J', 74);
	    put('K', 75);
	    put('L', 76);
	    put('M', 77);
	    put('N', 78);
	    put('O', 79);
	    put('P', 80);
	    put('Q', 81);
	    put('R', 82);
	    put('S', 83);
	    put('T', 84);
	    put('U', 85);
	    put('V', 86);
	    put('W', 87);
	    put('X', 88);
	    put('Y', 89);
	    put('Z', 90);
	  }
	};
  
  protected Character key;
  protected Integer vKey;
  
  public GenericKeyboardEvent() {
  	this.key = null;
  	this.vKey = null;
  }
  
  public GenericKeyboardEvent(Integer modifiers, Character c, Integer vk) {
    super(modifiers);
    this.vKey = vk;
    this.key = c;
  }
  
  public GenericKeyboardEvent(Integer modifiers, Character c) {
    super(modifiers);
    this.key = c;
    this.vKey = null;
  }
  
  public GenericKeyboardEvent(Integer modifiers, Integer vk) {
    super(modifiers);
    this.key = null;
    this.vKey = vk;
  }
  
  public GenericKeyboardEvent(Character c) {
  	super();
    this.key = c;
    this.vKey = null;
  }
  
  protected GenericKeyboardEvent(GenericKeyboardEvent other) {
  	super(other);
		this.key = new Character(other.key);
		this.vKey = new Integer(other.vKey);
	}
  
  @Override
	public GenericKeyboardEvent get() {
		return new GenericKeyboardEvent(this);
	}
  
  @Override
  public KeyboardShortcut shortcut() {
  	return new KeyboardShortcut(getModifiers(), getKeyCode());
  }
  
  //TODO hack
  public KeyboardShortcut keyShortcut() {
  	return new KeyboardShortcut(getKey());
  }
  
  public Character getKey() {
  	return key;
  }
  
  public Integer getKeyCode() {
    return vKey;
  }	
  
  /**
  public void setKeyCode(Integer vk) {
  	this.vKey = vk;
  }
  
  public void setKey(Character k) {
  	this.key = k;
  }  
  */
  
  public void setCharacterKeyCode(Character c, Integer i) {
  	map.put(c, i);
  }
  
  public boolean isKeyCodeInUse(Character c) {
		return map.containsKey(c);
	}
  
  public boolean isCharacterInUse(Integer i) {
		return map.containsValue(i);
	}
  
  /**
	 * Function that maps characters to virtual keys defined according to
	 * {@code java.awt.event.KeyEvent}.
	 */
  public static Integer getKeyCode(Character key) {
  	return map.get(key);
  }
	
	public String getKeyText() {
		return getKeyText(vKey);
	}
	
	/**
	 * Wrapper function that simply returns what
	 * {@code java.awt.event.KeyEvent.getKeyText(key)} would return.
	 */
	public static String getKeyText(Integer key) {
    String result = "Unrecognized key";
    Character c = null;
    for (Entry<Character, Integer> entry : map.entrySet()) {
      if (entry.getValue().equals(key)) {
      	c = entry.getKey();
      }
    }
    
    if (c != null)
    	result = c.toString();
    
    else {
    	switch (key) {        
        case LEFT : result = "LEFT"; break;
        case UP : result = "UP"; break;
        case RIGHT : result = "RIGHT"; break;        
        case DOWN : result = "DOWN"; break;
        
        //default: result = "Unrecognized key";
          //       break;
      }
    }
    return result;
	}
}
