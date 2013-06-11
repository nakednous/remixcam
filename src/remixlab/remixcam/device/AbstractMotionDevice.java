package remixlab.remixcam.device;

import remixlab.remixcam.core.*;
import remixlab.remixcam.event.*;
import remixlab.remixcam.profile.*;

public abstract class AbstractMotionDevice extends AbstractDevice implements Constants {
	protected AbstractMotionProfile<?> camProfile, frameProfile;
	protected ClickProfile clickProfile;
	protected float[] sens;
	
	public AbstractMotionDevice(AbstractScene scn, String n) {
		super(scn, n);
		clickProfile = new ClickProfile();
		
		clickProfile().setClickBinding(LEFT, 1, DOF_0Action.DRAW_FRAME_SELECTION_HINT);
		clickProfile().setClickBinding(RIGHT, 1, DOF_0Action.DRAW_AXIS);
		
		//clickProfile().setClickBinding(LEFT, 1, DOF_0Action.DRAW_AXIS);		
		//setClickBinding(RIGHT, 2, DOF_0Action.DRAW_GRID);
		
		//clickProfile().setClickBinding(RIGHT, 1, DOF_0Action.DRAW_FRAME_SELECTION_HINT);
		
		clickProfile().setClickBinding(DLKeyEvent.SHIFT, LEFT, 2, DOF_0Action.ALIGN_CAMERA);
		clickProfile().setClickBinding(DLKeyEvent.SHIFT, CENTER, 2, DOF_0Action.SHOW_ALL);
		clickProfile().setClickBinding((DLKeyEvent.SHIFT | DLKeyEvent.CTRL ), RIGHT, 2, DOF_0Action.ZOOM_TO_FIT);	
	}
	
	public AbstractMotionProfile<?> cameraProfile() {
		return camProfile;
	}
	
	public AbstractMotionProfile<?> frameProfile() {
		return camProfile;
	}
	
	public float [] sensitivities() {
		return sens;
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
