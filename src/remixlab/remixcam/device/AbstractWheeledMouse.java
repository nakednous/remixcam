package remixlab.remixcam.device;

import remixlab.remixcam.core.AbstractScene;
import remixlab.remixcam.event.*;
import remixlab.remixcam.profile.DOF1Profile;

public class AbstractWheeledMouse extends AbstractMouse {
	protected DOF1Profile wheelProfile;
	protected DOF1Profile frameWheelProfile;
	
	public AbstractWheeledMouse(AbstractScene scn, String n) {
		super(scn, n);
		wheelProfile = new DOF1Profile();
		frameWheelProfile = new DOF1Profile();
		
		wheelProfile.setBinding(DOF_1Action.ZOOM);
		frameWheelProfile.setBinding(DOF_1Action.ZOOM);
	}
	
	public DOF1Profile wheelProfile() {
		return wheelProfile;
	}
	
	public void setWheelProfile(DOF1Profile profile) {
		wheelProfile = profile;
	}
	
	public DOF1Profile frameWheelProfile() {
		return frameWheelProfile;
	}
	
	public void setFrameWheelProfile(DOF1Profile profile) {
		frameWheelProfile = profile;
	}
	
	@Override
	public void handle(DLEvent<?> event) {
		if( event instanceof DOF1Event ) {
			if(scene.activeFrame() != null)
				wheelProfile.handle(event);
			else
				frameWheelProfile.handle(event);
			((MotionEvent<?>)event).modulate(sens);
			event.enqueue(scene);
		}
		else
			super.handle(event);
	}
}
