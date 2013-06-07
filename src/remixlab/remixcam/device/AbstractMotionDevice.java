package remixlab.remixcam.device;

import remixlab.remixcam.core.*;
import remixlab.remixcam.profile.*;

public abstract class AbstractMotionDevice extends AbstractDevice {
	protected AbstractMotionProfile<?> camProfile, frameProfile;
	
	public AbstractMotionDevice(AbstractScene scn, String n) {
		super(scn, n);
	}
	
	public AbstractMotionProfile<?> cameraProfile() {
		return camProfile;
	}
	
	public AbstractMotionProfile<?> frameProfile() {
		return camProfile;
	}
	
	public void setCameraProfile(AbstractMotionProfile<?>	profile) {
		camProfile = profile;
	}
	
	public void setFrameProfile(AbstractMotionProfile<?>	profile) {
		frameProfile = profile;
	}
}
