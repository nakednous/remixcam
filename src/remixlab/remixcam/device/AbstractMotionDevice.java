package remixlab.remixcam.device;

import java.util.ArrayList;

import remixlab.remixcam.core.*;
import remixlab.remixcam.event.*;
import remixlab.remixcam.profile.*;

public abstract class AbstractMotionDevice extends AbstractDevice {
	protected AbstractMotionProfile<?> camProfile, frameProfile;
	protected ClickProfile clickProfile;
	protected ArrayList<Float> sens;
	
	public AbstractMotionDevice(AbstractScene scn, String n) {
		super(scn, n);
		clickProfile = new ClickProfile();
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
	
	public void setFrameProfile(AbstractMotionProfile<?> profile) {
		frameProfile = profile;
	}
	
	public ClickProfile clickProfile() {
		return clickProfile;
	}
	
	public void setClickProfile(ClickProfile profile) {
		clickProfile = profile;
	}
	
	// /**
	@Override
	public void handle(DLEvent<?> event) {
		if( event instanceof DLClickEvent )
			clickProfile.handle(event);
		else {
			if(scene.activeFrame() != null)
				frameProfile.handle(event);
			else
				camProfile.handle(event);
			((MotionEvent<?>)event).modulate(sens);
		}
		event.enqueue(scene);
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
