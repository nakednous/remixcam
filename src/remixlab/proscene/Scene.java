/**
 *                     ProScene (version 1.0.1)      
 *    Copyright (c) 2010-2011 by National University of Colombia
 *                 @author Jean Pierre Charalambos      
 *           http://www.disi.unal.edu.co/grupos/remixlab/
 *                           
 * This java package provides classes to ease the creation of interactive 3D
 * scenes in Processing.
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

package remixlab.proscene;

import processing.core.*;
import remixlab.remixcam.core.GLFrame;
import remixlab.remixcam.core.KeyFrameInterpolator;
import remixlab.remixcam.core.RCScene;
import remixlab.remixcam.core.Camera;
import remixlab.remixcam.core.InteractiveDrivableFrame;
import remixlab.remixcam.core.InteractiveFrame;
import remixlab.remixcam.core.MouseGrabbable;
import remixlab.remixcam.core.KeyFrameInterpolator.KeyFrame;
import remixlab.remixcam.devices.Actions.KeyboardAction;
import remixlab.remixcam.devices.Actions.CameraKeyboardAction;
import remixlab.remixcam.devices.Actions.ClickAction;
import remixlab.remixcam.devices.Actions.MouseAction;
import remixlab.remixcam.geom.Matrix3D;
import remixlab.remixcam.geom.Point;
import remixlab.remixcam.geom.Quaternion;
import remixlab.remixcam.geom.Vector3D;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * A 3D interactive Processing scene.
 * <p>
 * A Scene has a full reach Camera, an two means to manipulate objects: an
 * {@link #interactiveFrame()} single instance (which by default is null) and a
 * {@link #mouseGrabber()} pool.
 * <h3>Usage</h3>
 * To use a Scene you have three choices:
 * <ol>
 * <li><b>Direct instantiation</b>. In this case you should instantiate your own
 * Scene object at the {@code PApplet.setup()} function.
 * See the example <i>BasicUse</i>.
 * <li><b>Inheritance</b>. In this case, once you declare a Scene derived class,
 * you should implement {@link #proscenium()} which defines the objects in your
 * scene. Just make sure to define the {@code PApplet.draw()} method, even if
 * it's empty. See the example <i>AlternativeUse</i>.
 * <li><b>External draw handler registration</b>. You can even declare an
 * external drawing method and then register it at the Scene with
 * {@link #addDrawHandler(Object, String)}. That method should return {@code
 * void} and have one single {@code Scene} parameter. This strategy may be useful
 * when there are multiple viewers sharing the same drawing code. See the
 * example <i>StandardCamera</i>.
 * </ol>
 * <h3>Interactivity mechanisms</h3>
 * Proscene provides two interactivity mechanisms to manage your scene: global
 * keyboard shortcuts and camera profiles.
 * <ol>
 * <li><b>Global keyboard shortcuts</b> provide global configuration options
 * such as {@link #drawGrid()} or {@link #drawAxis()} that are common among
 * the different registered camera profiles. To define a global keyboard shortcut use
 * {@link #setShortcut(Character, KeyboardAction)} or one of its different forms.
 * Check {@link #setDefaultShortcuts()} to see the default global keyboard shortcuts.
 * <li><b>Camera profiles</b> represent a set of camera keyboard shortcuts, and camera and
 * frame mouse bindings which together represent a "camera mode". The scene provide
 * high-level methods to manage camera profiles such as
 * {@link #registerCameraProfile(CameraProfile)},
 * {@link #unregisterCameraProfile(CameraProfile)} or {@link #currentCameraProfile()}
 * among others. To perform the configuration of a camera profile see the CameraProfile
 * class documentation.
 * </ol>
 * <h3>Animation mechanisms</h3>
 * Proscene provides three animation mechanisms to define how your scene evolves
 * over time:
 * <ol>
 * <li><b>Overriding the {@link #animate()} method.</b>  In this case, once you
 * declare a Scene derived class, you should implement {@link #animate()} which
 * defines how your scene objects evolve over time. See the example <i>Animation</i>.
 * <li><b>External animation handler registration.</b> You can also declare an
 * external animation method and then register it at the Scene with
 * {@link #addAnimationHandler(Object, String)}. That method should return {@code
 * void} and have one single {@code Scene} parameter. See the example
 * <i>AnimationHandler</i>.
 * <li><b>By querying the state of the {@link #animatedFrameWasTriggered} variable.</b>
 * During the drawing loop, the variable {@link #animatedFrameWasTriggered} is set
 * to {@code true} each time an animated frame is triggered (and to {@code false}
 * otherwise), which is useful to notify the outside world when an animation event
 * occurs. See the example <i>Flock</i>.
 * </ol>
 * <b>Attention:</b> To set the PApplet's background you should call one of the
 * {@code Scene.background()} versions instead of any of the {@code
 * PApplet.background()} ones. The background is set to black by default.
 */
public class Scene extends RCScene implements PConstants {
	// proscene version
	public static final String version = "2.0.0";
	/**
	 * Returns the major release version number of proscene as an integer.
	 * <p>
	 * {@code Scene.version} will return the complete version (major+minor)
	 * number as a string. 
	 */
	public static int majorVersionNumber() {
		return Integer.parseInt(majorVersion());
	}
	
	/**
	 * Returns the major release version number of proscene as a string.
	 * <p>
	 * {@code Scene.version} will return the complete version (major+minor)
	 * number as a string.
	 */
	public static String majorVersion() {
		return version.substring(0, version.indexOf("."));
	}
	
	/**
	 * Returns the minor release version number of proscene as a float.
	 * <p>
	 * {@code Scene.version} will return the complete version (major+minor)
	 * number as a string.
	 */
	public static float minorVersionNumber() {
		return Float.parseFloat(minorVersion());
	}
	
	/**
	 * Returns the minor release version number of proscene as a string.
	 * <p>
	 * {@code Scene.version} will return the complete version (major+minor)
	 * number as a string.
	 */
	public static String minorVersion() {
		return version.substring(version.indexOf(".") + 1);
	}

	/**
	 * Constants associated to the different arrow keys. Taken from Processing constants 
	 * (which follows java conventions). 
	 */	
	public enum Arrow {
		UP(PApplet.UP), DOWN(PApplet.DOWN), LEFT(PApplet.LEFT), RIGHT(PApplet.RIGHT);
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

	/**
	 * This enum defines the papplet background mode which should be set by
	 * proscene.
	 */
	public enum BackgroundMode {
		RGB, RGB_ALPHA, GRAY, GRAY_ALPHA, XYZ, XYZ_ALPHA, PIMAGE
	}

	// K E Y F R A M E S
	protected Bindings<Integer, Integer> pathKeys;
	protected Modifier addKeyFrameKeyboardModifier;
	protected Modifier deleteKeyFrameKeyboardModifier;

	// S h o r t c u t k e y s
	protected Bindings<KeyboardShortcut, KeyboardAction> gProfile;

	// c a m e r a p r o f i l e s
	private HashMap<String, CameraProfile> cameraProfileMap;
	private ArrayList<String> cameraProfileNames;
	private CameraProfile currentCameraProfile;
	
	// background
	private BackgroundMode backgroundMode;
	private boolean enableBackground;
	int rgb;
	float gray, alpha, x, y, z;
	PImage image;

	// P R O C E S S I N G   A P P L E T   A N D   O B J E C T S
	public PApplet parent;
	public PGraphics3D pg3d;		

	// O B J E C T S
	protected DesktopEvents dE;

	// S C R E E N C O O R D I N A T E S
	float halfWidthSpace;
	float halfHeightSpace;
	float zC;

	// E X C E P T I O N H A N D L I N G
	protected int startCoordCalls;
  protected int beginOffScreenDrawingCalls;	

	// K E Y B O A R D A N D M O U S E
	protected boolean mouseHandling;
	protected boolean keyboardHandling;
	
	// A N I M A T I O N
	protected float targetFrameRate;
	protected float animationFrameRate;
	private long initialDrawingFrameWhenAnimationStarted;
	private long currentAnimationFrame;
	private float animationToFrameRateRatio;
	//private int framesInBetween;
	private boolean animationStarted;
	public boolean animatedFrameWasTriggered;
	private float animationPeriod;

	// R E G I S T E R   D R A W   A N D   A N I M A T I O N   M E T H O D S
	// Draw
	/** The object to handle the draw event */
	protected Object drawHandlerObject;
	/** The method in drawHandlerObject to execute */
	protected Method drawHandlerMethod;
	/** the name of the method to handle the event */
	protected String drawHandlerMethodName;
	// Animation
	/** The object to handle the animation */
	protected Object animateHandlerObject;
	/** The method in animateHandlerObject to execute */
	protected Method animateHandlerMethod;
	/** the name of the method to handle the animation */
	protected String animateHandlerMethodName;	
	
	// DRAWING STUFF
	GLFrame tmpFrame;
	
	/**
	 * All viewer parameters (display flags, scene parameters, associated
	 * objects...) are set to their default values. The PApplet background is set
	 * to black. See the associated documentation.
	 */	
	public Scene(PApplet p) {
		this(p, (PGraphics3D) p.g);
	}

	/**
	 * All viewer parameters (display flags, scene parameters, associated
	 * objects...) are set to their default values. The PApplet background is set
	 * to black. See the associated documentation. A custom renderer can be
	 * specified as well, and if it is different from the PApplet's renderer,
	 * this will result in an offscreen Scene.
	 * <p>
	 * <b>Attention:</b> If the Scene is in offscreen mode, mouse tracking and
	 * screen rendering are completely disabled.  
	 */
	public Scene(PApplet p, PGraphics3D renderer) {
		parent = p;
		pg3d = renderer;
		width = pg3d.width;
		height = pg3d.height;		
		
		timerPool = new PTimerPool();
		timerPool.registerInTimerPool(this, timerFx);
		
		//event handler
		dE = new DesktopEvents(this);		

		gProfile = new Bindings<KeyboardShortcut, KeyboardAction>(this);
		pathKeys = new Bindings<Integer, Integer>(this);		
		setDefaultShortcuts();

		//TODO check this line
		cam = new Camera(this);
		setCamera(camera());// showAll();It is set in setCamera()
		setInteractiveFrame(null);
		setAvatar(null);
		
  	// This scene is offscreen if the provided renderer is
		// different from the main PApplet renderer.
		offscreen = renderer != p.g;
		beginOffScreenDrawingCalls = 0;
		setMouseTracking(!offscreen);
		mouseGrabberPool.setMouseGrabber(null, camera());

		initDefaultCameraProfiles();

		//animation		
		animationStarted = false;
		setFrameRate(60, false);
		setAnimationPeriod(1000/60, false); // 60Hz
		stopAnimation();
		
		arpFlag = false;
		pupFlag = false;	

		setAxisIsDrawn(true);
		setGridIsDrawn(true);
		setFrameSelectionHintIsDrawn(false);
		setCameraPathsAreDrawn(false);		

		// E X C E P T I O N H A N D L I N G
		startCoordCalls = 0;

		enableBackgroundHanddling();
		image = null;
		background(0);
		parent.registerPre(this);
		parent.registerDraw(this);
		// parent.registerPost(this);
		enableKeyboardHandling();
		enableMouseHandling();
		parseKeyXxxxMethods();
		parseMouseXxxxMethods();

		// register draw method
		removeDrawHandler();
	  // register animation method
		removeAnimationHandler();
		
		// DRAWING STUFF
		tmpFrame = new GLFrame();

		// called only once
		init();
	}
	
	// 1. Scene overloaded
	
	/**
	 * This method is called before the first drawing happen and should be overloaded to
	 * initialize stuff not initialized in {@code PApplet.setup()}. The default
	 * implementation is empty.
	 * <p>
	 * Typical usage include {@link #camera()} initialization ({@link #showAll()})
	 * and Scene state setup ({@link #setAxisIsDrawn(boolean)} and
	 * {@link #setGridIsDrawn(boolean)}.
	 */
	public void init() {}
	
	/**
	 * The method that actually defines the scene.
	 * <p>
	 * If you build a class that inherits from Scene, this is the method you
	 * should overload, but no if you instantiate your own Scene object (in this
	 * case you should just overload {@code PApplet.draw()} to define your scene).
	 * <p>
	 * The processing camera set in {@link #pre()} converts from the world to the
	 * camera coordinate systems. Vertices given in {@link #draw()} can then be
	 * considered as being given in the world coordinate system. The camera is
	 * moved in this world using the mouse. This representation is much more
	 * intuitive than a camera-centric system (which for instance is the standard
	 * in OpenGL).
	 */
	public void proscenium() {}

	// 2. Associated objects

	/**
	 * Replaces the current {@link #camera()} with {@code camera}
	 */
	//TODO should have been implemented in the super class but
	//we need the view-port width and height
	public void setCamera(Camera camera) {
		if (camera == null)
			return;

		camera.setSceneRadius(radius());
		camera.setSceneCenter(camera().sceneCenter());
		camera.setScreenWidthAndHeight(pg3d.width, pg3d.height);

		cam = camera;

		showAll();
	}	

	// 4. State of the viewer

	/**
	 * Enables background handling in the Scene (see the different {@code
	 * background} functions), otherwise the background should be set with the
	 * corresponding PApplet functions.
	 * 
	 * @see #toggleBackgroundHanddling()
	 * @see #backgroundIsHandled()
	 */
	public void enableBackgroundHanddling() {
		enableBackground = true;
	}

	/**
	 * Disables background handling by the Scene. Hence the background should be
	 * set with the corresponding PApplet functions.
	 * 
	 * @see #toggleBackgroundHanddling()
	 * @see #backgroundIsHandled()
	 */
	public void disableBackgroundHanddling() {
		enableBackground = false;
	}

	/**
	 * Returns {@code true} if the background is handled by the Scene and {@code
	 * false} otherwise.
	 * 
	 * @see #enableBackgroundHanddling()
	 * @see #disableBackgroundHanddling()
	 */
	public boolean backgroundIsHandled() {
		return enableBackground;
	}
	
	/**
	 * Toggles the state of the {@link #backgroundIsHandled()}.
	 * 
	 * @see #enableBackgroundHanddling()
	 * @see #disableBackgroundHanddling()
	 */
	public void toggleBackgroundHanddling() {
		if (backgroundIsHandled())
			disableBackgroundHanddling();
		else
			enableBackgroundHanddling();
	}

