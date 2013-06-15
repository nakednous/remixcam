package remixlab.remixcam.ownevent;

import remixlab.remixcam.event.DOF3Event;

/**
import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;
*/

import remixlab.remixcam.core.Constants;

public class DLDOF3Event extends DOF3Event<Constants.DOF_3Action> {
	public DLDOF3Event(float x, float y, float z, int modifiers, int button) {
		super(x, y, z, modifiers, button);
	}

  public DLDOF3Event(float x, float y, float z, DOF_3Action a) {
		super(x, y, z, a);
	}

  public DLDOF3Event(DOF3Event<DOF_3Action> prevEvent, float x, float y, float z, int modifiers, int button) {
		super(prevEvent, x, y, z, modifiers, button);
	}
	
  public DLDOF3Event(DOF3Event<DOF_3Action> prevEvent, float x, float y, float z, DOF_3Action a) {
		super(prevEvent, x, y, z, a);
	}
	
  protected DLDOF3Event(DOF3Event<DOF_3Action> other) {
		super(other);
	}

  @Override
	public DLDOF3Event get() {
		return new DLDOF3Event(this);
	}
  
  @Override
	public DOF_3Action getAction() {
  	return (DOF_3Action)action;
  }
  
  public DLDOF2Event dof2Event(DOF_2Action a2) {
  	DLDOF2Event e2 = dof2Event();
  	e2.setAction(a2);
  	return e2;
  }
  
  public DLDOF2Event dof2Event() {
  	DLDOF2Event pe2;
  	DLDOF2Event e2;
  	if(relative()) {  		
  			pe2 = new DLDOF2Event(getPrevX(), getPrevY(), getModifiers(), getButton());
  			e2 = new DLDOF2Event(pe2, getX(), getY(), getModifiers(), getButton());  		
  	}
  	else {
  		e2 = new DLDOF2Event(getX(), getY(), getModifiers(), getButton()); 
  	}
  	return e2;
  }
}
