package remixlab.remixcam.event;

import remixlab.remixcam.action.Actionable;
import remixlab.remixcam.core.Constants;

public enum DLCameraKeyboardAction implements Actionable, Constants {
	/**
	 * Defines the different camera actions that can be associated with a specific
	 * keyboard key. Actions are defined here, but bindings are defined at the CameraProfile level,
	 * i.e., the scene acts like a bridge between the CameraProfile and proscene low-level classes.
	 */
	INTERPOLATE_TO_ZOOM_ON_PIXEL(DLAction.INTERPOLATE_TO_ZOOM_ON_PIXEL),	
	INTERPOLATE_TO_FIT_SCENE(DLAction.INTERPOLATE_TO_FIT_SCENE),	
	SHOW_ALL(DLAction.SHOW_ALL),	
	MOVE_CAMERA_LEFT(DLAction.MOVE_CAMERA_LEFT),	
	MOVE_CAMERA_RIGHT(DLAction.MOVE_CAMERA_RIGHT),	
	MOVE_CAMERA_UP(DLAction.MOVE_CAMERA_UP),	
	MOVE_CAMERA_DOWN(DLAction.MOVE_CAMERA_DOWN),	
	INCREASE_ROTATION_SENSITIVITY(DLAction.INCREASE_ROTATION_SENSITIVITY),	
	DECREASE_ROTATION_SENSITIVITY(DLAction.DECREASE_ROTATION_SENSITIVITY),	
	INCREASE_CAMERA_FLY_SPEED(DLAction.INCREASE_CAMERA_FLY_SPEED),	
	DECREASE_CAMERA_FLY_SPEED(DLAction.DECREASE_CAMERA_FLY_SPEED),	
	INCREASE_AVATAR_FLY_SPEED(DLAction.INCREASE_AVATAR_FLY_SPEED),	
	DECREASE_AVATAR_FLY_SPEED(DLAction.DECREASE_AVATAR_FLY_SPEED),	
	INCREASE_AZYMUTH(DLAction.INCREASE_AZYMUTH),	
	DECREASE_AZYMUTH(DLAction.DECREASE_AZYMUTH),	
	INCREASE_INCLINATION(DLAction.INCREASE_INCLINATION),	
	DECREASE_INCLINATION(DLAction.DECREASE_INCLINATION),	
	INCREASE_TRACKING_DISTANCE(DLAction.INCREASE_TRACKING_DISTANCE),	
	DECREASE_TRACKING_DISTANCE(DLAction.DECREASE_TRACKING_DISTANCE),
	CUSTOM1(DLAction.CUSTOM1),
	CUSTOM2(DLAction.CUSTOM2),
	CUSTOM3(DLAction.CUSTOM3),
	CUSTOM4(DLAction.CUSTOM4);
	
	@Override
	public DLAction action() {
		return act;
	}

	@Override
	public String description() {
		return this.action().description();
	}
	
	@Override
	public boolean is2D() {
		return act.is2D();
	}

	DLAction act;

	DLCameraKeyboardAction(DLAction a) {
		act = a;
	}
}