	/**
	 * Internal use only. Call the proper PApplet background function at the
	 * beginning of {@link #pre()}.
	 * 
	 * @see #pre()
	 * @see #background(float)
	 * @see #background(int)
	 * @see #background(PImage)
	 * @see #background(float, float)
	 * @see #background(int, float)
	 * @see #background(float, float, float)
	 * @see #background(float, float, float, float)
	 */
	protected void setBackground() {
		switch (backgroundMode) {
		case RGB:
			pg3d.background(rgb);
			break;
		case RGB_ALPHA:
			pg3d.background(rgb, alpha);
			break;
		case GRAY:
			pg3d.background(gray);
			break;
		case GRAY_ALPHA:
			pg3d.background(gray, alpha);
			break;
		case XYZ:
			pg3d.background(x, y, z);
			break;
		case XYZ_ALPHA:
			pg3d.background(x, y, z, alpha);
			break;
		case PIMAGE:
			pg3d.background(image);
			break;
		}
	}

	/**
	 * Wrapper function for the {@code PApplet.background()} function with the
	 * same signature. Sets the color used for the background of the Processing
	 * window. The default background is black. See the processing documentation
	 * for details.
	 * <p>
	 * The {@code PApplet.background()} is automatically called at the beginning
	 * of the {@link #pre()} method (Hence you can call this function from where
	 * ever you want) and is used to clear the display window.
	 */
	public void background(int my_rgb) {
		rgb = my_rgb;
		backgroundMode = BackgroundMode.RGB;
	}

	/**
	 * Wrapper function for the {@code PApplet.background()} function with the
	 * same signature. Sets the color used for the background of the Processing
	 * window. The default background is black. See the processing documentation
	 * for details.
	 * <p>
	 * The {@code PApplet.background()} is automatically called at the beginning
	 * of the {@link #pre()} method (Hence you can call this function from where
	 * ever you want) and is used to clear the display window.
	 */
	public void background(int my_rgb, float my_alpha) {
		rgb = my_rgb;
		alpha = my_alpha;
		backgroundMode = BackgroundMode.RGB_ALPHA;
	}

	/**
	 * Wrapper function for the {@code PApplet.background()} function with the
	 * same signature. Sets the color used for the background of the Processing
	 * window. The default background is black. See the processing documentation
	 * for details.
	 * <p>
	 * The {@code PApplet.background()} is automatically called at the beginning
	 * of the {@link #pre()} method (Hence you can call this function from where
	 * ever you want) and is used to clear the display window.
	 */
	public void background(float my_gray) {
		gray = my_gray;
		backgroundMode = BackgroundMode.GRAY;
	}

	/**
	 * Wrapper function for the {@code PApplet.background()} function with the
	 * same signature. Sets the color used for the background of the Processing
	 * window. The default background is black. See the processing documentation
	 * for details.
	 * <p>
	 * The {@code PApplet.background()} is automatically called at the beginning
	 * of the {@link #pre()} method (Hence you can call this function from where
	 * ever you want) and is used to clear the display window.
	 */
	public void background(float my_gray, float my_alpha) {
		gray = my_gray;
		alpha = my_alpha;
		backgroundMode = BackgroundMode.GRAY_ALPHA;
	}

	/**
	 * Wrapper function for the {@code PApplet.background()} function with the
	 * same signature. Sets the color used for the background of the Processing
	 * window. The default background is black. See the processing documentation
	 * for details.
	 * <p>
	 * The {@code PApplet.background()} is automatically called at the beginning
	 * of the {@link #pre()} method (Hence you can call this function from where
	 * ever you want) and is used to clear the display window.
	 */
	public void background(float my_x, float my_y, float my_z) {
		x = my_x;
		y = my_y;
		z = my_z;
		backgroundMode = BackgroundMode.XYZ;
	}

	/**
	 * Wrapper function for the {@code PApplet.background()} function with the
	 * same signature. Sets the color used for the background of the Processing
	 * window. The default background is black. See the processing documentation
	 * for details.
	 * <p>
	 * The {@code PApplet.background()} is automatically called at the beginning
	 * of the {@link #pre()} method (Hence you can call this function from where
	 * ever you want) and is used to clear the display window.
	 */
	public void background(float my_x, float my_y, float my_z, float my_a) {
		x = my_x;
		y = my_y;
		z = my_z;
		alpha = my_a;
		backgroundMode = BackgroundMode.XYZ_ALPHA;
	}

	/**
	 * Wrapper function for the {@code PApplet.background()} function with the
	 * same signature. Sets the PImage used for the background of the Processing
	 * window. The default background is black. See the processing documentation
	 * for details.
	 * <p>
	 * The {@code PApplet.background()} is automatically called at the beginning
	 * of the {@link #pre()} method (Hence you can call this function from where
	 * ever you want) and is used to clear the display window.
	 * <p>
	 * <b>Attention:</b> If the sizes of the {@code img} and the PApplet differ,
	 * the {@code img} will be resized to accommodate the size of the PApplet.
	 */
	public void background(PImage img) {
		image = img;
		if ((image.width != pg3d.width) || (image.height != pg3d.height))
			image.resize(pg3d.width, pg3d.height);
		backgroundMode = BackgroundMode.PIMAGE;
	}

	/**
	 * Returns the background image if any.
	 * 
	 * @return image
	 */
	public PImage backgroundImage() {
		return image;
	}

	// 5. Drawing methods

	/**
	 * Internal use. Display various on-screen visual hints to be called from {@link #pre()}
	 * or {@link #draw()} depending on the {@link #backgroundIsHandled()} state.
	 */
	private void displayVisualHints() {		
		if (frameSelectionHintIsDrawn())
			drawSelectionHints();
		if (cameraPathsAreDrawn()) {
			drawAllPaths();
			drawCameraPathSelectionHints();
		} else {
			camera().hideAllPaths();
		}
		if (dE.camMouseAction == MouseAction.ZOOM_ON_REGION)
			drawZoomWindowHint();
		if (dE.camMouseAction == MouseAction.SCREEN_ROTATE)
			drawScreenRotateLineHint();
		if (arpFlag)
			drawArcballReferencePointHint();
		if (pupFlag) {
			Vector3D v = camera().projectedCoordinatesOf( pupVec );
			drawCross(v.x, v.y);
		}
	}

	/**
	 * Paint method which is called just before your {@code PApplet.draw()}
	 * method. This method is registered at the PApplet and hence you don't need
	 * to call it.
	 * <p>
	 * First sets the background (see {@link #setBackground()}) and then sets the
	 * processing camera parameters from {@link #camera()} and displays axis,
	 * grid, interactive frames' selection hints and camera paths, accordingly to
	 * user flags.
	 */
	public void pre() {
		if (isOffscreen()) return;
		
		if( timerPool.needInit() )
			timerPool.init();
		
		// handle possible resize events
		// weird: we need to bypass the handling of a resize event when running the
		// applet from eclipse		
		if ((parent.frame != null) && (parent.frame.isResizable())) {
			if (backgroundIsHandled() && (backgroundMode == BackgroundMode.PIMAGE))
				this.background(this.image);
			if ((width != pg3d.width) || (height != pg3d.height)) {
				width = pg3d.width;
				height = pg3d.height;
				// weirdly enough we need to bypass what processing does
				// to the matrices when a resize event takes place
				// TODO: P5Cam?
				//camera().detachFromPCamera();
				camera().setScreenWidthAndHeight(width, height);
				//camera().attachToPCamera();
			} else {
				if ((currentCameraProfile().mode() == CameraProfile.Mode.THIRD_PERSON)
						&& (!camera().anyInterpolationIsStarted())) {
					camera().setPosition(avatar().cameraPosition());
					camera().setUpVector(avatar().upVector());
					camera().lookAt(avatar().target());
				}
				// We set the processing camera matrices from our
				// remixlab.proscene.Camera
				setPProjectionMatrix();
				setPModelViewMatrix();
				// camera().computeProjectionMatrix();
				// camera().computeModelViewMatrix();
			}
		} else {
			if ((currentCameraProfile().mode() == CameraProfile.Mode.THIRD_PERSON)
					&& (!camera().anyInterpolationIsStarted())) {
				camera().setPosition(avatar().cameraPosition());
				camera().setUpVector(avatar().upVector());
				camera().lookAt(avatar().target());
			}
			// We set the processing camera matrices from our remixlab.proscene.Camera
			setPProjectionMatrix();
			setPModelViewMatrix();
			// same as the two previous lines:
			// WARNING: this can produce visual artifacts when using OPENGL and
			// GLGRAPHICS renderers because
			// processing will anyway set the matrices at the end of the rendering
			// loop.
			// camera().computeProjectionMatrix();
			// camera().computeModelViewMatrix();
		}

		if (frustumEquationsUpdateIsEnable())
			camera().updateFrustumEquations();

		if (backgroundIsHandled()) {
			setBackground();
			if (gridIsDrawn())
				drawGrid(camera().sceneRadius());
			if (axisIsDrawn())
				drawAxis(camera().sceneRadius());
			displayVisualHints();
		}
	}

	/**
	 * Paint method which is called just after your {@code PApplet.draw()} method.
	 * This method is registered at the PApplet and hence you don't need to call
	 * it.
	 * <p>
	 * First calls {@link #proscenium()} which is the main drawing method that
	 * could be overloaded. then, if there's an additional drawing method
	 * registered at the Scene, calls it (see
	 * {@link #addDrawHandler(Object, String)}). Finally, displays the
	 * {@link #displayGlobalHelp()} and some visual hints (such {@link #drawZoomWindowHint()},
	 * {@link #drawScreenRotateLineHint()} and
	 * {@link #drawArcballReferencePointHint()}) according to user interaction and
	 * flags.
	 * 
	 * @see #proscenium()
	 * @see #addDrawHandler(Object, String)
	 */
	public void draw() {
		if (isOffscreen()) return;		
		
		if( animationIsStarted() )
			performAnimation();
		
		// 1. Alternative use only
		proscenium();

		// 2. Draw external registered method
		if (drawHandlerObject != null) {
			try {
				drawHandlerMethod.invoke(drawHandlerObject, new Object[] { this });
			} catch (Exception e) {
				PApplet.println("Something went wrong when invoking your "	+ drawHandlerMethodName + " method");
				e.printStackTrace();
			}
		}

		// 3. Try to draw what should have been drawn in the pre()
		if (!backgroundIsHandled()) {
			if (gridIsDrawn())
				drawGrid(camera().sceneRadius());
			if (axisIsDrawn())
				drawAxis(camera().sceneRadius());
			displayVisualHints();
		}
	}

	/**
	 * This method should be called when using offscreen rendering 
	 * right after renderer.beginDraw(), and it sets the background 
	 * and display the grid and the axis if necessary.
	 * <p>
	 * <b>Attention:</b> All visual hints that use on screen rendering,
	 * are completely disabled in offscreen mode.
   */	
	public void beginDraw() {
		if (isOffscreen()) {
			if( timerPool.needInit() )
				timerPool.init();
			
			if (beginOffScreenDrawingCalls != 0)
				throw new RuntimeException(
						"There should be exactly one beginDraw() call followed by a "
								+ "endDraw() and they cannot be nested. Check your implementation!");
			
			beginOffScreenDrawingCalls++;
			if ((currentCameraProfile().mode() == CameraProfile.Mode.THIRD_PERSON)
					&& (!camera().anyInterpolationIsStarted())) {
				camera().setPosition(avatar().cameraPosition());
				camera().setUpVector(avatar().upVector());
				camera().lookAt(avatar().target());
			}
			// We set the processing camera matrices from our remixlab.proscene.Camera
			setPProjectionMatrix();
			setPModelViewMatrix();

			if (frustumEquationsUpdateIsEnable())
				camera().updateFrustumEquations();

			setBackground();
			//in this mode on-screen visual hints should not be drawn
			if (gridIsDrawn())
				drawGrid(camera().sceneRadius());
			if (axisIsDrawn())
				drawAxis(camera().sceneRadius());
		}
	}

	/**
	 * This method should be called when using offscreen rendering 
	 * right before renderer.endDraw(). It currently does nothing though,
	 * just for code consistency. 
   */		
	public void endDraw() {
		beginOffScreenDrawingCalls--;
		
		if (beginOffScreenDrawingCalls != 0)
			throw new RuntimeException(
					"There should be exactly one beginDraw() call followed by a "
							+ "endDraw() and they cannot be nested. Check your implementation!");
	}
	
	// 4. Scene dimensions

	/**
	 * Returns the {@link PApplet#width} to {@link PApplet#height} aspect ratio of
	 * the processing display window.
	 */
	public float aspectRatio() {
		return (float) pg3d.width / (float) pg3d.height;
	}

	// 6. Display of visual hints and Display methods
	
	/**
	 * Draws a rectangle on the screen showing the region where a zoom operation
	 * is taking place.
	 */
	protected void drawZoomWindowHint() {
		float p1x = (float) dE.fCorner.getX();
		float p1y = (float) dE.fCorner.getY();
		float p2x = (float) dE.lCorner.getX();
		float p2y = (float) dE.lCorner.getY();
		beginScreenDrawing();
		pg3d.pushStyle();
		pg3d.stroke(255, 255, 255);
		pg3d.strokeWeight(2);
		pg3d.noFill();
		pg3d.beginShape();
		pg3d.vertex(xCoord(p1x), yCoord(p1y), zCoord());
		pg3d.vertex(xCoord(p2x), yCoord(p1y), zCoord());
		pg3d.vertex(xCoord(p2x), yCoord(p2y), zCoord());
		pg3d.vertex(xCoord(p1x), yCoord(p2y), zCoord());
		pg3d.endShape(CLOSE);
		pg3d.popStyle();
		endScreenDrawing();
	}

	/**
	 * Draws visual hint (a line on the screen) when a screen rotation is taking
	 * place.
	 */
	protected void drawScreenRotateLineHint() {
		float p1x = (float) dE.fCorner.getX();
		float p1y = (float) dE.fCorner.getY();
		Vector3D p2 = camera().projectedCoordinatesOf(camera().arcballReferencePoint());
		beginScreenDrawing();
		pg3d.pushStyle();
		pg3d.stroke(255, 255, 255);
		pg3d.strokeWeight(2);
		pg3d.noFill();
		pg3d.beginShape(LINE);
		pg3d.vertex(xCoord(p1x), yCoord(p1y), zCoord());
		pg3d.vertex(xCoord(p2.x), yCoord(p2.y), zCoord());
		pg3d.endShape();
		pg3d.popStyle();
		endScreenDrawing();
	}

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
	protected void drawArcballReferencePointHint() {
		Vector3D p = camera().projectedCoordinatesOf(camera().arcballReferencePoint());
		drawCross(p.x, p.y);
	}

