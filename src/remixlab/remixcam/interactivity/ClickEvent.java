package remixlab.remixcam.interactivity;

import remixlab.remixcam.core.Constants;
import remixlab.remixcam.event.GenericClickEvent;

/**
import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;
*/

public class ClickEvent extends GenericClickEvent<Constants.DOF_0Action> {
	public ClickEvent(Integer modifiers, int b, int clicks, DOF_0Action a) {
		super(modifiers, b, clicks, a);
	}

	public ClickEvent(Integer modifiers, int b, int clicks) {
		super(modifiers, b, clicks);
	}
	
	public ClickEvent(Integer modifiers, int b, DOF_0Action a) {
		super(modifiers, b, a);
	}
	
	public ClickEvent(Integer modifiers, int b) {
		super(modifiers, b);
	}
	
	public ClickEvent(int b, int clicks, DOF_0Action a) {
		super(b, clicks, a);
	}
	
	public ClickEvent(int b, int clicks) {
		super(b, clicks);
	}
	
	public ClickEvent(int b, DOF_0Action a) {
		super(b, a);
	}
	
	public ClickEvent(int b) {
		super(b);
	}
	
	protected ClickEvent(GenericClickEvent<DOF_0Action> other) {
		super(other);
	}
	
	@Override
	public ClickEvent get() {
		return new ClickEvent(this);
	}
	
	@Override
	public DOF_0Action getAction() {
  	return (DOF_0Action)action;
  }
}
