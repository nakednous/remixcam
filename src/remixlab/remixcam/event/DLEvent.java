package remixlab.remixcam.event;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

import remixlab.remixcam.core.AbstractScene;
import remixlab.remixcam.core.Constants;
import remixlab.remixcam.core.Copyable;
import remixlab.remixcam.device.HIDevice;

public class DLEvent implements Constants, Copyable {
	@Override
	public int hashCode() {
    return new HashCodeBuilder(17, 37).		
		//append(action).
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
		//.append(action, other.action)
		.append(modifiers, other.modifiers)
		.isEquals();
	}
	
  //protected Integer action;
  protected Integer modifiers;
  //protected Integer ds;//TODO pending
  //HIDevice hid;//TODO pending
  
  public DLEvent() {    
    this.modifiers = 0;
  }
 
  public DLEvent(Integer modifiers) {
    this.modifiers = modifiers;
  }
  
  protected DLEvent(DLEvent other) {
		this.modifiers = other.modifiers;
	}  
  
  public int dofs() {
  	//TODO pending
  	return 0;
  }
  
  public void queueEvent(AbstractScene scn) {
  	scn.enqueueEvent(this);
  }
  
  @Override
	public DLEvent get() {
		return new DLEvent(this);
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