	/**
	 * Draws all InteractiveFrames' selection regions: a shooter target
	 * visual hint of {@link remixlab.remixcam.core.InteractiveFrame#grabsMouseThreshold()} pixels size.
	 * 
	 * <b>Attention:</b> If the InteractiveFrame is part of a Camera path draws
	 * nothing.
	 * 
	 * @see #drawCameraPathSelectionHints()
	 */
	protected void drawSelectionHints() {
		for (MouseGrabbable mg : mouseGrabberPool.mouseGrabberPool()) {
			if(mg instanceof InteractiveFrame) {
				InteractiveFrame iF = (InteractiveFrame) mg;// downcast needed
				if (!iF.isInCameraPath()) {
					Vector3D center = camera().projectedCoordinatesOf(iF.position());
					if (mg.grabsMouse())
						drawShooterTarget(pg3d.color(0, 255, 0), center, (iF.grabsMouseThreshold() + 1), 2);
					else
						drawShooterTarget(pg3d.color(240, 240, 240), center, iF.grabsMouseThreshold(), 1);
				}
			}
		}
	}

	/**
	 * Draws the selection regions (a shooter target visual hint of
	 * {@link remixlab.remixcam.core.InteractiveFrame#grabsMouseThreshold()} pixels size) of all
	 * InteractiveFrames forming part of the Camera paths.
	 * 
	 * @see #drawSelectionHints()
	 */
	protected void drawCameraPathSelectionHints() {
		for (MouseGrabbable mg : mouseGrabberPool.mouseGrabberPool()) {
			if(mg instanceof InteractiveFrame) {
				InteractiveFrame iF = (InteractiveFrame) mg;// downcast needed
				if (iF.isInCameraPath()) {
					Vector3D center = camera().projectedCoordinatesOf(iF.position());
					if (mg.grabsMouse())
						drawShooterTarget(pg3d.color(0, 255, 255), center, (iF.grabsMouseThreshold() + 1), 2);
					else
						drawShooterTarget(pg3d.color(255, 255, 0), center, iF.grabsMouseThreshold(), 1);
				}
			}
		}
	}

	/**
	 * Convenience function that simply calls {@code
	 * drawCross(pg3d.color(255, 255, 255), px, py, 15, 3)}.
	 */
	public void drawCross(float px, float py) {
		drawCross(pg3d.color(255, 255, 255), px, py, 15, 3);
	}

	/**
	 * Draws a cross on the screen centered under pixel {@code (px, py)}, and edge
	 * of size {@code size}. {@code strokeWeight} defined the weight of the
	 * stroke.
	 * 
	 * @see #drawArcballReferencePointHint()
	 */
	public void drawCross(int color, float px, float py, float size, int strokeWeight) {
		if (isOffscreen()) {
			PApplet.println("Warning: Nothing drawn. Off-screen rendering disables screen rendering.");
			return;
		}
		beginScreenDrawing();
		pg3d.pushStyle();
		pg3d.stroke(color);
		pg3d.strokeWeight(strokeWeight);
		pg3d.noFill();
		pg3d.beginShape(LINES);
		pg3d.vertex(xCoord(px - size), yCoord(py), zCoord());
		pg3d.vertex(xCoord(px + size), yCoord(py), zCoord());
		pg3d.vertex(xCoord(px), yCoord(py - size), zCoord());
		pg3d.vertex(xCoord(px), yCoord(py + size), zCoord());
		pg3d.endShape();
		pg3d.popStyle();
		endScreenDrawing();
	}

	/**
	 * Draws a filled circle using screen coordinates.
	 * 
	 * @param color
	 *          Color used to fill the circle.
	 * @param center
	 *          Circle screen center.
	 * @param radius
	 *          Circle screen radius.
	 * 
	 * @see #beginScreenDrawing()
	 * @see #endScreenDrawing()
	 */
	public void drawFilledCircle(int color, Vector3D center, float radius) {
		if (isOffscreen()) {
			PApplet.println("Warning: Nothing drawn. Off-screen rendering disables screen rendering.");
			return;
		}
		float x = center.x;
		float y = center.y;
		float angle, x2, y2;
		beginScreenDrawing();
		pg3d.pushStyle();
		pg3d.noStroke();
		pg3d.fill(color);
		pg3d.beginShape(TRIANGLE_FAN);
		pg3d.vertex(xCoord(x), yCoord(y), zCoord());
		for (angle = 0.0f; angle <= TWO_PI; angle += 0.157f) {
			x2 = x + PApplet.sin(angle) * radius;
			y2 = y + PApplet.cos(angle) * radius;
			pg3d.vertex(xCoord(x2), yCoord(y2), zCoord());
		}
		pg3d.endShape();
		pg3d.popStyle();
		endScreenDrawing();
	}

	/**
	 * Draws a filled square using screen coordinates.
	 * 
	 * @param color
	 *          Color used to fill the square.
	 * @param center
	 *          Square screen center.
	 * @param edge
	 *          Square edge length.
	 * 
	 * @see #beginScreenDrawing()
	 * @see #endScreenDrawing()
	 */
	public void drawFilledSquare(int color, Vector3D center, float edge) {
		if (isOffscreen()) {
			PApplet.println("Warning: Nothing drawn. Off-screen rendering disables screen rendering.");
			return;
		}
		float x = center.x;
		float y = center.y;
		beginScreenDrawing();
		pg3d.pushStyle();
		pg3d.noStroke();
		pg3d.fill(color);
		pg3d.beginShape(QUADS);
		pg3d.vertex(xCoord(x - edge), yCoord(y + edge), zCoord());
		pg3d.vertex(xCoord(x + edge), yCoord(y + edge), zCoord());
		pg3d.vertex(xCoord(x + edge), yCoord(y - edge), zCoord());
		pg3d.vertex(xCoord(x - edge), yCoord(y - edge), zCoord());
		pg3d.endShape();
		pg3d.popStyle();
		endScreenDrawing();
	}

	/**
	 * Draws the classical shooter target on the screen.
	 * 
	 * @param color
	 *          Color of the target
	 * @param center
	 *          Center of the target on the screen
	 * @param length
	 *          Length of the target in pixels
	 * @param strokeWeight
	 *          Stroke weight
	 */
	public void drawShooterTarget(int color, Vector3D center, float length, int strokeWeight) {
		if (isOffscreen()) {
			PApplet.println("Warning: Nothing drawn. Off-screen rendering disables screen rendering.");
			return;
		}
		float x = center.x;
		float y = center.y;
		beginScreenDrawing();
		pg3d.pushStyle();

		pg3d.stroke(color);
		pg3d.strokeWeight(strokeWeight);
		pg3d.noFill();

		pg3d.beginShape();
		pg3d.vertex(xCoord((x - length)), yCoord((y - length) + (0.6f * length)),
				zCoord());
		pg3d.vertex(xCoord(x - length), yCoord(y - length), zCoord());
		pg3d.vertex(xCoord((x - length) + (0.6f * length)), yCoord((y - length)),
				zCoord());
		pg3d.endShape();

		pg3d.beginShape();
		pg3d.vertex(xCoord((x + length) - (0.6f * length)), yCoord(y - length),
				zCoord());
		pg3d.vertex(xCoord(x + length), yCoord(y - length), zCoord());
		pg3d.vertex(xCoord(x + length), yCoord((y - length) + (0.6f * length)),
				zCoord());
		pg3d.endShape();

		pg3d.beginShape();
		pg3d.vertex(xCoord(x + length), yCoord((y + length) - (0.6f * length)),
				zCoord());
		pg3d.vertex(xCoord(x + length), yCoord(y + length), zCoord());
		pg3d.vertex(xCoord((x + length) - (0.6f * length)), yCoord(y + length),
				zCoord());
		pg3d.endShape();

		pg3d.beginShape();
		pg3d.vertex(xCoord((x - length) + (0.6f * length)), yCoord(y + length),
				zCoord());
		pg3d.vertex(xCoord(x - length), yCoord(y + length), zCoord());
		pg3d.vertex(xCoord(x - length), yCoord((y + length) - (0.6f * length)),
				zCoord());
		pg3d.endShape();

		pg3d.popStyle();
		endScreenDrawing();

		drawCross(color, center.x, center.y, 0.6f * length, strokeWeight);
	}

	/**
	 * Computes the world coordinates of an screen object so that drawing can be
	 * done directly with 2D screen coordinates.
	 * <p>
	 * All screen drawing should be enclosed between {@link #beginScreenDrawing()}
	 * and {@link #endScreenDrawing()}. Then you can just begin drawing your
	 * screen shapes (defined between {@code PApplet.beginShape()} and {@code
	 * PApplet.endShape()}).
	 * <p>
	 * <b>Note:</b> The (x,y) vertex screen coordinates should be specified as:
	 * {@code vertex(xCoord(x), yCoord(y), Scene.zCoord())}.
	 * 
	 * @see #endScreenDrawing()
	 * @see #xCoord(float)
	 * @see #yCoord(float)
	 * @see #zCoord()
	 */
	public void beginScreenDrawing() {
		if (isOffscreen())
			throw new RuntimeException("Screen rendering is not allowed in off-screen rendering mode!");

		if (startCoordCalls != 0)
			throw new RuntimeException(
					"There should be exactly one startScreenCoordinatesSystem() call followed by a "
							+ "stopScreenCoordinatesSystem() and they cannot be nested. Check your implementation!");
		
		startCoordCalls++;

		float threshold = 0.03f;
		zC = camera().zNear() + threshold * (camera().zFar() - camera().zNear());
		if (camera().type() == Camera.Type.PERSPECTIVE) {
			halfWidthSpace = PApplet.tan(camera().horizontalFieldOfView() / 2) * zC;
			halfHeightSpace = PApplet.tan(camera().fieldOfView() / 2) * zC;
		} else {
			float wh[] = camera().getOrthoWidthHeight();
			halfWidthSpace = wh[0];
			halfHeightSpace = wh[1];
		}

		pg3d.pushMatrix();
		if (camera().frame() != null)
			// TODO check this replacement
			//camera().frame().applyTransformation(pg3d);
			applyTransformation(camera().frame());			
	}

	/**
	 * Ends screen drawing. See {@link #beginScreenDrawing()} for details.
	 * 
	 * @see #beginScreenDrawing()
	 * @see #xCoord(float)
	 * @see #yCoord(float)
	 * @see #zCoord()
	 */
	public void endScreenDrawing() {
		startCoordCalls--;
		if (startCoordCalls != 0)
			throw new RuntimeException(
					"There should be exactly one startScreenCoordinatesSystem() call followed by a "
							+ "stopScreenCoordinatesSystem() and they cannot be nested. Check your implementation!");

		pg3d.popMatrix();
	}

	/**
	 * Computes the {@code x} coordinate of the {@code px} screen coordinate.
	 * <p>
	 * This method is only useful when drawing directly on screen. It should be
	 * used in conjunction with {@link #beginScreenDrawing()} and
	 * {@link #endScreenDrawing()} (which may be consulted for details).
	 * 
	 * @see #beginScreenDrawing()
	 * @see #endScreenDrawing()
	 * @see #yCoord(float)
	 * @see #zCoord()
	 */
	public float xCoord(float px) {
		// translate screen origin to center
		px = px - (pg3d.width / 2);
		// normalize
		px = px / (pg3d.width / 2);
		return halfWidthSpace * px;
	}

	/**
	 * Computes the {@code y} coordinate of the {@code py} screen coordinate.
	 * <p>
	 * This method is only useful when drawing directly on screen. It should be
	 * used in conjunction with {@link #beginScreenDrawing()} and
	 * {@link #endScreenDrawing()} (which may be consulted for details).
	 * 
	 * @see #beginScreenDrawing()
	 * @see #endScreenDrawing()
	 * @see #xCoord(float)
	 * @see #zCoord()
	 */
	public float yCoord(float py) {
		// translate screen origin to center
		py = py - (pg3d.height / 2);
		// normalize
		py = py / (pg3d.height / 2);
		return halfHeightSpace * py;
	}

	/**
	 * Returns the {@code z} coordinate needed when drawing objects directly on
	 * screen.
	 * <p>
	 * This should be used in conjunction with {@link #beginScreenDrawing()} and
	 * {@link #endScreenDrawing()} (which may be consulted for details).
	 * 
	 * @see #beginScreenDrawing()
	 * @see #endScreenDrawing()
	 * @see #yCoord(float)
	 * @see #zCoord()
	 */
	public float zCoord() {
		return -zC;
	}

	// 7. Camera profiles

	/**
	 * Internal method that defines the default camera profiles: WHEELED_ARCBALL
	 * and FIRST_PERSON.
	 */
	private void initDefaultCameraProfiles() {
		cameraProfileMap = new HashMap<String, CameraProfile>();
		cameraProfileNames = new ArrayList<String>();
		currentCameraProfile = null;
		// register here the default profiles
		//registerCameraProfile(new CameraProfile(this, "ARCBALL", CameraProfile.Mode.ARCBALL));
		registerCameraProfile( new CameraProfile(this, "WHEELED_ARCBALL", CameraProfile.Mode.WHEELED_ARCBALL) );
		registerCameraProfile( new CameraProfile(this, "FIRST_PERSON", CameraProfile.Mode.FIRST_PERSON) );
		//setCurrentCameraProfile("ARCBALL");
		setCurrentCameraProfile("WHEELED_ARCBALL");
	}

	/**
	 * Registers a camera profile. Returns true if succeeded. If there's a
	 * registered camera profile with the same name, registration will fail. 
	 * <p>
	 * <b>Attention:</b> This method doesn't make current {@code cp}. For that call
	 * {@link #setCurrentCameraProfile(CameraProfile)}.
	 * 
	 * @param cp camera profile
	 * 
	 * @see #setCurrentCameraProfile(CameraProfile)
	 * @see #unregisterCameraProfile(CameraProfile) 
	 */
	public boolean registerCameraProfile(CameraProfile cp) {
		// if(!isCameraProfileRegistered(cp)) {
		if (cp == null)
			return false;
		if (!isCameraProfileRegistered(cp)) {
			cameraProfileNames.add(cp.name());
			cameraProfileMap.put(cp.name(), cp);
			return true;
		}
		return false;
	}

	/**
	 * Convenience function that simply returns {@code unregisterCameraProfile(cp.name())}.
	 */
	public boolean unregisterCameraProfile(CameraProfile cp) {
		return unregisterCameraProfile(cp.name());
	}

