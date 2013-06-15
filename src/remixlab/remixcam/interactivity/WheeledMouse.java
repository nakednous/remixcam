package remixlab.remixcam.interactivity;

import remixlab.remixcam.core.AbstractScene;
import remixlab.remixcam.event.*;

public class WheeledMouse extends Mouse {
	protected DOF1Profile wheelProfile;
	protected DOF1Profile frameWheelProfile;
	
	public WheeledMouse(AbstractScene scn, String n) {
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
	public void handle(GenericEvent<?> event) {
		if( event instanceof DOF1Event ) {
			if(scene.aliveInteractiveFrame() != null)
				wheelProfile.handle(event);
			else
				frameWheelProfile.handle(event);
			((GenericMotionEvent<?>)event).modulate(sens);
			if( scene.isDeviceRegistered(this) ) event.enqueue(scene);
		}
		else
			super.handle(event);
	}
}
