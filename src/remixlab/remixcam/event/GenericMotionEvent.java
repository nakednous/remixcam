package remixlab.remixcam.event;

import remixlab.remixcam.core.Actionable;
import remixlab.remixcam.shortcut.*;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

// /**
public class GenericMotionEvent<A extends Actionable<?>> extends GenericEvent<A> {
	@Override
	public int hashCode() {
    return new HashCodeBuilder(17, 37).
    appendSuper(super.hashCode()).
		append(button).
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
		
		GenericMotionEvent<?> other = (GenericMotionEvent<?>) obj;
		return new EqualsBuilder()
    .appendSuper(super.equals(obj))
		.append(button, other.button)
		.append(delay, other.delay)
		.append(distance, other.distance)
		.append(speed, other.speed)
		.isEquals();
	}

	protected Integer button;
	
	//defaulting to zero:
  //http://stackoverflow.com/questions/3426843/what-is-the-default-initialization-of-an-array-in-java
	protected long delay;
	protected float distance, speed;
	
	public GenericMotionEvent(A a) {
    super(a);
    this.button = NOBUTTON;
  }
	
	public GenericMotionEvent(int modifiers) {
    super(modifiers);
    this.button = NOBUTTON;
  }
	
	public GenericMotionEvent(int modifiers, int button) {
    super(modifiers);
    this.button = button;
  }
  
	// ---
	
  protected GenericMotionEvent(GenericMotionEvent<A> other) {
  	super(other);
		this.button = new Integer(other.button);
		this.delay = other.delay;
		this.distance = other.distance;
		this.speed = other.speed;
	}
  
  @Override
	public GenericMotionEvent<A> get() {
		return new GenericMotionEvent<A>(this);
	}
  
  public void modulate(float [] sens) {}
  
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
	
	protected boolean sameSequence(GenericMotionEvent<?> prevEvent) {
		boolean result = false;
		long tThreshold = 5000;
		float dThreshold =  50;
		delay = this.timestamp() - prevEvent.timestamp();
		
		if(delay==0)
			speed = distance;
		else
			speed = distance / (float)delay;
		
		//if(prevEvent != null)
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
