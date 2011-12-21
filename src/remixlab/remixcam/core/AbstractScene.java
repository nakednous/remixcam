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

import java.util.ArrayList;
import java.util.List;

import remixlab.remixcam.devices.*;
import remixlab.remixcam.geom.*;
import remixlab.remixcam.util.*;

public abstract class AbstractScene {
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

	/**
	 * Constants associated to the different mouse buttons which follow java conventions.
	 */
	public enum Button {
		// values correspond to: BUTTON1_DOWN_MASK, BUTTON2_DOWN_MASK and BUTTON3_DOWN_MASK
		// see: http://download-llnw.oracle.com/javase/6/docs/api/constant-values.html
		LEFT(1024), MIDDLE(2048), RIGHT(4096);
		public final int ID;
    Button(int code) {
    	this.ID = code;
    }    
    //The following code works but is considered overkill :)
    /**
    public int id() { return ID; }
    private static final Map<Integer,Button> lookup = new HashMap<Integer,Button>();
    static {
    	for(Button s : EnumSet.allOf(Button.class))
         lookup.put(s.id(), s);
    }
    public static Button get(int code) { 
      return lookup.get(code);
    }
    // */
	}

	/**
	 * Constants associated to the different arrow keys. Taken from Processing constants 
	 * (which follows java conventions). 
	 */	
	public enum Arrow {
		UP(38), DOWN(40), LEFT(37), RIGHT(39);
		public final int ID;
    Arrow(int code) {
    	this.ID = code;
    }
    //The following code works but is considered overkill :)
    /**
    public int id() { return ID; }
    private static final Map<Integer,Arrow> lookup = new HashMap<Integer,Arrow>();
    static {
    	for(Arrow s : EnumSet.allOf(Arrow.class))
         lookup.put(s.id(), s);
    }
    public static Arrow get(int code) { 
      return lookup.get(code);
    }
    // */
	}

	/**
	 * Constants associated to the different modifier keys which follow java conventions.
	 */
	public enum Modifier {
		// values correspond to: ALT_DOWN_MASK, SHIFT_DOWN_MASK, CTRL_DOWN_MASK, META_DOWN_MASK, ALT_GRAPH_DOWN_MASK
		// see: http://download-llnw.oracle.com/javase/6/docs/api/constant-values.html
		ALT(512), SHIFT(64), CTRL(128), META(256), ALT_GRAPH(8192);
		public final int ID;
		Modifier(int code) {
      this.ID = code;
    }
    //The following code works but is considered overkill :)
    /**
    public int id() { return ID; }
    private static final Map<Integer,Modifier> lookup = new HashMap<Integer,Modifier>();
    static {
    	for(Modifier s : EnumSet.allOf(Modifier.class))
         lookup.put(s.id(), s);
    }
    public static Modifier get(int code) {
      return lookup.get(code);
    }
    // */
	}	
	
	//M A T R I X S T A C K
	protected MatrixStack mStack;
	
  //O B J E C T S	
	protected Camera cam;
	protected InteractiveFrame glIFrame;
	protected boolean iFrameIsDrwn;
	protected Trackable trck;
	public boolean avatarIsInteractiveDrivableFrame;
	protected boolean avatarIsInteractiveAvatarFrame;
	
  // T i m e r P o o l
  protected boolean prosceneTimers;
	protected ArrayList<AbstractTimerJob> timerPool;

	// M o u s e G r a b b e r
	protected List<DeviceGrabbable> msGrabberPool;
	protected DeviceGrabbable mouseGrbbr;
	public boolean mouseGrabberIsAnIFrame;	
	protected boolean mouseTrckn;

	// D I S P L A Y F L A G S
	protected boolean axisIsDrwn; // world axis
	protected boolean gridIsDrwn; // world XY grid
	protected boolean frameSelectionHintIsDrwn;
	protected boolean cameraPathsAreDrwn;
	
  //C O N S T R A I N T S
	protected boolean withConstraint;
	
	// LEFT vs RIGHT_HAND
	protected boolean rightHanded;
	
  //A N I M A T I O N
	protected SingleThreadedTimer animationTimer;
	protected boolean animationStarted;
	public boolean animatedFrameWasTriggered;
	protected long animationPeriod;
	
  //D E V I C E S	
	protected ArrayList<AbstractDevice> devices;
	
	// L O C A L   T I M E R
	protected boolean arpFlag;
	protected boolean pupFlag;
	protected Vector3D pupVec;
	protected AbstractTimerJob timerFx;
	
	// S I Z E
	protected int width, height;
	
	public AbstractScene() {
	  // 1 ->
		//drawing timer pool
		timerPool = new ArrayList<AbstractTimerJob>();
		timerFx = new AbstractTimerJob() {
			public void execute() {
				unSetTimerFlag();
				}
			};
		prosceneTimers = false;
		registerInTimerPool(timerFx);
		
		//mouse grabber pool
		msGrabberPool = new ArrayList<DeviceGrabbable>();
		//devices
		devices = new ArrayList<AbstractDevice>();
		// <- 1
		
		setRightHanded();
		
		mStack = new MatrixStack(this);
	}
	
	// MATRIX STACK WRAPPERS
	
	/**
	 * Push a copy of the current transformation matrix onto the stack.
   */
	public void pushMatrix() {
		mStack.pushMatrix();
	}
	
	/**
	 * Replace the current transformation matrix with the top of the stack.
	 */
	public void popMatrix() {
		mStack.popMatrix();
	}	 
	
