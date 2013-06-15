package remixlab.remixcam.event;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

import remixlab.remixcam.core.Actionable;
import remixlab.remixcam.geom.Geom;

public class GenericDOF1Event<A extends Actionable<?>> extends GenericMotionEvent<A> {
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).
				appendSuper(super.hashCode()).
				append(x).
				append(dx).
				toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (obj.getClass() != getClass())
			return false;

		GenericDOF1Event<?> other = (GenericDOF1Event<?>) obj;
		return new EqualsBuilder()
		.appendSuper(super.equals(obj))
		.append(x, other.x)
		.append(dx, other.dx)
		.isEquals();
	}

	protected Float x, dx;

	public GenericDOF1Event(float x, int modifiers, int button) {
		super(modifiers, button);
		this.x = x;
		this.dx = 0f;
	}

	public GenericDOF1Event(GenericDOF1Event<A> prevEvent, float x, int modifiers, int button) {
		this(x, modifiers, button);
		if(prevEvent!=null) {
			distance = this.getX() - prevEvent.getX();
			if (sameSequence(prevEvent)) {
				this.action = prevEvent.getAction();
				this.dx = this.getX() - prevEvent.getX();
			}
		}
	}

	// ready to be enqueued
	public GenericDOF1Event(float x, A a) {
		super(a);
		this.x = x;
		this.dx = 0f;
		this.button = NOBUTTON;
	}

	// idem
	public GenericDOF1Event(GenericDOF1Event<A> prevEvent, float x, A a) {
		super(a);		
		this.x = x;
		this.dx = 0f;
		this.button = NOBUTTON;
		if(prevEvent!=null) {
			distance = this.getX() - prevEvent.getX();
			if (sameSequence(prevEvent))
				this.dx = this.getX() - prevEvent.getX();
		}
	}

	// ---

	protected GenericDOF1Event(GenericDOF1Event<A> other) {
		super(other);
		this.x = new Float(other.x);
		this.dx = new Float(other.dx);
	}

	@Override
	public GenericDOF1Event<A> get() {
		return new GenericDOF1Event<A>(this);
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
	
	@Override
	public void modulate(float [] sens) {
		if(sens != null)
		if(sens.length>=1 && this.absolute())
			x = x*sens[0];
	}
	
	@Override
	public boolean isNull() {
  	if(relative() && Geom.zero(getDX()) )
  			return true;
  	if(absolute() && Geom.zero(getX()))
  		return true;
  	return false;
  }
}