	/**
	 * Unregisters the given camera profile by its name. Returns true if succeeded.
	 * Registration will fail in two cases: no camera profile is registered under
	 * the provided name, or the camera profile is the only registered camera profile which
	 * mode is different than THIRD_PERSON.
	 * <p>
	 * The last condition above guarantees that there should always be registered at least
	 * one camera profile which mode is different than THIRD_PERSON. 
	 * 
	 * @param cp camera profile
	 * @return true if succeeded
	 */
	public boolean unregisterCameraProfile(String cp) {
		if (!isCameraProfileRegistered(cp))
			return false;

		CameraProfile cProfile = cameraProfile(cp);
		int instancesDifferentThanThirdPerson = 0;

		for (CameraProfile camProfile : cameraProfileMap.values())
			if (camProfile.mode() != CameraProfile.Mode.THIRD_PERSON)
				instancesDifferentThanThirdPerson++;

		if ((cProfile.mode() != CameraProfile.Mode.THIRD_PERSON)
				&& (instancesDifferentThanThirdPerson == 1))
			return false;

		if (isCurrentCameraProfile(cp))
			nextCameraProfile();

		if (cameraProfileNames.remove(cp)) {
			cameraProfileMap.remove(cp);
			return true;
		}

		return false;
	}

	/**
	 * Returns the camera profile which name matches the one provided.
	 * Returns null if there's no camera profile registered by this name.
	 * 
	 * @param name camera profile name
	 * @return camera profile object
	 */
	public CameraProfile cameraProfile(String name) {
		return cameraProfileMap.get(name);
	}
	
	/**
	 * Returns an array of the camera profile objects that are currently
	 * registered at the Scene.
	 */
	public CameraProfile [] getCameraProfiles() {		
		return cameraProfileMap.values().toArray(new CameraProfile[0]);
	}

	/**
	 * Returns true the given camera profile is currently registered.
	 */
	public boolean isCameraProfileRegistered(CameraProfile cp) {
		return cameraProfileMap.containsValue(cp);
	}

	/**
	 * Returns true if currently there's a camera profile registered by
	 * the given name.
	 */
	public boolean isCameraProfileRegistered(String name) {
		return cameraProfileMap.containsKey(name);
	}

	/**
	 * Returns the current camera profile object. Never null.
	 */
	public CameraProfile currentCameraProfile() {
		return currentCameraProfile;
	}

	/**
	 * Returns true if the {@link #currentCameraProfile()} matches 
	 * the one by the given name.
	 */
	boolean isCurrentCameraProfile(String cp) {
		return isCurrentCameraProfile(cameraProfileMap.get(cp));
	}

	/**
	 * Returns true if the {@link #currentCameraProfile()} matches 
	 * the one given.
	 */
	boolean isCurrentCameraProfile(CameraProfile cp) {
		return currentCameraProfile() == cp;
	}

	/**
	 * Set current the given camera profile. Returns true if succeeded.
	 * <p>
	 * Registers first the given camera profile if it is not registered.
	 */
	public boolean setCurrentCameraProfile(CameraProfile cp) {
		if (cp == null) {
			return false;
		}
		if (!isCameraProfileRegistered(cp))
			if (!registerCameraProfile(cp))
				return false;

		return setCurrentCameraProfile(cp.name());
	}
	
	/**
	 * Set current the camera profile associated to the given name.
	 * Returns true if succeeded.
	 * <p>
	 * This method triggers smooth transition animations
	 * when switching between camera profile modes.
	 */
	public boolean setCurrentCameraProfile(String cp) {
		CameraProfile camProfile = cameraProfileMap.get(cp);
		if (camProfile == null)
			return false;
		if ((camProfile.mode() == CameraProfile.Mode.THIRD_PERSON) && (avatar() == null))
			return false;
		else {
			if (camProfile.mode() == CameraProfile.Mode.THIRD_PERSON) {
				setDrawInteractiveFrame();
				setCameraType(Camera.Type.PERSPECTIVE);
				if (avatarIsInteractiveDrivableFrame)
					((InteractiveDrivableFrame) avatar()).removeFromMouseGrabberPool();
				camera().frame().updateFlyUpVector();// ?
				camera().frame().stopSpinning();
				if (avatarIsInteractiveDrivableFrame) {
					((InteractiveDrivableFrame) (avatar())).updateFlyUpVector();
					((InteractiveDrivableFrame) (avatar())).stopSpinning();
				}
				// perform small animation ;)
				if (camera().anyInterpolationIsStarted())
					camera().stopAllInterpolations();
				Camera cm = camera().getCopy();
				cm.setPosition(avatar().cameraPosition());
				cm.setUpVector(avatar().upVector());
				cm.lookAt(avatar().target());
				camera().interpolateTo(cm.frame());
				currentCameraProfile = camProfile;
			} else {
				camera().frame().updateFlyUpVector();
				camera().frame().stopSpinning();
				
				if(currentCameraProfile != null)
					if (currentCameraProfile.mode() == CameraProfile.Mode.THIRD_PERSON)
						camera().interpolateToFitScene();
        
				currentCameraProfile = camProfile;        
				
				setDrawInteractiveFrame(false);
				if (avatarIsInteractiveDrivableFrame)
					((InteractiveDrivableFrame) avatar()).addInMouseGrabberPool();
			}
			return true;
		}
	}

	/**
	 * Sets the next registered camera profile as current.
	 * <p>
	 * Camera profiles are ordered by their registration order.
	 */
	@Override
	public void nextCameraProfile() {
		int currentCameraProfileIndex = cameraProfileNames
				.indexOf(currentCameraProfile().name());
		nextCameraProfile(++currentCameraProfileIndex);
	}

	/**
	 * Internal use. Used by {@link #nextCameraProfile()}.
	 */
	private void nextCameraProfile(int index) {
		if (!cameraProfileNames.isEmpty()) {
			if (index == cameraProfileNames.size())
				index = 0;

			if (!setCurrentCameraProfile(cameraProfileNames.get(index)))
				nextCameraProfile(++index);
			// debug:
			else
				PApplet.println("Camera profile changed to: "
						+ cameraProfileNames.get(index));
		}
	}

	// 8. Keyboard customization

	/**
	 * Parses the sketch to find if any KeyXxxx method has been implemented. If
	 * this is the case, print a warning message telling the user what to do to
	 * avoid possible conflicts with proscene.
	 * <p>
	 * The methods sought are: {@code keyPressed}, {@code keyReleased}, and
	 * {@code keyTyped}.
	 */
	protected void parseKeyXxxxMethods() {
		boolean foundKP = true;
		boolean foundKR = true;
		boolean foundKT = true;

		try {
			parent.getClass().getDeclaredMethod("keyPressed");
		} catch (SecurityException e) {
			e.printStackTrace();
			foundKP = false;
		} catch (NoSuchMethodException e) {
			foundKP = false;
		}

		try {
			parent.getClass().getDeclaredMethod("keyReleased");
		} catch (SecurityException e) {
			e.printStackTrace();
			foundKR = false;
		} catch (NoSuchMethodException e) {
			foundKR = false;
		}

		try {
			parent.getClass().getDeclaredMethod("keyTyped");
		} catch (SecurityException e) {
			e.printStackTrace();
			foundKT = false;
		} catch (NoSuchMethodException e) {
			foundKT = false;
		}

		if ( (foundKP || foundKR || foundKT) && keyboardIsHandled() ) {
			// if( (foundKP || foundKR || foundKT) &&
			// (!parent.getClass().getName().equals("remixlab.proscene.Viewer")) ) {
			PApplet.println("Warning: it seems that you have implemented some KeyXxxxMethod in your sketch. You may temporarily disable proscene " +
					"keyboard handling with Scene.disableKeyboardHandling() (you can re-enable it later with Scene.enableKeyboardHandling()).");
		}
	}

	/**
	 * Returns {@code true} if the keyboard is currently being handled by proscene
	 * and {@code false} otherwise. Set keyboard handling with
	 * {@link #enableMouseHandling(boolean)}.
	 * <p>
	 * Keyboard handling is enable by default.
	 */
	public boolean keyboardIsHandled() {
		return keyboardHandling;
	}

	/**
	 * Toggles the state of {@link #keyboardIsHandled()}
	 */
	public void toggleKeyboardHandling() {
		enableKeyboardHandling(!keyboardHandling);
	}

	/**
	 * Enables or disables proscene keyboard handling according to {@code enable}
	 * 
	 * @see #keyboardIsHandled()
	 */
	public void enableKeyboardHandling(boolean enable) {
		if (enable)
			enableKeyboardHandling();
		else
			disableKeyboardHandling();
	}

	/**
	 * Enables Proscene keyboard handling.
	 * 
	 * @see #keyboardIsHandled()
	 * @see #enableMouseHandling()
	 * @see #disableKeyboardHandling()
	 */
	public void enableKeyboardHandling() {
		keyboardHandling = true;
		parent.registerKeyEvent(dE);
	}

	/**
	 * Disables Proscene keyboard handling.
	 * 
	 * @see #keyboardIsHandled()
	 */
	public void disableKeyboardHandling() {
		keyboardHandling = false;
		parent.unregisterKeyEvent(dE);
	}

	/**
	 * Sets global default keyboard shortcuts and the default key-frame shortcut keys.
	 * <p>
	 * Default global keyboard shortcuts are:
	 * <p>
	 * <ul>
	 * <li><b>'a'</b>: {@link remixlab.proscene.Scene.KeyboardAction#DRAW_AXIS}.
	 * <li><b>'e'</b>: {@link remixlab.proscene.Scene.KeyboardAction#CAMERA_TYPE}.
	 * <li><b>'g'</b>: {@link remixlab.proscene.Scene.KeyboardAction#DRAW_GRID}.
	 * <li><b>'h'</b>: {@link remixlab.proscene.Scene.KeyboardAction#GLOBAL_HELP}
	 * <li><b>'H'</b>: {@link remixlab.proscene.Scene.KeyboardAction#CURRENT_CAMERA_PROFILE_HELP}
	 * <li><b>'r'</b>: {@link remixlab.proscene.Scene.KeyboardAction#EDIT_CAMERA_PATH}.
	 * <li><b>space bar</b>: {@link remixlab.proscene.Scene.KeyboardAction#CAMERA_PROFILE}.
	 * </ul> 
	 * <p>
	 * Default key-frame shortcuts keys are:
	 * <ul>
	 * <li><b>'[1..5]'</b>: Play path [1..5]. 
	 * <li><b>'CTRL'+'[1..5]'</b>: Add key-frame to path [1..5].   
	 * <li><b>'ALT'+'[1..5]'</b>: Remove path [1..5].
	 * </ul> 
	 */
	public void setDefaultShortcuts() {
		// D e f a u l t s h o r t c u t s		
		setShortcut('a', KeyboardAction.DRAW_AXIS);
		setShortcut('g', KeyboardAction.DRAW_GRID);
		setShortcut(' ', KeyboardAction.CAMERA_PROFILE);
		setShortcut('e', KeyboardAction.CAMERA_TYPE);		
		setShortcut('h', KeyboardAction.GLOBAL_HELP);
		setShortcut('H', KeyboardAction.CURRENT_CAMERA_PROFILE_HELP);
		setShortcut('r', KeyboardAction.EDIT_CAMERA_PATH);

		// K e y f r a m e s s h o r t c u t k e y s
		setAddKeyFrameKeyboardModifier(Modifier.CTRL);
		setDeleteKeyFrameKeyboardModifier(Modifier.ALT);
		setPathKey('1', 1);
		setPathKey('2', 2);
		setPathKey('3', 3);
		setPathKey('4', 4);
		setPathKey('5', 5);
	}

	/**
	 * Associates key-frame interpolator path to key. High-level version
	 * of {@link #setPathKey(Integer, Integer)}.
	 *  
	 * @param key character (internally converted to a key coded) defining the shortcut
	 * @param path key-frame interpolator path
	 * 
	 * @see #setPathKey(Integer, Integer)
	 */
	public void setPathKey(Character key, Integer path) {
		setPathKey(DesktopEvents.getVKey(key), path);
	}
	
	/**
	 * Associates key-frame interpolator path to the given virtual key. Low-level version
	 * of {@link #setPathKey(Character, Integer)}.
	 * 
	 * @param vKey shortcut
	 * @param path key-frame interpolator path
	 * 
	 * @see #setPathKey(Character, Integer)
	 */
	public void setPathKey(Integer vKey, Integer path) {
		if ( isPathKeyInUse(vKey) ) {
			Integer p = path(vKey);
			PApplet.println("Warning: overwritting path key which was previously binded to path " + p);
		}
		pathKeys.setBinding(vKey, path);
	}

	/**
	 * Returns the key-frame interpolator path associated with key. High-level version
	 * of {@link #path(Integer)}.
	 * 
	 * @param key character (internally converted to a key coded) defining the shortcut
	 * 
	 * @see #path(Integer)
	 */
	public Integer path(Character key) {
		return path(DesktopEvents.getVKey(key));
	}
	
	/**
	 * Returns the key-frame interpolator path associated with key. Low-level version
	 * of {@link #path(Character)}.
	 * 
	 * @param vKey shortcut
	 * 
	 * @see #path(Character)
	 */
	public Integer path(Integer vKey) {
		return pathKeys.binding(vKey);
	}

	/**
	 * Removes the key-frame interpolator shortcut. High-level version
	 * of {@link #removePathKey(Integer)}.
	 * 
	 * @param key character (internally converted to a key coded) defining the shortcut
	 * 
	 * @see #removePathKey(Integer)
	 */
	public void removePathKey(Character key) {
		removePathKey(DesktopEvents.getVKey(key));
	}
	
	/**
	 * Removes the key-frame interpolator shortcut. Low-level version
	 * of {@link #removePathKey(Character)}.
	 * 
	 * @param vKey shortcut
	 * 
	 * @see #removePathKey(Character)
	 */
	public void removePathKey(Integer vKey) {
		pathKeys.removeBinding(vKey);
	}
	
	/**
	 * Returns true if the given key binds a key-frame interpolator path. High-level version
	 * of {@link #isPathKeyInUse(Integer)}.
	 * 
	 * @param key character (internally converted to a key coded) defining the shortcut
	 * 
	 * @see #isPathKeyInUse(Integer)
	 */
	public boolean isPathKeyInUse(Character key) {
		return isPathKeyInUse(DesktopEvents.getVKey(key));
	}
	
	/**
	 * Returns true if the given virtual key binds a key-frame interpolator path. Low-level version
	 * of {@link #isPathKeyInUse(Character)}.
	 * 
	 * @param vKey shortcut
	 * 
	 * @see #isPathKeyInUse(Character)
	 */
	public boolean isPathKeyInUse(Integer vKey) {
		return pathKeys.isShortcutInUse(vKey);
	}
	
	/**
	 * Returns the modifier key needed to play the key-frame interpolator paths.
	 * 
	 * @see #setAddKeyFrameKeyboardModifier(Modifier)
	 */
	public Modifier addKeyFrameKeyboardModifier() {
		return addKeyFrameKeyboardModifier;
	}
	
