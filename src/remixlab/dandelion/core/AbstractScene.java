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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import remixlab.dandelion.geom.*;
import remixlab.dandelion.renderer.*;
import remixlab.dandelion.util.*;
import remixlab.tersehandling.core.*;
import remixlab.tersehandling.generic.event.*;
import remixlab.tersehandling.generic.profile.*;
import remixlab.tersehandling.event.*;

public abstract class AbstractScene implements Constants, Grabbable {	
	protected boolean dottedGrid;	
	
  //O B J E C T S
	protected Renderable renderer;
	protected Viewport vport;
	protected Trackable trck;
	public boolean avatarIsInteractiveFrame;
	protected boolean avatarIsInteractiveAvatarFrame;
	
  //E X C E P T I O N H A N D L I N G
	protected int startCoordCalls;
	
  // T i m e r P o o l
  //T I M E R S
  protected boolean singleThreadedTaskableTimers;
	protected ArrayList<AbstractTimerJob> timerPool;
	
	//TerseHandler
	protected TerseHandler terseHandler;

	// D I S P L A Y F L A G S
	protected boolean axisIsDrwn; // world axis
	protected boolean gridIsDrwn; // world XY grid
	protected boolean frameSelectionHintIsDrwn;
	protected boolean viewportPathsAreDrwn;
	
	// LEFT vs RIGHT_HAND
	protected boolean rightHanded;
	
  //A N I M A T I O N
	protected SeqTimer animationTimer;
	protected boolean animationStarted;
	public boolean animatedFrameWasTriggered;
	protected long animationPeriod;	
	
	// S I Z E
	protected int width, height;
	
	//offscreen
	public Point upperLeftCorner;
	protected boolean offscreen;
	
	/**
   * The system variables <b>cursorX</b> and <b>cursorY</b> always contains the current horizontal
   * and vertical coordinates of the mouse.
   */ 
  public int cursorX, cursorY, pcursorX, pcursorY;
	
	protected long frameCount;
	protected float frameRate;
	protected long frameRateLastNanos;
	
	public AbstractScene() {		
		frameCount = 0;
		frameRate = 10;
		frameRateLastNanos = 0;
		
	  // E X C E P T I O N H A N D L I N G
	  startCoordCalls = 0;
			
	  // 1 ->
		//To define the timers to be used pass the flag in the constructor?
		singleThreadedTaskableTimers = true;
		//drawing timer pool
		timerPool = new ArrayList<AbstractTimerJob>();
		
		terseHandler = new TerseHandler();
		
		setDottedGrid(true);
		setRightHanded();
	}
	
	protected void setRenderer(Renderable r) {
		renderer = r;
	}
		
	public Renderable renderer() {
		return renderer;
	}	
	
	public boolean gridIsDotted() {
		return dottedGrid;
	}
	
	public void setDottedGrid(boolean dotted) {
		dottedGrid = dotted;
	}
	
	/**
	 * Returns {@code true} if this Scene is associated to an offscreen 
	 * renderer and {@code false} otherwise.
	 * 
	 * @see #Scene(PApplet, PGraphicsOpenGL)
	 */	
	public boolean isOffscreen() {
		return offscreen;
	}	
	
	// E V E N T   HA N D L I N G
	
	@Override
	public boolean grabsAgent(Agent agent) {
		return agent.grabber() == this;
	}
	
	@Override
	public boolean checkIfGrabsInput(TerseEvent event) {		
		return (event instanceof GenericKeyboardEvent || event instanceof GenericClickEvent);
	}
	
