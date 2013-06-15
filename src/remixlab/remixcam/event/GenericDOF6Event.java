package remixlab.remixcam.event;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

import remixlab.remixcam.core.Actionable;
import remixlab.remixcam.geom.Geom;

public class GenericDOF6Event<A extends Actionable<?>> extends GenericMotionEvent<A> {
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
		
		GenericDOF6Event<?> other = (GenericDOF6Event<?>) obj;
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

	public GenericDOF6Event(float x, float y, float z, float rx, float ry, float rz, int modifiers, int button) {
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
	
	public GenericDOF6Event(GenericDOF6Event<A> prevEvent, float x, float y, float z, float rx, float ry, float rz, int modifiers, int button) {
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
	public GenericDOF6Event(float x, float y, float z, float rx, float ry, float rz, A a) {
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
	public GenericDOF6Event(GenericDOF6Event<A> prevEvent, float x, float y, float z, float rx, float ry, float rz, A a) {
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
  
  protected GenericDOF6Event(GenericDOF6Event<A> other) {
  	super(other);
		this.x = new Float(other.x);
		this.dx = new Float(other.dx);
  	this.y = new Float(other.y);
  	this.dy = new Float(other.dy);
  	this.z = new Float(other.z);
  	this.dz = new Float(other.z);
  	this.rx = new Float(other.rx);
  	this.drx = new Float(other.drx);
  	this.ry = new Float(other.ry);
  	this.dry = new Float(other.dry);
  	this.rz = new Float(other.rz);  	
  	this.drz = new Float(other.drz);
	}
  
  @Override
	public GenericDOF6Event<A> get() {
		return new GenericDOF6Event<A>(this);
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
  
	@Override
	public void modulate(float [] sens) {
		if(sens != null)
		if(sens.length>=6 && this.absolute()) {
			x = x*sens[0];
			y = y*sens[1];
			z = z*sens[2];
			rx = rx*sens[3];
			ry = ry*sens[4];
			rz = rz*sens[5];
		}
	}
	
	@Override
	public boolean isNull() {
  	if(relative() && Geom.zero(getDX()) && Geom.zero(getDY()) && Geom.zero(getDZ()) && Geom.zero(getDRX()) && Geom.zero(getDRY()) && Geom.zero(getDRZ()))
  			return true;
  	if(absolute() && Geom.zero(getX()) && Geom.zero(getY()) && Geom.zero(getZ()) && Geom.zero(getRX()) && Geom.zero(getRY()) && Geom.zero(getRZ()))
  		return true;
  	return false;
  }
}
