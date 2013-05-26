package remixlab.remixcam.event;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

public class DLClickEvent extends DLEvent {
	@Override
	public int hashCode() {
    return new HashCodeBuilder(17, 37).
    appendSuper(super.hashCode()).
    append(button).
		append(numberOfClicks).
    toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;		
		if (obj.getClass() != getClass()) return false;
		
		DLClickEvent other = (DLClickEvent) obj;
		return new EqualsBuilder()
    .appendSuper(super.equals(obj))
    .append(button, other.button)
    .append(numberOfClicks, other.numberOfClicks)
		.isEquals();
	}
	
	protected final Integer numberOfClicks;
	protected final Integer button;
	
	public DLClickEvent(int b) {
		this.button = b;
  	this.numberOfClicks = 1;  	
  }
	
	public DLClickEvent(int b, DLAction a) {
		super(a);
		this.button = b;
  	this.numberOfClicks = 1;
  }
	
	public DLClickEvent(int b, int clicks) {
		this.button = b;
  	this.numberOfClicks = clicks;
  }
	
	public DLClickEvent(int b, int clicks, DLAction a) {
		super(a);
		this.button = b;
  	this.numberOfClicks = clicks;
  }
	
	//--
	
	public DLClickEvent(Integer modifiers, int b) {
		super(modifiers);
		this.button = b;
  	this.numberOfClicks = 1;  	
  }
	
	public DLClickEvent(Integer modifiers, int b, DLAction a) {
		super(modifiers, a);
		this.button = b;
  	this.numberOfClicks = 1;
  }
	
	public DLClickEvent(Integer modifiers, int b, int clicks) {
		super(modifiers);
		this.button = b;
  	this.numberOfClicks = clicks;
  }
	
	public DLClickEvent(Integer modifiers, int b, int clicks, DLAction a) {
		super(modifiers, a);
		this.button = b;
  	this.numberOfClicks = clicks;
  }
	
	protected DLClickEvent(DLClickEvent other) {
		super(other);
		this.button = other.button;
		this.numberOfClicks = other.numberOfClicks;		
	}
	
	@Override
	public DLClickEvent get() {
		return new DLClickEvent(this);
	}
	
	public int getClickCount() {
		return numberOfClicks;
	}
	
	public int getButton() {
		return button;
	}
}
