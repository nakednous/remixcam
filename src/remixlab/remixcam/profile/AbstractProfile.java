package remixlab.remixcam.profile;

import remixlab.remixcam.core.*;
import remixlab.remixcam.shortcut.*;
import remixlab.remixcam.event.*;

public abstract class AbstractProfile<K extends Shortcut> implements Constants {
	protected AbstractScene scene;
	protected String nm;
	Bindings<K> bindings;
	
	/**
	protected Object handlerObject;	
	protected String handlerMethodName;
	*/
	
	public AbstractProfile(AbstractScene scn, String n) {		
		scene = scn;
		nm = n;
		bindings = new Bindings<K>();
		setDefaultBindings();
		//active = false;
	}
	
	public String name() {
		return nm;
	}
	
	//TODO should go at device
	//public abstract DLEvent<K> feed(Object event);
	
	public void setDefaultBindings() {}
	
	public void handle(DLEvent e) {
		if(e != null)
			e.setAction( binding(e.shortcut()) );		
	}
	
	//female
  //public abstract Integer feedModifiers();
	
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
	
	public String bindingsDescription() {
		return bindings.description();
	}
	
	/**
	 * Removes all camera keyboard shortcuts.
	 */
	public void removeAllBindings() {
		bindings.removeAllBindings();
	}
	
	public DLAction binding(Shortcut k) {
  	return bindings.binding(k);
  }
	
	/**
	public DLAction binding(K k) {
  	return bindings.binding(k);
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
}
