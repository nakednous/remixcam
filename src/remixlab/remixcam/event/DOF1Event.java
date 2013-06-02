package remixlab.remixcam.event;

import remixlab.remixcam.shortcut.*;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

/**
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
//*/

/**
public class DOF1Event extends DOFEvent {
	@Override
	public int hashCode() {
    return new HashCodeBuilder(17, 37).
    appendSuper(super.hashCode()).
		append(button).
    toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;		
		if (obj.getClass() != getClass()) return false;
		
		DOF1Event other = (DOF1Event) obj;
		return new EqualsBuilder()
    .appendSuper(super.equals(obj))
		.append(button, other.button)
		.isEquals();
	}

	protected Integer button;
	
  public DOF1Event(float x) {
  	super(x);
  	this.button = 0;
  }
  
  public DOF1Event(float x, int button) {
  	super(x);
  	this.button = button;
  }
  
  public DOF1Event(float x, int modifiers, int button) {
  	super(x, modifiers);
  	this.button = button;
  }  
  
  public DOF1Event(float x, DLAction a) {
  	super(x, a);
  	this.button = 0;
  }
  
  public DOF1Event(float x, int button, DLAction a) {
  	super(x, a);
  	this.button = button;
  }

  public DOF1Event(float x, int modifiers, int button, DLAction a) {
    super(x, modifiers, a);
    this.button = button;
  }
  
  protected DOF1Event(DOF1Event other) {
  	super(other);
  	this.x = other.x;
		this.button = other.button;
	}
  
  @Override
	public DOF1Event get() {
		return new DOF1Event(this);
	}
  
  public float getX() {
    return x;
  }
  
	public int getButton() {
		return button;
	}

	@Override
	public ButtonShortcut shortcut() {
		//TODO test this condition
		if( getButton() == 0 )
			return new ButtonShortcut(getModifiers());
		return new ButtonShortcut(getModifiers(), getButton());
	}
  
  public static DOF1Event deltaEvent(DOF1Event current, DOF1Event prev) {
  	return new DOF1Event((current.getX() - prev.getX()), current.modifiers, current.button, current.action);
  }
  
  public DOF1Event deltaEvent(DOF1Event prev) {
  	return deltaEvent(this, prev);
  }
}
//*/

// /**
public class DOF1Event extends DLEvent {
	@Override
	public int hashCode() {
    return new HashCodeBuilder(17, 37).
    appendSuper(super.hashCode()).
		append(button).
		append(x).
		append(dx).
		append(delay).
		append(distance).
		append(speed).
    toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;		
		if (obj.getClass() != getClass()) return false;
		
		DOF1Event other = (DOF1Event) obj;
		return new EqualsBuilder()
    .appendSuper(super.equals(obj))
		.append(button, other.button)
		.append(x, other.x)
		.append(dx, other.dx)
		.append(delay, other.delay)
		.append(distance, other.distance)
		.append(speed, other.speed)
		.isEquals();
	}

	protected Float x, dx;
	protected Integer button;
	
	//defaulting to zero:
  //http://stackoverflow.com/questions/3426843/what-is-the-default-initialization-of-an-array-in-java
	protected long delay;
	protected float distance, speed;		
	
	public DOF1Event(float x, int modifiers, int button) {
    super(modifiers);
    this.x = x;
    this.dx = 0f;
    this.button = button;
  }
	
	public DOF1Event(DOF1Event prevEvent, float x, int modifiers, int button) {
    this(x, modifiers, button);    
    distance = this.getX() - prevEvent.getX();
    if( sameSequence(prevEvent) ) {
    	this.action = prevEvent.getAction();
    	this.dx = this.getX() - prevEvent.getX();
    }
  }
	
	//ready to be enqueued
	public DOF1Event(float x, DLAction a) {
    super(a);
    this.x = x;
    this.dx = 0f;
    this.button = NOBUTTON;
	}
	
  //idem
	public DOF1Event(DOF1Event prevEvent, float x, DLAction a) {
    super(a);
    this.button = NOBUTTON;
    this.x = x;    
    distance = this.getX() - prevEvent.getX();
    if( sameSequence(prevEvent) )
    	this.dx = this.getX() - prevEvent.getX();    
	}
  
	// ---
	
  protected DOF1Event(DOF1Event other) {
  	super(other);
  	this.x = other.x;
  	this.dx = other.dx;
		this.button = other.button;
		this.delay = other.delay;
		this.distance = other.distance;
		this.speed = other.speed;
	}
  
  @Override
	public DOF1Event get() {
		return new DOF1Event(this);
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
  
	public int getButton() {
		return button;
	}
	
	@Override
	public ButtonShortcut shortcut() {
		if( getButton() == 0 )
			return new ButtonShortcut(getModifiers());
		return new ButtonShortcut(getModifiers(), getButton());
	}
	
	public long delay() {
		return delay;
	}
	
	public float distance() {
		return distance;
	}
	
	public float speed() {
		return speed;
	}
	
	public boolean relative() {
		return distance() != 0;
	}
	
	public boolean absolute() {
		return !relative();
	}
	
	//-- 
	protected boolean sameSequence(DOF1Event prevEvent) {
		boolean result = false;
		long tThreshold = 5000;
		float dThreshold = 30;
		delay = this.timestamp() - prevEvent.timestamp();
		
		if(delay==0)
			speed = distance;
		else
			speed = distance / (float)delay;
		
		if(prevEvent != null)
    	if( prevEvent.shortcut().equals(this.shortcut()) )
    		if( ( distance <= dThreshold) && ( delay <= tThreshold ) ) {    			
    			result = true;    			
    		} else {
    			delay = 0L;
        	speed = 0f;
        	distance = 0f;
    		}
		return result;
	}
}
// */
