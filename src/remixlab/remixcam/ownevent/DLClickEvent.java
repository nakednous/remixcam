package remixlab.remixcam.ownevent;

import remixlab.remixcam.core.Constants;
import remixlab.remixcam.event.ClickEvent;

/**
import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;
*/

public class DLClickEvent extends ClickEvent<Constants.DOF_0Action> {
	public DLClickEvent(Integer modifiers, int b, int clicks, DOF_0Action a) {
		super(modifiers, b, clicks, a);
	}

	public DLClickEvent(Integer modifiers, int b, int clicks) {
		super(modifiers, b, clicks);
	}
	
	public DLClickEvent(Integer modifiers, int b, DOF_0Action a) {
		super(modifiers, b, a);
	}
	
	public DLClickEvent(Integer modifiers, int b) {
		super(modifiers, b);
	}
	
	public DLClickEvent(int b, int clicks, DOF_0Action a) {
		super(b, clicks, a);
	}
	
	public DLClickEvent(int b, int clicks) {
		super(b, clicks);
	}
	
	public DLClickEvent(int b, DOF_0Action a) {
		super(b, a);
	}
	
	public DLClickEvent(int b) {
		super(b);
	}
	
	protected DLClickEvent(ClickEvent<DOF_0Action> other) {
		super(other);
	}
	
	@Override
	public DLClickEvent get() {
		return new DLClickEvent(this);
	}
	
	@Override
	public DOF_0Action getAction() {
  	return (DOF_0Action)action;
  }
}
