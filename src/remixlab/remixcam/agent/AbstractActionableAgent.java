package remixlab.remixcam.agent;

import remixlab.remixcam.core.AbstractScene;
import remixlab.remixcam.core.Actionable;
import remixlab.remixcam.core.Duoble;
import remixlab.remixcam.core.EventGrabberTuple;
import remixlab.remixcam.core.Grabbable;
import remixlab.remixcam.event.GenericEvent;
import remixlab.remixcam.profile.AbstractProfile;

public abstract class AbstractActionableAgent extends AbstractAgent {
	protected class EventGrabberDuobleTuple extends EventGrabberTuple {
		/**
		public EventGrabberDuobleTuple(GenericEvent e, Grabbable g) {
			super(e, g);
		}
		*/
		
		public EventGrabberDuobleTuple(GenericEvent e, Actionable<?> a, Grabbable g) {
	  	super(e,g);
	  	if(event instanceof Duoble)
	  		((Duoble<?>)event).setAction(a);
	  	else
	  		System.out.println("Action will not be handled by grabber using this event type. Supply a Duoble event");
	  	}
	}
	
	protected AbstractProfile<?,?> profile;
	
	public AbstractActionableAgent(AbstractScene scn, String n) {
		super(scn, n);
	}

	public AbstractProfile<?,?> profile() {
		return profile;
	}
	
	public void setProfile(AbstractProfile<?,?>	p) {
		profile = p;
	}
}
