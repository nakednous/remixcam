package remixlab.remixcam.profile;

import remixlab.remixcam.action.DLDeviceAction;
import remixlab.remixcam.core.AbstractScene;
import remixlab.remixcam.event.*;

public class ArcballCameraProfile extends CameraProfile {
	public ArcballCameraProfile(AbstractScene scn, String n) {
		super(scn, n);
		setCameraMouseBinding(LEFT, DLDeviceAction.ROTATE);
		arcballDefaultShortcuts();			
	}
}
