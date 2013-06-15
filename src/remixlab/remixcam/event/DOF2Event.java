package remixlab.remixcam.event;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

import remixlab.remixcam.core.Actionable;
import remixlab.remixcam.geom.Geom;

public class DOF2Event<A extends Actionable<?>> extends MotionEvent<A> {
	@Override
	public int hashCode() {
    return new HashCodeBuilder(17, 37).
    appendSuper(super.hashCode()).
		appendSuper(super.hashCode()).
		append(x).
		append(dx).
		append(y).
		append(dy).
    toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;		
		if (obj.getClass() != getClass()) return false;
		
		DOF2Event<?> other = (DOF2Event<?>) obj;
		return new EqualsBuilder()
    .appendSuper(super.equals(obj))		
    .append(x, other.x)
		.append(dx, other.dx)
		.append(y, other.y)
		.append(dy, other.dy)
		.isEquals();
	}

	protected Float x, dx;
  protected Float y, dy;
  
	public DOF2Event(float x, float y, int modifiers, int button) {
    super(modifiers, button);
		this.x = x;
		this.dx = 0f;
    this.y = y;
    this.dy = 0f;
  }
	
	public DOF2Event(DOF2Event<A> prevEvent, float x, float y, int modifiers, int button) {
		this(x, y, modifiers, button);
		if(prevEvent!=null) {
			distance = Geom.distance(x, y, prevEvent.getX(), prevEvent.getY()); 
			if( sameSequence(prevEvent) ) {
				this.dx = this.getX() - prevEvent.getX();
				this.dy = this.getY() - prevEvent.getY();
				this.action = prevEvent.getAction();
  		}
		}
    // */
		
    /**
    //TODO debug
    if( sameSequence(prevEvent) ) {
    	this.dx = this.getX() - prevEvent.getX();
  		this.dy = this.getY() - prevEvent.getY();
  		this.action = prevEvent.getAction();
  		System.out.println("Current event: x: " + getX() + " y: " + getY());
  		System.out.println("Prev event: x: " + getPrevX() + " y: " + getPrevY());
  		//System.out.println("Distance: " + distance());
  		//System.out.println("Delay: " + delay());
  		//System.out.println("Speed: " + speed());
    }
    else {
    	System.out.println("different sequence!");
    }
    // */
  }
	
	//ready to be enqueued
	public DOF2Event(float x, float y, A a) {
    super(a);
    this.x = x;
		this.dx = 0f;
    this.y = y;
    this.dy = 0f;
    this.button = NOBUTTON;
	}

	//idem
	public DOF2Event(DOF2Event<A> prevEvent, float x, float y, A a) {
    super(a);
		this.x = x;
		this.dx = 0f;
    this.y = y;
    this.dy = 0f;
    this.button = NOBUTTON;
    if(prevEvent!=null) {
    	distance = Geom.distance(x, y, prevEvent.getX(), prevEvent.getY());
    	if( sameSequence(prevEvent) ) {
    		this.dx = this.getX() - prevEvent.getX();
    		this.dy = this.getY() - prevEvent.getY();
    	}
    }
	}
  
  protected DOF2Event(DOF2Event<A> other) {
  	super(other);
		this.x = new Float(other.x);
		this.dx = new Float(other.dx);
  	this.y = new Float(other.y);
  	this.dy = new Float(other.dy);
	}
  
  @Override
	public DOF2Event<A> get() {
		return new DOF2Event<A>(this);
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
  
	@Override
	public void modulate(float [] sens) {
		if(sens != null)
		if(sens.length>=2 && this.absolute()) {
			x = x*sens[0];
			y = y*sens[1];
		}
	}
	
	@Override
	public boolean isNull() {
  	if(relative() && Geom.zero(getDX()) && Geom.zero(getDY()))
  			return true;
  	if(absolute() && Geom.zero(getX()) && Geom.zero(getY()))
  		return true;
  	return false;
  }

}
