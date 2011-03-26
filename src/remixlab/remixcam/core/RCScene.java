package remixlab.remixcam.core;

import java.util.List;

import remixlab.remixcam.geom.Vector3D;
import remixlab.remixcam.util.*;

public abstract class RCScene {
  //M o u s e G r a b b e r
	protected MouseGrabberPool mouseGrabberPool;
	protected TimerPool timerPool;
	
	public RCScene() {
		mouseGrabberPool = new MouseGrabberPool();
	}	

	public TimerPool timerPool() {
		return timerPool;
	}
	
	/**
	 * Returns an object containing references to all the active MouseGrabbers.
	 * <p>
	 * Used to parse all the MouseGrabbers and to check if any of them
	 * {@link remixlab.proscene.MouseGrabbable#grabsMouse()} using
	 * {@link remixlab.proscene.MouseGrabbable#checkIfGrabsMouse(int, int, Camera)}.
	 * <p>
	 * Use {@link #removeFromMouseGrabberPool(MouseGrabbable)} and
	 * {@link #addInMouseGrabberPool(MouseGrabbable)} to modify this list.
	 */
	// TODO refactor the name
	public MouseGrabberPool mouseGrabberPoolObject() {
		return mouseGrabberPool;
	}
	
	//TODO: document me
	public List<MouseGrabbable> mouseGrabberPool() {
		return mouseGrabberPool.mouseGrabberPool();
	}
	
	public MouseGrabbable mouseGrabber() {
		return mouseGrabberPoolObject().mouseGrabber();
	}	
	
	public boolean mouseGrabberIsAnIFrame() {
		return mouseGrabberPoolObject().mouseGrabberIsAnIFrame();
	}
	
	public boolean mouseGrabberIsADrivableFrame() {
		return mouseGrabberPoolObject().mouseGrabberIsADrivableFrame();
	}
	
  // 3. Mouse grabber handling
	
	public boolean isInMouseGrabberPool(MouseGrabbable mouseGrabber) {
		return mouseGrabberPoolObject().isInMouseGrabberPool(mouseGrabber);
	}
	
	public void addInMouseGrabberPool(MouseGrabbable mouseGrabber) {
		mouseGrabberPoolObject().addInMouseGrabberPool(mouseGrabber);
	}

	public void removeFromMouseGrabberPool(MouseGrabbable mouseGrabber) {
		mouseGrabberPoolObject().removeFromMouseGrabberPool(mouseGrabber);
	}

	public void clearMouseGrabberPool() {
		mouseGrabberPoolObject().clearMouseGrabberPool();
	}
	
	// drawing functions
	
	/**
	 * Draws a cylinder of width {@code w} and height {@code h}, along the {@code
	 * p3d} positive {@code z} axis.
	 */
	public abstract void cylinder(float w, float h);	
	
	/**
	 * Draws a cone along the {@code p3d} positive {@code z} axis, with its
	 * base centered at {@code (x,y)}, height {@code h}, and radius {@code r}.
	 */
	public abstract void cone(int detail, float x, float y, float r, float h);
	
	/**
	 * Draws a truncated cone along the {@code parent} positive {@code z} axis,
	 * with its base centered at {@code (x,y)}, height {@code h}, and radii
	 * {@code r1} and {@code r2} (basis and height respectively).
	 */
	public abstract void cone(int detail, float x, float y, float r1, float r2, float h);
	
	/**
	 * Draws an axis of length {@code length} which origin correspond to the
	 * {@code parent}'s world coordinate system origin.
	 */
	public abstract void drawAxis(float length);
	
	/**
	 * Draws a 3D arrow along the {@code parent} positive Z axis.
	 * <p>
	 * {@code length} and {@code radius} define its geometry.
	 * <p>
	 * Use {@link #drawArrow(Vector3D, Vector3D, float)} to place the arrow
	 * in 3D.
	 */
	public abstract void drawArrow(float length, float radius);
	
	/**
	 * Draws a 3D arrow between the 3D point {@code from} and the 3D point {@code
	 * to}, both defined in the current {@code parent} ModelView coordinates
	 * system.
	 * 
	 * @see #drawArrow(float, float)
	 */
	public abstract void drawArrow(Vector3D from, Vector3D to,	float radius);
	
	/**
	 * Draws a grid in the XY plane, centered on (0,0,0) (defined in the current
	 * coordinate system).
	 * <p>
	 * {@code size} (processing scene units) and {@code nbSubdivisions} define its
	 * geometry.
	 * 
	 * @see #drawAxis(float)
	 */
	public abstract void drawGrid(float size, int nbSubdivisions);
	
	/**
	 * Draws a representation of the {@code camera} in the {@code p3d} 3D
	 * virtual world using {@code color}.
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
	public abstract void drawCamera(Camera camera, int color, boolean drawFarPlane, float scale);
	
	public abstract void drawKFICamera(int color, float scale);
	
	/**
	 * Draws the path used to interpolate the {@link #frame()}.
	 * <p>
	 * {@code mask} controls what is drawn: If ( (mask & 1) != 0 ), the position
	 * path is drawn. If ( (mask & 2) != 0 ), a camera representation is regularly
	 * drawn and if ( (mask & 4) != 0 ), an oriented axis is regularly drawn.
	 * Examples:
	 * <p>
	 * {@code drawPath(); // Simply draws the interpolation path} <br>
	 * {@code drawPath(3); // Draws path and cameras} <br>
	 * {@code drawPath(5); // Draws path and axis} <br>
	 * <p>
	 * In the case where camera or axis is drawn, {@code nbFrames} controls the
	 * number of objects (axis or camera) drawn between two successive keyFrames.
	 * When {@code nbFrames = 1}, only the path KeyFrames are drawn. {@code
	 * nbFrames = 2} also draws the intermediate orientation, etc. The maximum
	 * value is 30. {@code nbFrames} should divide 30 so that an object is drawn
	 * for each KeyFrame. Default value is 6.
	 * <p>
	 * {@code scale} controls the scaling of the camera and axis drawing. A value
	 * of {@link remixlab.proscene.Scene#radius()} should give good results.
	 */
	public abstract void drawPath(KeyFrameInterpolator KFI, int mask, int nbFrames, float scale);
}
