package remixlab.remixcam.interactivity;

//import com.flipthebird.gwthashcodeequals.EqualsBuilder;
//import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

import remixlab.remixcam.core.Constants;
import remixlab.remixcam.event.GenericDOF1Event;
///**
public class DOF1Event extends GenericDOF1Event<Constants.DOF_1Action> {
	public DOF1Event(float x, DOF_1Action a) {
		super(x, a);
	}

	public DOF1Event(float x, int modifiers, int button) {
		super(x, modifiers, button);
	}
	
	public DOF1Event(GenericDOF1Event<DOF_1Action> prevEvent, float x, DOF_1Action a) {
		super(prevEvent, x, a);
	}

	public DOF1Event(GenericDOF1Event<DOF_1Action> prevEvent, float x, int modifiers, int button) {
		super(prevEvent, x, modifiers, button);
	}

	protected DOF1Event(DOF1Event other) {
		super(other);
	}
	
	@Override
	public DOF1Event get() {
		return new DOF1Event(this);
	}
	
	@Override
	public DOF_1Action getAction() {
  	return (DOF_1Action)action;
  }
}