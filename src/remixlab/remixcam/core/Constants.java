/**
 *                     RemixCam (version 0.70.0)      
 *      Copyright (c) 2013 by National University of Colombia
 *                 @author Jean Pierre Charalambos      
 *              https://github.com/nakednous/remixcam
 *                           
 * This java library provides classes to ease the creation of interactive 3D
 * scenes in various languages and frameworks such as JOGL, WebGL and Processing.
 * 
 * This source file is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option)
 * any later version.
 * 
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 * 
 * A copy of the GNU General Public License is available on the World Wide Web
 * at <http://www.gnu.org/copyleft/gpl.html>. You can also obtain it by
 * writing to the Free Software Foundation, 51 Franklin Street, Suite 500
 * Boston, MA 02110-1335, USA.
 */

package remixlab.remixcam.core;


public interface Constants {	
	/**
   * PI is a mathematical constant with the value 3.14159265358979323846.
   * It is the ratio of the circumference of a circle to its diameter.
   * It is useful in combination with the trigonometric functions <b>sin()</b> and <b>cos()</b>.
   *  
   * @see #HALF_PI
   * @see #TWO_PI
   * @see #QUARTER_PI
   * 
   */
	static final float PI = (float) Math.PI;
  /**
   * HALF_PI is a mathematical constant with the value 1.57079632679489661923.
   * It is half the ratio of the circumference of a circle to its diameter.
   * It is useful in combination with the trigonometric functions <b>sin()</b> and <b>cos()</b>.
   * 
   * @see #PI
   * @see #TWO_PI
   * @see #QUARTER_PI
   */
  static final float HALF_PI    = PI / 2.0f;
  static final float THIRD_PI   = PI / 3.0f;
  /**
   * QUARTER_PI is a mathematical constant with the value 0.7853982.
   * It is one quarter the ratio of the circumference of a circle to its diameter.
   * It is useful in combination with the trigonometric functions <b>sin()</b> and <b>cos()</b>.
   * 
   * @see #PI
   * @see #TWO_PI
   * @see #HALF_PI
   */
  static final float QUARTER_PI = PI / 4.0f;
  /**
   * TWO_PI is a mathematical constant with the value 6.28318530717958647693.
   * It is twice the ratio of the circumference of a circle to its diameter.
   * It is useful in combination with the trigonometric functions <b>sin()</b> and <b>cos()</b>.
   * 
   * @see #PI
   * @see #HALF_PI
   * @see #QUARTER_PI
   */
  static final float TWO_PI     = PI * 2.0f;  
  
  //modifier keys
  static public final int NOMODIFIER_MASK    = 0;
  static public final int SHIFT              = 1 << 0;
  static public final int CTRL               = 1 << 1;
  static public final int META               = 1 << 2;
  static public final int ALT                = 1 << 3;
  static public final int ALT_GRAPH          = 1 << 4;
  
  static public final int SHIFT_DOWN         = 64;
  static public final int CTRL_DOWN          = 128;
  static public final int META_DOWN          = 256;
  static public final int ALT_DOWN           = 512;
  static public final int ALT_GRAPH_DOWN     = 8192;
  
  static final int CENTER = 3;
  
  //Arrows and Buttons
  static final int NOBUTTON	= 0;
  
  static final int LEFT  = 37;
  static final int UP    = 38;
  static final int RIGHT = 39;
  static final int DOWN  = 40; 
  
  //Actions
  public enum DLAction {
  	//NO_ACTION("No action", true, 0),
  	
    //selection action to subscribe to
  	SELECT_0("Select-0 action", true, 0),
  	SELECT_1("Select-1 action", true, 1),
  	SELECT_2("Select-2 action", true, 2),
  	SELECT_3("Select-3 action", true, 3),
  	SELECT_6("Select-6 action", true, 6),
  	
  	//deselection action to subscribe to
  	DESELECT_0("Deselect-0 action", true, 0),
  	DESELECT_1("Deselect-1 action", true, 1),
  	DESELECT_2("Deselect-2 action", true, 2),
  	DESELECT_3("Deselect-3 action", true, 3),
  	DESELECT_6("Deselect-6 action", true, 6),
  	
  	//KEYfRAMES
  	ADD_KEYFRAME_TO_PATH("Add keyframe to path", true, 0),
  	PLAY_PATH("Play keyframe path", true, 0),
  	DELETE_PATH("Delete keyframepath", true, 0),
  	
