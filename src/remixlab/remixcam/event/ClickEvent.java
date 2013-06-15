package remixlab.remixcam.event;

import remixlab.remixcam.core.Actionable;
import remixlab.remixcam.shortcut.ClickShortcut;

import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

public class ClickEvent<A extends Actionable<?>> extends DLEvent<A> {
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
		
		ClickEvent<?> other = (ClickEvent<?>) obj;
		return new EqualsBuilder()
    .appendSuper(super.equals(obj))
    .append(button, other.button)
    .append(numberOfClicks, other.numberOfClicks)
		.isEquals();
	}
	
	protected final Integer numberOfClicks;
	protected final Integer button;
	
	public ClickEvent(int b) {
		this.button = b;
  	this.numberOfClicks = 1;  	
  }
	
	public ClickEvent(int b, A a) {
		super(a);
		this.button = b;
  	this.numberOfClicks = 1;
  }
	
	public ClickEvent(int b, int clicks) {
		this.button = b;
  	this.numberOfClicks = clicks;
  }
	
	public ClickEvent(int b, int clicks, A a) {
		super(a);
		this.button = b;
  	this.numberOfClicks = clicks;
  }
	
	//--
	
	public ClickEvent(Integer modifiers, int b) {
		super(modifiers);
		this.button = b;
  	this.numberOfClicks = 1;  	
  }
	
	public ClickEvent(Integer modifiers, int b, A a) {
		super(modifiers, a);
		this.button = b;
  	this.numberOfClicks = 1;
  }
	
	public ClickEvent(Integer modifiers, int b, int clicks) {
		super(modifiers);
		this.button = b;
  	this.numberOfClicks = clicks;
  }
	
	public ClickEvent(Integer modifiers, int b, int clicks, A a) {
		super(modifiers, a);
		this.button = b;
  	this.numberOfClicks = clicks;
  }
	
	protected ClickEvent(ClickEvent<A> other) {
		super(other);
		this.button = new Integer(other.button);
		this.numberOfClicks = new Integer(other.numberOfClicks);		
	}
	
	@Override
	public ClickEvent<A> get() {
		return new ClickEvent<A>(this);
	}
	
	@Override
	public ClickShortcut shortcut() {
		return new ClickShortcut(getModifiers(), getButton(), getClickCount());
	}
	
	public int getClickCount() {
		return numberOfClicks;
	}
	
	public int getButton() {
		return button;
	}
}
