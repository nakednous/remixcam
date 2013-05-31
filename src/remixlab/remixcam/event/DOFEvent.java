package remixlab.remixcam.event;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

//TODO should decide whether or not this class should go
//i.e., wheel event may be understood as a DOF1Event with button = 0
public class DOFEvent extends DLEvent {
	@Override
	public int hashCode() {
    return new HashCodeBuilder(17, 37).
    appendSuper(super.hashCode()).
		append(x).
    toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;		
		if (obj.getClass() != getClass()) return false;
		
		DOFEvent other = (DOFEvent) obj;
		return new EqualsBuilder()
    .appendSuper(super.equals(obj))
		.append(x, other.x)
		.isEquals();
	}

	protected Float x;
	
  public DOFEvent(float x) {
  	super();
  	this.x = x;
  }
  
  public DOFEvent(float x, int modifiers) {
  	super(modifiers);
  	this.x = x;
  }  
  
  public DOFEvent(float x, DLAction a) {
  	super(a);
  	this.x = x;
  }
  

  public DOFEvent(float x, int modifiers, DLAction a) {
    super(modifiers, a);
    this.x = x;
  }
  
  protected DOFEvent(DOFEvent other) {
  	super(other);
  	this.x = other.x;
	}
  
  @Override
	public DOFEvent get() {
		return new DOFEvent(this);
	}
  
  public float getX() {
    return x;
  }
  
  public static DOFEvent deltaEvent(DOFEvent current, DOFEvent prev) {
  	return new DOFEvent((current.getX() - prev.getX()), current.modifiers, current.action);
  }
  
  public DOFEvent deltaEvent(DOFEvent prev) {
  	return deltaEvent(this, prev);
  }
}
