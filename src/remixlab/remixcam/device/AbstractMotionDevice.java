package remixlab.remixcam.device;

import remixlab.remixcam.core.*;
import remixlab.remixcam.event.*;
import remixlab.remixcam.profile.*;

public abstract class AbstractMotionDevice extends AbstractDevice implements Constants {
	protected AbstractMotionProfile<?> camProfile, frameProfile;
	protected AbstractClickProfile<?> clickProfile;
	protected float[] sens;
	
	public AbstractMotionDevice(AbstractScene scn, String n) {
		super(scn, n);	
	}
	
	public AbstractMotionProfile<?> cameraProfile() {
		return camProfile;
	}
	
	public AbstractMotionProfile<?> frameProfile() {
		return camProfile;
	}
	
	public AbstractClickProfile<?> clickProfile() {
		return clickProfile;
	}
	
	public void setCameraProfile(AbstractMotionProfile<?>	profile) {
		camProfile = profile;
	}
	
	public void setFrameProfile(AbstractMotionProfile<?> profile) {
		frameProfile = profile;
	}
	
	public void setClickProfile(AbstractClickProfile<?> profile) {
		clickProfile = profile;
	}
	
	public float [] sensitivities() {
		return sens;
	}
	
	// /**
	@Override
	public void handle(DLEvent<?> event) {
		if(event == null)	return;
		if( event instanceof DLClickEvent )
			clickProfile.handle(event);
		else {
			if(scene.aliveInteractiveFrame() != null)
				frameProfile.handle(event);
			else
				camProfile.handle(event);
			((MotionEvent<?>)event).modulate(sens);
		}
		if( scene.isDeviceRegistered(this) ) event.enqueue(scene);
	}
	// */
	
	 /**
	@Override
	public void handle(DLEvent event) {
		if( event instanceof DLClickEvent )
			clickProfile.handle(event);
		else
			if(scene.activeFrame() != null)
				frameProfile.handle(event);
			else
				camProfile.handle(event);
		event.enqueue(scene);
	}
	// */
}
