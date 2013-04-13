/**
 *                     ProScene (version 1.9.90)      
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
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import processing.opengl.*;

/**
import remixlab.remixcam.core.*;
import remixlab.remixcam.devices.*;
import remixlab.remixcam.geom.*;
import remixlab.remixcam.util.*;
// */

import remixlab.proscene.renderers.*;
import remixlab.proscene.util.TimerWrap;
// /*
import remixlab.remixcam.core.AbstractScene;
import remixlab.remixcam.core.Camera;
import remixlab.remixcam.core.InteractiveFrame;
import remixlab.remixcam.core.ViewWindow;
import remixlab.remixcam.devices.CameraProfile;
//import remixlab.remixcam.core.SimpleFrame;
//import remixlab.remixcam.core.KeyFrameInterpolator;
import remixlab.remixcam.devices.DeviceGrabbable;
import remixlab.remixcam.devices.Bindings;
import remixlab.remixcam.devices.KeyboardShortcut;
import remixlab.remixcam.events.DLKeyEvent;
import remixlab.remixcam.events.DLMouseEvent;
import remixlab.remixcam.events.DesktopEvents;
import remixlab.remixcam.util.AbstractTimerJob;
import remixlab.remixcam.util.SingleThreadedTaskableTimer;
import remixlab.remixcam.util.SingleThreadedTimer;
import remixlab.remixcam.geom.Matrix3D;
//import remixlab.remixcam.geom.Matrix3D;
import remixlab.remixcam.geom.Vector3D;
import remixlab.remixcam.geom.Point;
// */

import java.lang.reflect.Method;
import java.nio.FloatBuffer;

/**
 * A 3D interactive Processing scene.
 * <p>
 * A Scene has a full reach Camera, it can be used for on-screen or off-screen
 * rendering purposes (see the different constructors), and it has two means to
 * manipulate objects: an {@link #interactiveFrame()} single instance (which by
 * default is null) and a {@link #mouseGrabber()} pool.
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
 */
public class Scene extends AbstractScene /**implements PConstants*/ {
	public class P5DesktopEvents extends DesktopEvents {	
		public P5DesktopEvents(Scene s) {
			super(s);
		}
		public void keyEvent(KeyEvent e) {
			handleKeyEvent(new DLKeyEvent(e.getMillis(), e.getAction(), e.getModifiers(), e.getKey(), e.getKeyCode()));
		}
		public void mouseEvent(MouseEvent e) {
			handleMouseEvent(new DLMouseEvent(e.getMillis(), e.getAction(), e.getModifiers(), e.getX(), e.getY(), e.getButton(), e.getAmount()));
		}
	}
	
	// proscene version
	public static final String version = "1.9.60";
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

	// P R O C E S S I N G   A P P L E T   A N D   O B J E C T S
	public PApplet parent;	

	// O B J E C T S
	//TODO pending
	public P5DesktopEvents dE;

	// E X C E P T I O N H A N D L I N G	
  protected int beginOffScreenDrawingCalls;  
  	
	/**
	// M O U S E   G R A B B E R   H I N T   C O L O R S
	private int onSelectionHintColor;
	private int offSelectionHintColor;
	private int cameraPathOnSelectionHintColor;
	private int cameraPathOffSelectionHintColor;
	*/

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

	/**
	 * Constructor that defines an on-screen Scene (the one that most likely
	 * would just fulfill all of your needs). All viewer parameters (display flags,
	 * scene parameters, associated objects...) are set to their default values.
	 * See the associated documentation. This is actually just a convenience
	 * function that simply calls {@code this(p, (PGraphicsOpenGL) p.g)}. Call any
	 * other constructor by yourself to possibly define an off-screen Scene.
	 * 
	 * @see #Scene(PApplet, PGraphics)
	 * @see #Scene(PApplet, PGraphics, int, int)
	 */	
	public Scene(PApplet p) {
		this(p, (PGraphics) p.g);		
	}
	
