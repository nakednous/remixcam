/**
 *                     RemixCam (version 1.0.0)      
 *      Copyright (c) 2012 by National University of Colombia
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
  static public final int SHIFT     = 1 << 0;
  static public final int CTRL      = 1 << 1;
  static public final int META      = 1 << 2;
  static public final int ALT       = 1 << 3;
  static public final int ALT_GRAPH = 1 << 4;
  
  static final int CENTER = 3;
  
  //Arrows
  static final int LEFT  = 37;
  static final int UP    = 38;
  static final int RIGHT = 39;
  static final int DOWN  = 40; 
  
  //Actions
  public enum DLAction {
  	NO_ACTION("No action", true, 0),
  	
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
    
    public String description() {
      return description;
    }
    
    public boolean is2D() {
    	return twoD;
    }
    
    public int dofs() {
    	return dofs;
    }
  }
}
