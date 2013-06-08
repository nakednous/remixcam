package remixlab.remixcam.profile;

import remixlab.remixcam.core.*;
import remixlab.remixcam.shortcut.*;
import remixlab.remixcam.event.*;

public abstract class AbstractProfile<K extends Shortcut, A extends Constants.Actionable> implements Constants, Copyable {
	/**
	protected AbstractScene scene;
	protected String nm;
	*/
	protected Bindings<K, A> bindings;
	
	public AbstractProfile() {
		bindings = new Bindings<K, A>();
		setDefaultBindings();
	}
	
	public void setDefaultBindings() {}
	
	// /**
	public void handle(DLEvent<A> e) {
		if(e != null)
			e.setAction(binding(e.shortcut()));
	}
	// */
	
	/**
	//TODO testing
	public void handle(DLEvent<?> event) {
		if(event != null) {
			//TODO testing
			System.out.println("Try to handle event...");
			System.out.println(event.shortcut().description());
			event.setAction(binding(event.shortcut()));
		}		
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
	
	public Actionable binding(Shortcut k) {
  	return bindings.binding(k);
  }
	
	/**
	public DLAction binding(K k) {
  	return bindings.binding(k);
  }
  // */
}