  /**
   * Translate in X and Y.
   */
  public void translate(float tx, float ty) {    
    mStack.translate(tx, ty);
  }

  /**
   * Translate in X, Y, and Z.
   */
  public void translate(float tx, float ty, float tz) {    
    mStack.translate(tx, ty, tz);
  }

  /**
   * Two dimensional rotation.
   *
   * Same as rotateZ (this is identical to a 3D rotation along the z-axis)
   * but included for clarity. It'd be weird for people drawing 2D graphics
   * to be using rotateZ. And they might kick our a-- for the confusion.
   *
   * <A HREF="http://www.xkcd.com/c184.html">Additional background</A>.
   */
  public void rotate(float angle) {    
    mStack.rotate(angle);
  }

  /**
   * Rotate around the X axis.
   */
  public void rotateX(float angle) {    
    mStack.rotateX(angle);
  }

  /**
   * Rotate around the Y axis.
   */
  public void rotateY(float angle) {
  	mStack.rotateY(angle);
  }

  /**
   * Rotate around the Z axis.
   *
   * The functions rotate() and rotateZ() are identical, it's just that it make
   * sense to have rotate() and then rotateX() and rotateY() when using 3D;
   * nor does it make sense to use a function called rotateZ() if you're only
   * doing things in 2D. so we just decided to have them both be the same.
   */
  public void rotateZ(float angle) {
  	mStack.rotateZ(angle);
  }

  /**
   * Rotate about a vector in space. Same as the glRotatef() function.
   */
  public void rotate(float angle, float vx, float vy, float vz) {
  	mStack.rotate(angle, vx, vy, vz);
  }

  /**
   * Scale in all dimensions.
   */
  public void scale(float s) {
  	mStack.scale(s);
  }

  /**
   * Scale in X and Y. Equivalent to scale(sx, sy, 1).
   *
   * Not recommended for use in 3D, because the z-dimension is just
   * scaled by 1, since there's no way to know what else to scale it by.
   */
  public void scale(float sx, float sy) {
  	mStack.scale(sx, sy);
  }

  /**
   * Scale in X, Y, and Z.
   */
  public void scale(float x, float y, float z) {
  	mStack.scale(x, y, z);
  }

  /**
   * Shear along X axis
   */
  public void shearX(float angle) {
  	mStack.shearX(angle);
  }

  /**
   * Shear along Y axis
   */
  public void shearY(float angle) {
  	mStack.shearY(angle);
  }
  
  public void loadIdentity() {
  	mStack.loadIdentity();
  }

  /**
   * Set the current transformation matrix to identity.
   */
  public void resetMatrix() {
  	mStack.resetMatrix();
  }
  
  public void loadMatrix(Matrix3D source) {
  	mStack.loadMatrix(source);
  }
  
  public void multiplyMatrix(Matrix3D source) {
  	mStack.multiplyMatrix(source);
  }
  
  public void applyMatrix(Matrix3D source) {    
    mStack.applyMatrix(source);
  }

  /**
   * Apply a 4x4 transformation matrix.
   */
  public void applyMatrix(float n00, float n01, float n02, float n03,
                          float n10, float n11, float n12, float n13,
                          float n20, float n21, float n22, float n23,
                          float n30, float n31, float n32, float n33) {    
  	mStack.applyMatrix(n00, n01, n02, n03, n10, n11, n12, n13, n20, n21, n22, n23, n30, n31, n32, n33);
  }
  
  public void frustum(float left, float right, float bottom, float top, float znear, float zfar) {
  	mStack.frustum(left, right, bottom, top, znear, zfar);
  }

  public Matrix3D getMatrix() {
    return mStack.getMatrix();
  }

  /**
   * Copy the current transformation matrix into the specified target.
   * Pass in null to create a new matrix.
   */
  public Matrix3D getMatrix(Matrix3D target) {
    return mStack.getMatrix(target);
  }

  /**
   * Set the current transformation matrix to the contents of another.
   */
  public void setMatrix(Matrix3D source) {
  	mStack.setMatrix(source);
  }

  /**
   * Print the current model (or "transformation") matrix.
   */
  public void printMatrix() {
  	mStack.printMatrix();
  }
  
  public void matrixMode( int mode  ) {
  	mStack.matrixMode(mode);
  }
	
	// end matrix stack wrapper
	
	public boolean isLeftHanded() {
		return !this.rightHanded;
	}
	
	public boolean isRightHanded() {
		return this.rightHanded;
	}
	
	public void setRightHanded() {
		rightHanded = true;
	}
	
	public void setLeftHanded() {
		rightHanded = false;
	}
	
	// 0. Optimization stuff
	
