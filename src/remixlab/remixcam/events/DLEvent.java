package remixlab.remixcam.events;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

import remixlab.remixcam.core.AbstractScene;
import remixlab.remixcam.core.Constants;
import remixlab.remixcam.core.Copyable;

public class DLEvent implements Constants, Copyable {
	@Override
	public int hashCode() {
    return new HashCodeBuilder(17, 37).		
		append(action).
		append(modifiers).
    toHashCode();		
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;		
		if (obj.getClass() != getClass()) return false;		
		
		DLEvent other = (DLEvent) obj;
	  return new EqualsBuilder()		
		.append(action, other.action)
		.append(modifiers, other.modifiers)
		.isEquals();
	}
	
  protected Integer action;
  protected Integer modifiers;

  // Types of events. As with all constants in Processing, brevity's preferred.
  static public final int KEY = 1;
  static public final int MOUSE = 2;
  static public final int TOUCH = 3;
  protected int flavor;

  public DLEvent(Integer action, Integer modifiers) {        
    this.action = action;
    this.modifiers = modifiers;
  }
  
  public DLEvent(Integer action) { 
    this(action, null);
  }
  
  protected DLEvent(DLEvent other) {
		this.action = other.action;
		this.modifiers = other.modifiers;
	}
  
  public DLEvent() { 
    this(null, null);
  }
  
  @Override
	public Object get() {
		return new DLEvent(this);
	}

  public Integer getFlavor() {
    return flavor;
  }

  public Integer getAction() {
    return action;
  }

  public Integer getModifiers() {
    return modifiers;
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
	
	/**
	 * Attempt to add a 'feed' handler method to the HIDevice. The default feed
	 * handler is a method that returns void and has one single HIDevice parameter.
	 * 
	 * @param obj the object to handle the feed
	 * @param methodName the method to execute the feed in the object handler class
	 * 
	 * @see #removeHandler()
	 * @see #invoke()
	 */
	public void addHandler(Object obj, String methodName) {
		AbstractScene.showMissingImplementationWarning("addHandler");
	}
	
	/**
	 * Unregisters the 'feed' handler method (if any has previously been added to
	 * the HIDevice).
	 * 
	 * @see #addHandler(Object, String)
	 * @see #invoke()
	 */
	public void removeHandler() {
		AbstractScene.showMissingImplementationWarning("removeHandler");
	}
	
	/**
	 * called by {@link #handle()}. Invokes the method added by
	 * {@link #addHandler(Object, String)}. Returns {@code true} if
	 * succeeded and {@code false} otherwise (e.g., no method was added).
	 * 
	 * @see #addHandler(Object, String)
	 * @see #removeHandler()
	 */
	public boolean invoke() {
		AbstractScene.showMissingImplementationWarning("invoke");
		return false;
	}
}
