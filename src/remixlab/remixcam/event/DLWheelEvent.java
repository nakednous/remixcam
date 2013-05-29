package remixlab.remixcam.event;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

public class DLWheelEvent extends DLEvent {
	@Override
	public int hashCode() {
    return new HashCodeBuilder(17, 37).
    appendSuper(super.hashCode()).
		append(amount).
    toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;		
		if (obj.getClass() != getClass()) return false;
		
		DLWheelEvent other = (DLWheelEvent) obj;
		return new EqualsBuilder()
    .appendSuper(super.equals(obj))
    .append(amount, other.amount)
		.isEquals();
	}
	
	protected final Float amount;
	
	public DLWheelEvent() {
  	this.amount = 0f;
  }
	
	public DLWheelEvent(DLAction a) {
		super(a);
  	this.amount = 0f;
  }
	
	public DLWheelEvent(float am) {
  	this.amount = am;
  }
	
	public DLWheelEvent(float am, DLAction a) {
		super(a);
  	this.amount = am;
  }
	
	//--
	
	public DLWheelEvent(Integer modifiers) {
		super(modifiers);
  	this.amount = 0f;  	
  }
	
	public DLWheelEvent(Integer modifiers, DLAction a) {
		super(modifiers, a);
  	this.amount = 0f;
  }
	
	public DLWheelEvent(Integer modifiers, float am) {
		super(modifiers);
  	this.amount = am;
  }
	
	public DLWheelEvent(Integer modifiers, float am, DLAction a) {
		super(modifiers, a);
  	this.amount = am;
  }
	
	protected DLWheelEvent(DLWheelEvent other) {
		super(other);
		this.amount = other.amount;		
	}
	
	@Override
	public DLWheelEvent get() {
		return new DLWheelEvent(this);
	}
	
	public float getAmount() {
		return amount;
	}
}
