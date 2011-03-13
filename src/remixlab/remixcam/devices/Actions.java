package remixlab.remixcam.devices;

public class Actions {
	/**
	 * Defines the different actions that can be associated with a specific
	 * keyboard key.
	 */
	public enum KeyboardAction {
		/** Toggles the display of the world axis. */
		DRAW_AXIS("Toggles the display of the world axis"),
		/** Toggles the display of the XY grid. */
		DRAW_GRID("Toggles the display of the XY grid"),
		/** Cycles to the registered camera profiles. */
		CAMERA_PROFILE("Cycles to the registered camera profiles"),
		/** Toggles camera type (orthographic or perspective. */
		CAMERA_TYPE("Toggles camera type (orthographic or perspective)"),
		/** Toggles camera kind (proscene or standard). */
		CAMERA_KIND("Toggles camera kind (proscene or standard)"),
		/** Toggles animation. */
		ANIMATION("Toggles animation"),
		/** Set the arcball reference point from the pixel under the mouse. */
		ARP_FROM_PIXEL("Set the arcball reference point from the pixel under the mouse"),
		/** Reset the arcball reference point to the 3d frame world origin. */
		RESET_ARP("Reset the arcball reference point to the 3d frame world origin"),
		/** Displays the global help. */
		GLOBAL_HELP("Displays the global help"),
		/** Displays the current camera profile help. */
		CURRENT_CAMERA_PROFILE_HELP("Displays the current camera profile help"),
		/** Toggles the key frame camera paths (if any) for edition. */
		EDIT_CAMERA_PATH("Toggles the key frame camera paths (if any) for edition"),
		/** Toggle interactivity between camera and interactive frame (if any). */
		FOCUS_INTERACTIVE_FRAME("Toggle interactivity between camera and interactive frame (if any)"),
		/** Toggle interactive frame selection region drawing. */
		DRAW_FRAME_SELECTION_HINT("Toggle interactive frame selection region drawing"),
		/** Toggles on and off frame constraints (if any). */
		CONSTRAIN_FRAME("Toggles on and off frame constraints (if any)");
		
		private String description;
		
		KeyboardAction(String description) {
	     this.description = description;
	  }
	  
	  public String description() {
	    return description;
	  }
	}
	
	/**
	 * Defines the different camera actions that can be associated with a specific
	 * keyboard key. Actions are defined here, but bindings are defined at the CameraProfile level,
	 * i.e., the scene acts like a bridge between the CameraProfile and proscene low-level classes.
	 */
	public enum CameraKeyboardAction {
		/** Interpolate the camera to zoom on pixel. */
		INTERPOLATE_TO_ZOOM_ON_PIXEL("Interpolate the camera to zoom on pixel"),
		/** Interpolate the camera to fit the whole scene. */
		INTERPOLATE_TO_FIT_SCENE("Interpolate the camera to fit the whole scene"),
		/** Show the whole scene. */
		SHOW_ALL("Show the whole scene"),
		/** Move camera to the left. */
		MOVE_CAMERA_LEFT("Move camera to the left"),
		/** Move camera to the right. */
		MOVE_CAMERA_RIGHT("Move camera to the right"),
		/** Move camera up. */
		MOVE_CAMERA_UP("Move camera up"),
		/** Move camera down. */
		MOVE_CAMERA_DOWN("Move camera down"),
		/** Increase camera rotation sensitivity (only meaningful in arcball mode). */
		INCREASE_ROTATION_SENSITIVITY("Increase camera rotation sensitivity (only meaningful in arcball mode)"),
		/** Decrease camera rotation sensitivity (only meaningful in arcball mode). */
		DECREASE_ROTATION_SENSITIVITY("Decrease camera rotation sensitivity (only meaningful in arcball mode)"),
		/** Increase camera fly speed (only meaningful in first-person mode). */
		INCREASE_CAMERA_FLY_SPEED("Increase camera fly speed (only meaningful in first-person mode)"),
		/** Decrease camera fly speed (only meaningful in first-person mode). */
		DECREASE_CAMERA_FLY_SPEED("Decrease camera fly speed (only meaningful in first-person mode)"),
		/** Increase avatar fly speed (only meaningful in third-person mode). */
		INCREASE_AVATAR_FLY_SPEED("Increase avatar fly speed (only meaningful in third-person mode)"),
		/** Decrease avatar fly speed (only meaningful in third-person mode). */
		DECREASE_AVATAR_FLY_SPEED("Decrease avatar fly speed (only meaningful in third-person mode)"),
		/** Increase camera azymuth respect to the avatar (only meaningful in third-person mode). */
		INCREASE_AZYMUTH("Increase camera azymuth respect to the avatar (only meaningful in third-person mode)"),
		/** Decrease camera azymuth respect to the avatar (only meaningful in third-person mode). */
		DECREASE_AZYMUTH("Decrease camera azymuth respect to the avatar (only meaningful in third-person mode)"),
		/** Increase camera inclination respect to the avatar (only meaningful in third-person mode). */
		INCREASE_INCLINATION("Increase camera inclination respect to the avatar (only meaningful in third-person mode)"),
		/** Decrease camera inclination respect to the avatar (only meaningful in third-person mode). */
		DECREASE_INCLINATION("Decrease camera inclination respect to the avatar (only meaningful in third-person mode)"),
		/** Increase camera tracking distance respect to the avatar (only meaningful in third-person mode). */
		INCREASE_TRACKING_DISTANCE("Increase camera tracking distance respect to the avatar (only meaningful in third-person mode)"),
		/** Decrease camera tracking distance respect to the avatar (only meaningful in third-person mode). */
		DECREASE_TRACKING_DISTANCE("Decrease camera tracking distance respect to the avatar (only meaningful in third-person mode)");
		
