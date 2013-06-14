package remixlab.remixcam.event;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

import remixlab.remixcam.action.VActionable;
import remixlab.remixcam.core.*;
import remixlab.remixcam.shortcut.Shortcut;

public class DLEvent<A extends VActionable> implements Constants, Copyable {
	//TODO fix modifiers!
	@Override
	public int hashCode() {
    return new HashCodeBuilder(17, 37).		
		append(action).
		append(modifiers).
		append(timestamp).
    toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;		
		if (obj.getClass() != getClass()) return false;		
		
		DLEvent<?> other = (DLEvent<?>) obj;
	  return new EqualsBuilder()		
		.append(action, other.action)
		.append(modifiers, other.modifiers)
		.append(timestamp, other.timestamp)
		.isEquals();
	}
	
  protected VActionable action;
  protected final Integer modifiers;
  protected final Long timestamp;
  
  public DLEvent() {
    this.modifiers = 0;
    this.action = null;
    timestamp = System.currentTimeMillis();
  }
 
  public DLEvent(Integer modifiers) {
    this.modifiers = modifiers;
    this.action = null;
    timestamp = System.currentTimeMillis();
  }  
  
  public DLEvent(A a) {    
    this.modifiers = 0;
    this.action = a;
    timestamp = System.currentTimeMillis();
  }
 
  public DLEvent(Integer modifiers, A a) {
    this.modifiers = modifiers;
    this.action = a;
    timestamp = System.currentTimeMillis();
  } 
  
  protected DLEvent(DLEvent<A> other) {
		this.modifiers = new Integer(other.modifiers);
		this.action = other.action;
		this.timestamp = new Long(System.currentTimeMillis());
	}  
  
  public void enqueue(AbstractScene scn) {
  	scn.enqueueEvent(this);
  }
  
  @Override
	public DLEvent<A> get() {
		return new DLEvent<A>(this);
	}
  
  public Shortcut shortcut() {
  	//System.out.println("calling DLEvent shortcut");
  	return new Shortcut(getModifiers());
  }
  
  public VActionable getAction() {
  	return action;
  }
  
  public void setAction(VActionable actionable) {
  	action = actionable;
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

  // /**
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
	// */
  
  /**
  public boolean isShiftDown() {
    return (modifiers & SHIFT_DOWN) != 0;
  }

  public boolean isControlDown() {
    return (modifiers & CTRL_DOWN) != 0;
  }

  public boolean isMetaDown() {
    return (modifiers & META_DOWN) != 0;
  }

  public boolean isAltDown() {
    return (modifiers & ALT_DOWN) != 0;
  }
  
  public boolean isAltGraph() {
    return (modifiers & ALT_GRAPH_DOWN) != 0;
  }
  
	public static String getModifiersText(int mask) {
		String r = new String();
		if((ALT_DOWN & mask)       == ALT_DOWN) r += "ALT";						
		if((SHIFT_DOWN & mask)     == SHIFT_DOWN) r += (r.length() > 0) ? "+SHIFT" : "SHIFT";
		if((CTRL_DOWN & mask)      == CTRL_DOWN) r += (r.length() > 0) ? "+CTRL" : "CTRL";
		if((META_DOWN & mask)      == META_DOWN) r += (r.length() > 0) ? "+META" : "META";
		if((ALT_GRAPH_DOWN & mask) == ALT_GRAPH_DOWN) r += (r.length() > 0) ? "+ALT_GRAPH" : "ALT_GRAPH";
		return r;
	}
	// */
}