	/**
	 * Returns the modifier key needed to delete the key-frame interpolator paths.
	 * 
	 * @see #setDeleteKeyFrameKeyboardModifier(Modifier)
	 */
	public Modifier deleteKeyFrameKeyboardModifier() {
		return deleteKeyFrameKeyboardModifier;
	}

	/**
	 * Sets the modifier key needed to play the key-frame interpolator paths.
	 * 
	 * @param modifier
	 * 
	 * @see #addKeyFrameKeyboardModifier()
	 */
	public void setAddKeyFrameKeyboardModifier(Modifier modifier) {
		addKeyFrameKeyboardModifier = modifier;
	}

	/**
	 * Sets the modifier key needed to delete the key-frame interpolator paths.
	 * 
	 * @param modifier
	 * 
	 * @see #deleteKeyFrameKeyboardModifier()
	 */
	public void setDeleteKeyFrameKeyboardModifier(Modifier modifier) {
		deleteKeyFrameKeyboardModifier = modifier;
	}

  /**
   * Defines a global keyboard shortcut to bind the given action.
   * 
   * @param key shortcut
   * @param action keyboard action
   */
	public void setShortcut(Character key, KeyboardAction action) {
		if ( isKeyInUse(key) ) {
			KeyboardAction a = shortcut(key);
			PApplet.println("Warning: overwritting shortcut which was previously binded to " + a);
		}
		gProfile.setBinding(new KeyboardShortcut(key), action);
	}
	
  /**
   * Defines a global keyboard shortcut to bind the given action. High-level version
   * of {@link #setShortcut(Integer, Integer, KeyboardAction)}.
   * 
   * @param mask modifier mask defining the shortcut
   * @param key character (internally converted to a coded key) defining the shortcut
   * @param action keyboard action
   * 
   * @see #setShortcut(Integer, Integer, KeyboardAction)
   */
	public void setShortcut(Integer mask, Character key, KeyboardAction action) {
		setShortcut(mask, DesktopEvents.getVKey(key), action);
	}
	
  /**
   * Defines a global keyboard shortcut to bind the given action. High-level version
   * of {@link #setShortcut(Integer, Character, KeyboardAction)}.
   * 
   * @param mask modifier mask defining the shortcut
   * @param vKey coded key defining the shortcut
   * @param action keyboard action
   * 
   * @see #setShortcut(Integer, Character, KeyboardAction)
   */
	public void setShortcut(Integer mask, Integer vKey, KeyboardAction action) {
		if ( isKeyInUse(mask, vKey) ) {
			KeyboardAction a = shortcut(mask, vKey);
			PApplet.println("Warning: overwritting shortcut which was previously binded to " + a);
		}
		gProfile.setBinding(new KeyboardShortcut(mask, vKey), action);
	}

	/**
	 * Defines a global keyboard shortcut to bind the given action.
	 * 
	 * @param vKey coded key defining the shortcut
	 * @param action keyboard action
	 */
	public void setShortcut(Integer vKey, KeyboardAction action) {
		if ( isKeyInUse(vKey) ) {
			KeyboardAction a = shortcut(vKey);
			PApplet.println("Warning: overwritting shortcut which was previously binded to " + a);
		}
		gProfile.setBinding(new KeyboardShortcut(vKey), action);
	}

	/**
	 * Removes all global keyboard shortcuts.
	 */
	public void removeAllShortcuts() {
		gProfile.removeAllBindings();
	}
	
	/**
	 * Removes the global keyboard shortcut.
	 * 
	 * @param key shortcut
	 */
	public void removeShortcut(Character key) {
		gProfile.removeBinding(new KeyboardShortcut(key));
	}
	
  /**
   * Removes the global keyboard shortcut. High-level version
   * of {@link #removeShortcut(Integer, Integer)}.
   * 
   * @param mask modifier mask defining the shortcut
   * @param key character (internally converted to a coded key) defining the shortcut
   * 
   * @see #removeShortcut(Integer, Integer)
   */
	public void removeShortcut(Integer mask, Character key) {
		removeShortcut(mask, DesktopEvents.getVKey(key));
	}

	/**
   * Removes the global keyboard shortcut. Low-level version
   * of {@link #removeShortcut(Integer, Character)}.
   * 
   * @param mask modifier mask defining the shortcut
   * @param vKey virtual coded-key defining the shortcut
   * 
   * @see #removeShortcut(Integer, Character)
   */
	public void removeShortcut(Integer mask, Integer vKey) {
		gProfile.removeBinding(new KeyboardShortcut(mask, vKey));
	}

	/**
	 * Removes the global keyboard shortcut.
	 * 
	 * @param vKey virtual coded-key defining the shortcut
	 */
	public void removeShortcut(Integer vKey) {
		gProfile.removeBinding(new KeyboardShortcut(vKey));
	}
	
	/**
	 * Returns the action that is binded to the given global keyboard shortcut.
	 * 
	 * @param key shortcut
	 */
	public KeyboardAction shortcut(Character key) {
		return gProfile.binding(new KeyboardShortcut(key));
	}
	
  /**
   * Returns the action that is binded to the given global keyboard shortcut.
   * High-level version of {@link #shortcut(Integer, Integer)}.
   * 
   * @param mask modifier mask defining the shortcut
   * @param key character (internally converted to a coded key) defining the shortcut
   * 
   * @see #shortcut(Integer, Integer)
   */
	public KeyboardAction shortcut(Integer mask, Character key) {
		return shortcut(mask, DesktopEvents.getVKey(key));
	}

	/**
   * Returns the action that is binded to the given global keyboard shortcut.
   * Low-level version of {@link #shortcut(Integer, Character)}.
   * 
   * @param mask modifier mask defining the shortcut
   * @param vKey virtual coded-key defining the shortcut
   * 
   * @see #shortcut(Integer, Character)
   */
	public KeyboardAction shortcut(Integer mask, Integer vKey) {
		return gProfile.binding(new KeyboardShortcut(mask, vKey));
	}

	/**
	 * Returns the action that is binded to the given global keyboard shortcut.
	 * 
	 * @param vKey virtual coded-key defining the shortcut
	 */
	public KeyboardAction shortcut(Integer vKey) {
		return gProfile.binding(new KeyboardShortcut(vKey));
	}

	/**
	 * Returns true if the given global keyboard shortcut binds an action.
	 * 
	 * @param key shortcut
	 */
	public boolean isKeyInUse(Character key) {
		return gProfile.isShortcutInUse(new KeyboardShortcut(key));
	}
	
  /**
   * Returns true if the given global keyboard shortcut binds an action.
   * High-level version of {@link #isKeyInUse(Integer, Integer)}.
   * 
   * @param mask modifier mask defining the shortcut
   * @param key character (internally converted to a coded key) defining the shortcut
   * 
   * @see #isKeyInUse(Integer, Integer)
   */
	public boolean isKeyInUse(Integer mask, Character key) {
		return isKeyInUse(mask, DesktopEvents.getVKey(key));
	}
	
	/**
   * Returns true if the given global keyboard shortcut binds an action.
   * Low-level version of {@link #isKeyInUse(Integer, Character)}.
   * 
   * @param mask modifier mask defining the shortcut
   * @param vKey virtual coded-key defining the shortcut
   * 
   * @see #isKeyInUse(Integer, Character)
   */
	public boolean isKeyInUse(Integer mask, Integer vKey) {
		return gProfile.isShortcutInUse(new KeyboardShortcut(mask, vKey));
	}
	
	/**
	 * Returns true if the given global keyboard shortcut binds an action.
	 * 
	 * @param vKey virtual coded-key defining the shortcut
	 */
	public boolean isKeyInUse(Integer vKey) {
		return gProfile.isShortcutInUse(new KeyboardShortcut(vKey));
	}

	/**
	 * Returns true if there is a global keyboard shortcut for the given action.
	 */
	public boolean isActionBinded(KeyboardAction action) {
		return gProfile.isActionMapped(action);
	}

	/**
	 * Internal method. Handles the different global keyboard actions.
	 */
	protected void handleKeyboardAction(KeyboardAction id) {
		super.handleKeyboardAction(id, new Point(parent.mouseX, parent.mouseY));
	}

	/**
	 * Internal method. Handles the different camera keyboard actions.
	 */
	protected void handleCameraKeyboardAction(CameraKeyboardAction id) {
		super.handleCameraKeyboardAction(id, new Point(parent.mouseX, parent.mouseY));
	}

	/**
	 * Convenience function that simply calls {@code displayGlobalHelp(true)}.
	 * 
	 * @see #displayGlobalHelp(boolean)
	 */
	@Override
	public void displayGlobalHelp() {
		displayGlobalHelp(true);
	}
	
	/**
	 * Displays global keyboard bindings.
	 * 
	 * @param onConsole if this flag is true displays the help on console.
	 * Otherwise displays it on the applet
	 * 
	 * @see #displayGlobalHelp()
	 */
	public void displayGlobalHelp(boolean onConsole) {
		if (onConsole)
			PApplet.println(globalHelp());
		else { //on applet
			pg3d.textFont(parent.createFont("Arial", 12));
			pg3d.textMode(SCREEN);
			pg3d.fill(0,255,0);
			pg3d.textLeading(20);
			pg3d.text(globalHelp(), 10, 10, (pg3d.width-20), (pg3d.height-20));
		}
	}
	
	/**
	 * Returns a String with the global keyboard bindings.
	 * 
	 * @see #displayGlobalHelp()
	 */
	public String globalHelp() {
		String description = new String();
		description += "GLOBAL keyboard shortcuts\n";
		for (Entry<KeyboardShortcut, KeyboardAction> entry : gProfile.map.entrySet()) {			
			Character space = ' ';
			if (!entry.getKey().description().equals(space.toString())) 
				description += entry.getKey().description() + " -> " + entry.getValue().description() + "\n";
			else
				description += "space_bar" + " -> " + entry.getValue().description() + "\n";
		}
		
		for (Entry<Integer, Integer> entry : pathKeys.map.entrySet())
			description += DesktopEvents.getKeyText(entry.getKey()) + " -> plays camera path " + entry.getValue().toString() + "\n";
		description += DesktopEvents.getModifiersExText(addKeyFrameKeyboardModifier.ID) + " + one of the above keys -> adds keyframe to the camera path \n";
		description += DesktopEvents.getModifiersExText(deleteKeyFrameKeyboardModifier.ID) + " + one of the above keys -> deletes the camera path \n";
		
		return description;		
	}
	
	/**
	 * Convenience function that simply calls {@code displayCurrentCameraProfileHelp(true)}.
	 * 
	 * @see #displayCurrentCameraProfileHelp(boolean)
	 */
	@Override
	public void displayCurrentCameraProfileHelp() {
		displayCurrentCameraProfileHelp(true);
	}
	
	/**
	 * Displays the {@link #currentCameraProfile()} bindings.
	 * 
	 * @param onConsole if this flag is true displays the help on console.
	 * Otherwise displays it on the applet
	 * 
	 * @see #displayCurrentCameraProfileHelp()
	 */
	public void displayCurrentCameraProfileHelp(boolean onConsole) {
		if (onConsole)
			PApplet.println(currentCameraProfileHelp());
		else { //on applet
			pg3d.textFont(parent.createFont("Arial", 12));
			pg3d.textMode(SCREEN);
			pg3d.fill(0,255,0);
			pg3d.textLeading(20);
			pg3d.text(currentCameraProfileHelp(), 10, 10, (pg3d.width-20), (pg3d.height-20));			
		}
	}
	
	/**
	 * Returns a String with the {@link #currentCameraProfile()} keyboard and mouse bindings.
	 * 
	 * @see remixlab.proscene.CameraProfile#cameraMouseBindingsDescription()
	 * @see remixlab.proscene.CameraProfile#frameMouseBindingsDescription()
	 * @see remixlab.proscene.CameraProfile#mouseClickBindingsDescription()
	 * @see remixlab.proscene.CameraProfile#keyboardShortcutsDescription()
	 * @see remixlab.proscene.CameraProfile#cameraWheelBindingsDescription()
	 * @see remixlab.proscene.CameraProfile#frameWheelBindingsDescription()
	 */
	public String currentCameraProfileHelp() {
		String description = new String();
		description += currentCameraProfile().name() + " camera profile keyboard shortcuts and mouse bindings\n";
		int index = 1;
		if( currentCameraProfile().keyboardShortcutsDescription().length() != 0 ) {
			description += index + ". " + "Keyboard shortcuts\n";
			description += currentCameraProfile().keyboardShortcutsDescription();
			index++;
		}
		if( currentCameraProfile().cameraMouseBindingsDescription().length() != 0 ) {
			description += index + ". " + "Camera mouse bindings\n";
			description += currentCameraProfile().cameraMouseBindingsDescription();
			index++;
		}
		if( currentCameraProfile().mouseClickBindingsDescription().length() != 0 ) {
			description += index + ". " + "Mouse click bindings\n";
			description += currentCameraProfile().mouseClickBindingsDescription();
			index++;
		}
		if( currentCameraProfile().frameMouseBindingsDescription().length() != 0 ) {
			description += index + ". " + "Interactive frame mouse bindings\n";
			description += currentCameraProfile().frameMouseBindingsDescription();
			index++;
		}
		if( currentCameraProfile().cameraWheelBindingsDescription().length() != 0 ) {
			description += index + ". " + "Camera mouse wheel bindings\n";
			description += currentCameraProfile().cameraWheelBindingsDescription();
			index++;
		}
		if( currentCameraProfile().frameWheelBindingsDescription().length() != 0 ) {
			description += index + ". " + "Interactive frame mouse wheel bindings\n";
			description += currentCameraProfile().frameWheelBindingsDescription();
			index++;
		}
		return description;
	}

	// 9. Mouse customization

	/**
	 * Parses the sketch to find if any mouseXxxx method has been implemented. If
	 * this is the case, print a warning message telling the user what to do to
	 * avoid possible conflicts with proscene.
	 * <p>
	 * The methods sought are: {@code mouseDragged}, {@code mouseMoved}, {@code
	 * mouseReleased}, {@code mousePressed}, and {@code mouseClicked}.
	 */
	protected void parseMouseXxxxMethods() {
		boolean foundMD = true;
		boolean foundMM = true;
		boolean foundMR = true;
		boolean foundMP = true;
		boolean foundMC = true;

		try {
			parent.getClass().getDeclaredMethod("mouseDragged");
		} catch (SecurityException e) {
			e.printStackTrace();
			foundMD = false;
		} catch (NoSuchMethodException e) {
			foundMD = false;
		}

		try {
			parent.getClass().getDeclaredMethod("mouseMoved");
		} catch (SecurityException e) {
			e.printStackTrace();
			foundMM = false;
		} catch (NoSuchMethodException e) {
			foundMM = false;
		}

		try {
			parent.getClass().getDeclaredMethod("mouseReleased");
		} catch (SecurityException e) {
			e.printStackTrace();
			foundMR = false;
		} catch (NoSuchMethodException e) {
			foundMR = false;
		}

		try {
			parent.getClass().getDeclaredMethod("mousePressed");
		} catch (SecurityException e) {
			e.printStackTrace();
			foundMP = false;
		} catch (NoSuchMethodException e) {
			foundMP = false;
		}

		try {
			parent.getClass().getDeclaredMethod("mouseClicked");
		} catch (SecurityException e) {
			e.printStackTrace();
			foundMC = false;
		} catch (NoSuchMethodException e) {
			foundMC = false;
		}

		if ( (foundMD || foundMM || foundMR || foundMP || foundMC) && mouseIsHandled() ) {			
			PApplet.println("Warning: it seems that you have implemented some mouseXxxxMethod in your sketch. You may temporarily disable proscene " +
			"mouse handling with Scene.disableMouseHandling() (you can re-enable it later with Scene.enableMouseHandling()).");
		}
	}

