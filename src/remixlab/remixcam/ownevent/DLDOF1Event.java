package remixlab.remixcam.ownevent;

//import com.flipthebird.gwthashcodeequals.EqualsBuilder;
//import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

import remixlab.remixcam.core.Constants;
import remixlab.remixcam.event.DOF1Event;
///**
public class DLDOF1Event extends DOF1Event<Constants.DOF_1Action> {
	public DLDOF1Event(float x, DOF_1Action a) {
		super(x, a);
	}

	public DLDOF1Event(float x, int modifiers, int button) {
		super(x, modifiers, button);
	}
	
	public DLDOF1Event(DOF1Event<DOF_1Action> prevEvent, float x, DOF_1Action a) {
		super(prevEvent, x, a);
	}

	public DLDOF1Event(DOF1Event<DOF_1Action> prevEvent, float x, int modifiers, int button) {
		super(prevEvent, x, modifiers, button);
	}

	protected DLDOF1Event(DLDOF1Event other) {
		super(other);
	}
	
	@Override
	public DLDOF1Event get() {
		return new DLDOF1Event(this);
	}
	
	@Override
	public DOF_1Action getAction() {
  	return (DOF_1Action)action;
  }
}