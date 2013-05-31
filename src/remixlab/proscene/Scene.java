package remixlab.proscene;

import remixlab.remixcam.core.*;
import remixlab.remixcam.event.*;
import remixlab.remixcam.geom.*;
import remixlab.remixcam.util.*;
import remixlab.remixcam.profile.*;
import remixlab.remixcam.renderer.*;
//import remixlab.remixcam.shortcut.*;

/**
import remixlab.remixcam.core.AbstractScene;
import remixlab.remixcam.core.Camera;
import remixlab.remixcam.core.Drawerable;
import remixlab.remixcam.core.InteractiveFrame;
import remixlab.remixcam.core.ViewWindow;
import remixlab.remixcam.devices.DeviceGrabbable;
import remixlab.remixcam.devices.CameraProfile;
import remixlab.remixcam.events.DLKeyEvent;
import remixlab.remixcam.events.DLMouseEvent;
import remixlab.remixcam.events.DesktopEvents;
import remixlab.remixcam.geom.GeomFrame;
import remixlab.remixcam.geom.Matrix3D;
import remixlab.remixcam.geom.Point;
import remixlab.remixcam.geom.Vector3D;
import remixlab.remixcam.geom.Quaternion;
import remixlab.remixcam.renderers.ProjectionRenderer;
import remixlab.remixcam.renderers.Renderer;
import remixlab.remixcam.util.AbstractTimerJob;
import remixlab.remixcam.util.SingleThreadedTaskableTimer;
import remixlab.remixcam.util.SingleThreadedTimer;
import remixlab.remixcam.util.Taskable;
import remixlab.remixcam.util.Timable;
// */

import processing.core.*;
import processing.event.*;
import processing.opengl.*;

