package remixlab.remixcam.event;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

public class DOF2Event extends DOF1Event {
	@Override
	public int hashCode() {
    return new HashCodeBuilder(17, 37).
    appendSuper(super.hashCode()).
		append(y).
    toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;		
		if (obj.getClass() != getClass()) return false;
		
		DOF2Event other = (DOF2Event) obj;
		return new EqualsBuilder()
    .appendSuper(super.equals(obj))		
		.append(y, other.y)
		.isEquals();
	}

  protected Float y;
  
  public DOF2Event(float x, float y) {
  	super(x);
  	this.y = y;
  }
  
  public DOF2Event(float x, float y, int button) {
  	super(x, button);
  	this.y = y;
  }
  
  public DOF2Event(float x, float y, int modifiers, int button) {
  	super(x, modifiers, button);
  	this.y = y;
  }  
  
  public DOF2Event(float x, float y, DLAction a) {
  	super(x, a);
  	this.y = y;
  }
  
  public DOF2Event(float x, float y, int button, DLAction a) {
  	super(x, button, a);
  	this.y = y;
  }

  public DOF2Event(float x, float y, int modifiers, int button, DLAction a) {
    super(x, modifiers, button, a);
    this.y = y;
  }
  
  protected DOF2Event(DOF2Event other) {
  	super(other);
  	this.y = other.y;
	}
  
  @Override
	public DOF2Event get() {
		return new DOF2Event(this);
	}
  
  public float getY() {
    return y;
  }
  
  public static DOF2Event deltaEvent(DOF2Event current, DOF2Event prev) {
  	return new DOF2Event((current.getX() - prev.getX()), (current.getY() - prev.getY()), current.modifiers, current.button, current.action);
  }
  
  public DOF2Event deltaEvent(DOF2Event prev) {
  	return deltaEvent(this, prev);
  }
}
