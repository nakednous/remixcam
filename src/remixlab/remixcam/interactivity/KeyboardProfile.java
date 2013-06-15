package remixlab.remixcam.interactivity;

import java.util.Map;

import remixlab.remixcam.core.*;
import remixlab.remixcam.profile.AbstractKeyboardProfile;
import remixlab.remixcam.profile.Bindings;
import remixlab.remixcam.shortcut.*;

public class KeyboardProfile extends AbstractKeyboardProfile<Constants.DOF_0Action> implements Constants {
	public KeyboardProfile() {
		super();
	}
	
	protected KeyboardProfile(KeyboardProfile other) {
		bindings = new Bindings<KeyboardShortcut, DOF_0Action>();    
    for (Map.Entry<KeyboardShortcut, DOF_0Action> entry : other.bindings.map().entrySet()) {
    	KeyboardShortcut key = entry.getKey().get();
    	DOF_0Action value = entry.getValue();
    	bindings.setBinding(key, value);
    }
	}
	
  @Override
	public KeyboardProfile get() {
		return new KeyboardProfile(this);
	}
}
