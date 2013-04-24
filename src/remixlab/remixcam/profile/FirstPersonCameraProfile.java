package remixlab.remixcam.profile;

import remixlab.remixcam.core.AbstractScene;

public class FirstPersonCameraProfile extends CameraProfile {
	public FirstPersonCameraProfile(AbstractScene scn, String n) {
		super(scn, n);
		setCameraMouseBinding(LEFT, AbstractScene.DeviceAction.MOVE_FORWARD);
		setCameraMouseBinding(CENTER, AbstractScene.DeviceAction.LOOK_AROUND);
		setCameraMouseBinding(RIGHT, AbstractScene.DeviceAction.MOVE_BACKWARD);   		
		setCameraMouseBinding(SHIFT, LEFT, AbstractScene.DeviceAction.ROLL);			
		setCameraMouseBinding(SHIFT, RIGHT, AbstractScene.DeviceAction.DRIVE);
		setFrameMouseBinding(LEFT, AbstractScene.DeviceAction.ROTATE);
		setFrameMouseBinding(CENTER, AbstractScene.DeviceAction.ZOOM);
		setFrameMouseBinding(RIGHT, AbstractScene.DeviceAction.TRANSLATE);

		setShortcut('+', AbstractScene.CameraKeyboardAction.INCREASE_CAMERA_FLY_SPEED);
		setShortcut('-', AbstractScene.CameraKeyboardAction.DECREASE_CAMERA_FLY_SPEED);

		setShortcut('s', AbstractScene.CameraKeyboardAction.INTERPOLATE_TO_FIT_SCENE);
		setShortcut('S', AbstractScene.CameraKeyboardAction.SHOW_ALL);
	}
}
