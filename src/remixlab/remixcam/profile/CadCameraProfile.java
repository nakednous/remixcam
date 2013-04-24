package remixlab.remixcam.profile;

import remixlab.remixcam.core.AbstractScene;

public class CadCameraProfile extends CameraProfile {
	public CadCameraProfile(AbstractScene scn, String n) {
		super(scn, n);
		setCameraMouseBinding(LEFT, AbstractScene.DeviceAction.CAD_ROTATE);
		arcballDefaultShortcuts();
	}
}
