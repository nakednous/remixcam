package remixlab.tersehandling.duoable.event;

import remixlab.tersehandling.duoable.profile.Actionable;
import remixlab.tersehandling.duoable.profile.Duoble;
import remixlab.tersehandling.event.THDOF1Event;

public class GenericDOF1Event<A extends Actionable<?>> extends THDOF1Event implements Duoble<A> {
	Actionable<?> action;
	
	public GenericDOF1Event(float x, int modifiers, int button) {
		super(x,modifiers, button);
	}
	
	public GenericDOF1Event(GenericDOF1Event<A> prevEvent, float x, int modifiers, int button) {
		super(prevEvent, x, modifiers, button);
	}
	
	public GenericDOF1Event(float x) {
		super(x);
	}
	
	public GenericDOF1Event(GenericDOF1Event<A> prevEvent, float x) {
		super(prevEvent, x);
	}
	
	public GenericDOF1Event(float x, int modifiers, int button, Actionable<?> a) {
		super(x,modifiers, button);
		action = a;
	}
	
	public GenericDOF1Event(GenericDOF1Event<A> prevEvent, float x, int modifiers, int button, Actionable<?> a) {
		super(prevEvent, x, modifiers, button);
		action = a;
	}
	
	public GenericDOF1Event(float x, Actionable<?> a) {
		super(x);
		action = a;
	}
	
	public GenericDOF1Event(GenericDOF1Event<A> prevEvent, float x, Actionable<?> a) {
		super(prevEvent, x);
		action = a;
	}
	
	protected GenericDOF1Event(GenericDOF1Event<A> other) {
		super(other);
		action = other.action;
	}

	@Override
	public Actionable<?> getAction() {
		return action;
	}
	
	@Override
	public void setAction(Actionable<?> a) {
		if( a instanceof Actionable<?> ) action = a;
	}
	
	@Override
	public GenericDOF1Event<A> get() {
		return new GenericDOF1Event<A>(this);
	}
}
