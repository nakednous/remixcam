package remixlab.remixcam.interactivity;

import remixlab.remixcam.core.Constants;
import remixlab.remixcam.event.GenericDOF6Event;

/**
import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;
*/

public class DOF6Event extends GenericDOF6Event<Constants.DOF_6Action> {
	public DOF6Event(float x, float y, float z, float rx, float ry, float rz,
			int modifiers, int button) {
		super(x, y, z, rx, ry, rz, modifiers, button);
	}

	public DOF6Event(float x, float y, float z, float rx, float ry, float rz,
			DOF_6Action a) {
		super(x, y, z, rx, ry, rz, a);
	}
	
	public DOF6Event(GenericDOF6Event<DOF_6Action> prevEvent, float x, float y,
			float z, float rx, float ry, float rz, int modifiers, int button) {
		super(prevEvent, x, y, z, rx, ry, rz, modifiers, button);
	}
	
	public DOF6Event(GenericDOF6Event<DOF_6Action> prevEvent, float x, float y, float z, float rx, float ry, float rz, DOF_6Action a) {
		super(prevEvent, x, y, z, rx, ry, rz, a);
	}
	
	protected DOF6Event(GenericDOF6Event<DOF_6Action> other) {
		super(other);
	}

  @Override
	public DOF6Event get() {
		return new DOF6Event(this);
	}
	
  @Override
	public DOF_6Action getAction() {
  	return (DOF_6Action)action;
  }
  
  public DOF3Event dof3Event(DOF_3Action a3) {
  	return dof3Event(true, a3);
  }
  
  public DOF3Event dof3Event(boolean fromTranslation, DOF_3Action a3) {
  	DOF3Event e3 = dof3Event(fromTranslation);
  	e3.setAction(a3);
  	return e3;
  }
  
  public DOF3Event dof3Event() {
  	return dof3Event(true);
  }
  
  public DOF3Event dof3Event(boolean fromTranslation) {
  	DOF3Event pe3;
  	DOF3Event e3;
  	if(relative()) {
  		if(fromTranslation) {
  			pe3 = new DOF3Event(getPrevX(), getPrevY(), getPrevZ(), getModifiers(), getButton());
  			e3 = new DOF3Event(pe3, getX(), getY(), getZ(), getModifiers(), getButton());
  		}
  		else {
  			pe3 = new DOF3Event(getPrevRX(), getPrevRY(), getPrevRZ(), getModifiers(), getButton());
  			e3 = new DOF3Event(pe3, getRX(), getRY(), getRZ(), getModifiers(), getButton());
  		}
  	}
  	else {
  		if(fromTranslation)
    		e3 = new DOF3Event(getX(), getY(), getZ(), getModifiers(), getButton());
  		else
  			e3 = new DOF3Event(getRX(), getRY(), getRZ(), getModifiers(), getButton());  		
  	}
  	return e3;
  }
}
