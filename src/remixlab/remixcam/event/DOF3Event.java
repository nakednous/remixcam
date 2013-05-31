package remixlab.remixcam.event;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

public class DOF3Event extends DOF2Event {
	@Override
	public int hashCode() {
    return new HashCodeBuilder(17, 37).
    appendSuper(super.hashCode()).
		append(z).
    toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;		
		if (obj.getClass() != getClass()) return false;
		
		DOF3Event other = (DOF3Event) obj;
		return new EqualsBuilder()
    .appendSuper(super.equals(obj))		
		.append(z, other.z)
		.isEquals();
	}

  protected Float z;
  
  public DOF3Event(float x, float y, float z) {
  	super(x, y);
  	this.z = z;
  }
  
  public DOF3Event(float x, float y, float z, int button) {
  	super(x, y, button);
  	this.z = z;
  }
  
  public DOF3Event(float x, float y, float z, int modifiers, int button) {
  	super(x, y, modifiers, button);
  	this.z = z;
  }  
  
  public DOF3Event(float x, float y, float z, DLAction a) {
  	super(x, y, a);
  	this.z = z;
  }
  
  public DOF3Event(float x, float y, float z, int button, DLAction a) {
  	super(x, y, button, a);
  	this.z = z;
  }

  public DOF3Event(float x, float y, float z, int modifiers, int button, DLAction a) {
    super(x, y, modifiers, button, a);
    this.z = z;
  }
  
  protected DOF3Event(DOF3Event other) {
  	super(other);
  	this.z = other.z;
	}
  
  @Override
	public DOF3Event get() {
		return new DOF3Event(this);
	}
  
  public float getZ() {
    return z;
  }
  
  public static DOF3Event deltaEvent(DOF3Event current, DOF3Event prev) {
  	return new DOF3Event((current.getX() - prev.getX()), (current.getY() - prev.getY()), (current.getZ() - prev.getZ()), current.modifiers, current.button, current.action);
  }
  
  public DOF3Event deltaEvent(DOF3Event prev) {
  	return deltaEvent(this, prev);
  }
}