	/**
	 * This constructor is typically used to define an off-screen Scene. This is
	 * accomplished simply by specifying a custom {@code renderer}, different
	 * from the PApplet's renderer. All viewer parameters (display flags, scene
	 * parameters, associated objects...) are set to their default values. This
	 * is actually just a convenience function that simply calls
	 * {@code this(p, renderer, 0, 0)}. If you plan to define an on-screen Scene,
	 * call {@link #Scene(PApplet)} instead.
	 * 
	 * @see #Scene(PApplet)
	 * @see #Scene(PApplet, PGraphics, int, int)
	 */
	public Scene(PApplet p, PGraphics renderer) {	
		this(p, renderer, 0, 0);
	}

	/**
	 * This constructor is typically used to define an off-screen Scene. This is
	 * accomplished simply by specifying a custom {@code renderer}, different
	 * from the PApplet's renderer. All viewer parameters (display flags, scene
	 * parameters, associated objects...) are set to their default values. The
	 * {@code x} and {@code y} parameters define the position of the upper-left
	 * corner where the off-screen Scene is expected to be displayed, e.g., for
	 * instance with a call to the Processing built-in {@code image(img, x, y)}
	 * function. If {@link #isOffscreen()} returns {@code false} (i.e.,
	 * {@link #renderer()} equals the PApplet's renderer), the values of x and y
	 * are meaningless (both are set to 0 to be taken as dummy values). If you
	 * plan to define an on-screen Scene, call {@link #Scene(PApplet)} instead. 
	 * 
	 * @see #Scene(PApplet)
	 * @see #Scene(PApplet, PGraphicsOpenGL)
	 */
	public Scene(PApplet p, PGraphics pg, int x, int y) {
		parent = p;
		
		if( pg instanceof PGraphicsJava2D )
			setRenderer( new P5RendererJava2D(this, (PGraphicsJava2D)pg) );	
		else
			if( pg instanceof PGraphics2D )
				setRenderer( new P5Renderer2D(this, (PGraphics2D)pg) );
			else
				if( pg instanceof PGraphics3D )
					setRenderer( new P5Renderer3D(this, (PGraphics3D)pg) );
		
		width = pg.width;
		height = pg.height;
		
		if(is2D())
			this.setGridDotted(false);
		setJavaTimers();
		setLeftHanded();
		
		/**
		// TODO decide if this should go
		//mouse grabber selection hint colors		
		setMouseGrabberOnSelectionHintColor(pg3d.color(0, 0, 255));
		setMouseGrabberOffSelectionHintColor(pg3d.color(255, 0, 0));
		setMouseGrabberCameraPathOnSelectionHintColor(pg3d.color(255, 255, 0));
		setMouseGrabberCameraPathOffSelectionHintColor(pg3d.color(0, 255, 255));
		*/		
		
		//event handler
		dE = new P5DesktopEvents(this);
		
		// 1 ->   	

		// /**
		//TODO pull up
		gProfile = new Bindings<KeyboardShortcut, KeyboardAction>(this);
		pathKeys = new Bindings<Integer, Integer>(this);		
		setDefaultShortcuts();
		// */

		avatarIsInteractiveDrivableFrame = false;// also init in setAvatar, but we
		// need it here to properly init the camera
		avatarIsInteractiveAvatarFrame = false;// also init in setAvatar, but we
		// need it here to properly init the camera
		
		if( is3D() )
			ph = new Camera(this);
		else
			ph = new ViewWindow(this);
		setViewPort(pinhole());//calls showAll();
		
		//TODO pull up to AbstractScene
		initDefaultCameraProfiles();
				
		setInteractiveFrame(null);
		setAvatar(null);
		
  	// This scene is offscreen if the provided renderer is
		// different from the main PApplet renderer.
		offscreen = pg != p.g;
		if(offscreen)
			upperLeftCorner = new Point(x, y);
		else
			upperLeftCorner = new Point(0, 0);
		beginOffScreenDrawingCalls = 0;		
		setMouseTracking(true);
		setMouseGrabber(null);
		
		mouseGrabberIsAnIFrame = false;

		//animation
		animationTimer = new SingleThreadedTimer(this);
		setAnimationPeriod(40, false); // 25Hz
		stopAnimation();
		
		arpFlag = false;
		pupFlag = false;

		withConstraint = true;

		setAxisIsDrawn(true);
		setGridIsDrawn(true);
		setFrameSelectionHintIsDrawn(false);
		setCameraPathsAreDrawn(false);
		
		disableFrustumEquationsUpdate();		

		parent.registerMethod("pre", this);
		parent.registerMethod("draw", this);
		// parent.registerPost(this);
		enableKeyboardHandling();
		enableMouseHandling();
		parseKeyXxxxMethods();
		parseMouseXxxxMethods();

		// register draw method
		removeDrawHandler();
	  // register animation method
		removeAnimationHandler();

		// called only once
		init();
	}	
	
