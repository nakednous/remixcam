package remixlab.remixcam.device;

import java.util.ArrayList;

import remixlab.remixcam.core.*;
import remixlab.remixcam.profile.DOF6Profile;

public class AbstractHIDevice  extends AbstractMotionDevice {
	public AbstractHIDevice(AbstractScene scn, String n) {
		super(scn, n);
		camProfile = new DOF6Profile();
		frameProfile = new DOF6Profile();
		sens = new ArrayList<Float>(6);
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
		sens.set(0, s);
	}
	
	public void setYTranslationSensitivity(float s) {
		sens.set(1, s);
	}
	
	public void setZTranslationSensitivity(float s) {
		sens.set(2, s);
	}
	
	public void setXRotationSensitivity(float s) {
		sens.set(3, s);
	}
	
	public void setYRotationSensitivity(float s) {
		sens.set(4, s);
	}
	
	public void setZRotationSensitivity(float s) {
		sens.set(5, s);
	}
}