	/**
	 * Internal method. Handles the different global keyboard actions.
	 */
	@Override
	public void performInteraction(TerseEvent event) {
		if( !(event instanceof GenericClickEvent) && ! (event instanceof GenericKeyboardEvent))
			return;
		
		Actionable<DandelionAction> a=null;
		
		if(event instanceof GenericClickEvent<?>)
		  a = (ClickAction) ((GenericClickEvent<?>) event).action();
		if(event instanceof GenericKeyboardEvent<?>)
			a =  (KeyboardAction) ((GenericKeyboardEvent<?>) event).action();		
		if(a == null) return;
		DandelionAction id = a.referenceAction();
		
		if( !id.is2D() && this.is2D() )
			return;
		Vec trans;
		switch (id) {
		case ADD_KEYFRAME_TO_PATH_1:
			viewport().addKeyFrameToPath(1);
			break;
		case DELETE_PATH_1:
			viewport().deletePath(1);
			break;
		case PLAY_PATH_1:
			viewport().playPath(1);
			break;
		case ADD_KEYFRAME_TO_PATH_2:
			viewport().addKeyFrameToPath(2);
			break;
		case DELETE_PATH_2:
			viewport().deletePath(2);
			break;
		case PLAY_PATH_2:
			viewport().playPath(2);
			break;
		case ADD_KEYFRAME_TO_PATH_3:
			viewport().addKeyFrameToPath(3);
			break;
		case DELETE_PATH_3:
			viewport().deletePath(3);
			break;
		case PLAY_PATH_3:
			viewport().playPath(3);
			break;
		case DRAW_AXIS:
			toggleAxisIsDrawn();
			break;
		case DRAW_GRID:
			toggleGridIsDrawn();
			break;
		case CAMERA_TYPE:
			toggleCameraType();
			break;
		case CAMERA_KIND:
			toggleCameraKind();
			break;
		case ANIMATION:
			toggleAnimation();
			break;
		case GLOBAL_HELP:
			displayInfo();
			break;
		case EDIT_VIEWPORT_PATH:
			toggleViewportPathsAreDrawn();
			break;
		case DRAW_FRAME_SELECTION_HINT:
			toggleFrameSelectionHintIsDrawn();
			break;
		case SHOW_ALL:
			showAll();
			break;
		case MOVE_VIEWPORT_LEFT:
			trans = new Vec(-10.0f * viewport().flySpeed(), 0.0f, 0.0f);
			if(this.is3D())
				trans.div(camera().frame().magnitude());
			viewport().frame().translate(viewport().frame().inverseTransformOf(trans));			
			break;
		case MOVE_VIEWPORT_RIGHT:
			trans = new Vec(10.0f * viewport().flySpeed(), 0.0f, 0.0f);
			if(this.is3D())
				trans.div(camera().frame().magnitude());
			viewport().frame().translate(viewport().frame().inverseTransformOf(trans));			
			break;
		case MOVE_VIEWPORT_UP:
			trans = viewport().frame().inverseTransformOf(new Vec(0.0f, isRightHanded() ? 10.0f : -10.0f * viewport().flySpeed(), 0.0f));
			if(this.is3D())
				trans.div(camera().frame().magnitude());
			viewport().frame().translate(trans);					  
			break;
		case MOVE_VIEWPORT_DOWN:
			trans = viewport().frame().inverseTransformOf(new Vec(0.0f, isRightHanded() ? -10.0f : 10.0f * viewport().flySpeed(), 0.0f));
			if(this.is3D())
				trans.div(camera().frame().magnitude());
			viewport().frame().translate(trans);			
			break;
		case INCREASE_ROTATION_SENSITIVITY:
			viewport().setRotationSensitivity(viewport().rotationSensitivity() * 1.2f);
			break;
		case DECREASE_ROTATION_SENSITIVITY:
			viewport().setRotationSensitivity(viewport().rotationSensitivity() / 1.2f);
			break;
		case INCREASE_CAMERA_FLY_SPEED:
			((Camera) viewport()).setFlySpeed(((Camera) viewport()).flySpeed() * 1.2f);
			break;
		case DECREASE_CAMERA_FLY_SPEED:
			((Camera) viewport()).setFlySpeed(((Camera) viewport()).flySpeed() / 1.2f);
			break;
		case INCREASE_AVATAR_FLY_SPEED:
			if (avatar() != null)
				if (avatarIsInteractiveFrame)
					((InteractiveFrame) avatar()).setFlySpeed(((InteractiveFrame) avatar()).flySpeed() * 1.2f);
			break;
		case DECREASE_AVATAR_FLY_SPEED:
			if (avatar() != null)
				if (avatarIsInteractiveFrame)
					((InteractiveFrame) avatar()).setFlySpeed(((InteractiveFrame) avatar()).flySpeed() / 1.2f);
			break;
		case INCREASE_AZYMUTH:
			if (avatar() != null)
				if (avatarIsInteractiveAvatarFrame)
					((InteractiveAvatarFrame) avatar()).setAzimuth(((InteractiveAvatarFrame) avatar()).azimuth() + PI / 64);
			break;
		case DECREASE_AZYMUTH:
			if (avatar() != null)
				if (avatarIsInteractiveAvatarFrame)
					((InteractiveAvatarFrame) avatar()).setAzimuth(((InteractiveAvatarFrame) avatar()).azimuth() - PI / 64);
			break;
		case INCREASE_INCLINATION:
			if (avatar() != null)
				if (avatarIsInteractiveAvatarFrame)
					((InteractiveAvatarFrame) avatar()).setInclination(((InteractiveAvatarFrame) avatar()).inclination() + PI / 64);
			break;
		case DECREASE_INCLINATION:
			if (avatar() != null)
				if (avatarIsInteractiveAvatarFrame)
					((InteractiveAvatarFrame) avatar()).setInclination(((InteractiveAvatarFrame) avatar()).inclination() - PI / 64);
			break;
		case INCREASE_TRACKING_DISTANCE:
			if (avatar() != null)
				if (avatarIsInteractiveAvatarFrame)
					((InteractiveAvatarFrame) avatar()).setTrackingDistance(((InteractiveAvatarFrame) avatar()).trackingDistance() + radius() / 50);
			break;
		case DECREASE_TRACKING_DISTANCE:
			if (avatar() != null)
				if (avatarIsInteractiveAvatarFrame)
					((InteractiveAvatarFrame) avatar()).setTrackingDistance(((InteractiveAvatarFrame) avatar()).trackingDistance() - radius() / 50);
			break;
		case INTERPOLATE_TO_FIT:
			viewport().interpolateToFitScene();
			break;
		case RESET_ARP:		  
			viewport().setArcballReferencePoint(new Vec(0, 0, 0));
			//looks horrible, but works ;)
			viewport().frame().arpFlag = true;
			viewport().frame().timerFx.runOnce(1000);				
			break;
		default: 
			System.out.println("Action cannot be handled here!");
    break;
		}
	}
	
	/**
	 * Convenience function that simply calls {@code displayCurrentCameraProfileHelp(true)}.
	 * 
	 * @see #displayInfo(boolean)
	 */	
	public String info() {
		String description = new String();
		description += "Agents' info\n";
		int index = 1;
		for( Agent agent : terseHandler().agents() ) {
			description += index;
			description += ". ";
			description += agent.info();
			index++;
		}
		return description;
	}
	
	public void displayInfo() {
		displayInfo(true);
	}
	
	public void displayInfo(boolean onConsole) {
		if (onConsole)
			System.out.println(info());
		else
			AbstractScene.showMissingImplementationWarning("displayInfo");
	}
	
	/**
	 * Returns the {@link PApplet#width} to {@link PApplet#height} aspect ratio of
	 * the processing display window.
	 */
	public float aspectRatio() {
		return (float) width() / (float) height();
	}
	
	// D R A W I N G   M E T H O D S
	
	public void preDraw() {
		if ( avatar() != null	&& (!viewport().anyInterpolationIsStarted() ) ) {
			viewport().setPosition(avatar().viewportPosition());
			viewport().setUpVector(avatar().upVector());
			viewport().lookAt(avatar().target());
		}
		 
		updateFrameRate();		
		bindMatrices();
		if (frustumEquationsUpdateIsEnable())
			viewport().updateFrustumEquations();
	}
	