	// TODO fix documentation
	/**
	 * Apply the transformation defined by {@code frame}.
	 * The Frame is first translated and then rotated around the new translated origin.
	 * <p>
	 * Same as:
	 * <p>
	 * {@code renderer().translate(translation().x, translation().y, translation().z);} <br>
	 * {@code renderer().rotate(rotation().angle(), rotation().axis().x,
	 * rotation().axis().y, rotation().axis().z);} <br>
	 * <p>
	 * This method may be used to modify the modelview matrix from a Frame hierarchy.
	 * For example, with this Frame hierarchy:
	 * <p>
	 * {@code Frame body = new Frame();} <br>
	 * {@code Frame leftArm = new Frame();} <br>
	 * {@code Frame rightArm = new Frame();} <br>
	 * {@code leftArm.setReferenceFrame(body);} <br>
	 * {@code rightArm.setReferenceFrame(body);} <br>
	 * <p>
	 * The associated processing drawing code should look like:
	 * <p>
	 * {@code pushMatrix();} <br>
	 * {@code applyTransformation(body);} <br>
	 * {@code drawBody();} <br>
	 * {@code pushMatrix();} <br>
	 * {@code applyTransformation(leftArm);} <br>
	 * {@code drawArm();} <br>
	 * {@code popMatrix();} <br>
	 * {@code pushMatrix();} <br>
	 * {@code applyTransformation(rightArm);} <br>
	 * {@code drawArm();} <br>
	 * {@code popMatrix();} <br>
	 * {@code popMatrix();} <br>
	 * <p>
	 * If the frame hierarchy to be drawn should be applied to a different renderer
	 * context than the PApplet's (e.g., an off-screen rendering context), you may
	 * call {@code renderer().pushMatrix();} and {@code renderer().popMatrix();} above.
	 * <p> 
	 * Note the use of nested {@code pushMatrix()} and {@code popMatrix()} blocks
	 * to represent the frame hierarchy: {@code leftArm} and {@code rightArm} are
	 * both correctly drawn with respect to the {@code body} coordinate system.
	 * <p>
	 * <b>Attention:</b> When drawing a frame hierarchy as above, this method
	 * should be used whenever possible.
	 */
	public abstract void applyTransformation(SimpleFrame frame);
	
	/**
	 * Returns the approximate frame rate of the software as it executes.
	 * The initial value is 10 fps and is updated with each frame.
	 * The value is averaged (integrated) over several frames.
	 * As such, this value won't be valid until after 5-10 frames.
	 */
	public abstract float frameRate();
	
	/**
	 * Returns the number of frames displayed since the program started.
	 */
	public abstract long frameCount();
	
	// 1. Associated objects
	
	/**
	 * Returns the associated Camera, never {@code null}.
	 */
	public Camera camera() {
		return cam;
	}

	/**
	 * Replaces the current {@link #camera()} with {@code camera}
	 */
	// TODO can be implemented here?
	public abstract void setCamera(Camera camera);
	
	/**
	 * Returns {@code true} if automatic update of the camera frustum plane
	 * equations is enabled and {@code false} otherwise. Computation of the
	 * equations is expensive and hence is disabled by default.
	 * 
	 * @see #toggleFrustumEquationsUpdate()
	 * @see #disableFrustumEquationsUpdate()
	 * @see #enableFrustumEquationsUpdate()
	 * @see #enableFrustumEquationsUpdate(boolean)
	 * @see remixlab.remixcam.core.Camera#updateFrustumEquations()
	 */
	public boolean frustumEquationsUpdateIsEnable() {
		return camera().frustumEquationsUpdateIsEnable();
	}

	/**
	 * Toggles automatic update of the camera frustum plane equations every frame.
	 * Computation of the equations is expensive and hence is disabled by default.
	 * 
	 * @see #frustumEquationsUpdateIsEnable()
	 * @see #disableFrustumEquationsUpdate()
	 * @see #enableFrustumEquationsUpdate()
	 * @see #enableFrustumEquationsUpdate(boolean)
	 * @see remixlab.remixcam.core.Camera#updateFrustumEquations()
	 */
	public void toggleFrustumEquationsUpdate() {
		if ( frustumEquationsUpdateIsEnable() )
			disableFrustumEquationsUpdate();
		else
			enableFrustumEquationsUpdate();
	}

	/**
	 * Disables automatic update of the camera frustum plane equations every
	 * frame. Computation of the equations is expensive and hence is disabled by
	 * default.
	 * 
	 * @see #frustumEquationsUpdateIsEnable()
	 * @see #toggleFrustumEquationsUpdate()
	 * @see #enableFrustumEquationsUpdate()
	 * @see #enableFrustumEquationsUpdate(boolean)
	 * @see remixlab.remixcam.core.Camera#updateFrustumEquations()
	 */
	public void disableFrustumEquationsUpdate() {
		enableFrustumEquationsUpdate(false);
	}

	/**
	 * Enables automatic update of the camera frustum plane equations every frame.
	 * Computation of the equations is expensive and hence is disabled by default.
	 * 
	 * @see #frustumEquationsUpdateIsEnable()
	 * @see #toggleFrustumEquationsUpdate()
	 * @see #disableFrustumEquationsUpdate()
	 * @see #enableFrustumEquationsUpdate(boolean)
	 * @see remixlab.remixcam.core.Camera#updateFrustumEquations()
	 */
	public void enableFrustumEquationsUpdate() {
		enableFrustumEquationsUpdate(true);
	}

	/**
	 * Enables or disables automatic update of the camera frustum plane equations
	 * every frame according to {@code flag}. Computation of the equations is
	 * expensive and hence is disabled by default.
	 * 
	 * @see #frustumEquationsUpdateIsEnable()
	 * @see #toggleFrustumEquationsUpdate()
	 * @see #disableFrustumEquationsUpdate()
	 * @see #enableFrustumEquationsUpdate()
	 * @see remixlab.remixcam.core.Camera#updateFrustumEquations()
	 */
	public void enableFrustumEquationsUpdate(boolean flag) {
		camera().enableFrustumEquationsUpdate(flag);
	}
	
	/**
	 * Toggles the {@link #camera()} type between PERSPECTIVE and ORTHOGRAPHIC.
	 */
	public void toggleCameraType() {
		if (camera().type() == Camera.Type.PERSPECTIVE)
			setCameraType(Camera.Type.ORTHOGRAPHIC);
		else
			setCameraType(Camera.Type.PERSPECTIVE);
	}

