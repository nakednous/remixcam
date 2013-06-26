package remixlab.remixcam.core;

import remixlab.remixcam.shortcut.Shortcut;

//TODO across all the library where possible, e.g., at least in a Grabbable dereived class
public interface Duoble <A extends Actionable<?>>{
	//public void setAction(Actionable<?> action);
	public Actionable<?> getAction();
	public void setAction(Actionable<?> a);
	public Shortcut shortcut();
	
	//
	//public GenericEvent getGenericEvent();
}
