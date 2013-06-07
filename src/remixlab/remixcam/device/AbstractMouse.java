package remixlab.remixcam.device;

import remixlab.remixcam.core.*;
import remixlab.remixcam.event.*;
import remixlab.remixcam.profile.DOF2Profile;

public class AbstractMouse extends AbstractMotionDevice {
	public AbstractMouse(AbstractScene scn, String n) {
		super(scn, n);
		camProfile = new DOF2Profile();
		frameProfile = new DOF2Profile();
	}

	/**
	@Override
	public void handle(DLEvent<DOF_2Action> event) {
		camProfile.handle(event);		
	}
	*/

	@Override
	public void handle(DLEvent<?> event) {
		camProfile.handle(event);
	}
}