	// matrix stuff
	
	@Override
	public void pushMatrix() {
		pg().pushMatrix();
	}
	
	@Override
	public void popMatrix() {
		pg().popMatrix();
	}
	
	@Override
	public void resetMatrix() {
		pg().resetMatrix();
	}
	
	@Override
	public Matrix3D getMatrix() {
		PMatrix3D pM = (PMatrix3D) pg().getMatrix();
		return new Matrix3D(pM.get(new float[16]), true);// set it transposed
	}
	
	@Override
	public Matrix3D getMatrix(Matrix3D target) {
		PMatrix3D pM = (PMatrix3D) pg().getMatrix();
		target.setTransposed(pM.get(new float[16]));
		return target;
	}
	
	@Override
	public void setMatrix(Matrix3D source) {
		resetMatrix();
		applyMatrix(source);
	}
	
	@Override
	public void printMatrix() {
		pg().printMatrix();
	}
	
	@Override
	public void applyMatrix(Matrix3D source) {
		PMatrix3D pM = new PMatrix3D();
		pM.set(source.getTransposed(new float[16]));
		pg().applyMatrix(pM);
	}
	
	@Override
	public void applyMatrixRowMajorOrder(float n00, float n01, float n02, float n03,
			                                 float n10, float n11, float n12, float n13,
			                                 float n20, float n21, float n22, float n23,
			                                 float n30, float n31, float n32, float n33) {
		pg().applyMatrix(n00, n01, n02, n03, n10, n11, n12, n13, n20, n21, n22,	n23, n30, n31, n32, n33);
	}	
	
	//
	
	@Override
	public void translate(float tx, float ty) {
		pg().translate(tx, ty);		
	}

	@Override
	public void translate(float tx, float ty, float tz) {
		pg().translate(tx, ty, tz);	
	}
	
	@Override
	public void rotate(float angle) {
		pg().rotate(angle);		
	}

	@Override
	public void rotateX(float angle) {
		pg().rotateX(angle);		
	}

	@Override
	public void rotateY(float angle) {
		pg().rotateY(angle);
	}

	@Override
	public void rotateZ(float angle) {
		pg().rotateZ(angle);
	}
	
	@Override
	public void rotate(float angle, float vx, float vy, float vz) {
		pg().rotate(angle, vx, vy, vz);
	}
	
	@Override
	public void scale(float s) {
		pg().scale(s);	
	}

	@Override
	public void scale(float sx, float sy) {
		pg().scale(sx, sy);	
	}

	@Override
	public void scale(float x, float y, float z) {
		pg().scale(x, y, z);
	}

	// 2. Associated objects	
	
	@Override
	public void registerJob(AbstractTimerJob job) {
		if (timersAreSingleThreaded())
			super.registerJob(job);
		else {
			job.setTimer(new TimerWrap(this, job));
			timerPool.add(job);
		}
	}
	
	public void setSingleThreadedTimers() {
		if( timersAreSingleThreaded() )
			return;
		
		boolean isActive;
		
		for ( AbstractTimerJob job : timerPool ) {
			long period = 0;
			boolean rOnce = false;
			isActive = job.isActive();
			if(isActive) {
				period = job.period();
				rOnce = job.timer().isSingleShot();
			}
			job.stop();
			job.setTimer(new SingleThreadedTaskableTimer(this, job));			
			if(isActive) {
				if(rOnce)
					job.runOnce(period);
				else
					job.run(period);
			}
		}
		
		singleThreadedTaskableTimers = true;		
		PApplet.println("single threaded timers set");
	}
	
