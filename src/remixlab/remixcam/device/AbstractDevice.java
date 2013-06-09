package remixlab.remixcam.device;

import remixlab.remixcam.core.*;
import remixlab.remixcam.event.*;

public abstract class AbstractDevice {
	/**
	protected Object handlerObject;	
	protected String handlerMethodName;
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
	
	protected AbstractScene scene;
	protected String nm;
	
	public AbstractDevice(AbstractScene scn, String n) {
		scene = scn;
		nm = n;
		scene.registerDevice(this);
	}
	
	public String name() {
		return nm;
	}
	
	public abstract void handle(DLEvent<?> event);
	
	public DLEvent<?> feed() {
		return null;
	}
}
