package remixlab.remixcam.core;

import remixlab.remixcam.event.GenericEvent;

public class EventGrabberTuple {
	protected GenericEvent event;
	protected Grabbable grabber;
	
	public EventGrabberTuple(GenericEvent e, Grabbable g) {
		event = e;
		grabber = g;
	}
	
  public EventGrabberTuple(GenericEvent e, Actionable<?> a, Grabbable g) {
  	event = e;
  	if(event instanceof Duoble)
  		((Duoble<?>)event).setAction(a);
		grabber = g;
	}
  
  public boolean perform() {
  	if(grabber != null) {
  		grabber.performInteraction(event);
  		return true;
  	}
  	return false;
  }
}
