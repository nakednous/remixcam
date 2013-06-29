package remixlab.tersehandling.duoable.agent;


import remixlab.tersehandling.core.BasicScene;
import remixlab.tersehandling.duoable.profile.AbstractClickProfile;
import remixlab.tersehandling.duoable.profile.AbstractMotionProfile;
import remixlab.tersehandling.duoable.profile.Duoble;
import remixlab.tersehandling.event.*;

public abstract class AbstractMotionAgent extends AbstractActionableAgent {
	protected AbstractClickProfile<?> clickProfile;
	protected float[] sens;
	
	public AbstractMotionAgent(BasicScene scn, String n) {
		super(scn, n);	
	}
	
	public AbstractMotionProfile<?> motionProfile() {
		return (AbstractMotionProfile<?>) profile();
	}
	
	public AbstractClickProfile<?> clickProfile() {
		return clickProfile;
	}
	
	public void setMotionProfile(AbstractMotionProfile<?> profile) {
		setProfile(profile);
	}
	
	public void setClickProfile(AbstractClickProfile<?> profile) {
		clickProfile = profile;
	}
	
	public float [] sensitivities() {
		return sens;
	}
	
	@Override
	public void handle(GenericEvent event) {
		//overkill but feels safer ;)
		if(event == null || !scene.isAgentRegistered(this))	return;		
		if(event instanceof Duoble<?>) {
			if(event instanceof GenericClickEvent)
				scene.enqueueEventTuple(new EventGrabberDuobleTuple(event, clickProfile().handle((Duoble<?>)event), grabber()));
			else
				if(event instanceof GenericMotionEvent) {
					((GenericMotionEvent)event).modulate(sens);
					if (trackedGrabber() != null )
						scene.enqueueEventTuple(new EventGrabberDuobleTuple(event, motionProfile().handle((Duoble<?>)event), trackedGrabber()));			
			}
		}
	}	
}