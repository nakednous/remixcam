package remixlab.remixcam.profile;

import java.util.Map;

import remixlab.remixcam.core.*;
import remixlab.remixcam.shortcut.ButtonShortcut;

public class DOF3Profile extends AbstractMotionProfile<Constants.DOF_3Action> {
	public DOF3Profile() {
		super();
	}
	
	protected DOF3Profile(DOF3Profile other) {
		bindings = new Bindings<ButtonShortcut, DOF_3Action>();    
    for (Map.Entry<ButtonShortcut, DOF_3Action> entry : other.bindings.map.entrySet()) {
    	ButtonShortcut key = entry.getKey().get();
    	DOF_3Action value = entry.getValue();
    	bindings.setBinding(key, value);
    }
	}
	
  @Override
	public DOF3Profile get() {
		return new DOF3Profile(this);
	}
  
	/**
	public DOF3Profile(AbstractScene scn, String n) {
		super(scn, n);
	}
	*/
}