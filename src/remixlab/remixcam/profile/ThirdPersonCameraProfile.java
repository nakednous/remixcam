package remixlab.remixcam.profile;

import remixlab.remixcam.core.AbstractScene;

public class ThirdPersonCameraProfile extends CameraProfile {

	public ThirdPersonCameraProfile(AbstractScene scn, String n) {
		super(scn, n);
		setFrameMouseBinding(LEFT, AbstractScene.DeviceAction.MOVE_FORWARD);
		setFrameMouseBinding(CENTER, AbstractScene.DeviceAction.LOOK_AROUND);
		setFrameMouseBinding(RIGHT, AbstractScene.DeviceAction.MOVE_BACKWARD);
		setFrameMouseBinding(SHIFT, LEFT, AbstractScene.DeviceAction.ROLL);
		setFrameMouseBinding(SHIFT, RIGHT, AbstractScene.DeviceAction.DRIVE);

		setShortcut('+', AbstractScene.CameraKeyboardAction.INCREASE_AVATAR_FLY_SPEED);
		setShortcut('-', AbstractScene.CameraKeyboardAction.DECREASE_AVATAR_FLY_SPEED);
		setShortcut('a', AbstractScene.CameraKeyboardAction.INCREASE_AZYMUTH);
		setShortcut('A', AbstractScene.CameraKeyboardAction.DECREASE_AZYMUTH);
		setShortcut('i', AbstractScene.CameraKeyboardAction.INCREASE_INCLINATION);
		setShortcut('I', AbstractScene.CameraKeyboardAction.DECREASE_INCLINATION);
		setShortcut('t', AbstractScene.CameraKeyboardAction.INCREASE_TRACKING_DISTANCE);
		setShortcut('T', AbstractScene.CameraKeyboardAction.DECREASE_TRACKING_DISTANCE);
	}
}
