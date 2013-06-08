package remixlab.remixcam.device;

import java.util.ArrayList;

import remixlab.remixcam.core.*;
import remixlab.remixcam.profile.DOF2Profile;

public class AbstractMouse extends AbstractMotionDevice implements Constants {
	public AbstractMouse(AbstractScene scn, String n) {
		super(scn, n);
		camProfile = new DOF2Profile();
		frameProfile = new DOF2Profile();
		sens = new ArrayList<Float>(2);
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
		sens.set(0, s);
	}
	
	public void setYTranslationSensitivity(float s) {
		sens.set(1, s);
	}
	
	
}