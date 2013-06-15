package remixlab.remixcam.ownevent;

import remixlab.remixcam.core.Constants;
import remixlab.remixcam.event.DOF6Event;

/**
import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;
*/

public class DLDOF6Event extends DOF6Event<Constants.DOF_6Action> {
	public DLDOF6Event(float x, float y, float z, float rx, float ry, float rz,
			int modifiers, int button) {
		super(x, y, z, rx, ry, rz, modifiers, button);
	}

	public DLDOF6Event(float x, float y, float z, float rx, float ry, float rz,
			DOF_6Action a) {
		super(x, y, z, rx, ry, rz, a);
	}
	
	public DLDOF6Event(DOF6Event<DOF_6Action> prevEvent, float x, float y,
			float z, float rx, float ry, float rz, int modifiers, int button) {
		super(prevEvent, x, y, z, rx, ry, rz, modifiers, button);
	}
	
	public DLDOF6Event(DOF6Event<DOF_6Action> prevEvent, float x, float y, float z, float rx, float ry, float rz, DOF_6Action a) {
		super(prevEvent, x, y, z, rx, ry, rz, a);
	}
	
	protected DLDOF6Event(DOF6Event<DOF_6Action> other) {
		super(other);
	}

  @Override
	public DLDOF6Event get() {
		return new DLDOF6Event(this);
	}
	
  @Override
	public DOF_6Action getAction() {
  	return (DOF_6Action)action;
  }
  
  public DLDOF3Event dof3Event(DOF_3Action a3) {
  	return dof3Event(true, a3);
  }
  
  public DLDOF3Event dof3Event(boolean fromTranslation, DOF_3Action a3) {
  	DLDOF3Event e3 = dof3Event(fromTranslation);
  	e3.setAction(a3);
  	return e3;
  }
  
  public DLDOF3Event dof3Event() {
  	return dof3Event(true);
  }
  
  public DLDOF3Event dof3Event(boolean fromTranslation) {
  	DLDOF3Event pe3;
  	DLDOF3Event e3;
  	if(relative()) {
  		if(fromTranslation) {
  			pe3 = new DLDOF3Event(getPrevX(), getPrevY(), getPrevZ(), getModifiers(), getButton());
  			e3 = new DLDOF3Event(pe3, getX(), getY(), getZ(), getModifiers(), getButton());
  		}
  		else {
  			pe3 = new DLDOF3Event(getPrevRX(), getPrevRY(), getPrevRZ(), getModifiers(), getButton());
  			e3 = new DLDOF3Event(pe3, getRX(), getRY(), getRZ(), getModifiers(), getButton());
  		}
  	}
  	else {
  		if(fromTranslation)
    		e3 = new DLDOF3Event(getX(), getY(), getZ(), getModifiers(), getButton());
  		else
  			e3 = new DLDOF3Event(getRX(), getRY(), getRZ(), getModifiers(), getButton());  		
  	}
  	return e3;
  }
}