	/**
	 * Returns {@code true} if the mouse is currently being handled by proscene and
	 * {@code false} otherwise. Set mouse handling with
	 * {@link #enableMouseHandling(boolean)}.
	 * <p>
	 * Mouse handling is enable by default.
	 */
	public boolean mouseIsHandled() {
		return mouseHandling;
	}

	/**
	 * Toggles the state of {@link #mouseIsHandled()}
	 */
	public void toggleMouseHandling() {
		enableMouseHandling(!mouseHandling);
	}

	/**
	 * Enables or disables proscene mouse handling according to {@code enable}
	 * 
	 * @see #mouseIsHandled()
	 */
	public void enableMouseHandling(boolean enable) {
		if (enable)
			enableMouseHandling();
		else
			disableMouseHandling();
	}

	/**
	 * Enables Proscene mouse handling.
	 * 
	 * @see #mouseIsHandled()
	 * @see #disableMouseHandling()
	 * @see #enableKeyboardHandling()
	 */
	public void enableMouseHandling() {
		mouseHandling = true;
		parent.registerMouseEvent(dE);
	}

	/**
	 * Disables Proscene mouse handling.
	 * 
	 * 
	 * @see #mouseIsHandled()
	 */
	public void disableMouseHandling() {
		mouseHandling = false;
		parent.unregisterMouseEvent(dE);
	}

	/**
	 * Internal method. Handles the different mouse click actions.
	 */
	protected void handleClickAction(ClickAction action) {
		super.handleClickAction(action, new Point(parent.mouseX, parent.mouseY));
	}	

	// 10. Draw method registration

	/**
	 * Attempt to add a 'draw' handler method to the Scene. The default event
	 * handler is a method that returns void and has one single Scene parameter.
	 * 
	 * @param obj
	 *          the object to handle the event
	 * @param methodName
	 *          the method to execute in the object handler class
	 */
	public void addDrawHandler(Object obj, String methodName) {
		try {
			drawHandlerMethod = obj.getClass().getMethod(methodName, new Class[] { Scene.class });
			drawHandlerObject = obj;
			drawHandlerMethodName = methodName;
		} catch (Exception e) {
			  PApplet.println("Something went wrong when registering your " + methodName + " method");
			  e.printStackTrace();
		}
	}

	/**
	 * Unregisters the 'draw' handler method (if any has previously been added to
	 * the Scene).
	 * 
	 * @see #addDrawHandler(Object, String)
	 */
	public void removeDrawHandler() {
		drawHandlerMethod = null;
		drawHandlerObject = null;
		drawHandlerMethodName = null;
	}

	/**
	 * Returns {@code true} if the user has registered a 'draw' handler method to
	 * the Scene and {@code false} otherwise.
	 */
	public boolean hasRegisteredDrawHandler() {
		if (drawHandlerMethodName == null)
			return false;
		return true;
	}
	
	// 11. Animation
	
	/**
	 * Returns the target frame rate.
	 * 
	 * @see #setFrameRate(float, boolean)
	 */
	public float frameRate() {
		return targetFrameRate;
	}
	
	/**
	 * Convenience function that simply calls {@code setFrameRate(fRate, true)}.
	 * 
	 * @see #setFrameRate(float, boolean)
	 */
	public void setFrameRate(float fRate) {
		setFrameRate(fRate, true);
	}
	
	/**
	 * Specifies the number of frames to be displayed every second. If the processor
	 * is not fast enough to maintain the specified rate, it will not be achieved.
	 * <p>
	 * For example, the function call setFrameRate(30) will attempt to refresh 30 times
	 * a second. It is recommended to set the frame rate within setup(). If restart is {@code true}
	 * and {@link #animationIsStarted()} then {@link #restartAnimation()} is called.
	 * <p>
	 * The default rate is 60 frames per second.
	 */
	public void setFrameRate(float fRate, boolean restart) {
		targetFrameRate = fRate;
		parent.frameRate(targetFrameRate);
		if(animationIsStarted() && restart)
			restartAnimation();
	}
	
	/**
	 * Return {@code true} when the animation loop is started.
	 * <p>
	 * Proscene animation loop relies on processing drawing loop. The {@link #draw()} function will
	 * check when {@link #animationIsStarted()} and then called the animation handler method
	 * (set with {@link #addAnimationHandler(Object, String)}) or {@link #animate()} (if no handler
	 * has been added to the scene) every {@link #animationPeriod()} milliseconds. In addition,
	 * During the drawing loop, the variable {@link #animatedFrameWasTriggered} is set
   * to {@code true} each time an animated frame is triggered (and {@code false} otherwise),
   * which is useful to notify to the outside world when an animation event occurs. 
	 * <p>
	 * Be sure to call {@code loop()} before an animation is started.
	 * <p>
	 * <b>Note:</b> The drawing frame rate may be modified when {@link #startAnimation()} is called,
	 * depending on the {@link #animationPeriod()}.   
	 * <p>
	 * Use {@link #startAnimation()}, {@link #stopAnimation()} or {@link #toggleAnimation()}
	 * to change this value.
	 * 
	 * @see #startAnimation()
	 * @see #addAnimationHandler(Object, String)
	 * @see #animate()
	 */
	@Override
	public boolean animationIsStarted() {
		return animationStarted;
	}
	
	/**
	 * The animation loop period, in milliseconds. When {@link #animationIsStarted()}, this is
	 * the delay that takes place between two consecutive iterations of the animation loop.
	 * <p>
	 * This delay defines a target frame rate that will only be achieved if your
	 * {@link #animate()} and {@link #draw()} methods are fast enough. If you want to know
	 * the maximum possible frame rate of your machine on a given scene,
	 * {@link #setAnimationPeriod(float)} to {@code 1}, and {@link #startAnimation()}. The display
	 * will then be updated as often as possible, and the frame rate will be meaningful.  
	 * <p>
	 * Default value is 16.6666 milliseconds (60 Hz) which matches <b>processing</b> default
	 * frame rate.
	 * <p>
	 * <b>Note:</b> This value is taken into account only the next time you call
	 * {@link #startAnimation()}. If {@link #animationIsStarted()}, you should
	 * {@link #stopAnimation()} first. See {@link #restartAnimation()} and
	 * {@link #setAnimationPeriod(float, boolean)}.
	 * 
	 * @see #setAnimationPeriod(float, boolean)
	 */
	public float animationPeriod() {
		return animationPeriod;
	}
	
	/**
	 * Convenience function that simply calls {@code setAnimationPeriod(period, true)}.
	 * 
	 * @see #setAnimationPeriod(float, boolean)
	 */
	public void setAnimationPeriod(float period) {
		setAnimationPeriod(period, true);
	}
	
	/**
	 * Sets the {@link #animationPeriod()}, in milliseconds. If restart is {@code true}
	 * and {@link #animationIsStarted()} then {@link #restartAnimation()} is called.
	 * <p>
	 * <b>Note:</b> The drawing frame rate could be modified when {@link #startAnimation()} is called
	 * depending on the {@link #animationPeriod()}.
	 * 
	 * @see #startAnimation()
	 */
	public void setAnimationPeriod(float period, boolean restart) {
		if(period>0) {
			animationPeriod = period;
			animationFrameRate = 1000f/animationPeriod;
			if(animationIsStarted() && restart)				
				restartAnimation();
		}
	}
	
	/**
	 * Stops animation.
	 * <p>
	 * <b>Warning:</b> Restores the {@code PApplet} frame rate to its default value,
	 * i.e., calls {@code parent.frameRate(60)}. 
	 * 
	 * @see #animationIsStarted()
	 */
	@Override
	public void stopAnimation()	{
		animationStarted = false;
		animatedFrameWasTriggered = false;
		setFrameRate(targetFrameRate, false);
	}
	
	/**
	 * Starts the animation loop.
	 * <p>
	 * Syncs the drawing frame rate according to {@link #animationPeriod()}: If the animation
	 * frame rate (which value depends on the {@link #animationPeriod()})
	 * is higher than the current {@link #frameRate()}, the frame rate is modified to match it,
	 * i.e., each drawing frame will trigger exactly one animation event. If the animation
	 * frame rate is lower than the {@link #frameRate()}, the frame rate is left unmodified,
	 * and the animation frames will be interleaved among the drawing frames in intervals
	 * needed to achieve the target {@link #animationPeriod()} (provided that your
	 * {@link #animate()} and {@link #draw()} methods are fast enough).
	 * 
	 * @see #animationIsStarted()
	 */
	@Override
	public void startAnimation() {
		animationStarted = true;		
		//sync with processing drawing method:		
		currentAnimationFrame = -1;
		animatedFrameWasTriggered = false;
		if( (animationFrameRate > targetFrameRate) )
			parent.frameRate( animationFrameRate ); //bypass setFrameRate()
		else {
			parent.frameRate( targetFrameRate ); //same as setFrameRate(targetFrameRate, false)
			initialDrawingFrameWhenAnimationStarted = parent.frameCount;
			currentAnimationFrame = 0;
			animationToFrameRateRatio = animationFrameRate/targetFrameRate;
		}
	}	
  
  /**
	 * Internal use.
	 * <p>
	 * Calls the animation handler. Calls {@link #animate()} if there's no such a handler. Sets
	 * the value of {@link #animatedFrameWasTriggered} to {@code true} or {@code false}
	 * depending on whether or not an animation event was triggered during this drawing frame
	 * (useful to notify the outside world when an animation event occurs). 
	 * 
	 * @see #animationPeriod()
	 * @see #startAnimation()
	 */
	protected void performAnimation() {		
		if( currentAnimationFrame >= 0 ) {
			long previousAnimationFrame = currentAnimationFrame;
			currentAnimationFrame = PApplet.round( (parent.frameCount - initialDrawingFrameWhenAnimationStarted) * animationToFrameRateRatio );
			if(currentAnimationFrame == previousAnimationFrame) {
				animatedFrameWasTriggered = false;
				return;
			}				
		}		
		animatedFrameWasTriggered = true;		
		if (animateHandlerObject != null) {
			try {
				animateHandlerMethod.invoke(animateHandlerObject, new Object[] { this });
			} catch (Exception e) {
				PApplet.println("Something went wrong when invoking your "	+ animateHandlerMethodName + " method");
				e.printStackTrace();
			}
		}
		else
			animate();
	}
	
	/**
	 * Scene animation method.
	 * <p>
	 * When {@link #animationIsStarted()}, this method defines how your scene evolves over time.
	 * <p>
	 * Overload it as needed. Default implementation is empty. You may
	 * {@link #addAnimationHandler(Object, String)} instead.
	 * <p>
	 * <b>Note</b> that remixlab.proscene.KeyFrameInterpolator (which regularly updates a Frame)
	 * do not use this method.
	 * 
	 * @see #addAnimationHandler(Object, String).
	 */
	public void animate() {
	}	
	
	/**
	 * Attempt to add an 'animation' handler method to the Scene. The default event
	 * handler is a method that returns void and has one single Scene parameter.
	 * 
	 * @param obj
	 *          the object to handle the event
	 * @param methodName
	 *          the method to execute in the object handler class
	 * 
	 * @see #animate()
	 */
	public void addAnimationHandler(Object obj, String methodName) {
		try {
			animateHandlerMethod = obj.getClass().getMethod(methodName, new Class[] { Scene.class });
			animateHandlerObject = obj;
			animateHandlerMethodName = methodName;
		} catch (Exception e) {
			  PApplet.println("Something went wrong when registering your " + methodName + " method");
			  e.printStackTrace();
		}
	}

	/**
	 * Unregisters the 'animation' handler method (if any has previously been added to
	 * the Scene).
	 * 
	 * @see #addAnimationHandler(Object, String)
	 */
	public void removeAnimationHandler() {
		animateHandlerMethod = null;
		animateHandlerObject = null;
		animateHandlerMethodName = null;
	}

	/**
	 * Returns {@code true} if the user has registered an 'animation' handler method to
	 * the Scene and {@code false} otherwise.
	 */
	public boolean hasRegisteredAnimationHandler() {
		if (animateHandlerMethodName == null)
			return false;
		return true;
	}

	// 12. Processing objects

	/**
	 * Sets the processing camera projection matrix from {@link #camera()}. Calls
	 * {@code PApplet.perspective()} or {@code PApplet.orhto()} depending on the
	 * {@link remixlab.remixcam.core.Camera#type()}.
	 */
	protected void setPProjectionMatrix() {
		// compute the processing camera projection matrix from our camera()
		// parameters
		switch (camera().type()) {
		case PERSPECTIVE:
			pg3d.perspective(camera().fieldOfView(), camera().aspectRatio(), camera().zNear(), camera().zFar());
			break;
		case ORTHOGRAPHIC:
			float[] wh = camera().getOrthoWidthHeight();
			pg3d.ortho(-wh[0], wh[0], -wh[1], wh[1], camera().zNear(), camera().zFar());
			break;
		}
		// if our camera() matrices are detached from the processing Camera
		// matrices,
		// we cache the processing camera projection matrix into our camera()
		camera().setProjectionMatrix(toMatrix3D(pg3d.projection));
		// camera().setProjectionMatrix(((PGraphics3D) parent.g).projection);
	}

	/**
	 * Sets the processing camera matrix from {@link #camera()}. Simply calls
	 * {@code PApplet.camera()}.
	 */
	protected void setPModelViewMatrix() {
		// compute the processing camera modelview matrix from our camera()
		// parameters
		pg3d.camera(camera().position().x, camera().position().y, camera()
				.position().z, camera().at().x, camera().at().y, camera().at().z,
				camera().upVector().x, camera().upVector().y, camera().upVector().z);
		// if our camera() matrices are detached from the processing Camera
		// matrices,
		// we cache the processing camera modelview matrix into our camera()
		camera().setModelViewMatrix(toMatrix3D(pg3d.modelview));
		// camera().setProjectionMatrix(((PGraphics3D) parent.g).modelview);
	}

	
	// Drawing methods
	
