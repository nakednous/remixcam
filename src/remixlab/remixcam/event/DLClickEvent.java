package remixlab.remixcam.event;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

public class DLClickEvent extends DLEvent {
	@Override
	public int hashCode() {
    return new HashCodeBuilder(17, 37).
    appendSuper(super.hashCode()).
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
    .append(numberOfClicks, other.numberOfClicks)
		.isEquals();
	}
	
	protected Integer numberOfClicks;
	
	protected DLClickEvent(DLClickEvent other) {
		super(other);
		this.numberOfClicks = other.numberOfClicks;
	}
	
	public DLClickEvent() {
  	this.numberOfClicks = 1;
  }
	
	public DLClickEvent(int clicks) {
  	this.numberOfClicks = clicks;
  }
	
	public DLClickEvent(DLAction a) {
		super(a);
  	this.numberOfClicks = 1;
  }
	
	public DLClickEvent(int clicks, DLAction a) {
		super(a);
  	this.numberOfClicks = clicks;
  }
	
	@Override
	public DLClickEvent get() {
		return new DLClickEvent(this);
	}
	
	public int getClickCount() {
		return numberOfClicks;
	}
}
