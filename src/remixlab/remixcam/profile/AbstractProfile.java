package remixlab.remixcam.profile;

import remixlab.remixcam.action.VActionable;
import remixlab.remixcam.core.*;
import remixlab.remixcam.shortcut.*;
import remixlab.remixcam.event.*;

public abstract class AbstractProfile<K extends Shortcut, A extends VActionable> implements Constants, Copyable {
	/**
	protected AbstractScene scene;
	protected String nm;
	*/
	protected Bindings<K, A> bindings;
	
	public AbstractProfile() {
		bindings = new Bindings<K, A>();
	}
	
	/**
	public void handle(DLEvent<A> e) {
		if(e != null)
			e.setAction(binding(e.shortcut()));
	}
	// */
	
	// /**
	//TODO testing
	public void handle(DLEvent<?> event) {
		if(event != null)
			event.setAction(binding(event.shortcut()));
	}	
	// */

	public String bindingsDescription() {
		return bindings.description();
	}
	
	/**
	 * Removes all camera keyboard shortcuts.
	 */
	public void removeAllBindings() {
		bindings.removeAllBindings();
	}
	
	public VActionable binding(Shortcut k) {
  	return bindings.binding(k);
  }
}
