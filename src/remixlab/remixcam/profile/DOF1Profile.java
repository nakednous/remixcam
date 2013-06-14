package remixlab.remixcam.profile;

import java.util.Map;

import remixlab.remixcam.core.*;
import remixlab.remixcam.shortcut.ButtonShortcut;

public class DOF1Profile extends AbstractMotionProfile<Constants.DOF_1Action> {
	public DOF1Profile() {
		super();
	}
	
	protected DOF1Profile(DOF1Profile other) {
		bindings = new Bindings<ButtonShortcut, DOF_1Action>();    
    for (Map.Entry<ButtonShortcut, DOF_1Action> entry : other.bindings.map.entrySet()) {
    	ButtonShortcut key = entry.getKey().get();
    	DOF_1Action value = entry.getValue();
    	bindings.setBinding(key, value);
    }
	}
	
  @Override
	public DOF1Profile get() {
		return new DOF1Profile(this);
	}
}