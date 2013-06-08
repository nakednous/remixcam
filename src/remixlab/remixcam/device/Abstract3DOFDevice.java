package remixlab.remixcam.device;

import remixlab.remixcam.core.AbstractScene;
import remixlab.remixcam.profile.DOF3Profile;

public class Abstract3DOFDevice extends AbstractMotionDevice {
	public Abstract3DOFDevice(AbstractScene scn, String n) {
		super(scn, n);
		camProfile = new DOF3Profile();
		frameProfile = new DOF3Profile();
	}

}