	/**
	 * Same as {@code cone(12, 0, 0, r, h);}
	 * 
	 * @see #cone(int, float, float, float, float)
	 */
	public void cone(float r, float h) {
		cone(12, 0, 0, r, h);
	}
	
	/**
	 * Same as {@code cone(det, 0, 0, r, h);}
	 * 
	 * @see #cone(int, float, float, float, float)
	 */
	public void cone(int det, float r, float h) {
		cone(det, 0, 0, r, h);
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
	 * Same as {@code cone(det, 0, 0, r1, r2, h);}
	 * 
	 * @see #cone(int, float, float, float, float, float)
	 */
	public void cone(int det, float r1, float r2, float h) {
		cone(det, 0, 0, r1, r2, h);
	}

	/**
	 * The code of this function was adapted from
	 * http://processinghacks.com/hacks:cone Thanks to Tom Carden.
	 * 
	 * @see #cone(int, float, float, float, float, float)
	 */
	@Override
	public void cone(int detail, float x, float y, float r, float h) {
		float unitConeX[] = new float[detail + 1];
		float unitConeY[] = new float[detail + 1];

		for (int i = 0; i <= detail; i++) {
			float a1 = TWO_PI * i / detail;
			unitConeX[i] = r * (float) Math.cos(a1);
			unitConeY[i] = r * (float) Math.sin(a1);
		}

		pg3d.pushMatrix();
		pg3d.translate(x, y);
		pg3d.beginShape(TRIANGLE_FAN);
		pg3d.vertex(0, 0, h);
		for (int i = 0; i <= detail; i++) {
			pg3d.vertex(unitConeX[i], unitConeY[i], 0.0f);
		}
		pg3d.endShape();
		pg3d.popMatrix();		
	}

	@Override
	public void cone(int detail, float x, float y, float r1, float r2, float h) {
		float firstCircleX[] = new float[detail + 1];
		float firstCircleY[] = new float[detail + 1];
		float secondCircleX[] = new float[detail + 1];
		float secondCircleY[] = new float[detail + 1];

		for (int i = 0; i <= detail; i++) {
			float a1 = TWO_PI * i / detail;
			firstCircleX[i] = r1 * (float) Math.cos(a1);
			firstCircleY[i] = r1 * (float) Math.sin(a1);
			secondCircleX[i] = r2 * (float) Math.cos(a1);
			secondCircleY[i] = r2 * (float) Math.sin(a1);
		}

		pg3d.pushMatrix();
		pg3d.translate(x, y);
		pg3d.beginShape(QUAD_STRIP);
		for (int i = 0; i <= detail; i++) {
			pg3d.vertex(firstCircleX[i], firstCircleY[i], 0);
			pg3d.vertex(secondCircleX[i], secondCircleY[i], h);
		}
		pg3d.endShape();
		pg3d.popMatrix();		
	}
	
	@Override
	public void cylinder(float w, float h) {
		float px, py;

		pg3d.beginShape(QUAD_STRIP);
		for (float i = 0; i < 13; i++) {
			px = PApplet.cos(PApplet.radians(i * 30)) * w;
			py = PApplet.sin(PApplet.radians(i * 30)) * w;
			pg3d.vertex(px, py, 0);
			pg3d.vertex(px, py, h);
		}
		pg3d.endShape();

		pg3d.beginShape(TRIANGLE_FAN);
		pg3d.vertex(0, 0, 0);
		for (float i = 12; i > -1; i--) {
			px = PApplet.cos(PApplet.radians(i * 30)) * w;
			py = PApplet.sin(PApplet.radians(i * 30)) * w;
			pg3d.vertex(px, py, 0);
		}
		pg3d.endShape();

		pg3d.beginShape(TRIANGLE_FAN);
		pg3d.vertex(0, 0, h);
		for (float i = 0; i < 13; i++) {
			px = PApplet.cos(PApplet.radians(i * 30)) * w;
			py = PApplet.sin(PApplet.radians(i * 30)) * w;
			pg3d.vertex(px, py, h);
		}
		pg3d.endShape();
	}

	/**
	 * Draws all the Camera paths defined by {@link #keyFrameInterpolator(int)}
	 * and makes them editable by adding all its Frames to the mouse grabber pool.
	 * <p>
	 * First calls
	 * {@link remixlab.remixcam.core.KeyFrameInterpolator#addFramesToMouseGrabberPool()}
	 * and then
	 * {@link remixlab.remixcam.core.KeyFrameInterpolator#drawPath(int, int, float)}
	 * for all the defined paths.
	 * 
	 * @see #hideAllPaths()
	 */
	public void drawAllPaths() {
		HashMap<Integer, KeyFrameInterpolator> kfi = camera().kfiMap();
		Iterator<Integer> itrtr = kfi.keySet().iterator();
		while (itrtr.hasNext()) {
			Integer key = itrtr.next();
			kfi.get(key).addFramesToMouseGrabberPool();
			drawPath(kfi.get(key), 3, 5, camera().sceneRadius());
		}		
	}

	@Override
	public void drawArrow(float length, float radius) {
		float head = 2.5f * (radius / length) + 0.1f;
		float coneRadiusCoef = 4.0f - 5.0f * head;

		cylinder(radius, length	* (1.0f - head / coneRadiusCoef));
		pg3d.translate(0.0f, 0.0f, length * (1.0f - head));
		cone(coneRadiusCoef * radius, head * length);
		pg3d.translate(0.0f, 0.0f, -length * (1.0f - head));
	}

	@Override
	public void drawArrow(Vector3D from, Vector3D to, float radius) {
		pg3d.pushMatrix();
		pg3d.translate(from.x, from.y, from.z);
	  // TODO: fix data conversion in an stronger way:
		pg3d.applyMatrix(fromMatrix3D(new Quaternion(new Vector3D(0, 0, 1), Vector3D.sub(new Vector3D(to.x, to.y, to.z), new Vector3D(from.x, from.y, from.z))).matrix()));
		drawArrow(Vector3D.sub(to, from).mag(), radius);
		pg3d.popMatrix();
	}
	
	/**
	 * Convenience wrapper function that simply calls {@code drawAxis(100)}
	 * 
	 * @see #drawAxis(float)
	 */
	public void drawAxis() {
		drawAxis(100);
	}

	@Override
	public void drawAxis(float length) {
		final float charWidth = length / 40.0f;
		final float charHeight = length / 30.0f;
		final float charShift = 1.04f * length;

		// pg3d.noLights();

		pg3d.pushStyle();
		
		pg3d.beginShape(LINES);		
		pg3d.strokeWeight(2);
		// The X
		pg3d.stroke(255, 178, 178);
		pg3d.vertex(charShift, charWidth, -charHeight);
		pg3d.vertex(charShift, -charWidth, charHeight);
		pg3d.vertex(charShift, -charWidth, -charHeight);
		pg3d.vertex(charShift, charWidth, charHeight);
		// The Y
		pg3d.stroke(178, 255, 178);
		pg3d.vertex(charWidth, charShift, charHeight);
		pg3d.vertex(0.0f, charShift, 0.0f);
		pg3d.vertex(-charWidth, charShift, charHeight);
		pg3d.vertex(0.0f, charShift, 0.0f);
		pg3d.vertex(0.0f, charShift, 0.0f);
		pg3d.vertex(0.0f, charShift, -charHeight);
		// The Z
		pg3d.stroke(178, 178, 255);
		
		//left_handed
		pg3d.vertex(-charWidth, -charHeight, charShift);
		pg3d.vertex(charWidth, -charHeight, charShift);
		pg3d.vertex(charWidth, -charHeight, charShift);
		pg3d.vertex(-charWidth, charHeight, charShift);
		pg3d.vertex(-charWidth, charHeight, charShift);
		pg3d.vertex(charWidth, charHeight, charShift);
	  //right_handed coordinate system should go like this:
		//pg3d.vertex(-charWidth, charHeight, charShift);
		//pg3d.vertex(charWidth, charHeight, charShift);
		//pg3d.vertex(charWidth, charHeight, charShift);
		//pg3d.vertex(-charWidth, -charHeight, charShift);
		//pg3d.vertex(-charWidth, -charHeight, charShift);
		//pg3d.vertex(charWidth, -charHeight, charShift);
		
		pg3d.endShape();

		// Z axis
		pg3d.noStroke();
		pg3d.fill(178, 178, 255);
		drawArrow(length, 0.01f * length);

		// X Axis
		pg3d.fill(255, 178, 178);
		pg3d.pushMatrix();
		pg3d.rotateY(HALF_PI);
		drawArrow(length, 0.01f * length);
		pg3d.popMatrix();

		// Y Axis
		pg3d.fill(178, 255, 178);
		pg3d.pushMatrix();
		pg3d.rotateX(-HALF_PI);
		drawArrow(length, 0.01f * length);
		pg3d.popMatrix();

		pg3d.popStyle();
	}

	/**
	 * Convenience wrapper function that simply calls {@code drawGrid(100, 10)}
	 * 
	 * @see #drawGrid(float, int)
	 */
	public void drawGrid() {
		drawGrid(100, 10);
	}

	/**
	 * Convenience wrapper function that simply calls {@code drawGrid(size, 10)}
	 * 
	 * @see #drawGrid(float, int)
	 */
	public void drawGrid(float size) {
		drawGrid(size, 10);
	}

	/**
	 * Convenience wrapper function that simply calls {@code drawGrid(100, nbSubdivisions)}
	 * 
	 * @see #drawGrid(float, int)
	 */
	public void drawGrid(int nbSubdivisions) {
		drawGrid(100, nbSubdivisions);
	}

	@Override
	public void drawGrid(float size, int nbSubdivisions) {
		pg3d.pushStyle();
		pg3d.stroke(170, 170, 170);
		pg3d.strokeWeight(1);
		pg3d.beginShape(LINES);
		for (int i = 0; i <= nbSubdivisions; ++i) {
			final float pos = size * (2.0f * i / nbSubdivisions - 1.0f);
			pg3d.vertex(pos, -size);
			pg3d.vertex(pos, +size);
			pg3d.vertex(-size, pos);
			pg3d.vertex(size, pos);
		}
		pg3d.endShape();
		pg3d.popStyle();
	}
	
	/**
	 * Convenience function that simply calls {@code drawCamera(camera,
	 * 170, true, 1.0f)}
	 * 
	 * @see #drawCamera(Camera, int, boolean, float)
	 */
	public void drawCamera(Camera camera) {
		drawCamera(camera, 170, true, 1.0f);
	}
	
	/**
	 * Convenience function that simply calls {@code drawCamera(camera,
	 * 170, true, scale)}
	 * 
	 * @see #drawCamera(Camera, int, boolean, float)
	 */
	public void drawCamera(PGraphics3D pg3d, Camera camera, float scale) {
		drawCamera(camera, 170, true, scale);
	}
	
	/**
	 * Convenience function that simply calls {@code drawCamera(camera,
	 * color, true, 1.0f)}
	 * 
	 * @see #drawCamera(Camera, int, boolean, float)
	 */
	public void drawCamera(Camera camera, int color) {
		drawCamera(camera, color, true, 1.0f);
	}
	
	/**
	 * Convenience function that simply calls {@code drawCamera(camera,
	 * 170, drawFarPlane, 1.0f)}
	 * 
	 * @see #drawCamera(Camera, int, boolean, float)
	 */
	public void drawCamera(Camera camera,	boolean drawFarPlane) {
		drawCamera(camera, 170, drawFarPlane, 1.0f);
	}
	
	/**
	 * Convenience function that simply calls {@code drawCamera(camera, 170, drawFarPlane, scale)}
	 * 
	 * @see #drawCamera(Camera, int, boolean, float)
	 */
	public void drawCamera(Camera camera,	boolean drawFarPlane, float scale) {
		drawCamera(camera, 170, drawFarPlane, scale);
	}
	
	/**
	 * Convenience function that simply calls {@code drawCamera(camera, color, true, scale)}
	 * 
	 * @see #drawCamera(Camera, int, boolean, float)
	 */
	public void drawCamera(Camera camera, int color,	float scale) {
		drawCamera(camera, color, true, scale);
	}
	
	/**
	 * Convenience function that simply calls {@code drawCamera(camera,
	 * color, drawFarPlane, 1.0f)}
	 * 
	 * @see #drawCamera(Camera, int, boolean, float)
	 */
	public void drawCamera(Camera camera, int color,	boolean drawFarPlane) {
		drawCamera(camera, color, drawFarPlane, 1.0f);
	}

	@Override
	public void drawCamera(Camera camera, int color, boolean drawFarPlane, float scale) {
		pg3d.pushMatrix();
		// pg3d.applyMatrix(camera.frame().worldMatrix());
		// same as the previous line, but maybe more efficient		
		tmpFrame.fromMatrix(camera.frame().worldMatrix());
		//tmpFrame.applyTransformation(pg3d);// TODO: fix me?		
		pg3d.translate(tmpFrame.translation().x, tmpFrame.translation().y, tmpFrame.translation().z);
		pg3d.rotate(tmpFrame.rotation().angle(), tmpFrame.rotation().axis().x, tmpFrame.rotation().axis().y, tmpFrame.rotation().axis().z);

		// 0 is the upper left coordinates of the near corner, 1 for the far one
		PVector[] points = new PVector[2];
		points[0] = new PVector();
		points[1] = new PVector();

		points[0].z = scale * camera.zNear();
		points[1].z = scale * camera.zFar();

		switch (camera.type()) {
		case PERSPECTIVE: {
			points[0].y = points[0].z * PApplet.tan(camera.fieldOfView() / 2.0f);
			points[0].x = points[0].y * camera.aspectRatio();
			float ratio = points[1].z / points[0].z;
			points[1].y = ratio * points[0].y;
			points[1].x = ratio * points[0].x;
			break;
		}
		case ORTHOGRAPHIC: {
			float[] wh = camera.getOrthoWidthHeight();
			points[0].x = points[1].x = scale * wh[0];
			points[0].y = points[1].y = scale * wh[1];
			break;
		}
		}

		int farIndex = drawFarPlane ? 1 : 0;

		// Near and (optionally) far plane(s)
		pg3d.pushStyle();
		pg3d.noStroke();
		pg3d.fill(color);
		pg3d.beginShape(PApplet.QUADS);
		for (int i = farIndex; i >= 0; --i) {
			pg3d.normal(0.0f, 0.0f, (i == 0) ? 1.0f : -1.0f);
			pg3d.vertex(points[i].x, points[i].y, -points[i].z);
			pg3d.vertex(-points[i].x, points[i].y, -points[i].z);
			pg3d.vertex(-points[i].x, -points[i].y, -points[i].z);
			pg3d.vertex(points[i].x, -points[i].y, -points[i].z);
		}
		pg3d.endShape();

		// Up arrow
		float arrowHeight = 1.5f * points[0].y;
		float baseHeight = 1.2f * points[0].y;
		float arrowHalfWidth = 0.5f * points[0].x;
		float baseHalfWidth = 0.3f * points[0].x;

		// pg3d.noStroke();
		pg3d.fill(color);
		// Base
		pg3d.beginShape(PApplet.QUADS);
		
		pg3d.vertex(-baseHalfWidth, -points[0].y, -points[0].z);
		pg3d.vertex(baseHalfWidth, -points[0].y, -points[0].z);
		pg3d.vertex(baseHalfWidth, -baseHeight, -points[0].z);
		pg3d.vertex(-baseHalfWidth, -baseHeight, -points[0].z);
  	//right_handed coordinate system should go like this:
		//pg3d.vertex(-baseHalfWidth, points[0].y, -points[0].z);
		//pg3d.vertex(baseHalfWidth, points[0].y, -points[0].z);
		//pg3d.vertex(baseHalfWidth, baseHeight, -points[0].z);
		//pg3d.vertex(-baseHalfWidth, baseHeight, -points[0].z);
		
		pg3d.endShape();

		// Arrow
		pg3d.fill(color);
		pg3d.beginShape(PApplet.TRIANGLES);
		
		pg3d.vertex(0.0f, -arrowHeight, -points[0].z);
		pg3d.vertex(-arrowHalfWidth, -baseHeight, -points[0].z);
		pg3d.vertex(arrowHalfWidth, -baseHeight, -points[0].z);
  	//right_handed coordinate system should go like this:
		//pg3d.vertex(0.0f, arrowHeight, -points[0].z);
		//pg3d.vertex(-arrowHalfWidth, baseHeight, -points[0].z);
		//pg3d.vertex(arrowHalfWidth, baseHeight, -points[0].z);
		
		pg3d.endShape();

		// Frustum lines
		pg3d.stroke(color);
		pg3d.strokeWeight(2);
		switch (camera.type()) {
		case PERSPECTIVE:
			pg3d.beginShape(PApplet.LINES);
			pg3d.vertex(0.0f, 0.0f, 0.0f);
			pg3d.vertex(points[farIndex].x, points[farIndex].y, -points[farIndex].z);
			pg3d.vertex(0.0f, 0.0f, 0.0f);
			pg3d.vertex(-points[farIndex].x, points[farIndex].y, -points[farIndex].z);
			pg3d.vertex(0.0f, 0.0f, 0.0f);
			pg3d.vertex(-points[farIndex].x, -points[farIndex].y, -points[farIndex].z);
			pg3d.vertex(0.0f, 0.0f, 0.0f);
			pg3d.vertex(points[farIndex].x, -points[farIndex].y, -points[farIndex].z);
			pg3d.endShape();
			break;
		case ORTHOGRAPHIC:
			if (drawFarPlane) {
				pg3d.beginShape(PApplet.LINES);
				pg3d.vertex(points[0].x, points[0].y, -points[0].z);
				pg3d.vertex(points[1].x, points[1].y, -points[1].z);
				pg3d.vertex(-points[0].x, points[0].y, -points[0].z);
				pg3d.vertex(-points[1].x, points[1].y, -points[1].z);
				pg3d.vertex(-points[0].x, -points[0].y, -points[0].z);
				pg3d.vertex(-points[1].x, -points[1].y, -points[1].z);
				pg3d.vertex(points[0].x, -points[0].y, -points[0].z);
				pg3d.vertex(points[1].x, -points[1].y, -points[1].z);
				pg3d.endShape();
			}
		}

		pg3d.popStyle();

		pg3d.popMatrix();
	}
	
	public void drawKFICamera(float scale) {
		drawKFICamera(170, scale);
	}

	@Override
	public void drawKFICamera(int color, float scale) {
		float halfHeight = scale * 0.07f;
		float halfWidth = halfHeight * 1.3f;
		float dist = halfHeight / PApplet.tan(PApplet.PI / 8.0f);

		float arrowHeight = 1.5f * halfHeight;
		float baseHeight = 1.2f * halfHeight;
		float arrowHalfWidth = 0.5f * halfWidth;
		float baseHalfWidth = 0.3f * halfWidth;

		// Frustum outline
		pg3d.pushStyle();

		pg3d.noFill();
		pg3d.stroke(color);
		pg3d.beginShape();
		pg3d.vertex(-halfWidth, halfHeight, -dist);
		pg3d.vertex(-halfWidth, -halfHeight, -dist);
		pg3d.vertex(0.0f, 0.0f, 0.0f);
		pg3d.vertex(halfWidth, -halfHeight, -dist);
		pg3d.vertex(-halfWidth, -halfHeight, -dist);
		pg3d.endShape();
		pg3d.noFill();
		pg3d.beginShape();
		pg3d.vertex(halfWidth, -halfHeight, -dist);
		pg3d.vertex(halfWidth, halfHeight, -dist);
		pg3d.vertex(0.0f, 0.0f, 0.0f);
		pg3d.vertex(-halfWidth, halfHeight, -dist);
		pg3d.vertex(halfWidth, halfHeight, -dist);
		pg3d.endShape();

		// Up arrow
		pg3d.noStroke();
		pg3d.fill(color);
		// Base
		pg3d.beginShape(PApplet.QUADS);
		
		pg3d.vertex(baseHalfWidth, -halfHeight, -dist);
		pg3d.vertex(-baseHalfWidth, -halfHeight, -dist);
		pg3d.vertex(-baseHalfWidth, -baseHeight, -dist);
		pg3d.vertex(baseHalfWidth, -baseHeight, -dist);
  	//right_handed coordinate system should go like this:
		//pg3d.vertex(-baseHalfWidth, halfHeight, -dist);
		//pg3d.vertex(baseHalfWidth, halfHeight, -dist);
		//pg3d.vertex(baseHalfWidth, baseHeight, -dist);
		//pg3d.vertex(-baseHalfWidth, baseHeight, -dist);
		
		pg3d.endShape();
		// Arrow
		pg3d.beginShape(PApplet.TRIANGLES);
		
		pg3d.vertex(0.0f, -arrowHeight, -dist);
		pg3d.vertex(arrowHalfWidth, -baseHeight, -dist);
		pg3d.vertex(-arrowHalfWidth, -baseHeight, -dist);
	  //right_handed coordinate system should go like this:
		//pg3d.vertex(0.0f, arrowHeight, -dist);
		//pg3d.vertex(-arrowHalfWidth, baseHeight, -dist);
		//pg3d.vertex(arrowHalfWidth, baseHeight, -dist);
		
		pg3d.endShape();

		pg3d.popStyle();		
	}

	@Override
	public void drawPath(KeyFrameInterpolator KFI, int mask, int nbFrames,	float scale) {
		int nbSteps = 30;
		if (!KFI.pathIsValid()) {
			KFI.drawingPath().clear();

			if (KFI.keyFrame().isEmpty())
				return;

			if (!KFI.valuesAreValid())
				KFI.updateModifiedFrameValues();

			if (KFI.keyFrame().get(0) == KFI.keyFrame().get(KFI.keyFrame().size() - 1))
				KFI.drawingPath().add(new GLFrame(KFI.keyFrame().get(0).position(), KFI.keyFrame().get(0).orientation()));
			else {
				KeyFrame[] kf = new KeyFrame[4];
				kf[0] = KFI.keyFrame().get(0);
				kf[1] = kf[0];

				int index = 1;
				kf[2] = (index < KFI.keyFrame().size()) ? KFI.keyFrame().get(index) : null;
				index++;
				kf[3] = (index < KFI.keyFrame().size()) ? KFI.keyFrame().get(index) : null;

				while (kf[2] != null) {
					Vector3D diff = Vector3D.sub(kf[2].position(), kf[1].position());
					Vector3D vec1 = Vector3D.add(Vector3D.mult(diff, 3.0f), Vector3D.mult(
							kf[1].tgP(), (-2.0f)));
					vec1 = Vector3D.sub(vec1, kf[2].tgP());
					Vector3D vec2 = Vector3D.add(Vector3D.mult(diff, (-2.0f)), kf[1].tgP());
					vec2 = Vector3D.add(vec2, kf[2].tgP());

					for (int step = 0; step < nbSteps; ++step) {
						float alpha = step / (float) nbSteps;
						KFI.drawingFrame().setPosition(Vector3D.add(kf[1].position(), Vector3D.mult(
								Vector3D.add(kf[1].tgP(), Vector3D.mult(Vector3D.add(vec1, Vector3D
										.mult(vec2, alpha)), alpha)), alpha)));
						KFI.drawingFrame().setOrientation(Quaternion.squad(kf[1].orientation(), kf[1]
								.tgQ(), kf[2].tgQ(), kf[2].orientation(), alpha));
						KFI.drawingPath().add( KFI.drawingFrame().getCopy() );
					}

					// Shift
					kf[0] = kf[1];
					kf[1] = kf[2];
					kf[2] = kf[3];

					index++;
					kf[3] = (index < KFI.keyFrame().size()) ? KFI.keyFrame().get(index) : null;
				}
				// Add last KeyFrame
				KFI.drawingPath().add(new GLFrame(kf[1].position(), kf[1].orientation()));
			}
			KFI.validatePath();
		}

		if (mask != 0) {
			pg3d.pushStyle();
			pg3d.strokeWeight(2);

			if ((mask & 1) != 0) {
				pg3d.noFill();
				pg3d.stroke(170);
				pg3d.beginShape();
				for (GLFrame myFr : KFI.drawingPath())
					pg3d.vertex(myFr.position().x, myFr.position().y, myFr.position().z);
				pg3d.endShape();
			}
			if ((mask & 6) != 0) {
				int count = 0;
				if (nbFrames > nbSteps)
					nbFrames = nbSteps;
				float goal = 0.0f;

				for (GLFrame myFr : KFI.drawingPath())
					if ((count++) >= goal) {
						goal += nbSteps / (float) nbFrames;
						pg3d.pushMatrix();

						// pg3d.applyMatrix(myFr.matrix());
						//myFr.applyTransformation(pg3d);// replaced with the two lines below:						
						pg3d.translate(myFr.translation().x, myFr.translation().y, myFr.translation().z);
						pg3d.rotate(myFr.rotation().angle(), myFr.rotation().axis().x, myFr.rotation().axis().y, myFr.rotation().axis().z);

						if ((mask & 2) != 0)
							drawKFICamera(scale);
						if ((mask & 4) != 0)
							drawAxis(scale / 10.0f);

						pg3d.popMatrix();
					}
			}
			pg3d.popStyle();
		}
	}
	
	// TODO: check where to put all these
	
	/**
	 * Utility function that returns the PMatrix3D representation of the given Matrix3D.
	 */
	public static final Matrix3D toMatrix3D(PMatrix3D m) {
		return new Matrix3D(m.m00, m.m01, m.m02, m.m03, 
				                m.m10, m.m11, m.m12, m.m13,
				                m.m20, m.m21, m.m22, m.m23,
				                m.m30, m.m31, m.m32, m.m33);
	}
	
	/**
	 * Utility function that returns the PMatrix3D representation of the given Matrix3D.
	 */
	public static final PMatrix3D fromMatrix3D(Matrix3D m) {
		return new PMatrix3D(m.m00, m.m01, m.m02, m.m03, 
				                 m.m10, m.m11, m.m12, m.m13,
				                 m.m20, m.m21, m.m22, m.m23,
				                 m.m30, m.m31, m.m32, m.m33);
	}
	
	/**
	 * Apply the transformation defined by this Frame to {@code p3d}. The Frame is
	 * first translated and then rotated around the new translated origin.
	 * <p>
	 * Same as:
	 * <p>
	 * {@code p3d.translate(translation().x, translation().y, translation().z);} <br>
	 * {@code p3d.rotate(rotation().angle(), rotation().axis().x,
	 * rotation().axis().y, rotation().axis().z);} <br>
	 * <p>
	 * This method should be used in conjunction with PApplet to modify the
	 * processing modelview matrix from a Frame hierarchy. For example, with this
	 * Frame hierarchy:
	 * <p>
	 * {@code Frame body = new Frame();} <br>
	 * {@code Frame leftArm = new Frame();} <br>
	 * {@code Frame rightArm = new Frame();} <br>
	 * {@code leftArm.setReferenceFrame(body);} <br>
	 * {@code rightArm.setReferenceFrame(body);} <br>
	 * <p>
	 * The associated processing drawing code should look like:
	 * <p>
	 * {@code p3d.pushMatrix();//p is the PApplet instance} <br>
	 * {@code body.applyTransformation(p);} <br>
	 * {@code drawBody();} <br>
	 * {@code p3d.pushMatrix();} <br>
	 * {@code leftArm.applyTransformation(p);} <br>
	 * {@code drawArm();} <br>
	 * {@code p3d.popMatrix();} <br>
	 * {@code p3d.pushMatrix();} <br>
	 * {@code rightArm.applyTransformation(p);} <br>
	 * {@code drawArm();} <br>
	 * {@code p3d.popMatrix();} <br>
	 * {@code p3d.popMatrix();} <br>
	 * <p>
	 * Note the use of nested {@code pushMatrix()} and {@code popMatrix()} blocks
	 * to represent the frame hierarchy: {@code leftArm} and {@code rightArm} are
	 * both correctly drawn with respect to the {@code body} coordinate system.
	 * <p>
	 * <b>Attention:</b> When drawing a frame hierarchy as above, this method
	 * should be used whenever possible (one can also use {@link #matrix()}
	 * instead).
	 * 
	 * @see #matrix()
	 */
	public void applyTransformation(InteractiveFrame iFrame) {
		pg3d.translate( iFrame.translation().x, iFrame.translation().y, iFrame.translation().z );
		pg3d.rotate( iFrame.rotation().angle(), iFrame.rotation().axis().x, iFrame.rotation().axis().y, iFrame.rotation().axis().z);
	}
	
	// TODO implement at the scene parent, once camera profile are pulled  up
	public void setDrawInteractiveFrame(boolean draw) {
		if (draw && (glIFrame == null))
			return;
		if (!draw && (currentCameraProfile().mode() == CameraProfile.Mode.THIRD_PERSON)
				&& interactiveFrame().equals(avatar()))// more natural than to bypass it
			return;
		iFrameIsDrwn = draw;
	}
}