import java.lang.reflect.Method;
import java.nio.FloatBuffer;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A 3D interactive Processing scene.
 * <p>
 * A Scene has a full reach Camera, it can be used for on-screen or off-screen
 * rendering purposes (see the different constructors), and it has two means to
 * manipulate objects: an {@link #interactiveFrame()} single instance (which by
 * default is null) and a {@link #deviceGrabber()} pool.
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
 * Check {@link #setDefaultBindings()} to see the default global keyboard shortcuts.
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
	
	public class ProsceneKeyboardProfile extends KeyboardProfile {
		/**
		protected Method handlerMethod;
		private Integer m;
		private Character c;
		private Integer kc;
		*/
		
		//KeyEvent event;

		public ProsceneKeyboardProfile(AbstractScene scn, String n) {
			super(scn, n);
		}
		
		/**
		@Override
		public Character feedKey() {
			return event.getKey();			
			//if(event != null) return event.getKey(); else	return null;			
		}

		@Override
		public Integer feedKeyCode() {
			return event.getKeyCode();
			//if(event != null) return event.getKeyCode(); else return null;
		}

		@Override
		public Integer feedModifiers() {
			return event.getModifiers();
			//if(event != null) return event.getModifiers(); else return null;
		}
		*/
		
		//@Override
		public void keyEvent(KeyEvent e) {
			DLKeyEvent event;
			if(e.getAction() == KeyEvent.TYPE && e.getModifiers() == 0) {
				event = new DLKeyEvent( e.getModifiers(), e.getKey(), e.getKeyCode() );
				handleKey(event);
				eventQueue.add(event);
			}
			else
				if(e.getAction() == KeyEvent.RELEASE) {
					event = new DLKeyEvent( e.getModifiers(), e.getKey(), e.getKeyCode() );
					handle(event);
					eventQueue.add(event);
				}
			
			/**
			if( (e.getAction() == KeyEvent.TYPE && e.getModifiers() == 0) || 
					(e.getAction() == KeyEvent.RELEASE) ) {
				//TODO debug
				if( e.getAction() == KeyEvent.TYPE )
					System.out.println("KeyEvent.TYPE");
				else
					if( e.getAction() == KeyEvent.RELEASE )
						System.out.println("KeyEvent.RELEASE");
					else
						if( (e.getAction() !=  KeyEvent.TYPE) && (e.getAction() !=  KeyEvent.RELEASE) )
							System.out.println("KeyEvent.IMPOSSSIBLE DIFFERENT");
				event = new DLKeyEvent( e.getModifiers(), e.getKey(), e.getKeyCode() );
				handle(event);
			  eventQueue.add(event);
			}
			*/
		}		
		
		/**
		public void keyEvent(KeyEvent e) {
			event = e;
			if( (e.getAction() == KeyEvent.TYPE && e.getModifiers() == 0) || (e.getAction() == KeyEvent.RELEASE) )
				activate();
		}
		*/
		
		/**
	  public void feed(Integer _m, Character _c, Integer _kc) {
	  	m = _m;
	  	c = _c;
	  	kc = _kc;
		}
		*/
		
		/**
		 * Overriding of
		 * {@link remixlab.remixcam.devices.AbstractHIDevice#addHandler(Object, String)}.
		 */
		/**
		@Override
		public void addHandler(Object obj, String methodName) {
			try {
				handlerMethod = obj.getClass().getMethod(methodName, new Class[] { ProsceneKeyboardProfile.class });
				handlerObject = obj;
				handlerMethodName = methodName;
			} catch (Exception e) {
				  System.out.println("Something went wrong when registering your " + methodName + " method");
				  e.printStackTrace();
			}
		}
		*/
		
		/**
		 * Overriding of
		 * {@link remixlab.remixcam.devices.AbstractHIDevice#removeHandler()}.
		 */		
		/**
		@Override
		public void removeHandler() {
			handlerMethod = null;
			handlerObject = null;
			handlerMethodName = null;
		}
		*/
		
		/**
		@Override
		public DLKeyEvent handle() {
			if (handlerObject != null) {
				try {
					handlerMethod.invoke(handlerObject, new Object[] { this });
				} catch (Exception e) {
					System.out.println("Something went wrong when invoking your "	+ handlerMethodName + " method");
					e.printStackTrace();
				}
				//--
				DLAction action = shortcut(c);
				if (action == null)
					action = shortcut(m, kc);
				return new DLKeyEvent(m, c, kc, action);
				//-
			}
			else
				return super.handle();
		}
		*/
	}
	
	public class ProsceneClickProfile extends ClickProfile {
		//MouseEvent event;

		public ProsceneClickProfile(AbstractScene scn, String n) {
			super(scn, n);
		}
		
		@Override
		public void setDefaultBindings() {
			//setClickBinding(PApplet.LEFT, 1, DOF_0Action.DRAW_AXIS);
			//setClickBinding(PApplet.RIGHT, 2, DOF_0Action.DRAW_GRID);
			setClickBinding(PApplet.RIGHT, 1, DOF_0Action.DRAW_FRAME_SELECTION_HINT);
			
			setClickBinding(DLKeyEvent.SHIFT, PApplet.LEFT, 2, DOF_0Action.ALIGN_CAMERA);
			setClickBinding(DLKeyEvent.SHIFT, PApplet.CENTER, 2, DOF_0Action.SHOW_ALL);
			setClickBinding((DLKeyEvent.SHIFT | DLKeyEvent.CTRL ), PApplet.RIGHT, 2, DOF_0Action.ZOOM_TO_FIT);
		}
		
		public void mouseEvent(MouseEvent e) {
			DLClickEvent event;
			if( ((MouseEvent)e).getAction() == MouseEvent.CLICK ) {
				event = new DLClickEvent(((MouseEvent)e).getModifiers(), ((MouseEvent)e).getButton(), ((MouseEvent)e).getCount());
				handle(event);
			  eventQueue.add(event);
			}
		}		

		/**
		@Override
		public Integer feedModifiers() {
			return event.getModifiers();
		}
		
		@Override
		public Integer feedButton() {
			return event.getButton();
		}

		@Override
		public Integer feedClickCount() {
			return event.getCount();
		}
		
		public boolean mouseEvent(MouseEvent e) {
			event = e;
			if( e.getAction() == MouseEvent.CLICK )
				activate();
			return true;
		}
		*/
	}
	
	/**
	public class ProsceneWheelProfile extends WheelProfile {
		public ProsceneWheelProfile(AbstractScene scn, String n) {
			super(scn, n);
		}
		
		public void mouseEvent(MouseEvent e) {
			DOFEvent event;
			if( ((MouseEvent)e).getAction() == MouseEvent.WHEEL ) {
				event = new DOFEvent(((MouseEvent)e).getCount(), ((MouseEvent)e).getModifiers());
				handle(event);
			  eventQueue.add(event);
			}
		}
	}
	// */
	
	// /**
	public class ProsceneDOF1Profile extends DOF1Profile {
		public ProsceneDOF1Profile(AbstractScene scn, String n) {
			super(scn, n);
		}
		
		public void mouseEvent(MouseEvent e) {
			DOF1Event event;
			if( ((MouseEvent)e).getAction() == MouseEvent.WHEEL ) {
				event = new DOF1Event(((MouseEvent)e).getCount(), ((MouseEvent)e).getModifiers(), NOBUTTON);
				handle(event);
			  eventQueue.add(event);
			}
		}
	}
	// */
	
	protected class TimerWrap implements Timable {
		Scene scene;
		Timer timer;
		TimerTask timerTask;
		Taskable caller;
		boolean runOnlyOnce;
		long prd;

		public TimerWrap(Scene scn, Taskable o) {
			this(scn, o, false);
		}

		public TimerWrap(Scene scn, Taskable o, boolean singleShot) {
			scene = scn;
			runOnlyOnce = singleShot;
			caller = o;
		}

		public Taskable timerJob() {
			return caller;
		}

		@Override
		public void create() {
			stop();
			timer = new Timer();
			timerTask = new TimerTask() {
				public void run() {
					caller.execute();
				}
			};
		}

		@Override
		public void run(long period) {
			prd = period;
			run();
		}

		@Override
		public void run() {
			create();
			if(isSingleShot())
				timer.schedule(timerTask, prd);
			else
				timer.scheduleAtFixedRate(timerTask, 0, prd);		
		}

		@Override
		public void cancel() {
			stop();
		}

		@Override
		public void stop() {
			if (timer != null) {
				timer.cancel();
				timer.purge();
				/**
				 * prd = 0; runOnlyOnce = false;
				 */
			}
		}

		@Override
		public boolean isActive() {
			return timer != null;
		}

		@Override
		public long period() {
			return prd;
		}

		@Override
		public void setPeriod(long period) {
			prd = period;
		}

		@Override
		public boolean isSingleShot() {
			return runOnlyOnce;
		}

		@Override
		public void setSingleShot(boolean singleShot) {
			runOnlyOnce = singleShot;
		}
	}
	
	protected class P5Drawing2D implements Drawerable, PConstants {
		protected Scene scene;
		Matrix3D proj;

		public P5Drawing2D(Scene scn) {
			scene = scn;
			proj = new Matrix3D();
		}
		
		public Scene scene() {
			return scene;
		}
		
		public boolean isRightHanded() {
			return scene.isRightHanded();
		}
		
		public boolean isLeftHanded() {
			return scene.isLeftHanded();
		}
		
		public PGraphics pg() {
			return scene.pg();
		}	
		
		@Override
		public void drawAxis(float length) {
			final float charWidth = length / 40.0f;
			final float charHeight = length / 30.0f;
			final float charShift = 1.05f * length;
			
	    pg().pushStyle();		
	    pg().strokeWeight(2);
			pg().beginShape(LINES);	
			
			// The X		
			pg().stroke(200, 0, 0);		
			pg().vertex(charShift + charWidth, -charHeight);
			pg().vertex(charShift - charWidth, charHeight);
			pg().vertex(charShift - charWidth, -charHeight);
			pg().vertex(charShift + charWidth, charHeight);
			
			// The Y
			pg().stroke(0, 200, 0);
			pg().vertex(charWidth, charShift + charHeight);
			pg().vertex(0.0f, charShift + 0.0f);
			pg().vertex(-charWidth, charShift + charHeight);
			pg().vertex(0.0f, charShift + 0.0f);
			pg().vertex(0.0f, charShift + 0.0f);
			pg().vertex(0.0f, charShift + -charHeight);
			
			pg().endShape();		
			pg().popStyle();		
			
			pg().pushStyle();				
			pg().strokeWeight(2);			  
			
		  // X Axis
			pg().stroke(200, 0, 0);
			pg().line(0, 0, length, 0);
		  // Y Axis
			pg().stroke(0, 200, 0);		
			pg().line(0, 0, 0, length);		

			pg().popStyle();
		}

		public void drawGrid(float size, int nbSubdivisions) {
			pg().pushStyle();
			pg().stroke(170, 170, 170);
			pg().strokeWeight(1);
			pg().beginShape(LINES);
			for (int i = 0; i <= nbSubdivisions; ++i) {
				final float pos = size * (2.0f * i / nbSubdivisions - 1.0f);
				pg().vertex(pos, -size);
				pg().vertex(pos, +size);
				pg().vertex(-size, pos);
				pg().vertex(size, pos);
			}
			pg().endShape();
			pg().popStyle();
		}

		@Override
		public void drawDottedGrid(float size, int nbSubdivisions) {
			float posi, posj;
			pg().pushStyle();
			pg().stroke(170);
			pg().strokeWeight(2);
			pg().beginShape(POINTS);
			for (int i = 0; i <= nbSubdivisions; ++i) {
				posi = size * (2.0f * i / nbSubdivisions - 1.0f);
				for(int j = 0; j <= nbSubdivisions; ++j) {
					posj = size * (2.0f * j / nbSubdivisions - 1.0f);
					pg().vertex(posi, posj);
				}
			}
			pg().endShape();
			//pg().popStyle();
			
			int internalSub = 5;
			int subSubdivisions = nbSubdivisions * internalSub;
			//pg().pushStyle();
			pg().stroke(100);
			pg().strokeWeight(1);
			pg().beginShape(POINTS);
			for (int i = 0; i <= subSubdivisions; ++i) {
				posi = size * (2.0f * i / subSubdivisions - 1.0f);
				for(int j = 0; j <= subSubdivisions; ++j) {
					posj = size * (2.0f * j / subSubdivisions - 1.0f);
					if(( (i%internalSub) != 0 ) || ( (j%internalSub) != 0 ) )
						pg().vertex(posi, posj);
				}
			}
			pg().endShape();
			pg().popStyle();
		}	

		//TODO these two are pending
		@Override
		public void drawZoomWindowHint() {
			/**
			float p1x = (float) ((Scene)scene).eventDispatcher.fCorner.getX();
			float p1y = (float) ((Scene)scene).eventDispatcher.fCorner.getY();
			float p2x = (float) ((Scene)scene).eventDispatcher.lCorner.getX();
			float p2y = (float) ((Scene)scene).eventDispatcher.lCorner.getY();
			scene.beginScreenDrawing();
			pg().pushStyle();
			pg().stroke(255, 255, 255);
			pg().strokeWeight(2);
			pg().noFill();
			pg().beginShape();
			pg().vertex(p1x, p1y);
			pg().vertex(p2x, p1y);
			pg().vertex(p2x, p2y);		
			pg().vertex(p1x, p2y);
			pg().endShape(CLOSE);
			pg().popStyle();
			scene.endScreenDrawing();	
			*/	
		}

		@Override
		public void drawScreenRotateLineHint() {
			/**
			float p1x = (float) ((Scene)scene).eventDispatcher.fCorner.getX();
			float p1y = (float) ((Scene)scene).eventDispatcher.fCorner.getY();
			Vector3D p2 = scene.pinhole().projectedCoordinatesOf(scene.arcballReferencePoint());
			scene.beginScreenDrawing();
			pg().pushStyle();
			pg().stroke(255, 255, 255);
			pg().strokeWeight(2);
			pg().noFill();
			pg().line(p2.x(), p2.y(), p1x, p1y);
			pg().popStyle();
			scene.endScreenDrawing();
			*/
		}

		@Override
		public void drawArcballReferencePointHint() {
			Vector3D p = scene.pinhole().projectedCoordinatesOf(scene.arcballReferencePoint());
			pg().pushStyle();
			pg().stroke(255);
			pg().strokeWeight(3);
			scene.drawCross(p.vec[0], p.vec[1]);
			pg().popStyle();
		}

		@Override
		public void drawCross(float px, float py, float size) {
			scene.beginScreenDrawing();
			pg().pushStyle();		
			//pg().stroke(color);
			//pg().strokeWeight(strokeWeight);
			pg().noFill();
			pg().beginShape(LINES);
			pg().vertex(px - size, py);
			pg().vertex(px + size, py);
			pg().vertex(px, py - size);
			pg().vertex(px, py + size);
			pg().endShape();
			pg().popStyle();
			scene.endScreenDrawing();		
		}

		@Override
		public void drawFilledCircle(int subdivisions, Vector3D center, float radius) {
			float precision = TWO_PI/subdivisions;
			float x = center.x();
			float y = center.y();
			float angle, x2, y2;
			scene.beginScreenDrawing();
			pg().pushStyle();
			pg().noStroke();
			//pg().fill(color);
			pg().beginShape(TRIANGLE_FAN);		
			pg().vertex(x, y);
			for (angle = 0.0f; angle <= TWO_PI + 1.1*precision; angle += precision) {			
				x2 = x + PApplet.sin(angle) * radius;
				y2 = y + PApplet.cos(angle) * radius;			
				pg().vertex(x2, y2);
			}
			pg().endShape();
			pg().popStyle();
			scene.endScreenDrawing();
		}

		@Override
		public void drawFilledSquare(Vector3D center, float edge) {
			float x = center.x();
			float y = center.y();
			scene.beginScreenDrawing();		
			pg().pushStyle();
			pg().noStroke();
			//pg().fill(color);
			pg().beginShape(QUADS);
			pg().vertex(x - edge, y + edge);
			pg().vertex(x + edge, y + edge);
			pg().vertex(x + edge, y - edge);
			pg().vertex(x - edge, y - edge);
			pg().endShape();
			pg().popStyle();
			scene.endScreenDrawing();
		}

		@Override
		public void drawShooterTarget(Vector3D center, float length) {
			float x = center.x();
			float y = center.y();
			scene.beginScreenDrawing();
			
			pg().pushStyle();

			//pg().stroke(color);
			//pg().strokeWeight(strokeWeight);
			pg().noFill();

			pg().beginShape();
			pg().vertex((x - length), (y - length) + (0.6f * length));
			pg().vertex((x - length), (y - length));
			pg().vertex((x - length) + (0.6f * length), (y - length));
			pg().endShape();

			pg().beginShape();
			pg().vertex((x + length) - (0.6f * length), (y - length));
			pg().vertex((x + length), (y - length));
			pg().vertex((x + length), ((y - length) + (0.6f * length)));
			pg().endShape();
			
			pg().beginShape();
			pg().vertex((x + length), ((y + length) - (0.6f * length)));
			pg().vertex((x + length), (y + length));
			pg().vertex(((x + length) - (0.6f * length)), (y + length));
			pg().endShape();

			pg().beginShape();
			pg().vertex((x - length) + (0.6f * length), (y + length));
			pg().vertex((x - length), (y + length));
			pg().vertex((x - length), ((y + length) - (0.6f * length)));
			pg().endShape();

			pg().popStyle();
			scene.endScreenDrawing();

			drawCross(center.x(), center.y(), 0.6f * length);
		}

		@Override
		public void drawViewWindow(ViewWindow camera, float scale) {
			pg().pushMatrix();
			
			/**
			VFrame tmpFrame = new VFrame(scene.is3D());
			tmpFrame.fromMatrix(camera.frame().worldMatrix(), camera.frame().magnitude());		
			scene().applyTransformation(tmpFrame);
			// */
			//Same as above, but easier ;)
		  scene().applyWorldTransformation(camera.frame());

			//upper left coordinates of the near corner
			Vector3D upperLeft = new Vector3D();
			
			pg().pushStyle();
			
			/**
			float[] wh = camera.getOrthoWidthHeight();
			upperLeft.x = scale * wh[0];
			upperLeft.y = scale * wh[1];
			*/
			
			upperLeft.x(scale * scene.width() / 2);
			upperLeft.y(scale * scene.height() / 2);
							
			pg().noStroke();		
			pg().beginShape(PApplet.QUADS);				
			pg().vertex(upperLeft.x(), upperLeft.y());
			pg().vertex(-upperLeft.x(), upperLeft.y());
			pg().vertex(-upperLeft.x(), -upperLeft.y());
			pg().vertex(upperLeft.x(), -upperLeft.y());		
			pg().endShape();

			// Up arrow
			float arrowHeight = 1.5f * upperLeft.y();
			float baseHeight = 1.2f * upperLeft.y();
			float arrowHalfWidth = 0.5f * upperLeft.x();
			float baseHalfWidth = 0.3f * upperLeft.x();
			
		  // Base
			pg().beginShape(PApplet.QUADS);		
			if( camera.scene.isLeftHanded() ) {
				pg().vertex(-baseHalfWidth, -upperLeft.y());
				pg().vertex(baseHalfWidth, -upperLeft.y());
				pg().vertex(baseHalfWidth, -baseHeight);
				pg().vertex(-baseHalfWidth, -baseHeight);	
			}
			else {
				pg().vertex(-baseHalfWidth, upperLeft.y());
				pg().vertex(baseHalfWidth, upperLeft.y());
				pg().vertex(baseHalfWidth, baseHeight);
				pg().vertex(-baseHalfWidth, baseHeight);
			}
			pg().endShape();
			
		  // Arrow
			pg().beginShape(PApplet.TRIANGLES);
			if( camera.scene.isLeftHanded() ) {
				pg().vertex(0.0f, -arrowHeight);
				pg().vertex(-arrowHalfWidth, -baseHeight);
				pg().vertex(arrowHalfWidth, -baseHeight);
			}
			else {
				pg().vertex(0.0f, arrowHeight);
				pg().vertex(-arrowHalfWidth, baseHeight);
				pg().vertex(arrowHalfWidth, baseHeight);
			}
			pg().endShape();		
			
			pg().popStyle();
			pg().popMatrix();
		}
		
		 @Override
		  public void cylinder(float w, float h) {
		  	AbstractScene.showDepthWarning("cylinder");
		  }
		  
		  @Override
		 	public void hollowCylinder(int detail, float w, float h, Vector3D m, Vector3D n) {
		  	AbstractScene.showDepthWarning("cylinder");
		 	}
		  
		  @Override
		  public void cone(int detail, float x, float y, float r, float h) {
		  	AbstractScene.showDepthWarning("cylinder");
		 	}
		  
		  @Override
		  public void cone(int detail, float x, float y, float r1, float r2, float h) {
		  	AbstractScene.showDepthWarning("cylinder");
		 	}
		  
		  @Override
		  public void drawCamera(Camera camera, boolean drawFarPlane, float scale) {
		  	AbstractScene.showDepthWarning("cylinder");
		 	}

		  @Override
		  public void drawKFIViewport(float scale) {
		  	AbstractScene.showDepthWarning("cylinder");
		 	}

		@Override
		public void drawPath(List<GeomFrame> path, int mask, int nbFrames,	int nbSteps, float scale) {
			// TODO pend		
		}
	}
	
	protected class P5Drawing3D extends P5Drawing2D {
		public P5Drawing3D(Scene scn) {
			super(scn);
		}
		
		public PGraphics3D pg3d() {
		  return (PGraphics3D) pg();	
		}
		
		/**
		 * Overriding of {@link remixlab.remixcam.core.Rendarable#cylinder(float, float)}.
		 * <p>
		 * Code adapted from http://www.processingblogs.org/category/processing-java/ 
		 */
		@Override
		public void cylinder(float w, float h) {
			float px, py;
			
			pg3d().beginShape(PApplet.QUAD_STRIP);
			for (float i = 0; i < 13; i++) {
				px = (float) Math.cos(PApplet.radians(i * 30)) * w;
				py = (float) Math.sin(PApplet.radians(i * 30)) * w;
				pg3d().vertex(px, py, 0);
				pg3d().vertex(px, py, h);
			}
			pg3d().endShape();
			
			pg3d().beginShape(PApplet.TRIANGLE_FAN);
			pg3d().vertex(0, 0, 0);
			for (float i = 12; i > -1; i--) {
				px = (float) Math.cos(PApplet.radians(i * 30)) * w;
				py = (float) Math.sin(PApplet.radians(i * 30)) * w;
				pg3d().vertex(px, py, 0);
			}
			pg3d().endShape();
			
			pg3d().beginShape(PApplet.TRIANGLE_FAN);
			pg3d().vertex(0, 0, h);
			for (float i = 0; i < 13; i++) {
				px = (float) Math.cos(PApplet.radians(i * 30)) * w;
				py = (float) Math.sin(PApplet.radians(i * 30)) * w;
				pg3d().vertex(px, py, h);
			}
			pg3d().endShape();
		}
		
		/**
		 * Convenience function that simply calls
		 * {@code hollowCylinder(20, w, h, new Vector3D(0,0,-1), new Vector3D(0,0,1))}.
		 * 
		 * @see #hollowCylinder(int, float, float, Vector3D, Vector3D)
		 * @see #cylinder(float, float)
		 */
		public void hollowCylinder(float w, float h) {
			this.hollowCylinder(20, w, h, new Vector3D(0,0,-1), new Vector3D(0,0,1));
		}
		
		/**
		 * Convenience function that simply calls
		 * {@code hollowCylinder(detail, w, h, new Vector3D(0,0,-1), new Vector3D(0,0,1))}.
		 * 
		 * @see #hollowCylinder(int, float, float, Vector3D, Vector3D)
		 * @see #cylinder(float, float)
		 */
		public void hollowCylinder(int detail, float w, float h) {
			this.hollowCylinder(detail, w, h, new Vector3D(0,0,-1), new Vector3D(0,0,1));
		}
	 
		/**
		 * Draws a cylinder whose bases are formed by two cutting planes ({@code m}
		 * and {@code n}), along the {@link #renderer()} positive {@code z} axis.
		 * 
		 * @param detail
		 * @param w radius of the cylinder and h is its height
		 * @param h height of the cylinder
		 * @param m normal of the plane that intersects the cylinder at z=0
		 * @param n normal of the plane that intersects the cylinder at z=h
		 * 
		 * @see #cylinder(float, float)
		 */
		@Override
		public void hollowCylinder(int detail, float w, float h, Vector3D m, Vector3D n) {
			//eqs taken from: http://en.wikipedia.org/wiki/Line-plane_intersection
			Vector3D pm0 = new Vector3D(0,0,0);
			Vector3D pn0 = new Vector3D(0,0,h);
			Vector3D l0 = new Vector3D();		
			Vector3D l = new Vector3D(0,0,1);
			Vector3D p = new Vector3D();
			float x,y,d;		
			
			pg3d().noStroke();
			pg3d().beginShape(PApplet.QUAD_STRIP);
			
			for (float t = 0; t <= detail; t++) {
				x = w * PApplet.cos(t * TWO_PI/detail);
				y = w * PApplet.sin(t * TWO_PI/detail);
				l0.set(x,y,0);
				
				d = ( m.dot(Vector3D.sub(pm0, l0)) )/( l.dot(m) );
				p =  Vector3D.add( Vector3D.mult(l, d), l0 );
				pg3d().vertex(p.x(), p.y(), p.z());
				
				l0.z(h);
				d = ( n.dot(Vector3D.sub(pn0, l0)) )/( l.dot(n) );
				p =  Vector3D.add( Vector3D.mult(l, d), l0 );
				pg3d().vertex(p.x(), p.y(), p.z());
			}
			pg3d().endShape();
		}

		/**
		 * Overriding of {@link remixlab.remixcam.core.Renderable#cone(int, float, float, float, float)}.
		 * <p>
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
				float a1 = PApplet.TWO_PI * i / detail;
				unitConeX[i] = r * (float) Math.cos(a1);
				unitConeY[i] = r * (float) Math.sin(a1);
			}

			pg3d().pushMatrix();
			pg3d().translate(x, y);
			pg3d().beginShape(PApplet.TRIANGLE_FAN);
			pg3d().vertex(0, 0, h);
			for (int i = 0; i <= detail; i++) {
				pg3d().vertex(unitConeX[i], unitConeY[i], 0.0f);
			}
			pg3d().endShape();
			pg3d().popMatrix();
		}

		/**
		 * Overriding of {@link remixlab.remixcam.core.Renderable#cone(int, float, float, float, float, float)}.
		 */
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

			pg3d().pushMatrix();
			pg3d().translate(x, y);
			pg3d().beginShape(PApplet.QUAD_STRIP);
			for (int i = 0; i <= detail; i++) {
				pg3d().vertex(firstCircleX[i], firstCircleY[i], 0);
				pg3d().vertex(secondCircleX[i], secondCircleY[i], h);
			}
			pg3d().endShape();
			pg3d().popMatrix();		
		}

		@Override
		public void drawAxis(float length) {
			final float charWidth = length / 40.0f;
			final float charHeight = length / 30.0f;
			final float charShift = 1.04f * length;

			// pg3d().noLights();

			pg3d().pushStyle();
			
			pg3d().beginShape(PApplet.LINES);		
			pg3d().strokeWeight(2);
			// The X
			pg3d().stroke(200, 0, 0);
			pg3d().vertex(charShift, charWidth, -charHeight);
			pg3d().vertex(charShift, -charWidth, charHeight);
			pg3d().vertex(charShift, -charWidth, -charHeight);
			pg3d().vertex(charShift, charWidth, charHeight);
			// The Y
			pg3d().stroke(0, 200, 0);
			pg3d().vertex(charWidth, charShift, charHeight);
			pg3d().vertex(0.0f, charShift, 0.0f);
			pg3d().vertex(-charWidth, charShift, charHeight);
			pg3d().vertex(0.0f, charShift, 0.0f);
			pg3d().vertex(0.0f, charShift, 0.0f);
			pg3d().vertex(0.0f, charShift, -charHeight);
			// The Z
			pg3d().stroke(0, 100, 200);
			
			//left_handed
			if( isLeftHanded() ) {
				pg3d().vertex(-charWidth, -charHeight, charShift);
				pg3d().vertex(charWidth, -charHeight, charShift);
				pg3d().vertex(charWidth, -charHeight, charShift);
				pg3d().vertex(-charWidth, charHeight, charShift);
				pg3d().vertex(-charWidth, charHeight, charShift);
				pg3d().vertex(charWidth, charHeight, charShift);
			}
			else {
				pg3d().vertex(-charWidth, charHeight, charShift);
				pg3d().vertex(charWidth, charHeight, charShift);
				pg3d().vertex(charWidth, charHeight, charShift);
				pg3d().vertex(-charWidth, -charHeight, charShift);
				pg3d().vertex(-charWidth, -charHeight, charShift);
				pg3d().vertex(charWidth, -charHeight, charShift);
			}
			
			pg3d().endShape();
			
		  /**
			// Z axis
			pg3d().noStroke();
			pg3d().fill(0, 100, 200);
			drawArrow(length, 0.01f * length);

			// X Axis
			pg3d().fill(200, 0, 0);
			pg3d().pushMatrix();
			pg3d().rotateY(HALF_PI);
			drawArrow(length, 0.01f * length);
			pg3d().popMatrix();

			// Y Axis
			pg3d().fill(0, 200, 0);
			pg3d().pushMatrix();
			pg3d().rotateX(-HALF_PI);
			drawArrow(length, 0.01f * length);
			pg3d().popMatrix();
			// */
			
		  // X Axis
			pg3d().stroke(200, 0, 0);
			pg3d().line(0, 0, 0, length, 0, 0);
		  // Y Axis
			pg3d().stroke(0, 200, 0);		
			pg3d().line(0, 0, 0, 0, length, 0);
			// Z Axis
			pg3d().stroke(0, 100, 200);
			pg3d().line(0, 0, 0, 0, 0, length);		

			pg3d().popStyle();
		}
		
		@Override
		public void drawCamera(Camera cam, boolean drawFarPlane, float scale) {
			pg3d().pushMatrix();
			
			//applyMatrix(camera.frame().worldMatrix());
			// same as the previous line, but maybe more efficient
			/**
			VFrame tmpFrame = new VFrame(scene.is3D());
			tmpFrame.fromMatrix(camera.frame().worldMatrix());
			scene().applyTransformation(tmpFrame);
			// */
			//same as above but easier
			
			//fails due to scaling!
			//scene().applyTransformation(camera.frame());
				
			pg3d().translate( cam.frame().translation().vec[0], cam.frame().translation().vec[1], cam.frame().translation().vec[2] );
			pg3d().rotate( cam.frame().rotation().angle(), ((Quaternion)cam.frame().rotation()).axis().vec[0], ((Quaternion)cam.frame().rotation()).axis().vec[1], ((Quaternion)cam.frame().rotation()).axis().vec[2]);

			// 0 is the upper left coordinates of the near corner, 1 for the far one
			Vector3D[] points = new Vector3D[2];
			points[0] = new Vector3D();
			points[1] = new Vector3D();

			points[0].z(scale * cam.zNear());
			points[1].z(scale * cam.zFar());

			switch (cam.type()) {
			case PERSPECTIVE: {
				points[0].y(points[0].z() * PApplet.tan(cam.fieldOfView() / 2.0f));
				points[0].x(points[0].y() * cam.aspectRatio());
				float ratio = points[1].z() / points[0].z();
				points[1].y(ratio * points[0].y());
				points[1].x(ratio * points[0].x());
				break;
			}
			case ORTHOGRAPHIC: {
				float[] wh = cam.getOrthoWidthHeight();
				//points[0].x = points[1].x = scale * wh[0];
				//points[0].y = points[1].y = scale * wh[1];
				
				points[0].x(scale * wh[0]);
				points[1].x(scale * wh[0]);
				points[0].y(scale * wh[1]); 
				points[1].y(scale * wh[1]);
				break;
			}
			}

			int farIndex = drawFarPlane ? 1 : 0;
			
		  // Frustum lines
			pg3d().pushStyle();		
			pg3d().strokeWeight(2);
			//pg3d().stroke(255,255,0);
			switch (cam.type()) {
				case PERSPECTIVE:
					pg3d().beginShape(PApplet.LINES);
					pg3d().vertex(0.0f, 0.0f, 0.0f);
					pg3d().vertex(points[farIndex].x(), points[farIndex].y(), -points[farIndex].z());
					pg3d().vertex(0.0f, 0.0f, 0.0f);
					pg3d().vertex(-points[farIndex].x(), points[farIndex].y(), -points[farIndex].z());
					pg3d().vertex(0.0f, 0.0f, 0.0f);
					pg3d().vertex(-points[farIndex].x(), -points[farIndex].y(),	-points[farIndex].z());
					pg3d().vertex(0.0f, 0.0f, 0.0f);
					pg3d().vertex(points[farIndex].x(), -points[farIndex].y(), -points[farIndex].z());
					pg3d().endShape();
					break;
				case ORTHOGRAPHIC:
					if (drawFarPlane) {
						pg3d().beginShape(PApplet.LINES);
						pg3d().vertex(points[0].x(), points[0].y(), -points[0].z());
						pg3d().vertex(points[1].x(), points[1].y(), -points[1].z());
						pg3d().vertex(-points[0].x(), points[0].y(), -points[0].z());
						pg3d().vertex(-points[1].x(), points[1].y(), -points[1].z());
						pg3d().vertex(-points[0].x(), -points[0].y(), -points[0].z());
						pg3d().vertex(-points[1].x(), -points[1].y(), -points[1].z());
						pg3d().vertex(points[0].x(), -points[0].y(), -points[0].z());
						pg3d().vertex(points[1].x(), -points[1].y(), -points[1].z());
						pg3d().endShape();
					}
			}
			
			// Near and (optionally) far plane(s)		
			pg3d().noStroke();
			//pg3d().fill(255,255,0,160);
			pg3d().beginShape(PApplet.QUADS);
			for (int i = farIndex; i >= 0; --i) {
				pg3d().normal(0.0f, 0.0f, (i == 0) ? 1.0f : -1.0f);			
				pg3d().vertex(points[i].x(), points[i].y(), -points[i].z());
				pg3d().vertex(-points[i].x(), points[i].y(), -points[i].z());
				pg3d().vertex(-points[i].x(), -points[i].y(), -points[i].z());
				pg3d().vertex(points[i].x(), -points[i].y(), -points[i].z());
			}
			pg3d().endShape();

			// Up arrow
			float arrowHeight = 1.5f * points[0].y();
			float baseHeight = 1.2f * points[0].y();
			float arrowHalfWidth = 0.5f * points[0].x();
			float baseHalfWidth = 0.3f * points[0].x();

			// pg3d().noStroke();
			// Base
			pg3d().beginShape(PApplet.QUADS);		
			if( cam.scene.isLeftHanded() ) {
				pg3d().vertex(-baseHalfWidth, -points[0].y(), -points[0].z());
				pg3d().vertex(baseHalfWidth, -points[0].y(), -points[0].z());
				pg3d().vertex(baseHalfWidth, -baseHeight, -points[0].z());
				pg3d().vertex(-baseHalfWidth, -baseHeight, -points[0].z());
			}
			else {
				pg3d().vertex(-baseHalfWidth, points[0].y(), -points[0].z());
				pg3d().vertex(baseHalfWidth, points[0].y(), -points[0].z());
				pg3d().vertex(baseHalfWidth, baseHeight, -points[0].z());
				pg3d().vertex(-baseHalfWidth, baseHeight, -points[0].z());
			}
			pg3d().endShape();

			// Arrow
			pg3d().beginShape(PApplet.TRIANGLES);
			
			if( cam.scene.isLeftHanded() ) {
				pg3d().vertex(0.0f, -arrowHeight, -points[0].z());
				pg3d().vertex(-arrowHalfWidth, -baseHeight, -points[0].z());
				pg3d().vertex(arrowHalfWidth, -baseHeight, -points[0].z());
			}
			else {
				pg3d().vertex(0.0f, arrowHeight, -points[0].z());
				pg3d().vertex(-arrowHalfWidth, baseHeight, -points[0].z());
				pg3d().vertex(arrowHalfWidth, baseHeight, -points[0].z());
			}
			pg3d().endShape();
			
			pg3d().popStyle();
			pg3d().popMatrix();
		}

		@Override
		public void drawKFIViewport(float scale) {
			float halfHeight = scale * 0.07f;
			float halfWidth = halfHeight * 1.3f;
			float dist = halfHeight / (float) Math.tan(PApplet.PI / 8.0f);

			float arrowHeight = 1.5f * halfHeight;
			float baseHeight = 1.2f * halfHeight;
			float arrowHalfWidth = 0.5f * halfWidth;
			float baseHalfWidth = 0.3f * halfWidth;

			// Frustum outline
			pg3d().pushStyle();

			pg3d().noFill();		
			pg3d().beginShape();
			pg3d().vertex(-halfWidth, halfHeight, -dist);
			pg3d().vertex(-halfWidth, -halfHeight, -dist);
			pg3d().vertex(0.0f, 0.0f, 0.0f);
			pg3d().vertex(halfWidth, -halfHeight, -dist);
			pg3d().vertex(-halfWidth, -halfHeight, -dist);
			pg3d().endShape();
			pg3d().noFill();
			pg3d().beginShape();
			pg3d().vertex(halfWidth, -halfHeight, -dist);
			pg3d().vertex(halfWidth, halfHeight, -dist);
			pg3d().vertex(0.0f, 0.0f, 0.0f);
			pg3d().vertex(-halfWidth, halfHeight, -dist);
			pg3d().vertex(halfWidth, halfHeight, -dist);
			pg3d().endShape();

			// Up arrow
			pg3d().noStroke();
			pg3d().fill(170);
			// Base
			pg3d().beginShape(PApplet.QUADS);
			
			if( isLeftHanded() ) {
				pg3d().vertex(baseHalfWidth, -halfHeight, -dist);
				pg3d().vertex(-baseHalfWidth, -halfHeight, -dist);
				pg3d().vertex(-baseHalfWidth, -baseHeight, -dist);
				pg3d().vertex(baseHalfWidth, -baseHeight, -dist);
			}
			else {
				pg3d().vertex(-baseHalfWidth, halfHeight, -dist);
				pg3d().vertex(baseHalfWidth, halfHeight, -dist);
				pg3d().vertex(baseHalfWidth, baseHeight, -dist);
				pg3d().vertex(-baseHalfWidth, baseHeight, -dist);
			}
			
			pg3d().endShape();
			// Arrow
			pg3d().beginShape(PApplet.TRIANGLES);
			
			if( isLeftHanded() ) {
				pg3d().vertex(0.0f, -arrowHeight, -dist);
				pg3d().vertex(arrowHalfWidth, -baseHeight, -dist);
				pg3d().vertex(-arrowHalfWidth, -baseHeight, -dist);
			}
			else {
			  pg3d().vertex(0.0f, arrowHeight, -dist);
			  pg3d().vertex(-arrowHalfWidth, baseHeight, -dist);
			  pg3d().vertex(arrowHalfWidth, baseHeight, -dist);
			}
			
			pg3d().endShape();

			pg3d().popStyle();
		}
		
		@Override
		public void drawPath(List<GeomFrame> path, int mask, int nbFrames, int nbSteps, float scale) {
			if (mask != 0) {
				pg3d().pushStyle();
				pg3d().strokeWeight(2);
				pg3d().noFill();
				pg3d().stroke(170);
				
				if (((mask & 1) != 0) && path.size() > 1 ) {				
					pg3d().beginShape();
					for (GeomFrame myFr : path)
						pg3d().vertex(myFr.position().x(), myFr.position().y(), myFr.position().z());
					pg3d().endShape();
				}
				if ((mask & 6) != 0) {
					int count = 0;
					if (nbFrames > nbSteps)
						nbFrames = nbSteps;
					float goal = 0.0f;

					for (GeomFrame myFr : path)
						if ((count++) >= goal) {
							goal += nbSteps / (float) nbFrames;
							pg3d().pushMatrix();
												  
							scene.applyTransformation(myFr);						

							if ((mask & 2) != 0)
								drawKFIViewport(scale);
							if ((mask & 4) != 0)
								drawAxis(scale / 10.0f);

							pg3d().popMatrix();
						}
				}
				pg3d().popStyle();
			}
		}
		
		@Override
		public void drawViewWindow(ViewWindow camera, float scale) {
			pg().pushMatrix();
			
			//VFrame tmpFrame = new VFrame(scene.is3D());
			//tmpFrame.fromMatrix(camera.frame().worldMatrix(), camera.frame().magnitude());		
			//scene().applyTransformation(tmpFrame);
			
			//Same as above, but easier ;)
		  scene().applyWorldTransformation(camera.frame());

			//upper left coordinates of the near corner
			Vector3D upperLeft = new Vector3D();
			
			pg().pushStyle();
			
			//float[] wh = camera.getOrthoWidthHeight();
			//upperLeft.x = scale * wh[0];
			//upperLeft.y = scale * wh[1];
			
			upperLeft.x(scale * scene.width() / 2);
			upperLeft.y(scale * scene.height() / 2);
							
			pg().noStroke();		
			pg().beginShape(PApplet.QUADS);				
			pg().vertex(upperLeft.x(), upperLeft.y());
			pg().vertex(-upperLeft.x(), upperLeft.y());
			pg().vertex(-upperLeft.x(), -upperLeft.y());
			pg().vertex(upperLeft.x(), -upperLeft.y());		
			pg().endShape();

			// Up arrow
			float arrowHeight = 1.5f * upperLeft.y();
			float baseHeight = 1.2f * upperLeft.y();
			float arrowHalfWidth = 0.5f * upperLeft.x();
			float baseHalfWidth = 0.3f * upperLeft.x();
			
		  // Base
			pg().beginShape(PApplet.QUADS);		
			if( camera.scene.isLeftHanded() ) {
				pg().vertex(-baseHalfWidth, -upperLeft.y());
				pg().vertex(baseHalfWidth, -upperLeft.y());
				pg().vertex(baseHalfWidth, -baseHeight);
				pg().vertex(-baseHalfWidth, -baseHeight);	
			}
			else {
				pg().vertex(-baseHalfWidth, upperLeft.y());
				pg().vertex(baseHalfWidth, upperLeft.y());
				pg().vertex(baseHalfWidth, baseHeight);
				pg().vertex(-baseHalfWidth, baseHeight);
			}
			pg().endShape();
			
		  // Arrow
			pg().beginShape(PApplet.TRIANGLES);
			if( camera.scene.isLeftHanded() ) {
				pg().vertex(0.0f, -arrowHeight);
				pg().vertex(-arrowHalfWidth, -baseHeight);
				pg().vertex(arrowHalfWidth, -baseHeight);
			}
			else {
				pg().vertex(0.0f, arrowHeight);
				pg().vertex(-arrowHalfWidth, baseHeight);
				pg().vertex(arrowHalfWidth, baseHeight);
			}
			pg().endShape();		
			
			pg().popStyle();
			pg().popMatrix();
		}
		
		/**
		@Override
		public void drawGrid(float size, int nbSubdivisions) {
			pg().pushStyle();
			pg().stroke(170, 170, 170);
			pg().strokeWeight(1);
			pg().beginShape(PApplet.LINES);
			for (int i = 0; i <= nbSubdivisions; ++i) {
				final float pos = size * (2.0f * i / nbSubdivisions - 1.0f);
				pg().vertex(pos, -size);
				pg().vertex(pos, +size);
				pg().vertex(-size, pos);
				pg().vertex(size, pos);
			}
			pg().endShape();
			pg().popStyle();
		}
		*/
	}
	
	protected class P5RendererJava2D extends Renderer {
		PGraphics pg;
		Matrix3D proj;
		
		public P5RendererJava2D(Scene scn, PGraphics renderer, Drawerable d) {
			super(scn, d);
			pg = renderer;
		}
		
		public P5RendererJava2D(Scene scn, PGraphics renderer) {
			super(scn, new P5Drawing2D(scn));
			pg = renderer;
		}		
		
		public PGraphics pg() {
			return pg;
		}
		
		public PGraphicsJava2D pgj2d() {
		  return (PGraphicsJava2D) pg();	
		}

		@Override
		public boolean is3D() {
			return false;
		}
	}
	
	protected abstract class P5Renderer extends ProjectionRenderer {
		PGraphicsOpenGL pg;
		Matrix3D proj;
		
		public P5Renderer(Scene scn, PGraphicsOpenGL renderer, Drawerable d) {
			super(scn, d);
			pg = renderer;
			proj = new Matrix3D();
		}
		
		public PGraphics pg() {
			return pg;
		}
		
		public PGraphicsOpenGL pggl() {
		  return (PGraphicsOpenGL) pg();	
		}	
		
		@Override
		public void pushProjection() {
			pggl().pushProjection();		
		}

		@Override
		public void popProjection() {
			pggl().popProjection();
		}

		@Override
		public void resetProjection() {
			pggl().resetProjection();
		}
		
		@Override
		public Matrix3D getProjection() {
			PMatrix3D pM = pggl().projection.get();
	    return new Matrix3D(pM.get(new float[16]), true);// set it transposed
		}

		@Override
		public Matrix3D getProjection(Matrix3D target) {
			PMatrix3D pM = pggl().projection.get();
	    target.setTransposed(pM.get(new float[16]));
	    return target;
		}

		@Override
		public void applyProjection(Matrix3D source) {
			PMatrix3D pM = new PMatrix3D();
	    pM.set(source.getTransposed(new float[16]));
	    pggl().applyProjection(pM);		
		}

		@Override
		public void applyProjectionRowMajorOrder(float n00, float n01, float n02,
				float n03, float n10, float n11, float n12, float n13, float n20,
				float n21, float n22, float n23, float n30, float n31, float n32,
				float n33) {
			pggl().applyProjection(new PMatrix3D(n00, n01, n02, n03, n10, n11, n12, n13, n20, n21, n22, n23, n30, n31, n32, n33));
		}
	}
	
	protected class P5Renderer2D extends P5Renderer {	
		public P5Renderer2D(Scene scn, PGraphicsOpenGL renderer) {
			super(scn, renderer, new P5Drawing2D(scn));
		}
		
		@Override
		public boolean is3D() {
			return false;
		}
		
		public PGraphics2D pg2d() {
		  return (PGraphics2D) pg();	
		}	

		@Override
		public void setProjection(Matrix3D source) {
			PMatrix3D pM = new PMatrix3D();
			pM.set(source.getTransposed(new float[16]));		
			pg2d().projection.set(pM);		
		}

		@Override
		public void drawPath(List<GeomFrame> path, int mask, int nbFrames, int nbSteps, float scale) {
			// TODO Auto-generated method stub
			
		}	

		@Override
		public void setMatrix(Matrix3D source) {
			PMatrix3D pM = new PMatrix3D();
			pM.set(source.getTransposed(new float[16]));
			//pg2d().setMatrix(pM);
	    pg2d().modelview.set(pM);
		}	

		/**
		 * Sets the processing camera projection matrix from {@link #camera()}. Calls
		 * {@code PApplet.perspective()} or {@code PApplet.orhto()} depending on the
		 * {@link remixlab.remixcam.core.Camera#type()}.
		 */
		@Override
		protected void setProjectionMatrix() {
		  // All options work seemlessly
			/**		
			// Option 1
			Matrix3D mat = new Matrix3D();		
			scene.viewWindow().getProjectionMatrix(mat, true);
			mat.transpose();		
			float[] target = new float[16];
			pg2d().projection.set(mat.get(target));		
			// */	  

			/**		
			// Option 2		
			pg2d().projection.set(scene.viewWindow().getProjectionMatrix(true).getTransposed(new float[16]));
			// */

			// /**
		  // option 3 (new, Andres suggestion)
			//TODO: optimize me set per value basis
			// /**		
			proj = scene.viewWindow().getProjectionMatrix(true);
			pg2d().setProjection(new PMatrix3D( proj.mat[0],  proj.mat[4], proj.mat[8],  proj.mat[12],
		                                      proj.mat[1],  proj.mat[5], proj.mat[9],  proj.mat[13],
		                                      proj.mat[2],  proj.mat[6], proj.mat[10], proj.mat[14],
		                                      proj.mat[3],  proj.mat[7], proj.mat[11], proj.mat[15] ));
			// */

			/**
			proj = scene.viewWindow().getProjectionMatrix(true);
			pg2d().flush();
		  pg2d().projection.set( proj.mat[0], proj.mat[4],                                  proj.mat[8],  proj.mat[12],
			                       proj.mat[1], isLeftHanded() ? proj.mat[5] : -proj.mat[5], proj.mat[9],  proj.mat[13],
			                       proj.mat[2], proj.mat[6],                                  proj.mat[10], proj.mat[14],
			                       proj.mat[3], proj.mat[7],                                  proj.mat[11], proj.mat[15] );
			pg2d().updateProjmodelview();
			// */
		}

		/**
		 * Sets the processing camera matrix from {@link #camera()}. Simply calls
		 * {@code PApplet.camera()}.
		 */
		@Override
		protected void setModelViewMatrix() {
		  // The two options work seamlessly
			/**		
			// Option 1
			Matrix3D mat = new Matrix3D();		
			scene.viewWindow().getViewMatrix(mat, true);
			mat.transpose();// experimental
			float[] target = new float[16];
			pg2d().modelview.set(mat.get(target));
			// */

			// /**		
			// Option 2
			pg2d().modelview.set(scene.viewWindow().getViewMatrix(true).getTransposed(new float[16]));						
			// Finally, caches projmodelview
			//pg2d().projmodelview.set(scene.viewWindow().getProjectionViewMatrix(true).getTransposed(new float[16]));		
			Matrix3D.mult(proj, scene.viewWindow().view(), scene.viewWindow().projectionView());
			pg2d().projmodelview.set(scene.viewWindow().getProjectionViewMatrix(false).getTransposed(new float[16]));
		  // */
		}
	}
	
	protected class P5Renderer3D extends P5Renderer {
		Vector3D at;	
		
		public P5Renderer3D(Scene scn, PGraphicsOpenGL renderer) {
			super(scn, renderer, new P5Drawing3D(scn));		
			at = new Vector3D();		
		}
		
		public PGraphics3D pg3d() {
		  return (PGraphics3D) pg();	
		}
		
		@Override
		public boolean is3D() {
			return true;
		}	

		@Override
		public void setProjection(Matrix3D source) {
			PMatrix3D pM = new PMatrix3D();
	    pM.set(source.getTransposed(new float[16]));
	    pg3d().setProjection(pM);
		}
		
		@Override
		public void setMatrix(Matrix3D source) {
			PMatrix3D pM = new PMatrix3D();
			pM.set(source.getTransposed(new float[16]));
			pg3d().setMatrix(pM);//needs testing in screen drawing
		}	
		
		//---

		/**
	   * Sets the processing camera projection matrix from {@link #camera()}. Calls
	   * {@code PApplet.perspective()} or {@code PApplet.orhto()} depending on the
	   * {@link remixlab.remixcam.core.Camera#type()}.
	   */
		@Override
		protected void setProjectionMatrix() {
		  // All options work seemlessly
		  /**
		  // Option 1
		  Matrix3D mat = new Matrix3D();
		  scene.camera().getProjectionMatrix(mat, true);
		  mat.transpose();
		  float[] target = new float[16];
		  pg3d().projection.set(mat.get(target));
		  // */	

		  /**
		  // Option 2
		  pg3d().projection.set(scene.camera().getProjectionMatrix(true).getTransposed(new float[16]));
		  // */

		  // /**
		  // option 3 (new, Andres suggestion)
		  //TODO: optimize me set per value basis
		  //proj.set((scene.camera().getProjectionMatrix(true).getTransposed(new float[16])));
		  proj = scene.camera().getProjectionMatrix(true);
		  pg3d().setProjection(new PMatrix3D( proj.mat[0], proj.mat[4], proj.mat[8], proj.mat[12],
		  																		proj.mat[1], proj.mat[5], proj.mat[9], proj.mat[13],
		  																		proj.mat[2], proj.mat[6], proj.mat[10], proj.mat[14],
		  																		proj.mat[3], proj.mat[7], proj.mat[11], proj.mat[15] ));
		  // */

		  /**
		  proj = scene.camera().getProjectionMatrix(true);
		  pg3d().flush();
		  pg3d().projection.set( proj.mat[0], proj.mat[4], proj.mat[8], proj.mat[12],
		  proj.mat[1], isLeftHanded() ? proj.mat[5] : -proj.mat[5], proj.mat[9], proj.mat[13],
		  proj.mat[2], proj.mat[6], proj.mat[10], proj.mat[14],
		  proj.mat[3], proj.mat[7], proj.mat[11], proj.mat[15] );
		  pg3d().updateProjmodelview();//only in P5-head
		  // */	

		  /**
		  // Option 4
		  // compute the processing camera projection matrix from our camera() parameters
		  switch (scene.camera().type()) {
		  case PERSPECTIVE:
		  pg3d().perspective(scene.camera().fieldOfView(), scene.camera().aspectRatio(), scene.camera().zNear(), scene.camera().zFar());
		  break;
		  case ORTHOGRAPHIC:
		  float[] wh = scene.camera().getOrthoWidthHeight();
		  pg3d().ortho(-wh[0], wh[0], -wh[1], wh[1], scene.camera().zNear(), scene.camera().zFar());
		  break;
		  }
		  // hack:
		  //if(this.isRightHanded())
		  //pg3d().projection.m11 = -pg3d().projection.m11;
		  // We cache the processing camera projection matrix into our camera()
		  scene.camera().setProjectionMatrix( pg3d().projection.get(new float[16]), true ); // set it transposed
		  // */
		}

		/**
		* Sets the processing camera matrix from {@link #camera()}. Simply calls
		* {@code PApplet.camera()}.
		*/	
		@Override
		protected void setModelViewMatrix() {
		  // All three options work seamlessly
		  /**
		  // Option 1
		  Matrix3D mat = new Matrix3D();
		  scene.camera().getViewMatrix(mat, true);
		  mat.transpose();// experimental
		  float[] target = new float[16];
		  pg3d().modelview.set(mat.get(target));
		  //caches projmodelview
		  pg3d().projmodelview.set(scene.camera().getProjectionViewMatrix(true).getTransposed(new float[16]));
		  // */

		  /**
		  // Option 2
		  pg3d().modelview.set(scene.camera().getViewMatrix(true).getTransposed(new float[16]));
		  // Finally, caches projmodelview
		  //pg3d().projmodelview.set(scene.camera().getProjectionViewMatrix(true).getTransposed(new float[16]));
		  Matrix3D.mult(proj, scene.camera().view(), scene.camera().projectionView());
		  pg3d().projmodelview.set(scene.camera().getProjectionViewMatrix(false).getTransposed(new float[16]));
		  // */	

		  // /**
		  // Option 3
		  // compute the processing camera modelview matrix from our camera() parameters
		  at = scene.camera().at();
		  pg3d().camera(scene.camera().position().x(), scene.camera().position().y(), scene.camera().position().z(),
		  //scene.camera().at().x(), scene.camera().at().y(), scene.camera().at().z(),
		  at.x(), at.y(), at.z(),
		  scene.camera().upVector().x(), scene.camera().upVector().y(), scene.camera().upVector().z());
		  // We cache the processing camera modelview matrix into our camera()
		  scene.camera().setViewMatrix( pg3d().modelview.get(new float[16]), true );// set it transposed
		  // We cache the processing camera projmodelview matrix into our camera()
		  scene.camera().setProjectionViewMatrix( pg3d().projmodelview.get(new float[16]), true );// set it transposed
		  // */
		}	
	}
	
	// ---- //
	
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
	
	// H A R D W A R E
	protected ProsceneKeyboardProfile keyboard;
	protected ProsceneClickProfile clicker;
	//protected ProsceneWheelProfile wheel;
	protected ProsceneDOF1Profile wheel;
	
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
			this.setDottedGrid(false);
		
		//setJavaTimers();
		this.parent.frameRate(100);
		setLeftHanded();
		
		/**
		// TODO decide if this should go
		//mouse grabber selection hint colors		
		setMouseGrabberOnSelectionHintColor(pg3d.color(0, 0, 255));
		setMouseGrabberOffSelectionHintColor(pg3d.color(255, 0, 0));
		setMouseGrabberCameraPathOnSelectionHintColor(pg3d.color(255, 255, 0));
		setMouseGrabberCameraPathOffSelectionHintColor(pg3d.color(0, 255, 255));
		*/		
		
		// 1 ->   	

		/**
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
		setDeviceTracking(true);
		setDeviceGrabber(null);
		
		deviceGrabberIsAnIFrame = false;

		//animation
		animationTimer = new SingleThreadedTimer(this);
		setAnimationPeriod(40, false); // 25Hz
		stopAnimation();
		
		arpFlag = false;
		pupFlag = false;

		//withConstraint = true;

		setAxisIsDrawn(true);
		setGridIsDrawn(true);
		setFrameSelectionHintIsDrawn(false);
		setCameraPathsAreDrawn(false);
		
		disableFrustumEquationsUpdate();
		
		//TODO testing keyboard
		keyboard = new ProsceneKeyboardProfile(this, "ProsceneKeyboard");
		parent.registerMethod("keyEvent", keyboard);
		this.registerProfile(keyboard);
		
		clicker = new ProsceneClickProfile(this, "Clicker");
		parent.registerMethod("mouseEvent", clicker);
		this.registerProfile(clicker);
		
		wheel = new ProsceneDOF1Profile(this, "Wheel");
		//wheel = new ProsceneWheelProfile(this, "Wheel");
		parent.registerMethod("mouseEvent", wheel);
		this.registerProfile(wheel);

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
	
	//TODO all these should be pending now
	
	/**
	 * Enables Proscene keyboard handling.
	 * 
	 * @see #keyboardIsHandled()
	 * @see #enableMouseHandling()
	 * @see #disableKeyboardHandling()
	 */
	/**
	@Override
	public void enableKeyboardHandling() {
		if( !this.keyboardIsHandled() ) {
			super.enableKeyboardHandling();
			parent.registerMethod("keyEvent", eventDispatcher);
		}
	}
	*/

	/**
	 * Disables Proscene keyboard handling.
	 * 
	 * @see #keyboardIsHandled()
	 */
	/**
	@Override
	public void disableKeyboardHandling() {
		if( this.keyboardIsHandled() ) {
			super.disableKeyboardHandling();
			parent.unregisterMethod("keyEvent", eventDispatcher);
		}
	}
	*/

	/**
	 * Enables Proscene mouse handling.
	 * 
	 * @see #mouseIsHandled()
	 * @see #disableMouseHandling()
	 * @see #enableKeyboardHandling()
	 */
	/**
	@Override
	public void enableMouseHandling() {
		if( !this.mouseIsHandled() ) {
			super.enableMouseHandling();
			parent.registerMethod("mouseEvent", eventDispatcher);
		}
	}
	*/
	
	/**
	 * Disables Proscene mouse handling.
	 * 
	 * @see #mouseIsHandled()
	 */
	/**
	@Override
	public void disableMouseHandling() {
		if( this.mouseIsHandled() ) {
			super.disableMouseHandling();
			parent.unregisterMethod("mouseEvent", eventDispatcher);
		}
	}
	*/
	
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
		//TODO pending
		/**
		if (eventDispatcher.camMouseAction == DOF_6Action.ZOOM_ON_REGION)			
			drawZoomWindowHint();		
		if (eventDispatcher.camMouseAction == DOF_6Action.SCREEN_ROTATE)
			drawScreenRotateLineHint();
		*/
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
		}
		//TODO pending
		/**
		else {
			if ((currentCameraProfile() instanceof ThirdPersonCameraProfile)
					&& (!pinhole().anyInterpolationIsStarted())) {
				pinhole().setPosition(avatar().cameraPosition());
				pinhole().setUpVector(avatar().upVector());
				pinhole().lookAt(avatar().target());
			}
		}
		*/

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
						
			//TODO pending
			/**
			if ((currentCameraProfile() instanceof ThirdPersonCameraProfile)
					&& (!pinhole().anyInterpolationIsStarted())) {
				pinhole().setPosition(avatar().cameraPosition());
				pinhole().setUpVector(avatar().upVector());
				pinhole().lookAt(avatar().target());
			}
			*/
			
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
	
	@Override
	protected void updateCursorPosition() {
		pcursorX = cursorX;
		pcursorY = cursorY;
		cursorX = parent.mouseX;
		cursorY = parent.mouseY;
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
					if (mg.grabsCursor()) {						
						pg().pushStyle();
					  //pg3d.stroke(mouseGrabberOnSelectionHintColor());
						pg().stroke(pg().color(0, 255, 0));
						pg().strokeWeight(2);
						drawShooterTarget(center, (iF.grabsDeviceThreshold() + 1));
						pg().popStyle();					
					}
					else {						
						pg().pushStyle();
					  //pg3d.stroke(mouseGrabberOffSelectionHintColor());
						pg().stroke(pg().color(240, 240, 240));
						pg().strokeWeight(1);
						drawShooterTarget(center, iF.grabsDeviceThreshold());
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
					if (mg.grabsCursor()) {
						pg().pushStyle();						
					  //pg3d.stroke(mouseGrabberCameraPathOnSelectionHintColor());
						pg().stroke(pg().color(0, 255, 255));
						pg().strokeWeight(2);
						drawShooterTarget(center, (iF.grabsDeviceThreshold() + 1));
						pg().popStyle();
					}
					else {
						pg().pushStyle();
					  //pg3d.stroke(mouseGrabberCameraPathOffSelectionHintColor());
						pg().stroke(pg().color(255, 255, 0));
						pg().strokeWeight(1);
						drawShooterTarget(center, iF.grabsDeviceThreshold());
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
	 * Displays global keyboard bindings.
	 * 
	 * @param onConsole if this flag is true displays the help on console.
	 * Otherwise displays it on the applet
	 * 
	 * @see #displayGlobalHelp()
	 */
	//TODO pending
	@Override
	public void displayGlobalHelp(boolean onConsole) {
		/**
		if (onConsole)
			System.out.println(globalHelp());
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
		*/
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
		//TODO pending
		/**
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
		*/
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