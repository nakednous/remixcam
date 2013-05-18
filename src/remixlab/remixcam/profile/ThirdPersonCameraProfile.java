package remixlab.remixcam.profile;

import remixlab.remixcam.action.DOF_0Action;
import remixlab.remixcam.action.DOF_6Action;
import remixlab.remixcam.core.AbstractScene;
import remixlab.remixcam.event.*;

public class ThirdPersonCameraProfile extends CameraProfile {

	public ThirdPersonCameraProfile(AbstractScene scn, String n) {
		super(scn, n);
		setFrameMouseBinding(LEFT, DOF_6Action.MOVE_FORWARD);
		setFrameMouseBinding(CENTER, DOF_6Action.LOOK_AROUND);
		setFrameMouseBinding(RIGHT, DOF_6Action.MOVE_BACKWARD);
		setFrameMouseBinding(SHIFT, LEFT, DOF_6Action.ROLL);
		setFrameMouseBinding(SHIFT, RIGHT, DOF_6Action.DRIVE);

		setShortcut('+', DOF_0Action.INCREASE_AVATAR_FLY_SPEED);
		setShortcut('-', DOF_0Action.DECREASE_AVATAR_FLY_SPEED);
		setShortcut('a', DOF_0Action.INCREASE_AZYMUTH);
		setShortcut('A', DOF_0Action.DECREASE_AZYMUTH);
		setShortcut('i', DOF_0Action.INCREASE_INCLINATION);
		setShortcut('I', DOF_0Action.DECREASE_INCLINATION);
		setShortcut('t', DOF_0Action.INCREASE_TRACKING_DISTANCE);
		setShortcut('T', DOF_0Action.DECREASE_TRACKING_DISTANCE);
	}
}
