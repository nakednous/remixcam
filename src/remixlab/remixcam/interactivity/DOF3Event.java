package remixlab.remixcam.interactivity;

import remixlab.remixcam.event.GenericDOF3Event;

/**
import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;
*/

import remixlab.remixcam.core.Constants;

public class DOF3Event extends GenericDOF3Event<Constants.DOF_3Action> {
	public DOF3Event(float x, float y, float z, int modifiers, int button) {
		super(x, y, z, modifiers, button);
	}

  public DOF3Event(float x, float y, float z, DOF_3Action a) {
		super(x, y, z, a);
	}

  public DOF3Event(GenericDOF3Event<DOF_3Action> prevEvent, float x, float y, float z, int modifiers, int button) {
		super(prevEvent, x, y, z, modifiers, button);
	}
	
  public DOF3Event(GenericDOF3Event<DOF_3Action> prevEvent, float x, float y, float z, DOF_3Action a) {
		super(prevEvent, x, y, z, a);
	}
	
  protected DOF3Event(GenericDOF3Event<DOF_3Action> other) {
		super(other);
	}

  @Override
	public DOF3Event get() {
		return new DOF3Event(this);
	}
  
  @Override
	public DOF_3Action getAction() {
  	return (DOF_3Action)action;
  }
  
  public DOF2Event dof2Event(DOF_2Action a2) {
  	DOF2Event e2 = dof2Event();
  	e2.setAction(a2);
  	return e2;
  }
  
  public DOF2Event dof2Event() {
  	DOF2Event pe2;
  	DOF2Event e2;
  	if(relative()) {  		
  			pe2 = new DOF2Event(getPrevX(), getPrevY(), getModifiers(), getButton());
  			e2 = new DOF2Event(pe2, getX(), getY(), getModifiers(), getButton());  		
  	}
  	else {
  		e2 = new DOF2Event(getX(), getY(), getModifiers(), getButton()); 
  	}
  	return e2;
  }
}
