package remixlab.remixcam.interactivity;

import java.util.Map;

import remixlab.remixcam.core.*;
import remixlab.remixcam.profile.AbstractMotionProfile;
import remixlab.remixcam.profile.Bindings;
import remixlab.remixcam.shortcut.ButtonShortcut;

public class DOF2Profile extends AbstractMotionProfile<Constants.DOF_2Action> {
	public DOF2Profile() {
		super();
	}
	
	protected DOF2Profile(DOF2Profile other) {
		bindings = new Bindings<ButtonShortcut, DOF_2Action>();    
    for (Map.Entry<ButtonShortcut, DOF_2Action> entry : other.bindings.map().entrySet()) {
    	ButtonShortcut key = entry.getKey().get();
    	DOF_2Action value = entry.getValue();
    	bindings.setBinding(key, value);
    }
	}
	
  @Override
	public DOF2Profile get() {
		return new DOF2Profile(this);
	}
  
  /**
	@Override
	public void handle(DLEvent<DOF_2Action> e) {
		//super.handle((DLKeyEvent)e);
		super.handle(e);
	}
	*/
  
	/**
	public DOF2Profile(AbstractScene scn, String n) {
		super(scn, n);
	}
	*/
}