	/**
	 * Toggles the {@link #camera()} kind between PROSCENE and STANDARD.
	 */
	public void toggleCameraKind() {
		if (camera().kind() == Camera.Kind.PROSCENE)
			setCameraKind(Camera.Kind.STANDARD);
		else
			setCameraKind(Camera.Kind.PROSCENE);
	}
	
	/**
	 * Returns the current {@link #camera()} type.
	 */
	public final Camera.Type cameraType() {
		return camera().type();
	}

	/**
	 * Sets the {@link #camera()} type.
	 */
	public void setCameraType(Camera.Type type) {
		if (type != camera().type())
			camera().setType(type);
	}

	/**
	 * Returns the current {@link #camera()} kind.
	 */
	public final Camera.Kind cameraKind() {
		return camera().kind();
	}

	/**
	 * Sets the {@link #camera()} kind.
	 */
	public void setCameraKind(Camera.Kind kind) {
		if (kind != camera().kind()) {
			camera().setKind(kind);
			if (kind == Camera.Kind.PROSCENE)
				System.out.println("Changing camera kind to Proscene");
			else
				System.out.println("Changing camera kind to Standard");
		}
	}
	
	/**
	 * Returns a list containing references to all the active MouseGrabbers.
	 * <p>
	 * Used to parse all the MouseGrabbers and to check if any of them
	 * {@link remixlab.remixcam.devices.DeviceGrabbable#grabsMouse()} using
	 * {@link remixlab.remixcam.devices.DeviceGrabbable#checkIfGrabsMouse(int, int, Camera)}.
	 * <p>
	 * You should not have to directly use this list. Use
	 * {@link #removeFromMouseGrabberPool(DeviceGrabbable)} and
	 * {@link #addInMouseGrabberPool(DeviceGrabbable)} to modify this list.
	 */
	public List<DeviceGrabbable> mouseGrabberPool() {
		return msGrabberPool;
	}
	
	/**
	 * Returns {@code true} if a mouse moved event  is called even when no mouse button is pressed.
	 * <p>
	 * You need to {@link #setMouseTracking(boolean)} to {@code true} in order to use MouseGrabber
	 * (see {@link #mouseGrabber()}).
	 */
	public boolean hasMouseTracking() {
		return mouseTrckn;
	}
	
	/**
	 * Sets the {@link #hasMouseTracking()} value.
	 */
	public void setMouseTracking(boolean enable) {		
		if(!enable) {
			if( mouseGrabber() != null )
				mouseGrabber().setGrabsMouse(false);
			setMouseGrabber(null);
		}
		mouseTrckn = enable;
	}
	
	/**
	 * Calls {@link #setMouseTracking(boolean)} to toggle the {@link #hasMouseTracking()} value.
	 */
	public void toggleMouseTracking() {
		setMouseTracking(!hasMouseTracking());
	}
	
	// TODO need this methods here?
	public abstract void registerInTimerPool(AbstractTimerJob job);
	
	public abstract void unregisterFromTimerPool(SingleThreadedTimer t);
	
	public void unregisterFromTimerPool(AbstractTimerJob job) {
		if (prosceneTimers) {			
			timerPool.remove(job);
		}
	}	
	
  //2. Associated objects
	
	/**
	 * Returns the InteractiveFrame associated to this Scene. It could be null if
	 * there's no InteractiveFrame associated to this Scene.
	 * 
	 * @see #setInteractiveFrame(InteractiveFrame)
	 */
	public InteractiveFrame interactiveFrame() {
		return glIFrame;
	}
	
	/**
	 * Returns the avatar object to be tracked by the Camera when it
	 * is in Third Person mode.
	 * <p>
	 * Simply returns {@code null} if no avatar has been set.
	 */
	public Trackable avatar() {
		return trck;
	}

	/**
	 * Sets the avatar object to be tracked by the Camera when it is in Third
	 * Person mode.
	 * 
	 * @see #unsetAvatar()
	 */
	public void setAvatar(Trackable t) {
		trck = t;
		avatarIsInteractiveAvatarFrame = false;
		avatarIsInteractiveDrivableFrame = false;
		if (avatar() instanceof InteractiveAvatarFrame) {
			avatarIsInteractiveAvatarFrame = true;
			avatarIsInteractiveDrivableFrame = true;
			if (interactiveFrame() != null)
				((InteractiveDrivableFrame) interactiveFrame())
						.setFlySpeed(0.01f * radius());
		} else if (avatar() instanceof InteractiveDrivableFrame) {
			avatarIsInteractiveAvatarFrame = false;
			avatarIsInteractiveDrivableFrame = true;
			if (interactiveFrame() != null)
				((InteractiveDrivableFrame) interactiveFrame())
						.setFlySpeed(0.01f * radius());
		}
	}

	/**
	 * If there's a avatar unset it.
	 * 
	 * @see #setAvatar(Trackable)
	 */
	public void unsetAvatar() {
		trck = null;
		avatarIsInteractiveAvatarFrame = false;
		avatarIsInteractiveDrivableFrame = false;
	}

