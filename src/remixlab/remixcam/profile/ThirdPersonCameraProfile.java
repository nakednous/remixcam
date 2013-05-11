package remixlab.remixcam.profile;

import remixlab.remixcam.core.AbstractScene;
import remixlab.remixcam.event.*;

public class ThirdPersonCameraProfile extends CameraProfile {

	public ThirdPersonCameraProfile(AbstractScene scn, String n) {
		super(scn, n);
		setFrameMouseBinding(LEFT, DLDeviceAction.MOVE_FORWARD);
		setFrameMouseBinding(CENTER, DLDeviceAction.LOOK_AROUND);
		setFrameMouseBinding(RIGHT, DLDeviceAction.MOVE_BACKWARD);
		setFrameMouseBinding(SHIFT, LEFT, DLDeviceAction.ROLL);
		setFrameMouseBinding(SHIFT, RIGHT, DLDeviceAction.DRIVE);

		setShortcut('+', DLCameraKeyboardAction.INCREASE_AVATAR_FLY_SPEED);
		setShortcut('-', DLCameraKeyboardAction.DECREASE_AVATAR_FLY_SPEED);
		setShortcut('a', DLCameraKeyboardAction.INCREASE_AZYMUTH);
		setShortcut('A', DLCameraKeyboardAction.DECREASE_AZYMUTH);
		setShortcut('i', DLCameraKeyboardAction.INCREASE_INCLINATION);
		setShortcut('I', DLCameraKeyboardAction.DECREASE_INCLINATION);
		setShortcut('t', DLCameraKeyboardAction.INCREASE_TRACKING_DISTANCE);
		setShortcut('T', DLCameraKeyboardAction.DECREASE_TRACKING_DISTANCE);
	}
}
