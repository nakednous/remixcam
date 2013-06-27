package remixlab.remixcam.core;

import remixlab.remixcam.event.GenericEvent;

public class EventGrabberTuple {
	protected GenericEvent event;
	protected Grabbable grabber;
	
	public EventGrabberTuple(GenericEvent e, Grabbable g) {
		event = e;
		grabber = g;
	}
  
  protected boolean perform() {
  	if(grabber != null) {
  		grabber.performInteraction(event);
  		return true;
  	}
  	return false;
  }
  
  public GenericEvent event() {
  	return event;
  }
  
  public Grabbable grabber() {
  	return grabber;
  }
}