	public void setJavaTimers() {
		if( !timersAreSingleThreaded() )
			return;
		
		boolean isActive;
		
		for ( AbstractTimerJob job : timerPool ) {
			long period = 0;
			boolean rOnce = false;
			isActive = job.isActive();
			if(isActive) {
				period = job.period();
				rOnce = job.timer().isSingleShot();
			}
			job.stop();
			job.setTimer(new TimerWrap(this, job));			
			if(isActive) {
				if(rOnce)
					job.runOnce(period);
				else
					job.run(period);
			}
		}	
		
		singleThreadedTaskableTimers = false;
		PApplet.println("awt timers set");
	}
	
	public void switchTimers() {
		if( timersAreSingleThreaded() )
			setJavaTimers();
		else
			setSingleThreadedTimers();
	}
	
	/**
	public void registerJob(AbstractTimerJob job) {
		if (timersAreSingleThreaded()) {
			super.registerJob(job);
			//registerJobInTimerPool(job);
			//PApplet.println("registering singleThreadedTaskableTimer " +  job.getClass() +  " in timer pool");
		}
		else {
			job.setTimer(new TimerWrap(this, job));
			//PApplet.println("creating new awt timer " +  job.getClass());
		}
	}
	*/	
	
	// 5. Drawing methods

	/**
	 * Internal use. Display various on-screen visual hints to be called from {@link #pre()}
	 * or {@link #draw()}.
	 */
	@Override
	protected void displayVisualHints() {		
		if (frameSelectionHintIsDrawn())
			drawSelectionHints();
		if (cameraPathsAreDrawn()) {
			pinhole().drawAllPaths();
			drawCameraPathSelectionHints();
		} else {
			pinhole().hideAllPaths();
		}
		if (dE.camMouseAction == MouseAction.ZOOM_ON_REGION)			
			drawZoomWindowHint();		
		if (dE.camMouseAction == MouseAction.SCREEN_ROTATE)
			drawScreenRotateLineHint();
		if (arpFlag) 
			drawArcballReferencePointHint();
		if (pupFlag) {
			Vector3D v = pinhole().projectedCoordinatesOf(pupVec);
			pg().pushStyle();		
			pg().stroke(255);
			pg().strokeWeight(3);
			drawCross(v.vec[0], v.vec[1]);
			pg().popStyle();
		}
	}	

	/**
	 * Paint method which is called just before your {@code PApplet.draw()}
	 * method. This method is registered at the PApplet and hence you don't need
	 * to call it.
	 * <p>
	 * Sets the processing camera parameters from {@link #pinhole()} and updates
	 * the frustum planes equations if {@link #enableFrustumEquationsUpdate(boolean)}
	 * has been set to {@code true}.
	 */
	public void pre() {
		if (isOffscreen()) return;		
		
		if ((width != pg().width) || (height != pg().height)) {
			width = pg().width;
			height = pg().height;				
			pinhole().setScreenWidthAndHeight(width, height);				
		} else {
			if ((currentCameraProfile().mode() == CameraProfile.Mode.THIRD_PERSON)
					&& (!pinhole().anyInterpolationIsStarted())) {
				pinhole().setPosition(avatar().cameraPosition());
				pinhole().setUpVector(avatar().upVector());
				pinhole().lookAt(avatar().target());
			}
		}

		preDraw();
	}

	/**
	 * Paint method which is called just after your {@code PApplet.draw()} method.
	 * This method is registered at the PApplet and hence you don't need to call
	 * it. Calls {@link #drawCommon()}.
	 * 
	 * @see #drawCommon()
	 */
	public void draw() {
		if (isOffscreen()) return;
		postDraw();
	}	
	
	@Override
	protected void invokeRegisteredMethod() {
     	// 3. Draw external registered method
			if (drawHandlerObject != null) {
				try {
					drawHandlerMethod.invoke(drawHandlerObject, new Object[] { this });
				} catch (Exception e) {
					PApplet.println("Something went wrong when invoking your "	+ drawHandlerMethodName + " method");
					e.printStackTrace();
				}
			}	
	}	

