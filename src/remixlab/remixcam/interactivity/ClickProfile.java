package remixlab.remixcam.interactivity;

import java.util.Map;

import remixlab.remixcam.core.*;
import remixlab.remixcam.profile.AbstractClickProfile;
import remixlab.remixcam.profile.Bindings;
import remixlab.remixcam.shortcut.*;

public class ClickProfile extends AbstractClickProfile<Constants.DOF_0Action> {
	public ClickProfile() {
		super();
	}
	
	protected ClickProfile(ClickProfile other) {
		bindings = new Bindings<ClickShortcut, DOF_0Action>();    
    for (Map.Entry<ClickShortcut, DOF_0Action> entry : other.bindings.map().entrySet()) {
    	ClickShortcut key = entry.getKey().get();
    	DOF_0Action value = entry.getValue();
    	bindings.setBinding(key, value);
    }
	}
	
  @Override
	public ClickProfile get() {
		return new ClickProfile(this);
	}
}