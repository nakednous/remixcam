package remixlab.remixcam.action;

import remixlab.remixcam.core.Constants;
import remixlab.remixcam.core.Constants.DLAction;

public enum DOF_0Action implements Actionable, Constants {
  //DOF_0
	NO_ACTION(DLAction.NO_ACTION),
	
	// CLICk ACTIONs	  	
	ZOOM_TO_FIT(DLAction.ZOOM_TO_FIT),
	CENTER_FRAME(DLAction.CENTER_FRAME),
	CENTER_SCENE(DLAction.CENTER_SCENE),
	ALIGN_FRAME(DLAction.ALIGN_FRAME),
	ALIGN_CAMERA(DLAction.ALIGN_CAMERA),
	
  //Click actions require cursor pos:
  ZOOM_ON_PIXEL(DLAction.ZOOM_ON_PIXEL),
  INTERPOLATE_TO_ZOOM_ON_PIXEL(DLAction.INTERPOLATE_TO_ZOOM_ON_PIXEL),
  ARP_FROM_PIXEL(DLAction.ARP_FROM_PIXEL),
	
	//GENERAL KEYBOARD ACTIONs	
	DRAW_AXIS(DLAction.DRAW_AXIS),
	DRAW_GRID(DLAction.DRAW_GRID),
	CAMERA_PROFILE(DLAction.CAMERA_PROFILE),
	CAMERA_TYPE(DLAction.CAMERA_TYPE),
	CAMERA_KIND(DLAction.CAMERA_KIND),
	ANIMATION(DLAction.ANIMATION),  	
	RESET_ARP(DLAction.RESET_ARP),
	GLOBAL_HELP(DLAction.GLOBAL_HELP),
	CURRENT_CAMERA_PROFILE_HELP(DLAction.CURRENT_CAMERA_PROFILE_HELP),
	EDIT_CAMERA_PATH(DLAction.EDIT_CAMERA_PATH),
	FOCUS_INTERACTIVE_FRAME(DLAction.FOCUS_INTERACTIVE_FRAME),
	DRAW_FRAME_SELECTION_HINT(DLAction.DRAW_FRAME_SELECTION_HINT),
	CONSTRAIN_FRAME(DLAction.CONSTRAIN_FRAME),
	INTERPOLATE_TO_FIT_SCENE(DLAction.INTERPOLATE_TO_FIT_SCENE),
	SHOW_ALL(DLAction.SHOW_ALL),
	
  //CAMERA KEYBOARD ACTIONs
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

	DOF_0Action(DLAction a) {
		act = a;
	}
}