	/**
	 * Sets {@code frame} as the InteractiveFrame associated to this Scene. If
	 * {@code frame} is instance of Trackable it is also automatically set as the
	 * Scene {@link #avatar()} (by automatically calling {@code
	 * setAvatar((Trackable) frame)}).
	 * 
	 * @see #interactiveFrame()
	 * @see #setAvatar(Trackable)
	 */
	public void setInteractiveFrame(InteractiveFrame frame) {
		glIFrame = frame;		
		if (glIFrame == null)
			iFrameIsDrwn = false;
		else if (glIFrame instanceof Trackable)
			setAvatar((Trackable) glIFrame);
	}

	/**
	 * Returns the current MouseGrabber, or {@code null} if none currently grabs
	 * mouse events.
	 * <p>
	 * When {@link remixlab.remixcam.devices.DeviceGrabbable#grabsMouse()}, the different
	 * mouse events are sent to it instead of their usual targets (
	 * {@link #camera()} or {@link #interactiveFrame()}).
	 */
	public DeviceGrabbable mouseGrabber() {
		return mouseGrbbr;
	}
	
	/**
	 * Directly defines the {@link #mouseGrabber()}.
	 * <p>
	 * You should not call this method directly as it bypasses the
	 * {@link remixlab.remixcam.devices.DeviceGrabbable#checkIfGrabsMouse(int, int, Camera)}
	 * test performed by parsing the mouse moved event.
	 */
	public void setMouseGrabber(DeviceGrabbable mouseGrabber) {
		mouseGrbbr = mouseGrabber;

		mouseGrabberIsAnIFrame = mouseGrabber instanceof InteractiveFrame;
	}
	
	// 3. Mouse grabber handling
	
	/**
	 * Returns true if the mouseGrabber is currently in the {@link #mouseGrabberPool()} list.
	 * <p>
	 * When set to false using {@link #removeFromMouseGrabberPool(DeviceGrabbable)}, the Scene no longer
	 * {@link remixlab.remixcam.devices.DeviceGrabbable#checkIfGrabsMouse(int, int, Camera)} on this mouseGrabber.
	 * Use {@link #addInMouseGrabberPool(DeviceGrabbable)} to insert it back.
	 */
	public boolean isInMouseGrabberPool(DeviceGrabbable mouseGrabber) {
		return mouseGrabberPool().contains(mouseGrabber);
	}
	
	/**
	 * Adds the mouseGrabber in the {@link #mouseGrabberPool()}.
	 * <p>
	 * All created InteractiveFrames (which are MouseGrabbers) are automatically added in the
	 * {@link #mouseGrabberPool()} by their constructors. Trying to add a
	 * mouseGrabber that already {@link #isInMouseGrabberPool(DeviceGrabbable)} has no effect.
	 * <p>
	 * Use {@link #removeFromMouseGrabberPool(DeviceGrabbable)} to remove the mouseGrabber from
	 * the list, so that it is no longer tested with
	 * {@link remixlab.remixcam.devices.DeviceGrabbable#checkIfGrabsMouse(int, int, Camera)}
	 * by the Scene, and hence can no longer grab mouse focus. Use
	 * {@link #isInMouseGrabberPool(DeviceGrabbable)} to know the current state of the MouseGrabber.
	 */
	public void addInMouseGrabberPool(DeviceGrabbable mouseGrabber) {
		if (!isInMouseGrabberPool(mouseGrabber))
			mouseGrabberPool().add(mouseGrabber);
	}

	/**
	 * Removes the mouseGrabber from the {@link #mouseGrabberPool()}.
	 * <p>
	 * See {@link #addInMouseGrabberPool(DeviceGrabbable)} for details. Removing a mouseGrabber
	 * that is not in {@link #mouseGrabberPool()} has no effect.
	 */
	public void removeFromMouseGrabberPool(DeviceGrabbable mouseGrabber) {
		mouseGrabberPool().remove(mouseGrabber);
	}

	/**
	 * Clears the {@link #mouseGrabberPool()}.
	 * <p>
	 * Use this method only if it is faster to clear the
	 * {@link #mouseGrabberPool()} and then to add back a few MouseGrabbers
	 * than to remove each one independently.
	 */
	public void clearMouseGrabberPool() {
		mouseGrabberPool().clear();
	}
	
	// 2. Local timer
	/**
	 * Called from the timer to stop displaying the point under pixel and arcball
	 * reference point visual hints.
	 */
	protected void unSetTimerFlag() {
		arpFlag = false;
		pupFlag = false;
	}
	
	// 3. Scene dimensions

	/**
	 * Returns the scene radius.
	 * <p>
	 * Convenience wrapper function that simply calls {@code
	 * camera().sceneRadius()}
	 * 
	 * @see #setRadius(float)
	 * @see #center()
	 */
	public float radius() {
		return camera().sceneRadius();
	}

	/**
	 * Returns the scene center.
	 * <p>
	 * Convenience wrapper function that simply returns {@code
	 * camera().sceneCenter()}
	 * 
	 * @see #setCenter(Vector3D) {@link #radius()}
	 */
	public Vector3D center() {
		return camera().sceneCenter();
	}

	/**
	 * Returns the arcball reference point.
	 * <p>
	 * Convenience wrapper function that simply returns {@code
	 * camera().arcballReferencePoint()}
	 * 
	 * @see #setCenter(Vector3D) {@link #radius()}
	 */
	public Vector3D arcballReferencePoint() {
		return camera().arcballReferencePoint();
	}

	/**
	 * Sets the {@link #radius()} of the Scene.
	 * <p>
	 * Convenience wrapper function that simply returns {@code
	 * camera().setSceneRadius(radius)}
	 * 
	 * @see #setCenter(Vector3D)
	 */
	public void setRadius(float radius) {
		camera().setSceneRadius(radius);
	}

