package remixlab.remixcam.event;

import remixlab.remixcam.geom.Geom;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

public class DOF6Event extends DOF3Event {
	@Override
	public int hashCode() {
    return new HashCodeBuilder(17, 37).
    appendSuper(super.hashCode()).
		append(rx).
		append(ry).
		append(rz).
		append(drx).
		append(dry).
		append(drz).
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
		.append(rx, other.rx)
		.append(ry, other.ry)
		.append(rz, other.rz)
		.append(drx, other.drx)
		.append(dry, other.dry)
		.append(drz, other.drz)
		.isEquals();
	}

  protected Float rx, ry, rz, drx, dry, drz;  

	public DOF6Event(float x, float y, float z, float rx, float ry, float rz, int modifiers, int button) {
    super(x, y, z, modifiers, button);
    this.rx = rx;
    this.ry = ry;
    this.rz = rz;
    this.drx = 0f;
    this.dry = 0f;
    this.drz = 0f;
  }
	
	public DOF6Event(DOF6Event prevEvent, float x, float y, float z, float rx, float ry, float rz, int modifiers, int button) {
    this(x, y, z, rx, ry, rz, modifiers, button);
    distance = Geom.distance(x, y, z, rx, ry, rz,
    		                     prevEvent.getX(), prevEvent.getY(), prevEvent.getZ(), prevEvent.getRX(), prevEvent.getRY(), prevEvent.getRZ());
    if( sameSequence(prevEvent) ) {
    	this.dx = this.getX() - prevEvent.getX();
    	this.dy = this.getY() - prevEvent.getY();
    	this.dz = this.getZ() - prevEvent.getZ();
    	this.drx = this.getRX() - prevEvent.getRX();
    	this.dry = this.getRY() - prevEvent.getRY();
    	this.drz = this.getRZ() - prevEvent.getRZ();
    	this.action = prevEvent.getAction();
    }
  }
	
	//ready to be enqueued
	public DOF6Event(float x, float y, float z, float rx, float ry, float rz, DLAction a) {
    super(x, y, z, a);
    this.rx = rx;
    this.ry = ry;
    this.rz = rz;
    this.drx = 0f;
    this.dry = 0f;
    this.drz = 0f;
    this.button = NOBUTTON;
	}

	//idem
	public DOF6Event(DOF6Event prevEvent, float x, float y, float z, float rx, float ry, float rz, DLAction a) {
    super(prevEvent, x, y, z, a);
    this.rx = rx;
    this.ry = ry;
    this.rz = rz;
    this.button = NOBUTTON;    
    distance = Geom.distance(x, y, z, rx, ry, rz,
                             prevEvent.getX(), prevEvent.getY(), prevEvent.getZ(), prevEvent.getRX(), prevEvent.getRY(), prevEvent.getRZ());
    if( sameSequence(prevEvent) ) {
    	this.dx = this.getX() - prevEvent.getX();
    	this.dy = this.getY() - prevEvent.getY();
    	this.dz = this.getZ() - prevEvent.getZ();
    	this.drx = this.getRX() - prevEvent.getRX();
    	this.dry = this.getRY() - prevEvent.getRY();
    	this.drz = this.getRZ() - prevEvent.getRZ();
    }
	}
  
  protected DOF6Event(DOF6Event other) {
  	super(other);
  	this.rx = other.rx;
  	this.ry = other.ry;
  	this.rz = other.rz;
  	this.drx = other.drx;
  	this.dry = other.dry;
  	this.drz = other.drz;
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
  
  public float getDRX() {
    return drx;
  }
  
  public float getDRY() {
    return dry;
  }
  
  public float getDRZ() {
    return drz;
  }
  
  public float getPrevRX() {
  	return getRX() - getDRX();
  }
  
  public float getPrevRY() {
  	return getRY() - getDRY();
  }
  
  public float getPrevRZ() {
  	return getRZ() - getDRZ();
  }
}
