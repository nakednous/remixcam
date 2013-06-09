package remixlab.remixcam.device;

import remixlab.remixcam.core.*;
import remixlab.remixcam.profile.DOF2Profile;

public class DLMouse extends AbstractMotionDevice implements Constants {
	public DLMouse(AbstractScene scn, String n) {
		super(scn, n);
		camProfile = new DOF2Profile();
		frameProfile = new DOF2Profile();
		sens = new float[2];
		sens[0] = 1f;
		sens[1] = 1f;
		cameraProfile().setBinding(CENTER, DOF_2Action.ZOOM);
		cameraProfile().setBinding(LEFT, DOF_2Action.ROTATE);
		cameraProfile().setBinding(RIGHT, DOF_2Action.TRANSLATE);
		//testing things out :P
		frameProfile().setBinding(CENTER, DOF_2Action.ZOOM);
		frameProfile().setBinding(RIGHT, DOF_2Action.ROTATE);
		frameProfile().setBinding(LEFT, DOF_2Action.TRANSLATE);
	}
	
	@Override
	public DOF2Profile cameraProfile() {
		return (DOF2Profile)camProfile;
	}
	
	@Override
	public DOF2Profile frameProfile() {
		return (DOF2Profile)frameProfile;
	}
	
	public void setXTranslationSensitivity(float s) {
		sens[0] = s;
	}
	
	public void setYTranslationSensitivity(float s) {
		sens[1] = s;
	}
	
	
}
