package remixlab.remixcam.profile;

import remixlab.remixcam.core.*;
import remixlab.remixcam.event.*;
import remixlab.remixcam.shortcut.*;

public abstract class ClickProfile extends AbstractProfile<ClickShortcut> {
	public ClickProfile(AbstractScene scn, String n) {
		super(scn, n);
	}

	@Override
	public DLEvent handle() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void setDefaultBindings() {
	}

	
}