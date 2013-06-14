package remixlab.remixcam.device;

import remixlab.remixcam.core.*;
import remixlab.remixcam.event.DLKeyEvent;
import remixlab.remixcam.profile.ClickProfile;
import remixlab.remixcam.profile.DOF2Profile;

public class DLMouse extends AbstractMotionDevice implements Constants {
	public DLMouse(AbstractScene scn, String n) {
		super(scn, n);
		camProfile = new DOF2Profile();
		frameProfile = new DOF2Profile();
		clickProfile = new ClickProfile();
		sens = new float[2];
		sens[0] = 1f;
		sens[1] = 1f;
		cameraProfile().setBinding(CENTER, DOF_2Action.ZOOM);
		cameraProfile().setBinding(LEFT, DOF_2Action.ROTATE);
		cameraProfile().setBinding(RIGHT, DOF_2Action.TRANSLATE);
		//testing things out :P
		frameProfile().setBinding(CENTER, DOF_2Action.ZOOM);
		frameProfile().setBinding(RIGHT, DOF_2Action.ROTATE);
		frameProfile().setBinding(LEFT, DOF_2Action.TRANSLATE);
		
		clickProfile().setClickBinding(LEFT, 1, DOF_0Action.DRAW_FRAME_SELECTION_HINT);
		clickProfile().setClickBinding(RIGHT, 1, DOF_0Action.DRAW_AXIS);
		
		//clickProfile().setClickBinding(LEFT, 1, DOF_0Action.DRAW_AXIS);		
		//setClickBinding(RIGHT, 2, DOF_0Action.DRAW_GRID);
		
		//clickProfile().setClickBinding(RIGHT, 1, DOF_0Action.DRAW_FRAME_SELECTION_HINT);
		
		clickProfile().setClickBinding(DLKeyEvent.SHIFT, LEFT, 2, DOF_0Action.ALIGN_CAMERA);
		clickProfile().setClickBinding(DLKeyEvent.SHIFT, CENTER, 2, DOF_0Action.SHOW_ALL);
		clickProfile().setClickBinding((DLKeyEvent.SHIFT | DLKeyEvent.CTRL ), RIGHT, 2, DOF_0Action.ZOOM_TO_FIT);
	}
	
	@Override
	public DOF2Profile cameraProfile() {
		return (DOF2Profile)camProfile;
	}
	
	@Override
	public DOF2Profile frameProfile() {
		return (DOF2Profile)frameProfile;
	}
	
	@Override
	public ClickProfile clickProfile() {
		return (ClickProfile)clickProfile;
	}
	
	public void setXTranslationSensitivity(float s) {
		sens[0] = s;
	}
	
	public void setYTranslationSensitivity(float s) {
		sens[1] = s;
	}
	
	public void setSensitivities(float x, float y) {
		sens[0] = x;
		sens[1] = y;
	}
}