	/**
	 * Sets the {@link #center()} of the Scene.
	 * <p>
	 * Convenience wrapper function that simply calls {@code }
	 * 
	 * @see #setRadius(float)
	 */
	public void setCenter(Vector3D center) {
		camera().setSceneCenter(center);
	}

	/**
	 * Sets the {@link #center()} and {@link #radius()} of the Scene from the
	 * {@code min} and {@code max} vectors.
	 * <p>
	 * Convenience wrapper function that simply calls {@code
	 * camera().setSceneBoundingBox(min,max)}
	 * 
	 * @see #setRadius(float)
	 * @see #setCenter(Vector3D)
	 */
	public void setBoundingBox(Vector3D min, Vector3D max) {
		camera().setSceneBoundingBox(min, max);
	}

	/**
	 * Convenience wrapper function that simply calls {@code
	 * camera().showEntireScene()}
	 * 
	 * @see remixlab.remixcam.core.Camera#showEntireScene()
	 */
	public void showAll() {
		camera().showEntireScene();
	}

	/**
	 * Convenience wrapper function that simply returns {@code
	 * camera().setArcballReferencePointFromPixel(pixel)}.
	 * <p>
	 * Current implementation set no
	 * {@link remixlab.remixcam.core.Camera#arcballReferencePoint()}. Override
	 * {@link remixlab.remixcam.core.Camera#pointUnderPixel(Point)} in your openGL
	 * based camera for this to work.
	 * 
	 * @see remixlab.remixcam.core.Camera#setArcballReferencePointFromPixel(Point)
	 * @see remixlab.remixcam.core.Camera#pointUnderPixel(Point)
	 */
	public boolean setArcballReferencePointFromPixel(Point pixel) {
		return camera().setArcballReferencePointFromPixel(pixel);
	}

	/**
	 * Convenience wrapper function that simply returns {@code
	 * camera().interpolateToZoomOnPixel(pixel)}.
	 * <p>
	 * Current implementation does nothing. Override
	 * {@link remixlab.remixcam.core.Camera#pointUnderPixel(Point)} in your openGL
	 * based camera for this to work.
	 * 
	 * @see remixlab.remixcam.core.Camera#interpolateToZoomOnPixel(Point)
	 * @see remixlab.remixcam.core.Camera#pointUnderPixel(Point)
	 */
	public Camera.WorldPoint interpolateToZoomOnPixel(Point pixel) {
		return camera().interpolateToZoomOnPixel(pixel);
	}

	/**
	 * Convenience wrapper function that simply returns {@code
	 * camera().setSceneCenterFromPixel(pixel)}
	 * <p>
	 * Current implementation set no
	 * {@link remixlab.remixcam.core.Camera#sceneCenter()}. Override
	 * {@link remixlab.remixcam.core.Camera#pointUnderPixel(Point)} in your openGL
	 * based camera for this to work.
	 * 
	 * @see remixlab.remixcam.core.Camera#setSceneCenterFromPixel(Point)
	 * @see remixlab.remixcam.core.Camera#pointUnderPixel(Point)
	 */
	public boolean setCenterFromPixel(Point pixel) {
		return camera().setSceneCenterFromPixel(pixel);
	}
	
	// * Control what is drawing
	
	/**
	 * Toggles the state of {@link #axisIsDrawn()}.
	 * 
	 * @see #axisIsDrawn()
	 * @see #setAxisIsDrawn(boolean)
	 */
	public void toggleAxisIsDrawn() {
		setAxisIsDrawn(!axisIsDrawn());
	}

	/**
	 * Toggles the state of {@link #gridIsDrawn()}.
	 * 
	 * @see #setGridIsDrawn(boolean)
	 */
	public void toggleGridIsDrawn() {
		setGridIsDrawn(!gridIsDrawn());
	}

	/**
	 * Toggles the state of {@link #frameSelectionHintIsDrawn()}.
	 * 
	 * @see #setFrameSelectionHintIsDrawn(boolean)
	 */
	public void toggleFrameSelectionHintIsDrawn() {
		setFrameSelectionHintIsDrawn(!frameSelectionHintIsDrawn());
	}

	/**
	 * Toggles the state of {@link #cameraPathsAreDrawn()}.
	 * 
	 * @see #setCameraPathsAreDrawn(boolean)
	 */
	public void toggleCameraPathsAreDrawn() {
		setCameraPathsAreDrawn(!cameraPathsAreDrawn());
	}	

	/**
	 * Toggles the draw with constraint on and off.
	 */
	public void toggleDrawWithConstraint() {
		if (drawIsConstrained())
			setDrawWithConstraint(false);
		else
			setDrawWithConstraint(true);
	}	
	
	/**
	 * Returns {@code true} if axis is currently being drawn and {@code false}
	 * otherwise.
	 */
	public boolean axisIsDrawn() {
		return axisIsDrwn;
	}

	/**
	 * Returns {@code true} if grid is currently being drawn and {@code false}
	 * otherwise.
	 */
	public boolean gridIsDrawn() {
		return gridIsDrwn;
	}

	/**
	 * Returns {@code true} if the frames selection visual hints are currently
	 * being drawn and {@code false} otherwise.
	 */
	public boolean frameSelectionHintIsDrawn() {
		return frameSelectionHintIsDrwn;
	}

	/**
	 * Returns {@code true} if the camera key frame paths are currently being
	 * drawn and {@code false} otherwise.
	 */
	public boolean cameraPathsAreDrawn() {
		return cameraPathsAreDrwn;
	}

