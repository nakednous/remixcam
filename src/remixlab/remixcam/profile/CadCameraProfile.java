package remixlab.remixcam.profile;

import remixlab.remixcam.action.DOF_6Action;
import remixlab.remixcam.core.AbstractScene;
import remixlab.remixcam.event.*;

public class CadCameraProfile extends CameraProfile {
	public CadCameraProfile(AbstractScene scn, String n) {
		super(scn, n);
		setCameraMouseBinding(LEFT, DOF_6Action.CAD_ROTATE);
		arcballDefaultShortcuts();
	}
}
