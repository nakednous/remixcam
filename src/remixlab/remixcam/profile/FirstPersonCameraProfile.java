package remixlab.remixcam.profile;

import remixlab.remixcam.core.AbstractScene;
import remixlab.remixcam.event.*;

public class FirstPersonCameraProfile extends CameraProfile {
	public FirstPersonCameraProfile(AbstractScene scn, String n) {
		super(scn, n);
		setCameraMouseBinding(LEFT, DLDeviceAction.MOVE_FORWARD);
		setCameraMouseBinding(CENTER, DLDeviceAction.LOOK_AROUND);
		setCameraMouseBinding(RIGHT, DLDeviceAction.MOVE_BACKWARD);   		
		setCameraMouseBinding(SHIFT, LEFT, DLDeviceAction.ROLL);			
		setCameraMouseBinding(SHIFT, RIGHT, DLDeviceAction.DRIVE);
		setFrameMouseBinding(LEFT, DLDeviceAction.ROTATE);
		setFrameMouseBinding(CENTER, DLDeviceAction.ZOOM);
		setFrameMouseBinding(RIGHT, DLDeviceAction.TRANSLATE);

		setShortcut('+', DLCameraKeyboardAction.INCREASE_CAMERA_FLY_SPEED);
		setShortcut('-', DLCameraKeyboardAction.DECREASE_CAMERA_FLY_SPEED);

		setShortcut('s', DLCameraKeyboardAction.INTERPOLATE_TO_FIT_SCENE);
		setShortcut('S', DLCameraKeyboardAction.SHOW_ALL);
	}
}