	/**
	 * Returns {@code true} if axis is currently being drawn and {@code false}
	 * otherwise.
	 */
	public boolean interactiveFrameIsDrawn() {
		return iFrameIsDrwn;
	}

	/**
	 * Convenience function that simply calls {@code setAxisIsDrawn(true)}
	 */
	public void setAxisIsDrawn() {
		setAxisIsDrawn(true);
	}

	/**
	 * Sets the display of the axis according to {@code draw}
	 */
	public void setAxisIsDrawn(boolean draw) {
		axisIsDrwn = draw;
	}

	/**
	 * Convenience function that simply calls {@code setGridIsDrawn(true)}
	 */
	public void setGridIsDrawn() {
		setGridIsDrawn(true);
	}

	/**
	 * Sets the display of the grid according to {@code draw}
	 */
	public void setGridIsDrawn(boolean draw) {
		gridIsDrwn = draw;
	}

	/**
	 * Sets the display of the interactive frames' selection hints according to
	 * {@code draw}
	 */
	public void setFrameSelectionHintIsDrawn(boolean draw) {
		frameSelectionHintIsDrwn = draw;
	}

	/**
	 * Sets the display of the camera key frame paths according to {@code draw}
	 */
	public void setCameraPathsAreDrawn(boolean draw) {
		cameraPathsAreDrwn = draw;
	}
	
	/**
	 * Returns {@code true} if drawn is currently being constrained and {@code
	 * false} otherwise.
	 */
	public boolean drawIsConstrained() {
		return withConstraint;
	}

	/**
	 * Constrain frame displacements according to {@code wConstraint}
	 */
	public void setDrawWithConstraint(boolean wConstraint) {
		withConstraint = wConstraint;
	}
	
	// * Abstract drawing methods
	
	/**
	 * Draws a cylinder of width {@code w} and height {@code h}, along the 
	 * positive {@code z} axis. 
	 */
	public abstract void cylinder(float w, float h);
	
	/**
	 * Same as {@code cone(det, 0, 0, r, h);}
	 * 
	 * @see #cone(int, float, float, float, float)
	 */
	public void cone(int det, float r, float h) {
		cone(det, 0, 0, r, h);
	}		
	
	/**
	 * Same as {@code cone(12, 0, 0, r, h);}
	 * 
	 * @see #cone(int, float, float, float, float)
	 */
	public void cone(float r, float h) {
		cone(12, 0, 0, r, h);
	}
	
	/**
	 * Draws a cone along the positive {@code z} axis, with its base centered
	 * at {@code (x,y)}, height {@code h}, and radius {@code r}. 
	 * 
	 * @see #cone(int, float, float, float, float, float)
	 */
	public abstract void cone(int detail, float x, float y, float r, float h);
	
	/**
	 * Same as {@code cone(det, 0, 0, r1, r2, h);}
	 * 
	 * @see #cone(int, float, float, float, float, float)
	 */
	public void cone(int det, float r1, float r2, float h) {
		cone(det, 0, 0, r1, r2, h);
	}	
	
	/**
	 * Same as {@code cone(18, 0, 0, r1, r2, h);}
	 * 
	 * @see #cone(int, float, float, float, float, float)
	 */
	public void cone(float r1, float r2, float h) {
		cone(18, 0, 0, r1, r2, h);
	}
	
	/**
	 * Draws a truncated cone along the positive {@code z} axis,
	 * with its base centered at {@code (x,y)}, height {@code h}, and radii
	 * {@code r1} and {@code r2} (basis and height respectively).
	 * 
	 * @see #cone(int, float, float, float, float)
	 */
	public abstract void cone(int detail, float x, float y,	float r1, float r2, float h);
	
	/**
	 * Convenience function that simply calls {@code drawAxis(100)}.
	 */
	public void drawAxis() {
		drawAxis(100);
	}
	
	/**
	 * Draws an axis of length {@code length} which origin correspond to the
	 * world coordinate system origin.
	 * 
	 * @see #drawGrid(float, int)
	 */
	public abstract void drawAxis(float length);
	
	/**
	 * Simply calls {@code drawArrow(length, 0.05f * length)}
	 * 
	 * @see #drawArrow(float, float)
	 */
	public void drawArrow(float length) {
		drawArrow(length, 0.05f * length);
	}
	
	/**
	 * Draws a 3D arrow along the positive Z axis.
	 * <p>
	 * {@code length} and {@code radius} define its geometry.
	 * <p>
	 * Use {@link #drawArrow(Vector3D, Vector3D, float)} to place the arrow
	 * in 3D.
	 */
	public abstract void drawArrow(float length, float radius);
	
	/**
	 * Draws a 3D arrow between the 3D point {@code from} and the 3D point {@code
	 * to}, both defined in the current world coordinate system.
	 * 
	 * @see #drawArrow(float, float)
	 */
	public abstract void drawArrow(Vector3D from, Vector3D to,	float radius);
	
	/**
	 * Convenience function that simply calls {@code drawGrid(100, 10)}
	 * 
	 * @see #drawGrid(float, int)
	 */
	public void drawGrid() {
		drawGrid(100, 10);
	}	
	
	/**
	 * Convenience function that simply calls {@code drawGrid(size, 10)}
	 * 
	 * @see #drawGrid(float, int)
	 */
	public void drawGrid(float size) {
		drawGrid(size, 10);
	}
	
