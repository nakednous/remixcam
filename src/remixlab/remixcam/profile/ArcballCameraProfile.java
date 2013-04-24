package remixlab.remixcam.profile;

import remixlab.remixcam.core.AbstractScene;
import remixlab.remixcam.core.AbstractScene.DeviceAction;

public class ArcballCameraProfile extends CameraProfile {
	public ArcballCameraProfile(AbstractScene scn, String n) {
		super(scn, n);
		setCameraMouseBinding(LEFT, DeviceAction.ROTATE);
		arcballDefaultShortcuts();			
	}
}
