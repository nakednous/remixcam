package remixlab.remixcam.event;

public enum DLAction {		
	//KEYBOARD ACTIONs
	
	/** Toggles the display of the world axis. */
	DRAW_AXIS("Toggles the display of the world axis", true),
	/** Toggles the display of the XY grid. */
	DRAW_GRID("Toggles the display of the XY grid", true),
	/** Cycles to the registered camera profiles. */
	CAMERA_PROFILE("Cycles to the registered camera profiles", true),
	/** Toggles camera type (orthographic or perspective. */
	CAMERA_TYPE("Toggles camera type (orthographic or perspective)", false),
	/** Toggles camera kind (proscene or standard). */
	CAMERA_KIND("Toggles camera kind (proscene or standard)", false),
	/** Toggles animation. */
	ANIMATION("Toggles animation", true),
	/** Set the arcball reference point from the pixel under the mouse. */
	ARP_FROM_PIXEL("Set the arcball reference point from the pixel under the mouse", true),
	/** Reset the arcball reference point to the 3d frame world origin. */
	RESET_ARP("Reset the arcball reference point to the 3d frame world origin", true),
	/** Displays the global help. */
	GLOBAL_HELP("Displays the global help", true),
	/** Displays the current camera profile help. */
	CURRENT_CAMERA_PROFILE_HELP("Displays the current camera profile help", true),
	/** Toggles the key frame camera paths (if any) for edition. */
	EDIT_CAMERA_PATH("Toggles the key frame camera paths (if any) for edition", true),
	/** Toggle interactivity between camera and interactive frame (if any). */
	FOCUS_INTERACTIVE_FRAME("Toggle interactivity between camera and interactive frame (if any)", true),
	/** Toggle interactive frame selection region drawing. */
	DRAW_FRAME_SELECTION_HINT("Toggle interactive frame selection region drawing", true),
	/** Toggles on and off frame constraints (if any). */
	CONSTRAIN_FRAME("Toggles on and off frame constraints (if any)", true),
	
  //CAMERA KEYBOARD ACTIONs
	
	/** Interpolate the camera to zoom on pixel. */
	INTERPOLATE_TO_ZOOM_ON_PIXEL("Interpolate the camera to zoom on pixel", true),
	/** Interpolate the camera to fit the whole scene. */
	INTERPOLATE_TO_FIT_SCENE("Interpolate the camera to fit the whole scene", true),
	/** Show the whole scene. */
	SHOW_ALL("Show the whole scene", true),
	/** Move camera to the left. */
	MOVE_CAMERA_LEFT("Move camera to the left", true),
	/** Move camera to the right. */
	MOVE_CAMERA_RIGHT("Move camera to the right", true),
	/** Move camera up. */
	MOVE_CAMERA_UP("Move camera up", true),
	/** Move camera down. */
	MOVE_CAMERA_DOWN("Move camera down", true),
	/** Increase camera rotation sensitivity (only meaningful in arcball mode). */
	INCREASE_ROTATION_SENSITIVITY("Increase camera rotation sensitivity (only meaningful in arcball mode)", true),
	/** Decrease camera rotation sensitivity (only meaningful in arcball mode). */
	DECREASE_ROTATION_SENSITIVITY("Decrease camera rotation sensitivity (only meaningful in arcball mode)", true),
	/** Increase camera fly speed (only meaningful in first-person mode). */
	INCREASE_CAMERA_FLY_SPEED("Increase camera fly speed (only meaningful in first-person mode)", false),
	/** Decrease camera fly speed (only meaningful in first-person mode). */
	DECREASE_CAMERA_FLY_SPEED("Decrease camera fly speed (only meaningful in first-person mode)", false),
	/** Increase avatar fly speed (only meaningful in third-person mode). */
	INCREASE_AVATAR_FLY_SPEED("Increase avatar fly speed (only meaningful in third-person mode)", false),
	/** Decrease avatar fly speed (only meaningful in third-person mode). */
	DECREASE_AVATAR_FLY_SPEED("Decrease avatar fly speed (only meaningful in third-person mode)", false),
	/** Increase camera azymuth respect to the avatar (only meaningful in third-person mode). */
	INCREASE_AZYMUTH("Increase camera azymuth respect to the avatar (only meaningful in third-person mode)", false),
	/** Decrease camera azymuth respect to the avatar (only meaningful in third-person mode). */
	DECREASE_AZYMUTH("Decrease camera azymuth respect to the avatar (only meaningful in third-person mode)", false),
	/** Increase camera inclination respect to the avatar (only meaningful in third-person mode). */
	INCREASE_INCLINATION("Increase camera inclination respect to the avatar (only meaningful in third-person mode)", false),
	/** Decrease camera inclination respect to the avatar (only meaningful in third-person mode). */
	DECREASE_INCLINATION("Decrease camera inclination respect to the avatar (only meaningful in third-person mode)", false),
	/** Increase camera tracking distance respect to the avatar (only meaningful in third-person mode). */
	INCREASE_TRACKING_DISTANCE("Increase camera tracking distance respect to the avatar (only meaningful in third-person mode)", false),
	/** Decrease camera tracking distance respect to the avatar (only meaningful in third-person mode). */
	DECREASE_TRACKING_DISTANCE("Decrease camera tracking distance respect to the avatar (only meaningful in third-person mode)", false),
	
