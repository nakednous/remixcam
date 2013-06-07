package remixlab.remixcam.event;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

import remixlab.remixcam.core.Constants;
///**
public class DOF1Event extends MotionEvent<Constants.DOF_1Action> {
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

		DOF1Event other = (DOF1Event) obj;
		return new EqualsBuilder()
		.appendSuper(super.equals(obj))
		.append(x, other.x)
		.append(dx, other.dx)
		.isEquals();
	}

	protected Float x, dx;

	public DOF1Event(float x, int modifiers, int button) {
		super(modifiers, button);
		this.x = x;
		this.dx = 0f;
	}

	public DOF1Event(DOF1Event prevEvent, float x, int modifiers, int button) {
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
	public DOF1Event(float x, DOF_1Action a) {
		super(a);
		this.x = x;
		this.dx = 0f;
		this.button = NOBUTTON;
	}

	// idem
	public DOF1Event(DOF1Event prevEvent, float x, DOF_1Action a) {
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

	protected DOF1Event(DOF1Event other) {
		super(other);
		this.x = new Float(other.x);
		this.dx = new Float(other.dx);
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
}
// */