		private String description;
		
		CameraKeyboardAction(String description) {
       this.description = description;
    }
		
    public String description() {
        return description;
    }
	}
	
	/**
	 * This enum defines mouse click actions to be binded to the mouse.
	 * Actions are defined here, but bindings are defined at the CameraProfile level,
	 * i.e., the scene acts like a bridge between the CameraProfile and proscene low-level classes.
	 */
	public enum ClickAction {
		/** No click action. */
		NO_CLICK_ACTION("No click action"),
		/** Zoom on pixel */
		ZOOM_ON_PIXEL("Zoom on pixel"),
		/** Zoom to fit the scene */
		ZOOM_TO_FIT("Zoom to fit the scene"),
		/** Set the arcball reference point from the pixel under the mouse */
		ARP_FROM_PIXEL("Set the arcball reference point from the pixel under the mouse"),
		/** Reset the arcball reference point to the 3d frame world origin */
		RESET_ARP("Reset the arcball reference point to the 3d frame world origin"),
		/** Center frame */
		CENTER_FRAME("Center frame"),
		/** Center scene */
		CENTER_SCENE("Center scene"),
		/** Show the whole scene */
		SHOW_ALL("Show the whole scene"),
		/** Align interactive frame (if any) with world */
		ALIGN_FRAME("Align interactive frame (if any) with world"),
		/** Align camera with world */
		ALIGN_CAMERA("Align camera with world");

		private String description;
		
		ClickAction(String description) {
       this.description = description;
    }
		
    public String description() {
        return description;
    }
	}

	/**
	 * This enum defines mouse actions (click + drag) to be binded to the mouse.
	 * Actions are defined here, but bindings are defined at the CameraProfile level,
	 * i.e., the scene acts like a bridge between the CameraProfile and proscene low-level classes.
	 */
	public enum MouseAction {
		/** No mouse action. */
		NO_MOUSE_ACTION("No mouse action"),
		/** Rotate frame (camera or interactive frame. */
		ROTATE("Rotate frame (camera or interactive frame)"),
		/** Zoom. */
		ZOOM("Zoom"),
		/** Translate frame (camera or interactive frame). */
		TRANSLATE("Translate frame (camera or interactive frame)"),
		/** Move forward frame (camera or interactive frame). */
		MOVE_FORWARD("Move forward frame (camera or interactive frame)"),
		/** move backward frame (camera or interactive frame). */
		MOVE_BACKWARD("move backward frame (camera or interactive frame)"),
		/** Look around with frame (camera or interactive drivable frame). */
		LOOK_AROUND("Look around with frame (camera or interactive drivable frame)"),
		/** Screen rotate (camera or interactive frame). */
		SCREEN_ROTATE("Screen rotate (camera or interactive frame)"),
		/** Roll frame (camera or interactive drivable frame). */
		ROLL("Roll frame (camera or interactive drivable frame)"),
		/** Drive (camera or interactive drivable frame). */
		DRIVE("Drive (camera or interactive drivable frame)"),
		/** Screen translate frame (camera or interactive frame). */
		SCREEN_TRANSLATE("Screen translate frame (camera or interactive frame)"),
		/** Zoom on region (camera or interactive drivable frame). */
		ZOOM_ON_REGION("Zoom on region (camera or interactive drivable frame)");		

		private String description;
		
		MouseAction(String description) {
       this.description = description;
    }
		
    public String description() {
        return description;
    }
	}
}
