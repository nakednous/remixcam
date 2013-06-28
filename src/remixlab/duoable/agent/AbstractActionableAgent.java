package remixlab.duoable.agent;

import remixlab.duoable.profile.AbstractProfile;
import remixlab.duoable.profile.Actionable;
import remixlab.duoable.profile.Duoble;
import remixlab.tersehandling.agent.AbstractAgent;
import remixlab.tersehandling.core.EventGrabberTuple;
import remixlab.tersehandling.core.Grabbable;
import remixlab.tersehandling.core.TAbstractScene;
import remixlab.tersehandling.event.GenericEvent;

public abstract class AbstractActionableAgent extends AbstractAgent {
	public class EventGrabberDuobleTuple extends EventGrabberTuple {
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
	
	public AbstractActionableAgent(TAbstractScene scn, String n) {
		super(scn, n);
	}

	public AbstractProfile<?,?> profile() {
		return profile;
	}
	
	public void setProfile(AbstractProfile<?,?>	p) {
		profile = p;
	}
	
	@Override
	public void handle(GenericEvent event) {		
		//overkill but feels safer ;)
		if(event == null || !scene.isAgentRegistered(this))	return;		
		if(event instanceof Duoble<?>)
			scene.enqueueEventTuple(new EventGrabberDuobleTuple(event, profile().handle((Duoble<?>)event), deviceGrabber()));
	}	
}