  	// CLICk ACTIONs	  	
  	ZOOM_TO_FIT("Zoom to fit the scene", true, 0),
  	//ARP_FROM_PIXEL("Set the arcball reference point from the pixel under the mouse"),
  	//RESET_ARP("Reset the arcball reference point to the 3d frame world origin"),
  	CENTER_FRAME("Center frame", true, 0),
  	CENTER_SCENE("Center scene", true, 0),
  	//SHOW_ALL("Show the whole scene"),
  	ALIGN_FRAME("Align interactive frame (if any) with world", true, 0),
  	ALIGN_CAMERA("Align camera with world", true, 0),
  	
    //Click actions require cursor pos:
    ZOOM_ON_PIXEL("Zoom on pixel", true, 0),
    INTERPOLATE_TO_ZOOM_ON_PIXEL("Interpolate the camera to zoom on pixel", true, 0),
    ARP_FROM_PIXEL("Set the arcball reference point from the pixel under the mouse", true, 0),
  	
  	//GENERAL KEYBOARD ACTIONs	
  	DRAW_AXIS("Toggles the display of the world axis", true, 0),
  	DRAW_GRID("Toggles the display of the XY grid", true, 0),
  	CAMERA_PROFILE("Cycles to the registered camera profiles", true, 0),
  	CAMERA_TYPE("Toggles camera type (orthographic or perspective)", false, 0),
  	CAMERA_KIND("Toggles camera kind (proscene or standard)", false, 0),
  	ANIMATION("Toggles animation", true, 0),  	
  	RESET_ARP("Reset the arcball reference point to the 3d frame world origin", true, 0),
  	GLOBAL_HELP("Displays the global help", true, 0),
  	CURRENT_CAMERA_PROFILE_HELP("Displays the current camera profile help", true, 0),
  	EDIT_CAMERA_PATH("Toggles the key frame camera paths (if any) for edition", true, 0),
  	FOCUS_INTERACTIVE_FRAME("Toggle interactivity between camera and interactive frame (if any)", true, 0),
  	DRAW_FRAME_SELECTION_HINT("Toggle interactive frame selection region drawing", true, 0),
  	CONSTRAIN_FRAME("Toggles on and off frame constraints (if any)", true, 0),
  	INTERPOLATE_TO_FIT_SCENE("Interpolate the camera to fit the whole scene", true, 0),
  	SHOW_ALL("Show the whole scene", true, 0),
  	
    //CAMERA KEYBOARD ACTIONs  // TODO all of these could be dof_1
  	MOVE_CAMERA_LEFT("Move camera to the left", true, 0),
  	MOVE_CAMERA_RIGHT("Move camera to the right", true, 0),
  	MOVE_CAMERA_UP("Move camera up", true, 0),
  	MOVE_CAMERA_DOWN("Move camera down", true, 0),
  	INCREASE_ROTATION_SENSITIVITY("Increase camera rotation sensitivity (only meaningful in arcball mode)", true, 0),
  	DECREASE_ROTATION_SENSITIVITY("Decrease camera rotation sensitivity (only meaningful in arcball mode)", true, 0),
  	INCREASE_CAMERA_FLY_SPEED("Increase camera fly speed (only meaningful in first-person mode)", false, 0),
  	DECREASE_CAMERA_FLY_SPEED("Decrease camera fly speed (only meaningful in first-person mode)", false, 0),
  	INCREASE_AVATAR_FLY_SPEED("Increase avatar fly speed (only meaningful in third-person mode)", false, 0),
  	DECREASE_AVATAR_FLY_SPEED("Decrease avatar fly speed (only meaningful in third-person mode)", false, 0),
  	INCREASE_AZYMUTH("Increase camera azymuth respect to the avatar (only meaningful in third-person mode)", false, 0),
  	DECREASE_AZYMUTH("Decrease camera azymuth respect to the avatar (only meaningful in third-person mode)", false, 0),
  	INCREASE_INCLINATION("Increase camera inclination respect to the avatar (only meaningful in third-person mode)", false, 0),
  	DECREASE_INCLINATION("Decrease camera inclination respect to the avatar (only meaningful in third-person mode)", false, 0),
  	INCREASE_TRACKING_DISTANCE("Increase camera tracking distance respect to the avatar (only meaningful in third-person mode)", false, 0),
  	DECREASE_TRACKING_DISTANCE("Decrease camera tracking distance respect to the avatar (only meaningful in third-person mode)", false, 0),
  	
