package remixlab.remixcam.profile;

import remixlab.remixcam.action.DOF_0Action;
import remixlab.remixcam.action.DOF_6Action;
import remixlab.remixcam.core.AbstractScene;
import remixlab.remixcam.event.*;

public class FirstPersonCameraProfile extends CameraProfile {
	public FirstPersonCameraProfile(AbstractScene scn, String n) {
		super(scn, n);
		setCameraMouseBinding(LEFT, DOF_6Action.MOVE_FORWARD);
		setCameraMouseBinding(CENTER, DOF_6Action.LOOK_AROUND);
		setCameraMouseBinding(RIGHT, DOF_6Action.MOVE_BACKWARD);   		
		setCameraMouseBinding(SHIFT, LEFT, DOF_6Action.ROLL);			
		setCameraMouseBinding(SHIFT, RIGHT, DOF_6Action.DRIVE);
		setFrameMouseBinding(LEFT, DOF_6Action.ROTATE);
		setFrameMouseBinding(CENTER, DOF_6Action.ZOOM);
		setFrameMouseBinding(RIGHT, DOF_6Action.TRANSLATE);

		setShortcut('+', DOF_0Action.INCREASE_CAMERA_FLY_SPEED);
		setShortcut('-', DOF_0Action.DECREASE_CAMERA_FLY_SPEED);

		setShortcut('s', DOF_0Action.INTERPOLATE_TO_FIT_SCENE);
		setShortcut('S', DOF_0Action.SHOW_ALL);
	}
}
