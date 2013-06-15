package remixlab.remixcam.ownevent;

import remixlab.remixcam.event.DOF2Event;
/**
import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;
*/

import remixlab.remixcam.core.Constants;

public class DLDOF2Event extends DOF2Event<Constants.DOF_2Action> {	
	public DLDOF1Event dof1Event(DOF_1Action a1) {
  	return dof1Event(true, a1);
  }
	
	public DLDOF2Event(DOF2Event<DOF_2Action> prevEvent, float x, float y, DOF_2Action a) {
		super(prevEvent, x, y, a);
	}
	
	public DLDOF2Event(float x, float y, int modifiers, int button) {
		super(x, y, modifiers, button);
	}
	
	public DLDOF2Event(DOF2Event<DOF_2Action> prevEvent, float x, float y,	int modifiers, int button) {
		super(prevEvent, x, y, modifiers, button);
	}

	public DLDOF2Event(float x, float y, DOF_2Action a) {
		super(x, y, a);
	}
	
	protected DLDOF2Event(DLDOF2Event other) {
		super(other);
	}
	
	@Override
	public DLDOF2Event get() {
		return new DLDOF2Event(this);
	}
	
	@Override
	public DOF_2Action getAction() {
  	return (DOF_2Action)action;
  }
  
  public DLDOF1Event dof1Event(boolean fromX, DOF_1Action a1) {
  	DLDOF1Event e1 = dof1Event(fromX);
  	e1.setAction(a1);
  	return e1;
  }  
  
  public DLDOF1Event dof1Event() {
  	return dof1Event(true);
  }
  
  public DLDOF1Event dof1Event(boolean fromX) {
  	DLDOF1Event pe1;
  	DLDOF1Event e1;
  	if(fromX) {
  		if(relative()) {
  			pe1 = new DLDOF1Event(getPrevX(), getModifiers(), getButton());
  			e1 = new DLDOF1Event(pe1, getX(), getModifiers(), getButton());
  		}
  		else {
  			e1 = new DLDOF1Event(getX(), getModifiers(), getButton());
  		}
  	}
  	else {
  		if(relative()) {
  			pe1 = new DLDOF1Event(getPrevY(), getModifiers(), getButton());
  			e1 = new DLDOF1Event(pe1, getY(), getModifiers(), getButton());
  		}
  		else {
  			e1 = new DLDOF1Event(getY(), getModifiers(), getButton());
  		}
  	}
  	return e1;
  }
}