    // Typically wheel
  	ZOOM("Zoom", true, 1),
    
  	// DEVICE ACTIONs
  	//NO_DEVICE_ACTION("No device action", true, 2),
  	ROTATE("Rotate frame (camera or interactive frame)", true, 2),
  	CAD_ROTATE("Rotate (only) camera frame as in CAD applications)", false, 2),
  	//ZOOM("Zoom", true, 1),
  	TRANSLATE("Translate frame (camera or interactive frame)", true, 2),
  	MOVE_FORWARD("Move forward frame (camera or interactive frame)", false, 2),
  	MOVE_BACKWARD("move backward frame (camera or interactive frame)", false, 2),
  	LOOK_AROUND("Look around with frame (camera or interactive drivable frame)", false, 2),
  	SCREEN_ROTATE("Screen rotate (camera or interactive frame)", true, 2),
  	ROLL("Roll frame (camera or interactive drivable frame)", true, 2),
  	DRIVE("Drive (camera or interactive drivable frame)", false, 2),
  	SCREEN_TRANSLATE("Screen translate frame (camera or interactive frame)", true, 2),
  	ZOOM_ON_REGION("Zoom on region (camera or interactive drivable frame)", true, 2),
  	
  	TRANSLATE3("Translate frame (camera or interactive frame) from dx, dy, dz simultaneously", false, 3),	
  	FROM_EULER_ANGLES("Rotate frame (camera or interactive frame) from Euler angles", false, 3),	
  	
  	GOOGLE_EARTH("Google earth emulation", false, 6),	
  	NATURAL("Natural (camera or interactive frame)", false, 6),
  	
  	//CUSTOM ACTIONs  	
  	CUSTOM1("User defined action no 1"),
  	CUSTOM2("User defined action no 2"),
  	CUSTOM3("User defined action no 3"),
  	CUSTOM4("User defined action no 4");
  	
  	String description;
  	boolean twoD;
  	int dofs;
  	
  	DLAction(String description, boolean td, int ds) {
       this.description = description;
       this.twoD = td;
       this.dofs = ds;
    }
  	
  	DLAction(String description, int ds) {
      this.description = description;
      this.twoD = true;
      this.dofs = ds;
    }
  	
  	DLAction(String description, boolean td) {
      this.description = description;
      this.twoD = td;
      this.dofs = 2;
    }
     	
  	DLAction(String description) {
      this.description = description;
      this.twoD = true;
      this.dofs = 0;
    }
    
  	//TODO These methods should belong to the interface!
    public String description() {
      return description;
    }
    
    public boolean is2D() {
    	return twoD;
    }
    
    public int dofs() {
    	return dofs;
    }
    
    /**
    public DOF_6Action dof6Action() {
    	DOF_6Action a6;
    	switch (this) {
  			case CAD_ROTATE:
  				a6 = DOF_6Action.CAD_ROTATE;
  				break;
  			case CUSTOM1:
  				a6 = DOF_6Action.CUSTOM1;
  				break;
  			case CUSTOM2:
  				a6 = DOF_6Action.CUSTOM2;
  				break;
  			case CUSTOM3:
  				a6 = DOF_6Action.CUSTOM3;
  				break;
  			case CUSTOM4:
  				a6 = DOF_6Action.CUSTOM4;
  				break;
  			case DRIVE:
  				a6 = DOF_6Action.DRIVE;
  				break;
  			case FROM_EULER_ANGLES:
  				a6 = DOF_6Action.FROM_EULER_ANGLES;
  				break;
  			case GOOGLE_EARTH:
  				a6 = DOF_6Action.GOOGLE_EARTH;
  				break;
  			case LOOK_AROUND:
  				a6 = DOF_6Action.LOOK_AROUND;
  				break;
  			case MOVE_BACKWARD:
  				a6 = DOF_6Action.MOVE_BACKWARD;
  				break;
  			case MOVE_FORWARD:
  				a6 = DOF_6Action.MOVE_FORWARD;
  				break;
  			case NATURAL:
  				a6 = DOF_6Action.NATURAL;
  				break;  			
  			case ROLL:
  				a6 = DOF_6Action.ROLL;
  				break;
  			case ROTATE:
  				a6 = DOF_6Action.ROTATE;
  				break;
  			case SCREEN_ROTATE:
  				a6 = DOF_6Action.SCREEN_ROTATE;
  				break;
  			case SCREEN_TRANSLATE:
  				a6 = DOF_6Action.SCREEN_TRANSLATE;
  				break;
  			case TRANSLATE:
  				a6 = DOF_6Action.TRANSLATE;
  				break;
  			case TRANSLATE3:
  				a6 = DOF_6Action.TRANSLATE3;
  				break;
  			case ZOOM:
  				a6 = DOF_6Action.ZOOM;
  				break;
  			case ZOOM_ON_REGION:
  				a6 = DOF_6Action.ZOOM_ON_REGION;
  				break;
  			default:
  				a6 = DOF_6Action.NO_ACTION;
  				break;    	
      	}
    		return a6;
    	}
    	*/   
  }
  