	/**
	 * This method should be called when using offscreen rendering 
	 * right after renderer.beginDraw().
   */	
	public void beginDraw() {
		if (isOffscreen()) {
			if (beginOffScreenDrawingCalls != 0)
				throw new RuntimeException(
						"There should be exactly one beginDraw() call followed by a "
								+ "endDraw() and they cannot be nested. Check your implementation!");			
			beginOffScreenDrawingCalls++;
						
			if ((currentCameraProfile().mode() == CameraProfile.Mode.THIRD_PERSON)
					&& (!pinhole().anyInterpolationIsStarted())) {
				pinhole().setPosition(avatar().cameraPosition());
				pinhole().setUpVector(avatar().upVector());
				pinhole().lookAt(avatar().target());
			}
			
			preDraw();	
		}
	}

	/**
	 * This method should be called when using offscreen rendering 
	 * right before renderer.endDraw(). Calls {@link #drawCommon()}.
	 * 
	 * @see #drawCommon() 
   */		
	public void endDraw() {
		beginOffScreenDrawingCalls--;
		
		if (beginOffScreenDrawingCalls != 0)
			throw new RuntimeException(
					"There should be exactly one beginDraw() call followed by a "
							+ "endDraw() and they cannot be nested. Check your implementation!");
		
		postDraw();
	}
	
  // 4. Scene dimensions
	
	/**
	@Override
	public float frameRate() {
		return parent.frameRate;
	}
	*/

	/**
	@Override
	public long frameCount() {
		return parent.frameCount;
	}
	*/

	// 6. Display of visual hints and Display methods		
	
	// 2. CAMERA	
	
	// 3. KEYFRAMEINTERPOLATOR CAMERA
	
	/**
	 * Sets the mouse grabber on selection hint {@code color}
	 * (drawn as a shooter target).
	 * 
	 * @see #drawSelectionHints()
	 */
  //public void setMouseGrabberOnSelectionHintColor(int color) { 	onSelectionHintColor = color; }
	
  /**
	 * Sets the mouse grabber off selection hint {@code color}
	 * (drawn as a shooter target).
	 * 
	 * @see #drawSelectionHints()
	 */  
	//public void setMouseGrabberOffSelectionHintColor(int color) { offSelectionHintColor = color;	}
	
	/**
	 * Returns the mouse grabber on selection hint {@code color}.
	 * 
	 * @see #drawSelectionHints()
	 */
	//public int mouseGrabberOnSelectionHintColor() {	return onSelectionHintColor;}
	
	/**
	 * Returns the mouse grabber off selection hint {@code color}.
	 * 
	 * @see #drawSelectionHints()
	 */
  //public int mouseGrabberOffSelectionHintColor() {return offSelectionHintColor;}
  
  /**
	 * Sets the mouse grabber on selection hint {@code color} for camera paths
	 * (drawn as a shooter target).
	 * 
	 * @see #drawCameraPathSelectionHints()
	 */
  // public void setMouseGrabberCameraPathOnSelectionHintColor(int color) {	cameraPathOnSelectionHintColor = color; }
	
  /**
	 * Sets the mouse grabber off selection hint {@code color} for camera paths
	 * (drawn as a shooter target).
	 * 
	 * @see #drawCameraPathSelectionHints()
	 */
	//public void setMouseGrabberCameraPathOffSelectionHintColor(int color) {	cameraPathOffSelectionHintColor = color;	}
	
	/**
	 * Returns the mouse grabber on selection hint {@code color} for camera paths.
	 * 
	 * @see #drawCameraPathSelectionHints()
	 */
	//public int mouseGrabberCameraPathOnSelectionHintColor() {	return cameraPathOnSelectionHintColor;	}
	
	/**
	 * Returns the mouse grabber off selection hint {@code color} for camera paths.
	 * 
	 * @see #drawCameraPathSelectionHints()
	 */
  //public int mouseGrabberCameraPathOffSelectionHintColor() {	return cameraPathOffSelectionHintColor;	}
	
