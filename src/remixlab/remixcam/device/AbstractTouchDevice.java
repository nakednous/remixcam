package remixlab.remixcam.device;

import remixlab.remixcam.core.*;
import remixlab.remixcam.event.*;
import remixlab.remixcam.profile.DOF6Profile;

public class AbstractTouchDevice extends AbstractMotionDevice {

	public AbstractTouchDevice(AbstractScene scn, String n) {
		super(scn, n);
		camProfile = new DOF6Profile();
		frameProfile = new DOF6Profile();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handle(DLEvent<?> event) {
		// TODO Auto-generated method stub
		
	}
}