  public enum DOF_0Action implements Actionable<DLAction> {
    //DOF_0
  	//NO_ACTION(DLAction.NO_ACTION),
  	
  	SELECT(DLAction.SELECT_0),  	
  	DESELECT(DLAction.DESELECT_0),
  	
    //KEYfRAMES
  	ADD_KEYFRAME_TO_PATH(DLAction.ADD_KEYFRAME_TO_PATH),
  	PLAY_PATH(DLAction.PLAY_PATH),
  	DELETE_PATH(DLAction.DELETE_PATH),
  	
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
  	
  	@Override
  	public int dofs() {
  		return act.dofs();
  	}
  	
  	/**
  	@Override
		public boolean selectionMode() {
  		if( this == ADD_KEYFRAME_TO_PATH )
  			return true;
			return false;
		}
		*/
  	
  	/**
  	@Override
		public DLAction defaultAction() {
			return DLAction.NO_ACTION;
		}
		// */
  	
    // /**
   	@Override
 		public DLAction selectionAction() {
 			return DLAction.SELECT_0;
 		}
 		// */
   	
   	@Override
 		public DLAction deselectionAction() {
 			return DLAction.DESELECT_0;
 		}

  	DLAction act;

  	DOF_0Action(DLAction a) {
  		act = a;
  	}
  }
  
  public enum DOF_1Action implements Actionable<DLAction> {
    //DOF_0
  	//NO_ACTION(DLAction.NO_ACTION),
  	
  	SELECT(DLAction.SELECT_1),
  	DESELECT(DLAction.DESELECT_1),
  	
  	ZOOM(DLAction.ZOOM),
  	
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
  	
  	@Override
  	public int dofs() {
  		return act.dofs();
  	}
  	
  	/**
  	@Override
		public DLAction defaultAction() {
			return DLAction.NO_ACTION;
		}
		*/
  	
  	@Override
 		public DLAction selectionAction() {
 			return DLAction.SELECT_1;
 		}
  	
  	@Override
 		public DLAction deselectionAction() {
 			return DLAction.DESELECT_1;
 		}

  	DLAction act;

  	DOF_1Action(DLAction a) {
  		act = a;
  	}
  }
  
  public enum DOF_2Action implements Actionable<DLAction> {
  	//NO_ACTION(DLAction.NO_ACTION),
  	
  	SELECT(DLAction.SELECT_2),
  	DESELECT(DLAction.DESELECT_2),
  	
    //DOF_1
  	ZOOM(DLAction.ZOOM),
    
  	//DOF_2
  	ROTATE(DLAction.ROTATE),
  	CAD_ROTATE(DLAction.CAD_ROTATE),
  	TRANSLATE(DLAction.TRANSLATE),
  	MOVE_FORWARD(DLAction.MOVE_FORWARD),
  	MOVE_BACKWARD(DLAction.MOVE_BACKWARD),
  	LOOK_AROUND(DLAction.LOOK_AROUND),
  	SCREEN_ROTATE(DLAction.SCREEN_ROTATE),
  	ROLL(DLAction.ROLL),
  	DRIVE(DLAction.DRIVE),
  	SCREEN_TRANSLATE(DLAction.SCREEN_TRANSLATE),
  	ZOOM_ON_REGION(DLAction.ZOOM_ON_REGION),
  	
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
  	
  	@Override
  	public int dofs() {
  		return act.dofs();
  	}
  	