	/**
	 * Internal method. Called by {@link #draw()} and {@link #endDraw()}.
	 * <p>
	 * First performs any scheduled animation, then calls {@link #proscenium()}
	 * which is the main drawing method that could be overloaded. Then, if
	 * there's an additional drawing method registered at the Scene, calls it (see
	 * {@link #addDrawHandler(Object, String)}). Finally, displays the
	 * {@link #displayGlobalHelp()}, the axis, the grid, the interactive frames' selection
	 * hints and camera paths, and some visual hints (such {@link #drawZoomWindowHint()},
	 * {@link #drawScreenRotateLineHint()} and {@link #drawArcballReferencePointHint()})
	 * according to user interaction and flags.
	 * 
	 * @see #proscenium()
	 * @see #addDrawHandler(Object, String)
	 * @see #gridIsDrawn()
	 * @see #axisIsDrwn
	 * @see #addDrawHandler(Object, String)
	 * @see #addAnimationHandler(Object, String)
	 */
	public void postDraw() {	
		//updateFrameRate();
		
		// 1
		updateCursor();
			
		// 2. timers
		if (timersAreSingleThreaded())
			handleTimers();
		
		// 3. Agents
		terseHandler().handle();
		
	  // 4. Alternative use only
		proscenium();
		
		// 5. Animation
		if( animationIsStarted() )
			performAnimation(); //abstract	
		
		// 6. Draw external registered method (only in java sub-classes)
		invokeRegisteredMethod(); // abstract
    
    // 7. Grid and axis drawing
 		if (gridIsDrawn()) {
 			if(gridIsDotted())
 				drawDottedGrid(viewport().sceneRadius());
 			else
 				drawGrid(viewport().sceneRadius());
 		}
 		if (axisIsDrawn())
 			drawAxis(viewport().sceneRadius());		
 		
    // 8. Display visual hints
 		displayVisualHints(); // abstract
	}
	
	public TerseHandler terseHandler() {
		return terseHandler;
	}
	
	protected abstract void updateCursor();
	
	private void updateFrameRate() {
		long now = System.nanoTime();
		
		if(frameCount > 1) {
		  // update the current frameRate
	     double rate = 1000000.0 / ((now - frameRateLastNanos) / 1000000.0);
	     float instantaneousRate = (float) rate / 1000.0f;
	     frameRate = (frameRate * 0.9f) + (instantaneousRate * 0.1f);
		}
			
		frameRateLastNanos = now;
		frameCount++;
	}
	
	protected abstract void invokeRegisteredMethod();
	
	/**
	 * Bind processing matrices to proscene matrices.
	 */	
	protected void bindMatrices() {
		// we should simply go:
		renderer.bindMatrices();
	}

	protected void setProjectionModelViewMatrix() {
		viewport().computeProjectionViewMatrix();
	}
	
	// Try to optimize assignments in all three matrix getters?
	// Requires allowing returning references as well as copies of the matrices,
	// but it seems overkill. 
	public Mat getModelViewMatrix() {		
		Mat modelview;
  	modelview = getMatrix();
  	modelview.preApply(getViewMatrix());
  	return modelview;
	}
	
	public Mat getViewMatrix() {
		Mat view = viewport().getViewMatrix();  	
  	return view;
	}
	
	public Mat getModelMatrix() {		
		Mat model;
  	model = getMatrix();  	
  	return model;
	}
	
	public Mat getProjectionMatrix() {		
		Mat projection;  	
  	projection = getProjection();
  	return projection;
	}
	
	/**
	 * This method restores the matrix mode.
	 */
	public Mat getModelViewProjectionMatrix() {		
		Mat PVM;  	
  	PVM = getMatrix();//model  	
    //PVM.preApply(camera().projectionViewMat);
  	PVM.preApply(viewport().getProjectionViewMatrix());  	
  	return PVM;
	}
	
	protected abstract void displayVisualHints();
	
	protected void handleTimers() {
		for ( AbstractTimerJob tJob : timerPool )
			if (tJob.timer() != null)
				if (tJob.timer() instanceof SeqTaskableTimer)
					((SeqTaskableTimer)tJob.timer()).execute();
	}	
	
  //1. Scene overloaded
	
