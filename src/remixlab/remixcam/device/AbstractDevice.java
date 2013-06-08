package remixlab.remixcam.device;

import remixlab.remixcam.core.*;
import remixlab.remixcam.event.*;

public abstract class AbstractDevice {
	/**
	protected Object handlerObject;	
	protected String handlerMethodName;
	*/
	
  /**
	public boolean isRegistered() {
		return scene.isProfileRegistered(this);
	}
	
	public void register() {
		scene.registerProfile(this);
	}
	
	public void unregister() {
		scene.unregisterProfile(this);
	}
	*/
	
	/**
	 * Attempt to add a 'feed' handler method to the HIDevice. The default feed
	 * handler is a method that returns void and has one single HIDevice parameter.
	 * 
	 * @param obj the object to handle the feed
	 * @param methodName the method to execute the feed in the object handler class
	 * 
	 * @see #removeHandler()
	 * @see #invoke()
	 */
	/**
	public void addHandler(Object obj, String methodName) {
		AbstractScene.showMissingImplementationWarning("addHandler");
	}
	*/
	
	/**
	 * Unregisters the 'feed' handler method (if any has previously been added to
	 * the HIDevice).
	 * 
	 * @see #addHandler(Object, String)
	 * @see #invoke()
	 */
	/**
	public void removeHandler() {
		AbstractScene.showMissingImplementationWarning("removeHandler");
	}
	*/
	
	//protected HashMap<String, AbstractProfile<?, ?>> profiles;
	
	protected AbstractScene scene;
	protected String nm;
	
	public AbstractDevice(AbstractScene scn, String n) {
		scene = scn;
		nm = n;
		scene.registerDevice(this);
		//profiles = new HashMap<String, AbstractProfile<?,?>>();
	}
	
	public String name() {
		return nm;
	}
	
	public abstract void handle(DLEvent<?> event);
	
	/**
	 * Returns an array of the camera profile objects that are currently
	 * registered at the Scene.
	 */
	/**
	public AbstractProfile<?,?> [] getProfiles() {
		return profiles.values().toArray(new AbstractProfile<?,?>[0]);
	}
	*/
	
	/**
	 * Adds an HIDevice to the scene.
	 * 
	 * @see #unregisterProfile(HIDevice)
	 * @see #removeAllDevices()
	 */
	/**
	public void registerProfile(AbstractProfile<?,?> profile) {
		if(!isProfileRegistered(profile))
			profiles.put(profile.name(), profile);
		else {
			System.out.println("Nothing done. A profile with the same name is already registered. Current profile names are:");
			for (AbstractProfile<?,?> dev : profiles.values())
				System.out.println(dev.name());
		}
	}
	*/
	
	/**
	public boolean isProfileRegistered(AbstractProfile<?,?> profile) {
		return profiles.containsKey(profile.name());
	}
	
	public boolean isProfileRegistered(String name) {
		return profiles.containsKey(name);
	}
	
	public AbstractProfile<?,?> getProfile(String name) {
		return profiles.get(name);
	}
	*/
	
	/**
	 * Removes the device from the scene.
	 * 
	 * @see #registerProfile(HIDevice)
	 * @see #removeAllDevices()
	 */
	/**
	public AbstractProfile<?,?> unregisterProfile(AbstractProfile<?,?> profile) {
		return profiles.remove(profile.name());
	}

	public AbstractProfile<?,?> unregisterProfile(String name) {
		return profiles.remove(name);
	}
	*/
	
	/**
	 * Removes all registered devices from the scene.
	 * 
	 * @see #registerProfile(HIDevice)
	 * @see #unregisterProfile(HIDevice)
	 */
	/**
	public void unregisterAllProfiles() {
		profiles.clear();
	}
	*/
	
	
	//public class ProsceneKeyboardProfile extends KeyboardProfile {
		/**
		protected Method handlerMethod;
		private Integer m;
		private Character c;
		private Integer kc;
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
		
	/**
	public class ProsceneKeyboardProfile extends KeyboardProfile {
		public ProsceneKeyboardProfile(AbstractScene scn, String n) {
			super(scn, n);
		}
		
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
					System.out.println("trying to handle key release... ");
					event = new DLKeyEvent( e.getModifiers(), e.getKey(), e.getKeyCode() );
					System.out.println("passed event creation... ");
					handle(event);
					eventQueue.add(event);
				}
		}
	}
	*/
	
	/**
	public class ProsceneClickProfile extends ClickProfile {
		public ProsceneClickProfile(AbstractScene scn, String n) {
			super(scn, n);
		}
		
		@Override
		public void setDefaultBindings() {
			setClickBinding(PApplet.LEFT, 1, DOF_0Action.DRAW_AXIS);
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
	}
	*/
	
	/**
	public class ProsceneDOF1Profile extends DOF1Profile {
		public ProsceneDOF1Profile(AbstractScene scn, String n) {
			super(scn, n);
		}
		
		public void mouseEvent(MouseEvent e) {
			DOF1Event event;
			if( e.getAction() == MouseEvent.WHEEL ) {
				event = new DOF1Event(e.getCount(), e.getModifiers(), NOBUTTON);
				handle(event);
			  eventQueue.add(event);
			}
		}
	}
	// */
	
	/**
	public class ProsceneDOF2Profile extends DOF2Profile {
		DOF2Event event, prevEvent;
		
		public ProsceneDOF2Profile(AbstractScene scn, String n) {
			super(scn, n);
			//prevEvent = new DOF2Event(0,0,DLAction.NO_ACTION);
			//event = new DOF2Event(0,0,DLAction.NO_ACTION);
		}
		
		@Override
		public void setDefaultBindings() {
			setBinding(PApplet.LEFT, DOF_2Action.ROTATE);
			setBinding(PApplet.RIGHT, DOF_2Action.TRANSLATE);
			//setBinding(DOF_2Action.TRANSLATE);
		}
		
		public void mouseEvent(MouseEvent e) {
			//if( ((MouseEvent)e).getAction() == MouseEvent.PRESS ) {
			//	prevEvent = new DOF2Event(e.getX(), e.getY(), e.getModifiers(), e.getButton());
			//}
			if( e.getAction() == MouseEvent.DRAG ) {
			//if( e.getAction() == MouseEvent.MOVE ) {
				//TODO debug
				//System.out.println("P5 coord: x: " + e.getX() + " y: " + e.getY());
				event = new DOF2Event(prevEvent, e.getX(), e.getY(), e.getModifiers(), e.getButton());
				//event = new DOF2Event(e.getX(), e.getY(), e.getModifiers(), e.getButton());
				handle(event);
			  eventQueue.add(event);
				//camera().frame().execAction3D((DOF1Event)event);
			  prevEvent = event.get();
			}
		}
	}
	*/	
}