	/**
	 * Convenience function that simply calls {@code drawGrid(100,
	 * nbSubdivisions)}
	 * 
	 * @see #drawGrid(float, int)
	 */
	public void drawGrid(int nbSubdivisions) {
		drawGrid(100, nbSubdivisions);
	}
	
	/**
	 * Draws a grid in the XY plane, centered on (0,0,0) (defined in the current
	 * coordinate system).
	 * <p>
	 * {@code size} and {@code nbSubdivisions} define its geometry.
	 * 
	 * @see #drawAxis(float)
	 */
	public abstract void drawGrid(float size, int nbSubdivisions);
	
	/**
	 * Convenience function that simply calls {@code drawCamera(camera, true, 1.0f)}
	 * 
	 * @see #drawCamera(Camera, boolean, float)
	 */
	public void drawCamera(Camera camera) {
		drawCamera(camera, true, 1.0f);
	}

	/**
	 * Convenience function that simply calls {@code drawCamera(camera, true, scale)}
	 * 
	 * @see #drawCamera(Camera, boolean, float)
	 */
	public void drawCamera(Camera camera, float scale) {
		drawCamera(camera, true, scale);
	}
	
	/**
	 * Convenience function that simply calls {@code drawCamera(camera, drawFarPlane, 1.0f)}
	 * 
	 * @see #drawCamera(Camera, boolean, float)
	 */
	public void drawCamera(Camera camera, boolean drawFarPlane) {
		drawCamera(camera, drawFarPlane, 1.0f);
	}
	
	/**
	 * Draws a representation of the {@code camera} in the 3D virtual world.
	 * <p>
	 * The near and far planes are drawn as quads, the frustum is drawn using
	 * lines and the camera up vector is represented by an arrow to disambiguate
	 * the drawing.
	 * <p>
	 * When {@code drawFarPlane} is {@code false}, only the near plane is drawn.
	 * {@code scale} can be used to scale the drawing: a value of 1.0 (default)
	 * will draw the Camera's frustum at its actual size.
	 * <p>
	 * <b>Note:</b> The drawing of a Scene's own Scene.camera() should not be
	 * visible, but may create artifacts due to numerical imprecisions.
	 */
	public abstract void drawCamera(Camera camera, boolean drawFarPlane, float scale);
	
	public abstract void drawKFICamera(float scale);
	
	/**
	 * Draws a rectangle on the screen showing the region where a zoom operation
	 * is taking place.
	 */	
	protected abstract void drawZoomWindowHint();
	
	/**
	 * Draws visual hint (a line on the screen) when a screen rotation is taking
	 * place.
	 */
	protected abstract void drawScreenRotateLineHint();
	
	/**
	 * Draws visual hint (a cross on the screen) when the
	 * {@link #arcballReferencePoint()} is being set.
	 * <p>
	 * Simply calls {@link #drawCross(float, float)} on {@code
	 * camera().projectedCoordinatesOf(arcballReferencePoint())} {@code x} and
	 * {@code y} coordinates.
	 * 
	 * @see #drawCross(float, float)
	 */	
	public abstract void drawArcballReferencePointHint();
	
	/**
	 * Draws all InteractiveFrames' selection regions: a shooter target
	 * visual hint of {@link remixlab.remixcam.core.InteractiveFrame#grabsMouseThreshold()} pixels size.
	 * 
	 * <b>Attention:</b> If the InteractiveFrame is part of a Camera path draws
	 * nothing.
	 * 
	 * @see #drawCameraPathSelectionHints()
	 */
	protected abstract void drawSelectionHints();
	
	/**
	 * Draws the selection regions (a shooter target visual hint of
	 * {@link remixlab.remixcam.core.InteractiveFrame#grabsMouseThreshold()} pixels size) of all
	 * InteractiveFrames forming part of the Camera paths.
	 * 
	 * @see #drawSelectionHints()
	 */
	protected abstract void drawCameraPathSelectionHints();
		
	public abstract void drawCross(float px, float py);
	
	/**
	 * Draws a cross on the screen centered under pixel {@code (px, py)}, and edge
	 * of size {@code size}.
	 * 
	 * @see #drawArcballReferencePointHint()
	 */
	public abstract void drawCross(float px, float py, float size);
	
	/**
	 * Convenience function that simply calls
	 * {@code drawFilledCircle(40, center, radius)}.
	 * 
	 * @see #drawFilledCircle(int, Vector3D, float)
	 */
	public void drawFilledCircle(Vector3D center, float radius) {
		drawFilledCircle(40, center, radius);
	}
	
	/**
	 * Draws a filled circle using screen coordinates.
	 * 
	 * @param subdivisions
	 *          Number of triangles approximating the circle. 
	 * @param center
	 *          Circle screen center.
	 * @param radius
	 *          Circle screen radius.
	 */	
	public abstract void drawFilledCircle(int subdivisions, Vector3D center, float radius);
	
	/**
	 * Draws a filled square using screen coordinates.
	 * 
	 * @param center
	 *          Square screen center.
	 * @param edge
	 *          Square edge length.
	 */
	public abstract void drawFilledSquare(Vector3D center, float edge);
	
	/**
	 * Draws the classical shooter target on the screen.
	 * 
	 * @param center
	 *          Center of the target on the screen
	 * @param length
	 *          Length of the target in pixels
	 */
	public abstract void drawShooterTarget(Vector3D center, float length);
		
	public abstract void drawPath(List<SimpleFrame> path, int mask, int nbFrames, int nbSteps, float scale);
}
