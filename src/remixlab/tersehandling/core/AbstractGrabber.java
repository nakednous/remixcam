package remixlab.tersehandling.core;

public abstract class AbstractGrabber implements Grabbable {
	@Override
	public boolean grabsAgent(AbstractAgent agent) {
		return agent.trackedGrabber() == this;
	}
}
