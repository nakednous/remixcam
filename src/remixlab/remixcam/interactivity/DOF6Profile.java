package remixlab.remixcam.interactivity;

import java.util.Map;

import remixlab.remixcam.core.*;
import remixlab.remixcam.profile.AbstractMotionProfile;
import remixlab.remixcam.profile.Bindings;
import remixlab.remixcam.shortcut.ButtonShortcut;

public class DOF6Profile extends AbstractMotionProfile<Constants.DOF_6Action> {
	public DOF6Profile() {
		super();
	}
	
	protected DOF6Profile(DOF6Profile other) {
		bindings = new Bindings<ButtonShortcut, DOF_6Action>();    
    for (Map.Entry<ButtonShortcut, DOF_6Action> entry : other.bindings.map().entrySet()) {
    	ButtonShortcut key = entry.getKey().get();
    	DOF_6Action value = entry.getValue();
    	bindings.setBinding(key, value);
    }
	}
	
  @Override
	public DOF6Profile get() {
		return new DOF6Profile(this);
	}
	/**
	public DOF6Profile(AbstractScene scn, String n) {
		super(scn, n);
	}
	*/
}