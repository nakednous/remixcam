package remixlab.remixcam.device;

import java.util.ArrayList;

import remixlab.remixcam.core.AbstractScene;
import remixlab.remixcam.profile.DOF3Profile;

public class Abstract3DOFDevice extends AbstractMotionDevice {
	public Abstract3DOFDevice(AbstractScene scn, String n) {
		super(scn, n);
		camProfile = new DOF3Profile();
		frameProfile = new DOF3Profile();
		sens = new ArrayList<Float>(3);
	}
	
	@Override
	public DOF3Profile cameraProfile() {
		return (DOF3Profile)camProfile;
	}
	
	@Override
	public DOF3Profile frameProfile() {
		return (DOF3Profile)frameProfile;
	}
	
	public void setXTranslationSensitivity(float s) {
		sens.set(0, s);
	}
	
	public void setYTranslationSensitivity(float s) {
		sens.set(1, s);
	}
	
	public void setZTranslationSensitivity(float s) {
		sens.set(2, s);
	}
}
