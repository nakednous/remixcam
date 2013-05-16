package remixlab.remixcam.profile;

import remixlab.remixcam.action.DLDeviceAction;
import remixlab.remixcam.core.AbstractScene;
import remixlab.remixcam.event.*;

public class CadCameraProfile extends CameraProfile {
	public CadCameraProfile(AbstractScene scn, String n) {
		super(scn, n);
		setCameraMouseBinding(LEFT, DLDeviceAction.CAD_ROTATE);
		arcballDefaultShortcuts();
	}
}
