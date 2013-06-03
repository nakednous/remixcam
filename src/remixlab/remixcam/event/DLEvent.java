package remixlab.remixcam.event;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

import remixlab.remixcam.core.*;
import remixlab.remixcam.shortcut.Shortcut;

public class DLEvent<A extends Constants.Actionable> implements Constants, Copyable {
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
	
  protected DLAction action;
  protected final Integer modifiers;
  protected final Long timestamp;
  
  public DLEvent() {
    this.modifiers = 0;
    this.action = DLAction.NO_ACTION;
    timestamp = System.currentTimeMillis();
  }
 
  public DLEvent(Integer modifiers) {
    this.modifiers = modifiers;
    this.action = DLAction.NO_ACTION;
    timestamp = System.currentTimeMillis();
  }  
  
  public DLEvent(A a) {    
    this.modifiers = 0;
    this.action = a.action();
    if(action == null)
    	action = DLAction.NO_ACTION;
    timestamp = System.currentTimeMillis();
  }
 
  public DLEvent(Integer modifiers, A a) {
    this.modifiers = modifiers;
    this.action = a.action();
    if(action == null)
    	action = DLAction.NO_ACTION;
    timestamp = System.currentTimeMillis();
  } 
  
  protected DLEvent(DLEvent<?> other) {
		this.modifiers = other.modifiers;
		this.action = other.action;
		timestamp = System.currentTimeMillis();
	}  
  
  public void enqueue(AbstractScene scn) {
  	scn.enqueueEvent(this);
  }
  
  @Override
	public DLEvent<?> get() {
		return new DLEvent<A>(this);
	}
  
  public Shortcut shortcut() {
  	System.out.println("calling DLEvent shortcut");
  	return new Shortcut(getModifiers());
  }
  
  public DLAction getAction() {
  	return action;
  }
  
  public void setAction(Actionable actionable) {
  	if(actionable != null)
  		setAction(actionable.action());
  	else
  		setAction(DLAction.NO_ACTION);
  }
  
  protected void setAction(DLAction a) {
  	if(a == null)
    	action = DLAction.NO_ACTION;
  	else
  		action = a;
  }

  public Integer getModifiers() {
    return modifiers;
  }
  
  public long timestamp() {
  	return timestamp;
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
