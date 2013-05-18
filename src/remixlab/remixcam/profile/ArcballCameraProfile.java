package remixlab.remixcam.profile;

import remixlab.remixcam.action.DOF_6Action;
import remixlab.remixcam.core.AbstractScene;
import remixlab.remixcam.event.*;

public class ArcballCameraProfile extends CameraProfile {
	public ArcballCameraProfile(AbstractScene scn, String n) {
		super(scn, n);
		setCameraMouseBinding(LEFT, DOF_6Action.ROTATE);
		arcballDefaultShortcuts();			
	}
}
