package remixlab.remixcam.event;

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
    this(action, 0);
  }
  
  public DLEvent() { 
    this(0, 0);
  }

  public DLEvent(AbstractScene scn, Integer action, Integer modifiers) {
    this.action = action;
    this.modifiers = modifiers;
    queueEvent(scn);
  } 
  
  public DLEvent(AbstractScene scn, Integer action) { 
    this(scn, action, 0);
  }
  
  public DLEvent(AbstractScene scn) { 
    this(scn, 0, 0);
  }
  
  protected DLEvent(DLEvent other) {
		this.action = other.action;
		this.modifiers = other.modifiers;
	}
  
  public void queueEvent(AbstractScene scn) {
  	scn.enqueueEvent(this);
  }
  
  @Override
	public Object get() {
		return new DLEvent(this);
	}

  public Integer getFlavor() {
    return flavor;
  }
  
  public void setAction(Integer a) {
  	this.action = a;
  }

  public Integer getAction() {
    return action;
  }

  public Integer getModifiers() {
    return modifiers;
  }
  
  public void setModifiers(Integer m) {
  	this.modifiers = m;
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
