/**
 *                     Dandelion (version 0.70.0)      
 *          Copyright (c) 2013 by Jean Pierre Charalambos
 *                 @author Jean Pierre Charalambos      
 *              https://github.com/nakednous/remixcam
 *                           
 * This library provides classes to ease the creation of interactive
 * frame-based, 2d and 3d scenes.
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

package remixlab.dandelion.core;

import remixlab.tersehandling.core.EventConstants;
import remixlab.tersehandling.generic.profile.Actionable;

public interface Constants extends EventConstants {	
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
  
  //Actions
  public enum DandelionAction {
  	//KEYfRAMES
  	ADD_KEYFRAME_TO_PATH("Add keyframe to path", true, 0),
  	PLAY_PATH("Play keyframe path", true, 0),
  	DELETE_PATH("Delete keyframepath", true, 0),
  	
  	// CLICk ACTIONs	  	  	
  	CENTER_FRAME("Center frame", true, 0),
  	ALIGN_FRAME("Align frame with world", true, 0),
  	
    //Click actions require cursor pos:
    ZOOM_ON_PIXEL("Interpolate the camera to zoom on pixel", true, 0),
    ARP_FROM_PIXEL("Set the arcball reference point from the pixel under the mouse", true, 0),
  	
  	//GENERAL KEYBOARD ACTIONs	
  	DRAW_AXIS("Toggles the display of the world axis", true, 0),
  	DRAW_GRID("Toggles the display of the XY grid", true, 0),
  	//CAMERA_PROFILE("Cycles to the registered camera profiles", true, 0),
  	CAMERA_TYPE("Toggles camera type (orthographic or perspective)", false, 0),
  	CAMERA_KIND("Toggles camera kind (proscene or standard)", false, 0),
  	ANIMATION("Toggles animation", true, 0),
  	INTERPOLATE_TO_FIT("Zoom to fit the scene", true, 0),
  	RESET_ARP("Reset the arcball reference point to the 3d frame world origin", true, 0),
  	GLOBAL_HELP("Displays the global help", true, 0),
  	//CURRENT_CAMERA_PROFILE_HELP("Displays the current camera profile help", true, 0),
  	EDIT_CAMERA_PATH("Toggles the key frame camera paths (if any) for edition", true, 0),
  	//FOCUS_INTERACTIVE_FRAME("Toggle interactivity between camera and interactive frame (if any)", true, 0),
  	DRAW_FRAME_SELECTION_HINT("Toggle interactive frame selection region drawing", true, 0),
  	//CONSTRAIN_FRAME("Toggles on and off frame constraints (if any)", true, 0),
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
  	//CAD_ROTATE("Rotate (only) camera frame as in CAD applications)", false, 2),
  	//ZOOM("Zoom", true, 1),
  	TRANSLATE("Translate frame (camera or interactive frame)", true, 2),
  	MOVE_FORWARD("Move forward frame (camera or interactive frame)", false, 2),
  	MOVE_BACKWARD("move backward frame (camera or interactive frame)", false, 2),
  	LOOK_AROUND("Look around with frame (camera or interactive drivable frame)", false, 2),
  	ROLL("Roll frame (camera or interactive drivable frame)", true, 2),
  	DRIVE("Drive (camera or interactive drivable frame)", false, 2),
  	SCREEN_ROTATE("Screen rotate (camera or interactive frame)", true, 2),
  	SCREEN_TRANSLATE("Screen translate frame (camera or interactive frame)", true, 2),
  	ZOOM_ON_REGION("Zoom on region (camera or interactive drivable frame)", true, 2),
  	 	
  	TRANSLATE3("Translate frame (camera or interactive frame) from dx, dy, dz simultaneously", false, 3),	
  	ROTATE3("Rotate frame (camera or interactive frame) from Euler angles", false, 3),
  	
  	//GOOGLE_EARTH("Google earth emulation", false, 6),	
  	TRANSLATE_ROTATE("Natural (camera or interactive frame)", false, 6),
  	
  	//CUSTOM ACTIONs  	
  	CUSTOM("User defined action");
  	
  	String description;
  	boolean twoD;
  	int dofs;
  	
  	DandelionAction(String description, boolean td, int ds) {
       this.description = description;
       this.twoD = td;
       this.dofs = ds;
    }
  	
  	DandelionAction(String description, int ds) {
      this.description = description;
      this.twoD = true;
      this.dofs = ds;
    }
  	
  	DandelionAction(String description, boolean td) {
      this.description = description;
      this.twoD = td;
      this.dofs = 2;
    }
     	
  	DandelionAction(String description) {
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
  }
  
  public enum DOF0Action implements Actionable<DandelionAction> {
    //DOF_0  	
    //KEYfRAMES
  	ADD_KEYFRAME_TO_PATH(DandelionAction.ADD_KEYFRAME_TO_PATH),
  	PLAY_PATH(DandelionAction.PLAY_PATH),
  	DELETE_PATH(DandelionAction.DELETE_PATH),
  	
  	// CLICk ACTIONs	  	
  	INTERPOLATE_TO_FIT(DandelionAction.INTERPOLATE_TO_FIT),
  	
  	//GENERAL KEYBOARD ACTIONs	
  	DRAW_AXIS(DandelionAction.DRAW_AXIS),
  	DRAW_GRID(DandelionAction.DRAW_GRID),
  	//CAMERA_PROFILE(DandelionAction.CAMERA_PROFILE),
  	CAMERA_TYPE(DandelionAction.CAMERA_TYPE),
  	CAMERA_KIND(DandelionAction.CAMERA_KIND),
  	ANIMATION(DandelionAction.ANIMATION),  	
  	RESET_ARP(DandelionAction.RESET_ARP),
  	GLOBAL_HELP(DandelionAction.GLOBAL_HELP),
  	//CURRENT_CAMERA_PROFILE_HELP(DandelionAction.CURRENT_CAMERA_PROFILE_HELP),
  	EDIT_CAMERA_PATH(DandelionAction.EDIT_CAMERA_PATH),
  	//FOCUS_INTERACTIVE_FRAME(DandelionAction.FOCUS_INTERACTIVE_FRAME),
  	DRAW_FRAME_SELECTION_HINT(DandelionAction.DRAW_FRAME_SELECTION_HINT),
  	//CONSTRAIN_FRAME(DandelionAction.CONSTRAIN_FRAME),
  	SHOW_ALL(DandelionAction.SHOW_ALL),
  	
    //CAMERA KEYBOARD ACTIONs
  	MOVE_CAMERA_LEFT(DandelionAction.MOVE_CAMERA_LEFT),
  	MOVE_CAMERA_RIGHT(DandelionAction.MOVE_CAMERA_RIGHT),
  	MOVE_CAMERA_UP(DandelionAction.MOVE_CAMERA_UP),
  	MOVE_CAMERA_DOWN(DandelionAction.MOVE_CAMERA_DOWN),
  	INCREASE_ROTATION_SENSITIVITY(DandelionAction.INCREASE_ROTATION_SENSITIVITY),
  	DECREASE_ROTATION_SENSITIVITY(DandelionAction.DECREASE_ROTATION_SENSITIVITY),
  	INCREASE_CAMERA_FLY_SPEED(DandelionAction.INCREASE_CAMERA_FLY_SPEED),
  	DECREASE_CAMERA_FLY_SPEED(DandelionAction.DECREASE_CAMERA_FLY_SPEED),
  	INCREASE_AVATAR_FLY_SPEED(DandelionAction.INCREASE_AVATAR_FLY_SPEED),
  	DECREASE_AVATAR_FLY_SPEED(DandelionAction.DECREASE_AVATAR_FLY_SPEED),
  	INCREASE_AZYMUTH(DandelionAction.INCREASE_AZYMUTH),
  	DECREASE_AZYMUTH(DandelionAction.DECREASE_AZYMUTH),
  	INCREASE_INCLINATION(DandelionAction.INCREASE_INCLINATION),
  	DECREASE_INCLINATION(DandelionAction.DECREASE_INCLINATION),
  	INCREASE_TRACKING_DISTANCE(DandelionAction.INCREASE_TRACKING_DISTANCE),
  	DECREASE_TRACKING_DISTANCE(DandelionAction.DECREASE_TRACKING_DISTANCE),
  	
  	CUSTOM(DandelionAction.CUSTOM);

  	@Override
  	public DandelionAction referenceAction() {
  		return act;
  	}

  	@Override
  	public String description() {
  		return this.referenceAction().description();
  	}
  	
  	@Override
  	public int dofs() {
  		return act.dofs();
  	}
  	
  	public boolean is2D() {
  		return act.is2D();
  	}

  	DandelionAction act;

  	DOF0Action(DandelionAction a) {
  		act = a;
  	}
  }
  
  public enum DOF1Action implements Actionable<DandelionAction> {  	
    //DOF_1  	
  	ZOOM(DandelionAction.ZOOM),
  	
  	CUSTOM(DandelionAction.CUSTOM);

  	@Override
  	public DandelionAction referenceAction() {
  		return act;
  	}

  	@Override
  	public String description() {
  		return this.referenceAction().description();
  	}
  	
  	@Override
  	public int dofs() {
  		return act.dofs();
  	}
  	
  	public boolean is2D() {
  		return act.is2D();
  	}
  	
  	DandelionAction act;

  	DOF1Action(DandelionAction a) {
  		act = a;
  	}
  }
  
  public enum DOF2Action implements Actionable<DandelionAction> {
  	//DOF_1
  	ZOOM(DandelionAction.ZOOM),
    
  	//DOF_2
  	ROTATE(DandelionAction.ROTATE),
  	TRANSLATE(DandelionAction.TRANSLATE),
  	MOVE_FORWARD(DandelionAction.MOVE_FORWARD),
  	MOVE_BACKWARD(DandelionAction.MOVE_BACKWARD),
  	LOOK_AROUND(DandelionAction.LOOK_AROUND),
  	SCREEN_ROTATE(DandelionAction.SCREEN_ROTATE),
  	ROLL(DandelionAction.ROLL),
  	DRIVE(DandelionAction.DRIVE),
  	SCREEN_TRANSLATE(DandelionAction.SCREEN_TRANSLATE),
  	ZOOM_ON_REGION(DandelionAction.ZOOM_ON_REGION),
  	
  	CUSTOM(DandelionAction.CUSTOM);

  	@Override
  	public DandelionAction referenceAction() {
  		return act;
  	}

  	@Override
  	public String description() {
  		return this.referenceAction().description();
  	}
  	
  	@Override
  	public int dofs() {
  		return act.dofs();
  	}
  	
  	public boolean is2D() {
  		return act.is2D();
  	}

  	DandelionAction act;

  	DOF2Action(DandelionAction a) {
  		act = a;
  	}
  }
  
  public enum DOF2ClickAction implements Actionable<DandelionAction> {
    //DOF_0
  	
  	// CLICk ACTIONs	  	
  	INTERPOLATE_TO_FIT(DandelionAction.INTERPOLATE_TO_FIT),
   	CENTER_FRAME(DandelionAction.CENTER_FRAME),
  	ALIGN_FRAME(DandelionAction.ALIGN_FRAME),
  	
    //Click actions require cursor pos:
    ZOOM_ON_PIXEL(DandelionAction.ZOOM_ON_PIXEL),
    ARP_FROM_PIXEL(DandelionAction.ARP_FROM_PIXEL),
  	
  	//GENERAL KEYBOARD ACTIONs	
  	DRAW_AXIS(DandelionAction.DRAW_AXIS),
  	DRAW_GRID(DandelionAction.DRAW_GRID),
  	//CAMERA_PROFILE(DandelionAction.CAMERA_PROFILE),
  	CAMERA_TYPE(DandelionAction.CAMERA_TYPE),
  	CAMERA_KIND(DandelionAction.CAMERA_KIND),
  	ANIMATION(DandelionAction.ANIMATION),  	
  	RESET_ARP(DandelionAction.RESET_ARP),
  	GLOBAL_HELP(DandelionAction.GLOBAL_HELP),
  	//CURRENT_CAMERA_PROFILE_HELP(DandelionAction.CURRENT_CAMERA_PROFILE_HELP),
  	EDIT_CAMERA_PATH(DandelionAction.EDIT_CAMERA_PATH),
  	//FOCUS_INTERACTIVE_FRAME(DandelionAction.FOCUS_INTERACTIVE_FRAME),
  	DRAW_FRAME_SELECTION_HINT(DandelionAction.DRAW_FRAME_SELECTION_HINT),
  	//CONSTRAIN_FRAME(DandelionAction.CONSTRAIN_FRAME),
  	SHOW_ALL(DandelionAction.SHOW_ALL),
  	
    //CAMERA KEYBOARD ACTIONs
  	MOVE_CAMERA_LEFT(DandelionAction.MOVE_CAMERA_LEFT),
  	MOVE_CAMERA_RIGHT(DandelionAction.MOVE_CAMERA_RIGHT),
  	MOVE_CAMERA_UP(DandelionAction.MOVE_CAMERA_UP),
  	MOVE_CAMERA_DOWN(DandelionAction.MOVE_CAMERA_DOWN),
  	INCREASE_ROTATION_SENSITIVITY(DandelionAction.INCREASE_ROTATION_SENSITIVITY),
  	DECREASE_ROTATION_SENSITIVITY(DandelionAction.DECREASE_ROTATION_SENSITIVITY),
  	INCREASE_CAMERA_FLY_SPEED(DandelionAction.INCREASE_CAMERA_FLY_SPEED),
  	DECREASE_CAMERA_FLY_SPEED(DandelionAction.DECREASE_CAMERA_FLY_SPEED),
  	INCREASE_AVATAR_FLY_SPEED(DandelionAction.INCREASE_AVATAR_FLY_SPEED),
  	DECREASE_AVATAR_FLY_SPEED(DandelionAction.DECREASE_AVATAR_FLY_SPEED),
  	INCREASE_AZYMUTH(DandelionAction.INCREASE_AZYMUTH),
  	DECREASE_AZYMUTH(DandelionAction.DECREASE_AZYMUTH),
  	INCREASE_INCLINATION(DandelionAction.INCREASE_INCLINATION),
  	DECREASE_INCLINATION(DandelionAction.DECREASE_INCLINATION),
  	INCREASE_TRACKING_DISTANCE(DandelionAction.INCREASE_TRACKING_DISTANCE),
  	DECREASE_TRACKING_DISTANCE(DandelionAction.DECREASE_TRACKING_DISTANCE),
  	
  	CUSTOM(DandelionAction.CUSTOM);

  	@Override
  	public DandelionAction referenceAction() {
  		return act;
  	}

  	@Override
  	public String description() {
  		return this.referenceAction().description();
  	}
  	
  	@Override
  	public int dofs() {
  		return act.dofs();
  	}
  	
  	public boolean is2D() {
  		return act.is2D();
  	}

  	DandelionAction act;

  	DOF2ClickAction(DandelionAction a) {
  		act = a;
  	}
  }
  
  public enum DOF3Action implements Actionable<DandelionAction> {
  	//DOF_1
  	ZOOM(DandelionAction.ZOOM),
    
  	//DOF_2
  	ROTATE(DandelionAction.ROTATE),
  	TRANSLATE(DandelionAction.TRANSLATE),
  	MOVE_FORWARD(DandelionAction.MOVE_FORWARD),
  	MOVE_BACKWARD(DandelionAction.MOVE_BACKWARD),
  	LOOK_AROUND(DandelionAction.LOOK_AROUND),
  	SCREEN_ROTATE(DandelionAction.SCREEN_ROTATE),
  	ROLL(DandelionAction.ROLL),
  	DRIVE(DandelionAction.DRIVE),
  	SCREEN_TRANSLATE(DandelionAction.SCREEN_TRANSLATE),
  	ZOOM_ON_REGION(DandelionAction.ZOOM_ON_REGION),
  	
    //DOF_3
  	TRANSLATE3(DandelionAction.TRANSLATE3),	
  	ROTATE3(DandelionAction.ROTATE3),
  	
  	CUSTOM(DandelionAction.CUSTOM);

  	@Override
  	public DandelionAction referenceAction() {
  		return act;
  	}

  	@Override
  	public String description() {
  		return this.referenceAction().description();
  	}
  	
  	@Override
  	public int dofs() {
  		return act.dofs();
  	}
  	
  	public boolean is2D() {
  		return act.is2D();
  	}
  
  	DandelionAction act;

  	DOF3Action(DandelionAction a) {
  		act = a;
  	}
  }
  
  public enum DOF6Action implements Actionable<DandelionAction> {
    //NO_ACTION(DLAction.NO_ACTION),
  	
    //DOF_1
  	ZOOM(DandelionAction.ZOOM),
    
  	//DOF_2
  	ROTATE(DandelionAction.ROTATE),
  	TRANSLATE(DandelionAction.TRANSLATE),
  	MOVE_FORWARD(DandelionAction.MOVE_FORWARD),
  	MOVE_BACKWARD(DandelionAction.MOVE_BACKWARD),
  	LOOK_AROUND(DandelionAction.LOOK_AROUND),
  	SCREEN_ROTATE(DandelionAction.SCREEN_ROTATE),
  	ROLL(DandelionAction.ROLL),
  	DRIVE(DandelionAction.DRIVE),
  	SCREEN_TRANSLATE(DandelionAction.SCREEN_TRANSLATE),
  	ZOOM_ON_REGION(DandelionAction.ZOOM_ON_REGION),
  	
    //DOF_3
  	TRANSLATE3(DandelionAction.TRANSLATE3),	
  	ROTATE3(DandelionAction.ROTATE3),
  	
    //DOF_6
  	TRANSLATE_ROTATE(DandelionAction.TRANSLATE_ROTATE),
  	
  	CUSTOM(DandelionAction.CUSTOM);

  	@Override
  	public DandelionAction referenceAction() {
  		return act;
  	}

  	@Override
  	public String description() {
  		return this.referenceAction().description();
  	}
  	
  	@Override
  	public int dofs() {
  		return act.dofs();
  	}
  	
  	public boolean is2D() {
  		return act.is2D();
  	}
  
  	DandelionAction act;

  	DOF6Action(DandelionAction a) {
  		act = a;
  	}  	
  }
}
