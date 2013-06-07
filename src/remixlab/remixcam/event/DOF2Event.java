package remixlab.remixcam.event;

import remixlab.remixcam.geom.Geom;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

import remixlab.remixcam.core.Constants;

public class DOF2Event extends MotionEvent<Constants.DOF_2Action> {
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
		
		DOF2Event other = (DOF2Event) obj;
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
	
	public DOF2Event(DOF2Event prevEvent, float x, float y, int modifiers, int button) {
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
	public DOF2Event(float x, float y, DOF_2Action a) {
    super(a);
    this.x = x;
		this.dx = 0f;
    this.y = y;
    this.dy = 0f;
    this.button = NOBUTTON;
	}

	//idem
	public DOF2Event(DOF2Event prevEvent, float x, float y, DOF_2Action a) {
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
  
  protected DOF2Event(DOF2Event other) {
  	super(other);
		this.x = new Float(other.x);
		this.dx = new Float(other.dx);
  	this.y = new Float(other.y);
  	this.dy = new Float(other.dy);
	}
  
  @Override
	public DOF2Event get() {
		return new DOF2Event(this);
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
  
  public DOF1Event dof1Event(DOF_1Action a1) {
  	return dof1Event(true, a1);
  } 
  
  public DOF1Event dof1Event(boolean fromX, DOF_1Action a1) {
  	DOF1Event e1 = dof1Event(fromX);
  	e1.setAction(a1);
  	return e1;
  }  
  
  public DOF1Event dof1Event() {
  	return dof1Event(true);
  }
  
  public DOF1Event dof1Event(boolean fromX) {
  	DOF1Event pe1;
  	DOF1Event e1;
  	if(fromX) {
  		if(relative()) {
  			pe1 = new DOF1Event(getPrevX(), getModifiers(), getButton());
  			e1 = new DOF1Event(pe1, getX(), getModifiers(), getButton());
  		}
  		else {
  			e1 = new DOF1Event(getX(), getModifiers(), getButton());
  		}
  	}
  	else {
  		if(relative()) {
  			pe1 = new DOF1Event(getPrevY(), getModifiers(), getButton());
  			e1 = new DOF1Event(pe1, getY(), getModifiers(), getButton());
  		}
  		else {
  			e1 = new DOF1Event(getY(), getModifiers(), getButton());
  		}
  	}
  	return e1;
  }
}