	public PGraphics pg() {
		/**
		if( renderer() instanceof P5Renderer )
			return ((P5Renderer)renderer()).pg();
		*/
		if( renderer() instanceof P5Renderer2D )
			return ((P5Renderer2D)renderer()).pg();
		if( renderer() instanceof P5Renderer3D )
			return ((P5Renderer3D)renderer()).pg();
		//if( renderer() instanceof P5RendererJava2D )
		return ((P5RendererJava2D)renderer()).pg();
	}
	
	public PGraphicsJava2D pgj2d() {
		if (pg() instanceof PGraphicsJava2D)
			return (PGraphicsJava2D) pg();
		else 
			throw new RuntimeException("pGraphics is not instance of PGraphicsJava2D");		
	}
	
	public PGraphicsOpenGL pggl() {
		if (pg() instanceof PGraphicsOpenGL)
			return (PGraphicsOpenGL) pg();
		else 
			throw new RuntimeException("pGraphics is not instance of PGraphicsOpenGL");		
	}
	
	public PGraphics2D pg2d() {
		if (pg() instanceof PGraphics2D)
			return ((P5Renderer2D) renderer()).pg2d();
		else 
			throw new RuntimeException("pGraphics is not instance of PGraphics2D");		
	}
	
	public PGraphics3D pg3d() {
		if (pg() instanceof PGraphics3D)
			return ((P5Renderer3D) renderer()).pg3d();
		else 
			throw new RuntimeException("pGraphics is not instance of PGraphics3D");		
	}
	
	@Override
  public void disableDepthTest() {
		pg().hint(PApplet.DISABLE_DEPTH_TEST);
	}
	
	@Override
	public void enableDepthTest() {
		pg().hint(PApplet.ENABLE_DEPTH_TEST);
	}
	
	@Override
	protected void drawSelectionHints() {
		for (DeviceGrabbable mg : msGrabberPool) {
			if(mg instanceof InteractiveFrame) {
				InteractiveFrame iF = (InteractiveFrame) mg;// downcast needed
				if (!iF.isInCameraPath()) {
					Vector3D center = pinhole().projectedCoordinatesOf(iF.position());
					if (mg.grabsMouse()) {						
						pg().pushStyle();
					  //pg3d.stroke(mouseGrabberOnSelectionHintColor());
						pg().stroke(pg().color(0, 255, 0));
						pg().strokeWeight(2);
						drawShooterTarget(center, (iF.grabsMouseThreshold() + 1));
						pg().popStyle();					
					}
					else {						
						pg().pushStyle();
					  //pg3d.stroke(mouseGrabberOffSelectionHintColor());
						pg().stroke(pg().color(240, 240, 240));
						pg().strokeWeight(1);
						drawShooterTarget(center, iF.grabsMouseThreshold());
						pg().popStyle();
					}
				}
			}
		}
	}

	@Override
	protected void drawCameraPathSelectionHints() {
		for (DeviceGrabbable mg : msGrabberPool) {
			if(mg instanceof InteractiveFrame) {
				InteractiveFrame iF = (InteractiveFrame) mg;// downcast needed
				if (iF.isInCameraPath()) {
					Vector3D center = pinhole().projectedCoordinatesOf(iF.position());
					if (mg.grabsMouse()) {
						pg().pushStyle();						
					  //pg3d.stroke(mouseGrabberCameraPathOnSelectionHintColor());
						pg().stroke(pg().color(0, 255, 255));
						pg().strokeWeight(2);
						drawShooterTarget(center, (iF.grabsMouseThreshold() + 1));
						pg().popStyle();
					}
					else {
						pg().pushStyle();
					  //pg3d.stroke(mouseGrabberCameraPathOffSelectionHintColor());
						pg().stroke(pg().color(255, 255, 0));
						pg().strokeWeight(1);
						drawShooterTarget(center, iF.grabsMouseThreshold());
						pg().popStyle();
					}
				}
			}
		}
	}	
	
	@Override
	public int width() {
		return pg().width;
	}

