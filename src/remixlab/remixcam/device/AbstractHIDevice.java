package remixlab.remixcam.device;

import remixlab.remixcam.core.*;
import remixlab.remixcam.profile.DOF6Profile;

public class AbstractHIDevice extends AbstractMotionDevice {
	public AbstractHIDevice(AbstractScene scn, String n) {
		super(scn, n);
		camProfile = new DOF6Profile();
		frameProfile = new DOF6Profile();
		sens = new float[6];
		sens[0] = 1f;
		sens[1] = 1f;
		sens[2] = 1f;
		sens[3] = 1f;
		sens[4] = 1f;
		sens[5] = 1f;
		cameraProfile().setBinding(DOF_6Action.NATURAL);
		//cameraProfile().setBinding(DOF_6Action.GOOGLE_EARTH);
		frameProfile().setBinding(DOF_6Action.NATURAL);
	}
	
	@Override
	public DOF6Profile cameraProfile() {
		return (DOF6Profile)camProfile;
	}
	
	@Override
	public DOF6Profile frameProfile() {
		return (DOF6Profile)frameProfile;
	}
	
	public void setXTranslationSensitivity(float s) {
		sens[0] = s;
	}
	
	public void setYTranslationSensitivity(float s) {
		sens[1] = s;
	}
	
	public void setZTranslationSensitivity(float s) {
		sens[2] = s;
	}
	
	public void setXRotationSensitivity(float s) {
		sens[3] = s;
	}
	
	public void setYRotationSensitivity(float s) {
		sens[4] = s;
	}
	
	public void setZRotationSensitivity(float s) {
		sens[5] = s;
	}
}
