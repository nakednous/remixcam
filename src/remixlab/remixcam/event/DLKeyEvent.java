package remixlab.remixcam.event;

import java.util.HashMap;
import java.util.Map.Entry;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

public class DLKeyEvent extends DLEvent {
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
		
		DLKeyEvent other = (DLKeyEvent) obj;
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
  
  Character key;
  Integer vKey;
  
  public DLKeyEvent() {
  	this.key = null;
  	this.vKey = null;
  }
  
  public DLKeyEvent(Integer modifiers, Character c, Integer vk) {
    super(modifiers);
    this.vKey = vk;
    this.key = c;
  }
  
  public DLKeyEvent(Integer modifiers, Character c) {
    super(modifiers);
    this.key = c;
    this.vKey = null;
  }
  
  public DLKeyEvent(Integer modifiers, Integer vk) {
    super(modifiers);
    this.key = null;
    this.vKey = vk;
  }
  
  public DLKeyEvent(Character c) {
  	super();
    this.key = c;
    this.vKey = null;
  }
  
  protected DLKeyEvent(DLKeyEvent other) {
  	super(other);
		this.key = other.key;
		this.vKey = other.vKey;
	}
  
  @Override
	public DLKeyEvent get() {
		return new DLKeyEvent(this);
	}
  
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
  
  public void setKeyCode(Integer vk) {
  	this.vKey = vk;
  }
  
  public void setKey(Character k) {
  	this.key = k;
  }

  public Character getKey() {
  	return key;
  }
  
  public Integer getKeyCode() {
    return vKey;
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
