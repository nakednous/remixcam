package remixlab.remixcam.interactivity;

import remixlab.remixcam.event.GenericDOF2Event;
/**
import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;
*/

import remixlab.remixcam.core.Constants;

public class DOF2Event extends GenericDOF2Event<Constants.DOF_2Action> {	
	public DOF1Event dof1Event(DOF_1Action a1) {
  	return dof1Event(true, a1);
  }
	
	public DOF2Event(GenericDOF2Event<DOF_2Action> prevEvent, float x, float y, DOF_2Action a) {
		super(prevEvent, x, y, a);
	}
	
	public DOF2Event(float x, float y, int modifiers, int button) {
		super(x, y, modifiers, button);
	}
	
	public DOF2Event(GenericDOF2Event<DOF_2Action> prevEvent, float x, float y,	int modifiers, int button) {
		super(prevEvent, x, y, modifiers, button);
	}

	public DOF2Event(float x, float y, DOF_2Action a) {
		super(x, y, a);
	}
	
	protected DOF2Event(DOF2Event other) {
		super(other);
	}
	
	@Override
	public DOF2Event get() {
		return new DOF2Event(this);
	}
	
	@Override
	public DOF_2Action getAction() {
  	return (DOF_2Action)action;
  }
  
  public DOF1Event dof1Event(boolean fromX, DOF_1Action a1) {
  	DOF1Event e1 = dof1Event(fromX);
  	e1.setAction(a1);
  	return e1;
  }  
  
  public DOF1Event dof1Event() {
  	return dof1Event(true);
  }
  
  public DOF1Event dof1Event(boolean fromX) {
  	DOF1Event pe1;
  	DOF1Event e1;
  	if(fromX) {
  		if(relative()) {
  			pe1 = new DOF1Event(getPrevX(), getModifiers(), getButton());
  			e1 = new DOF1Event(pe1, getX(), getModifiers(), getButton());
  		}
  		else {
  			e1 = new DOF1Event(getX(), getModifiers(), getButton());
  		}
  	}
  	else {
  		if(relative()) {
  			pe1 = new DOF1Event(getPrevY(), getModifiers(), getButton());
  			e1 = new DOF1Event(pe1, getY(), getModifiers(), getButton());
  		}
  		else {
  			e1 = new DOF1Event(getY(), getModifiers(), getButton());
  		}
  	}
  	return e1;
  }
}
