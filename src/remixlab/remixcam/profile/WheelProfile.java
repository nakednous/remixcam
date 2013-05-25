package remixlab.remixcam.profile;

import remixlab.remixcam.core.*;
import remixlab.remixcam.event.*;
import remixlab.remixcam.shortcut.*;

public abstract class WheelProfile extends AbstractProfile<Shortcut> {
	public WheelProfile(AbstractScene scn, String n) {
		super(scn, n);
	}
	
	@Override
	public void setDefaultBindings() {
	}

	@Override
	public DLEvent handle() {
		// TODO Auto-generated method stub
		return null;
	}
}