	@Override
	public int height() {
		return pg().height;
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
	 * Enables Proscene keyboard handling.
	 * 
	 * @see #keyboardIsHandled()
	 * @see #enableMouseHandling()
	 * @see #disableKeyboardHandling()
	 */
	@Override
	public void enableKeyboardHandling() {
		if( !this.keyboardIsHandled() ) {
			super.enableKeyboardHandling();
			parent.registerMethod("keyEvent", dE);
		}
	}

	/**
	 * Disables Proscene keyboard handling.
	 * 
	 * @see #keyboardIsHandled()
	 */
	@Override
	public void disableKeyboardHandling() {
		if( this.keyboardIsHandled() ) {
			super.disableKeyboardHandling();
			parent.unregisterMethod("keyEvent", dE);
		}
	}
		
	/**
	 * Displays global keyboard bindings.
	 * 
	 * @param onConsole if this flag is true displays the help on console.
	 * Otherwise displays it on the applet
	 * 
	 * @see #displayGlobalHelp()
	 */
	@Override
	public void displayGlobalHelp(boolean onConsole) {
		if (onConsole)
			PApplet.println(globalHelp());
		else { //on applet
			pg().textFont(parent.createFont("Arial", 12));
			//pGraphics().textMode(SCREEN);
			//TODO test me!
			beginScreenDrawing();
			pg().fill(0,255,0);
			pg().textLeading(20);
			pg().text(globalHelp(), 10, 10, (pg().width-20), (pg().height-20));
			endScreenDrawing();
		}
	}	
	
	/**
	 * Displays the {@link #currentCameraProfile()} bindings.
	 * 
	 * @param onConsole if this flag is true displays the help on console.
	 * Otherwise displays it on the applet
	 * 
	 * @see #displayCurrentCameraProfileHelp()
	 */
	@Override
	public void displayCurrentCameraProfileHelp(boolean onConsole) {
		if (onConsole)
			PApplet.println(currentCameraProfileHelp());
		else { //on applet
			pg().textFont(parent.createFont("Arial", 12));
			//TODO test me!
			beginScreenDrawing();
			pg().fill(0,255,0);
			pg().textLeading(20);
			pg().text(currentCameraProfileHelp(), 10, 10, (pg().width-20), (pg().height-20));
			endScreenDrawing();
		}
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
	 * Enables Proscene mouse handling.
	 * 
	 * @see #mouseIsHandled()
	 * @see #disableMouseHandling()
	 * @see #enableKeyboardHandling()
	 */
	@Override
	public void enableMouseHandling() {
		if( !this.mouseIsHandled() ) {
			super.enableMouseHandling();
			parent.registerMethod("mouseEvent", dE);
		}
	}

	/**
	 * Disables Proscene mouse handling.
	 * 
	 * @see #mouseIsHandled()
	 */
	@Override
	public void disableMouseHandling() {
		if( this.mouseIsHandled() ) {
			super.disableMouseHandling();
			parent.unregisterMethod("mouseEvent", dE);
		}
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
	 * 
	 * @see #removeDrawHandler()
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
	@Override
	protected void performAnimation() {
		if( !animationTimer.isTrigggered() ) {
			animatedFrameWasTriggered = false;
			return;
		}
		
		animatedFrameWasTriggered = true;		
		if (animateHandlerObject != null) {
			try {
				animateHandlerMethod.invoke(animateHandlerObject, new Object[] { this });
			} catch (Exception e) {
				PApplet.println("Something went wrong when invoking your " + animateHandlerMethodName + " method");
				e.printStackTrace();
			}
		}
		else
			animate();
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
	
	//
	
	/**
	 * Returns the coordinates of the 3D point located at {@code pixel} (x,y) on
	 * screen.
	 */
	@Override
	protected Camera.WorldPoint pointUnderPixel(Point pixel) {
		float[] depth = new float[1];
		PGL pgl = pggl().beginPGL();
		pgl.readPixels((int) pixel.x, (camera().screenHeight() - (int) pixel.y), 1, 1, PGL.DEPTH_COMPONENT, PGL.FLOAT, FloatBuffer.wrap(depth));		
		pggl().endPGL();		
		Vector3D point = new Vector3D((int) pixel.x, (int) pixel.y, depth[0]);		
		point = camera().unprojectedCoordinatesOf(point);
		return camera().new WorldPoint(point, (depth[0] < 1.0f));
	}	
}