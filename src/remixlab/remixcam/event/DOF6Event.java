package remixlab.remixcam.event;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

public class DOF6Event extends DOF3Event {
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
		
		DOF6Event other = (DOF6Event) obj;
		return new EqualsBuilder()
    .appendSuper(super.equals(obj))		
		.append(z, other.z)
		.isEquals();
	}

  protected Float rx, ry, rz;
  
  public DOF6Event(float x, float y, float z, float rx, float ry, float rz) {
  	super(x, y, z);
  	this.rx = rx;
  	this.ry = ry;
  	this.rz = rz;
  }
  
  public DOF6Event(float x, float y, float z, float rx, float ry, float rz, int button) {
  	super(x, y, z, button);
  	this.rx = rx;
  	this.ry = ry;
  	this.rz = rz;
  }
  
  public DOF6Event(float x, float y, float z, float rx, float ry, float rz, int modifiers, int button) {
  	super(x, y, z, modifiers, button);
  	this.rx = rx;
  	this.ry = ry;
  	this.rz = rz;
  }  
  
  public DOF6Event(float x, float y, float z, float rx, float ry, float rz, DLAction a) {
  	super(x, y, z, a);
  	this.rx = rx;
  	this.ry = ry;
  	this.rz = rz;
  }
  
  public DOF6Event(float x, float y, float z, float rx, float ry, float rz, int button, DLAction a) {
  	super(x, y, z, button, a);
  	this.rx = rx;
  	this.ry = ry;
  	this.rz = rz;
  }

  public DOF6Event(float x, float y, float z, float rx, float ry, float rz, int modifiers, int button, DLAction a) {
    super(x, y, z, modifiers, button, a);
    this.rx = rx;
  	this.ry = ry;
  	this.rz = rz;
  }
  
  protected DOF6Event(DOF6Event other) {
  	super(other);
  	this.rx = other.rx;
  	this.ry = other.ry;
  	this.rz = other.rz;
	}
  
  @Override
	public DOF6Event get() {
		return new DOF6Event(this);
	}
  
  public float roll() {
  	return getRX();
  }
  
  public float getRX() {
    return rx;
  }
  
  public float pitch() {
  	return getRY();
  }
  
  public float getRY() {
    return ry;
  }
  
  public float yaw() {
  	return getRZ();
  }
  
  public float getRZ() {
    return rz;
  }
  
  public static DOF6Event deltaEvent(DOF6Event current, DOF6Event prev) {
  	return new DOF6Event((current.getX() - prev.getX()), (current.getY() - prev.getY()), (current.getZ() - prev.getZ()),
  			         				 (current.getRX() - prev.getRX()), (current.getY() - prev.getRY()), (current.getRZ() - prev.getRZ()),
  			                  current.modifiers, current.button, current.action);
  }
  
  public DOF6Event deltaEvent(DOF6Event prev) {
  	return deltaEvent(this, prev);
  }
}
