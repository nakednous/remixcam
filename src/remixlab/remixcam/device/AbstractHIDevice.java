package remixlab.remixcam.device;

import remixlab.remixcam.core.*;
import remixlab.remixcam.event.*;
import remixlab.remixcam.profile.DOF6Profile;

public class AbstractHIDevice  extends AbstractMotionDevice {

	public AbstractHIDevice(AbstractScene scn, String n) {
		super(scn, n);
		camProfile = new DOF6Profile();
		frameProfile = new DOF6Profile();
	}

	@Override
	public void handle(DLEvent<?> event) {
		// TODO Auto-generated method stub
		
	}
}
