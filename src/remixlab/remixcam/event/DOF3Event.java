package remixlab.remixcam.event;

import remixlab.remixcam.geom.Geom;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

public class DOF3Event extends DOF2Event {
	@Override
	public int hashCode() {
    return new HashCodeBuilder(17, 37).
    appendSuper(super.hashCode()).
		append(z).
		append(dz).
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
		.append(dz, other.dz)
		.isEquals();
	}

  protected Float z, dz;
  
	public DOF3Event(float x, float y, float z, int modifiers, int button) {
    super(x, y, modifiers, button);
    this.z = z;
    this.dz = 0f;
  }
	
	public DOF3Event(DOF3Event prevEvent, float x, float y, float z, int modifiers, int button) {
    this(x, y, z, modifiers, button);
    distance = Geom.distance(x, y, z, prevEvent.getX(), prevEvent.getY(), prevEvent.getZ());
    if( sameSequence(prevEvent) ) {
    	this.dx = this.getX() - prevEvent.getX();
    	this.dy = this.getY() - prevEvent.getY();
    	this.dz = this.getZ() - prevEvent.getZ();
    	this.action = prevEvent.getAction();    	
    }
  }
	
	//ready to be enqueued
	public DOF3Event(float x, float y, float z, DLAction a) {
    super(x, y, a);
    this.z = z;
    this.dz = 0f;
    this.button = NOBUTTON;
	}

	//idem
	public DOF3Event(DOF3Event prevEvent, float x, float y, float z, DLAction a) {
    super(prevEvent, x, y, a);
    this.z = z;
    this.dz = 0f;
    this.button = NOBUTTON;    
    distance = Geom.distance(x, y, z, prevEvent.getX(), prevEvent.getY(), prevEvent.getZ());
    if( sameSequence(prevEvent) ) {
    	this.dx = this.getX() - prevEvent.getX();
    	this.dy = this.getY() - prevEvent.getY();
    	this.dz = this.getZ() - prevEvent.getZ();
    }
	}
  
  protected DOF3Event(DOF3Event other) {
  	super(other);
  	this.z = other.z;
  	this.dz = other.z;
	}
  
  @Override
	public DOF3Event get() {
		return new DOF3Event(this);
	}
  
  public float getZ() {
    return z;
  }
  
  public float getDZ() {
    return dz;
  }
  
  public float getPrevZ() {
  	return getZ() - getDZ();
  }
}
