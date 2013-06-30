package remixlab.tersehandling.duoable.agent;


import remixlab.tersehandling.core.TerseHandler;
import remixlab.tersehandling.duoable.profile.GenericClickProfile;
import remixlab.tersehandling.duoable.profile.GenericProfile;
import remixlab.tersehandling.duoable.profile.Duoble;
import remixlab.tersehandling.event.*;

public class GenericMotionAgent<P extends GenericProfile<?,?>, C extends GenericClickProfile<?>> extends GenericActionableAgent<P> {
	protected C clickProfile;
	protected float[] sens;
	
	public GenericMotionAgent(P p, C c, TerseHandler scn, String n) {
		super(p, scn, n);
		clickProfile = c;
	}
	
	public P motionProfile() {
		return profile();
	}
	
	public void setMotionProfile(P profile) {
		setProfile(profile);
	}
	
	public C clickProfile() {
		return clickProfile;
	}
	
	public void setClickProfile(C profile) {
		clickProfile = profile;
	}
	
	public float [] sensitivities() {
		return sens;
	}
	
	@Override
	public void handle(BaseEvent event) {
		//overkill but feels safer ;)
		if(event == null || !handler.isAgentRegistered(this))	return;		
		if(event instanceof Duoble<?>) {
			if(event instanceof ClickEvent)
				handler.enqueueEventTuple(new EventGrabberDuobleTuple(event, clickProfile().handle((Duoble<?>)event), grabber()));
			else
				if(event instanceof MotionEvent) {
					((MotionEvent)event).modulate(sens);
					if (trackedGrabber() != null )
						handler.enqueueEventTuple(new EventGrabberDuobleTuple(event, motionProfile().handle((Duoble<?>)event), trackedGrabber()));			
			}
		}
	}	
}