	// CLICk ACTIONs	
	
	/** No click action. */
	NO_CLICK_ACTION("No click action", true),
	/** Zoom on pixel */
	ZOOM_ON_PIXEL("Zoom on pixel", true),
	/** Zoom to fit the scene */
	ZOOM_TO_FIT("Zoom to fit the scene", true),
	//ARP_FROM_PIXEL("Set the arcball reference point from the pixel under the mouse"),
	//RESET_ARP("Reset the arcball reference point to the 3d frame world origin"),
	/** Center frame */
	CENTER_FRAME("Center frame", true),
	/** Center scene */
	CENTER_SCENE("Center scene", true),
	//SHOW_ALL("Show the whole scene"),
	/** Align interactive frame (if any) with world */
	ALIGN_FRAME("Align interactive frame (if any) with world", true),
	/** Align camera with world */
	ALIGN_CAMERA("Align camera with world", true),
	
	// DEVICE ACTIONs
	
	/** No mouse action. */
	NO_DEVICE_ACTION("No device action", true),
	/** Rotate frame (camera or interactive frame. */
	ROTATE("Rotate frame (camera or interactive frame)", true),
	/** Rotate (only) camera frame as in CAD applications. */
	CAD_ROTATE("Rotate (only) camera frame as in CAD applications)", false),
	/** Zoom. */
	ZOOM("Zoom", true),
	/** Translate frame (camera or interactive frame). */
	TRANSLATE("Translate frame (camera or interactive frame)", true),
	/** Move forward frame (camera or interactive frame). */
	MOVE_FORWARD("Move forward frame (camera or interactive frame)", false),
	/** move backward frame (camera or interactive frame). */
	MOVE_BACKWARD("move backward frame (camera or interactive frame)", false),
	/** Look around with frame (camera or interactive drivable frame). */
	LOOK_AROUND("Look around with frame (camera or interactive drivable frame)", false),
	/** Screen rotate (camera or interactive frame). */
	SCREEN_ROTATE("Screen rotate (camera or interactive frame)", true),
	/** Roll frame (camera or interactive drivable frame). */
	ROLL("Roll frame (camera or interactive drivable frame)", true),
	/** Drive (camera or interactive drivable frame). */
	DRIVE("Drive (camera or interactive drivable frame)", false),
	/** Screen translate frame (camera or interactive frame). */
	SCREEN_TRANSLATE("Screen translate frame (camera or interactive frame)", true),
	/** Zoom on region (camera or interactive drivable frame). */
	ZOOM_ON_REGION("Zoom on region (camera or interactive drivable frame)", true),
	
	//CUSTOM ACTIONs
	
	CUSTOM1("User defined action no 1", false),
	CUSTOM2("User defined action no 2", false),
	CUSTOM1_2D("User defined action no 3", true),
	CUSTOM2_2D("User defined action no 4", true);
	
	String description;
	boolean twoD;
	
	DLAction(String description, boolean td) {
     this.description = description;
     this.twoD = td;
  }
  
  public String description() {
    return description;
  }
  
  public boolean is2D() {
  	return twoD;
  }
}
