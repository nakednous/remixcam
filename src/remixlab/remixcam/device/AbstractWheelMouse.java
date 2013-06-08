package remixlab.remixcam.device;

import remixlab.remixcam.core.AbstractScene;
import remixlab.remixcam.event.*;
import remixlab.remixcam.profile.DOF1Profile;

public class AbstractWheelMouse extends AbstractMouse {
	protected DOF1Profile wheelProfile;
	
	public AbstractWheelMouse(AbstractScene scn, String n) {
		super(scn, n);
		wheelProfile = new DOF1Profile();
	}
	
	public DOF1Profile wheelProfile() {
		return wheelProfile;
	}
	
	public void setWheelProfile(DOF1Profile profile) {
		wheelProfile = profile;
	}
	
	@Override
	public void handle(DLEvent event) {
		if( event instanceof DOF1Event ) {
			wheelProfile.handle(event);
			event.enqueue(scene);
		}
		else
			super.handle(event);
	}
}
