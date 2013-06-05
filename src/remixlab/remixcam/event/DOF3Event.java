package remixlab.remixcam.event;

import remixlab.remixcam.geom.Geom;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;
import remixlab.remixcam.core.Constants;

public class DOF3Event extends MotionEvent<Constants.DOF_3Action> {
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
    .append(x, other.x)
		.append(dx, other.dx)
		.append(y, other.y)
		.append(dy, other.dy)
		.append(z, other.z)
		.append(dz, other.dz)
		.isEquals();
	}

	protected Float x, dx;
  protected Float y, dy;
  protected Float z, dz;
  
	public DOF3Event(float x, float y, float z, int modifiers, int button) {
    super(modifiers, button);
		this.x = x;
		this.dx = 0f;
    this.y = y;
    this.dy = 0f;
    this.z = z;
    this.dz = 0f;
  }
	
	public DOF3Event(DOF3Event prevEvent, float x, float y, float z, int modifiers, int button) {
    this(x, y, z, modifiers, button);
    if(prevEvent!=null) {
    	distance = Geom.distance(x, y, z, prevEvent.getX(), prevEvent.getY(), prevEvent.getZ());
    	if( sameSequence(prevEvent) ) {
    		this.dx = this.getX() - prevEvent.getX();
    		this.dy = this.getY() - prevEvent.getY();
    		this.dz = this.getZ() - prevEvent.getZ();
    		this.action = prevEvent.getAction();
    	}
    }
  }
	
	//ready to be enqueued
	public DOF3Event(float x, float y, float z, DOF_3Action a) {
    super(a);
    this.x = x;
		this.dx = 0f;
    this.y = y;
    this.dy = 0f;
    this.z = z;
    this.dz = 0f;
    this.button = NOBUTTON;
	}

	//idem
	public DOF3Event(DOF3Event prevEvent, float x, float y, float z, DOF_3Action a) {
    super(a);
    this.x = x;
		this.dx = 0f;
    this.y = y;
    this.dy = 0f;
    this.z = z;
    this.dz = 0f;
    this.button = NOBUTTON;
    if(prevEvent!=null) {
    	distance = Geom.distance(x, y, z, prevEvent.getX(), prevEvent.getY(), prevEvent.getZ());
    	if( sameSequence(prevEvent) ) {
    		this.dx = this.getX() - prevEvent.getX();
    		this.dy = this.getY() - prevEvent.getY();
    		this.dz = this.getZ() - prevEvent.getZ();
    	}
    }
	}
  
  protected DOF3Event(DOF3Event other) {
  	super(other);
		this.x = other.x;
		this.dx = other.dx;
  	this.y = other.y;
  	this.dy = other.dy;
  	this.z = other.z;
  	this.dz = other.z;
	}
  
  @Override
	public DOF3Event get() {
		return new DOF3Event(this);
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
  
  public DOF2Event dof2Event(DOF_2Action a2) {
  	DOF2Event e2 = dof2Event();
  	e2.setAction(a2);
  	return e2;
  }
  
  public DOF2Event dof2Event() {
  	DOF2Event pe2;
  	DOF2Event e2;
  	if(relative()) {  		
  			pe2 = new DOF2Event(getPrevX(), getPrevY(), getModifiers(), getButton());
  			e2 = new DOF2Event(pe2, getX(), getY(), getModifiers(), getButton());  		
  	}
  	else {
  		e2 = new DOF2Event(getX(), getY(), getModifiers(), getButton()); 
  	}
  	return e2;
  }
}