  	/**
  	@Override
		public DLAction defaultAction() {
			return DLAction.NO_ACTION;
		}
		*/
  	
  	@Override
 		public DLAction selectionAction() {
 			return DLAction.SELECT_2;
 		}
  	
  	@Override
 		public DLAction deselectionAction() {
 			return DLAction.DESELECT_2;
 		}

  	DLAction act;

  	DOF_2Action(DLAction a) {
  		act = a;
  	}
  }
  
  public enum DOF_3Action implements Actionable<DLAction> {
    //NO_ACTION(DLAction.NO_ACTION),
  	
  	SELECT(DLAction.SELECT_3),
  	DESELECT(DLAction.DESELECT_3),
  	
    //DOF_1
  	ZOOM(DLAction.ZOOM),
    
  	//DOF_2
  	ROTATE(DLAction.ROTATE),
  	CAD_ROTATE(DLAction.CAD_ROTATE),
  	TRANSLATE(DLAction.TRANSLATE),
  	MOVE_FORWARD(DLAction.MOVE_FORWARD),
  	MOVE_BACKWARD(DLAction.MOVE_BACKWARD),
  	LOOK_AROUND(DLAction.LOOK_AROUND),
  	SCREEN_ROTATE(DLAction.SCREEN_ROTATE),
  	ROLL(DLAction.ROLL),
  	DRIVE(DLAction.DRIVE),
  	SCREEN_TRANSLATE(DLAction.SCREEN_TRANSLATE),
  	ZOOM_ON_REGION(DLAction.ZOOM_ON_REGION),
  	
    //DOF_3
  	TRANSLATE3(DLAction.TRANSLATE3),	
  	FROM_EULER_ANGLES(DLAction.TRANSLATE3),
  	
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
  	
  	@Override
  	public int dofs() {
  		return act.dofs();
  	}
  	
  	/**
  	@Override
		public DLAction defaultAction() {
			return DLAction.NO_ACTION;
		}
		*/
  	
  	@Override
 		public DLAction selectionAction() {
 			return DLAction.SELECT_3;
 		}
  	
  	@Override
 		public DLAction deselectionAction() {
 			return DLAction.DESELECT_3;
 		}

  	DLAction act;

  	DOF_3Action(DLAction a) {
  		act = a;
  	}
  }
  
  public enum DOF_6Action implements Actionable<DLAction> {
    //NO_ACTION(DLAction.NO_ACTION),
  	
  	SELECT(DLAction.SELECT_6),
  	DESELECT(DLAction.DESELECT_6),
  	
    //DOF_1
  	ZOOM(DLAction.ZOOM),
    
  	//DOF_2
  	ROTATE(DLAction.ROTATE),
  	CAD_ROTATE(DLAction.CAD_ROTATE),
  	TRANSLATE(DLAction.TRANSLATE),
  	MOVE_FORWARD(DLAction.MOVE_FORWARD),
  	MOVE_BACKWARD(DLAction.MOVE_BACKWARD),
  	LOOK_AROUND(DLAction.LOOK_AROUND),
  	SCREEN_ROTATE(DLAction.SCREEN_ROTATE),
  	ROLL(DLAction.ROLL),
  	DRIVE(DLAction.DRIVE),
  	SCREEN_TRANSLATE(DLAction.SCREEN_TRANSLATE),
  	ZOOM_ON_REGION(DLAction.ZOOM_ON_REGION),
  	
    //DOF_3
  	TRANSLATE3(DLAction.TRANSLATE3),	
  	FROM_EULER_ANGLES(DLAction.TRANSLATE3),
  	
    //DOF_6
  	GOOGLE_EARTH(DLAction.GOOGLE_EARTH),	
  	NATURAL(DLAction.NATURAL),
  	
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
  	
  	@Override
  	public int dofs() {
  		return act.dofs();
  	}
  	
  	/**
  	@Override
		public DLAction defaultAction() {
			return DLAction.NO_ACTION;
		}
		*/
  	
  	@Override
 		public DLAction selectionAction() {
 			return DLAction.SELECT_6;
 		}
  	
  	@Override
 		public DLAction deselectionAction() {
 			return DLAction.DESELECT_6;
 		}

  	DLAction act;

  	DOF_6Action(DLAction a) {
  		act = a;
  	}  	
  }
}