	/**
	 * This method is called before the first drawing happen and should be overloaded to
	 * initialize stuff not initialized in {@code PApplet.setup()}. The default
	 * implementation is empty.
	 * <p>
	 * Typical usage include {@link #viewport()} initialization ({@link #showAll()})
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
	
	// ANIMATION
	
	protected abstract void performAnimation();
	
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
	public long animationPeriod() {
		return animationPeriod;
	}
	
	/**
	 * Convenience function that simply calls {@code setAnimationPeriod(period, true)}.
	 * 
	 * @see #setAnimationPeriod(float, boolean)
	 */
	public void setAnimationPeriod(long period) {
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
	public void setAnimationPeriod(long period, boolean restart) {
		if(period>0) {
			animationPeriod = period;
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
	public void stopAnimation()	{
		animationStarted = false;
		animatedFrameWasTriggered = false;
		animationTimer.stop();
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
	public void startAnimation() {
		animationStarted = true;	
		animationTimer.run(animationPeriod);
	}
	
	/**
	 * Restart the animation.
	 * <p>
	 * Simply calls {@link #stopAnimation()} and then {@link #startAnimation()}.
	 */
  public void restartAnimation() {
  	stopAnimation();
  	startAnimation();
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
	 * Calls {@link #startAnimation()} or {@link #stopAnimation()}, depending on
	 * {@link #animationIsStarted()}.
	 */
	public void toggleAnimation() {
		if (animationIsStarted()) stopAnimation(); else startAnimation();
	}
	
  // Event registration
		
	// WRAPPERS
	
  public boolean is2D() {
  	return renderer.is2D();
  }
	
	public boolean is3D() {
		return renderer.is3D();
	}
	
	/**
	 * Push a copy of the modelview matrix onto the stack.
   */
	public void pushMatrix() {
		if( renderer instanceof StackRenderer )
			renderer.pushMatrix();
		else
			AbstractScene.showMissingImplementationWarning("pushMatrix");
	}
	
	/**
	 * Replace the current modelview matrix with the top of the stack.
	 */
	public void popMatrix() {
		if( renderer instanceof StackRenderer )
			renderer.popMatrix();
		else
			AbstractScene.showMissingImplementationWarning("popMatrix");
	}
	
	/**
	 * Push a copy of the projection matrix onto the stack.
   */
	public void pushProjection() {
		if( renderer instanceof StackRenderer )
			renderer.pushProjection();
		else
			AbstractScene.showMissingImplementationWarning("pushProjection");
	}
	
	/**
	 * Replace the current projection matrix with the top of the stack.
	 */
	public void popProjection() {
		if( renderer instanceof StackRenderer )
			renderer.popProjection();
		else
			AbstractScene.showMissingImplementationWarning("popProjection");
	}
	
  /**
   * Translate in X and Y.
   */
  public void translate(float tx, float ty) {    
  	if( renderer instanceof StackRenderer )
  		renderer.translate(tx, ty);
  	else
			AbstractScene.showMissingImplementationWarning("translate");
  }

  /**
   * Translate in X, Y, and Z.
   */
  public void translate(float tx, float ty, float tz) {
  	if( renderer instanceof StackRenderer )
  		renderer.translate(tx, ty, tz);
  	else
			AbstractScene.showMissingImplementationWarning("translate");
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
  	if( renderer instanceof StackRenderer )
  		renderer.rotate(angle);
  	else
			AbstractScene.showMissingImplementationWarning("rotate");
  }

  /**
   * Rotate around the X axis.
   */
  public void rotateX(float angle) { 
  	if( renderer instanceof StackRenderer )
  		renderer.rotateX(angle);
  	else
			AbstractScene.showMissingImplementationWarning("rotateX");
  }

  /**
   * Rotate around the Y axis.
   */
  public void rotateY(float angle) {
  	if( renderer instanceof StackRenderer )
  		renderer.rotateY(angle);
  	else
  		AbstractScene.showMissingImplementationWarning("rotateY");
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
  	if( renderer instanceof StackRenderer )
  		renderer.rotateZ(angle);
  	else
  		AbstractScene.showMissingImplementationWarning("rotateZ");
  }

  /**
   * Rotate about a vector in space. Same as the glRotatef() function.
   */
  public void rotate(float angle, float vx, float vy, float vz) {
  	if( renderer instanceof StackRenderer )
  		renderer.rotate(angle, vx, vy, vz);
  	else
  		AbstractScene.showMissingImplementationWarning("rotate");
  }

  /**
   * Scale in all dimensions.
   */
  public void scale(float s) {
  	if( renderer instanceof StackRenderer )
  		renderer.scale(s);
  	else
  		AbstractScene.showMissingImplementationWarning("scale");
  }

  /**
   * Scale in X and Y. Equivalent to scale(sx, sy, 1).
   *
   * Not recommended for use in 3D, because the z-dimension is just
   * scaled by 1, since there's no way to know what else to scale it by.
   */
  public void scale(float sx, float sy) {
  	if( renderer instanceof StackRenderer )
  		renderer.scale(sx, sy);
  	else
  		AbstractScene.showMissingImplementationWarning("scale");
  }

  /**
   * Scale in X, Y, and Z.
   */
  public void scale(float x, float y, float z) {
  	if( renderer instanceof StackRenderer )
  		renderer.scale(x, y, z);
  	else
  		AbstractScene.showMissingImplementationWarning("scale");
  }  
  
  /**
   * Set the current modelview matrix to identity.
   */
  public void resetMatrix() {
  	if( renderer instanceof StackRenderer )
  		renderer.resetMatrix();
  	else
  		AbstractScene.showMissingImplementationWarning("resetMatrix");
  }
  
  /**
   * Set the current projection matrix to identity.
   */
  public void resetProjection() {
  	if( renderer instanceof StackRenderer )
  		renderer.resetProjection();
  	else
  		AbstractScene.showMissingImplementationWarning("resetProjection");
  }  
  
  public void applyMatrix(Mat source) {
  	if( renderer instanceof StackRenderer )
  		renderer.applyMatrix(source);
  	else
  		AbstractScene.showMissingImplementationWarning("applyMatrix");
  }
  
  public void applyProjection(Mat source) {
  	if( renderer instanceof StackRenderer )
  		renderer.applyProjection(source);
  	else
  		AbstractScene.showMissingImplementationWarning("applyProjection");
  }

  /**
   * Apply a 4x4 modelview matrix.
   */
  public void applyMatrixRowMajorOrder(float n00, float n01, float n02, float n03,
                                       float n10, float n11, float n12, float n13,
                                       float n20, float n21, float n22, float n23,
                                       float n30, float n31, float n32, float n33) {    
  	renderer.applyMatrixRowMajorOrder(n00, n01, n02, n03, n10, n11, n12, n13, n20, n21, n22, n23, n30, n31, n32, n33);
  }
  
  /**
   * Apply a 4x4 projection matrix.
   */
  public void applyProjectionRowMajorOrder(float n00, float n01, float n02, float n03,
                                       float n10, float n11, float n12, float n13,
                                       float n20, float n21, float n22, float n23,
                                       float n30, float n31, float n32, float n33) {    
  	renderer.applyProjectionRowMajorOrder(n00, n01, n02, n03, n10, n11, n12, n13, n20, n21, n22, n23, n30, n31, n32, n33);
  }
  
  /**
  public void frustum(float left, float right, float bottom, float top, float znear, float zfar) {
  	renderer.frustum(left, right, bottom, top, znear, zfar);
  }
  */

  public Mat getMatrix() {
  	if( renderer instanceof StackRenderer )
  		return renderer.getMatrix();
  	else {
  		AbstractScene.showMissingImplementationWarning("getMatrix");
  		return null;
  	}
  }
  
  public Mat getProjection() {
  	if( renderer instanceof StackRenderer )
  		return renderer.getProjection();
  	else {
  		AbstractScene.showMissingImplementationWarning("getProjection");
  		return null;
  	}
  }

  /**
   * Copy the current modelview matrix into the specified target.
   * Pass in null to create a new matrix.
   */
  public Mat getMatrix(Mat target) {
  	if( renderer instanceof StackRenderer )
  		return renderer.getMatrix(target);
  	else {
  		AbstractScene.showMissingImplementationWarning("getMatrix");
  		return null;
  	}
  }
  
  /**
   * Copy the current projection matrix into the specified target.
   * Pass in null to create a new matrix.
   */
  public Mat getProjection(Mat target) {
  	if( renderer instanceof StackRenderer )
  		return renderer.getProjection(target);
  	else {
  		AbstractScene.showMissingImplementationWarning("getProjection");
  		return null;
  	}
  }

  /**
   * Set the current modelview matrix to the contents of another.
   */
  public void setMatrix(Mat source) {
  	renderer.setMatrix(source);
  }
  
  /**
   * Set the current projection matrix to the contents of another.
   */
  public void setProjection(Mat source) {
  	renderer.setProjection(source);
  }

  /**
   * Print the current modelview matrix.
   */
  public void printMatrix() {
  	if( renderer instanceof StackRenderer )
  		renderer.printMatrix();
  	else
  		AbstractScene.showMissingImplementationWarning("printMatrix");
  }
  
  /**
   * Print the current projection matrix.
   */
  public void printProjection() {
  	if( renderer instanceof StackRenderer )
  		renderer.printProjection();
  	else
  		AbstractScene.showMissingImplementationWarning("printMatrix");
  }
  
  /**
	 * Draws a cylinder of width {@code w} and height {@code h}, along the 
	 * positive {@code z} axis. 
	 */
  public void cylinder(float w, float h) {
  	renderer.cylinder(w, h);
  }
  
  public void cone(int detail, float x, float y, float r, float h) {
  	renderer.cone(detail, x, y, r, h);
  }
  
  public void cone(int detail, float x, float y, float r1, float r2, float h) {
  	renderer.cone(detail, x, y, r1, r2, h);
  }
  
  public void drawAxis(float length) {
  	renderer.drawAxis(length);
  }
  
  public void drawGrid(float size, int nbSubdivisions) {
  	renderer.drawGrid(size, nbSubdivisions);
  }
  
  public void drawDottedGrid(float size, int nbSubdivisions) {
  	renderer.drawDottedGrid(size, nbSubdivisions);
  }
    
  public void drawWindow(Window window, float scale) {
  	renderer.drawWindow(window, scale);
  }
  
  public void drawCamera(Camera camera, boolean drawFarPlane, float scale) {
  	renderer.drawCamera(camera, drawFarPlane, scale);
  }
  
  public void drawKFIViewport(float scale) {
  	renderer.drawKFIViewport(scale);
  }
  
  public void drawZoomWindowHint() {
  	renderer.drawZoomWindowHint();
  }
  
  public void drawScreenRotateLineHint() {
  	renderer.drawScreenRotateLineHint();
  }
  
  public void drawArcballReferencePointHint() {
  	renderer.drawArcballReferencePointHint();
  }
  
  public void drawCross(float px, float py, float size) {
  	renderer.drawCross(px, py, size);
  }
  
  public void drawFilledCircle(int subdivisions, Vec center, float radius) {
  	renderer.drawFilledCircle(subdivisions, center, radius);
  }
  
  public void drawFilledSquare(Vec center, float edge) {
  	renderer.drawFilledSquare(center, edge);
  }
  
  public void drawShooterTarget(Vec center, float length) {
		renderer.drawShooterTarget(center, length);
	}
	
	public void drawPath(List<RefFrame> path, int mask, int nbFrames, int nbSteps, float scale) {
		renderer.drawPath(path, mask, nbFrames, nbSteps, scale);
	}
	
  public void beginScreenDrawing() {
  	if (startCoordCalls != 0)
			throw new RuntimeException("There should be exactly one beginScreenDrawing() call followed by a "
							                 + "endScreenDrawing() and they cannot be nested. Check your implementation!");
		
		startCoordCalls++;
		
		disableDepthTest();
		renderer.beginScreenDrawing();
  }
	
	public void endScreenDrawing() {
		startCoordCalls--;
		if (startCoordCalls != 0)
			throw new RuntimeException("There should be exactly one beginScreenDrawing() call followed by a "
							                 + "endScreenDrawing() and they cannot be nested. Check your implementation!");
		
		renderer.endScreenDrawing();
		enableDepthTest();
	}
	
	public abstract void disableDepthTest();
	
	public abstract void enableDepthTest();
	
	// end wrapper
	
	public boolean isLeftHanded() {
		return !rightHanded;
	}
	
	public boolean isRightHanded() {
		return rightHanded;
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
	public void applyTransformation(RefFrame frame) {
		if( is2D() ) {
			translate(frame.translation().x(), frame.translation().y());
			rotate(frame.rotation().angle());
			scale(frame.scaling().x(), frame.scaling().y());
		}
		else {
			translate( frame.translation().vec[0], frame.translation().vec[1], frame.translation().vec[2] );
			rotate( frame.rotation().angle(), ((Quat)frame.rotation()).axis().vec[0], ((Quat)frame.rotation()).axis().vec[1], ((Quat)frame.rotation()).axis().vec[2]);
			scale(frame.scaling().x(), frame.scaling().y(), frame.scaling().z());
		}
	}
	
	public void applyWorldTransformation(RefFrame frame) {
		RefFrame refFrame = frame.referenceFrame();
		if(refFrame != null) {
			applyWorldTransformation(refFrame);
			applyTransformation(frame);
		}
		else {
			applyTransformation(frame);
		}
	}	
	
	/**
	 * Returns the approximate frame rate of the software as it executes.
	 * The initial value is 10 fps and is updated with each frame.
	 * The value is averaged (integrated) over several frames.
	 * As such, this value won't be valid until after 5-10 frames.
	 */
	public float frameRate() {
		return frameRate;
	}
	//public abstract float frameRate();
	
	/**
	 * Returns the number of frames displayed since the program started.
	 */
	public long frameCount() {
		return frameCount;
	}
	//public abstract long frameCount();
	
	// 1. Associated objects
	
	public boolean timersAreSingleThreaded() {
		return singleThreadedTaskableTimers;
	}
	
	/**
	 * Returns the associated Camera, never {@code null}.
	 */
	public Viewport viewport() {
		return vport;
	}
	
	/**
	 * Returns the associated Camera, never {@code null}.
	 */
	public Camera camera() {
		if (this.is3D())
			return (Camera) vport;
		else 
			throw new RuntimeException("Camera type is only available in 3D");
	}
	
	public Window window() {
		if (this.is2D())
			return (Window) vport;
		else 
			throw new RuntimeException("Window type is only available in 2D");
	}

	/**
	 * Replaces the current {@link #viewport()} with {@code vp}
	 */
	public void setViewPort(Viewport vp)  {
		if (vp == null)
			return;

		vp.setSceneRadius(radius());		
		vp.setSceneCenter(center());

		vp.setScreenWidthAndHeight(width(), height());
    
		vport = vp;		

		showAll();
	}
	
	/**
	 * Returns {@code true} if automatic update of the camera frustum plane
	 * equations is enabled and {@code false} otherwise. Computation of the
	 * equations is expensive and hence is disabled by default.
	 * 
	 * @see #toggleFrustumEquationsUpdate()
	 * @see #disableFrustumEquationsUpdate()
	 * @see #enableFrustumEquationsUpdate()
	 * @see #enableFrustumEquationsUpdate(boolean)
	 * @see remixlab.dandelion.core.Camera#updateFrustumEquations()
	 */
	public boolean frustumEquationsUpdateIsEnable() {
		return viewport().frustumEquationsUpdateIsEnable();
	}

	/**
	 * Toggles automatic update of the camera frustum plane equations every frame.
	 * Computation of the equations is expensive and hence is disabled by default.
	 * 
	 * @see #frustumEquationsUpdateIsEnable()
	 * @see #disableFrustumEquationsUpdate()
	 * @see #enableFrustumEquationsUpdate()
	 * @see #enableFrustumEquationsUpdate(boolean)
	 * @see remixlab.dandelion.core.Camera#updateFrustumEquations()
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
	 * @see remixlab.dandelion.core.Camera#updateFrustumEquations()
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
	 * @see remixlab.dandelion.core.Camera#updateFrustumEquations()
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
	 * @see remixlab.dandelion.core.Camera#updateFrustumEquations()
	 */
	public void enableFrustumEquationsUpdate(boolean flag) {
		viewport().enableFrustumEquationsUpdate(flag);
	}
	
	/**
	 * Toggles the {@link #viewport()} type between PERSPECTIVE and ORTHOGRAPHIC.
	 */
	public void toggleCameraType() {
		if( this.is2D() ) {
			System.out.println("Warning: Camera Type is only available in 3D");
		}
		else {
		if (((Camera) viewport()).type() == Camera.Type.PERSPECTIVE)
			setCameraType(Camera.Type.ORTHOGRAPHIC);
		else
			setCameraType(Camera.Type.PERSPECTIVE);
		}
	}

	/**
	 * Toggles the {@link #viewport()} kind between PROSCENE and STANDARD.
	 */
	public void toggleCameraKind() {
		if( this.is2D() ) {
			System.out.println("Warning: Camera Kind is only available in 3D");
		}
		else {
		if (((Camera) viewport()).kind() == Camera.Kind.PROSCENE)
			setCameraKind(Camera.Kind.STANDARD);
		else
			setCameraKind(Camera.Kind.PROSCENE);
		}
	}
	
	/**
	 * Returns the current {@link #viewport()} type.
	 */
	public final Camera.Type cameraType() {
		if( this.is2D() ) {
			System.out.println("Warning: Camera Type is only available in 3D");
			return null;
		}
		else
			return ((Camera) viewport()).type();		
	}

	/**
	 * Sets the {@link #viewport()} type.
	 */
	public void setCameraType(Camera.Type type) {
		if( this.is2D() ) {
			System.out.println("Warning: Camera Type is only available in 3D");			
		}
		else
			if (type != ((Camera) viewport()).type())
				((Camera) viewport()).setType(type);
	}

	/**
	 * Returns the current {@link #viewport()} kind.
	 */
	public final Camera.Kind cameraKind() {
		if( this.is2D() ) {
			System.out.println("Warning: Camera Kype is only available in 3D");
			return null;
		}
		return ((Camera) viewport()).kind();
	}

	/**
	 * Sets the {@link #viewport()} kind.
	 */
	public void setCameraKind(Camera.Kind kind) {
		if( this.is2D() ) {
			System.out.println("Warning: Camera Kind is only available in 3D");			
		}
		else {
		if (kind != ((Camera) viewport()).kind()) {
			((Camera) viewport()).setKind(kind);
			if (kind == Camera.Kind.PROSCENE)
				System.out.println("Changing camera kind to Proscene");
			else
				System.out.println("Changing camera kind to Standard");
		}
		}
	}
	
	/**
	 * Returns {@code true} if a mouse moved event  is called even when no mouse button is pressed.
	 * <p>
	 * You need to {@link #setDeviceTracking(boolean)} to {@code true} in order to use MouseGrabber
	 * (see {@link #deviceGrabber()}).
	 */
	/**
	public boolean isTrackingDevice() {
		return deviceTrckn;
	}
	*/
	
	/**
	 * Sets the {@link #isTrackingDevice()} value.
	 */
	/**
	public void setDeviceTracking(boolean enable) {		
		if(!enable) {
			if( deviceGrabber() != null )
				deviceGrabber().setGrabsInput(false);
			setDeviceGrabber(null);
		}
		deviceTrckn = enable;
	}
	*/
	
	/**
	 * Calls {@link #setDeviceTracking(boolean)} to toggle the {@link #isTrackingDevice()} value.
	 */
	/**
	public void toggleDeviceTracking() {
		setDeviceTracking(!isTrackingDevice());
	}
	*/
	  
	public void registerJob(AbstractTimerJob job) {
		job.setTimer(new SeqTaskableTimer(this, job));
		timerPool.add(job);
	}
	
	public void unregisterJob(SeqTaskableTimer t) {		
			timerPool.remove( t.timerJob() );
	}
	
	public void unregisterJob(AbstractTimerJob job) {
		timerPool.remove(job);		
	}  
	
	public boolean isJobRegistered(AbstractTimerJob job) {
		return timerPool.contains(job);
	}		
	
  //2. Associated objects
	
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
		avatarIsInteractiveFrame = false;
		if (avatar() == null)
			return;			
		if (avatar() instanceof InteractiveFrame) {
			avatarIsInteractiveFrame = true;
			if (avatar() instanceof InteractiveAvatarFrame)
				avatarIsInteractiveAvatarFrame = true;
			viewport().frame().updateFlyUpVector();// ?
			viewport().frame().stopSpinning();
			if( this.avatarIsInteractiveFrame ) {
				((InteractiveFrame) (avatar())).updateFlyUpVector();
				((InteractiveFrame) (avatar())).stopSpinning();
			}
			// perform small animation ;)
			if (viewport().anyInterpolationIsStarted())
				viewport().stopAllInterpolations();
			Viewport cm = viewport().get();
			cm.setPosition(avatar().viewportPosition());
			cm.setUpVector(avatar().upVector());
			cm.lookAt(avatar().target());
			viewport().interpolateTo(cm.frame());
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
		avatarIsInteractiveFrame = false;
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
		return viewport().sceneRadius();
	}

	/**
	 * Returns the scene center.
	 * <p>
	 * Convenience wrapper function that simply returns {@code
	 * camera().sceneCenter()}
	 * 
	 * @see #setCenter(Vec) {@link #radius()}
	 */
	public Vec center() {
		return viewport().sceneCenter();
	}

	/**
	 * Returns the arcball reference point.
	 * <p>
	 * Convenience wrapper function that simply returns {@code
	 * camera().arcballReferencePoint()}
	 * 
	 * @see #setCenter(Vec) {@link #radius()}
	 */
	public Vec arcballReferencePoint() {
		return viewport().arcballReferencePoint();
	}

	/**
	 * Sets the {@link #radius()} of the Scene.
	 * <p>
	 * Convenience wrapper function that simply returns {@code
	 * camera().setSceneRadius(radius)}
	 * 
	 * @see #setCenter(Vec)
	 */
	public void setRadius(float radius) {
		viewport().setSceneRadius(radius);
	}

	/**
	 * Sets the {@link #center()} of the Scene.
	 * <p>
	 * Convenience wrapper function that simply calls {@code }
	 * 
	 * @see #setRadius(float)
	 */
	public void setCenter(Vec center) {
		viewport().setSceneCenter(center);
	}

	/**
	 * Sets the {@link #center()} and {@link #radius()} of the Scene from the
	 * {@code min} and {@code max} vectors.
	 * <p>
	 * Convenience wrapper function that simply calls {@code
	 * camera().setSceneBoundingBox(min,max)}
	 * 
	 * @see #setRadius(float)
	 * @see #setCenter(Vec)
	 */
	public void setBoundingBox(Vec min, Vec max) {
		if( this.is2D() )
			System.out.println("setBoundingBox is available only in 3D. Use setBoundingRect instead");
		else
			((Camera) viewport()).setSceneBoundingBox(min, max);
	}
	
	public void setBoundingRect(Vec min, Vec max) {
		if( this.is3D() )
			System.out.println("setBoundingRect is available only in 2D. Use setBoundingBox instead");
		else
			((Window) viewport()).setSceneBoundingRect(min, max);
	}

	/**
	 * Convenience wrapper function that simply calls {@code
	 * camera().showEntireScene()}
	 * 
	 * @see remixlab.dandelion.core.Camera#showEntireScene()
	 */
	public void showAll() {
		viewport().showEntireScene();
	}

	/**
	 * Convenience wrapper function that simply returns {@code
	 * camera().setArcballReferencePointFromPixel(pixel)}.
	 * <p>
	 * Current implementation set no
	 * {@link remixlab.dandelion.core.Camera#arcballReferencePoint()}. Override
	 * {@link remixlab.dandelion.core.Camera#pointUnderPixel(Point)} in your openGL
	 * based camera for this to work.
	 * 
	 * @see remixlab.dandelion.core.Camera#setArcballReferencePointFromPixel(Point)
	 * @see remixlab.dandelion.core.Camera#pointUnderPixel(Point)
	 */
	public boolean setArcballReferencePointFromPixel(Point pixel) {
		return viewport().setArcballReferencePointFromPixel(pixel);
	}
	
	/**
	 * Convenience wrapper function that simply returns {@code
	 * camera().setSceneCenterFromPixel(pixel)}
	 * <p>
	 * Current implementation set no
	 * {@link remixlab.dandelion.core.Camera#sceneCenter()}. Override
	 * {@link remixlab.dandelion.core.Camera#pointUnderPixel(Point)} in your openGL
	 * based camera for this to work.
	 * 
	 * @see remixlab.dandelion.core.Camera#setSceneCenterFromPixel(Point)
	 * @see remixlab.dandelion.core.Camera#pointUnderPixel(Point)
	 */
	public boolean setCenterFromPixel(Point pixel) {
		return viewport().setSceneCenterFromPixel(pixel);
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
	 * Toggles the state of {@link #viewportPathsAreDrawn()}.
	 * 
	 * @see #setViewportPathsAreDrawn(boolean)
	 */
	public void toggleViewportPathsAreDrawn() {
		setViewportPathsAreDrawn(!viewportPathsAreDrawn());
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
	public boolean viewportPathsAreDrawn() {
		return viewportPathsAreDrwn;
	}

	/**
	 * Returns {@code true} if axis is currently being drawn and {@code false}
	 * otherwise.
	 */
	/**
	public boolean interactiveFrameIsDrawn() {
		return iFrameIsDrwn;
	}
	*/

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
	public void setViewportPathsAreDrawn(boolean draw) {
		viewportPathsAreDrwn = draw;
	}
	
	// Abstract drawing methods
		
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
	 * Convenience function that simply calls {@code drawAxis(100)}.
	 */
	public void drawAxis() {
		drawAxis(100);
	}	
	
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
	 * Use {@link #drawArrow(Vec, Vec, float)} to place the arrow
	 * in 3D.
	 */
	public void drawArrow(float length, float radius) {
		float head = 2.5f * (radius / length) + 0.1f;
		float coneRadiusCoef = 4.0f - 5.0f * head;

		cylinder(radius, length * (1.0f - head / coneRadiusCoef));
		translate(0.0f, 0.0f, length * (1.0f - head));
		cone(coneRadiusCoef * radius, head * length);
		translate(0.0f, 0.0f, -length * (1.0f - head));
	}
	
	/**
	 * Draws a 3D arrow between the 3D point {@code from} and the 3D point {@code
	 * to}, both defined in the current world coordinate system.
	 * 
	 * @see #drawArrow(float, float)
	 */
	public void drawArrow(Vec from, Vec to,	float radius) {
		pushMatrix();
		translate(from.x(), from.y(), from.z());
		applyMatrix(new Quat(new Vec(0, 0, 1), Vec.sub(to,	from)).matrix());
		drawArrow(Vec.sub(to, from).mag(), radius);
		popMatrix();
	}
	
	public void drawDottedGrid() {
		drawDottedGrid(100, 10);
	}
	
	/**
	 * Convenience function that simply calls {@code drawGrid(100, 10)}
	 * 
	 * @see #drawGrid(float, int)
	 */
	public void drawGrid() {
		drawGrid(100, 10);
	}
	
	public void drawDottedGrid(float size) {
		drawDottedGrid(size, 10);
	}
		
	/**
	 * Convenience function that simply calls {@code drawGrid(size, 10)}
	 * 
	 * @see #drawGrid(float, int)
	 */
	public void drawGrid(float size) {
		drawGrid(size, 10);
	}
	
	public void drawDottedGrid(int nbSubdivisions) {
		drawDottedGrid(100, nbSubdivisions);
	}
	
	/**
	 * Convenience function that simply calls {@code drawGrid(100, nbSubdivisions)}
	 * 
	 * @see #drawGrid(float, int)
	 */
	public void drawGrid(int nbSubdivisions) {
		drawGrid(100, nbSubdivisions);
	}
	
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
	 * Convenience function that simply calls {@code drawViewWindow(camera, 1)}
	 * 
	 * @see #drawWindow(Window, float)
	 */
	public void drawWindow(Window vWindow) {
		drawWindow(vWindow, 1);
	}
		
	/**
	 * Draws all InteractiveFrames' selection regions: a shooter target
	 * visual hint of {@link remixlab.dandelion.core.InteractiveFrame#grabsInputThreshold()} pixels size.
	 * 
	 * <b>Attention:</b> If the InteractiveFrame is part of a Camera path draws
	 * nothing.
	 * 
	 * @see #drawViewportPathSelectionHints()
	 */
	protected abstract void drawSelectionHints();
	
	/**
	 * Draws the selection regions (a shooter target visual hint of
	 * {@link remixlab.dandelion.core.InteractiveFrame#grabsInputThreshold()} pixels size) of all
	 * InteractiveFrames forming part of the Camera paths.
	 * 
	 * @see #drawSelectionHints()
	 */
	protected abstract void drawViewportPathSelectionHints();
		
	/**
	 * Convenience function that simply calls
	 * {@code drawCross(pg3d.color(255, 255, 255), px, py, 15, 3)}.
	 */
	public void drawCross(float px, float py) {
		drawCross(px, py, 15);
	}
		
	/**
	 * Convenience function that simply calls
	 * {@code drawFilledCircle(40, center, radius)}.
	 * 
	 * @see #drawFilledCircle(int, Vec, float)
	 */
	public void drawFilledCircle(Vec center, float radius) {
		drawFilledCircle(40, center, radius);
	}
	
	protected abstract Camera.WorldPoint pointUnderPixel(Point pixel);
	
  //dimensions
  public abstract int width();
  
  public abstract int height();
  

  // WARNINGS and EXCEPTIONS
     
  static protected HashMap<String, Object> warnings;

  /**
   * Show a renderer error, and keep track of it so that it's only shown once.
   * @param msg the error message (which will be stored for later comparison)
   */
  static public void showWarning(String msg) {  // ignore
    if (warnings == null) {
      warnings = new HashMap<String, Object>();
    }
    if (!warnings.containsKey(msg)) {
      System.err.println(msg);
      warnings.put(msg, new Object());
    }
  }
  
  /**
   * Display a warning that the specified method is only available with 3D.
   * @param method The method name (no parentheses)
   */
  static public void showDepthWarning(String method) {
    showWarning(method + "() can only be used with a renderer that supports 3D, such as P3D or OPENGL.");
  }

  /**
   * Display a warning that the specified method that takes x, y, z parameters
   * can only be used with x and y parameters in this renderer.
   * @param method The method name (no parentheses)
   */
  static public void showDepthWarningXYZ(String method) {
    showWarning(method + "() with x, y, and z coordinates " +
                "can only be used with a renderer that " +
                "supports 3D, such as P3D or OPENGL. " +
                "Use a version without a z-coordinate instead.");
  }

  /**
   * Display a warning that the specified method is simply unavailable.
   */
  static public void showMethodWarning(String method) {
    showWarning(method + "() is not available with this renderer.");
  }

  /**
   * Error that a particular variation of a method is unavailable (even though
   * other variations are). For instance, if vertex(x, y, u, v) is not
   * available, but vertex(x, y) is just fine.
   */
  static public void showVariationWarning(String str) {
    showWarning(str + " is not available with this renderer.");
  }

  /**
   * Display a warning that the specified method is not implemented, meaning
   * that it could be either a completely missing function, although other
   * variations of it may still work properly.
   */
  static public void showMissingWarning(String method) {
    showWarning(method + "(), or this particular variation of it, " +
                "is not available with this renderer.");
  }
  
  /**
   * Display a warning that the specified method lacks implementation.
   */  
  static public void showMissingImplementationWarning(String method) {
    showWarning(method + "(), should be implemented by your AbstractScene, " +
                "derived class.");
  }
  
  static public void showMissingImplementationWarning(DandelionAction action) {
    showWarning(action + "(), should be implemented by your iFrame, " +
                "derived class.");
  }
  
  static public void showVariationWarning(DandelionAction action) {
    showWarning(action + " is not available in 2D.");
  }
  
  static public void showEventVariationWarning(DandelionAction action) {
    showWarning(action + " can only be performed using a relative event.");
  }
}
