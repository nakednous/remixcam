package remixlab.remixcam.event;

import remixlab.remixcam.core.Constants;
import remixlab.remixcam.geom.Geom;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

public class DOF6Event extends MotionEvent<Constants.DOF_6Action> {
	@Override
	public int hashCode() {
    return new HashCodeBuilder(17, 37).
    appendSuper(super.hashCode()).
		append(x).
		append(dx).
		append(y).
		append(dy).
		append(z).
		append(dz).
		append(rx).
		append(drx).
		append(ry).
		append(dry).
		append(rz).		
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
    .append(x, other.x)
		.append(dx, other.dx)
		.append(y, other.y)
		.append(dy, other.dy)
		.append(z, other.z)
		.append(dz, other.dz)
		.append(rx, other.rx)
		.append(drx, other.drx)
		.append(ry, other.ry)
		.append(dry, other.dry)
		.append(rz, other.rz)		
		.append(drz, other.drz)
		.isEquals();
	}

	protected Float x, dx;
  protected Float y, dy;
  protected Float z, dz;
  
  protected Float rx, drx;
  protected Float ry, dry;
  protected Float rz, drz;  

	public DOF6Event(float x, float y, float z, float rx, float ry, float rz, int modifiers, int button) {
    super(modifiers, button);
		this.x = x;
		this.dx = 0f;
    this.y = y;
    this.dy = 0f;
    this.z = z;
    this.dz = 0f;
    this.rx = rx;
    this.drx = 0f;
    this.ry = ry;
    this.dry = 0f;
    this.rz = rz;    
    this.drz = 0f;
  }
	
	public DOF6Event(DOF6Event prevEvent, float x, float y, float z, float rx, float ry, float rz, int modifiers, int button) {
    this(x, y, z, rx, ry, rz, modifiers, button);
    if(prevEvent!=null) {
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
  }
	
	//ready to be enqueued
	public DOF6Event(float x, float y, float z, float rx, float ry, float rz, DOF_6Action a) {
    super(a);
    this.x = x;
		this.dx = 0f;
    this.y = y;
    this.dy = 0f;
    this.z = z;
    this.dz = 0f;
    this.rx = rx;
    this.drx = 0f;
    this.ry = ry;
    this.dry = 0f;
    this.rz = rz;    
    this.drz = 0f;
    this.button = NOBUTTON;
	}

	//idem
	public DOF6Event(DOF6Event prevEvent, float x, float y, float z, float rx, float ry, float rz, DOF_6Action a) {
    super(a);
    this.x = x;
 		this.dx = 0f;
    this.y = y;
    this.dy = 0f;
    this.z = z;
    this.dz = 0f;
    this.rx = rx;
    this.drx = 0f;
    this.ry = ry;
    this.dry = 0f;
    this.rz = rz;    
    this.drz = 0f;
    this.button = NOBUTTON;
    if(prevEvent!=null) {
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
	}
  
  protected DOF6Event(DOF6Event other) {
  	super(other);
		this.x = other.x;
		this.dx = other.dx;
  	this.y = other.y;
  	this.dy = other.dy;
  	this.z = other.z;
  	this.dz = other.z;
  	this.rx = other.rx;
  	this.drx = other.drx;
  	this.ry = other.ry;
  	this.dry = other.dry;
  	this.rz = other.rz;  	
  	this.drz = other.drz;
	}
  
  @Override
	public DOF6Event get() {
		return new DOF6Event(this);
	}
  
	public float getX() {
		return x;
	}

	public float getDX() {
		return dx;
	}

	public float getPrevX() {
		return getX() - getDX();
	}
  
  public float getY() {
    return y;
  }
  
  public float getDY() {
    return dy;
  }
  
  public float getPrevY() {
  	return getY() - getDY();
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
  
  public DOF3Event dof3Event(DOF_3Action a3) {
  	return dof3Event(true, a3);
  }
  
  public DOF3Event dof3Event(boolean fromTranslation, DOF_3Action a3) {
  	DOF3Event e3 = dof3Event(fromTranslation);
  	e3.setAction(a3);
  	return e3;
  }
  
  public DOF3Event dof3Event() {
  	return dof3Event(true);
  }
  
  public DOF3Event dof3Event(boolean fromTranslation) {
  	DOF3Event pe3;
  	DOF3Event e3;
  	if(relative()) {
  		if(fromTranslation) {
  			pe3 = new DOF3Event(getPrevX(), getPrevY(), getPrevZ(), getModifiers(), getButton());
  			e3 = new DOF3Event(pe3, getX(), getY(), getZ(), getModifiers(), getButton());
  		}
  		else {
  			pe3 = new DOF3Event(getPrevRX(), getPrevRY(), getPrevRZ(), getModifiers(), getButton());
  			e3 = new DOF3Event(pe3, getRX(), getRY(), getRZ(), getModifiers(), getButton());
  		}
  	}
  	else {
  		if(fromTranslation)
    		e3 = new DOF3Event(getX(), getY(), getZ(), getModifiers(), getButton());
  		else
  			e3 = new DOF3Event(getRX(), getRY(), getRZ(), getModifiers(), getButton());  		
  	}
  	return e3;
  }
}
